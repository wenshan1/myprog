package me.wenshan.stockmodel.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.mtw.StockModelTongJiMgr;
import me.wenshan.stock.service.StockServiceImp;
import me.wenshan.stockmodel.domain.StockModelData;

// 股票1 和 股票2 轮动
class Stock1or2 {
    private static final Logger logger = Logger.getLogger(Stock1or2.class);
    
    private StockModelDataService stockMDService;
    private double dbIndex = 1000; /* 2011-1-1 */
    private String stock2Name;
    private String stock8Name;
    private int cycle;
    private String nextstockName = "";
    private String nnextstockName = "";
    private String currentstockName = "";
    private String modelName ;
    
    public Stock1or2 (String modelName, String s1, String s2, int c1, StockModelDataService srv) {
        stock2Name = s1;
        stock8Name = s2;
        cycle = c1;
        stockMDService = srv;
        this.modelName = modelName;
    }
    
    private double getPreData(String riqi, String stockname) {

        List<StockIndex> lst = StockServiceImp.getInstance().getDataRecord(stockname, riqi, cycle);
        /*
         * for (int i = 0; i < 20; i++) { StockIndex ind = lst.get(i); m20 +=
         * ind.getCloseprice(); }
         * 
         * return m20 / 20;
         */

        return lst.get(cycle - 1).getCloseprice();
    }
    
    private boolean getCurrentIndex(String strriqi) {

        // 1. 得到 2股票 和 8股票 当日数据

        StockIndex stock2 = StockServiceImp.getInstance().getStockIndex(strriqi, stock2Name);
        StockIndex stock8 = StockServiceImp.getInstance().getStockIndex(strriqi, stock8Name);
        if (stock2 == null || stock8 == null)
            return false;

        // 2. 得到 2 股票 和 8股票 前数据

        double stock2m20 = getPreData(strriqi, stock2Name);
        double stock8m20 = getPreData(strriqi, stock8Name);

        // 计算指数
        if (!currentstockName.isEmpty()) {
            StockIndex curr;
            StockIndex pre;
            List<StockIndex> lst;
            if (currentstockName.compareTo(stock2Name) == 0) {
                curr = stock2;
            } else {
                curr = stock8;
            }
            lst = StockServiceImp.getInstance().getDataRecord(currentstockName, strriqi, 1);
            pre = lst.get(0);
            dbIndex = dbIndex * (1 + (curr.getCloseprice() - pre.getCloseprice()) / pre.getCloseprice());
        }

        // 计算下一个指数

        double zhanghu2;
        double zhanghu8;
        zhanghu2 = (stock2.getCloseprice() - stock2m20) / stock2m20;
        zhanghu8 = (stock8.getCloseprice() - stock8m20) / stock8m20;

        if (zhanghu2 < 0 && zhanghu8 < 0) {
            nnextstockName = "";
        } else {
            if (zhanghu2 > zhanghu8)
                nnextstockName = stock2Name;
            else
                nnextstockName = stock8Name;
        }
        return true;
    }
    
    public void weeklyIndex() {
        
        // 开始
        
        Calendar cal = Calendar.getInstance();
        Date todaydate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -7);

        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int y = cal.get(Calendar.YEAR);

        String strriqi = String.format("%d-%02d-%02d", y, m, d);

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date riqi;
        try {
            riqi = formater.parse(strriqi);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return;
        }
        cal.setTime(riqi);

        // 得到上一个收盘日数据

        List<StockModelData> lstM20 = stockMDService.getStockDataRecord(modelName, strriqi, 1);
        StockModelData m20data = lstM20.get(0);
        dbIndex = m20data.getCloseprice();
        currentstockName = m20data.getNextStockName();
        nextstockName = m20data.getNnextStockName();

        do {
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DAY_OF_MONTH);
            y = cal.get(Calendar.YEAR);
            strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                m20data = new StockModelData(modelName, cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                stockMDService.saveOrUpdate(m20data);
                
                StockModelTongJiMgr.get().saveModelData (modelName, 
                                                         cal.getTime(), dbIndex, currentstockName);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() <= todaydate.getTime());
        
    }
    
    public boolean init () {
        Calendar cal = Calendar.getInstance();
        Date todaydate = cal.getTime();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date riqi;
        try {
            riqi = formater.parse("2011-01-01");
        } catch (ParseException e1) {
            e1.printStackTrace();
            return false;
        }
        cal.setTime(riqi);

        do {
            int m = cal.get(Calendar.MONTH) + 1;
            int d = cal.get(Calendar.DAY_OF_MONTH);
            int y = cal.get(Calendar.YEAR);
            String strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                StockModelData m20Data = new StockModelData(modelName, cal.getTime(), dbIndex, 
                        currentstockName, nextstockName, nnextstockName);
                stockMDService.save(m20Data);
                
                StockModelTongJiMgr.get().saveModelData (modelName, 
                                                         cal.getTime(), dbIndex, currentstockName);
                
                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() < todaydate.getTime());
        
        return true;
    }
}

@Component
public class StockModelManager {
    @Autowired
    private StockModelDataService stockMDService;
    public boolean genModelData (String modelName, String stockName1, String stockName2, 
            int cycle) { 
        stockMDService.removeAllData(modelName);
        if (stockName2.isEmpty()){
            return false;
        }
        else {
            Stock1or2 st = new Stock1or2 (modelName, stockName1, stockName2, cycle, 
                    stockMDService);
            return st.init ();
        }
    }
    
    public void weekly (String modelName, String stockName1, String stockName2, 
            int cycle) {
        if (stockName2.isEmpty()){
            return ;
        }
        else {
            Stock1or2 st = new Stock1or2 (modelName, stockName1, stockName2, cycle, 
                    stockMDService);
            st.weeklyIndex();
        }
    }
}
