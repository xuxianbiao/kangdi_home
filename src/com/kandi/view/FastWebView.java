package com.kandi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * 自定义的WebView
 * 
 * @author xxb
 * 
 */
public class FastWebView extends WebView {
	private boolean is_gone = false;

	public FastWebView(Context context) {
		super(context);
	}

	public FastWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.GONE) {
			try {
				WebView.class.getMethod("onPause").invoke(this);// stop flash
			} catch (Exception e) {
			}
			this.pauseTimers();
			this.is_gone = true;
		} else if (visibility == View.VISIBLE) {
			try {
				WebView.class.getMethod("onResume").invoke(this);// resume flash
			} catch (Exception e) {
			}
			this.resumeTimers();
			this.is_gone = false;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (this.is_gone) {
			try {
				this.destroy();
			} catch (Exception e) {
			}
		}
	}

}
