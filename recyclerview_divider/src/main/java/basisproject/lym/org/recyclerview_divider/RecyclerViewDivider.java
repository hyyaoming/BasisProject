package basisproject.lym.org.recyclerview_divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Px;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import basisproject.lym.org.recyclerview_divider.extension.LayoutManagerExtensions;
import basisproject.lym.org.recyclerview_divider.extension.LayoutParamsExtensions;
import basisproject.lym.org.recyclerview_divider.extension.ViewExtensions;
import basisproject.lym.org.recyclerview_divider.manager.drawable.DefaultDrawableManager;
import basisproject.lym.org.recyclerview_divider.manager.drawable.DrawableManager;
import basisproject.lym.org.recyclerview_divider.manager.inset.DefaultInsetManager;
import basisproject.lym.org.recyclerview_divider.manager.inset.InsetManager;
import basisproject.lym.org.recyclerview_divider.manager.size.DefaultSizeManager;
import basisproject.lym.org.recyclerview_divider.manager.size.SizeManager;
import basisproject.lym.org.recyclerview_divider.manager.tint.DefaultTintManager;
import basisproject.lym.org.recyclerview_divider.manager.tint.TintManager;
import basisproject.lym.org.recyclerview_divider.manager.visibility.DefaultVisibilityManager;
import basisproject.lym.org.recyclerview_divider.manager.visibility.HideLastVisibilityManager;
import basisproject.lym.org.recyclerview_divider.manager.visibility.VisibilityManager;


/**
 * Copyright (c) 2017 Fondesa
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * 原作者GitHub地址：https://github.com/Fondesa/RecyclerViewDivider
 * <p>
 * 原版为Kotlin，项目中目前没有使用kotlin特将它改成了java版
 * 对原项目进行了部分扩展改良
 * 修复了之前使用GridLayoutManger时Item宽度不一致的问题
 * 该Divider支持一下功能
 * 1.支持单独配置某一个颜色，以及大小
 * 2.支持配置多种Drawable分割线属性
 * 3.支持是否绘制最后一行分割线
 * 4.支持分割线设置偏移量进行绘制
 * 5.支持控制单个item的分割线是否绘制
 * 该分割线基本上可以满足所有场景分割线的绘制
 * 搭配recyclerView多布局使用效果更佳
 *
 * @author ym.li
 * @version 2.19.0
 * @since 2019/3/29 0029 10:23
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {
    private boolean mIsSpace;
    /**
     * 可以设置一个Drawable作为当前分割线的背景
     */
    private DrawableManager mDrawableManager;
    /**
     * 可以设置当前分割线绘制的偏移量
     */
    private InsetManager mInsetManager;
    /**
     * 可以设置当前分割线的大小
     */
    private SizeManager mSizeManager;
    /**
     * 可以设置不同的drawable
     */
    private TintManager mTintManager;
    /**
     * 可以设置当前分割线是否绘制
     */
    private VisibilityManager mVisibilityManager;

    /**
     * Used to draw a divider between [RecyclerView]'s elements.
     *
     * @param isSpace           true if the divider must be managed like a simple space.
     * @param drawableManager   instance of [DrawableManager] taken from [Builder].
     * @param insetManager      instance of [DrawableManager] taken from [Builder].
     * @param sizeManager       instance of [SizeManager] taken from [Builder].
     * @param tintManager       instance of [TintManager] taken from [Builder].
     * @param visibilityManager instance of [VisibilityManager] taken from [Builder].
     */
    private RecyclerViewDivider(boolean isSpace,
                                DrawableManager drawableManager,
                                InsetManager insetManager,
                                SizeManager sizeManager,
                                TintManager tintManager,
                                VisibilityManager visibilityManager) {
        this.mIsSpace = isSpace;
        this.mDrawableManager = drawableManager;
        this.mInsetManager = insetManager;
        this.mSizeManager = sizeManager;
        this.mTintManager = tintManager;
        this.mVisibilityManager = visibilityManager;
    }

    /**
     * Builder class for [RecyclerViewDivider].
     *
     * @param context [Context] used to access the resources.
     * @return RecyclerViewDivider.Builder
     */
    public static RecyclerViewDivider.Builder with(Context context) {
        return new Builder(context);
    }

    /**
     * Add this divider to a [RecyclerView].
     *
     * @param recyclerView [RecyclerView] at which the divider will be added.
     */
    public void addTo(RecyclerView recyclerView) {
        this.removeFrom(recyclerView);
        recyclerView.addItemDecoration(this);
    }

    /**
     * Remove this divider from a [RecyclerView].
     *
     * @param recyclerView [RecyclerView] from which the divider will be removed.
     */
    private void removeFrom(RecyclerView recyclerView) {
        recyclerView.removeItemDecoration(this);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (null == adapter) {
            return;
        }
        int listSize = adapter.getItemCount();
        if (listSize > 0) {
            RecyclerView.LayoutManager lm = parent.getLayoutManager();
            if (null == lm) {
                return;
            }
            int itemPosition = parent.getChildAdapterPosition(view);
            if (itemPosition != -1) {
                int groupCount = LayoutManagerExtensions.getGroupCount(lm, listSize);
                int groupIndex = LayoutManagerExtensions.getGroupIndex(lm, itemPosition);
                long showDivider = this.mVisibilityManager.itemVisibility(groupCount, groupIndex, itemPosition);
                if (showDivider != 0L) {
                    int orientation = LayoutManagerExtensions.getOrientation(lm);
                    int spanCount = LayoutManagerExtensions.getSpanCount(lm);
                    int spanSize = LayoutManagerExtensions.getSpanSize(lm, itemPosition);
                    int lineAccumulatedSpan = LayoutManagerExtensions.getAccumulatedSpanInLine(lm, spanSize, itemPosition, groupIndex);
                    Drawable divider = this.mDrawableManager.itemDrawable(groupCount, groupIndex, itemPosition);
                    int size = this.mSizeManager.itemSize(divider, orientation, groupCount, groupIndex, itemPosition);
                    int insetBefore = this.mInsetManager.itemInsetBefore(groupCount, groupIndex, itemPosition);
                    int insetAfter = this.mInsetManager.itemInsetAfter(groupCount, groupIndex, itemPosition);
                    if (spanCount > 1 && (insetBefore > 0 || insetAfter > 0)) {
                        insetBefore = 0;
                        insetAfter = 0;
                    }
                    int halfSize = size / 2;
                    size = showDivider == 1L ? 0 : size;
                    halfSize = showDivider == 2L ? 0 : halfSize;
                    boolean isRTL = ViewExtensions.isRTL((View) parent);
                    if (orientation == RecyclerView.VERTICAL) {
                        if (spanCount != 1 && spanSize != spanCount) {
                            if (lineAccumulatedSpan == spanSize) {
                                // first element in the group
                                setOutRect(isRTL, outRect, 0, 0, halfSize + insetAfter, size);
                            } else if (lineAccumulatedSpan == spanCount) {
                                // last element in the group
                                setOutRect(isRTL, outRect, halfSize + insetBefore, 0, 0, size);
                            } else {
                                // element in the middle
                                setOutRect(isRTL, outRect, halfSize + insetBefore, 0, halfSize + insetAfter, size);
                            }
                        } else {
                            // LinearLayoutManager or GridLayoutManager with 1 column
                            setOutRect(isRTL, outRect, 0, 0, 0, size);
                        }
                    } else if (spanCount != 1 && spanSize != spanCount) {
                        // last element in the group
                        if (lineAccumulatedSpan == spanSize) {
                            // first element in the group
                            setOutRect(isRTL, outRect, 0, 0, size, halfSize + insetAfter);
                        } else if (lineAccumulatedSpan == spanCount) {
                            // last element in the group
                            setOutRect(isRTL, outRect, 0, halfSize + insetBefore, size, 0);
                        } else {
                            // element in the middle
                            setOutRect(isRTL, outRect, 0, halfSize + insetBefore, size, halfSize + insetAfter);
                        }
                    } else {
                        // LinearLayoutManager or GridLayoutManager with 1 row
                        setOutRect(isRTL, outRect, 0, 0, size, 0);
                    }
                }
            }
        }
    }

    private void setOutRect(boolean isRTL, Rect outRect, int leftB, int topB, int rightB, int bottomB) {
        if (isRTL) {
            outRect.set(rightB, topB, leftB, bottomB);
        } else {
            outRect.set(leftB, topB, rightB, bottomB);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int listSize = adapter != null ? adapter.getItemCount() : 0;
        // if the divider isn't a simple space, it will be drawn
        if (!this.mIsSpace && listSize != 0) {
            RecyclerView.LayoutManager lm = parent.getLayoutManager();
            if (null == lm) {
                return;
            }
            int orientation = LayoutManagerExtensions.getOrientation(lm);
            int spanCount = LayoutManagerExtensions.getSpanCount(lm);
            int childCount = parent.getChildCount();
            int groupCount = LayoutManagerExtensions.getGroupCount(lm, listSize);
            boolean isRTL = ViewExtensions.isRTL(parent);
            int i = 0;
            for (; i < childCount; ++i) {
                View child = parent.getChildAt(i);
                int itemPosition = parent.getChildAdapterPosition(child);
                if (itemPosition == RecyclerView.NO_POSITION) {
                    // Avoid the computation if the position of at least one view cannot be retrieved.
                    return;
                }
                int groupIndex = LayoutManagerExtensions.getGroupIndex(lm, itemPosition);
                long showDivider = this.mVisibilityManager.itemVisibility(groupCount, groupIndex, itemPosition);
                if (showDivider != 0L) {
                    Drawable divider = this.mDrawableManager.itemDrawable(groupCount, groupIndex, itemPosition);
                    int size = this.mSizeManager.itemSize(divider, orientation, groupCount, groupIndex, itemPosition);
                    int spanSize = LayoutManagerExtensions.getSpanSize(lm, itemPosition);
                    int lineAccumulatedSpan = LayoutManagerExtensions.getAccumulatedSpanInLine(lm, spanSize, itemPosition, groupIndex);
                    int insetBefore = this.mInsetManager.itemInsetBefore(groupCount, groupIndex, itemPosition);
                    int insetAfter = this.mInsetManager.itemInsetAfter(groupCount, groupIndex, itemPosition);
                    if (spanCount > 1 && (insetBefore > 0 || insetAfter > 0)) {
                        insetBefore = 0;
                        insetAfter = 0;
                    }
                    int endMargin;
                    if (this.mTintManager != null) {
                        endMargin = mTintManager.itemTint(groupCount, groupIndex);
                        Drawable wrappedDrawable = DrawableCompat.wrap(divider);
                        DrawableCompat.setTint(wrappedDrawable, endMargin);
                        divider = wrappedDrawable;
                    }
                    ViewGroup.LayoutParams childLayoutParams = child.getLayoutParams();
                    if (childLayoutParams == null) {
                        throw new NullPointerException("null cannot be cast to non-null type android.support.v7.widget.RecyclerView.LayoutParams");
                    }
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childLayoutParams;
                    int startMargin = LayoutParamsExtensions.getStartMarginCompat((ViewGroup.MarginLayoutParams) params);
                    int topMargin = params.topMargin;
                    endMargin = LayoutParamsExtensions.getEndMarginCompat((ViewGroup.MarginLayoutParams) params);
                    int bottomMargin = params.bottomMargin;
                    int halfSize = size < 2 ? size : size / 2;
                    size = showDivider == 1L ? 0 : size;
                    halfSize = showDivider == 2L ? 0 : halfSize;
                    int childBottom = child.getBottom();
                    int childTop = child.getTop();
                    int childRight = child.getRight();
                    int childLeft = child.getLeft();
                    // if the last element in the span doesn't complete the span count, its size will be full, not the half
                    // halfSize * 2 is used instead of size to handle the case Show.ITEMS_ONLY in which size will be == 0
                    int lastElementInSpanSize = itemPosition == listSize - 1 ? halfSize * 2 : halfSize;
                    int left, top, right, bottom;
                    if (orientation == RecyclerView.VERTICAL) {
                        // The RecyclerView is vertical.
                        if (spanCount > 1 && spanSize < spanCount) {
                            top = childTop - topMargin;
                            // size is added to draw filling point between horizontal and vertical dividers
                            bottom = childBottom + bottomMargin + size;
                            // First element.
                            if (lineAccumulatedSpan == spanSize) {
                                if (isRTL) {
                                    right = childLeft - endMargin;
                                    left = right - lastElementInSpanSize;
                                } else {
                                    right = childRight + endMargin;
                                    left = right + lastElementInSpanSize;
                                }
                                onDraw(parent, divider, c, left, top, right, bottom);
                            } else if (lineAccumulatedSpan == spanCount) {
                                // Last element.
                                if (isRTL) {
                                    right = childRight + startMargin;
                                    left = right + lastElementInSpanSize;
                                } else {
                                    right = childLeft - startMargin;
                                    left = right - lastElementInSpanSize;
                                }
                                onDraw(parent, divider, c, left, top, right, bottom);
                            } else {
                                // Element in the middle.
                                if (isRTL) {
                                    right = childRight + startMargin;
                                    left = right + lastElementInSpanSize;
                                } else {
                                    right = childLeft - startMargin;
                                    left = right - lastElementInSpanSize;
                                }
                                onDraw(parent, divider, c, left, top, right, bottom);
                                if (isRTL) {
                                    right = childLeft - endMargin;
                                    left = right - lastElementInSpanSize;
                                } else {
                                    right = childRight + endMargin;
                                    left = right + lastElementInSpanSize;
                                }
                                onDraw(parent, divider, c, left, top, right, bottom);
                            }
                        }
                        // draw bottom divider
                        top = childBottom + bottomMargin;
                        bottom = top + size;
                        if (isRTL) {
                            left = childLeft + insetAfter - endMargin;
                            right = childRight - insetBefore + startMargin;
                        } else {
                            left = childLeft + insetBefore - startMargin;
                            right = childRight - insetAfter + endMargin;
                        }
                        onDraw(parent, divider, c, left, top, right, bottom);
                    } else {
                        // The RecyclerView is horizontal.
                        if (spanCount > 1 && spanSize < spanCount) {
                            // size is added to draw filling point between horizontal and vertical dividers
                            if (isRTL) {
                                left = childLeft - endMargin - size;
                                right = childRight + startMargin;
                            } else {
                                left = childLeft - startMargin;
                                right = childRight + endMargin + size;
                            }
                            // first element in the group
                            if (lineAccumulatedSpan == spanSize) {
                                top = childBottom + bottomMargin;
                                bottom = top + lastElementInSpanSize;
                                onDraw(parent, divider, c, left, top, right, bottom);
                            } else if (lineAccumulatedSpan == spanCount) {   // last element in the group
                                bottom = childTop - topMargin;
                                top = bottom - lastElementInSpanSize;
                                onDraw(parent, divider, c, left, top, right, bottom);
                            } else {
                                // element in the middle
                                // top half divider
                                bottom = childTop - topMargin;
                                top = bottom - lastElementInSpanSize;
                                divider.setBounds(left, top, right, bottom);
                                divider.draw(c);
                                // bottom half divider
                                top = childBottom + bottomMargin;
                                bottom = top + lastElementInSpanSize;
                                onDraw(parent, divider, c, left, top, right, bottom);
                            }
                        }
                        // draw right divider
                        bottom = childBottom - insetAfter + bottomMargin;
                        top = childTop + insetBefore - topMargin;
                        if (isRTL) {
                            left = childLeft - endMargin;
                            right = left - size;
                        } else {
                            left = childRight + endMargin;
                            right = left + size;
                        }
                        onDraw(parent, divider, c, left, top, right, bottom);
                    }
                }
            }
        }
    }

    private void onDraw(RecyclerView view, Drawable drawable, Canvas canvas, int left, int top, int right, int bottom) {
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    /**
     * Build RecyclerView Divider Value
     */
    public static class Builder {
        private DrawableManager mDrawableManager;
        private InsetManager mInsetManager;
        private SizeManager mSizeManager;
        private TintManager mTintManager;
        private VisibilityManager mVisibilityManager;
        private boolean mIsSpace;
        private Context mContext;

        /**
         * Build recyclerView divider
         *
         * @param context context
         */
        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * Set this divider as a space.
         * This method is different from setting the transparent color as divider, because
         * it will not draw anything, so, it's the most optimized one.
         *
         * @return RecyclerViewDivider.Builder
         */
        public RecyclerViewDivider.Builder asSpace() {
            this.mIsSpace = true;
            return this;
        }

        /**
         * Set the color of all dividers.
         * <br>
         * To set a custom color for each divider use [mDrawableManager] instead.
         *
         * @param color resolved color for this divider, not a resource.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder color(@ColorInt int color) {
            return this.drawable(new ColorDrawable(color));
        }

        /**
         * Set the drawable of all dividers.
         * <br>
         * To set a custom drawable for each divider use [mDrawableManager] instead.
         * Warning: if the span count is major than one and the drawable can't be mirrored,
         * the drawable will not be shown correctly.
         *
         * @param drawable drawable custom drawable for this divider.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder drawable(Drawable drawable) {
            return this.drawableManager(new DefaultDrawableManager(drawable));
        }

        /**
         * Hide the divider after the last group of items.
         * <br>
         * Warning: when the spanCount is major than 1, only the divider after
         * the last group will be hidden, the items' dividers, instead, will be shown.
         * <br>
         * If you want to specify a more flexible behaviour, use [mVisibilityManager] instead.
         *
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder hideLastDivider() {
            return this.visibilityManager(new HideLastVisibilityManager());
        }

        /**
         * Set the inset before and after the divider.
         * It will be equal for all dividers.
         * To set a custom inset for each divider, use [mInsetManager] instead.
         *
         * @param insetBefore the inset that will be applied before.
         * @param insetAfter  the inset that will be applied after.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder inset(@Px int insetBefore, @Px int insetAfter) {
            return this.insetManager(new DefaultInsetManager(insetBefore, insetAfter));
        }

        /**
         * Set the size of all dividers. The divider's final size will depend on RecyclerView's orientation:
         * Size is referred to the height of an horizontal divider and to the width of a vertical divider.
         * To set a custom size for each divider use [mSizeManager] instead.
         *
         * @param size size in pixels for this divider.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder size(@Px int size) {
            return this.sizeManager(new DefaultSizeManager(size));
        }

        /**
         * Set the tint color of all dividers' drawables.
         * If you want to create a plain divider with a single color, [color] is recommended.
         * To set a custom tint color for each divider's drawable use [mTintManager] instead.
         *
         * @param color color that will be used as drawable's tint.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder tint(@ColorInt int color) {
            return this.tintManager(new DefaultTintManager(color));
        }

        /**
         * Set the divider's custom [DrawableManager].
         * Warning: if the span count is major than one and the drawable can't be mirrored,
         * the drawable will not be shown correctly.
         *
         * @param drawableManager custom [DrawableManager] to set.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder drawableManager(DrawableManager drawableManager) {
            this.mDrawableManager = drawableManager;
            this.mIsSpace = false;
            return this;
        }

        /**
         * Set the divider's custom [InsetManager].
         *
         * @param insetManager custom [InsetManager] to set.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder insetManager(InsetManager insetManager) {
            this.mInsetManager = insetManager;
            return this;
        }

        /**
         * Set the divider's custom [SizeManager].
         *
         * @param sizeManager custom [SizeManager] to set.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder sizeManager(SizeManager sizeManager) {
            this.mSizeManager = sizeManager;
            return this;
        }

        /**
         * Set the divider's custom [TintManager].
         *
         * @param tintManager custom [TintManager] to set.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder tintManager(TintManager tintManager) {
            this.mTintManager = tintManager;
            return this;
        }

        /**
         * Set the divider's custom [VisibilityManager].
         * <br>
         * If you want to hide only the last divider use [hideLastDivider] instead.
         *
         * @param visibilityManager custom [VisibilityManager] to set.
         * @return this [Builder] instance.
         */
        public RecyclerViewDivider.Builder visibilityManager(VisibilityManager visibilityManager) {
            this.mVisibilityManager = visibilityManager;
            return this;
        }

        /**
         * Creates a new [RecyclerViewDivider] with given configurations and initializes all values.
         *
         * @return a new [RecyclerViewDivider] with these [Builder] configurations.
         */
        public RecyclerViewDivider build() {
            //set default  mDrawableManager
            if (this.mDrawableManager == null) {
                mDrawableManager = new DefaultDrawableManager();
            }
            //set default  mInsetManager
            if (this.mInsetManager == null) {
                mInsetManager = (new DefaultInsetManager());
            }
            //set default  mSizeManager
            if (this.mSizeManager == null) {
                mSizeManager = new DefaultSizeManager(this.mContext);
            }
            //set default  mVisibilityManager
            if (this.mVisibilityManager == null) {
                mVisibilityManager = new DefaultVisibilityManager();
            }
            // Creates the divider.
            return new RecyclerViewDivider(this.mIsSpace, mDrawableManager, mInsetManager, mSizeManager, this.mTintManager, mVisibilityManager);
        }
    }
}
