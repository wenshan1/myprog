package me.wenshan.web;

import java.io.PrintWriter;
import java.util.Calendar;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.beijing.service.FetchData;
import me.wenshan.beijing.service.KongQiZhiLiangService;
import me.wenshan.biz.OptionManager;
import me.wenshan.blog.backend.form.DataOption;
import me.wenshan.newsmth.service.NewsmthFetchData;
import me.wenshan.newsmth.service.NewsmthServiceImp;
import me.wenshan.stock.gemex.StockGEMEx;
import me.wenshan.stock.mtw.Stock50GEM;
import me.wenshan.stock.mtw.StockM20;
import me.wenshan.stock.mtw.StockMGEM;
import me.wenshan.stock.service.StockDataFetcher;
import me.wenshan.stock.service.StockIndexFetcher;
import me.wenshan.util.MyBeanFactory;

@Component
public class UpdateHourlyThread implements Runnable {
	
	public UpdateHourlyThread ()
	{
		
	}
	
	@Scheduled(cron = "0 0 0/1 * * ?")
	public static void updateHourly_sc ()
	{
		updateHourly (null);
	}
	
	public static void updateHourly (PrintWriter out)
	{
		try { 
			OptionManager opm = MyBeanFactory.getBean (OptionManager.class);
			DataOption dataop = opm.getDataOption();
            Calendar cal = Calendar.getInstance();
            int d = cal.get(Calendar.HOUR_OF_DAY);
            
            if ( d > 14) {
    			
    			StockIndexFetcher.get10DayData_Sc(); // 更新指数数据
    			// 生产最新m20等数据
    			StockM20 m20 = new StockM20();
    			m20.weeklyIndex();
    			StockMGEM tmp = new StockMGEM();
    			tmp.weeklyIndex();
    			Stock50GEM nn = new Stock50GEM();
    			nn.weeklyIndex();
    			StockGEMEx tmp3 = new StockGEMEx();
    			tmp3.weeklyIndex();
    			
    			if (dataop.getStockDataNum() > 0) {
    				
    			//得到股票的数据
    			StockDataFetcher.getTenDayData_Sc();
    			}
            }
            
			FetchData.fetchAll_Sc(); // 更新北京房地产数据和空气质量
			NewsmthFetchData.fetchAll_sc();
			
			// 删除过多newsmth记录
			NewsmthServiceImp.getInstance().deleteOld(dataop.getNewsmthNum());
			KongQiZhiLiangService.getInstance().deleteOld(dataop.getBeijingQuality());
						
		} catch (Exception e) {
			if (out != null)
				e.printStackTrace(out);
		}
	}

	@Override
	public void run() {
		UpdateHourlyThread.updateHourly(null);
	}

}
