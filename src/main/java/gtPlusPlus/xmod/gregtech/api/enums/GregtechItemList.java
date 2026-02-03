package gtPlusPlus.xmod.gregtech.api.enums;

import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public enum GregtechItemList implements IItemContainer {

    /**
     * Items
     */

    // Advanced Hazmat Suit
    Armour_Hazmat_Advanced_Helmet,
    Armour_Hazmat_Advanced_Chest,
    Armour_Hazmat_Advanced_Legs,
    Armour_Hazmat_Advanced_Boots,

    // Mixed Components
    TransmissionComponent_LV,
    TransmissionComponent_MV,
    TransmissionComponent_HV,
    TransmissionComponent_EV,
    TransmissionComponent_IV,
    TransmissionComponent_LuV,
    TransmissionComponent_ZPM,
    TransmissionComponent_UV,
    TransmissionComponent_UHV,

    // Batteries
    Battery_RE_EV_Sodium,
    Battery_RE_EV_Cadmium,
    Battery_RE_EV_Lithium,

    // Shapes for Extruder
    Shape_Extruder_WindmillShaft,
    Shape_Extruder_SmallGear,

    // Larger Volumetric Flasks
    VOLUMETRIC_FLASK_8k,
    VOLUMETRIC_FLASK_32k,
    KLEIN_BOTTLE,

    // RTG Fuels
    Pellet_RTG_PU238,
    Pellet_RTG_SR90,
    Pellet_RTG_PO210,
    Pellet_RTG_AM241,

    // Computer Cube
    Gregtech_Computer_Cube,

    // Casings for batteries
    Battery_Casing_Gem_1,
    Battery_Casing_Gem_2,
    Battery_Casing_Gem_3,
    Battery_Casing_Gem_4,

    // Custom Batteries
    Battery_Gem_1,
    Battery_Gem_2,
    Battery_Gem_3,
    Battery_Gem_4,

    // Compressed Fusion MK3
    Compressed_Fusion_Reactor,

    // Carbon Materials

    // End Game Laser Engraver Lens
    Laser_Lens_WoodsGlass,
    Laser_Lens_Special,

    // Pellet Mold
    Pellet_Mold,

    // Upgrade chip for Maceration Stack
    Maceration_Upgrade_Chip,

    // Milling Balls
    Milling_Ball_Alumina,
    Milling_Ball_Soapstone,

    // ----------------------------------------------------------------------------

    /**
     * MultiBlocks
     */

    // Tier GT++ Casings
    GTPP_Casing_ULV,
    GTPP_Casing_LV,
    GTPP_Casing_MV,
    GTPP_Casing_HV,
    GTPP_Casing_EV,
    GTPP_Casing_IV,
    GTPP_Casing_LuV,
    GTPP_Casing_ZPM,
    GTPP_Casing_UV,
    GTPP_Casing_UHV,

    // Tiered Energy Cores
    Energy_Core_ULV,
    Energy_Core_LV,
    Energy_Core_MV,
    Energy_Core_HV,
    Energy_Core_EV,
    Energy_Core_IV,
    Energy_Core_LuV,
    Energy_Core_ZPM,
    Energy_Core_UV,
    Energy_Core_UHV,

    // IronBlastFurnace Machine_Bronze_BlastFurnace
    Casing_IronPlatedBricks,

    // Large Centrifuge
    Industrial_Centrifuge,
    Casing_Centrifuge1,

    // Large Alloy Smelter
    Industrial_AlloySmelter,

    // Coke Oven
    Industrial_CokeOven,
    Casing_CokeOven,
    Casing_CokeOven_Coil1,
    Casing_CokeOven_Coil2,

    // Bending Maching // Plate Press // Press
    Industrial_PlatePress,
    Casing_MaterialPress,

    // Matter Fab
    Industrial_MassFab,
    Casing_MatterGen,
    Casing_MatterFab,

    // ABS
    Industrial_AlloyBlastSmelter,
    Casing_Coil_BlastSmelter,
    Casing_BlastSmelter,
    Mega_AlloyBlastSmelter,

    // Quantum Force Transformer
    QuantumForceTransformer,
    Casing_Coil_QuantumForceTransformer,
    NeutronPulseManipulator,
    CosmicFabricManipulator,
    InfinityInfusedManipulator,
    SpaceTimeContinuumRipper,
    NeutronShieldingCore,
    CosmicFabricShieldingCore,
    InfinityInfusedShieldingCore,
    SpaceTimeBendingCore,
    ForceFieldGlass,

    // Industrial Electrolyzer
    Industrial_Electrolyzer,
    Casing_Electrolyzer,

    // Industrial Electromagnetic Separator
    Industrial_Electromagnetic_Separator,

    // Industrial Maceration Stack
    Industrial_MacerationStack,
    Casing_MacerationStack,

    // Industrial Wire Factory
    Industrial_WireFactory,
    Casing_WireFactory,

    // Power sub-station for mass storage. 3 hatches for input and output, whatever voltages you desire.
    PowerSubStation,
    Casing_Vanadium_Redox,
    Casing_Vanadium_Redox_IV,
    Casing_Vanadium_Redox_LuV,
    Casing_Vanadium_Redox_ZPM,
    Casing_Vanadium_Redox_UV,
    Casing_Vanadium_Redox_MAX,
    Casing_Power_SubStation,

    // LFTR
    ThoriumReactor,
    Casing_Reactor_I,
    Casing_Reactor_II,

    // Nuclear Salt Processing Plant
    Nuclear_Salt_Processing_Plant,

    // Multitank
    /* Industrial_MultiTank, */
    Industrial_MultiTankDense,
    Casing_MultitankExterior,

    // Fission Fuel Refinery
    Industrial_FuelRefinery,
    Casing_Refinery_External,
    Casing_Refinery_Structural,
    Casing_Refinery_Internal,

    // Industrial Sifter
    Industrial_Sifter,
    Casing_Sifter,
    Casing_SifterGrate,

    // Large Thermal Centrifuge
    Industrial_ThermalCentrifuge,
    Casing_ThermalCentrifuge,

    // Cyclotron
    COMET_Cyclotron,
    Casing_Cyclotron_Coil,
    Casing_Cyclotron_External,

    // Thermal Boiler
    GT4_Thermal_Boiler,
    Casing_ThermalContainment,

    // Tree Farm
    Industrial_TreeFarm,
    TreeFarmer_Structural,
    Casing_PLACEHOLDER_TreeFarmer,

    // Fish Pond
    Industrial_FishingPond,
    Casing_FishPond,

    // Algae
    AlgaeFarm_Controller,

    // Chemical Plant
    ChemicalPlant_Controller,

    // GT4 autoCrafter
    GT4_Multi_Crafter,
    Casing_Autocrafter,

    // industrial Ore-Washer
    Industrial_WashPlant,
    Casing_WashPlant,

    // Cutting Factory Controller
    Industrial_CuttingFactoryController,
    Casing_CuttingFactoryFrame,

    // Large Extruder
    Industrial_Extruder,
    Casing_Extruder,

    Casing_Multi_Use,

    // Bedrock Mining Platforms
    Casing_BedrockMiner,

    // Large Packager
    Amazon_Warehouse_Controller,
    Casing_AmazonWarehouse,

    // Advanced GT vanilla Multis
    Machine_Adv_BlastFurnace,
    Casing_Adv_BlastFurnace,
    Machine_Adv_ImplosionCompressor,
    Machine_Adv_DistillationTower,

    // Advanced Vacuum Freezer
    Industrial_Cryogenic_Freezer,
    Casing_AdvancedVacuum,

    // FusionTek MK IV
    FusionComputer_UV2,
    Casing_Fusion_External,
    Casing_Fusion_Internal,

    // FusionTech MK V

    FusionComputer_UV3,
    Casing_Fusion_External2,
    Casing_Fusion_Internal2,

    // large mixer
    Industrial_Mixer,

    // Containment Casing
    Casing_Containment,

    // Arc Furnace
    Industrial_Arc_Furnace,
    Casing_Industrial_Arc_Furnace,

    // Solar Tower
    Industrial_Solar_Tower,
    Casing_SolarTower_Structural,
    Casing_SolarTower_SaltContainment,
    Casing_SolarTower_HeatContainment,

    // Larger Turbines
    Large_Steam_Turbine,
    Large_HPSteam_Turbine,
    Large_Gas_Turbine,
    Large_Plasma_Turbine,
    Large_SCSteam_Turbine,
    Casing_Turbine_Shaft,
    Casing_Turbine_LP,
    Casing_Turbine_HP,
    Casing_Turbine_Gas,
    Casing_Turbine_Plasma,
    Casing_Turbine_SC,
    XL_HeatExchanger,
    Casing_XL_HeatExchanger,

    // Large Engine
    Casing_Reinforced_Engine_Casing,

    // Large Vacuum Furnace
    Casing_Vacuum_Furnace,
    Controller_Vacuum_Furnace,

    // Water pump
    WaterPump,

    // Large Rocket Engine
    Casing_RocketEngine,
    Controller_RocketEngine,

    // Large Semifluid
    Controller_LargeSemifluidGenerator,

    // IsaMill
    Controller_IsaMill,
    Casing_IsaMill_Casing,
    Casing_IsaMill_Gearbox,
    Casing_IsaMill_Pipe,

    // Flotation Cell
    Controller_Flotation_Cell,
    Casing_Flotation_Cell,

    // Sparge Tower
    Controller_Sparge_Tower,
    Casing_Sparge_Tower_Exterior,
    Casing_Sparge_Tower_Interior,

    // Elemental Duplicator
    Controller_ElementalDuplicator,
    Casing_ElementalDuplicator,

    // Forge Hammer
    Controller_IndustrialForgeHammer,
    Casing_IndustrialForgeHammer,

    // Molecular Transformer
    Controller_MolecularTransformer,
    Casing_Molecular_Transformer_1,
    Casing_Molecular_Transformer_2,
    Casing_Molecular_Transformer_3,

    // Big Steam Macerator
    Controller_SteamMaceratorMulti,
    // Bit Steam Washer
    Controller_SteamWasherMulti,
    // Big Steam Centrifuge
    Controller_SteamCentrifugeMulti,
    // Bit Steam Forge Hammer
    Controller_SteamForgeHammerMulti,
    // Big Steam Compressor
    Controller_SteamMixerMulti,
    // Big Steam Mixer
    Controller_SteamCompressorMulti,
    // Big Steam Alloy Smelter
    Controller_SteamAlloySmelterMulti,
    // Big Steam Furnace
    Controller_SteamFurnaceMulti,

    // Industrial Rock Breaker
    Controller_IndustrialRockBreaker,

    // Industrial Chisel
    Controller_IndustrialAutoChisel,
    Casing_IndustrialAutoChisel,

    // Industrial Fluid Heater
    Controller_IndustrialFluidHeater,

    // Custom Machine Casings
    Casing_Machine_Custom_1,
    Casing_Machine_Custom_2,
    Casing_Machine_Custom_3,
    Casing_Machine_Custom_4,
    Casing_Machine_Custom_5,
    Casing_Machine_Custom_6,

    // ----------------------------------------------------------------------------

    /**
     * Custom hatches/Busses
     */

    // Buffer Dynamos
    Hatch_Buffer_Dynamo_ULV,
    Hatch_Buffer_Dynamo_LV,
    Hatch_Buffer_Dynamo_MV,
    Hatch_Buffer_Dynamo_HV,
    Hatch_Buffer_Dynamo_EV,
    Hatch_Buffer_Dynamo_IV,
    Hatch_Buffer_Dynamo_LuV,
    Hatch_Buffer_Dynamo_ZPM,
    Hatch_Buffer_Dynamo_UV,
    Hatch_Buffer_Dynamo_MAX,

    // Air Intake hatch
    Hatch_Air_Intake,
    Hatch_Air_Intake_Extreme,
    Hatch_Air_Intake_Atmospheric,

    // Reservoir Hatch
    Hatch_Reservoir,

    // XL Turbine Rotor Hatch
    Hatch_Turbine_Rotor,

    // Standard Turbine Rotor Hatch
    Hatch_Input_TurbineHousing,

    // Milling Ball Bus
    Bus_Milling_Balls,

    // Catalyst Bus
    Bus_Catalysts,

    // Custom Fluid Hatches
    Hatch_Input_Cryotheum,
    Hatch_Input_Pyrotheum,
    Hatch_Input_Steam,

    // Steam Multi Buses
    Hatch_Input_Bus_Steam,
    Hatch_Output_Bus_Steam,

    // Elemental Duplicator Data Orb Bus
    Hatch_Input_Elemental_Duplicator,

    // RTG Hatch

    // Battery hatches for PSS
    Hatch_Input_Battery_MV,
    Hatch_Input_Battery_EV,
    Hatch_Output_Battery_MV,
    Hatch_Output_Battery_EV,

    // Advanced Mufflers
    Hatch_Muffler_Adv_LV,
    Hatch_Muffler_Adv_MV,
    Hatch_Muffler_Adv_HV,
    Hatch_Muffler_Adv_EV,
    Hatch_Muffler_Adv_IV,
    Hatch_Muffler_Adv_LuV,
    Hatch_Muffler_Adv_ZPM,
    Hatch_Muffler_Adv_UV,
    Hatch_Muffler_Adv_MAX,

    // Super Input Busses
    Hatch_SuperBus_Input_LV,
    Hatch_SuperBus_Input_MV,
    Hatch_SuperBus_Input_HV,
    Hatch_SuperBus_Input_EV,
    Hatch_SuperBus_Input_IV,
    Hatch_SuperBus_Input_LuV,
    Hatch_SuperBus_Input_ZPM,
    Hatch_SuperBus_Input_UV,
    Hatch_SuperBus_Input_MAX,

    // Super Output Busses
    Hatch_SuperBus_Output_LV,
    Hatch_SuperBus_Output_MV,
    Hatch_SuperBus_Output_HV,
    Hatch_SuperBus_Output_EV,
    Hatch_SuperBus_Output_IV,
    Hatch_SuperBus_Output_LuV,
    Hatch_SuperBus_Output_ZPM,
    Hatch_SuperBus_Output_UV,
    Hatch_SuperBus_Output_MAX,

    // Chisel Buses for Industrial Chisel
    ChiselBus_LV,
    ChiselBus_MV,
    ChiselBus_HV,

    // Solidifier Hatches for Industrial Multi Machine
    Hatch_Solidifier_I,
    Hatch_Solidifier_II,
    Hatch_Solidifier_III,
    Hatch_Solidifier_IV,

    // Extrusion Hatches for Industrial Extruder
    Hatch_Extrusion_I,
    Hatch_Extrusion_II,
    Hatch_Extrusion_III,
    Hatch_Extrusion_IV,

    // ----------------------------------------------------------------------------

    /**
     * Blocks
     */
    ResonanceChamber_I,
    ResonanceChamber_II,
    ResonanceChamber_III,
    ResonanceChamber_IV,

    Modulator_I,
    Modulator_II,
    Modulator_III,
    Modulator_IV,

    // ----------------------------------------------------------------------------

    /**
     * Single Block Tile Entities
     */

    // Auto TC Research Creator
    Thaumcraft_Researcher,

    // infinite Items
    Infinite_Item_Chest,

    // GT4 automation
    GT4_Electric_Auto_Workbench_LV,
    GT4_Electric_Auto_Workbench_MV,
    GT4_Electric_Auto_Workbench_HV,
    GT4_Electric_Auto_Workbench_EV,
    GT4_Electric_Auto_Workbench_IV,
    GT4_Electric_Auto_Workbench_LuV,
    GT4_Electric_Auto_Workbench_ZPM,
    GT4_Electric_Auto_Workbench_UV,

    // GT4 Crop Harvester
    GT4_Crop_Harvester_LV,
    GT4_Crop_Harvester_MV,
    GT4_Crop_Harvester_HV,
    GT4_Crop_Harvester_EV,
    GT4_Crop_Harvester_IV,
    GT4_Crop_Harvester_LuV,
    GT4_Crop_Harvester_ZPM,
    GT4_Crop_Harvester_UV,

    // Geothermal Engines
    Geothermal_Engine_EV,
    Geothermal_Engine_IV,
    Geothermal_Engine_LuV,

    // Tesseracts
    GT4_Tesseract_Generator,
    GT4_Tesseract_Terminal,

    // Advanced Boilers
    Boiler_Advanced_LV,
    Boiler_Advanced_MV,
    Boiler_Advanced_HV,

    // Fancy Pollution Devices
    Pollution_Detector,
    Pollution_Cleaner_LV,
    Pollution_Cleaner_MV,
    Pollution_Cleaner_HV,
    Pollution_Cleaner_EV,
    Pollution_Cleaner_IV,
    Pollution_Cleaner_LuV,
    Pollution_Cleaner_ZPM,
    Pollution_Cleaner_UV,
    Pollution_Cleaner_MAX,

    // Debug machine
    Pollution_Creator,

    // Basically is an automatic Cauldron
    SimpleDustWasher_LV,
    SimpleDustWasher_MV,
    SimpleDustWasher_HV,
    SimpleDustWasher_EV,
    SimpleDustWasher_IV,
    SimpleDustWasher_LuV,
    SimpleDustWasher_ZPM,
    SimpleDustWasher_UV,

    // Solar Tower Reflector
    Solar_Tower_Reflector,

    // Wireless Chargers
    Charger_LV,
    Charger_MV,
    Charger_HV,
    Charger_EV,
    Charger_IV,
    Charger_LuV,
    Charger_ZPM,
    Charger_UV,
    Charger_UHV,

    // Reactor Processing Unit
    ReactorProcessingUnit_IV,
    ReactorProcessingUnit_ZPM,

    // Cold Trap
    ColdTrap_IV,
    ColdTrap_ZPM,

    // Variable voltage RF convertor
    Energy_Buffer_1by1_ULV,
    Energy_Buffer_1by1_LV,
    Energy_Buffer_1by1_MV,
    Energy_Buffer_1by1_HV,
    Energy_Buffer_1by1_EV,
    Energy_Buffer_1by1_IV,
    Energy_Buffer_1by1_LuV,
    Energy_Buffer_1by1_ZPM,
    Energy_Buffer_1by1_UV,
    Energy_Buffer_1by1_MAX,

    // Rocket Engines
    Rocket_Engine_EV,
    Rocket_Engine_IV,
    Rocket_Engine_LuV,

    // Hi Amp Transformers
    Transformer_HA_LV_ULV,
    Transformer_HA_MV_LV,
    Transformer_HA_HV_MV,
    Transformer_HA_EV_HV,
    Transformer_HA_IV_EV,
    Transformer_HA_LuV_IV,
    Transformer_HA_ZPM_LuV,
    Transformer_HA_UV_ZPM,
    Transformer_HA_MAX_UV,

    // Semifluid generators
    Generator_SemiFluid_LV,
    Generator_SemiFluid_MV,
    Generator_SemiFluid_HV,
    Generator_SemiFluid_EV,
    Generator_SemiFluid_IV,

    // Chemical Dehydrators for nuclear fuels
    GT_Dehydrator_MV,
    GT_Dehydrator_HV,
    GT_Dehydrator_EV,
    GT_Dehydrator_IV,
    GT_Dehydrator_LuV,
    GT_Dehydrator_ZPM,

    // Fluid Storage Tanks
    GTFluidTank_ULV,
    GTFluidTank_LV,
    GTFluidTank_MV,
    GTFluidTank_HV,
    GTFluidTank_EV,
    GTFluidTank_IV,
    GTFluidTank_LuV,
    GTFluidTank_ZPM,
    GTFluidTank_UV,
    GTFluidTank_MAX,

    // GT RTG
    RTG,

    // Chisel Machines
    GT_Chisel_LV,
    GT_Chisel_MV,
    GT_Chisel_HV,

    // ----------------------------------------------------------------------------

    /**
     * Covers
     */

    // Fluid Void Covers
    Cover_Overflow_Valve_LV,
    Cover_Overflow_Valve_MV,
    Cover_Overflow_Valve_HV,
    Cover_Overflow_Valve_EV,
    Cover_Overflow_Valve_IV,

    // ----------------------------------------------------------------------------

    // Redstone Utilities
    RedstoneButtonPanel,
    RedstoneCircuitBlock,
    RedstoneLamp,
    RedstoneStrengthDisplay,
    RedstoneStrengthScale,

    // ----------------------------------------------------------------------------
    // Items previously stored elsewhere, moved here for common reference

    // Catalyst Carriers
    EmptyCatalystCarrier,
    GreenMetalCatalyst,
    RedMetalCatalyst,
    YellowMetalCatalyst,
    BlueMetalCatalyst,
    OrangeMetalCatalyst,
    PurpleMetalCatalyst,
    BrownMetalCatalyst,
    PinkMetalCatalyst,
    FormaldehydeCatalyst,
    SolidAcidCatalyst,
    InfiniteMutationCatalyst,

    PlatinumGroupCatalyst,
    PlasticPolymerCatalyst,
    RubberPolymerCatalyst,
    AdhesionPromoterCatalyst,
    TitaTungstenIndiumCatalyst,
    RadioactivityCatalyst,
    RareEarthGroupCatalyst,
    SimpleNaquadahCatalyst,
    HellishForceCatalyst,
    CrystalColorizationCatalyst,
    AdvancedNaquadahCatalyst,
    RawIntelligenceCatalyst,
    UltimatePlasticCatalyst,
    BiologicalIntelligenceCatalyst,
    TemporalHarmonyCatalyst,
    ParticleAccelerationCatalyst,
    SynchrotronCapableCatalyst,
    AlgagenicGrowthPromoterCatalyst,

    // Algae Items
    Algae,
    AlgaeBiomass,
    GreenAlgaeBiomass,
    BrownAlgaeBiomass,
    GoldenBrownAlgaeBiomass,
    RedAlgaeBiomass,
    CelluloseFiber,
    GoldenBrownCelluloseFiber,
    RedCelluloseFiber,
    Compost,
    WoodPellet,
    WoodBrick,
    CellulosePulp,
    RawBioResin,
    AlginicAcid,
    Alumina, // todo
    AluminiumPellet,
    SodiumAluminate, // todo
    SodiumCarbonate, // todo?
    LithiumChloride, // todo
    CleanAluminiumMix,
    Pinecone,
    CrushedPineMaterials,

    // Generic Chem Items
    SodiumEthoxide,
    SodiumEthylXanthate,
    PotassiumEthylXanthate,
    PotassiumHydroxide,

    // Milled Items
    MilledSphalerite,
    MilledChalcopyrite,
    MilledNickel,
    MilledPlatinum,
    MilledPentlandite,
    MilledRedstone,
    MilledSpessartine,
    MilledGrossular,
    MilledAlmandine,
    MilledPyrope,
    MilledMonazite,
    MilledNetherite,

    // Compressed Stuff
    CactusCharcoal,
    BlockCactusCharcoal,
    CompressedCactusCharcoal,
    DoubleCompressedCactusCharcoal,
    TripleCompressedCactusCharcoal,
    QuadrupleCompressedCactusCharcoal,
    QuintupleCompressedCactusCharcoal,

    CactusCoke,
    BlockCactusCoke,
    CompressedCactusCoke,
    DoubleCompressedCactusCoke,
    TripleCompressedCactusCoke,
    QuadrupleCompressedCactusCoke,
    QuintupleCompressedCactusCoke,

    SugarCharcoal,
    BlockSugarCharcoal,
    CompressedSugarCharcoal,
    DoubleCompressedSugarCharcoal,
    TripleCompressedSugarCharcoal,
    QuadrupleCompressedSugarCharcoal,
    QuintupleCompressedSugarCharcoal,

    SugarCoke,
    BlockSugarCoke,
    CompressedSugarCoke,
    DoubleCompressedSugarCoke,
    TripleCompressedSugarCoke,
    QuadrupleCompressedSugarCoke,
    QuintupleCompressedSugarCoke,

    CompressedObsidian,
    DoubleCompressedObsidian,
    TripleCompressedObsidian,
    QuadrupleCompressedObsidian,
    QuintupleCompressedObsidian,
    InvertedObsidian,

    CompressedGlowstone,
    DoubleCompressedGlowstone,
    TripleCompressedGlowstone,
    QuadrupleCompressedGlowstone,
    QuintupleCompressedGlowstone,

    CompressedNetherrack,
    DoubleCompressedNetherrack,
    TripleCompressedNetherrack,

    // IC2 Rotors
    EnergeticAlloyRotor,
    EnergeticAlloyRotorBlade,
    EnergeticAlloyShaft,

    TungstenSteelRotor,
    TungstenSteelRotorBlade,
    TungstenSteelShaft,

    VibrantAlloyRotor,
    VibrantAlloyRotorBlade,
    VibrantAlloyShaft,

    IridiumRotor,
    IridiumRotorBlade,
    IridiumShaft,

    // Basic Turbines
    BasicIronTurbine,
    BasicBronzeTurbine,
    BasicSteelTurbine,

    // Bee Frames
    HiveFrameAccelerated,
    HiveFrameVoid,
    HiveFrameMutagenic,
    HiveFrameBusy,
    HiveFrameDecay,
    HiveFrameSlow,
    HiveFrameStabilize,
    HiveFrameArborist,

    // Thermal Inspired Items
    BlizzRod,
    BlizzPowder,
    CryotheumDust,
    PyrotheumDust,
    PyrotheumBucket,
    CryotheumBucket,
    EnderBucket,

    // Misc
    CustomCoalCoke,
    BlueprintBase,
    MiningExplosives,
    AlkalusDisk,
    WitherGuard,
    MagicFeather,
    PestKiller,
    FishTrap,

    // ----------------------------------------------------------------------------
    // Dust Items TODO convert to materials some day

    // Tumbaga Mix
    TumbagaMixDust,
    SmallTumbagaMixDust,
    TinyTumbagaMixDust,

    PhthalicAnhydrideDust,
    SmallPhthalicAnhydrideDust,
    TinyPhthalicAnhydrideDust,

    LithiumHydroperoxide,
    SmallLithiumHydroperoxide,
    TinyLithiumHydroperoxide,

    FormaldehydeCatalystDust,
    SmallFormaldehydeCatalystDust,
    TinyFormaldehydeCatalystDust,

    AmmoniumNitrateDust,
    SmallAmmoniumNitrateDust,
    TinyAmmoniumNitrateDust,

    LithiumCarbonateDust,
    SmallLithiumCarbonateDust,
    TinyLithiumCarbonateDust,

    LithiumPeroxideDust,
    SmallLithiumPeroxideDust,
    TinyLithiumPeroxideDust,

    LithiumHydroxideDust,
    SmallLithiumHydroxideDust,
    TinyLithiumHydroxideDust,

    CalciumHydroxideDust,
    SmallCalciumHydroxideDust,
    TinyCalciumHydroxideDust,

    CalciumCarbonateDust,
    SmallCalciumCarbonateDust,
    TinyCalciumCarbonateDust,

    Li2CO3CaOH2Dust,
    SmallLi2CO3CaOH2Dust,
    TinyLi2CO3CaOH2Dust,

    Neptunium238Dust,
    Neptunium239Dust,
    Radium226Dust,
    DecayedRadium226Dust,
    Protactinium233Dust,

    ZirconiumPellet,

    ZrCl4Dust,
    SmallZrCl4Dust,
    TinyZrCl4Dust,

    CookedZrCl4Dust,
    SmallCookedZrCl4Dust,
    TinyCookedZrCl4Dust,

    SimpleHandPump,
    AdvancedHandPump,
    SuperHandPump,
    UltimateHandPump,
    ExpandableHandPump,
    DehydratorCoilWireEV,
    DehydratorCoilWireIV,
    DehydratorCoilWireLuV,
    DehydratorCoilWireZPM,
    DehydratorCoilEV,
    DehydratorCoilIV,
    DehydratorCoilLuV,
    DehydratorCoilZPM,
    PersonalCloakingDevice,
    PersonalHealingDevice,
    Hatch_Input_Debug_Steam,
    SupremePizzaGloves,
    LFTRControlCircuit,

    ChargePack_LV,
    ChargePack_MV,
    ChargePack_HV,
    ChargePack_EV,
    ChargePack_IV,
    ChargePack_LuV,
    ChargePack_ZPM,
    ChargePack_UV,
    ChargePack_UHV,

    HalfCompleteCasing_I,
    HalfCompleteCasing_II,
    HalfCompleteCasing_III,
    HalfCompleteCasing_IV,

    BoilerChassis_Tier0,
    BoilerChassis_Tier1,
    BoilerChassis_Tier2,

    AirFilter_Tier1,
    AirFilter_Tier2,
    LavaFilter,

    BitCoin,
    HandPumpToken_I,
    HandPumpToken_II,
    HandPumpToken_III,
    HandPumpToken_IV,

    RawHumanMeat,
    CookedHumanMeat,
    RawHorseMeat,
    CookedHorseMeat,
    RawWolfMeat,
    CookedWolfMeat,
    RawOcelotMeat,
    CookedOcelotMeat,
    BlazeFlesh,

    ;

    private ItemStack mStack;
    private boolean mHasNotBeenSet = true;

    @Override
    public GregtechItemList set(final Item aItem) {
        this.mHasNotBeenSet = false;
        if (aItem == null) {
            return this;
        }
        final ItemStack aStack = new ItemStack(aItem, 1, 0);
        this.mStack = GTUtility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public GregtechItemList set(final ItemStack aStack) {
        this.mHasNotBeenSet = false;
        this.mStack = GTUtility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public IItemContainer hidden() {
        codechicken.nei.api.API.hideItem(get(1));
        return this;
    }

    @Override
    public Item getItem() {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(this.mStack)) {
            return null;
        }
        return this.mStack.getItem();
    }

    @Override
    public Block getBlock() {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        return getBlockFromStack(this.getItem());
    }

    @Override
    public final boolean hasBeenSet() {
        return !this.mHasNotBeenSet;
    }

    @Override
    public IItemContainer setRender(IItemRenderer aRender) {
        throw new UnsupportedOperationException("Custom renderer not implemented for GT++ items!");
    }

    @Override
    public boolean isStackEqual(final Object aStack) {
        return this.isStackEqual(aStack, false, false);
    }

    @Override
    public boolean isStackEqual(final Object aStack, final boolean aWildcard, final boolean aIgnoreNBT) {
        if (GTUtility.isStackInvalid(aStack)) {
            return false;
        }
        return GTUtility
            .areUnificationsEqual((ItemStack) aStack, aWildcard ? this.getWildcard(1) : this.get(1), aIgnoreNBT);
    }

    public static Block getBlockFromStack(Object aStack) {
        if (GTUtility.isStackInvalid(aStack)) return Blocks.air;
        return Block.getBlockFromItem(((ItemStack) aStack).getItem());
    }

    @Override
    public ItemStack get(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(this.mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmount(aAmount, GTOreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getWildcard(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(this.mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmountAndMetaData(aAmount, WILDCARD, GTOreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getUndamaged(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(this.mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmountAndMetaData(aAmount, 0, GTOreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getAlmostBroken(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(this.mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility
            .copyAmountAndMetaData(aAmount, this.mStack.getMaxDamage() - 1, GTOreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getWithName(final long aAmount, final String aDisplayName, final Object... aReplacements) {
        final ItemStack rStack = this.get(1, aReplacements);
        if (GTUtility.isStackInvalid(rStack)) {
            return null;
        }
        rStack.setStackDisplayName(aDisplayName);
        return GTUtility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithCharge(final long aAmount, final int aEnergy, final Object... aReplacements) {
        final ItemStack rStack = this.get(1, aReplacements);
        if (GTUtility.isStackInvalid(rStack)) {
            return null;
        }
        GTModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
        return GTUtility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithDamage(final long aAmount, final long aMetaValue, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(this.mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmountAndMetaData(aAmount, aMetaValue, GTOreDictUnificator.get(this.mStack));
    }

    @Override
    public GregtechItemList registerOre(final Object... aOreNames) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        for (final Object tOreName : aOreNames) {
            GTOreDictUnificator.registerOre(tOreName, this.get(1));
        }
        return this;
    }

    @Override
    public GregtechItemList registerWildcardAsOre(final Object... aOreNames) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        for (final Object tOreName : aOreNames) {
            GTOreDictUnificator.registerOre(tOreName, this.getWildcard(1));
        }
        return this;
    }
}
