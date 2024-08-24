package gregtech.client;

import static gregtech.api.enums.Mods.GregTech;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;
import gregtech.common.config.client.ConfigColorModulation;
import gregtech.common.config.client.ConfigInterface;
import gregtech.common.config.client.ConfigPreference;
import gregtech.common.config.client.ConfigRender;
import gregtech.common.config.client.ConfigWaila;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import gregtech.api.GregTech_API;

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
            ConfigWaila.class
        );
    }
}
