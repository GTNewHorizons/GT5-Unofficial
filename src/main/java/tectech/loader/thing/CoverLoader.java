package tectech.loader.thing;

import net.minecraft.item.ItemStack;

import gregtech.api.covers.CoverPlacer;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.render.TextureFactory;
import tectech.TecTech;
import tectech.thing.cover.CoverEnderFluidLink;
import tectech.thing.cover.CoverPowerPassUpgrade;
import tectech.thing.cover.CoverTeslaCoil;
import tectech.thing.cover.CoverTeslaCoilUltimate;
import tectech.thing.item.ItemEnderFluidLinkCover;
import tectech.thing.item.ItemPowerPassUpgradeCover;
import tectech.thing.item.ItemTeslaCoilCover;

public class CoverLoader implements Runnable {

    public void run() {
        final IIconContainer TESLA_OVERLAY = new Textures.BlockIcons.CustomIcon("iconsets/TESLA_OVERLAY");
        final IIconContainer TESLA_OVERLAY_ULTIMATE = new Textures.BlockIcons.CustomIcon(
            "iconsets/TESLA_OVERLAY_ULTIMATE");
        final IIconContainer ENDERFLUIDLINK_OVERLAY = new Textures.BlockIcons.CustomIcon(
            "iconsets/ENDERFLUIDLINK_OVERLAY");
        final IIconContainer POWERPASSUPGRADE_OVERLAY = new Textures.BlockIcons.CustomIcon(
            "iconsets/POWERPASSUPGRADE_OVERLAY");

        CoverRegistry.registerCover(
            new ItemStack(ItemTeslaCoilCover.INSTANCE, 1, 0),
            TextureFactory.of(TESLA_OVERLAY),
            CoverTeslaCoil::new);
        CoverRegistry.registerCover(
            new ItemStack(ItemTeslaCoilCover.INSTANCE, 1, 1),
            TextureFactory.of(TESLA_OVERLAY_ULTIMATE),
            CoverTeslaCoilUltimate::new);
        CoverRegistry.registerCover(
            new ItemStack(ItemEnderFluidLinkCover.INSTANCE, 1, 0),
            TextureFactory.of(ENDERFLUIDLINK_OVERLAY),
            context -> new CoverEnderFluidLink(context, TextureFactory.of(ENDERFLUIDLINK_OVERLAY)));
        CoverRegistry.registerCover(
            new ItemStack(ItemPowerPassUpgradeCover.INSTANCE, 1, 0),
            TextureFactory.of(POWERPASSUPGRADE_OVERLAY),
            CoverPowerPassUpgrade::new,
            CoverPlacer.builder()
                .onlyPlaceIf(CoverPowerPassUpgrade::isCoverPlaceable)
                .build());
        TecTech.LOGGER.info("Cover functionality registered");
    }
}
