package me.wenshan.stock.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.wenshan.constants.StockConstants;
import me.wenshan.stock.domain.StockIndex;

class StockJson {
	private List< List<String> >  day;
	public StockJson () {
		
	}
	public List< List<String> > getDay() {
		return day;
	}
	public void setDay(List< List<String> > day) {
		this.day = day;
	}

}
class DataJson {
	private StockJson stockName;
	public DataJson () {
		
	}
	public StockJson getStockName() {
		return stockName;
	}
	public void setStockName(StockJson stockName) {
		this.stockName = stockName;
	}
}
class StockIndexJson {
    private int code;
    private String msg;
    private DataJson data;
    public StockIndexJson() {
    	
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DataJson getData() {
		return data;
	}

	public void setData(DataJson data) {
		this.data = data;
	}
}
class FetchDataFromTencen {
	private String stockName; 
	private  String startDate;
	private  String endDate;
	private  List<String> years;
	
	FetchDataFromTencen (String stockName, String startDate, String endDate){
		this.stockName = stockName;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	// 获得年
	private List<String> getYears (){
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;		
        try {
        	start = formater.parse(startDate);
        	end = formater.parse(endDate);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return null;
        }
        
		Calendar startCl = Calendar.getInstance();
		Calendar endCl = Calendar.getInstance();
		startCl.setTime(start);
		endCl.setTime(end);
        int y = startCl.get(Calendar.YEAR);
        List<String> lst = new ArrayList<String> ();
        do {
        	lst.add((new Integer(y)).toString());
        	y++;
        }
        while (y <= endCl.get(Calendar.YEAR));

        return lst;
	}
	
	// 获得所以请求url
	public List<String> getUrls (){
		List<String> lst = new ArrayList<String> ();
		for (int i = 0; i < years.size(); i++) {
			java.util.Random random=new java.util.Random(17);// 定义随机类
			double ran=random.nextDouble();
			String year = years.get(i);
			String url = String.format("http://web.ifzq.gtimg.cn/appstock/app/fqkline/get?_var=kline_day%s&param=%s,day,%s,%s,320,&r=%f",
					year, stockName, year + "-01-01", year + "-12-31", ran);
			lst.add(url);
		}
		return lst;
	}
	private List<StockIndex> genStockIndexList (List< List<String> > lstlst){
		List<StockIndex> stockIndexLst = new ArrayList<StockIndex> ();
		for (Iterator< List<String> > itit = lstlst.iterator(); itit.hasNext(); ) {
			List<String> lst = itit.next();
			StockIndex stockIndex = new StockIndex ();
			stockIndexLst.add(stockIndex);
			
			stockIndex.getPk().setName(stockName);
			for (int i = 0; i < lst.size(); i++ ) {
				String str = lst.get(i);
				switch (i) {
				case 0:
	        		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
	        		try {
	        			stockIndex.getPk().setRiqi(formater.parse(str));
					} catch (ParseException e) {
						e.printStackTrace();
					}        		
					break;
				case 1:
					stockIndex.setOpenprice(new Double(str));
					break;
				case 2:
					stockIndex.setCloseprice(new Double(str));
					break;
				case 3:
					stockIndex.setHighprice(new Double(str));
					break;
				case 4:
					stockIndex.setLowprice(new Double(str));
					break;
				case 5:
					stockIndex.setVolume(new Double(str));
					break;					
				}
			}
		}
		return stockIndexLst;
	}
	private List<StockIndex> get_DAY_K_Data (String strurl){
		StringBuilder sb = new StringBuilder();
		
		try {
			URL url = new URL(strurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/57.0");
			connection.setRequestMethod("GET");
			connection.setReadTimeout(6 * 10000);
			
			if (connection.getResponseCode() == 200) {
				InputStream in = connection.getInputStream();
				byte[] buf = new byte[1024];
				int count;
				while ((count = in.read(buf)) != -1) {
					sb.append(new String (buf, 0, count, "utf-8"));
				}				
				in.close();
			}
			else
				return null;

		} catch (Exception  e) {
			e.printStackTrace();
			return null;
		}
		System.out.println(sb.toString());
		// 去除“qt”后面的串
		int n = sb.indexOf("\"qt\"");
		if (n != -1) {
			sb.delete(n, sb.length());
			n = sb.lastIndexOf(",");
			if (n != -1) {
				sb.delete(n , sb.length());
			}
		}
		sb.append("}}}");
		n = sb.indexOf("=");
		if (n!=-1) {
			sb.delete(0, n + 1);
		}
		//  替换具体指数名字为"stockName"
		n = sb.indexOf(stockName);
		if (n!=-1) {
			sb.replace(n, n + stockName.length(), "stockName");
		}
		
		System.out.println(sb.toString());
		
		 ObjectMapper mapper = new ObjectMapper(); //转换器
		 StockIndexJson indexJson = null;
		 try {
			 indexJson = mapper.readValue(sb.toString(), StockIndexJson.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (indexJson == null || indexJson.getCode() != 0)
			return null;
		return genStockIndexList (indexJson.getData().getStockName().getDay());
	}
	public List<StockIndex> getIndexData_K() throws InterruptedException {
		years = getYears ();
		if (years == null || years.isEmpty())
			return null;
		 List<String> urls = getUrls ();
		 if (urls == null || urls.isEmpty())
			 return null;
		 List<StockIndex> lstTotal = new ArrayList<StockIndex> ();
		 for (Iterator<String>  it = urls.iterator(); it.hasNext(); ) {
			 List<StockIndex>  lst = get_DAY_K_Data (it.next());
			 if (lst != null)
				 lstTotal.addAll(lst);
			 Thread.sleep(3);
		 }
		return lstTotal;
	}
}

@Service
public class StockFetchDataImpl implements StockFetchData {
	@Autowired
	private IStockService stockService;

	@Override
	public List<StockIndex> getIndexData_K(String stockName, String startDate, String endDate) {
		if (stockName == null || startDate == null)
			return null;
		if (endDate == null || endDate.isEmpty()) {
			Calendar cal = Calendar.getInstance();
            int m = cal.get(Calendar.MONTH) + 1;
            int d = cal.get(Calendar.DAY_OF_MONTH);
            int y = cal.get(Calendar.YEAR);
            endDate = String.format("%d-%02d-%02d", y, m, d);
		}
		List<StockIndex> lst = null;
		FetchDataFromTencen ft = new FetchDataFromTencen (stockName, startDate, endDate);
		try {
			 lst = ft.getIndexData_K();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return lst;
	}

	@Override
	public boolean initAllIndexData() { 
		stockService.removeAll();
		List<StockIndex> lst = null;
		for (Iterator<String> it =StockConstants.getSockNames().iterator(); 
		     it.hasNext();) {
			lst = getIndexData_K (it.next(), "2004-01-01", null);
			if (lst != null && lst.size() !=0)
			    stockService.saveAll(lst, true);
		}
		return true;
	}

	@Override
	public boolean updateAllIndexData() {
		boolean bRet = false;
		
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int y = cal.get(Calendar.YEAR);
        String strriqi = String.format("%d-%02d-%02d", y, m, d);
		
		List<StockIndex> lst = null;
		for (Iterator<String> it =StockConstants.getSockNames().iterator(); 
			 it.hasNext();) {
			String name = it.next();
			if (stockService.getStockIndex(strriqi, name) != null)
				continue;
			
			lst = getIndexData_K (name, strriqi, null);
			if (lst != null && lst.size() !=0)
			    stockService.saveAll(lst, false);
			bRet = true;
		}
		return bRet;
	}

}
