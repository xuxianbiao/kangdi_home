package com.kandi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;

public class ChartUtil {
	public static HashMap<Double,Double> intArrToHashMap(int[] generalInfo){
		HashMap<Double, Double> map = new HashMap<Double, Double>();
		//for(int i =0;i<generalInfo.length;i++){
		for(int i =generalInfo.length-1;i>-1;i--){
			map.put((double)i, (double)generalInfo[i]);
		}
		return map;
	}
	public static DataSeries getBarData(int[] nCellVolArr) {  
		DataSeries series = new DataSeries();  
        List<DataElement> itemListOne = new ArrayList<DataElement>();
        for(int i =0;i<nCellVolArr.length;i++){
        	float gi = nCellVolArr[i];
        	if(gi<1200){
        		itemListOne.add(new DataElement(i+"",nCellVolArr[i], Color.RED));
        	}else if(gi>=1200&&gi<=3600){
        		itemListOne.add(new DataElement(i+"",nCellVolArr[i], Color.BLUE));
        	}else{
        		itemListOne.add(new DataElement(i+"",nCellVolArr[i], Color.YELLOW));
        	}
        	
        }
        
        series.addSeries("", itemListOne);  
        
        return series;  
    }  
}
