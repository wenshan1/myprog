
package me.wenshan.beijing.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "beijing_kong_qi_zhi_liang")
public class KongQiZhiLiang {
	@Id
	@Column (name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Column (name="datetime")
	private Date datetime;
	
	@NotNull
	@Column (name="zhi_liang")
	private int zhiLiang;

	public KongQiZhiLiang (){
		Calendar cal = Calendar.getInstance();
        long srcTime = cal.getTimeInMillis();
        long off = TimeZone.getDefault().getRawOffset();
        long targetTime = srcTime - off;
        TimeZone tz = TimeZone.getTimeZone ("GMT+8");
        off = tz.getRawOffset();
        targetTime = targetTime + off;
        
        datetime = new Date (targetTime);
        zhiLiang = -1;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public int getZhiLiang() {
		return zhiLiang;
	}

	public void setZhiLiang(int zhiLiang) {
		this.zhiLiang = zhiLiang;
	}
}
