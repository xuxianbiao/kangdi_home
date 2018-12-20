package com.kandi.customview;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.kandi.home.R;

public class BarChatView extends  View{  

	boolean isAutoCoordY = true;	//根据数据自动调整Y坐标比例

    int marginX=10;				//画布绘图区左边距
    int marginY=15;				//画布绘图区上边距

    int canvasWidth=600;		//画布绘图区宽度
    int canvasHeight=200;		//画布绘图区高度
    
    //double srcCoordOffsetX=0;	//源坐标原点X偏移量
    double srcCoordWidth=4;		//源坐标系宽度
    double srcCoordMinY=0;		//源坐标系Y最小值
    double srcCoordMaxY=100;	//源坐标系Y最大值

    double srcCoordGridH=0.5;	//源坐标格宽度
    double srcCoordGridW=5;		//源坐标格高度

    double dataOffset;
    double dataRange;
    int data[];
    int dataLength;
    Point points[];
    
    String unitX = "";
    String unitY = "";
	DecimalFormat fmtTextY = new DecimalFormat("#");		//"#.0.0"
	DecimalFormat fmtTextX = new DecimalFormat("0.#");		//"#.0.0"
    
	int colorBar = Color.BLUE&0x80ffffff;
	int colorBarMax = Color.RED&0x80ffffff;
	int colorBarMin = Color.YELLOW&0x80ffffff;
	int levelMaxData=100;
	int levelMinData=0;
    
	public void setChartCanvasSize(int width, int hight, int marginX, int marginY) {
		this.canvasWidth = width;
		this.canvasHeight = hight;
		this.marginX = marginX;
		this.marginY = marginY;
	}
	
	public void setChartSrcSize(double coordWidth, double srcCoordMinY, double srcCoordMaxY, double gridWidth, double gridHeight) {
		this.srcCoordWidth = coordWidth;
		this.srcCoordMinY = srcCoordMinY;
		this.srcCoordMaxY = srcCoordMaxY;
		this.srcCoordGridW = gridWidth;
		this.srcCoordGridH = gridHeight;
	}
    
    
	public void setUnitX(String unitX) {
		this.unitX = unitX;
	}

	public void setUnitY(String unitY) {
		this.unitY = unitY;
	}

	   
    //坐标换算 src -> canvas
    int srcX2canvasX(double dx) {
    	int x = (int)(dx/srcCoordWidth*canvasWidth) + marginX;
    	return x;
    }
    int srcY2canvasY(double dy) {
    	int y = (int)((1.0-(dy-srcCoordMinY)/(srcCoordMaxY-srcCoordMinY))*canvasHeight) + marginY;
    	return y;
    }
    //坐标换算 canvas -> src
    double canvasX2srcX(int x) {
    	double dx = (double)(x-marginX)/canvasWidth*srcCoordWidth;
    	return dx;
    }
    double canvasY2srcY(int y) {
    	double dy = (1.0 - (double)(y-marginY)/canvasHeight)*(srcCoordMaxY-srcCoordMinY) + srcCoordMinY;
    	return dy;
    }
    
     
    public BarChatView(Context context) {  
        super(context);  
    }  
    public BarChatView(Context context, AttributeSet attrs) {  
        super(context,attrs);  
    } 
    public BarChatView(Context context, AttributeSet attrs, int defStyle) {  
        super(context,attrs,defStyle);  
    } 
      
    public void setData(int data[], int length) {  
        
    	//set date
    	this.data = data;
        this.dataLength = length;
        
        //set max/min data
        levelMaxData=data[0];
        levelMinData=data[0];
        for(int i=0; i< length;i++) {
            int d = data[i];
        	if(d > levelMaxData) levelMaxData = d;
        	if(d < levelMinData) levelMinData = d;
        }
        
        //auto coordinate y 
        if(isAutoCoordY) {
        	srcCoordMaxY = (int)(levelMaxData/srcCoordGridH+1)*(srcCoordGridH);											//顶部留余量
        	srcCoordMinY = (int)(levelMinData/srcCoordGridH- (((levelMinData%srcCoordGridH)==0)?0:1))*srcCoordGridH;	//底部对齐
        	
        	if(srcCoordMaxY<0) srcCoordMaxY = 0;
        	if(srcCoordMinY>0) srcCoordMinY = 0;
        }
        
    }  

    @Override    
    public void onDraw(Canvas canvas) {   

        // set up paint  
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        paint.setColor(getResources().getColor(R.color.energy_chart_coordi_grid));
        paint.setStrokeWidth(1);
        paint.setStyle(Style.STROKE);
        
    	//draw x coordinate line
    	int x1=srcX2canvasX(0);
    	int x2=srcX2canvasX(srcCoordWidth);
        for(double dy=srcCoordMinY;dy<=srcCoordMaxY;dy+=srcCoordGridH)
        {
        	int y = srcY2canvasY(dy);
        	canvas.drawLine(x1,y,x2,y, paint);
        	
        	//drawCoodiText(fmtTextY.format(dy)+unitY, x1-30, y+5, canvas);
        	drawCoodiText(fmtTextY.format(dy), x1-30, y+5, canvas);
        }

        //draw y coordinate line
    	int y1=srcY2canvasY(srcCoordMinY);
    	int y2=srcY2canvasY(srcCoordMaxY);
        for(double dx=0;dx<=srcCoordWidth;dx+=srcCoordGridW)
        {
        	int x = srcX2canvasX(dx);
        	canvas.drawLine(x,y1,x,y2, paint);
        	
        	//drawCoodiText(fmtTextX.format(dx-srcCoordWidth)+unitX, x, y1+25, canvas);
        }
        
        
        paint.setStyle(Style.FILL); 
        final float srcDx=0.6f;
        for(int i=0; i<dataLength; i++) {
        	
        	int d = data[i];
        	float rx0 = srcX2canvasX(i+0.5-srcDx/2);
        	float rx1 = srcX2canvasX(i+0.5+srcDx/2);
        	float ry0 = srcY2canvasY((d>0)?d:0);
        	float ry1 = srcY2canvasY((d>0)?0:d);

        	//draw bar
        	if(d>=this.levelMaxData) {
                paint.setColor(this.colorBarMax);
        	}
        	else if(d<=this.levelMinData) {
        		paint.setColor(this.colorBarMin);
        	}
        	else {
        		paint.setColor(this.colorBar);
        	}
        	
        	canvas.drawRect(rx0, ry0, rx1, ry1, paint);  	
        	
        	//draw x coordinate text
        	int x = (int)((rx0+rx1)/2);
        	drawCoodiText(fmtTextX.format(i+1)+unitX, x, y1+25, canvas);
        	
        	//draw data value
        	if(srcCoordWidth < 10) {
        		drawValueText(fmtTextX.format(d)+unitY, x, (d>0)?((int)ry0-5):((int)ry1-5), canvas);
        	}
        	else {
        		drawValueTextVertical(fmtTextX.format(d)+unitY, x, (int)ry1-5, canvas);
        	}
        	
        }

    }  
    
    private void drawCoodiText(String text,int x,int y,Canvas canvas)
    {
    	Paint p = new Paint();
    	p.setAlpha(0x0000ff);   
    	p.setTextSize(15);   
        p.setColor(getResources().getColor(R.color.energy_chart_coordi_text));
        
//    	String familyName = "宋体";   
//    	Typeface font = Typeface.create(familyName,Typeface.ITALIC);   
//    	p.setTypeface(font);   
    	p.setTextAlign(Paint.Align.CENTER);     
    	canvas.drawText(text, x, y, p);
    }

    private void drawValueText(String text,int x,int y,Canvas canvas)
    {
    	Paint p = new Paint();
    	p.setAlpha(0x0000ff);   
    	p.setTextSize(15);   
        p.setColor(getResources().getColor(R.color.energy_chart_value_text));
        
//    	String familyName = "宋体";   
//    	Typeface font = Typeface.create(familyName,Typeface.ITALIC);   
//    	p.setTypeface(font);   
    	p.setTextAlign(Paint.Align.CENTER);     
    	canvas.drawText(text, x, y, p);
    }
    

    private void drawValueTextVertical(String text,int x,int y,Canvas canvas)
    {
    	Paint p = new Paint();
    	p.setAlpha(0x0000ff);   
    	p.setTextSize(15);   
        //p.setColor(getResources().getColor(R.color.energy_chart_value_text_vertical));
    	p.setColor(getResources().getColor(R.color.energy_chart_value_text));
        
//    	String familyName = "宋体";   
//    	Typeface font = Typeface.create(familyName,Typeface.ITALIC);   
//    	p.setTypeface(font);   
    	
        //p.setTextAlign(Paint.Align.CENTER);
        p.setTextAlign(Paint.Align.RIGHT);

    	canvas.save();
    	canvas.rotate(90, x, y);
    	canvas.drawText(text, x, y+5, p);
    	canvas.restore();
    }
    
}  