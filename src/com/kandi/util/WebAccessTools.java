package com.kandi.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.kandi.home.R;

import android.content.Context;
import android.widget.Toast;

public class WebAccessTools {  
    
    private Context context;  
    public WebAccessTools(Context context) {  
        this.context = context;  
    }  
      
    public  String getWebContent(String url) {  
        HttpGet request = new HttpGet(url);  
        HttpParams params=new BasicHttpParams();  
        HttpConnectionParams.setConnectionTimeout(params, 30000);  
        HttpConnectionParams.setSoTimeout(params, 50000);  
        HttpClient httpClient = new DefaultHttpClient(params);  
        try{  
            HttpResponse response = httpClient.execute(request);  
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                String content = EntityUtils.toString(response.getEntity());  
                return content;  
            } else {  
                Toast.makeText(context, context.getString(R.string.net_error_tocheck), Toast.LENGTH_LONG).show();  
                System.out.println("connection error");
            }  
              
        }catch(Exception e) {  
            e.printStackTrace();  
        } finally {  
            httpClient.getConnectionManager().shutdown();  
        }  
        return null;  
    }  
}  
