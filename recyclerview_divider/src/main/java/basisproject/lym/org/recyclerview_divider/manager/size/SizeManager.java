package basisproject.lym.org.recyclerview_divider.manager.size;

import android.graphics.drawable.Drawable;
import android.support.annotation.Px;

/**
 * Used to specify a custom logic to set different sizes to the divider.
 * <br>
 * Size is referred to the height of an horizontal divider and to the width of a vertical divider.
 * <br>
 * You can add a custom [SizeManager] in your [RecyclerViewDivider.Builder] using
 * the [RecyclerViewDivider.Builder.sizeManager] method.
 *
 * @author ym.li
 * @since 2019年3月31日10:51:51
 */
public interface SizeManager {
    /**
     * Defines a custom size for each group of divider.
     *
     * @param drawable     current divider's [Drawable].
     * @param orientation  [RecyclerView.VERTICAL] or [RecyclerView.HORIZONTAL].
     * @param groupCount   number of groups in a list (equal to the list size when the span count is 1).
     * @param groupIndex   position of the group (equal to the item position when the span count is 1).
     * @param itemPosition position is in adapter position.
     * @return height for an horizontal divider, width for a vertical divider (expressed in pixels).
     */
    @Px
    int itemSize(Drawable drawable, int orientation, int groupCount, int groupIndex, int itemPosition);
}
