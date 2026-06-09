package gregtech.api.modularui2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.theme.SelectableTheme;
import com.cleanroommc.modularui.theme.SlotTheme;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.theme.WidgetThemeKey;
import com.cleanroommc.modularui.theme.WidgetThemeParser;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.Widget;
import com.google.gson.JsonObject;

import gregtech.common.modularui2.theme.ProgressbarWidgetTheme;

/**
 * Holds and registers all the {@link WidgetTheme}s used in GT. It can be used to customize the appearance of specific
 * widget while keeping the overall {@link GTGuiTheme} the same. The primary use case is for bronze/steel/primitive
 * machines, but it also can be used for other "base" machines to allow individual derivatives to have custom "themed"
 * textures while keeping the GUI layout consistent.
 * <p>
 * To use existing widget theme, simply pass the widget theme ID to {@link Widget#widgetTheme}.
 * <p>
 * To add new widget theme, follow these steps:
 * <ol>
 * <li>Define all the relevant texture IDs, typically at {@link GTTextureIds}.</li>
 * <li>Register {@link com.cleanroommc.modularui.drawable.UITexture UITexture}s, typically at {@link GTGuiTextures}.
 * Make sure to register texture ID with {@link com.cleanroommc.modularui.drawable.UITexture.Builder#name(String)
 * builder#name}.</li>
 * <li>Define widget theme ID at this class.</li>
 * <li>Register widget theme at this class.</li>
 * <li>Register relevant information of the widget theme for each theme to use at {@link GTGuiThemes}.</li>
 * </ol>
 */
public final class GTWidgetThemes {

    private static final IThemeApi themeApi = IThemeApi.get();
    public static WidgetThemeKey<WidgetTheme> TEXT_TITLE = themeApi
        .widgetThemeKeyBuilder("textTitle", WidgetTheme.class)
        .defaultTheme(new WidgetTheme(0, 0, null, Color.WHITE.main, 0x404040, false, 0))
        .defaultHoverTheme(null)
        .register();

    // Use for plain/unlocalized display strings and dynamic values (numbers, status text, mode text)
    public static WidgetThemeKey<WidgetTheme> DISPLAY_TEXT_WHITE = themeApi
        .widgetThemeKeyBuilder("displayTextWhite", WidgetTheme.class)
        .defaultTheme(new WidgetTheme(0, 0, null, Color.WHITE.main, 0xFAFAFA, false, 0))
        .defaultHoverTheme(null)
        .register();
    public static WidgetThemeKey<WidgetTheme> DISPLAY_TEXT_GRAY = themeApi
        .widgetThemeKeyBuilder("displayTextGray", WidgetTheme.class)
        .defaultTheme(new WidgetTheme(0, 0, null, Color.GREY.main, 0x404040, false, 0))
        .defaultHoverTheme(null)
        .register();

    public static WidgetThemeKey<WidgetTheme> BACKGROUND_POPUP = registerThemedTexture("backgroundPopup");
    public static WidgetThemeKey<WidgetTheme> BACKGROUND_TITLE = registerThemedTexture("backgroundTitle");
    public static WidgetThemeKey<WidgetTheme> BACKGROUND_TERMINAL = themeApi
        .widgetThemeKeyBuilder("backgroundTerminal", WidgetTheme.class)
        .defaultTheme(new WidgetTheme(0, 0, GTGuiTextures.BACKGROUND_TEXT_FIELD, Color.WHITE.main, 0xFAFAFA, false, 0))
        .defaultHoverTheme(null)
        .register();

    public static WidgetThemeKey<WidgetTheme> BACKGROUND_REDSTONE_SNIFFER = themeApi
        .widgetThemeKeyBuilder("backgroundRedstoneSniffer", WidgetTheme.class)
        .defaultTheme(
            new WidgetTheme(0, 0, GTGuiTextures.BACKGROUND_REDSTONE_SNIFFER, Color.WHITE.main, 0xFAFAFA, false, 0))
        .defaultHoverTheme(null)
        .register();

    public static WidgetThemeKey<WidgetTheme> BACKGROUND_CHAOS_LOCATOR = themeApi
        .widgetThemeKeyBuilder("backgroundChaosLocator", WidgetTheme.class)
        .defaultTheme(
            new WidgetTheme(0, 0, GTGuiTextures.BACKGROUND_CHAOS_LOCATOR, Color.WHITE.main, 0xFAFAFA, false, 0))
        .defaultHoverTheme(null)
        .register();

    public static WidgetThemeKey<SlotTheme> OVERLAY_ITEM_SLOT_DUST = registerThemedItemSlot("overlayItemSlotDust");
    public static WidgetThemeKey<SlotTheme> OVERLAY_ITEM_SLOT_INGOT = registerThemedItemSlot("overlayItemSlotIngot");
    public static WidgetThemeKey<SlotTheme> OVERLAY_ITEM_SLOT_FURNACE = registerThemedItemSlot(
        "overlayItemSlotFurnace");
    public static WidgetThemeKey<SlotTheme> OVERLAY_ITEM_SLOT_IN = registerThemedItemSlot("overlayItemSlotIn");
    public static WidgetThemeKey<SlotTheme> OVERLAY_ITEM_SLOT_OUT = registerThemedItemSlot("overlayItemSlotOut");
    public static WidgetThemeKey<SlotTheme> OVERLAY_ITEM_SLOT_COAL = registerThemedItemSlot("overlayItemSlotCoal");
    public static WidgetThemeKey<SlotTheme> OVERLAY_ITEM_SLOT_BLOCK = registerThemedItemSlot("overlayItemSlotBlock");

    public static WidgetThemeKey<SlotTheme> OVERLAY_FLUID_SLOT_IN = registerThemedFluidSlot("overlayFluidSlotIn");

    public static WidgetThemeKey<WidgetTheme> PROGRESSBAR_BOILER_HEAT = themeApi
        .widgetThemeKeyBuilder("progressbarBoilerHeat", WidgetTheme.class)
        .defaultTheme(
            new ProgressbarWidgetTheme(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.PROGRESSBAR_BOILER_HEAT, 54))
        .defaultHoverTheme(null)
        .register();
    public static WidgetThemeKey<WidgetTheme> PROGRESSBAR_FUEL = themeApi
        .widgetThemeKeyBuilder("progressbarFuel", WidgetTheme.class)
        .defaultTheme(new ProgressbarWidgetTheme(GTGuiTextures.PROGRESSBAR_FUEL_STANDARD, 14))
        .defaultHoverTheme(null)
        .register();
    public static WidgetThemeKey<WidgetTheme> STEAM_GAUGE = themeApi
        .widgetThemeKeyBuilder("steamGauge", WidgetTheme.class)
        .defaultTheme(new WidgetTheme(0, 0, GTGuiTextures.STEAM_GAUGE_BG, Color.WHITE.main, Color.WHITE.main, false, 0))
        .defaultHoverTheme(null)
        .register();
    public static WidgetThemeKey<WidgetTheme> STEAM_GAUGE_NEEDLE = themeApi
        .widgetThemeKeyBuilder("steamGaugeNeedle", WidgetTheme.class)
        .defaultTheme(new WidgetTheme(0, 0, null, Color.BROWN.main, 0xFF404040, false, 0))
        .defaultHoverTheme(null)
        .parser(noInheritanceParser(WidgetTheme.class))
        .register();

    public static WidgetThemeKey<WidgetTheme> BUTTON_COVER_TAB_ENABLED = registerThemedButton("buttonCoverTabEnabled");
    public static WidgetThemeKey<WidgetTheme> BUTTON_COVER_TAB_DISABLED = registerThemedButton(
        "buttonCoverTabDisabled");

    public static WidgetThemeKey<WidgetTheme> BUTTON_BLACK = themeApi
        .widgetThemeKeyBuilder("buttonBlack", WidgetTheme.class)
        .defaultTheme(new WidgetTheme(0, 0, GuiTextures.MC_BUTTON, 0x333333, 0xFAFAFA, false, 0))
        .defaultHoverTheme(null)
        .register();

    public static WidgetThemeKey<SelectableTheme> TOGGLE_BUTTON_DISABLED = themeApi
        .widgetThemeKeyBuilder("toggleButtonDisabled", SelectableTheme.class)
        .defaultTheme(
            new SelectableTheme(
                18,
                18,
                GTGuiTextures.TOGGLE_BUTTON_STANDARD_DISABLED.getSubArea(0, 0, 1, 0.5f),
                Color.argb(1f, 1f, 1f, 0.5f),
                0xFAFAFA,
                false,
                0,
                GTGuiTextures.TOGGLE_BUTTON_STANDARD_DISABLED.getSubArea(0, 0.5f, 1, 1),
                Color.argb(1f, 1f, 1f, 0.5f),
                0xFAFAFA,
                false,
                0))
        .defaultHoverTheme(null)
        .parser(noInheritanceParser(SelectableTheme.class))
        .register();

    public static WidgetThemeKey<WidgetTheme> BUTTON_DISABLED = themeApi
        .widgetThemeKeyBuilder("buttonDisabled", WidgetTheme.class)
        .defaultTheme(
            new WidgetTheme(
                18,
                18,
                GTGuiTextures.BUTTON_STANDARD_DISABLED,
                Color.argb(1f, 1f, 1f, 0.5f),
                0xFAFAFA,
                false,
                0))
        .defaultHoverTheme(null)
        .parser(noInheritanceParser(WidgetTheme.class))
        .register();

    public static WidgetThemeKey<WidgetTheme> PICTURE_CANISTER = registerThemedTexture("pictureCanister");
    public static WidgetThemeKey<WidgetTheme> PICTURE_LOGO = registerThemedTexture("pictureLogo");

    public static WidgetThemeKey<WidgetTheme> TESLA_TOWER_CHART = themeApi
        .widgetThemeKeyBuilder("teslaTowerChart", WidgetTheme.class)
        .defaultTheme(
            new WidgetTheme(
                0,
                0,
                new Rectangle().color(Color.rgb(100, 30, 80)),
                Color.rgb(55, 255, 55),
                0xFFFAFAFA,
                false,
                0))
        .defaultHoverTheme(null)
        .parser(noInheritanceParser(WidgetTheme.class))
        .register();

    public static WidgetThemeKey<WidgetTheme> TESLA_TOWER_CHART_SPECIAL = themeApi
        .widgetThemeKeyBuilder("teslaTowerChartSpecial", WidgetTheme.class)
        .defaultTheme(
            new WidgetTheme(
                0,
                0,
                new DrawableStack(
                    new Rectangle().color(Color.rgb(100, 30, 80)),
                    GTGuiTextures.BACKGROUND_TESLA_TOWER_CHART),
                Color.rgb(55, 255, 55),
                0xFFFAFAFA,
                false,
                0))
        .defaultHoverTheme(null)
        .parser(noInheritanceParser(WidgetTheme.class))
        .register();

    private static WidgetThemeKey<WidgetTheme> registerThemedTexture(String textureThemeId) {
        return themeApi.widgetThemeKeyBuilder(textureThemeId, WidgetTheme.class)
            .defaultTheme(new WidgetTheme(0, 0, null, Color.WHITE.main, 0xFF404040, false, 0))
            .defaultHoverTheme(null)
            .register();
    }

    private static WidgetThemeKey<SlotTheme> registerThemedItemSlot(String textureThemeId) {
        return themeApi.widgetThemeKeyBuilder(textureThemeId, SlotTheme.class)
            .defaultTheme(new SlotTheme(GuiTextures.SLOT_ITEM, Color.withAlpha(Color.WHITE.main, 0x60)))
            .defaultHoverTheme(null)
            .register();
    }

    private static WidgetThemeKey<SlotTheme> registerThemedFluidSlot(String textureThemeId) {
        return themeApi.widgetThemeKeyBuilder(textureThemeId, SlotTheme.class)
            .defaultTheme(new SlotTheme(GuiTextures.SLOT_FLUID, Color.withAlpha(Color.WHITE.main, 0x60)))
            .defaultHoverTheme(null)
            .register();
    }

    private static WidgetThemeKey<WidgetTheme> registerThemedButton(String textureThemeId) {
        return themeApi.widgetThemeKeyBuilder(textureThemeId, WidgetTheme.class)
            .defaultTheme(new WidgetTheme(0, 0, null, Color.WHITE.main, 0xFF404040, false, 0))
            .defaultHoverTheme(null)
            .register();
    }

    // Needed because by default it will inherit the global color
    private static <T extends WidgetTheme> WidgetThemeParser<T> noInheritanceParser(Class<T> type) {
        try {
            Constructor<T> ctor = type.getConstructor(type, JsonObject.class, JsonObject.class);
            return (parent, json, _) -> {
                try {
                    return ctor.newInstance(parent, json, null);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to instantiate widget theme of type " + type.getSimpleName(), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                String.format(
                    "No constructor with signature '%s(%s parent, JsonObject json, JsonObject fallback)' found",
                    type.getSimpleName(),
                    type.getSimpleName()));
        }
    }
}
