package vnp.com.qrcode.main;

import org.com.cnc.qrcode.BarcodeScreen;
import org.com.cnc.qrcode.RGBLuminanceSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.vnp.qrcode.R;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private static final int REQUEST_0 = 0;
	private static final int REQUEST_2 = 2;

	private View new_main_result;
	private TextView new_main_result_txt;
	private ListView listview;
	private TextView txtContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_main);

		new_main_result = findViewById(R.id.new_main_result);
		new_main_result.setOnClickListener(null);
		new_main_result.setVisibility(View.GONE);
		new_main_result_txt = (TextView) findViewById(R.id.new_main_result_txt);

		listview = (ListView) findViewById(R.id.listView1);
		txtContent = (TextView) findViewById(R.id.txtContent);
		txtContent.setText("");
		findViewById(R.id.btn_google_play).setOnClickListener(this);
		findViewById(R.id.btn_gallery).setOnClickListener(this);
		findViewById(R.id.btn_scan_qr_code).setOnClickListener(this);

		findViewById(R.id.btn_result_email).setOnClickListener(this);
		findViewById(R.id.btn_result_close).setOnClickListener(this);
		findViewById(R.id.btn_result_sms).setOnClickListener(this);
		findViewById(R.id.btn_result_web).setOnClickListener(this);
		saveData(null);
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
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_2);
		} else if (v.getId() == R.id.btn_google_play) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://search?q=pub:Vnp Game"));
			startActivity(intent);
		} else if (v.getId() == R.id.btn_result_email) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");

			String aEmailList[] = { "" };
			String aEmailCCList[] = {};
			String aEmailBCCList[] = {};
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
			emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Content of Barcode or QRcode");
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, new_main_result_txt.getText().toString());
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} else if (v.getId() == R.id.btn_result_sms) {
			try {
				String message = "Content of Barcode or QRcode\n" + new_main_result_txt.getText().toString();
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", message);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			} catch (Exception e) {
				showDialog(getString(R.string.no_support_sms));
			}
		} else if (v.getId() == R.id.btn_result_web) {
			try {
				String url = new_main_result_txt.getText().toString();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			} catch (Exception ex) {
				showDialog(getString(R.string.no_support_web));
			}
		} else if (v.getId() == R.id.btn_result_close) {

			showResult(false);
		}
	}

	private void showDialog(String message) {
		Builder builder = new Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, null);
		builder.show();
	}

	@Override
	public void onBackPressed() {
		if (new_main_result.getVisibility() == View.VISIBLE) {
			showResult(false);
			return;
		}
		super.onBackPressed();
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
					progressDialog = ProgressDialog.show(MainActivity.this, null, null);
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
		// Intent intent = new Intent(this, ResultScreen.class);
		// intent.putExtra("ARG0", data);
		// startActivity(intent);

		txtContent.setText(data);

		// new_main_result_txt.setText(data);

		// showResult(true);

		saveData(data);
	}

	private void saveData(String data) {
		SharedPreferences preferences = getPreferences(0);
		String currentData = preferences.getString("data", "[]");
		JSONArray ja = new JSONArray();
		try {
			ja = new JSONArray(currentData);
		} catch (JSONException e) {
		}

		if (data != null) {
			ja.put(data);
		}

		Editor editor = preferences.edit();
		editor.putString("data", ja.toString());
		editor.commit();
		final int count = ja.length();
		final JSONArray array = ja;

		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				try {
					String url = parent.getItemAtPosition(position).toString();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				} catch (Exception ex) {
					showDialog(getString(R.string.no_support_web));
				}
			}

		});
		listview.setAdapter(new BaseAdapter() {

			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = new TextView(parent.getContext());
					((TextView) convertView).setTextColor(Color.WHITE);
					((TextView) convertView).setTextSize(parent.getResources().getDimension(R.dimen.dimen_10dp));

					int padding = (int) parent.getResources().getDimension(R.dimen.dimen_10dp);
					((TextView) convertView).setPadding(padding, padding, padding, padding);
				}

				((TextView) convertView).setText(getItem(position).toString());
				return convertView;
			}

			public long getItemId(int position) {
				return 0;
			}

			public Object getItem(int position) {
				try {
					return array.get(position).toString();
				} catch (JSONException e) {
					return "";
				}
			}

			public int getCount() {
				return count;
			}
		});
	}

	private void showResult(boolean isShow) {

		int width = 0;
		int height = 0;

		new_main_result.setVisibility(View.VISIBLE);
		Animation animation = new ScaleAnimation(0, 1, 0, 1, width / 2, height / 2);
		animation.setDuration(500);
		if (isShow) {
			new_main_result.startAnimation(animation);
		} else {
			animation = new ScaleAnimation(1, 0, 1, 0, width / 2, height / 2);
			animation.setDuration(500);
			animation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {

				}

				public void onAnimationRepeat(Animation animation) {

				}

				public void onAnimationEnd(Animation animation) {
					new_main_result.setVisibility(View.GONE);
				}
			});
			new_main_result.startAnimation(animation);
		}
	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		String result = "";
		if (cursor.moveToFirst()) {
			result = cursor.getString(column_index);
			cursor.close();
		}

		return result;
	}
}