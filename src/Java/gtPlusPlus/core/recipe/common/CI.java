package gtPlusPlus.core.recipe.common;

import gregtech.api.enums.ItemList;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.LOADER_Machine_Components;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class CI {

	//null
	public static ItemStack _NULL = ItemUtils.getSimpleStack(ModItems.AAA_Broken);

	//Circuits
	public static Object circuitPrimitive;
	public static Object circuitTier1;
	public static Object circuitTier2;
	public static Object circuitTier3;
	public static Object circuitTier4;
	public static Object circuitTier5;
	public static Object circuitTier6;
	public static Object circuitTier7;
	public static Object circuitTier8;
	public static Object circuitTier9;

	//Machine Components
	public static ItemStack electricMotor_ULV;
	public static ItemStack electricMotor_LV;
	public static ItemStack electricMotor_MV;
	public static ItemStack electricMotor_HV;
	public static ItemStack electricMotor_EV;
	public static ItemStack electricMotor_IV;
	public static ItemStack electricMotor_LuV;
	public static ItemStack electricMotor_ZPM;
	public static ItemStack electricMotor_UV;
	public static ItemStack electricMotor_MAX;
	public static ItemStack electricPump_ULV;
	public static ItemStack electricPump_LV;
	public static ItemStack electricPump_MV;
	public static ItemStack electricPump_HV;
	public static ItemStack electricPump_EV;
	public static ItemStack electricPump_IV;
	public static ItemStack electricPump_LuV;
	public static ItemStack electricPump_ZPM;
	public static ItemStack electricPump_UV;
	public static ItemStack electricPump_MAX;
	public static ItemStack electricPiston_ULV;
	public static ItemStack electricPiston_LV;
	public static ItemStack electricPiston_MV;
	public static ItemStack electricPiston_HV;
	public static ItemStack electricPiston_EV;
	public static ItemStack electricPiston_IV;
	public static ItemStack electricPiston_LuV;
	public static ItemStack electricPiston_ZPM;
	public static ItemStack electricPiston_UV ;
	public static ItemStack electricPiston_MAX;
	public static ItemStack robotArm_ULV;
	public static ItemStack robotArm_LV;
	public static ItemStack robotArm_MV;
	public static ItemStack robotArm_HV;
	public static ItemStack robotArm_EV;
	public static ItemStack robotArm_IV;
	public static ItemStack robotArm_LuV;
	public static ItemStack robotArm_ZPM;
	public static ItemStack robotArm_UV;
	public static ItemStack robotArm_MAX;
	public static ItemStack conveyorModule_ULV;
	public static ItemStack conveyorModule_LV;
	public static ItemStack conveyorModule_MV;
	public static ItemStack conveyorModule_HV;
	public static ItemStack conveyorModule_EV;
	public static ItemStack conveyorModule_IV;
	public static ItemStack conveyorModule_LuV;
	public static ItemStack conveyorModule_ZPM;
	public static ItemStack conveyorModule_UV;
	public static ItemStack conveyorModule_MAX;
	public static ItemStack emitter_ULV;
	public static ItemStack emitter_LV;
	public static ItemStack emitter_MV;
	public static ItemStack emitter_HV;
	public static ItemStack emitter_EV;
	public static ItemStack emitter_IV;
	public static ItemStack emitter_LuV;
	public static ItemStack emitter_ZPM;
	public static ItemStack emitter_UV;
	public static ItemStack emitter_MAX;
	public static ItemStack fieldGenerator_ULV;
	public static ItemStack fieldGenerator_LV;
	public static ItemStack fieldGenerator_MV;
	public static ItemStack fieldGenerator_HV;
	public static ItemStack fieldGenerator_EV;
	public static ItemStack fieldGenerator_IV;
	public static ItemStack fieldGenerator_LuV;
	public static ItemStack fieldGenerator_ZPM;
	public static ItemStack fieldGenerator_UV;
	public static ItemStack fieldGenerator_MAX;
	public static ItemStack sensor_ULV;
	public static ItemStack sensor_LV;
	public static ItemStack sensor_MV;
	public static ItemStack sensor_HV;
	public static ItemStack sensor_EV;
	public static ItemStack sensor_IV;
	public static ItemStack sensor_LuV;
	public static ItemStack sensor_ZPM;
	public static ItemStack sensor_UV;
	public static ItemStack sensor_MAX;

	public static void Init(){
		//Circuits
		circuitPrimitive = getTieredCircuit(0);
		circuitTier1 = getTieredCircuit(1);
		circuitTier2 = getTieredCircuit(2);
		circuitTier3 = getTieredCircuit(3);
		circuitTier4 = getTieredCircuit(4);
		circuitTier5 = getTieredCircuit(5);
		circuitTier6 = getTieredCircuit(6);
		circuitTier7 = getTieredCircuit(7);
		circuitTier8 = getTieredCircuit(8);
		circuitTier9 = getTieredCircuit(9);

		//Machine Components
		LOADER_Machine_Components.initialise();
	}	

	public static Object getTieredCircuit(int tier){
		if (CORE.configSwitches.enableOldGTcircuits){
			if (tier == 0){
				return GregtechItemList.Old_Circuit_Primitive.get(1);
			}
			else if (tier == 1){
				return GregtechItemList.Old_Circuit_Basic.get(1);				
			}
			else if (tier == 2){
				return GregtechItemList.Old_Circuit_Good.get(1);				
			}
			else if (tier == 3){
				return GregtechItemList.Old_Circuit_Advanced.get(1);				
			}
			else if (tier == 4){
				return GregtechItemList.Old_Circuit_Elite.get(1);				
			}
			else if (tier == 5){
				return GregtechItemList.Old_Circuit_Master.get(1);				
			}
			else if (tier == 6){
				return GregtechItemList.Old_Circuit_Ultimate.get(1);				
			}
			else if (tier == 7){
				return GregtechItemList.Circuit_IV.get(1);				
			}
			else if (tier == 8){
				return GregtechItemList.Circuit_LuV.get(1);				
			}
			else if (tier == 9){
				return GregtechItemList.Circuit_ZPM.get(1);				
			}
		}
		else {
			if (tier == 0){
				return ItemList.Circuit_Primitive.get(1);
			}
			else if (tier == 1){
				return ItemList.Circuit_Basic.get(1);				
			}
			else if (tier == 2){
				return ItemList.Circuit_Good.get(1);				
			}
			else if (tier == 3){
				return ItemList.Circuit_Advanced.get(1);				
			}
			else if (tier == 4){
				return ItemList.Circuit_Elite.get(1);				
			}
			else if (tier == 5){
				return ItemList.Circuit_Master.get(1);				
			}
			else if (tier == 6){
				return ItemList.Circuit_Ultimate.get(1);				
			}
			else if (tier == 7){
				return "circuitSuperconductor";				
			}
			else if (tier == 8){
				return "circuitInfinite";				
			}
			else if (tier == 9){
				return "circuitQuantum";				
			}
		}
		return _NULL;
	}

	public static ItemStack getNumberedCircuit(int Meta){
		return ItemUtils.getGregtechCircuit(Meta);
	}

	

}
