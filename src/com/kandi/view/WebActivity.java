package com.kandi.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kandi.base.BaseActivity;
import com.kandi.customview.LinearLayoutThatDetectsSoftKeyboard.Listener;
import com.kandi.dao.FavDao;
import com.kandi.event.DRI_INSERT_CHARGEREvent;
import com.kandi.event.FinishWebEvent;
import com.kandi.event.PlayFreshWebListEvent;
import com.kandi.event.base.BaseEvent;
import com.kandi.fragment.WebHomeFragment;
import com.kandi.home.R;
import com.kandi.model.FavModel;
import com.kandi.util.FileUtil;

import de.greenrobot.event.EventBus;

public class WebActivity extends BaseActivity implements Listener,View.OnClickListener{
	@Override
	protected void onResume() {
		super.onResume();
		wv.getSettings().setJavaScriptEnabled(true);
		wv.resumeTimers();
		//加载页面
//		Intent intent = this.getIntent();
//		String url = intent.getStringExtra("url");
//
//		if(url!=null && !url.isEmpty()){
//			net_address.setText(url);
//			wv.loadUrl(url);
//		}
//		
//		intent.removeExtra("url");	//防止Activity切换返回时当前url被初始url覆盖
	}
	
	@Override
	protected void onStop() {
		wv.getSettings().setJavaScriptEnabled(false);
		super.onStop();
	}

	@Override
	protected void onPause() {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
//		wv.reload (); //解决Activity关闭后网页视频依然播放， 参考http://www.2cto.com/kf/201501/366465.html
		super.onPause();
		wv.pauseTimers();
		System.gc();
	}
	private FastWebView wv;
	private AutoCompleteTextView net_address;
	private LinearLayout bgview;
	private ImageView web_reload;
	private ImageView home;
	private ImageView web_stop;
	private ImageView web_forward;
	private ImageView web_back;
	private ImageView web_fav;
	private ProgressBar webProgressBar ;
	private FrameLayout webFragmentView;
	
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishWebEvent",FinishWebEvent.class);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView(){
		wv = (FastWebView) findViewById(R.id.wv);
		//支持javascript
		wv.getSettings().setJavaScriptEnabled(false); 
		// 设置可以支持缩放 
		wv.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		wv.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		wv.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wv.getSettings().setLoadWithOverviewMode(true);
		
		webProgressBar = (ProgressBar)findViewById(R.id.webProgressBar);
		net_address = (AutoCompleteTextView) findViewById(R.id.net_address);
		net_address.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent arg2) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(arg2.getAction() == KeyEvent.ACTION_DOWN){
//						if(BaseApplication.getInstance().requestCarSpeedMax()){
//							return true;
//						}
						String url = net_address.getText()+"";
						if(!url.startsWith("http://")){
							url = "http://"+url;
						}
						if (URLUtil.isNetworkUrl(url)) {
							wv.loadUrl(url);
							wv.loadUrl(url);
							InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
							if(imm.isActive()){  
								imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
							}  
							hideHomeFragment();
		                }
						return true;  
					}
				}  
				return false; 
			}
		});
		
		net_address.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = net_address.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > net_address.getWidth()
                        - net_address.getPaddingRight()
                        - drawable.getIntrinsicWidth()){
                	net_address.setText("");
                }
                return false;
            }
        });

	
		web_fav = (ImageView) findViewById(R.id.web_fav);
		web_fav.setOnClickListener(this);
		web_reload = (ImageView) findViewById(R.id.web_reload);
		web_reload.setOnClickListener(this);
		home = (ImageView) findViewById(R.id.home);
		home.setOnClickListener(this);
		web_stop = (ImageView) findViewById(R.id.web_stop);
		web_stop.setOnClickListener(this);
		web_forward = (ImageView) findViewById(R.id.web_forward);
		web_forward.setOnClickListener(this);
		web_back = (ImageView) findViewById(R.id.web_back);
		web_back.setOnClickListener(this);
		
		WebChromeClient wvcc = new WebChromeClient() {  
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					webProgressBar.setVisibility(View.INVISIBLE);
				} else {
					if (View.INVISIBLE == webProgressBar.getVisibility()) {
						webProgressBar.setVisibility(View.VISIBLE);
					}
					webProgressBar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
            @Override  
            public void onReceivedTitle(WebView view, String title) {  
                super.onReceivedTitle(view, title);  
                Log.d("ANDROID_LAB", "TITLE=" + title);  
                //txtTitle.setText("ReceivedTitle:" +title);  
                FavModel hm = new FavModel();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String addtime = sdf.format(new Date());
                hm.setAddtime(addtime);
                hm.setTitle(title);
                hm.setUrl(view.getUrl());
                hm.setOrderby(0);
                hm.setFavtype(1);	//历史记录
                FavDao.addFav(WebActivity.this, hm);
            }  
  
        };
        wv.setWebChromeClient(wvcc);
        wv.setDownloadListener(new MyWebViewDownLoadListener());
		wv.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				List<FavModel> list = FavDao.findFavItemByUrl(WebActivity.this, url);
				if(list.size()==0){
					web_fav.setImageResource(R.drawable.web_btn_collection);
				}else{
					web_fav.setImageResource(R.drawable.web_btn_collection_pressed);
				}
				if(!wv.getSettings().getLoadsImagesAutomatically()) {
			        wv.getSettings().setLoadsImagesAutomatically(true);
			    }
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				net_address.setText(url);
				if (wv.canGoBack()) {
					web_back.setEnabled(true);
				}else{
					web_back.setEnabled(false);
				}

				if (wv.canGoForward()) {
					web_forward.setEnabled(true);
				}else{
					web_forward.setEnabled(false);
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				if(BaseApplication.getInstance().requestCarSpeedMax()){
//					return true;
//				}
				return super.shouldOverrideUrlLoading(view, url);
			}
			
		});

		bgview = (LinearLayout) findViewById(R.id.bgview);
		webFragmentView = (FrameLayout) findViewById(R.id.web_home_fragment);
		
		OnNewUrlIntf newUrlIntf = new OnNewUrlIntf() {
			
			@Override
			public void onNewUrl(String url) {
				hideHomeFragment();
				net_address.setText(url);
				wv.loadUrl(url);
			}
		};
		
		WebHomeFragment webHomeFragment = new WebHomeFragment();
		webHomeFragment.setNewUrlIntf(newUrlIntf);
		
		// 添加HomeFragment
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.web_home_fragment, webHomeFragment);
		transaction.commit();
		
		showHomeFragment();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));

		setContentView(R.layout.web_pannel);
		this.initEvent();
		this.initView();
		this.init();
		ConnectivityManager connectionManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);    
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		if(networkInfo!=null){
			System.out.println(networkInfo.getType()+" "+networkInfo.getTypeName());
			if(networkInfo.getType()!=1){
				new AlertDialog.Builder(WebActivity.this)   
				.setTitle(getString(R.string.prompt))
				.setMessage(getString(R.string.now_use_networks))
				.setPositiveButton(getString(R.string.makesure), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				})  
				.show();
			}
		}
		
	}
	public void init() {
	    if(Build.VERSION.SDK_INT >= 19) {
	        wv.getSettings().setLoadsImagesAutomatically(true);
	    } else {
	        wv.getSettings().setLoadsImagesAutomatically(false);
	    }
	}
	public void FinishWebEvent(FinishWebEvent event) {
		Log.d("ui_stack",(new Throwable().getStackTrace()[0].toString()));
		this.moveTaskToBack(true);
	}
	@Override
	public void onSoftKeyboardShown(boolean isShowing) {
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.web_back:
			if (webFragmentView.getVisibility() == View.VISIBLE) {
				webFragmentView.setVisibility(View.GONE);
				web_stop.setEnabled(true);
				web_reload.setEnabled(true);
				web_fav.setEnabled(true);
				home.setEnabled(true);
			}
			else if(wv.canGoBack()){
				wv.goBack();
			}else{
				showHomeFragment();
			}
			
			break;
		case R.id.home:
			showHomeFragment();
			break;
		case R.id.web_forward:
			if(wv.canGoForward()){
				wv.goForward();
			}
			hideHomeFragment();
			break;
		case R.id.web_reload:
//			if(BaseApplication.getInstance().requestCarSpeedMax()){
//				break;
//			}
			wv.loadUrl(net_address.getText()+"");
			break;
		case R.id.web_stop:
			wv.stopLoading();
			break;
		case R.id.web_fav:
			String url = wv.getUrl();
			if (null == url) {
				return;
			}
			System.out.println(url);
			List<FavModel> list = FavDao.findFavItemByUrl(WebActivity.this, url);
			if(list.size()==0){
				
				web_fav.setImageResource(R.drawable.web_btn_collection_pressed);
				FavModel fm = new FavModel();
				fm.setIcon(""); 
				fm.setUrl(url);
				fm.setTitle(wv.getTitle());
				
	            fm.setThumb("");
	            fm.setFavtype(0);	//网页收藏
	            FavDao.addFav(this, fm);
			}else{
				FavDao.deleteFavByUrl(this, url);
				web_fav.setImageResource(R.drawable.web_btn_collection);
			}
			
			break;
		default:
			break;
		}
	}
	
	private void showHomeFragment() {
		PlayFreshWebListEvent event = new PlayFreshWebListEvent();
		event.type = PlayFreshWebListEvent.WEBLIST_STATE;
		EventBus.getDefault().post(event);
		webFragmentView.setVisibility(View.VISIBLE);
//		if (wv.canGoBack()) {
//			web_back.setEnabled(true);
//		}
//		else {
//		}
		web_back.setEnabled(false);
		
		web_forward.setEnabled(false);
		web_stop.setEnabled(false);
		web_reload.setEnabled(false);
		web_fav.setEnabled(false);
		home.setEnabled(false);
	}
	
	private void hideHomeFragment() {
		webFragmentView.setVisibility(View.GONE);
		
		if (wv.canGoBack()) {
			web_back.setEnabled(true);
		}

		if (wv.canGoForward()) {
			web_forward.setEnabled(true);
		}
		web_stop.setEnabled(true);
		web_reload.setEnabled(true);
		web_fav.setEnabled(true);
		home.setEnabled(true);
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		this.setIntent(intent);
		super.onNewIntent(intent);
	}
	
	
	public interface OnNewUrlIntf {
		public void onNewUrl(String url);
	}
	
	//内部类
	private class MyWebViewDownLoadListener implements DownloadListener {

	        @Override
	        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
	                                    long contentLength) {
	        	if((contentLength/1024/1024)<FileUtil.getSDFreeSize()){
	        		if(mimetype.equals("audio/mpeg") || mimetype.equals("audio/amr")
	        				|| mimetype.equals("audio/flac") || mimetype.equals("audio/x-wav")
	        				|| mimetype.equals("video/mp4") || mimetype.equals("video/3gp")
	        				|| mimetype.equals("audio/aac") || mimetype.equals("audio/x-m4a")
	        				|| mimetype.equals("application/force-download")){
	        			DownloaderTask task=new DownloaderTask();
	        			task.execute(url,mimetype,contentDisposition);
	        		}
	        	}else{
	        		Toast t = Toast.makeText(getApplicationContext(), getString(R.string.sdcard_isfull),
							Toast.LENGTH_LONG);
					t.setGravity(Gravity.CENTER, 0, 0);
					t.show();
	        	}
	        }

	    }

	// 内部类
	private class DownloaderTask extends AsyncTask<String, Void, String> {

		public DownloaderTask() {
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String mimetype = params[1];
			String fileName;
			String url = params[0];
			String contentDisposition = params[2];
			if(mimetype.equals("application/force-download")){
				fileName = url.substring(url.lastIndexOf("/") + 1);
			}else{
				String minetypetoname = getMimetypeToName(mimetype);
				if(!"".equals(minetypetoname)){
					if(contentDisposition.contains("filename")){
						contentDisposition = contentDisposition.substring(contentDisposition.indexOf("\"")+1,contentDisposition.lastIndexOf("\""));
						fileName = contentDisposition;
					}else{
						fileName = getRandomCharAndNumr(5)+minetypetoname;
					}
				}else{
					return null;
				}
			}
			fileName = URLDecoder.decode(fileName);

			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, fileName);
			if (file.exists()) {
				return fileName;
			}
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				CookieManager cookieManager = CookieManager.getInstance();
	            String CookieStr = cookieManager.getCookie(url);
				get.addHeader(new BasicHeader("Cookie",CookieStr));
				HttpResponse response = client.execute(get);
				if (HttpStatus.SC_OK == response.getStatusLine()
						.getStatusCode()) {
					HttpEntity entity = response.getEntity();
					InputStream input = entity.getContent();

					writeToSDCard(fileName, input);

					input.close();
					return fileName;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			closeProgressDialog();
			if (result == null) {
				Toast t = Toast.makeText(getApplicationContext(), getString(R.string.conn_wrong),
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
				return;
			}

			Toast t = Toast.makeText(getApplicationContext(), getString(R.string.save_sdcard_success), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			showProgressDialog();
			Toast t = Toast.makeText(getApplicationContext(), getString(R.string.downloading), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}
        
    public void writeToSDCard(String fileName,InputStream input){   
           
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){   
            File directory=Environment.getExternalStorageDirectory();   
            File file=new File(directory,fileName);   
            try {   
                FileOutputStream fos = new FileOutputStream(file);   
                byte[] b = new byte[2048];   
                int j = 0;   
                while ((j = input.read(b)) != -1) {   
                    fos.write(b, 0, j);   
                }   
                fos.flush();   
                fos.close();   
            } catch (FileNotFoundException e) {   
                e.printStackTrace();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }else{   
            Log.i("tag", "NO SDCard.");   
        }   
    }
    
    public String getMimetypeToName(String mimetype){
    	if(mimetype.equals("audio/mpeg")){
    		return ".mp3";
    	}else if(mimetype.equals("audio/amr")){
    		return ".amr";
    	}else if(mimetype.equals("audio/flac")){
    		return ".flac";
    	}else if(mimetype.equals("audio/x-wav")){
    		return ".wav";
    	}else if(mimetype.equals("video/mp4")){
    		return ".mp4";
    	}else if(mimetype.equals("video/3gp")){
    		return ".3gp";
    	}else if(mimetype.equals("audio/aac")){
    		return ".aac";
    	}else if(mimetype.equals("audio/x-m4a")){
    		return ".m4a";
    	}
		return "";
    }
    
    /** 
     * 获取随机字母数字组合 
     *  
     * @param length 
     *            字符串长度 
     * @return 
     */  
    public static String getRandomCharAndNumr(int length) {  
        String str = "";  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            boolean b = random.nextBoolean();  
            if (b) { // 字符串  
                // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母  
                str += (char) (65 + random.nextInt(26));// 取得大写字母  
            } else { // 数字  
                str += String.valueOf(random.nextInt(10));  
            }  
        }  
        return str;  
    }

	@Override
	public void CarChargingEvent(com.kandi.event.CarChargingEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void VolumeClickEvent(com.kandi.event.VolumeClickEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DRI_INSERT_CHARGEREvent(
			com.kandi.event.DRI_INSERT_CHARGEREvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DRI_CHARGER_ONOFFEvent(
			com.kandi.event.DRI_CHARGER_ONOFFEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doReceive(Intent intent) {

		final String m_key = "KD_CAST_EVENT";
		Bundle bundle = intent.getExtras();

		// 第一步分拣广播事件并用EventBus转发bundle对象（bundle有事件名称key和值）
		Set<String> keySet = bundle.keySet();
		for (String key : keySet) {
			if (!key.startsWith(m_key))
				continue;

			int eventId;
			try {
				eventId = Integer.parseInt(key.substring(m_key.length()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				continue;
			}

			if ((eventId >= BaseEvent.DRIEVENT.values().length)
					|| (eventId < 0)) {
				continue;
			}
			BaseEvent.DRIEVENT ev = BaseEvent.DRIEVENT.values()[eventId];

			switch (ev) {
			case DRI_INSERT_CHARGER:
				/** 充电抢插入状态变化事件 */
				try {
					EventBus.getDefault().postSticky(
							new DRI_INSERT_CHARGEREvent(bundle));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	
	}

	@Override
	protected void onDestroy() {
		wv.setVisibility(View.GONE);
		EventBus.getDefault().unregister(this,FinishWebEvent.class);
		super.onDestroy();
	}   
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

	
}
