package org.com.cnc.qrcode;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.ict.library.common.CommonResize;
import vnp.com.qrcode.R;

public class QRCodeOrBarcodeScreen extends Activity implements OnClickListener {
	private static final int REQUEST_0 = 0;
	private static final int REQUEST_1 = 1;
	private static final int REQUEST_2 = 2;
	private TextView tVValue;
	private static final int SIZE_BUTTON = 640 / 3;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		View view = findViewById(R.id.relativeLayout1);
		CommonResize._20130408_resizeLandW960H640(view, 960, 640);

		findViewById(R.id.Button02).setOnClickListener(this);
		CommonResize._20130408_resizeLandW960H640(findViewById(R.id.Button02),
				SIZE_BUTTON, SIZE_BUTTON);
		CommonResize._20130408_sendViewToPositionLandW960H640(
				findViewById(R.id.Button02), 480 - SIZE_BUTTON / 2
						- SIZE_BUTTON - 5, 320 - SIZE_BUTTON / 2);

		findViewById(R.id.Button01).setOnClickListener(this);

		findViewById(R.id.button1).setOnClickListener(this);
		CommonResize._20130408_resizeLandW960H640(findViewById(R.id.button1),
				SIZE_BUTTON, SIZE_BUTTON);

		CommonResize._20130408_sendViewToPositionLandW960H640(
				findViewById(R.id.button1), 480 - SIZE_BUTTON / 2 + SIZE_BUTTON
						+ 5, 320 - SIZE_BUTTON / 2);
		tVValue = (TextView) findViewById(R.id.textView1);

		// button more app
		findViewById(R.id.button2).setOnClickListener(this);
		CommonResize._20130408_resizeLandW960H640(findViewById(R.id.button2),
				SIZE_BUTTON, SIZE_BUTTON);
		CommonResize._20130408_sendViewToPositionLandW960H640(
				findViewById(R.id.button2), 480 - SIZE_BUTTON / 2,
				320 - SIZE_BUTTON / 2);

		tVValue.setOnClickListener(this);
		String adUnitId = "a14edad3d74598a";
		AdView adView = new AdView(this, AdSize.BANNER, adUnitId);
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout2);
		layout.addView(adView);
		AdRequest request = new AdRequest();
		request.setTesting(true);
		adView.loadAd(request);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.Button02) {
			try {
				Intent intent = new Intent(this, BarcodeScreen.class);
				startActivityForResult(intent, REQUEST_0);
			} catch (Exception e) {
				showDialog();
			}

		} else if (v.getId() == R.id.button1) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), REQUEST_2);
		}

		if (v == tVValue) {
			Builder builder = new Builder(this);
			builder.setMessage("ss");
			builder.show();
			if (!tVValue.getText().toString().endsWith("")) {

			}
		}

		if (v.getId() == R.id.button2) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://search?q=pub:Truong Vuong Van"));
			startActivity(intent);
		}
	}

	protected void onRestart() {
		super.onRestart();
		tVValue.setVisibility(View.GONE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_0 && resultCode == RESULT_OK) {
			String contents = data.getStringExtra("key");
			tVValue.setText(contents);
			sendResult(contents);

		} else if (requestCode == REQUEST_1 && resultCode == RESULT_OK) {
			String contents = data.getStringExtra("key");
			tVValue.setText(contents);
		} else if (requestCode == REQUEST_2 && resultCode == RESULT_OK) {
			Uri selectedImageUri = data.getData();
			String url = decode(getPath(selectedImageUri));
			tVValue.setText(url);
			sendResult(url);
		}

	}

	private void sendResult(String data) {
		Intent intent = new Intent(this, ResultScreen.class);
		intent.putExtra("ARG0", data);
		startActivity(intent);

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

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.menu_setting, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.item1) {
			// Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			// sendIntent.putExtra("Content of Barcode of QRcode",
			// tVValue.getText().toString());
			// sendIntent.setType("vnd.android-dir/mms-sms");
			// startActivity(sendIntent);
			// String urlString = "sms:" + "";
			// Uri uri = Uri.parse(urlString);
			// startActivity(new Intent(Intent.ACTION_VIEW, uri));

			// SmsManager sm = SmsManager.getDefault();
			// // here is where the destination of the text should go
			// String number = "01674537885";
			// sm.sendTextMessage(number, null, "Test SMS Message", null, null);
			// String number = "";
			// startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",
			// number, tVValue.getText().toString())));

			// Intent intent = new Intent(this,SMSScreen.class);
			// intent.putExtra("key", tVValue.getText().toString());
			// startActivity(intent);

			try {
				String message = "Content of Barcode or QRcode\n"
						+ tVValue.getText().toString();
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", message);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			} catch (Exception e) {
				showDialog();
			}
		} else if (item.getItemId() == R.id.item2) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");

			String aEmailList[] = { "" };
			String aEmailCCList[] = {};
			String aEmailBCCList[] = {};
			emailIntent
					.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
			emailIntent.putExtra(android.content.Intent.EXTRA_BCC,
					aEmailBCCList);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Content of Barcode or QRcode");
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, tVValue
					.getText().toString());
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}
		return true;
	}

	private void showDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("Message");
		builder.setMessage("Device don't sport");
		builder.setPositiveButton("Close", null);
		builder.show();
	}
}
