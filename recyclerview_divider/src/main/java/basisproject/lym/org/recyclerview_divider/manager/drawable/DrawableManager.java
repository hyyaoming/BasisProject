package basisproject.lym.org.recyclerview_divider.manager.drawable;

import android.graphics.drawable.Drawable;

/**
 * Used to specify a custom logic to use different drawables as divider.
 * * <br>
 * * You can add a custom [DrawableManager] in your [RecyclerViewDivider.Builder] using
 * * the [RecyclerViewDivider.Builder.drawableManager] method.
 *
 * @author ym.li
 * @since 2019年3月31日10:49:47
 */
public interface DrawableManager {
    /**
     * Defines a custom Drawable for each group of divider.
     *
     * @param groupCount   number of groups in a list (equal to the list size when the span count is 1).
     * @param groupIndex   position of the group (equal to the item position when the span count is 1).
     * @param itemPosition position is in adapter position.
     * @return [Drawable] resource for the divider int the current position.
     */
    Drawable itemDrawable(int groupCount, int groupIndex, int itemPosition);
}
