package com.kandi.audioControl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GPIO_Operation {

	public static final String GPIO_FILE_NAME = "/sys/class/gpio/gpio"; 
	public static final String GPIO_DIRECTION_OUT = "out"; 
	public static final String GPIO_DIRECTION_IN = "in"; 
	public static final String GPIO_VALUE_LOW = "0"; 
	public static final String GPIO_VALUE_HIGH = "1"; 
	public static final String GPIO_55 = "55"; 
	public static final String GPIO_137 = "137"; 
	
	private String gpioNumber;

	
	public GPIO_Operation(String gpioNumber)
	{
		this.gpioNumber = gpioNumber;
	}
	//设置GPIO的方向
	public boolean gpioSetDirection(String info)
	{
		System.out.println("gpioSetDirection"+GPIO_FILE_NAME + this.gpioNumber + "/direction");
		char []buff = info.toCharArray(); 
		if(writeToFile(GPIO_FILE_NAME + this.gpioNumber + "/direction", buff))
		{
			return true;
		}
		return false;	
	}

	//设置GPIO的值
	public boolean gpioSetValues(String gpio_value)
	{
		char []buff = gpio_value.toCharArray(); 
		System.out.println("gpioSetValues"+GPIO_FILE_NAME + this.gpioNumber + "/value");
		if(writeToFile(GPIO_FILE_NAME + this.gpioNumber + "/value", buff))
		{
			return true;
		}
		return false;
	}
	//获取GPIO的值
	public String gpioGetValues()
	{
		String gpioData;
		if(null != (gpioData = readToFile(GPIO_FILE_NAME + this.gpioNumber + "/value")))
		{
			return gpioData;
		}
		return null;
	}

	//写入文件
	private boolean writeToFile(String fileName, char[] buf)
	{
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			System.out.println("fileName: "+fileName);
			System.out.println("buf[0]: "+buf[0]+"len:"+buf.length);
			fw.write(buf); 
			fw.flush();   
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("exception:"+e);
			e.printStackTrace();
			return false;
		} finally {
	        try {
	        	if(fw != null)
	        		fw.close(); 
	        } catch (IOException e) {
	        e.printStackTrace();
	        }
		}
	}	
	
	//从文件中读出
	private String readToFile(String fileName)
	{
		FileReader fw = null;
		char[] data = new char[128];
		try {
			fw = new FileReader(fileName);
//			System.out.println("buf:"+buf[0]+buf[1]);
			System.out.println("fileName:"+fileName);
			fw.read(data); 
			return new String(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("exception:"+e);
			e.printStackTrace();
			return null;
		} finally {
	        try {
	        	if(fw != null)
	        		fw.close(); 
	        } catch (IOException e) {
	        e.printStackTrace();
	        }
		}
	}	
}
