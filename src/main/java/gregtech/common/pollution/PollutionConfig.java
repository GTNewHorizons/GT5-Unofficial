package gregtech.common.pollution;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

// needs to be loaded early from the coremod because
// it decides to load some mixins or not
@Config(modid = Mods.ModIDs.GREG_TECH, category = "Pollution", configSubDirectory = "GregTech", filename = "Pollution")
public class PollutionConfig {

    // override name to be at the top of the cfg file
    @Config.Name("Activate Pollution")
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
    @Config.DefaultInt(200)
    @Config.RequiresMcRestart
    public static int pollutionLargeBronzeBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large steel boiler.")
    @Config.DefaultInt(400)
    @Config.RequiresMcRestart
    public static int pollutionLargeSteelBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large titanium boiler.")
    @Config.DefaultInt(800)
    @Config.RequiresMcRestart
    public static int pollutionLargeTitaniumBoilerPerSecond;
    @Config.Comment("Controls the pollution released per second by the large tungstensteel boiler.")
    @Config.DefaultInt(1_600)
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
    @Config.DefaultDoubleList({ 0.1, 1.0, 0.9, 0.8, 0.7, 0.6 })
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
    @Config.DefaultFloat(333.34f)
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
    @Config.DefaultInt(10000)
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
    @Config.DefaultInt(10)
    public static int cokeOvenPollutionAmount;

    @Config.Comment("Pollution Amount for RC Firebox")
    @Config.DefaultInt(20)
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

    // gt++
    @Config.Comment("pollution rate in gibbl/s for the Amazon warehousing depot")
    @Config.DefaultInt(40)
    public static int pollutionPerSecondMultiPackager;
    @Config.Comment("pollution rate in gibbl/s for the Alloy blast smelter")
    @Config.DefaultInt(300)
    public static int pollutionPerSecondMultiIndustrialAlloySmelter;
    @Config.Comment("pollution rate in gibbl/s for the High current arc furnace")
    @Config.DefaultInt(2_400)
    public static int pollutionPerSecondMultiIndustrialArcFurnace;
    @Config.Comment("pollution rate in gibbl/s for the Industrial centrifuge")
    @Config.DefaultInt(300)
    public static int pollutionPerSecondMultiIndustrialCentrifuge;
    @Config.Comment("pollution rate in gibbl/s for the Industrial coke oven")
    @Config.DefaultInt(80)
    public static int pollutionPerSecondMultiIndustrialCokeOven;
    @Config.Comment("pollution rate in gibbl/s for the Cutting factory")
    @Config.DefaultInt(160)
    public static int pollutionPerSecondMultiIndustrialCuttingMachine;
    @Config.Comment("pollution rate in gibbl/s for the Utupu-Tanuri")
    @Config.DefaultInt(500)
    public static int pollutionPerSecondMultiIndustrialDehydrator;
    @Config.Comment("pollution rate in gibbl/s for the Industrial electrolyzer")
    @Config.DefaultInt(300)
    public static int pollutionPerSecondMultiIndustrialElectrolyzer;
    @Config.Comment("pollution rate in gibbl/s for the Industrial extrusion machine")
    @Config.DefaultInt(1_000)
    public static int pollutionPerSecondMultiIndustrialExtruder;
    @Config.Comment("pollution rate in gibbl/s for the Maceration stack")
    @Config.DefaultInt(400)
    public static int pollutionPerSecondMultiIndustrialMacerator;
    @Config.Comment("pollution rate in gibbl/s for the Industrial mixing machine")
    @Config.DefaultInt(800)
    public static int pollutionPerSecondMultiIndustrialMixer;
    @Config.Comment("pollution rate in gibbl/s for the Industrial material press in forming mode")
    @Config.DefaultInt(240)
    public static int pollutionPerSecondMultiIndustrialPlatePress_ModeForming;
    @Config.Comment("pollution rate in gibbl/s for the Industrial material press in bending mode")
    @Config.DefaultInt(480)
    public static int pollutionPerSecondMultiIndustrialPlatePress_ModeBending;
    @Config.Comment("pollution rate in gibbl/s for the Industrial Forge Hammer")
    @Config.DefaultInt(250)
    public static int pollutionPerSecondMultiIndustrialForgeHammer;
    @Config.Comment("pollution rate in gibbl/s for the Large Sifter")
    @Config.DefaultInt(40)
    public static int pollutionPerSecondMultiIndustrialSifter;
    @Config.Comment("pollution rate in gibbl/s for the Large thermal refinery")
    @Config.DefaultInt(1_000)
    public static int pollutionPerSecondMultiIndustrialThermalCentrifuge;
    @Config.Comment("pollution rate in gibbl/s for the Industrial fluid heater")
    @Config.DefaultInt(1_000)
    public static int pollutionPerSecondMultiIndustrialFluidHeater;
    @Config.Comment("pollution rate in gibbl/s for the Cryogenic freezer")
    @Config.DefaultInt(500)
    public static int pollutionPerSecondMultiIndustrialVacuumFreezer;
    @Config.Comment("pollution rate in gibbl/s for the Ore washing plant in chemical bath mode")
    @Config.DefaultInt(400)
    public static int pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath;
    @Config.Comment("pollution rate in gibbl/s for the Ore washing plant in ore washer mode")
    @Config.DefaultInt(100)
    public static int pollutionPerSecondMultiIndustrialWashPlant_ModeWasher;
    @Config.Comment("pollution rate in gibbl/s for the Wire factory")
    @Config.DefaultInt(100)
    public static int pollutionPerSecondMultiIndustrialWireMill;
    @Config.Comment("pollution rate in gibbl/s for the IsaMill grinding machine")
    @Config.DefaultInt(1_280)
    public static int pollutionPerSecondMultiIsaMill;
    @Config.Comment("pollution rate in gibbl/s for the Dangote distillus in distillery mode")
    @Config.DefaultInt(240)
    public static int pollutionPerSecondMultiAdvDistillationTower_ModeDistillery;
    @Config.Comment("pollution rate in gibbl/s for the Dangote distillus in distillation tower mode")
    @Config.DefaultInt(480)
    public static int pollutionPerSecondMultiAdvDistillationTower_ModeDT;
    @Config.Comment("pollution rate in gibbl/s for the Volcanus")
    @Config.DefaultInt(500)
    public static int pollutionPerSecondMultiAdvEBF;
    @Config.Comment("pollution rate in gibbl/s for the Density^2")
    @Config.DefaultInt(5_000)
    public static int pollutionPerSecondMultiAdvImplosion;
    @Config.Comment("pollution rate in gibbl/s for the Alloy blast furnace")
    @Config.DefaultInt(200)
    public static int pollutionPerSecondMultiABS;
    @Config.Comment("pollution rate in gibbl/s for the Zuhai - fishing port")
    @Config.DefaultInt(20)
    public static int pollutionPerSecondMultiIndustrialFishingPond;
    // pollutionPerSecondMultiLargeRocketEngine;
    @Config.Comment("pollution rate in gibbl/s for the Large semifluid burner")
    @Config.DefaultInt(1_280)
    public static int pollutionPerSecondMultiLargeSemiFluidGenerator;
    @Config.Comment("pollution rate in gibbl/s for the Matter fabrication CPU")
    @Config.DefaultInt(40)
    public static int pollutionPerSecondMultiMassFabricator;
    @Config.Comment("pollution rate in gibbl/s for the Reactor fuel processing plant")
    @Config.DefaultInt(4_000)
    public static int pollutionPerSecondMultiRefinery;
    @Config.Comment("pollution rate in gibbl/s for the Industrial Rock Breaker")
    @Config.DefaultInt(100)
    public static int pollutionPerSecondMultiIndustrialRockBreaker;
    @Config.Comment("pollution rate in gibbl/s for the Industrial Chisel")
    @Config.DefaultInt(50)
    public static int pollutionPerSecondMultiIndustrialChisel;
    @Config.Comment("pollution rate in gibbl/s for the Tree growth simulator")
    @Config.DefaultInt(100)
    public static int pollutionPerSecondMultiTreeFarm;
    @Config.Comment("pollution rate in gibbl/s for the Flotation cell regulator")
    @Config.DefaultInt(0)
    public static int pollutionPerSecondMultiFrothFlotationCell;
    @Config.Comment("pollution rate in gibbl/s for the Large-Scale auto assembler v1.01")
    @Config.DefaultInt(500)
    public static int pollutionPerSecondMultiAutoCrafter;
    @Config.Comment("pollution rate in gibbl/s for the Nuclear salt processing plant")
    @Config.DefaultInt(500)
    public static int pollutionPerSecondNuclearSaltProcessingPlant;
    @Config.Comment("pollution rate in gibbl/s for the Multiblock Molecular Transformer")
    @Config.DefaultInt(1_000)
    public static int pollutionPerSecondMultiMolecularTransformer;

    @Config.Comment("pollution rate in gibbl/s for the Elemental Duplicator")
    @Config.DefaultInt(1_000)
    public static int pollutionPerSecondElementalDuplicator;

    @Config.Comment("pollution rate in gibbl/s for the Thermal boiler")
    @Config.DefaultInt(700)
    public static int pollutionPerSecondMultiThermalBoiler;
    @Config.Comment("pollution rate in gibbl/s for the Algae farm")
    @Config.DefaultInt(0)
    public static int pollutionPerSecondMultiAlgaePond;
    @Config.Comment("base pollution rate in gibbl/s for the single block semi fluid generators")
    @Config.DefaultInt(40)
    public static int basePollutionPerSecondSemiFluidGenerator;
    @Config.Comment("coefficient applied to the base rate of the single block semi fluid generators based on its tier (first is tier 0 aka ULV)")
    @Config.DefaultDoubleList({ 0.0, 2.0, 4.0, 8.0, 12.0, 16.0 })
    public static double[] pollutionReleasedByTierSemiFluidGenerator;
    @Config.Comment("base pollution rate in gibbl/s for the single block boilers")
    @Config.DefaultInt(35)
    public static int basePollutionPerSecondBoiler;
    @Config.Comment("coefficient applied to the base rate of the single block boilers based on its tier (first is tier 0 aka ULV)")
    @Config.DefaultDoubleList({ 0.0, 1.0, 1.43, 1.86 })
    public static double[] pollutionReleasedByTierBoiler;
    @Config.Comment("minimum base pollution rate in gibbl/s for the single block rocket engines")
    @Config.DefaultInt(250)
    public static int baseMinPollutionPerSecondRocketFuelGenerator;
    @Config.Comment("maximum base pollution rate in gibbl/s for the single block rocket engines")
    @Config.DefaultInt(2_000)
    public static int baseMaxPollutionPerSecondRocketFuelGenerator;
    @Config.Comment("coefficient applied to the base rate of the single block rocket engines based on its tier (first is tier 0 aka ULV)")
    @Config.DefaultDoubleList({ 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0 })
    public static double[] pollutionReleasedByTierRocketFuelGenerator;
    @Config.Comment("base pollution rate in gibbl/s for the geothermal engines")
    @Config.DefaultInt(100)
    public static int basePollutionPerSecondGeothermalGenerator;
    @Config.Comment("coefficient applied to the base rate of the single block geothermal engines based on its tier (first is tier 0 aka ULV)")
    @Config.DefaultDoubleList({ 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0 })
    public static double[] pollutionReleasedByTierGeothermalGenerator;

}
