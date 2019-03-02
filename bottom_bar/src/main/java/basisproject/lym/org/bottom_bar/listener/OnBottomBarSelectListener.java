package basisproject.lym.org.bottom_bar.listener;

/**
 * Item Bar选中和再次选中回调事件
 * <p>
 * author: ym.li
 * since: 2019/3/2
 */
public interface OnBottomBarSelectListener {
    /**
     * 当前点击的角标
     *
     * @param position 当前选中的角标
     */
    void onBottomBarSelect(int position);

    /**
     * 当前选中的角标
     *
     * @param position 角标
     */
    void onBottomBarSelectRepeat(int position);
}
