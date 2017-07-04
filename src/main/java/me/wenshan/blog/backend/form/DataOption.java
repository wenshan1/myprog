package me.wenshan.blog.backend.form;

public class DataOption {
	private int beijingQuality;  //北京天气条数
	private int newsmthNum;     //水木数据条数
	private int stockDataNum;     //最近几年数据，比如：2012-1-1
	private String stockModelName1;  //股票模型名字1
	private String stockModelName2; //股票模型名字2
	private int stockModelCycle;    //股票模型轮动周期
	private int stockUpdateCycle;    //股票模型更新周期

	public int getBeijingQuality() {
		return beijingQuality;
	}

	public void setBeijingQuality(int beijingQuality) {
		this.beijingQuality = beijingQuality;
	}

	public int getNewsmthNum() {
		return newsmthNum;
	}

	public void setNewsmthNum(int newsmthNum) {
		this.newsmthNum = newsmthNum;
	}

	public int getStockDataNum() {
		return stockDataNum;
	}

	public void setStockDataNum(int stockDataNum) {
		this.stockDataNum = stockDataNum;
	}

    public String getStockModelName1() {
        return stockModelName1;
    }

    public void setStockModelName1(String stockModelName1) {
        this.stockModelName1 = stockModelName1;
    }

    public String getStockModelName2() {
        return stockModelName2;
    }

    public void setStockModelName2(String stockModelName2) {
        this.stockModelName2 = stockModelName2;
    }

    public int getStockModelCycle() {
        return stockModelCycle;
    }

    public void setStockModelCycle(int stockModelCycle) {
        this.stockModelCycle = stockModelCycle;
    }

    public int getStockUpdateCycle() {
        return stockUpdateCycle;
    }

    public void setStockUpdateCycle(int stockUpdateCycle) {
        this.stockUpdateCycle = stockUpdateCycle;
    }
    
}
