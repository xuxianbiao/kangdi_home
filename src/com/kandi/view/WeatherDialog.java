package com.kandi.view;

import net.tsz.afinal.utils.SharedPreferencesUtils;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kandi.driver.radio.Configs;
import com.kandi.home.R;

public class WeatherDialog extends Dialog {

	private EditText net_address;
	private Button btn_sure_city;
	private static final String QUERY_WEATHER = "yunos.weather.action.query";
	
	public WeatherDialog(Context context) {
		super(context,R.style.my_dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_select_weather);
		net_address = (EditText) findViewById(R.id.net_address);
		net_address.setText("");
		btn_sure_city = (Button) findViewById(R.id.btn_sure_city);
		btn_sure_city.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("cityname", net_address.getText().toString());
				intent.setAction(QUERY_WEATHER);  
				getContext().sendBroadcast(intent,null);
				Editor edt = SharedPreferencesUtils.getEditor(getContext());
				edt.putString(Configs.WEATHER_CITY, net_address.getText().toString());
				edt.commit();
				dismiss();
			}
		});
	}

}
