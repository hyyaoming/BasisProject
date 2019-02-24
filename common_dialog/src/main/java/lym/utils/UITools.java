package lym.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;

/**
 * UI相关操作
 * <p>
 * author: ym.li
 * since: 2019/2/24
 */
public class UITools {
    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 返回状态栏高度
     */
    public static int getStatusHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (isFlymeOs4x()) {
            return 2 * statusBarHeight;
        }
        return statusBarHeight;
    }

    /**
     * 构造一个GradientDrawable传入颜色和圆角度数
     *
     * @param bgColor      颜色值
     * @param cornerRadius 圆角度数
     * @return GradientDrawable
     */
    public static Drawable cornerDrawable(final int bgColor, float cornerRadius) {
        final GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(cornerRadius);
        bg.setColor(bgColor);
        return bg;
    }

    private static Drawable cornerDrawable(final int bgColor, float[] cornerRadius) {
        final GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadii(cornerRadius);
        bg.setColor(bgColor);
        return bg;
    }

    /**
     * 设置View点击时的颜色值
     *
     * @param radius      圆角
     * @param normalColor 默认颜色
     * @param pressColor  点击颜色
     * @return StateListDrawable
     */
    public static StateListDrawable btnSelector(float radius, int normalColor, int pressColor) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = cornerDrawable(normalColor, radius);
        Drawable pressed = cornerDrawable(pressColor, radius);
        bg.addState(new int[]{-android.R.attr.state_pressed}, normal);
        bg.addState(new int[]{android.R.attr.state_pressed}, pressed);
        return bg;
    }

    private static boolean isFlymeOs4x() {
        String sysVersion = android.os.Build.VERSION.RELEASE;
        if ("4.4.4".equals(sysVersion)) {
            String sysIncrement = android.os.Build.VERSION.INCREMENTAL;
            String displayId = android.os.Build.DISPLAY;
            if (!TextUtils.isEmpty(sysIncrement)) {
                return sysIncrement.contains("Flyme_OS_4");
            } else {
                return displayId.contains("Flyme OS 4");
            }
        }
        return false;
    }

}
