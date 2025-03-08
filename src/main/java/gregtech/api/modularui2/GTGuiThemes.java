package gregtech.api.modularui2;

import gregtech.api.enums.Dyes;

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
        .themedTexture(GTWidgetThemes.BACKGROUND_COVER_TAB_NORMAL, GTTextureIds.BACKGROUND_COVER_TAB_NORMAL_STANDARD)
        .themedTexture(
            GTWidgetThemes.BACKGROUND_COVER_TAB_HIGHLIGHT,
            GTTextureIds.BACKGROUND_COVER_TAB_HIGHLIGHT_STANDARD)
        .themedTexture(
            GTWidgetThemes.BACKGROUND_COVER_TAB_DISABLED,
            GTTextureIds.BACKGROUND_COVER_TAB_DISABLED_STANDARD)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_STANDARD)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO, GTTextureIds.PICTURE_GT_LOGO_STANDARD)
        .build();
    public static final GTGuiTheme BRONZE = GTGuiTheme.builder("gregtech:bronze")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_BRONZE)
        .itemSlot(GTTextureIds.SLOT_ITEM_BRONZE)
        .fluidSlot(GTTextureIds.SLOT_FLUID_BRONZE)
        .color(Dyes.dyeWhite.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_COVER_TAB_NORMAL, GTTextureIds.BACKGROUND_COVER_TAB_NORMAL_BRONZE)
        .themedTexture(
            GTWidgetThemes.BACKGROUND_COVER_TAB_HIGHLIGHT,
            GTTextureIds.BACKGROUND_COVER_TAB_HIGHLIGHT_BRONZE)
        .themedTexture(GTWidgetThemes.BACKGROUND_COVER_TAB_DISABLED, GTTextureIds.BACKGROUND_COVER_TAB_DISABLED_BRONZE)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_BRONZE)
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
        .themedTexture(GTWidgetThemes.BACKGROUND_COVER_TAB_NORMAL, GTTextureIds.BACKGROUND_COVER_TAB_NORMAL_STEEL)
        .themedTexture(GTWidgetThemes.BACKGROUND_COVER_TAB_HIGHLIGHT, GTTextureIds.BACKGROUND_COVER_TAB_HIGHLIGHT_STEEL)
        .themedTexture(GTWidgetThemes.BACKGROUND_COVER_TAB_DISABLED, GTTextureIds.BACKGROUND_COVER_TAB_DISABLED_STEEL)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_STEEL)
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
        .themedTexture(GTWidgetThemes.BACKGROUND_COVER_TAB_NORMAL, GTTextureIds.BACKGROUND_COVER_TAB_NORMAL_PRIMITIVE)
        .themedTexture(
            GTWidgetThemes.BACKGROUND_COVER_TAB_HIGHLIGHT,
            GTTextureIds.BACKGROUND_COVER_TAB_HIGHLIGHT_PRIMITIVE)
        .themedTexture(
            GTWidgetThemes.BACKGROUND_COVER_TAB_DISABLED,
            GTTextureIds.BACKGROUND_COVER_TAB_DISABLED_PRIMITIVE)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE, GTTextureIds.BACKGROUND_TITLE_PRIMITIVE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_DUST, GTTextureIds.OVERLAY_SLOT_DUST_PRIMITIVE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_INGOT, GTTextureIds.OVERLAY_SLOT_INGOT_PRIMITIVE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_SLOT_FURNACE, GTTextureIds.OVERLAY_SLOT_FURNACE_PRIMITIVE)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO, GTTextureIds.PICTURE_GT_LOGO_PRIMITIVE)
        .build();
}
