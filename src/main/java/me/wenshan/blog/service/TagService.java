package me.wenshan.blog.service;

import me.wenshan.blog.domain.Tag;

public interface TagService {
	boolean exists (String tagName);
	Tag save(Tag tag) ;
	public Tag getOne(String tagName);

}
