package gregtech.api.modularui2;

import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.theme.WidgetSlotTheme;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.Widget;

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
 * <li>Register {@link com.cleanroommc.modularui.drawable.UITexture UITexture}s, typically at
 * {@link GTGuiTextures}. Make sure to register texture ID with
 * {@link com.cleanroommc.modularui.drawable.UITexture.Builder#name(String) builder#name}.</li>
 * <li>Define widget theme ID at this class.</li>
 * <li>Register widget theme at this class.</li>
 * <li>Register relevant information of the widget theme for each theme to use at {@link GTGuiThemes}.</li>
 * </ol>
 */
public final class GTWidgetThemes {

    public static final String TEXT_TITLE = "textTitle";

    public static final String BACKGROUND_POPUP = "backgroundPopup";
    public static final String BACKGROUND_TITLE = "backgroundTitle";
    public static final String BACKGROUND_TERMINAL = "backgroundTerminal";

    public static final String OVERLAY_ITEM_SLOT_DUST = "overlayItemSlotDust";
    public static final String OVERLAY_ITEM_SLOT_INGOT = "overlayItemSlotIngot";
    public static final String OVERLAY_ITEM_SLOT_FURNACE = "overlayItemSlotFurnace";
    public static final String OVERLAY_ITEM_SLOT_IN = "overlayItemSlotIn";
    public static final String OVERLAY_ITEM_SLOT_OUT = "overlayItemSlotOut";
    public static final String OVERLAY_ITEM_SLOT_COAL = "overlayItemSlotCoal";
    public static final String OVERLAY_ITEM_SLOT_BLOCK = "overlayItemSlotBlock";

    public static final String OVERLAY_FLUID_SLOT_IN = "overlayFluidSlotIn";

    public static final String PROGRESSBAR_BOILER_HEAT = "progressbarBoilerHeat";
    public static final String PROGRESSBAR_FUEL = "progressbarFuel";

    public static final String BUTTON_COVER_TAB_ENABLED = "buttonCoverTabEnabled";
    public static final String BUTTON_COVER_TAB_DISABLED = "buttonCoverTabDisabled";

    public static final String PICTURE_CANISTER = "pictureCanister";
    public static final String PICTURE_LOGO = "pictureLogo";

    public static void register() {
        IThemeApi themeApi = IThemeApi.get();

        themeApi.registerWidgetTheme(
            TEXT_TITLE,
            new WidgetTheme(null, null, Color.WHITE.main, 0x404040, false),
            WidgetTheme::new);

        registerThemedTexture(BACKGROUND_POPUP);
        registerThemedTexture(BACKGROUND_TITLE);

        registerThemedItemSlot(OVERLAY_ITEM_SLOT_DUST);
        registerThemedItemSlot(OVERLAY_ITEM_SLOT_INGOT);
        registerThemedItemSlot(OVERLAY_ITEM_SLOT_FURNACE);
        registerThemedItemSlot(OVERLAY_ITEM_SLOT_IN);
        registerThemedItemSlot(OVERLAY_ITEM_SLOT_OUT);
        registerThemedItemSlot(OVERLAY_ITEM_SLOT_COAL);
        registerThemedItemSlot(OVERLAY_ITEM_SLOT_BLOCK);

        registerThemedFluidSlot(OVERLAY_FLUID_SLOT_IN);

        themeApi.registerWidgetTheme(
            PROGRESSBAR_BOILER_HEAT,
            new ProgressbarWidgetTheme(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.PROGRESSBAR_BOILER_HEAT, 54),
            ProgressbarWidgetTheme::new);
        themeApi.registerWidgetTheme(
            PROGRESSBAR_FUEL,
            new ProgressbarWidgetTheme(GTGuiTextures.PROGRESSBAR_FUEL_STANDARD, 14),
            ProgressbarWidgetTheme::new);
        themeApi.registerWidgetTheme(
            BACKGROUND_TERMINAL,
            new WidgetTheme(GTGuiTextures.BACKGROUND_TEXT_FIELD, null, Color.WHITE.main, 0xFAFAFA, false),
            WidgetTheme::new);

        registerThemedButton(BUTTON_COVER_TAB_ENABLED);
        registerThemedButton(BUTTON_COVER_TAB_DISABLED);

        registerThemedTexture(PICTURE_CANISTER);
        registerThemedTexture(PICTURE_LOGO);
    }

    private static void registerThemedTexture(String textureThemeId) {
        IThemeApi themeApi = IThemeApi.get();
        themeApi.registerWidgetTheme(
            textureThemeId,
            new WidgetTheme(null, null, Color.WHITE.main, 0xFF404040, false),
            WidgetTheme::new);
    }

    private static void registerThemedItemSlot(String textureThemeId) {
        IThemeApi themeApi = IThemeApi.get();
        themeApi.registerWidgetTheme(
            textureThemeId,
            new WidgetSlotTheme(GuiTextures.SLOT_ITEM, Color.withAlpha(Color.WHITE.main, 0x60)),
            WidgetSlotTheme::new);
    }

    private static void registerThemedFluidSlot(String textureThemeId) {
        IThemeApi themeApi = IThemeApi.get();
        themeApi.registerWidgetTheme(
            textureThemeId,
            new WidgetSlotTheme(GuiTextures.SLOT_FLUID, Color.withAlpha(Color.WHITE.main, 0x60)),
            WidgetSlotTheme::new);
    }

    private static void registerThemedButton(String textureThemeId) {
        IThemeApi themeApi = IThemeApi.get();
        themeApi.registerWidgetTheme(
            textureThemeId,
            new WidgetTheme(null, null, Color.WHITE.main, 0xFF404040, false),
            WidgetTheme::new);
    }
}
