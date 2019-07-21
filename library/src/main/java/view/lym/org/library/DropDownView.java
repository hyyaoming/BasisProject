package view.lym.org.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 下拉选择控件
 * <p>
 *
 * @author ym.li
 * @version 1.0.0
 * @since 2019/7/21
 */
public class DropDownView extends RelativeLayout {
    private static final long COLLAPSE_TRANSITION_DURATION = 250L;
    private static final int DROP_DOWN_CONTAINER_MIN_DEFAULT_VIEWS = 1;
    private static final int DROP_DOWN_HEADER_CONTAINER_MIN_DEFAULT_VIEWS = 0;
    private int mBackgroundColor;
    private int mOverlayColor;
    @Nullable
    private View mExpandedView;
    private ViewGroup mDropDownHeaderContainer;
    private LinearLayout mDropDownContainer;
    private boolean mIsExpanded;
    private View mEmptyDropDownSpace;
    private TransitionSet mExpandTransitionSet;
    private TransitionSet mCollapseTransitionSet;
    private ObjectAnimator mShadowFadeOutAnimator;
    private boolean mIsTransitioning;
    private DropDownListener mDropDownListener;

    private View.OnClickListener mEmptyDropDownSpaceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            collapseDropDown();
        }
    };

    public DropDownView(Context context) {
        super(context);
        init(context, null);
    }

    public DropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * @return the {@link DropDownListener} that was set by you. Default is null.
     * @see #setDropDownListener(DropDownListener)
     */
    @Nullable
    public DropDownListener getDropDownListener() {
        return mDropDownListener;
    }

    /**
     * @param dropDownListener your implementation of {@link DropDownListener}.
     * @see DropDownListener
     */
    public void setDropDownListener(DropDownListener dropDownListener) {
        this.mDropDownListener = dropDownListener;
    }

    /**
     * @return true if the view is expanded, false otherwise.
     */
    public boolean isExpanded() {
        return mIsExpanded;
    }

    /**
     * Sets the view that will always be visible and expandable. The height of your provided view will
     * determine the height of the entire {@link DropDownView} in collapsed mode
     * (only if you set <code>wrap_content</code> to the {@link DropDownView}).
     *
     * @param headerView your header view
     */
    public void setHeaderView(@NonNull View headerView) {
        if (mDropDownHeaderContainer.getChildCount() > DROP_DOWN_HEADER_CONTAINER_MIN_DEFAULT_VIEWS) {
            for (int i = DROP_DOWN_HEADER_CONTAINER_MIN_DEFAULT_VIEWS; i < mDropDownHeaderContainer.getChildCount(); i++) {
                mDropDownHeaderContainer.removeViewAt(i);
            }
        }
        mDropDownHeaderContainer.addView(headerView);
    }

    /**
     * Sets the view that will always be visible only when the {@link DropDownView} is in expanded mode.
     * The height of your provided view and your provided header view will determine the height of
     * the entire {@link DropDownView} in expanded mode.
     * (only if you set <code>wrap_content</code> to the {@link DropDownView}).
     *
     * @param expandedView your header view
     */
    public void setExpandedView(@NonNull View expandedView) {
        this.mExpandedView = expandedView;
        if (mDropDownContainer.getChildCount() > DROP_DOWN_CONTAINER_MIN_DEFAULT_VIEWS) {
            for (int i = DROP_DOWN_CONTAINER_MIN_DEFAULT_VIEWS; i < mDropDownContainer.getChildCount(); i++) {
                mDropDownContainer.removeViewAt(i);
            }
        }
        mDropDownContainer.addView(expandedView);
        expandedView.setVisibility(mIsExpanded ? View.VISIBLE : View.GONE);
    }

    /**
     * Animates and expands the drop down, displaying the provided expanded view. Must set expanded
     * view before this for the drop down to expand.
     *
     * @see #setExpandedView(View)
     */
    public void expandDropDown() {
        if (!mIsExpanded && !mIsTransitioning && mExpandedView != null) {
            beginDelayedExpandTransition();
            if (mDropDownListener != null) {
                mDropDownListener.onExpandDropDown();
            }
            mEmptyDropDownSpace.setVisibility(View.VISIBLE);
            mExpandedView.setVisibility(View.VISIBLE);
            mIsExpanded = true;
        }
    }

    /**
     * Animates and collapses the drop down, displaying only the provided header view. Must set expanded
     * view before this for the drop down to collapse.
     *
     * @see #setExpandedView(View)
     */
    public void collapseDropDown() {
        if (mIsExpanded && !mIsTransitioning && mExpandedView != null) {
            beginDelayedCollapseTransition();
            mExpandedView.setVisibility(View.GONE);
            if (mDropDownListener != null) {
                mDropDownListener.onCollapseDropDown();
            }
            mIsExpanded = false;
        }
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttrs(context, attrs);
        inflate(getContext(), R.layout.view_ddv_drop_down, this);
        bindViews();
        setupViews();
        setupTransitionSets();
    }

    private void handleAttrs(Context context, AttributeSet attrs) {
        if (context != null) {
            if (attrs != null) {
                TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DropDownView, 0, 0);
                try {
                    mBackgroundColor = a.getColor(R.styleable.DropDownView_containerBackgroundColor, ContextCompat.getColor(context, R.color.dDVColorPrimary));
                    mOverlayColor = a.getColor(R.styleable.DropDownView_overlayColor, ContextCompat.getColor(context, R.color.dDVTransparentGray));
                } finally {
                    a.recycle();
                }
            }
            if (mBackgroundColor == 0) {
                mBackgroundColor = ContextCompat.getColor(context, R.color.dDVColorPrimary);
            }
            if (mOverlayColor == 0) {
                mOverlayColor = ContextCompat.getColor(context, R.color.dDVTransparentGray);
            }
        }
    }

    private void setupViews() {
        mEmptyDropDownSpace.setOnClickListener(mEmptyDropDownSpaceClickListener);
        mDropDownContainer.setBackgroundColor(mBackgroundColor);
        mDropDownHeaderContainer.setBackgroundColor(mBackgroundColor);
        mEmptyDropDownSpace.setBackgroundColor(mOverlayColor);
    }

    private void bindViews() {
        mDropDownContainer = findViewById(R.id.drop_down_container);
        mEmptyDropDownSpace = findViewById(R.id.empty_drop_down_space);
        mDropDownHeaderContainer = findViewById(R.id.drop_down_header);
    }

    /**
     * return the dropDownHeader
     * {@link #setHeaderView(View)}
     *
     * @return ViewGroup
     */
    public ViewGroup getDropDownHeaderContainer() {
        return mDropDownHeaderContainer;
    }

    /**
     * return the mDropDownContainer
     * {@link #setExpandedView(View)} ()}
     *
     * @return LinearLayout
     */
    public LinearLayout getDropDownContainer() {
        return mDropDownContainer;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void beginDelayedExpandTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(this, mExpandTransitionSet);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void beginDelayedCollapseTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mShadowFadeOutAnimator.start();
            TransitionManager.beginDelayedTransition(mDropDownContainer, mCollapseTransitionSet);
        } else {
            mEmptyDropDownSpace.setVisibility(View.GONE);
        }
    }

    private void setupTransitionSets() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mShadowFadeOutAnimator = ObjectAnimator.ofFloat(mEmptyDropDownSpace, View.ALPHA, 0f);
            mShadowFadeOutAnimator.setDuration(COLLAPSE_TRANSITION_DURATION);
            mShadowFadeOutAnimator.setInterpolator(new AccelerateInterpolator());
            mShadowFadeOutAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mEmptyDropDownSpace.setVisibility(View.GONE);
                    mEmptyDropDownSpace.setAlpha(1f);
                }
            });
            mExpandTransitionSet = createTransitionSet();
            mCollapseTransitionSet = createTransitionSet();
            mCollapseTransitionSet.setDuration(COLLAPSE_TRANSITION_DURATION);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private TransitionSet createTransitionSet() {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.addTarget(mDropDownContainer);
        Fade fade = new Fade();
        fade.addTarget(mEmptyDropDownSpace);
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(changeBounds);
        transitionSet.addTransition(fade);
        transitionSet.setInterpolator(new AccelerateDecelerateInterpolator());
        transitionSet.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mIsTransitioning = true;
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mIsTransitioning = false;
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        return transitionSet;
    }

    /**
     * A listener that wraps functionality to be performed when the drop down is successfully expanded
     * or collapsed.
     */
    public interface DropDownListener {
        /**
         * This method will only be triggered when {@link #expandDropDown()} is called successfully.
         */
        void onExpandDropDown();

        /**
         * This method will only be triggered when {@link #collapseDropDown()} is called successfully.
         */
        void onCollapseDropDown();
    }

}
