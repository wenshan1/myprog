package me.wenshan.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockGEMEXData;

public class StockGEMEXDataServiceImp implements IStockGEMEXDataService {

    private static StockGEMEXDataServiceImp  imp;
    
    private StockGEMEXDataServiceImp ()
    {
        
    }
    
    public static IStockGEMEXDataService getInstance() {
        if (imp == null)
            imp = new StockGEMEXDataServiceImp();
        return imp;
    }
    
    @Override
    public void saveOrUpdate(StockGEMEXData data) {
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
    public List<StockGEMEXData> getDataRecord(String riqistr, int ncount) {
        String sql = String.format(
                "SELECT * FROM stockgemexdata where riqi <'%s' order by riqi desc limit 0, %d", 
                riqistr, ncount);

        List<StockGEMEXData> lst;
        Session sn = HibernateUtil.getSessionFactory().openSession();
        lst = sn.createSQLQuery(sql).addEntity(StockGEMEXData.class).list();
        sn.close();

        return  lst;
    }

    @Override
    public long Count() {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        long cou = (long) sn.createQuery("select count(u) from StockGEMEXData as u").uniqueResult();
        sn.close();
        return cou;
    }

    @Override
    public List<StockGEMEXData> getPageData(int first, int pageSize) {
        List<StockGEMEXData> lst = new ArrayList<StockGEMEXData> (); 
        
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from StockGEMEXData as a order by a.riqi desc");
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        
        List<?> queryList=query.list();
        for(Object obj : queryList){
            lst.add((StockGEMEXData) obj);
        }
        sn.close();
        return lst;
    }

	@Override
	public StockGEMEXData getData(String strriqi) {
        String sql = String.format("SELECT * FROM stockgemexdata where riqi = '%s' ", strriqi);
        Session sn = HibernateUtil.getSessionFactory().openSession();
        StockGEMEXData data =  (StockGEMEXData) sn.createSQLQuery(sql).addEntity(StockGEMEXData.class).uniqueResult();
        sn.close();
        return data;
	}

}
