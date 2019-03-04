package basisproject.lym.org.bottombar.listener;

import android.graphics.Bitmap;

/**
 * 自定义BottomBar数据模型
 * <p>
 * author: ym.li
 * since: 2019/3/2
 */
public interface OnBottomBarEntity {
    /**
     * 获取当前BottomBarItem文本
     *
     * @return 具体文本
     */
    CharSequence getText();

    /**
     * 获取选中时的Bitmap
     *
     * @return Bitmap
     */
    Bitmap getSelectBitmap();

    /**
     * 获取未选中时的bitmap
     *
     * @return Bitmap
     */
    Bitmap getUnSelectBitmap();

    /**
     * 获取选中的icon
     *
     * @return 选中的icon资源
     */
    int getSelectIcon();

    /**
     * 为选中的icon资源
     *
     * @return 为选中的icon资源
     */
    int getUnSelectIcon();
}
