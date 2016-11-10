package me.wenshan.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import me.wenshan.beijing.service.FetchData;
import me.wenshan.stock.gemex.StockGEMEx;
import me.wenshan.stock.mtw.Stock50GEM;
import me.wenshan.stock.mtw.StockM20;
import me.wenshan.stock.mtw.StockMGEM;
import me.wenshan.stock.service.StockIndexFetcher;

@Controller
public class RootController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	
	public String root () 
    {
	return "index";
    }
	
	@RequestMapping(value = "/updatehourly", method = RequestMethod.GET)
	public String updatehourly()
	    {
		FetchData.fetchAll_Sc();             //更新北京房地产数据和空气质量		
		StockIndexFetcher.get10DayData_Sc(); //更新指数数据
		
		//生产最新m20等数据
		
        StockM20 m20 = new StockM20();
        m20.weeklyIndex();
        StockMGEM tmp = new StockMGEM();
        tmp.weeklyIndex();
        Stock50GEM nn = new Stock50GEM();
        nn.weeklyIndex();
        StockGEMEx tmp3 = new StockGEMEx();
        tmp3.weeklyIndex();
		
        // 更新空气质量数据
        
        FetchData.fetchAll_Sc();
        
		return root ();
		}

}
