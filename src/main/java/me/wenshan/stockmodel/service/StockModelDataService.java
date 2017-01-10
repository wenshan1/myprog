package me.wenshan.stockmodel.service;

import java.util.List;

import me.wenshan.stock.domain.StockM20Data;
import me.wenshan.stockmodel.domain.StockModelData;

public interface StockModelDataService {
    long count (String modelName);
    void save(StockModelData data);
    void saveOrUpdate (StockModelData data);
    void removeAllData (String modelName);
    List<StockModelData> getStockDataRecord (String modelName, String riqistr, int ncount);
    List<StockModelData> getPageData(String modelName, int first, int pageSize);
}
