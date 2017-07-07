package me.wenshan.stock.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.stock.domain.StockData;
import me.wenshan.stock.domain.StockData_PK;

@Service
public class StockDataServiceImp implements IStockDataService {

	public StockDataServiceImp() {
	}

	@Override
	public void save(StockData data) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try {
			sn.save(data);
			sa.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sn.close();
	}

	@Override
	public void saveOrUpdate(StockData data) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try {
			sn.saveOrUpdate(data);
			sa.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sn.close();
	}

	@Override
	public void removeAllData() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		sn.createSQLQuery("delete from stockdata").executeUpdate();
		sa.commit();
		sn.close();
	}

	@Override
	public boolean save(List<StockData> lst) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		boolean bRet = true;
		Transaction sa = sn.beginTransaction();
		try {
			for (int i = 0; i < lst.size(); i++) {
				sn.save(lst.get(i));
				if (i % 200 == 0) {
					sa.commit();
					sa = sn.beginTransaction();
				}
			}
			sa.commit();
		} catch (Exception e) {
			e.printStackTrace();
			bRet = false;
		}
		sn.close();
		return bRet;
	}

	@Override
	public long count() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from StockData as u").uniqueResult();
		sn.close();
		return cou;
	}

	@Override
	public StockData get(String riqi, String stockName) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formater.parse(riqi);
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}

		StockData_PK pk = new StockData_PK(date, stockName);
		Session sn = HibernateUtil.getSessionFactory().openSession();
		StockData data = sn.get(StockData.class, pk);
		sn.close();

		return data;
	}

	/*
	 * public static boolean isTodayDataExist (String stockName) { boolean bRet =
	 * true; Calendar cal = Calendar.getInstance(); int m = cal.get(Calendar.MONTH)
	 * + 1; int d = cal.get(Calendar.DAY_OF_MONTH); int y = cal.get(Calendar.YEAR);
	 * String strriqi = String.format("%d-%02d-%02d", y, m, d); IStockDataService ss
	 * = StockDataServiceImp.get();
	 * 
	 * if (ss.get(strriqi, stockName) == null){ bRet = false; } return bRet; }
	 */
}
