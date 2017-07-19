package me.wenshan.stock.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

@Embeddable
public class StockDayHis_PK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;  // 股票代码 
	private Date riqi;   // 日期 
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getRiqi() {
		return riqi;
	}
	public void setRiqi(Date riqi) {
		this.riqi = riqi;
	}

}
