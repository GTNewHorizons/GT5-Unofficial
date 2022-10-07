/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

import static com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler.newStuff;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.*;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeLoader {

    protected static final long BITSD = GT_ModHandler.RecipeBits.DISMANTLEABLE
            | GT_ModHandler.RecipeBits.NOT_REMOVABLE
            | GT_ModHandler.RecipeBits.REVERSIBLE;

    @SuppressWarnings("deprecation")
    public static void run() {

        if (ConfigHandler.hardmode) {
            /*
             * GTNH "hardmode" Recipes
             */

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(Blocks.lapis_block),
                    Materials.Iron.getMolten(1296L),
                    new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0),
                    100,
                    BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0),
                        Materials.Lapis.getPlates(9),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2L),
                        GT_Utility.getIntegratedCircuit(17)
                    },
                    FluidRegistry.getFluidStack("ic2coolant", 1000),
                    new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1),
                    100,
                    BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1),
                        Materials.Lapis.getBlocks(8),
                        GT_Utility.getIntegratedCircuit(17)
                    },
                    GT_Values.NF,
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    100,
                    BW_Util.getMachineVoltageFromTier(3));
        } else {
            /*
             * Vanilla Recipes
             */

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        Materials.Lapis.getBlocks(8),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1L),
                        GT_Utility.getIntegratedCircuit(17)
                    },
                    GT_Values.NF,
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    100,
                    BW_Util.getMachineVoltageFromTier(1));

            GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[1]), RecipeLoader.BITSD, new Object[] {
                "LLL", "LCL", "LLL", 'L', Materials.Lapis.getBlocks(1), 'C', "circuitBasic"
            });

            GT_Values.RA.addCutterRecipe(
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 1),
                    GT_Values.NI,
                    100,
                    BW_Util.getMachineVoltageFromTier(1));
            GT_Values.RA.addCompressorRecipe(
                    new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 1),
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    100,
                    BW_Util.getMachineVoltageFromTier(1));
            GT_Values.RA.addCompressorRecipe(
                    new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 0),
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    100,
                    BW_Util.getMachineVoltageFromTier(1));
            GT_ModHandler.addShapelessCraftingRecipe(
                    new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0),
                    RecipeLoader.BITSD,
                    new Object[] {new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1)});
            GT_ModHandler.addShapelessCraftingRecipe(
                    new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1),
                    RecipeLoader.BITSD,
                    new Object[] {new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0)});
        }

        /*
         * Common Recipes
         */

        GT_ModHandler.addCraftingRecipe(
                new GT_TileEntity_LESU(ConfigHandler.IDOffset, "LESU", "L.E.S.U.").getStackForm(1L),
                RecipeLoader.BITSD,
                new Object[] {
                    "CDC",
                    "SBS",
                    "CFC",
                    'C',
                    ConfigHandler.hardmode ? "circuitAdvanced" : "circuitBasic",
                    'D',
                    ItemList.Cover_Screen.get(1L),
                    'S',
                    GT_OreDictUnificator.get(
                            OrePrefixes.cableGt12,
                            ConfigHandler.hardmode ? Materials.Platinum : Materials.AnnealedCopper,
                            1L),
                    'B',
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    'F',
                    ConfigHandler.hardmode ? ItemList.Field_Generator_HV.get(1L) : ItemList.Field_Generator_LV.get(1L)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.DESTRUCTOPACK), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                    "CPC",
                    "PLP",
                    "CPC",
                    'C',
                    "circuitAdvanced",
                    'P',
                    GT_OreDictUnificator.get(
                            ConfigHandler.hardmode ? OrePrefixes.plateDouble : OrePrefixes.plate,
                            Materials.Aluminium,
                            1L),
                    'L',
                    new ItemStack(Items.lava_bucket)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.DESTRUCTOPACK), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                    "CPC",
                    "PLP",
                    "CPC",
                    'C',
                    "circuitAdvanced",
                    'P',
                    GT_OreDictUnificator.get(
                            ConfigHandler.hardmode ? OrePrefixes.plateDouble : OrePrefixes.plate,
                            ConfigHandler.hardmode ? Materials.Steel : Materials.Iron,
                            1L),
                    'L',
                    new ItemStack(Items.lava_bucket)
                });

        GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.ROCKCUTTER_MV), RecipeLoader.BITSD, new Object[] {
            "DS ",
            "DP ",
            "DCB",
            'D',
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
            'S',
            GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 1L),
            'P',
            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1L),
            'C',
            "circuitGood",
            'B',
            ItemList.IC2_AdvBattery.get(1L)
        });

        GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.ROCKCUTTER_LV), RecipeLoader.BITSD, new Object[] {
            "DS ",
            "DP ",
            "DCB",
            'D',
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
            'S',
            GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1L),
            'P',
            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L),
            'C',
            "circuitBasic",
            'B',
            ItemList.IC2_ReBattery.get(1L)
        });

        GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.ROCKCUTTER_HV), RecipeLoader.BITSD, new Object[] {
            "DS ",
            "DP ",
            "DCB",
            'D',
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
            'S',
            GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 1L),
            'P',
            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
            'C',
            "circuitAdvanced",
            'B',
            ItemList.IC2_EnergyCrystal.get(1L)
        });

        if (ConfigHandler.teslastaff)
            GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.TESLASTAFF), RecipeLoader.BITSD, new Object[] {
                "BO ",
                "OP ",
                "  P",
                'O',
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Superconductor, 1L),
                'B',
                ItemList.Energy_LapotronicOrb.get(1L),
                'P',
                "plateAlloyIridium",
            });

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        if (newStuff) {

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.PUMPPARTS, 1, 0), // tube
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {" fG", " G ", "G  ", 'G', ItemList.Circuit_Parts_Glass_Tube.get(1L)});
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.PUMPPARTS, 1, 1), // motor
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "GLP",
                        "LSd",
                        "PfT",
                        'G',
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 1L),
                        'L',
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 1L),
                        'S',
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 1L),
                        'P',
                        new ItemStack(Blocks.piston),
                        'T',
                        new ItemStack(ItemRegistry.PUMPPARTS, 1, 0)
                    });
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.PUMPBLOCK, 1, 0), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                        "IPI",
                        "PMP",
                        "ISI",
                        'I',
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                        'P',
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Wood, 1L),
                        'M',
                        new ItemStack(ItemRegistry.PUMPPARTS, 1, 1),
                        'S',
                        Ic2Items.ironFurnace
                    });

            if (!ConfigHandler.hardmode)
                GT_ModHandler.addCraftingRecipe(ItemRegistry.dehp, RecipeLoader.BITSD, new Object[] {
                    "GPG",
                    "NCN",
                    "GPG",
                    'G',
                    GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 1L),
                    'P',
                    ItemList.Pump_IV.get(1L),
                    'N',
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 1L),
                    'C',
                    ItemList.MACHINE_HULLS[5],
                });
            else
                GT_Values.RA.addAssemblylineRecipe(
                        ItemList.Pump_IV.get(1L),
                        72000,
                        new ItemStack[] {
                            ItemList.Pump_IV.get(16),
                            GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 32L),
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 16L),
                            ItemList.Field_Generator_LuV.get(8)
                        },
                        new FluidStack[] {
                            new FluidStack(solderIndalloy, 32 * 144),
                            Materials.Polytetrafluoroethylene.getMolten(32 * 144)
                        },
                        ItemRegistry.dehp,
                        5000,
                        BW_Util.getMachineVoltageFromTier(6));

            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 1000),
                    GT_Utility.getIntegratedCircuit(17),
                    Materials.SolderingAlloy.getMolten(9216),
                    ItemRegistry.megaMachines[0],
                    72000,
                    BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 1002),
                    GT_Utility.getIntegratedCircuit(17),
                    Materials.SolderingAlloy.getMolten(9216),
                    ItemRegistry.megaMachines[1],
                    72000,
                    BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 1126),
                    GT_Utility.getIntegratedCircuit(17),
                    Materials.SolderingAlloy.getMolten(9216),
                    ItemRegistry.megaMachines[2],
                    72000,
                    BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 1169),
                    GT_Utility.getIntegratedCircuit(17),
                    Materials.SolderingAlloy.getMolten(9216),
                    ItemRegistry.megaMachines[3],
                    72000,
                    BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 64, 1160),
                    GT_Utility.getIntegratedCircuit(17),
                    Materials.SolderingAlloy.getMolten(9216),
                    ItemRegistry.megaMachines[4],
                    72000,
                    BW_Util.getMachineVoltageFromTier(3));

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.Titanium.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 1),
                    800,
                    BW_Util.getMachineVoltageFromTier(3));
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.TungstenSteel.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 2),
                    800,
                    BW_Util.getMachineVoltageFromTier(4));
            FluidStack LuVMaterialFluid = ConfigHandler.newStuff
                    ? WerkstoffLoader.LuVTierMaterial.getMolten(1152)
                    : Materials.Chrome.getMolten(1152);
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    LuVMaterialFluid,
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 3),
                    800,
                    BW_Util.getMachineVoltageFromTier(5));
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.Iridium.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 4),
                    800,
                    BW_Util.getMachineVoltageFromTier(6));
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.Osmium.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 5),
                    800,
                    BW_Util.getMachineVoltageFromTier(7));
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.Neutronium.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 13),
                    800,
                    BW_Util.getMachineVoltageFromTier(8));
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.CosmicNeutronium.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 14),
                    800,
                    BW_Util.getMachineVoltageFromTier(9));
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.Infinity.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 15),
                    800,
                    BW_Util.getMachineVoltageFromTier(10));
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Materials.TranscendentMetal.getMolten(1152),
                    new ItemStack(ItemRegistry.bw_glasses[1], 1, 0),
                    800,
                    BW_Util.getMachineVoltageFromTier(11));

            for (int i = 0; i < Dyes.dyeBrown.getSizeOfFluidList(); ++i) {
                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                        Dyes.dyeRed.getFluidDye(i, 36),
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 6),
                        null,
                        null,
                        null,
                        64,
                        2);
                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                        Dyes.dyeGreen.getFluidDye(i, 36),
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 7),
                        null,
                        null,
                        null,
                        64,
                        2);
                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                        Dyes.dyePurple.getFluidDye(i, 36),
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 8),
                        null,
                        null,
                        null,
                        64,
                        2);
                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                        Dyes.dyeYellow.getFluidDye(i, 36),
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 9),
                        null,
                        null,
                        null,
                        64,
                        2);
                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                        Dyes.dyeLime.getFluidDye(i, 36),
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 10),
                        null,
                        null,
                        null,
                        64,
                        2);
                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                        Dyes.dyeBrown.getFluidDye(i, 36),
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 11),
                        null,
                        null,
                        null,
                        64,
                        2);
            }
            // and reverse recipes... cause im nice :P
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 1),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.Titanium.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(4));
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 2),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.TungstenSteel.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(5));
            ItemStack LuVMaterialDust = ConfigHandler.newStuff
                    ? WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 8)
                    : Materials.Chrome.getDust(8);
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 3),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), LuVMaterialDust},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(6));
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 4),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.Iridium.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(7));
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 5),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.Osmium.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(8));
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 13),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.Neutronium.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(9));
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 14),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.CosmicNeutronium.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(10));
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 15),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.Infinity.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(11));
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[1], 1, 0),
                    new ItemStack[] {Materials.BorosilicateGlass.getDust(9), Materials.TranscendentMetal.getDust(8)},
                    null,
                    800,
                    BW_Util.getMachineVoltageFromTier(12));

            for (int i = 6; i < 11; i++) {
                GT_Values.RA.addPulveriserRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, i),
                        new ItemStack[] {Materials.BorosilicateGlass.getDust(9)},
                        null,
                        400,
                        BW_Util.getMachineVoltageFromTier(1));
                GT_Values.RA.addChemicalBathRecipe(
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, i),
                        Materials.Chlorine.getGas(50),
                        new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                        null,
                        null,
                        null,
                        64,
                        2);
            }

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.WINDMETER), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                        "SWF",
                        "Sf ",
                        "Ss ",
                        'S',
                        "stickWood",
                        'W',
                        new ItemStack(Blocks.wool, 1, Short.MAX_VALUE),
                        'F',
                        new ItemStack(Items.string),
                    });

            Materials[] cables = { // Cable material used in the acid gen, diode and energy distributor below
                Materials.Lead, // ULV
                Materials.Tin, // LV
                Materials.AnnealedCopper, // MV
                Materials.Gold, // HV
                Materials.Aluminium, // EV
                Materials.Tungsten, // IV
                Materials.VanadiumGallium, // LuV
                Materials.Naquadah, // ZPM
                Materials.NaquadahAlloy, // UV
                Materials.SuperconductorUV // UHV
            };

            ISubTagContainer[] hulls = { // Plate material used in the acid gen, diode and energy distributor below
                Materials.WroughtIron, // ULV
                Materials.Steel, // LV
                Materials.Aluminium, // MV
                Materials.StainlessSteel, // HV
                Materials.Titanium, // EV
                Materials.TungstenSteel, // IV
                WerkstoffLoader.LuVTierMaterial, // LuV
                Materials.Iridium, // ZPM
                Materials.Osmium, // UV
                Materials.Naquadah // UHV
            };

            ItemStack[] bats = {
                ItemList.Battery_Hull_LV.get(1L), ItemList.Battery_Hull_MV.get(1L), ItemList.Battery_Hull_HV.get(1L)
            };
            ItemStack[] chreac = {
                ItemList.Machine_MV_ChemicalReactor.get(1L),
                ItemList.Machine_HV_ChemicalReactor.get(1L),
                ItemList.Machine_EV_ChemicalReactor.get(1L)
            };

            for (int i = 0; i < 3; i++) {
                Materials cable = cables[i + 2];
                ItemStack machinehull = ItemList.MACHINE_HULLS[i + 2].get(1L);
                GT_ModHandler.addCraftingRecipe(ItemRegistry.acidGens[i], RecipeLoader.BITSD, new Object[] {
                    "HRH",
                    "HCH",
                    "HKH",
                    'H',
                    bats[i],
                    'K',
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, cable, 1L),
                    'C',
                    machinehull,
                    'R',
                    chreac[i]
                });
            }

            GT_ModHandler.addCraftingRecipe(ItemRegistry.acidGensLV, RecipeLoader.BITSD, new Object[] {
                "HRH",
                "KCK",
                "HKH",
                'H',
                ItemList.Battery_Hull_LV.get(1L),
                'K',
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L),
                'C',
                ItemList.Hull_LV.get(1L),
                'R',
                ItemList.Machine_LV_ChemicalReactor.get(1L),
            });

            for (int i = 0; i < GT_Values.VN.length - 1; i++) {
                try {
                    Materials cable = cables[i];
                    ItemStack hull = hulls[i] instanceof Materials
                            ? GT_OreDictUnificator.get(OrePrefixes.plate, hulls[i], 1L)
                            : ((Werkstoff) hulls[i]).get(OrePrefixes.plate);
                    ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);

                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.energyDistributor[i], RecipeLoader.BITSD, new Object[] {
                                "PWP",
                                "WCW",
                                "PWP",
                                'W',
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                                'P',
                                hull,
                                'C',
                                machinehull
                            });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode12A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt12, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode12A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt12, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode8A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt08, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode8A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt08, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode4A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode4A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode2A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode2A[i], RecipeLoader.BITSD, new Object[] {
                        "WDW",
                        "DCD",
                        "PDP",
                        'D',
                        ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode16A[i], RecipeLoader.BITSD, new Object[] {
                        "WHW",
                        "DCD",
                        "PDP",
                        'H',
                        ItemList.Circuit_Parts_Coil.get(1L),
                        'D',
                        ItemList.Circuit_Parts_Diode.get(1L, ItemList.Circuit_Parts_DiodeSMD.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });
                    GT_ModHandler.addCraftingRecipe(ItemRegistry.diode16A[i], RecipeLoader.BITSD, new Object[] {
                        "WHW",
                        "DCD",
                        "PDP",
                        'H',
                        ItemList.Circuit_Parts_Coil.get(1L),
                        'D',
                        ItemList.Circuit_Parts_DiodeSMD.get(1L, ItemList.Circuit_Parts_Diode.get(1L)),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                        'P',
                        hull,
                        'C',
                        machinehull
                    });

                } catch (ArrayIndexOutOfBoundsException e) {
                    // e.printStackTrace();
                }
            }

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L),
                        GT_Utility.getIntegratedCircuit(17)
                    },
                    Materials.Plastic.getMolten(1152L),
                    new ItemStack(ItemRegistry.BW_BLOCKS[2], 1, 1),
                    20,
                    BW_Util.getMachineVoltageFromTier(3));

            GT_ModHandler.addCraftingRecipe(
                    /*Loader.isModLoaded("tectech") ? new TT_TileEntity_ManualTrafo(ConfigHandler.IDOffset + GT_Values.VN.length * 6 + 1, "bw.manualtrafo", StatCollector.translateToLocal("tile.manutrafo.name")).getStackForm(1L) :*/ new GT_TileEntity_ManualTrafo(
                                    ConfigHandler.IDOffset + GT_Values.VN.length * 6 + 1,
                                    "bw.manualtrafo",
                                    StatCollector.translateToLocal("tile.manutrafo.name"))
                            .getStackForm(1L),
                    RecipeLoader.BITSD,
                    new Object[] {
                        "SCS",
                        "CHC",
                        "ZCZ",
                        'S',
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L),
                        'C',
                        new ItemStack(ItemRegistry.BW_BLOCKS[2]),
                        'H',
                        ItemList.Hull_HV.get(1L),
                        'Z',
                        "circuitAdvanced"
                    });

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                        Materials.Aluminium.getPlates(1),
                        ItemList.Circuit_Board_Plastic.get(1L),
                        ItemList.Battery_RE_LV_Lithium.get(1L)
                    },
                    Materials.SolderingAlloy.getMolten(288L),
                    new ItemStack(ItemRegistry.CIRCUIT_PROGRAMMER),
                    600,
                    BW_Util.getMachineVoltageFromTier(2));

            GT_ModHandler.addCraftingRecipe(
                    new GT_TileEntity_Windmill(
                                    ConfigHandler.IDOffset + GT_Values.VN.length * 6 + 2,
                                    "bw.windmill",
                                    StatCollector.translateToLocal("tile.bw.windmill.name"))
                            .getStackForm(1L),
                    RecipeLoader.BITSD,
                    new Object[] {
                        "BHB",
                        "WGW",
                        "BWB",
                        'B',
                        new ItemStack(Blocks.brick_block),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                        'H',
                        new ItemStack(Blocks.hopper),
                        'G',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                    });

            String[] stones = {"stone", "stoneSmooth"};
            String[] granites = {"blockGranite", "stoneGranite", "Granite", "granite"};
            for (String granite : granites) {
                for (String stone : stones) {
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[] {
                                "SSS",
                                "DfD",
                                " h ",
                                'S',
                                stone,
                                'D',
                                new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE),
                            });
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[] {
                                "hDf",
                                "SSS",
                                'S',
                                stone,
                                'D',
                                new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE),
                            });
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[] {
                                "SSS", "DfD", " h ", 'S', stone, 'D', granite,
                            });
                    GT_ModHandler.addCraftingRecipe(
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                            new Object[] {
                                "hDf", "SSS", 'S', stone, 'D', granite,
                            });
                }
                GT_ModHandler.addCraftingRecipe(
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                        GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                        new Object[] {
                            "STS",
                            "h f",
                            "SBS",
                            'S',
                            granite,
                            'T',
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                            'B',
                            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                        });
            }
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "STS",
                        "h f",
                        "SBS",
                        'S',
                        new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE),
                        'T',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                        'B',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    });
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WLs", "WLh", "WLf", 'L', new ItemStack(Items.leather), 'W', "logWood",
                    });
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WLs", "WLh", "WLf", 'L', new ItemStack(Blocks.carpet), 'W', "logWood",
                    });
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WLs", "WLh", "WLf", 'L', new ItemStack(Items.paper), 'W', "logWood",
                    });

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WEs",
                        "WZh",
                        "WDf",
                        'E',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                        'Z',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                        'D',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                        'W',
                        "logWood",
                    });

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WEs",
                        "WZh",
                        "WDf",
                        'Z',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                        'E',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                        'D',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                        'W',
                        "logWood",
                    });

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WEs",
                        "WZh",
                        "WDf",
                        'D',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                        'Z',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                        'E',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                        'W',
                        "logWood",
                    });

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WEs",
                        "WZh",
                        "WDf",
                        'E',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                        'D',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                        'Z',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                        'W',
                        "logWood",
                    });

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] {
                        "WEs",
                        "WZh",
                        "WDf",
                        'Z',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                        'D',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                        'E',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                        'W',
                        "logWood",
                    });

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.LEATHER_ROTOR), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                        "hPf",
                        "PWP",
                        "sPr",
                        'P',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    });
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.WOOL_ROTOR), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                        "hPf",
                        "PWP",
                        "sPr",
                        'P',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    });
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.PAPER_ROTOR), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                        "hPf",
                        "PWP",
                        "sPr",
                        'P',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    });
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.COMBINED_ROTOR), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[] {
                        "hPf",
                        "PWP",
                        "sPr",
                        'P',
                        new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
                        'W',
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    });
            GT_ModHandler.addCraftingRecipe(new ItemStack(ItemRegistry.ROTORBLOCK), RecipeLoader.BITSD, new Object[] {
                "WRW",
                "RGR",
                "WRW",
                'R',
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 1L),
                'W',
                "plankWood",
                'G',
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
            });
            GT_TileEntity_THTR.THTRMaterials.registerTHR_Recipes();
            GT_ModHandler.addCraftingRecipe(ItemRegistry.THTR, RecipeLoader.BITSD, new Object[] {
                "BZB",
                "BRB",
                "BZB",
                'B',
                new ItemStack(GregTech_API.sBlockCasings3, 1, 12),
                'R',
                GT_ModHandler.getModItem("IC2", "blockGenerator", 1, 5),
                'Z',
                "circuitUltimate"
            });
            GT_TileEntity_HTGR.HTGRMaterials.registerTHR_Recipes();
            GT_ModHandler.addCraftingRecipe(ItemRegistry.HTGR, RecipeLoader.BITSD, new Object[] {
                "BZB",
                "BRB",
                "BZB",
                'B',
                new ItemStack(GregTech_API.sBlockCasings8, 1, 5),
                'R',
                GT_ModHandler.getModItem("IC2", "blockGenerator", 1, 5),
                'Z',
                "circuitSuperconductor"
            });

            GT_ModHandler.addCraftingRecipe(ItemRegistry.EIG, RecipeLoader.BITSD, new Object[] {
                "AZA",
                "BRB",
                "AZA",
                'B',
                new ItemStack(GregTech_API.sBlockCasings4, 1, 1),
                'R',
                GT_ModHandler.getModItem("EnderIO", "blockFarmStation", 1),
                'A',
                new ItemStack(GregTech_API.sBlockMachines, 1, 11104),
                'Z',
                "circuitUltimate"
            });

            if (LoaderReference.galacticgreg) {
                GT_Values.RA.addAssemblylineRecipe(
                        ItemList.OreDrill4.get(1L),
                        BW_Util.getMachineVoltageFromTier(6),
                        new Object[] {
                            ItemList.OreDrill4.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 9L),
                            Materials.Europium.getPlates(3),
                            ItemList.Electric_Motor_LuV.get(9L),
                            ItemList.Sensor_LuV.get(9L),
                            ItemList.Field_Generator_LuV.get(9L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Europium, 36L)
                        },
                        new FluidStack[] {
                            new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Neon.getFluidOrGas(20000),
                        },
                        ItemRegistry.voidminer[0].copy(),
                        108000,
                        BW_Util.getMachineVoltageFromTier(6));
            }

            if (!LoaderReference.tectech) {
                if (LoaderReference.galacticgreg) {
                    GT_Values.RA.addAssemblylineRecipe(
                            ItemRegistry.voidminer[0].copy(),
                            BW_Util.getMachineVoltageFromTier(7),
                            new Object[] {
                                ItemRegistry.voidminer[0].copy(),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 9L),
                                Materials.BlackPlutonium.getPlates(3),
                                ItemList.Electric_Motor_ZPM.get(9L),
                                ItemList.Sensor_ZPM.get(9L),
                                ItemList.Field_Generator_ZPM.get(9L),
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.BlackPlutonium, 36L)
                            },
                            new FluidStack[] {
                                new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Krypton.getFluidOrGas(20000)
                            },
                            ItemRegistry.voidminer[1].copy(),
                            216000,
                            BW_Util.getMachineVoltageFromTier(7));

                    GT_Values.RA.addAssemblylineRecipe(
                            ItemRegistry.voidminer[1].copy(),
                            BW_Util.getMachineVoltageFromTier(8),
                            new Object[] {
                                ItemRegistry.voidminer[1].copy(),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 9L),
                                Materials.Neutronium.getPlates(3),
                                ItemList.Electric_Motor_UV.get(9L),
                                ItemList.Sensor_UV.get(9L),
                                ItemList.Field_Generator_UV.get(9L),
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 36L)
                            },
                            new FluidStack[] {
                                new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Oganesson.getFluidOrGas(20000)
                            },
                            ItemRegistry.voidminer[2].copy(),
                            432000,
                            BW_Util.getMachineVoltageFromTier(8));
                }
                GT_Values.RA.addAssemblylineRecipe(
                        ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                        24000,
                        new ItemStack[] {
                            ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                            Materials.Neutronium.getBlocks(5),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Osmium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 64),
                            ItemList.Electric_Piston_UV.get(64),
                        },
                        new FluidStack[] {
                            new FluidStack(solderIndalloy, 1440),
                            Materials.Osmium.getMolten(1440),
                            Materials.Neutronium.getMolten(1440)
                        },
                        ItemRegistry.eic.copy(),
                        240000,
                        BW_Util.getMachineVoltageFromTier(8));
            } else {
                ItemStack[][] converters = ItemRegistry.TecTechLaserAdditions[0];
                ItemStack[][] input = ItemRegistry.TecTechLaserAdditions[1];
                ItemStack[][] dynamo = ItemRegistry.TecTechLaserAdditions[2];

                ItemList[] emitters = {
                    ItemList.Emitter_EV, ItemList.Emitter_IV, ItemList.Emitter_LuV, ItemList.Emitter_ZPM
                };

                ItemList[] sensors = {ItemList.Sensor_EV, ItemList.Sensor_IV, ItemList.Sensor_LuV, ItemList.Sensor_ZPM};

                OrePrefixes[] prefixes = {
                    WerkstoffLoader.gtnhGT ? OrePrefixes.cableGt04 : OrePrefixes.wireGt04,
                    WerkstoffLoader.gtnhGT ? OrePrefixes.cableGt08 : OrePrefixes.wireGt08,
                    WerkstoffLoader.gtnhGT ? OrePrefixes.cableGt12 : OrePrefixes.wireGt12,
                    WerkstoffLoader.gtnhGT ? OrePrefixes.cableGt16 : OrePrefixes.cableGt12
                };

                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] {
                            ItemList.Circuit_Parts_GlassFiber.get(32),
                            GT_OreDictUnificator.get(
                                    WerkstoffLoader.gtnhGT ? OrePrefixes.foil : OrePrefixes.plateDouble,
                                    Materials.Electrum,
                                    WerkstoffLoader.gtnhGT ? 8 : 1),
                            WerkstoffLoader.CubicZirconia.get(OrePrefixes.gemExquisite, 2)
                        },
                        Materials.Polytetrafluoroethylene.getMolten(72),
                        new ItemStack(
                                ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                                1,
                                ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                        200,
                        BW_Util.getMachineVoltageFromTier(4));

                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        ItemStack converter = converters[j][i];
                        ItemStack eInput = input[j][i];
                        ItemStack eDynamo = dynamo[j][i];

                        int solderingAmount = Math.max(144 * i, 72) * (j + 1);
                        GT_Values.RA.addAssemblerRecipe(
                                new ItemStack[] {
                                    new ItemStack(
                                            ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                                            ((j + 1) * 16),
                                            ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                                    WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                                    GT_OreDictUnificator.get(prefixes[j], cables[i + 4], 8),
                                    emitters[i].get(2 * (j + 1)),
                                    sensors[i].get(2 * (j + 1)),
                                    ItemList.TRANSFORMERS[4 + i].get(2 * (j + 1)),
                                },
                                Materials.SolderingAlloy.getMolten(solderingAmount),
                                converter,
                                200 * (j + 1),
                                BW_Util.getMachineVoltageFromTier(4 + i));
                        GT_Values.RA.addAssemblerRecipe(
                                new ItemStack[] {
                                    new ItemStack(
                                            ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                                            ((j + 1) * 16),
                                            ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                                    WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                                    GT_OreDictUnificator.get(prefixes[j], cables[i + 4], 8),
                                    sensors[i].get(2 * (j + 1)),
                                    ItemList.HATCHES_ENERGY[4 + i].get(2 * (j + 1)),
                                },
                                Materials.SolderingAlloy.getMolten(solderingAmount),
                                eInput,
                                200 * (j + 1),
                                BW_Util.getMachineVoltageFromTier(4 + i));
                        GT_Values.RA.addAssemblerRecipe(
                                new ItemStack[] {
                                    new ItemStack(
                                            ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                                            ((j + 1) * 16),
                                            ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                                    WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                                    GT_OreDictUnificator.get(prefixes[j], cables[i + 4], 8),
                                    emitters[i].get(2 * (j + 1)),
                                    ItemList.HATCHES_DYNAMO[4 + i].get(2 * (j + 1)),
                                },
                                Materials.SolderingAlloy.getMolten(solderingAmount),
                                eDynamo,
                                200 * (j + 1),
                                BW_Util.getMachineVoltageFromTier(4 + i));
                    }
                }
            }

            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.add(new BWRecipes.DynamicGTRecipe(
                    false,
                    new ItemStack[] {
                        ItemList.Hatch_Input_HV.get(64),
                        Materials.LiquidAir.getCells(1),
                        GT_Utility.getIntegratedCircuit(17)
                    },
                    new ItemStack[] {ItemRegistry.compressedHatch.copy()},
                    null,
                    null,
                    null,
                    null,
                    300,
                    BW_Util.getMachineVoltageFromTier(3),
                    0));
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.add(new BWRecipes.DynamicGTRecipe(
                    false,
                    new ItemStack[] {ItemList.Hatch_Output_HV.get(64), GT_Utility.getIntegratedCircuit(17)},
                    new ItemStack[] {ItemRegistry.giantOutputHatch.copy()},
                    null,
                    null,
                    null,
                    null,
                    300,
                    BW_Util.getMachineVoltageFromTier(3),
                    0));

            GT_Values.RA.addAssemblylineRecipe(
                    ItemList.Machine_LuV_CircuitAssembler.get(1L),
                    24000,
                    new ItemStack[] {
                        ItemList.Machine_LuV_CircuitAssembler.get(1L),
                        ItemList.Robot_Arm_LuV.get(4L),
                        ItemList.Electric_Motor_LuV.get(4L),
                        ItemList.Field_Generator_LuV.get(1L),
                        ItemList.Emitter_LuV.get(1L),
                        ItemList.Sensor_LuV.get(1L),
                        Materials.Chrome.getPlates(8)
                    },
                    new FluidStack[] {new FluidStack(solderIndalloy, 1440)},
                    ItemRegistry.cal.copy(),
                    24000,
                    BW_Util.getMachineVoltageFromTier(6));
        }
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Forsterit.get(OrePrefixes.dust, 7),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 6),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                250,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Fayalit.get(OrePrefixes.dust, 7),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                320,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Prasiolite.get(OrePrefixes.dust, 16),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                580,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Hedenbergit.get(OrePrefixes.dust, 10),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 2L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                300,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.FuchsitAL.get(OrePrefixes.dust, 21),
                ItemList.Cell_Empty.get(2),
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2),
                GT_Values.NI,
                GT_Values.NI,
                null,
                390,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.FuchsitCR.get(OrePrefixes.dust, 21),
                ItemList.Cell_Empty.get(2),
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2),
                GT_Values.NI,
                GT_Values.NI,
                null,
                460,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.VanadioOxyDravit.get(OrePrefixes.dust, 53),
                ItemList.Cell_Empty.get(3),
                GT_Values.NF,
                Materials.Oxygen.getGas(19000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnalium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3),
                null,
                710,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.ChromoAluminoPovondrait.get(OrePrefixes.dust, 53),
                ItemList.Cell_Empty.get(3),
                GT_Values.NF,
                Materials.Oxygen.getGas(19000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnalium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3),
                null,
                720,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.FluorBuergerit.get(OrePrefixes.dust, 50),
                ItemList.Cell_Empty.get(3),
                GT_Values.NF,
                Materials.Oxygen.getGas(6000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fluorine, 3),
                null,
                730,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Olenit.get(OrePrefixes.dust, 51),
                ItemList.Cell_Empty.get(1),
                GT_Values.NF,
                Materials.Oxygen.getGas(1000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1),
                GT_Values.NI,
                null,
                790,
                120);
    }
}
