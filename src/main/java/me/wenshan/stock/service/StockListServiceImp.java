package me.wenshan.stock.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockList;

@Service
public class StockListServiceImp implements IStockListService {

    // 返回的值是一个js代码段 包括指定url页面包含的所有股票代码
    private String getBatchStackCodes(URL url) {
        HttpURLConnection connection;
        boolean bConnectionRet = false;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
            connection.setRequestMethod("GET");
            connection.setReadTimeout(6 * 10000);
            bConnectionRet = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        StringBuffer sb = new StringBuffer();
        if (bConnectionRet) {
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                boolean flag = false;
                while ((line = br.readLine()) != null) {
                    if (line.contains("<script type=\"text/javascript\">") || flag) {
                        sb.append(line);
                        flag = true;
                    }
                    if (line.contains("</script>")) {
                        flag = false;
                        if (sb.length() > 0) {
                            if (sb.toString().contains("code_list") && sb.toString().contains("element_list")) {
                                break;
                            } else {
                                sb.setLength(0);
                            }
                        }
                    }
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return sb.toString();
            }
        }
        return sb.toString();
    }

    // 解析一组股票代码字符串 把code中包括的所有股票代码放入List中
    private List<String> handleStockCode(String code) {
        List<String> codes = null;
        int end = code.indexOf(";");
        code = code.substring(0, end);
        int start = code.lastIndexOf("=");
        code = code.substring(start);
        code = code.substring(code.indexOf("\"") + 1, code.lastIndexOf("\""));
        codes = Arrays.asList(code.split(","));
        return codes;
    }

    // 获得上海市场所有数据
    private List<String> getSHAllStackCodes() {
        List<String> codes = new ArrayList<String>();
        int i = 1;
        URL url = null;
        for (; i < 18; i++) {
            try {
                String tmp = String.format(
                        "http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=%d&market=sh", i);
                url = new URL(tmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return codes;
            }
            String code = getBatchStackCodes(url);
            codes.addAll(handleStockCode(code));
        }
        return codes;
    }

    // 获得深圳市场所有数据
    private List<String> getSZAllStackCodes() {
        List<String> codes = new ArrayList<String>();
        int i = 1;
        URL url = null;
        for (; i < 30; i++) {
            try {
                String tmp = String.format(
                        "http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=%d&market=sz", i);
                url = new URL(tmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return codes;
            }
            String code = getBatchStackCodes(url);
            codes.addAll(handleStockCode(code));
        }
        return codes;
    }

    @Override
    public List<StockList> getStockListFromWeb() {
        List<String> strings = getSHAllStackCodes();
        String str;
        ArrayList<StockList> lst = new ArrayList<StockList>();
        for (int i = 0; i < strings.size(); i++) {
            str = strings.get(i);
            if (str.contains("sh"))
                lst.add(new StockList(str));
        }

        strings = getSZAllStackCodes();
        for (int i = 0; i < strings.size(); i++) {
            str = strings.get(i);
            if (str.contains("sz"))
                lst.add(new StockList(strings.get(i)));
        }
        return lst;
    }

    @Override
    public void save(StockList data) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        try {
            sn.save(data);
            sa.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sn.close();
    }

    @Override
    public void removeAllData() {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        sn.createSQLQuery("delete from stocklist").executeUpdate();
        sa.commit();
        sn.close();
    }

    @Override
    public List<StockList> getAllDataFromDB() {
        List<StockList> lst;
        Session sn = HibernateUtil.getSessionFactory().openSession();
        lst = sn.createSQLQuery("SELECT * FROM stocklist").addEntity(StockList.class).list();
        sn.close();
        return  lst;
    }

	@Override
	public long count() {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        long cou = (long) sn.createQuery("select count(u) from StockList as u").uniqueResult();
        sn.close();
        return cou;
	}

}
