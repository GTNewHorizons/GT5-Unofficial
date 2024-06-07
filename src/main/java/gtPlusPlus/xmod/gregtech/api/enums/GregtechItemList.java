package gtPlusPlus.xmod.gregtech.api.enums;

import static gregtech.api.enums.GT_Values.W;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.api.interfaces.GregtechItemContainer;

/**
 * Class containing all non-OreDict Items of GregTech.
 */
public enum GregtechItemList implements GregtechItemContainer {

    /**
     * Items
     */

    // Advanced Hazmat Suit
    Armour_Hazmat_Advanced_Helmet,
    Armour_Hazmat_Advanced_Chest,
    Armour_Hazmat_Advanced_Legs,
    Armour_Hazmat_Advanced_Boots,

    // Gregtech Machine Parts
    Electric_Motor_LuV,
    Electric_Motor_ZPM,
    Electric_Motor_UV,
    Electric_Pump_LuV,
    Electric_Pump_ZPM,
    Electric_Pump_UV,
    Conveyor_Module_LuV,
    Conveyor_Module_ZPM,
    Conveyor_Module_UV,
    Electric_Piston_LuV,
    Electric_Piston_ZPM,
    Electric_Piston_UV,
    Robot_Arm_LuV,
    Robot_Arm_ZPM,
    Robot_Arm_UV,
    Field_Generator_LuV,
    Field_Generator_ZPM,
    Field_Generator_UV,
    Emitter_LuV,
    Emitter_ZPM,
    Emitter_UV,
    Sensor_LuV,
    Sensor_ZPM,
    Sensor_UV,

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

    // Recipe Circuit
    Circuit_BioRecipeSelector,
    Circuit_T3RecipeSelector,

    // Circuits
    Old_Circuit_Primitive,
    Old_Circuit_Basic,
    Old_Circuit_Good,
    Old_Circuit_Advanced,
    Old_Circuit_Data,
    Old_Circuit_Elite,
    Old_Circuit_Master,
    Old_Tool_DataOrb,
    Old_Circuit_Ultimate,
    Old_Tool_DataStick,
    Circuit_IV,
    Circuit_LuV,
    Circuit_ZPM,

    // Circuit Parts
    Circuit_Board_IV,
    Circuit_Board_LuV,
    Circuit_Board_ZPM,
    Circuit_Parts_Crystal_Chip_IV,
    Circuit_Parts_Crystal_Chip_LuV,
    Circuit_Parts_Crystal_Chip_ZPM,
    Circuit_Parts_IV,
    Circuit_Parts_LuV,
    Circuit_Parts_ZPM,
    Circuit_Parts_Wiring_IV,
    Circuit_Parts_Wiring_LuV,
    Circuit_Parts_Wiring_ZPM,

    // Old Style Circuits
    Old_Circuit_Board_Basic,
    Old_Circuit_Board_Advanced,
    Old_Circuit_Board_Elite,
    Old_Circuit_Parts_Crystal_Chip_Elite,
    Old_Circuit_Parts_Crystal_Chip_Master,
    Old_Circuit_Parts_Advanced,
    Old_Circuit_Parts_Wiring_Basic,
    Old_Circuit_Parts_Wiring_Advanced,
    Old_Circuit_Parts_Wiring_Elite,
    Old_Empty_Board_Basic,
    Old_Empty_Board_Elite,

    // Batteries
    Battery_RE_EV_Sodium,
    Battery_RE_EV_Cadmium,
    Battery_RE_EV_Lithium,

    // Shapes for Extruder
    Shape_Extruder_WindmillShaft,
    Shape_Extruder_SmallGear,

    // Cooked Raisin Toast for ImQ009
    Food_Baked_Raisin_Bread,

    // Fluid Cells to regulate flows.
    Fluid_Cell_1L,
    Fluid_Cell_16L,
    Fluid_Cell_36L,
    Fluid_Cell_144L,

    // Debug
    TESTITEM,

    // Larger Volumetric Flasks
    VOLUMETRIC_FLASK_8k,
    VOLUMETRIC_FLASK_32k,

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

    // Upgrade chip for Distillus
    Distillus_Upgrade_Chip,
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

    // Multi-Machine
    Industrial_MultiMachine,
    Casing_Multi_Use,

    // Bedrock Mining Platforms
    /* BedrockMiner_MKI, */
    /* BedrockMiner_MKII, */
    /* BedrockMiner_MKIII, */
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

    // Naq Reactor
    Casing_Naq_Reactor_A,
    Casing_Naq_Reactor_B,
    Casing_Naq_Reactor_C,
    /* Controller_Naq_Reactor, */
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

    // Large Rocket Engine
    Casing_RocketEngine,
    Controller_RocketEngine,

    // Large Semi-Fluid
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
    // Big Steam Compressor
    Controller_SteamCompressorMulti,

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
    Hatch_Input_Naquadah,
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
    GT_MetaTileEntity_ChiselBus_LV,
    GT_MetaTileEntity_ChiselBus_MV,
    GT_MetaTileEntity_ChiselBus_HV,

    // Solidifier Hatches for Industrial Multi Machine
    GT_MetaTileEntity_Solidifier_I,
    GT_MetaTileEntity_Solidifier_II,
    GT_MetaTileEntity_Solidifier_III,
    GT_MetaTileEntity_Solidifier_IV,

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

    // Crate Box
    CrateStorage,

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
    GT4_Electric_Inventory_Manager_LV,
    GT4_Electric_Inventory_Manager_MV,
    GT4_Electric_Inventory_Manager_HV,
    GT4_Electric_Inventory_Manager_EV,
    GT4_Electric_Inventory_Manager_IV,
    GT4_Electric_Inventory_Manager_LuV,
    GT4_Electric_Inventory_Manager_ZPM,
    GT4_Electric_Inventory_Manager_UV,

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
    SimpleDustWasher_ULV,
    SimpleDustWasher_MV,
    SimpleDustWasher_EV,
    SimpleDustWasher_LuV,
    SimpleDustWasher_UV,

    // Solar Tower Reflector
    Solar_Tower_Reflector,

    // Super Tier Chests
    Super_Chest_LV,
    Super_Chest_MV,
    Super_Chest_HV,
    Super_Chest_EV,
    Super_Chest_IV,

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

    // Solar Generators
    GT_Solar_ULV,
    GT_Solar_LV,
    GT_Solar_MV,
    GT_Solar_HV,
    GT_Solar_EV,
    GT_Solar_IV,
    GT_Solar_LuV,
    GT_Solar_ZPM,
    GT_Solar_UV,
    GT_Solar_MAX,

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

    // Semi-Fluid generators
    Generator_SemiFluid_LV,
    Generator_SemiFluid_MV,
    Generator_SemiFluid_HV,
    Generator_SemiFluid_EV,
    Generator_SemiFluid_IV,

    // Advanced Mixer 4x4
    Machine_Advanced_LV_Mixer,
    Machine_Advanced_MV_Mixer,
    Machine_Advanced_HV_Mixer,
    Machine_Advanced_EV_Mixer,
    Machine_Advanced_IV_Mixer,
    Machine_Advanced_LuV_Mixer,
    Machine_Advanced_ZPM_Mixer,
    Machine_Advanced_UV_Mixer,

    // Block that enables uplink to a superconductor network
    SuperConductorInputNode,

    // Heat Pipes
    HeatPipe_Tier_1,
    HeatPipe_Tier_2,
    HeatPipe_Tier_3,

    // Chemical Dehydrators for nuclear fuels
    GT_Dehydrator_MV,
    GT_Dehydrator_HV,
    GT_Dehydrator_EV,
    GT_Dehydrator_IV,
    GT_Dehydrator_LuV,
    GT_Dehydrator_ZPM,

    // Fluid Storage Tanks
    GT_FluidTank_ULV,
    GT_FluidTank_LV,
    GT_FluidTank_MV,
    GT_FluidTank_HV,
    GT_FluidTank_EV,
    GT_FluidTank_IV,
    GT_FluidTank_LuV,
    GT_FluidTank_ZPM,
    GT_FluidTank_UV,
    GT_FluidTank_MAX,

    // GT RTG
    RTG,

    // Chisel Machines
    GT_Chisel_LV,
    GT_Chisel_MV,
    GT_Chisel_HV,

    // Plasma Tank
    /* Plasma_Tank, */

    // ----------------------------------------------------------------------------

    /**
     * Covers
     */

    // Fluid Void Covers
    Cover_Overflow_LV,
    Cover_Overflow_MV,
    Cover_Overflow_HV,
    Cover_Overflow_EV,
    Cover_Overflow_IV,

    // Item Void Covers
    Cover_Overflow_Item_ULV,
    Cover_Overflow_Item_LV,
    Cover_Overflow_Item_MV,
    Cover_Overflow_Item_HV,
    Cover_Overflow_Item_EV,
    Cover_Overflow_Item_IV,

    // ----------------------------------------------------------------------------
    // Additional washers
    SimpleDustWasher_LV,
    SimpleDustWasher_HV,
    SimpleDustWasher_IV,
    SimpleDustWasher_ZPM,

    ;

    public static final GregtechItemList[] DYE_ONLY_ITEMS = { Energy_Buffer_1by1_EV, Energy_Buffer_1by1_EV };
    private ItemStack mStack;
    private boolean mHasNotBeenSet = true;

    @Override
    public GregtechItemList set(final Item aItem) {
        this.mHasNotBeenSet = false;
        if (aItem == null) {
            return this;
        }
        final ItemStack aStack = new ItemStack(aItem, 1, 0);
        this.mStack = GT_Utility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public GregtechItemList set(final ItemStack aStack) {
        this.mHasNotBeenSet = false;
        this.mStack = GT_Utility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public Item getItem() {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(this.mStack)) {
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
    public boolean isStackEqual(final Object aStack) {
        return this.isStackEqual(aStack, false, false);
    }

    @Override
    public boolean isStackEqual(final Object aStack, final boolean aWildcard, final boolean aIgnoreNBT) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return false;
        }
        return GT_Utility
            .areUnificationsEqual((ItemStack) aStack, aWildcard ? this.getWildcard(1) : this.get(1), aIgnoreNBT);
    }

    public static Block getBlockFromStack(Object aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return Blocks.air;
        return Block.getBlockFromItem(((ItemStack) aStack).getItem());
    }

    @Override
    public ItemStack get(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(this.mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmount(aAmount, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getWildcard(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(this.mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmountAndMetaData(aAmount, W, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getUndamaged(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(this.mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmountAndMetaData(aAmount, 0, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getAlmostBroken(final long aAmount, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(this.mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility
            .copyAmountAndMetaData(aAmount, this.mStack.getMaxDamage() - 1, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getWithName(final long aAmount, final String aDisplayName, final Object... aReplacements) {
        final ItemStack rStack = this.get(1, aReplacements);
        if (GT_Utility.isStackInvalid(rStack)) {
            return null;
        }
        rStack.setStackDisplayName(aDisplayName);
        return GT_Utility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithCharge(final long aAmount, final int aEnergy, final Object... aReplacements) {
        final ItemStack rStack = this.get(1, aReplacements);
        if (GT_Utility.isStackInvalid(rStack)) {
            return null;
        }
        GT_ModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
        return GT_Utility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithDamage(final long aAmount, final long aMetaValue, final Object... aReplacements) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        if (GT_Utility.isStackInvalid(this.mStack)) {
            return GT_Utility.copyAmount(aAmount, aReplacements);
        }
        return GT_Utility.copyAmountAndMetaData(aAmount, aMetaValue, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public GregtechItemList registerOre(final Object... aOreNames) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        for (final Object tOreName : aOreNames) {
            GT_OreDictUnificator.registerOre(tOreName, this.get(1));
        }
        return this;
    }

    @Override
    public GregtechItemList registerWildcardAsOre(final Object... aOreNames) {
        if (this.mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        }
        for (final Object tOreName : aOreNames) {
            GT_OreDictUnificator.registerOre(tOreName, this.getWildcard(1));
        }
        return this;
    }
}
