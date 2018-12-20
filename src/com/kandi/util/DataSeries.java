package com.kandi.util;

import java.util.HashMap;  
import java.util.List;  
  
public class DataSeries {  
    private HashMap<String, List<DataElement>> map;  
      
    public DataSeries() {  
        map = new HashMap<String, List<DataElement>>();  
    }  
      
    public void addSeries(String key, List<DataElement> itemList) {  
        map.put(key, itemList);  
    }  
      
    public List<DataElement> getItems(String key) {  
        return map.get(key);  
    }  
      
    public int getSeriesCount() {  
        return map.size();  
    }  
      
    public String[] getSeriesKeys() {  
        return map.keySet().toArray(new String[0]);  
    }  
  
} 
