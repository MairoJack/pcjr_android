package com.pcjr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.plugins.GestureContentView;
import com.pcjr.plugins.GestureDrawline;
import com.pcjr.utils.SharedPreferenceUtil;


/**
 *
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends Activity {

	private RelativeLayout close;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView forget,login;
	private long mExitTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);
		setUpViews();
	}
	

	
	private void setUpViews() {
		mTextTip = (TextView) findViewById(R.id.text_tip);
		forget = (TextView) findViewById(R.id.forget);
		close = (RelativeLayout) findViewById(R.id.close);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);

        SharedPreferenceUtil spu = new SharedPreferenceUtil(GestureVerifyActivity.this, Constant.FILE);
        String gesture = spu.getGesture();

        // 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, true, gesture,
				new GestureDrawline.GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {

					}

					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						//Toast.makeText(GestureVerifyActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("abc","fuck");
                        setResult(RESULT_OK,intent);
                        Constant.isGestureLogin = true;
                        GestureVerifyActivity.this.finish();

					}

					@Override
					public void checkedFail() {
						mGestureContentView.clearDrawlineState(1300L);
						mTextTip.setVisibility(View.VISIBLE);
						mTextTip.setText("密码错误");
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);


		forget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
                startActivity(new Intent(GestureVerifyActivity.this,LoginActivity.class));
			}
		});

		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
