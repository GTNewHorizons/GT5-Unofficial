package gtPlusPlus.xmod.gregtech.loaders.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Mods;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.xmod.forestry.ForestryTreeHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm;

public class RecipeLoader_TreeFarm {

    public static void generateRecipes() {
        generateVanillaTrees();

        if (Mods.IndustrialCraft2.isModLoaded()) generateIC2Trees();
        if (Mods.TinkerConstruct.isModLoaded()) generateTinkersTrees();
        generateGTPPTrees();

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

        if (Mods.Forestry.isModLoaded()) {
            ForestryTreeHandler.generateForestryTrees();
        }

        if (Mods.Forestry.isModLoaded() && Mods.ExtraTrees.isModLoaded()) ForestryTreeHandler.generateExtraTreesTrees();
    }

    private static void generateVanillaTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Oak
            new ItemStack(Blocks.sapling, 1, 0),
            new ItemStack(Blocks.log, 1, 0),
            new ItemStack(Blocks.leaves, 1, 0),
            new ItemStack(Items.apple, 1, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Spruce
            new ItemStack(Blocks.sapling, 1, 1),
            new ItemStack(Blocks.log, 2, 1),
            new ItemStack(Blocks.leaves, 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Birch
            new ItemStack(Blocks.sapling, 1, 2),
            new ItemStack(Blocks.log, 1, 2),
            new ItemStack(Blocks.leaves, 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Jungle
            new ItemStack(Blocks.sapling, 1, 3),
            new ItemStack(Blocks.log, 2, 3),
            new ItemStack(Blocks.leaves, 1, 3),
            new ItemStack(Items.dye, 1, 3));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Acacia
            new ItemStack(Blocks.sapling, 1, 4),
            new ItemStack(Blocks.log2, 1, 0),
            new ItemStack(Blocks.leaves2, 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Dark Oak
            new ItemStack(Blocks.sapling, 1, 5),
            new ItemStack(Blocks.log2, 1, 1),
            new ItemStack(Blocks.leaves2, 1, 1),
            new ItemStack(Items.apple, 1, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Brown Mushroom
            new ItemStack(Blocks.brown_mushroom, 1, 0),
            new ItemStack(Blocks.brown_mushroom_block, 1, 0),
            null,
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Red Mushroom
            new ItemStack(Blocks.red_mushroom, 1, 0),
            new ItemStack(Blocks.red_mushroom_block, 1, 0),
            null,
            null);
    }

    private static void generateIC2Trees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Rubber Tree
            GT_ModHandler.getModItem(Mods.IndustrialCraft2.ID, "blockRubSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.IndustrialCraft2.ID, "blockRubWood", 1, 0),
            GT_ModHandler.getModItem(Mods.IndustrialCraft2.ID, "blockRubLeaves", 1, 0),
            GT_ModHandler.getModItem(Mods.IndustrialCraft2.ID, "itemHarz", 1, 0));
    }

    private static void generateTinkersTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Slimy
            GT_ModHandler.getModItem(Mods.TinkerConstruct.ID, "slime.sapling", 1, 0),
            GT_ModHandler.getModItem(Mods.TinkerConstruct.ID, "slime.gel", 1, 1),
            GT_ModHandler.getModItem(Mods.TinkerConstruct.ID, "slime.leaves", 1, 0),
            GT_ModHandler.getModItem(Mods.TinkerConstruct.ID, "strangeFood", 1, 0));
    }

    private static void generateGTPPTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Rainforest Oak
            GT_ModHandler.getModItem(Mods.GTPlusPlus.ID, "blockRainforestOakSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.GTPlusPlus.ID, "blockRainforestOakLog", 3, 0),
            GT_ModHandler.getModItem(Mods.GTPlusPlus.ID, "blockRainforestOakLeaves", 1, 0),
            new ItemStack(Items.apple, 1, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Pine
            GT_ModHandler.getModItem(Mods.GTPlusPlus.ID, "blockPineSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.GTPlusPlus.ID, "blockPineLogLog", 1, 0),
            GT_ModHandler.getModItem(Mods.GTPlusPlus.ID, "blockPineLeaves", 1, 0),
            GT_ModHandler.getModItem(Mods.GTPlusPlus.ID, "item.BasicAgrichemItem", 1, 24));
    }

    private static void generateTwilightForestTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Sickly Twilight Oak
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Canopy Tree
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 1),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 1),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Twilight Mangrove
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 2),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 2),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Darkwood
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 3),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 3),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.DarkLeaves", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Robust Twilight Oak
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 4),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 4, 0),
            // Does not drop more robust saplings normally:
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Tree of Time
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 5),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLog", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLeaves", 1, 0),
            // No I am not making this drop clocks.
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Tree of Transformation
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 6),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLog", 1, 1),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLeaves", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Sorting Tree
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 8),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLog", 1, 3),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFMagicLeaves", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Rainbow Oak
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFSapling", 1, 9),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLog", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Thorns
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThorns", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThorns", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThorns", 1, 1),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves3", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFThornRose", 1, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Magic Beans
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "item.magicBeans", 1, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.HugeStalk", 5, 0),
            GT_ModHandler.getModItem(Mods.TwilightForest.ID, "tile.TFLeaves3", 1, 1),
            null);
    }

    private static void generateGalaxySpaceTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Barnarda C
            GT_ModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCsapling", 1, 1),
            GT_ModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaClog", 1, 0),
            GT_ModHandler.getModItem(Mods.GalaxySpace.ID, "barnardaCleaves", 1, 0),
            null);
    }

    private static void generateAmunRaTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Virilig
            GT_ModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.saplings", 1, 0),
            GT_ModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.log1", 1, 0),
            GT_ModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.null", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Lumipod
            GT_ModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.saplings", 1, 1),
            GT_ModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.wood1", 1, 0),
            null,
            GT_ModHandler.getModItem(Mods.GalacticraftAmunRa.ID, "tile.wood1", 1, 1));
    }

    private static void generateNaturaTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Redwood
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "redwood", 5, 1),
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 2, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "floraleaves", 2, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "redwood", 2, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Eucalyptus
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 1),
            GT_ModHandler.getModItem(Mods.Natura.ID, "tree", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "floraleaves", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Hopseed
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 2),
            GT_ModHandler.getModItem(Mods.Natura.ID, "tree", 1, 3),
            GT_ModHandler.getModItem(Mods.Natura.ID, "floraleaves", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Sakura
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 3),
            GT_ModHandler.getModItem(Mods.Natura.ID, "tree", 1, 1),
            GT_ModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Ghostwood
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 4),
            GT_ModHandler.getModItem(Mods.Natura.ID, "tree", 1, 2),
            GT_ModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Blood
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 5),
            GT_ModHandler.getModItem(Mods.Natura.ID, "bloodwood", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 2),
            new ItemStack(Items.redstone, 1, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Darkwood
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 6),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Dark Tree", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Dark Leaves", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Natura.netherfood", 1, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Fusewood
            GT_ModHandler.getModItem(Mods.Natura.ID, "florasapling", 1, 7),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Dark Tree", 1, 1),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Dark Leaves", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Maple
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Silverbell
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 1),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 1),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Amaranth
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 2),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 2),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Tigerwood
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 3),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Tree", 1, 3),
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Leaves", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Willow
            GT_ModHandler.getModItem(Mods.Natura.ID, "Rare Sapling", 1, 4),
            GT_ModHandler.getModItem(Mods.Natura.ID, "willow", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "floraleavesnocolor", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Green Glowshroom
            GT_ModHandler.getModItem(Mods.Natura.ID, "Glowshroom", 1, 0),
            GT_ModHandler.getModItem(Mods.Natura.ID, "greenGlowshroom", 1, 0),
            null,
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Purple Glowshroom
            GT_ModHandler.getModItem(Mods.Natura.ID, "Glowshroom", 1, 1),
            GT_ModHandler.getModItem(Mods.Natura.ID, "purpleGlowshroom", 1, 0),
            null,
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Blue Glowshroom
            GT_ModHandler.getModItem(Mods.Natura.ID, "Glowshroom", 1, 2),
            GT_ModHandler.getModItem(Mods.Natura.ID, "blueGlowshroom", 1, 0),
            null,
            null);
    }

    private static void generateBOPTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Apple
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 0),
            new ItemStack(Blocks.log, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "appleLeaves", 1, 0),
            new ItemStack(Items.apple, 2, 0));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Yellow Autumn
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 1),
            new ItemStack(Blocks.log, 1, 2),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Bamboo
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 2),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "bamboo", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 9),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Magic
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 3),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Dark
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 4),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 2),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves1", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Dying
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 5),
            new ItemStack(Blocks.log, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Fir
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 6),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 3),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "misc", 1, 13));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Ethereal
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 7),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Orange Autumn
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 8),
            new ItemStack(Blocks.log2, 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves2", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Origin
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 9),
            new ItemStack(Blocks.log, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Pink Cherry
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 10),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Maple
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 11),
            new ItemStack(Blocks.log, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // White Cherry
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 12),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves3", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Hellbark
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 13),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves4", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 1, 8));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Jacaranda
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 14),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 2),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "leaves4", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Persimmon
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "saplings", 1, 15),
            new ItemStack(Blocks.log, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "persimmonLeaves", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "food", 2, 8));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Sacred Oak
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs1", 4, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 2, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 2, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Mangrove
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 2),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Palm
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 2),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs2", 1, 3),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Redwood
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 3),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 2, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves1", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Willow
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 4),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 1, 1),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Pine
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 5),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Mahogany
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 6),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs4", 1, 3),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Flowering Oak
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedSaplings", 1, 7),
            new ItemStack(Blocks.log, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "colorizedLeaves2", 1, 3),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Red Flower Stem
            new ItemStack(Blocks.red_flower, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 1, 3),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "petals", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Yellow Flower Stem
            new ItemStack(Blocks.yellow_flower, 1, 0),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "logs3", 1, 3),
            GT_ModHandler.getModItem(Mods.BiomesOPlenty.ID, "petals", 1, 1),
            null);
    }

    private static void addPamTree(String name, int meta) {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts(
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pam" + name + "Sapling", 1, 0),
            new ItemStack(Blocks.log, 1, meta),
            new ItemStack(Blocks.leaves, 1, meta),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, name + "Item", 2, 0));
    }

    private static void generatePamsTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Cinnamon
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamappleSapling", 1, 0),
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

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Cinnamon
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamcinnamonSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamCinnamon", 1, 0),
            new ItemStack(Blocks.leaves, 1, 3),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "cinnamonItem", 2, 0));

        addPamTree("coconut", 3);
        addPamTree("date", 3);
        addPamTree("dragonfruit", 3);
        addPamTree("durian", 3);
        addPamTree("fig", 3);
        addPamTree("grapefruit", 3);
        addPamTree("lemon", 3);
        addPamTree("lime", 3);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Maple
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pammapleSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamMaple", 1, 0),
            new ItemStack(Blocks.leaves, 1, 1),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "maplesyrupItem", 2, 0));

        addPamTree("mango", 3);
        addPamTree("nutmeg", 0);
        addPamTree("olive", 0);
        addPamTree("orange", 3);
        addPamTree("papaya", 3);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Paperbark
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pampaperbarkSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pamPaperbark", 1, 0),
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
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Ignis Fruit
            GT_ModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "netherSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "netherLog", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "netherLeaves", 1, 0),
            GT_ModHandler.getModItem(Mods.PamsHarvestTheNether.ID, "ignisfruitItem", 2, 0));
    }

    private static void generateThaumcraftTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Greatwood
            GT_ModHandler.getModItem(Mods.Thaumcraft.ID, "blockCustomPlant", 1, 0),
            GT_ModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLog", 2, 0),
            GT_ModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLeaves", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Silverwood
            GT_ModHandler.getModItem(Mods.Thaumcraft.ID, "blockCustomPlant", 1, 1),
            GT_ModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLog", 1, 1),
            GT_ModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLeaves", 1, 1),
            null);
    }

    private static void generateThaumicBasesTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Golden Oak
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 0),
            new ItemStack(Blocks.log, 1, 0),
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 0),
            GT_ModHandler.getModItem(Mods.Thaumcraft.ID, "blockMagicalLeaves", 1, 0),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Peaceful
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 1),
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "genLogs", 1, 0),
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Nether
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 2),
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "genLogs", 1, 1),
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 2),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Ender
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "goldenOakSapling", 1, 3),
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "genLogs", 1, 2),
            GT_ModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 3),
            null);
    }

    private static void generateTaintedMagicTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Warpwood
            GT_ModHandler.getModItem(Mods.TaintedMagic.ID, "BlockWarpwoodSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.TaintedMagic.ID, "BlockWarpwoodLog", 1, 0),
            GT_ModHandler.getModItem(Mods.TaintedMagic.ID, "BlockWarpwoodLeaves", 1, 0),
            null);
    }

    private static void generateForbiddenMagicTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Warpwood
            GT_ModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintSapling", 1, 0),
            GT_ModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintLog", 1, 0),
            GT_ModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintLeaves", 1, 0),
            GT_ModHandler.getModItem(Mods.ForbiddenMagic.ID, "TaintFruit", 1, 0));
    }

    private static void generateWitcheryTrees() {
        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Rowan
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchsapling", 1, 0),
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchlog", 1, 0),
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchleaves", 1, 0),
            GT_ModHandler.getModItem(Mods.Witchery.ID, "ingredient", 1, 63));

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Alder
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchsapling", 1, 1),
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchlog", 1, 1),
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchleaves", 1, 1),
            null);

        GregtechMetaTileEntityTreeFarm.registerTreeProducts( // Hawthorn
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchsapling", 1, 2),
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchlog", 1, 2),
            GT_ModHandler.getModItem(Mods.Witchery.ID, "witchleaves", 1, 2),
            null);
    }

}
