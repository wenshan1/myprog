package me.wenshan.blog.backend.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.wenshan.beijing.service.FangDiCanQianYueService;
import me.wenshan.beijing.service.KongQiZhiLiangService;
import me.wenshan.newsmth.service.NewsmthServiceImp;
import me.wenshan.stock.service.IStockListService;
import me.wenshan.stock.service.StockDataServiceImp;
import me.wenshan.stock.service.StockServiceImp;
import me.wenshan.userinfo.service.UserService;
import me.wenshan.util.OSInfo;

@Controller
@RequestMapping("/blog/backend")
public class BackendController {
	@Autowired
	private UserService userService;
	@Autowired
	private IStockListService stockListService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest req) {
				
		String str = SecurityContextHolder.getContext().getAuthentication().getName();
		
		OSInfo osinfo = new OSInfo (req);
		model.addAttribute("osInfo", osinfo);
		model.addAttribute("authUser", str);
		
		/* 基本站点统计信息 */
		
		model.addAttribute("userCount", userService.count());
		
		// 数据信息
		
		model.addAttribute("Newsmth_Count", NewsmthServiceImp.getInstance().count());
		model.addAttribute("Newsmth_Photo_Count", NewsmthServiceImp.getInstance().countPhoto());
		model.addAttribute("StockIndex_Count", StockServiceImp.getInstance().count());
		model.addAttribute("KongQiZhiLiang_Count", KongQiZhiLiangService.getInstance().count());
		model.addAttribute("Beijing_fangdican_qianyue_Count", FangDiCanQianYueService.getInstance().count());
		model.addAttribute("stockData_Count", StockDataServiceImp.get().count ());
		model.addAttribute("stockList_Count", stockListService.count());

		/*
		 * model.addAttribute("postCount", postService.count());
		 * model.addAttribute("commentCount", commentService.count());
		 * model.addAttribute("uploadCount", uploadService.count());
		 * 
		 * model.addAttribute("posts", postManager.listRecent(10,
		 * PostConstants.POST_CREATOR_ALL)); model.addAttribute("comments",
		 * commentManager.listRecent());
		 */
		return "blog/backend/index";
	}
}