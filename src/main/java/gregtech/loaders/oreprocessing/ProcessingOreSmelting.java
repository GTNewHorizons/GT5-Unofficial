package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingOreSmelting implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private final OrePrefixes[] mSmeltingPrefixes = { OrePrefixes.crushed, OrePrefixes.crushedPurified,
        OrePrefixes.crushedCentrifuged, OrePrefixes.dust, OrePrefixes.dustImpure, OrePrefixes.dustPure,
        OrePrefixes.dustRefined };

    public ProcessingOreSmelting() {
        for (OrePrefixes tPrefix : this.mSmeltingPrefixes) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        if (material.contains(SubTag.NO_ORE_PROCESSING)) return;

        GTModHandler.removeFurnaceSmelting(stack);

        if (material.contains(SubTag.NO_SMELTING)) return;

        // Blast furnace is required for processing this ore.
        if (material.mBlastFurnaceRequired || material.mDirectSmelting.mBlastFurnaceRequired) {
            if (material.mBlastFurnaceTemp >= 1000) return;
            if (material.mDirectSmelting.mBlastFurnaceTemp >= 1000) return;
            if (!material.mAutoGenerateBlastFurnaceRecipes) return;

            final ItemStack output = material.getIngots(1);

            if (output == null) return;

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .circuit(1)
                .itemOutputs(output)
                .duration(Math.max(material.getMass() / 4L, 1L) * material.mBlastFurnaceTemp * TICKS)
                .eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, (int) material.mBlastFurnaceTemp)
                .addTo(blastFurnaceRecipes);

            return;
        }

        // Blast furnace is *not* required for processing this ore.
        switch (prefix.getName()) {
            case "crushed", "crushedPurified", "crushedCentrifuged" -> {
                if (material.mDirectSmelting == material) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 10);
                } else if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 6);
                } else {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                }
            }
            case "dust" -> {
                if (material.mDirectSmelting == material) {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                } else if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 6);
                } else {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                }

                if (material.mDirectSmelting == material) return;

                final int outputAmount = GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;

                if (!material.contains(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
                    && material.mDirectSmelting.getIngots(1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, stack))
                        .itemOutputs(material.mDirectSmelting.getIngots(outputAmount))
                        .duration(2 * MINUTES)
                        .metadata(ADDITIVE_AMOUNT, 2)
                        .addTo(primitiveBlastRecipes);
                } else {
                    addSpecialDustRecipes(material, outputAmount);
                }

            }
            case "dustImpure", "dustPure", "dustRefined" -> {
                if (material.mDirectSmelting == material) {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                } else if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 6);
                } else {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                }
            }
            default -> addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
        }
    }

    private static void addSpecialDustRecipes(Materials material, int outputAmount) {
        if (material == Materials.Chalcopyrite) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2), new ItemStack(Blocks.sand, 2))
                .itemOutputs(
                    material.mDirectSmelting.getIngots(outputAmount),
                    Materials.Ferrosilite.getDustSmall(2 * outputAmount))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2), Materials.Glass.getDust(2))
                .itemOutputs(
                    material.mDirectSmelting.getIngots(outputAmount),
                    Materials.Ferrosilite.getDustSmall(7 * outputAmount))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2), Materials.SiliconDioxide.getDust(2))
                .itemOutputs(
                    material.mDirectSmelting.getIngots(outputAmount),
                    Materials.Ferrosilite.getDustSmall(outputAmount))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2), Materials.NetherQuartz.getDust(2))
                .itemOutputs(
                    material.mDirectSmelting.getIngots(outputAmount),
                    Materials.Ferrosilite.getDustSmall(outputAmount))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2), Materials.CertusQuartz.getDust(2))
                .itemOutputs(
                    material.mDirectSmelting.getIngots(outputAmount),
                    Materials.Ferrosilite.getDustSmall(outputAmount))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        } else if (material == Materials.Tetrahedrite) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2))
                .itemOutputs(
                    material.mDirectSmelting.getIngots(outputAmount),
                    Materials.Antimony.getNuggets(3 * outputAmount))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        } else if (material == Materials.Galena) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2))
                .itemOutputs(
                    material.mDirectSmelting.getIngots(outputAmount),
                    Materials.Silver.getNuggets(3 * outputAmount))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        }
    }

    private static void addSmeltingRecipe(Materials material, ItemStack stack, OrePrefixes prefix, int size) {
        ItemStack smeltingOutput = GTOreDictUnificator.get(prefix, material.mDirectSmelting, size);

        if (smeltingOutput == null) {
            smeltingOutput = material.contains(SubTag.SMELTING_TO_GEM)
                ? GTOreDictUnificator.get(OrePrefixes.gem, material.mDirectSmelting, 1L)
                : GTOreDictUnificator.get(OrePrefixes.ingot, material.mDirectSmelting, 1L);
        }

        if (smeltingOutput != null) GTModHandler.addSmeltingRecipe(stack, smeltingOutput);
    }
}
