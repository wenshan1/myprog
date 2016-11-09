package me.wenshan.stock.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockData;

public class StockDataServiceImp implements IStockDataService {

    @Override
    public void save(StockData data) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        try {
            sn.save(data);
            sa.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sn.close();
    }

    @Override
    public void saveOrUpdate(StockData data) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        try {
            sn.saveOrUpdate(data);
            sa.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sn.close();
    }

    @Override
    public void removeAllData() {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        sn.createSQLQuery("delete from stockdata").executeUpdate();
        sa.commit();
        sn.close();
    }

}
