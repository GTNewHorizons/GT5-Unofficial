package kekztech.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import kekztech.common.items.ErrorItem;
import kekztech.common.items.MetaItem_CraftingComponent;
import kekztech.common.tileentities.GTMTE_TFFTHatch;
import kekztech.core.Items;

public class CommonProxy {

    public void preInit(final FMLPreInitializationEvent e) {
        // Items
        ErrorItem.getInstance()
            .registerItem();
        MetaItem_CraftingComponent.getInstance()
            .registerItem();
        Items.registerOreDictNames();
        // Blocks
        Blocks.preInit();
        // TileEntities
        TileEntities.preInit();
        // TC Research
        Researches.preInit();
    }

    public void init(final FMLInitializationEvent e) {
        // GregTech Meta TileEntities
        TileEntities.init();
    }

    public void postInit(final FMLPostInitializationEvent e) {
        // Recipes
        Recipes.postInit();
        // Research
        Researches.postInit();

        GTMTE_TFFTHatch.registerAEIntegration();
    }
}
