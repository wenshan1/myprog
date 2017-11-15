package me.wenshan.util;

import java.util.Calendar;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import me.wenshan.constants.StockConstants;
import me.wenshan.stockmodel.domain.StockModelData;
import me.wenshan.stockmodel.service.StockModelDataService;  
               
@Component
public class EmailSend {
	@Autowired
	private StockModelDataService stockMdlServer;
	
	private String getContent () {
	     StockModelData data = stockMdlServer.getLatestData(StockConstants.MODEL_300_500);
	     if (data == null)
	    	 return null;
	     
	     StringBuffer sb = new StringBuffer ();
	     sb.append(
	     "<html>" + 
	     "<meta charset=\"UTF-8\" />" +
	     "</head>" +
	     "<h1>300-500模型数据</h1>" +
	        "<table border=\"1\" bordercolor=\"#a0c6e5\" style=\"border-collapse:collapse;\"" +
	             "<tr>" +
	                 "<th>日期</th>" +
	                 "<th>收盘价格</th>" +
	                 "<th>收盘股票名称</th>" +
	                 "<th>下一个交易日股票名称</th>" +
	                 "<th>下下一个交易日股票名称</th>" +
	             "</tr>");
	     sb.append("<tr >");
	     sb.append("<td >" + data.getPk().getRiqi() + "</td>");
	     sb.append("<td >" + data.getCloseprice() + "</td>");
	     sb.append("<td >" + data.getCurrStockName() + "</td>");
	     sb.append("<td >" + data.getNextStockName() + "</td>");
	     sb.append("<td >" + data.getNnextStockName() + "</td>");
	     sb.append("</tr>" + 
	         "</table>" +
	     "</html>");

		return sb.toString();
		
	}
	public void send () {
		Calendar cal = Calendar.getInstance();
		String content = getContent ();
		if (content == null)
			return;
		HtmlEmail email = new HtmlEmail();         
        email.setDebug(true);
        email.setSSLOnConnect(true);  
        email.setHostName("smtp.sina.com"); 
        email.setAuthentication("jetlan@sina.com", "hotmail.lb");  
        try     
        {    
         email.setFrom("jetlan@sina.com"); //发送方,这里可以写多个  
         email.addTo("jetlan@live.cn"); // 接收方    
         email.setCharset("UTF-8");  
         email.setSubject("最新28结果 " + cal.getTime()); // 标题  
         email.setHtmlMsg(content); // 内容  
         email.send();  
         System.out.println("发送成功");  
        } catch (EmailException e) {    
            e.printStackTrace();    
        }     
	}
}
