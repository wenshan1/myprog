package me.wenshan.stock.service;

import me.wenshan.biz.OptionManager;
import me.wenshan.constants.StockConstants;
import me.wenshan.stock.service.StockIndexFetcher;
import me.wenshan.stockmodel.service.StockModelManager;
import me.wenshan.stockmodel.service.StockModelTongJiMgr;

public class StockInitThread implements Runnable  {
	private boolean bInitIndex; //true - 初始化指数 
	private boolean bInitStock; //true - 初始化stock数据
	private boolean bInitModel; // true - 初始化模型数据 
	
	private StockModelManager smmManager; 
	private StockModelTongJiService mdlService;
	private OptionManager opm;
	
	public StockInitThread (boolean bInitIndex, boolean bInitStock, 
	        boolean bInitModel, StockModelManager smm,
	        StockModelTongJiService mdlService,
	        OptionManager opm) {
	    smmManager = smm;
		this.bInitIndex = bInitIndex;
		this.bInitStock = bInitStock;
		this.bInitModel = bInitModel;
		this.mdlService = mdlService;
		this.opm = opm;
	}
	private void initStockData () {
		StockDataFetcher.initStockList(opm);
		StockDataFetcher.initStockData(opm);
	}
	
	@Override
	public void run() {
		if (bInitIndex) {
		    StockServiceImp.getInstance().removeAll();
	        StockIndexFetcher.getInitData();
		}
		if (bInitStock) {
			initStockData ();
		}
		if (bInitModel) {
		    StockModelTongJiMgr.get(mdlService).removeAllData();
		    
		    /*
	        StockM20 m20 = new StockM20();
	        m20.initMyStock();
	        StockMGEM tmp = new StockMGEM();
	        tmp.initMyStock();
	        Stock50GEM nn = new Stock50GEM();
	        nn.initMyStock(); 
		    
	        StockGEMEx tmp3 = new StockGEMEx();
	        tmp3.initMyStock(); */
	        
	        smmManager.genModelData(StockConstants.MODEL_300_500, "sh000300", "sh000905", 20);
	        smmManager.genModelData(StockConstants.MODEL_300_CHUANGYEBAN, "sh000300", "sz399006", 20);
	        smmManager.genModelData(StockConstants.MODEL_50_CHUANGYEBAN, "sh000016", "sz399006", 20);
	        smmManager.genModelData(StockConstants.MODEL_CHUANGYEBANEXEX, "sz399006", "", 20);
		}
	}
}
