package me.wenshan.stock.service;

import me.wenshan.backend.form.DataOption;
import me.wenshan.biz.OptionManager;
import me.wenshan.constants.StockConstants;
import me.wenshan.stockmodel.service.StockModelManager;
import me.wenshan.stockmodel.service.StockModelTongJiMgr;

public class StockInitThread implements Runnable  {
	private boolean bInitIndex; //true - 初始化指数 
	private boolean bInitStock; //true - 初始化stock数据
	private boolean bInitModel; // true - 初始化模型数据 
	
	private StockModelManager smmManager; 
	private StockFetchData    stockFetchData;
	private StockModelTongJiService mdlService;
	private IStockDataService stockDataService;
	private OptionManager  optionManager;
	private static boolean bRunning = false;
	
	public StockInitThread (boolean bInitIndex, boolean bInitStock, 
	        boolean bInitModel, StockModelManager smm,
	        StockModelTongJiService mdlService,
	        OptionManager opm, 
	        IStockDataService stockDataService, 
	        StockFetchData    stockFetchData) {
	    smmManager = smm;
		this.bInitIndex = bInitIndex;
		this.bInitStock = bInitStock;
		this.bInitModel = bInitModel;
		this.mdlService = mdlService;
		this.stockDataService = stockDataService;
		optionManager = opm;
		this.stockFetchData = stockFetchData;
	}
	
	public static void initModel 
	    (
	    StockModelTongJiService mdlService,
        OptionManager  optionManager, 
	    StockModelManager smmManager) 
	    {
	    StockModelTongJiMgr.get(mdlService).removeAllData();
        DataOption datao = optionManager.getDataOption ();
        
        smmManager.genModelData(StockConstants.MODEL_300_500, "sh000300", "sh000905", 
        		datao.getStockModelCycle());
        smmManager.genModelData(StockConstants.MODEL_300_CHUANGYEBAN, "sh000300", "sz399006", 
        		datao.getStockModelCycle());
        smmManager.genModelData(StockConstants.MODEL_50_CHUANGYEBAN, "sh000016", "sz399006", 
        		datao.getStockModelCycle());
        smmManager.genModelData(StockConstants.MODEL_CHUANGYEBANEXEX, "sz399006", "", 
        		datao.getStockModelCycle());
        smmManager.genModelData(StockConstants.MODEL_CUSTOME, datao.getStockModelName1(), 
        		datao.getStockModelName2(), 
        		datao.getStockModelCycle());
        }
	
	@Override
	public void run() {
		if (bRunning)
			return ;
		bRunning = true;
		if (bInitIndex) {
			stockFetchData.initAllIndexData ();
		}
		if (bInitStock) {
	        stockDataService.removeAllData();
		}
		if (bInitModel) {
			initModel (mdlService, optionManager, smmManager);
		}
		bRunning = false; 
	}
}
