package gregtech.loaders.preload;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TwilightForest;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableMap;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class LoaderGTItemData implements Runnable {

    @Override
    public void run() {
        GT_FML_LOGGER.debug("GTMod: Loading Item Data Tags");
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "item.giantPick", 1L, 0),
            new ItemData(
                MU.materialOf(Materials2Materials.Stone),
                696729600L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 464486400L)));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "item.giantSword", 1L, 0),
            new ItemData(
                MU.materialOf(Materials2Materials.Stone),
                464486400L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 232243200L)));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "tile.GiantLog", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 232243200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "tile.GiantCobble", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 232243200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "tile.GiantObsidian", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Obsidian), 232243200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "item.minotaurAxe", 1L, 0),
            new ItemData(
                MU.materialOf(Materials2Materials.Diamond),
                14515200L,
                new MaterialStack(
                    MU.materialOf(Materials2Materials.Wood),
                    OrePrefixes.stick.getMaterialAmount() * 2L)));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "item.armorShards", 1L, 0),
            new ItemData(MU.materialOf(Materials2Materials.Knightmetal), 403200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(TwilightForest.ID, "item.shardCluster", 1L, 0),
            new ItemData(MU.materialOf(Materials2Materials.Knightmetal), 3628800L));
        GTOreDictUnificator.addItemData(
            ItemList.TF_LiveRoot.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.LiveRoot), 3628800L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10),
            new ItemData(MU.materialOf(Materials2Materials.CertusQuartz), 1814400L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11),
            new ItemData(MU.materialOf(Materials2Materials.NetherQuartz), 1814400L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12),
            new ItemData(Materials.Fluix, 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.quartz_block, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.NetherQuartz), 14515200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartz", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.CertusQuartz), 14515200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartzPillar", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.CertusQuartz), 14515200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartzChiseled", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.CertusQuartz), 14515200L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.wheat, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wheat), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.hay_block, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wheat), 32659200L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.snowball, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Snow), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.snow, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Snow), 14515200L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.glowstone, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Glowstone), 14515200L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.redstone_lamp, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Glowstone),
                14515200L,
                new MaterialStack(
                    MU.materialOf(Materials2Materials.Redstone),
                    OrePrefixes.dust.getMaterialAmount() * 4L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.lit_redstone_lamp, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Glowstone),
                14515200L,
                new MaterialStack(
                    MU.materialOf(Materials2Materials.Redstone),
                    OrePrefixes.dust.getMaterialAmount() * 4L)));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Forestry.ID, "craftingMaterial", 1L, 5),
            new ItemData(MU.materialOf(Materials2Materials.Ice), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.ice, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Ice), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.packed_ice, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Ice), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.clay_ball, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Clay), 1814400L));
        GTOreDictUnificator.removeItemData(new ItemStack(Blocks.clay, 1, 0));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.clay, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Clay), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.hardened_clay, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Clay), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stained_hardened_clay, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Clay), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.brick_block, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Brick), 3628800L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Uranium_238.get(1),
            new ItemData(MU.materialOf(Materials2Materials.Uranium), 3628800L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Uranium_235.get(1),
            new ItemData(MU.materialOf(Materials2Materials.Uranium235), 3628800L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Plutonium.get(1),
            new ItemData(MU.materialOf(Materials2Materials.Plutonium), 3628800L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Uranium_235_Small.get(1),
            new ItemData(MU.materialOf(Materials2Materials.Uranium235), 403200L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Plutonium_Small.get(1),
            new ItemData(MU.materialOf(Materials2Materials.Plutonium), 403200L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Item_Casing_Iron.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 1814400L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Item_Casing_Gold.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Gold), 1814400L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Item_Casing_Bronze.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Bronze), 1814400L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Item_Casing_Copper.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Copper), 1814400L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Item_Casing_Tin.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Tin), 1814400L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Item_Casing_Lead.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Lead), 1814400L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Item_Casing_Steel.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Steel), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.book, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Paper), 10886400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.written_book, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Paper), 10886400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.writable_book, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Paper), 10886400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.enchanted_book, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Paper), 10886400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.golden_apple, 1, 1),
            new ItemData(MU.materialOf(Materials2Materials.Gold), OrePrefixes.block.getMaterialAmount() * 8L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.golden_apple, 1, 0),
            new ItemData(MU.materialOf(Materials2Materials.Gold), OrePrefixes.ingot.getMaterialAmount() * 8L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.golden_carrot, 1, 0),
            new ItemData(MU.materialOf(Materials2Materials.Gold), OrePrefixes.nugget.getMaterialAmount() * 8L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.speckled_melon, 1, 0),
            new ItemData(MU.materialOf(Materials2Materials.Gold), OrePrefixes.nugget.getMaterialAmount() * 8L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.minecart, 1),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 18144000L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.iron_door, 1),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 21772800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.cauldron, 1),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 25401600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.iron_bars, 8, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 10886400L));
        ImmutableMap.of(
            NewHorizonsCoreMod.ID,
            ImmutableMap.<String, Materials>builder()
                .put("AluminiumBars", MU.materialOf(Materials2Materials.Aluminium))
                .put("ChromeBars", MU.materialOf(Materials2Materials.Chrome))
                .put("IridiumBars", MU.materialOf(Materials2Materials.Iridium))
                .put("NeutroniumBars", MU.materialOf(Materials2Materials.Neutronium))
                .put("OsmiumBars", MU.materialOf(Materials2Materials.Osmium))
                .put("StainlessSteelBars", MU.materialOf(Materials2Materials.StainlessSteel))
                .put("SteelBars", MU.materialOf(Materials2Materials.Steel))
                .put("TitaniumBars", MU.materialOf(Materials2Materials.Titanium))
                .put("TungstenSteelBars", MU.materialOf(Materials2Materials.TungstenSteel))
                .build(),
            EnderIO.ID,
            ImmutableMap.<String, Materials>builder()
                .put("blockDarkIronBars", MU.materialOf(Materials2Materials.DarkSteel))
                .put("blockEndSteelBars", MU.materialOf(Materials2Materials.EndSteel))
                .put("blockSoulariumBars", MU.materialOf(Materials2Materials.Soularium))
                .build())
            .forEach(
                (modId, items) -> items
                    .forEach((item, material) -> registerMetalBarUnificationData(modId, item, material)));
        GTOreDictUnificator.addItemData(
            GTModHandler.getIC2Item("ironFurnace", 1L),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 18144000L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Food_Can_Empty.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Tin), 1814400L));
        GTOreDictUnificator.addItemData(
            ItemList.IC2_Fuel_Rod_Empty.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.light_weighted_pressure_plate, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Gold), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.heavy_weighted_pressure_plate, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 7257600L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Railcraft.ID, "anvil", 1L, 0),
            new ItemData(MU.materialOf(Materials2Materials.Steel), 108864000L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Railcraft.ID, "anvil", 1L, 1),
            new ItemData(MU.materialOf(Materials2Materials.Steel), 72576000L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Railcraft.ID, "anvil", 1L, 2),
            new ItemData(MU.materialOf(Materials2Materials.Steel), 36288000L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.anvil, 1, 0),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 108864000L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.anvil, 1, 1),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 72576000L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.anvil, 1, 2),
            new ItemData(MU.materialOf(Materials2Materials.Iron), 36288000L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.hopper, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Iron),
                18144000L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 29030400L)));
        GTOreDictUnificator.addItemData(
            ItemList.Cell_Universal_Fluid.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Tin), 7257600L));
        GTOreDictUnificator
            .addItemData(ItemList.Cell_Empty.get(1L), new ItemData(MU.materialOf(Materials2Materials.Tin), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.tripwire_hook, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Iron),
                OrePrefixes.ring.getMaterialAmount() * 2L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 3628800L)));
        GTOreDictUnificator.addItemData(
            ItemList.Bottle_Empty.get(1L),
            new ItemData(MU.materialOf(Materials2Materials.Glass), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.potionitem, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Glass), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stained_glass, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Glass), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.glass, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Glass), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stained_glass_pane, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Glass), 1360800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.glass_pane, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Glass), 1360800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.clock, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Gold),
                14515200L,
                new MaterialStack(MU.materialOf(Materials2Materials.Redstone), 3628800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.compass, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Iron),
                14515200L,
                new MaterialStack(MU.materialOf(Materials2Materials.Redstone), 3628800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.iron_horse_armor, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Iron),
                29030400L,
                new MaterialStack(Materials.Leather, 21772800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.golden_horse_armor, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Gold),
                29030400L,
                new MaterialStack(Materials.Leather, 21772800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.diamond_horse_armor, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Diamond),
                29030400L,
                new MaterialStack(Materials.Leather, 21772800L)));
        GTOreDictUnificator
            .addItemData(new ItemStack(Items.leather, 1, 32767), new ItemData(Materials.Leather, 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.beacon, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.NetherStar),
                3628800L,
                new MaterialStack(MU.materialOf(Materials2Materials.Obsidian), 10886400L),
                new MaterialStack(MU.materialOf(Materials2Materials.Glass), 18144000L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.enchanting_table, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Diamond),
                7257600L,
                new MaterialStack(MU.materialOf(Materials2Materials.Obsidian), 14515200L),
                new MaterialStack(MU.materialOf(Materials2Materials.Paper), 10886400L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.ender_chest, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.EnderEye),
                3628800L,
                new MaterialStack(MU.materialOf(Materials2Materials.Obsidian), 29030400L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.bookshelf, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Paper),
                32659200L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 21772800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.lever, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Stone),
                3628800L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 1814400L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.ice, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Ice), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.packed_ice, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Ice), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.snow, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Snow), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.snowball, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Snow), 907200L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.snow_layer, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Snow), -1L));
        GTOreDictUnificator.addItemData(new ItemStack(Blocks.sand, 1, 32767), new ItemData(Materials.Sand, 3628800L));
        GTOreDictUnificator
            .addItemData(new ItemStack(Blocks.sandstone, 1, 32767), new ItemData(Materials.Sand, 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 0),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 8),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 0),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 8),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(new ItemStack(Blocks.stone_slab, 1, 1), new ItemData(Materials.Sand, 1814400L));
        GTOreDictUnificator.addItemData(new ItemStack(Blocks.stone_slab, 1, 9), new ItemData(Materials.Sand, 1814400L));
        GTOreDictUnificator
            .addItemData(new ItemStack(Blocks.double_stone_slab, 1, 1), new ItemData(Materials.Sand, 3628800L));
        GTOreDictUnificator
            .addItemData(new ItemStack(Blocks.double_stone_slab, 1, 9), new ItemData(Materials.Sand, 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 2),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 10),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 2),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 10),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 3),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 11),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 3),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 11),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 5),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_slab, 1, 13),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 1814400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 5),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.double_stone_slab, 1, 13),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.furnace, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 29030400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.lit_furnace, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 29030400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stonebrick, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.cobblestone, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.mossy_cobblestone, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_button, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.stone_pressure_plate, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Stone), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.ladder, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.wooden_button, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.wooden_pressure_plate, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.fence, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 5443200L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.bowl, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 3628800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.sign, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 7257600L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Items.wooden_door, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 21772800L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.chest, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 29030400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.trapped_chest, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 29030400L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.unlit_redstone_torch, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Wood),
                1814400L,
                new MaterialStack(MU.materialOf(Materials2Materials.Redstone), 3628800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.redstone_torch, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Wood),
                1814400L,
                new MaterialStack(MU.materialOf(Materials2Materials.Redstone), 3628800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.noteblock, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Wood),
                29030400L,
                new MaterialStack(MU.materialOf(Materials2Materials.Redstone), 3628800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.jukebox, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Wood),
                29030400L,
                new MaterialStack(MU.materialOf(Materials2Materials.Diamond), 3628800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.crafting_table, 1, 32767),
            new ItemData(MU.materialOf(Materials2Materials.Wood), 14515200L));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.piston, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Stone),
                14515200L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 10886400L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.sticky_piston, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Stone),
                14515200L,
                new MaterialStack(MU.materialOf(Materials2Materials.Wood), 10886400L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.dispenser, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Stone),
                25401600L,
                new MaterialStack(MU.materialOf(Materials2Materials.Redstone), 3628800L)));
        GTOreDictUnificator.addItemData(
            new ItemStack(Blocks.dropper, 1, 32767),
            new ItemData(
                MU.materialOf(Materials2Materials.Stone),
                25401600L,
                new MaterialStack(MU.materialOf(Materials2Materials.Redstone), 3628800L)));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Thaumcraft.ID, "ItemNuggetChicken", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.MeatCooked), 403200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Thaumcraft.ID, "ItemNuggetBeef", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.MeatCooked), 403200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Thaumcraft.ID, "ItemNuggetPork", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.MeatCooked), 403200L));
        GTOreDictUnificator.addItemData(
            GTModHandler.getModItem(Thaumcraft.ID, "ItemNuggetFish", 1L, 32767),
            new ItemData(MU.materialOf(Materials2Materials.MeatCooked), 403200L));

        for (ItemStack tItem : new ItemStack[] { GTModHandler.getModItem(TwilightForest.ID, "item.meefRaw", 1L, 0),
            GTModHandler.getModItem(TwilightForest.ID, "item.venisonRaw", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "muttonrawItem", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "turkeyrawItem", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "rabbitrawItem", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "venisonrawItem", 1L, 0), new ItemStack(Items.porkchop),
            new ItemStack(Items.beef), new ItemStack(Items.chicken), new ItemStack(Items.fish) }) {
            if (tItem != null) {
                GTOreDictUnificator.addItemData(
                    GTUtility.copyMetaData(32767, tItem),
                    new ItemData(
                        MU.materialOf(Materials2Materials.MeatRaw),
                        3628800L,
                        new MaterialStack(MU.materialOf(Materials2Materials.Bone), 403200L)));
            }
        }
        for (ItemStack tItem : new ItemStack[] { GTModHandler.getModItem(TwilightForest.ID, "item.meefSteak", 1L, 0),
            GTModHandler.getModItem(TwilightForest.ID, "item.venisonCooked", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "muttoncookedItem", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "turkeycookedItem", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "rabbitcookedItem", 1L, 0),
            GTModHandler.getModItem(PamsHarvestCraft.ID, "venisoncookedItem", 1L, 0),
            new ItemStack(Items.cooked_porkchop), new ItemStack(Items.cooked_beef), new ItemStack(Items.cooked_chicken),
            new ItemStack(Items.cooked_fished) }) {
            if (tItem != null) {
                GTOreDictUnificator.addItemData(
                    GTUtility.copyMetaData(32767, tItem),
                    new ItemData(
                        MU.materialOf(Materials2Materials.MeatCooked),
                        3628800L,
                        new MaterialStack(MU.materialOf(Materials2Materials.Bone), 403200L)));
            }
        }
    }

    private static void registerMetalBarUnificationData(String modId, String itemName, Materials material) {
        GTOreDictUnificator
            .addItemData(GTModHandler.getModItem(modId, itemName, 8L, 0), new ItemData(material, 10886400L));
    }
}
