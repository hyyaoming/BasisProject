package basisproject.lym.org.recyclerview_divider.manager.visibility;

import android.support.annotation.LongDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to specify a custom logic to use different visibility for each divider.
 * <br>
 * You can add a custom [VisibilityManager] in your [RecyclerViewDivider.Builder] using
 * the [RecyclerViewDivider.Builder.visibilityManager] method.
 *
 * @author ym.li
 * @since 2019年3月31日10:56:14
 */
public interface VisibilityManager {
    /***不绘制分割线*/
    long SHOW_NONE = 0L;
    /***至绘制分割线，不包括最后一个*/
    long SHOW_ITEMS_ONLY = 1L;
    /***至绘制分组*/
    long SHOW_GROUP_ONLY = 2L;
    /***绘制全部item分割线*/
    long SHOW_ALL = 3L;

    @Show
    long itemVisibility(int groupCount, int groupIndex, int itemPosition);

    @LongDef({SHOW_NONE, SHOW_ITEMS_ONLY, SHOW_GROUP_ONLY, SHOW_ALL})
    @Retention(RetentionPolicy.SOURCE)
    @interface Show {
    }
}

