package com.kandi.adapter;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kandi.cache.MusicListCache;
import com.kandi.event.VideoControlEvent;
import com.kandi.fragment.VideoFragment;
import com.kandi.home.R;
import com.kandi.util.CommonUtils;
import com.kandi.view.VideoListActivity;

import de.greenrobot.event.EventBus;


public class MovieListAdapter extends ArrayAdapter<Map<String,Object>> {
    Drawable tempImage;
    private VideoListActivity context;
    private String[] videopath;
    private View.OnClickListener btnListener;
    private final String List_Local="1";
    public boolean isShow;
    public MovieListAdapter(VideoListActivity context, List<Map<String,Object>> ml1Ls, ListView listView,String[] videopath) {
        super(context, 0, ml1Ls);
        this.context = context;
        this.videopath = videopath;
        
        btnListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ImageView status = (ImageView) arg0;
				EventBus.getDefault().post(new VideoControlEvent());
				if (VideoFragment.playSts == 1) {
        			status.setImageResource(R.drawable.music_file_icon_pause);
        		}
        		else {
        			status.setImageResource(R.drawable.music_file_icon_play);
        		}
				
			}
		};
        
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        View rowView = convertView;
        MusicListCache mls1Cache;
        LayoutInflater inflater = activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.video_list_item_layout, null);
        //rowView.setBackgroundResource(R.drawable.list_bg);
        mls1Cache = new MusicListCache(rowView);
        rowView.setTag(mls1Cache);
        Map<String,Object> commonMap = getItem(position);
        
        TextView songName = mls1Cache.getHeight_linearListView();
        String micfilepath = commonMap.get("moviepath").toString();
        String filename = micfilepath.substring(micfilepath.lastIndexOf("/")+1);
		if(filename != null && !"".equals(filename)){
			try {
				songName.setText(CommonUtils.subStr(filename, 40));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
        
        TextView musicpath = mls1Cache.getMusicpath();
        CheckBox checks = mls1Cache.getCheckBoxs();
        musicpath.setText(commonMap.get("movepath")+"");
        
        ImageView status = mls1Cache.getStatus();
        if(videopath!=null){
        	String str = commonMap.get("moviepath").toString();
        	if(videopath[0].equals(str)&&videopath[1].equals(List_Local)){
            	rowView.setBackgroundColor(context.getResources().getColor(R.color.blue));
            	status.setVisibility(View.VISIBLE);
            	
            	if (VideoFragment.playSts == 1) {
        			status.setImageResource(R.drawable.music_file_icon_pause);
        		}
        		else {
        			status.setImageResource(R.drawable.music_file_icon_play);
        		}
            	
            	status.setOnClickListener(btnListener);
            }
        }
        
        if (isShow) {
        	checks.setVisibility(View.VISIBLE);
			Boolean flag = context.recodeStatu.get(position);
			if (flag == null) {
				checks.setChecked(false);
			} else {
				checks.setChecked(flag);
			}
		} else {
			checks.setVisibility(View.GONE);
		}
        
        return rowView;
    }
    
    
}

