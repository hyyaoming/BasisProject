package lym.listener;

import lym.base.BaseDialog;

/**
 * Dialog按钮点击事件
 * <p>
 * author: ym.li
 * since: 2019/2/24
 */
public interface OnButtonListener {
    /**
     * dialog按钮具体回调
     *
     * @param clickType       dialog按钮种类
     *                        1.{@link lym.config.CommonDialogConfig.CANCEL_CLICK}
     *                        1.{@link lym.config.CommonDialogConfig.SURE_CLICK}
     *                        1.{@link lym.config.CommonDialogConfig.NEXT_CLICK}
     * @param dialogInterface dialogInterface
     */
    void onClick(int clickType, BaseDialog dialogInterface);
}
