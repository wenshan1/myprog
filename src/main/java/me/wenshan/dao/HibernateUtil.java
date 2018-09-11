package me.wenshan.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	@SuppressWarnings("rawtypes")
	private static List<Class> entityList = listEntityClass (); 

	// A SessionFactory is set up once for an application
	private static final SessionFactory sessionFactory = buildSessionFactory("hibernate.cfg.xml");

	@SuppressWarnings("rawtypes")
	private static List<Class> listEntityClass ()
	{
		ArrayList<Class> lst = new ArrayList<Class>();
		lst.add(me.wenshan.userinfo.domain.User.class);
		lst.add(me.wenshan.beijing.domain.Beijing_fangdican_qianyue.class);
		lst.add(me.wenshan.stock.domain.StockIndex.class);
		lst.add(me.wenshan.stock.domain.StockList.class);
		//lst.add(me.wenshan.stock.domain.StockM20Data.class);
		//lst.add(me.wenshan.stock.domain.StockGEMData.class);
		//lst.add(me.wenshan.stock.domain.Stock50GEMData.class);
		//lst.add(me.wenshan.stock.domain.StockGEMEXData.class);
		lst.add(me.wenshan.stock.domain.StockData.class);
		lst.add(me.wenshan.stockmodel.domain.StockModelTongJi.class);
		lst.add(me.wenshan.stockmodel.domain.StockModelData.class);
		
		//lst.add(me.wenshan.stock.domain.StockShData.class);
		//lst.add(me.wenshan.stock.domain.StockSzData.class);
		
		//lst.add(me.wenshan.blog.domain.Category.class);
		lst.add(me.wenshan.backend.domain.Option.class);
		lst.add(me.wenshan.blog.domain.Post.class);
		lst.add(me.wenshan.blog.domain.Tag.class);
		lst.add(me.wenshan.stock.domain.StockBasics.class);
		lst.add(me.wenshan.stock.domain.StockDayHis.class);
		
		
		//lst.add(me.wenshan.blog.domain.PostTag.class);

		return lst;
	}
	
	private static SessionFactory buildSessionFactory(String configFile) {
		try {
			Configuration configuration = new Configuration();
			configuration.configure(configFile);
			
			// if setenv, reset the jdbc url etc
			String str = System.getenv("MY_DB_HOST");
			if (str != null )
			{
				configuration.setProperty("hibernate.connection.url", 
						"jdbc:mysql://" + System.getenv("MY_DB_HOST") + ":3306" 
						+ "/stock?useUnicode=true&characterEncoding=UTF-8");
				configuration.setProperty("hibernate.connection.username", System.getenv("MY_DB_USERNAME"));
				configuration.setProperty("hibernate.connection.password", System.getenv("MY_DB_PASSWORD"));
			}
			//end of openshift
			
			for (int i=0; i< entityList.size(); i++)
				configuration.addAnnotatedClass (entityList.get(i));

			System.out.println(configuration.getProperties().toString());
			
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
			
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			logger.debug("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	private static void backAClass (SessionFactory from, SessionFactory to, Class<?> cls)
	{	
		logger.info("Begin backup " + cls.getName());
		
		final int pagesize = 500; 
		// 得到个数
		String sql = String.format("select count(a) from %s as a",  cls.getName());
		Session fromsn = from.openSession();
		Session tosn = to.openSession(); 
		
        long count = (long) fromsn.createQuery(sql).uniqueResult();
        long pagecount = count/pagesize;
        if (count%pagesize >0)
        	pagecount++;
        
        for (int page = 0; page <pagecount; page++)
        {
        	List<?> lst;
            sql = String.format("from %s as a",  cls.getName());   
            Query query = fromsn.createQuery(sql);
            query.setFirstResult( page * pagesize);
            query.setMaxResults(pagesize);
            lst = query.list();
            
            Transaction sa = tosn.beginTransaction();
            for (int i =0;i<lst.size(); i++)
    		{
    		tosn.save(lst.get(i));
    		}
            sa.commit();
        }
        
        
        fromsn.close();
        tosn.close();
        
        logger.info("End backup " + cls.getName());
        
        return;
	}
	
	public static void backData ()
	{
		SessionFactory from = buildSessionFactory("hibernate_from.cfg.xml");
		SessionFactory to = buildSessionFactory("hibernate_to.cfg.xml");
		for (int i=0; i<entityList.size(); i ++)
		{
			Session tosn = to.openSession();
			tosn.createQuery("delete from " + entityList.get(i).getName()).executeUpdate();
			tosn.close();
			
			backAClass (from ,to , entityList.get(i));
		}
	}
	
	
}
