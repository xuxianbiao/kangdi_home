package com.kandi.customview;

import java.math.BigDecimal;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.kandi.home.R;

public class HomeMileView extends RelativeLayout{
	enum COLOR {WRITE,YELLOW,RED};
	
	int mileLevelYellow =30;
	int mileLevelRed =10;
	int socLevelYellow=30;
	int socLevelRed=10;
	
	private LayoutInflater inflater; 
	private ImageButton kmnum1;
	private ImageButton kmnum2;
	private ImageButton kmnum3;
	private ImageButton percentno1;
	private ImageButton percentno2;
	private ImageButton percentno3;
	
	private ImageButton energyicon;
	private boolean isGunInserted = false;
	private final double MILEUTIL = 1.609344; // 英里-公里换算单位
	
	///private TextView chargingtxt;
	private void initView(Context context){
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.home_mile, null);
		RelativeLayout.LayoutParams rllp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.addView(view, rllp);
		kmnum1 = (ImageButton) view.findViewById(R.id.kmnum1);
		kmnum2 = (ImageButton) view.findViewById(R.id.kmnum2);
		kmnum3 = (ImageButton) view.findViewById(R.id.kmnum3);
		percentno1 = (ImageButton) view.findViewById(R.id.percentno1);
		percentno2 = (ImageButton) view.findViewById(R.id.percentno2);
		percentno3 = (ImageButton) view.findViewById(R.id.percentno3);
		
		energyicon = (ImageButton) view.findViewById(R.id.energyicon);
		//chargingtxt = (TextView) view.findViewById(R.id.chargingtxt);
	}
	public void fireChargeInsert(){
		isGunInserted = true;
		energyicon.setImageResource(R.drawable.home_tophalf_icon_energy_charge);
	}
	public void fireChargePull(){
		isGunInserted = false;
		//chargingtxt.setVisibility(View.GONE);
	}
	public void fireChargeStart(){
		//chargingtxt.setVisibility(View.VISIBLE);
	}
	
	public void fireChargeStop(){
		//chargingtxt.setVisibility(View.GONE);
	}

	public HomeMileView(Context context) {
		super(context);
		 this.initView(context);
	}
	public HomeMileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}
	public HomeMileView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.initView(context);
	}
	
	public void setMileColorLevel(int levelYellow, int levelRed) {
		mileLevelYellow = levelYellow;
		mileLevelRed = levelRed;
	}
	public void setSocColorLevel(int levelYellow, int levelRed) {
		socLevelYellow = levelYellow;
		socLevelRed = levelRed;
	}
	private void setNoW(int no,ImageButton v){
		switch (no) {
		case 0:
			v.setImageResource(R.drawable.home_tophalf_0);
			break;
		case 1:
			v.setImageResource(R.drawable.home_tophalf_1);
			break;
		case 2:
			v.setImageResource(R.drawable.home_tophalf_2);
			break;
		case 3:
			v.setImageResource(R.drawable.home_tophalf_3);
			break;
		case 4:
			v.setImageResource(R.drawable.home_tophalf_4);
			break;
		case 5:
			v.setImageResource(R.drawable.home_tophalf_5);
			break;
		case 6:
			v.setImageResource(R.drawable.home_tophalf_6);
			break;
		case 7:
			v.setImageResource(R.drawable.home_tophalf_7);
			break;
		case 8:
			v.setImageResource(R.drawable.home_tophalf_8);
			break;
		case 9:
			v.setImageResource(R.drawable.home_tophalf_9);
			break;
		default:
			break;
		}
	}
	private void setNoY(int no,ImageButton v){
		switch (no) {
		case 0:
			v.setImageResource(R.drawable.home_tophalf_0_02);
			break;
		case 1:
			v.setImageResource(R.drawable.home_tophalf_1_02);
			break;
		case 2:
			v.setImageResource(R.drawable.home_tophalf_2_02);
			break;
		case 3:
			v.setImageResource(R.drawable.home_tophalf_3_02);
			break;
		case 4:
			v.setImageResource(R.drawable.home_tophalf_4_02);
			break;
		case 5:
			v.setImageResource(R.drawable.home_tophalf_5_02);
			break;
		case 6:
			v.setImageResource(R.drawable.home_tophalf_6_02);
			break;
		case 7:
			v.setImageResource(R.drawable.home_tophalf_7_02);
			break;
		case 8:
			v.setImageResource(R.drawable.home_tophalf_8_02);
			break;
		case 9:
			v.setImageResource(R.drawable.home_tophalf_9_02);
			break;
		default:
			break;
		}
	}
	private void setNoR(int no,ImageButton v){
		switch (no) {
		case 0:
			v.setImageResource(R.drawable.home_tophalf_0_03);
			break;
		case 1:
			v.setImageResource(R.drawable.home_tophalf_1_03);
			break;
		case 2:
			v.setImageResource(R.drawable.home_tophalf_2_03);
			break;
		case 3:
			v.setImageResource(R.drawable.home_tophalf_3_03);
			break;
		case 4:
			v.setImageResource(R.drawable.home_tophalf_4_03);
			break;
		case 5:
			v.setImageResource(R.drawable.home_tophalf_5_03);
			break;
		case 6:
			v.setImageResource(R.drawable.home_tophalf_6_03);
			break;
		case 7:
			v.setImageResource(R.drawable.home_tophalf_7_03);
			break;
		case 8:
			v.setImageResource(R.drawable.home_tophalf_8_03);
			break;
		case 9:
			v.setImageResource(R.drawable.home_tophalf_9_03);
			break;
		default:
			break;
		}
	}
	
	private void setNo(int no,ImageButton v, COLOR color){
		switch(color) {
		case WRITE:
			setNoW(no, v);
			break;
		case YELLOW:
			setNoY(no, v);
			break;
		case RED:
			setNoR(no, v);
			break;
		}
	}
	private void setMileTitle(int percent){
		if(!isGunInserted){
			if(percent<10){
				energyicon.setImageResource(R.drawable.home_tophalf_icon_energy_0);
			}else if(percent>=10&&percent<25){
				energyicon.setImageResource(R.drawable.home_tophalf_icon_energy_1);
			}else if(percent>=25&&percent<50){
				energyicon.setImageResource(R.drawable.home_tophalf_icon_energy_2);
			}else if(percent>=50&&percent<75){
				energyicon.setImageResource(R.drawable.home_tophalf_icon_energy_3);
			}else if(percent>=75&&percent<90){
				energyicon.setImageResource(R.drawable.home_tophalf_icon_energy_4);
			}else{
				energyicon.setImageResource(R.drawable.home_tophalf_icon_energy_5);
			}
		}
		
	}
	//设置公里信息
	public void setMile(int mile,int percent, int soc){
		
		int hNo,tNo,oNo;
		
		if(mile > 999) mile = 999;
		if(percent > 999) percent = 999;
		
		// 获取系统语言，如果是英文则换算成英里，1英里=1.609344公里
		if(Locale.getDefault().getLanguage().equals("en")) { // 如果是英文，则四舍五入换算
			double mi = mile/MILEUTIL;
			mile = new BigDecimal(mi).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		}
		
		COLOR clrMile;
		if(mile <= mileLevelRed) {
			clrMile = COLOR.RED;
		}
		else if(mile <= mileLevelYellow) {
			clrMile = COLOR.YELLOW;
		}
		else {
			clrMile = COLOR.WRITE;
		}
		
		hNo = (mile/100)%10;
		tNo = (mile/10)%10;
		oNo = mile%10;

		this.setNo(hNo, kmnum1, clrMile);
		this.setNo(tNo, kmnum2, clrMile);
		this.setNo(oNo, kmnum3, clrMile);

		kmnum1.setVisibility((mile >=100)?View.VISIBLE:View.GONE);
		kmnum2.setVisibility((mile >=10)?View.VISIBLE:View.GONE);
		kmnum3.setVisibility(View.VISIBLE);
		

		COLOR clrSoc;
		if(soc <= socLevelRed) {
			clrSoc = COLOR.RED;
		}
		else if(soc <= socLevelYellow) {
			clrSoc = COLOR.YELLOW;
		}
		else {
			clrSoc = COLOR.WRITE;
		}

		hNo = (percent/100)%10;
		tNo = (percent/10)%10;
		oNo = percent%10;

		this.setNo(hNo, percentno1, clrSoc);
		this.setNo(tNo, percentno2, clrSoc);
		this.setNo(oNo, percentno3, clrSoc);

		percentno1.setVisibility((percent >=100)?View.VISIBLE:View.GONE);
		percentno2.setVisibility((percent >=10)?View.VISIBLE:View.GONE);
		percentno3.setVisibility(View.VISIBLE);
		
		setMileTitle(soc);
	}
}
