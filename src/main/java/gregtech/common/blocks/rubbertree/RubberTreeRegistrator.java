package gregtech.common.blocks.rubbertree;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.common.items.ItemStickyResin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.GregTechAPI;

public final class RubberTreeRegistrator {

    private RubberTreeRegistrator() {}

    public static Item stickyResin;

    public static void init() {
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

        stickyResin = new ItemStickyResin();
        GameRegistry.registerItem(stickyResin, "gt.sticky_resin");

        GameRegistry.registerTileEntity(TileEntityRubberLog.class, "gt.rubber_log");
    }
}
