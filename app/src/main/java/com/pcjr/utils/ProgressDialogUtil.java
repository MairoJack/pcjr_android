package com.pcjr.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

public class ProgressDialogUtil {
	private static ProgressDialog mProgressDialog = null;

	public static ProgressDialog SpinnerProgressDialog(Context context,
			boolean dismissIfShowing) {
		if (dismissIfShowing && mProgressDialog != null
				&& mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		mProgressDialog = new SpinnerProgressDialog(context);
		return mProgressDialog;
	}

	public static ProgressDialog showSpinnerProgressDialog(Context context) {
		return showSpinnerProgressDialog(context, null);
	}

	public static ProgressDialog showSpinnerProgressDialog(Context context,
			CharSequence message) {
		return showSpinnerProgressDialog(context, message, false);
	}

	public static ProgressDialog showSpinnerProgressDialog(Context context,
			CharSequence message, boolean indeterminate) {
		return showSpinnerProgressDialog(context, message, indeterminate,
				true, null);
	}

	public static ProgressDialog showSpinnerProgressDialog(Context context,
			CharSequence message, boolean indeterminate, boolean cancelable) {
		return showSpinnerProgressDialog(context, message, indeterminate,
				cancelable, null);
	}

	public static ProgressDialog showSpinnerProgressDialog(Context context,
			CharSequence message, boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		SpinnerProgressDialog dialog = new SpinnerProgressDialog(context);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.show();
		mProgressDialog = dialog;
		return mProgressDialog;
	}
}
