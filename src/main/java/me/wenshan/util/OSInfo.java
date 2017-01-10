package me.wenshan.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统信息
 * 
 *
 */

public class OSInfo {
	private String osName;
	private String osVersion;
	private String javaVersion;
	private String serverInfo;
	private long totalMemory;
	
	public OSInfo(HttpServletRequest req) {
		osName = System.getProperty("os.name");
		osVersion = System.getProperty("os.version");
		javaVersion = System.getProperty("java.version");
		serverInfo = req.getServletContext().getServerInfo();
		totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
	}

	public String getOsName() {
		return osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	/**
	 * 获取服务器信息
	 * 
	 * @return
	 */
	public String getServerInfo() {
		return serverInfo;
	}

	/**
	 * 获取java进程内存大小，以M为单位
	 * 
	 * @return
	 */
	public long getTotalMemory() {
		return totalMemory;
	}

	/*
	public  OSInfo getCurrentOSInfo() {
		OSInfo instance = new OSInfo();
		instance.osName = System.getProperty("os.name");
		instance.osVersion = System.getProperty("os.version");
		instance.javaVersion = System.getProperty("java.version");
		instance.serverInfo = servletContext.getServerInfo();
		instance.totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;	
		return instance;
	}*/
}
