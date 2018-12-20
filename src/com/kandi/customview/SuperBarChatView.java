package com.kandi.customview;

import java.text.NumberFormat;  
import java.util.ArrayList;  
import java.util.Collections;
import java.util.Comparator;
import java.util.List;  
  



import java.util.Map;

import com.kandi.driver.DriverServiceManger;
import com.kandi.driver.EnergyInfoDetailsDriver;
import com.kandi.driver.EnergyInfoDriver;
import com.kandi.util.ChartUtil;
import com.kandi.util.DataElement;
import com.kandi.util.DataSeries;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.DashPathEffect;  
import android.graphics.Paint;  
import android.graphics.Paint.Style;  
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;  
import android.view.View;  
import android.view.WindowManager;  
import android.widget.Toast;

public class SuperBarChatView extends  View{  
    private String unitStr = "c";
    private String plotTitle = "";  
    private DataSeries series;  
    private float yPlus = 602;
    private float maxVoltage = 5000;
    private float barRatio = 5000/280;
    private int batteryNum = 0;
    public final static int[] platterTable = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN}; 
    private Comparator<DataElement> dComparator = new Comparator<DataElement>() {
		@Override
		public int compare(DataElement de1,
				DataElement de2) {
			float v1 = de1.getValue();
			float v2 = de2.getValue();
			if(v1>v2){
				return -1;
			}else{
				return 0;
			}
		}
	};
    public SuperBarChatView(Context context, String plotTitle) {  
        this(context);  
        this.plotTitle = plotTitle;  
    }  
      
    public SuperBarChatView(Context context) {  
        super(context);  
    }  
    public SuperBarChatView(Context context, AttributeSet attrs) {  
        super(context,attrs);  
    } 
    public SuperBarChatView(Context context, AttributeSet attrs, int defStyle) {  
        super(context,attrs,defStyle);  
    } 
      
    public void setSeries(DataSeries series) {  
        this.series = series;  
    }  
      
    @Override    
    public void onDraw(Canvas canvas) {   
          
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);  
        Display display = wm.getDefaultDisplay();  
        int width = display.getWidth();  
          
        int height = display.getHeight() ;   
        System.out.println("width = " + width);  
        System.out.println("height = " + height);  
          
        Paint myPaint = new Paint();  
        myPaint.setColor(Color.TRANSPARENT);  
        myPaint.setStrokeWidth(2);  
        canvas.drawRect(0, 0, width, height, myPaint);  
        myPaint.setColor(Color.TRANSPARENT);  
        myPaint.setStrokeWidth(0);  
        canvas.drawRect(2, 2, width-2, height-2, myPaint);  
          
        int xOffset = (int)(width * 0.1);  
        int yOffset = (int)(height * 0.1);  
        System.out.println("xOffset = " + xOffset);  
          
        myPaint.setColor(Color.BLACK);  
        myPaint.setStrokeWidth(2);  
        canvas.drawLine(2+xOffset, height-2-yOffset, 2+xOffset, 2, myPaint);  
        canvas.drawLine(2+xOffset, height-2-yOffset, width-2, height-2-yOffset, myPaint);  
          
        myPaint.setAntiAlias(true);  
        myPaint.setStyle(Style.FILL);  
        canvas.drawText(plotTitle, (width-2)/4, 30, myPaint);  
          
        if(series == null) {  
            getMockUpSeries();  
        }  
        int xPadding = 10;  
        if(series != null) {  
        int count = series.getSeriesCount();  
        int xUnit = (width - 2 - xOffset)/count;  
            String[] seriesNames = series.getSeriesKeys();  
            for(int i=0; i<seriesNames.length; i++) {  
                canvas.drawText(seriesNames[i], xOffset + 2 + xPadding + xUnit*i-yPlus, height-yOffset + 10, myPaint);  
            }  
              
            float min = 0, max = 0;  
            for(int i=0; i<seriesNames.length; i++) {  
                List<DataElement> itemList = series.getItems(seriesNames[i]);  
                if(itemList != null && itemList.size() > 0) {  
                    for(DataElement item : itemList) {  
                        if(item.getValue() > max) {  
                            max = item.getValue();  
                        }  
                        if(item.getValue() < min) {  
                            min = item.getValue();  
                        }  
                    }  
                }  
            }  
              
            int yUnit = 22;   
            int unitValue = (height-2-yOffset)/yUnit;  
            myPaint.setStyle(Style.STROKE);  
            myPaint.setStrokeWidth(1);  
            myPaint.setColor(Color.LTGRAY);  
            myPaint.setPathEffect(new DashPathEffect(new float[] {1,3}, 0));  
            float ymarkers = (max-min)/yUnit;  
            NumberFormat nf = NumberFormat.getInstance();  
            nf.setMinimumFractionDigits(2);  
            nf.setMaximumFractionDigits(2);  
            for(int i=0; i<5; i++) {  
            	
                //canvas.drawLine(2+xOffset, height-2-yOffset - (unitValue * (i+1))-yPlus, width-2, height-2-yOffset - (unitValue * (i+1))-yPlus, myPaint);
            	canvas.drawLine(2+xOffset, 56*i, width-2, 56*i, myPaint);
            }  
            canvas.drawLine(2+xOffset, height-2-yOffset - (unitValue * (0+1))-yPlus, width-2, height-2-yOffset - (unitValue * (0+1))-yPlus, myPaint);
            // clear the path effect  
            myPaint.setColor(Color.WHITE);  
            myPaint.setStyle(Style.STROKE);  
            myPaint.setStrokeWidth(0);  
            myPaint.setPathEffect(null);   
            for(int i=0; i<5; i++) {  
                //float markValue = ymarkers * (i+1);  
                //canvas.drawText(nf.format(markValue)+"test", 3, height-2-yOffset - (unitValue * (i+1))-yPlus, myPaint);
                //canvas.drawText(i+"test", 3, height-2-yOffset - (unitValue * (i+1))-yPlus, myPaint);
            	if(unitStr.equals("℃")){
            		canvas.drawText((5-i)*50+unitStr, 3, 56*i, myPaint);
            	}else{
            		canvas.drawText((5-i)*1000+unitStr, 3, 56*i, myPaint);
            	}
                
            }  
              
            // draw bar chart now  
            myPaint.setStyle(Style.FILL);  
            myPaint.setStrokeWidth(0);  
            String maxItemsKey = null;  
            int maxItem = 0;  
            for(int i=0; i<seriesNames.length; i++) {  
                List<DataElement> itemList = series.getItems(seriesNames[i]);  
                int barWidth = (int)(xUnit/Math.pow(itemList.size(),2));  
                int startPos = xOffset + 2 + xPadding + xUnit*i;  
                int index = 0;  
                int interval = barWidth/2;  
                if(itemList.size() > maxItem) {  
                    maxItemsKey = seriesNames[i];  
                    maxItem = itemList.size();  
                }  
//                Collections.sort(itemList, dComparator);
//                System.out.println("1~~~~~~~"+itemList.get(0).getValue());
//                System.out.println("2~~~~~~~"+itemList.get(itemList.size()-1).getValue());
                for(DataElement item : itemList) {  
                    
                    //float barHeight = (int)((item.getValue()/ymarkers) * unitValue);  
                    if(item.getValue()<1200){
                    	myPaint.setColor(Color.RED);  
                    }else if(item.getValue()>3600){
                    	myPaint.setColor(Color.YELLOW);  
                    }else{
                    	myPaint.setColor(Color.BLUE);  
                    }
                    float barHeight = item.getValue()/barRatio;
//                    System.out.println(startPos + barWidth*index + interval*index+"~~~~1");
//                    System.out.println(height-2-yOffset-barHeight+"~~~~2");
//                    System.out.println(startPos + barWidth*index + interval*index + barWidth-20+"~~~~3");
//                    System.out.println(height-2-yOffset-yPlus+"~~~~4");
//                    canvas.drawRect(startPos + barWidth*index + interval*index, height-2-yOffset-barHeight,   
//                            startPos + barWidth*index + interval*index + barWidth-20, height-2-yOffset-582, myPaint);
                    barWidth = 24;
                    float left = startPos + barWidth*index+8;
                    float right = startPos + barWidth*index+barWidth;
//                    canvas.drawRect(startPos + barWidth*index + interval*index, 120.0f,   
//                            startPos + barWidth*index + interval*index + barWidth, height-2-yOffset-582, myPaint);
                    canvas.drawRect(left, 280.0f-barHeight,   
                            right, height-2-yOffset-642, myPaint);
                    index++;  
                }  
            }  
              
            List<DataElement> maxItemList = series.getItems(maxItemsKey);  
            int itemIndex = 0;  
            int basePos = 10;  
            for(DataElement item : maxItemList) {  
                myPaint.setColor(item.getColor());  
                canvas.drawRect(basePos + itemIndex * 10, height-yOffset + 15, basePos + itemIndex * 10 + 10, height-yOffset + 30, myPaint);  
                myPaint.setColor(Color.BLACK);  
                canvas.drawText(item.getItemName(), basePos + (itemIndex+1) * 10, height-yOffset + 25, myPaint);  
                itemIndex++;  
                basePos = basePos + xUnit*itemIndex;  
            }  
            
            
            myPaint.setColor(Color.WHITE);  
            myPaint.setStyle(Style.STROKE);  
            myPaint.setStrokeWidth(0);  
            myPaint.setPathEffect(null);
            int bottomWidth = 24;
            List<DataElement> itemList = series.getItems(seriesNames[0]);  
            //for(int i=0; i<itemList.size(); i++) {
            for(int i=0; i<batteryNum; i++) {
                canvas.drawText(i+"", 100+bottomWidth*i, 290, myPaint);
            }  
        }  
    }  
      
      
    public DataSeries getMockUpSeries() {  
        series = new DataSeries();  
        List<DataElement> itemListOne = new ArrayList<DataElement>();  
        itemListOne.add(new DataElement("jacket",80.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",1280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",3280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",3110.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",4500.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",3800.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",2080.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",3850.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",980.0f, platterTable[1]));
        
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        itemListOne.add(new DataElement("jacket",280.0f, platterTable[1]));
        
        series.addSeries("First Quarter", itemListOne);  
        
        return series;  
    }

	public String getUnitStr() {
		return unitStr;
	}

	public void setUnitStr(String unitStr) {
		this.unitStr = unitStr;
	}

	public int getBatteryNum() {
		return batteryNum;
	}

	public void setBatteryNum(int batteryNum) {
		this.batteryNum = batteryNum;
	}  
    
    
    
    
  
}  