package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.StockGEMEXData;

public interface IStockGEMEXDataService {
    void saveOrUpdate (StockGEMEXData data);
    List<StockGEMEXData> getDataRecord (String riqistr, int ncount);
    long Count ();
    List<StockGEMEXData> getPageData (int first, int pageSize);
	StockGEMEXData getData(String strriqi);
}
