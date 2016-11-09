package me.wenshan.newsmth.domain;

import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "newsmth_photo")
public class Photo {

	@Id
	@NotNull
	@Column(name = "link")
	private String link; /* 文章链接 */

	@NotNull
	@Column(name = "pic0")
	private Blob pic0 = null;

	@Column(name = "pic1")
	private Blob pic1 = null;

	@Column(name = "pic2")
	private Blob pic2 = null;

	@Column(name = "pic3")
	private Blob pic3 = null;

	@Column(name = "pic4")
	private Blob pic4 = null;

	@Column(name = "pic5")
	private Blob pic5 = null;

	@Column(name = "pic6")
	private Blob pic6 = null;

	@Column(name = "pic7")
	private Blob pic7 = null;

	@Column(name = "pic8")
	private Blob pic8 = null;

	@Column(name = "pic9")
	private Blob pic9 = null;
	
	public Blob getPhoto(int num){
		Blob photo = null;
		switch (num) {
		case 0:
			photo = pic0 ;
			break;
		case 1:
			photo = pic1 ;
			break;
		case 2:
			photo = pic2 ;
			break;
		case 3:
			photo = pic3 ;
			break;
		case 4:
			photo = pic4 ;
			break;
		case 5:
			photo = pic5 ;
			break;
		case 6:
			photo = pic6;
			break;
		case 7:
			photo = pic7 ;
			break;
		case 8:
			photo = pic8 ;
			break;
		default:
			photo = pic9;
			break;
		}
		return photo;
	}
	
	public void setPhoto(Blob photo, int num) {
		switch (num) {
		case 0:
			pic0 = photo;
			break;
		case 1:
			pic1 = photo;
			break;
		case 2:
			pic2 = photo;
			break;
		case 3:
			pic3 = photo;
			break;
		case 4:
			pic4 = photo;
			break;
		case 5:
			pic5 = photo;
			break;
		case 6:
			pic6 = photo;
			break;
		case 7:
			pic7 = photo;
			break;
		case 8:
			pic8 = photo;
			break;
		default:
			pic9 = photo;
			break;
		}
	}

	public Photo(String link) {
		this.link = link;
	}

	public Photo() {
		this.link = null;
	}
}
