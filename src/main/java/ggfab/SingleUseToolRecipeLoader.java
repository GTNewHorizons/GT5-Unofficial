package ggfab;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import ggfab.api.GGFabRecipeMaps;
import ggfab.api.GigaGramFabAPI;
import ggfab.items.SingleUseTool;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

class SingleUseToolRecipeLoader implements Runnable {

    public static final int RECIPE_DURATION = 6 * SECONDS;
    public static final int OUTPUT_QUANTITY_MIN = 2 * 64;
    public static final int OUTPUT_QUANTITY_MAX = 4 * 64;

    @Override
    public void run() {
        // Hard tool recipes
        addSingleUseToolRecipes(Materials.Steel, SingleUseTool.HARD_TOOLS);
        addSingleUseToolRecipes(Materials.Silver, SingleUseTool.HARD_TOOLS);
        addSingleUseToolRecipes(Materials.VanadiumSteel, SingleUseTool.HARD_TOOLS);
        addSingleUseToolRecipes(Materials.TungstenSteel, SingleUseTool.HARD_TOOLS);
        addSingleUseToolRecipes(Materials.HSSG, SingleUseTool.HARD_TOOLS);

        // Soft tool recipes
        addSingleUseToolRecipes(Materials.Rubber, SingleUseTool.SOFT_TOOLS);
        addSingleUseToolRecipes(Materials.StyreneButadieneRubber, SingleUseTool.SOFT_TOOLS);
        addSingleUseToolRecipes(Materials.Silicone, SingleUseTool.SOFT_TOOLS);

        // Mold recipes
        for (SingleUseTool singleUseTool : SingleUseTool.values()) {
            GTModHandler.addCraftingRecipe(
                singleUseTool.mold.get(1L),
                new Object[] { "h", "P", "I", 'P', ItemList.Shape_Empty, 'I', singleUseTool.toolDictName });
        }
    }

    /** Finds a common divisor of fluid and output so that output / divisor ≤ max, or falls back to an approximation. */
    private static double findDivisor(long fluidPerCraft, long outputQuantity) {
        for (long i = outputQuantity / OUTPUT_QUANTITY_MAX; i < Math.min(fluidPerCraft, outputQuantity); i++) {
            if (fluidPerCraft % i == 0 && outputQuantity % i == 0 && outputQuantity / i <= OUTPUT_QUANTITY_MAX)
                return (double) i;
        }
        return (double) outputQuantity / (long) OUTPUT_QUANTITY_MAX;
    }

    private void addSingleUseToolRecipes(Materials material, List<SingleUseTool> singleUseTools) {
        if (material.mStandardMoltenFluid == null) {
            throw new IllegalArgumentException("material does not have molten fluid form");
        }

        for (SingleUseTool singleUseTool : singleUseTools) {
            IToolStats toolStats = GigaGramFabAPI.SINGLE_USE_TOOLS.get(singleUseTool.toolDictName);
            Long toolCost = GigaGramFabAPI.COST_SINGLE_USE_TOOLS.get(singleUseTool.toolDictName);

            if (toolStats == null || toolCost == null) {
                throw new IllegalArgumentException(singleUseTool + " not registered");
            }

            float durabilityMultiplier = toolStats.getMaxDurabilityMultiplier();
            int damagePerCraft = toolStats.getToolDamagePerContainerCraft();

            long fluidPerCraft = toolCost * INGOTS / GTValues.M;
            long recipeDuration = RECIPE_DURATION;
            long outputQuantity = (long) (material.mDurability * durabilityMultiplier * 100 / damagePerCraft);

            if (outputQuantity > OUTPUT_QUANTITY_MAX) {
                // Too much output — scale down.
                double divisor = findDivisor(fluidPerCraft, outputQuantity);
                fluidPerCraft = Math.max((long) (fluidPerCraft / divisor), 1L);
                recipeDuration = Math.max((long) (recipeDuration / divisor), 1L);
                outputQuantity = Math.min((long) (outputQuantity / divisor), OUTPUT_QUANTITY_MAX);
            } else if (outputQuantity < OUTPUT_QUANTITY_MIN) {
                // Too little output — scale up.
                long multiplier = GTUtility.ceilDiv(OUTPUT_QUANTITY_MIN, outputQuantity);
                fluidPerCraft *= multiplier;
                recipeDuration *= multiplier;
                outputQuantity *= multiplier;
            }

            // Split into stacks
            ItemStack output = singleUseTool.tool.get(0L);
            output.stackSize = (int) outputQuantity; // This is a safe cast since it is between 128 and 256
            List<ItemStack> outputs = new ArrayList<>();
            int maxStackSize = output.getMaxStackSize();
            while (output.stackSize > maxStackSize) outputs.add(output.splitStack(maxStackSize));
            outputs.add(output);

            GTValues.RA.stdBuilder()
                .fluidInputs(material.getMolten(fluidPerCraft))
                .itemInputs(singleUseTool.mold.get(0L))
                .itemOutputs(outputs.toArray(new ItemStack[0]))
                .eut(TierEU.RECIPE_MV)
                .duration(recipeDuration)
                .addTo(GGFabRecipeMaps.toolCastRecipes);
        }
    }
}
