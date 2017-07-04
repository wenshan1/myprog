package me.wenshan.stock.service;

import java.util.List;

import me.wenshan.blog.backend.form.TongJiForm;
import me.wenshan.stockmodel.domain.StockModelTongJi;

public interface StockModelTongJiService {
	boolean save(StockModelTongJi st);
	StockModelTongJi getLastOne(String modelName);
	boolean saveorupdate (StockModelTongJi st);
	boolean removeAll ();
	List<StockModelTongJi> getLastData (String modelName, int num);
	TongJiForm getTongJiForm (String modelName);
}
