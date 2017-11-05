package me.wenshan.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.wenshan.beijing.service.FetchData;
import me.wenshan.biz.OptionManager;
import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.service.IStockService;
import me.wenshan.stockmodel.service.StockModelManager;

@Controller
public class RootController {
    @Autowired
    private StockModelManager smmManager;
    @Autowired
    private OptionManager opm;
    @Autowired
    private FetchData fetchData;

    @Autowired
    private IStockService stockService;
    
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

    @RequestMapping(value = "/updateminute", method = RequestMethod.GET)
    public void updatehourly(@RequestParam(value = "thread", required = false) Boolean useThread,
            HttpServletResponse response) throws IOException {
        if (useThread != null && useThread == true) {
            Thread thread = new Thread(new UpdateHourlyThread(smmManager, opm, fetchData));
            thread.start();
        } else {
            PrintWriter out = response.getWriter();
            out.println("10ver No Error");
            UpdateHourlyThread.updateMinute(smmManager, opm, fetchData, out);
        }
        return;
    }

    @RequestMapping(value = "/post/stockindex", method = RequestMethod.POST)
    @ResponseBody
    public String postStockIndex(@RequestBody StockIndexM sim) {
        if (sim.getToken().compareTo("698544885afeeggafdfadafafdiekee") == 0) {
        	
        	for (int i = 0; i < sim.getData().size(); i ++) {
        		StockIndex sti = sim.getData().get(i);
        		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        		String str = formater.format(sti.getRiqi());
        		try {
					sti.getPk().setRiqi(formater.parse(str));
				} catch (ParseException e) {
					e.printStackTrace();
				}        		
        	}
        	stockService.saveAll(sim.getData(), false);
            return "OK";
        }
        else {
            return "token error";
        }
    }
}

class StockIndexM {
    private String token;
    private List<StockIndex> data;

    public StockIndexM() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<StockIndex> getData() {
        return data;
    }

    public void setData(List<StockIndex> data) {
        this.data = data;
    }

}
