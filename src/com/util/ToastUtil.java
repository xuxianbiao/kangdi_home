package com.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static String oldMsg;  
    protected static Toast toast   = null;  
    private static long oneTime=0;  
    private static long twoTime=0;  
    
    /**
     * debug 开关，控制showDbgToast()显示
     */
    private static boolean isDebugOn = false; 

    public static void setDebugOn(boolean _isDebugOn) {
    	isDebugOn = _isDebugOn;
    }
    public static boolean isDebugOn() {
    	return isDebugOn;
    }
    
    /**
     * 实时显示/更新Toast内容，不受Toast输出队列时滞影响
     * @param context
     * @param s
     * @param duration
     */
    public static void showToast(Context context, String s, int duration){      
    	if(toast==null){ 
    		if(context == null) return;
            toast =Toast.makeText(context, s, duration);  
            toast.show();  
            oneTime=System.currentTimeMillis();
        }else{  
            twoTime=System.currentTimeMillis();  
            if(s.equals(oldMsg)){  
                if(twoTime-oneTime>duration){  
                    toast.show();  
                }  
            }else{  
                oldMsg = s;  
                toast.setText(s);  
                toast.show();  
            }         
        }  
        oneTime=twoTime;  
    }  
      
    /**
     * 实时显示/更新Toast内容，不受Toast输出队列时滞影响
     * @param context
     * @param resId
     * @param duration
     */
    public static void showToast(Context context, int resId,int duration){     
        showToast(context, context.getString(resId),duration);  
    }  

    
    /**
     * 实时显示/更新Toast内容，不受Toast输出队列时滞影响
     * @param context
     * @param s
     */
    public static void showDbgToast(Context context, String s){   
    	if(isDebugOn) {
    		showToast(context, s, Toast.LENGTH_LONG);
    	}
    }  

}
