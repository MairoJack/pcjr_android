package com.pcjr.utils;






import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.pcjr.R;


public class SpinnerProgressDialog extends ProgressDialog {
	private CharSequence mMessage = null;
	private TextView mMessageView;

	public SpinnerProgressDialog(Context context) {
		super(context);
	}

	public SpinnerProgressDialog(Context context, int messageId) {
		super(context);
		String str = context.getString(messageId);
		this.mMessage = str;
	}

	public SpinnerProgressDialog(Context context, CharSequence message) {
		super(context);
		if (message != null) {
			this.mMessage = message.toString();
		}
	}

	@Override
	protected final void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.progress_dialog);
		mMessageView = (TextView) findViewById(R.id.progress_msg);
		if (mMessage != null) {
			mMessageView.setText(mMessage);
		}
	}

	@Override
	public void setMessage(CharSequence message) {
		mMessage = message;
	}
}
