package me.wenshan.backend.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.wenshan.beijing.service.FangDiCanQianYueService;
import me.wenshan.stock.service.IStockDataService;
import me.wenshan.stock.service.IStockListService;
import me.wenshan.stock.service.IStockService;
import me.wenshan.stock.service.StockBasicsService;
import me.wenshan.stock.service.StockDayHisService;
import me.wenshan.userinfo.service.UserService;
import me.wenshan.util.OSInfo;

@Controller
@RequestMapping("/backend")
public class BackendController {
	@Autowired
	private UserService userService;
	@Autowired
	private IStockListService stockListService;
	@Autowired
	private FangDiCanQianYueService fangDiCanQianYueService;
	@Autowired
	private IStockDataService stockDataService;
	@Autowired 
	private IStockService stockService;
	@Autowired
	private StockDayHisService stockDayHistService;
	@Autowired
	private StockBasicsService  stockBasicServer;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest req) {
				
		String str = SecurityContextHolder.getContext().getAuthentication().getName();
		
		OSInfo osinfo = new OSInfo (req);
		model.addAttribute("osInfo", osinfo);
		model.addAttribute("authUser", str);
		
		/* 基本站点统计信息 */
		
		model.addAttribute("userCount", userService.count());
		
		// 数据信息
		model.addAttribute("StockIndex_Count", stockService.count());
		model.addAttribute("Beijing_fangdican_qianyue_Count", fangDiCanQianYueService.count());
		model.addAttribute("stockData_Count", stockDataService.count ());
		model.addAttribute("stockList_Count", stockListService.count());
		model.addAttribute("stockDayHis_Count", stockDayHistService.count());
		model.addAttribute("stockBasic_Count", stockBasicServer.count());

		/*
		 * model.addAttribute("postCount", postService.count());
		 * model.addAttribute("commentCount", commentService.count());
		 * model.addAttribute("uploadCount", uploadService.count());
		 * 
		 * model.addAttribute("posts", postManager.listRecent(10,
		 * PostConstants.POST_CREATOR_ALL)); model.addAttribute("comments",
		 * commentManager.listRecent());
		 */
		return "backend/index";
	}
}
