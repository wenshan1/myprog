package me.wenshan.stock.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stockmtwdata")
public class StockM20Data {
	@Id
	@Column(name="riqi")
    private Date riqi;
	
	@Column (name="openprice")
	private double openprice = 0; /* 开盘价格 */
	
	@Column (name="highprice")
	private double highprice = 0; /* 最高价格 */
	
	@Column (name="lowprice")
	private double lowprice = 0; /* 最低价格 */
	
	@Column (name="closeprice")
	private double closeprice = 0; /* 收盘价格 */
	
	@Column (name="volume")
	private long volume = 0; /* 成交量 */
	
	@Column (name="currstockname")
	private String currStockName; /* 收盘股票名称 */
	
	@Column (name="nextstockname")
	private String nextStockName;  /* 下一个交易日股票名称 */
	
	@Column (name="nnextstockname")
	private String nnextStockName; /* 下下一个交易日股票名称 */
	
	@Column (name="adjcloseprice")
	private double adjcloseprice; /* 复权收盘价格 */
	
	public StockM20Data()
	{
		
	}
	
	public String getClosepriceStr () {
		return String.format("%.2f", closeprice);
	}
	
	public String getCurrStockName ()
	{
		return this.currStockName;
	}
	
	public String getNextStockName ()
	{
		return this.nextStockName;
	}
	
	public String getNnextStockName ()
	{
	return this.nnextStockName;	
	}
	
	public StockM20Data (Date riqi, double close, String currStockName, String nStockname, String nnStockName)
	{
		this.riqi = riqi;
		this.closeprice = close;
		this.currStockName = currStockName;
		this.nextStockName = nStockname;
		this.nnextStockName = nnStockName;
		
	}
	
	public Date getRiqi() {
		return riqi;
	}
	public void setRiqi(Date riqi) {
		this.riqi = riqi;
	}
	
	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public double getCloseprice() {
		return closeprice;
	}

	public void setCloseprice(double closeprice) {
		this.closeprice = closeprice;
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

	public double getAdjcloseprice() {
		return adjcloseprice;
	}

	public void setAdjcloseprice(double adjcloseprice) {
		this.adjcloseprice = adjcloseprice;
	}

	public double getOpenprice() {
		return openprice;
	}

	public void setOpenprice(double openprice) {
		this.openprice = openprice;
	}
	
}
