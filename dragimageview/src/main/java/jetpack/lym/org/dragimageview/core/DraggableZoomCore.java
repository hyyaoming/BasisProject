package jetpack.lym.org.dragimageview.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import jetpack.lym.org.dragimageview.PhotoView;
import jetpack.lym.org.dragimageview.entities.DraggableImageInfo;
import jetpack.lym.org.dragimageview.entities.DraggableParamsInfo;

/**
 * author: ym.li
 * since: 2019/9/8
 */
class DraggableZoomCore {
    private static final int MAX_TRANSLATE_Y = 1500;
    private static final int DURATION = 200;
    private static final float MIN_SCALE_XY = 0.3f;
    private static final int MAX_ALPHA = 255;
    private boolean isAnimating;
    private DraggableParamsInfo draggableParams;
    private float maxHeight = 1f;
    private int mContainerWidth;
    private int mContainerHeight;
    private int mCurrentHeight;
    private int mCurrentWidth;
    private float mCurrentTransLateY;
    private float mCurrentTranslateX;
    private float mTargetTranslateY;
    private int mAlpha;
    private View scaleDraggableView;
    private float mCurrentScaleX = 1f;
    private float mCurrentScaleY = 1f;
    private ActionListener actionListener;
    private ExitAnimatorCallback exitCallback;
    //drag event params
    private float mDownX;
    private float mDownY;

    DraggableZoomCore(DraggableImageInfo draggableImageInfo, PhotoView mPhotoView, int width, int height, ActionListener draggableZoomActionListener, ExitAnimatorCallback exitAnimatorCallback) {
        this.draggableParams = draggableImageInfo.getDraggableInfo();
        this.scaleDraggableView = mPhotoView;
        this.actionListener = draggableZoomActionListener;
        this.exitCallback = exitAnimatorCallback;
        this.mContainerWidth = width;
        this.mContainerHeight = height;
    }

    boolean isAnimating() {
        return isAnimating;
    }

    void adjustScaleViewToInitLocation() {
        if (draggableParams.isValid()) {
            mCurrentHeight = draggableParams.getViewHeight();
            mCurrentWidth = draggableParams.getViewWidth();
            mCurrentTranslateX = draggableParams.getViewLeft();
            mCurrentTransLateY = draggableParams.getViewTop();
            maxHeight = mContainerWidth / draggableParams.getScaledViewWhRadio();
            if (maxHeight > mContainerHeight) {
                maxHeight = mContainerHeight;
            }
            mTargetTranslateY = (mContainerHeight - maxHeight) / 2;
        }
    }

    void adjustScaleViewToCorrectLocation() {
        if (draggableParams.isValid()) {
            maxHeight = mContainerWidth / draggableParams.getScaledViewWhRadio();
            if (maxHeight > mContainerHeight) {
                maxHeight = mContainerHeight;
            }
            mCurrentHeight = (int) maxHeight;
            mCurrentWidth = mContainerWidth;
            mCurrentTranslateX = 0f;
            mCurrentTransLateY = (mContainerHeight - maxHeight) / 2;
            mTargetTranslateY = mCurrentTransLateY;
        } else {
            mCurrentWidth = mContainerWidth;
            mCurrentHeight = mContainerHeight;
            mCurrentTranslateX = 0f;
            mCurrentTransLateY = 0f;
            mTargetTranslateY = 0f;
        }
        mAlpha = MAX_ALPHA;
        changeChildViewAnimateParams();
    }

    void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mDownX = event.getX();
                this.mDownY = event.getY();
                break;
            case 1:
                if (event.getPointerCount() == 1) {
                    if (this.mCurrentScaleY != 1.0F) {
                        if ((double) this.mCurrentScaleY < 0.7D) {
                            this.exitWithAnimator(true);
                        } else {
                            this.restoreStatusWithAnimator();
                        }
                    }
                    if (this.mCurrentTransLateY < this.mTargetTranslateY) {
                        this.restoreStatusWithAnimator();
                    }
                }
                break;
            case 2:
                if (this.mDownX == 0.0F && this.mDownY == 0.0F) {
                    this.mDownX = event.getX();
                    this.mDownY = event.getY();
                }
                float dx = event.getX() - this.mDownX;
                float dy = event.getY() - this.mDownY;
                this.onActionMove(dx, dy);
        }
    }

    private void onActionMove(float offsetX, float offsetY) {
        float percent = offsetY / MAX_TRANSLATE_Y;
        if (percent > 1) {
            percent = 1f;
        }
        if (percent < 0) {
            percent = 0f;
        }

        mCurrentTransLateY = mTargetTranslateY + offsetY;
        mCurrentTranslateX = offsetX;

        mCurrentScaleX = 1 - percent;
        mCurrentScaleY = 1 - percent;

        if (mCurrentScaleX <= MIN_SCALE_XY) mCurrentScaleX = MIN_SCALE_XY;
        if (mCurrentScaleY <= MIN_SCALE_XY) mCurrentScaleY = MIN_SCALE_XY;

        if (mCurrentScaleX > 1) mCurrentScaleX = 1f;
        if (mCurrentScaleY > 1) mCurrentScaleY = 1f;

        mCurrentWidth = (int) (mContainerWidth * mCurrentScaleX);
        mCurrentHeight = (int) (mContainerHeight * mCurrentScaleY);

        mAlpha = (int) (MAX_ALPHA - MAX_ALPHA * percent);
        changeChildViewDragParams();
    }

    //用户没有触发拖拽退出，还原状态
    private void restoreStatusWithAnimator() {
        final int initAlpha = mAlpha;
        final int dyAlpha = MAX_ALPHA - mAlpha;

        final float initScaleX = mCurrentScaleX;
        final float dScaleX = 1 - mCurrentScaleX;

        final float initScaleY = mCurrentScaleY;
        final float dScaleY = 1 - mCurrentScaleY;
        final float initX = mCurrentTranslateX;
        final float dx = 0 - mCurrentTranslateX;

        final float initY = mCurrentTransLateY;
        final float dy = mTargetTranslateY - mCurrentTransLateY;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0F, 1F);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                mCurrentTransLateY = initY + (dy * percent);
                mCurrentTranslateX = initX + (dx * percent);
                mCurrentScaleY = initScaleY + (dScaleY * percent);
                mCurrentScaleX = initScaleX + (dScaleX * percent);
                mAlpha = (int) (initAlpha + (dyAlpha * percent));
                changeChildViewDragParams();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimating = true;
            }
        });
        valueAnimator.start();
    }

    private void changeChildViewDragParams() {
        if (null != scaleDraggableView) {
            scaleDraggableView.setTranslationX(mCurrentTranslateX);
            scaleDraggableView.setTranslationY(mCurrentTransLateY);
            scaleDraggableView.setScaleX(mCurrentScaleX);
            scaleDraggableView.setScaleY(mCurrentScaleY);
        }
        if (null != actionListener) {
            actionListener.currentAlphaValue(mAlpha);
        }
    }

    boolean onInterceptTouchEvent(boolean intercept, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mDownX = event.getX();
                this.mDownY = event.getY();
                break;
            case 1:
                break;
            case 2:
                if (event.getPointerCount() == 1) {
                    float dx = event.getX() - this.mDownX;
                    float dy = event.getY() - this.mDownY;
                    if (Math.abs(dx) > Math.abs(dy)) {
                        return false;
                    }
                    if (dy > (float) 0) {
                        return true;
                    }
                }
        }
        return intercept;
    }

    private void changeChildViewAnimateParams() {
        ViewGroup.LayoutParams params = scaleDraggableView.getLayoutParams();
        if (null != params) {
            params.width = mCurrentWidth;
            params.height = mCurrentHeight;
            scaleDraggableView.setLayoutParams(params);
            scaleDraggableView.setTranslationX(this.mCurrentTranslateX);
            scaleDraggableView.setTranslationY(this.mCurrentTransLateY);
            scaleDraggableView.setScaleX(this.mCurrentScaleX);
            scaleDraggableView.setScaleY(this.mCurrentScaleY);
        }
        if (actionListener != null) {
            actionListener.currentAlphaValue(this.mAlpha);
        }
    }

    /**
     * exit animator
     */
    void exitWithAnimator(boolean isDragScale) {
        float scaleWidth = mContainerWidth * mCurrentScaleX;
        float scaleHeight = maxHeight * mCurrentScaleY;
        mCurrentTranslateX += mContainerWidth * (1 - mCurrentScaleX) / 2;
        if (isDragScale) {
            //这个是为 photo view 特殊处理的！！！
            float exScale = maxHeight / mContainerHeight;
            mCurrentTransLateY += (mContainerHeight * (1 - mCurrentScaleY * exScale) / 2 - mTargetTranslateY);
        } else {
            mCurrentTransLateY += maxHeight * (1 - mCurrentScaleY) / 2;
        }
        mCurrentScaleX = 1f;
        mCurrentScaleY = 1f;
        if (draggableParams.isValid()) {
            animateToOriginLocation(scaleWidth, scaleHeight);
        } else {
            if (null != actionListener) {
                actionListener.onExit();
            }
        }
    }

    void enterWithAnimator(final EnterAnimatorCallback animatorCallback) {
        if (!draggableParams.isValid()) {
            return;
        }
        final float dx = mCurrentTranslateX - 0;
        final float dy = mCurrentTransLateY - mTargetTranslateY;
        final float dWidth = mContainerWidth - draggableParams.getViewWidth();
        final float dHeight = maxHeight - draggableParams.getViewHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0F, 1F);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                mCurrentTranslateX = draggableParams.getViewLeft() - dx * percent;
                mCurrentTransLateY = draggableParams.getViewTop() - dy * percent;
                mCurrentWidth = (int) (draggableParams.getViewWidth() + (dWidth * percent));
                mCurrentHeight = (int) (draggableParams.getViewHeight() + (dHeight * percent));
                mAlpha = (int) (MAX_ALPHA * percent);
                changeChildViewAnimateParams();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                if (null != animatorCallback) {
                    animatorCallback.onEnterAnimatorEnd();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimating = true;
                if (null != animatorCallback) {
                    animatorCallback.onEnterAnimatorStart();
                }
            }
        });
        valueAnimator.start();
    }

    //兼容 photo view, 使photo view 的缩放功能体验更好
    void adjustViewToMatchParent() {
        mCurrentWidth = mContainerWidth;
        mCurrentHeight = mContainerHeight;
        mCurrentTranslateX = 0f;
        mCurrentTransLateY = 0f;
        mTargetTranslateY = 0f;
        changeChildViewAnimateParams();
    }

    //退出时，如果有指定入场时的view， 则回到最初的位置
    private void animateToOriginLocation(float currentWidth, float currentHeight) {
        if (null != exitCallback) {
            exitCallback.onStartInitAnimatorParams();
        }
        final float dx = mCurrentTranslateX - draggableParams.getViewLeft();
        final float dy = mCurrentTransLateY - draggableParams.getViewTop();
        final float dWidth = currentWidth - draggableParams.getViewWidth();
        final float dHeight = currentHeight - draggableParams.getViewHeight();
        ValueAnimator exitAnimator = ValueAnimator.ofFloat(1F, 0F);
        exitAnimator.setDuration(DURATION);
        exitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                mCurrentTranslateX = draggableParams.getViewLeft() + dx * percent;
                mCurrentTransLateY = draggableParams.getViewTop() + dy * percent;
                mCurrentWidth = (int) (draggableParams.getViewWidth() + (dWidth * percent));
                mCurrentHeight = (int) (draggableParams.getViewHeight() + (dHeight * percent));
                mAlpha = (int) (mAlpha * percent);
                changeChildViewAnimateParams();
            }
        });
        exitAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                if (null != actionListener) {
                    actionListener.onExit();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimating = true;
            }
        });
        exitAnimator.start();
    }

    interface ActionListener {
        void onExit();

        void currentAlphaValue(int percent);
    }

    interface EnterAnimatorCallback {
        void onEnterAnimatorStart();

        void onEnterAnimatorEnd();
    }

    interface ExitAnimatorCallback {
        void onStartInitAnimatorParams();
    }
}
