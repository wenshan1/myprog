package me.wenshan.backend.service;

import me.wenshan.backend.domain.Option;

public interface OptionService {
	public void save (Option op);
	public long count();
	public String getOptionValue (String name);
	public void updateOptionValue (String name, String value);
}
