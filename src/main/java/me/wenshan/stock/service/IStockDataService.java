package me.wenshan.stock.service;

import me.wenshan.stock.domain.StockData;

public interface IStockDataService {
    void save (StockData data);
    void saveOrUpdate (StockData data);
    void removeAllData ();
}
