package competition.fragment.Anim.View;

import android.content.Context;
import android.util.DisplayMetrics;


public class ViewUtil {

    /**
     * 根据手机分辨率从 px(像素) 单位 转成 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机分辨率从 dp 单位 转成 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
