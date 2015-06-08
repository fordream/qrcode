package vnp.com.qrcode.main;

import org.com.cnc.qrcode.BarcodeScreen;
import org.com.cnc.qrcode.RGBLuminanceSource;
import org.com.cnc.qrcode.ResultScreen;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import vnp.com.qrcode.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	private static final int REQUEST_0 = 0;
	private static final int REQUEST_2 = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_main);

		findViewById(R.id.btn_google_play).setOnClickListener(this);
		findViewById(R.id.btn_gallery).setOnClickListener(this);
		findViewById(R.id.btn_scan_qr_code).setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btn_scan_qr_code) {
			try {
				Intent intent = new Intent(this, BarcodeScreen.class);
				startActivityForResult(intent, REQUEST_0);
			} catch (Exception e) {
			}

		} else if (v.getId() == R.id.btn_gallery) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), REQUEST_2);
		} else if (v.getId() == R.id.btn_google_play) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://search?q=pub:Truong Vuong Van"));
			startActivity(intent);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_0 && resultCode == RESULT_OK) {
			String contents = data.getStringExtra("key");
			// sendResult(contents);

			load(false, contents);
		} else if (requestCode == REQUEST_2 && resultCode == RESULT_OK) {

			// sendResult(url);

			load(true, data);
		}

	}

	private void load(final boolean isPath, final Object data) {
		new AsyncTask<String, String, String>() {
			ProgressDialog progressDialog;

			protected void onPreExecute() {
				if (progressDialog == null) {
					progressDialog = ProgressDialog.show(MainActivity.this,
							null, null);
				}
			};

			@Override
			protected String doInBackground(String... params) {
				String result = "";
				if (!isPath) {
					if (data != null) {
						result = data.toString();
					}
				} else {
					if (data != null) {
						Uri selectedImageUri = ((Intent) data).getData();
						result = decode(getPath(selectedImageUri));
					}
				}
				return result;
			}

			protected void onPostExecute(String result) {
				if (progressDialog != null)
					progressDialog.dismiss();
				sendResult(result);
			};
		}.execute("");
	}

	private String decode(String path) {
		try {
			RGBLuminanceSource source = new RGBLuminanceSource(path);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			MultiFormatReader mMultiFormatReader = new MultiFormatReader();
			Result result = mMultiFormatReader.decodeWithState(bitmap);
			return result.getText();
		} catch (Exception e) {
			return null;
		}
	}

	private void sendResult(String data) {
		Intent intent = new Intent(this, ResultScreen.class);
		intent.putExtra("ARG0", data);
		startActivity(intent);

	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		String result = "";
		if (cursor.moveToFirst()) {
			result = cursor.getString(column_index);
			cursor.close();
		}

		return result;
	}
}