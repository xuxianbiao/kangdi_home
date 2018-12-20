package com.kandi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandi.home.R;

public class CamVideoGridAdapter extends BaseAdapter
{

    private LayoutInflater inflater; 
    private List<GridItem> gridItemList; 
    private List<Map<String,Object>> dataList; 
    private Context context;
    private Integer type;
    public Integer isDeviceAll=0;
    public CamVideoGridAdapter(List<Map<String,Object>> dataList, Context context) 
    { 
        super(); 
        this.context = context;
        this.dataList = dataList;
        gridItemList = new ArrayList<GridItem>(); 
        inflater = LayoutInflater.from(context); 
        
        this.type=0;
    } 
    public CamVideoGridAdapter(String[] titles, int[] images,String[] description, Context context) 
    { 
        super(); 
        this.context = context;
        gridItemList = new ArrayList<GridItem>(); 
        inflater = LayoutInflater.from(context); 
        for (int i = 0; i < images.length; i++) 
        { 
            GridItem picture = new GridItem(titles[i], images[i],description[i]); 
            gridItemList.add(picture); 
        } 
        this.type=1;
    } 
    @Override
    public int getCount( )
    {
    	if(type==0){
    		if (null != dataList) 
            { 
                return dataList.size(); 
            } 
            else
            { 
                return 0; 
            } 
    	}else if(type==1){
    		if (null != gridItemList) 
            { 
                return gridItemList.size(); 
            } 
            else
            { 
                return 0; 
            } 
    	}else{
    		if (null != gridItemList) 
            { 
                return gridItemList.size(); 
            } 
            else
            { 
                return 0; 
            } 
    	}
        
    }

    @Override
    public Object getItem( int position )
    {
    	if(this.type==0){
    		return dataList.get(position);
    	}else if(type==1){
    		return gridItemList.get(position);
    	}else{
    		return gridItemList.get(position);
    	}
         
    }

    @Override
    public long getItemId( int position )
    {
        return position; 
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        ViewHolder viewHolder; 
        
        if (convertView == null) 
        { 
            convertView = inflater.inflate(R.layout.cam_record_item, null); 
            viewHolder = new ViewHolder(); 
            viewHolder.title = (TextView) convertView.findViewById(R.id.textView1); 
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageAirIcon);
            viewHolder.filepath = (TextView) convertView.findViewById(R.id.filepath);
            viewHolder.lockimg = (ImageView) convertView.findViewById(R.id.lockimg);
            convertView.setTag(viewHolder); 
        } else
        { 
            viewHolder = (ViewHolder) convertView.getTag(); 
        } 
        Map<String,Object> map = dataList.get(position);
    	viewHolder.title.setText((map.get("itemText")+"").replace(Environment.getExternalStorageDirectory().getPath() + "/CamVideo/", ""));
    	viewHolder.filepath.setText(map.get("itemText")+"");
    	
    	String isLock = map.get("isLock")+"";
    	if(isLock.equals("yes")){
    		viewHolder.lockimg.setVisibility(View.VISIBLE);
    	}else{
    		viewHolder.lockimg.setVisibility(View.GONE);
    	}
    	
        //Resources res=context.getResources();
        //int pic = (Integer) dataList.get(position).get("itemImage");
        //Bitmap icon=BitmapFactory.decodeResource(res, pic);
        if(position<dataList.size()) {
            Bitmap icon = (Bitmap) dataList.get(position).get("itemImage");
            if(icon != null) {
            	viewHolder.image.setImageBitmap(icon);
            }
    		else {
    			//TODO: ������ȱʡ�ļ�icon
    			//viewHolder.image.setImageBitmap(default_icon);
    		}
        }
        return convertView;
        
    }
    class GridItem 
    { 
        private String title; 
        private int imageId; 
        private String description;
        
        public GridItem() 
        { 
            super(); 
        } 
     
        public GridItem(String title, int imageId,String time) 
        { 
            super(); 
            this.title = title; 
            this.imageId = imageId; 
            this.description = time;
        } 
     
        public String getTime( )
        {
            return description;
        }

        public String getTitle() 
        { 
            return title; 
        } 
     
        public int getImageId() 
        { 
            return imageId; 
        } 
    } 
    static class ViewHolder 
    { 
        public ImageView image; 
        public TextView title;
        public TextView filepath;
        public ImageView lockimg;
    }
	public Integer getIsDeviceAll() {
		return isDeviceAll;
	}
	public void setIsDeviceAll(Integer isDeviceAll) {
		this.isDeviceAll = isDeviceAll;
	}
    
}