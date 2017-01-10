package me.wenshan.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import me.wenshan.biz.OptionManager;
import me.wenshan.stockmodel.service.StockModelManager;

@Controller
public class RootController {
    @Autowired
    private StockModelManager smmManager;
    @Autowired
    private OptionManager opm;
    
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String root() {
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(String msg, Model model) {
		return "login";
	}
	 
	@RequestMapping(value = "/logout")
	public String logout(String msg, Model model) {
		return "logout";
	}
	
	@RequestMapping(value = "/updatehourly", method = RequestMethod.GET)
	public void updatehourly(@RequestParam(value = "thread", required = false) Boolean useThread,
			HttpServletResponse response) throws IOException {
		if (useThread != null && useThread == true) {
			Thread thread = new Thread(new UpdateHourlyThread(smmManager, opm));
			thread.start();
		} else {
			PrintWriter out = response.getWriter();
			out.println("10ver No Error");
			UpdateHourlyThread.updateHourly(smmManager, opm, out);
		}
		return;
	}
	
}
