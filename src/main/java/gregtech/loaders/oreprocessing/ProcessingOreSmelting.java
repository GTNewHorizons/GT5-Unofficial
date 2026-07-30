package gregtech.loaders.oreprocessing;

import static bartworks.system.material.gtenhancement.PlatinumSludgeOutputs.convertSmelting;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
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
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == null) return;

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) return;

        GTModHandler.removeFurnaceSmelting(stack);

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING)) return;

        // Blast furnace is required for processing this ore.
        if (MaterialUtils.blastFurnaceRequired(material)
            || MaterialUtils.blastFurnaceRequired(MaterialUtils.directSmelting(material))) {
            if (MaterialUtils.blastFurnaceTemp(material) >= 1000) return;
            if (MaterialUtils.blastFurnaceTemp(MaterialUtils.directSmelting(material)) >= 1000) return;
            if (!MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) return;

            final ItemStack output = GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L);

            if (output == null) return;

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .circuit(1)
                .itemOutputs(output)
                .duration(
                    Math.max(MaterialUtils.mass(material) / 4L, 1L) * MaterialUtils.blastFurnaceTemp(material) * TICKS)
                .eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, MaterialUtils.blastFurnaceTemp(material))
                .addTo(blastFurnaceRecipes);

            return;
        }

        // Blast furnace is *not* required for processing this ore.
        switch (prefix.getName()) {
            case "crushed", "crushedPurified", "crushedCentrifuged" -> {
                if (MaterialUtils.directSmelting(material) == material) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 10);
                } else if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 6);
                } else {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                }
            }
            case "dust" -> {
                if (MaterialUtils.directSmelting(material) == material) {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                } else if (GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                    addSmeltingRecipe(material, stack, OrePrefixes.nugget, 6);
                } else {
                    addSmeltingRecipe(material, stack, OrePrefixes.ingot, 1);
                }

                if (MaterialUtils.directSmelting(material) == material) return;

                final int outputAmount = GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;

                if (!MaterialUtils.hasFlag(material, GTMaterialFlag.DONT_ADD_DEFAULT_BBF_RECIPE)
                    && GTOreDictUnificator.get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, stack))
                        .itemOutputs(
                            GTOreDictUnificator
                                .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), outputAmount))
                        .duration(2 * MINUTES)
                        .metadata(ADDITIVE_AMOUNT, 2)
                        .addTo(primitiveBlastRecipes);
                } else {
                    addSpecialDustRecipes(material, outputAmount);
                }

            }
            case "dustImpure", "dustPure", "dustRefined" -> {
                if (MaterialUtils.directSmelting(material) == material) {
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

    private static void addSpecialDustRecipes(Material material, int outputAmount) {
        if (material == Materials.Chalcopyrite) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 2L), new ItemStack(Blocks.sand, 2))
                .itemOutputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), (long) outputAmount),
                    MaterialLibAPI.getStack(Materials.Ferrosilite, Shapes.dustSmall, (int) (2 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, material, 2L),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dust, 2))
                .itemOutputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), (long) outputAmount),
                    MaterialLibAPI.getStack(Materials.Ferrosilite, Shapes.dustSmall, (int) (7 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, material, 2L),
                    MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 2))
                .itemOutputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), (long) outputAmount),
                    MaterialLibAPI.getStack(Materials.Ferrosilite, Shapes.dustSmall, (int) (outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, material, 2L),
                    MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 2))
                .itemOutputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), (long) outputAmount),
                    MaterialLibAPI.getStack(Materials.Ferrosilite, Shapes.dustSmall, (int) (outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, material, 2L),
                    MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, 2))
                .itemOutputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), (long) outputAmount),
                    MaterialLibAPI.getStack(Materials.Ferrosilite, Shapes.dustSmall, (int) (outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        } else if (material == Materials.Tetrahedrite) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 2L))
                .itemOutputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), (long) outputAmount),
                    MaterialLibAPI.getStack(Materials.Antimony, Shapes.nugget, (int) (3 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        } else if (material == Materials.Galena) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 2L))
                .itemOutputs(
                    GTOreDictUnificator
                        .get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), (long) outputAmount),
                    MaterialLibAPI.getStack(Materials.Silver, Shapes.nugget, (int) (3 * outputAmount)))
                .duration(2 * MINUTES)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(primitiveBlastRecipes);
        }
    }

    private static void addSmeltingRecipe(Material material, ItemStack stack, OrePrefixes prefix, int size) {
        ItemStack smeltingOutput = GTOreDictUnificator.get(prefix, MaterialUtils.directSmelting(material), size);

        if (smeltingOutput == null) {
            smeltingOutput = MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)
                ? GTOreDictUnificator.get(OrePrefixes.gem, MaterialUtils.directSmelting(material), 1L)
                : GTOreDictUnificator.get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), 1L);
        }

        if (smeltingOutput != null) GTModHandler.addSmeltingRecipe(
            stack,
            convertSmelting(material, GTOreDictUnificator.getAssociation(stack).mPrefix, smeltingOutput));
    }
}
