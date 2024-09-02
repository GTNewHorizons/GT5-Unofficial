package gregtech.loaders.preload;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TwilightForest;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class LoaderGTOreDictionary implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: Register OreDict Entries of Non-GT-Items.");
        GTOreDictUnificator.set(OrePrefixes.cell, Materials.Empty, ItemList.Cell_Empty.get(1L));
        GTOreDictUnificator.set(OrePrefixes.cell, Materials.Lava, ItemList.Cell_Lava.get(1L));
        GTOreDictUnificator.set(OrePrefixes.cell, Materials.Lava, GTModHandler.getIC2Item("lavaCell", 1L));
        GTOreDictUnificator.set(OrePrefixes.cell, Materials.Water, ItemList.Cell_Water.get(1L));
        GTOreDictUnificator.set(OrePrefixes.cell, Materials.Water, GTModHandler.getIC2Item("waterCell", 1L));
        GTOreDictUnificator.set(
            OrePrefixes.cell,
            Materials.Creosote,
            GTModHandler.getModItem(Railcraft.ID, "fluid.creosote.cell", 1L));

        GTOreDictUnificator.set(OrePrefixes.cell, Materials.UUMatter, GTModHandler.getIC2Item("uuMatterCell", 1L));
        GTOreDictUnificator.set(OrePrefixes.cell, Materials.ConstructionFoam, GTModHandler.getIC2Item("CFCell", 1L));

        GTOreDictUnificator.set(OrePrefixes.bucket, Materials.Empty, new ItemStack(Items.bucket, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.bucket, Materials.Water, new ItemStack(Items.water_bucket, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.bucket, Materials.Lava, new ItemStack(Items.lava_bucket, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.bucket, Materials.Milk, new ItemStack(Items.milk_bucket, 1, 0));
        // Clay buckets handled in gregtech.common.GTProxy.onLoad() as they aren't registered until Iguana Tweaks
        // pre-init.

        GTOreDictUnificator.set(OrePrefixes.bottle, Materials.Empty, new ItemStack(Items.glass_bottle, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.bottle, Materials.Water, new ItemStack(Items.potionitem, 1, 0));

        GTOreDictUnificator.set(OrePrefixes.plateAlloy, Materials.Iridium, GTModHandler.getIC2Item("iridiumPlate", 1L));
        GTOreDictUnificator
            .set(OrePrefixes.plateAlloy, Materials.Advanced, GTModHandler.getIC2Item("advancedAlloy", 1L));
        GTOreDictUnificator.set(OrePrefixes.plateAlloy, Materials.Carbon, GTModHandler.getIC2Item("carbonPlate", 1L));

        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Coal, new ItemStack(Blocks.coal_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Iron, new ItemStack(Blocks.iron_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Lapis, new ItemStack(Blocks.lapis_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Redstone, new ItemStack(Blocks.redstone_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Redstone, new ItemStack(Blocks.lit_redstone_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Gold, new ItemStack(Blocks.gold_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Diamond, new ItemStack(Blocks.diamond_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.Emerald, new ItemStack(Blocks.emerald_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ore, Materials.NetherQuartz, new ItemStack(Blocks.quartz_ore, 1));
        GTOreDictUnificator.set(OrePrefixes.ingot, Materials.Copper, GTModHandler.getIC2Item("copperIngot", 1L));
        GTOreDictUnificator.set(OrePrefixes.ingot, Materials.Tin, GTModHandler.getIC2Item("tinIngot", 1L));
        GTOreDictUnificator.set(OrePrefixes.ingot, Materials.Lead, GTModHandler.getIC2Item("leadIngot", 1L));
        GTOreDictUnificator.set(OrePrefixes.ingot, Materials.Bronze, GTModHandler.getIC2Item("bronzeIngot", 1L));
        GTOreDictUnificator.set(OrePrefixes.ingot, Materials.Silver, GTModHandler.getIC2Item("silverIngot", 1L));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.Iridium, GTModHandler.getIC2Item("iridiumOre", 1L));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.Lapis, new ItemStack(Items.dye, 1, 4));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.EnderEye, new ItemStack(Items.ender_eye, 1));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.EnderPearl, new ItemStack(Items.ender_pearl, 1));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.Diamond, new ItemStack(Items.diamond, 1));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.Emerald, new ItemStack(Items.emerald, 1));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.Coal, new ItemStack(Items.coal, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.Charcoal, new ItemStack(Items.coal, 1, 1));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.NetherQuartz, new ItemStack(Items.quartz, 1));
        GTOreDictUnificator.set(OrePrefixes.gem, Materials.NetherStar, new ItemStack(Items.nether_star, 1));
        GTOreDictUnificator.set(OrePrefixes.nugget, Materials.Gold, new ItemStack(Items.gold_nugget, 1));
        GTOreDictUnificator.set(OrePrefixes.ingot, Materials.Gold, new ItemStack(Items.gold_ingot, 1));
        GTOreDictUnificator.set(OrePrefixes.ingot, Materials.Iron, new ItemStack(Items.iron_ingot, 1));
        GTOreDictUnificator.set(OrePrefixes.plate, Materials.Paper, new ItemStack(Items.paper, 1));
        GTOreDictUnificator.set(OrePrefixes.dust, Materials.Sugar, new ItemStack(Items.sugar, 1));
        GTOreDictUnificator.set(OrePrefixes.dust, Materials.Bone, ItemList.Dye_Bonemeal.get(1L));
        GTOreDictUnificator.set(OrePrefixes.stick, Materials.Wood, new ItemStack(Items.stick, 1));
        GTOreDictUnificator.set(OrePrefixes.dust, Materials.Redstone, new ItemStack(Items.redstone, 1));
        GTOreDictUnificator.set(OrePrefixes.dust, Materials.Gunpowder, new ItemStack(Items.gunpowder, 1));
        GTOreDictUnificator.set(OrePrefixes.dust, Materials.Glowstone, new ItemStack(Items.glowstone_dust, 1));
        GTOreDictUnificator.set(OrePrefixes.dust, Materials.Blaze, new ItemStack(Items.blaze_powder, 1));
        GTOreDictUnificator.set(OrePrefixes.stick, Materials.Blaze, new ItemStack(Items.blaze_rod, 1));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Iron, new ItemStack(Blocks.iron_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Gold, new ItemStack(Blocks.gold_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Diamond, new ItemStack(Blocks.diamond_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Emerald, new ItemStack(Blocks.emerald_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Lapis, new ItemStack(Blocks.lapis_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Coal, new ItemStack(Blocks.coal_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Redstone, new ItemStack(Blocks.redstone_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.NetherQuartz, new ItemStack(Blocks.quartz_block, 1, 0));
        GTOreDictUnificator.set(OrePrefixes.block, Materials.Clay, new ItemStack(Blocks.clay, 1, 0));
        if (Blocks.ender_chest != null) {
            GTOreDictUnificator.registerOre(OreDictNames.enderChest, new ItemStack(Blocks.ender_chest, 1));
        }
        GTOreDictUnificator.registerOre(OreDictNames.craftingAnvil, new ItemStack(Blocks.anvil, 1));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingAnvil, GTModHandler.getModItem(Railcraft.ID, "anvil", 1L, 0));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingIndustrialDiamond, ItemList.IC2_Industrial_Diamond.get(1L));
        GTOreDictUnificator
            .registerOre(OrePrefixes.glass, Materials.Reinforced, GTModHandler.getIC2Item("reinforcedGlass", 1L));

        GTOreDictUnificator
            .registerOre(OrePrefixes.stone, Materials.Basalt, GTModHandler.getModItem(Railcraft.ID, "cube", 1L, 6));
        GTOreDictUnificator
            .registerOre(OrePrefixes.stone, Materials.Marble, GTModHandler.getModItem(Railcraft.ID, "cube", 1L, 7));
        GTOreDictUnificator.registerOre(
            OrePrefixes.stone,
            Materials.Basalt,
            GTModHandler.getModItem(Railcraft.ID, "brick.abyssal", 1L, 32767));
        GTOreDictUnificator.registerOre(
            OrePrefixes.stone,
            Materials.Marble,
            GTModHandler.getModItem(Railcraft.ID, "brick.quarried", 1L, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.stone, Materials.Obsidian, new ItemStack(Blocks.obsidian, 1, 32767));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Stone, new ItemStack(Blocks.stone, 1, 32767));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneMossy, new ItemStack(Blocks.mossy_cobblestone, 1, 32767));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneCobble, new ItemStack(Blocks.mossy_cobblestone, 1, 32767));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneCobble, new ItemStack(Blocks.cobblestone, 1, 32767));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneSmooth, new ItemStack(Blocks.stone, 1, 32767));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneBricks, new ItemStack(Blocks.stonebrick, 1, 32767));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneMossy, new ItemStack(Blocks.stonebrick, 1, 1));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneCracked, new ItemStack(Blocks.stonebrick, 1, 2));
        GTOreDictUnificator.registerOre(OrePrefixes.stoneChiseled, new ItemStack(Blocks.stonebrick, 1, 3));
        GTOreDictUnificator.registerOre(OrePrefixes.stone, Materials.Sand, new ItemStack(Blocks.sandstone, 1, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.stone, Materials.Netherrack, new ItemStack(Blocks.netherrack, 1, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.stone, Materials.NetherBrick, new ItemStack(Blocks.nether_brick, 1, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.stone, Materials.Endstone, new ItemStack(Blocks.end_stone, 1, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.stone, Materials.Glowstone, new ItemStack(Blocks.glowstone, 1, 32767));

        GTOreDictUnificator
            .registerOre("paperResearchFragment", GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 9));
        GTOreDictUnificator.registerOre(
            "itemCertusQuartz",
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1));
        GTOreDictUnificator.registerOre(
            "itemCertusQuartz",
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10));
        GTOreDictUnificator.registerOre(
            "itemNetherQuartz",
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11));
        GTOreDictUnificator.registerOre(
            OreDictNames.craftingQuartz,
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1));
        GTOreDictUnificator.registerOre(
            OreDictNames.craftingQuartz,
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10));
        GTOreDictUnificator.registerOre(
            OreDictNames.craftingQuartz,
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11));
        GTOreDictUnificator.registerOre("cropLemon", ItemList.FR_Lemon.get(1L));
        GTOreDictUnificator.registerOre("cropCoffee", ItemList.IC2_CoffeeBeans.get(1L));
        GTOreDictUnificator.registerOre("cropPotato", ItemList.Food_Raw_Potato.get(1L));
        GTOreDictUnificator.registerOre(OrePrefixes.battery, Materials.LV, GTModHandler.getIC2Item("reBattery", 1L));
        GTOreDictUnificator
            .registerOre(OrePrefixes.battery, Materials.LV, GTModHandler.getIC2Item("chargedReBattery", 1L, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.battery, Materials.MV, GTModHandler.getIC2Item("advBattery", 1L, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.battery, Materials.HV, GTModHandler.getIC2Item("energyCrystal", 1L, 32767));
        GTOreDictUnificator
            .registerOre(OrePrefixes.battery, Materials.EV, GTModHandler.getIC2Item("lapotronCrystal", 1L, 32767));

        GTOreDictUnificator
            .registerOre(OreDictNames.craftingWireCopper, GTModHandler.getIC2Item("insulatedCopperCableItem", 1L));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingWireGold, GTModHandler.getIC2Item("insulatedGoldCableItem", 1L));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingWireIron, GTModHandler.getIC2Item("insulatedIronCableItem", 1L));
        GTOreDictUnificator.registerOre(
            OreDictNames.craftingWireTin,
            GTModHandler
                .getIC2Item("insulatedTinCableItem", 1L, GTModHandler.getIC2Item("insulatedCopperCableItem", 1L)));

        GTOreDictUnificator
            .registerOre(OreDictNames.craftingRedstoneTorch, new ItemStack(Blocks.redstone_torch, 1, 32767));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingRedstoneTorch, new ItemStack(Blocks.unlit_redstone_torch, 1, 32767));

        GTOreDictUnificator.registerOre(OreDictNames.craftingWorkBench, new ItemStack(Blocks.crafting_table, 1));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingWorkBench, new ItemStack(GregTechAPI.sBlockMachines, 1, 16));

        GTOreDictUnificator.registerOre(OreDictNames.craftingPiston, new ItemStack(Blocks.piston, 1, 32767));
        GTOreDictUnificator.registerOre(OreDictNames.craftingPiston, new ItemStack(Blocks.sticky_piston, 1, 32767));

        GTOreDictUnificator.registerOre(OreDictNames.craftingSafe, new ItemStack(GregTechAPI.sBlockMachines, 1, 45));
        GTOreDictUnificator.registerOre(OreDictNames.craftingSafe, GTModHandler.getIC2Item("personalSafe", 1L));

        GTOreDictUnificator.registerOre(OreDictNames.craftingChest, new ItemStack(Blocks.chest, 1, 32767));
        GTOreDictUnificator.registerOre(OreDictNames.craftingChest, new ItemStack(Blocks.trapped_chest, 1, 32767));

        GTOreDictUnificator.registerOre(OreDictNames.craftingFurnace, new ItemStack(Blocks.furnace, 1, 32767));
        GTOreDictUnificator.registerOre(OreDictNames.craftingFurnace, new ItemStack(Blocks.lit_furnace, 1, 32767));

        GTOreDictUnificator.registerOre(OreDictNames.craftingPump, GTModHandler.getIC2Item("pump", 1L));
        GTOreDictUnificator.registerOre(OreDictNames.craftingElectromagnet, GTModHandler.getIC2Item("magnetizer", 1L));
        GTOreDictUnificator.registerOre(OreDictNames.craftingTeleporter, GTModHandler.getIC2Item("teleporter", 1L));

        GTOreDictUnificator.registerOre(OreDictNames.craftingMacerator, GTModHandler.getIC2Item("macerator", 1L));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingMacerator, new ItemStack(GregTechAPI.sBlockMachines, 1, 50));

        GTOreDictUnificator.registerOre(OreDictNames.craftingExtractor, GTModHandler.getIC2Item("extractor", 1L));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingExtractor, new ItemStack(GregTechAPI.sBlockMachines, 1, 51));

        GTOreDictUnificator.registerOre(OreDictNames.craftingCompressor, GTModHandler.getIC2Item("compressor", 1L));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingCompressor, new ItemStack(GregTechAPI.sBlockMachines, 1, 52));

        GTOreDictUnificator.registerOre(OreDictNames.craftingRecycler, GTModHandler.getIC2Item("recycler", 1L));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingRecycler, new ItemStack(GregTechAPI.sBlockMachines, 1, 53));

        GTOreDictUnificator.registerOre(OreDictNames.craftingIronFurnace, GTModHandler.getIC2Item("ironFurnace", 1L));

        GTOreDictUnificator
            .registerOre(OreDictNames.craftingCentrifuge, new ItemStack(GregTechAPI.sBlockMachines, 1, 62));

        GTOreDictUnificator
            .registerOre(OreDictNames.craftingInductionFurnace, GTModHandler.getIC2Item("inductionFurnace", 1L));

        GTOreDictUnificator
            .registerOre(OreDictNames.craftingElectricFurnace, GTModHandler.getIC2Item("electroFurnace", 1L));
        GTOreDictUnificator
            .registerOre(OreDictNames.craftingElectricFurnace, new ItemStack(GregTechAPI.sBlockMachines, 1, 54));

        GTOreDictUnificator.registerOre(OreDictNames.craftingGenerator, GTModHandler.getIC2Item("generator", 1L));

        GTOreDictUnificator
            .registerOre(OreDictNames.craftingGeothermalGenerator, GTModHandler.getIC2Item("geothermalGenerator", 1L));

        GTOreDictUnificator.registerOre(OreDictNames.craftingFeather, new ItemStack(Items.feather, 1, 32767));
        GTOreDictUnificator.registerOre(
            OreDictNames.craftingFeather,
            GTModHandler.getModItem(TwilightForest.ID, "item.tfFeather", 1L, 32767));

        GTOreDictUnificator.registerOre("itemWheat", new ItemStack(Items.wheat, 1, 32767));
        GTOreDictUnificator.registerOre("paperEmpty", new ItemStack(Items.paper, 1, 32767));
        GTOreDictUnificator.registerOre("paperMap", new ItemStack(Items.map, 1, 32767));
        GTOreDictUnificator.registerOre("paperMap", new ItemStack(Items.filled_map, 1, 32767));
        GTOreDictUnificator.registerOre("bookEmpty", new ItemStack(Items.book, 1, 32767));
        GTOreDictUnificator.registerOre("bookWritable", new ItemStack(Items.writable_book, 1, 32767));
        GTOreDictUnificator.registerOre("bookWritten", new ItemStack(Items.written_book, 1, 32767));
        GTOreDictUnificator.registerOre("bookEnchanted", new ItemStack(Items.enchanted_book, 1, 32767));
        GTOreDictUnificator.registerOre(OreDictNames.craftingBook, new ItemStack(Items.book, 1, 32767));
        GTOreDictUnificator.registerOre(OreDictNames.craftingBook, new ItemStack(Items.writable_book, 1, 32767));
        GTOreDictUnificator.registerOre(OreDictNames.craftingBook, new ItemStack(Items.written_book, 1, 32767));
        GTOreDictUnificator.registerOre(OreDictNames.craftingBook, new ItemStack(Items.enchanted_book, 1, 32767));

        GTOreDictUnificator.addToBlacklist(GTModHandler.getIC2Item("electronicCircuit", 1L));
        GTOreDictUnificator.addToBlacklist(GTModHandler.getIC2Item("advancedCircuit", 1L));
        GTOreDictUnificator
            .registerOre(OrePrefixes.circuit, Materials.LV, GTModHandler.getIC2Item("electronicCircuit", 1L));
        GTOreDictUnificator
            .registerOre(OrePrefixes.circuit, Materials.HV, GTModHandler.getIC2Item("advancedCircuit", 1L));

        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Copper,
            GTModHandler.getModItem(IndustrialCraft2.ID, "itemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Tin,
            GTModHandler.getModItem(IndustrialCraft2.ID, "itemCasing", 1L, 1));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Bronze,
            GTModHandler.getModItem(IndustrialCraft2.ID, "itemCasing", 1L, 2));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Gold,
            GTModHandler.getModItem(IndustrialCraft2.ID, "itemCasing", 1L, 3));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Iron,
            GTModHandler.getModItem(IndustrialCraft2.ID, "itemCasing", 1L, 4));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Steel,
            GTModHandler.getModItem(IndustrialCraft2.ID, "itemCasing", 1L, 5));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Lead,
            GTModHandler.getModItem(IndustrialCraft2.ID, "itemCasing", 1L, 6));

        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Osmium,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.OsmiumItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Aluminium,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.AluminiumItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.StainlessSteel,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.StainlessSteelItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Tungsten,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TungstenItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Neutronium,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.NeutroniumItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.TungstenSteel,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TungstenSteelItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Iridium,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.IridiumItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Titanium,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TitaniumItemCasing", 1L, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.itemCasing,
            Materials.Chrome,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ChromeItemCasing", 1L, 0));

        // Fake Circuits
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.ULV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitULV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.LV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitLV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.MV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitMV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.HV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitHV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.EV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitEV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.IV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitIV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.LuV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitLuV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.ZPM,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitZPM", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.UV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitUV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.UHV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitUHV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.UEV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitUEV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.UIV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitUIV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.UMV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitUMV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.UXV,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitUXV", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.circuit,
            Materials.MAX,
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CircuitMAX", 1L));

        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.Manasteel,
            GTModHandler.getModItem(Botania.ID, "storage", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.Terrasteel,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 1));
        GTOreDictUnificator.registerOre(
            OrePrefixes.ingot,
            MaterialsBotania.ElvenElementium,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 7));
        GTOreDictUnificator.registerOre(
            OrePrefixes.nugget,
            MaterialsBotania.ElvenElementium,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 19));
        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.ElvenElementium,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 2));
        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.Livingrock,
            GTModHandler.getModItem(Botania.ID, "livingrock", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.ingot,
            MaterialsBotania.GaiaSpirit,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 14));
        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.Livingwood,
            GTModHandler.getModItem(Botania.ID, "livingwood", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.Dreamwood,
            GTModHandler.getModItem(Botania.ID, "dreamwood", 1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.gem,
            MaterialsBotania.ManaDiamond,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 2));
        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.ManaDiamond,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 3));
        GTOreDictUnificator.registerOre(
            OrePrefixes.gem,
            MaterialsBotania.BotaniaDragonstone,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 9));
        GTOreDictUnificator.registerOre(
            OrePrefixes.block,
            MaterialsBotania.BotaniaDragonstone,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 4));
    }
}
