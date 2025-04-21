package gtnhintergalactic.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gtnhintergalactic.block.IGBlocks;
import gtnhintergalactic.item.IGItems;
import gtnhintergalactic.loader.MachineLoader;
import gtnhintergalactic.loader.RecipeLoader;
import gtnhintergalactic.recipe.IG_RecipeAdder;
import gtnhintergalactic.recipe.MachineRecipes;
import gtnhintergalactic.recipe.ResultNoSpaceProject;
import gtnhintergalactic.recipe.SpaceProjectRegistration;
import gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

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
