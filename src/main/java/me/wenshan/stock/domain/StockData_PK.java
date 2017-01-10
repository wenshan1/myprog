package me.wenshan.stock.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable 
public class StockData_PK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column (name="name", nullable = false)
	private String name; /* 股票代码  sh601318 */
	
	@Column (name="riqi", nullable = false)
	private Date riqi; /* 日期 */
	
	public StockData_PK () {
		
	}
	public StockData_PK (Date riqi, String name) {
		this.riqi = riqi;
		this.name = name;
		
	}
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
		StockData_PK pk = (StockData_PK) obj;
		return pk.getRiqi().compareTo(riqi) == 0 && pk.name.compareTo(name) == 0;
	}
}
