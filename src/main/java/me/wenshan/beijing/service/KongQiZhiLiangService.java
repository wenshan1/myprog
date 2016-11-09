package me.wenshan.beijing.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.beijing.domain.KongQiZhiLiang;
import me.wenshan.dao.HibernateUtil;

public class KongQiZhiLiangService {
	private static KongQiZhiLiangService service;
	
	private KongQiZhiLiangService()
	{
		
	}
	public static KongQiZhiLiangService getInstance ()
	{
		if ( service == null)
			service = new KongQiZhiLiangService();
		return service;
	}
	public void saveOrUpdate(KongQiZhiLiang kongqi)
	{
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa=sn.beginTransaction();
		sn.saveOrUpdate(kongqi);
		sa.commit();
		sn.close();
	};
	
	public long count(){
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou=(long) sn.createQuery("select count(u) from KongQiZhiLiang as u").uniqueResult();
		sn.close();
		return cou;
	}
	
    public List<KongQiZhiLiang> getPageData(int first, int pageSize) {
        List<KongQiZhiLiang> lst = new ArrayList<KongQiZhiLiang> (); 
        
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from KongQiZhiLiang as a order by a.datetime desc");
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        
        List<?> queryList=query.list();
        for(Object obj : queryList){
            lst.add((KongQiZhiLiang) obj);
        }
        sn.close();
        return lst;
    }
}
