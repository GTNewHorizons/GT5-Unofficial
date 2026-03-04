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
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
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
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.GLASS;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.loaders.BioCultureLoader;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.FluidLoader;
import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BioCulture;
import bartworks.util.BioDNA;
import bartworks.util.BioData;
import bartworks.util.BioPlasmid;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import gtPlusPlus.core.fluids.GTPPFluids;

public class AdditionalRecipes {

    private static void runBWRecipes() {

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
                    .fluidInputs(GTModHandler.getDistilledWater(1_000))
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
                    .fluidInputs(GTModHandler.getLiquidDNA(1_000))
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
                    .fluidInputs(GTModHandler.getLiquidDNA(1_000))
                    .special(BioItemList.mBioLabParts[2])
                    .duration(25 * SECONDS)
                    .eut(GTValues.VP[4 + DNA.getTier()])
                    .ignoreCollision()
                    .fake()
                    .addTo(bioLabRecipes);
            }
        }

        long energyUsageWithTransformModule = 1;
        for (ItemStack stack : BioItemList.getAllPetriDishes()) {
            BioData DNA = BioData.getBioDataFromNBTTag(
                stack.getTagCompound()
                    .getCompoundTag("DNA"));
            BioData Plasmid = BioData.getBioDataFromNBTTag(
                stack.getTagCompound()
                    .getCompoundTag("Plasmid"));
            if (!Objects.equals(DNA.getName(), Plasmid.getName())) {
                if ("TCetiEis Fucus Serratus".equals(DNA.getName())) {
                    energyUsageWithTransformModule = TierEU.RECIPE_LuV;
                } else if ("Escherichia koli".equals(DNA.getName())) {
                    energyUsageWithTransformModule = TierEU.RECIPE_EV;
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        BioItemList.getPetriDish(BioCulture.getBioCulture(DNA.getName())),
                        BioItemList.getPlasmidCell(BioPlasmid.convertDataToPlasmid(Plasmid)),
                        FluidLoader.BioLabFluidCells[2])
                    .itemOutputs(stack, ItemList.Cell_Empty.get(1L))
                    .outputChances(Plasmid.getChance(), 100_00)
                    .fluidInputs(GTModHandler.getDistilledWater(1_000))
                    .special(BioItemList.mBioLabParts[3])
                    .duration(25 * SECONDS)
                    .eut(energyUsageWithTransformModule)
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
            .fluidInputs(GTModHandler.getLiquidDNA(8_000))
            .special(BioItemList.mBioLabParts[4])
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .ignoreCollision()
            .fake()
            .addTo(bioLabRecipes);

        FluidStack[] easyFluids = { Materials.Water.getFluid(1_000), GTModHandler.getDistilledWater(1_000) };
        for (FluidStack fluidStack : easyFluids) {
            for (BioCulture bioCulture : BioCulture.BIO_CULTURE_ARRAY_LIST) {
                if (bioCulture.isBreedable() && bioCulture.getTier() == 0) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(Items.sugar, 64))
                        .special(BioItemList.getPetriDish(bioCulture))
                        .circuit(1)
                        .fluidInputs(fluidStack)
                        .fluidOutputs(new FluidStack(bioCulture.getFluid(), 10))
                        .metadata(GLASS, 3)
                        .duration(50 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(bacterialVatRecipes);

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            BioItemList.getPetriDish(null),
                            fluidStack.equals(Materials.Water.getFluid(1_000)) ? Materials.Water.getCells(1)
                                : FluidContainerRegistry.fillFluidContainer(
                                    GTModHandler.getDistilledWater(1_000),
                                    ItemList.Cell_Empty.get(1)))
                        .itemOutputs(BioItemList.getPetriDish(bioCulture), Materials.Empty.getCells(1))
                        .outputChances(bioCulture.getChance(), 100_00)
                        .fluidInputs(new FluidStack(bioCulture.getFluid(), 1_000))
                        .duration(25 * SECONDS)
                        .eut(TierEU.RECIPE_HV)
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
            ImmutablePair.of(WerkstoffLoader.FormicAcid.getBridgeMaterial(), 40),
            ImmutablePair.of(WerkstoffLoader.HexafluorosilicicAcid.getBridgeMaterial(), 350),
            ImmutablePair.of(Materials.PhthalicAcid, 270),
            ImmutablePair.of(Materials.NaphthenicAcid, 250));
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
        // should probably also find a way to auto-fill these with an array but i am too lazy to do that rn
        GTValues.RA.stdBuilder()
            .itemInputs(
                FluidContainerRegistry.fillFluidContainer(
                    new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 1000),
                    ItemList.Cell_Empty.get(1L)))
            .itemOutputs(Materials.Empty.getCells(1))
            .metadata(FUEL_VALUE, 320)
            .addTo(BartWorksRecipeMaps.acidGenFuels);
        GTValues.RA.stdBuilder()
            .itemInputs(
                FluidContainerRegistry.fillFluidContainer(
                    new FluidStack(GTPPFluids.IndustrialStrengthHydrogenChloride, 1000),
                    ItemList.Cell_Empty.get(1L)))
            .itemOutputs(Materials.Empty.getCells(1))
            .metadata(FUEL_VALUE, 224)
            .addTo(BartWorksRecipeMaps.acidGenFuels);
        GTValues.RA.stdBuilder()
            .itemInputs(
                FluidContainerRegistry
                    .fillFluidContainer(new FluidStack(GTPPFluids.PropionicAcid, 1000), ItemList.Cell_Empty.get(1L)))
            .itemOutputs(Materials.Empty.getCells(1))
            .metadata(FUEL_VALUE, 150)
            .addTo(BartWorksRecipeMaps.acidGenFuels);
    }

    public static void run() {
        runBWRecipes();

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.RawAdemicSteel.get(dust))
            .itemOutputs(WerkstoffLoader.AdemicSteel.get(dust))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(implosionRecipes);

        // Thorium/Yttrium Glass
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.YttriumOxide.get(dustSmall, 2), WerkstoffLoader.Thorianit.get(dustSmall, 2))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 12))
            .fluidInputs(Materials.Glass.getMolten(1 * INGOTS))
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

        // Prasiolite
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(dust, Materials.Quartzite, 4L), Materials.Amethyst.getDust(1))
            .itemOutputs(WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 2))
            .duration(4 * SECONDS)
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
            .itemInputs(Materials.Yttrium.getDust(2))
            .circuit(5)
            .itemOutputs(WerkstoffLoader.YttriumOxide.get(dust, 5))
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .duration(3 * MINUTES + 24 * SECONDS + 16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr + 2O =Y22O3= ZrO2
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Zirconium.get(dust, 10), WerkstoffLoader.YttriumOxide.get(dust, 0))
            .itemOutputs(WerkstoffLoader.CubicZirconia.get(gemFlawed, 40))
            .fluidInputs(Materials.Oxygen.getGas(20_000))
            .duration(48 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2953)
            .addTo(blastFurnaceRecipes);

        // Tellurium
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(crushed, Materials.Lead, 1L))
            .circuit(17)
            .itemOutputs(Materials.Lead.getIngots(1), Materials.Tellurium.getNuggets(2))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 722)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium.getMolten(3 * NUGGETS), Materials.Beryllium.getMolten(3 * NUGGETS))
            .fluidOutputs(WerkstoffLoader.Californium.getMolten(3 * NUGGETS))
            .duration(12 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 480_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                WerkstoffLoader.Californium.getMolten(2 * NUGGETS),
                WerkstoffLoader.Calcium.getMolten(5 * INGOTS))
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(720))
            .duration(24 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 600_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.LiquidAir.getFluid(100_000_000))
            .fluidOutputs(
                Materials.Nitrogen.getGas(78_084_000),
                Materials.Oxygen.getGas(20_946_000),
                Materials.Argon.getGas(934_000),
                Materials.CarbonDioxide.getGas(40_700),
                WerkstoffLoader.Neon.getFluidOrGas(1_818),
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
            .fluidInputs(WerkstoffLoader.Neon.getFluidOrGas(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.MagnetoResonaticDust.get(dust))
            .itemOutputs(WerkstoffLoader.MagnetoResonaticDust.get(gem))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        // Milk

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                Materials.Sugar.getDustSmall(21),
                Materials.Calcium.getDustTiny(1),
                Materials.Magnesium.getDustTiny(1),
                Materials.Potassium.getDustTiny(1),
                Materials.Sodium.getDustTiny(4),
                Materials.Phosphorus.getDustTiny(1))
            .outputChances(100_00, 100_00, 10_00, 100_00, 10_00, 10_00)
            .fluidInputs(Materials.Milk.getFluid(10_000))
            .fluidOutputs(Materials.Water.getFluid(8_832))
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
    }
}
