package me.wenshan.stock.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;

@Embeddable 
class StockSzData_PK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column (name="name", nullable = false)
	private String name; /* 股票代码  sh601318 */
	
	@Column (name="riqi", nullable = false)
	private Date riqi; /* 日期 */
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRiqi() {
		return riqi;
	}
	public void setRiqi(Date riqi) {
		this.riqi = riqi;
	}
	
	public int hashCode(){
		return name.hashCode() + riqi.hashCode();
	}
	
	public boolean equals(Object obj){
		if (obj == null)
			return false;
		StockSzData_PK pk = (StockSzData_PK) obj;
		return pk.getRiqi().compareTo(riqi) == 0 && pk.name.compareTo(name) == 0;
	}
}

//保存深圳股票数据类

public class StockSzData {
	@EmbeddedId
	private StockSzData_PK pk = new StockSzData_PK ();
	
	@Column (name="openprice", nullable = false)
	private double openprice;  /* 开盘价格 */
	
	@Column (name="highprice", nullable = false)
	private double highprice;  /* 最高价格 */
	
	@Column (name="lowprice", nullable = false)
	private double lowprice; /* 最低价格 */
	
	@Column (name="closeprice", nullable = false)
	private double closeprice; /* 收盘价格 */
	
	@Column (name="volume", nullable = false)
	private long volume; /* 成交量 */
	
	@Column (name="adjcloseprice")
	private double adjcloseprice; /* 复权收盘价格 */

	public StockSzData () {
		
	}
	
	public double getOpenprice() {
		return openprice;
	}

	public void setOpenprice(double openprice) {
		this.openprice = openprice;
	}

	public double getHighprice() {
		return highprice;
	}

	public void setHighprice(double highprice) {
		this.highprice = highprice;
	}

	public double getLowprice() {
		return lowprice;
	}

	public void setLowprice(double lowprice) {
		this.lowprice = lowprice;
	}

	public double getCloseprice() {
		return closeprice;
	}

	public void setCloseprice(double closeprice) {
		this.closeprice = closeprice;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public double getAdjcloseprice() {
		return adjcloseprice;
	}

	public void setAdjcloseprice(double adjcloseprice) {
		this.adjcloseprice = adjcloseprice;
	}

	public StockSzData_PK getPk() {
		return pk;
	}

	public void setPk(StockSzData_PK pk) {
		this.pk = pk;
	}
}
