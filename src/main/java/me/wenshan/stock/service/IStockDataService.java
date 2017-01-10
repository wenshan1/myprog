package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.StockData;

public interface IStockDataService {
    void save (StockData data);
    void saveOrUpdate (StockData data);
    void removeAllData ();
    boolean save(List<StockData> lst);
    long count ();
    StockData get(String riqi, String stockName);
}
