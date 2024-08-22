package gregtech.common.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "pollution",configSubDirectory = "GregTech",filename = "GregTech")
public class ConfigPollution {
    // Pollution: edit GT_Proxy.java to change default values
    @Config.Comment("if true, enables pollution in the game.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mPollution;

    @Config.Comment("Controls the threshold starting from which you can see fog.")
    @Config.DefaultInt(550_000)
    @Config.RequiresMcRestart
    public static int mPollutionSmogLimit;
    @Config.Comment("Controls the threshold starting from which players get poison effect.")
    @Config.DefaultInt(750_000)
    @Config.RequiresMcRestart
    public static int mPollutionPoisonLimit;
    @Config.Comment("Controls the threshold starting from which vegetation starts to be killed.")
    @Config.DefaultInt(1_000_000)
    @Config.RequiresMcRestart
    public static int mPollutionVegetationLimit;
    @Config.Comment("Controls the threshold starting from which if it rains, will turn cobblestone into gravel and gravel into sand.")
    @Config.DefaultInt(2_000_000)
    @Config.RequiresMcRestart
    public static int mPollutionSourRainLimit;
    @Config.Comment("Controls the pollution released by an explosion.")
    @Config.DefaultInt(100_000)
    @Config.RequiresMcRestart
    public static int mPollutionOnExplosion;
    @Config.Comment("Controls the pollution released per second by the bricked blast furnace.")
    @Config.DefaultInt(200)
    @Config.RequiresMcRestart
    public static int mPollutionPrimitveBlastFurnacePerSecond;
    @Config.Comment("Controls the pollution released per second by the charcoal pile igniter.")
    @Config.DefaultInt(100)
    @Config.RequiresMcRestart
    public static int mPollutionCharcoalPitPerSecond;
    @Config.Comment("Controls the pollution released per second by the EBF.")
    @Config.DefaultInt(400)
    @Config.RequiresMcRestart
    public static int mPollutionEBFPerSecond;
    @Config.Comment("Controls the pollution released per second by the large combustion engine.")
    @Config.DefaultInt(480)
    @Config.RequiresMcRestart
    public static int mPollutionLargeCombustionEnginePerSecond;
    @Config.Comment("Controls the pollution released per second by the extreme combustion engine.")
    @Config.DefaultInt(3_840)
    @Config.RequiresMcRestart
    public static int mPollutionExtremeCombustionEnginePerSecond;
    @Config.Comment("Controls the pollution released per second by the implosion compressor.")
    @Config.DefaultInt(10_000)
    @Config.RequiresMcRestart
    public static int mPollutionImplosionCompressorPerSecond;
    @Config.Comment("Controls the pollution released per second by the large bronze boiler.")
    @Config.DefaultInt(1_000)
    @Config.RequiresMcRestart
    public static int mPollutionLargeBronzeBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large steel boiler.")
    @Config.DefaultInt(2_000)
    @Config.RequiresMcRestart
    public static int mPollutionLargeSteelBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large titanium boiler.")
    @Config.DefaultInt(3_000)
    @Config.RequiresMcRestart
    public static int mPollutionLargeTitaniumBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large tungstensteel boiler.")
    @Config.DefaultInt(4_000)
    @Config.RequiresMcRestart
    public static int mPollutionLargeTungstenSteelBoilerPerSecond;
    @Config.Comment("Controls the pollution reduction obtained with each increment of the circuit when throttling large boilers.")
    @Config.DefaultFloat(1.0f / 24.0f) // divided by 24 because there are 24 circuit configs.
    @Config.RequiresMcRestart
    public static int mPollutionReleasedByThrottle;
    @Config.Comment("Controls the pollution released per second by the large gas turbine.")
    @Config.DefaultInt(300)
    @Config.RequiresMcRestart
    public static int mPollutionLargeGasTurbinePerSecond;
    @Config.Comment("Controls the pollution released per second by the multi smelter.")
    @Config.DefaultInt(400)
    @Config.RequiresMcRestart
    public static int mPollutionMultiSmelterPerSecond;
    @Config.Comment("Controls the pollution released per second by the pyrolyse oven.")
    @Config.DefaultInt(300)
    @Config.RequiresMcRestart
    public static int mPollutionPyrolyseOvenPerSecond;
    @Config.Comment("Controls the pollution released per second by the small coil boiler.")
    @Config.DefaultInt(20)
    @Config.RequiresMcRestart
    public static int mPollutionSmallCoalBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the high pressure lava boiler.")
    @Config.DefaultInt(20)
    @Config.RequiresMcRestart
    public static int mPollutionHighPressureLavaBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the high pressure coil boiler.")
    @Config.DefaultInt(30)
    @Config.RequiresMcRestart
    public static int mPollutionHighPressureCoalBoilerPerSecond;

    @Config.Comment("Controls the pollution released per second by the base diesel generator.")
    @Config.DefaultInt(40)
    @Config.RequiresMcRestart
    public static int mPollutionBaseDieselGeneratorPerSecond;

    // Cannot implement it yet due to GTNHLib not having the annotations for it yet
    // public static double[] mPollutionDieselGeneratorReleasedByTier;

    @Config.Comment("Controls the pollution released per second by the base gas turbine.")
    @Config.DefaultInt(40)
    @Config.RequiresMcRestart
    public static int mPollutionBaseGasTurbinePerSecond;

    // Cannot implement it yet due to GTNHLib not having the annotations for it yet
    // public static double[] mPollutionGasTurbineReleasedByTier;
}
