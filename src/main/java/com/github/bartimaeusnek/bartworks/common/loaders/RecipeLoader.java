package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.tileentities.*;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static com.github.bartimaeusnek.bartworks.common.ConfigHandler.newStuff;

public class RecipeLoader implements Runnable {

    private static final long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE;

    @Override
    public void run() {

        if (MainMod.GTNH) {
            /*
             * GTNH "hardmode" Recipes
             */

            GT_Values.RA.addFluidSolidifierRecipe(new ItemStack(Blocks.lapis_block), Materials.Iron.getMolten(1296L), new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0), 100, (int) (GT_Values.V[3] - (GT_Values.V[3] / 10)));
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0), Materials.Lapis.getPlates(9), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2L), GT_Utility.getIntegratedCircuit(17)}, FluidRegistry.getFluidStack("ic2coolant", 1000), new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), 100, (int) (GT_Values.V[3] - (GT_Values.V[3] / 10)));
            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), Materials.Lapis.getBlocks(8), GT_Utility.getIntegratedCircuit(17)}, GT_Values.NF, new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, (int) (GT_Values.V[3] - (GT_Values.V[3] / 10)));
        } else {
            /*
             * Vanilla Recipes
             */


            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{Materials.Lapis.getBlocks(8), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1L), GT_Utility.getIntegratedCircuit(17)}, GT_Values.NF, new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, (int) (GT_Values.V[1] - (GT_Values.V[1] / 10)));

            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "LLL",
                            "LCL",
                            "LLL",
                            'L', Materials.Lapis.getBlocks(1),
                            'C', "circuitBasic"
                    });

            GT_Values.RA.addCutterRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[1]), new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 1), GT_Values.NI, 100, (int) (GT_Values.V[1] - (GT_Values.V[1] / 10)));
            GT_Values.RA.addCompressorRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 1), new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, (int) (GT_Values.V[1] - (GT_Values.V[1] / 10)));
            GT_Values.RA.addCompressorRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 9, 0), new ItemStack(ItemRegistry.BW_BLOCKS[1]), 100, (int) (GT_Values.V[1] - (GT_Values.V[1] / 10)));
            GT_ModHandler.addShapelessCraftingRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0), RecipeLoader.bitsd, new Object[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1)});
            GT_ModHandler.addShapelessCraftingRecipe(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1), RecipeLoader.bitsd, new Object[]{new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0)});
        }

        /*
         * Common Recipes
         */

        GT_ModHandler.addCraftingRecipe(
                new GT_TileEntity_LESU(ConfigHandler.IDOffset, "LESU", "LESU").getStackForm(1L),
                RecipeLoader.bitsd,
                new Object[]{
                        "CDC",
                        "SBS",
                        "CFC",
                        'C', GT_OreDictUnificator.get(OrePrefixes.circuit, MainMod.GTNH ? Materials.Advanced : Materials.Basic, 1L),
                        'D', ItemList.Cover_Screen.get(1L),
                        'S', GT_OreDictUnificator.get(OrePrefixes.cableGt12, MainMod.GTNH ? Materials.Platinum : Materials.AnnealedCopper, 1L),
                        'B', new ItemStack(ItemRegistry.BW_BLOCKS[1]),
                        'F', MainMod.GTNH ? ItemList.Field_Generator_HV.get(1L) : ItemList.Field_Generator_LV.get(1L)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.Destructopack),
                RecipeLoader.bitsd,
                new Object[]{
                        "CPC",
                        "PLP",
                        "CPC",
                        'C', GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                        'P', GT_OreDictUnificator.get(MainMod.GTNH ? OrePrefixes.plateDouble : OrePrefixes.plate, Materials.Aluminium, 1L),
                        'L', new ItemStack(Items.lava_bucket)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.Destructopack),
                RecipeLoader.bitsd,
                new Object[]{
                        "CPC",
                        "PLP",
                        "CPC",
                        'C', GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                        'P', GT_OreDictUnificator.get(MainMod.GTNH ? OrePrefixes.plateDouble : OrePrefixes.plate, MainMod.GTNH ? Materials.Steel : Materials.Iron, 1L),
                        'L', new ItemStack(Items.lava_bucket)
                });

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.RockcutterMV),
                RecipeLoader.bitsd,
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
                new ItemStack(ItemRegistry.RockcutterLV),
                RecipeLoader.bitsd,
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
                new ItemStack(ItemRegistry.RockcutterHV),
                RecipeLoader.bitsd,
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
                    new ItemStack(ItemRegistry.Teslastaff),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "BO ",
                            "OP ",
                            "  P",
                            'O', GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Superconductor, 1L),
                            'B', ItemList.Energy_LapotronicOrb.get(1L),
                            'P', "plateAlloyIridium",
                    });

        if (newStuff) {
            Materials[] cables = {Materials.Lead, Materials.Tin, Materials.AnnealedCopper, Materials.Gold, Materials.Aluminium, Materials.Tungsten, Materials.VanadiumGallium, Materials.Naquadah, Materials.NaquadahAlloy, Materials.Superconductor};
            Materials[] hulls = {Materials.WroughtIron, Materials.Steel, Materials.Aluminium, Materials.StainlessSteel, Materials.Titanium, Materials.TungstenSteel, Materials.Chrome, Materials.Iridium, Materials.Osmium, Materials.Naquadah};

            for (int i = 0; i < GT_Values.VN.length; i++) {
                try {
                    Materials cable = cables[i];
                    Materials hull = hulls[i];
                    ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);

                    GT_ModHandler.addCraftingRecipe(
                            new GT_MetaTileEntity_EnergyDistributor(ConfigHandler.IDOffset + 1 + i, "Energy Distributor " + GT_Values.VN[i], "Energy Distributor " + GT_Values.VN[i], i, "Splits Amperage into several Sides").getStackForm(1L),
                            RecipeLoader.bitsd,
                            new Object[]{
                                    "PWP",
                                    "WCW",
                                    "PWP",
                                    'W', GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                                    'P', GT_OreDictUnificator.get(OrePrefixes.plate, hull, 1L),
                                    'C', machinehull
                            });
                    GT_ModHandler.addCraftingRecipe(
                            ItemRegistry.Diode12A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 4 + 1 + i, "Cable Diode 12A " + GT_Values.VN[i], "Cable Diode 12A " + GT_Values.VN[i], i, 12).getStackForm(1L),
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode12A[i],
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode8A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 3 + 1 + i, "Cable Diode 8A " + GT_Values.VN[i], "Cable Diode 8A " + GT_Values.VN[i], i, 8).getStackForm(1L),
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode8A[i],
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode4A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 2 + 1 + i, "Cable Diode 4A " + GT_Values.VN[i], "Cable Diode 4A " + GT_Values.VN[i], i, 4).getStackForm(1L),
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode4A[i],
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode2A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length + 1 + i, "Cable Diode 2A " + GT_Values.VN[i], "Cable Diode 2A " + GT_Values.VN[i], i, 2).getStackForm(1L),
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode2A[i],
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode16A[i] = new GT_MetaTileEntity_Diode(ConfigHandler.IDOffset + GT_Values.VN.length * 5 + 5 + i, "Cable Diode 16A " + GT_Values.VN[i], "Cable Diode 16A " + GT_Values.VN[i], i, 16).getStackForm(1L),
                            RecipeLoader.bitsd,
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
                            ItemRegistry.Diode16A[i],
                            RecipeLoader.bitsd,
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
                    new ItemStack(ItemRegistry.BW_BLOCKS[2],1,1),
                    20,
                    (int) (GT_Values.V[3] - (GT_Values.V[3] / 10))
            );

            GT_ModHandler.addCraftingRecipe(
                new GT_TileEntity_ManualTrafo(ConfigHandler.IDOffset + GT_Values.VN.length * 6 + 1, "Manual Travo", "Manual Travo").getStackForm(1L),
                    RecipeLoader.bitsd,
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

            GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.circuit,Materials.Good,1L),Materials.Aluminium.getPlates(1),ItemList.Circuit_Board_Plastic.get(1L), ItemList.Battery_RE_LV_Lithium.get(1L)}, Materials.SolderingAlloy.getMolten(288L),new ItemStack(ItemRegistry.CircuitProgrammer),600,(int) (GT_Values.V[2] - (GT_Values.V[2] / 10)));

            GT_ModHandler.addCraftingRecipe(
                    new GT_TileEntity_Windmill(ConfigHandler.IDOffset+GT_Values.VN.length*6+2,"Windmill","Windmill").getStackForm(1L),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "BHB",
                            "WGW",
                            "BWB",
                            'B', new ItemStack(Blocks.brick_block),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                            'H', new ItemStack(Blocks.hopper),
                            'G', new ItemStack(ItemRegistry.craftingParts,1,2),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.craftingParts,1,0),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "SSS",
                            "DfD",
                            " h ",
                            'S', new ItemStack(Blocks.stone),
                            'D', new ItemStack(GregTech_API.sBlockGranites,1,OreDictionary.WILDCARD_VALUE),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.craftingParts,1,1),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "hDf",
                            "SSS",
                            'S', new ItemStack(Blocks.stone),
                            'D', new ItemStack(GregTech_API.sBlockGranites,1, OreDictionary.WILDCARD_VALUE),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.craftingParts,1,2),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "STS",
                            "h f",
                            "SBS",
                            'S', new ItemStack(GregTech_API.sBlockGranites,1, OreDictionary.WILDCARD_VALUE),
                            'T', new ItemStack(ItemRegistry.craftingParts, 1, 0),
                            'B', new ItemStack(ItemRegistry.craftingParts, 1, 1),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.craftingParts,1,3),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "WLs",
                            "WLh",
                            "WLf",
                            'L', new ItemStack(Items.leather),
                            'W', new ItemStack(Blocks.log,1,OreDictionary.WILDCARD_VALUE),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.craftingParts,1,4),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "WLs",
                            "WLh",
                            "WLf",
                            'L', new ItemStack(Blocks.carpet),
                            'W', new ItemStack(Blocks.log,1,OreDictionary.WILDCARD_VALUE),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.craftingParts,1,5),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "WLs",
                            "WLh",
                            "WLf",
                            'L', new ItemStack(Items.paper),
                            'W', new ItemStack(Blocks.log,1,OreDictionary.WILDCARD_VALUE),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.craftingParts,1,6),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "WEs",
                            "WZh",
                            "WDf",
                            'E', new ItemStack(ItemRegistry.craftingParts,1,3),
                            'Z', new ItemStack(ItemRegistry.craftingParts,1,4),
                            'D', new ItemStack(ItemRegistry.craftingParts,1,5),
                            'W', new ItemStack(Blocks.log,1,OreDictionary.WILDCARD_VALUE),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.LeatherRotor),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.craftingParts,1,3),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.WoolRotor),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.craftingParts,1,4),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.PaperRotor),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.craftingParts,1,5),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CombinedRotor),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "hPf",
                            "PWP",
                            "sPr",
                            'P', new ItemStack(ItemRegistry.craftingParts,1,6),
                            'W', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.ROTORBLOCK),
                    RecipeLoader.bitsd,
                    new Object[]{
                            "WRW",
                            "RGR",
                            "WRW",
                            'R',GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 1L),
                            'W', new ItemStack(Blocks.planks),
                            'G', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L),
                    }
            );
            //next free ID: ConfigHandler.IDOffset+GT_Values.VN.length*6+3

        }
    }
}
