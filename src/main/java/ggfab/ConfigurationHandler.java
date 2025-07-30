package ggfab;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.ModIDs.G_G_FAB, filename = "ggfab")
@Config.RequiresMcRestart
public class ConfigurationHandler {

    @Config.Comment("Laser overclock penalty factor. This will incredibly change the game balance. Even a small step from 0.2 to 0.3 can have very significant impact. Tweak with caution!")
    @Config.RangeFloat(min = 0.0f, max = 10.0f)
    public static float laserOCPenaltyFactor = 0.3f;
}
