package org.com.cnc.qrcode;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class SplashScreen extends CommonActivity {
	private RedirectAsyn redirectAsyn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash1);

		View view = findViewById(R.id.imageView1);
		if (view != null) {
			AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1f);
			alphaAnimation.setDuration(6 * 1000);
			view.startAnimation(alphaAnimation);
		}


		redirectAsyn = new RedirectAsyn(this);
		redirectAsyn.execute("");
	}

	protected void onDestroy() {
		redirectAsyn.isClose();
		super.onDestroy();
	}
}