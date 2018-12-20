package com.kandi.cache;





import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandi.home.R;


public class MovieListCache {
	private View baseView;
	private TextView height_linearListView;
	
	
	public MovieListCache(View baseView) {
        this.baseView = baseView;
    }
	public TextView getHeight_linearListView(){
    	if (height_linearListView == null) {
    		height_linearListView = (TextView) baseView.findViewById(R.id.height_linearListView);
        }
        return height_linearListView;
    }
}


