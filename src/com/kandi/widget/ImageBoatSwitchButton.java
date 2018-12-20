package com.kandi.widget;

import com.kandi.util.BitmapUtil;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class ImageBoatSwitchButton implements OnClickListener, OnTouchListener {

	public static class ImageSwitchButton implements OnClickListener, OnTouchListener{
		public ImageButton btn;
		public int imgIdOn;
		public int imgIdOff;
		public int imgIdOff2;
		public int imgIdOn_disabled;
		public int imgIdOff_disabled;
		public int imgIdOff2_disabled;
		
		public final static int SWITCH_ON = 1; 
		public final static int SWITCH_OFF = 2; 
		public final static int SWITCH_OFF2 = 3; 
		public final static int SWITCH_ON_DISABLED = 11; 
		public final static int SWITCH_OFF_DISABLED = 12; 
		public final static int SWITCH_OFF2_DISABLED = 13; 
		
		int state;
		boolean isSelfLocking = false;
		
		Fragment parentFragment;
		
		private OnClickListener _clickListener;
		private OnTouchListener _touchListener;
		
		public void setOnTouchListener(OnTouchListener otl) {
			_touchListener = otl;
		}
		
		public void setOnClickListener(OnClickListener ocl) {
			_clickListener = ocl;
		}

		public ImageSwitchButton(View rootView, Fragment parentView, int btnId, int imgIdOn, int imgIdOff, int imgIdOff2, int imgIdOn_disabled, int imgIdOff_disabled, int imgIdOff2_disabled) {
			this.imgIdOn = imgIdOn;
			this.imgIdOff = imgIdOff;
			this.imgIdOff2 = imgIdOff2;
			this.imgIdOn = imgIdOn_disabled;
			this.imgIdOff = imgIdOff_disabled;
			this.imgIdOff2 = imgIdOff2_disabled;

			state = SWITCH_OFF;
			this.parentFragment = parentView;

			this.btn =  (ImageButton) rootView.findViewById(btnId);
			btn.setOnTouchListener(this);
			btn.setOnClickListener(this);
			
		}
		
		public void setSwitchState(int state) {
			//Bitmap bitmap;
			//BitmapDrawable bd;

			switch(state) {
			case SWITCH_ON:
				//bitmap = BitmapUtil.makeBitMap(parentFragment.getResources(), imgIdOn);
				btn.setImageResource(imgIdOn);
				break;
			case SWITCH_OFF:
				//bitmap = BitmapUtil.makeBitMap(parentFragment.getResources(), imgIdOff);
				btn.setImageResource(imgIdOff);
				break;
			case SWITCH_OFF2:
				//bitmap = BitmapUtil.makeBitMap(parentFragment.getResources(), imgIdOff2);
				btn.setImageResource(imgIdOff2);
				break;
			case SWITCH_ON_DISABLED:
				//bitmap = BitmapUtil.makeBitMap(parentFragment.getResources(), imgIdOn_disabled);
				btn.setImageResource(imgIdOn_disabled);
				break;
			case SWITCH_OFF_DISABLED:
				//bitmap = BitmapUtil.makeBitMap(parentFragment.getResources(), imgIdOff_disabled);
				btn.setImageResource(imgIdOff_disabled);
				break;
			case SWITCH_OFF2_DISABLED:
				//bitmap = BitmapUtil.makeBitMap(parentFragment.getResources(), imgIdOff2_disabled);
				btn.setImageResource(imgIdOff2_disabled);
				break;
			default:
				return;
			}
			
//			bd = new BitmapDrawable(parentFragment.getResources(), bitmap);
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
	
	public final static int SWITCH_OFF = 0; 
	public final static int SWITCH_ON_A = 1; 
	public final static int SWITCH_ON_B = 2; 
	
	public ImageSwitchButton btnA;
	public ImageSwitchButton btnB;
	boolean isSelfLocking;
	
	private int boatSwitchState = -1;
	private OnClickListener _onClickListener;
	private OnTouchListener _onTouchListener;
	
	public ImageBoatSwitchButton(ImageSwitchButton buttonA, ImageSwitchButton buttonB, 	boolean isSelfLocking) {
		btnA = buttonA;
		btnB = buttonB;
		this.isSelfLocking = isSelfLocking;
		
		btnA.setOnTouchListener(this);
		btnA.setOnClickListener(this);

		btnB.setOnTouchListener(this);
		btnB.setOnClickListener(this);
	}
	
	public void setOnTouchListener(OnTouchListener otl) {
		_onTouchListener = otl;
	}
	
	public void setOnClickListener(OnClickListener ocl) {
		_onClickListener = ocl;
	}
	

	public void switchOnButton(int bootSwitchState) {

		switch(bootSwitchState) {
		case SWITCH_OFF:
			//button off
			btnA.setSwitchState(ImageSwitchButton.SWITCH_OFF);
			btnB.setSwitchState(ImageSwitchButton.SWITCH_OFF);
			break;
		case SWITCH_ON_A:
			//button a on
			btnA.setSwitchState(ImageSwitchButton.SWITCH_ON);
			btnB.setSwitchState(ImageSwitchButton.SWITCH_OFF2);
			break;
		case SWITCH_ON_B:
			//button b on
			btnA.setSwitchState(ImageSwitchButton.SWITCH_OFF2);
			btnB.setSwitchState(ImageSwitchButton.SWITCH_ON);
			break;
		default:
			return;
		}
		this.boatSwitchState = bootSwitchState;
	}

	public int getBoatSwitchState() {
		return boatSwitchState;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction()==MotionEvent.ACTION_DOWN) {
			
			if(v.getId() == btnA.btn.getId()) {
				btnA.setSwitchState(ImageSwitchButton.SWITCH_ON);
				btnB.setSwitchState(ImageSwitchButton.SWITCH_OFF2);
				this.boatSwitchState = SWITCH_ON_A;
			}
			else if(v.getId() == btnB.btn.getId()){
				btnA.setSwitchState(ImageSwitchButton.SWITCH_OFF2);
				btnB.setSwitchState(ImageSwitchButton.SWITCH_ON);
				this.boatSwitchState = SWITCH_ON_B;
			}
		}
		else if	(event.getAction()==MotionEvent.ACTION_UP){
			if(!isSelfLocking) {
				btnA.setSwitchState(ImageSwitchButton.SWITCH_OFF);
				btnB.setSwitchState(ImageSwitchButton.SWITCH_OFF);
				this.boatSwitchState = SWITCH_OFF;
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
