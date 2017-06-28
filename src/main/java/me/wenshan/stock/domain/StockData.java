package me.wenshan.stock.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

// 股票日k线数据

@Entity
@Table(name = "stockdata")
public class StockData {
	@EmbeddedId
	private StockData_PK pk = new StockData_PK ();
	
	@Column (name="openprice", nullable = false)
	private double openprice;  /* 开盘价格 */
	
	@Column (name="highprice", nullable = false)
	private double highprice;  /* 最高价格 */
	
	@Column (name="lowprice", nullable = false)
	private double lowprice; /* 最低价格 */
	
	@Column (name="closeprice", nullable = false)
	private double closeprice; /* 收盘价格 */
	
	@Column (name="volume", nullable = false)
	private double volume; /* 成交量 */
	
	@Column (name="adjcloseprice")
	private double adjcloseprice; /* 复权收盘价格 */

	public StockData () {
		
	}
	
	public StockData(String stockName, Date riqi, double open, double high, 
			double low, double close, long volum, double d) {
		pk.setName(stockName);
		pk.setRiqi(riqi);
		this.openprice = open;
		this.highprice = high;
		this.lowprice = low;
		this.closeprice = close;
		this.volume = volum;
		this.adjcloseprice = d;
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

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getAdjcloseprice() {
		return adjcloseprice;
	}

	public void setAdjcloseprice(double adjcloseprice) {
		this.adjcloseprice = adjcloseprice;
	}

	public StockData_PK getPk() {
		return pk;
	}

	public void setPk(StockData_PK pk) {
		this.pk = pk;
	}

}
