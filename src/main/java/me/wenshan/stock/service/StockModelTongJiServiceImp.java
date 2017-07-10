package me.wenshan.stock.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.blog.backend.form.TongJiForm;
import me.wenshan.dao.HibernateUtil;
import me.wenshan.stockmodel.domain.StockModelTongJi;

@Service
public class StockModelTongJiServiceImp implements StockModelTongJiService {

	@Override
	public boolean save(StockModelTongJi st) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try {
			sn.save(st);
			sa.commit();
		} finally {
			sn.close();
		}

		return true;
	}

	@Override
	public StockModelTongJi getLastOne(String modelName) {
		StockModelTongJi tongji = null;
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from StockModelTongJi where modelName = :name order by id desc").setString("name",
				modelName);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<?> lst = query.list();
		if (lst != null && !lst.isEmpty())
			tongji = (StockModelTongJi) lst.get(0);
		sn.close();

		return tongji;
	}

	@Override
	public List<StockModelTongJi> getLastData(String modelName, int num) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from StockModelTongJi where modelName = :name order by id desc ")
				.setString("name", modelName);
		query.setFirstResult(0);
		query.setMaxResults(num);

		List<StockModelTongJi> lst = (List<StockModelTongJi>) query.list();
		sn.close();
		return lst;
	}

	@Override
	public boolean saveorupdate(StockModelTongJi st) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try {
			sn.saveOrUpdate(st);
			sa.commit();
		} finally {
			sn.close();
		}

		return true;
	}

	@Override
	public boolean removeAll() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try {
			sn.createQuery("delete from StockModelTongJi").executeUpdate();
			sa.commit();
		} finally {
			sn.close();
		}

		return true;
	}

	@Override
	public TongJiForm getTongJiForm(String modelName) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from StockModelTongJi where modelName = :name order by id desc").setString("name",
				modelName);
		List<StockModelTongJi> lst = (List<StockModelTongJi>) query.list();
		sn.close();

		TongJiForm form = new TongJiForm();
		form.setCount(lst.size());
		for (int i = 0; i < lst.size(); i++) {
			StockModelTongJi tongji = lst.get(i);
			double yinglilv = (tongji.getEndClosePrice() - tongji.getStartClosePrice()) / tongji.getStartClosePrice();
			if (yinglilv > form.getYinMax()) {
				form.setYinMax(yinglilv);
				form.setYinMaxObject(tongji);
			}

			if (yinglilv < form.getYinMin()) {
				form.setYinMin(yinglilv);
				form.setYinMinObject(tongji);
			}

			if (yinglilv > 0)
				form.setYinCount(form.getYinCount() + 1);
		}

		return form;
	}
}
