package org.com.cnc.qrcode;

import android.content.Intent;
import android.os.AsyncTask;

public class RedirectAsyn extends AsyncTask<String, String, String> {
	private CommonActivity activity;

	private boolean isClose = false;

	public RedirectAsyn(CommonActivity activity) {
		super();
		this.activity = activity;
	}

	protected String doInBackground(String... params) {
		int count = 0;
		while (count < 5) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (isClose) {
				return "";
			}
			count++;
		}
		return null;
	}

	protected void onPostExecute(String result) {
		if (isClose) {
			return;
		}
		activity.startActivity(new Intent(activity, QRCodeOrBarcodeScreen.class));
		activity.finish();
	}

	public void isClose() {
		isClose = true;
	}

}