package me.wenshan.userinfo.service;

import me.wenshan.userinfo.domain.User;

public interface UserService {
	void save(User user);
	User loadById (String id);
	User getUser(String user);
	long count();
}
