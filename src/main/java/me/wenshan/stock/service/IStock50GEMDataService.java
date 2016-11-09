package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.stock.domain.Stock50GEMData;

public interface IStock50GEMDataService {
    List<Stock50GEMData> getDataRecord (String riqistr, int ncount);
    void saveData (Stock50GEMData data);
    long Count ();
    List<Stock50GEMData> getPageData (int first, int pageSize);
    Stock50GEMData getData (String riqi);
}
