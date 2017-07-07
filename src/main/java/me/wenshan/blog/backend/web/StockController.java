package me.wenshan.blog.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import me.wenshan.biz.OptionManager;
import me.wenshan.constants.StockConstants;
import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.service.IStockDataService;
import me.wenshan.stock.service.IStockService;
import me.wenshan.stock.service.StockInitThread;
import me.wenshan.stock.service.StockModelTongJiService;
import me.wenshan.stockmodel.domain.StockModelData;
import me.wenshan.stockmodel.service.StockModelDataService;
import me.wenshan.stockmodel.service.StockModelManager;
import me.wenshan.util.LBPage;
import me.wenshan.util.ShowHelp;

@Controller
@RequestMapping("/blog/backend/stock")
public class StockController {
    private final String path = "blog/backend/stock";
    private final int pageRecord = 20;
    
    @Autowired
    private IStockService stockService;
    @Autowired
    private StockModelTongJiService tongjiService;
    @Autowired
    private StockModelDataService smdService;
    @Autowired
    private StockModelTongJiService mdlService;

    @Autowired
    private StockModelManager smmManager;
    @Autowired
    private OptionManager opm;
    @Autowired
    private IStockDataService stockDataService;
     
    // 股票指数数据
    @RequestMapping(value = "/stockindex", method = RequestMethod.GET)
    public String stockindex(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {
        if (curPage == null)
            curPage = 1;
        LBPage<StockIndex> page = new LBPage<StockIndex>();
        page.setTotalrecord(stockService.count());
        page.setMaxresult(pageRecord);
        page.setCurrentpage(curPage);
        if (page.getTotalrecord() % page.getMaxresult() == 0)
            page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
        else
            page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

        java.util.List<StockIndex> lst = stockService.getPageData(page.getFirstResult(),
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

        String str = customHelp(StockConstants.MODEL_300_500, curPage, model);
        model.addAttribute("url", "stockm300_500");
        model.addAttribute("modelName", "轮动300-500");

        return str;
    }

    private String customHelp(String modelName, Integer curPage, Model model) {
        if (curPage == null)
            curPage = 1;
        LBPage<StockModelData> page = new LBPage<StockModelData>();
        page.setTotalrecord(smdService.count(modelName));
        page.setMaxresult(pageRecord);
        page.setCurrentpage(curPage);
        if (page.getTotalrecord() % page.getMaxresult() == 0)
            page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
        else
            page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

        java.util.List<StockModelData> lst = smdService.getPageData(modelName, page.getFirstResult(),
                page.getMaxresult());

        page.setRecords(lst);

        model.addAttribute("Page", page);
        model.addAttribute("showhelp", new ShowHelp());

        return path + "/custom";
    }

    // 定制模型数据

    @RequestMapping(value = "/custom", method = RequestMethod.GET)
    public String custom(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {
        if (curPage == null)
            curPage = 1;

        String str = customHelp(StockConstants.MODEL_CUSTOME, curPage, model);
        model.addAttribute("url", "custom");
        model.addAttribute("modelName", "定制模型数据");

        return str;
    }

    // 股票300-创业板模型数据
    @RequestMapping(value = "/stockm300_chuangyeban", method = RequestMethod.GET)
    public String stockm300_chuangyeban(@RequestParam(value = "curPage", required = false) Integer curPage,
            Model model) {
        if (curPage == null)
            curPage = 1;

        String str = customHelp(StockConstants.MODEL_300_CHUANGYEBAN, curPage, model);
        model.addAttribute("url", "stockm300_chuangyeban");
        model.addAttribute("modelName", "轮动牛300-创业板");

        return str;
    }

    // 股票50-创业板模型数据
    @RequestMapping(value = "/stockm50_chuangyeban", method = RequestMethod.GET)
    public String stockm50_chuangyeban(@RequestParam(value = "curPage", required = false) Integer curPage,
            Model model) {

        if (curPage == null)
            curPage = 1;

        String str = customHelp(StockConstants.MODEL_50_CHUANGYEBAN, curPage, model);
        model.addAttribute("url", "stockm50_chuangyeban");
        model.addAttribute("modelName", "50-创业板");

        return str;
    }

    // 创业板指数增强模型数据
    @RequestMapping(value = "/chuangyebanEn", method = RequestMethod.GET)
    public String chuangyebanEn(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {

        if (curPage == null)
            curPage = 1;

        String str = customHelp(StockConstants.MODEL_CHUANGYEBANEXEX, curPage, model);
        model.addAttribute("url", "chuangyebanEn");
        model.addAttribute("modelName", "创业板指数增强 ");

        return str;

    }

    // 初始化股票指数数据

    @RequestMapping(value = "/initstockindex", method = RequestMethod.GET)
    public String initstockindex(Model model) {
        Thread th = new Thread(new StockInitThread(true, false, false, smmManager, mdlService,
        		opm, stockDataService, stockService));
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
        Thread th = new Thread(new StockInitThread(false, true, false, smmManager, mdlService, 
        		opm, stockDataService, stockService));
        th.start();

        return "redirect:/blog/backend/index";
    }

    @RequestMapping(value = "/initmodeldata", method = RequestMethod.GET)
    public String initmodeldata(Model model) {
        Thread th = new Thread(new StockInitThread(false, false, true, smmManager, mdlService, 
        		opm, stockDataService, stockService));
        th.start();

        return "redirect:/blog/backend/index";
    }

    @GetMapping(value = "/modeltongji")
    public String modelTongJi(Model model) {
        // 300-500
        model.addAttribute("model_300_500", tongjiService.getLastData(StockConstants.MODEL_300_500, 5));
        model.addAttribute("tongji300500form", tongjiService.getTongJiForm(StockConstants.MODEL_300_500));

        // 50-创业板
        model.addAttribute("model_50_chuangyeban", tongjiService.getLastData(StockConstants.MODEL_50_CHUANGYEBAN, 5));
        model.addAttribute("model_50_chuangyebanform",
                tongjiService.getTongJiForm(StockConstants.MODEL_50_CHUANGYEBAN));

        // 增强创业板
        model.addAttribute("chuangyebanex", tongjiService.getLastData(StockConstants.MODEL_CHUANGYEBANEXEX, 5));
        model.addAttribute("chuangyebanexform", tongjiService.getTongJiForm(StockConstants.MODEL_CHUANGYEBANEXEX));
        return path + "/modeltongji";
    }

}
