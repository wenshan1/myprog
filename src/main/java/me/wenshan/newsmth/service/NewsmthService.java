/*
* Copyright (c) 2015 www.wenshan.me.
*
*/
 
/*
modification history
--------------------
02nov15,lanbin  created
*/


package me.wenshan.newsmth.service;

import java.util.List;

import me.wenshan.newsmth.domain.Newsmth;
import me.wenshan.newsmth.domain.NewsmthData;
import me.wenshan.newsmth.domain.Photo;

public interface NewsmthService {

	List<Newsmth> getAllDataByBoardName(String string);

	void save(NewsmthData nmdata);
	
	long count();
	long countPhoto();
	
	List<NewsmthData> getPageData(int first, int pageSize);
	
	Photo getPhotoById(String link);
	void delete (String link);
}
