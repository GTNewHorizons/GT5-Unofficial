package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingOreSmelting implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private final OrePrefixes[] mSmeltingPrefixes = { OrePrefixes.crushed, OrePrefixes.crushedPurified,
        OrePrefixes.crushedCentrifuged, OrePrefixes.dust, OrePrefixes.dustImpure, OrePrefixes.dustPure,
        OrePrefixes.dustRefined };

    public static ProcessingOreSmelting INSTANCE;

    public ProcessingOreSmelting() {
        INSTANCE = this;
        for (OrePrefixes tPrefix : this.mSmeltingPrefixes) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        if (MU.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) return;

        GTModHandler.removeFurnaceSmelting(stack);

        if (MU.hasFlag(material, GTMaterialFlag.NO_SMELTING)) return;

        // Blast furnace is required for processing this ore.
        if (material.mBlastFurnaceRequired || MU.directSmelting(material).mBlastFurnaceRequired) {
            if (MU.blastFurnaceTemp(material) >= 1000) return;
            if (MU.blastFurnaceTemp(MU.directSmelting(material)) >= 1000) return;
            if (!material.mAutoGenerateBlastFurnaceRecipes) return;

            final ItemStack output = material.getIngots(1);

            if (output == null) return;

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .circuit(1)
                .itemOutputs(output)
                .duration(Math.max(material.getMass() / 4L, 1L) * MU.blastFurnaceTemp(material) * TICKS)
                .eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, MU.blastFurnaceTemp(material))
                .addTo(blastFurnaceRecipes);

            return;
        }

        // Blast furnace is *not* required for processing this ore.
        switch (prefix.getName()) {
            case "crushed", "crushedPurified", "crushedCentrifuged" -> {
                if (MU.directSmelting(material) == material) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 10);
                } else if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 6);
                } else {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                }
            }
            case "dust" -> {
                if (MU.directSmelting(material) == material) {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                } else if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 6);
                } else {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                }

                if (MU.directSmelting(material) == material) return;

                final int outputAmount = GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;

                if (!MU.hasFlag(material, GTMaterialFlag.DONT_ADD_DEFAULT_BBF_RECIPE) && MU.directSmelting(material)
                    .getIngots(1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, stack))
                        .itemOutputs(
                            MU.directSmelting(material)
                                .getIngots(outputAmount))
                        .duration(2 * MINUTES)
                        .metadata(ADDITIVE_AMOUNT, 2)
                        .addTo(primitiveBlastRecipes);
                } else {
                    addSpecialDustRecipes(material, outputAmount);
                }

            }
            case "dustImpure", "dustPure", "dustRefined" -> {
                if (MU.directSmelting(material) == material) {
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
                    MU.directSmelting(material)
                        .getIngots(outputAmount),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Ferrosilite,
                        Materials2Shapes.shapeDustSmall,
                        (int) (2 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    material.getDust(2),
                    MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.shapeDust, (int) (2)))
                .itemOutputs(
                    MU.directSmelting(material)
                        .getIngots(outputAmount),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Ferrosilite,
                        Materials2Shapes.shapeDustSmall,
                        (int) (7 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    material.getDust(2),
                    MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (2)))
                .itemOutputs(
                    MU.directSmelting(material)
                        .getIngots(outputAmount),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Ferrosilite,
                        Materials2Shapes.shapeDustSmall,
                        (int) (outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    material.getDust(2),
                    MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapeDust, (int) (2)))
                .itemOutputs(
                    MU.directSmelting(material)
                        .getIngots(outputAmount),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Ferrosilite,
                        Materials2Shapes.shapeDustSmall,
                        (int) (outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    material.getDust(2),
                    MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeDust, (int) (2)))
                .itemOutputs(
                    MU.directSmelting(material)
                        .getIngots(outputAmount),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Ferrosilite,
                        Materials2Shapes.shapeDustSmall,
                        (int) (outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        } else if (material == Materials.Tetrahedrite) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2))
                .itemOutputs(
                    MU.directSmelting(material)
                        .getIngots(outputAmount),
                    MaterialLibAPI
                        .getStack(Materials2Materials.Antimony, Materials2Shapes.shapeNugget, (int) (3 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        } else if (material == Materials.Galena) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getDust(2))
                .itemOutputs(
                    MU.directSmelting(material)
                        .getIngots(outputAmount),
                    MaterialLibAPI
                        .getStack(Materials2Materials.Silver, Materials2Shapes.shapeNugget, (int) (3 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        }
    }

    private static void addSmeltingRecipe(Materials material, ItemStack stack, OrePrefixes prefix, int size) {
        ItemStack smeltingOutput = GTOreDictUnificator.get(prefix, MU.directSmelting(material), size);

        if (smeltingOutput == null) {
            smeltingOutput = MU.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)
                ? GTOreDictUnificator.get(OrePrefixes.gem, MU.directSmelting(material), 1L)
                : GTOreDictUnificator.get(OrePrefixes.ingot, MU.directSmelting(material), 1L);
        }

        if (smeltingOutput != null) GTModHandler.addSmeltingRecipe(stack, smeltingOutput);
    }
}
