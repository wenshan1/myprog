package me.wenshan.stock.gemex;

/*
 * 本指数成分标的为创业板指数和国债指数。本指数对比创业板指数当前交易日收盘数据与二十个交易日前的收盘数据，
 * 若为上涨则于下个交易日收盘时切换为持有创业板指数；若为下跌，则于下个交易日收盘时切换为持有国债指数。
 * 该指数起始时间2010月7月5日，起始点1000点。 历史回测接近6年时间，切换102次；其中，1年涨幅大于100%，
 * 1年涨幅大于50%，2年涨幅大于10%，1年涨幅小于5%，1年跌幅小于5%，截止本指数上线（2016年4月），
 * 2016年跌幅接近-20%；年最大涨幅出现在2015年，177%；峰值出现在2015年11月，6倍多；单次切换最大涨幅92%；
 * 单次切换最大跌幅-11.7%。 （以上数据由于计算方法、取值点有所差异，仅供参考，如有疑问，以指数K线为准）
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.stock.domain.StockGEMEXData;
import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.service.StockGEMEXDataServiceImp;
import me.wenshan.stock.service.StockIndexFetcher;
import me.wenshan.stock.service.StockServiceImp;

@Component
public class StockGEMEx {
    private double dbIndex = 1000; /* 2011-1-1 */
    private final String stockName = "sz399006";
    private String nextstockName = "";
    private String nnextstockName = "";
    private String currentstockName = "";

    private static final Logger logger = Logger.getLogger(StockGEMEx.class);

    private Calendar cal = null;
    private Date todaydate = null;
    private StockGEMEXData m20Data = null;

    private double getM20(String riqi, String stockname) {

        List<StockIndex> lst = StockServiceImp.getInstance().getDataRecord(stockname, riqi, 20);
        /*
         * for (int i = 0; i < 20; i++) { StockIndex ind = lst.get(i); m20 +=
         * ind.getCloseprice(); }
         * 
         * return m20 / 20;
         */

        return lst.get(19).getCloseprice();
    }

    private boolean getCurrentIndex(String strriqi) {

        // 1. 得到 股票当日数据

        StockIndex stockData = StockServiceImp.getInstance().getStockIndex(strriqi, stockName);

        if (stockData == null)
            return false;

        // 2. 得到 股票 前20日数据

        double stockm20 = getM20(strriqi, stockName);

        // 计算指数
        if (!currentstockName.isEmpty()) {
            StockIndex curr = stockData;
            StockIndex pre;
            List<StockIndex> lst;
            lst = StockServiceImp.getInstance().getDataRecord(currentstockName, strriqi, 1);
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

    public void initMyStock() {
        // 开始
        cal = Calendar.getInstance();
        todaydate = cal.getTime();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date riqi;
        try {
            riqi = formater.parse("2011-01-01");
        } catch (ParseException e1) {
            e1.printStackTrace();
            return;
        }
        cal.setTime(riqi);

        do {
            int m = cal.get(Calendar.MONTH) + 1;
            int d = cal.get(Calendar.DAY_OF_MONTH);
            int y = cal.get(Calendar.YEAR);
            String strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                m20Data = new StockGEMEXData(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                StockGEMEXDataServiceImp.getInstance().saveOrUpdate(m20Data);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() < todaydate.getTime());
    }

    public boolean isTodayDataExist (){
    	boolean  bRet     = true;
    	Calendar cal      = Calendar.getInstance();
        int       m       = cal.get(Calendar.MONTH) + 1;
        int       d       = cal.get(Calendar.DAY_OF_MONTH);
        int       y       = cal.get(Calendar.YEAR);
        String    strriqi = String.format("%d-%02d-%02d", y, m, d);
        
        if (null == StockGEMEXDataServiceImp.getInstance().getData(strriqi))
        	bRet = false;
        
    	return bRet;
    }
    
    @Scheduled(cron = "0 0/10 15-23 * * ?")
    public void weeklyIndex() {
    	if (!StockIndexFetcher.isTodayDataExist() || isTodayDataExist ())
    		return;
    	
        logger.info("Begin generate the GEM EX data.");

        // 开始
        cal = Calendar.getInstance();
        todaydate = cal.getTime();
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

        List<StockGEMEXData> lstM20 = StockGEMEXDataServiceImp.getInstance().getDataRecord(strriqi, 1);
        StockGEMEXData tmp = lstM20.get(0);
        dbIndex = tmp.getCloseprice();
        currentstockName = tmp.getNextStockName();
        nextstockName = tmp.getNnextStockName();

        do {
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DAY_OF_MONTH);
            y = cal.get(Calendar.YEAR);
            strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                m20Data = new StockGEMEXData(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                StockGEMEXDataServiceImp.getInstance().saveOrUpdate(m20Data);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() <= todaydate.getTime());

        logger.info("End of generate the GEM EX data.");
    }

}
