package gregtech.loaders.preload;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TwilightForest;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TieredItems;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.OreDictUtils;

public class LoaderGTOreDictionary implements Runnable {

    @Override
    public void run() {
        GT_FML_LOGGER.debug("GTMod: Register OreDict Entries of Non-GT-Items.");
        OreDictUtils.registerAndAssociate(OrePrefixes.cell, Materials.Empty, ItemList.Cell_Empty.get(1L));

        OreDictUtils.registerAndAssociate(OrePrefixes.bucket, Materials.Empty, new ItemStack(Items.bucket, 1, 0));
        OreDictUtils.registerAndAssociate(OrePrefixes.bucket, Materials.Water, new ItemStack(Items.water_bucket, 1, 0));
        OreDictUtils.registerAndAssociate(OrePrefixes.bucket, Materials.Lava, new ItemStack(Items.lava_bucket, 1, 0));
        OreDictUtils.registerAndAssociate(OrePrefixes.bucket, Materials.Milk, new ItemStack(Items.milk_bucket, 1, 0));
        // Clay buckets handled in gregtech.common.GTProxy.onInitialization()
        // as they aren't registered until Iguana Tweaks pre-init.

        OreDictUtils.registerAndAssociate(OrePrefixes.bottle, Materials.Empty, new ItemStack(Items.glass_bottle, 1, 0));
        OreDictUtils.registerAndAssociate(OrePrefixes.bottle, Materials.Water, new ItemStack(Items.potionitem, 1, 0));

        OreDictUtils.registerAndAssociate(
            OrePrefixes.plateAlloy,
            Materials.Iridium,
            GTModHandler.getIC2Item("iridiumPlate", 1L));
        OreDictUtils
            .registerAndAssociate(OrePrefixes.plateAlloy, "Advanced", GTModHandler.getIC2Item("advancedAlloy", 1L));
        OreDictUtils
            .registerAndAssociate(OrePrefixes.plateAlloy, Materials.Carbon, GTModHandler.getIC2Item("carbonPlate", 1L));

        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Coal, new ItemStack(Blocks.coal_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Iron, new ItemStack(Blocks.iron_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Lapis, new ItemStack(Blocks.lapis_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Redstone, new ItemStack(Blocks.redstone_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Redstone, new ItemStack(Blocks.lit_redstone_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Gold, new ItemStack(Blocks.gold_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Diamond, new ItemStack(Blocks.diamond_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.Emerald, new ItemStack(Blocks.emerald_ore, 1));
        OreDictUtils.registerOre(OrePrefixes.ore, Materials.NetherQuartz, new ItemStack(Blocks.quartz_ore, 1));

        if (Blocks.ender_chest != null) {
            OreDictUtils.registerOre(OreDictNames.enderChest, new ItemStack(Blocks.ender_chest, 1));
        }
        OreDictUtils.registerOre(OreDictNames.craftingAnvil, new ItemStack(Blocks.anvil, 1));
        OreDictUtils.registerOre(OreDictNames.craftingAnvil, GTModHandler.getModItem(Railcraft.ID, "anvil", 1L, 0));

        ItemStack industrialDiamond = ItemList.IC2_Industrial_Diamond.get(1);
        OreDictUtils.addToBlacklist(industrialDiamond);
        OreDictUtils.registerOre(OreDictNames.craftingIndustrialDiamond, industrialDiamond);
        OreDictUtils.registerOre(
            OreDictNames.craftingIndustrialDiamond,
            MaterialLibAPI.getStack(Materials.CubicZirconia, Shapes.gemExquisite, 1));

        OreDictUtils
            .registerOre(OrePrefixes.stone, Materials.Basalt, GTModHandler.getModItem(Railcraft.ID, "cube", 1L, 6));
        OreDictUtils
            .registerOre(OrePrefixes.stone, Materials.Marble, GTModHandler.getModItem(Railcraft.ID, "cube", 1L, 7));
        OreDictUtils.registerOre(
            OrePrefixes.stone,
            Materials.Basalt,
            GTModHandler.getModItem(Railcraft.ID, "brick.abyssal", 1L, 32767));
        OreDictUtils.registerOre(
            OrePrefixes.stone,
            Materials.Marble,
            GTModHandler.getModItem(Railcraft.ID, "brick.quarried", 1L, 32767));
        OreDictUtils.registerOre(OrePrefixes.stone, Materials.Obsidian, new ItemStack(Blocks.obsidian, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stone, Materials.Stone, new ItemStack(Blocks.stone, 1, 0));
        OreDictUtils.registerOre(OrePrefixes.stoneMossy, new ItemStack(Blocks.mossy_cobblestone, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stoneCobble, new ItemStack(Blocks.mossy_cobblestone, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stoneCobble, new ItemStack(Blocks.cobblestone, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stoneSmooth, new ItemStack(Blocks.stone, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stoneBricks, new ItemStack(Blocks.stonebrick, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stoneMossy, new ItemStack(Blocks.stonebrick, 1, 1));
        OreDictUtils.registerOre(OrePrefixes.stoneCracked, new ItemStack(Blocks.stonebrick, 1, 2));
        OreDictUtils.registerOre(OrePrefixes.stoneChiseled, new ItemStack(Blocks.stonebrick, 1, 3));
        OreDictUtils.registerOre("stoneSand", new ItemStack(Blocks.sandstone, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stone, Materials.Netherrack, new ItemStack(Blocks.netherrack, 1, 32767));
        OreDictUtils
            .registerOre(OrePrefixes.stone, Materials.NetherBrick, new ItemStack(Blocks.nether_brick, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stone, Materials.Endstone, new ItemStack(Blocks.end_stone, 1, 32767));
        OreDictUtils.registerOre(OrePrefixes.stone, Materials.Glowstone, new ItemStack(Blocks.glowstone, 1, 32767));

        OreDictUtils
            .registerOre("paperResearchFragment", GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 9));
        OreDictUtils.registerOre(
            "itemCertusQuartz",
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1));
        OreDictUtils.registerOre(
            "itemCertusQuartz",
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10));
        OreDictUtils.registerOre(
            "itemNetherQuartz",
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11));
        OreDictUtils.registerOre(
            OreDictNames.craftingQuartz,
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1));
        OreDictUtils.registerOre(
            OreDictNames.craftingQuartz,
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10));
        OreDictUtils.registerOre(
            OreDictNames.craftingQuartz,
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11));
        OreDictUtils.registerOre("cropLemon", ItemList.FR_Lemon.get(1L));
        OreDictUtils.registerOre("cropPotato", ItemList.Food_Raw_Potato.get(1L));
        for (TieredItems tier : TieredItems.values()) {
            if (tier.hasBattery()) OreDictUtils.registerOre(tier.getBatteryIngredient(), tier.getBattery(1));
        }
        OreDictUtils
            .registerOre(TieredItems.LV.getBatteryIngredient(), GTModHandler.getIC2Item("chargedReBattery", 1L, 32767));

        OreDictUtils
            .registerOre(OreDictNames.craftingWireCopper, GTModHandler.getIC2Item("insulatedCopperCableItem", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingWireGold, GTModHandler.getIC2Item("insulatedGoldCableItem", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingWireIron, GTModHandler.getIC2Item("insulatedIronCableItem", 1L));
        OreDictUtils.registerOre(
            OreDictNames.craftingWireTin,
            GTModHandler
                .getIC2Item("insulatedTinCableItem", 1L, GTModHandler.getIC2Item("insulatedCopperCableItem", 1L)));

        OreDictUtils.registerOre(OreDictNames.craftingRedstoneTorch, new ItemStack(Blocks.redstone_torch, 1, 32767));
        OreDictUtils
            .registerOre(OreDictNames.craftingRedstoneTorch, new ItemStack(Blocks.unlit_redstone_torch, 1, 32767));

        OreDictUtils.registerOre(OreDictNames.craftingWorkBench, new ItemStack(Blocks.crafting_table, 1));
        OreDictUtils.registerOre(OreDictNames.craftingWorkBench, new ItemStack(GregTechAPI.sBlockMachines, 1, 16));

        OreDictUtils.registerOre(OreDictNames.craftingPiston, new ItemStack(Blocks.piston, 1, 32767));
        OreDictUtils.registerOre(OreDictNames.craftingPiston, new ItemStack(Blocks.sticky_piston, 1, 32767));

        OreDictUtils.registerOre(OreDictNames.craftingSafe, new ItemStack(GregTechAPI.sBlockMachines, 1, 45));
        OreDictUtils.registerOre(OreDictNames.craftingSafe, GTModHandler.getIC2Item("personalSafe", 1L));

        OreDictUtils.registerOre(OreDictNames.craftingChest, new ItemStack(Blocks.chest, 1, 32767));
        OreDictUtils.registerOre(OreDictNames.craftingChest, new ItemStack(Blocks.trapped_chest, 1, 32767));

        OreDictUtils.registerOre(OreDictNames.craftingFurnace, new ItemStack(Blocks.furnace, 1, 32767));

        OreDictUtils.registerOre(OreDictNames.craftingPump, GTModHandler.getIC2Item("pump", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingElectromagnet, GTModHandler.getIC2Item("magnetizer", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingTeleporter, GTModHandler.getIC2Item("teleporter", 1L));

        OreDictUtils.registerOre(OreDictNames.craftingMacerator, GTModHandler.getIC2Item("macerator", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingMacerator, new ItemStack(GregTechAPI.sBlockMachines, 1, 50));

        OreDictUtils.registerOre(OreDictNames.craftingExtractor, GTModHandler.getIC2Item("extractor", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingExtractor, new ItemStack(GregTechAPI.sBlockMachines, 1, 51));

        OreDictUtils.registerOre(OreDictNames.craftingCompressor, GTModHandler.getIC2Item("compressor", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingCompressor, new ItemStack(GregTechAPI.sBlockMachines, 1, 52));

        OreDictUtils.registerOre(OreDictNames.craftingRecycler, GTModHandler.getIC2Item("recycler", 1L));
        OreDictUtils.registerOre(OreDictNames.craftingRecycler, new ItemStack(GregTechAPI.sBlockMachines, 1, 53));

        OreDictUtils.registerOre(
            OreDictNames.craftingBlastFurnace,
            GTModHandler.getModItem(EtFuturumRequiem.ID, "blast_furnace", 1));

        OreDictUtils.registerOre(OreDictNames.craftingCentrifuge, new ItemStack(GregTechAPI.sBlockMachines, 1, 62));

        OreDictUtils
            .registerOre(OreDictNames.craftingInductionFurnace, GTModHandler.getIC2Item("inductionFurnace", 1L));

        OreDictUtils.registerOre(OreDictNames.craftingElectricFurnace, GTModHandler.getIC2Item("electroFurnace", 1L));
        OreDictUtils
            .registerOre(OreDictNames.craftingElectricFurnace, new ItemStack(GregTechAPI.sBlockMachines, 1, 54));

        OreDictUtils.registerOre(OreDictNames.craftingGenerator, GTModHandler.getIC2Item("generator", 1L));

        OreDictUtils
            .registerOre(OreDictNames.craftingGeothermalGenerator, GTModHandler.getIC2Item("geothermalGenerator", 1L));

        OreDictUtils.registerOre(OreDictNames.craftingFeather, new ItemStack(Items.feather, 1, 32767));
        OreDictUtils.registerOre(
            OreDictNames.craftingFeather,
            GTModHandler.getModItem(TwilightForest.ID, "item.tfFeather", 1L, 32767));

        OreDictUtils.registerOre("itemWheat", new ItemStack(Items.wheat, 1, 32767));
        OreDictUtils.registerOre("paperEmpty", new ItemStack(Items.paper, 1, 32767));
        OreDictUtils.registerOre("paperMap", new ItemStack(Items.map, 1, 32767));
        OreDictUtils.registerOre("paperMap", new ItemStack(Items.filled_map, 1, 32767));
        OreDictUtils.registerOre("bookEmpty", new ItemStack(Items.book, 1, 32767));
        OreDictUtils.registerOre("bookWritable", new ItemStack(Items.writable_book, 1, 32767));
        OreDictUtils.registerOre("bookWritten", new ItemStack(Items.written_book, 1, 32767));
        OreDictUtils.registerOre("bookEnchanted", new ItemStack(Items.enchanted_book, 1, 32767));
        OreDictUtils.registerOre(OreDictNames.craftingBook, new ItemStack(Items.book, 1, 32767));
        OreDictUtils.registerOre(OreDictNames.craftingBook, new ItemStack(Items.writable_book, 1, 32767));
        OreDictUtils.registerOre(OreDictNames.craftingBook, new ItemStack(Items.written_book, 1, 32767));
        OreDictUtils.registerOre(OreDictNames.craftingBook, new ItemStack(Items.enchanted_book, 1, 32767));

        OreDictUtils.addToBlacklist(GTModHandler.getIC2Item("electronicCircuit", 1L));
        OreDictUtils.addToBlacklist(GTModHandler.getIC2Item("advancedCircuit", 1L));
        OreDictUtils.registerOre(
            OrePrefixes.circuit.oreDictName(Circuits.LV.materialName()),
            GTModHandler.getIC2Item("electronicCircuit", 1L));
        OreDictUtils.registerOre(
            OrePrefixes.circuit.oreDictName(Circuits.HV.materialName()),
            GTModHandler.getIC2Item("advancedCircuit", 1L));

        for (Circuits circuit : Circuits.values()) {
            OreDictUtils.registerOre(OrePrefixes.circuit.oreDictName(circuit.materialName()), circuit.get(1));
        }

        OreDictUtils
            .registerOre(OrePrefixes.block, Materials.Manasteel, GTModHandler.getModItem(Botania.ID, "storage", 1L));
        OreDictUtils.registerOre(
            OrePrefixes.block,
            Materials.Terrasteel,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 1));
        OreDictUtils.registerOre(
            OrePrefixes.ingot,
            Materials.ElvenElementium,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 7));
        OreDictUtils.registerOre(
            OrePrefixes.nugget,
            Materials.ElvenElementium,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 19));
        OreDictUtils.registerOre(
            OrePrefixes.block,
            Materials.ElvenElementium,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 2));
        OreDictUtils.registerOre(
            OrePrefixes.block,
            Materials.Livingrock,
            GTModHandler.getModItem(Botania.ID, "livingrock", 1L));
        OreDictUtils.registerOre(
            OrePrefixes.ingot,
            Materials.GaiaSpirit,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 14));
        OreDictUtils.registerOre(
            OrePrefixes.block,
            Materials.Livingwood,
            GTModHandler.getModItem(Botania.ID, "livingwood", 1L));
        OreDictUtils
            .registerOre(OrePrefixes.block, Materials.Dreamwood, GTModHandler.getModItem(Botania.ID, "dreamwood", 1L));
        OreDictUtils.registerOre(
            OrePrefixes.gem,
            Materials.ManaDiamond,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 2));
        OreDictUtils.registerOre(
            OrePrefixes.block,
            Materials.ManaDiamond,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 3));
        OreDictUtils.registerOre(
            OrePrefixes.gem,
            Materials.Dragonstone,
            GTModHandler.getModItem(Botania.ID, "manaResource", 1L, 9));
        OreDictUtils.registerOre(
            OrePrefixes.block,
            Materials.Dragonstone,
            GTModHandler.getModItem(Botania.ID, "storage", 1L, 4));
    }
}
