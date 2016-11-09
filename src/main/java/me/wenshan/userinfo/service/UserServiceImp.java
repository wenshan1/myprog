package me.wenshan.userinfo.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.userinfo.domain.User;

public class UserServiceImp implements UserService {

	private static UserServiceImp service = null;
	
	private UserServiceImp(){
		
	}
	
	public static UserServiceImp get(){
		if (service == null){
			service = new UserServiceImp();
		}
		return service;
	}
	
	@Override
	public void save(User user) {
	
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa=sn.beginTransaction();
		sn.saveOrUpdate(user);
		sa.commit();
		sn.close();
	}

	@Override
	public User getUser(String user) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		User userObj=(User) sn.createQuery("from User as u where u.username = ?").setString(0, user).uniqueResult();
		sn.close();
		return userObj;
	}

	@Override
	public long count() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou=(long) sn.createQuery("select count(u) from User as u").uniqueResult();
		sn.close();
		return cou;
	}
	
}
