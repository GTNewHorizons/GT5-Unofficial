package gregtech.common.config.gregtech;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "pollution", configSubDirectory = "GregTech", filename = "GregTech")
public class ConfigPollution {

    @Config.Comment("if true, enables pollution in the game.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean pollution;

    @Config.Comment("Controls the threshold starting from which you can see fog.")
    @Config.DefaultInt(550_000)
    @Config.RequiresMcRestart
    public static int pollutionSmogLimit;
    @Config.Comment("Controls the threshold starting from which players get poison effect.")
    @Config.DefaultInt(750_000)
    @Config.RequiresMcRestart
    public static int pollutionPoisonLimit;
    @Config.Comment("Controls the threshold starting from which vegetation starts to be killed.")
    @Config.DefaultInt(1_000_000)
    @Config.RequiresMcRestart
    public static int pollutionVegetationLimit;
    @Config.Comment("Controls the threshold starting from which if it rains, will turn cobblestone into gravel and gravel into sand.")
    @Config.DefaultInt(2_000_000)
    @Config.RequiresMcRestart
    public static int pollutionSourRainLimit;
    @Config.Comment("Controls the pollution released by an explosion.")
    @Config.DefaultInt(100_000)
    @Config.RequiresMcRestart
    public static int pollutionOnExplosion;
    @Config.Comment("Controls the pollution released per second by the bricked blast furnace.")
    @Config.DefaultInt(200)
    @Config.RequiresMcRestart
    public static int pollutionPrimitveBlastFurnacePerSecond;
    @Config.Comment("Controls the pollution released per second by the charcoal pile igniter.")
    @Config.DefaultInt(100)
    @Config.RequiresMcRestart
    public static int pollutionCharcoalPitPerSecond;
    @Config.Comment("Controls the pollution released per second by the EBF.")
    @Config.DefaultInt(400)
    @Config.RequiresMcRestart
    public static int pollutionEBFPerSecond;
    @Config.Comment("Controls the pollution released per second by the large combustion engine.")
    @Config.DefaultInt(480)
    @Config.RequiresMcRestart
    public static int pollutionLargeCombustionEnginePerSecond;
    @Config.Comment("Controls the pollution released per second by the extreme combustion engine.")
    @Config.DefaultInt(3_840)
    @Config.RequiresMcRestart
    public static int pollutionExtremeCombustionEnginePerSecond;
    @Config.Comment("Controls the pollution released per second by the implosion compressor.")
    @Config.DefaultInt(10_000)
    @Config.RequiresMcRestart
    public static int pollutionImplosionCompressorPerSecond;
    @Config.Comment("Controls the pollution released per second by the large bronze boiler.")
    @Config.DefaultInt(1_000)
    @Config.RequiresMcRestart
    public static int pollutionLargeBronzeBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large steel boiler.")
    @Config.DefaultInt(2_000)
    @Config.RequiresMcRestart
    public static int pollutionLargeSteelBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large titanium boiler.")
    @Config.DefaultInt(3_000)
    @Config.RequiresMcRestart
    public static int pollutionLargeTitaniumBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large tungstensteel boiler.")
    @Config.DefaultInt(4_000)
    @Config.RequiresMcRestart
    public static int pollutionLargeTungstenSteelBoilerPerSecond;
    @Config.Comment("Controls the pollution reduction obtained with each increment of the circuit when throttling large boilers.")
    @Config.DefaultFloat(1.0f / 24.0f) // divided by 24 because there are 24 circuit configs.
    @Config.RequiresMcRestart
    public static float pollutionReleasedByThrottle;
    @Config.Comment("Controls the pollution released per second by the large gas turbine.")
    @Config.DefaultInt(300)
    @Config.RequiresMcRestart
    public static int pollutionLargeGasTurbinePerSecond;
    @Config.Comment("Controls the pollution released per second by the multi smelter.")
    @Config.DefaultInt(400)
    @Config.RequiresMcRestart
    public static int pollutionMultiSmelterPerSecond;
    @Config.Comment("Controls the pollution released per second by the pyrolyse oven.")
    @Config.DefaultInt(300)
    @Config.RequiresMcRestart
    public static int pollutionPyrolyseOvenPerSecond;
    @Config.Comment("Controls the pollution released per second by the small coil boiler.")
    @Config.DefaultInt(20)
    @Config.RequiresMcRestart
    public static int pollutionSmallCoalBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the high pressure lava boiler.")
    @Config.DefaultInt(20)
    @Config.RequiresMcRestart
    public static int pollutionHighPressureLavaBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the high pressure coil boiler.")
    @Config.DefaultInt(30)
    @Config.RequiresMcRestart
    public static int pollutionHighPressureCoalBoilerPerSecond;

    @Config.Comment("Controls the pollution released per second by the base diesel generator.")
    @Config.DefaultInt(40)
    @Config.RequiresMcRestart
    public static int pollutionBaseDieselGeneratorPerSecond;

    // reading double as strings, not perfect, but better than nothing
    @Config.Comment({
        "Pollution released by tier, with the following formula: PollutionBaseDieselGeneratorPerSecond * PollutionDieselGeneratorReleasedByTier[Tier]",
        "The first entry has meaning as it is here to since machine tier with array index: LV is 1, etc." })
    @Config.DefaultDoubleList({ 0.1, 1.0, 0.9, 0.8 })
    @Config.RequiresMcRestart
    public static double[] pollutionDieselGeneratorReleasedByTier;

    @Config.Comment("Controls the pollution released per second by the base gas turbine.")
    @Config.DefaultInt(40)
    @Config.RequiresMcRestart
    public static int pollutionBaseGasTurbinePerSecond;

    // reading double as strings, not perfect, but better than nothing
    @Config.Comment({
        "Pollution released by tier, with the following formula: PollutionBaseGasTurbinePerSecond * PollutionGasTurbineReleasedByTier[Tier]",
        "The first entry has meaning as it is here to since machine tier with array index: LV is 1, etc." })
    @Config.DefaultDoubleList({ 0.1, 1.0, 0.9, 0.8, 0.7, 0.6 })
    @Config.RequiresMcRestart
    public static double[] pollutionGasTurbineReleasedByTier;
}
