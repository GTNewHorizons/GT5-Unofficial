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

import gregtech.api.enums.materials2.Materials;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.FluidLoader;
import bartworks.common.loaders.ItemRegistry;
import bartworks.util.BioCulture;
import bartworks.util.BioData;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.material.MaterialUtils;
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
                .getStack(Materials.Ethanol, Materials2CellShapes.cell, (int) (1));
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
        ItemStack resulting = ItemList.EmptyPetriDish.get(1)
            .setStackDisplayName("The Culture made from DNA");
        resulting.setTagInfo("NEI", new NBTTagByte((byte) 1));
        // Clonal Cellular Synthesis- [Liquid DNA] + Medium Petri Dish + Plasma Membrane + Stem Cells + Genome Data
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.EmptyPetriDish.get(1),
                ItemList.PlasmaMembrane.get(1),
                ItemList.Circuit_Chip_Stemcell.get(2L),
                Outp)
            .itemOutputs(resulting)
            .outputChances(75_00)
            .fluidInputs(GTModHandler.getLiquidDNA(8_000))
            .special(BioItemList.mBioLabParts[4])
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .ignoreCollision()
            .fake()
            .addTo(bioLabRecipes);

        FluidStack[] easyFluids = { GTUtility.getWater(1_000), GTModHandler.getDistilledWater(1_000) };
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
                            fluidStack.equals(GTUtility.getWater(1_000))
                                ? GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1)
                                : GTUtility.getContainersFromFluid(GTModHandler.getDistilledWater(1_000))
                                    .get(0))
                        .itemOutputs(
                            BioCultureEnum.getPetriDish(bioCulture),
                            GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
                        .outputChances(bioCulture.getChance(), 100_00)
                        .fluidInputs(new FluidStack(bioCulture.getFluid(), 1_000))
                        .duration(25 * SECONDS)
                        .eut(TierEU.RECIPE_HV)
                        .addTo(bioLabRecipes);
                }
            }
        }

        List<Pair<Material, Integer>> liquidFuels = Arrays.asList(
            ImmutablePair.of(Materials.PhosphoricAcidGT5U, 66),
            ImmutablePair.of(Materials.DilutedHydrochloricAcidGT5U, 26),
            ImmutablePair.of(Materials.HypochlorousAcid, 56),
            ImmutablePair.of(Materials.HydrofluoricAcidGT5U, 60),
            ImmutablePair.of(Materials.HydrochloricAcidGT5U, 52),
            ImmutablePair.of(Materials.NitricAcid, 72),
            ImmutablePair.of(Materials.Mercury, 32),
            ImmutablePair.of(Materials.DilutedSulfuricAcid, 14),
            ImmutablePair.of(Materials.SulfuricAcid, 28),
            ImmutablePair.of(Materials.AceticAcid, 21),
            ImmutablePair.of(Materials.FormicAcid, 40),
            ImmutablePair.of(Materials.HexafluorosilicicAcid, 350),
            ImmutablePair.of(Materials.phtalicacid, 270),
            ImmutablePair.of(Materials.NaphthenicAcid, 250),
            ImmutablePair.of(Materials.ChlorosulfonicAcid, 2304),
            ImmutablePair.of(Materials.FluoroantimonicAcid, 5760));
        for (Pair<Material, Integer> fuel : liquidFuels) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, fuel.getLeft(), 1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
                .metadata(FUEL_VALUE, fuel.getRight())
                .addTo(BartWorksRecipeMaps.acidGenFuels);
        }
        List<Pair<Fluid, Integer>> liquidOtherFuels = Arrays.asList(
            ImmutablePair.of(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 320),
            ImmutablePair.of(GTPPFluids.IndustrialStrengthHydrogenChloride, 224),
            ImmutablePair.of(GTPPFluids.PropionicAcid, 150),
            ImmutablePair.of(
                MaterialUtils.molten(Materials.Redstone, 1)
                    .getFluid(),
                40));
        for (Pair<Fluid, Integer> otherFuel : liquidOtherFuels) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    FluidContainerRegistry
                        .fillFluidContainer(new FluidStack(otherFuel.getLeft(), 1000), ItemList.Cell_Empty.get(1L)))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
                .metadata(FUEL_VALUE, otherFuel.getRight())
                .addTo(BartWorksRecipeMaps.acidGenFuels);
        }
    }

    public static void run() {
        runBWRecipes();

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RawAdemicSteel, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.AdemicSteel, Shapes.dust, 1))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(implosionRecipes);

        // Thorium/Yttrium Glass
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.YttriumOxide, Shapes.dustSmall, 2),
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dustSmall, 2))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[1], 1, 0))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Glass, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 3663)
            .addTo(blastFurnaceRecipes);

        // Thorianite recipes
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Thorianite, Shapes.crushedPurified, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 1))
            .outputChances(7000, 1300, 700, 600, 300, 100)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(sifterRecipes);

        // 3ThO2 + 4Al = 3Th + 2Al2O3
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (4)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, (int) (10)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ThO2 + 2Mg = Th + 2MgO
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, (int) (4)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Prasiolite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Amethyst, Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Prasiolite, Shapes.gemFlawed, 2))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 500)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, (int) (40)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Amethyst, Shapes.dust, (int) (10)))
            .duration(40 * SECONDS)
            .eut(0)
            .metadata(ADDITIVE_AMOUNT, 6)
            .addTo(primitiveBlastRecipes);

        // Cubic Circonia
        // 2Y + 3O = Y2O3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Yttrium, Shapes.dust, (int) (2)))
            .circuit(5)
            .itemOutputs(MaterialLibAPI.getStack(Materials.YttriumOxide, Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .duration(3 * MINUTES + 24 * SECONDS + 16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr + 2O =Y22O3= ZrO2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.YttriumOxide, Shapes.dust, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CubicZirconia, Shapes.gemFlawed, 40))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (20_000)))
            .duration(48 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 2953)
            .addTo(blastFurnaceRecipes);

        // Tellurium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lead, Shapes.crushed, (int) (1)))
            .circuit(17)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Lead, Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials.Tellurium, Shapes.nugget, (int) (2)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 722)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Plutonium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (3 * NUGGETS)),
                MaterialLibAPI.getFluidStack(
                    Materials.Beryllium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (3 * NUGGETS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Californium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (3 * NUGGETS)))
            .duration(12 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 480_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Californium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * NUGGETS)),
                MaterialLibAPI
                    .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidMolten, (int) (5 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oganesson, Materials2FluidShapes.fluidLiquid, (int) (720)))
            .duration(24 * SECONDS)
            .eut(49152)
            .metadata(FUSION_THRESHOLD, 600_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LiquidAir,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100_000_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (78_084_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (20_946_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, (int) (934_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (40_700)),
                MaterialLibAPI
                    .getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (1_818)),
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, (int) (524)),
                MaterialLibAPI.getFluidStack(Materials.Methane, Materials2FluidShapes.fluidGas, (int) (180)),
                MaterialLibAPI
                    .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (114)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (55)),
                MaterialLibAPI.getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (9)))
            .duration(6 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.gemChipped, 9))
            .outputChances(90_00)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.gem, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        // Milk

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sugar, Shapes.dustSmall, (int) (21)),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dustTiny, (int) (1)),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dustTiny, (int) (1)),
                MaterialLibAPI.getStack(Materials.Potassium, Shapes.dustTiny, (int) (1)),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dustTiny, (int) (4)),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dustTiny, (int) (1)))
            .outputChances(100_00, 100_00, 10_00, 100_00, 10_00, 10_00)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Milk, Materials2FluidShapes.fluidLiquid, (int) (10_000)))
            .fluidOutputs(GTUtility.getWater(8_832))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.bolt, 1))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 12_500)
            .addTo(smallNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.stick, 1))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 62_500)
            .addTo(largeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.stickLong, 1))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 125_000)
            .addTo(hugeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.stick, 1))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 31_250)
            .addTo(extremeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.stickLong, 1))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 125_000)
            .addTo(ultraHugeNaquadahReactorFuels);
    }
}
