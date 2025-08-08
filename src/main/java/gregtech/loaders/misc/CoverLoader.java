package gregtech.loaders.misc;

import static gregtech.api.enums.Textures.BlockIcons.VENT_ADVANCED;
import static gregtech.api.enums.Textures.BlockIcons.VENT_NORMAL;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.common.covers.CoverVent;

public class CoverLoader implements Runnable {

    @Override
    public void run() {
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            CoverRegistry
                .registerDecorativeCover(new ItemStack(Blocks.carpet, 1, i), TextureFactory.of(Blocks.wool, i));
        }
        CoverRegistry.registerCover(
            GTModHandler.getIC2Item("reactorVent", 1L, 1),
            TextureFactory.of(VENT_NORMAL),
            context -> new CoverVent(context, 1));
        CoverRegistry.registerCover(
            GTModHandler.getIC2Item("reactorVentCore", 1L, 1),
            TextureFactory.of(VENT_NORMAL),
            context -> new CoverVent(context, 1));
        CoverRegistry.registerCover(
            GTModHandler.getIC2Item("reactorVentGold", 1L, 1),
            TextureFactory.of(VENT_ADVANCED),
            context -> new CoverVent(context, 2));
        CoverRegistry.registerCover(
            GTModHandler.getIC2Item("reactorVentSpread", 1L),
            TextureFactory.of(VENT_NORMAL),
            context -> new CoverVent(context, 2));
        CoverRegistry.registerCover(
            GTModHandler.getIC2Item("reactorVentDiamond", 1L, 1),
            TextureFactory.of(VENT_ADVANCED),
            context -> new CoverVent(context, 3));
    }
}
