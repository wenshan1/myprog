/*
 * @author: BinLan
 * @email:  jetlan@live.cn
 */

package me.wenshan.stock.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
code,代码
name,名称
industry,所属行业
area,地区
pe,市盈率
outstanding,流通股本(亿)
totals,总股本(亿)
totalAssets,总资产(万)
liquidAssets,流动资产
fixedAssets,固定资产
reserved,公积金
reservedPerShare,每股公积金
esp,每股收益
bvps,每股净资
pb,市净率
timeToMarket,上市日期
undp,未分利润
perundp, 每股未分配
rev,收入同比(%)
profit,利润同比(%)
gpr,毛利率(%)
npr,净利润率(%)
holders,股东人数
 */

@Entity
@Table(name = "stockbasic")
public class StockBasic {
	@Id
	@Column(name="code")
    private String code;
	
	@Column (name="name", nullable = false)
	private String name;  /* 名称 */
	
	@Column (name="industry", nullable = false)
	private String industry;  /* 所属行业 */
	
	@Column (name="area")
	private String area; //地区
	
	@Column (name="pe")
	private double pe; //pe,市盈率
	
	@Column (name="outstanding")
	private double outstanding; //outstanding,流通股本(亿)
	
	@Column (name="totals")
	private double totals;	//totals,总股本(亿)
    
	@Column (name="totalAssets")
	private double totalAssets;		   //totalAssets,总资产(万)
}
