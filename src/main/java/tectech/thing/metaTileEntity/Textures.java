package tectech.thing.metaTileEntity;

import static gregtech.api.enums.Textures.BlockIcons.*;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

@Deprecated
public class Textures {

    public static final IIconContainer OVERLAY_ENERGY_IN_POWER = custom("iconsets/OVERLAY_ENERGY_IN_POWER");
    public static final IIconContainer OVERLAY_ENERGY_OUT_POWER = custom("iconsets/OVERLAY_ENERGY_OUT_POWER");
    public static final IIconContainer OVERLAY_ENERGY_IN_LASER = custom("iconsets/OVERLAY_ENERGY_IN_LASER");
    public static final IIconContainer OVERLAY_ENERGY_OUT_LASER = custom("iconsets/OVERLAY_ENERGY_OUT_LASER");
    public static final IIconContainer OVERLAY_ENERGY_ON_WIRELESS_4A = custom("iconsets/OVERLAY_ENERGY_ON_WIRELESS_4A");
    public static final IIconContainer OVERLAY_ENERGY_ON_WIRELESS_16A = custom(
        "iconsets/OVERLAY_ENERGY_ON_WIRELESS_16A");
    public static final IIconContainer OVERLAY_ENERGY_ON_WIRELESS_LASER = custom(
        "iconsets/OVERLAY_ENERGY_ON_WIRELESS_LASER");
    public static final IIconContainer MACHINE_UEV_SIDE = custom("iconsets/MACHINE_UEV_SIDE");
    public static final IIconContainer MACHINE_UIV_SIDE = custom("iconsets/MACHINE_UIV_SIDE");
    public static final IIconContainer MACHINE_UMV_SIDE = custom("iconsets/MACHINE_UMV_SIDE");
    public static final IIconContainer MACHINE_UXV_SIDE = custom("iconsets/MACHINE_UXV_SIDE");
    public static final IIconContainer MACHINE_MAX_SIDE = custom("iconsets/MACHINE_MAX_SIDE");
    public static final IIconContainer MACHINE_UEV_TOP = custom("iconsets/MACHINE_UEV_TOP");
    public static final IIconContainer MACHINE_UIV_TOP = custom("iconsets/MACHINE_UIV_TOP");
    public static final IIconContainer MACHINE_UMV_TOP = custom("iconsets/MACHINE_UMV_TOP");
    public static final IIconContainer MACHINE_UXV_TOP = custom("iconsets/MACHINE_UXV_TOP");
    public static final IIconContainer MACHINE_MAX_TOP = custom("iconsets/MACHINE_MAX_TOP");
    public static final IIconContainer MACHINE_UEV_BOTTOM = custom("iconsets/MACHINE_UEV_BOTTOM");
    public static final IIconContainer MACHINE_UIV_BOTTOM = custom("iconsets/MACHINE_UIV_BOTTOM");
    public static final IIconContainer MACHINE_UMV_BOTTOM = custom("iconsets/MACHINE_UMV_BOTTOM");
    public static final IIconContainer MACHINE_UXV_BOTTOM = custom("iconsets/MACHINE_UXV_BOTTOM");
    public static final IIconContainer MACHINE_MAX_BOTTOM = custom("iconsets/MACHINE_MAX_BOTTOM");

    public static final IIconContainer TESLA_TRANSCEIVER_TOP = custom("iconsets/TESLA_TRANSCEIVER_TOP");

    public static IIconContainer[] MACHINECASINGS_SIDE_TT = new IIconContainer[] { MACHINE_ULV_SIDE, MACHINE_LV_SIDE,
        MACHINE_MV_SIDE, MACHINE_HV_SIDE, MACHINE_EV_SIDE, MACHINE_IV_SIDE, MACHINE_LuV_SIDE, MACHINE_ZPM_SIDE,
        MACHINE_UV_SIDE, MACHINE_UHV_SIDE, MACHINE_UEV_SIDE, MACHINE_UIV_SIDE, MACHINE_UMV_SIDE, MACHINE_UXV_SIDE,
        MACHINE_MAX_SIDE, },
        MACHINECASINGS_TOP_TT = new IIconContainer[] { MACHINE_ULV_TOP, MACHINE_LV_TOP, MACHINE_MV_TOP, MACHINE_HV_TOP,
            MACHINE_EV_TOP, MACHINE_IV_TOP, MACHINE_LuV_TOP, MACHINE_ZPM_TOP, MACHINE_UV_TOP, MACHINE_UHV_TOP,
            MACHINE_UEV_TOP, MACHINE_UIV_TOP, MACHINE_UMV_TOP, MACHINE_UXV_TOP, MACHINE_MAX_TOP, },
        MACHINECASINGS_BOTTOM_TT = new IIconContainer[] { MACHINE_ULV_BOTTOM, MACHINE_LV_BOTTOM, MACHINE_MV_BOTTOM,
            MACHINE_HV_BOTTOM, MACHINE_EV_BOTTOM, MACHINE_IV_BOTTOM, MACHINE_LuV_BOTTOM, MACHINE_ZPM_BOTTOM,
            MACHINE_UV_BOTTOM, MACHINE_UHV_BOTTOM, MACHINE_UEV_BOTTOM, MACHINE_UIV_BOTTOM, MACHINE_UMV_BOTTOM,
            MACHINE_UXV_BOTTOM, MACHINE_MAX_BOTTOM, };
    public static ITexture[] OVERLAYS_ENERGY_IN_TT = new ITexture[] {
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 180, 180, 180, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 220, 220, 220, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 255, 100, 0, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 255, 255, 30, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 128, 128, 128, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 240, 240, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 220, 220, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 200, 200, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 180, 180, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 160, 160, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 140, 140, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 120, 120, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 100, 100, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 80, 80, 245, 0 }),
        TextureFactory.of(OVERLAY_ENERGY_IN, new short[] { 60, 60, 245, 0 }), },
        OVERLAYS_ENERGY_OUT_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 180, 180, 180, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 220, 220, 220, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 255, 100, 0, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 255, 255, 30, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 128, 128, 128, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 240, 240, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 220, 220, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 200, 200, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 180, 180, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 160, 160, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 140, 140, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 120, 120, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 100, 100, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 80, 80, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT, new short[] { 60, 60, 245, 0 }), },
        OVERLAYS_ENERGY_IN_MULTI_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 180, 180, 180, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 220, 220, 220, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 255, 100, 0, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 255, 255, 30, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 128, 128, 128, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 240, 240, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 220, 220, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 200, 200, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 180, 180, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 160, 160, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 140, 140, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 120, 120, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 100, 100, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 80, 80, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI_2A, new short[] { 60, 60, 245, 0 }), },
        OVERLAYS_ENERGY_OUT_MULTI_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 180, 180, 180, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 220, 220, 220, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 255, 100, 0, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 255, 255, 30, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 128, 128, 128, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 240, 240, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 220, 220, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 200, 200, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 180, 180, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 160, 160, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 140, 140, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 120, 120, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 100, 100, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 80, 80, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI_2A, new short[] { 60, 60, 245, 0 }), },
        OVERLAYS_ENERGY_IN_POWER_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 180, 180, 180, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 220, 220, 220, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 255, 100, 0, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 255, 255, 30, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 128, 128, 128, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 240, 240, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 220, 220, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 200, 200, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 180, 180, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 160, 160, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 140, 140, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 120, 120, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 100, 100, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 80, 80, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, new short[] { 60, 60, 245, 0 }), },
        OVERLAYS_ENERGY_OUT_POWER_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 180, 180, 180, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 220, 220, 220, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 255, 100, 0, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 255, 255, 30, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 128, 128, 128, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 240, 240, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 220, 220, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 200, 200, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 180, 180, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 160, 160, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 140, 140, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 120, 120, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 100, 100, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 80, 80, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, new short[] { 60, 60, 245, 0 }), },
        OVERLAYS_ENERGY_IN_LASER_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 180, 180, 180, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 220, 220, 220, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 255, 100, 0, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 255, 255, 30, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 128, 128, 128, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 240, 240, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 220, 220, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 200, 200, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 180, 180, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 160, 160, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 140, 140, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 120, 120, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 100, 100, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 80, 80, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, new short[] { 60, 60, 245, 0 }), },
        OVERLAYS_ENERGY_OUT_LASER_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 180, 180, 180, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 220, 220, 220, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 255, 100, 0, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 255, 255, 30, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 128, 128, 128, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 240, 240, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 220, 220, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 200, 200, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 180, 180, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 160, 160, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 140, 140, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 120, 120, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 100, 100, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 80, 80, 245, 0 }),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, new short[] { 60, 60, 245, 0 }), };
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

    public static final int TIERS = 15;
    public static final int CASING_COLORS = Dyes.VALUES.length + 1; // MACHINE_METAL followed by Dyes.VALUES
    public static ITexture[][] MACHINE_CASINGS_TT = new ITexture[TIERS][CASING_COLORS];

    public static ITexture TESLA_TRANSCEIVER_TOP_BA = TextureFactory.of(TESLA_TRANSCEIVER_TOP);

    public static void run() {
        for (byte tier = 0; tier < TIERS; tier++) {
            MACHINE_CASINGS_TT[tier][0] = TextureFactory.of(
                MACHINECASINGS_BOTTOM_TT[tier],
                MACHINECASINGS_TOP_TT[tier],
                MACHINECASINGS_SIDE_TT[tier],
                Dyes.MACHINE_METAL.getRGBA());

            for (Dyes dye : Dyes.VALUES) {
                MACHINE_CASINGS_TT[tier][dye.mIndex + 1] = TextureFactory.of(
                    MACHINECASINGS_BOTTOM_TT[tier],
                    MACHINECASINGS_TOP_TT[tier],
                    MACHINECASINGS_SIDE_TT[tier],
                    dye.getRGBA());
            }
        }

        MACHINE_CASINGS = MACHINE_CASINGS_TT;

        // These will throw IndexOutOfBoundsException if one of the arrays are the wrong length
        System.arraycopy(OVERLAYS_ENERGY_IN_TT, 0, OVERLAYS_ENERGY_IN, 0, OVERLAYS_ENERGY_IN_TT.length);
        System.arraycopy(OVERLAYS_ENERGY_OUT_TT, 0, OVERLAYS_ENERGY_OUT, 0, OVERLAYS_ENERGY_OUT_TT.length);
        System.arraycopy(
            OVERLAYS_ENERGY_IN_MULTI_TT,
            0,
            OVERLAYS_ENERGY_IN_MULTI_2A,
            0,
            OVERLAYS_ENERGY_IN_MULTI_TT.length);
        System.arraycopy(
            OVERLAYS_ENERGY_OUT_MULTI_TT,
            0,
            OVERLAYS_ENERGY_OUT_MULTI_2A,
            0,
            OVERLAYS_ENERGY_OUT_MULTI_TT.length);
    }
}
