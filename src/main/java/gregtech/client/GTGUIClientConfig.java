package gregtech.client;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

import gregtech.common.config.client.Client;

public class GTGUIClientConfig extends SimpleGuiConfig {

    public GTGUIClientConfig(GuiScreen parentScreen) throws ConfigException {
        super(parentScreen, GregTech.ID, "GregTech", true, Client.class);
    }
}
