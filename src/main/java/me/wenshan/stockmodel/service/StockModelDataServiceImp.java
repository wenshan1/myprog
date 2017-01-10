package me.wenshan.stockmodel.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stockmodel.domain.StockModelData;

@Service
public class StockModelDataServiceImp implements StockModelDataService {

    @Override
    public long count(String modelName) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        long cou = (long) sn.createQuery
                ("select count(a) from StockModelData as a where a.pk.name = :name").
                setString("name", modelName).uniqueResult();
        sn.close();
        return cou;
    }

    @Override
    public void removeAllData(String modelName) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        Query query = sn.createQuery("delete from StockModelData as a where a.pk.name = :name").
                setString("name", modelName);
        query.executeUpdate();
        sa.commit();
        sn.close();
    }
    
    @Override
    public void save(StockModelData data) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        try {
            sn.save(data);
            sa.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        sn.close();
        }
        
    }

    @Override
    public void saveOrUpdate(StockModelData data) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Transaction sa = sn.beginTransaction();
        try {
            sn.saveOrUpdate(data);
            sa.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        sn.close();
        }
        
    }

    @Override
    public List<StockModelData> getStockDataRecord(
            String modelName, 
            String riqistr, int ncount
            ) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from StockModelData as a where a.pk.name = :name and a.pk.riqi < :riqi order by a.pk.riqi desc").
                setString("riqi", riqistr).setString("name", modelName);
        query.setFirstResult(0);
        query.setMaxResults(ncount);
        
        @SuppressWarnings("unchecked")
        List<StockModelData> queryList=query.list();
        
        return queryList;
    }

    @Override
    public List<StockModelData> getPageData(String modelName, int first, int pageSize) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from StockModelData as a where a.pk.name = :name order by a.pk.riqi desc").
                setString("name", modelName);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        
        @SuppressWarnings("unchecked")
        List<StockModelData> queryList=query.list();
        
        return queryList;
    }

}