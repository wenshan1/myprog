package me.wenshan.blog.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import me.wenshan.blog.domain.Post;
import me.wenshan.blog.domain.PostStatus;
import me.wenshan.dao.HibernateUtil;

@Service
public class PostServiceImp implements PostService {

	@Override
	public Post getById(long id) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Post post = sn.get(Post.class, id);
		sn.close();
		return post;
	}

	@Override
	public boolean save(Post post) {
		Session sn = HibernateUtil.getSessionFactory().openSession();
		Transaction  sa = sn.beginTransaction();
		boolean bRet = true;
		try{
		sn.save(post);
		sa.commit();
		}catch (HibernateException  e) {
			sa.rollback();
			bRet = false;
		}finally {
		sn.close();
		}
		return bRet;
	}

	   @Override
	   public boolean update(Post post) {
	        Session sn = HibernateUtil.getSessionFactory().openSession();
	        Transaction  sa = sn.beginTransaction();
	        boolean bRet = true;
	        try{
	        sn.update(post);
	        sa.commit();
	        }catch (HibernateException e) {
	            sa.rollback();
	            bRet = false;
	        }
	        finally {
	        sn.close();
	        }
	        return bRet;
	    }
	   
    @Override
    public long count() {
        Session sn = HibernateUtil.getSessionFactory().openSession();
        long cou = (long) sn.createQuery("select count(u) from Post as u").uniqueResult();
        sn.close();
        return cou;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getPageData(int first, int pageSize) {
        List<Post> lst = null;
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from Post as a where a.pstatus != :psstatus order by a.id desc").
                setInteger("psstatus", PostStatus.get(PostStatus.trash));
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        
        lst =  (List<Post>) query.list();
        sn.close();
        return lst;
    }

    @Override
    public long getTopId() {
        List<Post> lst = null;
        Session sn = HibernateUtil.getSessionFactory().openSession();
        Query query = sn.createQuery("from Post as a order by a.id desc");
        query.setFirstResult(0);
        query.setMaxResults(1);
        lst =  (List<Post>) query.list();
        sn.close();
        return lst.get(0).getId();
    }

	
	
}
