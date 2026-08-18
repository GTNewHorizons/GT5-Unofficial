/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material.gtenhancement;

import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public final class PlatinumSludgeRecipes {

    private PlatinumSludgeRecipes() {}

    public static void register() {
        registerHVCircuitSupportRecipe();
        registerProcessRecipes();
    }

    private static void registerProcessRecipes() {
        registerReagentRecipes();
        registerConcentrateFeedRecipes();
        registerPlatinumRecipes();
        registerPalladiumRecipes();
        registerResidueAndRutheniumRecipes();
        registerOsmiumRecipes();
        registerIridiumRecipes();
        registerRhodiumRecipes();
    }

    private static void registerHVCircuitSupportRecipe() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Electrotine, Shapes.dust, 8))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
    }

    private static void registerReagentRecipes() {
        // DilutedSulfuricAcid
        // 2H2SO4 + H2O = 3H2SO4(d)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 2),
                GTOreDictUnificator.get(cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 3))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 2))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // FormicAcid
        // CO + NaOH = CHO2Na
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.CarbonMonoxide, 1),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumFormate, CellShapes.cell, 1))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        // H2SO4 + 2CHO2Na = 2CH2O2 + Na2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumFormate, CellShapes.cell, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.FormicAcid, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 7))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(cell, Materials.Empty, 1),
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 7))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SodiumFormate, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FormicAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // AquaRegia
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3),
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AquaRegia, CellShapes.cell, 4))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3),
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 1))
            .circuit(3)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3))
            .circuit(4)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // AmmoniumChloride
        // NH3 + HCl = NH4Cl
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AmmoniumChloride, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 64_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 64_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, 64_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void registerConcentrateFeedRecipes() {
        // base solution
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            if (ml.hasShape(Shapes.crushedPurified) && materialsContains(ml, Materials.Sulfur)
                && (materialsContains(ml, Materials.Copper) || materialsContains(ml, Materials.Nickel))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.crushedPurified, 1))
                    .circuit(1)
                    .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 300))
                    .fluidOutputs(
                        MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 300))
                    .duration(12 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.crushedPurified, 9))
                    .circuit(9)
                    .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 2_700))
                    .fluidOutputs(
                        MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 2_700))
                    .duration(11 * SECONDS + 5 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialLibAPI.getStack(ml, Shapes.crushedPurified, 9),
                        MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 9))
                    .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
                    .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 20_700))
                    .fluidOutputs(
                        MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 20_700))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);
            }
        }
        registerConcentrateFeedRecipes(Materials.Cooperite);
        registerConcentrateFeedRecipes(Materials.Tetrahedrite);
        registerConcentrateFeedRecipes(Materials.Chalcopyrite);
        registerConcentrateFeedRecipes(Materials.Pentlandite);
    }

    private static void registerConcentrateFeedRecipes(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 300))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 300))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 9))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 2_700))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 2_700))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(crushedPurified, material, 9),
                MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 20_700))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 20_700))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerPlatinumRecipes() {
        // Pt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Platinum, Shapes.nugget, 2))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, MaterialUtils.meltingPoint(Materials.Platinum))
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 18_000))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumConcentrate, CellShapes.cell, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dustTiny, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dustTiny, 4),
                MaterialParts.requireCell(Materials.NitrogenDioxide, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, 400))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, 400))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dustTiny, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dustTiny, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 4_000),
                MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, 400))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dust, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, 36_000),
                MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, 3_600))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, 3_600),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 27_000))
            .duration(700)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 87))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 900)
            .addTo(blastFurnaceRecipes);
        // 2PtCl + Ca = 2Pt + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 3))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerPalladiumRecipes() {
        // Pd
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dustTiny, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dustTiny, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, 9_000))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dust, 4),
                GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 2),
                MaterialParts.requireCell(Materials.Ethylene, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FormicAcid, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 4_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dust, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FormicAcid, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void registerResidueAndRutheniumRecipes() {
        // Na2SO4 + 2H = 2Na + H2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 7),
                MaterialParts.requireCell(Materials.Hydrogen, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 2),
                GTOreDictUnificator.get(cell, Materials.Empty, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        // Rh/Os/Ir/Ru
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PotassiumDisulfate,
                    FluidShapes.fluidMolten,
                    (int) (2 * INGOTS + 1 * HALF_INGOTS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.RhodiumSulfate, FluidShapes.fluidLiquid, 360))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        // Ru
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 10))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SodiumRuthenate, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialUtils.gas(Materials.Steam, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumRuthenate, Shapes.dust, 6),
                MaterialParts.requireCell(Materials.Chlorine, 3))
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.RutheniumTetroxideSolution, FluidShapes.fluidLiquid, 9_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.RutheniumTetroxideSolution, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.HotRutheniumTetroxideSolution, FluidShapes.fluidLiquid, 2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);
        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HotRutheniumTetroxideSolution, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(
                GTUtility.getWater(1_800),
                MaterialLibAPI.getFluidStack(Materials.RutheniumTetroxide, FluidShapes.fluidLiquid, 7_200))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RutheniumTetroxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Chlorine, 6))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerOsmiumRecipes() {
        // Os
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, 2))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 500))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AcidicOsmiumSolution, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AcidicOsmiumSolution, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.OsmiumSolution, FluidShapes.fluidLiquid, 100),
                GTUtility.getWater(900))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.OsmiumSolution, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Chlorine, 7))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerIridiumRecipes() {
        // Ir
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SludgeDustResidue, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.IridiumDioxide, Shapes.dust, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.IridiumDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AcidicIridiumSolution, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AcidicIridiumSolution, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.AmmoniumChloride, CellShapes.cell, 3))
            .itemOutputs(
                GTOreDictUnificator.get(cell, Materials.Empty, 4),
                MaterialLibAPI.getStack(Materials.IridiumChloride, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.IridiumChloride, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MetallicSludgeDustResidue, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CalciumChloride, FluidShapes.fluidLiquid, 3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);
    }

    private static void registerRhodiumRecipes() {
        // Rh
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumSulfate, CellShapes.cell, 11))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RhodiumSulfateSolution, CellShapes.cell, 11),
                MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dustTiny, 10))
            .fluidInputs(GTUtility.getWater(10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Potassium, FluidShapes.fluidMolten, 2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, 4))
            .fluidInputs(
                GTUtility.getWater(36_000),
                MaterialLibAPI.getFluidStack(Materials.RhodiumSulfate, FluidShapes.fluidLiquid, 39_600))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Potassium, FluidShapes.fluidMolten, (int) (50 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.RhodiumSulfateSolution, FluidShapes.fluidLiquid, 39_600))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ZincSulfate, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.CrudeRhodiumMetal, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.RhodiumSulfateSolution, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CrudeRhodiumMetal, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.RhodiumSalt, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 600)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumSalt, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.RhodiumSaltSolution, FluidShapes.fluidLiquid, 200))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 5))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RhodiumNitrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.RhodiumSaltSolution, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        // Na + HNO3 = NaNO3 + H
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(8 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(UniversalChemical);
        // NaOH + HNO3 = NaNO3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxide, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .circuit(7)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumNitrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.RhodiumFilterCakeSolution, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.ReprecipitatedRhodium, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.RhodiumFilterCakeSolution, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ReprecipitatedRhodium, Shapes.dust, 1),
                GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Ammonia, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static boolean materialsContains(Material one, Material other) {
        if (one == null) return false;
        for (MaterialStack stack : MaterialUtils.materialList(one)) if (stack.mMaterial == other) return true;
        return false;
    }
}
