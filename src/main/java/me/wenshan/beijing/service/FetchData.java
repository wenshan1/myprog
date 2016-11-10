package me.wenshan.beijing.service;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import me.wenshan.beijing.domain.Beijing_fangdican_qianyue;
import me.wenshan.beijing.domain.KongQiZhiLiang;

@Component
public class FetchData {
	private static final Logger logger = Logger.getLogger(FetchData.class);

	@Scheduled(cron = "0 0/20 * * * ?")
	public static void fetchAll_Sc() {
		logger.info("start fetch beijing data");
		fetchAll();
		logger.info("end fetch beijing data");
	}

	public static boolean fetchAll() {
		return scanBeijingKongQi() && scanBeijingFangDiCan();
	}

	private static boolean scanBeijingKongQi() {
		KongQiZhiLiangService kongqiservice = KongQiZhiLiangService.getInstance();

		String strZhiLiang = "获得数据失败";
		Connection con = Jsoup.connect(
				"http://www.baidu.com/s?wd=%E5%8C%97%E4%BA%AC%E7%A9%BA%E6%B0%94%E8%B4%A8%E9%87%8F%E6%8C%87%E6%95%B0");
		con.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
		con.timeout(300000);
		Document doc;
		try {
			doc = con.get();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("获得北京空气质量数据失败。");
			return false;
		}
		KongQiZhiLiang kongQi = new KongQiZhiLiang();
		Elements zhiLiangs;

		boolean bGot = false;
		zhiLiangs = doc.getElementsByClass("op_pm25_graexp");
		if (zhiLiangs != null) {
			Element zhiLiang = zhiLiangs.first();
			if (zhiLiang != null) {
				strZhiLiang = zhiLiang.text();
				kongQi.setZhiLiang(Integer.valueOf(strZhiLiang));
				bGot = true;
			}
		}

		if (bGot) {
			kongqiservice.saveOrUpdate(kongQi);
		} else
			kongQi = null;
		return bGot;
	}

	private static boolean scanBeijingFangDiCan() {
		FangDiCanQianYueService fangcanservice = FangDiCanQianYueService.getInstance();

		Connection con = Jsoup.connect("http://www.bjjs.gov.cn/tabid/2167/Default.aspx");
		con.timeout(300000);
		Document doc;
		try {
			doc = con.get();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("获得北京房地产数据失败。");
			return false;
		}

		Beijing_fangdican_qianyue beijing = new Beijing_fangdican_qianyue();

		// 日期
		Element riqiEle = doc.getElementById("ess_ctr5115_FDCJY_HouseTransactionStatist_timeMark2");
		
		if (riqiEle == null)
		{
			return false;
		}
		String str = riqiEle.text();
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		try {
			beijing.setRiqi(formater.parse(str));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		// 存量房总
		Element cunliangfang_zongEle = doc.getElementById("ess_ctr5112_FDCJY_SignOnlineStatistics_totalCount4");
		str = cunliangfang_zongEle.text();
		beijing.setCunliangfang_zong(Integer.valueOf(str));

		// 存量房总
		Element cunliangfang_zhuzhaiEle = doc.getElementById("ess_ctr5112_FDCJY_SignOnlineStatistics_residenceCount4");
		str = cunliangfang_zhuzhaiEle.text();
		beijing.setCunliangfang_zhuzhai(Integer.valueOf(str));

		// 现房 总
		Element xianfang_zongEle = doc.getElementById("ess_ctr5115_FDCJY_HouseTransactionStatist_totalCount8");
		str = xianfang_zongEle.text();
		beijing.setXianfang_zong(Integer.valueOf(str));

		// 现房住宅
		Element xianfang_zhuzhaiEle = doc.getElementById("ess_ctr5115_FDCJY_HouseTransactionStatist_residenceCount8");
		str = xianfang_zhuzhaiEle.text();
		beijing.setXianfang_zhuzhai(Integer.valueOf(str));

		// 期房
		Element qifang_zongEle = doc.getElementById("ess_ctr5115_FDCJY_HouseTransactionStatist_totalCount4");
		str = qifang_zongEle.text();
		beijing.setQifang_zong(Integer.valueOf(str));

		// 期房住宅
		Element qifang_zhuzhaiEle = doc.getElementById("ess_ctr5115_FDCJY_HouseTransactionStatist_residenceCount4");
		str = qifang_zhuzhaiEle.text();
		beijing.setQifang_zhuzhai(Integer.valueOf(str));

		fangcanservice.saveOrUpdate(beijing);

		beijing = null;

		return true;
	}

}
