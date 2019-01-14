/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.tileentities.GT_MetaTileEntity_BioLab;
import com.github.bartimaeusnek.bartworks.common.tileentities.GT_MetaTileEntity_RadioHatch;
import com.github.bartimaeusnek.bartworks.common.tileentities.GT_TileEntity_BioVat;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class BioRecipeLoader extends RecipeLoader {

    @Override
    public void run() {

        //DNAExtractionModule
        GT_ModHandler.addCraftingRecipe(
                BioItemList.mBioLabParts[0],
                RecipeLoader.BITSD,
                new Object[]{
                        "TET",
                        "CFC",
                        "TST",
                        'T', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L),
                        'E', ItemList.Emitter_EV.get(1L),
                        'C', GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L),
                        'S', ItemList.Sensor_EV.get(1L),
                        'F', ItemList.Field_Generator_EV.get(1L)
                }
        );

        //PCRThermoclyclingModule
        GT_ModHandler.addCraftingRecipe(
                BioItemList.mBioLabParts[1],
                RecipeLoader.BITSD,
                new Object[]{
                        "NEN",
                        "CFC",
                        "NSN",
                        'N', GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L),
                        'E', ItemList.Emitter_EV.get(1L),
                        'C', GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L),
                        'S', ItemList.Sensor_EV.get(1L),
                        'F', ItemList.Field_Generator_EV.get(1L)
                }
        );

        //PlasmidSynthesisModule
        GT_ModHandler.addCraftingRecipe(
                BioItemList.mBioLabParts[2],
                RecipeLoader.BITSD,
                new Object[]{
                        "SFE",
                        "CPC",
                        "NFN",
                        'N', GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L),
                        'C', "circuit" + Materials.Data,
                        'F', ItemList.Field_Generator_EV.get(1L),
                        'E', ItemList.Emitter_EV.get(1L),
                        'S', ItemList.Sensor_EV.get(1L),
                        'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L),
                }
        );
        //TransformationModule
        GT_ModHandler.addCraftingRecipe(
                BioItemList.mBioLabParts[3],
                RecipeLoader.BITSD,
                new Object[]{
                        "SFE",
                        "CPC",
                        "NFN",
                        'N', GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 1L),
                        'C', "circuit" + Materials.Master,
                        'F', ItemList.Field_Generator_LuV.get(1L),
                        'E', ItemList.Emitter_LuV.get(1L),
                        'S', ItemList.Sensor_LuV.get(1L),
                        'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 1L),
                }
        );

        //ClonalCellularSynthesisModule
        GT_ModHandler.addCraftingRecipe(
                BioItemList.mBioLabParts[4],
                RecipeLoader.BITSD,
                new Object[]{
                        "FEF",
                        "CPC",
                        "FSF",
                        'N', GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 1L),
                        'C', "circuit" + Materials.Master,
                        'F', ItemList.Field_Generator_LuV.get(1L),
                        'E', ItemList.Emitter_LuV.get(1L),
                        'S', ItemList.Sensor_LuV.get(1L),
                        'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 1L),
                }
        );

        if (Loader.isModLoaded("croploadcore"))
            for (int i = 0; i < OreDictionary.getOres("cropVine").size(); i++) {
                GT_Values.RA.addExtractorRecipe(OreDictionary.getOres("cropVine").get(i).splitStack(12), BioItemList.getOther(1), 500, BW_Util.getMachineVoltageFromTier(3));
            }
        else
            GT_Values.RA.addExtractorRecipe(new ItemStack(Blocks.vine, 12), BioItemList.getOther(1), 500, BW_Util.getMachineVoltageFromTier(3));

        GT_Values.RA.addExtractorRecipe(ItemList.Circuit_Chip_Stemcell.get(1L), BioItemList.getOther(4), 500, BW_Util.getMachineVoltageFromTier(6));

        FluidStack dnaFluid = Loader.isModLoaded("gendustry") ? FluidRegistry.getFluidStack("liquiddna", 1000) : Materials.Biomass.getFluid(1000L);
        GT_Values.RA.addMixerRecipe(GT_Utility.getIntegratedCircuit(17), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Radon, 1L), null, null, dnaFluid, new FluidStack(FluidLoader.BioLabFluidMaterials[0], 2000), Materials.Empty.getCells(1), 500, BW_Util.getMachineVoltageFromTier(3));

        GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(17), null, new FluidStack(BioCultureLoader.eColi.getFluid(), 1000), new FluidStack(FluidLoader.BioLabFluidMaterials[1], 10), BioItemList.getOther(4), null, null, null, null, null, new int[]{1000}, 60 * 20, BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(17), null, new FluidStack(FluidLoader.BioLabFluidMaterials[1], 1000), new FluidStack(FluidLoader.BioLabFluidMaterials[3], 250), null, null, null, null, null, null, null, 60 * 20, BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(17), null, new FluidStack(BioCultureLoader.CommonYeast.getFluid(), 1000), new FluidStack(FluidLoader.BioLabFluidMaterials[2], 10), null, null, null, null, null, null, null, 60 * 20, BW_Util.getMachineVoltageFromTier(3));


        ItemStack[] Pistons = {ItemList.Electric_Piston_HV.get(1L), ItemList.Electric_Piston_EV.get(1L), ItemList.Electric_Piston_IV.get(1L), ItemList.Electric_Piston_LuV.get(1L), ItemList.Electric_Piston_ZPM.get(1L), ItemList.Electric_Piston_UV.get(1L)};
        ItemStack[] BioLab = new ItemStack[GT_Values.VN.length - 3];
        ItemStack[] RadioHatch = new ItemStack[GT_Values.VN.length - 3];
        Materials[] cables = {Materials.Gold, Materials.Aluminium, Materials.Tungsten, Materials.VanadiumGallium, Materials.Naquadah, Materials.NaquadahAlloy, Materials.Superconductor};
        Materials[] hulls = {Materials.StainlessSteel, Materials.Titanium, Materials.TungstenSteel, Materials.Chrome, Materials.Iridium, Materials.Osmium, Materials.Naquadah};
        Materials[] wireMat = {Materials.Kanthal, Materials.Nichrome, Materials.TungstenSteel, Materials.Naquadah, Materials.NaquadahAlloy, Materials.Superconductor};
        Materials[] circuits = {Materials.Advanced, Materials.Data, Materials.Elite, Materials.Master, Materials.Ultimate, Materials.Superconductor};
        for (int i = 3; i < GT_Values.VN.length; i++) {
            //12625
            BioLab[(i - 3)] = new GT_MetaTileEntity_BioLab(ConfigHandler.IDOffset + GT_Values.VN.length * 6 + i, GT_Values.VN[i] + " Bio Lab", GT_Values.VN[i] + " Bio Lab", i, "The BioLab, a Multi-Use Bioengineering Station").getStackForm(1L);
            RadioHatch[(i - 3)] = new GT_MetaTileEntity_RadioHatch(ConfigHandler.IDOffset + GT_Values.VN.length * 7 - 2 + i, GT_Values.VN[i] + " Radio Hatch", GT_Values.VN[i] + " Radio Hatch", i).getStackForm(1L);
            try {
                ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);
                GT_ModHandler.addCraftingRecipe(
                        BioLab[(i - 3)],
                        RecipeLoader.BITSD,
                        new Object[]{
                                "PFP",
                                "WCW",
                                "OGO",
                                'F', GT_OreDictUnificator.get(OrePrefixes.frameGt, hulls[(i - 3)], 1L),
                                'W', GT_OreDictUnificator.get(OrePrefixes.wireGt01, wireMat[(i - 3)], 1L),
                                'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1L),
                                'O', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polystyrene, 1L),
                                'G', "circuit" + circuits[(i - 3)],
                                'C', machinehull
                        }
                );
                GT_ModHandler.addCraftingRecipe(
                        RadioHatch[(i - 3)],
                        RecipeLoader.BITSD,
                        new Object[]{
                                "DPD",
                                "DCD",
                                "DKD",
                                'D', GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 1L),
                                'C', machinehull,
                                'K', GT_OreDictUnificator.get(OrePrefixes.cableGt08, cables[(i - 3)], 1L),
                                'P', Pistons[(i - 3)]
                        }
                );
            } catch (ArrayIndexOutOfBoundsException e) {
                //e.printStackTrace();
            }
        }
        GT_ModHandler.addCraftingRecipe(
                new GT_TileEntity_BioVat(ConfigHandler.IDOffset + GT_Values.VN.length * 7, "BioVat", "BioVat").getStackForm(1L),
                RecipeLoader.BITSD,
                new Object[]{
                        "GCG",
                        "KHK",
                        "GCG",
                        'G', new ItemStack(BioItemList.bw_glasses[0], 1, 1),
                        'C', "circuit" + Materials.Data,
                        'K', GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Silver, 1L),
                        'H', ItemList.MACHINE_HULLS[3].get(1L)
                }
        );


        Materials[] sterilizers = {Materials.Ammonia, Materials.Chlorine, Materials.Ethanol, Materials.Methanol};
        for (Materials used : sterilizers) {
            GT_Values.RA.addAutoclaveRecipe(
                    ItemList.Circuit_Parts_PetriDish.get(1L),
                    used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L),
                    BioItemList.getPetriDish(null),
                    10000,
                    100,
                    BW_Util.getMachineVoltageFromTier(1)
            );

            GT_Values.RA.addAutoclaveRecipe(
                    new ItemStack(Items.glass_bottle),
                    used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L),
                    BioItemList.getDNASampleFlask(null),
                    10000,
                    100,
                    BW_Util.getMachineVoltageFromTier(1)
            );
        }

        GT_Values.RA.addLaserEngraverRecipe(
                new ItemStack(Items.emerald),
                GT_Utility.getIntegratedCircuit(17),
                BioItemList.getPlasmidCell(null),
                100,
                BW_Util.getMachineVoltageFromTier(1)

        );


        FluidStack[] easyFluids = {Materials.Water.getFluid(1000L), FluidRegistry.getFluidStack("ic2distilledwater", 1000)};
        for (FluidStack fluidStack : easyFluids) {

            BWRecipes.instance.addBioLabRecipeIncubation(
                    new ItemStack(Items.rotten_flesh),
                    BioCultureLoader.rottenFleshBacteria,
                    new int[]{3300},
                    new FluidStack[]{fluidStack},
                    500,
                    BW_Util.getMachineVoltageFromTier(3),
                    BW_Util.STANDART
            );

            BWRecipes.instance.addBioLabRecipeIncubation(
                    new ItemStack(Items.fermented_spider_eye),
                    BioCultureLoader.eColi,
                    new int[]{4500},
                    new FluidStack[]{fluidStack},
                    500,
                    BW_Util.getMachineVoltageFromTier(3),
                    BW_Util.STANDART
            );

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Food_Dough.get(1L),
                    BioCultureLoader.CommonYeast,
                    new int[]{7500},
                    new FluidStack[]{fluidStack},
                    500,
                    BW_Util.getMachineVoltageFromTier(3),
                    BW_Util.STANDART
            );

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Food_Dough_Sugar.get(1L),
                    BioCultureLoader.WhineYeast,
                    new int[]{2500},
                    new FluidStack[]{fluidStack},
                    500,
                    BW_Util.getMachineVoltageFromTier(3),
                    BW_Util.STANDART
            );

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Bottle_Wine.get(1L),
                    BioCultureLoader.WhineYeast,
                    new int[]{3300},
                    new FluidStack[]{fluidStack},
                    500,
                    BW_Util.getMachineVoltageFromTier(3),
                    BW_Util.STANDART
            );

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Bottle_Beer.get(1L),
                    BioCultureLoader.BeerYeast,
                    new int[]{2500},
                    new FluidStack[]{fluidStack},
                    500,
                    BW_Util.getMachineVoltageFromTier(3),
                    BW_Util.STANDART
            );

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Bottle_Dark_Beer.get(1L),
                    BioCultureLoader.BeerYeast,
                    new int[]{3300},
                    new FluidStack[]{fluidStack},
                    500,
                    BW_Util.getMachineVoltageFromTier(3),
                    BW_Util.STANDART
            );

            BWRecipes.instance.addBacterialVatRecipe(
                    new ItemStack[]{new ItemStack(Items.sugar, 64)},
                    new FluidStack[]{new FluidStack(fluidStack, 100)},
                    BioCulture.BIO_CULTURE_ARRAY_LIST.get(1),
                    new FluidStack[]{(Loader.isModLoaded("berriespp") ? FluidRegistry.getFluidStack("potion.GHP", 1) : Materials.Ethanol.getFluid(1L))},
                    350,
                    BW_Util.getMachineVoltageFromTier(4)
            );

            BWRecipes.instance.addBacterialVatRecipe(
                    new ItemStack[]{ItemList.Crop_Drop_Grapes.get(16)},
                    new FluidStack[]{new FluidStack(fluidStack, 100)},
                    BioCulture.BIO_CULTURE_ARRAY_LIST.get(2),
                    new FluidStack[]{FluidRegistry.getFluidStack("potion.wine", 12)},
                    200,
                    BW_Util.getMachineVoltageFromTier(2)
            );

            BWRecipes.instance.addBacterialVatRecipe(
                    new ItemStack[]{new ItemStack(Items.sugar, 4), ItemList.IC2_Hops.get(16L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 8L)},
                    new FluidStack[]{new FluidStack(fluidStack, 100)},
                    BioCulture.BIO_CULTURE_ARRAY_LIST.get(3),
                    new FluidStack[]{FluidRegistry.getFluidStack("potion.beer", 5)},
                    600,
                    BW_Util.getMachineVoltageFromTier(1)
            );
            BWRecipes.instance.addBacterialVatRecipe(
                    new ItemStack[]{ItemList.IC2_Hops.get(32L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 16L)},
                    new FluidStack[]{new FluidStack(fluidStack, 100)},
                    BioCulture.BIO_CULTURE_ARRAY_LIST.get(3),
                    new FluidStack[]{FluidRegistry.getFluidStack("potion.darkbeer", 10)},
                    600,
                    BW_Util.getMachineVoltageFromTier(1)
            );

        }

        BWRecipes.instance.addBacterialVatRecipe(
                null,
                new FluidStack[]{FluidRegistry.getFluidStack("potion.grapejuice", 100)},
                BioCulture.BIO_CULTURE_ARRAY_LIST.get(2),
                new FluidStack[]{FluidRegistry.getFluidStack("potion.wine", 12)},
                400,
                BW_Util.getMachineVoltageFromTier(1)
        );

        GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 0), Materials.Nickel.getMolten(5184), new ItemStack(BioItemList.bw_glasses[0], 1, 1), 800, BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 1), Materials.Tungsten.getMolten(1296), new ItemStack(BioItemList.bw_glasses[0], 1, 2), 800, BW_Util.getMachineVoltageFromTier(5));
        GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 2), Materials.Chrome.getMolten(1296), new ItemStack(BioItemList.bw_glasses[0], 1, 3), 800, BW_Util.getMachineVoltageFromTier(6));
        GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 3), Materials.Iridium.getMolten(3888), new ItemStack(BioItemList.bw_glasses[0], 1, 4), 800, BW_Util.getMachineVoltageFromTier(7));
        GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 4), Materials.Osmium.getMolten(1296), new ItemStack(BioItemList.bw_glasses[0], 1, 5), 800, BW_Util.getMachineVoltageFromTier(8));

        for (int i = 0; i < Dyes.dyeBrown.getSizeOfFluidList(); ++i) {
            GT_Values.RA.addChemicalBathRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 0), Dyes.dyeRed.getFluidDye(i, 36), new ItemStack(BioItemList.bw_glasses[0], 1, 6), null, null, null, 64, 2);
            GT_Values.RA.addChemicalBathRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 0), Dyes.dyeGreen.getFluidDye(i, 36), new ItemStack(BioItemList.bw_glasses[0], 1, 7), null, null, null, 64, 2);
            GT_Values.RA.addChemicalBathRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 0), Dyes.dyePurple.getFluidDye(i, 36), new ItemStack(BioItemList.bw_glasses[0], 1, 8), null, null, null, 64, 2);
            GT_Values.RA.addChemicalBathRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 0), Dyes.dyeYellow.getFluidDye(i, 36), new ItemStack(BioItemList.bw_glasses[0], 1, 9), null, null, null, 64, 2);
            GT_Values.RA.addChemicalBathRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 0), Dyes.dyeLime.getFluidDye(i, 36), new ItemStack(BioItemList.bw_glasses[0], 1, 10), null, null, null, 64, 2);
            GT_Values.RA.addChemicalBathRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 0), Dyes.dyeBrown.getFluidDye(i, 36), new ItemStack(BioItemList.bw_glasses[0], 1, 11), null, null, null, 64, 2);
        }
        //and reverse recipes... cause im nice :P
        GT_Values.RA.addPulveriserRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 1), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nickel.getDust(36)}, null, 800, BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addPulveriserRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 2), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nickel.getDust(36), Materials.Tungsten.getDust(9)}, null, 800, BW_Util.getMachineVoltageFromTier(5));
        GT_Values.RA.addPulveriserRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 3), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nichrome.getDust(45), Materials.Tungsten.getDust(9)}, null, 800, BW_Util.getMachineVoltageFromTier(6));
        GT_Values.RA.addPulveriserRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 4), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nichrome.getDust(45), Materials.Tungsten.getDust(9), Materials.Iridium.getDust(27)}, null, 800, BW_Util.getMachineVoltageFromTier(7));
        GT_Values.RA.addPulveriserRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, 5), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nichrome.getDust(45), Materials.Tungsten.getDust(9), Materials.Osmiridium.getDust(36)}, null, 800, BW_Util.getMachineVoltageFromTier(8));

        for (int i = 6; i < 11; i++) {
            GT_Values.RA.addPulveriserRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, i), new ItemStack[]{Materials.BorosilicateGlass.getDust(9)}, null, 400, BW_Util.getMachineVoltageFromTier(1));
            GT_Values.RA.addChemicalBathRecipe(new ItemStack(BioItemList.bw_glasses[0], 1, i), Materials.Chlorine.getGas(50), new ItemStack(BioItemList.bw_glasses[0], 1, 0), null, null, null, 64, 2);
        }


    }
}
