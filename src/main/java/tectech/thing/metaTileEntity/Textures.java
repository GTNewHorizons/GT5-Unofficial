package tectech.thing.metaTileEntity;

import static gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_8V_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_8V_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_8V_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_EV_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_EV_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_EV_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_HV_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_HV_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_HV_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_IV_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_IV_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_IV_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_LV_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_LV_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_LV_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_LuV_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_LuV_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_LuV_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_MAX_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_MAX_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_MAX_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_MV_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_MV_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_MV_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_UV_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_UV_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_UV_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_ZPM_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_ZPM_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_ZPM_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_IN_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_ON_WIRELESS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_OUT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_OUT_MULTI;
import static gregtech.api.util.ColorUtil.toRGB;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
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
        MACHINECASINGS_TOP_TT = new IIconContainer[] { MACHINE_8V_TOP, MACHINE_LV_TOP, MACHINE_MV_TOP, MACHINE_HV_TOP,
            MACHINE_EV_TOP, MACHINE_IV_TOP, MACHINE_LuV_TOP, MACHINE_ZPM_TOP, MACHINE_UV_TOP, MACHINE_MAX_TOP,
            MACHINE_UEV_TOP, MACHINE_UIV_TOP, MACHINE_UMV_TOP, MACHINE_UXV_TOP, MACHINE_MAXV_TOP, },
        MACHINECASINGS_BOTTOM_TT = new IIconContainer[] { MACHINE_8V_BOTTOM, MACHINE_LV_BOTTOM, MACHINE_MV_BOTTOM,
            MACHINE_HV_BOTTOM, MACHINE_EV_BOTTOM, MACHINE_IV_BOTTOM, MACHINE_LuV_BOTTOM, MACHINE_ZPM_BOTTOM,
            MACHINE_UV_BOTTOM, MACHINE_MAX_BOTTOM, MACHINE_UEV_BOTTOM, MACHINE_UIV_BOTTOM, MACHINE_UMV_BOTTOM,
            MACHINE_UXV_BOTTOM, MACHINE_MAXV_BOTTOM, };
    public static ITexture[] OVERLAYS_ENERGY_IN_TT = new ITexture[] {
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(180, 180, 180)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(220, 220, 220)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(255, 100, 0)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(255, 255, 30)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(128, 128, 128)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(240, 240, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(220, 220, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(200, 200, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(180, 180, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(160, 160, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(140, 140, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(120, 120, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(100, 100, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(80, 80, 245)),
        TextureFactory.of(OVERLAY_ENERGY_IN, toRGB(60, 60, 245)), },
        OVERLAYS_ENERGY_OUT_TT = new ITexture[] { TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(180, 180, 180)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(220, 220, 220)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(255, 100, 0)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(255, 255, 30)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(128, 128, 128)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(240, 240, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(220, 220, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(200, 200, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(180, 180, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(160, 160, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(140, 140, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(120, 120, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(100, 100, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(80, 80, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT, toRGB(60, 60, 245)), },
        OVERLAYS_ENERGY_IN_MULTI_TT = new ITexture[] { TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(180, 180, 180)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(220, 220, 220)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(255, 100, 0)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(255, 255, 30)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(128, 128, 128)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(240, 240, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(220, 220, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(200, 200, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(180, 180, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(160, 160, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(140, 140, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(120, 120, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(100, 100, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(80, 80, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_MULTI, toRGB(60, 60, 245)), },
        OVERLAYS_ENERGY_OUT_MULTI_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(180, 180, 180)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(220, 220, 220)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(255, 100, 0)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(255, 255, 30)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(128, 128, 128)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(240, 240, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(220, 220, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(200, 200, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(180, 180, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(160, 160, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(140, 140, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(120, 120, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(100, 100, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(80, 80, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI, toRGB(60, 60, 245)), },
        OVERLAYS_ENERGY_IN_POWER_TT = new ITexture[] { TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(180, 180, 180)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(220, 220, 220)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(255, 100, 0)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(255, 255, 30)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(128, 128, 128)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(240, 240, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(220, 220, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(200, 200, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(180, 180, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(160, 160, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(140, 140, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(120, 120, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(100, 100, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(80, 80, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_POWER, toRGB(60, 60, 245)), },
        OVERLAYS_ENERGY_OUT_POWER_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(180, 180, 180)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(220, 220, 220)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(255, 100, 0)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(255, 255, 30)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(128, 128, 128)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(240, 240, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(220, 220, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(200, 200, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(180, 180, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(160, 160, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(140, 140, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(120, 120, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(100, 100, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(80, 80, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_POWER, toRGB(60, 60, 245)), },
        OVERLAYS_ENERGY_IN_LASER_TT = new ITexture[] { TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(180, 180, 180)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(220, 220, 220)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(255, 100, 0)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(255, 255, 30)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(128, 128, 128)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(240, 240, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(220, 220, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(200, 200, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(180, 180, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(160, 160, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(140, 140, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(120, 120, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(100, 100, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(80, 80, 245)),
            TextureFactory.of(OVERLAY_ENERGY_IN_LASER, toRGB(60, 60, 245)), },
        OVERLAYS_ENERGY_OUT_LASER_TT = new ITexture[] {
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(180, 180, 180)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(220, 220, 220)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(255, 100, 0)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(255, 255, 30)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(128, 128, 128)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(240, 240, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(220, 220, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(200, 200, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(180, 180, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(160, 160, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(140, 140, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(120, 120, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(100, 100, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(80, 80, 245)),
            TextureFactory.of(OVERLAY_ENERGY_OUT_LASER, toRGB(60, 60, 245)), };
    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A = {
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_4A, toRGB(255, 255, 255)) };

    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A = {
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_16A, toRGB(255, 255, 255)) };

    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A = {
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS, toRGB(25, 43, 255)) };

    public static final ITexture[] OVERLAYS_ENERGY_IN_WIRELESS_LASER = {
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)),
        TextureFactory.of(OVERLAY_ENERGY_ON_WIRELESS_LASER, toRGB(255, 255, 255)) };

    private static final int TIERS = 15;
    private static final int CASING_COLORS = Dyes.VALUES.length + 1; // MACHINE_METAL followed by Dyes.VALUES
    public static ITexture[][] MACHINE_CASINGS_TT = new ITexture[TIERS][CASING_COLORS];

    public static ITexture TESLA_TRANSCEIVER_TOP_BA = TextureFactory.of(TESLA_TRANSCEIVER_TOP);

    public static void run() {
        for (byte tier = 0; tier < TIERS; tier++) {
            MACHINE_CASINGS_TT[tier][0] = TextureFactory.of(
                MACHINECASINGS_BOTTOM_TT[tier],
                MACHINECASINGS_TOP_TT[tier],
                MACHINECASINGS_SIDE_TT[tier],
                Dyes.MACHINE_METAL.colorRGB);

            for (Dyes dye : Dyes.VALUES) {
                MACHINE_CASINGS_TT[tier][dye.mIndex + 1] = TextureFactory.of(
                    MACHINECASINGS_BOTTOM_TT[tier],
                    MACHINECASINGS_TOP_TT[tier],
                    MACHINECASINGS_SIDE_TT[tier],
                    dye.colorRGB);
            }
        }

        MACHINE_CASINGS = MACHINE_CASINGS_TT;

        // These will throw IndexOutOfBoundsException if one of the arrays are the wrong length
        System.arraycopy(OVERLAYS_ENERGY_IN_TT, 0, OVERLAYS_ENERGY_IN, 0, OVERLAYS_ENERGY_IN_TT.length);
        System.arraycopy(OVERLAYS_ENERGY_OUT_TT, 0, OVERLAYS_ENERGY_OUT, 0, OVERLAYS_ENERGY_OUT_TT.length);
        System
            .arraycopy(OVERLAYS_ENERGY_IN_MULTI_TT, 0, OVERLAYS_ENERGY_IN_MULTI, 0, OVERLAYS_ENERGY_IN_MULTI_TT.length);
        System.arraycopy(
            OVERLAYS_ENERGY_OUT_MULTI_TT,
            0,
            OVERLAYS_ENERGY_OUT_MULTI,
            0,
            OVERLAYS_ENERGY_OUT_MULTI_TT.length);
    }
}
