package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.enums.MetaTileEntityIDs.BioLab_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BioVat;
import static gregtech.api.enums.MetaTileEntityIDs.LESU;
import static gregtech.api.enums.MetaTileEntityIDs.ManualTrafo;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_EV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_HV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_IV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_UV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Windmill;
import static gregtech.api.enums.Mods.IndustrialCraft2;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.common.loaders.RecipeLoader;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_BioVat;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_LESU;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ManualTrafo;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_Windmill;
import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.GT_MetaTileEntity_BioLab;
import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.core.Ic2Items;

public class CraftingRecipes implements Runnable {

    @Override
    public void run() {

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

        ItemStack[] bats = { ItemList.Battery_Hull_LV.get(1L), ItemList.Battery_Hull_MV.get(1L),
            ItemList.Battery_Hull_HV.get(1L) };
        ItemStack[] chreac = { ItemList.Machine_MV_ChemicalReactor.get(1L), ItemList.Machine_HV_ChemicalReactor.get(1L),
            ItemList.Machine_EV_ChemicalReactor.get(1L) };

        GT_ModHandler.addCraftingRecipe(
            new GT_TileEntity_LESU(LESU.ID, "LESU", "L.E.S.U.").getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "CDC", "SBS", "CFC", 'C', "circuitAdvanced", 'D', ItemList.Cover_Screen.get(1L), 'S',
                GT_OreDictUnificator.get(OrePrefixes.cableGt12, Materials.Platinum, 1L), 'B',
                new ItemStack(ItemRegistry.BW_BLOCKS[1]), 'F', ItemList.Field_Generator_HV.get(1L) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_MV),
            RecipeLoader.BITSD,
            new Object[] { "DS ", "DP ", "DCB", 'D', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                'S', GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 1L), 'P',
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1L), 'C', "circuitGood", 'B',
                ItemList.IC2_AdvBattery.get(1L) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_LV),
            RecipeLoader.BITSD,
            new Object[] { "DS ", "DP ", "DCB", 'D', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                'S', GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1L), 'P',
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L), 'C', "circuitBasic", 'B',
                ItemList.IC2_ReBattery.get(1L) });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_HV),
            RecipeLoader.BITSD,
            new Object[] { "DS ", "DP ", "DCB", 'D', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                'S', GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 1L), 'P',
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L), 'C', "circuitAdvanced", 'B',
                ItemList.IC2_EnergyCrystal.get(1L) });

        if (ConfigHandler.teslastaff) {
            GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.TESLASTAFF),
                RecipeLoader.BITSD,
                new Object[] { "BO ", "OP ", "  P", 'O',
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 1L), 'B',
                    ItemList.Energy_LapotronicOrb.get(1L), 'P', "plateAlloyIridium", });
        }

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPPARTS, 1, 0), // tube
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " fG", " G ", "G  ", 'G', ItemList.Circuit_Parts_Glass_Tube.get(1L) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPPARTS, 1, 1), // motor
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "GLP", "LSd", "PfT", 'G',
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 1L), 'L',
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 1L), 'S',
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 1L), 'P', new ItemStack(Blocks.piston),
                'T', new ItemStack(ItemRegistry.PUMPPARTS, 1, 0) });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPBLOCK, 1, 0),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "IPI", "PMP", "ISI", 'I', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                'P', GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Wood, 1L), 'M',
                new ItemStack(ItemRegistry.PUMPPARTS, 1, 1), 'S', Ic2Items.ironFurnace });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.WINDMETER),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "SWF", "Sf ", "Ss ", 'S', "stickWood", 'W', new ItemStack(Blocks.wool, 1, Short.MAX_VALUE),
                'F', new ItemStack(Items.string), });

        for (int i = 0; i < 3; i++) {
            Materials cable = cables[i + 2];
            ItemStack machinehull = ItemList.MACHINE_HULLS[i + 2].get(1L);
            GT_ModHandler.addCraftingRecipe(
                ItemRegistry.acidGens[i],
                RecipeLoader.BITSD,
                new Object[] { "HRH", "HCH", "HKH", 'H', bats[i], 'K',
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, cable, 1L), 'C', machinehull, 'R', chreac[i] });
        }

        GT_ModHandler.addCraftingRecipe(
            ItemRegistry.acidGensLV,
            RecipeLoader.BITSD,
            new Object[] { "HRH", "KCK", "HKH", 'H', ItemList.Battery_Hull_LV.get(1L), 'K',
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L), 'C', ItemList.Hull_LV.get(1L), 'R',
                ItemList.Machine_LV_ChemicalReactor.get(1L), });

        for (int i = 0; i < 9; i++) {
            try {
                Materials cable = cables[i];
                ItemStack hull = hulls[i] instanceof Materials
                    ? GT_OreDictUnificator.get(OrePrefixes.plate, hulls[i], 1L)
                    : ((Werkstoff) hulls[i]).get(OrePrefixes.plate);
                ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);

                GT_ModHandler.addCraftingRecipe(
                    ItemRegistry.energyDistributor[i],
                    RecipeLoader.BITSD,
                    new Object[] { "PWP", "WCW", "PWP", 'W', GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                        'P', hull, 'C', machinehull });
                GT_ModHandler.addCraftingRecipe(
                    ItemRegistry.diode12A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt12, cable, 1L), 'P', hull, 'C', machinehull });
                GT_ModHandler.addCraftingRecipe(
                    ItemRegistry.diode8A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt08, cable, 1L), 'P', hull, 'C', machinehull });
                GT_ModHandler.addCraftingRecipe(
                    ItemRegistry.diode4A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, cable, 1L), 'P', hull, 'C', machinehull });
                GT_ModHandler.addCraftingRecipe(
                    ItemRegistry.diode2A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, cable, 1L), 'P', hull, 'C', machinehull });
                GT_ModHandler.addCraftingRecipe(
                    ItemRegistry.diode16A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WHW", "DCD", "PDP", 'H', OrePrefixes.componentCircuit.get(Materials.Inductor), 'D',
                        OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L), 'P', hull, 'C', machinehull });

            } catch (ArrayIndexOutOfBoundsException ignored) {

            }

        }

        String[] stones = { "stone", "stoneSmooth" };
        String[] granites = { "blockGranite", "stoneGranite", "Granite", "granite" };
        for (String granite : granites) {
            for (String stone : stones) {
                GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "SSS", "DfD", " h ", 'S', stone, 'D',
                        new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), });
                GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "hDf", "SSS", 'S', stone, 'D',
                        new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), });
                GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "SSS", "DfD", " h ", 'S', stone, 'D', granite, });
                GT_ModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "hDf", "SSS", 'S', stone, 'D', granite, });
            }
            GT_ModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "STS", "h f", "SBS", 'S', granite, 'T', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    'B', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1), });
        }

        GT_ModHandler.addCraftingRecipe(
            new GT_TileEntity_ManualTrafo(
                ManualTrafo.ID,
                "bw.manualtrafo",
                StatCollector.translateToLocal("tile.manutrafo.name")).getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "SCS", "CHC", "ZCZ", 'S',
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L), 'C',
                new ItemStack(ItemRegistry.BW_BLOCKS[2]), 'H', ItemList.Hull_HV.get(1L), 'Z', "circuitAdvanced" });

        GT_ModHandler.addCraftingRecipe(
            new GT_TileEntity_Windmill(
                Windmill.ID,
                "bw.windmill",
                StatCollector.translateToLocal("tile.bw.windmill.name")).getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "BHB", "WGW", "BWB", 'B', new ItemStack(Blocks.brick_block), 'W',
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), 'H', new ItemStack(Blocks.hopper),
                'G', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2), });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "STS", "h f", "SBS", 'S',
                new ItemStack(GregTech_API.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), 'T',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0), 'B',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1), });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Items.leather), 'W', "logWood", });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Blocks.carpet), 'W', "logWood", });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Items.paper), 'W', "logWood", });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'Z',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'E',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'Z',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'D',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'D',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.LEATHER_ROTOR),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'W',
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.WOOL_ROTOR),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'W',
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PAPER_ROTOR),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5), 'W',
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.COMBINED_ROTOR),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6), 'W',
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GT_ModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROTORBLOCK),
            RecipeLoader.BITSD,
            new Object[] { "WRW", "RGR", "WRW", 'R', GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 1L),
                'W', "plankWood", 'G', GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });

        GT_ModHandler.addCraftingRecipe(
            ItemRegistry.THTR,
            RecipeLoader.BITSD,
            new Object[] { "BZB", "BRB", "BZB", 'B', new ItemStack(GregTech_API.sBlockCasings3, 1, 12), 'R',
                GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockGenerator", 1, 5), 'Z', "circuitUltimate" });

        GT_ModHandler.addCraftingRecipe(
            ItemRegistry.HTGR,
            RecipeLoader.BITSD,
            new Object[] { "BZB", "BRB", "BZB", 'B', new ItemStack(GregTech_API.sBlockCasings8, 1, 5), 'R',
                GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockGenerator", 1, 5), 'Z', "circuitSuperconductor" });

        // DNAExtractionModule
        GT_ModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[0],
            RecipeLoader.BITSD,
            new Object[] { "TET", "CFC", "TST", 'T',
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L), 'E', ItemList.Emitter_EV.get(1L),
                'C', GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L), 'S',
                ItemList.Sensor_EV.get(1L), 'F', ItemList.Field_Generator_EV.get(1L) });

        // PCRThermoclyclingModule
        GT_ModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[1],
            RecipeLoader.BITSD,
            new Object[] { "NEN", "CFC", "NSN", 'N',
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L), 'E',
                ItemList.Emitter_EV.get(1L), 'C',
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L), 'S',
                ItemList.Sensor_EV.get(1L), 'F', ItemList.Field_Generator_EV.get(1L) });

        // PlasmidSynthesisModule
        GT_ModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[2],
            RecipeLoader.BITSD,
            new Object[] { "SFE", "CPC", "NFN", 'N',
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L), 'C', "circuit" + Materials.EV,
                'F', ItemList.Field_Generator_EV.get(1L), 'E', ItemList.Emitter_EV.get(1L), 'S',
                ItemList.Sensor_EV.get(1L), 'P',
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L), });
        // TransformationModule
        GT_ModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[3],
            RecipeLoader.BITSD,
            new Object[] { "SFE", "CPC", "NFN", 'N',
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 1L), 'C', "circuit" + Materials.LuV,
                'F', ItemList.Field_Generator_LuV.get(1L), 'E', ItemList.Emitter_LuV.get(1L), 'S',
                ItemList.Sensor_LuV.get(1L), 'P', WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 1), });

        // ClonalCellularSynthesisModule
        GT_ModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[4],
            RecipeLoader.BITSD,
            new Object[] { "FEF", "CPC", "FSF", 'N',
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 1L), 'C', "circuit" + Materials.LuV,
                'F', ItemList.Field_Generator_LuV.get(1L), 'E', ItemList.Emitter_LuV.get(1L), 'S',
                ItemList.Sensor_LuV.get(1L), 'P', WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 1), });

        GT_ModHandler.addCraftingRecipe(
            new GT_TileEntity_BioVat(BioVat.ID, "bw.biovat", StatCollector.translateToLocal("tile.biovat.name"))
                .getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "GCG", "KHK", "GCG", 'G', new ItemStack(ItemRegistry.bw_glasses[0], 1, 1), 'C',
                "circuit" + Materials.EV, 'K', GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Silver, 1L),
                'H', ItemList.MACHINE_HULLS[3].get(1L) });

        ItemStack[] Pistons2 = { ItemList.Electric_Piston_HV.get(1L), ItemList.Electric_Piston_EV.get(1L),
            ItemList.Electric_Piston_IV.get(1L), ItemList.Electric_Piston_LuV.get(1L),
            ItemList.Electric_Piston_ZPM.get(1L), ItemList.Electric_Piston_UV.get(1L) };
        ItemStack[] BioLab2 = new ItemStack[GT_Values.VN.length - 3];
        ItemStack[] RadioHatch2 = new ItemStack[GT_Values.VN.length - 3];
        Materials[] cables2 = { Materials.Gold, Materials.Aluminium, Materials.Tungsten, Materials.VanadiumGallium,
            Materials.Naquadah, Materials.NaquadahAlloy, Materials.SuperconductorUHV };
        Materials[] hulls2 = { Materials.StainlessSteel, Materials.Titanium, Materials.TungstenSteel, Materials.Chrome,
            Materials.Iridium, Materials.Osmium, Materials.Naquadah };
        Materials[] wireMat2 = { Materials.Kanthal, Materials.Nichrome, Materials.TungstenSteel, Materials.Naquadah,
            Materials.NaquadahAlloy, Materials.SuperconductorUHV };
        Materials[] circuits2 = { Materials.HV, Materials.EV, Materials.IV, Materials.LuV, Materials.ZPM,
            Materials.UV };

        int[] BioLab = new int[] { BioLab_HV.ID, BioLab_EV.ID, BioLab_IV.ID, BioLab_LuV.ID, BioLab_ZPM.ID, BioLab_UV.ID,
            BioLab_UHV.ID, BioLab_UEV.ID, BioLab_UIV.ID, BioLab_UMV.ID, BioLab_UXV.ID, BioLab_MAX.ID };
        int[] RadioHatch = new int[] { RadioHatch_HV.ID, RadioHatch_EV.ID, RadioHatch_IV.ID, RadioHatch_LuV.ID,
            RadioHatch_ZPM.ID, RadioHatch_UV.ID, RadioHatch_UHV.ID, RadioHatch_UEV.ID, RadioHatch_UIV.ID,
            RadioHatch_UMV.ID, RadioHatch_UXV.ID, RadioHatch_MAX.ID };

        for (int i = 3; i < GT_Values.VN.length - 1; i++) {
            BioLab2[i - 3] = new GT_MetaTileEntity_BioLab(
                BioLab[i - 3],
                "bw.biolab" + GT_Values.VN[i],
                GT_Values.VN[i] + " " + StatCollector.translateToLocal("tile.biolab.name"),
                i).getStackForm(1L);
            RadioHatch2[i - 3] = new GT_MetaTileEntity_RadioHatch(
                RadioHatch[i - 3],
                "bw.radiohatch" + GT_Values.VN[i],
                GT_Values.VN[i] + " " + StatCollector.translateToLocal("tile.radiohatch.name"),
                i).getStackForm(1L);
            try {
                ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);
                GT_ModHandler.addCraftingRecipe(
                    BioLab2[i - 3],
                    RecipeLoader.BITSD,
                    new Object[] { "PFP", "WCW", "OGO", 'F',
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, hulls2[i - 3], 1L), 'W',
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, wireMat2[i - 3], 1L), 'P',
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1L), 'O',
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polystyrene, 1L), 'G',
                        "circuit" + circuits2[i - 3], 'C', machinehull });
                GT_ModHandler.addCraftingRecipe(
                    RadioHatch2[i - 3],
                    RecipeLoader.BITSD,
                    new Object[] { "DPD", "DCD", "DKD", 'D',
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 1L), 'C', machinehull, 'K',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt08, cables2[i - 3], 1L), 'P', Pistons2[i - 3] });
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }

    }
}
