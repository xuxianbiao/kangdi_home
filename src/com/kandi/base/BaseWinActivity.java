package com.kandi.base;

import com.kandi.event.BottomFloatOptionMenuHideEvent;
import com.kandi.event.BottomFloatOptionMenuShowEvent;
import com.kandi.event.HomeHideMaskEvent;
import com.kandi.event.HomeShowMaskEvent;
import com.kandi.event.SideButtonHideEvent;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.view.KeyEvent;
/**The basic Activity for windows*/
public class BaseWinActivity extends Activity{
	@Override
	protected void onResume(){
		super.onResume();
		try{
			EventBus.getDefault().postSticky(new SideButtonHideEvent());
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			EventBus.getDefault().postSticky(new BottomFloatOptionMenuHideEvent());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
