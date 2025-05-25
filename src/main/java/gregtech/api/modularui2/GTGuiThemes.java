package gregtech.api.modularui2;

import gregtech.api.enums.Dyes;
import gtPlusPlus.xmod.gregtech.common.modularui2.GTPPTextureIds;

/**
 * Holds all the {@link GTGuiTheme}s registered.
 */
public final class GTGuiThemes {

    public static void init() {}

    public static final GTGuiTheme STANDARD = GTGuiTheme.builder("gregtech:standard")
        .panel(GTTextureIds.BACKGROUND_STANDARD)
        .itemSlot(GTTextureIds.SLOT_ITEM_STANDARD)
        .fluidSlot(GTTextureIds.SLOT_FLUID_STANDARD)
        .color(Dyes.MACHINE_METAL.toInt())
        .textColor(0x404040)
        .textField(Dyes.dyeWhite.toInt())
        .customTextColor(GTWidgetThemes.TEXT_TITLE, 0x404040)
        .button(GTTextureIds.BUTTON_STANDARD)
        .simpleToggleButton(
            GTTextureIds.BUTTON_STANDARD,
            GTTextureIds.BUTTON_STANDARD_PRESSED,
            Dyes.MACHINE_METAL.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP, GTTextureIds.BACKGROUND_POPUP_STANDARD)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL, GTTextureIds.OVERLAY_SLOT_COAL_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN, GTTextureIds.OVERLAY_SLOT_IN_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT, GTTextureIds.OVERLAY_SLOT_OUT_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK, GTTextureIds.OVERLAY_SLOT_BLOCK_STANDARD)
        .themedOverlayFluidSlot(GTWidgetThemes.OVERLAY_FLUID_SLOT_IN, GTTextureIds.OVERLAY_SLOT_IN_STANDARD)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT,
            GTTextureIds.SLOT_FLUID_STANDARD,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .progressbar(GTWidgetThemes.PROGRESSBAR_FUEL, GTTextureIds.PROGRESSBAR_FUEL_STANDARD, 14)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED,
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_STANDARD,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_STANDARD)
        .themedButton(GTWidgetThemes.BUTTON_COVER_TAB_DISABLED, GTTextureIds.BUTTON_COVER_TAB_DISABLED_STANDARD)
        .themedTexture(GTWidgetThemes.PICTURE_CANISTER, GTPPTextureIds.PICTURE_CANISTER_DARK)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO, GTTextureIds.PICTURE_GT_LOGO_STANDARD)
        .build();
    public static final GTGuiTheme COVER = GTGuiTheme.builder("gregtech:cover")
        .parent(STANDARD)
        .textColor(0x555555)
        .customTextColor(GTWidgetThemes.TEXT_TITLE, 0x222222)
        .textField(Dyes.dyeWhite.toInt())
        .build();
    public static final GTGuiTheme BRONZE = GTGuiTheme.builder("gregtech:bronze")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_BRONZE)
        .itemSlot(GTTextureIds.SLOT_ITEM_BRONZE)
        .fluidSlot(GTTextureIds.SLOT_FLUID_BRONZE)
        .color(Dyes.dyeWhite.toInt())
        .button(GTTextureIds.BUTTON_BRONZE)
        .simpleToggleButton(GTTextureIds.BUTTON_BRONZE, GTTextureIds.BUTTON_BRONZE_PRESSED, Dyes.dyeWhite.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP, GTTextureIds.BACKGROUND_POPUP_BRONZE)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL, GTTextureIds.OVERLAY_SLOT_COAL_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN, GTTextureIds.OVERLAY_SLOT_IN_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT, GTTextureIds.OVERLAY_SLOT_OUT_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK, GTTextureIds.OVERLAY_SLOT_BLOCK_BRONZE)
        .themedOverlayFluidSlot(GTWidgetThemes.OVERLAY_FLUID_SLOT_IN, GTTextureIds.OVERLAY_SLOT_IN_BRONZE)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT,
            GTTextureIds.SLOT_FLUID_BRONZE,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .progressbar(GTWidgetThemes.PROGRESSBAR_FUEL, GTTextureIds.PROGRESSBAR_FUEL_BRONZE, 14)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED,
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_BRONZE,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_BRONZE)
        .themedButton(GTWidgetThemes.BUTTON_COVER_TAB_DISABLED, GTTextureIds.BUTTON_COVER_TAB_DISABLED_BRONZE)
        .themedTexture(GTWidgetThemes.PICTURE_CANISTER, GTTextureIds.OVERLAY_SLOT_CANISTER_BRONZE)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO, GTTextureIds.PICTURE_GT_LOGO_BRONZE)
        .build();
    public static final GTGuiTheme STEEL = GTGuiTheme.builder("gregtech:steel")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_STEEL)
        .itemSlot(GTTextureIds.SLOT_ITEM_STEEL)
        .fluidSlot(GTTextureIds.SLOT_FLUID_STEEL)
        .color(Dyes.dyeWhite.toInt())
        .textColor(0xfafaff)
        .customTextColor(GTWidgetThemes.TEXT_TITLE, 0xfafaff)
        .button(GTTextureIds.BUTTON_STEEL)
        .simpleToggleButton(GTTextureIds.BUTTON_STEEL, GTTextureIds.BUTTON_STEEL_PRESSED, Dyes.dyeWhite.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP, GTTextureIds.BACKGROUND_POPUP_STEEL)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL, GTTextureIds.OVERLAY_SLOT_COAL_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN, GTTextureIds.OVERLAY_SLOT_IN_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT, GTTextureIds.OVERLAY_SLOT_OUT_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK, GTTextureIds.OVERLAY_SLOT_BLOCK_STEEL)
        .themedOverlayFluidSlot(GTWidgetThemes.OVERLAY_FLUID_SLOT_IN, GTTextureIds.OVERLAY_SLOT_IN_STEEL)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT,
            GTTextureIds.SLOT_FLUID_STEEL,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .progressbar(GTWidgetThemes.PROGRESSBAR_FUEL, GTTextureIds.PROGRESSBAR_FUEL_STEEL, 14)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED,
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_STEEL,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_STEEL)
        .themedButton(GTWidgetThemes.BUTTON_COVER_TAB_DISABLED, GTTextureIds.BUTTON_COVER_TAB_DISABLED_STEEL)
        .themedTexture(GTWidgetThemes.PICTURE_CANISTER, GTTextureIds.OVERLAY_SLOT_CANISTER_STEEL)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO, GTTextureIds.PICTURE_GT_LOGO_STEEL)
        .build();
    public static final GTGuiTheme PRIMITIVE = GTGuiTheme.builder("gregtech:primitive")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_PRIMITIVE)
        .itemSlot(GTTextureIds.SLOT_ITEM_PRIMITIVE)
        .fluidSlot(GTTextureIds.SLOT_FLUID_PRIMITIVE)
        .color(Dyes.dyeWhite.toInt())
        .textColor(0xfafaff)
        .customTextColor(GTWidgetThemes.TEXT_TITLE, 0xfafaff)
        .button(GTTextureIds.BUTTON_PRIMITIVE)
        .simpleToggleButton(GTTextureIds.BUTTON_PRIMITIVE, GTTextureIds.BUTTON_PRIMITIVE_PRESSED, Dyes.dyeWhite.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP, GTTextureIds.BACKGROUND_POPUP_PRIMITIVE)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_PRIMITIVE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_PRIMITIVE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_PRIMITIVE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_PRIMITIVE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK, GTTextureIds.OVERLAY_SLOT_BLOCK_PRIMITIVE)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT,
            GTTextureIds.SLOT_FLUID_PRIMITIVE,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED,
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_PRIMITIVE,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_PRIMITIVE)
        .themedButton(GTWidgetThemes.BUTTON_COVER_TAB_DISABLED, GTTextureIds.BUTTON_COVER_TAB_DISABLED_PRIMITIVE)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO, GTTextureIds.PICTURE_GT_LOGO_PRIMITIVE)
        .build();
}
