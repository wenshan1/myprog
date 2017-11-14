package me.wenshan.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
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
	     sb.append("模型名字\t 日期\t 收盘价格\t 收盘股票名称\t 下一个交易日股票名称\t 下下一个交易日股票名称\n");
	     sb.append(data.getPk().getName()+"\t ");
	     sb.append(data.getPk().getRiqi()+"\t ");
	     sb.append(data.getClosepriceStr()+"\t ");
	     sb.append(data.getCurrStockName()+"\t ");
	     sb.append(data.getNextStockName()+"\t ");
	     sb.append(data.getNnextStockName()+"\t ");
	     sb.append("\n");
		return sb.toString();
		
	}
	public void send () {
		Calendar cal = Calendar.getInstance();
		String content = getContent ();
		if (content == null)
			return;
		SimpleEmail email = new SimpleEmail();       
        //email.setTLS(true); //是否TLS校验，，某些邮箱需要TLS安全校验，同理有SSL校验  
        email.setStartTLSEnabled(true);
        email.setDebug(true);  
        //email.setSSL(true);  
        
        email.setHostName("smtp.sina.com"); 
        email.setAuthentication("jetlan@sina.com", "hotmail.lb");  
        try     
        {    
         email.setFrom("jetlan@sina.com"); //发送方,这里可以写多个  
         email.addTo("jetlan@live.cn"); // 接收方    
         email.setCharset("UTF-8");  
         email.setSubject("最新28轮动结果 " + cal.getTime()); // 标题  
         email.setMsg(content); // 内容  
         email.send();  
         System.out.println("发送成功");  
        } catch (EmailException e) {    
            e.printStackTrace();    
        }     
	}
}
