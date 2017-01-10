package me.wenshan.blog.backend.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.wenshan.blog.backend.form.UserFormValidator;
import me.wenshan.userinfo.domain.User;
import me.wenshan.userinfo.service.UserService;
import me.wenshan.util.MapContainer;

@Controller
@RequestMapping("/blog/backend/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public String my(Model model) {
		String str = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("my", userService.getUser(str));
		return "blog/backend/user/my";
	}

	@RequestMapping(value = "my", method = RequestMethod.PUT)
	public String updatemy (@ModelAttribute me.wenshan.userinfo.domain.User user, String repass, Model model) {
	    MapContainer form = UserFormValidator.validateMy(user, repass);
	    if(!form.isEmpty()){
	      model.addAllAttributes(form);
	      model.addAttribute("my", user);
		  return "blog/backend/user/my";
	    }
	    
		String str = SecurityContextHolder.getContext().getAuthentication().getName();
		User upUser = userService.getUser(str);
		upUser.setEmail(user.getEmail());
		upUser.setRealName(user.getRealName());
		upUser.setLastUpdate(new Date());
		if (user.getPassword().length() > 0 ){
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			upUser.setPassword(passwordEncoder.encode (user.getPassword()));
		}
		
	    userService.save(upUser);
		return "redirect:/blog/backend/index";
	}
	  	    
}
