package gregtech.loaders.misc;

import static gregtech.api.enums.Textures.BlockIcons.VENT_ADVANCED;
import static gregtech.api.enums.Textures.BlockIcons.VENT_NORMAL;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.covers.Covers;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.common.covers.CoverVent;

public class CoverLoader implements Runnable {

    @Override
    public void run() {
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            Covers.registerCover(new ItemStack(Blocks.carpet, 1, i), TextureFactory.of(Blocks.wool, i), null);
        }
        Covers.registerCover(
            GTModHandler.getIC2Item("reactorVent", 1L, 1),
            TextureFactory.of(VENT_NORMAL),
            new CoverVent(1));
        Covers.registerCover(
            GTModHandler.getIC2Item("reactorVentCore", 1L, 1),
            TextureFactory.of(VENT_NORMAL),
            new CoverVent(1));
        Covers.registerCover(
            GTModHandler.getIC2Item("reactorVentGold", 1L, 1),
            TextureFactory.of(VENT_ADVANCED),
            new CoverVent(2));
        Covers.registerCover(
            GTModHandler.getIC2Item("reactorVentSpread", 1L),
            TextureFactory.of(VENT_NORMAL),
            new CoverVent(2));
        Covers.registerCover(
            GTModHandler.getIC2Item("reactorVentDiamond", 1L, 1),
            TextureFactory.of(VENT_ADVANCED),
            new CoverVent(3));
    }
}
