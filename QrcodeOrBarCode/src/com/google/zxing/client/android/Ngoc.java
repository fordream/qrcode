package com.google.zxing.client.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Ngoc extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this) ;
		Bundle b = this.getIntent().getExtras() ;
		tv.setText(b.getString("key")) ;
		setContentView(tv) ;
	}
}
