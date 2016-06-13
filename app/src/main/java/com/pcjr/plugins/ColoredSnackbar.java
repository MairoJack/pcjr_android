package com.pcjr.plugins;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.pcjr.R;

/**
 * Created by Mario on 2016/5/26.
 */
public class ColoredSnackbar {

    private static final int red = 0xfff44336;
    private static final int green = 0xff4caf50;
    private static final int blue = 0xff2195f3;
    private static final int orange = 0xffffc107;

    private static View getSnackBarLayout(TSnackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static TSnackbar colorSnackBar(TSnackbar snackbar, int colorId) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
            TextView text = (TextView) snackBarView.findViewById(R.id.snackbar_text);
            text.setTextColor(0xffffffff);
        }

        return snackbar;
    }

    public static TSnackbar info(TSnackbar snackbar) {
        return colorSnackBar(snackbar, blue);
    }

    public static TSnackbar warning(TSnackbar snackbar) {
        return colorSnackBar(snackbar, orange);
    }

    public static TSnackbar alert(TSnackbar snackbar) {
        return colorSnackBar(snackbar, red);
    }

    public static TSnackbar confirm(TSnackbar snackbar) {
        return colorSnackBar(snackbar, green);
    }
}
