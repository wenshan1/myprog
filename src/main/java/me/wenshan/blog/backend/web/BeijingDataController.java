package me.wenshan.blog.backend.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import me.wenshan.beijing.domain.Beijing_fangdican_qianyue;
import me.wenshan.beijing.domain.KongQiZhiLiang;
import me.wenshan.beijing.service.FangDiCanQianYueService;
import me.wenshan.beijing.service.KongQiZhiLiangService;
import me.wenshan.util.LBPage;

@Controller
@RequestMapping("/blog/backend/data")
public class BeijingDataController {
	private final String path = "blog/backend/data";
	private final int pageRecord = 20;
	
	// 房地产数据
	@RequestMapping(value = "/fangdicanshuju", method = RequestMethod.GET)
	public String fangdicanshuju(@RequestParam(value = "curPage", required = false) Integer curPage, Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<Beijing_fangdican_qianyue> page = new LBPage<Beijing_fangdican_qianyue>();
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

	// 北京空气质量数据
	@RequestMapping(value = "/beijing_kongqizhiliang", method = RequestMethod.GET)
	public String beijing_kongqizhiliang(@RequestParam(value = "curPage", required = false) Integer curPage,
			Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<KongQiZhiLiang> page = new LBPage<KongQiZhiLiang>();
		page.setTotalrecord(KongQiZhiLiangService.getInstance().count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<KongQiZhiLiang> lst = KongQiZhiLiangService.getInstance().getPageData(page.getFirstResult(),
				page.getMaxresult());
		page.setRecords(lst);

		model.addAttribute("Page", page);

		return path + "/beijing_kongqizhiliang";
	}
}
