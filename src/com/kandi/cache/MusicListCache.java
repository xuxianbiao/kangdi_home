package com.kandi.cache;





import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandi.home.R;


public class MusicListCache {
	private View baseView;
	private TextView height_linearListView;
	private TextView musicpath;
	private ImageView status;
	private CheckBox check;
	
	
	public MusicListCache(View baseView) {
        this.baseView = baseView;
    }
	public TextView getHeight_linearListView(){
    	if (height_linearListView == null) {
    		height_linearListView = (TextView) baseView.findViewById(R.id.height_linearListView);
        }
        return height_linearListView;
    }
	
	public TextView getMusicpath(){
    	if (musicpath == null) {
    		musicpath = (TextView) baseView.findViewById(R.id.musicpath);
        }
        return musicpath;
    }
	public ImageView getStatus(){
		if(status == null){
			status = (ImageView) baseView.findViewById(R.id.status);
		}
		return status;
	}
	
	public CheckBox getCheckBoxs(){
		if(check == null){
			check = (CheckBox) baseView.findViewById(R.id.check);
		}
		return check;
	}
}

