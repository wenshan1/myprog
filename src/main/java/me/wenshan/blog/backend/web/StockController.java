package me.wenshan.blog.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import me.wenshan.constants.StockConstants;
import me.wenshan.stock.domain.Stock50GEMData;
import me.wenshan.stock.domain.StockGEMData;
import me.wenshan.stock.domain.StockGEMEXData;
import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.domain.StockM20Data;
import me.wenshan.stock.mtw.StockModelTongJiMgr;
import me.wenshan.stock.service.Stock50GEMDataServiceImp;
import me.wenshan.stock.service.StockGEMEXDataServiceImp;
import me.wenshan.stock.service.StockInitThread;
import me.wenshan.stock.service.StockModelTongJiService;
import me.wenshan.stock.service.StockServiceImp;
import me.wenshan.stockmodel.domain.StockModelData;
import me.wenshan.stockmodel.service.StockModelDataService;
import me.wenshan.util.LBPage;
import me.wenshan.util.ShowHelp;

@Controller
@RequestMapping("/blog/backend/stock")
public class StockController {
	private final String path = "blog/backend/stock";
	private final int pageRecord = 20;
	
	@Autowired
	private StockModelTongJiService tongjiService;
	@Autowired
	private StockModelDataService  smdService;

	// 股票指数数据
	@RequestMapping(value = "/stockindex", method = RequestMethod.GET)
	public String stockindex(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<StockIndex> page = new LBPage<StockIndex>();
		page.setTotalrecord(StockServiceImp.getInstance().count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<StockIndex> lst = StockServiceImp.getInstance().getPageData(page.getFirstResult(),
				page.getMaxresult());
		page.setRecords(lst);

		model.addAttribute("Page", page);

		return path + "/stockindex";
	}

	// 股票300-500模型数据
	@RequestMapping(value = "/stockm300_500", method = RequestMethod.GET)
	public String stockm300_500(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<StockM20Data> page = new LBPage<StockM20Data>();
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

	   // 股票300-500模型数据
    @RequestMapping(value = "/custom", method = RequestMethod.GET)
    public String custom(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {
        if (curPage == null)
            curPage = 1;
        LBPage<StockModelData> page = new LBPage<StockModelData>();
        page.setTotalrecord(smdService.count(StockConstants.MODEL_CUSTOME));
        page.setMaxresult(pageRecord);
        page.setCurrentpage(curPage);
        if (page.getTotalrecord() % page.getMaxresult() == 0)
            page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
        else
            page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

        java.util.List<StockModelData> lst = smdService.getPageData(StockConstants.MODEL_CUSTOME,page.getFirstResult(),
                page.getMaxresult());

        page.setRecords(lst);

        model.addAttribute("Page", page);
        model.addAttribute("showhelp", new ShowHelp ());

        return path + "/custom";
    }
    
	// 股票300-创业板模型数据
	@RequestMapping(value = "/stockm300_chuangyeban", method = RequestMethod.GET)
	public String stockm300_chuangyeban(@RequestParam(value = "curPage", required = false) Integer curPage,
			Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<StockGEMData> page = new LBPage<StockGEMData>();
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
	@RequestMapping(value = "/stockm50_chuangyeban", method = RequestMethod.GET)
	public String stockm50_chuangyeban(@RequestParam(value = "curPage", required = false) Integer curPage,
			Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<Stock50GEMData> page = new LBPage<Stock50GEMData>();
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
	@RequestMapping(value = "/chuangyebanEn", method = RequestMethod.GET)
	public String chuangyebanEn(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<StockGEMEXData> page = new LBPage<StockGEMEXData>();
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

	// 初始化股票指数数据和模型相关数据
	
	@RequestMapping(value = "/initstock", method = RequestMethod.GET)
	public String initStock(Model model) {
		Thread th = new Thread(new StockInitThread(true));
		th.start();

		return "redirect:/blog/backend/index";
	}

	@RequestMapping(value = "/initstockdesc", method = RequestMethod.GET)
	public String initstockdesc(Model model) {
		return path + "/initstockdesc";
	}
	
	// 初始化具体股票数据（比如：中国平安sh601318)和模型相关数据
	
	@RequestMapping(value = "/initstockdata", method = RequestMethod.GET)
	public String initstockdata(Model model) {
		Thread th = new Thread(new StockInitThread(false));
		th.start();
		
		return "redirect:/blog/backend/index";
	}
	
	@GetMapping (value = "/modeltongji")
	public String modelTongJi (Model model) {
		// 300-500
		model.addAttribute("model_300_500", 
				tongjiService.getLastData(StockConstants.MODEL_300_500, 5));
		model.addAttribute("tongji300500form", 
				tongjiService.getTongJiForm(StockConstants.MODEL_300_500));
		
		// 50-创业板
		model.addAttribute("model_50_chuangyeban", 
				tongjiService.getLastData(StockConstants.MODEL_50_CHUANGYEBAN, 5));
		model.addAttribute("model_50_chuangyebanform", 
				tongjiService.getTongJiForm(StockConstants.MODEL_50_CHUANGYEBAN));
		
		// 增强创业板
		model.addAttribute("chuangyebanex", 
				tongjiService.getLastData(StockConstants.MODEL_CHUANGYEBANEXEX, 5));
		model.addAttribute("chuangyebanexform", 
				tongjiService.getTongJiForm(StockConstants.MODEL_CHUANGYEBANEXEX));
		return path + "/modeltongji";	
	}

}
