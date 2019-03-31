package basisproject.lym.org.recyclerview_divider.manager.tint;

/**
 * Default implementation of [SizeManager] that will use the same color to tint each item.
 * *
 * * @param tint the color used to tint each item.
 *
 * @author ym.li
 * @since 2019年3月31日10:55:25
 */
public class DefaultTintManager implements TintManager {
    private int mTint;

    public DefaultTintManager(int tint) {
        this.mTint = tint;
    }

    @Override
    public int itemTint(int groupIndex, int groupCount) {
        return mTint;
    }
}

