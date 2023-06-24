package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBenderRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sVacuumRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_RecipeRegistrator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

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
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .noItemOutputs()
                        .noFluidInputs()
                        .noFluidOutputs()
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .duration(0)
                        .eut(0)
                        .addTo(GT_RecipeConstants.Fuel);
                }
                if (aMaterial.mStandardMoltenFluid != null
                    && !(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                    // Fluid solidifier recipes

                    GT_Values.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L))
                        .fluidInputs(aMaterial.getMolten(144L))
                        .noFluidOutputs()
                        .duration(1 * SECONDS + 12 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(sFluidSolidficationRecipes);
                }
                // Reverse recipes
                {
                    GT_RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    GT_RecipeRegistrator
                        .registerReverseMacerating(aStack, aMaterial, aPrefix.mMaterialAmount, null, null, null, false);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GT_RecipeRegistrator.registerReverseArcSmelting(
                            GT_Utility.copyAmount(1L, aStack),
                            aMaterial,
                            aPrefix.mMaterialAmount,
                            null,
                            null,
                            null);
                    }
                }
                ItemStack tStack = GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L);
                if ((tStack != null) && ((aMaterial.mBlastFurnaceRequired) || aNoSmelting)) {
                    GT_ModHandler.removeFurnaceSmelting(tStack);
                }
                if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
                    && !aMaterial.contains(SubTag.NO_WORKING)
                    && !aMaterial.contains(SubTag.SMELTING_TO_GEM)
                    && aMaterial.contains(SubTag.MORTAR_GRINDABLE)
                    && GregTech_API.sRecipeFile.get(ConfigCategories.Tools.mortar, aMaterial.mName, true)) {
                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { ToolDictNames.craftingToolMortar, OrePrefixes.ingot.get(aMaterial) });
                }
                if (!aNoSmashing || aStretchy) {
                    // Forge hammer recipes
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(3L, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 2L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 16))
                            .addTo(sHammerRecipes);
                    }

                    // Bender recipes
                    {
                        if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(Math.max(aMaterialMass, 1L))
                                .eut(calculateRecipeEU(aMaterial, 24))
                                .addTo(sBenderRecipes);
                        }

                        if (GT_OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), GT_Utility.getIntegratedCircuit(2))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(sBenderRecipes);
                        }

                        if (GT_OreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(3L, aStack), GT_Utility.getIntegratedCircuit(3))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(Math.max(aMaterialMass * 3L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(sBenderRecipes);
                        }

                        if (GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(4L, aStack), GT_Utility.getIntegratedCircuit(4))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(Math.max(aMaterialMass * 4L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(sBenderRecipes);
                        }

                        if (GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(5L, aStack), GT_Utility.getIntegratedCircuit(5))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(Math.max(aMaterialMass * 5L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(sBenderRecipes);
                        }

                        if (GT_OreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(9L, aStack), GT_Utility.getIntegratedCircuit(9))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(Math.max(aMaterialMass * 9L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(sBenderRecipes);
                        }

                        if (GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(10))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, 4L))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 24))
                                .addTo(sBenderRecipes);
                        }
                    }
                }
            }
            case ingotDouble -> {
                if (!aNoSmashing || aStretchy) {
                    // bender recipes
                    {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(sBenderRecipes);

                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(2L, aStack), GT_Utility.getIntegratedCircuit(2))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(Math.max(aMaterialMass * 2L, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(sBenderRecipes);
                    }

                    // Enable crafting with hammer if tier is < IV.
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq
                        && GregTech_API.sRecipeFile
                            .get(ConfigCategories.Tools.hammermultiingot, aMaterial.toString(), true)) {
                        GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.ingotDouble, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "I", "I", "h", 'I', OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotTriple -> {
                if (!aNoSmashing || aStretchy) {
                    // Bender recipes
                    {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(sBenderRecipes);

                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(3L, aStack), GT_Utility.getIntegratedCircuit(3))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(Math.max(aMaterialMass * 3L, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(sBenderRecipes);
                    }

                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq
                        && GregTech_API.sRecipeFile
                            .get(ConfigCategories.Tools.hammermultiingot, aMaterial.toString(), true)) {
                        GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.ingotTriple, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "I", "B", "h", 'I', OrePrefixes.ingotDouble.get(aMaterial), 'B',
                                OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotQuadruple -> {
                if (!aNoSmashing || aStretchy) {
                    // Bender recipes
                    {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(sBenderRecipes);
                    }

                    // If tier < IV add manual crafting.
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq
                        && GregTech_API.sRecipeFile
                            .get(ConfigCategories.Tools.hammermultiingot, aMaterial.toString(), true)) {
                        GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.ingotQuadruple, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "I", "B", "h", 'I', OrePrefixes.ingotTriple.get(aMaterial), 'B',
                                OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotQuintuple -> {
                if (!aNoSmashing || aStretchy) {
                    // Bender recipes
                    {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(Math.max(aMaterialMass, 1L))
                            .eut(calculateRecipeEU(aMaterial, 96))
                            .addTo(sBenderRecipes);
                    }

                    // Crafting recipes
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV && aSpecialRecipeReq
                        && GregTech_API.sRecipeFile
                            .get(ConfigCategories.Tools.hammermultiingot, aMaterial.toString(), true)) {
                        GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.ingotQuintuple, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "I", "B", "h", 'I', OrePrefixes.ingotQuadruple.get(aMaterial), 'B',
                                OrePrefixes.ingot.get(aMaterial) });
                    }
                }
            }
            case ingotHot -> {
                if (aMaterial.mAutoGenerateVacuumFreezerRecipes
                    && GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                    // Vacuum freezer recipes
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 3L, 1L)) * TICKS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(sVacuumRecipes);
                }
            }
            default -> {}
        }
    }
}
