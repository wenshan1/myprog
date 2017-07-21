package me.wenshan.stock.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockBasics;
import me.wenshan.stock.domain.StockDayHis;

@Service
public class StockDayHisServiceImp implements StockDayHisService {

    @Override
    public long count()
        {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        long cou = (long) sn.createQuery("select count(u) from StockDayHis as u").uniqueResult();
        sn.close();
        return cou;
        }

    @Override
    public List<StockDayHis> getPageData(int first, int pageSize) 
        {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from StockDayHis as a order by a.pk.riqi desc");
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        List<?> lst = query.list();
        sn.close();
        if (lst == null)
            {
            return null;
            }
        return (List<StockDayHis>) lst;
    }


}
