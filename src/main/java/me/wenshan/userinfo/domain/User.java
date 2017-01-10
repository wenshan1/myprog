package me.wenshan.userinfo.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.wenshan.constants.UserConstants;
import me.wenshan.util.IdGenerator;

@Entity
@Table (name = "user")
public class User {
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "nickName", nullable = false)
	private String nickName;   /* 登录用户名 */
	
	@Column(name = "realName")
	private String realName;   
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "role", nullable = false)
	private int role;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "createTime")
	private Date createTime;
	
	@Column(name = "creator")
	private String creator;
	
	@Column(name = "lastUpdate")
	private Date lastUpdate;
	
	public void setLastUpdate (Date d) {
		lastUpdate = d;
	}
	
	public int getRole ()
	{
		return role;
	}
	
	public User ()
	{
		
	}
	
	public User (String username, String password, String status,
			int role)
	{
		this.setId(IdGenerator.uuid19());
		this.role = role;
		this.status = status;
		this.nickName = username;
		this.password = password;
		Calendar cal = Calendar.getInstance();
		createTime = cal.getTime();
	}

	public String getNickName () {
		return nickName;
	}
	public void setNickName (String s) {
		nickName = s;
	}
	
	public String getUsername() {
		return this.nickName;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword (String s) {
		password = s;
	}
	
	public boolean isEnable () {
		return UserConstants.USER_STATUS_NORMAL.equalsIgnoreCase(status);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
