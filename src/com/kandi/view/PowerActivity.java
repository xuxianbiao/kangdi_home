package com.kandi.view;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kandi.home.R;
import com.kandi.customview.LineChartView;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EnergyInfoDetailsDriver;
import com.kandi.driver.EnergyInfoDriver;
import com.kandi.event.FinishPowerEvent;
import com.kandi.util.ChartUtil;
import com.util.ToastUtil;

import de.greenrobot.event.EventBus;

public class PowerActivity extends Activity implements Callback{
	enum TAG{Min5,Min15,Min30};

	private ImageView soc;
	private ImageView gonglv;
	private ImageView dianliu;
	private LineChartView lineChartView;
	private RelativeLayout layout_a;
	private RelativeLayout batt_icon_layout_a;
	private RelativeLayout batt_icon_layout_b;
	
	private ImageView battery_icon_a;
	private ImageView battery_icon_b;
	private TextView chager_name_a;
	private TextView chager_name_b;
	private TextView chager_number_a;
	private TextView chager_number_b;
	private ImageButton timedragbtn;
	private FrameLayout timedragbg;
	private ImageView left;
	private ImageView right;
	private int batteryNum = 0;
	private int currentPage = 1;
	private EnergyInfoDriver model;
	private Handler handler;
	private int currentNTrendType =2;	//默认SOC
	private int currentNDataLen = 5;
	private float soc1;
	private float soc2;
	private float soc3;
	private float soc4;
	private int soc_avg;
	private EnergyInfoDetailsDriver eidd1;
	private EnergyInfoDetailsDriver eidd2;
	private EnergyInfoDetailsDriver eidd3;
	private EnergyInfoDetailsDriver eidd4;
	private Timer timer;
	private TimerTask task;
	private ImageView touchPoint; 
	private ImageView touchPointLine; 
	private TextView avgvalue;
	
	private void initView(){
		
		avgvalue = (TextView) findViewById(R.id.avgvalue);
		chager_name_a = (TextView) findViewById(R.id.chager_name_a);
		chager_name_b = (TextView) findViewById(R.id.chager_name_b);
		chager_number_a = (TextView) findViewById(R.id.chager_number_a);
		chager_number_b = (TextView) findViewById(R.id.chager_number_b);
		batt_icon_layout_a = (RelativeLayout) findViewById(R.id.batt_icon_layout_a);
		batt_icon_layout_b = (RelativeLayout) findViewById(R.id.batt_icon_layout_b);
		left = (ImageView) findViewById(R.id.left);
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(currentPage==2){
					currentPage = 1;
				}
				refreshPannel();
			}
		});
		right = (ImageView) findViewById(R.id.right);
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("~~~~"+batteryNum);
				if(batteryNum>2){
					if(currentPage==1){
						currentPage = 2;
					}
				}
				refreshPannel();
			}
		});
		left.setEnabled(false);
		right.setEnabled(false);

		timedragbtn = (ImageButton) findViewById(R.id.timedragbtn);
		timedragbtn.setClickable(false);
		timedragbtn.setTag(TAG.Min5);
		
		timedragbg = (FrameLayout) findViewById(R.id.timedragbg);
		timedragbg.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				float x = arg1.getX()-50;
				//System.out.println("~~~~~~:"+x);
				x = (x<0)?0:((x>120)?120:x);
				float xx;
				
				TAG tag = (TAG)timedragbtn.getTag();
				Message msg = Message.obtain();
				if(x<=40){
					xx=0;
					msg.what=4;
					if(tag==null || tag != TAG.Min5) {
						timedragbtn.setTag(TAG.Min5);
						timedragbtn.setImageResource(R.drawable.energy_pattern_5min);
					};
				}else if(x<80){
					xx=60;
					msg.what=5;
					if(tag==null || tag != TAG.Min15) {
						timedragbtn.setTag(TAG.Min15);
						timedragbtn.setImageResource(R.drawable.energy_pattern_15min);
					};
				}else{
					xx=120;
					msg.what=6;
					if(tag==null || tag != TAG.Min30) {
						timedragbtn.setTag(TAG.Min30);
						timedragbtn.setImageResource(R.drawable.energy_pattern_30min);
					};
				}

				FrameLayout.LayoutParams fl = (LayoutParams) timedragbtn.getLayoutParams();
				fl.leftMargin = (arg1.getAction()==MotionEvent.ACTION_UP)?(int)xx:(int)x;
				timedragbtn.setLayoutParams(fl);

				if(arg1.getAction()==MotionEvent.ACTION_UP) {
					handler.sendMessage(msg);
				}

				return true;
			}
		});
		
		
		layout_a = (RelativeLayout) findViewById(R.id.layout_a_bottom);
		layout_a.setBackgroundResource(R.drawable.tap_line);
		battery_icon_a = (ImageView) findViewById(R.id.battery_icon_a);
		battery_icon_a.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int nBattIndex=0;
				if(currentPage==1){
					nBattIndex=0;	//电池组A
				}else if(currentPage==2){
					nBattIndex=2;	//电池组C
				}
				
				model = DriverServiceManger.getInstance().getEnergyInfoDriver();

				if((model != null) && model.isBattarySet(nBattIndex)) {
					Intent intent = new Intent(PowerActivity.this, PowerDetailActivity.class);
					intent.putExtra("nBattIndex",nBattIndex);	//传递当前选中的电池组
					PowerActivity.this.startActivity(intent);
					finish();
				}
			}
		});
		battery_icon_b = (ImageView) findViewById(R.id.battery_icon_b);
		battery_icon_b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int nBattIndex=0;
				if(currentPage==1){
					nBattIndex=1;	//电池组B
				}else if(currentPage==2){
					nBattIndex=3;	//电池组D
				}

				model = DriverServiceManger.getInstance().getEnergyInfoDriver();

				if((model != null) && model.isBattarySet(nBattIndex)) {
					Intent intent = new Intent(PowerActivity.this, PowerDetailActivity.class);
					intent.putExtra("nBattIndex",nBattIndex);	//传递当前选中的电池组
					PowerActivity.this.startActivity(intent);
					finish();
				}
			}
		});
		soc = (ImageView) findViewById(R.id.soc);
		gonglv = (ImageView) findViewById(R.id.gonglv);
		dianliu = (ImageView) findViewById(R.id.dianliu);
		soc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				soc.setImageResource(R.drawable.energy_tap_soc_on);
				gonglv.setImageResource(R.drawable.energy_tap_effect_off);
				dianliu.setImageResource(R.drawable.energy_tap_current_off);
				layout_a.setBackgroundResource(R.drawable.tap_line);
				Message msg = new Message();
				msg.what = 2;
				handler.sendMessage(msg);
			}
		});
		gonglv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				soc.setImageResource(R.drawable.energy_tap_soc_off);
				gonglv.setImageResource(R.drawable.energy_tap_effect_on);
				dianliu.setImageResource(R.drawable.energy_tap_current_off);
				layout_a.setBackgroundResource(R.drawable.cam_tab_2_bg);
				
				Message msg = new Message();
				msg.what = 3;
				handler.sendMessage(msg);
			} 
		});
		dianliu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				soc.setImageResource(R.drawable.energy_tap_soc_off);
				gonglv.setImageResource(R.drawable.energy_tap_effect_off);
				dianliu.setImageResource(R.drawable.energy_tap_current_on);
				layout_a.setBackgroundResource(R.drawable.cam_tab_2_bg);
				
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
		
		lineChartView= (LineChartView)findViewById(R.id.lineChartView);
		lineChartView.setChartCanvasSize(650,220,70,30);
    	lineChartView.setChartSrcSize(4,100,0.5,20);	//4h,100%,0.5h等分、20%等分
    	lineChartView.setUnitX("h");
    	lineChartView.setUnitY("%");

		touchPoint= (ImageView) findViewById(R.id.energy_coordinate_point);
		touchPointLine= (ImageView) findViewById(R.id.energy_coordinate_point_line);


		//设置曲线触摸
		lineChartView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent me) {
				
				lineChartView.onTouchEvent(me);
				Point p = lineChartView.getSelectedPoint();
				drawPoint(p);
				//ToastUtil.showDbgToast(getApplicationContext(), "p="+p+",d="+lineChartView.getSelectedData());
				return true;
			}
		});
		//refreshPannel(); 
	}

	private void initEvent(){
		EventBus.getDefault().register(this,"FinishPowerEvent",FinishPowerEvent.class);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.power_layout);
		
		this.initEvent();
		this.initView();
		
		handler = new Handler(this);
		timer = new Timer();
	}
	public void FinishPowerEvent(
			FinishPowerEvent event) {
		finish();
		//overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
	}
	

	private void refershBatteryIcon(ImageView battery_icon, float soc) {
		/**SOC*/
		int socv = (int)soc;
		
		if(socv <0) {
			battery_icon.setImageResource(R.drawable.energy_bat_empty);
		}else if(socv<=10){
			battery_icon.setImageResource(R.drawable.energy_bat_010);
		}else if(socv<=20){
			battery_icon.setImageResource(R.drawable.energy_bat_020);
		}else if(socv<=30){
			battery_icon.setImageResource(R.drawable.energy_bat_030);
		}else if(socv<=40){
			battery_icon.setImageResource(R.drawable.energy_bat_040);
		}else if(socv<=50){
			battery_icon.setImageResource(R.drawable.energy_bat_050);
		}else if(socv<=60){
			battery_icon.setImageResource(R.drawable.energy_bat_060);
		}else if(socv<=70){
			battery_icon.setImageResource(R.drawable.energy_bat_070);
		}else if(socv<=80){
			battery_icon.setImageResource(R.drawable.energy_bat_080);
		}else if(socv<=90){
			battery_icon.setImageResource(R.drawable.energy_bat_090);
		}else { //if(socv<=100){
			battery_icon.setImageResource(R.drawable.energy_bat_100);
		}
	}
		
	/**
	 * 将模型数据更新到屏幕
	 */
	private void refreshPannel() {
		model = DriverServiceManger.getInstance().getEnergyInfoDriver();

		if(model != null) {
			try {
				model.retreveGeneralInfo();
			}catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
			
			//将模型数据更新到屏幕...
			//batteryNum = model.getBattaryCabinNum(); 
			batteryNum = 4; //需求变更2015-07-07, 屏幕显示4组电池，根据掩码确定哪组电池可用，其他位置置灰
			model.getBattaryCabinMask();
			
			eidd1 = model.getBattaryDetailInfo(0);
			eidd2 = model.getBattaryDetailInfo(1);
			eidd3 = model.getBattaryDetailInfo(2);
			eidd4 = model.getBattaryDetailInfo(3);
			soc1 = eidd1.getSOC();
			soc2 = eidd2.getSOC();
			soc3 = eidd3.getSOC();
			soc4 = eidd4.getSOC();
			
			batt_icon_layout_a.setVisibility(View.GONE);
			batt_icon_layout_b.setVisibility(View.GONE);
			 
			if(((batteryNum>=1) && (batteryNum<=2)) || ((batteryNum>=3) && (currentPage == 1)) ){
				currentPage = 1;
				
				//电池组A
				batt_icon_layout_a.setVisibility(View.VISIBLE);
				chager_name_a.setText(getString(R.string.batteries_a));
				if(model.isBattarySet(0)) {
					chager_number_a.setText(String.format("%.1f%%", soc1));
					refershBatteryIcon(this.battery_icon_a,soc1);
				}
				else {
					chager_number_a.setText("N/A");
					refershBatteryIcon(this.battery_icon_a,-1);
				}
			}
			
			if((batteryNum>=2) && (currentPage == 1)) {
				currentPage = 1;

				//电池组B
				batt_icon_layout_b.setVisibility(View.VISIBLE);
				chager_name_b.setText(getString(R.string.batteries_b));
				if(model.isBattarySet(1)) {
					chager_number_b.setText(String.format("%.1f%%", soc2));
					refershBatteryIcon(this.battery_icon_b,soc2);
				}
				else {
					chager_number_b.setText("N/A");
					refershBatteryIcon(this.battery_icon_b,-1);
				}
			}
			
			if((batteryNum>=3) && (currentPage == 2) ){
				
				//电池组C
				batt_icon_layout_a.setVisibility(View.VISIBLE);
				chager_name_a.setText(getString(R.string.batteries_c));
				if(model.isBattarySet(2)) {
					chager_number_a.setText(String.format("%.1f%%", soc3));
					refershBatteryIcon(this.battery_icon_a,soc3);
				}
				else {
					chager_number_a.setText("N/A");
					refershBatteryIcon(this.battery_icon_a,-1);
				}
			}

			if((batteryNum>=4) && (currentPage == 2)) {

				//电池组D
				batt_icon_layout_b.setVisibility(View.VISIBLE);
				chager_name_b.setText(getString(R.string.batteries_d));
				if(model.isBattarySet(3)) {
					chager_number_b.setText(String.format("%.1f%%", soc4));
					refershBatteryIcon(this.battery_icon_b,soc4);
				}
				else {
					chager_number_b.setText("N/A");
					refershBatteryIcon(this.battery_icon_b,-1);
				}
			}
			
			
			//换页按钮
			if(batteryNum <= 2) {
				left.setEnabled(false);
				right.setEnabled(false);
			}
			else {
				if(currentPage==2){
					left.setEnabled(true);
					left.setVisibility(View.VISIBLE);
					right.setEnabled(false);
					right.setVisibility(View.INVISIBLE);
				}else {
					right.setEnabled(true);
					right.setVisibility(View.VISIBLE);
					left.setEnabled(false);
					left.setVisibility(View.INVISIBLE);
				}
			}
			
			//SOC趋势图
			drawGraphics(currentNTrendType,currentNDataLen);
		}
		else {
			//ToastUtil.showToast(this.getApplicationContext(), getString(R.string.back_service_not_start), Toast.LENGTH_LONG);
			Log.e("KDSERVICE", "PowerActivity.refreshPannel() service is null");
		}
	}
	
	private void drawPoint(Point p) {
		if(p != null && touchPoint.getWidth()!=0) {		//初始化调用touchPoint.getWidth()==0时图片绘偏，不绘制
			FrameLayout.LayoutParams touchPointLP = (LayoutParams) touchPoint.getLayoutParams();
			touchPointLP.leftMargin = p.x - (int)(touchPoint.getWidth()/2.0);
			touchPointLP.topMargin = p.y - (int)(touchPoint.getHeight()/2.0);
			touchPoint.setLayoutParams(touchPointLP);
			touchPoint.setVisibility(View.VISIBLE);

			FrameLayout.LayoutParams touchPointLineLP = (LayoutParams) touchPointLine.getLayoutParams();
			touchPointLineLP.leftMargin = p.x - (int)(touchPointLine.getWidth()/2.0);
			//touchPointLineLP.topMargin = p.y - (int)(touchPointLine.getHeight()/2.0);
			touchPointLine.setLayoutParams(touchPointLineLP);
			touchPointLine.setVisibility(View.VISIBLE);

			avgvalue.setText(lineChartView.getSelectedData()+"%");
		}
		else {
			touchPoint.setVisibility(View.INVISIBLE);
			touchPointLine.setVisibility(View.INVISIBLE);
			avgvalue.setText("--%");
		}
	}

	private final int MAX_TRENDCHART_DATA_LEN = 100; 
	int[] nValueArr = new int[MAX_TRENDCHART_DATA_LEN];

	private void drawGraphics(int nTrendType,int nDataLen){
		int length = 4*60/nDataLen + 1; 
		//int[] nValueArr = new int[length];
		if(length > MAX_TRENDCHART_DATA_LEN) 
			length = MAX_TRENDCHART_DATA_LEN;
		try {
			
			if(model!=null) {
				model.getTrendData(nTrendType, length, 15*60*1000/10, nValueArr);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
    	lineChartView.loadData(0,4,nValueArr, 0, 100);
    	soc_avg = avrgSoc(nValueArr,length);
		Point p = lineChartView.getSelectedPoint();
		drawPoint(p);
	}
	
	private int avrgSoc(int[] array,int len){
		final int InvalideValue=0xffff;
		int value=0;
		int count=0;
		for(int i=0;i<len;i++){
			if(array[i]>=InvalideValue||array[i]<0){
				
			}else{
				value+=array[i];
				count++;
			}
		}
		if(count == 0){
			return 0;
		}
		value=value/count;
		return value;
	}
	
	@Override
	public boolean handleMessage(Message arg0) {
		
		switch (arg0.what) {
		case 1:
			currentNTrendType = 1;
			break;
		case 2:
			currentNTrendType = 2;
			break;
		case 3:
			currentNTrendType = 3;
			break;
		case 4:
			currentNDataLen = 5;
			break;
		case 5:
			currentNDataLen = 15;
			break;
		case 6:
			currentNDataLen = 30;
			break;
		case 7:
			PowerActivity.this.refreshPannel();
			avgvalue.setText(""+soc_avg+"%");
			lineChartView.invalidate();
			break;
		default:
			break;
		}
		return false;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		if(task != null) {
			task.cancel();
			task=null;
		}
		EventBus.getDefault().unregister(this,FinishPowerEvent.class);
		System.gc();
	}
	@Override
	protected void onResume() {
		super.onResume();
		refreshPannel();
		lineChartView.invalidate();

		if(task == null) {
			task = new TimerTask() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what=7;
					handler.sendMessage(msg);
				}
			};
			timer.schedule(task, 1000, 3000);
		}
	}
	@Override
	protected void onPause() {
		if(task != null) {
			task.cancel();
			task=null;
		}
		super.onPause();
	}
	
}
