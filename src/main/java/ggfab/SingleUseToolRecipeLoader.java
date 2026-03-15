package ggfab;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import ggfab.api.GigaGramFabAPI;
import ggfab.items.SingleUseTool;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

class SingleUseToolRecipeLoader implements Runnable {

    public static final int SOLIDIFIER_RECIPE_DURATION = 2 * SECONDS;
    public static final int SOLIDIFIER_QUANTITY_MIN = 32;
    public static final int SOLIDIFIER_QUANTITY_MAX = 64;

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
        addSingleUseToolRecipes(Materials.RubberSilicone, SingleUseTool.SOFT_TOOLS);

        // Mold recipes
        for (SingleUseTool singleUseTool : SingleUseTool.values()) {
            GTModHandler.addCraftingRecipe(
                singleUseTool.mold.get(1L),
                new Object[] { "h", "P", "I", 'P', ItemList.Shape_Empty, 'I', singleUseTool.toolDictName });
        }
    }

    private static double findDivisorSolidifier(long fluidPerCraft, long outputQuantity) {
        for (long i = outputQuantity / SOLIDIFIER_QUANTITY_MAX; i < Math.min(fluidPerCraft, outputQuantity); i++) {
            if (fluidPerCraft % i == 0 && outputQuantity % i == 0 && outputQuantity / i <= SOLIDIFIER_QUANTITY_MAX)
                return (double) i;
        }
        return (double) outputQuantity / (long) SOLIDIFIER_QUANTITY_MAX;
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
            long outputQuantity = (long) (material.mDurability * durabilityMultiplier * 100 / damagePerCraft);
            long solidifierFluidPerCraft = toolCost * INGOTS / GTValues.M;
            long solidifierRecipeDuration = SOLIDIFIER_RECIPE_DURATION;
            long solidifierOutputQuantity = (long) (material.mDurability * durabilityMultiplier * 100 / damagePerCraft);

            if (solidifierOutputQuantity > SOLIDIFIER_QUANTITY_MAX) {
                // Too much output — scale down.
                double divisor = findDivisorSolidifier(fluidPerCraft, outputQuantity);
                solidifierFluidPerCraft = Math.max((long) (solidifierFluidPerCraft / divisor), 1L);
                solidifierRecipeDuration = Math.max((long) (solidifierRecipeDuration / divisor), 1L);
                solidifierOutputQuantity = Math
                    .min((long) (solidifierOutputQuantity / divisor), SOLIDIFIER_QUANTITY_MAX);
            } else if (solidifierOutputQuantity < SOLIDIFIER_QUANTITY_MIN) {
                // Too little output — scale up.
                long multiplier = GTUtility.ceilDiv(SOLIDIFIER_QUANTITY_MIN, outputQuantity);
                solidifierFluidPerCraft *= multiplier;
                solidifierRecipeDuration *= multiplier;
                solidifierOutputQuantity *= multiplier;
            }

            // Split into stacks
            ItemStack output = singleUseTool.tool.get(0L);
            output.stackSize = (int) outputQuantity; // This is a safe cast since it is between 128 and 256

            List<ItemStack> outputs = new ArrayList<>();
            int maxStackSize = output.getMaxStackSize();
            while (output.stackSize > maxStackSize) outputs.add(output.splitStack(maxStackSize));
            outputs.add(output);

            ItemStack solidifierOutput = singleUseTool.tool.get(0L);
            solidifierOutput.stackSize = (int) solidifierOutputQuantity;

            GTValues.RA.stdBuilder()
                .fluidInputs(material.getMolten(solidifierFluidPerCraft))
                .itemInputs(singleUseTool.mold.get(0L))
                .itemOutputs(solidifierOutput)
                .eut(TierEU.RECIPE_MV)
                .duration(solidifierRecipeDuration)
                .addTo(RecipeMaps.fluidSolidifierRecipes);
        }
    }
}
