package com.kandi.util;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class BitmapUtil {
    
    public static synchronized Bitmap decodeSampledBitmapFromStream(
            InputStream in, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in, null, options);
    }
    public static synchronized int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        while (width / inSampleSize > reqWidth) {
            inSampleSize++;
        }
        while (height / inSampleSize > reqHeight) {
            inSampleSize++;
        }
        return inSampleSize;
    }
	public static synchronized Bitmap makeBitMap(Resources res, int resId){   
		BitmapFactory.Options opt = new BitmapFactory.Options();    
		opt.inPreferredConfig = Bitmap.Config.RGB_565;     
		opt.inPurgeable = true;    
		opt.inInputShareable = true;    
		InputStream is = res.openRawResource(resId);    
		return BitmapFactory.decodeStream(is,null,opt);    
	} 
	public static synchronized void updateViewBg(View v,Resources res, int resId){   
		BitmapFactory.Options opt = new BitmapFactory.Options();    
		opt.inPreferredConfig = Bitmap.Config.RGB_565;     
		opt.inPurgeable = true;    
		opt.inInputShareable = true;   
		opt.inSampleSize = 1;
		InputStream is = res.openRawResource(resId);    
		Bitmap bitmap = BitmapFactory.decodeStream(is,null,opt);
		
		bitmap = BitmapUtil.decodeSampledBitmapFromStream(is, 1000, 1000);
		
		BitmapDrawable bd = new BitmapDrawable(res,bitmap);
		v.setBackgroundDrawable(bd);
	} 
	
	public Bitmap decodebitmap(Resources res, int imageid,int scal) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeResource(res,
				imageid, options);
		if (bitmap == null) {
			return null;
		}
		options.inSampleSize = scal;
		options.inJustDecodeBounds = false;
		
		options.inPreferredConfig = Bitmap.Config.RGB_565;     
		options.inPurgeable = true;    
		options.inInputShareable = true; 
		bitmap = BitmapFactory.decodeResource(res, imageid,
				options);

		return bitmap;

	}
}
