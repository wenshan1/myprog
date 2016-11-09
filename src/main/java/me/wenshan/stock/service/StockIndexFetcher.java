package me.wenshan.stock.service;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.stock.domain.StockIndex;

@Component
public class StockIndexFetcher {

    final static boolean bUsedSina = true;
    private static final Logger logger = Logger.getLogger(StockIndexFetcher.class);

    private static ArrayList<String> getSockNames() {
        ArrayList<String> list = new ArrayList<String>();
        if (bUsedSina) {
            list.add("sh000001"); // 上证指数
            list.add("sz399001"); // 深圳指数

            list.add("sh000300"); // 沪深300
            list.add("sh000905"); // 中证500
            list.add("sz399006"); // 创业板
            list.add("sz399005"); // 中小板指数
            list.add("sh000010"); // 上证180
            list.add("sh000016"); // 上证50

        } else {
            list.add("000001.SS");
            list.add("000300.SS");
            list.add("399001.SZ");
        }
        return list;
    }

    private static StockIndex createStockForYahoo(String daima, String line) {
        String format = "yyyy-MM-dd";
        String[] stockinfo = line.split(",");
        if (stockinfo.length != 7) {
            return null;
        }
        if (stockinfo[0].indexOf('-') > 0) {
            if (stockinfo[0].length() == 10) {
                format = "yyyy-MM-dd";
            } else {
                format = "yyyy-M-d";
            }
        } else if (stockinfo[0].indexOf('.') > 0) {
            if (stockinfo[0].length() == 10) {
                format = "yyyy.MM.dd";
            } else {
                format = "yyyy.M.d";
            }
        } else if (stockinfo[0].indexOf('/') > 0) {
            if (stockinfo[0].length() == 10) {
                if (stockinfo[0].indexOf('/') > 3)
                    format = "yyyy/MM/dd";// 年月日
                else
                    format = "MM/dd/yyyy";// 月日年
            } else {
                if (stockinfo[0].indexOf('/') > 3)
                    format = "yyyy/M/d";// 年月日
                else
                    format = "M/d/yyyy";// 月日年
            }

        }

        SimpleDateFormat formater = new SimpleDateFormat(format);
        Date riqi;
        try {
            riqi = formater.parse(stockinfo[0]);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return new StockIndex(daima, riqi, Double.parseDouble(stockinfo[1]), Double.parseDouble(stockinfo[2]),
                Double.parseDouble(stockinfo[3]), Double.parseDouble(stockinfo[4]), Long.parseLong(stockinfo[5]),
                Double.parseDouble(stockinfo[6]));
    }

    private static ArrayList<StockIndex> getAllHistoryDataFromSina(String stockName, int nDay) {
        final String SINA_FINANCE_URL = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?";
        String period = "";
        String urlstr = null;
        ArrayList<StockIndex> list = new ArrayList<StockIndex>();
        if (nDay > 0) {
            Calendar cal = Calendar.getInstance();
            int d = cal.get(Calendar.MONTH) + 1;
            int e = cal.get(Calendar.DAY_OF_MONTH);
            int f = cal.get(Calendar.YEAR);
            cal.add(Calendar.DAY_OF_MONTH, 0 - nDay);
            int a = cal.get(Calendar.MONTH) + 1;
            int b = cal.get(Calendar.DAY_OF_MONTH);
            int c = cal.get(Calendar.YEAR);
            period = "&begin_date=" + c + String.format("%02d", a) + String.format("%02d", b) + "&end_date" + f
                    + String.format("%02d", d) + String.format("%02d", e);
        } else {
            Calendar cal = Calendar.getInstance();
            int d = cal.get(Calendar.MONTH) + 1;
            int e = cal.get(Calendar.DAY_OF_MONTH);
            int f = cal.get(Calendar.YEAR);
            period = "&begin_date=20060101" + "&end_date" + f + String.format("%02d", d) + String.format("%02d", e);
        }

        urlstr = SINA_FINANCE_URL + "symbol=" + stockName + period;

        CookieManager ckm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        // First set the default cookie manager.
        CookieHandler.setDefault(ckm);

        // All the following subsequent URLConnections will use the
        // same cookie manager.

        InputStream in = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
            connection.setRequestMethod("GET");
            connection.setReadTimeout(6 * 10000);

            if (connection.getResponseCode() == 200) {
                in = connection.getInputStream();

                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(in);
                // 获取根元素
                Element root = document.getRootElement();
                // 获取所有子元素
                List<Element> childList = root.elements();

                for (int i = 0; i < childList.size(); i++) {
                    Element e = childList.get(i);
                    String date = e.attribute("d").getText();
                    String open = e.attribute("o").getText();
                    String high = e.attribute("h").getText();
                    String close = e.attribute("c").getText();
                    String low = e.attribute("l").getText();
                    String volum = e.attribute("v").getText();
                    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    Date riqi;
                    try {
                        riqi = formater.parse(date);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                        return null;
                    }

                    StockIndex index = new StockIndex(stockName, riqi, Double.parseDouble(open),
                            Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(close),
                            Long.parseLong(volum), 0.0);
                    list.add(index);
                }

            }

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            list.clear();
        }

        return list;
    }

    private static ArrayList<StockIndex> getAllHistoryDataFromYahoo(String stockName, int nDay) {

        final String YAHOO_FINANCE_URL = "http://table.finance.yahoo.com/table.csv?";
        ArrayList<StockIndex> list = new ArrayList<StockIndex>();
        String period = "";
        String url = null;
        if (nDay > 0) {
            Calendar cal = Calendar.getInstance();
            int d = cal.get(Calendar.MONTH);
            int e = cal.get(Calendar.DAY_OF_MONTH);
            int f = cal.get(Calendar.YEAR);
            cal.add(Calendar.DAY_OF_MONTH, 0 - nDay);
            int a = cal.get(Calendar.MONTH);
            int b = cal.get(Calendar.DAY_OF_MONTH);
            int c = cal.get(Calendar.YEAR);
            period = "&a=" + a + "&b=" + b + "&c=" + c + "&d=" + d + "&e=" + e + "&f=" + f;

        }
        url = YAHOO_FINANCE_URL + "s=" + stockName + period;

        try {
            URL myurl = new URL(url);
            URLConnection con = myurl.openConnection();
            InputStreamReader ins = new InputStreamReader(con.getInputStream(), "UTF-8");
            BufferedReader in = new BufferedReader(ins);
            // 标题行
            String newLine = in.readLine();

            while ((newLine = in.readLine()) != null) {
                StockIndex st = createStockForYahoo(stockName, newLine.trim());
                if (st != null) {
                    list.add(st);
                }
            }

            if (ins != null) {
                ins.close();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static void SaveAllData(ArrayList<StockIndex> list, StringBuilder builder) {
        for (int i = 0; i < list.size(); i++) {
            StockServiceImp.getInstance().save(list.get(i));
            if (i == 0) {
                builder.append(list.get(i).toString());
                builder.append("\n");
            }
        }
    }
    
    public static boolean  isTodayDataExist ()
    {
    	boolean  bRet     = true;
    	Calendar cal      = Calendar.getInstance();
        int       m       = cal.get(Calendar.MONTH) + 1;
        int       d       = cal.get(Calendar.DAY_OF_MONTH);
        int       y       = cal.get(Calendar.YEAR);
        String    strriqi = String.format("%d-%02d-%02d", y, m, d);
        
        ArrayList<String> names = StockIndexFetcher.getSockNames();
        for (int i = 0; i < names.size(); i++) {
        	if (StockServiceImp.getInstance().getStockIndex(strriqi, names.get(i)) == null){
        		bRet = false;
        		break;
        	}
        }
        
    	return bRet;
    }

    @Scheduled(cron = "0 0/10 15-23 * * ?")
    public static void get10DayData_Sc() {
    	if (isTodayDataExist ())
    		return;
    	
        boolean bRet;
        logger.info("Begin fetch index stock data");
        bRet = get10DayData ();
        while (!bRet)
        {
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bRet = get10DayData ();
        }
        logger.info("End of fetch index stock data");
    }

    public static boolean get10DayData() {
        boolean bRet = true;
        ArrayList<StockIndex> lst;
        StringBuilder builder = new StringBuilder();
        ArrayList<String> names = StockIndexFetcher.getSockNames();
        for (int i = 0; i < names.size(); i++) {
            if (bUsedSina)
            {
                lst = StockIndexFetcher.getAllHistoryDataFromSina(names.get(i), 10);
                if (lst.isEmpty())
                    bRet = false;
                SaveAllData(lst, builder);
            }
            else
                SaveAllData(StockIndexFetcher.getAllHistoryDataFromYahoo(names.get(i), 5), builder);
        }

        return bRet;
    }

    public static String getInitData() {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> names = StockIndexFetcher.getSockNames();
        for (int i = 0; i < names.size(); i++) {
            if (bUsedSina)
                SaveAllData(getAllHistoryDataFromSina(names.get(i), -1), builder);
            else
                SaveAllData(getAllHistoryDataFromYahoo(names.get(i), -1), builder);
        }

        return builder.toString();
    }

}
