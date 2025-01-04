package com.gtnewhorizons.gtnhintergalactic.proxy;

import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.loader.MachineLoader;
import com.gtnewhorizons.gtnhintergalactic.loader.RecipeLoader;
import com.gtnewhorizons.gtnhintergalactic.recipe.IG_RecipeAdder;
import com.gtnewhorizons.gtnhintergalactic.recipe.MachineRecipes;
import com.gtnewhorizons.gtnhintergalactic.recipe.ResultNoSpaceProject;
import com.gtnewhorizons.gtnhintergalactic.recipe.SpaceProjectRegistration;
import com.gtnewhorizons.gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

/**
 * Proxy used by both, the server and the client to load stuff
 *
 * @author minecraft7771
 */
public class CommonProxy {

    public void init(FMLInitializationEvent event) {
        if (Textures.BlockIcons.casingTexturePages[32] == null) {
            Textures.BlockIcons.casingTexturePages[32] = new ITexture[128];
        }
        IGItems.init();
        IGBlocks.init();
        new MachineLoader().run();
        IG_RecipeAdder.init();
        GameRegistry.registerTileEntity(TileEntitySpaceElevatorCable.class, "Space Elevator Cable");
        CheckRecipeResultRegistry.register(new ResultNoSpaceProject("", ""));
    }

    public void postInit(FMLPostInitializationEvent event) {
        new RecipeLoader().run();
        new SpaceProjectRegistration().run();
        new MachineRecipes().run();
        IG_RecipeAdder.postInit();
    }
}
