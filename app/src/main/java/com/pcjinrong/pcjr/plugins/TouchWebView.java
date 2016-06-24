package com.pcjinrong.pcjr.plugins;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Mario on 2016/6/24.
 */
public class TouchWebView extends WebView {
    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {

        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Mairo", "onTouchEvent: " + evt.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("Mairo", "onTouchEvent: " + evt.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.d("Mairo", "onTouchEvent: " + evt.getY());
                break;
        }
        return true;
    }

}