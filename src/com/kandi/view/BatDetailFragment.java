package com.kandi.view;
import java.util.Timer;
import java.util.TimerTask;

import com.kandi.customview.BarChatView;
import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EnergyInfoDetailsDriver;
import com.kandi.driver.EnergyInfoDriver;
import com.kandi.event.FinishPowerEvent;
import com.kandi.home.R;

import de.greenrobot.event.EventBus;

import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BatDetailFragment extends Fragment implements Callback{
	private BarChatView barChartTempView;
	private BarChatView barChartVoltView;
	private ImageButton back;
	
	/**编号*/
	private TextView battery_num;
	/**电池组*/
	private TextView chager_number_left;
//	/**电池组百分比*/
//	private TextView chager_number_middle;
	/**总电压*/
	private TextView tvOverallVol;
	/**软件版本*/
	private TextView ruanjianbanbentv;
	/**soc*/
	private TextView soctv;
	/**绝缘电阻*/
	private TextView jueyuandianzutv;
	/**电池电流*/
	private TextView dianchidianliutv;
	/**充电状态*/
	private TextView chongdianzhuangtaitv;
	/**电池最高温度*/
	private TextView tvBattaryMaxTemp;
	/**电池最低温度*/
	private TextView tvBattaryMinTemp;
	/**电池编码*/
	private TextView dianchibianmatv;
	/**硬件版本*/
	private TextView yingjianbanbentv;
	/**soh*/
	private TextView tvSOH;
	/**充电次数*/
	private TextView chongdiancisutv;
	/**单体最高电压*/
	private TextView tvCellMaxVolt;
	/**单体最低电压*/
	private TextView tvCellMinVolt;
	/**电池安全性*/
	private TextView dianchianquanxingtv;
	
	private ImageView wendu;
	private ImageView dianya;
	private int nBattIndex;
	
	private ImageView battery_icon;
	private Timer timer;
	private TimerTask task;
	private Handler handler;
	private View chart_title_area;
	
	private void switchChart(int index) {
		
		switch(index) {
		case 0:		//单体电压
			chart_title_area.setBackgroundResource(R.drawable.cam_tab_1_bg);
			barChartTempView.setVisibility(View.GONE);
			barChartVoltView.setVisibility(View.VISIBLE);
			break;
		case 1:		//电池温度
			chart_title_area.setBackgroundResource(R.drawable.cam_tab_2_bg);
			barChartTempView.setVisibility(View.VISIBLE);
			barChartVoltView.setVisibility(View.GONE);
			break;
		}
	}
	private void initView(View view){
		battery_num  = (TextView) view.findViewById(R.id.bat_name);
		battery_icon = (ImageView) view.findViewById(R.id.battery_icon);
		battery_icon = (ImageView) view.findViewById(R.id.battery_icon);
		
//		if(nBattIndex>=0&&nBattIndex<str.length){
//			battery_num.setText(str[nBattIndex]);
//		}
		
		wendu = (ImageView) view.findViewById(R.id.soc);
		wendu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switchChart(1);
			}
		});
		dianya = (ImageView) view.findViewById(R.id.dianliu);
		dianya.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switchChart(0);
			}
		});
		
		chager_number_left = (TextView) view.findViewById(R.id.chager_number_left);
		tvOverallVol = (TextView) view.findViewById(R.id.tvOverallVol);
		ruanjianbanbentv = (TextView) view.findViewById(R.id.ruanjianbanbentv);
		soctv = (TextView) view.findViewById(R.id.soctv);
		jueyuandianzutv = (TextView) view.findViewById(R.id.jueyuandianzutv);
		dianchidianliutv = (TextView) view.findViewById(R.id.dianchidianliutv);
		chongdianzhuangtaitv = (TextView) view.findViewById(R.id.chongdianzhuangtaitv);
		tvBattaryMaxTemp = (TextView) view.findViewById(R.id.tvBattaryMaxTemp);
		tvBattaryMinTemp = (TextView) view.findViewById(R.id.tvBattaryMinTemp);
		dianchibianmatv = (TextView) view.findViewById(R.id.dianchibianmatv);
		yingjianbanbentv = (TextView) view.findViewById(R.id.yingjianbanbentv);
		tvSOH = (TextView) view.findViewById(R.id.tvSOH);
		chongdiancisutv = (TextView) view.findViewById(R.id.chongdiancisutv);
		tvCellMaxVolt = (TextView) view.findViewById(R.id.tvCellMaxVolt);
		tvCellMinVolt = (TextView) view.findViewById(R.id.tvCellMinVolt);
		dianchianquanxingtv = (TextView) view.findViewById(R.id.dianchianquanxingtv);
		
		
		back = (ImageButton) view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/*Intent intent = new Intent(BatDetailFragment.this, PowerActivity.class);
				PowerDetailActivity.this.startActivity(intent);
				finish();*/
			}
		});
		
		chart_title_area = (View)view.findViewById(R.id.chart_title_area);
		
		//init temp chart view
		barChartTempView= (BarChatView)view.findViewById(R.id.barChartTempView);
		barChartTempView.setChartCanvasSize(650,220,70,30);
		barChartTempView.setUnitX("");
		barChartTempView.setChartSrcSize(4,0,100,4,20);	//4格,100%,1等分格、20%等分
		barChartTempView.setUnitY("℃");

		//init voltage chart view
		barChartVoltView= (BarChatView)view.findViewById(R.id.barChartVoltView);
		barChartVoltView.setChartCanvasSize(650,220,70,30);
		barChartVoltView.setUnitX("");
		barChartVoltView.setChartSrcSize(25,0,6000,1,2000);	//25格,6000mV,1等分格、2000mV等分
		barChartVoltView.setUnitY("mV");
		barChartVoltView.setVisibility(View.VISIBLE);
		
		//show temp chart view as default
		switchChart(0); 
		
		handler.sendEmptyMessage(1);
		
	}
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishPowerEvent",FinishPowerEvent.class);
	}
	
	public void FinishPowerEvent(
			FinishPowerEvent event) {
		this.getActivity().finish();
		//overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
	}
	
	int[] nCellVolArr = new int[EnergyInfoDetailsDriver.MAX_CELL_NUM];
	int[] nCellTempArr = new int[EnergyInfoDetailsDriver.MAX_CELL_NUM];
	int battaryCellNum=0;	//实际电池cell数量
	int tempSensorNum=0;	//实际温度探头数量
	
	/**
	 * 将模型数据更新到屏幕
	 */
	private void refreshPannel() {
		EnergyInfoDriver modelinfo = DriverServiceManger.getInstance().getEnergyInfoDriver();

		if(modelinfo == null) {
			//ToastUtil.showToast(this.getApplicationContext(), getString(R.string.back_service_not_start), Toast.LENGTH_LONG);
			Log.e("KDSERVICE", this.toString() + ".refreshPannel() service is null");
			return;
		}
		
		try {
			modelinfo.retreveGeneralInfo();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		String[] str={getResources().getString(R.string.batteries_a),getResources().getString(R.string.batteries_b),getResources().getString(R.string.batteries_c),getResources().getString(R.string.batteries_d)};
		if(nBattIndex>=0&&nBattIndex<4){
			battery_num.setText(str[modelinfo.getRealBattaryPosition(nBattIndex)]);
		}

		EnergyInfoDetailsDriver model = modelinfo.getBattaryDetailInfo(nBattIndex);

		if(model != null) {
			try {
				int r = model.retriveGeneralBatteryInfo();
				if(r==0){
					battaryCellNum = model.getBatCellVol(nCellVolArr);
					if(battaryCellNum<=0){
						//ToastUtil.showToast(getApplicationContext(), getString(R.string.getbattarycellv_failed)+battaryCellNum+")", Toast.LENGTH_LONG);
						battaryCellNum=0;
					}
					tempSensorNum = model.getBatCellTemp(nCellTempArr);
					if(tempSensorNum<=0){
						//ToastUtil.showToast(getApplicationContext(), getString(R.string.getbattarycellt_failed)+tempSensorNum+")", Toast.LENGTH_LONG);
						tempSensorNum=0;
					}
				}else{
					//ToastUtil.showToast(getApplicationContext(), getString(R.string.getbattaryinfo_failed)+r, Toast.LENGTH_LONG);
				}
				
			}catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
			
			
			//将模型数据更新到屏幕...
			/**电池组*/
			/*switch (model.nBattaryNum) {
			case 0:
				chager_number_left.setText(getString(R.string.batteries_a));
				break;
			case 1:
				chager_number_left.setText(getString(R.string.batteries_b));
				break;
			case 2:
				chager_number_left.setText(getString(R.string.batteries_c));
				break;
			case 3:
				chager_number_left.setText(getString(R.string.batteries_d));
				break;
			default:
				chager_number_left.setText("(N/A)");
				break;
			}*/
			
			/**电池序列号(电池编码)*/
			dianchibianmatv.setText("S/N: "+ model.getBattarySN());
			/**总电压*/
			tvOverallVol.setText(String.format("%.1fV", model.getBattaryVoltage()));
			/**软件版本*/
			ruanjianbanbentv.setText(model.getSoftwareRev()+"");
			
			/**SOC*/
			soctv.setText(String.format("%.1f%%", model.getSOC()));
			int socv = (int) Double.parseDouble((soctv.getText()+"").replace("%", ""));
			if(socv<=10){
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
			}else { // if(socv<=100){
				battery_icon.setImageResource(R.drawable.energy_bat_100);
			}
			/**绝缘电阻*/
			jueyuandianzutv.setText("+"+model.getInsulatedResP()+ "/-" + model.getInsulatedResN()+"KR");
			/**电池电流*/
			dianchidianliutv.setText(String.format("%.1fA", model.getBattaryCurrent()));
			/**充电状态*/
			chongdianzhuangtaitv.setText((model.getRechargeState()==1)?getString(R.string.charging2):getString(R.string.stop_charg));
			/**电池最高温度*/
			tvBattaryMaxTemp.setText(model.getMaxTemp()+"℃");
			/**电池最低温度*/
			tvBattaryMinTemp.setText(model.getMinTemp()+"℃");
			/**硬件版本*/ 
			yingjianbanbentv.setText(model.getHardwareRev()+"");
			/**SOH*/
			tvSOH.setText(String.format("%.1f%%", model.getSOH()));
			/**充电次数*/
			chongdiancisutv.setText(model.getRechargeCycle()+"");
			
			/**电池安全性*/
			switch(model.getBattSafeState()) {
			case 0:
				dianchianquanxingtv.setText(getString(R.string.safe));
				break;
			case 1:
				dianchianquanxingtv.setText(getString(R.string.warn));
				break;
			case 2:		
			default:
				dianchianquanxingtv.setText(getString(R.string.trouble));
				break;
			}
			
			/**单体最高电压*/
			tvCellMaxVolt.setText(model.getMaxVolt()+"mV");
			
			/**单体最低电压*/
			tvCellMinVolt.setText(model.getMinVolt()+"mV");
			
			//温度柱状图
			int srcWidthT= (tempSensorNum<1)?1:tempSensorNum;
			barChartTempView.setChartSrcSize(srcWidthT,-20,100,1,20);	//4格,100%,1等分格、20%等分
			barChartTempView.setData(nCellTempArr,tempSensorNum);
			if(barChartTempView.getVisibility() == View.VISIBLE) {
				barChartTempView.invalidate();
			}
			
			//电压柱状图
			int srcWidthV= (battaryCellNum<1)?1:battaryCellNum;
			barChartVoltView.setChartSrcSize(srcWidthV,-20,100,1,2000);	//25格,6000mV,1等分格、2000mV等分
			barChartVoltView.setData(nCellVolArr, battaryCellNum);
			if(barChartVoltView.getVisibility() == View.VISIBLE) {
				barChartVoltView.invalidate();
			}
			
			
		}
		else {
			Log.e("KDSERVICE", "PowerDetailActivity.refreshPannel() service is null");
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null) {
			task.cancel();
			task=null;
			if(timer!=null){
				timer.cancel();
				timer=null;
			}
		}
		System.gc();
	}
	@Override
	public void onResume() {
		super.onResume();
		
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.power_detail_layout, container, false);

		nBattIndex = this.getArguments().getInt("BatNum",0);

		this.initEvent();
		handler = new Handler(this);
		this.initView(view);
		
		timer = new Timer();
		if(task == null) {
			task = new TimerTask() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what=1;
					handler.sendMessage(msg);
				}
			};
			timer.schedule(task, 1500, 1500);
		}
		return view;
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		switch (arg0.what) {
		case 1:
			refreshPannel();
			break;

		default:
			break;
		}
		return false;
	}
	
	public static BatDetailFragment newInstance(int s) {
		BatDetailFragment newFragment = new BatDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("BatNum", s);
		newFragment.setArguments(bundle);
		return newFragment;
	}
	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this,FinishPowerEvent.class);
		Log.i("onDestroyView num", ""+nBattIndex);
		if(task != null) {
			task.cancel();
			task=null;
			if(timer!=null){
				timer.cancel();
				timer=null;
			}
		}
		super.onDestroyView();
	}
	
}
