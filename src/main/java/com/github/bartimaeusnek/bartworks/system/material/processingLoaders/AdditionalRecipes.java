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

package com.github.bartimaeusnek.bartworks.system.material.processingLoaders;

import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.bacterialVatRecipes;
import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.bioLabRecipes;
import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.enums.OrePrefixes.bolt;
import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.gemChipped;
import static gregtech.api.enums.OrePrefixes.gemExquisite;
import static gregtech.api.enums.OrePrefixes.gemFlawed;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.extremeNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.hugeNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.largeNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.smallNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.ultraHugeNaquadahReactorFuels;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GT_RecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_Meta_Items;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;

public class AdditionalRecipes {

    private static void runBWRecipes() {

        if (ConfigHandler.BioLab) {
            FluidStack[] dnaFluid = { Gendustry.isModLoaded() ? FluidRegistry.getFluidStack("liquiddna", 1000)
                : Materials.Biomass.getFluid(1000L) };

            for (ItemStack stack : BioItemList.getAllPetriDishes()) {
                BioData DNA = BioData.getBioDataFromNBTTag(
                    stack.getTagCompound()
                        .getCompoundTag("DNA"));
                if (DNA != null) {
                    ItemStack Detergent = BioItemList.getOther(1);
                    ItemStack DNAFlask = BioItemList.getDNASampleFlask(null);
                    ItemStack EthanolCell = Materials.Ethanol.getCells(1);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(stack, DNAFlask, Detergent, EthanolCell)
                        .itemOutputs(
                            BioItemList.getDNASampleFlask(BioDNA.convertDataToDNA(DNA)),
                            GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L))
                        .outputChances(DNA.getChance(), 100_00)
                        .fluidInputs(FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                        .special(BioItemList.mBioLabParts[0])
                        .duration(25 * SECONDS)
                        .eut(GT_Values.VP[3 + DNA.getTier()])
                        .ignoreCollision()
                        .fake()
                        .addTo(bioLabRecipes);
                }

            }

            for (ItemStack stack : BioItemList.getAllDNASampleFlasks()) {
                BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

                if (DNA != null) {
                    ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
                    Behaviour_DataOrb.setDataName(Outp, DNA.getName());

                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            stack,
                            FluidLoader.BioLabFluidCells[0],
                            FluidLoader.BioLabFluidCells[3],
                            ItemList.Tool_DataOrb.get(1L))
                        .itemOutputs(Outp, ItemList.Cell_Empty.get(2L))
                        .outputChances(DNA.getChance(), 100_00)
                        .fluidInputs(dnaFluid)
                        .special(BioItemList.mBioLabParts[1])
                        .duration(25 * SECONDS)
                        .eut(GT_Values.VP[4 + DNA.getTier()])
                        .ignoreCollision()
                        .fake()
                        .addTo(bioLabRecipes);
                }
            }

            for (ItemStack stack : BioItemList.getAllPlasmidCells()) {
                BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

                if (DNA != null) {
                    ItemStack inp = ItemList.Tool_DataOrb.get(0L);
                    Behaviour_DataOrb.setDataTitle(inp, "DNA Sample");
                    Behaviour_DataOrb.setDataName(inp, DNA.getName());
                    ItemStack inp2 = ItemList.Tool_DataOrb.get(0L);
                    Behaviour_DataOrb.setDataTitle(inp2, "DNA Sample");
                    Behaviour_DataOrb.setDataName(inp2, BioCultureLoader.BIO_DATA_BETA_LACMATASE.getName());

                    GT_Values.RA.stdBuilder()
                        .itemInputs(FluidLoader.BioLabFluidCells[1], BioItemList.getPlasmidCell(null), inp, inp2)
                        .itemOutputs(stack, ItemList.Cell_Empty.get(1L))
                        .outputChances(DNA.getChance(), 100_00)
                        .fluidInputs(dnaFluid)
                        .special(BioItemList.mBioLabParts[2])
                        .duration(25 * SECONDS)
                        .eut(GT_Values.VP[4 + DNA.getTier()])
                        .ignoreCollision()
                        .fake()
                        .addTo(bioLabRecipes);
                }
            }

            for (ItemStack stack : BioItemList.getAllPetriDishes()) {
                BioData DNA = BioData.getBioDataFromNBTTag(
                    stack.getTagCompound()
                        .getCompoundTag("DNA"));
                BioData Plasmid = BioData.getBioDataFromNBTTag(
                    stack.getTagCompound()
                        .getCompoundTag("Plasmid"));
                if (!Objects.equals(DNA.getName(), Plasmid.getName())) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            BioItemList.getPetriDish(BioCulture.getBioCulture(DNA.getName())),
                            BioItemList.getPlasmidCell(BioPlasmid.convertDataToPlasmid(Plasmid)),
                            FluidLoader.BioLabFluidCells[2])
                        .itemOutputs(stack, ItemList.Cell_Empty.get(1L))
                        .outputChances(Plasmid.getChance(), 100_00)
                        .fluidInputs(FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                        .special(BioItemList.mBioLabParts[3])
                        .duration(25 * SECONDS)
                        .eut(TierEU.RECIPE_LuV)
                        .ignoreCollision()
                        .fake()
                        .addTo(bioLabRecipes);
                }
            }

            ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
            Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
            Behaviour_DataOrb.setDataName(Outp, "Any DNA");
            // Clonal Cellular Synthesis- [Liquid DNA] + Medium Petri Dish + Plasma Membrane + Stem Cells + Genome Data
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    BioItemList.getPetriDish(null),
                    BioItemList.getOther(4),
                    ItemList.Circuit_Chip_Stemcell.get(2L),
                    Outp)
                .itemOutputs(
                    BioItemList.getPetriDish(null)
                        .setStackDisplayName("The Culture made from DNA"))
                .outputChances(75_00)
                .fluidInputs(new FluidStack(dnaFluid[0].getFluid(), 8000))
                .special(BioItemList.mBioLabParts[4])
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .ignoreCollision()
                .fake()
                .addTo(bioLabRecipes);

            FluidStack[] easyFluids = { Materials.Water.getFluid(1000L),
                FluidRegistry.getFluidStack("ic2distilledwater", 1000) };
            for (FluidStack fluidStack : easyFluids) {
                for (BioCulture bioCulture : BioCulture.BIO_CULTURE_ARRAY_LIST) {
                    if (bioCulture.isBreedable() && bioCulture.getTier() == 0) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.getIntegratedCircuit(1), new ItemStack(Items.sugar, 64))
                            .special(BioItemList.getPetriDish(bioCulture))
                            .fluidInputs(fluidStack)
                            .fluidOutputs(new FluidStack(bioCulture.getFluid(), 10))
                            .duration(50 * SECONDS)
                            .eut(TierEU.RECIPE_MV)
                            .addTo(bacterialVatRecipes);

                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                BioItemList.getPetriDish(null),
                                fluidStack.equals(Materials.Water.getFluid(1000L)) ? Materials.Water.getCells(1)
                                    : GT_Utility.getContainersFromFluid(GT_ModHandler.getDistilledWater(1000))
                                        .get(0))
                            .itemOutputs(BioItemList.getPetriDish(bioCulture), Materials.Empty.getCells(1))
                            .outputChances(bioCulture.getChance(), 100_00)
                            .fluidInputs(new FluidStack(bioCulture.getFluid(), 1000))
                            .duration(25 * SECONDS)
                            .eut(TierEU.RECIPE_HV)
                            .ignoreCollision()
                            .fake()
                            .addTo(bioLabRecipes);
                    }
                }
            }
        }

        List<Pair<Materials, Integer>> liquidFuels = Arrays.asList(
            ImmutablePair.of(Materials.PhosphoricAcid, 36),
            ImmutablePair.of(Materials.DilutedHydrochloricAcid, 14),
            ImmutablePair.of(Materials.HypochlorousAcid, 30),
            ImmutablePair.of(Materials.HydrofluoricAcid, 40),
            ImmutablePair.of(Materials.HydrochloricAcid, 28),
            ImmutablePair.of(Materials.NitricAcid, 24),
            ImmutablePair.of(Materials.Mercury, 32),
            ImmutablePair.of(Materials.DilutedSulfuricAcid, 9),
            ImmutablePair.of(Materials.SulfuricAcid, 18),
            ImmutablePair.of(Materials.AceticAcid, 11),
            ImmutablePair.of(WerkstoffLoader.FormicAcid.getBridgeMaterial(), 40));
        for (Pair<Materials, Integer> fuel : liquidFuels) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    fuel.getLeft()
                        .getCells(1))
                .itemOutputs(Materials.Empty.getCells(1))
                .metadata(FUEL_VALUE, fuel.getRight())
                .addTo(BartWorksRecipeMaps.acidGenFuels);
        }
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Redstone, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .metadata(FUEL_VALUE, 10)
            .addTo(BartWorksRecipeMaps.acidGenFuels);
    }

    @SuppressWarnings("deprecation")
    public static void run() {
        runBWRecipes();

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.RawAdemicSteel.get(dust))
            .itemOutputs(WerkstoffLoader.AdemicSteel.get(dust))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(implosionRecipes);

        // Thorium/Yttrium Glas
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.YttriumOxide.get(dustSmall, 2), WerkstoffLoader.Thorianit.get(dustSmall, 2))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 12))
            .fluidInputs(Materials.Glass.getMolten(144))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 3663)
            .addTo(blastFurnaceRecipes);

        // Thorianite recipes
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorianit.get(crushedPurified))
            .itemOutputs(
                WerkstoffLoader.Thorianit.get(dust),
                WerkstoffLoader.Thorianit.get(dust),
                WerkstoffLoader.Thorianit.get(dust),
                Materials.Thorium.getDust(1),
                Materials.Thorium.getDust(1),
                WerkstoffLoader.Thorium232.get(dust))
            .outputChances(7000, 1300, 700, 600, 300, 100)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(sifterRecipes);

        // 3ThO2 + 4Al = 3Th + 2Al2O3
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorianit.get(dust, 9), Materials.Aluminium.getDust(4))
            .itemOutputs(Materials.Thorium.getDust(3), Materials.Aluminiumoxide.getDust(10))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ThO2 + 2Mg = Th + 2MgO
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorianit.get(dust, 3), Materials.Magnesium.getDust(2))
            .itemOutputs(Materials.Thorium.getDust(1), Materials.Magnesia.getDust(4))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorianit.get(crushed), ItemList.Crop_Drop_Thorium.get(9))
            .itemOutputs(WerkstoffLoader.Thorianit.get(crushedPurified, 4))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(Materials.Thorium.getMolten(144))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(24)
            .addTo(UniversalChemical);

        // Prasiolite
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(dust, Materials.Quartzite, 40L), Materials.Amethyst.getDust(10))
            .itemOutputs(WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 20))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 500)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(dust, Materials.Quartzite, 40L))
            .itemOutputs(Materials.Amethyst.getDust(10))
            .duration(40 * SECONDS)
            .eut(0)
            .metadata(ADDITIVE_AMOUNT, 6)
            .addTo(primitiveBlastRecipes);

        // Cubic Circonia
        // 2Y + 3O = Y2O3
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Yttrium.getDust(2), GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(WerkstoffLoader.YttriumOxide.get(dust, 5))
            .fluidInputs(Materials.Oxygen.getGas(3000))
            .duration(3 * MINUTES + 24 * SECONDS + 16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr + 2O =Y22O3= ZrO2
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Zirconium.get(dust, 10), WerkstoffLoader.YttriumOxide.get(dust, 0))
            .itemOutputs(WerkstoffLoader.CubicZirconia.get(gemFlawed, 40))
            .fluidInputs(Materials.Oxygen.getGas(20000))
            .duration(48 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2953)
            .noOptimize()
            .addTo(blastFurnaceRecipes);

        // Tellurium
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(crushed, Materials.Lead, 10L), GT_Utility.getIntegratedCircuit(17))
            .itemOutputs(Materials.Lead.getIngots(10), Materials.Tellurium.getNuggets(20))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 722)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium.getMolten(48), Materials.Beryllium.getMolten(48))
            .fluidOutputs(WerkstoffLoader.Californium.getMolten(48))
            .duration(12 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 480_000_000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffLoader.Californium.getMolten(32), WerkstoffLoader.Calcium.getMolten(720))
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(720))
            .duration(24 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 600_000_000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.LiquidAir.getFluid(100000000))
            .fluidOutputs(
                Materials.Nitrogen.getGas(78084000),
                Materials.Oxygen.getGas(20946000),
                Materials.Argon.getGas(934000),
                Materials.CarbonDioxide.getGas(40700),
                WerkstoffLoader.Neon.getFluidOrGas(1818),
                Materials.Helium.getGas(524),
                Materials.Methane.getGas(180),
                WerkstoffLoader.Krypton.getFluidOrGas(114),
                Materials.Hydrogen.getGas(55),
                WerkstoffLoader.Xenon.getFluidOrGas(9))
            .duration(6 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.MagnetoResonaticDust.get(dust))
            .itemOutputs(WerkstoffLoader.MagnetoResonaticDust.get(gemChipped, 9))
            .outputChances(90_00)
            .fluidInputs(WerkstoffLoader.Neon.getFluidOrGas(1000))
            .duration(3 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.MagnetoResonaticDust.get(dust))
            .itemOutputs(WerkstoffLoader.MagnetoResonaticDust.get(gem))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(1000))
            .duration(3 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        // Milk

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(
                Materials.Sugar.getDustSmall(21),
                Materials.Calcium.getDustTiny(1),
                Materials.Magnesium.getDustTiny(1),
                Materials.Potassium.getDustTiny(1),
                Materials.Sodium.getDustTiny(4),
                Materials.Phosphor.getDustTiny(1))
            .outputChances(100_00, 100_00, 10_00, 100_00, 10_00, 10_00)
            .fluidInputs(Materials.Milk.getFluid(10000))
            .fluidOutputs(Materials.Water.getFluid(8832))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        // Magneto Resonatic Circuits

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");
        // ULV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(3),
                WerkstoffLoader.MagnetoResonaticDust.get(gem),
                ItemList.NandChip.get(1),
                ItemList.Circuit_Parts_DiodeSMD.get(4),
                ItemList.Circuit_Parts_CapacitorSMD.get(4),
                ItemList.Circuit_Parts_TransistorSMD.get(4))
            .itemOutputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(36))
            .duration(37 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .addTo(circuitAssemblerRecipes);

        // LV-EV
        long[] voltages = new long[] { 0, TierEU.RECIPE_LV, TierEU.RECIPE_MV, TierEU.RECIPE_HV, TierEU.RECIPE_EV };
        for (int i = 1; i <= 4; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    BW_Meta_Items.getNEWCIRCUITS()
                        .getStack(3),
                    WerkstoffLoader.MagnetoResonaticDust.get(gem),
                    BW_Meta_Items.getNEWCIRCUITS()
                        .getStack(i + 3),
                    ItemList.Circuit_Parts_DiodeSMD.get((i + 1) * 4),
                    ItemList.Circuit_Parts_CapacitorSMD.get((i + 1) * 4),
                    ItemList.Circuit_Parts_TransistorSMD.get((i + 1) * 4))
                .itemOutputs(
                    BW_Meta_Items.getNEWCIRCUITS()
                        .getStack(i + 4))
                .fluidInputs(Materials.SolderingAlloy.getMolten((i + 1) * 36))
                .duration((i + 1) * (37 * SECONDS + 10 * TICKS))
                .eut(voltages[i])
                .noOptimize()
                .addTo(circuitAssemblerRecipes);
        }
        // IV-LuV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(3),
                WerkstoffLoader.MagnetoResonaticDust.get(gem),
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(8),
                ItemList.Circuit_Parts_DiodeASMD.get(24),
                ItemList.Circuit_Parts_CapacitorASMD.get(24),
                ItemList.Circuit_Parts_TransistorASMD.get(24))
            .itemOutputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(9))
            .fluidInputs(new FluidStack(solderIndalloy, 216))
            .duration(3 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .noOptimize()
            .addTo(circuitAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(3),
                WerkstoffLoader.MagnetoResonaticDust.get(gem),
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(9),
                ItemList.Circuit_Parts_DiodeASMD.get(28),
                ItemList.Circuit_Parts_CapacitorASMD.get(28),
                ItemList.Circuit_Parts_TransistorASMD.get(28))
            .itemOutputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(12))
            .fluidInputs(new FluidStack(solderIndalloy, 252))
            .duration(4 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .noOptimize()
            .addTo(circuitAssemblerRecipes);

        // ZPM
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(3),
                WerkstoffLoader.MagnetoResonaticDust.get(gemExquisite, 1),
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(10),
                ItemList.Circuit_Parts_DiodeASMD.get(52),
                ItemList.Circuit_Parts_CapacitorASMD.get(52),
                ItemList.Circuit_Parts_TransistorASMD.get(52))
            .itemOutputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(11))
            .fluidInputs(new FluidStack(solderIndalloy, 288))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_UV)
            .requiresCleanRoom()
            .noOptimize()
            .addTo(circuitAssemblerRecipes);

        // UV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(3),
                WerkstoffLoader.MagnetoResonaticDust.get(gemExquisite, 1),
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(11),
                ItemList.Circuit_Parts_DiodeASMD.get(56),
                ItemList.Circuit_Parts_CapacitorASMD.get(56),
                ItemList.Circuit_Parts_TransistorASMD.get(56))
            .itemOutputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(12))
            .fluidInputs(new FluidStack(solderUEV, 324))
            .duration(11 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .requiresCleanRoom()
            .noOptimize()
            .addTo(circuitAssemblerRecipes);

        // UHV-UEV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(3),
                WerkstoffLoader.MagnetoResonaticDust.get(gemExquisite, 1),
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(12),
                ItemList.Circuit_Parts_DiodeXSMD.get(60),
                ItemList.Circuit_Parts_CapacitorXSMD.get(60),
                ItemList.Circuit_Parts_TransistorXSMD.get(60))
            .itemOutputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(13))
            .fluidInputs(new FluidStack(solderUEV, 360))
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .requiresCleanRoom()
            .noOptimize()
            .addTo(circuitAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(3),
                WerkstoffLoader.MagnetoResonaticDust.get(gemExquisite, 1),
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(13),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                ItemList.Circuit_Parts_TransistorXSMD.get(64))
            .itemOutputs(
                BW_Meta_Items.getNEWCIRCUITS()
                    .getStack(14))
            .fluidInputs(new FluidStack(solderUEV, 396))
            .duration(13 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .requiresCleanRoom()
            .noOptimize()
            .addTo(circuitAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(bolt))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 12_500)
            .addTo(smallNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stick))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 62_500)
            .addTo(largeNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stickLong))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 125_000)
            .addTo(hugeNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stick))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 31_250)
            .addTo(extremeNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stickLong))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 125_000)
            .addTo(ultraHugeNaquadahReactorFuels);

        LoadItemContainers.run();

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), WerkstoffLoader.Tiberium.get(dust, 3))
            .itemOutputs(BW_NonMeta_MaterialItems.TiberiumCell_1.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_NonMeta_MaterialItems.TiberiumCell_1.get(2L),
                GT_OreDictUnificator.get(stick, Materials.TungstenSteel, 4L))
            .itemOutputs(BW_NonMeta_MaterialItems.TiberiumCell_2.get(1L))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_NonMeta_MaterialItems.TiberiumCell_1.get(4L),
                GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 6L))
            .itemOutputs(BW_NonMeta_MaterialItems.TiberiumCell_4.get(1L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_NonMeta_MaterialItems.TiberiumCell_2.get(2L),
                GT_OreDictUnificator.get(stick, Materials.TungstenSteel, 4L))
            .itemOutputs(BW_NonMeta_MaterialItems.TiberiumCell_4.get(1L))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.NaquadahCell_1.get(32L),
                GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                WerkstoffLoader.Tiberium.get(dust, 64),
                WerkstoffLoader.Tiberium.get(dust, 64))
            .itemOutputs(BW_NonMeta_MaterialItems.TheCoreCell.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GregTech_API.sAfterGTPostload.add(new AddSomeRecipes());
    }
}
