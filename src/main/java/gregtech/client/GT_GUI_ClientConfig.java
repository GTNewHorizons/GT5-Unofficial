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
import gregtech.common.config.gregtech.ConfigDebug;
import gregtech.common.config.gregtech.ConfigFeatures;
import gregtech.common.config.gregtech.ConfigGeneral;
import gregtech.common.config.gregtech.ConfigHarvestLevel;
import gregtech.common.config.gregtech.ConfigMachines;
import gregtech.common.config.gregtech.ConfigOreDropBehavior;
import gregtech.common.config.gregtech.ConfigPollution;
import gregtech.common.config.machinestats.ConfigBronzeSolarBoiler;
import gregtech.common.config.machinestats.ConfigMassFabricator;
import gregtech.common.config.machinestats.ConfigMicrowaveEnergyTransmitter;
import gregtech.common.config.machinestats.ConfigSteelSolarBoiler;
import gregtech.common.config.machinestats.ConfigTeleporter;
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
