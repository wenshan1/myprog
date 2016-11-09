package me.wenshan.newsmth.domain;

public class NewsmthData {
	private Newsmth newsmth = null;
	private Photo photo = null;
	
	public NewsmthData(){
	}
	
	public Newsmth getNewsmth(){
		return newsmth;
	}
	
	public void  setNewsmth(Newsmth nm){
		 newsmth = nm;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
}
