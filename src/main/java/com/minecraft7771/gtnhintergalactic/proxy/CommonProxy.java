package com.minecraft7771.gtnhintergalactic.proxy;

import com.minecraft7771.gtnhintergalactic.GTNHIntergalactic;
import com.minecraft7771.gtnhintergalactic.Tags;
import com.minecraft7771.gtnhintergalactic.block.BlockCasingSpaceElevator;
import com.minecraft7771.gtnhintergalactic.block.BlockCasingSpaceElevatorMotor;
import com.minecraft7771.gtnhintergalactic.block.IGBlocks;
import com.minecraft7771.gtnhintergalactic.config.Config;
import com.minecraft7771.gtnhintergalactic.item.IGItems;
import com.minecraft7771.gtnhintergalactic.loader.MachineLoader;
import com.minecraft7771.gtnhintergalactic.loader.RecipeLoader;
import com.minecraft7771.gtnhintergalactic.nei.IMCForNEI;
import com.minecraft7771.gtnhintergalactic.recipe.SpaceProjectRegistration;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import net.minecraft.util.IIcon;

/**
 * Proxy used by both, the server and the client to load stuff
 *
 * @author minecraft7771
 */
public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        IGItems.init();
        IGBlocks.init();
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        IMCForNEI.IMCSender();
        if (Textures.BlockIcons.casingTexturePages[32] == null) {
            Textures.BlockIcons.casingTexturePages[32] = new ITexture[128];
        }
        BlockCasingSpaceElevator.INSTANCE = new BlockCasingSpaceElevator();
        BlockCasingSpaceElevatorMotor.INSTANCE = new BlockCasingSpaceElevatorMotor();
        new MachineLoader().run();
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        new RecipeLoader().run();
        new SpaceProjectRegistration().run();
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}

    /**
     * Mark a texture as used, to prevent hodgepodge from optimizing it
     *
     * @param o Textured to be used
     */
    public void markTextureUsed(IIcon o) {}
}
