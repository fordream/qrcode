package org.com.cnc.qrcode;

import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public interface ICommonActivityView {

	public void showLoading(boolean show);

	public CheckBox getCheckBox(int res);

	public EditText getEditText(int res);

	public TextView getTextView(int res);

	public Button getButton(int res);

	public ImageButton getImageButton(int res);

	public ImageView getImageView(int res);

	public ListView getListView(int res);

	public RadioButton getRadioButton(int res);

	public Spinner getSpinner(int res);

	public LinearLayout getLinearLayout(int res);
	
	public RelativeLayout getRelativeLayout(int res);

	public void hiddenKeyboard(Context context, EditText et);

	public String getText(EditText eText);

	public String getText(TextView eText);

	public void makeText(Context context, String msg);

	public void close();

	public void config(int resource_layouy);

	public void setTiteMessagge(String title);

	public String getTitleMessagge();
}
