package me.wenshan.backend.form;

import me.wenshan.blog.domain.Post;

public class PostForm {
	private long id = -1;
	private String title = "";
	private String content = "";

	/* 文章状态 */
	private int pstatus = 0;

	/* 评论状态 */
	private int cstatus = 0;
	
	private String strtag = "";

	public PostForm () {
	    
	}
	
	public PostForm(Post post) {
	    id = post.getId();
	    this.title = post.getTitle();
	    content = post.getContent();
	    pstatus = post.getPstatusInt();
	    cstatus = post.getCstatusInt();
	    strtag = post.getTagsStr ();
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPstatus() {
		return pstatus;
	}

	public void setPstatus(int pstatus) {
		this.pstatus = pstatus;
	}

	public int getCstatus() {
		return cstatus;
	}

	public void setCstatus(int cstatus) {
		this.cstatus = cstatus;
	}

	public String getStrtag() {
		return strtag;
	}

	public void setStrtag(String strtag) {
		this.strtag = strtag;
	}

}
