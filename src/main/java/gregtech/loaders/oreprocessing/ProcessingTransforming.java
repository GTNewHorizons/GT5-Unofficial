package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.polarizerRecipes;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class ProcessingTransforming implements IOreRecipeRegistrator {

    public ProcessingTransforming() {
        for (OrePrefixes tPrefix : OrePrefixes.values())
            if (((tPrefix.mMaterialAmount > 0L) && (!tPrefix.mIsContainer) && (!tPrefix.mIsEnchantable))
                || (tPrefix == OrePrefixes.plank)) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {

        if (aPrefix == OrePrefixes.plank) {
            aPrefix = OrePrefixes.plate;
        }

        switch (aMaterial.mName) {
            case "Wood" ->
            // Chemical bath recipes
            {
                if (OreDictUnificator.get(aPrefix, Materials.WoodSealed, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(OreDictUnificator.get(aPrefix, Materials.WoodSealed, 1L))
                        .fluidInputs(
                            Materials.SeedOil
                                .getFluid(GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 120L, true)))
                        .duration(5 * SECONDS)
                        .eut(TierEU.ULV)
                        .addTo(chemicalBathRecipes);
                }

            }
            case "Iron" -> {
                // Chemical bath recipes
                {
                    if (OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 250L, true)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L))
                            .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "WroughtIron" -> {
                // Chemical bath recipes
                {
                    if (OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 225L, true)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L))
                            .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "Steel" -> {
                // Chemical Bath recipes
                {
                    if (OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 200L, true)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (OreDictUnificator.get(aPrefix, Materials.SteelMagnetic, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(OreDictUnificator.get(aPrefix, Materials.SteelMagnetic, 1L))
                            .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "Neodymium" ->
            // Polarizer recipes
            {
                if (OreDictUnificator.get(aPrefix, Materials.NeodymiumMagnetic, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(OreDictUnificator.get(aPrefix, Materials.NeodymiumMagnetic, 1L))
                        .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                        .eut((int) TierEU.HV / 2)
                        .addTo(polarizerRecipes);
                }
            }
            case "Samarium" ->
            // Polarizer recipes
            {
                if (OreDictUnificator.get(aPrefix, Materials.SamariumMagnetic, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(OreDictUnificator.get(aPrefix, Materials.SamariumMagnetic, 1L))
                        .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                        .eut((int) TierEU.IV / 2)
                        .addTo(polarizerRecipes);
                }
            }

            case "TengamPurified" ->
            // Polarizer recipes
            {
                if (OreDictUnificator.get(aPrefix, Materials.TengamAttuned, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(OreDictUnificator.get(aPrefix, Materials.TengamAttuned, 1L))
                        .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                        .eut((int) TierEU.RECIPE_UHV)
                        .addTo(polarizerRecipes);
                }
            }

            default -> { /* NO-OP */ }
        }
    }
}
