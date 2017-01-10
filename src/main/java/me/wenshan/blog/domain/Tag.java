package me.wenshan.blog.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag {

	@Id
	@Column(name = "tag_name", nullable = false)
	private String name;
	
	@Column(name = "description", columnDefinition = "text")
	private String description;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "tags")
	private List<Post> posts;
	
	public List<Post> getPosts() {
		if (posts == null)
			posts = new ArrayList<>();
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public Tag(String name) {
		setName(name);
	}

	public Tag() {
		this("");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
