package com.kandi.customview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.kandi.home.R;
import com.kandi.util.InterpolationUtil;


public class SuperLineChartView extends SurfaceView implements Callback {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	private Paint pointPaint, linePaint, textPaint,xTextPaint;
	private int mX = getResources().getDimensionPixelSize(
			R.dimen.linechartmxval);
	private int screenW;
	private int bottomY = getResources().getDimensionPixelSize(
			R.dimen.linechartbottomy);
	private String[] deviceData;
	private int allHeight=getResources().getDimensionPixelSize(
			R.dimen.linechar_allheight);
	private int nowpy=getResources().getDimensionPixelSize(
			R.dimen.linechartmxval_nowpy);
	private int yTextpy=getResources().getDimensionPixelSize(
			R.dimen.ytextpy);
	private int line_ypy = getResources().getDimensionPixelSize(R.dimen.line_ypy);
	private void initContent() {
//		initTestData();
		this.setZOrderOnTop(true);
		mHolder = this.getHolder();
		mHolder.setFormat(PixelFormat.TRANSPARENT);
		mHolder.addCallback(this);

		pointPaint = new Paint();
		pointPaint.setAntiAlias(true);
		pointPaint.setColor(getContext().getResources().getColor(R.color.blue));
		pointPaint
				.setStrokeWidth(getPxFromResource(R.dimen.linechartpointpaintstrokewidth));

		textPaint = new Paint();
		textPaint.setColor(Color.GRAY);
		textPaint
				.setStrokeWidth(getPxFromResource(R.dimen.linecharttextpaintstrokewidth));
		textPaint
				.setTextSize(getPxFromResource(R.dimen.linecharttextpaintsize));
		textPaint.setAntiAlias(true);
		
		xTextPaint = new Paint();
		xTextPaint.setColor(getContext().getResources().getColor(R.color.blue));
		xTextPaint
		.setStrokeWidth(getPxFromResource(R.dimen.linecharttextpaintstrokewidth));
		xTextPaint
		.setTextSize(getPxFromResource(R.dimen.linecharttextpaintsize));
		xTextPaint.setAntiAlias(true);

		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStyle(Style.STROKE);
		linePaint
				.setStrokeWidth(getPxFromResource(R.dimen.linechartlinepaintstrokewidth));
		linePaint.setColor(Color.GRAY);

		setFocusable(true);
	}

	public SuperLineChartView(Context context) {
		super(context);
	}

	public SuperLineChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		initTestData();
		this.initContent();
	}

	public SuperLineChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initContent();
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
	}

	private int getPxFromResource(int dimenId) {
		return this.getResources().getDimensionPixelSize(dimenId);
	}

	private void drawInterpolationPoint(Canvas mCanvas, String data, String flag,String nowData) {
		mCanvas.drawColor(Color.argb(0, 255, 255, 255));
		mCanvas.drawLine(mX * 2, bottomY,
				getPxFromResource(R.dimen.linechartscrollwidth) - 2
						* getPxFromResource(R.dimen.linechartmxval), bottomY,
				linePaint);

		List<Map<String, Integer>> resList = new ArrayList<Map<String, Integer>>();
		if (data == null || "".equals(data)) {
			return;
		}
		String tempStr = data;
		String[] tempStrs = tempStr.split("%");
		deviceData = tempStrs[3].split("#");

//		int RColor = 254;
//		int GColor = 204;
//		int BColor = 164;
//		int isRPlus = 1;
//		int isGPlus = 1;
//		int isBPlus = 1;

		double maxNum = 0;
		double minNum = 0;
		for (int i = 0; i < deviceData.length; i++) {
//			if(i==deviceData.length-1){
//				continue;
//			}
			Map<String, Integer> map = new HashMap<String, Integer>();
			String[] temperatureInfo = deviceData[i].split("#");
			String[] dateInfo = temperatureInfo[0].split("&");
			String[] dateDetails = dateInfo[0].split("-");
			String hour = "";
			if("electricity".equals(flag)){
				hour = dateDetails[1];
			}else{
				hour = dateDetails[3];
			}
			String temperature = dateInfo[1];
			temperature = "11";
			map.put("hour", Integer.parseInt(hour));
			map.put("x", i);
			map.put("temperature", (int) - Float.parseFloat("1111.11"));
			resList.add(map);
			if(Double.parseDouble(temperature)>maxNum){
				maxNum = Double.parseDouble(temperature);
			}
			if(Double.parseDouble(temperature)<minNum){
				minNum = Double.parseDouble(temperature);
			}
		}

		if(maxNum==0&&minNum==0){
			maxNum=-1;
		}
		double[] xs = new double[resList.size()];
		double[] ys = new double[resList.size()];

		for (int i = 0; i < resList.size(); i++) {
			Map<String, Integer> map = resList.get(i);
			int x = map.get("x");
			int y = map.get("temperature");

			xs[i] = x;
			ys[i] = y;
		}
		InterpolationUtil it = new InterpolationUtil(xs, ys, true);

		double startX = xs[0];
		int slopeRate = getPxFromResource(R.dimen.linechartsloperate);
		int plusVal = 0;
		for(double i=xs[1];i<=xs[xs.length-1];i=i+0.005){
//			if(plusVal%50==0){
//				RColor = RColor+1*isRPlus;
//	    		GColor = GColor+2*isGPlus;
//	    		BColor = BColor+3*isBPlus;
//	    		if(RColor<=0){
//	    			isRPlus = 1;
//	    			RColor = RColor+1*isRPlus;
//	    		}else if(RColor>=255){
//	    			isRPlus = -1;
//	    			RColor = RColor+1*isRPlus;
//	    		}
//	    		if(GColor<=0){
//	    			isGPlus = 1;
//	    			GColor = GColor+2*isGPlus;
//	    		}else if(GColor>=255){
//	    			isGPlus = -1;
//	    			GColor = GColor+2*isGPlus;
//	    		}
//	    		
//	    		if(BColor<=0){
//	    			isBPlus = 1;
//	    			BColor = BColor+3*isBPlus;
//	    		}else if(BColor>=255){
//	    			isBPlus = -1;
//	    			BColor = BColor+3*isBPlus;
//	    		}
//	    		pointPaint.setColor(Color.argb(0xff, RColor, GColor, BColor));
//			}
			plusVal++;
			
			
			int x = (int)((i*slopeRate)-(startX*slopeRate)+mX*2);
			//int y = (int)(it.splineInterpolation(i)*10);
//			int y = (int)(it.linearInterpolation(i)*5)/allHeight;
			float y = bottomY+(float)((it.linearInterpolation(i)+minNum)/(maxNum-minNum))*allHeight;
			mCanvas.drawCircle((screenW)-x,  y, getPxFromResource(R.dimen.linechartpointradius), pointPaint);
		}

		int pointCount1 = 0;
		int first = 0;
		for (double i = xs[0]; i <= xs[xs.length - 1]; i = i + 0.01) {
			int x = (int) ((i * slopeRate) - (startX * slopeRate) + mX * 2);
			float y = bottomY+(float)((it.linearInterpolation(i)+minNum)/(maxNum-minNum))*allHeight;
			if (pointCount1 == 0) {
			}
			pointCount1++;
 			if (pointCount1 % 100 == 0) {
				mCanvas.drawLine((screenW) - x,  y, (screenW) - x, bottomY,linePaint);
				String dt = deviceData[(pointCount1 / 100)].split("&")[0].split("-")[3];
				if(first==0){
					mCanvas.drawText(Integer.parseInt(dt)+"/???"+"",(screenW)- x- (15 * dt.length())+ getPxFromResource(R.dimen.linechartlefttxtdeviation),bottomY + line_ypy, textPaint);
 				}else{
 					mCanvas.drawText(Integer.parseInt(dt)+"",(screenW)- x- (15 * dt.length())+ getPxFromResource(R.dimen.linechartlefttxtdeviation),bottomY + line_ypy, textPaint);
 				}
 				first++;
				String tempY = "";
				if("hum".equals(flag)){
					tempY = (int)Float.parseFloat(tempY)+"";
				}
				if("temp".equals(flag)){
					tempY = (int)Float.parseFloat(tempY)+"";
				}
				if("power".equals(flag)){
					tempY = ""+(int)Float.parseFloat("100");
				}
				mCanvas.drawText(tempY,(screenW)- x- (11 * tempY.length())+ getPxFromResource(R.dimen.linechartlefttxtdeviation),y - yTextpy, xTextPaint);
				Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
				mCanvas.drawBitmap(mBitmap,(screenW)- x- getPxFromResource(R.dimen.linechartpointicondeviation), y- getPxFromResource(R.dimen.linechartpointicondeviation),pointPaint);
			}
		}

		// drawVerticalLines(mCanvas, pointLs);
	}

	public void mDraw(String data, String flag,String nowData) {
		mCanvas = mHolder.lockCanvas();
		// mCanvas.drawColor(Color.WHITE);
		// drawPoint(mCanvas);
		// drawLines(mCanvas);
		// mPaint.setColor(Color.BLACK);
		if(mCanvas==null){
			return;
		}
		mCanvas.drawColor(Color.argb(0, 255, 255, 255));
		if (data != null && !"".equals(data)) {
			if("hum".equals(flag)){
				allHeight=getResources().getDimensionPixelSize(
						R.dimen.linechar_allheight);
				bottomY = getResources().getDimensionPixelSize(
						R.dimen.linechartbottomy);
			}
			if("temp".equals(flag)){
				allHeight=getResources().getDimensionPixelSize(
						R.dimen.linechar_tempallheight);
				bottomY = getResources().getDimensionPixelSize(
						R.dimen.linechartbottomy);
			}
			if("power".equals(flag)){
				bottomY = getResources().getDimensionPixelSize(
						R.dimen.linechartpowerbottomy);
			}
			drawInterpolationPoint(mCanvas, data, flag,nowData);
		}
		mHolder.unlockCanvasAndPost(mCanvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

}
