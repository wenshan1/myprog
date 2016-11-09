/*
* Copyright (c) 2015 www.wenshan.me.
*
*/
 
/*
modification history
--------------------
02nov15,lanbin  created
*/


package me.wenshan.newsmth.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.newsmth.domain.Newsmth;
import me.wenshan.newsmth.domain.NewsmthData;
import me.wenshan.newsmth.domain.Photo;

public class NewsmthServiceImp implements NewsmthService {
	
	private static NewsmthServiceImp  nm = null;
	
	private NewsmthServiceImp ()
	{
		
	}
	public static NewsmthServiceImp getInstance ()
	{
		{
			if (nm == null)
			{
				nm = new NewsmthServiceImp();
			}
		}
		return nm;
	}
	
	@Override
	public void save(NewsmthData nmdata) {
		
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa=sn.beginTransaction();
		sn.saveOrUpdate(nmdata.getNewsmth());
		sa.commit();
		
		if (nmdata.getPhoto() != null){
			sa=sn.beginTransaction();
			sn.saveOrUpdate(nmdata.getPhoto());
			sa.commit();
			}
		
		sn.close();	
		sn = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Newsmth> getAllDataByBoardName(String boardName) {
		List<?> lst;
		Session sn = HibernateUtil.getSessionFactory().openSession();
		lst = sn.createQuery("from Newsmth as nm where nm.boardName = ?").setString(0,boardName).list();
		sn.close();
		return (List<Newsmth>) lst;
	}
	
	@Override
	public long count() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou=(long) sn.createQuery("select count(u) from Newsmth as u").uniqueResult();
		sn.close();
		return cou;
	}
	@Override
	public long countPhoto() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou=(long) sn.createQuery("select count(u) from Photo as u").uniqueResult();
		sn.close();
		return cou;
	}
	
	
	@Override
	public List<NewsmthData> getPageData(int first, int pageSize) {
		List<NewsmthData> lst = new ArrayList<NewsmthData> (); 
		
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from Newsmth as a order by a.publishDate desc");
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List<?> queryList=query.list();
		for(Object obj : queryList){
			NewsmthData data = new NewsmthData();
			data.setNewsmth((Newsmth) obj);
			data.setPhoto(getPhotoById (data.getNewsmth().getLink()));
			lst.add(data);
		}
		
		sn.close();
		return lst;
	}
	
	@Override
	public Photo getPhotoById(String link) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Photo ph = (Photo) sn.createQuery("from Photo as a where a.link = ?").setString(0, link).uniqueResult();
		sn.close();
		
		return ph;
	}
	@Override
	public void delete(String link) {
		try{
		Newsmth nm = new Newsmth();
		nm.setLink(link);
		
		Photo ph = new Photo(link);
		
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa=sn.beginTransaction();
		sn.delete(nm);
		sa.commit();
		
		sa = sn.beginTransaction();
		sn.delete(ph);
		sa.commit();
		sn.close();
		}
		catch (Exception e)
		{
			
		}
	}
	
}
