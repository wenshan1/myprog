package me.wenshan.backend.form;

import me.wenshan.stockmodel.domain.StockModelTongJi;

public class TongJiForm {
	// 最大盈利率
	private double yinMax = 0;
	private StockModelTongJi  yinMaxObject = null;

	// 最小盈利率
	private double yinMin = 0;
	private StockModelTongJi  yinMinObject = null;
	
	//记录数
	private long   count = 0;
	
	//盈利记录数
	private long   yinCount = 0;
	
	// 最近一年盈利率
	private double latestYearLv = 0;

	// 今年以来盈利率
	private double yearLv = 0;
	
	// 最近三年盈利率
	private double threeYearLv = 0;
	
	public double getYinMax() {
		return yinMax;
	}

	public void setYinMax(double yinMax) {
		this.yinMax = yinMax;
	}

	public StockModelTongJi getYinMaxObject() {
		return yinMaxObject;
	}

	public void setYinMaxObject(StockModelTongJi yinMaxObject) {
		this.yinMaxObject = yinMaxObject;
	}

	public double getYinMin() {
		return yinMin;
	}

	public void setYinMin(double yinMin) {
		this.yinMin = yinMin;
	}

	public StockModelTongJi getYinMinObject() {
		return yinMinObject;
	}

	public void setYinMinObject(StockModelTongJi yinMinObject) {
		this.yinMinObject = yinMinObject;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getYinCount() {
		return yinCount;
	}

	public void setYinCount(long yinCount) {
		this.yinCount = yinCount;
	}

	public double getLatestYearLv() {
		return latestYearLv;
	}

	public void setLatestYearLv(double latestYearLv) {
		this.latestYearLv = latestYearLv;
	}

	public double getYearLv() {
		return yearLv;
	}

	public void setYearLv(double yearLv) {
		this.yearLv = yearLv;
	}

	public double getThreeYearLv() {
		return threeYearLv;
	}

	public void setThreeYearLv(double threeYearLv) {
		this.threeYearLv = threeYearLv;
	}
	
}
