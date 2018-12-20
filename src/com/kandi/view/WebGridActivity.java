package com.kandi.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandi.adapter.WebExpandAdapter;
import com.kandi.dao.FavDao;
import com.kandi.event.BottomMenuServiceHideEvent;
import com.kandi.event.FinishWebGridEvent;
import com.kandi.home.R;
import com.kandi.model.FavModel;

import de.greenrobot.event.EventBus;
public class WebGridActivity extends Activity {
	public GridView  gv1;
	public GridView  gv2;
	public GridView  gv3;
	
	private ExpandableListView explist;
	private  List<String> groupArray;  
	private  List<List<FavModel>> childArray;
	private AutoCompleteTextView net_address;
	
	private ImageButton navbtn0;
	private ImageButton navbtn1;
	private ImageButton navbtn2;
	private ImageButton navbtn3;
	private ImageButton navbtn4;
	private ImageButton navbtn5;
	
	private ImageView web_back;
	private String currenturl;
	private ImageView web_forward;
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishWebGridEvent",FinishWebGridEvent.class);
	}
	public void FinishWebGridEvent(FinishWebGridEvent event) {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		finish();
	}
	private void openWebUrl(String url){
		Intent intent = new Intent(WebGridActivity.this, WebActivity.class);
		if(url!=null && !url.isEmpty()) {
			intent.putExtra("url", url);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		WebGridActivity.this.startActivity(intent);
		WebGridActivity.this.finish();
	}
	private void initView(){
		ImageButton web_back = (ImageButton)findViewById(R.id.web_back);
		ImageButton web_stop = (ImageButton)findViewById(R.id.web_stop);
		ImageButton web_reload = (ImageButton)findViewById(R.id.web_reload);
		ImageButton home = (ImageButton)findViewById(R.id.home);
		ImageButton web_fav = (ImageButton)findViewById(R.id.web_fav);
		web_back.setEnabled(false);
		web_stop.setEnabled(false);
		web_reload.setEnabled(false);
		home.setEnabled(false);
		web_fav.setEnabled(false);
		
		currenturl = this.getIntent().getStringExtra("currenturl");
		web_forward = (ImageView) findViewById(R.id.web_forward);
		web_forward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(currenturl!=null){
					openWebUrl(currenturl);
				}
			}
		});
		web_back = (ImageButton) findViewById(R.id.web_back);
		web_back.setEnabled(false);
		web_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(currenturl!=null){
					//openWebUrl(currenturl);
				}
				
			}
		});
		navbtn0 = (ImageButton) findViewById(R.id.navbtn0);
		navbtn0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl("http://www.kandigroup.com.cn/");
			}
		});
		navbtn1 = (ImageButton) findViewById(R.id.navbtn1);
		navbtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl("http://baidu.com");
			}
		});
		navbtn2 = (ImageButton) findViewById(R.id.navbtn2);
		navbtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl("http://sina.com");
			}
		});
		navbtn3 = (ImageButton) findViewById(R.id.navbtn3);
		navbtn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl("http://qq.com");
			}
		});
		navbtn4 = (ImageButton) findViewById(R.id.navbtn4);
		navbtn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl("http://ctrip.com");
			}
		});
		navbtn5 = (ImageButton) findViewById(R.id.navbtn5);
		navbtn5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl("http://dianping.com");
			}
		});
		
		explist = (ExpandableListView) findViewById(R.id.explist);
		explist.setVerticalScrollBarEnabled(true);
		net_address = (AutoCompleteTextView) findViewById(R.id.net_address);
		net_address.setText("http://www.sina.com");
		net_address.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent arg2) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					String url = net_address.getText()+"";
					Intent intent = new Intent(WebGridActivity.this, WebActivity.class);
					intent.putExtra("url", url);
					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
					if(imm.isActive()){  
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
					}  
					WebGridActivity.this.startActivity(intent);
					WebGridActivity.this.finish();
					return true;  
				}  
				return false; 
			}
		});
	}
	private void refreshList(){
		childArray.clear();
		
		List<FavModel> favList = FavDao.findFavByFavType(this, "0");		//0:网页收藏
		List<FavModel> historyList = FavDao.findFavByFavType(this, "1");	//1:历史记录
		  
		childArray.add(historyList);
		childArray.add(favList);
		

		WebExpandAdapter wea = new WebExpandAdapter(this, childArray, groupArray);
		explist.setAdapter(wea);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.web_grid_new_pannel);
		initView();
		
		
		groupArray = new  ArrayList<String>();  
		childArray = new  ArrayList<List<FavModel>>();  
		  
		groupArray.add(getString(R.string.history_record));  
		groupArray.add(getString(R.string.my_collection));  

		refreshList();
		
		
		explist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long arg4) {
				
				String url=null;

				TextView webIdTv = (TextView) arg1.findViewById(R.id.webid);
				String webid = webIdTv.getText()+"";
				FavModel fm= FavDao.findFavById(WebGridActivity.this, Integer.parseInt(webid));
				if(fm!=null) {
					url = fm.getUrl();
				}
				openWebUrl(url);
				return false;
			}
		});
		explist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView webIdTv = (TextView) arg1.findViewById(R.id.webid);
				final String webid = webIdTv.getText()+"";
				if(webid.equals("")){
					return false;
				}
				final AlertDialog cameralockDialog = new AlertDialog.Builder(WebGridActivity.this).create();
				
				cameralockDialog.show();  
				cameralockDialog.getWindow().setContentView(R.layout.webalert_layout);  
				cameralockDialog.getWindow()  
	            .findViewById(R.id.deletebtn).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						FavDao.deleteFavById(WebGridActivity.this, Integer.parseInt(webid));
						refreshList();
						cameralockDialog.dismiss();
					}
				});
				cameralockDialog.getWindow()  
	            .findViewById(R.id.cancelbtn).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						cameralockDialog.dismiss();
					}
				});
				return true;
			}
		});
		//initHome();
		this.initEvent();
	}
	public List<Map<String, Object>> loadFavData(int page){
		List<FavModel> list = FavDao.findFavByPage(this, page);
		List<Map<String, Object>> resList = new ArrayList<Map<String,Object>>();
		for(FavModel fm:list){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("webimg", fm.getThumb());
			map.put("webtitle", fm.getTitle());
			map.put("webid", fm.getId()+"");
			resList.add(map);
		}
		return resList;
	}

	@Override
	protected void onStart() {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		super.onStart();
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		EventBus.getDefault().postSticky(new BottomMenuServiceHideEvent());
		super.onResume();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		this.setIntent(intent);
		super.onNewIntent(intent);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		super.onPause();
		System.gc();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		super.onDestroy();
		System.gc();
	}

}
