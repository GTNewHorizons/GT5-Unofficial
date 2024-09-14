package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_PLUS_PLUS,configSubDirectory = "GTPlusPlus", filename = "GTPlusPlus")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.RequiresMcRestart
public class Configuration {
}
