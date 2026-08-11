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

import static bartworks.system.material.WerkstoffLoader.AcidicIridiumSolution;
import static bartworks.system.material.WerkstoffLoader.AcidicOsmiumSolution;
import static bartworks.system.material.WerkstoffLoader.AmmoniumChloride;
import static bartworks.system.material.WerkstoffLoader.AquaRegia;
import static bartworks.system.material.WerkstoffLoader.CalciumChloride;
import static bartworks.system.material.WerkstoffLoader.CrudeRhMetall;
import static bartworks.system.material.WerkstoffLoader.FormicAcid;
import static bartworks.system.material.WerkstoffLoader.HotRutheniumTetroxideSollution;
import static bartworks.system.material.WerkstoffLoader.IrLeachResidue;
import static bartworks.system.material.WerkstoffLoader.IrOsLeachResidue;
import static bartworks.system.material.WerkstoffLoader.IridiumChloride;
import static bartworks.system.material.WerkstoffLoader.IridiumDioxide;
import static bartworks.system.material.WerkstoffLoader.LeachResidue;
import static bartworks.system.material.WerkstoffLoader.OsmiumSolution;
import static bartworks.system.material.WerkstoffLoader.PDAmmonia;
import static bartworks.system.material.WerkstoffLoader.PDMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.PDRawPowder;
import static bartworks.system.material.WerkstoffLoader.PDSalt;
import static bartworks.system.material.WerkstoffLoader.PGSDResidue;
import static bartworks.system.material.WerkstoffLoader.PGSDResidue2;
import static bartworks.system.material.WerkstoffLoader.PTConcentrate;
import static bartworks.system.material.WerkstoffLoader.PTMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.PTRawPowder;
import static bartworks.system.material.WerkstoffLoader.PTResidue;
import static bartworks.system.material.WerkstoffLoader.PTSaltCrude;
import static bartworks.system.material.WerkstoffLoader.PTSaltRefined;
import static bartworks.system.material.WerkstoffLoader.PotassiumDisulfate;
import static bartworks.system.material.WerkstoffLoader.RHFilterCakeSolution;
import static bartworks.system.material.WerkstoffLoader.RHNitrate;
import static bartworks.system.material.WerkstoffLoader.RHSalt;
import static bartworks.system.material.WerkstoffLoader.RHSaltSolution;
import static bartworks.system.material.WerkstoffLoader.RHSulfate;
import static bartworks.system.material.WerkstoffLoader.RHSulfateSolution;
import static bartworks.system.material.WerkstoffLoader.ReRh;
import static bartworks.system.material.WerkstoffLoader.RhFilterCake;
import static bartworks.system.material.WerkstoffLoader.Rhodium;
import static bartworks.system.material.WerkstoffLoader.Ruthenium;
import static bartworks.system.material.WerkstoffLoader.RutheniumTetroxide;
import static bartworks.system.material.WerkstoffLoader.RutheniumTetroxideSollution;
import static bartworks.system.material.WerkstoffLoader.SodiumNitrate;
import static bartworks.system.material.WerkstoffLoader.SodiumRuthenate;
import static bartworks.system.material.WerkstoffLoader.Sodiumformate;
import static bartworks.system.material.WerkstoffLoader.Sodiumsulfate;
import static bartworks.system.material.WerkstoffLoader.ZincSulfate;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustTiny;
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

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

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
            .itemInputs(Materials.Redstone.getDust(1), Materials.Electrum.getDust(1))
            .circuit(1)
            .itemOutputs(Materials.Electrotine.getDust(8))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
    }

    private static void registerReagentRecipes() {
        // DilutedSulfuricAcid
        // 2H2SO4 + H2O = 3H2SO4(d)
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SulfuricAcid.getCells(2), Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.DilutedSulfuricAcid.getCells(3))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.SulfuricAcid.getFluid(2_000))
            .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(3_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SulfuricAcid.getCells(2))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(3_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // FormicAcid
        // CO + NaOH = CHO2Na
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CarbonMonoxide.getCells(1), Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(Sodiumformate.get(cell))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        // H2SO4 + 2CHO2Na = 2CH2O2 + Na2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(Sodiumformate.get(cell, 2))
            .circuit(1)
            .itemOutputs(FormicAcid.get(cell, 2), Sodiumsulfate.get(dust, 7))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SulfuricAcid.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1), Sodiumsulfate.get(dust, 7))
            .fluidInputs(Sodiumformate.getFluidOrGas(2_000))
            .fluidOutputs(FormicAcid.getFluidOrGas(2_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // AquaRegia
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(3), Materials.NitricAcid.getCells(1))
            .circuit(1)
            .itemOutputs(AquaRegia.get(cell, 4))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(3), Materials.NitricAcid.getCells(1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(4))
            .fluidOutputs(AquaRegia.getFluidOrGas(4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.NitricAcid.getCells(1))
            .circuit(3)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3_000))
            .fluidOutputs(AquaRegia.getFluidOrGas(4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(3))
            .circuit(4)
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(Materials.NitricAcid.getFluid(1_000))
            .fluidOutputs(AquaRegia.getFluidOrGas(4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // AmmoniumChloride
        // NH3 + HCl = NH4Cl
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ammonia.getCells(1))
            .circuit(1)
            .itemOutputs(AmmoniumChloride.get(cell, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Ammonia.getGas(1_000))
            .fluidOutputs(AmmoniumChloride.getFluidOrGas(1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(Materials.Ammonia.getGas(64_000), Materials.HydrochloricAcid.getFluid(64_000))
            .fluidOutputs(AmmoniumChloride.getFluidOrGas(64_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void registerConcentrateFeedRecipes() {
        // base solution
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (werkstoff.containsStuff(Materials.Sulfur)
                && (werkstoff.containsStuff(Materials.Copper) || werkstoff.containsStuff(Materials.Nickel))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(crushedPurified))
                    .circuit(1)
                    .fluidInputs(AquaRegia.getFluidOrGas(300))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(300))
                    .duration(12 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);
                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(crushedPurified, 9))
                    .circuit(9)
                    .fluidInputs(AquaRegia.getFluidOrGas(2_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(2_700))
                    .duration(11 * SECONDS + 5 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);
                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(crushedPurified, 9), PTMetallicPowder.get(dust, 9))
                    .itemOutputs(PTResidue.get(dust))
                    .fluidInputs(AquaRegia.getFluidOrGas(20_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(20_700))
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

    private static void registerConcentrateFeedRecipes(Materials material) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 1))
            .circuit(1)
            .fluidInputs(AquaRegia.getFluidOrGas(300))
            .fluidOutputs(PTConcentrate.getFluidOrGas(300))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 9))
            .circuit(9)
            .fluidInputs(AquaRegia.getFluidOrGas(2_700))
            .fluidOutputs(PTConcentrate.getFluidOrGas(2_700))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 9), PTMetallicPowder.get(dust, 9))
            .itemOutputs(PTResidue.get(dust))
            .fluidInputs(AquaRegia.getFluidOrGas(20_700))
            .fluidOutputs(PTConcentrate.getFluidOrGas(20_700))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerPlatinumRecipes() {
        // Pt
        GTValues.RA.stdBuilder()
            .itemInputs(PTMetallicPowder.get(dust, 3))
            .circuit(1)
            .itemOutputs(Materials.Platinum.getNuggets(2))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, Materials.Platinum.mMeltingPoint)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(PTMetallicPowder.get(dust))
            .circuit(1)
            .itemOutputs(PTResidue.get(dustTiny))
            .fluidInputs(AquaRegia.getFluidOrGas(2_000))
            .fluidOutputs(PTConcentrate.getFluidOrGas(2_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(PTMetallicPowder.get(dust, 9))
            .circuit(9)
            .itemOutputs(PTResidue.get(dust))
            .fluidInputs(AquaRegia.getFluidOrGas(18_000))
            .fluidOutputs(PTConcentrate.getFluidOrGas(18_000))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(PTConcentrate.get(cell, 4))
            .itemOutputs(
                PTSaltCrude.get(dustTiny, 16),
                PTRawPowder.get(dustTiny, 4),
                Materials.NitrogenDioxide.getCells(1),
                Materials.HydrochloricAcid.getCells(3))
            .fluidInputs(AmmoniumChloride.getFluidOrGas(400))
            .fluidOutputs(PDAmmonia.getFluidOrGas(400))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(PTSaltCrude.get(dustTiny, 16), PTRawPowder.get(dustTiny, 4))
            .fluidInputs(PTConcentrate.getFluidOrGas(4_000), AmmoniumChloride.getFluidOrGas(400))
            .fluidOutputs(
                PDAmmonia.getFluidOrGas(400),
                Materials.NitrogenDioxide.getGas(1_000),
                Materials.HydrochloricAcid.getFluid(3_000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(PTSaltCrude.get(dust, 16), PTRawPowder.get(dust, 4))
            .fluidInputs(PTConcentrate.getFluidOrGas(36_000), AmmoniumChloride.getFluidOrGas(3_600))
            .fluidOutputs(
                PDAmmonia.getFluidOrGas(3_600),
                Materials.NitrogenDioxide.getGas(9_000),
                Materials.HydrochloricAcid.getFluid(27_000))
            .duration(700)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(PTSaltCrude.get(dust))
            .itemOutputs(
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(PTSaltRefined.get(dust))
            .circuit(1)
            .itemOutputs(PTMetallicPowder.get(dust))
            .fluidOutputs(Materials.Chlorine.getGas(87))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 900)
            .addTo(blastFurnaceRecipes);
        // 2PtCl + Ca = 2Pt + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(PTRawPowder.get(dust, 4), Materials.Calcium.getDust(1))
            .itemOutputs(Materials.Platinum.getDust(2), CalciumChloride.get(dust, 3))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerPalladiumRecipes() {
        // Pd
        GTValues.RA.stdBuilder()
            .itemInputs(PDMetallicPowder.get(dust))
            .circuit(1)
            .fluidInputs(Materials.Ammonia.getGas(1_000))
            .fluidOutputs(PDAmmonia.getFluidOrGas(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(PDMetallicPowder.get(dust))
            .circuit(1)
            .itemOutputs(PDSalt.get(dustTiny, 16), PDRawPowder.get(dustTiny, 2))
            .fluidInputs(PDAmmonia.getFluidOrGas(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(PDMetallicPowder.get(dust, 9))
            .circuit(9)
            .itemOutputs(PDSalt.get(dust, 16), PDRawPowder.get(dust, 2))
            .fluidInputs(PDAmmonia.getFluidOrGas(9_000))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(PDSalt.get(dust))
            .fluidInputs(PDAmmonia.getFluidOrGas(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(PDSalt.get(dust))
            .itemOutputs(
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(PDRawPowder.get(dust, 4), Materials.Empty.getCells(1))
            .itemOutputs(Materials.Palladium.getDust(2), Materials.Ethylene.getCells(1))
            .fluidInputs(FormicAcid.getFluidOrGas(4_000))
            .fluidOutputs(Materials.Ammonia.getGas(4_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(PDRawPowder.get(dust, 4))
            .itemOutputs(Materials.Palladium.getDust(2))
            .fluidInputs(FormicAcid.getFluidOrGas(4_000))
            .fluidOutputs(
                Materials.Ammonia.getGas(4_000),
                Materials.Ethylene.getGas(1_000),
                Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void registerResidueAndRutheniumRecipes() {
        // Na2SO4 + 2H = 2Na + H2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(Sodiumsulfate.get(dust, 7), Materials.Hydrogen.getCells(2))
            .itemOutputs(Materials.Sodium.getDust(2), Materials.Empty.getCells(2))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(1_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        // Rh/Os/Ir/Ru
        GTValues.RA.stdBuilder()
            .itemInputs(PTResidue.get(dust))
            .circuit(11)
            .itemOutputs(LeachResidue.get(dust))
            .fluidInputs(PotassiumDisulfate.getMolten(2 * INGOTS + 1 * HALF_INGOTS))
            .fluidOutputs(RHSulfate.getFluidOrGas(360))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        // Ru
        GTValues.RA.stdBuilder()
            .itemInputs(LeachResidue.get(dust, 10), Materials.Saltpeter.getDust(10))
            .itemOutputs(SodiumRuthenate.get(dust, 3), IrOsLeachResidue.get(dust, 6))
            .fluidInputs(Materials.SaltWater.getFluid(1_000))
            .fluidOutputs(Materials.Steam.getGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(SodiumRuthenate.get(dust, 6), Materials.Chlorine.getCells(3))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidOutputs(RutheniumTetroxideSollution.getFluidOrGas(9_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .fluidInputs(RutheniumTetroxideSollution.getFluidOrGas(1_000))
            .fluidOutputs(HotRutheniumTetroxideSollution.getFluidOrGas(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);
        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Salt.getDust(6))
            .fluidInputs(HotRutheniumTetroxideSollution.getFluidOrGas(9_000))
            .fluidOutputs(Materials.Water.getFluid(1_800), RutheniumTetroxide.getFluidOrGas(7_200))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(RutheniumTetroxide.get(dust, 1), Materials.HydrochloricAcid.getCells(6))
            .itemOutputs(Ruthenium.get(dust), Materials.Chlorine.getCells(6))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerOsmiumRecipes() {
        // Os
        GTValues.RA.stdBuilder()
            .itemInputs(IrOsLeachResidue.get(dust, 2))
            .circuit(11)
            .itemOutputs(IrLeachResidue.get(dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(500))
            .fluidOutputs(AcidicOsmiumSolution.getFluidOrGas(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(AcidicOsmiumSolution.getFluidOrGas(1_000))
            .fluidOutputs(OsmiumSolution.getFluidOrGas(100), Materials.Water.getFluid(900))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(OsmiumSolution.get(cell), Materials.HydrochloricAcid.getCells(6))
            .itemOutputs(Materials.Osmium.getDust(1), Materials.Chlorine.getCells(7))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void registerIridiumRecipes() {
        // Ir
        GTValues.RA.stdBuilder()
            .itemInputs(IrLeachResidue.get(dust))
            .circuit(1)
            .itemOutputs(PGSDResidue.get(dust), IridiumDioxide.get(dust))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(IridiumDioxide.get(dust), Materials.HydrochloricAcid.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidOutputs(AcidicIridiumSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(AcidicIridiumSolution.get(cell), AmmoniumChloride.get(cell, 3))
            .itemOutputs(Materials.Empty.getCells(4), IridiumChloride.get(dust))
            .fluidOutputs(Materials.Ammonia.getGas(3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(IridiumChloride.get(dust), Materials.Calcium.getDust(1))
            .itemOutputs(PGSDResidue2.get(dust), Materials.Iridium.getDust(1))
            .fluidOutputs(CalciumChloride.getFluidOrGas(3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);
    }

    private static void registerRhodiumRecipes() {
        // Rh
        GTValues.RA.stdBuilder()
            .itemInputs(RHSulfate.get(cell, 11))
            .circuit(1)
            .itemOutputs(RHSulfateSolution.get(cell, 11), LeachResidue.get(dustTiny, 10))
            .fluidInputs(Materials.Water.getFluid(10_000))
            .fluidOutputs(Materials.Potassium.getMolten(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(LeachResidue.get(dust, 4))
            .fluidInputs(Materials.Water.getFluid(36_000), RHSulfate.getFluidOrGas(39_600))
            .fluidOutputs(Materials.Potassium.getMolten(50 * INGOTS), RHSulfateSolution.getFluidOrGas(39_600))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Zinc.getDust(1))
            .itemOutputs(ZincSulfate.get(dust, 6), CrudeRhMetall.get(dust))
            .fluidInputs(RHSulfateSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(CrudeRhMetall.get(dust), Materials.Salt.getDust(1))
            .itemOutputs(RHSalt.get(dust, 3))
            .fluidInputs(Materials.Chlorine.getGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 600)
            .addTo(blastFurnaceRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(RHSalt.get(dust, 1))
            .fluidInputs(Materials.Water.getFluid(200))
            .fluidOutputs(RHSaltSolution.getFluidOrGas(200))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(SodiumNitrate.get(dust, 5))
            .circuit(1)
            .itemOutputs(RHNitrate.get(dust), Materials.Salt.getDust(2))
            .fluidInputs(RHSaltSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        // Na + HNO3 = NaNO3 + H
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(1))
            .itemOutputs(SodiumNitrate.get(dust, 5))
            .fluidInputs(Materials.NitricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Hydrogen.getGas(1_000))
            .duration(8 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(RHNitrate.get(dust))
            .itemOutputs(
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(RhFilterCake.get(dust))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(RHFilterCakeSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(ReRh.get(dust))
            .fluidInputs(RHFilterCakeSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
        GTValues.RA.stdBuilder()
            .itemInputs(ReRh.get(dust), Materials.Empty.getCells(1))
            .itemOutputs(Rhodium.get(dust), Materials.Ammonia.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Chlorine.getGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }
}
