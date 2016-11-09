package me.wenshan.userinfo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name = "user")
public class User {

	@Id
	@NotNull
	@Column (name="username")
	private String  username;        /* 用户名 */
	
	@NotNull
	@Column (name = "password")
	private String password;       /* sha 加密密码 */
	
	@NotNull
	@Column (name = "role")
	private int role;   /* 0 - 管理  1- 普通用户     -1--验证没有通过 */
	
	public User(String name, String pwd, int role){
		username = name;
		password = pwd;
		this.role = role;
	}
	public User(){
		username = null;
		password = null;
		role = 0;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword ()
	    {
		return password;
	    }
	
	public int getRole()
	    {
		return role;
	    }
	
}
