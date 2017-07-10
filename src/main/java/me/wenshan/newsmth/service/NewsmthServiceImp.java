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
import org.springframework.stereotype.Service;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.newsmth.domain.Photo;
import me.wenshan.newsmth.domain.Newsmth;
import me.wenshan.newsmth.domain.NewsmthData;

@Service
public class NewsmthServiceImp implements NewsmthService {

	public NewsmthServiceImp() {

	}

	@Override
	public void save(NewsmthData nmdata) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		Object obj = sn.createQuery("select nm.id from Newsmth as nm where nm.link = ?")
				.setString(0, nmdata.getNewsmth().getLink()).uniqueResult();
		sn.clear();

		try {

			if (obj != null) {
				long id = (long) obj;
				nmdata.getNewsmth().setId(id);
				sn.update(nmdata.getNewsmth());
			} else {
				sn.save(nmdata.getNewsmth());
			}

			if (nmdata.getPhotos() != null && nmdata.getPhotos().size() != 0) {
				long id = (long) sn.createQuery("select nm.id from Newsmth as nm where nm.link = ?")
						.setString(0, nmdata.getNewsmth().getLink()).uniqueResult();
				sn.createQuery(
						"delete from Photo ph where ph.newsmthid in (select nm.id from Newsmth as nm where nm.link = :link)")
						.setString("link", nmdata.getNewsmth().getLink()).executeUpdate();
				for (int i = 0; i < nmdata.getPhotos().size(); i++) {
					nmdata.getPhotos().get(i).setNewsmthid(id);
					sn.save(nmdata.getPhotos().get(i));
				}
			}

			sa.commit();
		} catch (Exception e) {
			e.printStackTrace();
			sa.rollback();
		} finally {
			sn.close();
			sn = null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Newsmth> getAllDataByBoardName(String boardName) {
		List<?> lst;
		Session sn = HibernateUtil.getSessionFactory().openSession();
		lst = sn.createQuery("from Newsmth as nm where nm.boardName = ?").setString(0, boardName).list();
		sn.close();
		return (List<Newsmth>) lst;
	}

	@Override
	public long count() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from Newsmth as u").uniqueResult();
		sn.close();
		return cou;
	}

	@Override
	public long countPhoto() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from Photo as u").uniqueResult();
		sn.close();
		return cou;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteOld(long preservernum) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		try {
			if (count() > preservernum) {
				long nRemoved = count() - preservernum;
				List<Object> lst;
				lst = sn.createQuery("select nm.id from Newsmth nm order by id").list();
				Transaction sa = sn.beginTransaction();
				for (int i = 0; i < nRemoved; i++) {
					long id = (long) lst.get(i);
					sn.createQuery("delete from Newsmth nm where nm.id = :id").setLong("id", id).executeUpdate();
					sn.createQuery("delete from Photo ph where ph.newsmthid = :newsmthid").setLong("newsmthid", id)
							.executeUpdate();
				}
				sa.commit();
			}
		} finally {
			sn.close();
		}
	}

	@Override
	public List<NewsmthData> getPageData(int first, int pageSize) {
		List<NewsmthData> lst = new ArrayList<NewsmthData>();

		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from Newsmth as a order by a.id desc");
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		List<?> queryList = query.list();
		for (Object obj : queryList) {
			NewsmthData data = new NewsmthData();
			data.setNewsmth((Newsmth) obj);
			List<Photo> photolst = (List<Photo>) sn.createQuery("from Photo as a where a.newsmthid = ?")
					.setLong(0, data.getNewsmth().getId()).list();
			data.setPhotos(photolst);
			lst.add(data);
		}

		sn.close();
		return lst;
	}

	@Override
	public void delete(long id) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try {
			sn.createQuery("delete from Newsmth ns where ns.id = ?").setLong(0, id).executeUpdate();
			sn.createQuery("delete from Photo ph where ph.newsmthid = ?").setLong(0, id).executeUpdate();
			sa.commit();
		} finally {
			sn.close();
		}

	}

}
