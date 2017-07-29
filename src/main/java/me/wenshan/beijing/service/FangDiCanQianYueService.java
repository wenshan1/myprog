package me.wenshan.beijing.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.beijing.domain.Beijing_fangdican_qianyue;
import me.wenshan.dao.HibernateUtil;

@Service
public class FangDiCanQianYueService {

	public FangDiCanQianYueService() {

	}

	public void saveOrUpdate(Beijing_fangdican_qianyue bj) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction sa = sn.beginTransaction();
		try {
			sn.saveOrUpdate(bj);
			sa.commit();
		} finally {
			sn.close();
		}
	}

	public long count() {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		long cou = (long) sn.createQuery("select count(u) from Beijing_fangdican_qianyue as u").uniqueResult();
		sn.close();
		return cou;
	}

	public List<Beijing_fangdican_qianyue> getAllData() {

		Session sn = HibernateUtil.getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		List<Beijing_fangdican_qianyue> query = (List<Beijing_fangdican_qianyue>) sn
				.createQuery("from Beijing_fangdican_qianyue as a order by a.riqi desc").list();
		sn.close();
		return query;
	}

	public List<Beijing_fangdican_qianyue> getPageData(int first, int pageSize) {
		List<Beijing_fangdican_qianyue> lst = new ArrayList<Beijing_fangdican_qianyue>();

		Session sn = HibernateUtil.getSessionFactory().openSession();
		Query query = sn.createQuery("from Beijing_fangdican_qianyue as a order by a.riqi desc");
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		List<?> queryList = query.list();
		for (Object obj : queryList) {
			lst.add((Beijing_fangdican_qianyue) obj);
		}
		sn.close();
		return lst;
	}

}
