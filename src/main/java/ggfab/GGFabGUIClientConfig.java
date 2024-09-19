package ggfab;

import static gregtech.api.enums.Mods.GGFab;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

public class GGFabGUIClientConfig extends SimpleGuiConfig {

    public GGFabGUIClientConfig(GuiScreen parentScreen) throws ConfigException {
        super(parentScreen, GGFab.ID, GGConstants.MODNAME, false, ConfigurationHandler.class);
    }
}
