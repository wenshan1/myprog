package me.wenshan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import me.wenshan.backend.domain.Option;
import me.wenshan.backend.service.OptionService;
import me.wenshan.backend.service.OptionServiceImp;
import me.wenshan.constants.UserConstants;
import me.wenshan.userinfo.domain.User;
import me.wenshan.userinfo.service.UserServiceImp;
import me.wenshan.util.Global;

@SpringBootApplication
@EnableScheduling
public class MyprogApplication { 

	public static void main(String[] args) {
		SpringApplication.run(MyprogApplication.class, args);
		
		initData ();
	}
	
	private static void initData () {
		OptionService ops = new OptionServiceImp ();
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (UserServiceImp.get().count() == 0) {
			User user = new User("admin", passwordEncoder.encode("stock"), 
					UserConstants.USER_STATUS_NORMAL, UserConstants.USER_ROLE_ADMIN);
			UserServiceImp.get().save(user);
		}
		
		// init option
		
		if (ops.count() == 0) {
			ops.save(new Option ("title", "文山"));
			ops.save(new Option ("subtitle", "记录生活"));
			ops.save(new Option ("description", "描述"));
			ops.save(new Option ("allowComment", "true"));
			ops.save(new Option ("maxshow", "10"));
		}
		
		Global.getDataFromDB(ops);
	}
	
}
