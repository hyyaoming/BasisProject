package lym.base;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import basisproject.lym.org.common_dialog.R;
import lym.config.CommonDialogConfig;
import lym.listener.OnButtonListener;

/**
 * 提示框基类
 * <p>
 * author: ym.li
 * since: 2019/2/24
 */
public abstract class BaseAlertDialog extends BaseDialog implements View.OnClickListener {
    /*内容布局*/
    /**
     * 内容布局{@link #onCreateView()}
     */
    protected LinearLayout mContainerView;
    /**
     * 内容布局背景颜色{@link #setContainerBgColor(int)}
     */
    protected int mContainerBgColor;
    /**
     * 内容布局圆角度数{@link #setCornerRadius(float)}
     */
    protected float mCornerRadius = 3f;
    /* 头部布局相关*/
    /**
     * 头部标题
     */
    protected TextView mTitleView;
    /**
     * 头部标题文字大小{@link #setTitleTvSize(float)}
     */
    protected float mTitleTvSize;
    /**
     * 头部标题文字{@link #setTitleTvContent(CharSequence)}
     */
    protected CharSequence mTitleTvContent;
    /**
     * 头部标题文字颜色{@link #setTitleTvColor(int)}
     */
    protected int mTitleTvColor;
    /**
     * 是否显示头部标题{@link #setShowTitleTvEnable(boolean)}
     */
    protected boolean mShowTitleTvEnable = true;
    /**
     * 内容布局相关
     */
    protected TextView mContentView;
    /**
     * 是否显示内容布局{@link #setShowContentTvEnable(boolean)}
     */
    protected boolean mShowContentTvEnable = true;
    /**
     * 内容布局文字大小{@link #setContentTvSize(float)}
     */
    protected float mContentTvSize;
    /**
     * 内容布局文字颜色{@link #setContentTvColor(int)}
     */
    protected int mContentTvColor;
    /**
     * 内容布局文案 @{@link #setContentTv(CharSequence)}
     */
    protected CharSequence mContentTv;
    /* 按钮布局相关*/
    /**
     * 装载Button的ParentView
     */
    protected LinearLayout mButtonLayout;
    /**
     * 按钮点击颜色{@link #setBtnPressColor(int)}
     */
    protected int mBtnPressColor;
    /**
     * 取消按钮
     */
    protected TextView mCancelButton;
    /**
     * 取消按钮文本{@link #setCancelBtnContent(CharSequence)}
     */
    protected CharSequence mCancelBtnContent;
    /**
     * 取消按钮文字颜色{@link #setCancelBtnColor(int)}
     */
    protected int mCancelBtnColor;
    /**
     * 取消按钮文字大小{@link #setCancelBtnSize(float)}
     */
    protected float mCancelBtnSize = CommonDialogConfig.BUTTON_SIZE;
    /**
     * 是否显示取消按钮{@link #setShowCancelBtnEnable(boolean)}
     */
    protected boolean mShowCancelBtnEnable = true;
    /**
     * 确认按钮
     */
    protected TextView mSureButton;
    /**
     * 确认按钮文字{@link #setSureBtnContent(CharSequence)}
     */
    protected CharSequence mSureBtnContent;
    /**
     * 确认按钮文字大小{@link #setSureBtnSize(float)}
     */
    protected float mSureBtnSize = CommonDialogConfig.BUTTON_SIZE;
    /**
     * 确认按钮文字颜色{@link #setSureBtnColor(int)}
     */
    protected int mSureBtnColor;
    /**
     * 是否显示确认按钮{@link #setShowSureBtnEnable(boolean)}
     */
    protected boolean mShowSureBtnEnable = true;
    /**
     * 继续按钮
     */
    protected TextView mNextButton;
    /**
     * 是否显示继续按钮{@link #setShowNextBtnEnable(boolean)}
     */
    protected boolean mShowNextBtnEnable = false;
    /**
     * 继续按钮文字颜色{@link #setNextBtnColor(int)}
     */
    protected int mNextBtnColor;
    /**
     * 继续按钮文字大小{@link #setNextBtnSize(float)}
     */
    protected float mNextBtnSize = CommonDialogConfig.BUTTON_SIZE;
    /**
     * 继续按钮文案{@link #setNextBtnContent(CharSequence)}
     */
    protected CharSequence mNextBtnContent;
    /**
     * 按钮点击回调{@link #setButtonClickListener(OnButtonListener)}
     */
    protected OnButtonListener mButtonClickListener;

    /**
     * constructor
     *
     * @param context 上下文
     */
    BaseAlertDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        initContainerView();
        initTitleView();
        initContentView();
        initButtonView();
    }

    private void initButtonView() {
        //init button parent layout
        mBtnPressColor = ContextCompat.getColor(mContext, R.color.dialog_material_design_default_burton_click_color);
        mButtonLayout = new LinearLayout(mContext);
        mButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        //init cancel button
        mCancelButton = new TextView(mContext);
        mCancelButton.setGravity(Gravity.CENTER);
        //init sure button
        mSureButton = new TextView(mContext);
        mSureButton.setGravity(Gravity.CENTER);
        //init next button
        mNextButton = new TextView(mContext);
        mNextButton.setGravity(Gravity.CENTER);
    }

    private void initContentView() {
        mContentView = new TextView(mContext);
        mContentView.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initContainerView() {
        mContainerBgColor = Color.WHITE;
        mContainerView = new LinearLayout(mContext);
        mContainerView.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected float getWidthScale() {
        return CommonDialogConfig.ALERT_DIALOG_WIDTH_SCALE;
    }

    private void initTitleView() {
        mTitleView = new TextView(mContext);
        mTitleView.setGravity(Gravity.CENTER);
    }

    @Override
    protected void showDialogBefore() {
        super.showDialogBefore();
        bindTitleView();
        bindContentView();
        bindButtonView();
    }

    private void bindButtonView() {
        //bind cancel button
        mCancelButton.setTextColor(mCancelBtnColor);
        mCancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, mCancelBtnSize);
        mCancelButton.setVisibility(mShowCancelBtnEnable ? View.VISIBLE : View.GONE);
        mCancelButton.setText(TextUtils.isEmpty(mCancelBtnContent)
                ? mContext.getResources().getString(R.string.dialog_button_cancel) : mCancelBtnContent);
        mCancelButton.setTag(CommonDialogConfig.CANCEL_CLICK);
        mCancelButton.setOnClickListener(this);
        //bind sure button
        mSureButton.setTextColor(mSureBtnColor);
        mSureButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSureBtnSize);
        mSureButton.setVisibility(mShowSureBtnEnable ? View.VISIBLE : View.GONE);
        mSureButton.setText(TextUtils.isEmpty(mSureBtnContent)
                ? mContext.getResources().getString(R.string.dialog_button_sure) : mSureBtnContent);
        mSureButton.setTag(CommonDialogConfig.SURE_CLICK);
        mSureButton.setOnClickListener(this);
        //bind next button
        mNextButton.setVisibility(mShowNextBtnEnable ? View.VISIBLE : View.GONE);
        mNextButton.setTextColor(mNextBtnColor);
        mNextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, mNextBtnSize);
        mNextButton.setText(TextUtils.isEmpty(mNextBtnContent)
                ? mContext.getResources().getString(R.string.dialog_button_next) : mNextBtnContent);
        mNextButton.setTag(CommonDialogConfig.NEXT_CLICK);
        mNextButton.setOnClickListener(this);
    }

    private void bindContentView() {
        mContentView.setVisibility(mShowContentTvEnable ? View.VISIBLE : View.GONE);
        mContentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContentTvSize);
        mContentView.setText(mContentTv);
        mContentView.setLineSpacing(0, CommonDialogConfig.LINE_SPACING);
        mContentView.setTextColor(mContentTvColor);
    }

    private void bindTitleView() {
        mTitleView.setVisibility(mShowTitleTvEnable ? View.VISIBLE : View.GONE);
        mTitleView.setTextColor(mTitleTvColor);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleTvSize);
        mTitleView.setText(TextUtils.isEmpty(mTitleTvContent)
                ? mContext.getResources().getString(R.string.dialog_title_default_text) : mTitleTvContent);
    }

    /**
     * 设置头部标题文本大小
     *
     * @param titleTvSize 头部标题文字大小
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setTitleTvSize(float titleTvSize) {
        this.mTitleTvSize = titleTvSize;
        return this;
    }

    /**
     * 设置头部文本内容  默认为“温馨提示”
     *
     * @param titleTvContent 设置头部标题文本内容
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setTitleTvContent(CharSequence titleTvContent) {
        this.mTitleTvContent = titleTvContent;
        return this;
    }

    /**
     * 设置头部标题颜色
     *
     * @param titleTvColor 头部标题颜色
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setTitleTvColor(int titleTvColor) {
        this.mTitleTvColor = titleTvColor;
        return this;
    }

    /**
     * 头部标题是否显示
     *
     * @param showTitleTvEnable 头部标题是否显示 true 显示 false 隐藏 默认显示
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setShowTitleTvEnable(boolean showTitleTvEnable) {
        this.mShowTitleTvEnable = showTitleTvEnable;
        return this;
    }

    /**
     * 设置中部内容文本是否显示
     *
     * @param showContentTvEnable 中部内容文本是否显示 true 显示 false 隐藏 默认显示
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setShowContentTvEnable(boolean showContentTvEnable) {
        this.mShowContentTvEnable = showContentTvEnable;
        return this;
    }

    /**
     * 设置中部内容文本大小
     *
     * @param contentTvSize 中部内容文本大小
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setContentTvSize(float contentTvSize) {
        this.mContentTvSize = contentTvSize;
        return this;
    }

    /**
     * 设置中部内容文本颜色
     *
     * @param contentTvColor 中部内容文本颜色
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setContentTvColor(int contentTvColor) {
        this.mContentTvColor = contentTvColor;
        return this;
    }

    /**
     * 设置中部内容文本
     *
     * @param contentTv 中部内容文本
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setContentTv(CharSequence contentTv) {
        this.mContentTv = contentTv;
        return this;
    }

    /**
     * 设置取消按钮文本内容   默认文本为“取消”
     *
     * @param cancelBtnContent 取消按钮文本内容
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setCancelBtnContent(CharSequence cancelBtnContent) {
        this.mCancelBtnContent = cancelBtnContent;
        return this;
    }

    /**
     * 设置取消按钮文字颜色
     *
     * @param cancelBtnColor 取消按钮文本颜色
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setCancelBtnColor(int cancelBtnColor) {
        this.mCancelBtnColor = cancelBtnColor;
        return this;
    }

    /**
     * 设置取消按钮文字大小
     *
     * @param cancelBtnSize 取消按钮文字大小
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setCancelBtnSize(float cancelBtnSize) {
        this.mCancelBtnSize = cancelBtnSize;
        return this;
    }

    /**
     * 设置取消按钮是否显示
     *
     * @param showCancelBtnEnable 设置取消按钮是否显示 true 显示 false 隐藏  默认显示
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setShowCancelBtnEnable(boolean showCancelBtnEnable) {
        this.mShowCancelBtnEnable = showCancelBtnEnable;
        return this;
    }

    /**
     * 设置确认按钮文字内容   默认文案为“确定”
     *
     * @param sureBtnContent 确认按钮文字内容
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setSureBtnContent(CharSequence sureBtnContent) {
        this.mSureBtnContent = sureBtnContent;
        return this;
    }

    /**
     * 设置确认按钮文字大小
     *
     * @param sureBtnSize 确认按钮文字大小
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setSureBtnSize(float sureBtnSize) {
        this.mSureBtnSize = sureBtnSize;
        return this;
    }

    /**
     * 设置确认按钮颜色值
     *
     * @param sureBtnColor 确认按钮颜色
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setSureBtnColor(int sureBtnColor) {
        this.mSureBtnColor = sureBtnColor;
        return this;
    }

    /**
     * 设置确认按钮是否显示
     *
     * @param showSureBtnEnable 是否显示确认按钮 true 显示 false 隐藏 默认显示
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setShowSureBtnEnable(boolean showSureBtnEnable) {
        this.mShowSureBtnEnable = showSureBtnEnable;
        return this;
    }

    /**
     * 设置继续按钮是否显示
     *
     * @param showNextBtnEnable 继续按钮是否显示 true 显示  false隐藏 默认隐藏
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setShowNextBtnEnable(boolean showNextBtnEnable) {
        this.mShowNextBtnEnable = showNextBtnEnable;
        return this;
    }

    /**
     * 设置继续文本颜色
     *
     * @param nextBtnColor 继续按钮文本颜色资源
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setNextBtnColor(int nextBtnColor) {
        this.mNextBtnColor = nextBtnColor;
        return this;
    }

    /**
     * 设置继续按钮文本大小
     *
     * @param nextBtnSize 继续按钮文本大小
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setNextBtnSize(float nextBtnSize) {
        this.mNextBtnSize = nextBtnSize;
        return this;
    }

    /**
     * 设置继续按钮文本 默认文本为“继续”
     *
     * @param nextBtnContent 继续按钮文本内容
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setNextBtnContent(CharSequence nextBtnContent) {
        this.mNextBtnContent = nextBtnContent;
        return this;
    }

    /**
     * 设置Dialog点击事件
     *
     * @param buttonClickListener OnButtonListener
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setButtonClickListener(OnButtonListener buttonClickListener) {
        mButtonClickListener = buttonClickListener;
        return this;
    }


    /**
     * 设置内容布局背景颜色
     *
     * @param containerBgColor 内容布局背景颜色
     */
    public BaseAlertDialog setContainerBgColor(int containerBgColor) {
        this.mContainerBgColor = containerBgColor;
        return this;
    }

    /**
     * 设置内容布局圆角度数
     *
     * @param cornerRadius 内容布局圆角度数
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;
        return this;
    }

    /**
     * 设置按钮点击颜色
     *
     * @param btnPressColor 按钮点击颜色
     * @return BaseAlertDialog
     */
    public BaseAlertDialog setBtnPressColor(int btnPressColor) {
        this.mBtnPressColor = btnPressColor;
        return this;
    }

    @Override
    public void onClick(View v) {
        int viewTag = (int) v.getTag();
        if (null != mButtonClickListener) {
            this.mButtonClickListener.onClick(viewTag, this);
        } else {
            dismiss();
        }
    }
}
