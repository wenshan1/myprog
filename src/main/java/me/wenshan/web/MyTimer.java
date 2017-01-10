package me.wenshan.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.biz.OptionManager;
import me.wenshan.stockmodel.service.StockModelManager;

@Component
public class MyTimer {
    @Autowired
    private StockModelManager smmManager;
    @Autowired
    private OptionManager opm;
    
    @Scheduled(cron = "0 0/2 * * * ?")
    public void updateHourly_sc () {
        UpdateHourlyThread.updateHourly (smmManager, opm, null);
    }

}
