package me.wenshan.backend.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import me.wenshan.newsmth.domain.NewsmthData;
import me.wenshan.newsmth.service.NewsmthService;
import me.wenshan.util.LBPage;

@Controller
@RequestMapping("/backend/data")
public class NewsmthController {
	@Autowired
	private NewsmthService nmservice;
	private final String path = "backend/data";
	private final int pageRecord = 5;

	@RequestMapping(value = "/newsmth", method = RequestMethod.GET)
	public String newsmth(@RequestParam(value = "curPage", required = false) Integer curPage, Model model,
	        HttpSession session) {
        if (curPage == null) {
            if (session.getAttribute("newsmthCurPage") == null) {
                curPage = 1;
            }
            else {
                curPage = (Integer) session.getAttribute("newsmthCurPage");
            }
        }
        session.setAttribute("newsmthCurPage", curPage);
        
		LBPage<NewsmthData> page = new LBPage<NewsmthData>();
		page.setTotalrecord(nmservice.count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		List<NewsmthData> lst = nmservice.getPageData(page.getFirstResult(), page.getMaxresult());
		page.setRecords(lst);

		model.addAttribute("Page", page);

		return path + "/newsmth";
	}

	@RequestMapping(value = "/newsmth/{id}/delete", method = RequestMethod.DELETE)
	public String newsmthDelete(@PathVariable Integer id, 
			@RequestParam(value = "curPage", required = false) Integer curPage) {
		if (id == null)
			return "redirect:/" + path + "/newsmth";
		
		nmservice.delete(id);
		
		return "redirect:/" + path + "/newsmth?curPage=" + curPage;
	}
}
