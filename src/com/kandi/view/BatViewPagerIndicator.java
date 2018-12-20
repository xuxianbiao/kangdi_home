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
public class BatViewPagerIndicator extends RelativeLayout {
	private ImageButton batleftbtn;
	private ImageButton batrightbtn;
	private View.OnClickListener btnListener;
	private ViewPagerListener vpListener;
	private ViewPager viewPager;
	private OnPageChangeListener pageChangeListener;
	private int viewpagerCurrentItem = 0;
	private int viewpagerMaxItem=0;

	public BatViewPagerIndicator(Context context) {
		this(context, null);
	}

	public BatViewPagerIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BatViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.battery_indicator_layout, this);

		btnListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (view.getId() == R.id.batleftbtn) {
					// 左边的点击设置成1
					viewpagerCurrentItem--;
					if(viewpagerCurrentItem<=0){
						viewpagerCurrentItem=0;
						view.setVisibility(INVISIBLE);
						batrightbtn.setVisibility(VISIBLE);
					}
				} else if (view.getId() == R.id.batrightbtn) {
					viewpagerCurrentItem++;
					if(viewpagerCurrentItem>=viewpagerMaxItem){
						viewpagerCurrentItem=viewpagerMaxItem;
						batleftbtn.setVisibility(VISIBLE);
						view.setVisibility(INVISIBLE);
					}
				}
				viewPager.setCurrentItem(viewpagerCurrentItem);
			}
		};

		batleftbtn = (ImageButton) findViewById(R.id.batleftbtn);
		batrightbtn = (ImageButton) findViewById(R.id.batrightbtn);
		batleftbtn.setOnClickListener(btnListener);
		batrightbtn.setOnClickListener(btnListener);

		// 先准备好viewpager监听
		pageChangeListener = new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				viewpagerCurrentItem = arg0;
				if(viewpagerCurrentItem>=viewpagerMaxItem){
					batleftbtn.setVisibility(VISIBLE);
					batrightbtn.setVisibility(INVISIBLE);
				}else if(viewpagerCurrentItem<=0){
					batleftbtn.setVisibility(INVISIBLE);
					batrightbtn.setVisibility(VISIBLE);
				}else{
					batleftbtn.setVisibility(VISIBLE);
					batrightbtn.setVisibility(VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		};
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
		ChangedViewItemNum(viewPager);
		viewPager.setOnPageChangeListener(pageChangeListener);
	}
	
	public void setVpListener(ViewPagerListener vpListener) {
		this.vpListener = vpListener;
	}
	private void ChangedViewItemNum(ViewPager viewPager){
		viewpagerMaxItem = viewPager.getAdapter().getCount()-1;
		if(this.viewpagerMaxItem<=0){
			batleftbtn.setVisibility(INVISIBLE);
			batrightbtn.setVisibility(INVISIBLE);
		}else {
			if(viewpagerMaxItem >= viewPager.getCurrentItem()){
				if(viewPager.getCurrentItem() == 0){
					batleftbtn.setVisibility(INVISIBLE);
					batrightbtn.setVisibility(VISIBLE);
				}else if(viewpagerMaxItem == viewPager.getCurrentItem()){
					batleftbtn.setVisibility(VISIBLE);
					batrightbtn.setVisibility(INVISIBLE);
				}
			}
		}
	}
}
