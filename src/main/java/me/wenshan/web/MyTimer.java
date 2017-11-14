package me.wenshan.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.beijing.service.FetchData;
import me.wenshan.biz.OptionManager;
import me.wenshan.stock.service.IStockDataService;
import me.wenshan.stock.service.IStockService;
import me.wenshan.stock.service.StockInitThread;
import me.wenshan.stock.service.StockModelTongJiService;
import me.wenshan.stockmodel.service.StockModelManager;
import me.wenshan.util.EmailSend;

@Component
public class MyTimer {
	@Autowired
	private EmailSend emailSend;
    @Autowired
    private StockModelManager smmManager;
    @Autowired
    private OptionManager opm;
    @Autowired 
    private FetchData fetchData;
    @Autowired
    private StockModelTongJiService mdlService;
    @Autowired
    private IStockDataService stockDataService;
    @Autowired
    private IStockService stockService;
    @Scheduled(cron = "0 0 */8 * * ?")
    public void email_send () {
    	emailSend.send();
    }
    
    @Scheduled(cron = "0 0 */1 * * ?")
    public void updateHourly_sc () {
        Thread th = new Thread(new StockInitThread(false, false, true, smmManager, mdlService, 
        		opm, stockDataService, stockService));
        th.start();
    }
    
    @Scheduled(cron = "0 0 0 */1 * ?")
    public void updateFanDican () {
        fetchData.fetchAll_FandDiCan(); // 更新北京房地产数据
    }

}
