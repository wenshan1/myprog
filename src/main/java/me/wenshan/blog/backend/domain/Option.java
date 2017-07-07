package me.wenshan.blog.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "myoption")
public class Option {
	@Id
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "value")
	private String value;

	public Option () {
	
	}
	
    public Option (String name, String value) {
		setName (name);
		setValue (value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
