package gregtech.common.blocks.rubbertree;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.common.items.ItemRubberTreeTap;
import gregtech.common.items.ItemStickyResin;

public final class RubberTreeRegistrator {

    private static final int TREE_TAP_WOODEN_DURABILITY = 64;
    private static final int TREE_TAP_BRONZE_DURABILITY = 256;
    private static final int TREE_TAP_STEEL_DURABILITY = 2048;

    private RubberTreeRegistrator() {}

    public static void initBlocks() {
        GregTechAPI.sBlockRubberLog = new BlockRubberLog();
        GregTechAPI.sBlockRubberLogNatural = new BlockRubberLogNatural();
        GregTechAPI.sBlockRubberLeaves = new BlockRubberLeaves();
        GregTechAPI.sBlockRubberSapling = new BlockRubberSapling();

        GameRegistry.registerTileEntity(TileEntityRubberLogTapped.class, "gt.tile_rubber_log_tapped");

        OreDictionary
            .registerOre("logRubber", new ItemStack(GregTechAPI.sBlockRubberLog, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary
            .registerOre("woodRubber", new ItemStack(GregTechAPI.sBlockRubberLog, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary
            .registerOre("treeLeaves", new ItemStack(GregTechAPI.sBlockRubberLeaves, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre(
            "treeSapling",
            new ItemStack(GregTechAPI.sBlockRubberSapling, 1, OreDictionary.WILDCARD_VALUE));

        GameRegistry.registerFuelHandler(new RubberTreeFuelHandler());
    }

    public static void initItems() {
        ItemList.Sticky_Resin.set(new ItemStickyResin("item_sticky_resin", "Sticky Resin", "Sap of a Rubber Tree"));

        ItemList.Tree_Tap_Wooden.set(
            new ItemRubberTreeTap(
                "item_tree_tap_wooden",
                "Wooden Tree Tap",
                "Can be installed on a Rubber Tree with a soft mallet in right hand and Tree Tap in left hand",
                TREE_TAP_WOODEN_DURABILITY));
        ItemList.Tree_Tap_Bronze.set(
            new ItemRubberTreeTap(
                "item_tree_tap_bronze",
                "Bronze Tree Tap",
                "Can be installed on a Rubber Tree with a soft mallet in right hand and Tree Tap in left hand",
                TREE_TAP_BRONZE_DURABILITY));
        ItemList.Tree_Tap_Steel.set(
            new ItemRubberTreeTap(
                "item_tree_tap_steel",
                "Steel Tree Tap",
                "Can be installed on a Rubber Tree with a soft mallet in right hand and Tree Tap in left hand",
                TREE_TAP_STEEL_DURABILITY));

        registerTreeTapOreDictionaryEntries();
        registerTreeTapRecipes();
    }

    private static void registerTreeTapOreDictionaryEntries() {
        OreDictionary.registerOre("treeTapRubber", ItemList.Tree_Tap_Wooden.get(1));
        OreDictionary.registerOre("treeTapRubber", ItemList.Tree_Tap_Bronze.get(1));
        OreDictionary.registerOre("treeTapRubber", ItemList.Tree_Tap_Steel.get(1));
    }

    private static void registerTreeTapRecipes() {
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ItemList.Tree_Tap_Wooden.get(1),
                " P ",
                "PSP",
                " S ",
                'P',
                "plankWood",
                'S',
                "stickWood"));

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ItemList.Tree_Tap_Bronze.get(1),
                " P ",
                "PRP",
                " S ",
                'P',
                "plateBronze",
                'R',
                "ringBronze",
                'S',
                "stickBronze"));

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ItemList.Tree_Tap_Steel.get(1),
                " P ",
                "PRP",
                " S ",
                'P',
                "plateSteel",
                'R',
                "ringSteel",
                'S',
                "stickSteel"));
    }
}
