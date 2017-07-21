package me.wenshan.util;

import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import me.wenshan.backend.service.OptionService;
import me.wenshan.constants.OptionConstants;

public class Global {
	private String domain      = "";
	private String title       = "";
	private String subtitle    = "";
	private String description = "";
	private String Keywords    = "";
	private static Global global = null;
	
	public static void getDataFromDB (OptionService ops) {
		Global g = getInstance (null);
		g.title = ops.getOptionValue(OptionConstants.TITLE);
		g.subtitle = ops.getOptionValue(OptionConstants.SUBTITLE);
		g.description = ops.getOptionValue(OptionConstants.DESCRIPTION);
		g.Keywords = ops.getOptionValue(OptionConstants.KEYWORDS);
	}
	public static Global getInstance (HttpServletRequest request) {
		if (global != null) {
			if (global.domain.length() == 0 && request != null) {
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 
				                  request.getServerPort() + path + "/";
				global.setDomain(basePath);
			}	
			return global;
		}
		global = new Global ();
		return global;
	}
	private Global() {	
	}

	public boolean isAllowComment() {
		return Boolean.parseBoolean((OptionConstants.ALLOW_COMMENT));
	}

	public int getYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.YEAR);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return Keywords;
	}

	public void setKeywords(String keywords) {
		Keywords = keywords;
	}

}
