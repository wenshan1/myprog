package me.wenshan.stock.service;

import me.wenshan.stock.gemex.StockGEMEx;
import me.wenshan.stock.mtw.Stock50GEM;
import me.wenshan.stock.mtw.StockM20;
import me.wenshan.stock.mtw.StockMGEM;
import me.wenshan.stock.service.StockIndexFetcher;

public class StockInitThread implements Runnable  {

	@Override
	public void run() {
		
		//初始化指数和m20
        StockIndexFetcher.getInitData();
        StockM20 m20 = new StockM20();
        m20.initMyStock();
        StockMGEM tmp = new StockMGEM();
        tmp.initMyStock();
        Stock50GEM nn = new Stock50GEM();
        nn.initMyStock();
        StockGEMEx tmp3 = new StockGEMEx();
        tmp3.initMyStock();
	}
}
