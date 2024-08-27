package gregtech.client;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

import gregtech.common.config.client.ConfigColorModulation;
import gregtech.common.config.client.ConfigInterface;
import gregtech.common.config.client.ConfigPreference;
import gregtech.common.config.client.ConfigRender;
import gregtech.common.config.client.ConfigWaila;

public class GT_GUI_ClientConfig extends SimpleGuiConfig {

    public GT_GUI_ClientConfig(GuiScreen parentScreen) throws ConfigException {
        super(
            parentScreen,
            GregTech.ID,
            "GregTech",
            ConfigColorModulation.class,
            ConfigInterface.class,
            ConfigPreference.class,
            ConfigRender.class,
            ConfigWaila.class);
    }
}
