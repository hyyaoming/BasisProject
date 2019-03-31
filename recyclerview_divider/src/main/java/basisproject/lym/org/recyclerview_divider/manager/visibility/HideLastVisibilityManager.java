package basisproject.lym.org.recyclerview_divider.manager.visibility;

/**
 * Default implementation of [VisibilityManager] that will show all dividers
 * excluding the one after the last group.
 *
 * @author ym.li
 * @since 2019年3月31日10:58:19
 */
public class HideLastVisibilityManager implements VisibilityManager {
    public long itemVisibility(int groupCount, int groupIndex, int itemPosition) {
        return groupIndex == groupCount - 1 ? SHOW_ITEMS_ONLY : SHOW_ALL;
    }
}

