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

public interface NewsmthService {

	List<Newsmth> getAllDataByBoardName(String string);

	void save(NewsmthData nmdata);
	
	long count();
	long countPhoto();
	//preservernum: 保留条数
	void deleteOld (long preservernum);
	
	List<NewsmthData> getPageData(int first, int pageSize);
	
	void delete (long id);
}
