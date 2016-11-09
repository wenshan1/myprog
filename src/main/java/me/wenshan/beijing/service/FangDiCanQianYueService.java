package me.wenshan.beijing.service;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.beijing.domain.Beijing_fangdican_qianyue;
import me.wenshan.dao.HibernateUtil;


public class FangDiCanQianYueService  {
	private static FangDiCanQianYueService service = null;
	
	private FangDiCanQianYueService ()
	{
		
	}
	public static FangDiCanQianYueService getInstance()
	{
		if (service == null)
			service = new FangDiCanQianYueService();
		return service;
	}
	
	public void saveOrUpdate(Beijing_fangdican_qianyue bj)
	{
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa=sn.beginTransaction();
		sn.saveOrUpdate(bj);
		sa.commit();
		sn.close();
	}
	
	public long count(){
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou=(long) sn.createQuery("select count(u) from Beijing_fangdican_qianyue as u").uniqueResult();
		sn.close();
		return cou;
	}
	
    public List<Beijing_fangdican_qianyue> getPageData(int first, int pageSize) {
        List<Beijing_fangdican_qianyue> lst = new ArrayList<Beijing_fangdican_qianyue> (); 
        
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from Beijing_fangdican_qianyue as a order by a.riqi desc");
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        
        List<?> queryList=query.list();
        for(Object obj : queryList){
            lst.add((Beijing_fangdican_qianyue) obj);
        }
        sn.close();
        return lst;
    }

}
