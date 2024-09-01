package tectech.loader.thing;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
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

        GregTech_API.registerCover(
            new ItemStack(ItemTeslaCoilCover.INSTANCE, 1, 0),
            new GT_RenderedTexture(TESLA_OVERLAY),
            new CoverTeslaCoil());
        GregTech_API.registerCover(
            new ItemStack(ItemTeslaCoilCover.INSTANCE, 1, 1),
            new GT_RenderedTexture(TESLA_OVERLAY_ULTIMATE),
            new CoverTeslaCoilUltimate());
        GregTech_API.registerCover(
            new ItemStack(ItemEnderFluidLinkCover.INSTANCE, 1, 0),
            new GT_RenderedTexture(ENDERFLUIDLINK_OVERLAY),
            new CoverEnderFluidLink());
        GregTech_API.registerCover(
            new ItemStack(ItemPowerPassUpgradeCover.INSTANCE, 1, 0),
            new GT_RenderedTexture(POWERPASSUPGRADE_OVERLAY),
            new CoverPowerPassUpgrade());
        TecTech.LOGGER.info("Cover functionality registered");
    }
}
