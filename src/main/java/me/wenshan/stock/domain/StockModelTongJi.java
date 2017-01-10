package me.wenshan.stock.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "stockmodeltongji")
public class StockModelTongJi {
	@Id
	@Column (name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Column (name="modelName")
	private String modelName;  /* 模型名字 */
	
	@NotNull
	@Column (name="startDate")
	private Date   startDate; 
	
	@NotNull
	@Column (name="startStockName")
	private String startStockName = "";  
	
	@NotNull
	@Column (name="startClosePrice")
	private double startClosePrice = 0; /* 收盘价格 */
	
	@Column (name="endDate")
	private Date   endDate; 
	
	@Column (name="endClosePrice")
	private double endClosePrice   = 0; /* 收盘价格 */
	
	@Column (name="endStockName")
	private String endStockName = "";
	
	@NotNull
	@Column (name="status")
	private int    status          = 0; /*  0 - 为初始化  1 - 开始记录数据， 2 - 结束这段数据记录 */

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public double getStartClosePrice() {
		return startClosePrice;
	}

	public void setStartClosePrice(double startClosePrice) {
		this.startClosePrice = startClosePrice;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getEndClosePrice() {
		return endClosePrice;
	}

	public void setEndClosePrice(double endClosePrice) {
		this.endClosePrice = endClosePrice;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStartStockName() {
		return startStockName;
	}

	public void setStartStockName(String startStockName) {
		this.startStockName = startStockName;
	}

	public String getEndStockName() {
		return endStockName;
	}

	public void setEndStockName(String endStockName) {
		this.endStockName = endStockName;
	}
}
