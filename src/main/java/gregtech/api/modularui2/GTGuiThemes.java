package gregtech.api.modularui2;

import java.awt.Color;

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
        .color(Dyes.GUI_METAL.toInt())
        .textColor(0x404040)
        .textField(Dyes.dyeWhite.toInt())
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), 0x404040)
        .button(GTTextureIds.BUTTON_STANDARD)
        .simpleToggleButton(GTTextureIds.BUTTON_STANDARD, GTTextureIds.BUTTON_STANDARD_PRESSED, Dyes.GUI_METAL.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP.getFullName(), GTTextureIds.BACKGROUND_POPUP_STANDARD)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE.getFullName(), GTTextureIds.BACKGROUND_TITLE_STANDARD)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL.getFullName(),
            GTTextureIds.OVERLAY_SLOT_COAL_STANDARD)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST.getFullName(),
            GTTextureIds.OVERLAY_SLOT_DUST_STANDARD)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN.getFullName(), GTTextureIds.OVERLAY_SLOT_IN_STANDARD)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT.getFullName(),
            GTTextureIds.OVERLAY_SLOT_INGOT_STANDARD)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE.getFullName(),
            GTTextureIds.OVERLAY_SLOT_FURNACE_STANDARD)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT.getFullName(),
            GTTextureIds.OVERLAY_SLOT_OUT_STANDARD)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK.getFullName(),
            GTTextureIds.OVERLAY_SLOT_BLOCK_STANDARD)
        .themedOverlayFluidSlot(
            GTWidgetThemes.OVERLAY_FLUID_SLOT_IN.getFullName(),
            GTTextureIds.OVERLAY_SLOT_IN_STANDARD)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT.getFullName(),
            GTTextureIds.SLOT_FLUID_STANDARD,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .progressbar(GTWidgetThemes.PROGRESSBAR_FUEL.getFullName(), GTTextureIds.PROGRESSBAR_FUEL_STANDARD, 14)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_STANDARD,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_STANDARD)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_DISABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_STANDARD)
        .themedTexture(GTWidgetThemes.PICTURE_CANISTER.getFullName(), GTPPTextureIds.PICTURE_CANISTER_DARK)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_GT_LOGO_STANDARD)
        .build();
    public static final GTGuiTheme STANDARD_BLACK = GTGuiTheme.builder("gregtech:standard_black")
        .parent(STANDARD)
        .fullColor(Dyes.dyeBlack)
        .build();
    public static final GTGuiTheme STANDARD_RED = GTGuiTheme.builder("gregtech:standard_red")
        .parent(STANDARD)
        .fullColor(Dyes.dyeRed)
        .build();
    public static final GTGuiTheme STANDARD_GREEN = GTGuiTheme.builder("gregtech:standard_green")
        .parent(STANDARD)
        .fullColor(Dyes.dyeGreen)
        .build();
    public static final GTGuiTheme STANDARD_BROWN = GTGuiTheme.builder("gregtech:standard_brown")
        .parent(STANDARD)
        .fullColor(Dyes.dyeBrown)
        .build();
    public static final GTGuiTheme STANDARD_BLUE = GTGuiTheme.builder("gregtech:standard_blue")
        .parent(STANDARD)
        .fullColor(Dyes.dyeBlue)
        .build();
    public static final GTGuiTheme STANDARD_PURPLE = GTGuiTheme.builder("gregtech:standard_purple")
        .parent(STANDARD)
        .fullColor(Dyes.dyePurple)
        .build();
    public static final GTGuiTheme STANDARD_CYAN = GTGuiTheme.builder("gregtech:standard_cyan")
        .parent(STANDARD)
        .fullColor(Dyes.dyeCyan)
        .build();
    public static final GTGuiTheme STANDARD_LIGHT_GRAY = GTGuiTheme.builder("gregtech:standard_light_gray")
        .parent(STANDARD)
        .fullColor(Dyes.dyeLightGray)
        .build();
    public static final GTGuiTheme STANDARD_GRAY = GTGuiTheme.builder("gregtech:standard_standard_gray")
        .parent(STANDARD)
        .fullColor(Dyes.dyeGray)
        .build();
    public static final GTGuiTheme STANDARD_PINK = GTGuiTheme.builder("gregtech:standard_pink")
        .parent(STANDARD)
        .fullColor(Dyes.dyePink)
        .build();
    public static final GTGuiTheme STANDARD_LIME = GTGuiTheme.builder("gregtech:standard_lime")
        .parent(STANDARD)
        .fullColor(Dyes.dyeLime)
        .build();
    public static final GTGuiTheme STANDARD_YELLOW = GTGuiTheme.builder("gregtech:standard_yellow")
        .parent(STANDARD)
        .fullColor(Dyes.dyeYellow)
        .build();
    public static final GTGuiTheme STANDARD_LIGHT_BLUE = GTGuiTheme.builder("gregtech:standard_light_blue")
        .parent(STANDARD)
        .fullColor(Dyes.dyeLightBlue)
        .build();
    public static final GTGuiTheme STANDARD_MAGENTA = GTGuiTheme.builder("gregtech:standard_magenta")
        .parent(STANDARD)
        .fullColor(Dyes.dyeMagenta)
        .build();
    public static final GTGuiTheme STANDARD_ORANGE = GTGuiTheme.builder("gregtech:standard_orange")
        .parent(STANDARD)
        .fullColor(Dyes.dyeOrange)
        .build();
    public static final GTGuiTheme STANDARD_WHITE = GTGuiTheme.builder("gregtech:standard_white")
        .parent(STANDARD)
        .fullColor(Dyes.dyeWhite)
        .build();

    public static final GTGuiTheme COVER = GTGuiTheme.builder("gregtech:cover")
        .parent(STANDARD)
        .textColor(0x555555)
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), 0x222222)
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
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP.getFullName(), GTTextureIds.BACKGROUND_POPUP_BRONZE)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE.getFullName(), GTTextureIds.BACKGROUND_TITLE_BRONZE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL.getFullName(),
            GTTextureIds.OVERLAY_SLOT_COAL_BRONZE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST.getFullName(),
            GTTextureIds.OVERLAY_SLOT_DUST_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN.getFullName(), GTTextureIds.OVERLAY_SLOT_IN_BRONZE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT.getFullName(),
            GTTextureIds.OVERLAY_SLOT_INGOT_BRONZE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE.getFullName(),
            GTTextureIds.OVERLAY_SLOT_FURNACE_BRONZE)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT.getFullName(), GTTextureIds.OVERLAY_SLOT_OUT_BRONZE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK.getFullName(),
            GTTextureIds.OVERLAY_SLOT_BLOCK_BRONZE)
        .themedOverlayFluidSlot(GTWidgetThemes.OVERLAY_FLUID_SLOT_IN.getFullName(), GTTextureIds.OVERLAY_SLOT_IN_BRONZE)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT.getFullName(),
            GTTextureIds.SLOT_FLUID_BRONZE,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .progressbar(GTWidgetThemes.PROGRESSBAR_FUEL.getFullName(), GTTextureIds.PROGRESSBAR_FUEL_BRONZE, 14)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_BRONZE,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_BRONZE)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_DISABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_BRONZE)
        .themedTexture(GTWidgetThemes.PICTURE_CANISTER.getFullName(), GTTextureIds.OVERLAY_SLOT_CANISTER_BRONZE)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_GT_LOGO_BRONZE)
        .build();
    public static final GTGuiTheme STEEL = GTGuiTheme.builder("gregtech:steel")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_STEEL)
        .itemSlot(GTTextureIds.SLOT_ITEM_STEEL)
        .fluidSlot(GTTextureIds.SLOT_FLUID_STEEL)
        .color(Dyes.dyeWhite.toInt())
        .textColor(0xfafaff)
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), 0xfafaff)
        .button(GTTextureIds.BUTTON_STEEL)
        .simpleToggleButton(GTTextureIds.BUTTON_STEEL, GTTextureIds.BUTTON_STEEL_PRESSED, Dyes.dyeWhite.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP.getFullName(), GTTextureIds.BACKGROUND_POPUP_STEEL)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE.getFullName(), GTTextureIds.BACKGROUND_TITLE_STEEL)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL.getFullName(),
            GTTextureIds.OVERLAY_SLOT_COAL_STEEL)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST.getFullName(),
            GTTextureIds.OVERLAY_SLOT_DUST_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN.getFullName(), GTTextureIds.OVERLAY_SLOT_IN_STEEL)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT.getFullName(),
            GTTextureIds.OVERLAY_SLOT_INGOT_STEEL)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE.getFullName(),
            GTTextureIds.OVERLAY_SLOT_FURNACE_STEEL)
        .themedOverlayItemSlot(GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT.getFullName(), GTTextureIds.OVERLAY_SLOT_OUT_STEEL)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK.getFullName(),
            GTTextureIds.OVERLAY_SLOT_BLOCK_STEEL)
        .themedOverlayFluidSlot(GTWidgetThemes.OVERLAY_FLUID_SLOT_IN.getFullName(), GTTextureIds.OVERLAY_SLOT_IN_STEEL)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT.getFullName(),
            GTTextureIds.SLOT_FLUID_STEEL,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .progressbar(GTWidgetThemes.PROGRESSBAR_FUEL.getFullName(), GTTextureIds.PROGRESSBAR_FUEL_STEEL, 14)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_STEEL,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_STEEL)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_DISABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_STEEL)
        .themedTexture(GTWidgetThemes.PICTURE_CANISTER.getFullName(), GTTextureIds.OVERLAY_SLOT_CANISTER_STEEL)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_GT_LOGO_STEEL)
        .themedColor(GTWidgetThemes.STEAM_GAUGE_NEEDLE.getFullName(), 0x3d3847)
        .build();
    public static final GTGuiTheme PRIMITIVE = GTGuiTheme.builder("gregtech:primitive")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_PRIMITIVE)
        .itemSlot(GTTextureIds.SLOT_ITEM_PRIMITIVE)
        .fluidSlot(GTTextureIds.SLOT_FLUID_PRIMITIVE)
        .color(Dyes.dyeWhite.toInt())
        .textColor(0xfafaff)
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), 0xfafaff)
        .button(GTTextureIds.BUTTON_PRIMITIVE)
        .simpleToggleButton(GTTextureIds.BUTTON_PRIMITIVE, GTTextureIds.BUTTON_PRIMITIVE_PRESSED, Dyes.dyeWhite.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP.getFullName(), GTTextureIds.BACKGROUND_POPUP_PRIMITIVE)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE.getFullName(), GTTextureIds.BACKGROUND_TITLE_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST.getFullName(),
            GTTextureIds.OVERLAY_SLOT_DUST_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT.getFullName(),
            GTTextureIds.OVERLAY_SLOT_INGOT_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE.getFullName(),
            GTTextureIds.OVERLAY_SLOT_FURNACE_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK.getFullName(),
            GTTextureIds.OVERLAY_SLOT_BLOCK_PRIMITIVE)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT.getFullName(),
            GTTextureIds.SLOT_FLUID_PRIMITIVE,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_PRIMITIVE,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_PRIMITIVE)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_DISABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_PRIMITIVE)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_GT_LOGO_PRIMITIVE)
        .build();
    public static final GTGuiTheme TECTECH_STANDARD = GTGuiTheme.builder("tectech:standard")
        .parent(STANDARD)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_TECTECH_LOGO_DARK)
        .themedTexture(GTWidgetThemes.BACKGROUND_TERMINAL.getFullName(), GTTextureIds.BACKGROUND_TERMINAL_TECTECH)
        .build();
    public static final GTGuiTheme EXOFOUNDRY = GTGuiTheme.builder("exofoundry")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_FOUNDRY)
        .itemSlot(GTTextureIds.SLOT_ITEM_FOUNDRY)
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP.getFullName(), GTTextureIds.BACKGROUND_POPUP_FOUNDRY)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE.getFullName(), GTTextureIds.BACKGROUND_TITLE_FOUNDRY)
        .button(GTTextureIds.BUTTON_FOUNDRY)
        .simpleToggleButton(
            GTTextureIds.BUTTON_FOUNDRY,
            GTTextureIds.BUTTON_FOUNDRY_PRESSED,
            Dyes.MACHINE_METAL.toInt())
        .color(Dyes.dyeWhite.toInt())
        .textColor(new Color(0xBDA44A).getRGB())
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), new Color(0xBDA44A).getRGB())
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_LOGO_EXOFOUNDRY)
        .build();

    public static final GTGuiTheme COKE_OVEN = GTGuiTheme.builder("gregtech:primitive")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_COKE_OVEN)
        .itemSlot(GTTextureIds.SLOT_ITEM_PRIMITIVE)
        .fluidSlot(GTTextureIds.SLOT_FLUID_PRIMITIVE)
        .color(Dyes.dyeWhite.toInt())
        .textColor(0xfafaff)
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), 0xfafaff)
        .button(GTTextureIds.BUTTON_PRIMITIVE)
        .simpleToggleButton(GTTextureIds.BUTTON_PRIMITIVE, GTTextureIds.BUTTON_PRIMITIVE_PRESSED, Dyes.dyeWhite.toInt())
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP.getFullName(), GTTextureIds.BACKGROUND_POPUP_PRIMITIVE)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE.getFullName(), GTTextureIds.BACKGROUND_TITLE_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST.getFullName(),
            GTTextureIds.OVERLAY_SLOT_DUST_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT.getFullName(),
            GTTextureIds.OVERLAY_SLOT_INGOT_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE.getFullName(),
            GTTextureIds.OVERLAY_SLOT_FURNACE_PRIMITIVE)
        .themedOverlayItemSlot(
            GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK.getFullName(),
            GTTextureIds.OVERLAY_SLOT_BLOCK_PRIMITIVE)
        .progressbar(
            GTWidgetThemes.PROGRESSBAR_BOILER_HEAT.getFullName(),
            GTTextureIds.SLOT_FLUID_PRIMITIVE,
            GTTextureIds.PROGRESSBAR_BOILER_HEAT,
            54)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_ENABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_NORMAL_PRIMITIVE,
            GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_PRIMITIVE)
        .themedButton(
            GTWidgetThemes.BUTTON_COVER_TAB_DISABLED.getFullName(),
            GTTextureIds.BUTTON_COVER_TAB_DISABLED_PRIMITIVE)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_GT_LOGO_PRIMITIVE)
        .build();

    public static final GTGuiTheme NANOCHIP = GTGuiTheme.builder("nanochip")
        .parent(STANDARD)
        .panel(GTTextureIds.BACKGROUND_NANOCHIP)
        .itemSlot(GTTextureIds.SLOT_ITEM_NANOCHIP)
        .themedTexture(GTWidgetThemes.BACKGROUND_POPUP.getFullName(), GTTextureIds.BACKGROUND_POPUP_NANOCHIP)
        .themedTexture(GTWidgetThemes.BACKGROUND_TITLE.getFullName(), GTTextureIds.BACKGROUND_TITLE_NANOCHIP)
        .button(GTTextureIds.BUTTON_NANOCHIP)
        .simpleToggleButton(
            GTTextureIds.BUTTON_NANOCHIP,
            GTTextureIds.BUTTON_NANOCHIP_PRESSED,
            Dyes.MACHINE_METAL.toInt())
        .color(Dyes.dyeWhite.toInt())
        .textColor(new Color(0xFFDBE0).getRGB())
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), new Color(0xFFDBE0).getRGB())
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_LOGO_NANOCHIP)
        .build();
}
