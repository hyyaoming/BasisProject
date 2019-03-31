package basisproject.lym.org.recyclerview_divider.manager.inset;


import android.support.annotation.Px;

/**
 * Default implementation of [InsetManager] that will use the same inset for each item.
 * *
 * * @param insetBefore the inset that will be applied before each item.
 * * @param insetAfter the inset that will be applied after each item.
 *
 * @author ym.li
 * @since 2019年3月31日10:54:05
 */
public class DefaultInsetManager implements InsetManager {
    private int mInsetBefore;
    private int mInsetAfter;

    /**
     * Constructor  that assigns a insetBefore inset equal to insetAfter.
     *
     * @param insetBefore insetBefore the inset that will be applied before each item.
     * @param insetAfter  insetAfter the inset that will be applied after each item.
     */
    public DefaultInsetManager(@Px int insetBefore, @Px int insetAfter) {
        this.mInsetBefore = insetBefore;
        this.mInsetAfter = insetAfter;
    }

    /**
     * Constructor that assigns a default inset equal to 0.
     */
    public DefaultInsetManager() {
        this(0, 0);
    }

    @Override
    public int itemInsetBefore(int groupCount, int groupIndex, int itemPosition) {
        return mInsetBefore;
    }

    @Override
    public int itemInsetAfter(int groupCount, int groupIndex, int itemPosition) {
        return mInsetAfter;
    }
}
