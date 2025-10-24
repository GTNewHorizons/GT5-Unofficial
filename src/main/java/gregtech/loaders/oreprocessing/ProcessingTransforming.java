package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.polarizerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingTransforming implements IOreRecipeRegistrator {

    public ProcessingTransforming() {
        for (OrePrefixes tPrefix : OrePrefixes.VALUES)
            if (((tPrefix.getMaterialAmount() > 0L) && (!tPrefix.isContainer()) && (!tPrefix.isEnchantable()))
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
                if (GTOreDictUnificator.get(aPrefix, Materials.WoodSealed, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.WoodSealed, 1L))
                        .fluidInputs(
                            Materials.SeedOil
                                .getFluid(GTUtility.translateMaterialToAmount(aPrefix.getMaterialAmount(), 120L, true)))
                        .duration(5 * SECONDS)
                        .eut(TierEU.ULV)
                        .addTo(chemicalBathRecipes);
                }

            }
            case "Iron" -> {
                // Chemical bath recipes
                {
                    if (GTOreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GTUtility.translateMaterialToAmount(aPrefix.getMaterialAmount(), 250L, true)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (GTOreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L))
                            .duration(((int) Math.max(16L, aPrefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "WroughtIron" -> {
                // Chemical bath recipes
                {
                    if (GTOreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GTUtility.translateMaterialToAmount(aPrefix.getMaterialAmount(), 225L, true)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (GTOreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.IronMagnetic, 1L))
                            .duration(((int) Math.max(16L, aPrefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "Steel" -> {
                // Chemical Bath recipes
                {
                    if (GTOreDictUnificator.get(aPrefix, Materials.FierySteel, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.FierySteel, 1L))
                            .fluidInputs(
                                Materials.FierySteel.getFluid(
                                    GTUtility.translateMaterialToAmount(aPrefix.getMaterialAmount(), 200L, true)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (GTOreDictUnificator.get(aPrefix, Materials.SteelMagnetic, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.SteelMagnetic, 1L))
                            .duration(((int) Math.max(16L, aPrefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                            .eut((int) TierEU.LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "Neodymium" ->
            // Polarizer recipes
            {
                if (GTOreDictUnificator.get(aPrefix, Materials.NeodymiumMagnetic, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.NeodymiumMagnetic, 1L))
                        .duration(((int) Math.max(16L, aPrefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                        .eut((int) TierEU.HV / 2)
                        .addTo(polarizerRecipes);
                }
            }
            case "Samarium" ->
            // Polarizer recipes
            {
                if (GTOreDictUnificator.get(aPrefix, Materials.SamariumMagnetic, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.SamariumMagnetic, 1L))
                        .duration(((int) Math.max(16L, aPrefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                        .eut((int) TierEU.IV / 2)
                        .addTo(polarizerRecipes);
                }
            }

            case "TengamPurified" ->
            // Polarizer recipes
            {
                if (GTOreDictUnificator.get(aPrefix, Materials.TengamAttuned, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTOreDictUnificator.get(aPrefix, Materials.TengamAttuned, 1L))
                        .duration(((int) Math.max(16L, aPrefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                        .eut((int) TierEU.RECIPE_UHV)
                        .addTo(polarizerRecipes);
                }
            }

            default -> { /* NO-OP */ }
        }
    }
}
