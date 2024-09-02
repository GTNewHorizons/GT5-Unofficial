package bartworks.common.loaders.recipes;

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

import bartworks.common.configs.ConfigHandler;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.ItemRegistry;
import bartworks.common.loaders.RecipeLoader;
import bartworks.common.tileentities.multis.MTEBioVat;
import bartworks.common.tileentities.multis.MTELESU;
import bartworks.common.tileentities.multis.MTEManualTrafo;
import bartworks.common.tileentities.multis.MTEWindmill;
import bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
import bartworks.common.tileentities.tiered.MTEBioLab;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
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

        GTModHandler.addCraftingRecipe(
            new MTELESU(LESU.ID, "LESU", "L.E.S.U.").getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "CDC", "SBS", "CFC", 'C', "circuitAdvanced", 'D', ItemList.Cover_Screen.get(1L), 'S',
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Platinum, 1L), 'B',
                new ItemStack(ItemRegistry.BW_BLOCKS[1]), 'F', ItemList.Field_Generator_HV.get(1L) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_MV),
            RecipeLoader.BITSD,
            new Object[] { "DS ", "DP ", "DCB", 'D', GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                'S', GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 1L), 'P',
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1L), 'C', "circuitGood", 'B',
                ItemList.IC2_AdvBattery.get(1L) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_LV),
            RecipeLoader.BITSD,
            new Object[] { "DS ", "DP ", "DCB", 'D', GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                'S', GTOreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1L), 'P',
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L), 'C', "circuitBasic", 'B',
                ItemList.IC2_ReBattery.get(1L) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_HV),
            RecipeLoader.BITSD,
            new Object[] { "DS ", "DP ", "DCB", 'D', GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                'S', GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 1L), 'P',
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L), 'C', "circuitAdvanced", 'B',
                ItemList.IC2_EnergyCrystal.get(1L) });

        if (ConfigHandler.teslastaff) {
            GTModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.TESLASTAFF),
                RecipeLoader.BITSD,
                new Object[] { "BO ", "OP ", "  P", 'O',
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 1L), 'B',
                    ItemList.Energy_LapotronicOrb.get(1L), 'P', "plateAlloyIridium", });
        }

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPPARTS, 1, 0), // tube
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " fG", " G ", "G  ", 'G', ItemList.Circuit_Parts_Glass_Tube.get(1L) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPPARTS, 1, 1), // motor
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "GLP", "LSd", "PfT", 'G',
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 1L), 'L',
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 1L), 'S',
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 1L), 'P', new ItemStack(Blocks.piston), 'T',
                new ItemStack(ItemRegistry.PUMPPARTS, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPBLOCK, 1, 0),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "IPI", "PMP", "ISI", 'I', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                'P', GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Wood, 1L), 'M',
                new ItemStack(ItemRegistry.PUMPPARTS, 1, 1), 'S', Ic2Items.ironFurnace });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.WINDMETER),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "SWF", "Sf ", "Ss ", 'S', "stickWood", 'W', new ItemStack(Blocks.wool, 1, Short.MAX_VALUE),
                'F', new ItemStack(Items.string), });

        for (int i = 0; i < 3; i++) {
            Materials cable = cables[i + 2];
            ItemStack machinehull = ItemList.MACHINE_HULLS[i + 2].get(1L);
            GTModHandler.addCraftingRecipe(
                ItemRegistry.acidGens[i],
                RecipeLoader.BITSD,
                new Object[] { "HRH", "HCH", "HKH", 'H', bats[i], 'K',
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, cable, 1L), 'C', machinehull, 'R', chreac[i] });
        }

        GTModHandler.addCraftingRecipe(
            ItemRegistry.acidGensLV,
            RecipeLoader.BITSD,
            new Object[] { "HRH", "KCK", "HKH", 'H', ItemList.Battery_Hull_LV.get(1L), 'K',
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L), 'C', ItemList.Hull_LV.get(1L), 'R',
                ItemList.Machine_LV_ChemicalReactor.get(1L), });

        for (int i = 0; i < 9; i++) {
            try {
                Materials cable = cables[i];
                ItemStack hull = hulls[i] instanceof Materials
                    ? GTOreDictUnificator.get(OrePrefixes.plate, hulls[i], 1L)
                    : ((Werkstoff) hulls[i]).get(OrePrefixes.plate);
                ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);

                GTModHandler.addCraftingRecipe(
                    ItemRegistry.energyDistributor[i],
                    RecipeLoader.BITSD,
                    new Object[] { "PWP", "WCW", "PWP", 'W', GTOreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                        'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode12A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt12, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode8A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt08, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode4A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt04, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode2A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WDW", "DCD", "PDP", 'D', OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt02, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode16A[i],
                    RecipeLoader.BITSD,
                    new Object[] { "WHW", "DCD", "PDP", 'H', OrePrefixes.componentCircuit.get(Materials.Inductor), 'D',
                        OrePrefixes.componentCircuit.get(Materials.Diode), 'W',
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L), 'P', hull, 'C', machinehull });

            } catch (ArrayIndexOutOfBoundsException ignored) {

            }

        }

        String[] stones = { "stone", "stoneSmooth" };
        String[] granites = { "blockGranite", "stoneGranite", "Granite", "granite" };
        for (String granite : granites) {
            for (String stone : stones) {
                GTModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    GTModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "SSS", "DfD", " h ", 'S', stone, 'D',
                        new ItemStack(GregTechAPI.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), });
                GTModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    GTModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "hDf", "SSS", 'S', stone, 'D',
                        new ItemStack(GregTechAPI.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), });
                GTModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    GTModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "SSS", "DfD", " h ", 'S', stone, 'D', granite, });
                GTModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    GTModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { "hDf", "SSS", 'S', stone, 'D', granite, });
            }
            GTModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                GTModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "STS", "h f", "SBS", 'S', granite, 'T', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    'B', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1), });
        }

        GTModHandler.addCraftingRecipe(
            new MTEManualTrafo(ManualTrafo.ID, "bw.manualtrafo", StatCollector.translateToLocal("tile.manutrafo.name"))
                .getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "SCS", "CHC", "ZCZ", 'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L),
                'C', new ItemStack(ItemRegistry.BW_BLOCKS[2]), 'H', ItemList.Hull_HV.get(1L), 'Z', "circuitAdvanced" });

        GTModHandler.addCraftingRecipe(
            new MTEWindmill(Windmill.ID, "bw.windmill", StatCollector.translateToLocal("tile.bw.windmill.name"))
                .getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "BHB", "WGW", "BWB", 'B', new ItemStack(Blocks.brick_block), 'W',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), 'H', new ItemStack(Blocks.hopper), 'G',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2), });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "STS", "h f", "SBS", 'S',
                new ItemStack(GregTechAPI.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), 'T',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0), 'B',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Items.leather), 'W', "logWood", });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Blocks.carpet), 'W', "logWood", });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Items.paper), 'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'Z',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'E',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'Z',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'D',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "WEs", "WZh", "WDf", 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'D',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.LEATHER_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'W',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.WOOL_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'W',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PAPER_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5), 'W',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.COMBINED_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6), 'W',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROTORBLOCK),
            RecipeLoader.BITSD,
            new Object[] { "WRW", "RGR", "WRW", 'R', GTOreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 1L), 'W',
                "plankWood", 'G', GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Iron, 1L), });

        GTModHandler.addCraftingRecipe(
            ItemRegistry.THTR,
            RecipeLoader.BITSD,
            new Object[] { "BZB", "BRB", "BZB", 'B', new ItemStack(GregTechAPI.sBlockCasings3, 1, 12), 'R',
                GTModHandler.getModItem(IndustrialCraft2.ID, "blockGenerator", 1, 5), 'Z', "circuitUltimate" });

        GTModHandler.addCraftingRecipe(
            ItemRegistry.HTGR,
            RecipeLoader.BITSD,
            new Object[] { "BZB", "BRB", "BZB", 'B', new ItemStack(GregTechAPI.sBlockCasings8, 1, 5), 'R',
                GTModHandler.getModItem(IndustrialCraft2.ID, "blockGenerator", 1, 5), 'Z', "circuitSuperconductor" });

        // DNAExtractionModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[0],
            RecipeLoader.BITSD,
            new Object[] { "TET", "CFC", "TST", 'T', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L),
                'E', ItemList.Emitter_EV.get(1L), 'C',
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L), 'S',
                ItemList.Sensor_EV.get(1L), 'F', ItemList.Field_Generator_EV.get(1L) });

        // PCRThermoclyclingModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[1],
            RecipeLoader.BITSD,
            new Object[] { "NEN", "CFC", "NSN", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L), 'E', ItemList.Emitter_EV.get(1L),
                'C', GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L), 'S',
                ItemList.Sensor_EV.get(1L), 'F', ItemList.Field_Generator_EV.get(1L) });

        // PlasmidSynthesisModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[2],
            RecipeLoader.BITSD,
            new Object[] { "SFE", "CPC", "NFN", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L), 'C', "circuit" + Materials.EV,
                'F', ItemList.Field_Generator_EV.get(1L), 'E', ItemList.Emitter_EV.get(1L), 'S',
                ItemList.Sensor_EV.get(1L), 'P', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L), });
        // TransformationModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[3],
            RecipeLoader.BITSD,
            new Object[] { "SFE", "CPC", "NFN", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 1L), 'C', "circuit" + Materials.LuV,
                'F', ItemList.Field_Generator_LuV.get(1L), 'E', ItemList.Emitter_LuV.get(1L), 'S',
                ItemList.Sensor_LuV.get(1L), 'P', WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 1), });

        // ClonalCellularSynthesisModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[4],
            RecipeLoader.BITSD,
            new Object[] { "FEF", "CPC", "FSF", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 1L), 'C', "circuit" + Materials.LuV,
                'F', ItemList.Field_Generator_LuV.get(1L), 'E', ItemList.Emitter_LuV.get(1L), 'S',
                ItemList.Sensor_LuV.get(1L), 'P', WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 1), });

        GTModHandler.addCraftingRecipe(
            new MTEBioVat(BioVat.ID, "bw.biovat", StatCollector.translateToLocal("tile.biovat.name")).getStackForm(1L),
            RecipeLoader.BITSD,
            new Object[] { "GCG", "KHK", "GCG", 'G', new ItemStack(ItemRegistry.bw_glasses[0], 1, 1), 'C',
                "circuit" + Materials.EV, 'K', GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Silver, 1L), 'H',
                ItemList.MACHINE_HULLS[3].get(1L) });

        ItemStack[] Pistons2 = { ItemList.Electric_Piston_HV.get(1L), ItemList.Electric_Piston_EV.get(1L),
            ItemList.Electric_Piston_IV.get(1L), ItemList.Electric_Piston_LuV.get(1L),
            ItemList.Electric_Piston_ZPM.get(1L), ItemList.Electric_Piston_UV.get(1L) };
        ItemStack[] BioLab2 = new ItemStack[GTValues.VN.length - 3];
        ItemStack[] RadioHatch2 = new ItemStack[GTValues.VN.length - 3];
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

        for (int i = 3; i < GTValues.VN.length - 1; i++) {
            BioLab2[i - 3] = new MTEBioLab(
                BioLab[i - 3],
                "bw.biolab" + GTValues.VN[i],
                GTValues.VN[i] + " " + StatCollector.translateToLocal("tile.biolab.name"),
                i).getStackForm(1L);
            RadioHatch2[i - 3] = new GT_MetaTileEntity_RadioHatch(
                RadioHatch[i - 3],
                "bw.radiohatch" + GTValues.VN[i],
                GTValues.VN[i] + " " + StatCollector.translateToLocal("tile.radiohatch.name"),
                i).getStackForm(1L);
            try {
                ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);
                GTModHandler.addCraftingRecipe(
                    BioLab2[i - 3],
                    RecipeLoader.BITSD,
                    new Object[] { "PFP", "WCW", "OGO", 'F',
                        GTOreDictUnificator.get(OrePrefixes.frameGt, hulls2[i - 3], 1L), 'W',
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, wireMat2[i - 3], 1L), 'P',
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1L), 'O',
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polystyrene, 1L), 'G',
                        "circuit" + circuits2[i - 3], 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    RadioHatch2[i - 3],
                    RecipeLoader.BITSD,
                    new Object[] { "DPD", "DCD", "DKD", 'D',
                        GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 1L), 'C', machinehull, 'K',
                        GTOreDictUnificator.get(OrePrefixes.cableGt08, cables2[i - 3], 1L), 'P', Pistons2[i - 3] });
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }

    }
}
