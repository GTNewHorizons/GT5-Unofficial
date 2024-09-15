package bartworks.client.gui;

import static gregtech.api.enums.Mods.BartWorks;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

import bartworks.common.configs.Configuration;

public class ConfigGUI extends SimpleGuiConfig {

    public ConfigGUI(GuiScreen parentScreen) throws ConfigException {
        super(parentScreen, BartWorks.ID, "BartWorks", Configuration.class);
    }
}
