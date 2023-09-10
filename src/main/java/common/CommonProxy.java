package common;

import common.items.ErrorItem;
import common.items.MetaItem_CraftingComponent;
import common.items.MetaItem_ReactorComponent;
import common.tileentities.GTMTE_TFFTHatch;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import kekztech.Items;

public class CommonProxy {

    public void preInit(final FMLPreInitializationEvent e) {
        // Items
        ErrorItem.getInstance().registerItem();
        MetaItem_ReactorComponent.getInstance().registerItem();
        MetaItem_CraftingComponent.getInstance().registerItem();
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
