/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material.processingLoaders;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import bartworks.util.BWUtil;
import bartworks.util.StreamUtils;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;

public class DownTierLoader {

    public static void run() {
        RecipeMap.ALL_RECIPE_MAPS.values()
            .stream()
            .filter(map -> StreamUtils.filterVisualMaps(map) && map != RecipeMaps.fusionRecipes)
            .forEach(map -> {
                Set<GTRecipe> newRecipes = new HashSet<>();
                Set<GTRecipe> toRem = new HashSet<>();
                map.getAllRecipes()
                    .stream()
                    .filter(recipe -> Objects.nonNull(recipe) && recipe.mEUt > 128)
                    .forEach(recipe -> {
                        toRem.add(recipe);
                        newRecipes.add(BWUtil.copyAndSetTierToNewRecipe(recipe, (byte) 2));
                    });
                map.getBackend()
                    .removeRecipes(toRem);
                newRecipes.forEach(map::add);
            });
    }
}
