package basisproject.lym.org.bottom_bar;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import basisproject.lym.org.bottom_bar.weight.BadgeView;

/**
 * BottomBarItemView
 * <p>
 * author: ym.li
 * since: 2019/3/2
 */
public class BottomBarItemView extends FrameLayout {
    /**
     * 小圆点提示
     */
    private static final int ROUND_POINT_SIZE = 10;
    private static final int ROUND_POINT_TOP_MARGIN = 3;
    private static final int ROUND_POINT_LEFT_MARGIN = ROUND_POINT_SIZE;
    private static final float BADGE_TEXT_SIZE = 10f;
    /**
     * 数字提示
     */
    private static final int NUMBER_SIZE = 14;
    private static final int TEN_NUMBER = 10;
    private static final int NUMBER_LEFT_MARGIN = 16;
    private static final int NUMBER_LEFT_RIGHT_PADDING = 6;
    private static final int MAX_BADGE = 100;
    private static final String MAX_BADGE_TEXT = "99+";
    /*bottom icon*/
    private int mUnSelectIcon;
    private int mSelectIcon;
    private ImageView mBottomIconIv;
    private Bitmap mUnSelectBitmap;
    private Bitmap mSelectBitmap;
    /*bottom tv*/
    private TextView mBottomTv;
    private CharSequence mText;
    private float mTextSize;
    /*常量相关*/
    private int mUnSelectTextColor;
    private int mSelectTextColor;
    /*未读消息View*/
    private BadgeView mBadgeView;


    /**
     * constructor
     *
     * @param context 上下文
     */
    public BottomBarItemView(@NonNull Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public BottomBarItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * constructor
     *
     * @param context      上下文
     * @param attrs        属性集
     * @param defStyleAttr 样式集
     */
    public BottomBarItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 设置未选中Bitmap
     *
     * @param normalBitmap Bitmap
     */
    public void setUnSelectBitmap(Bitmap normalBitmap) {
        mUnSelectBitmap = normalBitmap;
    }

    /**
     * 设置选中Bitmap
     *
     * @param selectBitmap Bitmap
     */
    public void setSelectBitmap(Bitmap selectBitmap) {
        mSelectBitmap = selectBitmap;
    }

    private void initView() {
        inflate(getContext(), R.layout.bottom_item_layout, this);
        this.mBottomIconIv = findViewById(R.id.iv_bottom_icon);
        this.mBottomTv = findViewById(R.id.tv_bottom_content);
        this.mBadgeView = findViewById(R.id.badgeView);
    }

    /**
     * 获取当前ImageView
     *
     * @return ImageView
     */
    public ImageView getBottomIconIv() {
        return mBottomIconIv;
    }

    /**
     * 获取当前消息未读View
     *
     * @return BadgeView
     */
    public BadgeView getBadgeView() {
        return mBadgeView;
    }

    /**
     * 获取当前TextView
     *
     * @return TextView
     */
    public TextView getBottomTv() {
        return mBottomTv;
    }

    private void createBottomBarItem(Builder builder) {
        this.mUnSelectIcon = builder.mUnSelectRes;
        this.mSelectIcon = builder.mSelectRes;
        this.mText = builder.mText;
        this.mUnSelectTextColor = builder.mUnSelectTextColor;
        this.mTextSize = builder.mTextSize;
        this.mSelectTextColor = builder.mSelectTextColor;
        bindBottomBar();
    }

    private void bindBottomBar() {
        mBottomTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mBottomTv.setText(mText);
        updateTabState(false);
    }

    /**
     * 刷新当前item状态
     *
     * @param checked 当前item是选中还是未选中
     */
    public void updateTabState(boolean checked) {
        mBottomTv.setTextColor(checked ? mSelectTextColor : mUnSelectTextColor);
        if (null != mUnSelectBitmap && null != mSelectBitmap) {
            mBottomIconIv.setImageBitmap(checked ? mSelectBitmap : mUnSelectBitmap);
        } else {
            mBottomIconIv.setImageResource(checked ? mSelectIcon : mUnSelectIcon);
        }
    }

    /**
     * 显示未读消息
     *
     * @param num num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int num) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mBadgeView.getLayoutParams();
        mBadgeView.setVisibility(View.VISIBLE);
        if (num <= 0) {//圆点,设置默认宽高
            lp.width = dp2px(ROUND_POINT_SIZE);
            lp.height = dp2px(ROUND_POINT_SIZE);
            lp.topMargin = dp2px(ROUND_POINT_TOP_MARGIN);
            lp.leftMargin = dp2px(ROUND_POINT_LEFT_MARGIN);
            mBadgeView.setLayoutParams(lp);
        } else {
            mBadgeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, BADGE_TEXT_SIZE);
            lp.height = dp2px(NUMBER_SIZE);
            if (num < TEN_NUMBER) {//圆
                lp.width = dp2px(NUMBER_SIZE);
                mBadgeView.setText(String.valueOf(num));
            } else if (num < MAX_BADGE) {//圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                mBadgeView.setPadding(dp2px(NUMBER_LEFT_RIGHT_PADDING), 0, dp2px(NUMBER_LEFT_RIGHT_PADDING), 0);
                mBadgeView.setText(String.valueOf(num));
            } else {//数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                mBadgeView.setPadding(dp2px(NUMBER_LEFT_RIGHT_PADDING), 0, dp2px(NUMBER_LEFT_RIGHT_PADDING), 0);
                mBadgeView.setText(MAX_BADGE_TEXT);
            }
            lp.leftMargin = dp2px(NUMBER_LEFT_MARGIN);
            mBadgeView.setLayoutParams(lp);
        }
    }

    private int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 更新当前文本
     *
     * @param charSequence CharSequence
     */
    public void updateBottomContent(CharSequence charSequence) {
        this.mText = charSequence;
        mBottomTv.setText(charSequence);
    }

    /**
     * 隐藏当前消息未读View
     */
    public void hideBadge() {
        mBadgeView.setVisibility(GONE);
    }

    static class Builder {
        private Context mContext;
        private int mUnSelectTextColor;
        private int mSelectTextColor;
        private float mTextSize;
        private int mUnSelectRes;
        private int mSelectRes;
        private CharSequence mText;

        Builder(Context context) {
            mContext = context;
        }

        Builder setText(CharSequence textContent) {
            mText = textContent;
            return this;
        }

        Builder setUnSelectTextColor(int normalTextColor) {
            this.mUnSelectTextColor = normalTextColor;
            return this;
        }

        Builder setSelectTextColor(int selectTextColor) {
            this.mSelectTextColor = selectTextColor;
            return this;
        }

        Builder setTextSize(float textSize) {
            this.mTextSize = textSize;
            return this;
        }

        Builder setUnSelectRes(int normalRes) {
            this.mUnSelectRes = normalRes;
            return this;
        }

        Builder setSelectRes(int selectRes) {
            this.mSelectRes = selectRes;
            return this;
        }

        BottomBarItemView build() {
            BottomBarItemView itemView = new BottomBarItemView(mContext);
            itemView.createBottomBarItem(this);
            return itemView;
        }
    }
}
