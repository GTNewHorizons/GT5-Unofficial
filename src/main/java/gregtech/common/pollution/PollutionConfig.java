package gregtech.common.pollution;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

// needs to be loaded early from the coremod because
// it decides to load some mixins or not
@Config(modid = Mods.Names.GREG_TECH, configSubDirectory = "GregTech", filename = "Pollution")
public class PollutionConfig {

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

    // Minecraft
    @Config.Comment("Explosion pollution")
    @Config.DefaultFloat(33.34f)
    public static float explosionPollutionAmount;

    @Config.Comment("Make furnaces Pollute")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean furnacesPollute;

    @Config.Comment("Furnace pollution per second, min 1!")
    @Config.DefaultInt(20)
    public static int furnacePollutionAmount;

    // Galacticraft

    @Config.Comment("Pollution Amount for Rockets")
    @Config.DefaultInt(1000)
    public static int rocketPollutionAmount;

    @Config.Comment("Make rockets Pollute")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean rocketsPollute;

    // Railcraft

    @Config.Comment("Pollution Amount for Advanced Coke Ovens")
    @Config.DefaultInt(80)
    public static int advancedCokeOvenPollutionAmount;

    @Config.Comment("Pollution Amount for Coke Ovens")
    @Config.DefaultInt(3)
    public static int cokeOvenPollutionAmount;

    @Config.Comment("Pollution Amount for RC Firebox")
    @Config.DefaultInt(15)
    public static int fireboxPollutionAmount;

    @Config.Comment("Pollution Amount for hobbyist steam engine")
    @Config.DefaultInt(20)
    public static int hobbyistEnginePollutionAmount;

    @Config.Comment("Make Railcraft Pollute")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean railcraftPollutes;

    @Config.Comment("Pollution Amount for tunnel bore")
    @Config.DefaultInt(2)
    public static int tunnelBorePollutionAmount;

    // bartworks
    @Config.Comment("How much should the Simple Stirling Water Pump produce pollution per second")
    @Config.DefaultInt(5)
    public static int pollutionHeatedWaterPumpSecond;

    @Config.Comment("How much should the MBF produce pollution per tick per ingot. Then it'll be multiplied by the amount of ingots done in parallel")
    @Config.DefaultInt(400)
    public static int basePollutionMBFSecond;

    @Config.Comment("Changes colors of certain blocks based on pollution levels")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean pollutionBlockRecolor;

    @Config.Comment("Double Plant Blocks - Recolor Block List")
    @Config.DefaultStringList({ "net.minecraft.block.BlockDoublePlant:FLOWER", })
    @Config.RequiresMcRestart
    public static String[] renderBlockDoublePlant;

    @Config.Comment("Liquid Blocks - Recolor Block List")
    @Config.DefaultStringList({ "net.minecraft.block.BlockLiquid:LIQUID" })
    @Config.RequiresMcRestart
    public static String[] renderBlockLiquid;

    @Config.Comment("Block Vine - Recolor Block List")
    @Config.DefaultStringList({ "net.minecraft.block.BlockVine:FLOWER", })
    @Config.RequiresMcRestart
    public static String[] renderblockVine;

    @Config.Comment("Crossed Squares - Recolor Block List")
    @Config.DefaultStringList({ "net.minecraft.block.BlockTallGrass:FLOWER", "net.minecraft.block.BlockFlower:FLOWER",
        "biomesoplenty.common.blocks.BlockBOPFlower:FLOWER", "biomesoplenty.common.blocks.BlockBOPFlower2:FLOWER",
        "biomesoplenty.common.blocks.BlockBOPFoliage:FLOWER", })
    @Config.RequiresMcRestart
    public static String[] renderCrossedSquares;

    @Config.Comment("Standard Blocks - Recolor Block List")
    @Config.DefaultStringList({ "net.minecraft.block.BlockGrass:GRASS", "net.minecraft.block.BlockLeavesBase:LEAVES",
        "biomesoplenty.common.blocks.BlockOriginGrass:GRASS", "biomesoplenty.common.blocks.BlockLongGrass:GRASS",
        "biomesoplenty.common.blocks.BlockNewGrass:GRASS", "tconstruct.blocks.slime.SlimeGrass:GRASS",
        "thaumcraft.common.blocks.BlockMagicalLeaves:LEAVES", })
    @Config.RequiresMcRestart
    public static String[] renderStandardBlock;

}
