package me.wenshan.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockGEMData;
import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.domain.StockM20Data;

public class StockServiceImp implements IStockService {
	private static StockServiceImp ssimp;

	private StockServiceImp() {

	}

	public static IStockService getInstance() {
		if (ssimp == null)
			ssimp = new StockServiceImp();
		return ssimp;
	}

	@Override
	public void save(StockIndex sin) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		sn.saveOrUpdate(sin);
		sa.commit();
		sn.close();
	}

	@Override
	public long count() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from StockIndex as u").uniqueResult();
		sn.close();
		return cou;
	}

	@Override
	public List<StockIndex> getDataRecord(String stockname, String riqistr, int ncount) {
		String sql = String.format(
				"SELECT * FROM stockindex where name ='%s' and riqi <'%s' order by riqi desc limit 0, %d", stockname,
				riqistr, ncount);

		List<StockIndex> lst;
		Session sn = HibernateUtil.getSessionFactory().openSession();
		lst = sn.createSQLQuery(sql).addEntity(StockIndex.class).list();
		sn.close();

		return  lst;
	}

	@Override
	public StockIndex getStockIndex(String riqi, String stockName) {
		String sql = String.format("SELECT * FROM stockindex where name = '%s' and riqi = '%s'", stockName, riqi);
		Session sn = HibernateUtil.getSessionFactory().openSession();
		StockIndex index = (StockIndex) sn.createSQLQuery(sql).addEntity(StockIndex.class).uniqueResult();
		sn.close();
		return index;
	}

	@Override
	public void saveM20Data(StockM20Data data) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		sn.saveOrUpdate(data);
		sa.commit();
		sn.close();
	}

	@Override
	public List<StockM20Data> getStockM20DataRecord(String riqistr, int ncount) {
		String sql = String.format(
				"SELECT * FROM stockmtwdata where riqi <'%s' order by riqi desc limit 0, %d", 
				riqistr, ncount);

		List<StockM20Data> lst;
		Session sn = HibernateUtil.getSessionFactory().openSession();
		lst = sn.createSQLQuery(sql).addEntity(StockM20Data.class).list();
		sn.close();

		return  lst;
	}

	@Override
	public long M20DataCount() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from StockM20Data as u").uniqueResult();
		sn.close();
		return cou;
	}

	@Override
	public List<StockM20Data> getM20DataPageData(int first, int pageSize) {
		List<StockM20Data> lst = new ArrayList<StockM20Data> (); 
		
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from StockM20Data as a order by a.riqi desc");
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List<?> queryList=query.list();
		for(Object obj : queryList){
			lst.add((StockM20Data) obj);
		}
		sn.close();
		return lst;
	}

	public List<StockIndex> getPageData(int first, int pageSize) {
		List<StockIndex> lst = new ArrayList<StockIndex> (); 
		
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from StockIndex as a order by a.pk.riqi desc");
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List<?> queryList=query.list();
		for(Object obj : queryList){
			lst.add((StockIndex) obj);
		}
		sn.close();
		return lst;
	}

	@Override
	public void saveGEMData(StockGEMData data) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		sn.saveOrUpdate(data);
		sa.commit();
		sn.close();
	}

	@Override
	public List<StockGEMData> getStockGEMDataRecord(String riqistr, int ncount) {
		String sql = String.format(
				"SELECT * FROM stockgemdata where riqi <'%s' order by riqi desc limit 0, %d", 
				riqistr, ncount);

		List<StockGEMData> lst;
		Session sn = HibernateUtil.getSessionFactory().openSession();
		lst = sn.createSQLQuery(sql).addEntity(StockGEMData.class).list();
		sn.close();

		return  lst;
	}

	@Override
	public long GEMDataCount() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from StockGEMData as u").uniqueResult();
		sn.close();
		return cou;
	}

	@Override
	public List<StockGEMData> getGEMDataPageData(int first, int pageSize) {
		List<StockGEMData> lst = new ArrayList<StockGEMData> (); 
		
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from StockGEMData as a order by a.riqi desc");
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List<?> queryList=query.list();
		for(Object obj : queryList){
			lst.add((StockGEMData) obj);
		}
		sn.close();
		return lst;
	}

	@Override
	public StockM20Data getStockM20Data(String strriqi) {
        String sql = String.format("SELECT * FROM stockmtwdata where riqi = '%s' ", strriqi);
        Session sn = HibernateUtil.getSessionFactory().openSession();
        StockM20Data data =  (StockM20Data) sn.createSQLQuery(sql).addEntity(StockM20Data.class).uniqueResult();
        sn.close();
        return data;
	}

	@Override
	public StockGEMData getStockGEMData(String strriqi) {
        String sql = String.format("SELECT * FROM stockgemdata where riqi = '%s' ", strriqi);
        Session sn = HibernateUtil.getSessionFactory().openSession();
        StockGEMData data =  (StockGEMData) sn.createSQLQuery(sql).addEntity(StockGEMData.class).uniqueResult();
        sn.close();
        return data;
	}
}
