package lym.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import basisproject.lym.org.common_dialog.R;
import lym.config.CommonDialogConfig;
import lym.utils.UITools;


/**
 * author: ym.li
 * since: 2019/2/24
 */
public abstract class BaseDialog extends AppCompatDialog {
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 顶层布局
     */
    protected LinearLayout mDecorView;
    /**
     * 内容布局
     */
    protected LinearLayout mPatentView;
    /**
     * 由子类建造的内容布局{@link #onCreateView()}
     */
    protected View mRootView;
    /**
     * Dialog cancelEnable
     */
    protected boolean mCancelEnable = true;
    /**
     * 设置Dialog进出场动画{@link #setDialogAnim(int)}
     */
    protected int mDialogAnim = R.style.DialogAnim;
    /**
     * 设置当前Dialog的透明度{@link #setDialogAnim(int)}
     */
    protected float mDimAmount = CommonDialogConfig.DIALOG_DIMAMOUNT;

    /**
     * constructor
     *
     * @param context 上下文
     */
    BaseDialog(Context context) {
        super(context);
        this.mContext = context;
        if (isActivityFinish()) {
            return;
        }
        setCanceledOnTouchOutside(true);
    }

    private boolean isActivityFinish() {
        return null == mContext || mContext instanceof Activity && ((Activity) mContext).isFinishing();
    }

    /**
     * 由子类返回的布局
     *
     * @return View
     */
    protected abstract View onCreateView();

    /**
     * View建造好之后的回调
     *
     * @param view 返回子类自己创建的布局 {@link #onCreateView()}
     */
    protected void onViewCreate(View view) {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showDialogBefore();
        deployParentView();
    }

    private void deployParentView() {
        int width;
        int height;
        if (getWidthScale() == 0f) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            width = (int) (getDisplayMetrics().widthPixels * getWidthScale());
        }
        if (getHeightScale() == 0f) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (getHeightScale() == 1f) {
            height = getDisplayMetrics().heightPixels - UITools.getStatusHeight(mContext);
        } else {
            height = getHeightScale() * getDisplayMetrics().heightPixels;
        }
        mPatentView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    /**
     * 在显示Dialog之前子类可以做一些操作
     */
    protected void showDialogBefore() {
    }

    /**
     * 设置当前dialog的高度比例 取值为 0到1之间
     *
     * @return default heightScale
     */
    protected int getHeightScale() {
        return 0;
    }


    /**
     * 设置dialog进出场动画
     *
     * @param dialogAnim 动画Style
     * @return BaseDialog
     */
    public void setDialogAnim(int dialogAnim) {
        this.mDialogAnim = dialogAnim;
    }

    /**
     * 设置当前dialog的宽度壁垒，取值为0到1之间
     *
     * @return default1 widthScale
     */
    protected float getWidthScale() {
        return 0f;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        this.mCancelEnable = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogTheme();
        initView();
    }

    private void initView() {
        mDecorView = new LinearLayout(mContext);
        mDecorView.setGravity(Gravity.CENTER);
        mPatentView = new LinearLayout(mContext);
        mPatentView.setOrientation(LinearLayout.VERTICAL);
        mRootView = onCreateView();
        mRootView.setClickable(true);
        mPatentView.addView(mRootView);
        mDecorView.addView(mPatentView);
        onViewCreate(mRootView);
        int width = getDisplayMetrics().widthPixels;
        int height = getDisplayMetrics().heightPixels - UITools.getStatusHeight(mContext);
        setContentView(mDecorView, new ViewGroup.LayoutParams(width, height));
        mDecorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelEnable) {
                    dismiss();
                }
            }
        });
    }

    private DisplayMetrics getDisplayMetrics() {
        return mContext.getResources().getDisplayMetrics();
    }

    private void initDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new ColorDrawable());
            window.setDimAmount(mDimAmount);
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        }
    }

    @Override
    public void dismiss() {
        if (isActivityFinish()) {
            return;
        }
        super.dismiss();
    }

    @Override
    public void show() {
        if (isActivityFinish()) {
            return;
        }
        setAnim();
        super.show();
    }

    private void setAnim() {
        if (mDialogAnim != 0 && null != getWindow()) {
            getWindow().setWindowAnimations(mDialogAnim);
        }
    }

    /**
     * 设置当前Dialog弹出后背景的透明度
     *
     * @param dimAmount 透明度阈值 0到1之间
     */
    public void setDimAmount(float dimAmount) {
        this.mDimAmount = dimAmount;
    }

    /**
     * dp tp px
     *
     * @param dp dp value
     * @return px
     */
    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
