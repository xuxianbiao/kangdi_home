package com.kandi.util;

import java.io.UnsupportedEncodingException;

public class CommonUtils {
	private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 200) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
    public static boolean isFastDoubleClick(int total) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < total) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
    
    public static String subStr(String str, int subSLength)    
            throws UnsupportedEncodingException{   
        if (str == null)    
            return "";    
        else{   
            int tempSubLength = subSLength;//截取字节数  
            String subStr = str.substring(0, str.length()<subSLength ? str.length() : subSLength);//截取的子串    
            int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度   
            // 说明截取的字符串中包含有汉字    
            while (subStrByetsL > tempSubLength){
                int subSLengthTemp = --subSLength;  
                subStr = str.substring(0, subSLengthTemp>str.length() ? str.length() : subSLengthTemp);    
                subStrByetsL = subStr.getBytes("GBK").length;  
            }
            return subStr.length()>=str.length()?subStr:subStr+"...";   
        }  
    }
}
