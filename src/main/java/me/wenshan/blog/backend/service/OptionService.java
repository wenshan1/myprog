package me.wenshan.blog.backend.service;

import me.wenshan.blog.backend.domain.Option;

public interface OptionService {
	public void save (Option op);
	public long count();
	public String getOptionValue (String name);
	public void updateOptionValue (String name, String value);
}
