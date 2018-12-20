package com.kandi.customview;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.utils.SharedPreferencesUtils;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandi.driver.radio.Configs;
import com.kandi.home.R;

public class HomeWeatherView extends RelativeLayout{
	private LayoutInflater inflater; 
	private ImageButton weathericon;
	private ImageButton bt_refresh;
	private TextView temperaturetxt;
	private TextView weathertxt;
	private TextView city;
	String[] weathers;
	int[] weather_imgs = new int[] {R.drawable.weather_4_03,
			R.drawable.weather_4_05,R.drawable.weather_4_11,
			R.drawable.weather_4_24,R.drawable.weather_4_24,
			R.drawable.weather_4_25,R.drawable.weather_4_25,
			R.drawable.weather_4_26,R.drawable.weather_4_26,
			R.drawable.weather_4_27,R.drawable.weather_4_27,
			R.drawable.weather_4_61,R.drawable.weather_4_61,
			R.drawable.weather_4_62,R.drawable.weather_4_62,
			R.drawable.weather_4_07,R.drawable.weather_4_13,
			R.drawable.weather_4_44,R.drawable.weather_4_15,
			R.drawable.weather_4_29,R.drawable.weather_4_28,
			R.drawable.weather_4_09,R.drawable.weather_4_29,
			R.drawable.weather_4_30,R.drawable.weather_4_30,
			R.drawable.weather_4_42,R.drawable.weather_4_42,
			R.drawable.weather_4_43,R.drawable.weather_4_43,
			R.drawable.weather_4_57,R.drawable.weather_4_65,
			R.drawable.weather_4_66,R.drawable.weather_4_55,
			R.drawable.weather_4_56,R.drawable.weather_4_63,
			R.drawable.weather_4_64};
	Map<String,Integer> weathermap;
	private static final String QUERY_WEATHER = "yunos.weather.action.query";
	private void initView(Context context){
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.home_weather, null);
		this.addView(view);
		weathericon = (ImageButton) view.findViewById(R.id.weathericon);
		bt_refresh = (ImageButton) view.findViewById(R.id.bt_refresh);
		bt_refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String cityname = SharedPreferencesUtils.getSharedPreferences(
						getContext()).getString(Configs.WEATHER_CITY,
						Configs.DEFAULT_WEATHER_CITY);
				Intent intent = new Intent();
				intent.putExtra("cityname", cityname);
				intent.setAction(QUERY_WEATHER);  
				getContext().sendBroadcast(intent,null);
				Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_circle);  
				LinearInterpolator lin = new LinearInterpolator();  
				operatingAnim.setInterpolator(lin);
				if (operatingAnim != null) { 
					bt_refresh.clearAnimation(); 
					bt_refresh.startAnimation(operatingAnim);  
				} 
			}
		});
		temperaturetxt = (TextView) view.findViewById(R.id.temperaturetxt);
		weathertxt = (TextView) view.findViewById(R.id.weathertxt);
		city = (TextView) view.findViewById(R.id.city);
		weathers = getResources().getStringArray(R.array.weather_condition);
		weathermap = new HashMap<String, Integer>();
		for(int i=0;i<weathers.length;i++){
			weathermap.put(weathers[i], weather_imgs[i]);
		}
		
	}
	public HomeWeatherView(Context context) {
		super(context);
		 this.initView(context);
	}
	public HomeWeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}
	public HomeWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.initView(context);
	}
	private void setWeatherIcon(String weather,ImageButton v,Context context){
//		if(weather.contains(context.getString(R.string.sunny))){
//			v.setImageResource(R.drawable.weather_4_03);
//		}else if(weather.contains(context.getString(R.string.cloudy))){
//			v.setImageResource(R.drawable.weather_4_05);
//		}else if(weather.contains(context.getString(R.string.snow))){
//			v.setImageResource(R.drawable.weather_4_30);
//		}else if(weather.contains(context.getString(R.string.rain))){
//			v.setImageResource(R.drawable.weather_4_24);
//		}else if(weather.contains(context.getString(R.string.thunder))){
//			v.setImageResource(R.drawable.weather_4_13);
//		}else if(weather.contains(context.getString(R.string.shade))){
//			v.setImageResource(R.drawable.weather_4_11);
//		}
		if(weathermap.get(weather)!=null){
			v.setImageResource(weathermap.get(weather));
		}
	}
	
	public void setWeather(Map<String, String> map,Context context){
		if(map != null){
			if(context.getString(R.string.position).equals(map.get("CityName"))){
				weathertxt.setText("N/A");
				temperaturetxt.setText("N/A");
				city.setText("N/A");
			}else{
				weathertxt.setText(map.get("TodayWeather"));
				temperaturetxt.setText(map.get("TodayTemp") + "°C");
				city.setText(map.get("CityName"));
				this.setWeatherIcon(map.get("TodayWeather"), weathericon, context);
			}
		}else{
			weathertxt.setText("N/A");
			temperaturetxt.setText("N/A");
			city.setText("N/A");
		}
	}
}
