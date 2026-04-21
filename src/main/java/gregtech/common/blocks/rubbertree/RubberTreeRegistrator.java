package gregtech.common.blocks.rubbertree;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.common.items.ItemStickyResin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.GregTechAPI;

public final class RubberTreeRegistrator {

    private RubberTreeRegistrator() {}

    public static void initBlocks() {
        GregTechAPI.sBlockRubberLog = new BlockRubberLog();
        GregTechAPI.sBlockRubberLeaves = new BlockRubberLeaves();
        GregTechAPI.sBlockRubberSapling = new BlockRubberSapling();

        OreDictionary.registerOre(
            "logRubber",
            new ItemStack(GregTechAPI.sBlockRubberLog, 1, OreDictionary.WILDCARD_VALUE)
        );
        OreDictionary.registerOre(
            "woodRubber",
            new ItemStack(GregTechAPI.sBlockRubberLog, 1, OreDictionary.WILDCARD_VALUE)
        );
        OreDictionary.registerOre(
            "treeLeaves",
            new ItemStack(GregTechAPI.sBlockRubberLeaves, 1, OreDictionary.WILDCARD_VALUE)
        );
        OreDictionary.registerOre(
            "treeSapling",
            new ItemStack(GregTechAPI.sBlockRubberSapling, 1, OreDictionary.WILDCARD_VALUE)
        );

        GameRegistry.registerTileEntity(TileEntityRubberLog.class, "gt.rubber_log");
        GameRegistry.registerFuelHandler(new RubberTreeFuelHandler());
    }

    public static void initItems() {
        ItemList.Sticky_Resin.set(
            new ItemStickyResin(
                "item_sticky_resin", "Sticky Resin", "Sap of a Rubber Tree"));
    }
}
