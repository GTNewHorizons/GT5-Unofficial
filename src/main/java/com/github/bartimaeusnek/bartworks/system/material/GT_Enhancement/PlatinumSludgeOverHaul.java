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

package com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement;

import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.AcidicIridiumSolution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.AcidicOsmiumSolution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.AmmoniumChloride;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.AquaRegia;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.CalciumChloride;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.CrudeRhMetall;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.FormicAcid;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.HotRutheniumTetroxideSollution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.IrLeachResidue;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.IrOsLeachResidue;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.IridiumChloride;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.IridiumDioxide;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.LeachResidue;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.OsmiumSolution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PDAmmonia;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PDMetallicPowder;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PDRawPowder;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PDSalt;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PGSDResidue;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PGSDResidue2;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PTConcentrate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PTMetallicPowder;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PTRawPowder;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PTResidue;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PTSaltCrude;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PTSaltRefined;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.PotassiumDisulfate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RHFilterCakeSolution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RHNitrate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RHSalt;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RHSaltSolution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RHSulfate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RHSulfateSolution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.ReRh;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RhFilterCake;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Rhodium;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Ruthenium;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RutheniumTetroxide;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.RutheniumTetroxideSollution;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.SodiumNitrate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.SodiumRuthenate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Sodiumformate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.Sodiumsulfate;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.ZincSulfate;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.crateGtDust;
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
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBlastRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMixerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sSifterRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;

import java.lang.reflect.Field;
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
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Items;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.CachedReflectionUtils;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.items.GT_Generic_Block;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ores_Abstract;

public class PlatinumSludgeOverHaul {

    private static final Materials[] BLACKLIST = { Materials.HSSS, Materials.EnderiumBase, Materials.Osmiridium,
            Materials.TPV, Materials.get("Uraniumtriplatinid"),
            Materials.get("Tetranaquadahdiindiumhexaplatiumosminid"),
            Materials.get("Longasssuperconductornameforuvwire"), };
    private static final OrePrefixes[] OPBLACKLIST = { crushedCentrifuged, crushed, crushedPurified, dustPure,
            dustImpure, dustRefined, dust, dustTiny, dustSmall };

    private PlatinumSludgeOverHaul() {}

    private static void runHelperrecipes() {
        // DilutedSulfuricAcid
        // 2H2SO4 + H2O = 3H2SO4(d)
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        Materials.SulfuricAcid.getCells(2),
                        Materials.Water.getCells(1),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(Materials.DilutedSulfuricAcid.getCells(3)).duration(1 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV).addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(Materials.Water.getCells(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(Materials.Empty.getCells(1)).fluidInputs(Materials.SulfuricAcid.getFluid(2000))
                .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(3000)).duration(1 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV).addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(Materials.SulfuricAcid.getCells(2), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(Materials.Empty.getCells(2)).fluidInputs(Materials.Water.getFluid(1000))
                .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(3000)).duration(1 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV).addTo(sMixerRecipes);
        // FormicAcid
        // CO + NaOH = CHO2Na
        GT_Values.RA.stdBuilder().itemInputs(Materials.CarbonMonoxide.getCells(1), Materials.SodiumHydroxide.getDust(3))
                .itemOutputs(Sodiumformate.get(cell)).duration(15 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);
        // H2SO4 + 2CHO2Na = 2CH2O2 + Na2SO4

        GT_Values.RA.stdBuilder().itemInputs(Sodiumformate.get(cell, 2), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(FormicAcid.get(cell, 2), Sodiumsulfate.get(dust, 7))
                .fluidInputs(Materials.SulfuricAcid.getFluid(1000)).duration(15 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(Materials.Empty.getCells(1), Sodiumsulfate.get(dust, 7))
                .fluidInputs(Sodiumformate.getFluidOrGas(2000)).fluidOutputs(FormicAcid.getFluidOrGas(2000))
                .duration(15 * TICKS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        // AquaRegia
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        Materials.DilutedSulfuricAcid.getCells(1),
                        Materials.NitricAcid.getCells(1),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(AquaRegia.get(cell, 2)).duration(1 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        Materials.DilutedSulfuricAcid.getCells(1),
                        Materials.NitricAcid.getCells(1),
                        GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(Materials.Empty.getCells(2)).fluidOutputs(AquaRegia.getFluidOrGas(2000))
                .duration(1 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV).addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(Materials.NitricAcid.getCells(1), GT_Utility.getIntegratedCircuit(3))
                .itemOutputs(Materials.Empty.getCells(1)).fluidInputs(Materials.DilutedSulfuricAcid.getFluid(1000))
                .fluidOutputs(AquaRegia.getFluidOrGas(2000)).duration(1 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(Materials.DilutedSulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(4))
                .itemOutputs(Materials.Empty.getCells(1)).fluidInputs(Materials.NitricAcid.getFluid(1000))
                .fluidOutputs(AquaRegia.getFluidOrGas(2000)).duration(1 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(sMixerRecipes);

        // AmmoniumChloride
        // NH3 + HCl = NH4Cl

        GT_Values.RA.stdBuilder().itemInputs(Materials.Ammonia.getCells(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(AmmoniumChloride.get(cell, 1)).fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .duration(15 * TICKS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(Materials.Empty.getCells(1)).fluidInputs(Materials.Ammonia.getGas(1000))
                .fluidOutputs(AmmoniumChloride.getFluidOrGas(1000)).duration(15 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.Ammonia.getGas(64000), Materials.HydrochloricAcid.getFluid(64000) },
                new FluidStack[] { AmmoniumChloride.getFluidOrGas(64000) },
                null,
                60,
                480);
        // base solution
        for (Werkstoff w : Werkstoff.werkstoffHashSet) if (w.containsStuff(Materials.Sulfur)
                && (w.containsStuff(Materials.Copper) || w.containsStuff(Materials.Nickel))) {

                    GT_Values.RA.stdBuilder().itemInputs(w.get(crushedPurified), GT_Utility.getIntegratedCircuit(1))
                            .fluidInputs(AquaRegia.getFluidOrGas(150)).fluidOutputs(PTConcentrate.getFluidOrGas(150))
                            .duration(12 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

                    GT_Values.RA.stdBuilder().itemInputs(w.get(crushedPurified, 9), GT_Utility.getIntegratedCircuit(9))
                            .fluidInputs(AquaRegia.getFluidOrGas(1350)).fluidOutputs(PTConcentrate.getFluidOrGas(1350))
                            .duration(11 * SECONDS + 5 * TICKS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

                    GT_Values.RA.stdBuilder().itemInputs(w.get(crushedPurified, 9), PTMetallicPowder.get(dust, 9))
                            .itemOutputs(PTResidue.get(dust)).fluidInputs(AquaRegia.getFluidOrGas(10350))
                            .fluidOutputs(PTConcentrate.getFluidOrGas(10350))
                            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                            .addTo(UniversalChemical);

                }
        for (Materials m : Materials.values()) if (PlatinumSludgeOverHaul.materialsContains(m, Materials.Sulfur)
                && (PlatinumSludgeOverHaul.materialsContains(m, Materials.Copper)
                        || PlatinumSludgeOverHaul.materialsContains(m, Materials.Nickel))) {

                            GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                            GT_OreDictUnificator.get(crushedPurified, m, 1),
                                            GT_Utility.getIntegratedCircuit(1))
                                    .fluidInputs(AquaRegia.getFluidOrGas(150))
                                    .fluidOutputs(PTConcentrate.getFluidOrGas(150)).duration(12 * SECONDS + 10 * TICKS)
                                    .eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

                            GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                            GT_OreDictUnificator.get(crushedPurified, m, 9),
                                            GT_Utility.getIntegratedCircuit(9))
                                    .fluidInputs(AquaRegia.getFluidOrGas(1350))
                                    .fluidOutputs(PTConcentrate.getFluidOrGas(1350))
                                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                                    .addTo(UniversalChemical);

                            GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                            GT_OreDictUnificator.get(crushedPurified, m, 9),
                                            PTMetallicPowder.get(dust, 9))
                                    .itemOutputs(PTResidue.get(dust)).fluidInputs(AquaRegia.getFluidOrGas(10350))
                                    .fluidOutputs(PTConcentrate.getFluidOrGas(10350))
                                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                                    .addTo(UniversalChemical);

                        }
        // Pt
        GT_Values.RA.stdBuilder().itemInputs(PTMetallicPowder.get(dust, 3), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(Materials.Platinum.getNuggets(2)).duration(30 * SECONDS).eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, Materials.Platinum.mMeltingPoint).addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder().itemInputs(PTMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(PTResidue.get(dustTiny)).fluidInputs(AquaRegia.getFluidOrGas(1000))
                .fluidOutputs(PTConcentrate.getFluidOrGas(1000)).duration(12 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(PTMetallicPowder.get(dust, 9), GT_Utility.getIntegratedCircuit(9))
                .itemOutputs(PTResidue.get(dust)).fluidInputs(AquaRegia.getFluidOrGas(9000))
                .fluidOutputs(PTConcentrate.getFluidOrGas(9000)).duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(PTConcentrate.get(cell, 2))
                .itemOutputs(
                        PTSaltCrude.get(dustTiny, 16),
                        PTRawPowder.get(dustTiny, 4),
                        Materials.NitrogenDioxide.getCells(1),
                        Materials.DilutedSulfuricAcid.getCells(1))
                .fluidInputs(AmmoniumChloride.getFluidOrGas(400)).fluidOutputs(PDAmmonia.getFluidOrGas(400))
                .duration(60 * SECONDS).eut(TierEU.RECIPE_LV).addTo(sCentrifugeRecipes);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { PTConcentrate.getFluidOrGas(2000), AmmoniumChloride.getFluidOrGas(400) },
                new FluidStack[] { PDAmmonia.getFluidOrGas(400), Materials.NitrogenDioxide.getGas(1000),
                        Materials.DilutedSulfuricAcid.getFluid(1000) },
                new ItemStack[] { PTSaltCrude.get(dustTiny, 16), PTRawPowder.get(dustTiny, 4) },
                1200,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { PTConcentrate.getFluidOrGas(18000), AmmoniumChloride.getFluidOrGas(3600) },
                new FluidStack[] { PDAmmonia.getFluidOrGas(3600), Materials.NitrogenDioxide.getGas(9000),
                        Materials.DilutedSulfuricAcid.getFluid(9000) },
                new ItemStack[] { PTSaltCrude.get(dust, 16), PTRawPowder.get(dust, 4) },
                1400,
                240);

        GT_Values.RA.stdBuilder().itemInputs(PTSaltCrude.get(dust))
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
                .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500).duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(sSifterRecipes);

        GT_Values.RA.stdBuilder().itemInputs(PTSaltRefined.get(dust), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(PTMetallicPowder.get(dust)).fluidOutputs(Materials.Chlorine.getGas(87))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_MV).metadata(COIL_HEAT, 900).addTo(sBlastRecipes);

        // 2PtCl + Ca = 2Pt + CaCl2

        GT_Values.RA.stdBuilder().itemInputs(PTRawPowder.get(dust, 4), Materials.Calcium.getDust(1))
                .itemOutputs(Materials.Platinum.getDust(2), CalciumChloride.get(dust, 3))
                .duration(1 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);
        // Pd

        GT_Values.RA.stdBuilder().itemInputs(PDMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Ammonia.getGas(1000)).fluidOutputs(PDAmmonia.getFluidOrGas(1000))
                .duration(12 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(PDMetallicPowder.get(dust), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(PDSalt.get(dustTiny, 16), PDRawPowder.get(dustTiny, 2))
                .fluidInputs(PDAmmonia.getFluidOrGas(1000)).duration(12 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(PDMetallicPowder.get(dust, 9), GT_Utility.getIntegratedCircuit(9))
                .itemOutputs(PDSalt.get(dust, 16), PDRawPowder.get(dust, 2)).fluidInputs(PDAmmonia.getFluidOrGas(9000))
                .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(2)).itemOutputs(PDSalt.get(dust))
                .fluidInputs(PDAmmonia.getFluidOrGas(1000)).duration(12 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(PDSalt.get(dust))
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
                .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500).duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(sSifterRecipes);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                PDRawPowder.get(dust, 4),
                Materials.Empty.getCells(1),
                FormicAcid.getFluidOrGas(4000),
                Materials.Ammonia.getGas(4000),
                Materials.Palladium.getDust(2),
                Materials.Ethylene.getCells(1),
                250,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { PDRawPowder.get(dust, 4) },
                new FluidStack[] { FormicAcid.getFluidOrGas(4000) },
                new FluidStack[] { Materials.Ammonia.getGas(4000), Materials.Ethylene.getGas(1000),
                        Materials.Water.getFluid(1000) },
                new ItemStack[] { Materials.Palladium.getDust(2) },
                250,
                30);
        // Na2SO4 + 2H = 2Na + H2SO4

        GT_Values.RA.stdBuilder().itemInputs(Sodiumsulfate.get(dust, 7), Materials.Hydrogen.getCells(2))
                .itemOutputs(Materials.Sodium.getDust(2), Materials.Empty.getCells(2))
                .fluidOutputs(Materials.SulfuricAcid.getFluid(1000)).duration(1 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        // K2S2O7
        // GT_Values.RA.addChemicalRecipe(Sodiumsulfate.get(dust), Materials.Potassium.getDust(2),
        // Materials.Oxygen.getGas(3000), null, PotassiumDisulfate.get(dust,6), null, 30);
        // Rh/Os/Ir/Ru
        GT_Values.RA.stdBuilder().itemInputs(PTResidue.get(dust), GT_Utility.getIntegratedCircuit(11))
                .itemOutputs(LeachResidue.get(dust)).fluidInputs(PotassiumDisulfate.getMolten(360))
                .fluidOutputs(RHSulfate.getFluidOrGas(360)).duration(10 * SECONDS).eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 775).addTo(sBlastRecipes);

        // Ru
        GT_Values.RA.stdBuilder().itemInputs(LeachResidue.get(dust, 10), Materials.Saltpeter.getDust(10))
                .itemOutputs(SodiumRuthenate.get(dust, 3), IrOsLeachResidue.get(dust, 6))
                .fluidInputs(Materials.SaltWater.getFluid(1000)).fluidOutputs(GT_ModHandler.getSteam(1000))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_MV).metadata(COIL_HEAT, 775).addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder().itemInputs(SodiumRuthenate.get(dust, 6), Materials.Chlorine.getCells(3))
                .itemOutputs(Materials.Empty.getCells(3)).fluidOutputs(RutheniumTetroxideSollution.getFluidOrGas(9000))
                .duration(15 * SECONDS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(RutheniumTetroxideSollution.getFluidOrGas(800))
                .fluidOutputs(HotRutheniumTetroxideSollution.getFluidOrGas(800)).duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sFluidHeaterRecipes);

        GT_Values.RA.addCrackingRecipe(
                17,
                RutheniumTetroxideSollution.getFluidOrGas(1000),
                GT_ModHandler.getSteam(1000),
                HotRutheniumTetroxideSollution.getFluidOrGas(2000),
                150,
                480);
        GT_Values.RA.addDistillationTowerRecipe(
                HotRutheniumTetroxideSollution.getFluidOrGas(9000),
                new FluidStack[] { Materials.Water.getFluid(1800), RutheniumTetroxide.getFluidOrGas(7200) },
                Materials.Salt.getDust(6),
                1500,
                480);

        GT_Values.RA.stdBuilder().itemInputs(RutheniumTetroxide.get(dust, 1), Materials.HydrochloricAcid.getCells(6))
                .itemOutputs(Ruthenium.get(dust), Materials.Chlorine.getCells(6))
                .fluidOutputs(Materials.Water.getFluid(2000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        // Os
        GT_Values.RA.stdBuilder().itemInputs(IrOsLeachResidue.get(dust, 4), GT_Utility.getIntegratedCircuit(11))
                .itemOutputs(IrLeachResidue.get(dust, 2)).fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .fluidOutputs(AcidicOsmiumSolution.getFluidOrGas(2000)).duration(10 * SECONDS).eut(TierEU.RECIPE_MV)
                .metadata(COIL_HEAT, 775).addTo(sBlastRecipes);

        GT_Values.RA.addDistillationTowerRecipe(
                AcidicOsmiumSolution.getFluidOrGas(1000),
                new FluidStack[] { OsmiumSolution.getFluidOrGas(100), Materials.Water.getFluid(900) },
                null,
                150,
                (int) TierEU.RECIPE_IV);

        GT_Values.RA.stdBuilder().itemInputs(OsmiumSolution.get(cell), Materials.HydrochloricAcid.getCells(6))
                .itemOutputs(Materials.Osmium.getDust(1), Materials.Chlorine.getCells(7))
                .fluidOutputs(Materials.Water.getFluid(2000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        // Ir
        GT_Values.RA.stdBuilder().itemInputs(IrLeachResidue.get(dust), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(PGSDResidue.get(dust), IridiumDioxide.get(dust)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV).metadata(COIL_HEAT, 775).addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder().itemInputs(IridiumDioxide.get(dust), Materials.HydrochloricAcid.getCells(1))
                .itemOutputs(Materials.Empty.getCells(1)).fluidOutputs(AcidicIridiumSolution.getFluidOrGas(1000))
                .duration(15 * SECONDS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(AcidicIridiumSolution.get(cell), AmmoniumChloride.get(cell, 3))
                .itemOutputs(Materials.Empty.getCells(4), IridiumChloride.get(dust))
                .fluidOutputs(Materials.Ammonia.getGas(3000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(IridiumChloride.get(dust), Materials.Calcium.getDust(1))
                .itemOutputs(PGSDResidue2.get(dust), Materials.Iridium.getDust(1))
                .fluidOutputs(CalciumChloride.getFluidOrGas(3000)).duration(15 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(UniversalChemical);
        // Rh

        GT_Values.RA.stdBuilder().itemInputs(RHSulfate.get(cell, 11), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(RHSulfateSolution.get(cell, 11), LeachResidue.get(dustTiny, 10))
                .fluidInputs(Materials.Water.getFluid(10000)).fluidOutputs(Materials.Potassium.getMolten(2000))
                .duration(15 * SECONDS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(3) },
                new FluidStack[] { Materials.Water.getFluid(36000), RHSulfate.getFluidOrGas(39600) },
                new FluidStack[] { Materials.Potassium.getMolten(7200), RHSulfateSolution.getFluidOrGas(39600) },
                new ItemStack[] { LeachResidue.get(dust, 4) },
                1200,
                30);

        GT_Values.RA.stdBuilder().itemInputs(Materials.Zinc.getDust(1))
                .itemOutputs(ZincSulfate.get(dust, 6), CrudeRhMetall.get(dust))
                .fluidInputs(RHSulfateSolution.getFluidOrGas(1000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(CrudeRhMetall.get(dust), Materials.Salt.getDust(1))
                .itemOutputs(RHSalt.get(dust, 3)).fluidInputs(Materials.Chlorine.getGas(1000)).duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV).metadata(COIL_HEAT, 600).addTo(sBlastRecipes);

        GT_Values.RA.stdBuilder().itemInputs(RHSalt.get(dust, 10)).fluidInputs(Materials.Water.getFluid(2000))
                .fluidOutputs(RHSaltSolution.getFluidOrGas(2000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(SodiumNitrate.get(dust, 5), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(RHNitrate.get(dust), Materials.Salt.getDust(2))
                .fluidInputs(RHSaltSolution.getFluidOrGas(1000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);
        // Na + HNO3 = NaNO3 + H

        GT_Values.RA.stdBuilder().itemInputs(Materials.Sodium.getDust(1)).itemOutputs(SodiumNitrate.get(dust, 5))
                .fluidInputs(Materials.NitricAcid.getFluid(1000)).fluidOutputs(Materials.Hydrogen.getGas(1000))
                .duration(8 * TICKS).eut(60).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(RHNitrate.get(dust))
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
                .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500).duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(sSifterRecipes);

        GT_Values.RA.stdBuilder().itemInputs(RhFilterCake.get(dust)).fluidInputs(Materials.Water.getFluid(1000))
                .fluidOutputs(RHFilterCakeSolution.getFluidOrGas(1000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(2)).itemOutputs(ReRh.get(dust))
                .fluidInputs(RHFilterCakeSolution.getFluidOrGas(1000)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(ReRh.get(dust), Materials.Empty.getCells(1))
                .itemOutputs(Rhodium.get(dust), Materials.Ammonia.getCells(1))
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1000)).fluidOutputs(Materials.Chlorine.getGas(1000))
                .duration(15 * SECONDS).eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

    }

    private static boolean materialsContains(Materials one, ISubTagContainer other) {
        if (one == null || one.mMaterialList == null || one.mMaterialList.isEmpty()) return false;
        for (MaterialStack stack : one.mMaterialList) if (stack.mMaterial.equals(other)) return true;
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void replacePureElements() {
        // furnace
        for (Object entry : FurnaceRecipes.smelting().getSmeltingList().entrySet()) {
            Map.Entry realEntry = (Map.Entry) entry;
            if (GT_Utility.isStackValid(realEntry.getKey())
                    && BW_Util.checkStackAndPrefix((ItemStack) realEntry.getKey())) {
                ItemData association = GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getKey());
                if (!dust.equals(association.mPrefix) && !dustTiny.equals(association.mPrefix)
                        || !association.mMaterial.mMaterial.equals(Materials.Platinum))
                    if (GT_Utility.isStackValid(realEntry.getValue())
                            && BW_Util.checkStackAndPrefix((ItemStack) realEntry.getValue())) {
                                ItemData ass = GT_OreDictUnificator.getAssociation((ItemStack) realEntry.getValue());
                                if (ass.mMaterial.mMaterial.equals(Materials.Platinum))
                                    if (!PlatinumSludgeOverHaul.isInBlackList((ItemStack) realEntry.getKey()))
                                        realEntry.setValue(
                                                PTMetallicPowder.get(
                                                        ass.mPrefix == nugget ? dustTiny : dust,
                                                        ((ItemStack) realEntry.getValue()).stackSize * 2));
                                else if (ass.mMaterial.mMaterial.equals(Materials.Palladium))
                                    if (!PlatinumSludgeOverHaul.isInBlackList((ItemStack) realEntry.getKey()))
                                        realEntry.setValue(
                                                PDMetallicPowder.get(
                                                        ass.mPrefix == nugget ? dustTiny : dust,
                                                        ((ItemStack) realEntry.getValue()).stackSize * 2));
                            }
            }
        }
        // vanilla crafting
        CraftingManager.getInstance().getRecipeList().forEach(PlatinumSludgeOverHaul::setnewMaterialInRecipe);
        // gt crafting
        try {
            ((List<IRecipe>) FieldUtils.getDeclaredField(GT_ModHandler.class, "sBufferRecipeList", true).get(null))
                    .forEach(PlatinumSludgeOverHaul::setnewMaterialInRecipe);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // gt machines
        maploop: for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
            if (map == GT_Recipe.GT_Recipe_Map.sFusionRecipes || map == GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes
                    || map == GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes
                    || "gt.recipe.eyeofharmony".equals(map.mUnlocalizedName)
                    || "gtpp.recipe.quantumforcesmelter".equals(map.mUnlocalizedName))
                continue;
            HashSet<GT_Recipe> toDel = new HashSet<>();
            recipeloop: for (GT_Recipe recipe : map.mRecipeList) {
                if (recipe.mFakeRecipe) continue maploop;

                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                    if (map.equals(GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes)) continue maploop;
                    if ("gtpp.recipe.alloyblastsmelter".equals(map.mUnlocalizedName)) continue maploop;
                    if (map.equals(GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes)
                            || map.equals(GT_Recipe.GT_Recipe_Map.sChemicalRecipes)) {
                        if (GT_Utility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                                || GT_Utility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i]))
                            toDel.add(recipe);
                        else if (GT_Utility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i])) {
                            recipe.mFluidOutputs[i] = AcidicIridiumSolution.getFluidOrGas(1000);
                            recipe.reloadOwner();
                        } else
                            if (GT_Utility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i])) {
                                recipe.mFluidOutputs[i] = PTConcentrate.getFluidOrGas(1000);
                                recipe.reloadOwner();
                            } else
                                if (GT_Utility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i])) {
                                    recipe.mFluidOutputs[i] = AcidicOsmiumSolution.getFluidOrGas(1000);
                                    recipe.reloadOwner();
                                }
                    } else if (GT_Utility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                            || GT_Utility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i])
                            || GT_Utility.areFluidsEqual(Materials.Iridium.getMolten(1), recipe.mFluidOutputs[i])
                            || GT_Utility.areFluidsEqual(Materials.Platinum.getMolten(1), recipe.mFluidOutputs[i]))
                        toDel.add(recipe);
                    else if (GT_Utility.areFluidsEqual(Materials.Osmium.getMolten(1), recipe.mFluidOutputs[i]))
                        toDel.add(recipe);
                }
                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(recipe.mOutputs[i])) continue;
                    if ((BW_Util.areStacksEqualOrNull(Ruthenium.get(dust), recipe.mOutputs[i])
                            || BW_Util.areStacksEqualOrNull(Ruthenium.get(dustImpure), recipe.mOutputs[i])
                            || BW_Util.areStacksEqualOrNull(Ruthenium.get(dustPure), recipe.mOutputs[i]))
                            && !BW_Util.areStacksEqualOrNull(Ruthenium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs)
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput)) continue recipeloop;
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = LeachResidue.get(dust, amount);
                        recipe.reloadOwner();
                    }
                    if ((BW_Util.areStacksEqualOrNull(Rhodium.get(dust), recipe.mOutputs[i])
                            || BW_Util.areStacksEqualOrNull(Rhodium.get(dustImpure), recipe.mOutputs[i])
                            || BW_Util.areStacksEqualOrNull(Rhodium.get(dustPure), recipe.mOutputs[i]))
                            && !BW_Util.areStacksEqualOrNull(Rhodium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs)
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput)) continue recipeloop;
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = CrudeRhMetall.get(dust, amount);
                        recipe.reloadOwner();
                    }
                    if (!BW_Util.checkStackAndPrefix(recipe.mOutputs[i])) continue;
                    // Pt
                    if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                            .equals(Materials.Platinum)) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput)) continue recipeloop;
                        }
                        if (dust.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                || dustImpure.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                || dustPure.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = BW_Util.setStackSize(PTMetallicPowder.get(dust), amount * 2);
                            recipe.reloadOwner();
                        } else if (dustSmall.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = BW_Util.setStackSize(PTMetallicPowder.get(dustSmall), amount * 2);
                            recipe.reloadOwner();
                        } else if (dustTiny.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                            int amount = recipe.mOutputs[i].stackSize;
                            recipe.mOutputs[i] = BW_Util.setStackSize(PTMetallicPowder.get(dustTiny), amount * 2);
                            recipe.reloadOwner();
                        }
                    } else if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                            .equals(Materials.Palladium)) {
                                for (ItemStack mInput : recipe.mInputs) {
                                    if (PlatinumSludgeOverHaul.isInBlackList(mInput)) continue recipeloop;
                                }
                                if (dust.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                        || dustImpure
                                                .equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                        || dustPure.equals(
                                                GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                    int amount = recipe.mOutputs[i].stackSize;
                                    recipe.mOutputs[i] = BW_Util.setStackSize(PDMetallicPowder.get(dust), amount * 4);
                                    recipe.reloadOwner();
                                } else if (dustSmall
                                        .equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                            int amount = recipe.mOutputs[i].stackSize;
                                            recipe.mOutputs[i] = BW_Util
                                                    .setStackSize(PDMetallicPowder.get(dustSmall), amount * 4);
                                            recipe.reloadOwner();
                                        } else
                                    if (dustTiny
                                            .equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                                int amount = recipe.mOutputs[i].stackSize;
                                                recipe.mOutputs[i] = BW_Util
                                                        .setStackSize(PDMetallicPowder.get(dustTiny), amount * 4);
                                                recipe.reloadOwner();
                                            }
                            } else
                        if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                                .equals(Materials.Osmium)) {
                                    for (ItemStack mInput : recipe.mInputs) {
                                        if (PlatinumSludgeOverHaul.isInBlackList(mInput)) continue recipeloop;
                                    }
                                    if (dust.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                            || dustImpure.equals(
                                                    GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                            || dustPure.equals(
                                                    GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                        int amount = recipe.mOutputs[i].stackSize;
                                        recipe.mOutputs[i] = BW_Util.setStackSize(IrOsLeachResidue.get(dust), amount);
                                        recipe.reloadOwner();
                                    } else if (dustSmall
                                            .equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                                int amount = recipe.mOutputs[i].stackSize;
                                                recipe.mOutputs[i] = BW_Util
                                                        .setStackSize(IrOsLeachResidue.get(dustSmall), amount);
                                                recipe.reloadOwner();
                                            } else
                                        if (dustTiny.equals(
                                                GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                                    int amount = recipe.mOutputs[i].stackSize;
                                                    recipe.mOutputs[i] = BW_Util
                                                            .setStackSize(IrOsLeachResidue.get(dustTiny), amount);
                                                    recipe.reloadOwner();
                                                }
                                } else
                            if (GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mMaterial.mMaterial
                                    .equals(Materials.Iridium)) {
                                        for (ItemStack mInput : recipe.mInputs) {
                                            if (PlatinumSludgeOverHaul.isInBlackList(mInput)) continue recipeloop;
                                        }
                                        if (dust.equals(GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                                || dustImpure.equals(
                                                        GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)
                                                || dustPure.equals(
                                                        GT_OreDictUnificator
                                                                .getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                            int amount = recipe.mOutputs[i].stackSize;
                                            recipe.mOutputs[i] = BW_Util.setStackSize(IrLeachResidue.get(dust), amount);
                                            recipe.reloadOwner();
                                        } else if (dustSmall.equals(
                                                GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                                    int amount = recipe.mOutputs[i].stackSize;
                                                    recipe.mOutputs[i] = BW_Util
                                                            .setStackSize(IrLeachResidue.get(dustSmall), amount);
                                                    recipe.reloadOwner();
                                                } else
                                            if (dustTiny.equals(
                                                    GT_OreDictUnificator.getAssociation(recipe.mOutputs[i]).mPrefix)) {
                                                        int amount = recipe.mOutputs[i].stackSize;
                                                        recipe.mOutputs[i] = BW_Util
                                                                .setStackSize(IrLeachResidue.get(dustTiny), amount);
                                                        recipe.reloadOwner();
                                                    }
                                    }
                }
            }
            map.mRecipeList.removeAll(toDel);
        }
        // TODO: remove EnderIO recipes

        // fix HV tier
        PlatinumSludgeOverHaul.replaceHVCircuitMaterials();
        // add new recipes
        PlatinumSludgeOverHaul.runHelperrecipes();
    }

    @SuppressWarnings("deprecation")
    private static void replaceHVCircuitMaterials() {
        GT_Values.RA.addMixerRecipe(
                Materials.Redstone.getDust(1),
                Materials.Electrum.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                null,
                null,
                null,
                Materials.Nikolite.getDust(8),
                1800,
                120);
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.mRecipeList) {
            if (recipe.mEUt > 512) continue;
            if (BW_Util.checkStackAndPrefix(recipe.mOutputs[0])) {
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    ItemStack stack = recipe.mInputs[i];
                    ItemData ass = GT_OreDictUnificator.getAssociation(stack);
                    if (BW_Util.checkStackAndPrefix(stack) && ass.mMaterial.mMaterial.equals(Materials.Platinum)) {
                        recipe.mInputs[i] = GT_OreDictUnificator.get(ass.mPrefix, Materials.BlueAlloy, stack.stackSize);
                        recipe.reloadOwner();
                    }
                }
            }
        }
    }

    private static void setnewMaterialInRecipe(Object obj) {
        String inputName = "output";
        String inputItemName = "input";
        if (!(obj instanceof ShapedOreRecipe) && !(obj instanceof ShapelessOreRecipe)) {
            if (obj instanceof ShapedRecipes || obj instanceof ShapelessRecipes) {
                inputName = "recipeOutput";
                inputItemName = "recipeItems";
            } else if (GTPlusPlus.isModLoaded()) {
                try {
                    if (Class.forName("gtPlusPlus.api.objects.minecraft.ShapedRecipe").isAssignableFrom(obj.getClass()))
                        obj = CachedReflectionUtils.getField(obj.getClass(), "mRecipe").get(obj);
                } catch (ClassNotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        IRecipe recipe = (IRecipe) obj;
        ItemStack otpt = recipe.getRecipeOutput();

        Field out = CachedReflectionUtils.getDeclaredField(recipe.getClass(), inputName);
        if (out == null) out = CachedReflectionUtils.getField(recipe.getClass(), inputName);

        Field in = CachedReflectionUtils.getDeclaredField(recipe.getClass(), inputItemName);
        if (in == null) in = CachedReflectionUtils.getField(recipe.getClass(), inputItemName);
        if (in == null) return;

        Object input;
        try {
            input = in.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Platinum.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Platinum)) return;
            try {
                out.set(recipe, PTMetallicPowder.get(dust, otpt.stackSize * 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Palladium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Palladium)) return;
            try {
                out.set(recipe, PDMetallicPowder.get(dust, otpt.stackSize * 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Iridium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Iridium)) return;
            try {
                out.set(recipe, IrLeachResidue.get(dust, otpt.stackSize));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (out != null && GT_Utility.areStacksEqual(otpt, Materials.Osmium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Osmium)) return;
            try {
                out.set(recipe, IrOsLeachResidue.get(dust, otpt.stackSize));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static boolean checkRecipe(Object input, Materials mat) {
        if (input instanceof List || input instanceof Object[]) {
            Set lists = new HashSet(), stacks = new HashSet();
            List ip = input instanceof List ? (List) input : new ArrayList();
            Object[] ip2 = input instanceof Object[] ? (Object[]) input : new Object[0];

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

            for (Object stack : stacks) {
                if (stack instanceof ItemStack
                        && GT_Utility.areStacksEqual(GT_OreDictUnificator.get(crateGtDust, mat, 1), (ItemStack) stack))
                    return true;
            }

            boolean allSame = false;
            for (Object stack : stacks) {
                if (!(stack instanceof ItemStack)) {
                    allSame = false;
                    break;
                }
                allSame = BW_Util.checkStackAndPrefix((ItemStack) stack)
                        && GT_OreDictUnificator.getAssociation((ItemStack) stack).mMaterial.mMaterial.equals(mat);
                if (!allSame) break;
            }
            return allSame;
        }
        return false;
    }

    private static boolean isInBlackList(ItemStack stack) {
        if (stack == null || stack.getItem() instanceof BW_MetaGenerated_Items
                || MainMod.MOD_ID.equals(GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId)
                || BartWorksCrossmod.MOD_ID.equals(GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId))
            return true;

        if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(NewHorizonsCoreMod.ID)
                && !stack.getUnlocalizedName().contains("dust")
                && !stack.getUnlocalizedName().contains("Dust"))
            return true;

        if (Block.getBlockFromItem(stack.getItem()) instanceof GT_Generic_Block
                && !(Block.getBlockFromItem(stack.getItem()) instanceof GT_Block_Ores_Abstract))
            return true;

        if (Arrays.stream(ItemList.values()).filter(ItemList::hasBeenSet)
                .anyMatch(e -> !BW_Util.checkStackAndPrefix(stack) && GT_Utility.areStacksEqual(e.get(1), stack, true)))
            return true;

        if (stack.getItem() instanceof GT_Generic_Item) {
            if (!BW_Util.checkStackAndPrefix(stack)) return false;
            return !Arrays.asList(PlatinumSludgeOverHaul.OPBLACKLIST)
                    .contains(GT_OreDictUnificator.getAssociation(stack).mPrefix)
                    || Arrays.asList(PlatinumSludgeOverHaul.BLACKLIST)
                            .contains(GT_OreDictUnificator.getAssociation(stack).mMaterial.mMaterial);
        }

        if (GTPlusPlus.isModLoaded()) {
            try {
                if (Class.forName("gtPlusPlus.core.item.base.BaseItemComponent")
                        .isAssignableFrom(stack.getItem().getClass()) && !stack.getUnlocalizedName().contains("dust")
                        && !stack.getUnlocalizedName().contains("Dust"))
                    return true;
                if (Class.forName("gtPlusPlus.core.block.base.BlockBaseModular")
                        .isAssignableFrom(Block.getBlockFromItem(stack.getItem()).getClass()))
                    return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (!BW_Util.checkStackAndPrefix(stack)) return false;

        return Arrays.asList(PlatinumSludgeOverHaul.BLACKLIST)
                .contains(GT_OreDictUnificator.getAssociation(stack).mMaterial.mMaterial);
    }
}
