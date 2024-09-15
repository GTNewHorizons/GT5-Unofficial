package gtPlusPlus.core.gui.config;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;
import gtPlusPlus.core.config.Configuration;
import net.minecraft.client.gui.GuiScreen;

import static gregtech.api.enums.Mods.GTPlusPlus;

public class ConfigGUI extends SimpleGuiConfig {

    public ConfigGUI(GuiScreen parentScreen) throws ConfigException {
        super(parentScreen, GTPlusPlus.ID, "GT++", Configuration.class);
    }
}
