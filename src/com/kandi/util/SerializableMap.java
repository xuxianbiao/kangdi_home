package com.kandi.util;

import java.io.Serializable;
import java.util.Map;

public class SerializableMap implements Serializable {
	 
    private Map<Integer, Boolean> map;
 
    public Map<Integer, Boolean> getMap() {
        return map;
    }
 
    public void setMap(Map<Integer, Boolean> map) {
        this.map = map;
    }
}