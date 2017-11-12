package me.wenshan.stock.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

//股票日k线数据,不复权
@Entity
@Table(name = "stockdayhist")
public class StockDayHis {
	@EmbeddedId
	private StockDayHis_PK pk = new StockDayHis_PK ();
	@Column (name="open")
	private double open;   // 开盘价
	
	@Column (name="high")
	private double high;   // 最高价
	
	@Column (name="close")
	private double close;  // 收盘价
	
	@Column (name="low")
	private double low;    // 最低价
	
	@Column (name="volume")
	private double volume; // 成交量
	
	@Column (name="amount")
	private double amount; // 成交金额
	
	public StockDayHis_PK getPk() {
		return pk;
	}

	public void setPk(StockDayHis_PK pk) {
		this.pk = pk;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	

}
