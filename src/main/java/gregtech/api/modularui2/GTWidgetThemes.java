package gregtech.api.modularui2;

import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.theme.WidgetSlotTheme;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.Widget;

/**
 * Holds and registers all the {@link WidgetTheme}s used in GT. It can be used to customize the appearance of specific
 * widget while keeping overall {@link GTGuiTheme} the same. To use it, simply pass widget theme ID to
 * {@link Widget#widgetTheme(String)}.
 */
public final class GTWidgetThemes {

    public static final String TEXT_TITLE = "textTitle";

    public static final String BACKGROUND_COVER_TAB_NORMAL = "backgroundCoverTabNormal";
    public static final String BACKGROUND_COVER_TAB_HIGHLIGHT = "backgroundCoverTabHighlight";
    public static final String BACKGROUND_COVER_TAB_DISABLED = "backgroundCoverTabDisabled";
    public static final String BACKGROUND_POPUP = "backgroundPopup";
    public static final String BACKGROUND_TITLE = "backgroundTitle";

    public static final String OVERLAY_SLOT_DUST = "overlaySlotDust";
    public static final String OVERLAY_SLOT_INGOT = "overlaySlotIngot";
    public static final String OVERLAY_SLOT_FURNACE = "overlaySlotFurnace";

    public static final String PICTURE_LOGO = "pictureLogo";

    public static void register() {
        IThemeApi themeApi = IThemeApi.get();

        themeApi.registerWidgetTheme(
            TEXT_TITLE,
            new WidgetTheme(null, null, Color.WHITE.main, 0x404040, false),
            WidgetTheme::new);

        registerThemedTexture(BACKGROUND_COVER_TAB_NORMAL);
        registerThemedTexture(BACKGROUND_COVER_TAB_HIGHLIGHT);
        registerThemedTexture(BACKGROUND_COVER_TAB_DISABLED);
        registerThemedTexture(BACKGROUND_POPUP);
        registerThemedTexture(BACKGROUND_TITLE);

        registerThemedItemSlot(OVERLAY_SLOT_DUST);
        registerThemedItemSlot(OVERLAY_SLOT_INGOT);
        registerThemedItemSlot(OVERLAY_SLOT_FURNACE);

        registerThemedTexture(PICTURE_LOGO);
    }

    private static void registerThemedTexture(String textureThemeId) {
        IThemeApi themeApi = IThemeApi.get();
        themeApi.registerWidgetTheme(
            textureThemeId,
            new WidgetSlotTheme(null, Color.withAlpha(Color.WHITE.main, 0x60)),
            WidgetSlotTheme::new);
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
}
