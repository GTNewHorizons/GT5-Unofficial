package net.glease.ggfab.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.glease.ggfab.GGItemList;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.GT_Recipe;

public class GGFabRecipeMaps {

    public static final RecipeMetadataKey<ToolDictNames> OUTPUT_TYPE = SimpleRecipeMetadataKey
            .create(ToolDictNames.class, "output_type");
    public static final RecipeMetadataKey<Integer> OUTPUT_COUNT = SimpleRecipeMetadataKey
            .create(Integer.class, "output_count");
    public static final RecipeMap<RecipeMapBackend> toolCastRecipes = RecipeMapBuilder.of("ggfab.recipe.toolcast")
            .maxIO(1, 4, 1, 0).minInputs(1, 1).progressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .recipeEmitter(b -> {
                Optional<GT_Recipe> rr = b.noOptimize().validateNoInput().validateInputFluidCount(0, 1)
                        .validateNoOutput().validateNoOutputFluid().build();
                if (!rr.isPresent()) return Collections.emptyList();
                ToolDictNames outputType = b.getMetadata(OUTPUT_TYPE);
                GT_Recipe r = rr.get();
                int outputSize = b.getMetadataOrDefault(OUTPUT_COUNT, 0);
                if (outputSize > 64 * 4 || outputSize <= 0) return Collections.emptyList();
                ItemStack shape, output;
                try {
                    shape = GGItemList.valueOf("Shape_One_Use_" + outputType).get(0L);
                    output = GGItemList.valueOf("One_Use_" + outputType).get(outputSize);
                } catch (IllegalArgumentException ex) {
                    // this looks like python not java, but I don't have better way around this
                    return Collections.emptyList();
                }
                output.stackSize = outputSize;
                List<ItemStack> outputs = new ArrayList<>();
                int maxStackSize = output.getMaxStackSize();
                while (output.stackSize > maxStackSize) outputs.add(output.splitStack(maxStackSize));
                outputs.add(output);
                r.mInputs = new ItemStack[] { shape };
                r.mOutputs = outputs.toArray(new ItemStack[0]);
                return Collections.singletonList(r);
            }).build();
}
