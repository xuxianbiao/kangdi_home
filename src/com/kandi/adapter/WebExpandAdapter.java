package com.kandi.adapter;

import java.util.List;
import java.util.Map;

import com.kandi.home.R;
import com.kandi.cache.MusicListCache;
import com.kandi.cache.WebFavCache;
import com.kandi.model.FavModel;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public  class  WebExpandAdapter extends  BaseExpandableListAdapter  
{  
    Activity activity;  
    private List<List<FavModel>> childArray;
    private List<String> groupArray;
    public  WebExpandAdapter(Activity a,List<List<FavModel>> childArray,List<String> groupArray)  
    {  
        activity = a;  
        this.childArray = childArray;
        this.groupArray = groupArray;
    }  
    public  Object getChild(int  groupPosition, int  childPosition)  
    {  
        return  childArray.get(groupPosition).get(childPosition);  
    }  
    public  long  getChildId(int  groupPosition, int  childPosition)  
    {  
        return  childPosition;  
    }  
    public  int  getChildrenCount(int  groupPosition)  
    {  
        return  childArray.get(groupPosition).size();  
    }  
    public  View getChildView(int  groupPosition, int  childPosition,  
            boolean  isLastChild, View convertView, ViewGroup parent)  
    {  
        FavModel fav = childArray.get(groupPosition).get(childPosition);  
        //return  getGenericView(string);
        View rowView = convertView;
        WebFavCache cache;
        LayoutInflater inflater = activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.webfav_item_layout, null);
        //rowView.setBackgroundResource(R.drawable.list_bg);
        cache = new WebFavCache(rowView);
        rowView.setTag(cache);
        
        TextView url = cache.getUrl();
        url.setTextSize(18.0f);
        url.setText(fav.getUrl()+"");
        
        TextView webid = cache.getWebid();
        webid.setText(fav.getId()+"");
        
        TextView title = cache.getTitle();
        title.setTextSize(16.0f);
        title.setVisibility(View.VISIBLE);
        title.setText(fav.getTitle()+"");
        
        ImageView expandarrow = cache.getArrow();
        expandarrow.setVisibility(View.GONE);
        return rowView;
    }  
    // group method stub   
    public  Object getGroup(int  groupPosition)  
    {  
        return  groupArray.get(groupPosition);  
    }  
    public  int  getGroupCount()  
    {  
        return  groupArray.size();  
    }  
    public  long  getGroupId(int  groupPosition)  
    {  
        return  groupPosition;  
    }  
    public  View getGroupView(int  groupPosition, boolean  isExpanded,  
            View convertView, ViewGroup parent)  
    {  
//        String string = groupArray.get(groupPosition);  
//        return  getGenericView(string);
    	String string = groupArray.get(groupPosition);  
        View rowView = convertView;
        WebFavCache cache;
        LayoutInflater inflater = activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.webfav_item_group_layout, null);
        //rowView.setBackgroundResource(R.drawable.list_bg);
        cache = new WebFavCache(rowView);
        rowView.setTag(cache);
        
        TextView url = cache.getUrl();
        //url.setText(string);
        url.setVisibility(View.GONE);
        
        TextView title = cache.getTitle();
        title.setText(string);
        
        ImageView expandarrow = cache.getArrow();
        if(childArray.get(groupPosition).size()>0){
        	expandarrow.setVisibility(View.VISIBLE);
        }else{
        	expandarrow.setVisibility(View.GONE);
        }
        
        return rowView;
    }  
    // View stub to create Group/Children 's View   
    public  TextView getGenericView(String string)  
    {  
        // Layout parameters for the ExpandableListView   
        AbsListView.LayoutParams layoutParams = new  AbsListView.LayoutParams(  
                ViewGroup.LayoutParams.MATCH_PARENT, 64 );  
        TextView text = new  TextView(activity);  
        text.setLayoutParams(layoutParams);  
        // Center the text vertically   
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);  
        // Set the text starting position   
        text.setPadding(36 , 0 , 0 , 0 );  
        text.setText(string);  
        return  text;  
    }  
    public  boolean  hasStableIds()  
    {  
        return  false ;  
    }  
    public  boolean  isChildSelectable(int  groupPosition, int  childPosition)  
    {  
        return  true ;  
    }  
}  