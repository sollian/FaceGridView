package com.sollian.facepreview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;

/**
 * 工具类
 *
 * @author sollian
 */
public class Util {
    /**
     * 将dip转换为pixel
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 震动
     */
    public static void vibrate(Context context, long duration) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, duration};
        vibrator.vibrate(pattern, -1);
    }

    public static int getResourceId(Context context, String resName) {
        Resources res = context.getResources();
        return res.getIdentifier(resName, "drawable",
                context.getPackageName());// R类所在的包名
    }
}
