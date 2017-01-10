package me.wenshan.blog.backend.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.blog.backend.domain.Option;
import me.wenshan.dao.HibernateUtil;

@Service
public class OptionServiceImp implements OptionService {
	public OptionServiceImp () {
		
	}
	
	@Override
	public void save(Option op) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa=sn.beginTransaction();
		sn.save(op);
		sa.commit();
		sn.close();
	}

	@Override
	public long count() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from Option as u").uniqueResult();
		sn.close();
		return cou;
	}

	@Override
	public String getOptionValue(String name) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Option op = sn.get(Option.class, name);
		sn.close();
		if (op != null)
			return op.getValue();
		return "";
	}

	@Override
	public void updateOptionValue(String name, String value) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa=sn.beginTransaction();
		sn.saveOrUpdate(new Option(name, value));
		sa.commit();
		sn.close();
	}

}
