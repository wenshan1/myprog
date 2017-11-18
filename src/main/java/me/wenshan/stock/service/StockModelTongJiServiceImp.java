package me.wenshan.stock.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.wenshan.backend.form.TongJiForm;
import me.wenshan.dao.HibernateUtil;
import me.wenshan.stockmodel.domain.StockModelData;
import me.wenshan.stockmodel.domain.StockModelTongJi;
import me.wenshan.stockmodel.service.StockModelDataService;

@Service
public class StockModelTongJiServiceImp implements StockModelTongJiService {
    @Autowired
    private StockModelDataService stockModelDataService; 

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
		try {
			Query query = sn.createQuery("from StockModelTongJi where modelName = :name order by id desc")
					.setString("name", modelName);
			query.setFirstResult(0);
			query.setMaxResults(1);
			List<?> lst = query.list();
			if (lst != null && !lst.isEmpty())
				tongji = (StockModelTongJi) lst.get(0);
		} finally {
			sn.close();
		}

		return tongji;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockModelTongJi> getLastData(String modelName, int num) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		List<StockModelTongJi> lst = null;
		try {
			Query query = sn.createQuery("from StockModelTongJi where modelName = :name order by id desc ")
					.setString("name", modelName);
			query.setFirstResult(0);
			query.setMaxResults(num);

			lst = (List<StockModelTongJi>) query.list();
		} finally {
			sn.close();
		}
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

	private double latestYearLv (String modelName) 
        {
	    double lv = 0;
	    Calendar cal = Calendar.getInstance();
	    Date today = cal.getTime();
	    cal.add(Calendar.YEAR, -1);
	    Date lasteseYear = cal.getTime();
	    SimpleDateFormat fmt = new SimpleDateFormat ("yyyy/MM/dd");
	    StockModelData startdata = null;
	    StockModelData enddata = null;
	    
	    // 得到开始日期数据
	    do {
	        String riqistr = fmt.format(lasteseYear);
	        startdata = stockModelDataService.getStockModelData
	                    (modelName, riqistr);
	        if (startdata !=null) {
	            break;
	        }
	        cal.setTime(lasteseYear);
	        cal.add(Calendar.DAY_OF_MONTH, 1);
	        lasteseYear = cal.getTime();
	    }while (true);
	    
	       // 得到终结日期数据
        do {
            String riqistr = fmt.format(today);
            enddata = stockModelDataService.getStockModelData
                      (modelName, riqistr);
            if (enddata !=null) {
                break;
            }
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            today = cal.getTime();
        }while (true);
        lv = (enddata.getCloseprice() - startdata.getCloseprice())/startdata.getCloseprice();
	    return lv;
        }
	
	@SuppressWarnings("unchecked")
	@Override
	public TongJiForm getTongJiForm(String modelName) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		List<StockModelTongJi> lst = null;
		try {
			Query query = sn.createQuery("from StockModelTongJi where modelName = :name order by id desc")
					.setString("name", modelName);
			lst = (List<StockModelTongJi>) query.list();
		} finally {
			sn.close();
		}

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
		form.setLatestYearLv(latestYearLv(modelName));
		return form;
	}
}
