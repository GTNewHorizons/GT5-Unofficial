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
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedCentrifuged;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustRefined;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.rawOre;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.replicatorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;
import static gtPlusPlus.core.material.MaterialsAlloy.HELICOPTER;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.WHITE_METAL;
import static kubatech.loaders.HTGRLoader.HTGRRecipes;
import static kubatech.loaders.HTGRLoader.HTGR_ITEM;
import static tectech.recipe.TecTechRecipeMaps.eyeOfHarmonyRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;

import bartworks.MainMod;
import bartworks.system.material.BWMetaGeneratedItems;
import bartworks.system.material.Werkstoff;
import bartworks.util.BWUtil;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.items.GTGenericItem;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.GTBlockOre;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.item.base.BaseItemComponent;

public class PlatinumSludgeOverHaul {

    private static final Materials[] BLACKLIST = { Materials.HSSS, Materials.EnderiumBase, Materials.Osmiridium,
        Materials.TPV, Materials.get("Uraniumtriplatinid"), Materials.get("Tetranaquadahdiindiumhexaplatiumosminid"),
        Materials.get("Longasssuperconductornameforuvwire"), };
    private static final OrePrefixes[] OPBLACKLIST = { crushedCentrifuged, crushed, crushedPurified, dustPure,
        dustImpure, dustRefined, dust, dustTiny, dustSmall };

    private PlatinumSludgeOverHaul() {}

    private static void runHelperrecipes() {
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

        // base solution
        for (Werkstoff w : Werkstoff.werkstoffHashSet) if (w.containsStuff(Materials.Sulfur)
            && (w.containsStuff(Materials.Copper) || w.containsStuff(Materials.Nickel))) {

                GTValues.RA.stdBuilder()
                    .itemInputs(w.get(crushedPurified))
                    .circuit(1)
                    .fluidInputs(AquaRegia.getFluidOrGas(300))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(300))
                    .duration(12 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(w.get(crushedPurified, 9))
                    .circuit(9)
                    .fluidInputs(AquaRegia.getFluidOrGas(2_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(2_700))
                    .duration(11 * SECONDS + 5 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(w.get(crushedPurified, 9), PTMetallicPowder.get(dust, 9))
                    .itemOutputs(PTResidue.get(dust))
                    .fluidInputs(AquaRegia.getFluidOrGas(20_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(20_700))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

            }
        for (Materials m : Materials.values()) if (PlatinumSludgeOverHaul.materialsContains(m, Materials.Sulfur)
            && (PlatinumSludgeOverHaul.materialsContains(m, Materials.Copper)
                || PlatinumSludgeOverHaul.materialsContains(m, Materials.Nickel))) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTOreDictUnificator.get(crushedPurified, m, 1))
                        .circuit(1)
                        .fluidInputs(AquaRegia.getFluidOrGas(300))
                        .fluidOutputs(PTConcentrate.getFluidOrGas(300))
                        .duration(12 * SECONDS + 10 * TICKS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(UniversalChemical);

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTOreDictUnificator.get(crushedPurified, m, 9))
                        .circuit(9)
                        .fluidInputs(AquaRegia.getFluidOrGas(2_700))
                        .fluidOutputs(PTConcentrate.getFluidOrGas(2_700))
                        .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(UniversalChemical);

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTOreDictUnificator.get(crushedPurified, m, 9), PTMetallicPowder.get(dust, 9))
                        .itemOutputs(PTResidue.get(dust))
                        .fluidInputs(AquaRegia.getFluidOrGas(20_700))
                        .fluidOutputs(PTConcentrate.getFluidOrGas(20_700))
                        .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(UniversalChemical);

                }
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
            .itemInputs(PTConcentrate.get(cell, 8))
            .itemOutputs(
                PTSaltCrude.get(dustTiny, 32),
                PTRawPowder.get(dustTiny, 8),
                Materials.NitrogenDioxide.getCells(2),
                Materials.HydrochloricAcid.getCells(6))
            .fluidInputs(AmmoniumChloride.getFluidOrGas(800))
            .fluidOutputs(PDAmmonia.getFluidOrGas(800))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(PTSaltCrude.get(dustTiny, 32), PTRawPowder.get(dustTiny, 8))
            .fluidInputs(PTConcentrate.getFluidOrGas(8_000), AmmoniumChloride.getFluidOrGas(800))
            .fluidOutputs(
                PDAmmonia.getFluidOrGas(800),
                Materials.NitrogenDioxide.getGas(2_000),
                Materials.HydrochloricAcid.getFluid(6_000))
            .duration(1200)
            .eut(30)
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
            .eut(480)
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
            .circuit(1)
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
            .eut(60)
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

    private static boolean materialsContains(Materials one, ISubTagContainer other) {
        if (one == null || one.mMaterialList == null || one.mMaterialList.isEmpty()) return false;
        for (MaterialStack stack : one.mMaterialList) if (stack.mMaterial.equals(other)) return true;
        return false;
    }

    public static boolean isMapIgnored(RecipeMap<?> map) {
        return map == fusionRecipes || map == unpackagerRecipes
            || map == packagerRecipes
            || map == replicatorRecipes
            || map == eyeOfHarmonyRecipes
            || map == quantumForceTransformerRecipes
            || map == fluidExtractionRecipes
            || map == alloyBlastSmelterRecipes
            || map == HTGRRecipes
            || map == vacuumFurnaceRecipes;
    }

    public static String displayRecipe(GTRecipe recipe) {
        StringBuilder result = new StringBuilder();
        // item inputs
        result.append("Item inputs: ");
        for (ItemStack itemstack : recipe.mInputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid inputs
        result.append(" Fluid inputs: ");
        for (FluidStack fluidStack : recipe.mFluidInputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // item outputs
        result.append(" Item outputs: ");
        for (ItemStack itemstack : recipe.mOutputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid outputs
        result.append(" Fluid outputs: ");
        for (FluidStack fluidStack : recipe.mFluidOutputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        return result.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void replacePureElements() {
        // Cache the ItemList.values() call
        final ItemList[] values = ItemList.values();
        final ArrayList<ItemStack> ITEMLIST_VALUES = new ArrayList<>(values.length);
        for (ItemList e : values) {
            if (e.hasBeenSet()) {
                ITEMLIST_VALUES.add(e.get(1));
            }
        }

        // furnace
        for (Object entry : FurnaceRecipes.smelting()
            .getSmeltingList()
            .entrySet()) {
            Map.Entry realEntry = (Map.Entry) entry;
            if (!GTUtility.isStackValid(realEntry.getKey())) continue;
            if (!BWUtil.checkStackAndPrefix((ItemStack) realEntry.getKey())) continue;

            ItemData association = GTOreDictUnificator.getAssociation((ItemStack) realEntry.getKey());
            boolean isDust = dust.equals(association.mPrefix) || dustTiny.equals(association.mPrefix);
            ItemStack stack = (ItemStack) realEntry.getValue();
            if (isDust && association.mMaterial.mMaterial.equals(Materials.Platinum)) continue;

            if (!GTUtility.isStackValid(stack)) continue;
            if (!BWUtil.checkStackAndPrefix(stack)) continue;

            ItemData ass = GTOreDictUnificator.getAssociation(stack);
            OrePrefixes prefix = ass.mPrefix == nugget ? dustTiny : dust;
            boolean isPlatinumOrPalladium = ass.mMaterial.mMaterial.equals(Materials.Platinum)
                || ass.mMaterial.mMaterial.equals(Materials.Palladium);

            if (!isPlatinumOrPalladium) continue;

            Werkstoff mat = (ass.mMaterial.mMaterial.equals(Materials.Platinum)) ? PTMetallicPowder : PDMetallicPowder;

            if (PlatinumSludgeOverHaul.isInBlackList((ItemStack) realEntry.getKey(), ITEMLIST_VALUES)) continue;
            realEntry.setValue(mat.get(prefix, stack.stackSize * 2));
        }
        // vanilla crafting
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(PlatinumSludgeOverHaul::setnewMaterialInRecipe);
        // gt crafting
        GTModHandler.sBufferRecipeList.forEach(PlatinumSludgeOverHaul::setnewMaterialInRecipe);
        // gt machines
        maploop: for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            GTLog.err.println("Processing recipmap: " + map.unlocalizedName);
            if (isMapIgnored(map)) continue;

            HashSet<GTRecipe> toDel = new HashSet<>();

            recipeloop: for (GTRecipe recipe : map.getAllRecipes()) {
                if (recipe.mFakeRecipe) continue maploop;
                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                    if (map.equals(multiblockChemicalReactorRecipes) || map.equals(chemicalReactorRecipes)) {
                        if (GTUtility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                            || GTUtility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i])) {
                            toDel.add(recipe);
                            GTLog.err.println("Recipe marked for deletion: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i])) {
                            recipe.mFluidOutputs[i] = AcidicIridiumSolution.getFluidOrGas(1_000);
                            recipe.reloadOwner();
                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i])) {
                            recipe.mFluidOutputs[i] = PTConcentrate.getFluidOrGas(2_000);
                            recipe.reloadOwner();
                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i])) {
                            recipe.mFluidOutputs[i] = AcidicOsmiumSolution.getFluidOrGas(1_000);
                            recipe.reloadOwner();
                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                        }
                    } else if (GTUtility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i])) {
                            GTLog.err.println("Recipe marked for deletion: " + displayRecipe(recipe));
                            toDel.add(recipe);
                        }
                }
                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(recipe.mOutputs[i])) continue;

                    if ((BWUtil.areStacksEqualOrNull(Ruthenium.get(dust), recipe.mOutputs[i])
                        || BWUtil.areStacksEqualOrNull(Ruthenium.get(dustImpure), recipe.mOutputs[i])
                        || BWUtil.areStacksEqualOrNull(Ruthenium.get(dustPure), recipe.mOutputs[i]))
                        && !BWUtil.areStacksEqualOrNull(Ruthenium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs)
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput, ITEMLIST_VALUES)) continue recipeloop;
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                        recipe.mOutputs[i] = LeachResidue.get(dust, amount);
                        recipe.reloadOwner();
                    }
                    if ((BWUtil.areStacksEqualOrNull(Rhodium.get(dust), recipe.mOutputs[i])
                        || BWUtil.areStacksEqualOrNull(Rhodium.get(dustImpure), recipe.mOutputs[i])
                        || BWUtil.areStacksEqualOrNull(Rhodium.get(dustPure), recipe.mOutputs[i]))
                        && !BWUtil.areStacksEqualOrNull(Rhodium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs)
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput, ITEMLIST_VALUES)) continue recipeloop;
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                        recipe.mOutputs[i] = CrudeRhMetall.get(dust, amount);
                        recipe.reloadOwner();
                    }
                    if (!BWUtil.checkStackAndPrefix(recipe.mOutputs[i])) continue;
                    // Pt
                    if (GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                        .equals(Materials.Platinum)) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput, ITEMLIST_VALUES)) continue recipeloop;
                        }
                        if (dust.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                            || dustImpure.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                            || dustPure.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                            recipe.mOutputs[i] = BWUtil.setStackSize(PTMetallicPowder.get(dust), amount * 2);
                            recipe.reloadOwner();
                        } else if (dustSmall.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                            recipe.mOutputs[i] = BWUtil.setStackSize(PTMetallicPowder.get(dustSmall), amount * 2);
                            recipe.reloadOwner();
                        } else if (dustTiny.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                            recipe.mOutputs[i] = BWUtil.setStackSize(PTMetallicPowder.get(dustTiny), amount * 2);
                            recipe.reloadOwner();
                        }
                    } else if (GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                        .equals(Materials.Palladium)) {
                            for (ItemStack mInput : recipe.mInputs) {
                                if (PlatinumSludgeOverHaul.isInBlackList(mInput, ITEMLIST_VALUES)) continue recipeloop;
                            }
                            if (dust.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                || dustImpure.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                || dustPure.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                int amount = recipe.mOutputs[i].stackSize;
                                GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                recipe.mOutputs[i] = BWUtil.setStackSize(PDMetallicPowder.get(dust), amount * 4);
                                recipe.reloadOwner();
                            } else
                                if (dustSmall.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                    int amount = recipe.mOutputs[i].stackSize;
                                    GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                    recipe.mOutputs[i] = BWUtil
                                        .setStackSize(PDMetallicPowder.get(dustSmall), amount * 4);
                                    recipe.reloadOwner();
                                } else if (dustTiny
                                    .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                        int amount = recipe.mOutputs[i].stackSize;
                                        GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                        recipe.mOutputs[i] = BWUtil
                                            .setStackSize(PDMetallicPowder.get(dustTiny), amount * 4);
                                        recipe.reloadOwner();
                                    }
                        } else if (GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                            .equals(Materials.Osmium)) {
                                for (ItemStack mInput : recipe.mInputs) {
                                    if (PlatinumSludgeOverHaul.isInBlackList(mInput, ITEMLIST_VALUES))
                                        continue recipeloop;
                                }
                                if (dust.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                    || dustImpure.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                    || dustPure
                                        .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                    int amount = recipe.mOutputs[i].stackSize;
                                    GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                    recipe.mOutputs[i] = BWUtil.setStackSize(IrOsLeachResidue.get(dust), amount);
                                    recipe.reloadOwner();
                                } else if (dustSmall
                                    .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                        int amount = recipe.mOutputs[i].stackSize;
                                        GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                        recipe.mOutputs[i] = BWUtil
                                            .setStackSize(IrOsLeachResidue.get(dustSmall), amount);
                                        recipe.reloadOwner();
                                    } else if (dustTiny
                                        .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                            int amount = recipe.mOutputs[i].stackSize;
                                            recipe.mOutputs[i] = BWUtil
                                                .setStackSize(IrOsLeachResidue.get(dustTiny), amount);
                                            recipe.reloadOwner();
                                        }
                            } else if (GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                                .equals(Materials.Iridium)) {
                                    for (ItemStack mInput : recipe.mInputs) {
                                        if (PlatinumSludgeOverHaul.isInBlackList(mInput, ITEMLIST_VALUES))
                                            continue recipeloop;
                                    }
                                    if (dust.equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                        || dustImpure
                                            .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                        || dustPure
                                            .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                        int amount = recipe.mOutputs[i].stackSize;
                                        recipe.mOutputs[i] = BWUtil.setStackSize(IrLeachResidue.get(dust), amount);
                                        GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                        recipe.reloadOwner();
                                    } else if (dustSmall
                                        .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                            int amount = recipe.mOutputs[i].stackSize;
                                            recipe.mOutputs[i] = BWUtil
                                                .setStackSize(IrLeachResidue.get(dustSmall), amount);
                                            GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                            recipe.reloadOwner();
                                        } else if (dustTiny
                                            .equals(GTOreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                                int amount = recipe.mOutputs[i].stackSize;
                                                recipe.mOutputs[i] = BWUtil
                                                    .setStackSize(IrLeachResidue.get(dustTiny), amount);
                                                GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                                                recipe.reloadOwner();
                                            }
                                }
                }
            }
            map.getBackend()
                .removeRecipes(toDel);
        }
        // TODO: remove EnderIO recipes

        // fix HV tier
        PlatinumSludgeOverHaul.replaceHVCircuitMaterials();
        // add new recipes
        PlatinumSludgeOverHaul.runHelperrecipes();
    }

    private static void replaceHVCircuitMaterials() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Redstone.getDust(1), Materials.Electrum.getDust(1))
            .circuit(1)
            .itemOutputs(Materials.Electrotine.getDust(8))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTLog.err.println("Processing hv circuit materials (circuit assembler map)");
        for (GTRecipe recipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (recipe.mEUt > 512) continue;
            if (BWUtil.checkStackAndPrefix(recipe.mOutputs[0])) {
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    ItemStack stack = recipe.mInputs[i];
                    ItemData ass = GTOreDictUnificator.getAssociation(stack);
                    if (BWUtil.checkStackAndPrefix(stack) && ass.mMaterial.mMaterial.equals(Materials.Platinum)) {
                        recipe.mInputs[i] = GTOreDictUnificator.get(ass.mPrefix, Materials.BlueAlloy, stack.stackSize);
                        GTLog.err.println("Recipe edited: " + displayRecipe(recipe));
                        recipe.reloadOwner();
                    }
                }
            }
        }
    }

    private static void setnewMaterialInRecipe(IRecipe recipe) {
        ItemStack otpt = recipe.getRecipeOutput();

        if (!(recipe instanceof IRecipeMutableAccess mutableRecipe)) {
            return;
        }

        Object input = mutableRecipe.gt5u$getRecipeInputs();

        if (input == null) {
            return;
        }

        if (GTUtility.areStacksEqual(otpt, Materials.Platinum.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Platinum)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(PTMetallicPowder.get(dust, otpt.stackSize * 2));
        } else if (GTUtility.areStacksEqual(otpt, Materials.Palladium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Palladium)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(PDMetallicPowder.get(dust, otpt.stackSize * 2));
        } else if (GTUtility.areStacksEqual(otpt, Materials.Iridium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Iridium)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(IrLeachResidue.get(dust, otpt.stackSize));
        } else if (GTUtility.areStacksEqual(otpt, Materials.Osmium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Osmium)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(IrOsLeachResidue.get(dust, otpt.stackSize));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static boolean checkRecipe(Object input, Materials mat) {
        if (input instanceof List || input instanceof Object[]) {
            Set lists = new HashSet(), stacks = new HashSet();
            List ip = input instanceof List ? (List) input : new ArrayList();
            Object[] ip2 = input instanceof Object[] ? (Object[]) input : GTValues.emptyObjectArray;

            for (Object o : ip) {
                if (o instanceof List) lists.add(o);
                else if (o instanceof ItemStack) stacks.add(o);
            }
            for (Object o : ip2) {
                if (o instanceof List) lists.add(o);
                else if (o instanceof ItemStack) stacks.add(o);
            }

            for (Object o : lists) {
                if (!((List) o).isEmpty()) stacks.add(((List) o).get(0));
            }

            boolean allSame = false;
            for (Object stack : stacks) {
                if (!(stack instanceof ItemStack)) {
                    allSame = false;
                    break;
                }
                allSame = BWUtil.checkStackAndPrefix((ItemStack) stack)
                    && GTOreDictUnificator.getAssociation((ItemStack) stack).mMaterial.mMaterial.equals(mat);
                if (!allSame) break;
            }
            return allSame;
        }
        return false;
    }

    private static boolean isInBlackList(ItemStack stack, List<ItemStack> ITEMLIST_VALUES) {
        if (stack == null || stack.getItem() instanceof BWMetaGeneratedItems
            || MainMod.MOD_ID.equals(GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId)
            || BartWorksCrossmod.MOD_ID.equals(GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId))
            return true;

        if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(NewHorizonsCoreMod.ID)
            && !stack.getUnlocalizedName()
                .contains("dust")
            && !stack.getUnlocalizedName()
                .contains("Dust"))
            return true;

        if (Block.getBlockFromItem(stack.getItem()) instanceof GTGenericBlock
            && !(Block.getBlockFromItem(stack.getItem()) instanceof GTBlockOre)) return true;

        for (ItemStack itemStack : ITEMLIST_VALUES) {
            if (!BWUtil.checkStackAndPrefix(stack) && GTUtility.areStacksEqual(itemStack, stack, true)) {
                return true;
            }
        }

        if (stack.getItem() instanceof GTGenericItem) {
            if (!BWUtil.checkStackAndPrefix(stack)) return false;
            if (GTOreDictUnificator.getAssociation(stack).mPrefix != rawOre) {
                return !Arrays.asList(PlatinumSludgeOverHaul.OPBLACKLIST)
                    .contains(GTOreDictUnificator.getAssociation(stack).mPrefix)
                    || Arrays.asList(PlatinumSludgeOverHaul.BLACKLIST)
                        .contains(GTOreDictUnificator.getAssociation(stack).mMaterial.mMaterial);
            }
        }

        if (stack.getItem() instanceof BaseItemComponent && !stack.getUnlocalizedName()
            .contains("dust")
            && !stack.getUnlocalizedName()
                .contains("Dust")) {
            return true;
        }
        if (Block.getBlockFromItem(stack.getItem()) instanceof BlockBaseModular) {
            return true;
        }
        if (Block.getBlockFromItem(stack.getItem()) instanceof BlockFrameBox) {
            return true;
        }
        if (stack.getItem() == HELICOPTER.getDust(1)
            .getItem()) {
            return true;
        }
        if (stack.getItem() == HTGR_ITEM) {
            return true;
        }
        if (stack.getItem() == WHITE_METAL.getDust(1)
            .getItem()) {
            return true;
        }
        if (Railcraft.isModLoaded()) {
            if (Block.getBlockFromItem(stack.getItem())
                .getUnlocalizedName()
                .equals("tile.railcraft.machine.zeta")
                || Block.getBlockFromItem(stack.getItem())
                    .getUnlocalizedName()
                    .equals("tile.railcraft.machine.eta")) {
                return true;
            }
        }
        if (GalaxySpace.isModLoaded()) {
            if (stack.getItem() == GTModHandler.getModItem(GalaxySpace.ID, "metalsblock", 1L, 7)
                .getItem()) {
                return true;
            }
        }
        if (NewHorizonsCoreMod.isModLoaded()) {
            if (stack.getItem() == GTModHandler.getModItem(NewHorizonsCoreMod.ID, "IndustryFrame", 1L)
                .getItem()) {
                return true;
            }
        }
        if (!BWUtil.checkStackAndPrefix(stack)) {
            return false;
        }
        return Arrays.asList(PlatinumSludgeOverHaul.BLACKLIST)
            .contains(GTOreDictUnificator.getAssociation(stack).mMaterial.mMaterial);
    }
}
