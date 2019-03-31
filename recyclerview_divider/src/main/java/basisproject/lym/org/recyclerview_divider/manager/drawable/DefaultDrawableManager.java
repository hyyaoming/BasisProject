package basisproject.lym.org.recyclerview_divider.manager.drawable;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * Default implementation of [DrawableManager] that will draw the same [Drawable] for each item.
 * *
 * * @param drawable the [Drawable] that will be drawn for each item.
 *
 * @author ym.li
 * @since 2019年3月31日10:50:55
 */

public class DefaultDrawableManager implements DrawableManager {
    private Drawable mDrawable;

    /**
     * Constructor that will use a drawable
     *
     * @param drawable use a drawable canvas divider
     */
    public DefaultDrawableManager(Drawable drawable) {
        this.mDrawable = drawable;
    }

    /**
     * Constructor that will use a [ColorDrawable] that wraps the color #CFCFCF.
     */
    public DefaultDrawableManager() {
        this(Color.TRANSPARENT);
    }

    /**
     * Constructor that will use a [ColorDrawable] that wraps [color].
     *
     * @param color the color that will be wrapped in a [ColorDrawable].
     */
    private DefaultDrawableManager(@ColorInt int color) {
        this.mDrawable = new ColorDrawable(color);
    }

    @Override
    public Drawable itemDrawable(int groupCount, int groupIndex, int itemPosition) {
        return mDrawable;
    }
}
