package gregtech.api.gui.modularui2;

import gregtech.api.enums.Dyes;

/**
 * Holds all the {@link GTGuiTheme}s registered.
 */
public final class GTGuiThemes {

    public static void init() {}

    public static final GTGuiTheme STANDARD = GTGuiTheme.builder("gregtech:standard")
        .panel(GTGuiTextures.IDs.BACKGROUND_STANDARD)
        .itemSlot(GTGuiTextures.IDs.SLOT_ITEM_STANDARD)
        .fluidSlot(GTGuiTextures.IDs.SLOT_FLUID_STANDARD)
        .color(Dyes.MACHINE_METAL.toInt())
        .textColor(0x404040)
        .textField(Dyes.dyeWhite.toInt())
        .customTextColor(GTWidgetThemes.TITLE_TEXT, 0x202020)
        .button(GTGuiTextures.IDs.BUTTON_STANDARD)
        .simpleToggleButton(
            GTGuiTextures.IDs.BUTTON_STANDARD,
            GTGuiTextures.IDs.BUTTON_STANDARD_PRESSED,
            Dyes.MACHINE_METAL.toInt())
        .build();
    public static final GTGuiTheme BRONZE = GTGuiTheme.builder("gregtech:bronze")
        .panel(GTGuiTextures.IDs.BACKGROUND_BRONZE)
        .itemSlot(GTGuiTextures.IDs.SLOT_ITEM_BRONZE)
        .fluidSlot(GTGuiTextures.IDs.SLOT_FLUID_BRONZE)
        .build();
    public static final GTGuiTheme STEEL = GTGuiTheme.builder("gregtech:steel")
        .panel(GTGuiTextures.IDs.BACKGROUND_STEEL)
        .itemSlot(GTGuiTextures.IDs.SLOT_ITEM_STEEL)
        .fluidSlot(GTGuiTextures.IDs.SLOT_FLUID_STEEL)
        .build();
    public static final GTGuiTheme PRIMITIVE = GTGuiTheme.builder("gregtech:primitive")
        .panel(GTGuiTextures.IDs.BACKGROUND_PRIMITIVE)
        .itemSlot(GTGuiTextures.IDs.SLOT_ITEM_PRIMITIVE)
        .fluidSlot(GTGuiTextures.IDs.SLOT_FLUID_PRIMITIVE)
        .build();
}
