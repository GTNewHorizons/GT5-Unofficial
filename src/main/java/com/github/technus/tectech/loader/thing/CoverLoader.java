package com.github.technus.tectech.loader.thing;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.item.TeslaCoilCover;
import com.github.technus.tectech.thing.item.TeslaCoilCoverUltimate;
import gregtech.api.GregTech_API;
import net.minecraft.item.ItemStack;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.TESLA_COVER_TEXTURES;

public class CoverLoader implements Runnable {
    public void run(){
        //GregTech_API.registerCover(new ItemStack(TeslaCoilCover.INSTANCE, 1), new GT_RenderedTexture(Textures.BlockIcons.VENT_NORMAL), new GT_Cover_TM_TeslaCoil());
        //GregTech_API.registerCover(new ItemStack(TeslaCoilCoverUltimate.INSTANCE, 1), new GT_RenderedTexture(Textures.BlockIcons.VENT_ADVANCED), new GT_Cover_TM_TeslaCoil_Ultimate());
        GregTech_API.registerCover(new ItemStack(TeslaCoilCover.INSTANCE, 1), TESLA_COVER_TEXTURES[0], new GT_Cover_TM_TeslaCoil());
        GregTech_API.registerCover(new ItemStack(TeslaCoilCoverUltimate.INSTANCE, 1), TESLA_COVER_TEXTURES[1], new GT_Cover_TM_TeslaCoil_Ultimate());
        TecTech.LOGGER.info("Cover functionality registered");
    }
}
