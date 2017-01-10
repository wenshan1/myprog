package me.wenshan.stock.mtw;

import java.util.Date;
import me.wenshan.stock.domain.StockModelTongJi;
import me.wenshan.stock.service.StockModelTongJiService;
import me.wenshan.util.MyBeanFactory;

public class StockModelTongJiMgr {
	private static StockModelTongJiMgr instance = null;
	private StockModelTongJiMgr () {
		
	}
	
	public static StockModelTongJiMgr get () {
		if (instance == null)
			instance = new StockModelTongJiMgr ();
		return instance;
	}
	
	public boolean saveModelData(String modelName, Date date, double dbIndex, 
			                     String currentstockName) {
		StockModelTongJiService mdlService = MyBeanFactory.getBean (StockModelTongJiService.class);
		StockModelTongJi tongji = mdlService.getLastOne(modelName);
		if (currentstockName.isEmpty()) {
			// 如果统计模型已经完成了就忽略，否则就设置统计模型完成了。如果没有模型记录就忽略。
			if (tongji != null) {
				if (tongji.getStatus() == 1) {
					tongji.setStatus(2);
					mdlService.update(tongji);
				}
			}
		}
		else {
			// 最近的模型统计模型已经完成了，就新开一个模型统计记录。否则就更新数据。
			if (tongji == null) {
				tongji = new StockModelTongJi ();
				tongji.setStatus(1);
				tongji.setStartClosePrice(dbIndex);
				tongji.setStartStockName(currentstockName);
				tongji.setStartDate(date);
				tongji.setModelName(modelName);
				tongji.setEndClosePrice(dbIndex);
				tongji.setEndDate(date);
				tongji.setEndStockName(currentstockName);
				mdlService.save(tongji);
			}
			else {
				if (tongji.getStatus() == 2) {
					tongji = new StockModelTongJi ();
					tongji.setStatus(1);
					tongji.setStartDate(date);
					tongji.setStartClosePrice(dbIndex);
					tongji.setStartStockName(currentstockName);
					tongji.setModelName(modelName);
					tongji.setEndClosePrice(dbIndex);
					tongji.setEndDate(date);
					tongji.setEndStockName(currentstockName);
					mdlService.save(tongji);
				}
				else {
					tongji.setEndDate(date);
					tongji.setEndStockName(currentstockName);
					tongji.setEndClosePrice(dbIndex);
					mdlService.update(tongji);
				}
			}
		}
		return false;
	}
		
	public boolean removeAllData () {
		StockModelTongJiService mdlService = MyBeanFactory.getBean (StockModelTongJiService.class);
		return mdlService.removeAll ();
	}
}
