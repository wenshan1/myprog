package me.wenshan.beijing.service;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.wenshan.beijing.domain.Beijing_fangdican_qianyue;
import me.wenshan.beijing.domain.KongQiZhiLiang;

@Service
public class FetchData {
	@Autowired
	private FangDiCanQianYueService fangDiCanQianYueService;
	@Autowired
	private KongQiZhiLiangService kongQiZhiLiangService;
	private static final Logger logger = Logger.getLogger(FetchData.class);

	public void fetchAll_KongQi() {
		logger.info("start fetch beijing data");
		scanBeijingKongQi();
		logger.info("end fetch beijing data");
	}

	public void fetchAll_FandDiCan() {
		logger.info("start fetch beijing data");
		scanBeijingFangDiCan();
		logger.info("end fetch beijing data");
	}

	private boolean scanBeijingKongQi() {
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
		kongQiZhiLiangService.save(kongQi);
		return bGot;
	}

	private boolean scanBeijingFangDiCan() {
		Connection con = Jsoup.connect("http://www.bjjs.gov.cn/bjjs/fwgl/fdcjy/fwjy/index.shtml");
		con.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
		con.timeout(300000);
		Document doc;
		try {
			doc = con.get();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("获得北京房地产数据失败。");
			return false;
		}

		Elements elesTable = doc.getElementsByTag("table");
		// System.out.println(elesTable.html());

		Elements eles = elesTable;

		// 期房网上签约

		Element ele = eles.get(6);
		eles = ele.children(); // the table children
		eles = eles.get(0).children();
		String str = eles.get(0).text();
		String riqi = str.replaceAll("期房网上签约", "");

		String strQifang = eles.get(1).children().get(1).text(); // 总签约套数
		String strQifangZhuzai = eles.get(3).children().get(1).text(); // 住宅

		// 现房网上签约
		eles = elesTable;
		ele = eles.get(10);
		eles = ele.children(); // the table children
		eles = eles.get(0).children();
		String strXianfang = eles.get(1).children().get(1).text();
		String strXianfangZhuzai = eles.get(3).children().get(1).text(); // 住宅

		// 存量房网上签约
		eles = elesTable;
		ele = eles.get(13);
		eles = ele.children(); // the table children
		eles = eles.get(0).children();
		eles = eles.get(0).children();
		eles = eles.get(3).children();
		eles = eles.get(0).children();
		ele = eles.get(0);
		String strChuanlian = ele.children().get(1).children().get(1).text();
		String strChuanlianZhuzai = ele.children().get(3).children().get(1).text(); // 住宅

		// save data

		Beijing_fangdican_qianyue beijing = new Beijing_fangdican_qianyue();

		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		try {
			beijing.setRiqi(formater.parse(riqi));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		beijing.setQifang_zong(Integer.valueOf(strQifang));
		beijing.setQifang_zhuzhai(Integer.valueOf(strQifangZhuzai));

		beijing.setXianfang_zhuzhai(Integer.valueOf(strXianfangZhuzai));
		beijing.setXianfang_zong(Integer.valueOf(strXianfang));

		beijing.setCunliangfang_zhuzhai(Integer.valueOf(strChuanlianZhuzai));
		beijing.setCunliangfang_zong(Integer.valueOf(strChuanlian));
		fangDiCanQianYueService.saveOrUpdate(beijing);

		return true;
	}

}
