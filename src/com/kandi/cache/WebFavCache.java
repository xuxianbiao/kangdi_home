package com.kandi.cache;





import com.kandi.home.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class WebFavCache {
	private View baseView;
	private TextView url;
	private TextView title;
	private TextView webid;
	private ImageView expandarrow;
	
	
	public WebFavCache(View baseView) {
        this.baseView = baseView;
    }
	public TextView getUrl(){
    	if (url == null) {
    		url = (TextView) baseView.findViewById(R.id.url);
        }
        return url;
    }
	public TextView getTitle(){
    	if (title == null) {
    		title = (TextView) baseView.findViewById(R.id.title);
        }
        return title;
    }
	public ImageView getArrow(){
		if (expandarrow == null) {
			expandarrow = (ImageView) baseView.findViewById(R.id.expandarrow);
        }
        return expandarrow;
	}
	public TextView getWebid(){
    	if (webid == null) {
    		webid = (TextView) baseView.findViewById(R.id.webid);
        }
        return webid;
    }
}


