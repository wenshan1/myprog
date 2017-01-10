package me.wenshan.newsmth.domain;

import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "newsmth_photo")
public class Photo {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@Column(name = "newsmthid")
	private long newsmthid;

	@NotNull
	@Column(name = "pic")
	private Blob pic = null;

	@NotNull
	@Column(name = "picname")
	private String picname = null;

	public long getId() {
		return id;
	}

	public void setId(long i) {
		id = i;
	}

	public long getNewsmthid() {
		return newsmthid;
	}

	public void setNewsmthid(long i) {
		newsmthid = i;
	}

	public Blob getPic() {
		return pic;
	}

	public void setPic(Blob p) {
		pic = p;
	}

	public String getPicname() {
		return picname;
	}

	public void setPicname(String i) {
		picname = i;
	}

}
