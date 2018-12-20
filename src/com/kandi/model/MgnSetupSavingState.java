package com.kandi.model;

import java.util.Set;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.kandi.event.DRI_PARA_Event;
import com.kandi.event.base.BaseEvent;

import de.greenrobot.event.EventBus;

public class MgnSetupSavingState {

	private static MgnSetupSavingState _instance;
	public static MgnSetupSavingState getInstance() {
		if(_instance == null) {
			_instance = new MgnSetupSavingState();
		}
		return _instance;
	}
	private MgnSetupSavingState() {
		EventBus.getDefault().register(this,"DRI_PARA_Event",DRI_PARA_Event.class);

	}
	
	
	private boolean _isWriting=false;
	public boolean isSaving() { return _isWriting; }
	public void setIsSaving(boolean isWriting) { 
		_isWriting = isWriting; 
		_isCfgOK = false;
		}
	
	private boolean _isCfgOK;
	public boolean isCfgSuccess() { return _isCfgOK; }
	
	
	public void DRI_PARA_Event(DRI_PARA_Event event) {
		
		//接收设置结果Event
		Bundle bundle = event.bundle;
		Message message = Message.obtain();
		message.setData(bundle);

		final String m_key="KD_CAST_EVENT";
			
    	Set<String> keySet = bundle.keySet();
	   	for(String key : keySet) {
	   		
   		if(!key.startsWith(m_key)) continue;
   		
   		int eventId;
   		try {
			eventId = Integer.parseInt(key.substring(m_key.length()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			continue;
		}

			if ((eventId < 0)
					|| (eventId >= BaseEvent.DRIEVENT.values().length))
				continue;
			BaseEvent.DRIEVENT ev = BaseEvent.DRIEVENT.values()[eventId];

			switch (ev) {
			case DRI_SYS_CFG:/** 参数设置事件 */
			{
				//*  参数设置    **  key+17      **  0:设置异常；1:设置完成       **  boolean
				_isCfgOK = bundle.getBoolean(key);
				if(_isCfgOK) {
					//Toast.makeText(this, "设置成功", Toast.LENGTH_LONG).show();

					MgnSetupSavingState.getInstance().setIsSaving(false);
					//loading_panel.setVisibility(View.GONE);

				}
				else {
					//Toast.makeText(this, "设置失败", Toast.LENGTH_LONG).show();
					//TODO:
					//refreshPannel();
				}
				
				//refreshPannel();
			}
				break;
			}
		}
	}
}
