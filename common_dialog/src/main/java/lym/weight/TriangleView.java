package lym.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import basisproject.lym.org.common_dialog.R;

/**
 * 三角形指示器
 * 可配置颜色{@link #setColor(int)}
 * 可配置方向{@link #setGravity(int)}
 * <p>
 * author: ym.li
 * since: 2019/2/24
 */
public class TriangleView extends View {
    private Paint mPaint;
    private Path mPath;
    private int mWidth;
    private int mHeight;
    /**
     * {@link #setColor(int)}
     */
    private int mColor;
    /**
     * {@link #setGravity(int)}
     */
    private int mGravity;

    /**
     * constructor
     *
     * @param context 上下文
     */
    public TriangleView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * constructor
     *
     * @param context      上下文
     * @param attrs        属性集
     * @param defStyleAttr 样式集
     */
    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TriangleView);
        int mDefaultColor = ContextCompat.getColor(context, R.color.triangle_view_color);
        this.mColor = typedArray.getColor(R.styleable.TriangleView_triangle_color, mDefaultColor);
        this.mGravity = typedArray.getInt(R.styleable.TriangleView_triangle_gravity, Gravity.BOTTOM);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = getWidth();
        this.mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor);
        mPath.reset();
        switchDraw();
        canvas.drawPath(mPath, mPaint);
    }

    private void switchDraw() {
        switch (mGravity) {
            case Gravity.TOP:
                drawTop();
                break;
            case Gravity.END:
                drawRight();
                break;
            case Gravity.START:
                drawLeft();
                break;
            case Gravity.BOTTOM:
                drawBottom();
                break;
            default:
                drawBottom();
                break;
        }
    }

    /**
     * 三角形朝右
     */
    private void drawRight() {
        mPath.moveTo(0, 0);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(mWidth, mHeight / 2);
        mPath.close();
    }

    /**
     * 三角形朝左
     */
    private void drawLeft() {
        mPath.moveTo(0, mHeight / 2);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(mWidth, 0);
        mPath.close();
    }

    /**
     * 三角形朝下
     */
    private void drawBottom() {
        mPath.moveTo(0, 0);
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(mWidth / 2, mHeight);
        mPath.close();
    }

    /**
     * 三角形朝上
     */
    private void drawTop() {
        mPath.moveTo(mWidth / 2, 0);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(mWidth, mHeight);
        mPath.close();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
    }


    /**
     * 设置三角形指示器颜色
     *
     * @param color 颜色值
     */
    public void setColor(int color) {
        this.mColor = color;
    }

    /**
     * 设置指示器方向
     *
     * @param gravity Gravity.START 左边
     *                Gravity.END 右边
     *                Gravity.BOTTOM 下边
     *                Gravity.TOP 上边
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }
}
