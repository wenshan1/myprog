package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.StockIndex;

public interface IStockService {
	boolean removeAll ();
	boolean saveAll(List<StockIndex> lst, boolean saved);
	void save (StockIndex sin);
	long count();
	List<StockIndex> getDataRecord (String stockname, String riqistr, int ncount);
	StockIndex getStockIndex (String riqi, String stockName);
	List<StockIndex> getPageData(int first, int pageSize) ;
	long M20DataCount ();	
	long GEMDataCount ();
	
}
