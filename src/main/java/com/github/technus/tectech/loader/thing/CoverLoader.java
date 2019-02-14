package com.github.technus.tectech.loader.thing;

import com.github.technus.tectech.TecTech;

import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;

public class CoverLoader implements Runnable {
    @Override
    public void run() {
        //TODO Create a techTechModItem method, but for now I need it to actually USE the tesla cover item
        //GregTech_API.registerCover(GT_ModHandler.getModItem("tectech", "tm.teslaCoilCover", 1L), new GT_RenderedTexture(Textures.BlockIcons.VENT_ADVANCED), new GT_Cover_TM_TeslaCoil());
        GregTech_API.registerCover(GT_ModHandler.getIC2Item("reactorReflector", 1L, 1), new GT_RenderedTexture(Textures.BlockIcons.VENT_ADVANCED), new GT_Cover_TM_TeslaCoil());
        TecTech.LOGGER.info("Cover functionality registered");
    }
}
