package miscutil.core.recipe;

import miscutil.core.lib.CORE;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPE_CONSTANTS {

	//Machine Components
	public static ItemStack electricMotor_LuV = GregtechItemList.Electric_Motor_LuV.get(1);
	public static ItemStack electricMotor_ZPM = GregtechItemList.Electric_Motor_ZPM.get(1);
	public static ItemStack electricMotor_UV = GregtechItemList.Electric_Motor_UV.get(1);
	public static ItemStack electricMotor_MAX = GregtechItemList.Electric_Motor_MAX.get(1);
	public static ItemStack electricPump_LuV = GregtechItemList.Electric_Pump_LuV.get(1);
	public static ItemStack electricPump_ZPM = GregtechItemList.Electric_Pump_ZPM.get(1);
	public static ItemStack electricPump_UV = GregtechItemList.Electric_Pump_UV.get(1);
	public static ItemStack electricPump_MAX = GregtechItemList.Electric_Pump_MAX.get(1);
	public static ItemStack electricPiston_LuV = GregtechItemList.Electric_Piston_LuV.get(1);
	public static ItemStack electricPiston_ZPM = GregtechItemList.Electric_Piston_ZPM.get(1);
	public static ItemStack electricPiston_UV = GregtechItemList.Electric_Piston_UV.get(1);
	public static ItemStack electricPiston_MAX = GregtechItemList.Electric_Piston_MAX.get(1);
	public static ItemStack robotArm_LuV = GregtechItemList.Robot_Arm_LuV.get(1);
	public static ItemStack robotArm_ZPM = GregtechItemList.Robot_Arm_ZPM.get(1);
	public static ItemStack robotArm_UV = GregtechItemList.Robot_Arm_UV.get(1);
	public static ItemStack robotArm_MAX = GregtechItemList.Robot_Arm_MAX.get(1);
	public static ItemStack conveyorModule_LuV = GregtechItemList.Conveyor_Module_LuV.get(1);
	public static ItemStack conveyorModule_ZPM = GregtechItemList.Conveyor_Module_ZPM.get(1);
	public static ItemStack conveyorModule_UV = GregtechItemList.Conveyor_Module_UV.get(1);
	public static ItemStack conveyorModule_MAX = GregtechItemList.Conveyor_Module_MAX.get(1);
	public static ItemStack emitter_LuV = GregtechItemList.Emitter_LuV.get(1);
	public static ItemStack emitter_ZPM = GregtechItemList.Emitter_ZPM.get(1);
	public static ItemStack emitter_UV = GregtechItemList.Emitter_UV.get(1);
	public static ItemStack emitter_MAX = GregtechItemList.Emitter_MAX.get(1);
	public static ItemStack fieldGenerator_LuV = GregtechItemList.Field_Generator_LuV.get(1);
	public static ItemStack fieldGenerator_ZPM = GregtechItemList.Field_Generator_ZPM.get(1);
	public static ItemStack fieldGenerator_UV = GregtechItemList.Field_Generator_UV.get(1);
	public static ItemStack fieldGenerator_MAX = GregtechItemList.Field_Generator_MAX.get(1);
	public static ItemStack sensor_LuV = GregtechItemList.Sensor_LuV.get(1);
	public static ItemStack sensor_ZPM = GregtechItemList.Sensor_ZPM.get(1);
	public static ItemStack sensor_UV = GregtechItemList.Sensor_UV.get(1);
	public static ItemStack sensor_MAX = GregtechItemList.Sensor_MAX.get(1);
	
	public static void initialise(){
		if(CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			registerGTExperimentalComponents();
		}
	}
	
	private static void registerGTExperimentalComponents(){
		//Outputs
		//Machine Components
		electricMotor_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32606, 1);
		electricMotor_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32607, 1);
		electricMotor_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32608, 1);
	
		electricPump_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32620, 1);
		electricPump_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32621, 1);
		electricPump_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32622, 1);
	
		electricPiston_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32645, 1);
		electricPiston_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32646, 1);
		electricPiston_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32647, 1);
	
		robotArm_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32655, 1);
		robotArm_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32656, 1);
		robotArm_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32657, 1);
	
		conveyorModule_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32636, 1);
		conveyorModule_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32637, 1);
		conveyorModule_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32638, 1);
	
		emitter_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32685, 1);
		emitter_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32686, 1);
		emitter_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32687, 1);
	
		fieldGenerator_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32675, 1);
		fieldGenerator_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32676, 1);
		fieldGenerator_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32677, 1);
	
		sensor_LuV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32695, 1);
		sensor_ZPM = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32696, 1);
		sensor_UV = UtilsItems.simpleMetaStack("gregtech:gt.metaitem.01", 32697, 1);
	}

}
