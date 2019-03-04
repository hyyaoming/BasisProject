package basisproject.lym.org.bottombar;

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

import basisproject.lym.org.bottombar.weight.BadgeView;

/**
 * BottomLayout 子布局
 *
 * @author ym.li
 * @version 2.15.0
 * @since 2019年3月4日10:21:25
 */
public class BottomBarItemView extends FrameLayout {
    /**
     * 小圆点提示
     */
    private static final int ROUND_POINT_SIZE = 8;
    private static final float BADGE_TEXT_SIZE = 10f;
    /**
     * 数字提示
     */
    private static final int NUMBER_SIZE = 14;
    private static final int TEN_NUMBER = 10;
    private static final int NUMBER_LEFT_RIGHT_PADDING = 6;
    private static final int MAX_BADGE = 100;
    private static final String MAX_BADGE_TEXT = "99+";
    private static final int ROUND_POINT_LEFT_MARGIN = 10;
    private static final int ROUND_POINT_GREATER_THAN_TEN_LEFT_MARGIN = 8;
    /**
     * bottom icon
     */
    private int mUnSelectIcon;
    private int mSelectIcon;
    private ImageView mBottomIconIv;
    private Bitmap mUnSelectBitmap;
    private Bitmap mSelectBitmap;
    /**
     * bottom textView
     */
    private TextView mBottomTv;
    private CharSequence mText;
    private float mTextSize;
    private int mUnSelectTextColor;
    private int mSelectTextColor;
    /**
     * 未读消息View
     */
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
        setClipToPadding(false);
        setClipChildren(false);
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
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBadgeView.getLayoutParams();
        mBadgeView.setVisibility(View.VISIBLE);
        //圆点,设置默认宽高
        if (num <= 0) {
            lp.width = UITool.dip2px(getContext(), ROUND_POINT_SIZE);
            lp.height = UITool.dip2px(getContext(), ROUND_POINT_SIZE);
            mBadgeView.setLayoutParams(lp);
        } else {
            mBadgeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, BADGE_TEXT_SIZE);
            lp.height = UITool.dip2px(getContext(), NUMBER_SIZE);
            //圆
            if (num < TEN_NUMBER) {
                lp.width = UITool.dip2px(getContext(), NUMBER_SIZE);
                mBadgeView.setText(String.valueOf(num));
                lp.leftMargin = -UITool.dip2px(getContext(), ROUND_POINT_LEFT_MARGIN);
            } else {
                //圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                mBadgeView.setPadding(UITool.dip2px(getContext(), NUMBER_LEFT_RIGHT_PADDING), 0, UITool.dip2px(getContext(), NUMBER_LEFT_RIGHT_PADDING), 0);
                mBadgeView.setText(num < MAX_BADGE ? String.valueOf(num) : MAX_BADGE_TEXT);
                lp.leftMargin = -UITool.dip2px(getContext(), ROUND_POINT_GREATER_THAN_TEN_LEFT_MARGIN);
            }
            mBadgeView.setLayoutParams(lp);
        }
    }

    /**
     * 设置badgeView左,上重叠的大小
     *
     * @param leftMargin 左边重叠的部分
     * @param topMargin  上边重叠的部分
     */
    public void badgeMargin(int leftMargin, int topMargin) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBadgeView.getLayoutParams();
        if (null != params) {
            params.leftMargin = -UITool.dip2px(getContext(), leftMargin);
            params.topMargin = -UITool.dip2px(getContext(), topMargin);
            mBadgeView.setLayoutParams(params);
        }
    }

    /**
     * 设置Badge背景颜色
     *
     * @param colorRes 颜色属性
     */
    public void setBadgeBackgroundColor(int colorRes) {
        mBadgeView.setBackgroundColor(colorRes);
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
