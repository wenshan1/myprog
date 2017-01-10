package me.wenshan.newsmth.domain;

import java.util.ArrayList;
import java.util.List;

public class NewsmthData {
	private Newsmth newsmth = null;
	private List<Photo> photos = new ArrayList<Photo> ();
	
	public NewsmthData(){
		 
	}
	
	public Newsmth getNewsmth(){
		return newsmth;
	}
	
	public void  setNewsmth(Newsmth nm){
		 newsmth = nm;
	}
	public List<Photo> getPhotos ()
	{
		return photos;
	}
	public void setPhotos (List<Photo> l)
	{
		photos = l;
	}
}
