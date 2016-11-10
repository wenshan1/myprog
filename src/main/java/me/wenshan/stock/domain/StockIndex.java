package me.wenshan.stock.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Embeddable
class StockIndex_PK implements Serializable{
	private static final long serialVersionUID = -6282576227805319314L;
	
	@Column (name="name")
	private String name; /* 股票代码 */
	
	@Column (name="riqi")
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
		StockIndex_PK pk = (StockIndex_PK) obj;
		return pk.getRiqi().compareTo(riqi) == 0 && pk.name.compareTo(name) == 0;
	}
}

@Entity
@Table(name = "stockindex")
public class StockIndex {

	@EmbeddedId
	private StockIndex_PK pk = new StockIndex_PK ();
	
	@Column (name="openprice")
	private double openprice; /* 开盘价格 */
	
	@Column (name="highprice")
	private double highprice; /* 最高价格 */
	
	@Column (name="lowprice")
	private double lowprice; /* 最低价格 */
	
	@Column (name="closeprice")
	private double closeprice; /* 收盘价格 */
	
	@Column (name="volume")
	private long volume; /* 成交量 */
	
	@Column (name="adjcloseprice")
	private double adjcloseprice; /* 复权收盘价格 */

	public StockIndex() {

	}

	public StockIndex(String daima, Date riqi, double openPrice, double highprice, double lowprice, double closeprice,
			long volume, double adjcloseprice) {
		pk.setName(daima);
		pk.setRiqi(riqi);
		this.setOpenprice(openPrice);
		this.setHighprice(highprice);
		this.setLowprice(lowprice);
		this.setCloseprice(closeprice);
		this.setVolume(volume);
		this.setAdjcloseprice(adjcloseprice);
	}

	public StockIndex_PK getPk ()
	{
		return pk;
	}
	public String getStockName ()
	{
		return pk.getName();
	}
	public Date getRiqi ()
	{
		return pk.getRiqi();
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
