package me.wenshan.util;

import java.util.List;

public class Page<T> {
	
	/* 分页数据  */
	
	private List<T> records;
	
	/* 总记录数 */
	
	private long totalrecord;
	
	/* 总页数  */
	
	private long totalpage = 1;
	
	/* 每页显示记录数  */
	
	private int maxresult = 5;
	
	/* 当前页  */
	
	private int currentpage =1;
	
	
	public int getFirstResult() {
		return (currentpage - 1) * maxresult;
		}
	
	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public long getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(long totalpage) {
		this.totalpage = totalpage;
	}

	public int getMaxresult() {
		return maxresult;
	}

	public void setMaxresult(int maxresult) {
		this.maxresult = maxresult;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public long getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}
	
	
}
