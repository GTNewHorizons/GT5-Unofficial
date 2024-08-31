package gregtech.common.config.machinestats;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "microwave_energy_transmitter",
    configSubDirectory = "GregTech",
    filename = "MachineStats")
public class ConfigMicrowaveEnergyTransmitter {

    @Config.Comment("if true, it has a passive energy loss.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean passiveEnergyUse;

    @Config.Comment("max loss.")
    @Config.DefaultInt(50)
    @Config.RequiresMcRestart
    public static int maxLoss;

    @Config.Comment("max loss distance.")
    @Config.DefaultInt(10_000)
    @Config.RequiresMcRestart
    public static int maxLossDistance;
}
