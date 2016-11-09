package me.wenshan.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final String path = "admin";
	
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public ModelAndView admin() 
    {
	ModelAndView mv = new ModelAndView(path + "/index");
	return mv;
    }
	
	@RequestMapping(value = "/fangdicanshuju.html", method = RequestMethod.GET)
	public ModelAndView fangdicanshuju() 
    {
	ModelAndView mv = new ModelAndView(path + "/fangdicanshuju");
	return mv;
    }
}
