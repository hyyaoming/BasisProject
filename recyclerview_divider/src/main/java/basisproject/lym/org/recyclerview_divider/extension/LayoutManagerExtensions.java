package basisproject.lym.org.recyclerview_divider.extension;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

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
 *
 * @author ym.li
 * @since 2019年3月31日10:48:04
 */

public class LayoutManagerExtensions {
    /**
     * Get the orientation of a [RecyclerView] using its layout manager.
     *
     * @param manager recyclerView manager
     * @return [RecyclerView.VERTICAL] or [RecyclerView.HORIZONTAL]
     */
    public static int getOrientation(RecyclerView.LayoutManager manager) {
        return manager instanceof LinearLayoutManager ? ((LinearLayoutManager) manager).getOrientation()
                : (manager instanceof StaggeredGridLayoutManager ? ((StaggeredGridLayoutManager) manager).getOrientation() : 1);
    }

    /**
     * Get the span count of a [RecyclerView].
     * <br>
     * If the layout manager hasn't a span count (like [LinearLayoutManager]), the span count will be 1.
     *
     * @param manager recyclerView manager
     * @return span count of the [RecyclerView].
     */
    public static int getSpanCount(RecyclerView.LayoutManager manager) {
        return manager instanceof GridLayoutManager ? ((GridLayoutManager) manager).getSpanCount()
                : (manager instanceof StaggeredGridLayoutManager ? ((StaggeredGridLayoutManager) manager).getSpanCount() : 1);
    }

    /**
     * Check the span size of the current item.
     * <br>
     * The span size will be minor than or equal to the span count.
     *
     * @param itemPosition position of the current item.
     * @param manager      recyclerView layoutManager
     * @return span size of the current item
     */
    public static int getSpanSize(RecyclerView.LayoutManager manager, int itemPosition) {
        RecyclerView.LayoutManager layoutManager = manager;
        if (!(manager instanceof GridLayoutManager)) {
            layoutManager = null;
        }
        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        int spanSize;
        if (gridLayoutManager != null) {
            GridLayoutManager.SpanSizeLookup var3 = gridLayoutManager.getSpanSizeLookup();
            if (var3 != null) {
                spanSize = var3.getSpanSize(itemPosition);
                return spanSize;
            }
        }
        spanSize = 1;
        return spanSize;
    }

    /**
     * Calculate the group in which the item is.
     * <br>
     * This value is between 0 and [getGroupCount] - 1.
     *
     * @param itemPosition position of the current item.
     * @param manager      recyclerView layoutManager
     * @return the index of the group.
     */
    public static int getGroupIndex(RecyclerView.LayoutManager manager, int itemPosition) {
        RecyclerView.LayoutManager layoutManager = manager;
        if (!(manager instanceof GridLayoutManager)) {
            layoutManager = null;
        }
        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        int groupIndex;
        if (gridLayoutManager != null) {
            GridLayoutManager.SpanSizeLookup var3 = gridLayoutManager.getSpanSizeLookup();
            if (var3 != null) {
                groupIndex = var3.getSpanGroupIndex(itemPosition, ((GridLayoutManager) manager).getSpanCount());
                return groupIndex;
            }
        }
        groupIndex = itemPosition;
        return groupIndex;
    }

    /**
     * Calculate the number of items' group in a list.
     * <br>
     * If the span count is 1 (for example when the layout manager is a [LinearLayoutManager]),
     * the group count will be equal to the span count.
     *
     * @param itemCount number of items in the list.
     * @param manager   recyclerView layoutManager
     * @return the number of groups
     */
    public static int getGroupCount(RecyclerView.LayoutManager manager, int itemCount) {
        int groupIndex;
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager) manager).getSpanSizeLookup();
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
            int groupCount = 0;
            for (int pos = 0; pos < itemCount; ++pos) {
                if (spanSizeLookup.getSpanIndex(pos, spanCount) == 0) {
                    ++groupCount;
                }
            }
            groupIndex = groupCount;
        } else {
            groupIndex = itemCount;
        }
        return groupIndex;
    }

    /**
     * Calculate the span accumulated in this line.
     * <br>
     * This span is calculated through the sum of the previous items' spans
     * in this line and the current item's span.
     *
     * @param spanSize     span size of the item.
     * @param itemPosition position of the current item.
     * @param groupIndex   current index of the group.
     * @param manager      recyclerView manager
     * @return accumulated span.
     */
    public static int getAccumulatedSpanInLine(RecyclerView.LayoutManager manager, int spanSize, int itemPosition, int groupIndex) {
        int lineAccumulatedSpan = spanSize;
        for (int tempPos = itemPosition - 1; tempPos >= 0; --tempPos) {
            int tempGroupIndex = getGroupIndex(manager, tempPos);
            if (tempGroupIndex != groupIndex) {
                break;
            }
            int tempSpanSize = getSpanSize(manager, tempPos);
            lineAccumulatedSpan += tempSpanSize;
        }
        return lineAccumulatedSpan;
    }

}
