package gtPlusPlus.xmod.gregtech.loaders.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gregtech.common.tileentities.machines.multi.MTETreeFarm;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.forestry.ForestryTreeHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTETreeFarmLegacy;

public class RecipeLoaderTreeFarm {

    public static void generateRecipes() {
        generateVanillaTrees();
        generateRubberTrees();
        generateGTPPTrees();

        if (Mods.TinkerConstruct.isModLoaded()) generateTinkersTrees();

        if (Mods.TwilightForest.isModLoaded()) generateTwilightForestTrees();
        if (Mods.GalaxySpace.isModLoaded()) generateGalaxySpaceTrees();
        if (Mods.GalacticraftAmunRa.isModLoaded()) generateAmunRaTrees();

        if (Mods.Thaumcraft.isModLoaded()) generateThaumcraftTrees();
        if (Mods.ThaumicBases.isModLoaded()) generateThaumicBasesTrees();
        if (Mods.TaintedMagic.isModLoaded()) generateTaintedMagicTrees();
        if (Mods.ForbiddenMagic.isModLoaded()) generateForbiddenMagicTrees();
        if (Mods.Witchery.isModLoaded()) generateWitcheryTrees();

        if (Mods.Natura.isModLoaded()) generateNaturaTrees();
        if (Mods.BiomesOPlenty.isModLoaded()) generateBOPTrees();
        if (Mods.PamsHarvestCraft.isModLoaded()) generatePamsTrees();
        if (Mods.PamsHarvestTheNether.isModLoaded()) generatePamsNetherTrees();
        if (Mods.Fether.isModLoaded()) generateFetherTrees();
        if (Mods.EtFuturumRequiem.isModLoaded()) generateEtFuturumRequiemTrees();

        if (Mods.Forestry.isModLoaded()) {
            ForestryTreeHandler.generateForestryTrees();
        }

        if (Mods.Forestry.isModLoaded() && Mods.ExtraTrees.isModLoaded()) ForestryTreeHandler.generateExtraTreesTrees();
    }

    private static void registerTreeProducts(ItemStack sapling, ItemStack log, ItemStack leaves, ItemStack fruit) {
        registerTreeProducts(sapling, log, sapling, leaves, fruit);
    }

    private static void registerTreeProducts(ItemStack saplingIn, ItemStack log, ItemStack saplingOut, ItemStack leaves,
        ItemStack fruit) {
        MTETreeFarmLegacy.registerTreeProducts(saplingIn, log, saplingOut, leaves, fruit);
        MTETreeFarm.registerTreeProducts(saplingIn, log, saplingOut, leaves, fruit);
    }

    private static void generateVanillaTrees() {
        registerTreeProducts( // Oak
            new ItemStack(Blocks.sapling, 1, 0),
            new ItemStack(Blocks.log, 1, 0),
            new ItemStack(Blocks.leaves, 1, 0),
            new ItemStack(Items.apple, 1, 0));

        registerTreeProducts( // Spruce
            new ItemStack(Blocks.sapling, 1, 1),
            new ItemStack(Blocks.log, 2, 1),
            new ItemStack(Blocks.leaves, 1, 1),
            null);

        registerTreeProducts( // Birch
            new ItemStack(Blocks.sapling, 1, 2),
            new ItemStack(Blocks.log, 1, 2),
            new ItemStack(Blocks.leaves, 1, 2),
            null);

        registerTreeProducts( // Jungle
            new ItemStack(Blocks.sapling, 1, 3),
            new ItemStack(Blocks.log, 2, 3),
            new ItemStack(Blocks.leaves, 1, 3),
            new ItemStack(Items.dye, 1, 3));

        registerTreeProducts( // Acacia
            new ItemStack(Blocks.sapling, 1, 4),
            new ItemStack(Blocks.log2, 1, 0),
            new ItemStack(Blocks.leaves2, 1, 0),
            null);

        registerTreeProducts( // Dark Oak
            new ItemStack(Blocks.sapling, 1, 5),
            new ItemStack(Blocks.log2, 1, 1),
            new ItemStack(Blocks.leaves2, 1, 1),
            new ItemStack(Items.apple, 1, 0));

        registerTreeProducts( // Brown Mushroom
            new ItemStack(Blocks.brown_mushroom, 1, 0),
            new ItemStack(Blocks.brown_mushroom_block, 1, 0),
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "brown_mushroom", 1, 0),
            null);

        registerTreeProducts( // Red Mushroom
            new ItemStack(Blocks.red_mushroom, 1, 0),
            new ItemStack(Blocks.red_mushroom_block, 1, 0),
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "red_mushroom", 1, 0),
            null);
    }

    private static void generateRubberTrees() {
        MTETreeFarm.registerTreeProducts(
            new ItemStack(GregTechAPI.sBlockRubberSapling, 1, 0),
            new ItemStack(GregTechAPI.sBlockRubberLog, 1, 0),
            new ItemStack(GregTechAPI.sBlockRubberLeaves, 1, 0),
            ItemList.Sticky_Resin.get(1));
    }

    private static void generateTinkersTrees() {
        registerTreeProducts( // Slimy
            GTModHandler.getModItem(Mods.TinkerConstruct.ID, "slime.sapling", 1, 0),
            GTModHandler.getModItem(Mods.TinkerConstruct.ID, "slime.gel", 1, 1),
            GTModHandler.getModItem(Mods.TinkerConstruct.ID, "slime.leaves", 1, 0),
            GTModHandler.getModItem(Mods.TinkerConstruct.ID, "strangeFood", 1, 0));
    }

    private static void generateGTPPTrees() {
        registerTreeProducts( // Rainforest Oak
            new ItemStack(BOPBlockRegistrator.sapling_Rainforest, 1, 0),
            new ItemStack(BOPBlockRegistrator.log_Rainforest, 3, 0),
            new ItemStack(BOPBlockRegistrator.leaves_Rainforest, 1, 0),
            new ItemStack(Items.apple, 1, 0));

        registerTreeProducts( // Pine
            new ItemStack(BOPBlockRegistrator.sapling_Pine, 1, 0),
            new ItemStack(BOPBlockRegistrator.log_Pine, 1, 0),
            new ItemStack(BOPBlockRegistrator.leaves_Pine, 1, 0),
            GregtechItemList.Pinecone.get(1));
    }

    private static void generateTwilightForestTrees() {
        registerTreeProducts( // Sickly Twilight Oak
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 0),
            null);

        registerTreeProducts( // Canopy Tree
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 1),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 1),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 1),
            null);

        registerTreeProducts( // Twilight Mangrove
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 2),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 2),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 2),
            null);

        registerTreeProducts( // Darkwood
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 3),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 3),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.DarkLeaves", 1, 0),
            null);

        registerTreeProducts( // Robust Twilight Oak
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 4),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 4, 0),
            // Does not drop more robust saplings normally:
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 0),
            null);

        registerTreeProducts( // Tree of Time
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLog", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLeaves", 1, 0),
            // No I am not making this drop clocks.
            null);

        registerTreeProducts( // Tree of Transformation
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 6),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLog", 1, 1),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLeaves", 1, 1),
            null);

        registerTreeProducts( // Sorting Tree
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 8),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLog", 1, 3),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLeaves", 1, 3),
            null);

        registerTreeProducts( // Rainbow Oak
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 9),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 3),
            null);

        registerTreeProducts( // Thorns
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThorns", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThorns", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThorns", 1, 1),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves3", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThornRose", 1, 0));

        registerTreeProducts( // Magic Beans
            GTModHandler.getModItem(Mods.TwilightForest.ID, "item.magicBeans", 1, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.HugeStalk", 5, 0),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves3", 1, 1),
            null);

        MTETreeFarm.registerTreeProducts( // Minewood
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 7),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLog", 1, 2),
            GTModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLeaves", 1, 2),
            null);
    }

    private static void generateGalaxySpaceTrees() {
        registerTreeProducts( // Barnarda C
            GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 1, 0),
            GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaClog", 1, 0),
            GTModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCleaves", 1, 0),
            null);
    }

    private static void generateAmunRaTrees() {
        registerTreeProducts( // Virilig
            GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.saplings", 1, 0),
            GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.log1", 1, 0),
            GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.null", 1, 0),
            null);

        registerTreeProducts( // Lumipod
            GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.saplings", 1, 1),
            GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.wood1", 1, 0),
            null,
            GTModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.wood1", 1, 1));
    }

    private static void generateNaturaTrees() {
        registerTreeProducts( // Redwood
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "redwood", 5, 1),
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 2, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "floraleaves", 2, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "redwood", 2, 0));

        registerTreeProducts( // Eucalyptus
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 1),
            GTModHandler.getModItem(Mods.Natura.ID, "tree", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "floraleaves", 1, 1),
            null);

        registerTreeProducts( // Hopseed
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 2),
            GTModHandler.getModItem(Mods.Natura.ID, "tree", 1, 3),
            GTModHandler.getModItem(Mods.Natura.ID, "floraleaves", 1, 2),
            null);

        registerTreeProducts( // Sakura
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 3),
            GTModHandler.getModItem(Mods.Natura.ID, "tree", 1, 1),
            GTModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 0),
            null);

        registerTreeProducts( // Ghostwood
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 4),
            GTModHandler.getModItem(Mods.Natura.ID, "tree", 1, 2),
            GTModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 1),
            null);

        registerTreeProducts( // Blood
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 5),
            GTModHandler.getModItem(Mods.Natura.ID, "bloodwood", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 2),
            new ItemStack(Items.redstone, 1, 0));

        registerTreeProducts( // Darkwood
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 6),
            GTModHandler.getModItem(Mods.Natura.ID, "Dark Tree", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "Dark Leaves", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "Natura.netherfood", 1, 0));

        registerTreeProducts( // Fusewood
            GTModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 7),
            GTModHandler.getModItem(Mods.Natura.ID, "Dark Tree", 1, 1),
            GTModHandler.getModItem(Mods.Natura.ID, "Dark Leaves", 1, 3),
            null);

        registerTreeProducts( // Maple
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 0),
            null);

        registerTreeProducts( // Silverbell
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 1),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 1),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 1),
            null);

        registerTreeProducts( // Amaranth
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 2),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 2),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 2),
            null);

        registerTreeProducts( // Tigerwood
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 3),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 3),
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 3),
            null);

        registerTreeProducts( // Willow
            GTModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 4),
            GTModHandler.getModItem(Mods.Natura.ID, "willow", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 3),
            null);

        registerTreeProducts( // Green Glowshroom
            GTModHandler.getModItem(Mods.Natura.ID, "Glowshroom", 1, 0),
            GTModHandler.getModItem(Mods.Natura.ID, "greenGlowshroom", 1, 0),
            null,
            null);

        registerTreeProducts( // Purple Glowshroom
            GTModHandler.getModItem(Mods.Natura.ID, "Glowshroom", 1, 1),
            GTModHandler.getModItem(Mods.Natura.ID, "purpleGlowshroom", 1, 0),
            null,
            null);

        registerTreeProducts( // Blue Glowshroom
            GTModHandler.getModItem(Mods.Natura.ID, "Glowshroom", 1, 2),
            GTModHandler.getModItem(Mods.Natura.ID, "blueGlowshroom", 1, 0),
            null,
            null);
    }

    private static void generateBOPTrees() {
        registerTreeProducts( // Apple
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 0),
            new ItemStack(Blocks.log, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "appleLeaves", 1, 0),
            new ItemStack(Items.apple, 2, 0));

        registerTreeProducts( // Yellow Autumn
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 1),
            new ItemStack(Blocks.log, 1, 2),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        registerTreeProducts( // Bamboo
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 2),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "bamboo", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 9),
            null);

        registerTreeProducts( // Magic
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 3),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 2),
            null);

        registerTreeProducts( // Dark
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 4),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 2),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 3),
            null);

        registerTreeProducts( // Dying
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 5),
            new ItemStack(Blocks.log, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        registerTreeProducts( // Fir
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 6),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 3),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "misc", 1, 13));

        registerTreeProducts( // Ethereal
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 7),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 2),
            null);

        registerTreeProducts( // Orange Autumn
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 8),
            new ItemStack(Blocks.log2, 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 3),
            null);

        registerTreeProducts( // Origin
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 9),
            new ItemStack(Blocks.log, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        registerTreeProducts( // Pink Cherry
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 10),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 1),
            null);

        registerTreeProducts( // Maple
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 11),
            new ItemStack(Blocks.log, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 2),
            null);

        registerTreeProducts( // White Cherry
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 12),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 3),
            null);

        registerTreeProducts( // Hellbark
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 13),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves4", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        registerTreeProducts( // Jacaranda
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 14),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 2),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves4", 1, 1),
            null);

        registerTreeProducts( // Persimmon
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 15),
            new ItemStack(Blocks.log, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "persimmonLeaves", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 2, 8));

        registerTreeProducts( // Sacred Oak
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 4, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 2, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 2, 0),
            null);

        registerTreeProducts( // Mangrove
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 2),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 1, 1),
            null);

        registerTreeProducts( // Palm
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 2),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 3),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 1, 2),
            null);

        registerTreeProducts( // Redwood
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 3),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 2, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 1, 3),
            null);

        registerTreeProducts( // Willow
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 4),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 1, 1),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 0),
            null);

        registerTreeProducts( // Pine
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 5),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 1),
            null);

        registerTreeProducts( // Mahogany
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 6),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 3),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 2),
            null);

        registerTreeProducts( // Flowering Oak
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 7),
            new ItemStack(Blocks.log, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 3),
            null);

        registerTreeProducts( // Red Flower Stem
            new ItemStack(Blocks.red_flower, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 1, 3),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "petals", 1, 0),
            null);

        registerTreeProducts( // Yellow Flower Stem
            new ItemStack(Blocks.yellow_flower, 1, 0),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 1, 3),
            GTModHandler.getModItem(Mods.BiomesOPlenty.ID, "petals", 1, 1),
            null);
    }

    private static void addPamTree(String name, int meta) {
        registerTreeProducts(
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pam" + name + "Sapling", 1, 0),
            new ItemStack(Blocks.log, 1, meta),
            new ItemStack(Blocks.leaves, 1, meta),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, name + "Item", 2, 0));
    }

    private static void generatePamsTrees() {
        registerTreeProducts( // Cinnamon
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamappleSapling", 1, 0),
            new ItemStack(Blocks.log, 1, 0),
            new ItemStack(Blocks.leaves, 1, 0),
            new ItemStack(Items.apple, 2, 0));

        addPamTree("almond", 3);
        addPamTree("apricot", 3);
        addPamTree("avocado", 0);
        addPamTree("banana", 3);
        addPamTree("cashew", 3);
        addPamTree("cherry", 0);
        addPamTree("chestnut", 0);

        registerTreeProducts( // Cinnamon
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamcinnamonSapling", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamCinnamon", 1, 0),
            new ItemStack(Blocks.leaves, 1, 3),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "cinnamonItem", 2, 0));

        addPamTree("coconut", 3);
        addPamTree("date", 3);
        addPamTree("dragonfruit", 3);
        addPamTree("durian", 3);
        addPamTree("fig", 3);
        addPamTree("grapefruit", 3);
        addPamTree("lemon", 3);
        addPamTree("lime", 3);

        registerTreeProducts( // Maple
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pammapleSapling", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamMaple", 1, 0),
            new ItemStack(Blocks.leaves, 1, 1),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "maplesyrupItem", 2, 0));

        addPamTree("mango", 3);
        addPamTree("nutmeg", 0);
        addPamTree("olive", 0);
        addPamTree("orange", 3);
        addPamTree("papaya", 3);

        registerTreeProducts( // Paperbark
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pampaperbarkSapling", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamPaperbark", 1, 0),
            new ItemStack(Blocks.leaves, 1, 3),
            new ItemStack(Items.paper, 1, 0));

        addPamTree("peach", 3);
        addPamTree("pear", 0);
        addPamTree("pecan", 3);
        addPamTree("peppercorn", 3);
        addPamTree("persimmon", 3);
        addPamTree("pistachio", 3);
        addPamTree("plum", 0);
        addPamTree("pomegranate", 3);
        addPamTree("starfruit", 3);
        addPamTree("vanillabean", 3);
        addPamTree("walnut", 0);
        addPamTree("gooseberry", 0);
    }

    private static void generatePamsNetherTrees() {
        registerTreeProducts( // Ignis Fruit
            GTModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "netherSapling", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "netherLog", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "netherLeaves", 1, 0),
            GTModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "ignisfruitItem", 2, 0));
    }

    private static void generateFetherTrees() {
        registerTreeProducts( // Ignis Fruit
            GTModHandler.getModItem(Mods.Fether.ID, "nether_sapling", 1, 0),
            GTModHandler.getModItem(Mods.Fether.ID, "nether_log", 1, 0),
            GTModHandler.getModItem(Mods.Fether.ID, "nether_leaves", 1, 0),
            GTModHandler.getModItem(Mods.Fether.ID, "ignis_fruit", 2, 0));
    }

    private static void generateThaumcraftTrees() {
        registerTreeProducts( // Greatwood
            GTModHandler.getModItem(Mods.Thaumcraft.ID, "blockCustomPlant", 1, 0),
            GTModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLog", 2, 0),
            GTModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLeaves", 1, 0),
            null);

        registerTreeProducts( // Silverwood
            GTModHandler.getModItem(Mods.Thaumcraft.ID, "blockCustomPlant", 1, 1),
            GTModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLog", 1, 1),
            GTModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLeaves", 1, 1),
            null);
    }

    private static void generateThaumicBasesTrees() {
        registerTreeProducts( // Golden Oak
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 0),
            new ItemStack(Blocks.log, 1, 0),
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 0),
            GTModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLeaves", 1, 0),
            null);

        registerTreeProducts( // Peaceful
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 1),
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLogs", 1, 0),
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 1),
            null);

        registerTreeProducts( // Nether
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 2),
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLogs", 1, 1),
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 2),
            null);

        registerTreeProducts( // Ender
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 3),
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLogs", 1, 2),
            GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 3),
            null);
    }

    private static void generateTaintedMagicTrees() {
        registerTreeProducts( // Warpwood
            GTModHandler.getModItem(Mods.TaintedMagic.ID, "BlockWarpwoodSapling", 1, 0),
            GTModHandler.getModItem(Mods.TaintedMagic.ID, "BlockWarpwoodLog", 1, 0),
            GTModHandler.getModItem(Mods.TaintedMagic.ID, "BlockWarpwoodLeaves", 1, 0),
            null);
    }

    private static void generateForbiddenMagicTrees() {
        registerTreeProducts( // Warpwood
            GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintSapling", 1, 0),
            GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintLog", 1, 0),
            GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintLeaves", 1, 0),
            GTModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintFruit", 1, 0));
    }

    private static void generateWitcheryTrees() {
        registerTreeProducts( // Rowan
            GTModHandler.getModItem(Mods.Witchery.ID, "witchsapling", 1, 0),
            GTModHandler.getModItem(Mods.Witchery.ID, "witchlog", 1, 0),
            GTModHandler.getModItem(Mods.Witchery.ID, "witchleaves", 1, 0),
            GTModHandler.getModItem(Mods.Witchery.ID, "ingredient", 1, 63));

        registerTreeProducts( // Alder
            GTModHandler.getModItem(Mods.Witchery.ID, "witchsapling", 1, 1),
            GTModHandler.getModItem(Mods.Witchery.ID, "witchlog", 1, 1),
            GTModHandler.getModItem(Mods.Witchery.ID, "witchleaves", 1, 1),
            null);

        registerTreeProducts( // Hawthorn
            GTModHandler.getModItem(Mods.Witchery.ID, "witchsapling", 1, 2),
            GTModHandler.getModItem(Mods.Witchery.ID, "witchlog", 1, 2),
            GTModHandler.getModItem(Mods.Witchery.ID, "witchleaves", 1, 2),
            null);
    }

    private static void generateEtFuturumRequiemTrees() {
        registerTreeProducts( // Cherry
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "sapling", 1, 1),
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "cherry_log", 1, 0),
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "leaves", 1, 1),
            GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "cherryItem", 1, 0));

        registerTreeProducts( // Chorus
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "chorus_flower", 1, 0),
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "chorus_plant", 1, 0),
            null,
            GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "chorus_fruit", 1, 0));
    }
}
