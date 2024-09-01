package tectech.loader.thing;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import tectech.TecTech;
import tectech.thing.cover.GT_Cover_TM_EnderFluidLink;
import tectech.thing.cover.GT_Cover_TM_PowerPassUpgrade;
import tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import tectech.thing.item.EnderFluidLinkCover;
import tectech.thing.item.PowerPassUpgradeCover;
import tectech.thing.item.TeslaCoilCover;

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
            new ItemStack(TeslaCoilCover.INSTANCE, 1, 0),
            new GT_RenderedTexture(TESLA_OVERLAY),
            new GT_Cover_TM_TeslaCoil());
        GregTech_API.registerCover(
            new ItemStack(TeslaCoilCover.INSTANCE, 1, 1),
            new GT_RenderedTexture(TESLA_OVERLAY_ULTIMATE),
            new GT_Cover_TM_TeslaCoil_Ultimate());
        GregTech_API.registerCover(
            new ItemStack(EnderFluidLinkCover.INSTANCE, 1, 0),
            new GT_RenderedTexture(ENDERFLUIDLINK_OVERLAY),
            new GT_Cover_TM_EnderFluidLink());
        GregTech_API.registerCover(
            new ItemStack(PowerPassUpgradeCover.INSTANCE, 1, 0),
            new GT_RenderedTexture(POWERPASSUPGRADE_OVERLAY),
            new GT_Cover_TM_PowerPassUpgrade());
        TecTech.LOGGER.info("Cover functionality registered");
    }
}
