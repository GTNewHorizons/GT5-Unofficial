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
import net.minecraftforge.fluids.Fluid;

/**
 * Class containing all non-OreDict Items of GregTech.
 */
public enum GregtechItemList implements GregtechItemContainer {

	Energy_Buffer_CREATIVE,

	//Energy Buffers
	Energy_Buffer_1by1_ULV, Energy_Buffer_1by1_LV, Energy_Buffer_1by1_MV, Energy_Buffer_1by1_HV, Energy_Buffer_1by1_EV, Energy_Buffer_1by1_IV, Energy_Buffer_1by1_LuV, Energy_Buffer_1by1_ZPM, Energy_Buffer_1by1_UV, Energy_Buffer_1by1_MAX,

	//Cobble Generators
	Cobble_Generator_ULV, Cobble_Generator_LV, Cobble_Generator_MV, Cobble_Generator_HV, Cobble_Generator_EV, Cobble_Generator_IV, Cobble_Generator_LuV, Cobble_Generator_ZPM, Cobble_Generator_UV, Cobble_Generator_MAX,

	//The max Steam condenser
	Condensor_MAX,

	//Player owned Safes
	GT_Safe_ULV, GT_Safe_LV, GT_Safe_MV, GT_Safe_HV, GT_Safe_EV, GT_Safe_IV, GT_Safe_LuV, GT_Safe_ZPM, GT_Safe_UV, GT_Safe_MAX,

	//Rocket Engines
	Rocket_Engine_EV, Rocket_Engine_IV, Rocket_Engine_LuV,

	//IronBlastFurnace Machine_Bronze_BlastFurnace
	Machine_Iron_BlastFurnace, Casing_IronPlatedBricks,

	//Machine Casings
	Casing_Shielding,

	//Large Centrifuge
	Industrial_Centrifuge, Casing_Centrifuge1,

	//Coke Oven
	Industrial_CokeOven, Casing_CokeOven, Casing_CokeOven_Coil1, Casing_CokeOven_Coil2,

	//Bending Maching // Plate Press // Press
	Casing_MaterialPress, Industrial_PlatePress,

	//Gregtech Machine Parts
	Electric_Motor_LuV, Electric_Motor_ZPM, Electric_Motor_UV, Electric_Motor_MAX,
	Electric_Pump_LuV, Electric_Pump_ZPM, Electric_Pump_UV, Electric_Pump_MAX,
	Conveyor_Module_LuV, Conveyor_Module_ZPM, Conveyor_Module_UV, Conveyor_Module_MAX,
	Electric_Piston_LuV, Electric_Piston_ZPM, Electric_Piston_UV, Electric_Piston_MAX,
	Robot_Arm_LuV, Robot_Arm_ZPM, Robot_Arm_UV, Robot_Arm_MAX,
	Field_Generator_LuV, Field_Generator_ZPM, Field_Generator_UV, Field_Generator_MAX,
	Emitter_LuV, Emitter_ZPM, Emitter_UV, Emitter_MAX,
	Sensor_LuV, Sensor_ZPM, Sensor_UV, Sensor_MAX,
	
	//ULV Components
	Electric_Motor_ULV,
	Electric_Pump_ULV,
	Conveyor_Module_ULV,
	Electric_Piston_ULV,
	Robot_Arm_ULV,
	Field_Generator_ULV,
	Emitter_ULV,
	Sensor_ULV,

	//Circuits
	Old_Circuit_Primitive, Old_Circuit_Basic, Old_Circuit_Good,
	Old_Circuit_Advanced, Old_Circuit_Data, Old_Circuit_Elite,
	Old_Circuit_Master, Old_Tool_DataOrb, Old_Circuit_Ultimate, Old_Tool_DataStick,
	Circuit_IV, Circuit_LuV, Circuit_ZPM,
	//Circuit Parts
	Circuit_Board_IV, Circuit_Board_LuV, Circuit_Board_ZPM,
	Circuit_Parts_Crystal_Chip_IV, Circuit_Parts_Crystal_Chip_LuV, Circuit_Parts_Crystal_Chip_ZPM,
	Circuit_Parts_IV, Circuit_Parts_LuV, Circuit_Parts_ZPM,
	Circuit_Parts_Wiring_IV, Circuit_Parts_Wiring_LuV, Circuit_Parts_Wiring_ZPM,

	//Unused Machine Casings
	Casing_MacerationStack, Casing_MatterGen, Casing_MatterFab, Casing_U7,
	//Unused Machine Coils
	Casing_Coil_U1, Casing_Coil_U2, Casing_Coil_BlastSmelter, Casing_BlastSmelter,

	//Shapes for Extruder
	Shape_Extruder_WindmillShaft,
	Shape_Extruder_SmallGear,

	//Batteries
	Battery_RE_EV_Sodium, Battery_RE_EV_Cadmium, Battery_RE_EV_Lithium,

	//Industrial Electrolyzer
	Casing_Electrolyzer, Industrial_Electrolyzer,

	//Industrial Maceration Stack
	Casing_WireFactory, Industrial_MacerationStack,

	//Industrial Wire Factory
	Industrial_WireFactory,

	Industrial_MassFab,

	//Solar Generators
	GT_Solar_ULV, GT_Solar_LV, GT_Solar_MV,
	GT_Solar_HV, GT_Solar_EV, GT_Solar_IV,
	GT_Solar_LuV, GT_Solar_ZPM, GT_Solar_UV, GT_Solar_MAX,



	//Cooked Raisin Toast for ImQ009
	Food_Baked_Raisin_Bread,

	//For making alloys
	Industrial_AlloyBlastSmelter,

	//Block that enables uplink to a superconductor network
	SuperConductorInputNode,

	//The two tiers of reactor casings
	Casing_Reactor_I, Casing_Reactor_II,

	//Power sub-station for mass storage. 3 hatches for input and output, whatever voltages you desire.
	PowerSubStation,

	//Chemical Dehydrators for nuclear fuels
	GT_Dehydrator_EV, GT_Dehydrator_IV, GT_Dehydrator_LuV, GT_Dehydrator_ZPM,

	//Fluid Storage Tanks
	GT_FluidTank_ULV, GT_FluidTank_LV, GT_FluidTank_MV, GT_FluidTank_HV, GT_FluidTank_EV,
	GT_FluidTank_IV, GT_FluidTank_LuV, GT_FluidTank_ZPM, GT_FluidTank_UV, GT_FluidTank_MAX,

	//Fluid Cells to regulate flows.
	Fluid_Cell_1L, Fluid_Cell_16L, Fluid_Cell_36L, Fluid_Cell_144L,

	//Multitank
	Industrial_MultiTank, Industrial_MultiTankDense, Casing_MultitankExterior,

	//Gt4 Workbenches
	GT4_Workbench_Bronze, GT4_Workbench_Advanced,

	//Geothermal Engines
	Geothermal_Engine_EV, Geothermal_Engine_IV, Geothermal_Engine_LuV,

	//Tesseracts
	GT4_Tesseract_Generator, GT4_Tesseract_Terminal,

	//Casings Tier 2 [17-32]
	//Structural Glass
	Casing_ThermalCentrifuge,
	
	//Fission Fuel Refinery
	Casing_Refinery_External,
	Casing_Refinery_Structural,
	Casing_Refinery_Internal,
	
	//Unknown
	Casing_WashPlant,
	
	//Industrial Sifter
	Casing_Sifter,
	Casing_SifterGrate,
	
	//Power Substation
	Casing_Vanadium_Redox, 
	Casing_Power_SubStation, 
	
	//Cyclotron
	Casing_Cyclotron_Coil,
	Casing_Cyclotron_External,
	
	//Thermal Boiler
	Casing_ThermalContainment, 
	
	
	Casing_Autocrafter,
	Casing_CuttingFactoryFrame, 
	Casing_TeslaTower, 
	Casing_PLACEHOLDER_TreeFarmer,

	//LFTR
	ThoriumReactor,

	//Nuclear Fuel Processor
	Industrial_FuelRefinery,

	//Tree Farm
	Industrial_TreeFarm, TreeFarmer_Structural,
	
	//Sifter
	Industrial_Sifter, 
	
	//Advanced Boilers
	Boiler_Advanced_LV, Boiler_Advanced_MV, Boiler_Advanced_HV, 
	
	//Fancy Pollution Devices
	Pollution_Detector, 
	Pollution_Cleaner_ULV, Pollution_Cleaner_LV, Pollution_Cleaner_MV, Pollution_Cleaner_HV, Pollution_Cleaner_EV,
	Pollution_Cleaner_IV, Pollution_Cleaner_LuV, Pollution_Cleaner_ZPM, Pollution_Cleaner_UV, Pollution_Cleaner_MAX, 
	
	//Debug machine
	Pollution_Creator, 
	
	//Basically is an automatic Cauldron
	SimpleDustWasher, 
	
	//Old Style Circuits
	Old_Circuit_Board_Basic, Old_Circuit_Board_Advanced, Old_Circuit_Board_Elite,
	Old_Circuit_Parts_Crystal_Chip_Elite, Old_Circuit_Parts_Crystal_Chip_Master, Old_Circuit_Parts_Advanced,
	Old_Circuit_Parts_Wiring_Basic, Old_Circuit_Parts_Wiring_Advanced, Old_Circuit_Parts_Wiring_Elite,
	Old_Empty_Board_Basic, Old_Empty_Board_Elite, 
	
	
	//Debug
	TESTITEM, 
	
	//Tick Accelerators from GTNH	
	AcceleratorLV, AcceleratorMV, AcceleratorHV, AcceleratorEV,
	AcceleratorIV, AcceleratorLuV, AcceleratorZPM, AcceleratorUV, 
	
	//GT RTG
	RTG,
	Pellet_RTG_PU238, Pellet_RTG_SR90, Pellet_RTG_PO210, Pellet_RTG_AM241, 
	
	//Computer Cube
	Gregtech_Computer_Cube, 
	
	//Cyclotron
	COMET_Cyclotron, 
	
	//GT4 Multiblocks
	GT4_Thermal_Boiler, GT4_Multi_Crafter, 
	
	//GT4 Shelves
	GT4_Shelf, GT4_Shelf_Iron, GT4_Shelf_FileCabinet, GT4_Shelf_Desk, GT4_Shelf_Compartment, 
	
	
	//Hi Amp Transformers
	Transformer_HA_LV_ULV, Transformer_HA_MV_LV, Transformer_HA_HV_MV,
	Transformer_HA_EV_HV, Transformer_HA_IV_EV, Transformer_HA_LuV_IV,
	Transformer_HA_ZPM_LuV, Transformer_HA_UV_ZPM, Transformer_HA_MAX_UV, 
	
	//Large Thermal Centrifuge
	Industrial_ThermalCentrifuge,
	
	//industrial Ore-Washer
	Industrial_WashPlant, 
	
	//Semi-Fluid generators
	Generator_SemiFluid_LV, Generator_SemiFluid_MV, Generator_SemiFluid_HV, 
	
	//Advanced Mixer 4x4
	Machine_Advanced_LV_Mixer, Machine_Advanced_MV_Mixer, Machine_Advanced_HV_Mixer, 
	Machine_Advanced_EV_Mixer, Machine_Advanced_IV_Mixer, Machine_Advanced_LuV_Mixer, 
	Machine_Advanced_ZPM_Mixer, Machine_Advanced_UV_Mixer,
	
	//Custom hatches
	Hatch_Input_Battery_MV, Hatch_Input_Battery_EV, 
	Hatch_Output_Battery_MV, Hatch_Output_Battery_EV, 
	
	//Wireless Chargers
	Charger_LV,	Charger_MV, Charger_HV,
	Charger_EV,	Charger_IV,	Charger_LuV, 
	Charger_ZPM, Charger_UV, Charger_MAX, 
	
	//Generator Array
	Generator_Array_Controller, 
	
	//Cutting Factory Controller
	Industrial_CuttingFactoryController, 
	
	//Tiny Fusion
	Miniature_Fusion, 
	
	//Component Makers
	Machine_LV_Component_Maker, Machine_MV_Component_Maker, Machine_HV_Component_Maker, 
	Machine_EV_Component_Maker, Machine_IV_Component_Maker, 
	
	//Tesla Tower
	TelsaTower, 
	
	Battery_Gem_1, Battery_Gem_2, Battery_Gem_3, 
	
	//Super Tier Chests
	Super_Chest_LV, Super_Chest_MV, Super_Chest_HV, Super_Chest_EV, Super_Chest_IV, 
	
	//Fish Pond
	Casing_FishPond, Industrial_FishingPond, 
	
	//Chunkloader
	GT_Chunkloader_HV, GT_Chunkloader_EV, GT_Chunkloader_IV, 
	
	//Large Extruder
	Industrial_Extruder, Casing_Extruder, 
	
	//Multi-Machine
	Industrial_MultiMachine, Casing_Multi_Use, 
	
	//Bedrock Mining Platforms
	BedrockMiner_MKI, BedrockMiner_MKII, BedrockMiner_MKIII, Casing_BedrockMiner, 
	
	//Buffer Dynamos
	Hatch_Buffer_Dynamo_ULV, Hatch_Buffer_Dynamo_LV, Hatch_Buffer_Dynamo_MV, Hatch_Buffer_Dynamo_HV, Hatch_Buffer_Dynamo_EV, 
	Hatch_Buffer_Dynamo_IV, Hatch_Buffer_Dynamo_LuV, Hatch_Buffer_Dynamo_ZPM, Hatch_Buffer_Dynamo_UV, Hatch_Buffer_Dynamo_MAX, 
	
	Amazon_Warehouse_Controller, Casing_AmazonWarehouse,
	
	
	
	
	;

	public static final GregtechItemList[]
			DYE_ONLY_ITEMS = {
					Energy_Buffer_1by1_EV, Energy_Buffer_1by1_EV };
	private ItemStack mStack;
	private boolean mHasNotBeenSet = true;

	public static Fluid sOilExtraHeavy, sOilHeavy, sOilMedium, sOilLight, sNaturalGas;

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
		return GT_Utility.areUnificationsEqual((ItemStack)aStack, aWildcard?this.getWildcard(1):this.get(1), aIgnoreNBT);
	}
	
	public static Block getBlockFromStack(Object aStack) {
		if (GT_Utility.isStackInvalid(aStack))
			return Blocks.air;
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
		return GT_Utility.copyAmountAndMetaData(aAmount, this.mStack.getMaxDamage()-1, GT_OreDictUnificator.get(this.mStack));
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