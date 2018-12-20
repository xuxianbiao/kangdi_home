package com.kandi.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kandi.adapter.CamVideoGridAdapter;
import com.kandi.dao.LockFileDao;
import com.kandi.event.FinishCameraEvent;
import com.kandi.event.PlayVideoEvent;
import com.kandi.home.R;
import com.kandi.model.LockFileModel;
import com.kandi.util.FileUtil;

import de.greenrobot.event.EventBus;

public class CameraActivity extends Activity implements android.os.Handler.Callback{
    
    private SurfaceView canmare_SurfaceView;
    private SurfaceView video_surfaceview;
	private ImageButton canmare_play;
	private MediaPlayer mediaPlayer;
	private int currentPosition = 0;
	private ImageView canmare_pic;
	private int playSts = 0;//0 idle  1:playing 2:pause
	private ImageButton media_play;
	private GridView history_list;
	private String currentPlayFilePath = Environment.getExternalStorageDirectory().getPath();
	private List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	private SeekBar progressBar1;
	private Handler handler;
	private ImageButton media_next;
	private int currentIndex = 0;
	private TextView media_current;
	private TextView media_total;
	private ImageButton media_back;
	
	private Camera mCamera;
	private boolean bIfPreview = false;  
	private RelativeLayout loadingView;
	private void initCamera(){  
		video_surfaceview.setVisibility(View.VISIBLE);
		canmare_SurfaceView.setVisibility(View.GONE);
		if(!bIfPreview){  
			try{  
				for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
					CameraInfo info = new CameraInfo();
					Camera.getCameraInfo(i, info);
					if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
						mCamera = Camera.open(i);
					}
				}
				//mCamera = Camera.open();  
			}catch(Exception e){  
				e.printStackTrace();  
			}  
		}  
	    if (mCamera != null && !bIfPreview){  
	    	try{  
	    		mCamera.setPreviewDisplay(video_surfaceview.getHolder());  
	    		Camera.Parameters parameters = mCamera.getParameters();  
	    		parameters.setPictureFormat(PixelFormat.JPEG);  
	    		List<Camera.Size> s=parameters.getSupportedPreviewSizes();  
	    		try{  
	    			s=parameters.getSupportedPictureSizes();  
	    			try{  
	    				mCamera.setParameters(parameters);  
	    				mCamera.setPreviewDisplay(video_surfaceview.getHolder());  
	    				mCamera.startPreview();  
	    				bIfPreview = true;  
	    			}catch (Exception e) {  
	    				e.printStackTrace();  
	    			}     
	    		}  catch (Exception e) {  
	    			e.printStackTrace();  
	    		}  
	    	}catch (IOException e){  
	    		mCamera.release();  
	    		mCamera = null;  
	    		e.printStackTrace();  
	    	}  
	    }  
	} 
	private void resetCamera(){  
		if (mCamera != null && bIfPreview){  
			mCamera.stopPreview();  
			bIfPreview = false;  
		}  
	} 
	
	private OnSeekBarChangeListener osbcl = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			int progress = seekBar.getProgress();
			if (mediaPlayer != null) {
				mediaPlayer.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};
	private void initEvent(){
		EventBus.getDefault().register(this,"FinishCameraEvent",FinishCameraEvent.class);
	}
	private void refreshFiles(){
		loadingView.setVisibility(View.VISIBLE);
		new Thread(){
			@Override
			public void run() {
				CameraActivity.this.loadFiles();
				Message msg = new Message();
				msg.what =2;
				handler.sendMessage(msg);
				super.run();
			}
		}.start();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		EventBus.getDefault().postSticky(new BottomMenuServiceHideEvent());
		super.onResume();
	}
	public void initView(){
		handler = new Handler(this);
		loadingView = (RelativeLayout) findViewById(R.id.loadingView);
		
		video_surfaceview = (SurfaceView) findViewById(R.id.video_surfaceview);
		media_back = (ImageButton) findViewById(R.id.media_back);
		media_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				canmare_pic.setImageBitmap(null);
				canmare_pic.setBackgroundColor(getResources().getColor(R.color.none_color));
				canmare_play.setVisibility(View.GONE);
				CameraActivity.this.initCamera();
			}
		});
		media_current = (TextView) findViewById(R.id.media_current);
		media_total = (TextView) findViewById(R.id.media_total);
		media_next = (ImageButton) findViewById(R.id.media_next);
		media_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mediaPlayer==null){
					return;
				}
				if(dataList.size()==0){
					return;
				}

				playSts=0; 
				currentIndex = currentIndex+1;
				if(currentIndex>=dataList.size()-1){
					currentIndex = dataList.size()-1;
				}
				currentPlayFilePath = dataList.get(currentIndex).get("itemText")+"";
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mediaPlayer.reset();
				play(0);
				playSts=1;
			}
		});
		progressBar1 = (SeekBar) findViewById(R.id.seekBar);
		progressBar1.setOnSeekBarChangeListener(osbcl);
		history_list = (GridView) findViewById(R.id.history_list);
		
		refreshFiles();
		history_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final View view = arg1;
				TextView tv = (TextView) view.findViewById(R.id.filepath);
		    	final String path = tv.getText()+"";
		    	final List<LockFileModel> lfmList = LockFileDao.findLockFileByUrl(CameraActivity.this, path);
		    	
		    	final AlertDialog cameralockDialog = new AlertDialog.Builder(CameraActivity.this).create();
				
				cameralockDialog.show();  
				cameralockDialog.getWindow().setContentView(R.layout.cameralockdialog);  
//				cameralockDialog.getWindow()  
//		            .findViewById(R.id.button_back_mydialog)  
//		            .setOnClickListener(new View.OnClickListener() {  
//		            @Override  
//		            public void onClick(View v) {  
//		            	cameralockDialog.dismiss();  
//		            }  
//		        });
				cameralockDialog.getWindow()  
	            .findViewById(R.id.deletebtn).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						FileUtil.deleteFile(new File(path));
						//TODO: 需弹出确认对话框

						//Toast.makeText(CameraActivity.this, getString(R.string.deleted_success), Toast.LENGTH_SHORT).show();
						cameralockDialog.dismiss();
						refreshFiles();
					}
				});
				cameralockDialog.getWindow()  
	            .findViewById(R.id.unlockbtn).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						LockFileModel lfm = lfmList.get(0);
						LockFileDao.unlockFile(CameraActivity.this, lfm);
						cameralockDialog.dismiss();
						refreshFiles();
					}
				});
				cameralockDialog.getWindow()  
	            .findViewById(R.id.lockbtn).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						LockFileModel lfm = new LockFileModel();
						lfm.setPath(path);
						LockFileDao.addLockFile(CameraActivity.this, lfm);
						cameralockDialog.dismiss();
						refreshFiles();
					}
				});
		    	if(lfmList.size()==0){//未被锁住
		    		cameralockDialog.getWindow().findViewById(R.id.unlockbtn).setVisibility(View.GONE);
		    		cameralockDialog.getWindow().findViewById(R.id.lockbtn).setVisibility(View.VISIBLE);
		    		cameralockDialog.getWindow().findViewById(R.id.deletebtn).setVisibility(View.VISIBLE);
		    	}else{
		    		cameralockDialog.getWindow().findViewById(R.id.unlockbtn).setVisibility(View.VISIBLE);
		    		cameralockDialog.getWindow().findViewById(R.id.lockbtn).setVisibility(View.GONE);
		    		cameralockDialog.getWindow().findViewById(R.id.deletebtn).setVisibility(View.GONE);
		    	}
				
				
				
				
				
				
				return false;
			}
		});
		history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				video_surfaceview.setVisibility(View.GONE);
				canmare_SurfaceView.setVisibility(View.VISIBLE);
				CameraActivity.this.resetCamera();
				
				playSts=0; 
				currentIndex = position;
				TextView tv = (TextView) view.findViewById(R.id.filepath);
				currentPlayFilePath = tv.getText()+"";
				media_play.setImageResource(R.drawable.cam_opt_btn_pause);
				canmare_pic.setImageBitmap(null);
				canmare_pic.setBackgroundColor(getResources().getColor(R.color.none_color));
				canmare_play.setVisibility(View.GONE);
				if(mediaPlayer==null){
					mediaPlayer = new MediaPlayer();
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				mediaPlayer.reset();
				play(0);
				playSts=1;
			}
		});
		
		media_play = (ImageButton) findViewById(R.id.media_play);
		media_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				video_surfaceview.setVisibility(View.GONE);
				canmare_SurfaceView.setVisibility(View.VISIBLE);
				CameraActivity.this.resetCamera();
				
				if(mediaPlayer == null ) return;
				
				if(playSts == 2){
					mediaPlayer.start();
					System.out.println("!!!!!play22");
					media_play.setImageResource(R.drawable.cam_opt_btn_pause);
					playSts = 1;
					startProcess();
					postViewPlayEvent();
				}else if(playSts ==0){
					canmare_pic.setImageBitmap(null);
					canmare_pic.setBackgroundColor(getResources().getColor(R.color.none_color));
					//canmare_pic.setVisibility(View.GONE);
					media_play.setImageResource(R.drawable.cam_opt_btn_pause);
					play(0);
					playSts = 1;
				}else if(playSts==1){
					mediaPlayer.pause();
					media_play.setImageResource(R.drawable.cam_opt_btn_play);
					playSts = 2;
				}
				canmare_play.setVisibility(View.GONE);
				
				
			}
		});
		//overlay pic
		canmare_pic = (ImageView) findViewById(R.id.canmare_pic);
		canmare_pic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mediaPlayer!=null){
					pause();
					//canmare_play.setVisibility(View.VISIBLE);
					//media_play.setImageResource(R.drawable.tuxiang_caozuo_bofang);
				}
				
			}
		});
		
		
		canmare_SurfaceView = (SurfaceView) findViewById(R.id.canmare_surfaceview);

		canmare_play = (ImageButton) findViewById(R.id.canmare_play);
		canmare_play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("!!!!!play");
				if(playSts == 2){
					mediaPlayer.start();
					System.out.println("!!!!!play22");
					postViewPlayEvent();
				}else if(playSts ==0){
					canmare_pic.setImageBitmap(null);
					canmare_pic.setBackgroundColor(getResources().getColor(R.color.none_color));
					//canmare_pic.setVisibility(View.GONE);
					
					play(0);
				}
				v.setVisibility(View.GONE);
				media_play.setImageResource(R.drawable.cam_opt_btn_pause);
				playSts = 1;
				
			}
		});

		canmare_SurfaceView.getHolder().addCallback(callback);
		
		if(dataList.size() > 0) {
			currentPlayFilePath = dataList.get(0).get("itemText")+"";
		}
		else
		{
			currentPlayFilePath = "";
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cam_layout);
		this.initEvent();
		this.initView();
		
	}
	public void FinishCameraEvent(
			FinishCameraEvent event) {
		
		
		finish();
		//overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
	}
	private Bitmap getVideoThumbnail(String videoPath, int width, int height,  
            int kind) {  
        Bitmap bitmap = null;  
        try{
        	bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
            System.out.println("w"+bitmap.getWidth());  
            System.out.println("h"+bitmap.getHeight());  
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }catch(Exception e){
        	e.printStackTrace();
        }
          
        return bitmap;  
    }
	private void loadFiles(){
		dataList.clear();
		List<String> fileList = FileUtil.GetFiles(Environment.getExternalStorageDirectory().getPath() + "/CamVideo", "mp4");
		for(int i =0;i<fileList.size();i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("itemText", fileList.get(i));
			List<LockFileModel> lfList = LockFileDao.findLockFileByUrl(this, fileList.get(i));
			
			Bitmap icon = this.getVideoThumbnail(map.get("itemText")+"", 160, 90, MediaStore.Images.Thumbnails.MICRO_KIND);
			if(icon!=null){
				if(lfList.size()==0){
					map.put("isLock", "no");
				}else{
					map.put("isLock", "yes");
				}
				map.put("itemImage", icon);
				dataList.add(map);
			}
			
		}
		Collections.sort(dataList, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> map1,
					Map<String, Object> map2) {
				String v1 = (map1.get("itemText")+"").replace("secard/", "");
				String v2 = (map2.get("itemText")+"").replace("secard/", "");
				return v1.compareTo(v2);
			}
		});
	}

	private Callback callback = new Callback() {
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				currentPosition = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (currentPosition > 0) {
				play(currentPosition);
				currentPosition = 0;
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

	};




	protected void stop() {
		
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			canmare_play.setEnabled(true);
		}
		playSts = 0;
		
		this.resetCamera();
	}
	private void startProcess(){
		new Thread() {

			@Override
			public void run() {
				try {
					while (playSts==1) {
						int current = mediaPlayer
								.getCurrentPosition();
						progressBar1.setProgress(current);
						Message msg = new Message();
						msg.what = 1;
						msg.arg1 = current;
						handler.sendMessage(msg);
						sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	protected void play(final int msec) {
		//String path = "/sdcard/bb.mp4";
		File file = new File(currentPlayFilePath);
		if (!file.exists()) {
			return;
		}
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(file.getAbsolutePath());
			mediaPlayer.setDisplay(canmare_SurfaceView.getHolder());
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
					mediaPlayer.seekTo(msec);
					progressBar1.setMax(mediaPlayer.getDuration());
					String sec = sec2Time((mediaPlayer.getDuration()/1000));
					media_total.setText("/"+sec);
					startProcess();
					canmare_play.setEnabled(false);
					postViewPlayEvent();
				}
			});
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					System.out.println("finished !!!!!!!~~~~");
					playSts=0; 
					currentIndex = currentIndex+1;
					if(currentIndex>=dataList.size()-1){
						currentIndex = dataList.size()-1;
						return;
					}
					currentPlayFilePath = dataList.get(currentIndex).get("itemText")+"";
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mediaPlayer.reset();
					play(0);
					playSts=1;
				}
			});

			mediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					play(0);
					return false;
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private void postViewPlayEvent() {
		EventBus.getDefault().post(new PlayVideoEvent());
	}
	
	protected void replay() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
			return;
		}
		play(0);
	}

	protected void pause() {
		if(playSts==1){
			mediaPlayer.pause();
			playSts = 2;
			media_play.setImageResource(R.drawable.cam_opt_btn_play);
		}else if(playSts==2){
			mediaPlayer.start();
			playSts = 1;
			media_play.setImageResource(R.drawable.cam_opt_btn_pause);
			startProcess();
			postViewPlayEvent();
		}
	}
	private String sec2Time(int secv){
		int hour = secv/3600;
		int min = (secv/60)%60;
		int sec = (secv%3600)%60;
		System.out.println(hour+":"+min+":"+sec);
		String hourStr = hour<10?"0"+hour:hour+"";
		String minStr = min<10?"0"+min:min+"";
		String secStr = sec<10?"0"+sec:sec+"";
		if(hour==0){
			
			return minStr+":"+secStr;
		}else{
			return hourStr+":"+minStr+":"+secStr;
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		if(msg.what==1){
			String sec = sec2Time((msg.arg1/1000));
			media_current.setText(sec);
		}else if(msg.what==2){
			CamVideoGridAdapter vga = new CamVideoGridAdapter(dataList, this);
			history_list.setAdapter(vga);
			loadingView.setVisibility(View.GONE);
		}
		return false;
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
}
