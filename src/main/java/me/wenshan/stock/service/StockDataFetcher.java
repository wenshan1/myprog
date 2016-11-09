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

import me.wenshan.stock.domain.StockData;
import me.wenshan.stock.domain.StockList;

@Component
public class StockDataFetcher {
    
    private static final Logger logger = Logger.getLogger(StockDataFetcher.class);

    public static void initStockList() {
        IStockListService ss = new StockListServiceImp();
        List<StockList> lst = ss.getStockListFromWeb();
        if (lst != null) {
            ss.removeAllData();
            for (int i = 0; i < lst.size(); i++)
                ss.save(lst.get(i));
        }
    }

    public static void initStockData() {
        IStockListService ss = new StockListServiceImp();
        IStockDataService ssdata = new StockDataServiceImp();
        ssdata.removeAllData();
        List<StockList> lst = ss.getAllDataFromDB();
        for (int i = 0; i < lst.size(); i++) {
            ArrayList<StockData> lstData = getAllHistoryDataFromSina(lst.get(i).getStockName(), 360*5);
            SaveAllData (lstData);
        }

    }

    private static ArrayList<StockData> getAllHistoryDataFromSina(String stockName, int nDay) {
        final String SINA_FINANCE_URL = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?";
        String period = "";
        String urlstr = null;
        ArrayList<StockData> list = new ArrayList<StockData>();
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
        IStockDataService ss = new StockDataServiceImp();
        for (int i = 0; i < list.size(); i++) {
            ss.save(list.get(i));
        }
    }

    private static void SaveAllDataUpdate(ArrayList<StockData> list) {
        IStockDataService ss = new StockDataServiceImp();
        for (int i = 0; i < list.size(); i++) {
            ss.saveOrUpdate(list.get(i));
        }
    }
    
    public static void getTenDayData_Sc() {
        logger.info("Begin fetch stock data");
        IStockListService ss = new StockListServiceImp();
        List<StockList> lst = ss.getAllDataFromDB();
        for (int i = 0; i < lst.size(); i++) {
            ArrayList<StockData> lstData = getAllHistoryDataFromSina(lst.get(i).getStockName(), 10);
            SaveAllDataUpdate (lstData);
        }
        logger.info("End of fetch stock data");
    }
}
