package miscutil.core.xmod.gregtech.api.enums;

import static gregtech.api.enums.GT_Values.W;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import miscutil.core.xmod.gregtech.api.interfaces.GregtechItemContainer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

/**
 * Class containing all non-OreDict Items of GregTech.
 */
public enum GregtechItemList implements GregtechItemContainer {

	Credit_Copper,
	Credit_Iron,
	Credit_Silver,
	Credit_Gold,
	Credit_Platinum,
	Credit_Osmium,
	Credit_Greg_Copper,
	Credit_Greg_Cupronickel,
	Credit_Greg_Silver,
	Credit_Greg_Gold,
	Credit_Greg_Platinum,
	Credit_Greg_Osmium,
	Credit_Greg_Naquadah,
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
	Industrial_PlatePress, 
	
	//Gregtech Machine Parts
	Electric_Motor_LuV, Electric_Motor_ZPM, Electric_Motor_UV, Electric_Motor_MAX, 
	Electric_Pump_LuV, Electric_Pump_ZPM, Electric_Pump_UV, Electric_Pump_MAX,
	Conveyor_Module_LuV, Conveyor_Module_ZPM, Conveyor_Module_UV, Conveyor_Module_MAX, 
	Electric_Piston_LuV, Electric_Piston_ZPM, Electric_Piston_UV, Electric_Piston_MAX,
	Robot_Arm_LuV, Robot_Arm_ZPM, Robot_Arm_UV, Robot_Arm_MAX, 
	Field_Generator_LuV, Field_Generator_ZPM, Field_Generator_UV, Field_Generator_MAX, 
	Emitter_LuV, Emitter_ZPM, Emitter_UV, Emitter_MAX, 
	Sensor_LuV, Sensor_ZPM, Sensor_UV, Sensor_MAX, 
	
	//Circuits
	Circuit_Primitive, Circuit_Basic, Circuit_Good, Circuit_Advanced,	
	Circuit_Data, Circuit_Elite, Circuit_Master, Tool_DataOrb, Circuit_Ultimate, Tool_DataStick, 
	Circuit_IV, Circuit_LuV, Circuit_ZPM,
	//Circuit Parts
	Circuit_Board_IV, Circuit_Board_LuV, Circuit_Board_ZPM, 
	Circuit_Parts_Crystal_Chip_IV, Circuit_Parts_Crystal_Chip_LuV, Circuit_Parts_Crystal_Chip_ZPM,
	Circuit_Parts_IV, Circuit_Parts_LuV, Circuit_Parts_ZPM,
	Circuit_Parts_Wiring_IV, Circuit_Parts_Wiring_LuV, Circuit_Parts_Wiring_ZPM, 
	
	//Blast Furnace Test
	Machine_Electric_BlastFurnace;

	public static final GregtechItemList[]
			DYE_ONLY_ITEMS = {
		Energy_Buffer_1by1_EV, Energy_Buffer_1by1_EV };
	private ItemStack mStack;
	private boolean mHasNotBeenSet = true;

	public static Fluid sOilExtraHeavy, sOilHeavy, sOilMedium, sOilLight, sNaturalGas;

	@Override
	public GregtechItemList set(Item aItem) {
		mHasNotBeenSet = false;
		if (aItem == null) return this;
		ItemStack aStack = new ItemStack(aItem, 1, 0);
		mStack = GT_Utility.copyAmount(1, aStack);
		return this;
	}

	@Override
	public GregtechItemList set(ItemStack aStack) {
		mHasNotBeenSet = false;
		mStack = GT_Utility.copyAmount(1, aStack);
		return this;
	}

	@Override
	public Item getItem() {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return null;
		return mStack.getItem();
	}

	@Override
	public Block getBlock() {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		return GT_Utility.getBlockFromStack(getItem());
	}

	@Override
	public final boolean hasBeenSet() {
		return !mHasNotBeenSet;
	}

	@Override
	public boolean isStackEqual(Object aStack) {
		return isStackEqual(aStack, false, false);
	}

	@Override
	public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
		if (GT_Utility.isStackInvalid(aStack)) return false;
		return GT_Utility.areUnificationsEqual((ItemStack)aStack, aWildcard?getWildcard(1):get(1), aIgnoreNBT);
	}

	@Override
	public ItemStack get(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmount(aAmount, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getWildcard(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, W, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, 0, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, mStack.getMaxDamage()-1, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements) {
		ItemStack rStack = get(1, aReplacements);
		if (GT_Utility.isStackInvalid(rStack)) return null;
		rStack.setStackDisplayName(aDisplayName);
		return GT_Utility.copyAmount(aAmount, rStack);
	}

	@Override
	public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements) {
		ItemStack rStack = get(1, aReplacements);
		if (GT_Utility.isStackInvalid(rStack)) return null;
		GT_ModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
		return GT_Utility.copyAmount(aAmount, rStack);
	}

	@Override
	public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, aMetaValue, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public GregtechItemList registerOre(Object... aOreNames) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		for (Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, get(1));
		return this;
	}

	@Override
	public GregtechItemList registerWildcardAsOre(Object... aOreNames) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		for (Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, getWildcard(1));
		return this;
	}
}