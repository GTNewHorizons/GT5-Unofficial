package kekztech.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.Mods;
import gregtech.api.metatileentity.implementations.MTEHatchInputBusCompressed;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBusCompressed;
import kekztech.Items;
import kekztech.common.items.ErrorItem;
import kekztech.common.items.MetaItemCraftingComponent;
import kekztech.common.tileentities.MTEHatchTFFT;

public class CommonProxy {

    public void preInit(final FMLPreInitializationEvent e) {
        // Items
        ErrorItem.getInstance()
            .registerItem();
        MetaItemCraftingComponent.getInstance()
            .registerItem();
        Items.registerOreDictNames();
        // Blocks
        Blocks.preInit();
        // TileEntities
        TileEntities.preInit();
        if (Mods.Thaumcraft.isModLoaded() && Mods.ThaumicTinkerer.isModLoaded()) {
            // TC Research
            Researches.preInit();
        }
    }

    public void init(final FMLInitializationEvent e) {
        // GregTech Meta TileEntities
        TileEntities.init();
    }

    public void postInit(final FMLPostInitializationEvent e) {
        // Recipes
        Recipes.postInit();
        if (Mods.Thaumcraft.isModLoaded() && Mods.ThaumicTinkerer.isModLoaded()) {
            // Research
            Researches.postInit();
        }

        MTEHatchTFFT.registerAEIntegration();
        MTEHatchOutputBusCompressed.registerAEIntegration();
        MTEHatchInputBusCompressed.registerAEIntegration();
    }
}
