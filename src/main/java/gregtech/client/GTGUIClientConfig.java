package gregtech.client;

import static gregtech.api.enums.Mods.GregTech;

import gregtech.common.config.Client;
import gregtech.common.config.Gregtech;
import gregtech.common.config.MachineStats;
import gregtech.common.config.Worldgen;
import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

public class GTGUIClientConfig extends SimpleGuiConfig {

    public GTGUIClientConfig(GuiScreen parentScreen) throws ConfigException {
        super(
            parentScreen,
            GregTech.ID,
            "GregTech",
            true,
            Client.class,
            Gregtech.class,
            MachineStats.class,
            Worldgen.class);
    }
}
