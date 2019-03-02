package basisproject.lym.org.bottom_bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import basisproject.lym.org.bottom_bar.listener.OnBottomBarEntity;
import basisproject.lym.org.bottom_bar.listener.OnBottomBarSelectListener;

/**
 * 底部导航布局
 * <p>
 * author: ym.li
 * since: 2019/3/2
 */
public class BottomBarLayout extends LinearLayout {
    private static final int DEFAULT_SIZE = 10;
    private SparseArray<BottomBarItemView> mItems = new SparseArray<>();
    private int mCurrentItemIndex = -1;
    private float mTextSize;
    private int mUnSelectTextColor;
    private int mSelectTextColor;
    private OnBottomBarSelectListener mSelectListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public BottomBarLayout(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public BottomBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * constructor
     *
     * @param context      上下文
     * @param attrs        属性集
     * @param defStyleAttr 样式集
     */
    public BottomBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    /**
     * 设置BottomBar选中回调
     *
     * @param selectListener OnBottomBarSelectListener
     */
    public void setBottomBarSelectListener(OnBottomBarSelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    private int sp2px() {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) ((float) DEFAULT_SIZE * fontScale + 0.5f);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomBarLayout);
        this.mTextSize = typedArray.getDimensionPixelSize(R.styleable.BottomBarLayout_bi_text_size, sp2px());
        this.mUnSelectTextColor = typedArray.getColor(R.styleable.BottomBarLayout_bi_un_select_text_color, Color.parseColor("#cccccc"));
        this.mSelectTextColor = typedArray.getColor(R.styleable.BottomBarLayout_bi_select_text_color, Color.parseColor("#D33D3C"));
        typedArray.recycle();
    }

    private void addBottomBarItem(BottomBarItemView itemView) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        itemView.setLayoutParams(params);
        addView(itemView);
        initView();
    }

    /**
     * 获取当前选中的角标
     *
     * @return index
     */
    public int getCurrentItemIndex() {
        return mCurrentItemIndex;
    }

    private void initView() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            BottomBarItemView itemView = (BottomBarItemView) getChildAt(i);
            mItems.put(i, itemView);
            itemView.setOnClickListener(new ItemViewOnclickListener(i));
        }
    }

    /**
     * 设置当前BottomItem数据
     *
     * @param entices List<OnBottomBarEntity>
     */
    public void setTabEntity(List<OnBottomBarEntity> entices) {
        if (null == entices || entices.size() == 0) {
            throw new IllegalStateException("The array must not be empty, and the length must be greater than one");
        }
        refresh(entices);
    }

    private void refresh(List<OnBottomBarEntity> entices) {
        for (OnBottomBarEntity entity : entices) {
            BottomBarItemView barItemView = new BottomBarItemView.Builder(getContext())
                    .setSelectRes(entity.getSelectIcon())
                    .setUnSelectRes(entity.getUnSelectIcon())
                    .setText(entity.getText())
                    .setTextSize(mTextSize)
                    .setUnSelectTextColor(mUnSelectTextColor)
                    .setSelectTextColor(mSelectTextColor)
                    .build();
            addBottomBarItem(barItemView);
        }
        setDefaultChecked(0);
    }

    /**
     * 设置默认选中的Item
     *
     * @param position 下标
     */
    public void setDefaultChecked(int position) {
        if (position >= 0 && position < mItems.size()) {
            mCurrentItemIndex = position;
            mItems.get(position).updateTabState(true);
        }
    }

    private void updateBottomItemState(int currentIndex) {
        resetState();
        mCurrentItemIndex = currentIndex;
        mItems.get(mCurrentItemIndex).updateTabState(true);
    }

    private void resetState() {
        if (mCurrentItemIndex != -1 && mCurrentItemIndex < mItems.size()) {
            mItems.get(mCurrentItemIndex).updateTabState(false);
        }
    }

    private void checkArray(List<OnBottomBarEntity> array) {
        if (null == array || array.size() == 0) {
            throw new IllegalStateException("The array must not be empty, and the length must be greater than one");
        }
    }

    /**
     * 获取服务器动态Icon时可使用此方法动态替换icon和文本
     *
     * @param entities List<OnBottomBarEntity>
     */
    public void updateAllBottomBar(List<OnBottomBarEntity> entities) {
        checkArray(entities);
        int length = entities.size();
        for (int i = 0; i < length; i++) {
            BottomBarItemView itemView = mItems.get(i);
            OnBottomBarEntity entity = entities.get(i);
            itemView.setUnSelectBitmap(entity.getUnSelectBitmap());
            itemView.setSelectBitmap(entity.getSelectBitmap());
            itemView.updateBottomContent(entity.getText());
            itemView.updateTabState(false);
        }
    }

    /**
     * 显示未读消息数量
     *
     * @param position 下标
     * @param number   数量
     */
    public void showBadge(int position, int number) {
        if (position >= 0 && position < mItems.size()) {
            mItems.get(position).showMsg(number);
        }
    }

    /**
     * 隐藏未读消息数量
     *
     * @param position 下标
     */
    public void hideBadge(int position) {
        mItems.get(position).hideBadge();
    }

    private class ItemViewOnclickListener implements OnClickListener {

        private int mCurrentIndex;

        /**
         * constructor
         *
         * @param currentIndex 当前角标
         */
        ItemViewOnclickListener(int currentIndex) {
            mCurrentIndex = currentIndex;
        }

        @Override
        public void onClick(View v) {
            if (null != mSelectListener) {
                if (mCurrentIndex != mCurrentItemIndex) {
                    mSelectListener.onBottomBarSelect(mCurrentIndex);
                } else {
                    mSelectListener.onBottomBarSelectRepeat(mCurrentIndex);
                }
            }
            updateBottomItemState(mCurrentIndex);
        }
    }
}
