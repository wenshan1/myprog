package me.wenshan.newsmth.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import me.wenshan.newsmth.domain.Newsmth;
import me.wenshan.newsmth.domain.NewsmthData;
import me.wenshan.newsmth.domain.Photo;

@Component
public class NewsmthFetchData {

	private static final Logger logger = Logger.getLogger(NewsmthFetchData.class);
	
	private static NewsmthService nmservice = NewsmthServiceImp.getInstance();

	@Scheduled(cron = "0 0/20 * * * ?")
	public static void fetchAll_sc () {
		logger.info("start fetch www.newsmth.net data");
		fetchAll();
		logger.info("end fetch www.newsmth.net data");
	}
	
	public static boolean fetchAll() {
		boolean bRet;
		bRet = fetchNewsmth("http://www.newsmth.net/nForum/rss/topten", "topten");
		if (!bRet)
			return bRet;

		bRet = fetchNewsmth("http://www.newsmth.net/nForum/rss/board-FamilyLife", "FamilyLife");
		if (!bRet)
			return bRet;

		bRet = fetchNewsmth("http://www.newsmth.net/nForum/rss/board-WorkLife", "WorkLife");
		if (!bRet)
			return bRet;

		bRet = fetchNewsmth("http://www.newsmth.net/nForum/rss/board-NewExpress", "NewExpress");
		if (!bRet)
			return bRet;

		bRet = fetchNewsmth("http://www.newsmth.net/nForum/rss/board-MyPhoto", "MyPhoto");
		if (!bRet)
			return bRet;

		bRet = fetchNewsmth("http://www.newsmth.net/nForum/rss/board-Divorce", "Divorce");
		if (!bRet)
			return bRet;
		
		return bRet;
	}

	/* private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static byte[] toPrimitives(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];

		for (int i = 0; i < oBytes.length; i++) {
			bytes[i] = oBytes[i];
		}

		return bytes;
	} */

	private static void getPhoto(String str, NewsmthData nmdata) {
		Photo photo = new Photo(nmdata.getNewsmth().getLink());
		int num = 0;
		Document doc = Jsoup.parseBodyFragment(str);
		Elements media = doc.select("[src]");
		for (Element src : media) {
			if (src.tagName().equals("img") && src.attr("abs:src") != null && src.attr("abs:src").length() > 0) {
				//print(" * %s: <%s> %sx%s (%s)", src.tagName(), src.attr("abs:src"), src.attr("width"),
				//		src.attr("height"), trim(src.attr("alt"), 20));

				URL url;
				InputStream in = null;

				try {
					CookieManager ckm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
					// First set the default cookie manager.
					CookieHandler.setDefault(ckm);

					// All the following subsequent URLConnections will use the
					// same cookie manager.

					url = new URL(src.attr("abs:src"));

					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
					connection.setRequestMethod("GET");
					connection.setReadTimeout(6 * 10000);

					if (connection.getResponseCode() == 200) {
						in = connection.getInputStream();
						byte[] buf = new byte[1024];
						ArrayList<Byte> list = new ArrayList<Byte>();
						int count;
						while ((count = in.read(buf)) != -1) {
							for (int i = 0; i < count; i++)
								list.add(buf[i]);
						}

						byte[] bytes = new byte[list.size()];
						int i;
						for (i = 0; i < list.size(); i++) {
							bytes[i] = list.get(i);
						}
						/* not empty */
						if (i > 10) {
							photo.setPhoto(new SerialBlob(bytes), num);
							num++;
						}

					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SerialException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (in != null)
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}

			}
		}
		if (photo.getPhoto(0) != null)
			nmdata.setPhoto(photo);

	}

	private static boolean fetchNewsmth(String rss, String boardName) {
		boolean bRet = true;
		NewsmthData nmdata = new NewsmthData();
		Newsmth nm = new Newsmth();
		nmdata.setNewsmth(nm);
		
		try {
			URL url = new URL(rss);
			// 读取Rss源

			XmlReader reader = new XmlReader(url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(reader);

			List<SyndEntry> entries = feed.getEntries();
			// 循环得到每个子项信息
			for (int i = 0; i < entries.size(); i++) {
				SyndEntry entry = (SyndEntry) entries.get(i);

				/*
				 * System.out.println(entry.getAuthor());
				 * System.out.println(entry.getTitle());
				 * System.out.println(entry.getPublishedDate());
				 * System.out.println(entry.getLink());
				 */

				nm.setAuthor(entry.getAuthor());
				nm.setTitle(entry.getTitle());
				nm.setPublishDate(entry.getPublishedDate());
				nm.setLink(entry.getLink());
				SyndContent content = entry.getDescription();
				if (content != null) {
					String str = entry.getDescription().getValue();
					if (str != null) {
						nm.setDescriptVal(str);
						getPhoto(str, nmdata);
					}
					str = entry.getDescription().getType();
					if (str != null)
						nm.setDescriptType(str);
				}
				nm.setBoardName(boardName);

				nmservice.save(nmdata);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			bRet = false;
		}

		nmdata = null;
		
		return bRet;
	}
}
