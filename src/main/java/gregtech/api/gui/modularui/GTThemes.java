package gregtech.api.gui.modularui;

import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.theme.ReloadThemeEvent;
import com.cleanroommc.modularui.utils.JsonBuilder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Dyes;

public class GTThemes {

    @SubscribeEvent
    public void initThemes(ReloadThemeEvent.Pre event) {
        for (Dyes dye : Dyes.VALUES) {
            registerColorTheme(dye);
        }
        registerColorTheme(Dyes.MACHINE_METAL);
    }

    private static void registerColorTheme(Dyes dye) {
        JsonBuilder theme = new JsonBuilder();
        theme.add("color", String.valueOf(dye.getRGBInt()));
        IThemeApi.get()
            .registerTheme(getThemeName(dye), theme);
        // todo sGuiTintingEnabled
    }

    public static String getThemeName(Dyes dye) {
        return "gregtech." + dye.mName;
    }
}
