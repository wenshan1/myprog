package me.wenshan.blog.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.blog.domain.Tag;
import me.wenshan.dao.HibernateUtil;

@Service
public class TagServiceImp implements TagService {

	@Override
	public boolean exists(String tagName) {
		List<?> lst;
		Session sn = HibernateUtil.getSessionFactory().openSession();
		lst = sn.createQuery("from Tag as a where a.name = ?").setString(0, tagName).list();
		sn.close();
		return !lst.isEmpty();
	}

	@Override
	public Tag save(Tag tag) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try{
		sn.save(tag);
		sa.commit();
		}
		catch (HibernateException e) {
			sa.rollback();
		}finally {
		sn.close();
		}
		return tag;
	}

	@Override
	public Tag getOne(String tagName) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Tag tag = sn.get(Tag.class, tagName);
		sn.close();
		return tag;

	}

}
