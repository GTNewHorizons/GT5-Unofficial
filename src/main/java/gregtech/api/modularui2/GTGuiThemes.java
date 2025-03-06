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
        .customTextColor(GTWidgetThemes.TITLE_TEXT, 0x202020)
        .button(GTTextureIds.BUTTON_STANDARD)
        .simpleToggleButton(
            GTTextureIds.BUTTON_STANDARD,
            GTTextureIds.BUTTON_STANDARD_PRESSED,
            Dyes.MACHINE_METAL.toInt())
        .build();
    public static final GTGuiTheme BRONZE = GTGuiTheme.builder("gregtech:bronze")
        .panel(GTTextureIds.BACKGROUND_BRONZE)
        .itemSlot(GTTextureIds.SLOT_ITEM_BRONZE)
        .fluidSlot(GTTextureIds.SLOT_FLUID_BRONZE)
        .build();
    public static final GTGuiTheme STEEL = GTGuiTheme.builder("gregtech:steel")
        .panel(GTTextureIds.BACKGROUND_STEEL)
        .itemSlot(GTTextureIds.SLOT_ITEM_STEEL)
        .fluidSlot(GTTextureIds.SLOT_FLUID_STEEL)
        .build();
    public static final GTGuiTheme PRIMITIVE = GTGuiTheme.builder("gregtech:primitive")
        .panel(GTTextureIds.BACKGROUND_PRIMITIVE)
        .itemSlot(GTTextureIds.SLOT_ITEM_PRIMITIVE)
        .fluidSlot(GTTextureIds.SLOT_FLUID_PRIMITIVE)
        .build();
}
