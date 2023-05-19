package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sPolarizerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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
                if (GT_OreDictUnificator.get(aPrefix, Materials.WoodSealed, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.WoodSealed, 1L))
                        .fluidInputs(
                            Materials.SeedOil
                                .getFluid(GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 120L, true)))
                        .noFluidOutputs()
                        .duration(5 * SECONDS)
                        .eut(TierEU.ULV)
                        .addTo(sChemicalBathRecipes);
                }

            }
            case "Iron" -> {
                // Chemical bath recipes
                {
                    if (GT_OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 250L, true)))
                            .noFluidOutputs()
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(sChemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (GT_OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(sPolarizerRecipes);
                    }
                }
            }
            case "WroughtIron" -> {
                // Chemical bath recipes
                {
                    if (GT_OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 225L, true)))
                            .noFluidOutputs()
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(sChemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (GT_OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(sPolarizerRecipes);
                    }
                }
            }
            case "Steel" -> {
                // Chemical Bath recipes
                {
                    if (GT_OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GT_Utility.translateMaterialToAmount(aPrefix.mMaterialAmount, 200L, true)))
                            .noFluidOutputs()
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(sChemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (GT_OreDictUnificator.get(aPrefix, Materials.SteelMagnetic, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.SteelMagnetic, 1L))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(sPolarizerRecipes);
                    }
                }
            }
            case "Neodymium" ->
            // Polarizer recipes
            {
                if (GT_OreDictUnificator.get(aPrefix, Materials.NeodymiumMagnetic, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.NeodymiumMagnetic, 1L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                        .eut((int) TierEU.HV / 2)
                        .addTo(sPolarizerRecipes);
                }
            }
            case "Samarium" ->
            // Polarizer recipes
            {
                if (GT_OreDictUnificator.get(aPrefix, Materials.SamariumMagnetic, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.SamariumMagnetic, 1L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                        .eut((int) TierEU.IV / 2)
                        .addTo(sPolarizerRecipes);
                }
            }

            case "TengamPurified" ->
            // Polarizer recipes
            {
                if (GT_OreDictUnificator.get(aPrefix, Materials.TengamAttuned, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_OreDictUnificator.get(aPrefix, Materials.TengamAttuned, 1L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(16L, aPrefix.mMaterialAmount * 128L / GT_Values.M)) * TICKS)
                        .eut((int) TierEU.RECIPE_UHV)
                        .addTo(sPolarizerRecipes);
                }
            }

            default -> { /* NO-OP */ }
        }
    }
}
