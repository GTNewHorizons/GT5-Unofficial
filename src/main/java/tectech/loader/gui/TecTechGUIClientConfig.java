package tectech.loader.gui;

import static gregtech.api.enums.Mods.TecTech;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

import tectech.loader.ConfigHandler;

public class TecTechGUIClientConfig extends SimpleGuiConfig {

    public TecTechGUIClientConfig(GuiScreen parentScreen) throws ConfigException {
        super(parentScreen, TecTech.ID, "TecTech - Tec Technology!", false, ConfigHandler.class);
    }
}
