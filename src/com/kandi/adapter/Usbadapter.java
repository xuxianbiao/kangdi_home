package com.kandi.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandi.event.PlayFmEvent;
import com.kandi.home.R;
import com.kandi.model.MusicPlayerModel;
import com.kandi.util.CommonUtils;
import com.kandi.view.MusicListActivity;

import de.greenrobot.event.EventBus;

public class Usbadapter extends BaseAdapter{

	private int postion;
	private List<Map<String, Object>> mydata=new ArrayList<Map<String,Object>>();
	private MusicListActivity context;
	private String[] path;
	private final String List_Usb="2";
	public boolean isShow;
	private View.OnClickListener btnListener;
	public Usbadapter(List<Map<String, Object>> mydata,MusicListActivity context,String[] path) {
		this.mydata=mydata;
		this.context=context;
		this.path=path;
		
//		EventBus.getDefault().register(this, "onPlayMusic",
//				PlayMusicEvent.class);
		
		btnListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ImageView playBtn = (ImageView) arg0;
				if (MusicPlayerModel.getInstance().isPlaying()) {
					EventBus.getDefault().post(new PlayFmEvent());
					playBtn.setImageResource(R.drawable.music_file_icon_play);
				} else {
					boolean success = MusicPlayerModel.getInstance().play();
					if (success) {
						playBtn.setImageResource(R.drawable.music_file_icon_pause);
					}
				}
			}
		};
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mydata.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Activity activity=(Activity)context;
		myview myview=null;
		if(convertView==null)
		{
			myview=new myview();
			convertView=activity.getLayoutInflater().inflate(R.layout.music_list_item_layout, null);
			myview.tv_list_name = (TextView)convertView.findViewById(R.id.height_linearListView);
			myview.status=(ImageView)convertView.findViewById(R.id.status);
			myview.checks=(CheckBox)convertView.findViewById(R.id.check);
			convertView.setTag(myview);
		}								
		else
		{
			myview=(myview)convertView.getTag();
		}
		String filename = (String) mydata.get(position).get("name");
		if(filename != null && !"".equals(filename)){
			try {
				myview.tv_list_name.setText(CommonUtils.subStr(filename, 40));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(path!=null)
		{
			if(path[0].equals(mydata.get(position).get("musicpath"))&&path[1].equals(List_Usb))
			{
				convertView.setBackgroundColor(context.getResources().getColor(R.color.blue));
				myview.status.setVisibility(View.VISIBLE);
				if (MusicPlayerModel.getInstance().isPlaying()) {
					myview.status.setImageResource(R.drawable.music_file_icon_pause);
        		}
        		else {
        			myview.status.setImageResource(R.drawable.music_file_icon_play);
        		}
				myview.status.setOnClickListener(btnListener);
			} else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
        		myview.status.setVisibility(View.GONE);
			}
		}	
		if (isShow) {
			myview.checks.setVisibility(View.VISIBLE);
			Boolean flag = context.recodeStatu.get(position);
			if (flag == null) {
				myview.checks.setChecked(false);
			} else {
				myview.checks.setChecked(flag);
			}
		} else {
			myview.checks.setVisibility(View.GONE);
		}
		return convertView;
	}
	class myview
	{
		private TextView tv_list_name;
		private ImageView status;
		private CheckBox checks;
	}
}
