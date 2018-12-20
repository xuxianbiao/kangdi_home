package com.kandi.dao;

import java.util.ArrayList;
import java.util.List;

import com.kandi.model.FavModel;

import net.tsz.afinal.FinalDb;
import android.content.Context;


public class FavDao {
	public static List<FavModel> getAllFav(Context context,String orderBy){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAll(FavModel.class, orderBy);
	}
	public static List<FavModel> findAllFav(Context context){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAll(FavModel.class);
	}
	public static void addFav(Context context,FavModel fm){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.save(fm);
	}
	public static List<FavModel> findFavByUrl(Context context,String url){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAllByWhere(FavModel.class, "url = '"+url+"'");
	}
	
	public static List<FavModel> findFavItemByUrl(Context context,String url){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAllByWhere(FavModel.class, "url = '"+url+"' and favtype='0'");
	}
	public static List<FavModel> findFavByFavType(Context context,String favtype){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAllByWhere(FavModel.class, "favtype = '"+favtype+"' order by id desc limit 20");
	}
	public static void deleteFavByUrl(Context context,String url){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.deleteByWhere(FavModel.class, "url = '"+url+"'");
	}
	public static FavModel findFavById(Context context,int id ){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findById(id, FavModel.class);
	}
	public static void deleteFavById(Context context, int id){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.deleteById(FavModel.class, id);
	}
	public static List<FavModel> findFavByPage(Context context,int pageNo){
		FinalDb finalDb = FinalDb.create(context);
		List<FavModel> list = finalDb.findAll(FavModel.class);
		List<FavModel> resList = new ArrayList<FavModel>();
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
