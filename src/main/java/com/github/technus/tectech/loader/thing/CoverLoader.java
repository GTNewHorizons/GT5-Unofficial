package com.github.technus.tectech.loader.thing;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.item.TeslaCoilCover;
import com.github.technus.tectech.thing.item.TeslaCoilCoverUltimate;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.item.ItemStack;



public class CoverLoader implements Runnable {
    public void run(){
        final IIconContainer TESLA_OVERLAY = new Textures.BlockIcons.CustomIcon("iconsets/TESLA_OVERLAY");
        final IIconContainer TESLA_OVERLAY_ULTIMATE = new Textures.BlockIcons.CustomIcon("iconsets/TESLA_OVERLAY_ULTIMATE");

        GregTech_API.registerCover(new ItemStack(TeslaCoilCover.INSTANCE, 1), new GT_RenderedTexture(TESLA_OVERLAY), new GT_Cover_TM_TeslaCoil());
        GregTech_API.registerCover(new ItemStack(TeslaCoilCoverUltimate.INSTANCE, 1), new GT_RenderedTexture(TESLA_OVERLAY_ULTIMATE), new GT_Cover_TM_TeslaCoil_Ultimate());
        TecTech.LOGGER.info("Cover functionality registered");
    }
}
