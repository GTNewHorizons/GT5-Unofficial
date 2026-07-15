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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.FluidLoader;
import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import bartworks.util.BioCulture;
import bartworks.util.BioData;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import gtPlusPlus.core.fluids.GTPPFluids;

public class AdditionalRecipes {

    private static void runBWRecipes() {

        for (BioCultureEnum culture : BioCultureEnum.VALUES) {
            BioData DNA = culture.dna.getBioData();
            ItemStack Detergent = ItemList.DetergentPowder.get(1);
            ItemStack DNAFlask = ItemList.EmptyDNAFlask.get(1);
            ItemStack EthanolCell = MaterialLibAPI
                .getStack(Materials2Materials.Ethanol, Materials2CellShapes.cell, (int) (1));
            GTValues.RA.stdBuilder()
                .itemInputs(BioCultureEnum.getPetriDish(culture.bioCulture), DNAFlask, Detergent, EthanolCell)
                .itemOutputs(
                    BioDataEnum.getDNASampleFlask(DNA),
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L))
                .outputChances(DNA.getChance(), 100_00)
                .fluidInputs(GTModHandler.getDistilledWater(1_000))
                .special(BioItemList.mBioLabParts[0])
                .duration(25 * SECONDS)
                .eut(GTValues.VP[DNA.getTier()])
                .ignoreCollision()
                .fake()
                .addTo(bioLabRecipes);

        }

        for (ItemStack stack : BioDataEnum.getAllDNASampleFlasks()) {
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
                    .eut(GTValues.VP[DNA.getTier() + 1])
                    .ignoreCollision()
                    .fake()
                    .addTo(bioLabRecipes);
            }
        }

        for (ItemStack stack : BioDataEnum.getAllPlasmidCells()) {
            BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

            if (DNA != null) {
                ItemStack inp = ItemList.Tool_DataOrb.get(0L);
                BehaviourDataOrb.setDataTitle(inp, "DNA Sample");
                BehaviourDataOrb.setDataName(inp, DNA.getName());
                ItemStack inp2 = ItemList.Tool_DataOrb.get(0L);
                BehaviourDataOrb.setDataTitle(inp2, "DNA Sample");
                BehaviourDataOrb.setDataName(
                    inp2,
                    BioDataEnum.BetaLactamase.getBioData()
                        .getName());

                GTValues.RA.stdBuilder()
                    .itemInputs(FluidLoader.BioLabFluidCells[1], ItemList.EmptyPlasmid.get(1), inp, inp2)
                    .itemOutputs(stack, ItemList.Cell_Empty.get(1L))
                    .outputChances(DNA.getChance(), 100_00)
                    .fluidInputs(GTModHandler.getLiquidDNA(1_000))
                    .special(BioItemList.mBioLabParts[2])
                    .duration(25 * SECONDS)
                    .eut(GTValues.VP[DNA.getTier() + 1])
                    .ignoreCollision()
                    .fake()
                    .addTo(bioLabRecipes);
            }
        }

        long energyUsageWithTransformModule = 1;
        for (BioCultureEnum culture : BioCultureEnum.VALUES) {
            BioData DNA = culture.dna.getBioData();
            BioData Plasmid = culture.plasmid.getBioData();
            if (BioDataEnum.NullBioData.getBioData()
                .equals(DNA)) continue;
            if (!Objects.equals(DNA.getName(), Plasmid.getName())) {
                if ("TCetiEis Fucus Serratus".equals(DNA.getName())) {
                    energyUsageWithTransformModule = TierEU.RECIPE_LuV;
                } else if ("Escherichia koli".equals(DNA.getName())) {
                    energyUsageWithTransformModule = TierEU.RECIPE_EV;
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        BioCultureEnum.getPetriDish(BioCulture.getBioCulture(DNA.getName())),
                        BioDataEnum.getPlasmidCell(Plasmid),
                        FluidLoader.BioLabFluidCells[2])
                    .itemOutputs(BioCultureEnum.getPetriDish(culture.bioCulture), ItemList.Cell_Empty.get(1L))
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
                ItemList.EmptyPetriDish.get(1),
                ItemList.PlasmaMembrane.get(1),
                ItemList.Circuit_Chip_Stemcell.get(2L),
                Outp)
            .itemOutputs(
                ItemList.EmptyPetriDish.get(1)
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
            for (BioCulture bioCulture : BioCultureEnum.BIO_CULTURES) {
                if (bioCulture.isBreedable() && bioCulture.getTier() == 0) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(Items.sugar, 64))
                        .special(BioCultureEnum.getPetriDish(bioCulture))
                        .circuit(1)
                        .fluidInputs(fluidStack)
                        .fluidOutputs(new FluidStack(bioCulture.getFluid(), 10))
                        .metadata(GLASS, 3)
                        .duration(50 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(bacterialVatRecipes);

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            ItemList.EmptyPetriDish.get(1),
                            fluidStack.equals(Materials.Water.getFluid(1_000)) ? Materials.Water.getCells(1)
                                : GTUtility.getContainersFromFluid(GTModHandler.getDistilledWater(1_000))
                                    .get(0))
                        .itemOutputs(BioCultureEnum.getPetriDish(bioCulture), Materials.Empty.getCells(1))
                        .outputChances(bioCulture.getChance(), 100_00)
                        .fluidInputs(new FluidStack(bioCulture.getFluid(), 1_000))
                        .duration(25 * SECONDS)
                        .eut(TierEU.RECIPE_HV)
                        .addTo(bioLabRecipes);
                }
            }
        }

        List<Pair<Materials, Integer>> liquidFuels = Arrays.asList(
            ImmutablePair.of(Materials.PhosphoricAcid, 66),
            ImmutablePair.of(Materials.DilutedHydrochloricAcid, 26),
            ImmutablePair.of(Materials.HypochlorousAcid, 56),
            ImmutablePair.of(Materials.HydrofluoricAcid, 60),
            ImmutablePair.of(Materials.HydrochloricAcid, 52),
            ImmutablePair.of(Materials.NitricAcid, 72),
            ImmutablePair.of(Materials.Mercury, 32),
            ImmutablePair.of(Materials.DilutedSulfuricAcid, 14),
            ImmutablePair.of(Materials.SulfuricAcid, 28),
            ImmutablePair.of(Materials.AceticAcid, 21),
            ImmutablePair.of(WerkstoffLoader.FormicAcid.getBridgeMaterial(), 40),
            ImmutablePair.of(WerkstoffLoader.HexafluorosilicicAcid.getBridgeMaterial(), 350),
            ImmutablePair.of(Materials.PhthalicAcid, 270),
            ImmutablePair.of(Materials.NaphthenicAcid, 250),
            ImmutablePair.of(Materials.ChlorosulfonicAcid, 2304),
            ImmutablePair.of(GGMaterial.fluoroantimonicAcid.getBridgeMaterial(), 5760));
        for (Pair<Materials, Integer> fuel : liquidFuels) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    fuel.getLeft()
                        .getCells(1))
                .itemOutputs(Materials.Empty.getCells(1))
                .metadata(FUEL_VALUE, fuel.getRight())
                .addTo(BartWorksRecipeMaps.acidGenFuels);
        }
        List<Pair<Fluid, Integer>> liquidOtherFuels = Arrays.asList(
            ImmutablePair.of(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 320),
            ImmutablePair.of(GTPPFluids.IndustrialStrengthHydrogenChloride, 224),
            ImmutablePair.of(GTPPFluids.PropionicAcid, 150),
            ImmutablePair.of(Materials.Redstone.mStandardMoltenFluid, 40));
        for (Pair<Fluid, Integer> otherFuel : liquidOtherFuels) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    FluidContainerRegistry
                        .fillFluidContainer(new FluidStack(otherFuel.getLeft(), 1000), ItemList.Cell_Empty.get(1L)))
                .itemOutputs(Materials.Empty.getCells(1))
                .metadata(FUEL_VALUE, otherFuel.getRight())
                .addTo(BartWorksRecipeMaps.acidGenFuels);
        }
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
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[1], 1, 0))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glass, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
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
                MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.dust, (int) (1)),
                WerkstoffLoader.Thorium232.get(dust))
            .outputChances(7000, 1300, 700, 600, 300, 100)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(sifterRecipes);

        // 3ThO2 + 4Al = 3Th + 2Al2O3
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.Thorianit.get(dust, 9),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, (int) (4)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, (int) (10)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ThO2 + 2Mg = Th + 2MgO
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.Thorianit.get(dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.dust, (int) (4)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Prasiolite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Amethyst, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 2))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 500)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.dust, (int) (40)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Amethyst, Materials2Shapes.dust, (int) (10)))
            .duration(40 * SECONDS)
            .eut(0)
            .metadata(ADDITIVE_AMOUNT, 6)
            .addTo(primitiveBlastRecipes);

        // Cubic Circonia
        // 2Y + 3O = Y2O3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Yttrium, Materials2Shapes.dust, (int) (2)))
            .circuit(5)
            .itemOutputs(WerkstoffLoader.YttriumOxide.get(dust, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .duration(3 * MINUTES + 24 * SECONDS + 16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr + 2O =Y22O3= ZrO2
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Zirconium.get(dust, 10), WerkstoffLoader.YttriumOxide.get(dust, 0))
            .itemOutputs(WerkstoffLoader.CubicZirconia.get(gemFlawed, 40))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (20_000)))
            .duration(48 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2953)
            .addTo(blastFurnaceRecipes);

        // Tellurium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.crushed, (int) (1)))
            .circuit(17)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tellurium, Materials2Shapes.nugget, (int) (2)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 722)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Plutonium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (3 * NUGGETS)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Beryllium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (3 * NUGGETS)))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.LiquidAir,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100_000_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (78_084_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (20_946_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Argon, Materials2FluidShapes.fluidGas, (int) (934_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (40_700)),
                WerkstoffLoader.Neon.getFluidOrGas(1_818),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (524)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (180)),
                WerkstoffLoader.Krypton.getFluidOrGas(114),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (55)),
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
                MaterialLibAPI.getStack(Materials2Materials.Sugar, Materials2Shapes.dustSmall, (int) (21)),
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dustTiny, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dustTiny, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.dustTiny, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dustTiny, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dustTiny, (int) (1)))
            .outputChances(100_00, 100_00, 10_00, 100_00, 10_00, 10_00)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Milk, Materials2FluidShapes.fluidLiquid, (int) (10_000)))
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
