package com.pcjinrong.pcjr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pcjinrong.pcjr.plugins.GestureDrawline;
import com.pcjinrong.pcjr.utils.SharedPreferenceUtil;
import com.pcjr.R;
import com.pcjinrong.pcjr.common.Constant;
import com.pcjinrong.pcjr.plugins.GestureContentView;
import com.pcjinrong.pcjr.plugins.LockIndicator;


/**
 *
 * 手势密码设置界面
 *
 */
public class GestureEditActivity extends Activity{
	/** 首次提示绘制手势密码，可以选择跳过 */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
	private TextView mTextTitle;
	private TextView mTextCancel;
	private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private String mParamSetUpcode = null;
	private String mParamPhoneNumber;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;
	private String mConfirmPassword = null;
	private int mParamIntentCode;
    private RelativeLayout back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_edit);
		setUpViews();
	}
	
	private void setUpViews() {

		mTextReset = (TextView) findViewById(R.id.text_reset);
		mTextReset.setClickable(false);
		mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (!isInputPassValidate(inputCode)) {
					mTextTip.setText("最少链接4个点, 请重新输入");
					mGestureContentView.clearDrawlineState(0L);
					return;
				}
				if (mIsFirstInput) {
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					mGestureContentView.clearDrawlineState(0L);
					mTextReset.setClickable(true);
					mTextReset.setText(getString(R.string.reset_gesture_code));
				} else {
					if (inputCode.equals(mFirstPassword)) {
                        SharedPreferenceUtil spu = new SharedPreferenceUtil(GestureEditActivity.this, Constant.FILE);
                        spu.setGesture(inputCode);
                        Toast.makeText(GestureEditActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
						mGestureContentView.clearDrawlineState(0L);
						setResult(RESULT_OK);
						GestureEditActivity.this.finish();
					} else {
						mTextTip.setText("与上一次绘制不一致，请重新绘制");
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						// 保持绘制的线，1.5秒后清除
						mGestureContentView.clearDrawlineState(1300L);
					}
				}
				mIsFirstInput = false;
			}

			@Override
			public void checkedSuccess() {
				
			}

			@Override
			public void checkedFail() {
				
			}
		});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");

		mTextReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsFirstInput = true;
				updateCodeList("");
				mTextTip.setText(getString(R.string.set_gesture_pattern));
			}
		});
	}
	

	
	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		mLockIndicator.setPath(inputCode);
	}


	
	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            setResult(RESULT_CANCELED);
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }
	
}
