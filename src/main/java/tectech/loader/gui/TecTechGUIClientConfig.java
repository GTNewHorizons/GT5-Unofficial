package tectech.loader.gui;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;
import net.minecraft.client.gui.GuiScreen;
import tectech.loader.ConfigHandler;

import static gregtech.api.enums.Mods.TecTech;

public class TecTechGUIClientConfig extends SimpleGuiConfig {

    public TecTechGUIClientConfig(GuiScreen parentScreen) throws ConfigException {
        super(
            parentScreen,
            TecTech.ID,
            "TecTech - Tec Technology!",
            false,
            ConfigHandler.class);
    }
}
