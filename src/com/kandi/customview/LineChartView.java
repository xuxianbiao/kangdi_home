package com.kandi.customview;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kandi.home.R;

public class LineChartView extends View{
	
    Context context;
    
    private Point mSelectedPoint;
    int mSelectedData;

    int marginX=10;				//画布绘图区左边距
    int marginY=15;				//画布绘图区上边距

    int canvasWidth=600;		//画布绘图区宽度
    int canvasHeight=200;		//画布绘图区高度
    
    double srcCoordWidth=4;		//源坐标系宽度
    double srcCoordHeight=100;	//源坐标系高度

    double srcCoordGridH=0.5;	//源坐标格宽度
    double srcCoordGridW=5;		//源坐标格高度

    double dataOffset;
    double dataRange;
    int data[];
    Point points[];
    
    String unitX = "";
    String unitY = "";
	DecimalFormat fmtTextY = new DecimalFormat("#");		//"#.0.0"
	DecimalFormat fmtTextX = new DecimalFormat("0.#");		//"#.0.0"

	
	public void setChartCanvasSize(int width, int hight, int marginX, int marginY) {
		this.canvasWidth = width;
		this.canvasHeight = hight;
		this.marginX = marginX;
		this.marginY = marginY;
	}
	
	public void setChartSrcSize(double coordWidth, double coordHeight, double gridWidth, double gridHeight) {
		this.srcCoordWidth = coordWidth;
		this.srcCoordHeight = coordHeight;
		this.srcCoordGridW = gridWidth;
		this.srcCoordGridH = gridHeight;
	}
    
    
	public void setUnitX(String unitX) {
		this.unitX = unitX;
	}

	public void setUnitY(String unitY) {
		this.unitY = unitY;
	}

    public void setbg(int c)
    {
    	this.setBackgroundColor(c);
    }
    
    //坐标换算 src -> canvas
    int srcX2canvasX(double dx) {
    	int x = (int)(dx/srcCoordWidth*canvasWidth) + marginX;
    	return x;
    }
    int srcY2canvasY(double dy) {
    	int y = (int)((1.0-dy/srcCoordHeight)*canvasHeight) + marginY;
    	return y;
    }
    //坐标换算 canvas -> src
    double canvasX2srcX(int x) {
    	double dx = (double)(x-marginX)/canvasWidth*srcCoordWidth;
    	return dx;
    }
    double canvasY2srcY(int y) {
    	double dy = (double)(1.0-(y-marginY)/canvasHeight)*srcCoordHeight;
    	return dy;
    }
    
    
    /**
     * load data arrage
     * @param offset	offset of the range
     * @param range	the rage of the data array
     * @param d		data array
     * @param min	min valid value
     * @param max	max valid value
     */
    public void loadData(double offset, double range, int d[], int min, int max) {
    	data = d;
    	dataOffset= offset;
    	dataRange = range;
    	int len = d.length;
    	points = new Point[len];
    	for(int i=0; i<len;i++) {
    		int x = srcX2canvasX((double)i/(len-1)*srcCoordWidth);
    		int y = srcY2canvasY((double)d[i]);
    		
    		int value = d[i]; 
    		if( value >= min && value <= max) {
    			points[i] = new Point(x,y);
    		}
    		else {
    			points[i] = null;
    		}
    	}
    	this.mSelectedPoint = points[len-1];
    	this.mSelectedData = d[len-1];
    }
    
    
    int c=0;
    int resid=0;
    Boolean isylineshow;
   
    public LineChartView(Context ct)
    {
    	super(ct);
		this.context=ct;
    }
    
    public LineChartView(Context ct, AttributeSet attrs)
	{
		super( ct, attrs );
		this.context=ct;
	}
    
    public LineChartView(Context ct, AttributeSet attrs, int defStyle) 
	{		 
		super( ct, attrs, defStyle );
		this.context=ct;
	}
    
    @SuppressLint("DrawAllocation")
	@Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  

        if(c!=0)
        	this.setbg(c);
        if(resid!=0)
        	this.setBackgroundResource(resid);

        // set up paint  
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        paint.setColor(getResources().getColor(R.color.energy_chart_coordi_grid));
        paint.setStrokeWidth(1);
        paint.setStyle(Style.STROKE);
        
    	//绘制x坐标线
    	int x1=srcX2canvasX(0);
    	int x2=srcX2canvasX(srcCoordWidth);
        for(double dy=0;dy<=srcCoordHeight;dy+=srcCoordGridH)
        {
        	int y = srcY2canvasY(dy);
        	canvas.drawLine(x1,y,x2,y, paint);
        	
        	drawCoodiText(fmtTextY.format(dy)+unitY, x1-30, y+5, canvas);
        }

        //绘制y坐标线
    	int y1=srcY2canvasY(0);
    	int y2=srcY2canvasY(srcCoordHeight);
        for(double dx=0;dx<=srcCoordWidth;dx+=srcCoordGridW)
        {
        	int x = srcX2canvasX(dx);
        	canvas.drawLine(x,y1,x,y2, paint);
        	
        	drawCoodiText(fmtTextX.format(dx-srcCoordWidth)+unitX, x, y1+25, canvas);
        }

    	if(points == null || points.length < 1)
    		return;

    	//绘曲线
        paint.setColor(getResources().getColor(R.color.energy_chart_line));
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(0);
    	//drawscrollline(mPoints, canvas, paint);
		drawline(points, canvas, paint);
    	
//		//绘点
//        paint.setColor(Color.WHITE);  
//        paint.setStyle(Style.FILL);  
//        for (int i=0; i<points.length; i++)
//        {  
//        	Point p = points[i];
//        	if(p != null)
//				canvas.drawCircle(p.x, p.y, 3, paint);
//        }  
//        
//        //绘制触摸点
//        if(mSelectedPoint != null) {
//        	canvas.drawCircle(mSelectedPoint.x, mSelectedPoint.y, 5, paint);
//        }
    }  

    private void drawscrollline(Point[] ps,Canvas canvas,Paint paint)
    {
    	Point startp=new Point();
    	Point endp=new Point();
    	for(int i=0;i<ps.length-1;i++)
    	{
    		startp=ps[i];
    		endp=ps[i+1];
    		float wt=(startp.x+endp.x)/2;
        	Point p3=new Point();
        	Point p4=new Point();
        	p3.y=startp.y;
        	p4.y=endp.y;
        	
        	Path path = new Path();  
        	path.moveTo(startp.x,startp.y);
            path.cubicTo(wt, p3.y, wt, p4.y,endp.x, endp.y);
            canvas.drawPath(path, paint);
    		
    	}
    }    
    
    private void drawline(Point[] ps,Canvas canvas,Paint paint)
    {
    	Point p1;
    	Point p2;
    	for(int i=0;i<ps.length-1;i++)
    	{	
	    	p1=ps[i];
	    	p2=ps[i+1];
	    	if(p1 != null && p2 != null) {
	    		canvas.drawLine(p1.x,p1.y,p2.x,p2.y, paint);
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


    
    /***********************************************************
     * Touch event processing methods
     */
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) 
    {  
    	if (points == null) {
    		return false;
    	}
    	
        switch (event.getAction()) 
        {  
        case MotionEvent.ACTION_DOWN:  
        case MotionEvent.ACTION_MOVE:  
        	float x = event.getX();
        	double dx = canvasX2srcX((int)x);
        	
        	int i = (int)(dx/this.srcCoordWidth* points.length);
        	
        	if(i>=0 && i <points.length) {
            	mSelectedPoint = points[i];
            	mSelectedData = data[i];
        	}
        	else{
        		mSelectedPoint = points[points.length-1];
            	mSelectedData = data[points.length-1];
        	}

//            ToastUtil.showDbgToast(this.context.getApplicationContext(), 
//            	"e("+event.getX()+","+event.getY()
//            	+"),p.len="+points.length
//	            +",i="+i+",p="+this.mSelectedPoint
//	            +",d="+this.mSelectedData);
            
            break;  

        case MotionEvent.ACTION_UP:  
        default:  
    		mSelectedPoint = points[points.length-1];
        	mSelectedData = data[points.length-1];
        }         
        
    	//this.invalidate();
        return true;
    }  
     
    
	public Point getSelectedPoint() {
    	return mSelectedPoint;
    }
    
    /**
     * Get selected data value. Only available if getSelectedPoint() returns not null value.
     * @return selected data value.
     */
    public int getSelectedData() {
    	return mSelectedData;
    }
    
    
    
    /***********************************************************
     * Utilities
     */

    static public  int dip2px(Context context, float dpValue) 
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
   
    static public  int px2dip(Context context, float pxValue) 
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    

 
}  
