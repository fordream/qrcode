package org.com.cnc.qrcode;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ict.library.common.CommonResize;

public class ResultScreen extends CommonActivity implements OnClickListener {
	private TextView textView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		CommonResize._20130408_resizeLandW960H640(
				findViewById(R.id.linearLayout1), 960, 640);

		int scale = 2;
		CommonResize._20130408_resizeLandW960H640(
				findViewById(R.id.idResult_MENU), 300, 300);

		CommonResize._20130408_resizeLandW960H640(
				findViewById(R.id.linearLayout2), 310 * scale, 310 * scale);
		CommonResize._20130408_resizeLandW960H640(
				findViewById(R.id.scrollView1id), 290 * scale, 280 * scale);

		textView = (TextView) findViewById(R.id.textView1);
		textView.setText(getIntent().getStringExtra("ARG0"));
		findViewById(R.id.Button02).setOnClickListener(this);
		findViewById(R.id.Button01).setOnClickListener(this);
		findViewById(R.id.button1).setOnClickListener(this);

		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.Button03).setOnClickListener(this);
	}

	public void onClick(View arg0) {
		// email
		if (arg0.getId() == R.id.Button02) {
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
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, textView
					.getText().toString());
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} else if (arg0.getId() == R.id.button1) {
			try {
				String message = "Content of Barcode or QRcode\n"
						+ textView.getText().toString();
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", message);
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			} catch (Exception e) {
				showDialog();
			}
		} else if (arg0.getId() == R.id.Button01) {
			try {
				String url = textView.getText().toString();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			} catch (Exception ex) {

			}
		} else if (arg0.getId() == R.id.Button03) {
			// send list email
			// Intent intent = new Intent(this, SendEmailScreen.class);
			// intent.putExtra("ARG0", textView.getText().toString());
			// startActivity(intent);

		} else if (arg0.getId() == R.id.button2) {
			// send list sms
			// Intent intent = new Intent(this, SendSMSScreen.class);
			// intent.putExtra("ARG0", textView.getText().toString());
			// startActivity(intent);
		}
	}

	private void showDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("Message");
		builder.setMessage("Device don't sport");
		builder.setPositiveButton("Close", null);
		builder.show();
	}
}
