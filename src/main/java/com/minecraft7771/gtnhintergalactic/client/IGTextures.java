package com.minecraft7771.gtnhintergalactic.client;

import galaxyspace.core.register.GSBlocks;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

/**
 * Textures used for MTEs are defined here
 *
 * @author minecraft7771
 */
public class IGTextures implements Runnable {

    public static int ADVANCED_MACHINE_FRAME_INDEX = 154;
    public static IIconContainer SIPHON_OVERLAY_FRONT_GLOW;
    public static IIconContainer SIPHON_OVERLAY_FRONT_ACTIVE_GLOW;
    public static ITexture SIPHON_OVERLAY_FRONT;

    /**
     * Register all used textures
     */
    @Override
    public void run() {
        SIPHON_OVERLAY_FRONT = TextureFactory
                .of(new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_FRONT_PLANETARYSIPHON"));
        SIPHON_OVERLAY_FRONT_GLOW = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_FRONT_PLANETARYSIPHON_GLOW");
        SIPHON_OVERLAY_FRONT_ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon(
                "iconsets/OVERLAY_FRONT_PLANETARYSIPHON_ACTIVE_GLOW");
        Textures.BlockIcons
                .setCasingTextureForId(ADVANCED_MACHINE_FRAME_INDEX, TextureFactory.of(GSBlocks.MachineFrames));
    }
}
