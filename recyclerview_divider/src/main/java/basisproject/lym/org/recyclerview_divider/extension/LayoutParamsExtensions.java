package basisproject.lym.org.recyclerview_divider.extension;

import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.ViewGroup;

import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.ViewGroup;

/**
 * Copyright (c) 2017 Fondesa
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * *     http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 *
 * @author ym.li
 * @since 2019年3月31日10:48:09
 */

public class LayoutParamsExtensions {
    /**
     * Get the start margin for all APIs.
     *
     * @param layoutParams recyclerView child LayoutParams
     * @return the start margin of these [ViewGroup.MarginLayoutParams].
     */
    public static int getStartMarginCompat(ViewGroup.MarginLayoutParams layoutParams) {
        return MarginLayoutParamsCompat.getMarginStart(layoutParams);
    }

    /**
     * Get the end margin for all APIs.
     *
     * @param layoutParams recyclerView child LayoutParams
     * @return the end margin of these [ViewGroup.MarginLayoutParams].
     */
    public static int getEndMarginCompat(ViewGroup.MarginLayoutParams layoutParams) {
        return MarginLayoutParamsCompat.getMarginEnd(layoutParams);
    }
}
