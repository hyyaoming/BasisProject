package basisproject.lym.org.recyclerview_divider.manager.inset;

import android.support.annotation.Px;

/**
 * Used to specify a custom logic to set different insets to each divider.
 * * <br>
 * * You can add a custom [DrawableManager] in your [RecyclerViewDivider.Builder] using
 * * the [RecyclerViewDivider.Builder.drawableManager] method.
 *
 * @author ym.li
 * @since 2019年3月31日10:53:20
 */

public interface InsetManager {
    /**
     * Defines a custom inset that will be applied on the top of each element for a vertical list and
     * on the left of each element for an horizontal list.
     *
     * @param groupCount   number of groups in a list (equal to the list size when the span count is 1).
     * @param groupIndex   position of the group (equal to the item position when the span count is 1).
     * @param itemPosition position is in adapter position.
     * @return inset before the item (expressed in pixels).
     */
    @Px
    int itemInsetBefore(int groupCount, int groupIndex, int itemPosition);

    /**
     * Defines a custom inset that will be applied on the bottom of each element for a vertical list and
     * on the right of each element for an horizontal list.
     *
     * @param groupCount   number of groups in a list (equal to the list size when the span count is 1).
     * @param groupIndex   position of the group (equal to the item position when the span count is 1).
     * @param itemPosition position is in adapter position.
     * @return inset after the item (expressed in pixels).
     */
    @Px
    int itemInsetAfter(int groupCount, int groupIndex, int itemPosition);
}
