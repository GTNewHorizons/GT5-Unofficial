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

package bartworks.system.material.processingLoaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.bacterialVatRecipes;
import static bartworks.API.recipe.BartWorksRecipeMaps.bioLabRecipes;
import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.enums.OrePrefixes.bolt;
import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.gemChipped;
import static gregtech.api.enums.OrePrefixes.gemFlawed;
import static gregtech.api.enums.OrePrefixes.stick;
import static gregtech.api.enums.OrePrefixes.stickLong;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
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
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.loaders.BioCultureLoader;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.FluidLoader;
import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.BWNonMetaMaterialItems;
import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BioCulture;
import bartworks.util.BioDNA;
import bartworks.util.BioData;
import bartworks.util.BioPlasmid;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;

public class AdditionalRecipes {

    private static void runBWRecipes() {

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
                GTValues.RA.stdBuilder()
                    .itemInputs(stack, DNAFlask, Detergent, EthanolCell)
                    .itemOutputs(
                        BioItemList.getDNASampleFlask(BioDNA.convertDataToDNA(DNA)),
                        GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L))
                    .outputChances(DNA.getChance(), 100_00)
                    .fluidInputs(FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                    .special(BioItemList.mBioLabParts[0])
                    .duration(25 * SECONDS)
                    .eut(GTValues.VP[3 + DNA.getTier()])
                    .ignoreCollision()
                    .fake()
                    .addTo(bioLabRecipes);
            }

        }

        for (ItemStack stack : BioItemList.getAllDNASampleFlasks()) {
            BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

            if (DNA != null) {
                ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                BehaviourDataOrb.setDataTitle(Outp, "DNA Sample");
                BehaviourDataOrb.setDataName(Outp, DNA.getName());

                GTValues.RA.stdBuilder()
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
                    .eut(GTValues.VP[4 + DNA.getTier()])
                    .ignoreCollision()
                    .fake()
                    .addTo(bioLabRecipes);
            }
        }

        for (ItemStack stack : BioItemList.getAllPlasmidCells()) {
            BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

            if (DNA != null) {
                ItemStack inp = ItemList.Tool_DataOrb.get(0L);
                BehaviourDataOrb.setDataTitle(inp, "DNA Sample");
                BehaviourDataOrb.setDataName(inp, DNA.getName());
                ItemStack inp2 = ItemList.Tool_DataOrb.get(0L);
                BehaviourDataOrb.setDataTitle(inp2, "DNA Sample");
                BehaviourDataOrb.setDataName(inp2, BioCultureLoader.BIO_DATA_BETA_LACMATASE.getName());

                GTValues.RA.stdBuilder()
                    .itemInputs(FluidLoader.BioLabFluidCells[1], BioItemList.getPlasmidCell(null), inp, inp2)
                    .itemOutputs(stack, ItemList.Cell_Empty.get(1L))
                    .outputChances(DNA.getChance(), 100_00)
                    .fluidInputs(dnaFluid)
                    .special(BioItemList.mBioLabParts[2])
                    .duration(25 * SECONDS)
                    .eut(GTValues.VP[4 + DNA.getTier()])
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
                GTValues.RA.stdBuilder()
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
        BehaviourDataOrb.setDataTitle(Outp, "DNA Sample");
        BehaviourDataOrb.setDataName(Outp, "Any DNA");
        // Clonal Cellular Synthesis- [Liquid DNA] + Medium Petri Dish + Plasma Membrane + Stem Cells + Genome Data
        GTValues.RA.stdBuilder()
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
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.getIntegratedCircuit(1), new ItemStack(Items.sugar, 64))
                        .special(BioItemList.getPetriDish(bioCulture))
                        .fluidInputs(fluidStack)
                        .fluidOutputs(new FluidStack(bioCulture.getFluid(), 10))
                        .duration(50 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(bacterialVatRecipes);

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            BioItemList.getPetriDish(null),
                            fluidStack.equals(Materials.Water.getFluid(1000L)) ? Materials.Water.getCells(1)
                                : GTUtility.getContainersFromFluid(GTModHandler.getDistilledWater(1000))
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
            GTValues.RA.stdBuilder()
                .itemInputs(
                    fuel.getLeft()
                        .getCells(1))
                .itemOutputs(Materials.Empty.getCells(1))
                .metadata(FUEL_VALUE, fuel.getRight())
                .addTo(BartWorksRecipeMaps.acidGenFuels);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Redstone, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .metadata(FUEL_VALUE, 10)
            .addTo(BartWorksRecipeMaps.acidGenFuels);
    }

    @SuppressWarnings("deprecation")
    public static void run() {
        runBWRecipes();

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.RawAdemicSteel.get(dust))
            .itemOutputs(WerkstoffLoader.AdemicSteel.get(dust))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(implosionRecipes);

        // Thorium/Yttrium Glas
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.YttriumOxide.get(dustSmall, 2), WerkstoffLoader.Thorianit.get(dustSmall, 2))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 12))
            .fluidInputs(Materials.Glass.getMolten(144))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 3663)
            .addTo(blastFurnaceRecipes);

        // Thorianite recipes
        GTValues.RA.stdBuilder()
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
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorianit.get(dust, 9), Materials.Aluminium.getDust(4))
            .itemOutputs(Materials.Thorium.getDust(3), Materials.Aluminiumoxide.getDust(10))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ThO2 + 2Mg = Th + 2MgO
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorianit.get(dust, 3), Materials.Magnesium.getDust(2))
            .itemOutputs(Materials.Thorium.getDust(1), Materials.Magnesia.getDust(4))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Thorianit.get(crushed), ItemList.Crop_Drop_Thorium.get(9))
            .itemOutputs(WerkstoffLoader.Thorianit.get(crushedPurified, 4))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(Materials.Thorium.getMolten(144))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(24)
            .addTo(UniversalChemical);

        // Prasiolite
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(dust, Materials.Quartzite, 40L), Materials.Amethyst.getDust(10))
            .itemOutputs(WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 20))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 500)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(dust, Materials.Quartzite, 40L))
            .itemOutputs(Materials.Amethyst.getDust(10))
            .duration(40 * SECONDS)
            .eut(0)
            .metadata(ADDITIVE_AMOUNT, 6)
            .addTo(primitiveBlastRecipes);

        // Cubic Circonia
        // 2Y + 3O = Y2O3
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Yttrium.getDust(2), GTUtility.getIntegratedCircuit(5))
            .itemOutputs(WerkstoffLoader.YttriumOxide.get(dust, 5))
            .fluidInputs(Materials.Oxygen.getGas(3000))
            .duration(3 * MINUTES + 24 * SECONDS + 16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr + 2O =Y22O3= ZrO2
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Zirconium.get(dust, 10), WerkstoffLoader.YttriumOxide.get(dust, 0))
            .itemOutputs(WerkstoffLoader.CubicZirconia.get(gemFlawed, 40))
            .fluidInputs(Materials.Oxygen.getGas(20000))
            .duration(48 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2953)
            .noOptimize()
            .addTo(blastFurnaceRecipes);

        // Tellurium
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(crushed, Materials.Lead, 10L), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(Materials.Lead.getIngots(10), Materials.Tellurium.getNuggets(20))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 722)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium.getMolten(48), Materials.Beryllium.getMolten(48))
            .fluidOutputs(WerkstoffLoader.Californium.getMolten(48))
            .duration(12 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 480_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffLoader.Californium.getMolten(32), WerkstoffLoader.Calcium.getMolten(720))
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(720))
            .duration(24 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 600_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
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

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.MagnetoResonaticDust.get(dust))
            .itemOutputs(WerkstoffLoader.MagnetoResonaticDust.get(gemChipped, 9))
            .outputChances(90_00)
            .fluidInputs(WerkstoffLoader.Neon.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.MagnetoResonaticDust.get(dust))
            .itemOutputs(WerkstoffLoader.MagnetoResonaticDust.get(gem))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        // Milk

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(
                Materials.Sugar.getDustSmall(21),
                Materials.Calcium.getDustTiny(1),
                Materials.Magnesium.getDustTiny(1),
                Materials.Potassium.getDustTiny(1),
                Materials.Sodium.getDustTiny(4),
                Materials.Phosphorus.getDustTiny(1))
            .outputChances(100_00, 100_00, 10_00, 100_00, 10_00, 10_00)
            .fluidInputs(Materials.Milk.getFluid(10000))
            .fluidOutputs(Materials.Water.getFluid(8832))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(bolt))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 12_500)
            .addTo(smallNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stick))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 62_500)
            .addTo(largeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stickLong))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 125_000)
            .addTo(hugeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stick))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 31_250)
            .addTo(extremeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(stickLong))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 125_000)
            .addTo(ultraHugeNaquadahReactorFuels);

        LoadItemContainers.run();

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), WerkstoffLoader.Tiberium.get(dust, 3))
            .itemOutputs(BWNonMetaMaterialItems.TiberiumCell_1.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                BWNonMetaMaterialItems.TiberiumCell_1.get(2L),
                GTOreDictUnificator.get(stick, Materials.TungstenSteel, 4L))
            .itemOutputs(BWNonMetaMaterialItems.TiberiumCell_2.get(1L))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                BWNonMetaMaterialItems.TiberiumCell_1.get(4L),
                GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 6L))
            .itemOutputs(BWNonMetaMaterialItems.TiberiumCell_4.get(1L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                BWNonMetaMaterialItems.TiberiumCell_2.get(2L),
                GTOreDictUnificator.get(stick, Materials.TungstenSteel, 4L))
            .itemOutputs(BWNonMetaMaterialItems.TiberiumCell_4.get(1L))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.NaquadahCell_1.get(32L),
                GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                GTOreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                WerkstoffLoader.Tiberium.get(dust, 64),
                WerkstoffLoader.Tiberium.get(dust, 64))
            .itemOutputs(BWNonMetaMaterialItems.TheCoreCell.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GregTechAPI.sAfterGTPostload.add(new AddSomeRecipes());
    }
}
