package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.StockBasics;

public interface StockBasicsService {
    long count ();
    List<StockBasics> getPageData(int first, int pageSize);
}
