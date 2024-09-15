package gtPlusPlus.core.gui.config;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.core.config.Configuration;

public class ConfigGUI extends SimpleGuiConfig {

    public ConfigGUI(GuiScreen parentScreen) throws ConfigException {
        super(parentScreen, GTPlusPlus.ID, "GT++", true, Configuration.class, ASMConfiguration.class);
    }
}
