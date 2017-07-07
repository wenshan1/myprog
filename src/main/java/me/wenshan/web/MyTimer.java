package me.wenshan.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.beijing.service.FetchData;
import me.wenshan.beijing.service.KongQiZhiLiangService;
import me.wenshan.biz.OptionManager;
import me.wenshan.newsmth.service.NewsmthService;
import me.wenshan.stockmodel.service.StockModelManager;

@Component
public class MyTimer {
    @Autowired
    private StockModelManager smmManager;
    @Autowired
    private OptionManager opm;
    @Autowired 
    private FetchData fetchData;
    @Autowired 
    private KongQiZhiLiangService kongQiZhiLiangService;
    
    @Autowired 
    private NewsmthService newsmthService;
    
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateHourly_sc () {
        UpdateHourlyThread.updateMinute (smmManager, opm, fetchData, 
        		kongQiZhiLiangService, newsmthService, null);
    }

}
