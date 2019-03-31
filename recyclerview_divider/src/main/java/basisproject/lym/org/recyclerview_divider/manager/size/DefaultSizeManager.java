package basisproject.lym.org.recyclerview_divider.manager.size;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Px;

/**
 * Default implementation of [SizeManager] that will calculate the size of each item using the
 * * drawable's dimensions.
 * * If the dimensions can't be calculated, a default size will be used.
 *
 * @author ym.li
 * @since 2019年3月31日10:52:42
 */
public class DefaultSizeManager implements SizeManager {
    private static final float DP_SIZE = 0.5F;
    private int mDefaultSize;

    /**
     * Constructor that assigns a default size equal to 1dp.
     *
     * @param context the [Context] used to access the resources.
     */
    public DefaultSizeManager(Context context) {
        int dps = 1;
        Resources var10000 = context.getResources();
        float scale = var10000.getDisplayMetrics().density;
        this.mDefaultSize = (int) ((float) dps * scale + DP_SIZE);
    }

    /**
     * Constructor that assigns a default size equal to [defaultSize].
     *
     * @param defaultSize the size that will be set for each item.
     */
    public DefaultSizeManager(@Px int defaultSize) {
        this.mDefaultSize = defaultSize;
    }

    @Override
    public int itemSize(Drawable drawable, int orientation, int groupCount, int groupIndex, int itemPosition) {
        int size = orientation == 1 ? drawable.getIntrinsicHeight() : drawable.getIntrinsicWidth();
        return size == -1 ? this.mDefaultSize : size;
    }

}
