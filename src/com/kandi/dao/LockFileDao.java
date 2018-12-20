package com.kandi.dao;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.kandi.model.LockFileModel;

public class LockFileDao {
	public static List<LockFileModel> findLockFileByUrl(Context context,String path){
		FinalDb finalDb = FinalDb.create(context);
		return finalDb.findAllByWhere(LockFileModel.class, "path = '"+path+"'");
	}
	public static void addLockFile(Context context,LockFileModel fm){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.save(fm);
	}
	public static void unlockFile(Context context,LockFileModel lfm){
		FinalDb finalDb = FinalDb.create(context);
		finalDb.delete(lfm);
	}
	
}
