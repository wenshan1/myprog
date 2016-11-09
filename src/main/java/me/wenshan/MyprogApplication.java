package me.wenshan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import me.wenshan.dao.HibernateUtil;
import me.wenshan.userinfo.domain.User;
import me.wenshan.userinfo.service.UserServiceImp;

@SpringBootApplication
@EnableScheduling
public class MyprogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyprogApplication.class, args);
		
    	HibernateUtil.getSessionFactory();
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	
    	if (UserServiceImp.get().count() == 0){
    		User user = new User("admin", passwordEncoder.encode("stock"), 0);
    		UserServiceImp.get().save(user);
    	}
	}
}
