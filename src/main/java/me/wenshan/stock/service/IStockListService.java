package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.StockList;

public interface IStockListService {
    List<StockList> getStockListFromWeb (); 
    void save (StockList data);
    void removeAllData ();
    List<StockList> getAllDataFromDB ();
    long count();
}
