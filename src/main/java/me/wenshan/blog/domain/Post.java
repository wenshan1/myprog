package me.wenshan.blog.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import me.wenshan.constants.PostConstants;

/**
 * 文章/页面,注：post的creator为userid
 * 
 */

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", nullable = false)
	private long id;

	@Column(name="title", nullable = false)
	private String title;

	/* 摘录,当type为页面该项为null */
	@Column(name="excerpt", nullable = true)
	private String excerpt;

	@Column(name="content", nullable = true, columnDefinition="text")
	private String content;

	/* 文章类型（post/page等） */
	@Column(name="type", nullable = true)
	private PostType type = PostType.Post;

	/* 父文章ID，主要用于PAGE,只支持两级 */
	@Column(name="parent", nullable = true)
	private String parent = PostConstants.DEFAULT_PARENT;

	/* 文章状态 */
	@Column(name="pstatus", nullable = false)
	private PostStatus pstatus = PostStatus.publish;

	/* 评论状态 */
	@Column(name="cstatus", nullable = false)
	private PostComment cstatus = PostComment.open;

	/* 评论数 */
	@Column(name="ccount", nullable = true)
	private int ccount = 0;

	/* 阅读数 */
	@Column(name="rcount", nullable = true)
	private int rcount = 0;
	
	@Column(name="createTime", nullable = true)
	private Date createTime;
	
	@Column(name="creator", nullable = false)
	private String creator = "admin";
	   
	@Column(name="lastUpdate", nullable = true)
	private Date lastUpdate;

	  
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tag_post", joinColumns = @JoinColumn(name = "post_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_name"))
	private List<Tag> tags; 

	public List<Tag> getTags() {
		if (tags == null)
			tags = new ArrayList<>();
		return tags;
	}
	
	public void SetTags (List<Tag> tags) {
		this.tags = tags;
	}
	
	public String getTitle() {
		return title;
	}

	public int getPstatusInt () {
	    if (pstatus == PostStatus.publish)
	        return 0;
	    else if (pstatus == PostStatus.secret)
	        return 1;
	    else 
	        return 2;
	}
	
	public int getCstatusInt () {
	    if (cstatus == PostComment.open)
            return 0;
        else 
            return 1;
    }
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public PostStatus getPstatus() {
		return pstatus;
	}

	public void setPstatus(PostStatus pstatus) {
		this.pstatus = pstatus;
	}

	public PostComment getCstatus() {
		return cstatus;
	}

	public void setCstatus(PostComment cstatus) {
		this.cstatus = cstatus;
	}

	public int getCcount() {
		return ccount;
	}

	public void setCcount(int ccount) {
		this.ccount = ccount;
	}

	public int getRcount() {
		return rcount;
	}

	public void setRcount(int rcount) {
		this.rcount = rcount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public String getTagsStr () {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < tags.size(); j++) {
            sb.append(tags.get(j).getName());
            if (j != tags.size() - 1)
                sb.append(",");
        }
        return sb.toString();
    }
}
