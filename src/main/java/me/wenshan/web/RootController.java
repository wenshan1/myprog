package me.wenshan.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RootController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	
	public ModelAndView root () 
    {
	ModelAndView mv = new ModelAndView("index");
	return mv;
    }

}
