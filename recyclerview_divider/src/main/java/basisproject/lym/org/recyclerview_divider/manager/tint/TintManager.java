package basisproject.lym.org.recyclerview_divider.manager.tint;

import android.support.annotation.ColorInt;

/**
 * Used to specify a custom logic to use different tint colors to tint divider's drawables.
 * <br>
 * You can add a custom [TintManager] in your [RecyclerViewDivider.Builder] using
 * the [RecyclerViewDivider.Builder.tintManager] method.
 *
 * @author ym.li
 * @since 2019年3月31日10:54:57
 */
public interface TintManager {
    /**
     * Defines a custom tint color for each group of divider.
     *
     * @param groupCount number of groups in a list (equal to the list size when the span count is 1).
     * @param groupIndex position of the group (equal to the item position when the span count is 1).
     * @return tint color for the divider's drawable in the current position.
     */
    @ColorInt
    int itemTint(int groupIndex, int groupCount);
}

