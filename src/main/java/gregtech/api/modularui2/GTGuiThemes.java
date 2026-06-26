package gregtech.api.modularui2;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.TieredVariant;

/**
 * Holds all the {@link GTGuiTheme}s registered.
 */
public final class GTGuiThemes {

    public static void init() {
        TIERED_VARIANTS.put(TieredVariant.STANDARD, STANDARD);
    }

    public static final GTGuiTheme STANDARD = GTGuiTheme.builder("gregtech:standard")
        .panel(GTTextureIds.BACKGROUND_STANDARD)
        .itemSlot(GTTextureIds.SLOT_ITEM_STANDARD)
        .fluidSlot(GTTextureIds.SLOT_FLUID_STANDARD)
        .color(Dyes.GUI_METAL.toInt())
        .textColor(0x404040)
        .textField(Dyes.dyeWhite.toInt())
        .customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), 0x404040)
        .customTextColor(GTWidgetThemes.DISPLAY_TEXT_WHITE.getFullName(), 0xFAFAFA)
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
        .themedTexture(GTWidgetThemes.PICTURE_CANISTER.getFullName(), GTTextureIds.OVERLAY_SLOT_CANISTER_DARK)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_GT_LOGO_STANDARD)
        .themedTexture(GTWidgetThemes.PICTURE_ERROR.getFullName(), GTTextureIds.PICTURE_ERROR)
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
    public static final GTGuiTheme STANDARD_GRAY = GTGuiTheme.builder("gregtech:standard_gray")
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

    private static final BiFunction<TieredVariant, GTGuiTheme.Builder, GTGuiTheme.Builder> TIERED_VARIANT_BUILDER_MODIFIER = (
        variant, builder) -> {
        if (variant == TieredVariant.BRONZE || variant == TieredVariant.STEEL) builder
            .themedOverlayItemSlot(
                GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL.getFullName(),
                String.format(GTTextureIds.OVERLAY_SLOT_COAL_STEAM, variant))
            .themedOverlayItemSlot(
                GTWidgetThemes.OVERLAY_ITEM_SLOT_IN.getFullName(),
                String.format(GTTextureIds.OVERLAY_SLOT_IN_STEAM, variant))
            .themedOverlayItemSlot(
                GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT.getFullName(),
                String.format(GTTextureIds.OVERLAY_SLOT_OUT_STEAM, variant))
            .progressbar(
                GTWidgetThemes.PROGRESSBAR_FUEL.getFullName(),
                String.format(GTTextureIds.PROGRESSBAR_FUEL_STEAM, variant),
                14)
            .themedTexture(
                GTWidgetThemes.PICTURE_CANISTER.getFullName(),
                String.format(GTTextureIds.OVERLAY_SLOT_CANISTER_STEAM, variant));

        if (variant == TieredVariant.STEEL || variant == TieredVariant.PRIMITIVE)
            builder.customTextColor(GTWidgetThemes.TEXT_TITLE.getFullName(), 0xfafaff);

        if (variant == TieredVariant.STEEL) builder.textColor(0x404040)
            .themedTexture(GTWidgetThemes.STEAM_GAUGE.getFullName(), GTTextureIds.PICTURE_STEAM_GAUGE_STEEL)
            .themedColor(GTWidgetThemes.STEAM_GAUGE_NEEDLE.getFullName(), 0x3d3847);

        if (variant == TieredVariant.PRIMITIVE) builder.textColor(0xfafaff);

        return builder;
    };

    private static final Function<TieredVariant, GTGuiTheme> SPECIAL_VARIANT_BUILDER_BASE = variant -> TIERED_VARIANT_BUILDER_MODIFIER
        .apply(
            variant,
            GTGuiTheme.builder(String.format("gregtech:%s", variant))
                .parent(STANDARD)
                .panel(String.format(GTTextureIds.BACKGROUND_STEAM, variant))
                .itemSlot(String.format(GTTextureIds.SLOT_ITEM_STEAM, variant))
                .fluidSlot(String.format(GTTextureIds.SLOT_FLUID_STEAM, variant))
                .color(Dyes.dyeWhite.toInt())
                .button(String.format(GTTextureIds.BUTTON_STEAM, variant))
                .simpleToggleButton(
                    String.format(GTTextureIds.BUTTON_STEAM, variant),
                    String.format(GTTextureIds.BUTTON_STEAM_PRESSED, variant),
                    Dyes.dyeWhite.toInt())
                .themedTexture(
                    GTWidgetThemes.BACKGROUND_POPUP.getFullName(),
                    String.format(GTTextureIds.BACKGROUND_POPUP_STEAM, variant))
                .themedTexture(
                    GTWidgetThemes.BACKGROUND_TITLE.getFullName(),
                    String.format(GTTextureIds.BACKGROUND_TITLE_STEAM, variant))
                .themedOverlayItemSlot(
                    GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST.getFullName(),
                    String.format(GTTextureIds.OVERLAY_SLOT_DUST_STEAM, variant))
                .themedOverlayItemSlot(
                    GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT.getFullName(),
                    String.format(GTTextureIds.OVERLAY_SLOT_INGOT_STEAM, variant))
                .themedOverlayItemSlot(
                    GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE.getFullName(),
                    String.format(GTTextureIds.OVERLAY_SLOT_FURNACE_STEAM, variant))
                .themedOverlayItemSlot(
                    GTWidgetThemes.OVERLAY_ITEM_SLOT_BLOCK.getFullName(),
                    String.format(GTTextureIds.OVERLAY_SLOT_BLOCK_STEAM, variant))
                .themedOverlayFluidSlot(
                    GTWidgetThemes.OVERLAY_FLUID_SLOT_IN.getFullName(),
                    String.format(GTTextureIds.OVERLAY_SLOT_IN_STEAM, variant))
                .progressbar(
                    GTWidgetThemes.PROGRESSBAR_BOILER_HEAT.getFullName(),
                    String.format(GTTextureIds.SLOT_FLUID_STEAM, variant),
                    GTTextureIds.PROGRESSBAR_BOILER_HEAT,
                    54)
                .themedButton(
                    GTWidgetThemes.BUTTON_COVER_TAB_ENABLED.getFullName(),
                    String.format(GTTextureIds.BUTTON_COVER_TAB_NORMAL_STEAM, variant),
                    String.format(GTTextureIds.BUTTON_COVER_TAB_HIGHLIGHT_STEAM, variant))
                .themedButton(
                    GTWidgetThemes.BUTTON_COVER_TAB_DISABLED.getFullName(),
                    String.format(GTTextureIds.BUTTON_COVER_TAB_DISABLED_STEAM, variant))
                .themedTexture(
                    GTWidgetThemes.PICTURE_LOGO.getFullName(),
                    String.format(GTTextureIds.PICTURE_GT_LOGO_STEAM, variant))
                .themedTexture(GTWidgetThemes.PICTURE_ERROR.getFullName(), GTTextureIds.PICTURE_ERROR_STEAM))
        .build();

    public static final Map<TieredVariant, GTGuiTheme> TIERED_VARIANTS = Arrays.stream(TieredVariant.special_variants)
        .collect(HashMap::new, (acc, var) -> acc.put(var, SPECIAL_VARIANT_BUILDER_BASE.apply(var)), HashMap::putAll);

    public static final GTGuiTheme TECTECH_STANDARD = GTGuiTheme.builder("tectech:standard")
        .parent(STANDARD)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_TECTECH_LOGO)
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

    public static final GTGuiTheme COKE_OVEN = GTGuiTheme.builder("gregtech:coke_oven")
        .parent(TIERED_VARIANTS.get(TieredVariant.PRIMITIVE))
        .panel(GTTextureIds.BACKGROUND_COKE_OVEN)
        .build();
    public static final GTGuiTheme INTERGALACTIC_STANDARD = GTGuiTheme.builder("inntergalactic:standard")
        .parent(TECTECH_STANDARD)
        .themedTexture(GTWidgetThemes.PICTURE_LOGO.getFullName(), GTTextureIds.PICTURE_INTERGALACTIC_LOGO)
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
