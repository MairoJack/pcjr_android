package com.pcjr.plugins;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.pcjr.R;

/**
 * Created by Mario on 2016/5/24.
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context);
        this.setContentView(R.layout.loading_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;

    }

    public LoadingDialog(Context context,int theme) {
        super(context);
    }
}
