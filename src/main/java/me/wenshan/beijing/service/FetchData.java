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

@Service
public class FetchData {
	@Autowired
	private FangDiCanQianYueService fangDiCanQianYueService;
	private static final Logger logger = Logger.getLogger(FetchData.class);

	public void fetchAll_FandDiCan() {
		logger.info("start fetch beijing data");
		scanBeijingFangDiCan();
		logger.info("end fetch beijing data");
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
			riqi.replace('/', '-');
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
