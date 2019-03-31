package basisproject.lym.org.recyclerview_divider.manager.visibility;

/**
 * Default implementation of [VisibilityManager] that will show all dividers.
 *
 * @author ym.li
 * @since 2019年3月31日10:57:51
 */
public class DefaultVisibilityManager implements VisibilityManager {
    public long itemVisibility(int groupCount, int groupIndex, int itemPosition) {
        return SHOW_ALL;
    }
}

