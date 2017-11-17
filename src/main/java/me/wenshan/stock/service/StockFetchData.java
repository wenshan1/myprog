package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.*;

public interface StockFetchData {
	//获得指数日k线数据 
	List<StockIndex> getIndexData_K (final String stockName, final String startDate, final String endDate);
	boolean initAllIndexData ();
	boolean updateAllIndexData ();

}
