package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.StockDayHis;

public interface StockDayHisService {
    long count ();
    List<StockDayHis> getPageData(int first, int pageSize);
}
