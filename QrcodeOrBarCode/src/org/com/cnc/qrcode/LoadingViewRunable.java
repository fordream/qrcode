package org.com.cnc.qrcode;

import android.view.View;

public class LoadingViewRunable implements Runnable {
	private View view;
	private boolean isShow;

	public LoadingViewRunable(View view, boolean isShow) {
		this.view = view;
		this.isShow = isShow;
	}

	public void run() {
		if (view != null) {
			view.setVisibility(isShow ? View.VISIBLE : View.GONE);
		}
	}
}
