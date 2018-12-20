/**
 * PassThroughButton
 * 
 * NOTE:
 * call back method PassThroughButton.onTouchEvent() is invoked after OnTouchListener.OnTouch()
 * 
 * CODE SAMPLE OF USING 
 * OnTouchListener.OnTouch() {
 		if((me.getAction()==MotionEvent.ACTION_DOWN) && (!btnMainCarSettingLeftWinDown.isOnPassThroughArea(me))) {
			// do action down ...
		}
		else if((me.getAction()==MotionEvent.ACTION_UP) || (me.getAction()==MotionEvent.ACTION_CANCEL)) {
			// do action up or cancle ...
		}
	}
 */

package com.kandi.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

@SuppressLint("ClickableViewAccessibility")
public class PassThroughButton extends Button {
    private Bitmap mBitmap;
    private int mAlphaThreshold=0x80;

    public PassThroughButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Log.e("H3c", "csd");
    }

    int getAlphaThreshold() {
    	return mAlphaThreshold;
    }
	
    void setAlphaThreshold(int alpha) {
    	mAlphaThreshold = alpha;
    }
    
	public boolean isOnPassThroughArea(MotionEvent event) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		if((x<0) || (y<0) || (x >= mBitmap.getWidth()) || (y>=mBitmap.getHeight())) {
			return true;
		}
        int color = mBitmap.getPixel(x, y);
        if (((color >>24) & 0x000000FF) < mAlphaThreshold) {	//根据所点击按钮背景图像素Alpha值小于80判断为透明区域
        	return true;
        }
        else {
        	return false;
        }
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
     	//*
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int color = mBitmap.getPixel((int) event.getX(), (int) event.getY());
            //Log.e("H3c", "cl.." + String.format("%1$#9x", color));    //0xAARRGGBB
            if (((color >>24) & 0x000000FF) < mAlphaThreshold) {	//根据所点击按钮背景图像素Alpha值小于80判断为透明区域
            	return false;  //bypass following MotionEvent of MOVE, UP, CANCLE events send to OnTouchListener
            }
        }

/*/    	
		//debug
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	isPressed=true;
            int color = mBitmap
                    .getPixel((int) event.getX(), (int) event.getY());
            Log.e("H3c", "cl.." + String.format("%1$#9x", color));
            if (color == 0) {
                return false;
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
        	isPressed=false;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isPressed) {
                int color = mBitmap.getPixel((int) event.getX(), (int) event.getY());
            	Log.e("H3c", "cl.." + String.format("%1$#9x", color));
            }
        }
//*/
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w == 0 && h == 0 && oldw == 0 && oldh == 0) {
            super.onSizeChanged(w, h, oldw, oldh);
        } else {
            final StateListDrawable bkg = (StateListDrawable) getBackground();
            mBitmap = Bitmap.createScaledBitmap(
                    ((BitmapDrawable) bkg.getCurrent()).getBitmap(),
                    getWidth(), getHeight(), true);
        }
    }
}
