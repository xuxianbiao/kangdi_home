package com.kandi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kandi.adapter.WebExpandAdapter;
import com.kandi.dao.FavDao;
import com.kandi.event.BottomMenuServiceHideEvent;
import com.kandi.event.PlayFreshWebListEvent;
import com.kandi.home.R;
import com.kandi.model.FavModel;
import com.kandi.view.WebActivity.OnNewUrlIntf;

import de.greenrobot.event.EventBus;
public class WebHomeFragment extends Fragment {
	public GridView  gv1;
	public GridView  gv2;
	public GridView  gv3;
	
	private ExpandableListView explist;
	private  List<String> groupArray;  
	private  List<List<FavModel>> childArray;
	
	private ImageButton navbtn0;
	private ImageButton navbtn1;
	private ImageButton navbtn2;
	private ImageButton navbtn3;
	private ImageButton navbtn4;
	private ImageButton navbtn5;
	Context context;
	
	private OnNewUrlIntf newUrlIntf;
	
	private void openWebUrl(String url){
//		if(BaseApplication.getInstance().requestCarSpeedMax()){
//			return;
//		}
		if (null != newUrlIntf) {
			newUrlIntf.onNewUrl(url); 
		}
	}
	
	private void initView(View rootView){
		navbtn0 = (ImageButton) rootView.findViewById(R.id.navbtn0);
		navbtn0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl(getString(R.string.kangdi_address));
			}
		});
		navbtn1 = (ImageButton) rootView.findViewById(R.id.navbtn1);
		navbtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl(getString(R.string.google_address));
			}
		});
		navbtn2 = (ImageButton) rootView.findViewById(R.id.navbtn2);
		navbtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl(getString(R.string.facebook_address));
			}
		});
		navbtn3 = (ImageButton) rootView.findViewById(R.id.navbtn3);
		navbtn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl(getString(R.string.twitter_address));
			}
		});
		navbtn4 = (ImageButton) rootView.findViewById(R.id.navbtn4);
		navbtn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl(getString(R.string.youtobe_address));
			}
		});
		navbtn5 = (ImageButton) rootView.findViewById(R.id.navbtn5);
		navbtn5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				openWebUrl(getString(R.string.amazon_address));
			}
		});
		
		explist = (ExpandableListView) rootView.findViewById(R.id.explist);
		explist.setVerticalScrollBarEnabled(true);
	}
	private void refreshList(){
		childArray.clear();
		
		List<FavModel> favList = FavDao.findFavByFavType(getActivity(), "0");		//0:网页收藏
		List<FavModel> historyList = FavDao.findFavByFavType(getActivity(), "1");	//1:历史记录
		  
		childArray.add(favList);
		childArray.add(historyList);
		

		WebExpandAdapter wea = new WebExpandAdapter(getActivity(), childArray, groupArray);
		explist.setAdapter(wea);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.web_grid_new_pannel, null);
		this.context = inflater.getContext();
		initView(rootView);
		initFragment(rootView);
		initEvent();
		
		return rootView;
	}
	
	
	private void initEvent() {
		EventBus.getDefault().register(this, "onPlayFreshWebList",
				PlayFreshWebListEvent.class);
	}
	
	public void onPlayFreshWebList(PlayFreshWebListEvent event){
		if(event.type==PlayFreshWebListEvent.WEBLIST_STATE){
			refreshList();
		}
	}

	private void initFragment(View rootView) {
		groupArray = new  ArrayList<String>();  
		childArray = new  ArrayList<List<FavModel>>();  
		  
		groupArray.add(context.getString(R.string.my_collection));  
		groupArray.add(context.getString(R.string.history_record));  

		refreshList();
		
		
		explist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long arg4) {
				
				String url=null;

				TextView webIdTv = (TextView) arg1.findViewById(R.id.webid);
				String webid = webIdTv.getText()+"";
				FavModel fm= FavDao.findFavById(getActivity(), Integer.parseInt(webid));
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
				final AlertDialog cameralockDialog = new AlertDialog.Builder(getActivity()).create();
				
				cameralockDialog.show();  
				cameralockDialog.getWindow().setContentView(R.layout.webalert_layout);  
				cameralockDialog.getWindow()  
	            .findViewById(R.id.deletebtn).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						FavDao.deleteFavById(getActivity(), Integer.parseInt(webid));
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
	}
	public List<Map<String, Object>> loadFavData(int page){
		List<FavModel> list = FavDao.findFavByPage(getActivity(), page);
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
	public void onStart() {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		EventBus.getDefault().postSticky(new BottomMenuServiceHideEvent());
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		super.onDestroy();
		EventBus.getDefault().unregister(this,PlayFreshWebListEvent.class);
		System.gc();
	}

	
	
	public void setNewUrlIntf(OnNewUrlIntf newUrlIntf) {
		this.newUrlIntf = newUrlIntf;
	}

}
