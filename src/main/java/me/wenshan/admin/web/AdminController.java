package me.wenshan.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import me.wenshan.stock.domain.StockGEMData;
import me.wenshan.stock.domain.StockGEMEXData;
import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.domain.StockM20Data;
import me.wenshan.stock.service.StockInitThread;
import me.wenshan.stock.service.StockServiceImp;
import me.wenshan.beijing.service.KongQiZhiLiangService;
import me.wenshan.stock.service.StockGEMEXDataServiceImp;
import me.wenshan.stock.domain.Stock50GEMData;
import me.wenshan.stock.service.Stock50GEMDataServiceImp;
import me.wenshan.beijing.domain.Beijing_fangdican_qianyue;
import me.wenshan.beijing.domain.KongQiZhiLiang;
import me.wenshan.util.Page;
import me.wenshan.beijing.service.FangDiCanQianYueService;
import me.wenshan.newsmth.service.NewsmthServiceImp;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final String path = "admin";
	private final int    pageRecord = 15;

	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String admin(Model model) {
		model.addAttribute("Newsmth_Count", NewsmthServiceImp.getInstance().count());
		model.addAttribute("Newsmth_Photo_Count", NewsmthServiceImp.getInstance().countPhoto());
		model.addAttribute("StockIndex_Count", StockServiceImp.getInstance().count());
		model.addAttribute("KongQiZhiLiang_Count", KongQiZhiLiangService.getInstance().count());
		model.addAttribute("Beijing_fangdican_qianyue_Count", FangDiCanQianYueService.getInstance().count());

		return path + "/index";
	}

	//房地产数据
	@RequestMapping(value = "/fangdicanshuju.html", method = RequestMethod.GET)
	public String fangdicanshuju(@RequestParam("curPage") int curPage, Model model) {
		Page<Beijing_fangdican_qianyue> page = new Page<Beijing_fangdican_qianyue>();
		page.setTotalrecord(FangDiCanQianYueService.getInstance().count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<Beijing_fangdican_qianyue> lst = FangDiCanQianYueService.getInstance()
				.getPageData(page.getFirstResult(), page.getMaxresult());
		page.setRecords(lst);
		
		model.addAttribute("Page", page);
		
		return path + "/fangdicanshuju";
	}
	
	//北京空气质量数据
	@RequestMapping(value = "/beijing_kongqizhiliang.html", method = RequestMethod.GET)
	public String beijing_kongqizhiliang (@RequestParam("curPage") int curPage, Model model) {
		
		Page<KongQiZhiLiang> page = new Page<KongQiZhiLiang>();
		page.setTotalrecord(KongQiZhiLiangService.getInstance().count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<KongQiZhiLiang> lst = KongQiZhiLiangService.getInstance()
				.getPageData(page.getFirstResult(), page.getMaxresult());
		page.setRecords(lst);
		
		model.addAttribute("Page", page);
		
		return path + "/beijing_kongqizhiliang";
	}
	
	// 股票指数数据
	@RequestMapping(value = "/stockindex.html", method = RequestMethod.GET)
	public String stockindex (@RequestParam("curPage") int curPage, Model model) {
		
		Page<StockIndex> page = new Page<StockIndex>();
		page.setTotalrecord(StockServiceImp.getInstance().count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<StockIndex> lst = StockServiceImp.getInstance()
				.getPageData(page.getFirstResult(), page.getMaxresult());
		page.setRecords(lst);
		
		model.addAttribute("Page", page);
		
		return path + "/stockindex";
	}
	
	// 股票300-500模型数据
	@RequestMapping(value = "/stockm300_500.html", method = RequestMethod.GET)
	public String stockm300_500 (@RequestParam("curPage") int curPage, Model model) {
		
		Page<StockM20Data> page = new Page<StockM20Data>();
		page.setTotalrecord(StockServiceImp.getInstance().M20DataCount());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<StockM20Data> lst = StockServiceImp.getInstance().getM20DataPageData(page.getFirstResult(),
                page.getMaxresult());
		
		page.setRecords(lst);
		
		model.addAttribute("Page", page);
		
		return path + "/stockm300_500";
	}
	
	// 股票300-创业板模型数据
	@RequestMapping(value = "/stockm300_chuangyeban.html", method = RequestMethod.GET)
	public String stockm300_chuangyeban (@RequestParam("curPage") int curPage, Model model) {
		
		Page<StockGEMData> page = new Page<StockGEMData>();
		page.setTotalrecord(StockServiceImp.getInstance().GEMDataCount());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<StockGEMData> lst = StockServiceImp.getInstance().getGEMDataPageData(page.getFirstResult(),
                page.getMaxresult());
		
		page.setRecords(lst);
		
		model.addAttribute("Page", page);
		
		return path + "/stockm300_chuangyeban";
	}
	
	// 股票50-创业板模型数据
	@RequestMapping(value = "/stockm50_chuangyeban.html", method = RequestMethod.GET)
	public String stockm50_chuangyeban (@RequestParam("curPage") int curPage, Model model) {
		
		Page<Stock50GEMData> page = new Page<Stock50GEMData>();
		page.setTotalrecord(Stock50GEMDataServiceImp.getInstance().Count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

        java.util.List<Stock50GEMData> lst = Stock50GEMDataServiceImp.getInstance().getPageData(page.getFirstResult(),
                page.getMaxresult());
		page.setRecords(lst);
		
		model.addAttribute("Page", page);
		
		return path + "/stockm50_chuangyeban";
	}
	
	// 创业板指数增强模型数据
	@RequestMapping(value = "/chuangyebanEn.html", method = RequestMethod.GET)
	public String chuangyebanEn (@RequestParam("curPage") int curPage, Model model) {
		
		Page<StockGEMEXData> page = new Page<StockGEMEXData>();
		page.setTotalrecord(StockGEMEXDataServiceImp.getInstance().Count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

        java.util.List<StockGEMEXData> lst = StockGEMEXDataServiceImp.getInstance().getPageData(page.getFirstResult(),
                page.getMaxresult());
		page.setRecords(lst);
		
		model.addAttribute("Page", page);
		
		return path + "/chuangyebanEn";
	}
	
	//初始化股票所有数据
	
	@RequestMapping(value = "/initstock.html", method = RequestMethod.GET)
	public String initStock (Model model) {
    	Thread th = new Thread (new StockInitThread () );
    	th.start();
    	
		return admin (model);
	}
	
}

