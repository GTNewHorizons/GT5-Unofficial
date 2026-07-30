package bartworks.common.loaders.recipes;

import static gregtech.api.enums.MetaTileEntityIDs.BioLab_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BioLab_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LESU;
import static gregtech.api.enums.MetaTileEntityIDs.ManualTrafo;
import static gregtech.api.enums.MetaTileEntityIDs.RadioHatch_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Windmill;
import static gregtech.api.enums.Mods.IndustrialCraft2;

import gregtech.api.enums.materials2.MaterialFacades;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.ItemRegistry;
import bartworks.common.loaders.RecipeLoader;
import bartworks.common.tileentities.multis.MTELESU;
import bartworks.common.tileentities.multis.MTEManualTrafo;
import bartworks.common.tileentities.multis.MTEWindmill;
import bartworks.common.tileentities.tiered.MTEBioLab;
import bartworks.common.tileentities.tiered.MTERadioHatch;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.CircuitComponents;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TieredItems;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class CraftingRecipes implements Runnable {

    @Override
    public void run() {

        Material[] cables = { // Cable material used in the acid gen, diode and energy distributor below
            Materials.Lead, // ULV
            Materials.Tin, // LV
            Materials.AnnealedCopper, // MV
            Materials.Gold, // HV
            Materials.Aluminium, // EV
            Materials.Tungsten, // IV
            Materials.VanadiumGallium, // LuV
            Materials.Naquadah, // ZPM
            Materials.NaquadahAlloy // UV
        };

        Material[] hulls = { // Plate material used in the acid gen, diode and energy distributor below
            Materials.CastIron, // ULV
            Materials.Steel, // LV
            Materials.Aluminium, // MV
            Materials.StainlessSteel, // HV
            Materials.Titanium, // EV
            Materials.TungstenSteel, // IV
            Materials.RhodiumPlatedPalladium, // LuV
            Materials.Iridium, // ZPM
            Materials.Osmium, // UV
            Materials.Naquadah // UHV
        };

        ItemStack[] bats = { ItemList.Battery_Hull_LV.get(1L), ItemList.Battery_Hull_MV.get(1L),
            ItemList.Battery_Hull_HV.get(1L), ItemList.BatteryHull_EV.get(1L) };
        ItemStack[] chreac = { ItemList.Machine_MV_ChemicalReactor.get(1L), ItemList.Machine_HV_ChemicalReactor.get(1L),
            ItemList.Machine_EV_ChemicalReactor.get(1L), ItemList.Machine_IV_ChemicalReactor.get(1L) };

        GTModHandler.addCraftingRecipe(
            new MTELESU(LESU.ID, "LESU", "L.E.S.U.").getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CDC", "SBS", "CFC", 'C', "circuitAdvanced", 'D', ItemList.Cover_Screen.get(1L), 'S',
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Platinum, 1L), 'B',
                new ItemStack(ItemRegistry.BW_BLOCKS[1]), 'F', ItemList.Field_Generator_HV.get(1L) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_MV),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DS ", "DP ", "DCB", 'D',
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, (int) (1L)), 'S',
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, (int) (1L)), 'P',
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.plate, (int) (1L)), 'C',
                "circuitGood", 'B', TieredItems.MV.getBattery(1) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_LV),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DS ", "DP ", "DCB", 'D',
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, (int) (1L)), 'S',
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.stick, (int) (1L)), 'P',
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plate, (int) (1L)), 'C',
                "circuitBasic", 'B', ItemList.IC2_ReBattery.get(1L) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROCKCUTTER_HV),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DS ", "DP ", "DCB", 'D',
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, (int) (1L)), 'S',
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.stick, (int) (1L)), 'P',
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.plate, (int) (1L)), 'C',
                "circuitAdvanced", 'B', TieredItems.HV.getBattery(1) });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.TESLASTAFF),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "BO ", "OP ", "  P", 'O',
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialFacades.SuperconductorUHV, 1), 'B',
                ItemList.Energy_LapotronicOrb.get(1L), 'P', "plateAlloyIridium", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPPARTS, 1, 0), // tube
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { " fG", " G ", "G  ", 'G', ItemList.Circuit_Parts_Glass_Tube.get(1L) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPPARTS, 1, 1), // motor
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "GLP", "LSd", "PfT", 'G',
                MaterialLibAPI.getStack(Materials.Steel, Shapes.gearGtSmall, (int) (1L)), 'L',
                MaterialLibAPI.getStack(Materials.Steel, Shapes.stickLong, (int) (1L)), 'S',
                MaterialLibAPI.getStack(Materials.Steel, Shapes.screw, (int) (1L)), 'P',
                new ItemStack(Blocks.piston), 'T', new ItemStack(ItemRegistry.PUMPPARTS, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PUMPBLOCK, 1, 0),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "IPI", "PMP", "ISI", 'I',
                MaterialLibAPI.getStack(Materials.Iron, Shapes.plate, (int) (1L)), 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Wood, 1L), 'M',
                new ItemStack(ItemRegistry.PUMPPARTS, 1, 1), 'S', "craftingBlastFurnace" });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.WINDMETER),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SWF", "Sf ", "Ss ", 'S', "stickWood", 'W', new ItemStack(Blocks.wool, 1, Short.MAX_VALUE),
                'F', new ItemStack(Items.string), });

        for (int i = 0; i < 4; i++) {
            Material cable = cables[i + 2];
            ItemStack machinehull = ItemList.MACHINE_HULLS[i + 2].get(1L);
            GTModHandler.addCraftingRecipe(
                ItemRegistry.acidGens[i],
                RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "HRH", "HCH", "HKH", 'H', bats[i], 'K',
                    GTOreDictUnificator.get(OrePrefixes.cableGt01, cable, 1L), 'C', machinehull, 'R', chreac[i] });
        }

        GTModHandler.addCraftingRecipe(
            ItemRegistry.acidGensLV,
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "HRH", "KCK", "HKH", 'H', ItemList.Battery_Hull_LV.get(1L), 'K',
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L), 'C',
                ItemList.Hull_LV.get(1L), 'R', ItemList.Machine_LV_ChemicalReactor.get(1L), });

        for (int i = 0; i < 9; i++) {
            try {
                Material cable = cables[i];
                ItemStack hull = GTOreDictUnificator.get(OrePrefixes.plate, hulls[i], 1L);
                ItemStack machinehull = ItemList.MACHINE_HULLS[i].get(1L);

                GTModHandler.addCraftingRecipe(
                    ItemRegistry.energyDistributor[i],
                    RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PWP", "WCW", "PWP", 'W', GTOreDictUnificator.get(OrePrefixes.wireGt16, cable, 1L),
                        'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode12A[i],
                    RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "WDW", "DCD", "PDP", 'D', CircuitComponents.DIODE.getIngredient(), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt12, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode8A[i],
                    RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "WDW", "DCD", "PDP", 'D', CircuitComponents.DIODE.getIngredient(), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt08, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode4A[i],
                    RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "WDW", "DCD", "PDP", 'D', CircuitComponents.DIODE.getIngredient(), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt04, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode2A[i],
                    RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "WDW", "DCD", "PDP", 'D', CircuitComponents.DIODE.getIngredient(), 'W',
                        GTOreDictUnificator.get(OrePrefixes.cableGt02, cable, 1L), 'P', hull, 'C', machinehull });
                GTModHandler.addCraftingRecipe(
                    ItemRegistry.diode16A[i],
                    RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "WHW", "DCD", "PDP", 'H', CircuitComponents.INDUCTOR.getIngredient(), 'D',
                        CircuitComponents.DIODE.getIngredient(), 'W',
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
                    GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SSS", "DfD", " h ", 'S', stone, 'D',
                        new ItemStack(GregTechAPI.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), });
                GTModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "hDf", "SSS", 'S', stone, 'D',
                        new ItemStack(GregTechAPI.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), });
                GTModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SSS", "DfD", " h ", 'S', stone, 'D', granite, });
                GTModHandler.addCraftingRecipe(
                    new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1),
                    GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "hDf", "SSS", 'S', stone, 'D', granite, });
            }
            GTModHandler.addCraftingRecipe(
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
                GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "STS", "h f", "SBS", 'S', granite, 'T', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0),
                    'B', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1), });
        }

        GTModHandler.addCraftingRecipe(
            new MTEManualTrafo(ManualTrafo.ID, "bw.manualtrafo", StatCollector.translateToLocal("tile.manutrafo.name"))
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SCS", "CHC", "ZCZ", 'S',
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.screw, (int) (1L)), 'C',
                new ItemStack(ItemRegistry.BW_BLOCKS[2]), 'H', ItemList.Hull_HV.get(1L), 'Z', "circuitAdvanced" });

        GTModHandler.addCraftingRecipe(
            new MTEWindmill(Windmill.ID, "bw.windmill", StatCollector.translateToLocal("tile.bw.windmill.name"))
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "BHB", "WGW", "BWB", 'B', new ItemStack(Blocks.brick_block), 'W',
                MaterialLibAPI.getStack(Materials.Iron, Shapes.gearGt, (int) (1L)), 'H',
                new ItemStack(Blocks.hopper), 'G', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2), });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 2),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "STS", "h f", "SBS", 'S',
                new ItemStack(GregTechAPI.sBlockGranites, 1, OreDictionary.WILDCARD_VALUE), 'T',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 0), 'B',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 1), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Items.leather), 'W', "logWood", });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Blocks.carpet), 'W', "logWood", });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WLs", "WLh", "WLf", 'L', new ItemStack(Items.paper), 'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WEs", "WZh", "WDf", 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'Z',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WEs", "WZh", "WDf", 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'E',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WEs", "WZh", "WDf", 'D', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'Z',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WEs", "WZh", "WDf", 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'D',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WEs", "WZh", "WDf", 'Z', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'D',
                new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'E', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5),
                'W', "logWood", });

        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.LEATHER_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 3), 'W',
                MaterialLibAPI.getStack(Materials.Iron, Shapes.gearGt, (int) (1L)), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.WOOL_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 4), 'W',
                MaterialLibAPI.getStack(Materials.Iron, Shapes.gearGt, (int) (1L)), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.PAPER_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 5), 'W',
                MaterialLibAPI.getStack(Materials.Iron, Shapes.gearGt, (int) (1L)), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.COMBINED_ROTOR),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "hPf", "PWP", "sPr", 'P', new ItemStack(ItemRegistry.CRAFTING_PARTS, 1, 6), 'W',
                MaterialLibAPI.getStack(Materials.Iron, Shapes.gearGt, (int) (1L)), });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemRegistry.ROTORBLOCK),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "WRW", "RGR", "WRW", 'R',
                MaterialLibAPI.getStack(Materials.Iron, Shapes.ring, (int) (1L)), 'W', "plankWood",
                'G', MaterialLibAPI.getStack(Materials.Iron, Shapes.gearGt, (int) (1L)), });

        GTModHandler.addCraftingRecipe(
            ItemRegistry.THTR,
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "BZB", "BRB", "BZB", 'B', new ItemStack(GregTechAPI.sBlockCasings3, 1, 12), 'R',
                GTModHandler.getModItem(IndustrialCraft2.ID, "blockGenerator", 1, 5), 'Z', "circuitUltimate" });

        // DNAExtractionModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[0],
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "TET", "CFC", "TST", 'T',
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plate, (int) (1L)), 'E',
                ItemList.Emitter_EV.get(1L), 'C',
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L), 'S',
                ItemList.Sensor_EV.get(1L), 'F', ItemList.Field_Generator_EV.get(1L) });

        // PCRThermoclyclingModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[1],
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "NEN", "CFC", "NSN", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L), 'E',
                ItemList.Emitter_EV.get(1L), 'C',
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Aluminium, 1L), 'S',
                ItemList.Sensor_EV.get(1L), 'F', ItemList.Field_Generator_EV.get(1L) });

        // PlasmidSynthesisModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[2],
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SFE", "CPC", "NFN", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L), 'C',
                OrePrefixes.circuit.oreDictName(Circuits.EV.materialName()), 'F', ItemList.Field_Generator_EV.get(1L),
                'E', ItemList.Emitter_EV.get(1L), 'S', ItemList.Sensor_EV.get(1L), 'P',
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plate, (int) (1L)), });
        // TransformationModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[3],
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SFE", "PCP", "NFN", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Nichrome, 1L), 'C',
                OrePrefixes.circuit.oreDictName(Circuits.EV.materialName()), 'F', ItemList.Field_Generator_EV.get(1L),
                'E', ItemList.Emitter_EV.get(1L), 'S', ItemList.Sensor_EV.get(1L), 'P',
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plate, (int) (1L)), });

        // ClonalCellularSynthesisModule
        GTModHandler.addCraftingRecipe(
            BioItemList.mBioLabParts[4],
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "FEF", "CPC", "FSF", 'N',
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 1L), 'C',
                OrePrefixes.circuit.oreDictName(Circuits.LuV.materialName()), 'F', ItemList.Field_Generator_LuV.get(1L),
                'E', ItemList.Emitter_LuV.get(1L), 'S', ItemList.Sensor_LuV.get(1L), 'P',
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.plate, 1), });

        GTModHandler.addCraftingRecipe(
            ItemRegistry.vat.copy(),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "GCG", "KHK", "GCG", 'G', new ItemStack(ItemRegistry.bw_glasses[0], 1, 1), 'C',
                OrePrefixes.circuit.oreDictName(Circuits.EV.materialName()), 'K',
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Silver, 1L), 'H',
                ItemList.MACHINE_HULLS[3].get(1L) });

        // BioLabs
        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_HV.ID, "bw.biolabHV", StatCollector.translateToLocal("tile.biolab.name"), 3)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Kanthal, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.HV.materialName()), 'C', ItemList.MACHINE_HULLS[3].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_EV.ID, "bw.biolabEV", StatCollector.translateToLocal("tile.biolab.name"), 4)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Nichrome, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.EV.materialName()), 'C', ItemList.MACHINE_HULLS[4].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_IV.ID, "bw.biolabIV", StatCollector.translateToLocal("tile.biolab.name"), 5)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.TPVAlloy, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.IV.materialName()), 'C', ItemList.MACHINE_HULLS[5].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_LuV.ID, "bw.biolabLuV", StatCollector.translateToLocal("tile.biolab.name"), 6)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Chrome, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.HSSG, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.LuV.materialName()), 'C', ItemList.MACHINE_HULLS[6].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_ZPM.ID, "bw.biolabZPM", StatCollector.translateToLocal("tile.biolab.name"), 7)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.ZPM.materialName()), 'C', ItemList.MACHINE_HULLS[7].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_UV.ID, "bw.biolabUV", StatCollector.translateToLocal("tile.biolab.name"), 8)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.UV.materialName()), 'C', ItemList.MACHINE_HULLS[8].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_UHV.ID, "bw.biolabUHV", StatCollector.translateToLocal("tile.biolab.name"), 9)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.ElectrumFlux, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.UHV.materialName()), 'C', ItemList.MACHINE_HULLS[9].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_UEV.ID, "bw.biolabUEV", StatCollector.translateToLocal("tile.biolab.name"), 10)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bedrockium, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.DraconiumAwakened, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.UEV.materialName()), 'C',
                ItemList.MACHINE_HULLS[10].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_UIV.ID, "bw.biolabUIV", StatCollector.translateToLocal("tile.biolab.name"), 11)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1L), 'W',
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Infinity, 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.UIV.materialName()), 'C',
                ItemList.MACHINE_HULLS[11].get(1L) });

        GTModHandler.addCraftingRecipe(
            new MTEBioLab(BioLab_UMV.ID, "bw.biolabUMV", StatCollector.translateToLocal("tile.biolab.name"), 12)
                .getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "WCW", "OGO", 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L), 'W',
                GTOreDictUnificator.get("wireGt01Hypogen", 1L), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'O', MaterialLibAPI.getStack(Materials.Polystyrene, Shapes.plate, (int) (1L)), 'G',
                OrePrefixes.circuit.oreDictName(Circuits.UMV.materialName()), 'C',
                ItemList.MACHINE_HULLS[12].get(1L) });

        // Radio Hatches
        GTModHandler.addCraftingRecipe(
            new MTERadioHatch(
                RadioHatch_HV.ID,
                "bw.radiohatchHV",
                StatCollector.translateToLocal("tile.radiohatch.name"),
                3).getStackForm(1L),
            RecipeLoader.BITSD | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DPD", "DCD", "DKD", 'D',
                MaterialLibAPI.getStack(Materials.Lead, Shapes.plateDense, (int) (1L)), 'C',
                ItemList.MACHINE_HULLS[3].get(1L), 'K',
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.Gold, 1L), 'P',
                ItemList.Electric_Piston_HV.get(1) });

        GTModHandler.addCraftingRecipe(
            ItemList.Item_Power_Goggles.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "MPM", "LSL", "PRP", 'M', ItemList.Cover_Screen.get(1), 'P',
                MaterialLibAPI
                    .getStack(Materials.Polytetrafluoroethylene, Shapes.plate, (int) (1L)),
                'L', MaterialLibAPI.getStack(Materials.GarnetYellow, Shapes.lens, (int) (1L)), 'S',
                ItemList.Sensor_IV.get(1), 'R', OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber) });

        GTModHandler.addCraftingRecipe(
            ItemList.Item_Redstone_Sniffer.get(1L),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { " M ", "STS", "dPw", 'M', ItemList.Cover_Screen.get(1L), 'S',
                OrePrefixes.screw.ingredient(Materials.Titanium), 'T',
                GregtechItemList.TransmissionComponent_EV.get(1), 'P',
                OrePrefixes.plate.ingredient(Materials.Titanium) });

        GTModHandler.addCraftingRecipe(
            ItemList.Tool_Vajra.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "RMR", "hCd", "EBE", 'R', OrePrefixes.lens.ingredient(Materials.Amethyst), 'M',
                ItemList.Magnetron.get(1), 'C', ItemList.Vajra_Core.get(1), 'E',
                OrePrefixes.plateDense.ingredient(Materials.Silver), 'B',
                TieredItems.IV.getBatteryIngredient() });

        GTModHandler.addCraftingRecipe(
            ItemList.Magnetron.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DCD", "PWP", "DCD", 'D',
                OrePrefixes.plateDense.ingredient(Materials.NeodymiumMagnetic), 'C', ItemList.HV_Coil, 'P',
                OrePrefixes.plate.ingredient(Materials.Silver), 'W',
                MaterialParts.namedIngredient(OrePrefixes.wireGt12, MaterialFacades.SuperconductorIV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Vajra_Core.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "wEh", "ITI", "SRS", 'E', OrePrefixes.plate.ingredient(Materials.Silver), 'I',
                OrePrefixes.plateDense.ingredient(Materials.Iridium), 'T', ItemList.Transformer_EV_HV.get(1),
                'S', MaterialParts.namedIngredient(OrePrefixes.wireGt12, MaterialFacades.SuperconductorIV), 'R',
                ItemList.Transformer_IV_EV.get(1) });
    }
}
