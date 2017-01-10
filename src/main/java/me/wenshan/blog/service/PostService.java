package me.wenshan.blog.service;

import java.util.List;

import me.wenshan.blog.domain.Post;

public interface PostService {
	Post getById (long id);
	boolean save(Post post);
	long count ();
	public List<Post> getPageData(int first, int pageSize) ;
	long getTopId ();
	boolean update(Post post);
}
