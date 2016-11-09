package me.wenshan.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.Stock50GEMData;

public class Stock50GEMDataServiceImp implements IStock50GEMDataService {

    private static Stock50GEMDataServiceImp ssimp;

    private Stock50GEMDataServiceImp() {

    }

    public static IStock50GEMDataService getInstance() {
        if (ssimp == null)
            ssimp = new Stock50GEMDataServiceImp();
        return ssimp;
    }

    @Override
    public List<Stock50GEMData> getDataRecord(String riqistr, int ncount) {
        String sql = String.format(
                "SELECT * FROM stock50gemdata where riqi <'%s' order by riqi desc limit 0, %d", 
                riqistr, ncount);

        List<Stock50GEMData> lst;
        Session sn = HibernateUtil.getSessionFactory().openSession();
        lst = sn.createSQLQuery(sql).addEntity(Stock50GEMData.class).list();
        sn.close();

        return  lst;
    }

    @Override
    public void saveData(Stock50GEMData data) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        sn.saveOrUpdate(data);
        sa.commit();
        sn.close();
        
    }

    @Override
    public long Count() {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        long cou = (long) sn.createQuery("select count(u) from Stock50GEMData as u").uniqueResult();
        sn.close();
        return cou;
    }

    @Override
    public List<Stock50GEMData> getPageData(int first, int pageSize) {
        List<Stock50GEMData> lst = new ArrayList<Stock50GEMData> (); 
        
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from Stock50GEMData as a order by a.riqi desc");
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        
        List<?> queryList=query.list();
        for(Object obj : queryList){
            lst.add((Stock50GEMData) obj);
        }
        sn.close();
        return lst;
    }

	@Override
	public Stock50GEMData getData(String riqi) {
        String sql = String.format("SELECT * FROM stock50gemdata where riqi = '%s' ", riqi);
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Stock50GEMData data =  (Stock50GEMData) sn.createSQLQuery(sql).addEntity(Stock50GEMData.class).uniqueResult();
        sn.close();
        return data;
	}

}
