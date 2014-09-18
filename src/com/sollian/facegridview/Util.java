package com.sollian.facegridview;

import android.content.Context;
import android.os.Vibrator;

/**
 * 工具类
 * 
 * @author sollian
 * 
 */
public class Util {
	/**
	 * 将dip转换为pixel
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 震动
	 * 
	 * @param context
	 * @param duration
	 */
	public static void vibrate(Context context, long duration) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, duration };
		vibrator.vibrate(pattern, -1);
	}
}
