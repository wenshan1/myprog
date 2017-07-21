package me.wenshan.backend.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.wenshan.beijing.domain.Beijing_fangdican_qianyue;
import me.wenshan.beijing.domain.KongQiZhiLiang;
import me.wenshan.beijing.service.FangDiCanQianYueService;
import me.wenshan.beijing.service.KongQiZhiLiangService;
import me.wenshan.util.LBPage;

@Controller
@RequestMapping("/backend/data")
public class BeijingDataController {
	@Autowired
	private FangDiCanQianYueService fangDiCanQianYueService;
	@Autowired
	private KongQiZhiLiangService kongQiZhiLiangService;
	
	private final String path = "backend/data";
	private final int pageRecord = 20;

	//导出房地产数据
	
	@GetMapping ("/exportfangdicanshuju")
	public void exportfangdicanshuju (HttpServletResponse response) throws IOException {
		String filename= new String("北京房地产数据.csv".getBytes(),"iso-8859-1");//中文文件名必须使用此句话
		response.setContentType("application/octet-stream");
        response.setContentType("application/OCTET-STREAM;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+filename );
        List<Beijing_fangdican_qianyue> lst = fangDiCanQianYueService.getAllData();
    	
        PrintWriter out = response.getWriter();
        out.append("时间");
        out.append(",");
        out.append("存量房普通住宅");
        out.append(",");
        out.append("存量房总");
        out.append(",");
        out.append("期房普通住宅");
        out.append(",");
        out.append("期房总");
        out.append(",");
        out.append("现房普通住宅");
        out.append(",");
        out.append("现房总");
        out.append("\n");
        for (Beijing_fangdican_qianyue it : lst) {
        	SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        	String str = formater.format(it.getRiqi());
        	out.append(str);
            out.append(",");
            out.append(new Integer (it.getCunliangfang_zhuzhai()).toString());
            out.append(",");
            out.append(new Integer (it.getCunliangfang_zong()).toString());
            out.append(",");
            out.append(new Integer (it.getQifang_zhuzhai()).toString());
            out.append(",");
            out.append(new Integer (it.getQifang_zong()).toString());
            out.append(",");
            out.append(new Integer (it.getXianfang_zhuzhai()).toString());
            out.append(",");
            out.append(new Integer (it.getXianfang_zong()).toString());
            out.append("\n");
            
        }
        out.flush();
        out.close();
		return ;
		
	}
	
	@GetMapping ("/fangdicanshuju.json")
    public @ResponseBody List<Beijing_fangdican_qianyue>  fangdicanshujuJson() {
        List<Beijing_fangdican_qianyue> lst = fangDiCanQianYueService.getAllData();
        return lst;
    }
    
    @GetMapping ("/fangdicanshuju-qeushi")
    public String   fangdicanshujuQueshi() {
        return path + "/fangdicanshuju-queshi";
    }
    
	// 房地产数据
	@RequestMapping(value = "/fangdicanshuju", method = RequestMethod.GET)
	public String fangdicanshuju(@RequestParam(value = "curPage", required = false) Integer curPage,
			Model model) {
		if (curPage == null)
			curPage = 1;
		LBPage<Beijing_fangdican_qianyue> page = new LBPage<Beijing_fangdican_qianyue>();
		page.setTotalrecord(fangDiCanQianYueService.count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<Beijing_fangdican_qianyue> lst = fangDiCanQianYueService.getPageData(page.getFirstResult(),
				page.getMaxresult());
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
		page.setTotalrecord(kongQiZhiLiangService.count());
		page.setMaxresult(pageRecord);
		page.setCurrentpage(curPage);
		if (page.getTotalrecord() % page.getMaxresult() == 0)
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
		else
			page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);

		java.util.List<KongQiZhiLiang> lst = kongQiZhiLiangService.getPageData(page.getFirstResult(),
				page.getMaxresult());
		page.setRecords(lst);

		model.addAttribute("Page", page);

		return path + "/beijing_kongqizhiliang";
	}
}
