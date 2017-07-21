package me.wenshan.stock.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockBasics;

@Service
public class StockBasicsServiceImp implements StockBasicsService {

    @Override
    public long count() {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        long cou = (long) sn.createQuery("select count(u) from StockBasics as u").uniqueResult();
        sn.close();
        return cou;
    }

    @Override
    public List<StockBasics> getPageData(int first, int pageSize) {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from StockBasics as a order by a.code");
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        List<?> lst = query.list();
        sn.close();
        if (lst == null)
            {
            return null;
            }
        return (List<StockBasics>) lst;
    }

}
