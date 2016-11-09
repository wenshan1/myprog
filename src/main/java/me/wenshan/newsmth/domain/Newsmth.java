/*
* Copyright (c) 2015 www.wenshan.me.
*
*/
 
/*
modification history
--------------------
02nov15,lanbin  created
*/


package me.wenshan.newsmth.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name = "newsmth")
public class Newsmth {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long  id;
	
	@Id
	@NotNull
	@Column (name="link")
	private String  link;        /* 文章链接 */
	
	@NotNull
	@Column (name="board_name")
	private String  boardName;        /* 板块名 */
	
	@Column (name="author")
	private String  author;   /* 作者 */
	
	@Column (name="title")
	private String  title;   /*  标题 */
	
	@NotNull
	@Column (name="publish_date")
	private Date    publishDate;   /*  发布时间 */
	
	@Lob
	@Column (name="descript_val")
	private String  descriptVal;  /*  文章描述  */
	
	@Column (name="descript_type")
	private String  descriptType;  /*   */
		

	// 不采用hibernate表关联
	//@OneToMany (cascade = {CascadeType.ALL})
    //@JoinColumn(name="newsmth_fk")
	//private Set<Photo> photos = new HashSet<Photo>();
	
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescriptVal() {
		return descriptVal;
	}
	public void setDescriptVal(String descriptVal) {
		this.descriptVal = descriptVal;
	}
	public String getDescriptType() {
		return descriptType;
	}
	public void setDescriptType(String descriptType) {
		this.descriptType = descriptType;
	}
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
}
