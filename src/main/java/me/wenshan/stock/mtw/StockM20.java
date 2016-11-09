/*
本指数根据广泛传播于互联网的「二八轮动」趋势跟随策略编制，成分标的为沪深300指数、中证500指数和国债指数。本指数对比当前交易日收盘数据与二十 个交易日前的收盘数据，
选择沪深300指数和中证500指数中涨幅较大的一个，于下一个交易日收盘时切换为持有该指数；若两个指数均为下跌，则于下个交易 日收盘时切换为持有国债指数。
该指数起始时间2006月1月1日，起始点100点。 历史回测10年切换240次；10年中，3年涨幅大于100%，2年涨幅大于50%，4年涨幅小于3%，1年跌幅接近20%；
年最大涨幅出现在2007 年，168%；年最大跌幅出现在2008年，-18%；2015年涨幅为61%；峰值出现在2015年11月，41倍；单次切换最大涨幅75%；单次切换 最大跌幅-10.5%。 
（以上数据由于计算方法、取值点有所差异，仅供参考，如有疑问，以指数K线为准）
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

import me.wenshan.stock.domain.StockM20Data;
import me.wenshan.stock.service.StockIndexFetcher;
import me.wenshan.stock.service.StockServiceImp;

@Component
public class StockM20 extends Stock28 {
    private static final Logger logger = Logger.getLogger(StockM20.class);
    
    private StringBuilder strbuilder = new StringBuilder();
    private StockM20Data m20Data = null;
    protected Calendar cal = null;
    protected Date todaydate = null;

    public StockM20 ()
    {
        stock2Name = "sh000300";
        stock8Name = "sh000905";
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
        
        if (null == StockServiceImp.getInstance().getStockM20Data(strriqi))
        	bRet = false;
        
    	return bRet;
    }
    
    @Scheduled(cron = "0 0/10 15-23 * * ?")
    public void weeklyIndex() {
    	if (!StockIndexFetcher.isTodayDataExist() || isTodayDataExist ())
    		return;
    	
        logger.info("Begin generate the 300-500 data.");

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

        List<StockM20Data> lstM20 = StockServiceImp.getInstance().getStockM20DataRecord(strriqi, 1);
        StockM20Data m20data = lstM20.get(0);
        dbIndex = m20data.getCloseprice();
        currentstockName = m20data.getNextStockName();
        nextstockName = m20data.getNnextStockName();

        do {
            m = cal.get(Calendar.MONTH) + 1;
            d = cal.get(Calendar.DAY_OF_MONTH);
            y = cal.get(Calendar.YEAR);
            strriqi = String.format("%d-%02d-%02d", y, m, d);

            if (getCurrentIndex(strriqi)) {
                m20Data = new StockM20Data(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                StockServiceImp.getInstance().saveM20Data(m20Data);

                String tmp = String.format("<p>日期=%s 当前股票代码=%s 下一日股票代码=%s, 下下日股票代码=%s, 指数值=%f </p>", strriqi,
                        currentstockName, nextstockName, nnextstockName, dbIndex);
                strbuilder.append(tmp);

                currentstockName = nextstockName;
                nextstockName = nnextstockName;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (cal.getTime().getTime() <= todaydate.getTime());
        
        logger.info("End of generate the 300-500 data.");
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
                m20Data = new StockM20Data(cal.getTime(), dbIndex, currentstockName, nextstockName, nnextstockName);
                StockServiceImp.getInstance().saveM20Data(m20Data);

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
