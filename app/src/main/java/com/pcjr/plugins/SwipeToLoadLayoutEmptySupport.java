package com.pcjr.plugins;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.pcjr.R;

/**
 * Created by Mario on 2016/5/27.
 */
public class SwipeToLoadLayoutEmptySupport extends SwipeToLoadLayout {
    public SwipeToLoadLayoutEmptySupport(Context context) {
        super(context);
    }

    public SwipeToLoadLayoutEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeToLoadLayoutEmptySupport(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View targetView = this.findViewById(R.id.swipe_target);

    }
}
