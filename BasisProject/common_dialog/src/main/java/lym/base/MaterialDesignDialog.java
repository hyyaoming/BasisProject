package lym.base;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import basisproject.lym.org.common_dialog.R;
import lym.config.CommonDialogConfig;
import lym.utils.UITools;

/**
 * MaterialDesign风格Dialog
 * <p>
 * author: ym.li
 * since: 2019/2/24
 */
public class MaterialDesignDialog extends BaseAlertDialog {

    /**
     * constructor
     *
     * @param context 上下文
     */
    public MaterialDesignDialog(Context context) {
        super(context);
        initAttr();
    }

    private void initAttr() {
        setTitleTvColor(ContextCompat.getColor(mContext, R.color.dialog_material_design_default_title_color));
        setTitleTvSize(CommonDialogConfig.DEFAULT_TITLE_SIZE);
        setContentTvColor(ContextCompat.getColor(mContext, R.color.dialog_material_design_default_content_color));
        setContentTvSize(CommonDialogConfig.DEFAULT_CONTENT_SIZE);
        setCancelBtnColor(ContextCompat.getColor(mContext, R.color.dialog_material_design_default_cancel_color));
        setSureBtnColor(ContextCompat.getColor(mContext, R.color.dialog_material_design_default_sure_color));
        setNextBtnColor(ContextCompat.getColor(mContext, R.color.dialog_material_design_default_next_color));
    }

    @Override
    protected View onCreateView() {
        int padding = dp2px(CommonDialogConfig.DEFAULT_TITLE_PADDING);
        //title view
        addTitleView(padding);
        //content view
        addContentView(padding);
        //btn layout
        addButtonLayout();
        //button padding
        buttonPadding();
        mContainerView.addView(mButtonLayout);
        return mContainerView;
    }

    private void buttonPadding() {
        //取消按钮设置间距 left 15, right 8 , top 15, bottom 8;
        mCancelButton.setPadding(dp2px(CommonDialogConfig.DEFAULT_BUTTON_LEFT_PADDING),
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_RIGHT_PADDING),
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_LEFT_PADDING), dp2px(CommonDialogConfig.DEFAULT_BUTTON_RIGHT_PADDING));
        //继续按钮设置间距  left 15, right 8 , top 15, bottom 8;
        mNextButton.setPadding(dp2px(CommonDialogConfig.DEFAULT_BUTTON_LEFT_PADDING),
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_RIGHT_PADDING),
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_LEFT_PADDING), dp2px(CommonDialogConfig.DEFAULT_BUTTON_RIGHT_PADDING));
        //确认按钮设置间距  left 15, right 8 , top 15, bottom 8;
        mSureButton.setPadding(dp2px(CommonDialogConfig.DEFAULT_BUTTON_LEFT_PADDING),
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_RIGHT_PADDING),
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_LEFT_PADDING), dp2px(CommonDialogConfig.DEFAULT_BUTTON_RIGHT_PADDING));
        //包括三个按钮的ButtonLayout设置间距 left 20, right 0, top 10, bottom 10;
        mButtonLayout.setPadding(dp2px(CommonDialogConfig.DEFAULT_TITLE_PADDING), 0,
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_BOTTOM_PADDING),
                dp2px(CommonDialogConfig.DEFAULT_BUTTON_BOTTOM_PADDING));
    }

    private void addButtonLayout() {
        mButtonLayout.setGravity(Gravity.END);
        mButtonLayout.addView(mCancelButton);
        mButtonLayout.addView(mNextButton);
        mButtonLayout.addView(mSureButton);
    }

    private void addContentView(int padding) {
        mContentView.setPadding(padding, padding, padding, padding);
        mContentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mContainerView.addView(mContentView);
    }

    private void addTitleView(int padding) {
        mTitleView.setGravity(Gravity.CENTER_VERTICAL);
        mTitleView.setPadding(padding, padding, padding, 0);
        mTitleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mContainerView.addView(mTitleView);
    }

    @Override
    protected void showDialogBefore() {
        super.showDialogBefore();
        float radius = dp2px(mCornerRadius);
        mContainerView.setBackgroundDrawable(UITools.cornerDrawable(mContainerBgColor, radius));
        mCancelButton.setBackgroundDrawable(UITools.btnSelector(radius, mContainerBgColor, mBtnPressColor));
        mNextButton.setBackgroundDrawable(UITools.btnSelector(radius, mContainerBgColor, mBtnPressColor));
        mSureButton.setBackgroundDrawable(UITools.btnSelector(radius, mContainerBgColor, mBtnPressColor));
    }
}
