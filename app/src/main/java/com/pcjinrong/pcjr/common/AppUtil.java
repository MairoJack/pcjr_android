/**
 *
 */
package com.pcjinrong.pcjr.common;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class AppUtil {

	/**
	 * 获取屏幕分辨率
	 * @param context
	 * @return
	 */
	public static int[] getScreenDispaly(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;// 手机屏幕的宽度
		int height = size.y;// 手机屏幕的高度
		int result[] = { width, height };
		return result;
	}

}
