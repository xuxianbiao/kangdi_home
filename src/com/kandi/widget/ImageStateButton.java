package com.kandi.widget;

import com.kandi.util.BitmapUtil;
import com.kandi.widget.ImageInterlockSwitchButtonSet.ImageSwitchButton;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class ImageStateButton  implements OnClickListener, OnTouchListener, OnLongClickListener{
	public ImageButton btn;
	public int imgIdOn;
	public int imgIdOff;
	public int imgIdOn_press;
	public int imgIdOff_press;
	public int imgIdOn_disable;
	public int imgIdOff_disable;
	
	public final static int SWITCH_OFF = 0; 
	public final static int SWITCH_ON = 1; 
	public final static int SWITCH_OFF_PRESS = 2; 
	public final static int SWITCH_ON_PRESS = 3; 
	public final static int SWITCH_OFF_DISABLE = 4; 
	public final static int SWITCH_ON_DISABLE = 5; 
	
	boolean isSwitchStateByClick=true;
	int state;
	
	public int getState() {
		return state;
	};
	
	//Fragment parentFragment;
	//View rootView;
	Resources resources;
	
	private OnClickListener _onClickListener;
	private OnTouchListener _onTouchListener;
	private OnLongClickListener _onLongClickListener;
	
	public void setOnTouchListener(OnTouchListener otl) {
		_onTouchListener = otl;
	}
	
	public void setOnClickListener(OnClickListener ocl) {
		_onClickListener = ocl;
	}

	public void setOnLongClickListener(OnLongClickListener ocl) {
		_onLongClickListener = ocl;
	}

	public ImageStateButton(Resources resources, ImageButton btn, int imgIdOff, int imgIdOn, int imgIdOff_press, int imgIdOn_press, int imgId_disable) {
		init(resources, btn, imgIdOff, imgIdOn, imgIdOff_press, imgIdOn_press, imgId_disable, imgId_disable, true);
	}

	public ImageStateButton(Resources resources, ImageButton btn, int imgIdOff, int imgIdOn, int imgIdOff_press, int imgIdOn_press, int imgIdOff_disable, int imgIdOn_disable) {
		init(resources, btn, imgIdOff, imgIdOn, imgIdOff_press, imgIdOn_press, imgIdOff_disable, imgIdOn_disable, true);
	}
	
	/**
	 * 
	 * @param resources
	 * @param btn
	 * @param imgIdOff
	 * @param imgIdOn
	 * @param imgIdOff_press
	 * @param imgIdOn_press
	 * @param imgIdOff_disable
	 * @param imgIdOn_disable
	 * @param isSwitchStateByClick		指定按钮点击后是否切换on/off状态，true:点击按钮后切换on/off状态；false:点击按钮后不切换on/off状态，可用程序切换
	 */
	public ImageStateButton(Resources resources, ImageButton btn, int imgIdOff, int imgIdOn, int imgIdOff_press, int imgIdOn_press, int imgIdOff_disable, int imgIdOn_disable, boolean isSwitchStateByClick) {
		init(resources, btn, imgIdOff, imgIdOn, imgIdOff_press, imgIdOn_press, imgIdOff_disable, imgIdOn_disable, isSwitchStateByClick);
	}

	/**
	 * 
	 * @param resources
	 * @param btn
	 * @param imgIdOff
	 * @param imgIdOn
	 * @param imgIdOff_press
	 * @param imgIdOn_press
	 * @param imgIdOff_disable
	 * @param imgIdOn_disable
	 * @param isSwitchStateByClick		指定按钮点击后是否切换on/off状态，true:点击按钮后切换on/off状态；false:点击按钮后不切换on/off状态，可用程序切换
	 */
	public void init(Resources resources, ImageButton btn, int imgIdOff, int imgIdOn, int imgIdOff_press, int imgIdOn_press, int imgIdOff_disable, int imgIdOn_disable, boolean isSwitchStateByClick) {
		this.resources = resources;
		
		this.imgIdOn = imgIdOn;
		this.imgIdOff = imgIdOff;
		this.imgIdOn_press = imgIdOn_press;
		this.imgIdOff_press = imgIdOff_press;
		this.imgIdOn_disable = imgIdOn_disable;
		this.imgIdOff_disable = imgIdOff_disable;
		
		this.btn = btn; // (ImageButton) rootView.findViewById(btnId);
		btn.setOnTouchListener(this);
		btn.setOnClickListener(this);
		btn.setOnLongClickListener(this);
		
		this.isSwitchStateByClick = isSwitchStateByClick;
		
		state = SWITCH_OFF;
	}
	
	public void setSwitchState(int state) {
		//Bitmap bitmap;
		//BitmapDrawable bd;

		switch(state) {
		case SWITCH_OFF:
			//bitmap = BitmapUtil.makeBitMap(this.resources, imgIdOff);
			btn.setImageResource(imgIdOff);
			break;
		case SWITCH_ON:
			//bitmap = BitmapUtil.makeBitMap(this.resources, imgIdOn);
			btn.setImageResource(imgIdOn);
			break;
		case SWITCH_OFF_PRESS:
			//bitmap = BitmapUtil.makeBitMap(this.resources, imgIdOff_press);
			btn.setImageResource(imgIdOff_press);
			break;
		case SWITCH_ON_PRESS:
			//bitmap = BitmapUtil.makeBitMap(this.resources, imgIdOn_press);
			btn.setImageResource(imgIdOn_press);
			break;
		case SWITCH_OFF_DISABLE:
			//bitmap = BitmapUtil.makeBitMap(this.resources, imgIdOff_disable);
			btn.setImageResource(imgIdOff_disable);
			break;
		case SWITCH_ON_DISABLE:
			//bitmap = BitmapUtil.makeBitMap(this.resources, imgIdOn_disable);
			btn.setImageResource(imgIdOn_disable);
			break;
		default:
			return;
		}
		
//		bd = new BitmapDrawable(this.resources, bitmap);
//		btn.setImageDrawable(bd);

		this.state = state;
	}
	
	/**
	 * switch on button
	 * @param isSwitchOn	true:switch on, or false:switch off
	 * @param isUserEvent	true:the event is from user, perform onClick event, false:no onClick event to be performed
	 */
	public void switchOn(boolean isSwitchOn, boolean isUserEvent) {
		setSwitchState(isSwitchOn?ImageStateButton.SWITCH_ON:ImageStateButton.SWITCH_OFF);
		if(isUserEvent) {
			btn.performClick();
		}
	}

	public boolean isSwitchOn() {
		return (state == ImageStateButton.SWITCH_ON);
	}
	
	public void setEnabled(boolean isEnabled) {
		if(isEnabled) {
			switch(this.state) {
			case ImageStateButton.SWITCH_OFF_DISABLE:
				setSwitchState(ImageStateButton.SWITCH_OFF);
				break;
			case ImageStateButton.SWITCH_ON_DISABLE:
				setSwitchState(ImageStateButton.SWITCH_ON);
				break;
			}
		}
		else {
			switch(this.state) {
			case ImageStateButton.SWITCH_OFF:
			case ImageStateButton.SWITCH_OFF_PRESS:
				setSwitchState(ImageStateButton.SWITCH_OFF_DISABLE);
				break;
			case ImageStateButton.SWITCH_ON:
			case ImageStateButton.SWITCH_ON_PRESS:
				setSwitchState(ImageStateButton.SWITCH_ON_DISABLE);
				break;
			}
		}
		btn.setEnabled(isEnabled);
	}
	
	public boolean isEnabled() {
		return ((state != SWITCH_OFF_DISABLE) && (state != SWITCH_ON_DISABLE));
	}
	
	public boolean isSwitchStateByClick() {
		return isSwitchStateByClick;
	}
	
	public void setSwitchStateByClick(boolean flag) {
		this.isSwitchStateByClick = flag;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
        //Log.e("ImageStateButton", "event=" + event.getAction() +"      state="+state); 

		if(isEnabled()) {
			int x = (int)event.getX();
			int y = (int)event.getY();

			
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				if(state == SWITCH_OFF) {
					setSwitchState(SWITCH_OFF_PRESS);
				}
				else if(state == SWITCH_ON) {
					setSwitchState(SWITCH_ON_PRESS);
				}
			}
			else if((x<0) || (y<0) || (x >= btn.getWidth()) || (y>=btn.getHeight()) || (event.getAction()==MotionEvent.ACTION_CANCEL)) {
				//当按钮按下后触点移出按钮区域则取消该按钮事件
				if(state == SWITCH_OFF_PRESS) {
					setSwitchState(SWITCH_OFF);
				}
				else if(state == SWITCH_ON_PRESS) {
					setSwitchState(SWITCH_ON);
				}
				return false;
			}
			else if(event.getAction()==MotionEvent.ACTION_UP){
				if(isSwitchStateByClick) {
					if(state == SWITCH_OFF_PRESS) {
						setSwitchState(SWITCH_ON);
					}
					else if(state == SWITCH_ON_PRESS) {
						setSwitchState(SWITCH_OFF);
					}
				}
				else {
					if(state == SWITCH_OFF_PRESS) {
						setSwitchState(SWITCH_OFF);
					}
					else if(state == SWITCH_ON_PRESS) {
						setSwitchState(SWITCH_ON);
					}
				}
			}
		}

		if(_onTouchListener != null) {
			return _onTouchListener.onTouch(v, event);
		}
		else {
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(_onClickListener != null) {
			_onClickListener.onClick(v);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(_onLongClickListener != null) {
			return _onLongClickListener.onLongClick(v);
		}		
		return false;
	}
}
