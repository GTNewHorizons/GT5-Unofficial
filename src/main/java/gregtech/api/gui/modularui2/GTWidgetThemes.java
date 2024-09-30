package gregtech.api.gui.modularui2;

import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.Widget;

/**
 * Holds and registers all the {@link WidgetTheme}s used in GT. It can be used to customize the appearance of specific
 * widget while keeping overall {@link GTGuiTheme} the same. To use it, simply pass widget theme ID to
 * {@link Widget#widgetTheme(String)}.
 */
public final class GTWidgetThemes {

    public static final String TITLE_TEXT = "titleText";

    public static void register() {
        IThemeApi themeApi = IThemeApi.get();
        themeApi.registerWidgetTheme(
            TITLE_TEXT,
            new WidgetTheme(null, null, Color.WHITE.main, 0x202020, false),
            WidgetTheme::new);
    }
}
