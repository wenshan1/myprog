package me.wenshan.beijing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/* 
riqi 日期时间
cunliangfang_zong 存量房总签约套数
cunliangfang_zhuzai  存量房住宅签约套数
xianfang_zong      现房总签约套数
xianfang_zhuzhai   现房住宅签约套数
qifang_zong      期房总签约套数
qifang_zhuzhai      期房住宅签约套数    
*/


@Entity
@Table (name = "beijing_fangdican_qianyue")
public class Beijing_fangdican_qianyue {

	@Id
	@Column(name="riqi")
    private Date riqi;
	
	@NotNull
	@Column(name="cunliangfang_zong")
    private int cunliangfang_zong;
	
	@NotNull
	@Column(name="cunliangfang_zhuzhai")
    private int cunliangfang_zhuzhai;
	
	@NotNull
	@Column(name="xianfang_zong")
    private int xianfang_zong;
	
	@NotNull
	@Column(name="xianfang_zhuzhai")
    private int xianfang_zhuzhai;
	
	@NotNull
	@Column(name="qifang_zong")
    private int qifang_zong;
	
	@NotNull
	@Column(name="qifang_zhuzhai")
    private int qifang_zhuzhai;

    public Beijing_fangdican_qianyue() {
        cunliangfang_zong = 2;
        cunliangfang_zhuzhai = 3;
        xianfang_zong = 4;
        xianfang_zhuzhai = 5;
        qifang_zong = 6;
        qifang_zhuzhai = 7;

    }

    public Date getRiqi() {
        return riqi;
    }

    public void setRiqi(Date dd) {
        riqi = dd;
    }

    public int getCunliangfang_zong() {
        return cunliangfang_zong;
    }

    public void setCunliangfang_zong(int v) {
        cunliangfang_zong = v;
    }

    public int getCunliangfang_zhuzhai() {
        return cunliangfang_zhuzhai;
    }

    public void setCunliangfang_zhuzhai(int v) {
        cunliangfang_zhuzhai = v;
    }

    public void setXianfang_zong(int v) {
        xianfang_zong = v;
    }

    public int getXianfang_zong() {
        return xianfang_zong;
    }

    public void setXianfang_zhuzhai(int v) {
        xianfang_zhuzhai = v;
    }

    public int getXianfang_zhuzhai() {
        return xianfang_zhuzhai;
    }

    public void setQifang_zong(int v) {
        qifang_zong = v;
    }

    public int getQifang_zong() {
        return qifang_zong;
    }

    public void setQifang_zhuzhai(int v) {
        qifang_zhuzhai = v;
    }

    public int getQifang_zhuzhai() {
        return qifang_zhuzhai;
    }
    
    
}
