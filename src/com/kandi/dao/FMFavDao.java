package com.kandi.dao;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.kandi.model.FMFavModel;

public class FMFavDao {
	public static List<FMFavModel> getAllFav(Context context,String orderBy){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAll(FMFavModel.class, orderBy);
	}
	public static List<FMFavModel> getAllFav(Context context){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAll(FMFavModel.class);
	}
	public static void addFav(Context context,FMFavModel fm){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.save(fm);
	}
	public static List<FMFavModel> findFavByUrl(Context context,String url){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAllByWhere(FMFavModel.class, "url = '"+url+"'");
	}
	public static FMFavModel findFavById(Context context,int id ){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findById(id, FMFavModel.class);
	}
	public static void deleteFavFmById(Context context,int id){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.deleteById(FMFavModel.class, id);
	}
}
