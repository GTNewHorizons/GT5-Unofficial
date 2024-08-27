package gregtech.common.config.client;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "preference", configSubDirectory = "GregTech", filename = "Client")
public class ConfigPreference {

    @Config.Comment("if true, input filter will initially be on when input buses are placed in the world.")
    @Config.DefaultBoolean(false)
    public static boolean inputBusInitialFilter;

    @Config.Comment("if true, allow multistacks on single blocks by default when they are first placed in the world.")
    @Config.DefaultBoolean(false)
    public static boolean singleBlockInitialAllowMultiStack;

    @Config.Comment("if true, input filter will initially be on when machines are placed in the world.")
    @Config.DefaultBoolean(false)
    public static boolean singleBlockInitialFilter;
}
