/*
本指数根据广泛传播于互联网的「二八轮动」趋势跟随策略编制，成分标的为上证50指数、创业板指数。
 */

package me.wenshan.stock.mtw;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import me.wenshan.constants.StockConstants;
import me.wenshan.stock.domain.Stock50GEMData;
import me.wenshan.stock.service.Stock50GEMDataServiceImp;
import me.wenshan.stock.service.StockIndexFetcher;

public class Stock50GEM extends Stock28 {
    private static final Logger logger = Logger.getLogger(Stock50GEM.class);

    private Calendar cal = null;
    private Date todaydate = null;
    private StringBuilder strbuilder = new StringBuilder();
    private Stock50GEMData m20Data = null;

    public Stock50GEM() {
        stock2Name = "sh000016";  // 上证50
        stock8Name = "sz399006";
    }

    public String getRestul() {
        return strbuilder.toString();
    }
    
    public boolean isTodayDataExist (){
    	boolean  bRet     = true;
    	Calendar cal      = Calendar.getInstance();
        int       m       = cal.get(Calendar.MONTH) + 1;
        int       d       = cal.get(Calendar.DAY_OF_MONTH);
        int       y       = cal.get(Calendar.YEAR);
        String    strriqi = String.format("%d-%02d-%02d", y, m, d);
        
        if (null == Stock50GEMDataServiceImp.getInstance().getData(strriqi))
        	bRet = false;
        
    	return bRet;
    }
    
    public void weeklyIndex() {
    	if (!StockIndexFetcher.isTodayDataExist() || isTodayDataExist ())
    		return;
    	
        logger.info("Begin generate the 50-GEM data.");

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

        List<Stock50GEMData> lstM20 = Stock50GEMDataServiceImp.getInstance().getDataRecord(strriqi, 1);
        Stock50GEMData tmp = lstM20.get(0);
        dbIndex = tmp.getCloseprice();
        currentstockName = tmp.getNextStockName();
        nextstockName = tmp.getNnextStockName();

        do {
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DAY_OF_MONTH);
            y = cal.get(Calendar.YEAR);
            strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                m20Data = new Stock50GEMData(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                Stock50GEMDataServiceImp.getInstance().saveData(m20Data);
                StockModelTongJiMgr.get().saveModelData (StockConstants.MODEL_50_CHUANGYEBAN, 
                        cal.getTime(), dbIndex, currentstockName);
                
                String tmp2 = String.format("<p>日期=%s 当前股票代码=%s 下一日股票代码=%s, 下下日股票代码=%s, 指数值=%f </p>", strriqi,
                        currentstockName, nextstockName, nnextstockName, dbIndex);
                strbuilder.append(tmp2);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() <= todaydate.getTime());

        logger.info("End of generate the 50-GEM data.");
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
                m20Data = new Stock50GEMData(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                Stock50GEMDataServiceImp.getInstance().saveData(m20Data);
                StockModelTongJiMgr.get().saveModelData (StockConstants.MODEL_50_CHUANGYEBAN, 
                        cal.getTime(), dbIndex, currentstockName);
                
                String tmp = String.format("<p>日期=%s 当前股票代码=%s 下一日股票代码=%s, 下下日股票代码=%s, 指数值=%f </p>\n", strriqi,
                        currentstockName, nextstockName, nnextstockName, dbIndex);

                strbuilder.append(tmp);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() < todaydate.getTime());
    }

}
