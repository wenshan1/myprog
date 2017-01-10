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

import me.wenshan.biz.OptionManager;
import me.wenshan.blog.backend.form.DataOption;
import me.wenshan.stock.domain.StockData;
import me.wenshan.stock.domain.StockList;
import me.wenshan.util.MyBeanFactory;

public class StockDataFetcher {
    
    private static final Logger logger = Logger.getLogger(StockDataFetcher.class);

    public static void initStockList() {
		OptionManager opm = MyBeanFactory.getBean (OptionManager.class);
		DataOption dataop = opm.getDataOption();
        IStockListService ss = new StockListServiceImp();
        ss.removeAllData();
        if (dataop.getStockDataNum() == 0)
        	return;
        List<StockList> lst = ss.getStockListFromWeb();
        if (lst != null) {
            for (int i = 0; i < lst.size(); i++)
                ss.save(lst.get(i));
        }
    }

    public static void initStockData() {
        IStockListService ss = new StockListServiceImp();
        IStockDataService ssdata = StockDataServiceImp.get();
        ssdata.removeAllData();
		OptionManager opm = MyBeanFactory.getBean (OptionManager.class);
		DataOption dataop = opm.getDataOption();
		if (dataop.getStockDataNum() == 0)
			return;
        List<StockList> lst = ss.getAllDataFromDB();
        for (int i = 0; i < lst.size(); i++) {
            ArrayList<StockData> lstData = getAllHistoryDataFromSina(lst.get(i).getStockName(), 
            		dataop.getStockDataNum(), 0);
            SaveAllData (lstData);
        }

    }

    private static ArrayList<StockData> getAllHistoryDataFromSina(String stockName, int nYear, int nDay) {
        final String SINA_FINANCE_URL = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?";
        String period = "";
        String urlstr = null;
        ArrayList<StockData> list = new ArrayList<StockData>();
        if (nYear > 0) {
            Calendar cal = Calendar.getInstance();
            int d = cal.get(Calendar.MONTH) + 1;
            int e = cal.get(Calendar.DAY_OF_MONTH);
            int f = cal.get(Calendar.YEAR);
            
            int a = 1; //cal.get(Calendar.MONTH) + 1;
            int b = 1; //cal.get(Calendar.DAY_OF_MONTH);
            int c = f - nYear; //cal.get(Calendar.YEAR);
            period = "&begin_date=" + c + String.format("%02d", a) + String.format("%02d", b) + "&end_date" + f
                    + String.format("%02d", d) + String.format("%02d", e);
        } else if (nDay > 0) {
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
        }
        else {
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
                List<Element> childList = (List<Element>) root.elements();

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

                    StockData index = new StockData(stockName, riqi, Double.parseDouble(open), Double.parseDouble(high),
                            Double.parseDouble(low), Double.parseDouble(close), Long.parseLong(volum), 0.0);
                    list.add(index);
                }

            }

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static void SaveAllData(ArrayList<StockData> list) {
        IStockDataService ss = StockDataServiceImp.get();
        ss.save(list);
    }

    private static void SaveAllDataUpdate(ArrayList<StockData> list) {
        IStockDataService ss = StockDataServiceImp.get();
        for (int i = 0; i < list.size(); i++) {
            ss.saveOrUpdate(list.get(i));
        }
    }
    
    public static boolean  isTodayDataExist (String stockName) {
    	boolean  bRet     = true;
    	Calendar cal      = Calendar.getInstance();
        int       m       = cal.get(Calendar.MONTH) + 1;
        int       d       = cal.get(Calendar.DAY_OF_MONTH);
        int       y       = cal.get(Calendar.YEAR);
        String    strriqi = String.format("%d-%02d-%02d", y, m, d);
        IStockDataService ss = StockDataServiceImp.get();
        
        if (ss.get(strriqi, stockName) == null){
        	bRet = false;
        	}
    	return bRet;
    }
    
    public static void getTenDayData_Sc() {
        logger.info("Begin fetch stock data");
        IStockListService ss = new StockListServiceImp();
        List<StockList> lst = ss.getAllDataFromDB();
        for (int i = 0; i < lst.size(); i++) {
        	if (isTodayDataExist (lst.get(i).getStockName()))
        		continue;
            ArrayList<StockData> lstData = getAllHistoryDataFromSina(lst.get(i).getStockName(), 0, 10);
            SaveAllDataUpdate (lstData);
        }
        logger.info("End of fetch stock data");
    }
}
