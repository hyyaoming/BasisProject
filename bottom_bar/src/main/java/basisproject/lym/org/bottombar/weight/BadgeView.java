package basisproject.lym.org.bottombar.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import basisproject.lym.org.bottombar.R;

/**
 * 未读消息数量,或小红点提示
 * <p>
 * author: ym.li
 * since: 2019/3/2
 */
public class BadgeView extends AppCompatTextView {
    private Context context;
    private GradientDrawable gd_background = new GradientDrawable();
    private int backgroundColor;
    private int cornerRadius;
    private int strokeWidth;
    private int strokeColor;
    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public BadgeView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * constructor
     *
     * @param context      上下文
     * @param attrs        属性集
     * @param defStyleAttr 样式集
     */
    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BadgeView);
        backgroundColor = ta.getColor(R.styleable.BadgeView_bv_bg_color, Color.TRANSPARENT);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.BadgeView_bv_corner_radius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.BadgeView_bv_stroke_width, 0);
        strokeColor = ta.getColor(R.styleable.BadgeView_bv_stroke_color, Color.TRANSPARENT);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.BadgeView_bv_isRadiusHalfHeight, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.BadgeView_bv_isWidthHeightEqual, false);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isRadiusHalfHeight()) {
            setCornerRadius(getHeight() / 2);
        } else {
            setBgSelector();
        }
    }

    /**
     * 设置圆度度数是否为高度的一般
     *
     * @param isRadiusHalfHeight boolean
     */
    public void setIsRadiusHalfHeight(boolean isRadiusHalfHeight) {
        this.isRadiusHalfHeight = isRadiusHalfHeight;
        setBgSelector();
    }

    /**
     * 圆角矩形宽高相等,取较宽高中大值
     *
     * @param isWidthHeightEqual boolean
     */
    public void setIsWidthHeightEqual(boolean isWidthHeightEqual) {
        this.isWidthHeightEqual = isWidthHeightEqual;
        setBgSelector();
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor int
     */
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBgSelector();
    }

    /**
     * 设置圆角度数
     *
     * @param cornerRadius 圆角度数
     */
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = dp2px(cornerRadius);
        setBgSelector();
    }

    /**
     * 设置边框大小
     *
     * @param strokeWidth int
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = dp2px(strokeWidth);
        setBgSelector();
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor int
     */
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setBgSelector();
    }

    private boolean isRadiusHalfHeight() {
        return isRadiusHalfHeight;
    }

    private boolean isWidthHeightEqual() {
        return isWidthHeightEqual;
    }

    private int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);
        gd.setCornerRadius(cornerRadius);
        gd.setStroke(strokeWidth, strokeColor);
    }

    private void setBgSelector() {
        StateListDrawable bg = new StateListDrawable();
        setDrawable(gd_background, backgroundColor, strokeColor);
        bg.addState(new int[]{-android.R.attr.state_pressed}, gd_background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            setBackground(bg);
        } else {
            //noinspection deprecation
            setBackgroundDrawable(bg);
        }
    }
}
