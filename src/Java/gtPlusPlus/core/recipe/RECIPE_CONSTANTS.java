package gtPlusPlus.core.recipe;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPE_CONSTANTS {

	//Machine Components
	public static ItemStack electricMotor_LuV;
	public static ItemStack electricMotor_ZPM;
	public static ItemStack electricMotor_UV;
	public static ItemStack electricMotor_MAX;
	public static ItemStack electricPump_LuV;
	public static ItemStack electricPump_ZPM;
	public static ItemStack electricPump_UV;
	public static ItemStack electricPump_MAX;
	public static ItemStack electricPiston_LuV;
	public static ItemStack electricPiston_ZPM;
	public static ItemStack electricPiston_UV ;
	public static ItemStack electricPiston_MAX;
	public static ItemStack robotArm_LuV;
	public static ItemStack robotArm_ZPM;
	public static ItemStack robotArm_UV;
	public static ItemStack robotArm_MAX;
	public static ItemStack conveyorModule_LuV;
	public static ItemStack conveyorModule_ZPM;
	public static ItemStack conveyorModule_UV;
	public static ItemStack conveyorModule_MAX;
	public static ItemStack emitter_LuV;
	public static ItemStack emitter_ZPM;
	public static ItemStack emitter_UV;
	public static ItemStack emitter_MAX;
	public static ItemStack fieldGenerator_LuV;
	public static ItemStack fieldGenerator_ZPM;
	public static ItemStack fieldGenerator_UV;
	public static ItemStack fieldGenerator_MAX;
	public static ItemStack sensor_LuV;
	public static ItemStack sensor_ZPM;
	public static ItemStack sensor_UV;
	public static ItemStack sensor_MAX;

	public static void initialise(){

		if (!CORE.GTNH){					
			if(CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				registerGTExperimentalComponents();
			}
			else {
				registerGTStandardComponents();
			}
		}
		else {
			registerGTNHComponents();
		}
	}

	private static void registerGTExperimentalComponents(){
		//Machine Components
		electricMotor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32606, 1);
		electricMotor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32607, 1);
		electricMotor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32608, 1);
		electricPump_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32620, 1);
		electricPump_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32621, 1);
		electricPump_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32622, 1);
		electricPiston_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32645, 1);
		electricPiston_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32646, 1);
		electricPiston_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32647, 1);
		robotArm_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32655, 1);
		robotArm_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32656, 1);
		robotArm_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32657, 1);
		conveyorModule_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32636, 1);
		conveyorModule_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32637, 1);
		conveyorModule_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32638, 1);
		emitter_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32685, 1);
		emitter_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32686, 1);
		emitter_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32687, 1);
		fieldGenerator_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32675, 1);
		fieldGenerator_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32676, 1);
		fieldGenerator_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32677, 1);
		sensor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32695, 1);
		sensor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32696, 1);
		sensor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32697, 1);

		//Max Tier Components Blood Never added... Useless, lol.
		electricMotor_MAX = GregtechItemList.Electric_Motor_MAX.get(1);
		electricPump_MAX = GregtechItemList.Electric_Pump_MAX.get(1);
		electricPiston_MAX = GregtechItemList.Electric_Piston_MAX.get(1);
		robotArm_MAX = GregtechItemList.Robot_Arm_MAX.get(1);
		conveyorModule_MAX = GregtechItemList.Conveyor_Module_MAX.get(1);
		emitter_MAX = GregtechItemList.Emitter_MAX.get(1);
		fieldGenerator_MAX = GregtechItemList.Field_Generator_MAX.get(1);
		sensor_MAX = GregtechItemList.Sensor_MAX.get(1);
	}

	private static void registerGTStandardComponents(){
		electricMotor_LuV = GregtechItemList.Electric_Motor_LuV.get(1);
		electricMotor_ZPM = GregtechItemList.Electric_Motor_ZPM.get(1);
		electricMotor_UV = GregtechItemList.Electric_Motor_UV.get(1);
		electricMotor_MAX = GregtechItemList.Electric_Motor_MAX.get(1);
		electricPump_LuV = GregtechItemList.Electric_Pump_LuV.get(1);
		electricPump_ZPM = GregtechItemList.Electric_Pump_ZPM.get(1);
		electricPump_UV = GregtechItemList.Electric_Pump_UV.get(1);
		electricPump_MAX = GregtechItemList.Electric_Pump_MAX.get(1);
		electricPiston_LuV = GregtechItemList.Electric_Piston_LuV.get(1);
		electricPiston_ZPM = GregtechItemList.Electric_Piston_ZPM.get(1);
		electricPiston_UV = GregtechItemList.Electric_Piston_UV.get(1);
		electricPiston_MAX = GregtechItemList.Electric_Piston_MAX.get(1);
		robotArm_LuV = GregtechItemList.Robot_Arm_LuV.get(1);
		robotArm_ZPM = GregtechItemList.Robot_Arm_ZPM.get(1);
		robotArm_UV = GregtechItemList.Robot_Arm_UV.get(1);
		robotArm_MAX = GregtechItemList.Robot_Arm_MAX.get(1);
		conveyorModule_LuV = GregtechItemList.Conveyor_Module_LuV.get(1);
		conveyorModule_ZPM = GregtechItemList.Conveyor_Module_ZPM.get(1);
		conveyorModule_UV = GregtechItemList.Conveyor_Module_UV.get(1);
		conveyorModule_MAX = GregtechItemList.Conveyor_Module_MAX.get(1);
		emitter_LuV = GregtechItemList.Emitter_LuV.get(1);
		emitter_ZPM = GregtechItemList.Emitter_ZPM.get(1);
		emitter_UV = GregtechItemList.Emitter_UV.get(1);
		emitter_MAX = GregtechItemList.Emitter_MAX.get(1);
		fieldGenerator_LuV = GregtechItemList.Field_Generator_LuV.get(1);
		fieldGenerator_ZPM = GregtechItemList.Field_Generator_ZPM.get(1);
		fieldGenerator_UV = GregtechItemList.Field_Generator_UV.get(1);
		fieldGenerator_MAX = GregtechItemList.Field_Generator_MAX.get(1);
		sensor_LuV = GregtechItemList.Sensor_LuV.get(1);
		sensor_ZPM = GregtechItemList.Sensor_ZPM.get(1);
		sensor_UV = GregtechItemList.Sensor_UV.get(1);
		sensor_MAX = GregtechItemList.Sensor_MAX.get(1);
	}

	private static void registerGTNHComponents(){
		//Machine Components
		electricMotor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32606, 1);
		electricMotor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32607, 1);
		electricMotor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32608, 1);
		
		electricPump_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32615, 1);
		electricPump_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32616, 1);
		electricPump_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32617, 1);
		
		electricPiston_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32645, 1);
		electricPiston_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32646, 1);
		electricPiston_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32647, 1);
		
		robotArm_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32655, 1);
		robotArm_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32656, 1);
		robotArm_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32657, 1);
		
		conveyorModule_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32635, 1);
		conveyorModule_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32636, 1);
		conveyorModule_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32637, 1);
		
		emitter_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32685, 1);
		emitter_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32686, 1);
		emitter_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32687, 1);
		
		fieldGenerator_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32675, 1);
		fieldGenerator_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32676, 1);
		fieldGenerator_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32677, 1);
		
		sensor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32695, 1);
		sensor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32696, 1);
		sensor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32697, 1);

		//Max Tier Components Blood Never added... Useless, lol.
		electricMotor_MAX = GregtechItemList.Electric_Motor_MAX.get(1);
		electricPump_MAX = GregtechItemList.Electric_Pump_MAX.get(1);
		electricPiston_MAX = GregtechItemList.Electric_Piston_MAX.get(1);
		robotArm_MAX = GregtechItemList.Robot_Arm_MAX.get(1);
		conveyorModule_MAX = GregtechItemList.Conveyor_Module_MAX.get(1);
		emitter_MAX = GregtechItemList.Emitter_MAX.get(1);
		fieldGenerator_MAX = GregtechItemList.Field_Generator_MAX.get(1);
		sensor_MAX = GregtechItemList.Sensor_MAX.get(1);
	}

}
