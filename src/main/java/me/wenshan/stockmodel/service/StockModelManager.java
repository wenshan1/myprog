/*
本指数根据广泛传播于互联网的「二八轮动」趋势跟随策略编制，成分标的为沪深300指数、中证500指数和国债指数。本指数对比当前交易日收盘数据与二十 个交易日前的收盘数据，
选择沪深300指数和中证500指数中涨幅较大的一个，于下一个交易日收盘时切换为持有该指数；若两个指数均为下跌，则于下个交易 日收盘时切换为持有国债指数。
该指数起始时间2006月1月1日，起始点100点。 历史回测10年切换240次；10年中，3年涨幅大于100%，2年涨幅大于50%，4年涨幅小于3%，1年跌幅接近20%；
年最大涨幅出现在2007 年，168%；年最大跌幅出现在2008年，-18%；2015年涨幅为61%；峰值出现在2015年11月，41倍；单次切换最大涨幅75%；单次切换 最大跌幅-10.5%。 
（以上数据由于计算方法、取值点有所差异，仅供参考，如有疑问，以指数K线为准）
 */

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
import me.wenshan.stock.service.IStockService;
import me.wenshan.stock.service.StockModelTongJiService;
import me.wenshan.stockmodel.domain.StockModelData;

class Stock1 {
    private double dbIndex = 1000; /* 2011-1-1 */
    private String stockName ;
    private String nextstockName = "";
    private String nnextstockName = "";
    private String currentstockName = "";
    private int cycle;
    private String modelName ;
    private StockModelDataService stockMDService;
    private StockModelTongJiService mdlService;
    private IStockService stockService;
    
    private static final Logger logger = Logger.getLogger(Stock1.class);

    public Stock1 (String modelName, String s1, int c1, StockModelDataService srv
            , StockModelTongJiService mdlService,
            IStockService stockService) {
        stockName = s1;
        cycle = c1;
        stockMDService = srv;
        this.modelName = modelName;
        this.mdlService = mdlService;
        this.stockService = stockService;
    }
    
    private double getPreData(String riqi, String stockname) {

        List<StockIndex> lst = stockService.getDataRecord(stockname, riqi, cycle);
        
        /*
         * for (int i = 0; i < 20; i++) { StockIndex ind = lst.get(i); m20 +=
         * ind.getCloseprice(); }
         * 
         * return m20 / 20;
         */

        return lst.get(cycle - 1).getCloseprice();
    }
    
    private boolean getCurrentIndex(String strriqi) {

        // 1. 得到 股票当日数据

        StockIndex stockData = stockService.getStockIndex(strriqi, stockName);

        if (stockData == null)
            return false;

        // 2. 得到 股票 前20日数据

        double stockm20 = getPreData (strriqi, stockName);

        // 计算指数
        if (!currentstockName.isEmpty()) {
            StockIndex curr = stockData;
            StockIndex pre;
            List<StockIndex> lst;
            lst = stockService.getDataRecord(currentstockName, strriqi, 1);
            pre = lst.get(0);
            dbIndex = dbIndex * (1 + (curr.getCloseprice() - pre.getCloseprice()) / pre.getCloseprice());
        }

        // 计算下一个指数

        double zhanghu;
        zhanghu = (stockData.getCloseprice() - stockm20) / stockm20;

        if (zhanghu < 0) {
            nnextstockName = "";
        } else {
            nnextstockName = stockName;
        }
        return true;
    }
    
    public boolean init () {
        // 开始
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
                StockModelData m20Data = new StockModelData(modelName, cal.getTime(), 
                        dbIndex, currentstockName, nextstockName, nnextstockName);
                stockMDService.save(m20Data);
                StockModelTongJiMgr.get(mdlService).saveModelData (modelName, 
                        cal.getTime(), dbIndex, currentstockName);
                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() < todaydate.getTime());
        
        return true;
    }
    
    public void update() {
        
        logger.info("update: Begin generate " + modelName);

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
        StockModelData tmp = lstM20.get(0);
        
        dbIndex = tmp.getCloseprice();
        currentstockName = tmp.getNextStockName();
        nextstockName = tmp.getNnextStockName();

        do {
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DAY_OF_MONTH);
            y = cal.get(Calendar.YEAR);
            strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                StockModelData m20data = new StockModelData(modelName, cal.getTime(), dbIndex, currentstockName, 
                        nextstockName, nnextstockName);
                stockMDService.saveOrUpdate(m20data);
                
                StockModelTongJiMgr.get(mdlService).saveModelData (modelName, 
                        cal.getTime(), dbIndex, currentstockName);
                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() <= todaydate.getTime());

        logger.info("update: End generate " + modelName);
    }
}

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
    private StockModelTongJiService mdlService;
    private IStockService stockService;
    
    public Stock1or2 (String modelName, String s1, String s2, int c1, StockModelDataService srv,
            StockModelTongJiService mdlService) {
        stock2Name = s1;
        stock8Name = s2;
        cycle = c1;
        stockMDService = srv;
        this.modelName = modelName;
        this.mdlService = mdlService;
    }
    
    private double getPreData(String riqi, String stockname) {

        List<StockIndex> lst = stockService.getDataRecord(stockname, riqi, cycle);
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

        StockIndex stock2 = stockService.getStockIndex(strriqi, stock2Name);
        StockIndex stock8 = stockService.getStockIndex(strriqi, stock8Name);
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
            lst = stockService.getDataRecord(currentstockName, strriqi, 1);
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
    
    public void update() {
        
        logger.info("update: Begin generate " + modelName);
        
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
                
                StockModelTongJiMgr.get(mdlService).saveModelData (modelName, 
                                                         cal.getTime(), dbIndex, currentstockName);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() <= todaydate.getTime());
        
        logger.info("update: End generate " + modelName);
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
                
                StockModelTongJiMgr.get(mdlService).saveModelData (modelName, 
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
    @Autowired
    private StockModelTongJiService mdlService;
    
    @Autowired 
    private IStockService stockService;
    
    public boolean genModelData (String modelName, String stockName1, String stockName2, 
            int cycle) { 
        stockMDService.removeAllData(modelName);
        if (stockName2.isEmpty()){
            Stock1 st = new Stock1 (modelName, stockName1, cycle, 
                    stockMDService, mdlService, stockService);
            
            return st.init();
        }
        else {
            Stock1or2 st = new Stock1or2 (modelName, stockName1, stockName2, cycle, 
                    stockMDService, mdlService);
            return st.init ();
        }
    }
    
    public void weekly (String modelName, String stockName1, String stockName2, 
            int cycle) {
        if (stockName2.isEmpty()){
            Stock1 st = new Stock1 (modelName, stockName1, cycle, 
                    stockMDService, mdlService, stockService);
            st.update();
        }
        else {
            Stock1or2 st = new Stock1or2 (modelName, stockName1, stockName2, cycle, 
                    stockMDService, mdlService);
            st.update();
        }
        
        return ;
    }
}
