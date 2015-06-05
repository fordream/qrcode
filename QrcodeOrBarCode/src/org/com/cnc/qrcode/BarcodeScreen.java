package org.com.cnc.qrcode;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.client.android.CaptureActivity;

public class BarcodeScreen extends Activity {
	private Bundle b;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Intent intent = new Intent(getBaseContext(), CaptureActivity.class);
		startActivityForResult(intent, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("key");
				b = new Bundle();
				b.putString("key", contents);

				Builder builder = new Builder(this);
				builder.setMessage(contents);
				builder.show();
				Intent intent2 = new Intent();
				intent2.putExtras(b);
				setResult(RESULT_OK, intent);
				
				this.finish();
				//startActivity(new Intent(this, MenuScreen.class).putExtras(b));
			} else if (resultCode == RESULT_CANCELED) {
				this.finish();
			}
		}
	}
}