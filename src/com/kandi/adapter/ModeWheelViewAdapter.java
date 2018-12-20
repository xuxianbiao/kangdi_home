package com.kandi.adapter;

import java.util.List;

import net.tsz.afinal.utils.Utils;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kandi.home.R;
import com.lee.wheel.widget.TosGallery;

public class ModeWheelViewAdapter extends BaseAdapter {
    int mHeight = 100;	//item height
    int mPadding = 9;	//item padding top or bottom
    private Context context;
    private List<String> dataList;
    public ModeWheelViewAdapter(List<String> dataList, Context context) 
    { 
        super(); 
        this.context = context;
        this.dataList = dataList;
        
    } 
    public ModeWheelViewAdapter() {
        mHeight = (int) Utils.pixelToDp(context, mHeight);
    }

    @Override
    public int getCount() {
        return (null != dataList) ? dataList.size() : 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (null == convertView) {
        	convertView = new ImageView(context);
        	convertView.setLayoutParams(new TosGallery.LayoutParams(mHeight + mPadding*2, mHeight));
        	convertView.setPadding(mPadding, IGNORE_ITEM_VIEW_TYPE, mPadding, IGNORE_ITEM_VIEW_TYPE);
            
            imageView = (ImageView) convertView;
            switch (position) {
			case 0:
				imageView.setImageResource(R.drawable.wheel_mode_fm);
				break;
			case 1:
				imageView.setImageResource(R.drawable.wheel_mode_music);
				break;
			case 2:
				imageView.setImageResource(R.drawable.wheel_mode_video);
				break;

			default:
				break;
			}
        }

        return convertView;
    }
}
