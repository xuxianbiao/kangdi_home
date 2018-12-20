package com.kandi.adapter;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
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
import com.kandi.event.PlayFmEvent;
import com.kandi.home.R;
import com.kandi.model.MusicPlayerModel;
import com.kandi.util.CommonUtils;
import com.kandi.view.MusicListActivity;

import de.greenrobot.event.EventBus;


public class MusicListAdapter extends ArrayAdapter<Map<String,Object>> {
    Drawable tempImage;
    private MusicListActivity context;
    private String[] musicPath;
    private final String List_Local="1";
//    private ImageView selectedRightView;
    private View.OnClickListener btnListener;
    public boolean isShow;

    public MusicListAdapter(MusicListActivity context, List<Map<String,Object>> ml1Ls, ListView listView,String[] musicPath) {
        super(context, 0, ml1Ls);
        this.context = context;
        this.musicPath = musicPath;
        
//        EventBus.getDefault().register(this, "onPlayMusic",
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        View rowView = convertView;
        MusicListCache mls1Cache;
        LayoutInflater inflater = activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.music_list_item_layout, null);
        //rowView.setBackgroundResource(R.drawable.list_bg);
        mls1Cache = new MusicListCache(rowView);
        rowView.setTag(mls1Cache);
        Map<String,Object> commonMap = getItem(position);
        
        ImageView status = mls1Cache.getStatus();
        
        CheckBox checks = mls1Cache.getCheckBoxs();
        
        TextView songName = mls1Cache.getHeight_linearListView();
        String micfilepath = commonMap.get("musicpath").toString();
        String filename = micfilepath.substring(micfilepath.lastIndexOf("/")+1);
		if(filename != null && !"".equals(filename)){
			try {
				songName.setText(CommonUtils.subStr(filename, 40));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
        if(musicPath[0]!=null){
        	if(musicPath[0].equals(commonMap.get("musicpath").toString()+"")&&musicPath[1].equals(List_Local)){
            	rowView.setBackgroundColor(context.getResources().getColor(R.color.blue));
        		status.setVisibility(View.VISIBLE);
        		if (MusicPlayerModel.getInstance().isPlaying()) {
        			status.setImageResource(R.drawable.music_file_icon_pause);
        		}
        		else {
        			status.setImageResource(R.drawable.music_file_icon_play);
        		}
        		
        		status.setOnClickListener(btnListener);
//        		selectedRightView = status;
            }
        	else {
        		rowView.setBackgroundColor(Color.TRANSPARENT);
        		status.setVisibility(View.GONE);
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
    
    
//    public void onPlayMusic(PlayMusicEvent event) {
//    	if (null == selectedRightView) {
//    		return;
//    	}
//    	if(event.MUSIC_IDLE == 1){
//    		if (event.type == PlayMusicEvent.MUSIC_PAUSE) {
//    			selectedRightView.setImageResource(R.drawable.home_small_music_play_selector);
//    		}
//    		else if (event.type == PlayMusicEvent.MUSIC_PLAY) {
//    			selectedRightView.setImageResource(R.drawable.home_small_music_pause_selector);
//    		}
//    	}
//	}
}

