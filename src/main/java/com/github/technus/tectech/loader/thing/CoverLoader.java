package com.github.technus.tectech.loader.thing;

import com.github.technus.tectech.TecTech;

import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.item.TeslaCoilCover;
import com.github.technus.tectech.thing.item.TeslaCoilCoverUltimate;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.item.ItemStack;
public class CoverLoader implements Runnable {
    public void run(){
        GregTech_API.registerCover(new ItemStack(TeslaCoilCover.INSTANCE, 1), new GT_RenderedTexture(Textures.BlockIcons.VENT_NORMAL), new GT_Cover_TM_TeslaCoil());
        GregTech_API.registerCover(new ItemStack(TeslaCoilCoverUltimate.INSTANCE, 1), new GT_RenderedTexture(Textures.BlockIcons.VENT_ADVANCED), new GT_Cover_TM_TeslaCoil_Ultimate());
        TecTech.LOGGER.info("Cover functionality registered");
    }
}
