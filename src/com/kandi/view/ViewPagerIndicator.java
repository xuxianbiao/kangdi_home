package com.kandi.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.kandi.home.R;
import com.kandi.view.MainActivity.ViewPagerListener;

/**
 * 这个是首页Viewpager的指示器，很简单的指示器
 * 
 */
public class ViewPagerIndicator extends RelativeLayout {
	private ImageButton bottom_float_home_btn;
	private ImageButton bottom_float_setting_btn;
	private View.OnClickListener btnListener;
	private ViewPagerListener vpListener;
	private ViewPager viewPager;
	private OnPageChangeListener pageChangeListener;
	private int viewpagerCurrentItem = 0;

	public ViewPagerIndicator(Context context) {
		this(context, null);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void setPage(int page){
		if(page == 0){
			// 左边的点击设置成1
			viewPager.setCurrentItem(0);
			setBackgroundResource(R.drawable.home_bottom_tap_home_light);
		}else{
			viewPager.setCurrentItem(1);
			setBackgroundResource(R.drawable.home_bottom_tap_carset_light);
		}

	}
	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.bottom_float_layout, this);
		setBackgroundResource(R.drawable.home_bottom_tap_home_light);

		btnListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (view.getId() == R.id.bottom_float_home_btn) {
					// 左边的点击设置成1
					setPage(0);
				} else if (view.getId() == R.id.bottom_float_setting_btn) {
					setPage(1);
				}
			}
		};

		bottom_float_home_btn = (ImageButton) findViewById(R.id.bottom_float_home_btn);
		bottom_float_setting_btn = (ImageButton) findViewById(R.id.bottom_float_setting_btn);
		bottom_float_home_btn.setOnClickListener(btnListener);
		bottom_float_setting_btn.setOnClickListener(btnListener);

		// 先准备好viewpager监听
		pageChangeListener = new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					setBackgroundResource(R.drawable.home_bottom_tap_home_light);
				} else {
					setBackgroundResource(R.drawable.home_bottom_tap_carset_light);
				}
				
				viewpagerCurrentItem = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
					if (null != vpListener && viewpagerCurrentItem == 1) {
						vpListener.onPageSelected(1);
					}
				}
			}
		};
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
		viewPager.setOnPageChangeListener(pageChangeListener);
	}
	
	public void setVpListener(ViewPagerListener vpListener) {
		this.vpListener = vpListener;
	}
}
