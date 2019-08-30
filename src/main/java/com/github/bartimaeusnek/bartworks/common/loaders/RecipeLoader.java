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
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_LESU;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ManualTrafo;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_THTR;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_Windmill;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler.newStuff;

public class RecipeLoader implements Runnable {

    protected static final long BITSD = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE;

    @Override
    public void run() {

        if (ConfigHandler.GTNH) {
            /*
             * GTNH "hardmode" Recipes
             */

            GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(Blocks.lapis_block), Materials.Iron.getMolten(1296L), new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0), 100, BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0), Materials.Lapis.getPlates(9), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2L), GT_Utility.getIntegratedCircuit(17)}, FluidRegistry.getFluidStack("ic2coolant", 1000), new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), 100, BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), Materials.Lapis.getBlocks(8), GT_Utility.getIntegratedCircuit(17)}, GT_Values.NF, new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, BW_Util.getMachineVoltageFromTier(3));
        } else {
            /*
             * Vanilla Recipes
             */


            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{Materials.Lapis.getBlocks(8), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1L), GT_Utility.getIntegratedCircuit(17)}, GT_Values.NF, new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, BW_Util.getMachineVoltageFromTier(1));

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    RecipeLoader.BITSD,
                    new Object[]{
                            "LLL",
                            "LCL",
                            "LLL",
                            'L', Materials.Lapis.getBlocks(1),
                            'C', "circuitBasic"
                    });

            GT_Values.RA.addCutterRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[1]), new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 1), GT_Values.NI, 100, BW_Util.getMachineVoltageFromTier(1));
            GT_Values.RA.addCompressorRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 1), new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, BW_Util.getMachineVoltageFromTier(1));
            GT_Values.RA.addCompressorRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 0), new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, BW_Util.getMachineVoltageFromTier(1));
            GT_ModHandler.addShapelessCraftingRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0), RecipeLoader.BITSD, new Object[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1)});
            GT_ModHandler.addShapelessCraftingRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), RecipeLoader.BITSD, new Object[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0)});
        }

        /*
         * Common Recipes
         */

        GT_ModHandler.addCraftingRecipe(
                new GT_TileEntity_LESU(ConfigHandler.IDOffset, "LESU", "L.E.S.U.").getStackForm(1L),
                RecipeLoader.BITSD,
                new Object[]{
                        "CDC",
                        "SBS",
                        "CFC",
                        'C', GT_OreDictUnificator.get(OrePrefixes.circuit, ConfigHandler.GTNH ? Materials.Advanced : Materials.Basic, 1L),
                        'D', ItemList.Cover_Screen.get(1L),
                        'S', GT_OreDictUnificator.get(OrePrefixes.cableGt12, ConfigHandler.GTNH ? Materials.Platinum : Materials.AnnealedCopper, 1L),
                        'B', new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                        'F', ConfigHandler.GTNH ? ItemList.Field_Generator_HV.get(1L) : ItemList.Field_Generator_LV.get(1L)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.DESTRUCTOPACK),
                RecipeLoader.BITSD,
                new Object[]{
                        "CPC",
                        "PLP",
                        "CPC",
                        'C', GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                        'P', GT_OreDictUnificator.get(ConfigHandler.GTNH ? OrePrefixes.plateDouble : OrePrefixes.plate, Materials.Aluminium, 1L),
                        'L', new ItemStack(Items.lava_bucket)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.DESTRUCTOPACK),
                RecipeLoader.BITSD,
                new Object[]{
                        "CPC",
                        "PLP",
                        "CPC",
                        'C', GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                        'P', GT_OreDictUnificator.get(ConfigHandler.GTNH ? OrePrefixes.plateDouble : OrePrefixes.plate, ConfigHandler.GTNH ? Materials.Steel : Materials.Iron, 1L),
                        'L', new ItemStack(Items.lava_bucket)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.ROCKCUTTER_MV),
                RecipeLoader.BITSD,
                new Object[]{
                        "DS ",
                        "DP ",
                        "DCB",
                        'D', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                        'S', GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 1L),
                        'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1L),
                        'C', "circuitGood",
                        'B', ItemList.IC2_AdvBattery.get(1L)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.ROCKCUTTER_LV),
                RecipeLoader.BITSD,
                new Object[]{
                        "DS ",
                        "DP ",
                        "DCB",
                        'D', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                        'S', GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1L),
                        'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L),
                        'C', "circuitBasic",
                        'B', ItemList.IC2_ReBattery.get(1L)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.ROCKCUTTER_HV),
                RecipeLoader.BITSD,
                new Object[]{
                        "DS ",
                        "DP ",
                        "DCB",
                        'D', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                        'S', GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 1L),
                        'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                        'C', "circuitAdvanced",
                        'B', ItemList.IC2_EnergyCrystal.get(1L)
                });

        if (ConfigHandler.teslastaff)
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.TESLASTAFF),
                    RecipeLoader.BITSD,
                    new Object[]{
                            "BO ",
                            "OP ",
                            "  P",
                            'O', GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Superconductor, 1L),
                            'B', ItemList.Energy_LapotronicOrb.get(1L),
                            'P', "plateAlloyIridium",
                    });

        if (newStuff) {

            GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.PUMPPARTS, 1, 0),//tube
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            " fG",
                            " G ",
                            "G  ",
                            'G', ItemList.Circuit_Parts_Glass_Tube.get(1L)
                    }
            );
            GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.PUMPPARTS, 1, 1),//motor
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "GLP",
                            "LSd",
                            "PfT",
                            'G', GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 1L),
                            'L', GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 1L),
                            'S', GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 1L),
                            'P', new ItemStack(Blocks.piston),
                            'T', new ItemStack(ItemRegistry.PUMPPARTS, 1, 0)
                    }
            );
            GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.PUMPBLOCK, 1, 0),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "IPI",
                            "PMP",
                            "ISI",
                            'I', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                            'P', GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Wood, 1L),
                            'M', new ItemStack(ItemRegistry.PUMPPARTS, 1, 1),
                            'S', Ic2Items.ironFurnace
                    }
            );

            if (!ConfigHandler.GTNH)
                GT_ModHandler.addCraftingRecipe(
                        ItemRegistry.dehp,
                        RecipeLoader.BITSD,
                        new Object[]{
                                "GPG",
                                "NCN",
                                "GPG",
                                'G', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 1L),
                                'P', ItemList.Pump_IV.get(1L),
                                'N', GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 1L),
                                'C', ItemList.MACHINE_HULLS[5],
                        }
                );
            else
                GT_Values.RA.addAssemblylineRecipe(ItemList.Pump_IV.get(1L), 72000,
                        new ItemStack[]{
                                ItemList.Pump_IV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 32L),
                                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 16L),
                                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 16L),
                                ItemList.Field_Generator_LuV.get(8)
                        },
                        new FluidStack[]{
                                Materials.SolderingAlloy.getMolten(32 * 144),
                                Materials.Polytetrafluoroethylene.getMolten(32 * 144)
                        }, ItemRegistry.dehp, 5000, BW_Util.getMachineVoltageFromTier(6));

            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 1000), GT_Utility.getIntegratedCircuit(17), Materials.SolderingAlloy.getMolten(9216), ItemRegistry.megaMachines[0], 72000, BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 1002), GT_Utility.getIntegratedCircuit(17), Materials.SolderingAlloy.getMolten(9216), ItemRegistry.megaMachines[1], 72000, BW_Util.getMachineVoltageFromTier(3));

            GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), Materials.Nickel.getMolten(5184), new ItemStack(ItemRegistry.bw_glasses[0], 1, 1), 800, BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 1), Materials.Tungsten.getMolten(1296), new ItemStack(ItemRegistry.bw_glasses[0], 1, 2), 800, BW_Util.getMachineVoltageFromTier(5));
            GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 2), Materials.Chrome.getMolten(1296), new ItemStack(ItemRegistry.bw_glasses[0], 1, 3), 800, BW_Util.getMachineVoltageFromTier(6));
            GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 3), Materials.Iridium.getMolten(3888), new ItemStack(ItemRegistry.bw_glasses[0], 1, 4), 800, BW_Util.getMachineVoltageFromTier(7));
            GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 4), Materials.Osmium.getMolten(1296), new ItemStack(ItemRegistry.bw_glasses[0], 1, 5), 800, BW_Util.getMachineVoltageFromTier(8));

            for (int i = 0; i < Dyes.dyeBrown.getSizeOfFluidList(); ++i) {
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), Dyes.dyeRed.getFluidDye(i, 36), new ItemStack(ItemRegistry.bw_glasses[0], 1, 6), null, null, null, 64, 2);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), Dyes.dyeGreen.getFluidDye(i, 36), new ItemStack(ItemRegistry.bw_glasses[0], 1, 7), null, null, null, 64, 2);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), Dyes.dyePurple.getFluidDye(i, 36), new ItemStack(ItemRegistry.bw_glasses[0], 1, 8), null, null, null, 64, 2);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), Dyes.dyeYellow.getFluidDye(i, 36), new ItemStack(ItemRegistry.bw_glasses[0], 1, 9), null, null, null, 64, 2);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), Dyes.dyeLime.getFluidDye(i, 36), new ItemStack(ItemRegistry.bw_glasses[0], 1, 10), null, null, null, 64, 2);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), Dyes.dyeBrown.getFluidDye(i, 36), new ItemStack(ItemRegistry.bw_glasses[0], 1, 11), null, null, null, 64, 2);
            }
            //and reverse recipes... cause im nice :P
            GT_Values.RA.addPulveriserRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 1), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nickel.getDust(36)}, null, 800, BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addPulveriserRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 2), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nickel.getDust(36), Materials.Tungsten.getDust(9)}, null, 800, BW_Util.getMachineVoltageFromTier(5));
            GT_Values.RA.addPulveriserRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 3), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nichrome.getDust(45), Materials.Tungsten.getDust(9)}, null, 800, BW_Util.getMachineVoltageFromTier(6));
            GT_Values.RA.addPulveriserRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 4), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nichrome.getDust(45), Materials.Tungsten.getDust(9), Materials.Iridium.getDust(27)}, null, 800, BW_Util.getMachineVoltageFromTier(7));
            GT_Values.RA.addPulveriserRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, 5), new ItemStack[]{Materials.BorosilicateGlass.getDust(9), Materials.Nichrome.getDust(45), Materials.Tungsten.getDust(9), Materials.Osmiridium.getDust(36)}, null, 800, BW_Util.getMachineVoltageFromTier(8));

            for (int i = 6; i < 11; i++) {
                GT_Values.RA.addPulveriserRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, i), new ItemStack[]{Materials.BorosilicateGlass.getDust(9)}, null, 400, BW_Util.getMachineVoltageFromTier(1));
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(ItemRegistry.bw_glasses[0], 1, i), Materials.Chlorine.getGas(50), new ItemStack(ItemRegistry.bw_glasses[0], 1, 0), null, null, null, 64, 2);
            }


            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.WINDMETER),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "SWF",
                            "Sf ",
                            "Ss ",
                            'S', "stickWood",
                            'W', new ItemStack(Blocks.wool, 1, Short.MAX_VALUE),
                            'F', new ItemStack(Items.string),
                    }
            );

            Materials[] cables = {Materials.Lead, Materials.Tin, Materials.AnnealedCopper, Materials.Gold, Materials.Aluminium, Materials.Tungsten, Materials.VanadiumGallium, Materials.Naquadah, Materials.NaquadahAlloy, Materials.Superconductor};
            Materials[] hulls = {Materials.WroughtIron, Materials.Steel, Materials.Aluminium, Materials.StainlessSteel, Materials.Titanium, Materials.TungstenSteel, Materials.Chrome, Materials.Iridium, Materials.Osmium, Materials.Naquadah};
            ItemStack[] bats = {ItemList.Battery_Hull_LV.get(1L), ItemList.Battery_Hull_MV.get(1L), ItemList.Battery_Hull_HV.get(1L)};
            ItemStack[] chreac = {ItemList.Machine_MV_ChemicalReactor.get(1L), ItemList.Machine_HV_ChemicalReactor.get(1L), ItemList.Machine_EV_ChemicalReactor.get(1L)};

            for (int i = 0; i < 3; i++) {
                Materials cable = cables[i + 2];
                ItemStack machinehull = ItemList.MACHINE_HULLS[i + 2].get(1L);
                GT_ModHandler.addCraftingRecipe(
                        ItemRegistry.acidGens[i],
                        RecipeLoader.BITSD,
                        new Object[]{
                                "HRH",
                                "HCH",
                                "HKH",
                                'H', bats[i],
                                'K', GT_OreDictUnificator.get(OrePrefixes.cableGt01, cable, 1L),
                                'C', machinehull,
                                'R', chreac[i]
                        }
                );
            }

            for (int i = 0; i < GT_Values.VN.length; i++) {
                try {
                    Materials cable = cables[i];
                    Materials hull = hulls[i];
                    ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);

                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.energyDistributor[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "PWP",
                                    "WCW",
                                    "PWP",
                                    'W', GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            });
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode12A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt12, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode12A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt12, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode8A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt08, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode8A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt08, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode4A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt04, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode4A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt04, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode2A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt02, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode2A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WDW",
                                    "DCD",
                                    "PDP",
                                    'D', ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.cableGt02, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode16A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WHW",
                                    "DCD",
                                    "PDP",
                                    'H', ItemList.Circuit_Parts_Coil.get(1L),
                                    'D', ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.diode16A[i],
                            RecipeLoader.BITSD,
                            new Object[]{
                                    "WHW",
                                    "DCD",
                                    "PDP",
                                    'H', ItemList.Circuit_Parts_Coil.get(1L),
                                    'D', ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                                    'W', GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            }
                    );

                } catch (ArrayIndexOutOfBoundsException e) {
                    //e.printStackTrace();
                }
            }

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L), GT_Utility.getIntegratedCircuit(17)},
                    Materials.Plastic.getMolten(1152L),
                    new ItemStack(ItemRegistry.BW_BLOCKS[2], 1, 1),
                    20,
                    BW_Util.getMachineVoltageFromTier(3)
            );

            GT_ModHandler.addCraftingRecipe(
                    new GT_TileEntity_ManualTrafo(ConfigHandler.IDOffset + GT_Values.VN.length * 6 + 1, "bw.manualtrafo", StatCollector.translateToLocal("tile.manutrafo.name")).getStackForm(1L),
                    RecipeLoader.BITSD,
                    new Object[]{
                            "SCS",
                            "CHC",
                            "ZCZ",
                            'S', GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L),
                            'C', new ItemStack(ItemRegistry.BW_BLOCKS[2]),
                            'H', ItemList.Hull_HV.get(1L),
                            'Z', GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L)
                    }
            );

            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L), Materials.Aluminium.getPlates(1), ItemList.Circuit_Board_Plastic.get(1L), ItemList.Battery_RE_LV_Lithium.get(1L)}, Materials.SolderingAlloy.getMolten(288L), new ItemStack(ItemRegistry.CIRCUIT_PROGRAMMER), 600, BW_Util.getMachineVoltageFromTier(2));

            GT_ModHandler.addCraftingRecipe(
                    new GT_TileEntity_Windmill(ConfigHandler.IDOffset + GT_Values.VN.length * 6 + 2, "bw.windmill", StatCollector.translateToLocal("tile.bw.windmill.name")).getStackForm(1L),
                    RecipeLoader.BITSD,
                    new Object[]{
                            "BHB",
                            "WGW",
                            "BWB",
                            'B', new ItemStack(Blocks.brick_block),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                            'H', new ItemStack(Blocks.hopper),
                            'G', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                    }
            );

            String[] stones = {"stone", "stoneSmooth"};
            String[] granites = {"blockGranite", "stoneGranite", "Granite", "granite"};
            for (String granite : granites) {
                for (String stone : stones) {
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[]{
                                    "SSS",
                                    "DfD",
                                    " h ",
                                    'S', stone,
                                    'D', new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE),
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[]{
                                    "hDf",
                                    "SSS",
                                    'S', stone,
                                    'D', new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE),
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[]{
                                    "SSS",
                                    "DfD",
                                    " h ",
                                    'S', stone,
                                    'D', granite,
                            }
                    );
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[]{
                                    "hDf",
                                    "SSS",
                                    'S', stone,
                                    'D', granite,
                            }
                    );
                }
                GT_ModHandler.addCraftingRecipe(
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                        GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                        new Object[]{
                                "STS",
                                "h f",
                                "SBS",
                                'S', granite,
                                'T', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                                'B', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                        }
                );
            }
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "STS",
                            "h f",
                            "SBS",
                            'S', new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE),
                            'T', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                            'B', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WLs",
                            "WLh",
                            "WLf",
                            'L', new ItemStack(Items.leather),
                            'W', "logWood",
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WLs",
                            "WLh",
                            "WLf",
                            'L', new ItemStack(Blocks.carpet),
                            'W', "logWood",
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WLs",
                            "WLh",
                            "WLf",
                            'L', new ItemStack(Items.paper),
                            'W', "logWood",
                    }
            );

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WEs",
                            "WZh",
                            "WDf",
                            'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                            'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                            'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                            'W', "logWood",
                    }
            );

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WEs",
                            "WZh",
                            "WDf",
                            'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                            'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                            'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                            'W', "logWood",
                    }
            );

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WEs",
                            "WZh",
                            "WDf",
                            'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                            'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                            'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                            'W', "logWood",
                    }
            );

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WEs",
                            "WZh",
                            "WDf",
                            'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                            'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                            'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                            'W', "logWood",
                    }
            );

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "WEs",
                            "WZh",
                            "WDf",
                            'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                            'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                            'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                            'W', "logWood",
                    }
            );

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.LEATHER_ROTOR),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.WOOL_ROTOR),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.PAPER_ROTOR),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.COMBINED_ROTOR),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.ROTORBLOCK),
                    RecipeLoader.BITSD,
                    new Object[]{
                            "WRW",
                            "RGR",
                            "WRW",
                            'R', GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 1L),
                            'W', "plankWood",
                            'G', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_TileEntity_THTR.THTRMaterials.registerTHR_Recipes();
            GT_ModHandler.addCraftingRecipe(
                    ItemRegistry.thtr,
                    RecipeLoader.BITSD,
                    new Object[]{
                            "BZB",
                            "BRB",
                            "BZB",
                            'B',new ItemStack(GregTech_API.sBlockCasings3,1,12),
                            'R',GT_ModHandler.getModItem("IC2","blockGenerator",1,5),
                            'Z',"circuitUltimate"
                    }
            );

            GT_Values.RA.addAssemblylineRecipe(
                    ItemList.Machine_Multi_ImplosionCompressor.get(1L),24000,
                    new ItemStack[]{
                            ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                            Materials.Neutronium.getBlocks(5),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong,Materials.Osmium,64),
                            GT_OreDictUnificator.get(OrePrefixes.ring,Materials.Osmium,64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01,Materials.Superconductor,64),
                            ItemList.Electric_Piston_UV.get(64),
                    },
                    new FluidStack[]{
                            Materials.SolderingAlloy.getMolten(1440),
                            Materials.Osmium.getMolten(1440),
                            Materials.Neutronium.getMolten(1440)
                    },
                    ItemRegistry.eic.copy(),
                    240000,
                    BW_Util.getMachineVoltageFromTier(8)
            );
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.add(new BWRecipes.DynamicGTRecipe(false,new ItemStack[]{ItemList.Hatch_Input_HV.get(64),Materials.LiquidAir.getCells(1),GT_Utility.getIntegratedCircuit(17)},new ItemStack[]{ItemRegistry.compressedHatch.copy()},null,null,null,null,300, BW_Util.getMachineVoltageFromTier(3),0));
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.add(new BWRecipes.DynamicGTRecipe(false,new ItemStack[]{ItemList.Hatch_Output_HV.get(64),GT_Utility.getIntegratedCircuit(17)},new ItemStack[]{ItemRegistry.giantOutputHatch.copy()},null,null,null,null,300, BW_Util.getMachineVoltageFromTier(3),0));

            GT_Values.RA.addAssemblylineRecipe(
                    ItemList.Machine_LuV_CircuitAssembler.get(1L),24000,
                    new ItemStack[]{
                            ItemList.Machine_LuV_CircuitAssembler.get(1L),
                            ItemList.Robot_Arm_LuV.get(4L),
                            ItemList.Electric_Motor_LuV.get(4L),
                            ItemList.Field_Generator_LuV.get(1L),
                            ItemList.Emitter_LuV.get(1L),
                            ItemList.Sensor_LuV.get(1L),
                            Materials.Chrome.getPlates(8)
                    },
                    new FluidStack[]{
                            Materials.SolderingAlloy.getMolten(1440)
                    },
                    ItemRegistry.cal.copy(),
                    24000,
                    BW_Util.getMachineVoltageFromTier(6)
            );
        }
    }

}
