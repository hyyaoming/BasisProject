package basisproject.lym.org.bottombar;

import android.content.Context;

/**
 * Doc  UI相关操作
 *
 * @author ym.li
 * @version 2.15.0
 * @since 2019/3/4/004
 */
class UITool {
    private static final float CONVERT_VALUE = 0.5f;

    /**
     * sp转成像素
     *
     * @param context 上下文
     * @param spValue sp大小
     * @return 转换成像素后的大小
     */
    static int sp2px(Context context, int spValue) {
        if (context == null) {
            return 0;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) ((float) spValue * fontScale + CONVERT_VALUE);
    }

    /**
     * dip to px
     *
     * @param dipValue dip 值
     * @return 返回转换后的单位
     */
    static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + CONVERT_VALUE);
    }
}
