package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public class ProcessingIngot implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingIngot() {
        OrePrefixes.ingot.add(this);
        OrePrefixes.ingotDouble.add(this);
        OrePrefixes.ingotTriple.add(this);
        OrePrefixes.ingotQuadruple.add(this);
        OrePrefixes.ingotQuintuple.add(this);
        OrePrefixes.ingotHot.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        boolean aNoSmashing = aMaterial.contains(SubTag.NO_SMASHING);
        boolean aStretchy = aMaterial.contains(SubTag.STRETCHY);
        boolean aNoSmelting = aMaterial.contains(SubTag.NO_SMELTING);
        long aMaterialMass = aMaterial.getMass();
        boolean aSpecialRecipeReq = aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_SMASHING);

        switch (aPrefix) {
            case ingot -> {
                // Fuel recipe
                if (aMaterial.mFuelPower > 0) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GTRecipeConstants.Fuel);
                }
                if (aMaterial.mStandardMoltenFluid != null
                    && !(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                    // Fluid solidifier recipes

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L))
                        .fluidInputs(aMaterial.getMolten(144L))
                        .duration(1 * SECONDS + 12 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(fluidSolidifierRecipes);
                }
                // Reverse recipes
                {
                    GTRecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    GTRecipeRegistrator
                        .registerReverseMacerating(aStack, aMaterial, aPrefix.mMaterialAmount, null, null, null, false);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, aStack),
                            aMaterial,
                            aPrefix.mMaterialAmount,
                            null,
                            null,
                            null);
                    }
                }
                ItemStack tStack = GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L);
                if ((tStack != null) && ((aMaterial.mBlastFurnaceRequired) || aNoSmelting)) {
                    GTModHandler.removeFurnaceSmelting(tStack);
                }
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)
                    && !aMaterial.contains(SubTag.SMELTING_TO_GEM)
                    && aMaterial.contains(SubTag.MORTAR_GRINDABLE)) {
                    GTModHandler.addShapelessCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                        GTProxy.tBits,
                        new Object[] { ToolDictNames.craftingToolMortar, OrePrefixes.ingot.get(aMaterial) });
                }
                if (!aNoSmashing) {
                    // Forge hammer recipes
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV
                        && GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(3, aStack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 2L))
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 16))
                            .addTo(hammerRecipes);
                    }
                }
                if (!aNoSmashing || aStretchy) {

                    // Bender recipes
                    {
                        if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass, 1L))
                                .eut(calculateRecipeEU(aMaterial, 24))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, aStack), GTUtility.getIntegratedCircuit(2))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, aStack), GTUtility.getIntegratedCircuit(3))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 3L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(4, aStack), GTUtility.getIntegratedCircuit(4))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 4L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(5, aStack), GTUtility.getIntegratedCircuit(5))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 5L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, aStack), GTUtility.getIntegratedCircuit(9))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 9L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.foil, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(10))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.foil, aMaterial, 4L))
                                .duration(Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 24))
                                .addTo(benderRecipes);
                        }
                    }
                }
            }
            case ingotDouble -> {
                if (!aNoSmashing || aStretchy) {
                    // bender recipes
                    {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L))
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(benderRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(2, aStack), GTUtility.getIntegratedCircuit(2))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                            .duration(Math.max(aMaterialMass * 2L, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(benderRecipes);
                    }

                    // Enable crafting with hammer if tier is < IV.
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq) {
                        GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.ingotDouble, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "I", "I", "h", 'I', OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotTriple -> {
                if (!aNoSmashing || aStretchy) {
                    // Bender recipes
                    {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L))
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(benderRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(3, aStack), GTUtility.getIntegratedCircuit(3))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L))
                            .duration(Math.max(aMaterialMass * 3L, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(benderRecipes);
                    }

                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq) {
                        GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.ingotTriple, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "I", "B", "h", 'I', OrePrefixes.ingotDouble.get(aMaterial), 'B',
                                OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotQuadruple -> {
                if (!aNoSmashing || aStretchy) {
                    // Bender recipes
                    {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(benderRecipes);
                    }

                    // If tier < IV add manual crafting.
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq) {
                        GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.ingotQuadruple, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "I", "B", "h", 'I', OrePrefixes.ingotTriple.get(aMaterial), 'B',
                                OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotQuintuple -> {
                if (!aNoSmashing || aStretchy) {
                    // Bender recipes
                    {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L))
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(benderRecipes);
                    }

                    // Crafting recipes
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq) {
                        GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.ingotQuintuple, aMaterial, 1L),
                            GTProxy.tBits,
                            new Object[] { "I", "B", "h", 'I', OrePrefixes.ingotQuadruple.get(aMaterial), 'B',
                                OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotHot -> {
                if (aMaterial.mAutoGenerateVacuumFreezerRecipes
                    && GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                    // Vacuum freezer recipes
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L))
                        .duration(((int) Math.max(aMaterialMass * 3L, 1L)) * TICKS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(vacuumFreezerRecipes);
                }
            }
            default -> {}
        }
    }
}
