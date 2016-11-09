package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.StockGEMData;
import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.domain.StockM20Data;

public interface IStockService {
	
	void save (StockIndex sin);
	long count();
	List<StockIndex> getDataRecord (String stockname, String riqistr, int ncount);
	StockIndex getStockIndex (String riqi, String stockName);
	List<StockIndex> getPageData(int first, int pageSize) ;
	
	void saveM20Data (StockM20Data data);
	List<StockM20Data> getStockM20DataRecord (String riqistr, int ncount);	
	long M20DataCount ();
	List<StockM20Data> getM20DataPageData(int first, int pageSize);
	
	void saveGEMData (StockGEMData data);
	List<StockGEMData> getStockGEMDataRecord (String riqistr, int ncount);	
	long GEMDataCount ();
	List<StockGEMData> getGEMDataPageData(int first, int pageSize);
	StockM20Data getStockM20Data(String strriqi);
	StockGEMData getStockGEMData(String strriqi);
}
