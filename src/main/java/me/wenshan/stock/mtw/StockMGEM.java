/*
本指数根据广泛传播于互联网的「二八轮动」趋势跟随策略编制，成分标的为沪深300指数、创业板指数。
 */

package me.wenshan.stock.mtw;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.stock.domain.StockGEMData;
import me.wenshan.stock.service.StockIndexFetcher;
import me.wenshan.stock.service.StockServiceImp;

@Component
public class StockMGEM extends Stock28 {
    private static final Logger logger = Logger.getLogger(StockMGEM.class);

    private Calendar cal = null;
    private Date todaydate = null;
    private StringBuilder strbuilder = new StringBuilder();
    private StockGEMData m20Data = null;

    public StockMGEM() {
        stock2Name = "sh000300";
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
        
        if (null == StockServiceImp.getInstance().getStockGEMData(strriqi))
        	bRet = false;
        
    	return bRet;
    }
    
    @Scheduled(cron = "0 0/10 15-23 * * ?")
    public void weeklyIndex() {
    	if (!StockIndexFetcher.isTodayDataExist() || isTodayDataExist ())
    		return;
    	
        logger.info("Begin generate the 300-GEM data.");

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

        List<StockGEMData> lstM20 = StockServiceImp.getInstance().getStockGEMDataRecord(strriqi, 1);
        StockGEMData tmp = lstM20.get(0);
        dbIndex = tmp.getCloseprice();
        currentstockName = tmp.getNextStockName();
        nextstockName = tmp.getNnextStockName();

        do {
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DAY_OF_MONTH);
            y = cal.get(Calendar.YEAR);
            strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                m20Data = new StockGEMData(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                StockServiceImp.getInstance().saveGEMData(m20Data);

                String tmp2 = String.format("<p>日期=%s 当前股票代码=%s 下一日股票代码=%s, 下下日股票代码=%s, 指数值=%f </p>", strriqi,
                        currentstockName, nextstockName, nnextstockName, dbIndex);
                strbuilder.append(tmp2);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() <= todaydate.getTime());

        logger.info("End of generate the 300-GEM data.");
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
                m20Data = new StockGEMData(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                StockServiceImp.getInstance().saveGEMData(m20Data);

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
