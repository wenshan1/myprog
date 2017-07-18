package me.wenshan.stock.domain;

import java.util.Date;

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
@Table(name = "stockbasics")
public class StockBasics {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name", nullable = false)
    private String name; /* 名称 */

    @Column(name = "industry", nullable = false)
    private String industry; /* 所属行业 */

    @Column(name = "area")
    private String area; // 地区

    @Column(name = "pe")
    private double pe; // pe,市盈率

    @Column(name = "outstanding")
    private double outstanding; // outstanding,流通股本(亿)

    @Column(name = "totals")
    private double totals; // totals,总股本(亿)

    @Column(name = "totalAssets")
    private double totalAssets; // totalAssets,总资产(万)

    @Column(name = "liquidAssets")
    private double liquidAssets; // liquidAssets,流动资产

    @Column(name = "fixedAssets")
    private double fixedAssets; // fixedAssets,固定资产

    @Column(name = "reserved")
    private double reserved; // reserved,公积金

    @Column(name = "reservedPerShare")
    private double reservedPerShare; // reservedPerShare,每股公积金

    @Column(name = "esp")
    private double esp; // esp,每股收益

    @Column(name = "bsps")
    private double bvps; // bvps,每股净资

    @Column(name = "pb")
    private double pb; // pb,市净率

    @Column(name = "timeToMarket")
    private Date timeToMarket; // timeToMarket,上市日期

    @Column(name = "undp")
    private double undp; // undp,未分利润

    @Column(name = "rev")
    private double rev; // rev,收入同比(%)

    @Column(name = "profit")
    private double profit; // profit,利润同比(%)

    @Column(name = "gpr")
    private double gpr; // gpr,毛利率(%)

    @Column(name = "npr")
    private double npr; // npr,净利润率(%)

    @Column(name = "holders")
    private long holders; // holders,股东人数

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getPe() {
        return pe;
    }

    public void setPe(double pe) {
        this.pe = pe;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
    }

    public double getTotals() {
        return totals;
    }

    public void setTotals(double totals) {
        this.totals = totals;
    }

    public double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public double getLiquidAssets() {
        return liquidAssets;
    }

    public void setLiquidAssets(double liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    public double getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(double fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    public double getReserved() {
        return reserved;
    }

    public void setReserved(double reserved) {
        this.reserved = reserved;
    }

    public double getReservedPerShare() {
        return reservedPerShare;
    }

    public void setReservedPerShare(double reservedPerShare) {
        this.reservedPerShare = reservedPerShare;
    }

    public double getEsp() {
        return esp;
    }

    public void setEsp(double esp) {
        this.esp = esp;
    }

    public double getBvps() {
        return bvps;
    }

    public void setBvps(double bvps) {
        this.bvps = bvps;
    }

    public double getPb() {
        return pb;
    }

    public void setPb(double pb) {
        this.pb = pb;
    }

    public Date getTimeToMarket() {
        return timeToMarket;
    }

    public void setTimeToMarket(Date timeToMarket) {
        this.timeToMarket = timeToMarket;
    }

    public double getUndp() {
        return undp;
    }

    public void setUndp(double undp) {
        this.undp = undp;
    }

    public double getRev() {
        return rev;
    }

    public void setRev(double rev) {
        this.rev = rev;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getGpr() {
        return gpr;
    }

    public void setGpr(double gpr) {
        this.gpr = gpr;
    }

    public double getNpr() {
        return npr;
    }

    public void setNpr(double npr) {
        this.npr = npr;
    }

    public long getHolders() {
        return holders;
    }

    public void setHolders(long holders) {
        this.holders = holders;
    }

}
