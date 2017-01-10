package me.wenshan.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockIndex;

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
	public long M20DataCount() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from StockM20Data as u").uniqueResult();
		sn.close();
		return cou;
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
	public long GEMDataCount() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from StockGEMData as u").uniqueResult();
		sn.close();
		return cou;
	}

	

	

	@Override
	public boolean saveAll(List<StockIndex> lst, boolean saved) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		for (int i = 0; i < lst.size(); i++) {
			if (saved)
				sn.save(lst.get(i));
			else
				sn.saveOrUpdate(lst.get(i));

			if (i % 500 == 0) {
				sa.commit();
				sa = sn.beginTransaction();
			}
		}
		sa.commit();
		sn.close();
		return true;
	}

	@Override
	public boolean removeAll() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		sn.createQuery("delete from StockIndex").executeUpdate();
		sa.commit();
		sn.close();
		
		return true;
	}
}
