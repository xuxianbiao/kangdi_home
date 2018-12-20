package com.kandi.widget;

import com.kandi.util.BitmapUtil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class ImageInterlockSwitchButtonSet implements OnClickListener, OnTouchListener {

	public static class ImageSwitchButton implements OnClickListener, OnTouchListener{
		public ImageButton btn;
		public int imgIdOn;
		public int imgIdOff;
		public int imgIdOn_press;
		public int imgIdOff_press;
		public int imgId_disable;
		public int imgId_disable_press;
		
		public final static int SWITCH_OFF = 0; 
		public final static int SWITCH_ON = 1; 
		public final static int SWITCH_OFF_PRESS = 2; 
		public final static int SWITCH_ON_PRESS = 3; 
		public final static int SWITCH_DISABLE = 4; 
		public final static int SWITCH_DISABLE_PRESS=5;
		
		int state;
		
		//Fragment parentFragment;
		Resources resources;
		
		private OnClickListener _clickListener;
		private OnTouchListener _touchListener;
		
		public void setOnTouchListener(OnTouchListener otl) {
			_touchListener = otl;
		}
		
		public void setOnClickListener(OnClickListener ocl) {
			_clickListener = ocl;
		}

		public ImageSwitchButton(Resources resources, ImageButton btn, int imgIdOff, int imgIdOn, int imgIdOff_press, int imgIdOn_press, int imgId_disable,int imgId_disable_press) {
			this.resources = resources;
			
			this.imgIdOn = imgIdOn;
			this.imgIdOff = imgIdOff;
			this.imgIdOn_press = imgIdOn_press;
			this.imgIdOff_press = imgIdOff_press;
			this.imgId_disable = imgId_disable;
			this.imgId_disable_press=imgId_disable_press;
			state = SWITCH_OFF;
			
			//this.parentFragment = parentView;

			this.btn = btn;
			btn.setOnTouchListener(this);
			btn.setOnClickListener(this);
			
		}
		
		public void setSwitchState(int state) {
			//Bitmap bitmap;
			//BitmapDrawable bd;

			switch(state) {
			case SWITCH_OFF:
				//bitmap = BitmapUtil.makeBitMap(resources, imgIdOff);
				btn.setImageResource(imgIdOff);
				break;
			case SWITCH_ON:
				//bitmap = BitmapUtil.makeBitMap(resources, imgIdOn);
				btn.setImageResource(imgIdOn);
				break;
			case SWITCH_OFF_PRESS:
				//bitmap = BitmapUtil.makeBitMap(resources, imgIdOff_press);
				btn.setImageResource(imgIdOff_press);
				break;
			case SWITCH_ON_PRESS:
				//bitmap = BitmapUtil.makeBitMap(resources, imgIdOn_press);
				btn.setImageResource(imgIdOn_press);
				break;
			case SWITCH_DISABLE:
				//bitmap = BitmapUtil.makeBitMap(resources, imgId_disable);
				btn.setImageResource(imgId_disable);
				break;
			case SWITCH_DISABLE_PRESS:
				//bitmap = BitmapUtil.makeBitMap(resources, imgId_disable);
				btn.setImageResource(imgId_disable_press);
				break;
			default:
				return;
			}
			
//			bd = new BitmapDrawable(resources, bitmap);
//			btn.setImageDrawable(bd);

		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			
			if(_touchListener != null) {
				return _touchListener.onTouch(v, event);
			}
			else {
				return false;
			}
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(_clickListener != null) {
				_clickListener.onClick(v);
			}
		}
	}
	
	public ImageSwitchButton[] arrButton;
	private int indexOnButton = 0;
	private OnClickListener _onClickListener;
	private OnTouchListener _onTouchListener;
	
	public void setOnTouchListener(OnTouchListener otl) {
		_onTouchListener = otl;
	}
	
	public void setOnClickListener(OnClickListener ocl) {
		_onClickListener = ocl;
	}
	
	public ImageInterlockSwitchButtonSet(int switchNum) {
		arrButton = new ImageSwitchButton[switchNum];
	}
	
	public ImageInterlockSwitchButtonSet(ImageSwitchButton[] arrButton) {
		this.arrButton = arrButton;
		for(int i=0; i<arrButton.length; i++) {
			///arrButton[i].init();
			arrButton[i].btn.setOnTouchListener((OnTouchListener)this);
			arrButton[i].btn.setOnClickListener((OnClickListener)this);
		}
	}
	/**
	 * Switch on button
	 * @param index 	the index that the button to be switched on, set -1 to switch off all buttons
	 * @param isUserEvent 	true:the event is from user, perform onClick event, false:no onClick event to be performed
	 */
	public void switchOnButton(int index, boolean isUserEvent) {
		//if((indexOnButton >= 0) && (indexOnButton < arrButton.length)) {
			for(int i=0; i<arrButton.length; i++) {
				if(i==index) {
					arrButton[i].setSwitchState(ImageSwitchButton.SWITCH_ON);
					
					if(isUserEvent) {
						arrButton[i].btn.performClick();
					}
				}
				else {
					arrButton[i].setSwitchState(ImageSwitchButton.SWITCH_OFF);
				}
			}
		//}
		indexOnButton = index;
	}
	public int getOnButton() {
		return indexOnButton;
	}
	
	public void init()
	{
		for(int i=0; i<arrButton.length; i++) {
			///arrButton[i].init();
			arrButton[i].btn.setOnTouchListener((OnTouchListener)this);
			arrButton[i].btn.setOnClickListener((OnClickListener)this);
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction()==MotionEvent.ACTION_DOWN) {
			
			if((indexOnButton >= 0) && (indexOnButton < arrButton.length) && (v.getId() == arrButton[indexOnButton].btn.getId())) {
				//按下当前激活的按钮
				arrButton[indexOnButton].setSwitchState(ImageSwitchButton.SWITCH_ON_PRESS);
				return true; //
			}
			else {
				//按下当前非激活的按钮
				for(int i=0; i<arrButton.length; i++) {
					if(i == indexOnButton) {
						arrButton[i].setSwitchState(ImageSwitchButton.SWITCH_OFF);
					}
					else if(v.getId() == arrButton[i].btn.getId()) {
						arrButton[i].setSwitchState(ImageSwitchButton.SWITCH_ON_PRESS);
						//indexOnButton = i;	//set my event
					}
				}				
			}
		}
		else if	(event.getAction()==MotionEvent.ACTION_UP){
			for(int i=0; i<arrButton.length; i++) {
				if(v.getId() == arrButton[i].btn.getId()) {
					indexOnButton = i;	//set my event
					arrButton[i].setSwitchState(ImageSwitchButton.SWITCH_ON);
				}
				else {
					arrButton[i].setSwitchState(ImageSwitchButton.SWITCH_OFF);
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

}
