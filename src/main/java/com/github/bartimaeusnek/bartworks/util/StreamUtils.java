/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util;

import gregtech.api.util.GT_Recipe;
import java.util.Optional;
import java.util.function.Predicate;

public class StreamUtils {
    private StreamUtils() {}

    public static Predicate<GT_Recipe.GT_Recipe_Map> filterVisualMaps() {
        return gt_recipe_map -> {
            Optional<GT_Recipe> op = gt_recipe_map.mRecipeList.stream().findAny();
            return op.isPresent() && !op.get().mFakeRecipe;
        };
    }

    public static boolean filterVisualMaps(GT_Recipe.GT_Recipe_Map gt_recipe_map) {
        return filterVisualMaps().test(gt_recipe_map);
    }
}
