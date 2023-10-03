package com.github.technus.tectech.thing.metaTileEntity;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.enums.Textures.BlockIcons.*;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.GT_SidedTexture;
import gregtech.api.render.TextureFactory;

public class Textures {

    private static final IIconContainer OVERLAY_ENERGY_IN_POWER = new CustomIcon("iconsets/OVERLAY_ENERGY_IN_POWER");
    private static final IIconContainer OVERLAY_ENERGY_OUT_POWER = new CustomIcon("iconsets/OVERLAY_ENERGY_OUT_POWER");
    private static final IIconContainer OVERLAY_ENERGY_IN_LASER = new CustomIcon("iconsets/OVERLAY_ENERGY_IN_LASER");
    private static final IIconContainer OVERLAY_ENERGY_OUT_LASER = new CustomIcon("iconsets/OVERLAY_ENERGY_OUT_LASER");
    private static final IIconContainer OVERLAY_ENERGY_ON_WIRELESS_4A = new CustomIcon(
            "iconsets/OVERLAY_ENERGY_ON_WIRELESS_4A");
    private static final IIconContainer OVERLAY_ENERGY_ON_WIRELESS_16A = new CustomIcon(
            "iconsets/OVERLAY_ENERGY_ON_WIRELESS_16A");
    private static final IIconContainer OVERLAY_ENERGY_ON_WIRELESS_LASER = new CustomIcon(
            "iconsets/OVERLAY_ENERGY_ON_WIRELESS_LASER");
    private static final IIconContainer MACHINE_UEV_SIDE = new CustomIcon("iconsets/MACHINE_UEV_SIDE");
    private static final IIconContainer MACHINE_UIV_SIDE = new CustomIcon("iconsets/MACHINE_UIV_SIDE");
    private static final IIconContainer MACHINE_UMV_SIDE = new CustomIcon("iconsets/MACHINE_UMV_SIDE");
    private static final IIconContainer MACHINE_UXV_SIDE = new CustomIcon("iconsets/MACHINE_UXV_SIDE");
    private static final IIconContainer MACHINE_MAXV_SIDE = new CustomIcon("iconsets/MACHINE_MAXV_SIDE");
    private static final IIconContainer MACHINE_UEV_TOP = new CustomIcon("iconsets/MACHINE_UEV_TOP");
    private static final IIconContainer MACHINE_UIV_TOP = new CustomIcon("iconsets/MACHINE_UIV_TOP");
    private static final IIconContainer MACHINE_UMV_TOP = new CustomIcon("iconsets/MACHINE_UMV_TOP");
    private static final IIconContainer MACHINE_UXV_TOP = new CustomIcon("iconsets/MACHINE_UXV_TOP");
    private static final IIconContainer MACHINE_MAXV_TOP = new CustomIcon("iconsets/MACHINE_MAXV_TOP");
    private static final IIconContainer MACHINE_UEV_BOTTOM = new CustomIcon("iconsets/MACHINE_UEV_BOTTOM");
    private static final IIconContainer MACHINE_UIV_BOTTOM = new CustomIcon("iconsets/MACHINE_UIV_BOTTOM");
    private static final IIconContainer MACHINE_UMV_BOTTOM = new CustomIcon("iconsets/MACHINE_UMV_BOTTOM");
    private static final IIconContainer MACHINE_UXV_BOTTOM = new CustomIcon("iconsets/MACHINE_UXV_BOTTOM");
    private static final IIconContainer MACHINE_MAXV_BOTTOM = new CustomIcon("iconsets/MACHINE_MAXV_BOTTOM");

    private static final IIconContainer TESLA_TRANSCEIVER_TOP = new CustomIcon("iconsets/TESLA_TRANSCEIVER_TOP");

    public static IIconContainer[] MACHINECASINGS_SIDE_TT = new IIconContainer[] { MACHINE_8V_SIDE, MACHINE_LV_SIDE,
            MACHINE_MV_SIDE, MACHINE_HV_SIDE, MACHINE_EV_SIDE, MACHINE_IV_SIDE, MACHINE_LuV_SIDE, MACHINE_ZPM_SIDE,
            MACHINE_UV_SIDE, MACHINE_MAX_SIDE, MACHINE_UEV_SIDE, MACHINE_UIV_SIDE, MACHINE_UMV_SIDE, MACHINE_UXV_SIDE,
            MACHINE_MAXV_SIDE, },
            MACHINECASINGS_TOP_TT = new IIconContainer[] { MACHINE_8V_TOP, MACHINE_LV_TOP, MACHINE_MV_TOP,
                    MACHINE_HV_TOP, MACHINE_EV_TOP, MACHINE_IV_TOP, MACHINE_LuV_TOP, MACHINE_ZPM_TOP, MACHINE_UV_TOP,
                    MACHINE_MAX_TOP, MACHINE_UEV_TOP, MACHINE_UIV_TOP, MACHINE_UMV_TOP, MACHINE_UXV_TOP,
                    MACHINE_MAXV_TOP, },
            MACHINECASINGS_BOTTOM_TT = new IIconContainer[] { MACHINE_8V_BOTTOM, MACHINE_LV_BOTTOM, MACHINE_MV_BOTTOM,
                    MACHINE_HV_BOTTOM, MACHINE_EV_BOTTOM, MACHINE_IV_BOTTOM, MACHINE_LuV_BOTTOM, MACHINE_ZPM_BOTTOM,
                    MACHINE_UV_BOTTOM, MACHINE_MAX_BOTTOM, MACHINE_UEV_BOTTOM, MACHINE_UIV_BOTTOM, MACHINE_UMV_BOTTOM,
                    MACHINE_UXV_BOTTOM, MACHINE_MAXV_BOTTOM, };
    public static ITexture[] OVERLAYS_ENERGY_IN_TT = new ITexture[] {
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 180, 180, 180, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 220, 220, 220, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 255, 100, 0, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 255, 255, 30, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 128, 128, 128, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 240, 240, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 220, 220, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 200, 200, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 180, 180, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 160, 160, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 140, 140, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 120, 120, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 100, 100, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 80, 80, 245, 0 }),
            new GT_RenderedTexture(OVERLAY_ENERGY_IN, new short[] { 60, 60, 245, 0 }), },
            OVERLAYS_ENERGY_OUT_TT = new ITexture[] {
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 180, 180, 180, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 220, 220, 220, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 255, 100, 0, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 255, 255, 30, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 128, 128, 128, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 240, 240, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 220, 220, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 200, 200, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 180, 180, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 160, 160, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 140, 140, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 120, 120, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 100, 100, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 80, 80, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT, new short[] { 60, 60, 245, 0 }), },
            OVERLAYS_ENERGY_IN_MULTI_TT = new ITexture[] {
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 180, 180, 180, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 220, 220, 220, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 255, 100, 0, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 255, 255, 30, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 128, 128, 128, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 240, 240, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 220, 220, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 200, 200, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 180, 180, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 160, 160, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 140, 140, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 120, 120, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 100, 100, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 80, 80, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_MULTI, new short[] { 60, 60, 245, 0 }), },
            OVERLAYS_ENERGY_OUT_MULTI_TT = new ITexture[] {
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 180, 180, 180, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 220, 220, 220, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 255, 100, 0, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 255, 255, 30, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 128, 128, 128, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 240, 240, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 220, 220, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 200, 200, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 180, 180, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 160, 160, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 140, 140, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 120, 120, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 100, 100, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 80, 80, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_MULTI, new short[] { 60, 60, 245, 0 }), },
            OVERLAYS_ENERGY_IN_POWER_TT = new ITexture[] {
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 180, 180, 180, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 220, 220, 220, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 255, 100, 0, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 255, 255, 30, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 128, 128, 128, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 240, 240, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 220, 220, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 200, 200, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 180, 180, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 160, 160, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 140, 140, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 120, 120, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 100, 100, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 80, 80, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_POWER, new short[] { 60, 60, 245, 0 }), },
            OVERLAYS_ENERGY_OUT_POWER_TT = new ITexture[] {
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 180, 180, 180, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 220, 220, 220, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 255, 100, 0, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 255, 255, 30, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 128, 128, 128, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 240, 240, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 220, 220, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 200, 200, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 180, 180, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 160, 160, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 140, 140, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 120, 120, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 100, 100, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 80, 80, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_POWER, new short[] { 60, 60, 245, 0 }), },
            OVERLAYS_ENERGY_IN_LASER_TT = new ITexture[] {
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 180, 180, 180, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 220, 220, 220, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 255, 100, 0, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 255, 255, 30, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 128, 128, 128, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 240, 240, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 220, 220, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 200, 200, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 180, 180, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 160, 160, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 140, 140, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 120, 120, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 100, 100, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 80, 80, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_IN_LASER, new short[] { 60, 60, 245, 0 }), },
            OVERLAYS_ENERGY_OUT_LASER_TT = new ITexture[] {
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 180, 180, 180, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 220, 220, 220, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 255, 100, 0, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 255, 255, 30, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 128, 128, 128, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 240, 240, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 220, 220, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 200, 200, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 180, 180, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 160, 160, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 140, 140, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 120, 120, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 100, 100, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 80, 80, 245, 0 }),
                    new GT_RenderedTexture(OVERLAY_ENERGY_OUT_LASER, new short[] { 60, 60, 245, 0 }), };
    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A = {
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, new short[] { 255, 255, 255, 0 }) };

    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A = {
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, new short[] { 255, 255, 255, 0 }) };

    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A = {
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, new short[] { 25, 43, 255, 0 }) };

    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_LASER = {
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, new short[] { 255, 255, 255, 0 }) };

    public static ITexture[][] MACHINE_CASINGS_TT = new ITexture[15][17];

    public static ITexture TESLA_TRANSCEIVER_TOP_BA = new GT_RenderedTexture(TESLA_TRANSCEIVER_TOP);

    public static void run() {
        for (byte i = 0; i < MACHINE_CASINGS_TT.length; i++) {
            for (byte j = 0; j < MACHINE_CASINGS_TT[i].length; j++) {
                MACHINE_CASINGS_TT[i][j] = new GT_SidedTexture(
                        MACHINECASINGS_BOTTOM_TT[i],
                        MACHINECASINGS_TOP_TT[i],
                        MACHINECASINGS_SIDE_TT[i],
                        Dyes.getModulation(j - 1, MACHINE_METAL.mRGBa));
            }
        }
        MACHINE_CASINGS = MACHINE_CASINGS_TT;

        // These will throw IndexOutOfBoundsException if one of the arrays are the wrong length
        System.arraycopy(OVERLAYS_ENERGY_IN_TT, 0, OVERLAYS_ENERGY_IN, 0, OVERLAYS_ENERGY_IN_TT.length);
        System.arraycopy(OVERLAYS_ENERGY_OUT_TT, 0, OVERLAYS_ENERGY_OUT, 0, OVERLAYS_ENERGY_OUT_TT.length);
        System.arraycopy(
                OVERLAYS_ENERGY_IN_MULTI_TT,
                0,
                OVERLAYS_ENERGY_IN_MULTI,
                0,
                OVERLAYS_ENERGY_IN_MULTI_TT.length);
        System.arraycopy(
                OVERLAYS_ENERGY_OUT_MULTI_TT,
                0,
                OVERLAYS_ENERGY_OUT_MULTI,
                0,
                OVERLAYS_ENERGY_OUT_MULTI_TT.length);
    }
}
