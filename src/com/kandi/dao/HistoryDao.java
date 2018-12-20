package com.kandi.dao;

import java.util.ArrayList;
import java.util.List;

import com.kandi.model.HistoryModel;

import net.tsz.afinal.FinalDb;
import android.content.Context;


public class HistoryDao {
	public static List<HistoryModel> getAllFav(Context context,String orderBy){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAll(HistoryModel.class, orderBy);
	}
	public static List<HistoryModel> findAllHistory(Context context){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAll(HistoryModel.class);
	}
	public static void addFav(Context context,HistoryModel fm){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.save(fm);
	}
	public static List<HistoryModel> findFavByUrl(Context context,String url){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAllByWhere(HistoryModel.class, "url = '"+url+"' order by id desc limit 20");
	}
	public static void deleteFavByUrl(Context context,String url){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.deleteByWhere(HistoryModel.class, "url = '"+url+"'");
	}
	public static HistoryModel findFavById(Context context,int id ){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findById(id, HistoryModel.class);
	}
	public static void deleteFavById(Context context, int id){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.deleteById(HistoryModel.class, id);
	}
	public static List<HistoryModel> findFavByPage(Context context,int pageNo){
		FinalDb finalDb = FinalDb.create(context);
		List<HistoryModel> list = finalDb.findAll(HistoryModel.class);
		List<HistoryModel> resList = new ArrayList<HistoryModel>();
		for(int i =0;i<list.size();i++){
			int start = (pageNo-1)*9;
			int end = (pageNo-1)*9+9;
			if(i>=start&&i<end){
				resList.add(list.get(i));
			}
		}
		return resList;
	}
}
