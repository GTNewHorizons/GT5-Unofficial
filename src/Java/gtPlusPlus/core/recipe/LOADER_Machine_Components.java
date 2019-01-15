package gtPlusPlus.core.recipe;

import gregtech.api.enums.ItemList;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class LOADER_Machine_Components {

	public static void initialise(){

		registerDefaultComponents();

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

	private static void registerDefaultComponents(){
		//Machine Components
		CI.electricMotor_LV = ItemList.Electric_Motor_LV.get(1);
		CI.electricMotor_MV = ItemList.Electric_Motor_MV.get(1);
		CI.electricMotor_HV = ItemList.Electric_Motor_HV.get(1);
		CI.electricMotor_EV = ItemList.Electric_Motor_EV.get(1);
		CI.electricMotor_IV = ItemList.Electric_Motor_IV.get(1);
		CI.electricPump_LV = ItemList.Electric_Pump_LV.get(1);
		CI.electricPump_MV = ItemList.Electric_Pump_MV.get(1);
		CI.electricPump_HV = ItemList.Electric_Pump_HV.get(1);
		CI.electricPump_EV = ItemList.Electric_Pump_EV.get(1);
		CI.electricPump_IV = ItemList.Electric_Pump_IV.get(1);
		CI.electricPiston_LV = ItemList.Electric_Piston_LV.get(1);
		CI.electricPiston_MV = ItemList.Electric_Piston_MV.get(1);
		CI.electricPiston_HV = ItemList.Electric_Piston_HV.get(1);
		CI.electricPiston_EV = ItemList.Electric_Piston_EV.get(1);
		CI.electricPiston_IV = ItemList.Electric_Piston_IV.get(1);
		CI.robotArm_LV = ItemList.Robot_Arm_LV.get(1);
		CI.robotArm_MV = ItemList.Robot_Arm_MV.get(1);
		CI.robotArm_HV = ItemList.Robot_Arm_HV.get(1);
		CI.robotArm_EV = ItemList.Robot_Arm_EV.get(1);
		CI.robotArm_IV = ItemList.Robot_Arm_IV.get(1);
		CI.conveyorModule_LV = ItemList.Conveyor_Module_LV.get(1);
		CI.conveyorModule_MV = ItemList.Conveyor_Module_MV.get(1);
		CI.conveyorModule_HV = ItemList.Conveyor_Module_HV.get(1);
		CI.conveyorModule_EV = ItemList.Conveyor_Module_EV.get(1);
		CI.conveyorModule_IV = ItemList.Conveyor_Module_IV.get(1);
		CI.emitter_LV = ItemList.Emitter_LV.get(1);
		CI.emitter_MV = ItemList.Emitter_MV.get(1);
		CI.emitter_HV = ItemList.Emitter_HV.get(1);
		CI.emitter_EV = ItemList.Emitter_EV.get(1);
		CI.emitter_IV = ItemList.Emitter_IV.get(1);
		CI.fieldGenerator_LV = ItemList.Field_Generator_LV.get(1);
		CI.fieldGenerator_MV = ItemList.Field_Generator_MV.get(1);
		CI.fieldGenerator_HV = ItemList.Field_Generator_HV.get(1);
		CI.fieldGenerator_EV = ItemList.Field_Generator_EV.get(1);
		CI.fieldGenerator_IV = ItemList.Field_Generator_IV.get(1);
		CI.sensor_LV = ItemList.Sensor_LV.get(1);
		CI.sensor_MV = ItemList.Sensor_MV.get(1);
		CI.sensor_HV = ItemList.Sensor_HV.get(1);
		CI.sensor_EV = ItemList.Sensor_EV.get(1);
		CI.sensor_IV = ItemList.Sensor_IV.get(1);
	}

	private static void registerGTExperimentalComponents(){
		//GT++ Machine Components
		CI.electricMotor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32606, 1);
		CI.electricMotor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32607, 1);
		CI.electricMotor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32608, 1);
		CI.electricPump_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32620, 1);
		CI.electricPump_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32621, 1);
		CI.electricPump_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32622, 1);
		CI.electricPiston_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32645, 1);
		CI.electricPiston_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32646, 1);
		CI.electricPiston_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32647, 1);
		CI.robotArm_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32655, 1);
		CI.robotArm_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32656, 1);
		CI.robotArm_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32657, 1);
		CI.conveyorModule_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32636, 1);
		CI.conveyorModule_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32637, 1);
		CI.conveyorModule_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32638, 1);
		CI.emitter_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32685, 1);
		CI.emitter_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32686, 1);
		CI.emitter_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32687, 1);
		CI.fieldGenerator_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32675, 1);
		CI.fieldGenerator_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32676, 1);
		CI.fieldGenerator_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32677, 1);
		CI.sensor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32695, 1);
		CI.sensor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32696, 1);
		CI.sensor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32697, 1);

		registerComponentsULV();
		registerComponentsMAX();
	}

	private static void registerGTStandardComponents(){
		CI.electricMotor_LuV = GregtechItemList.Electric_Motor_LuV.get(1);
		CI.electricMotor_ZPM = GregtechItemList.Electric_Motor_ZPM.get(1);
		CI.electricMotor_UV = GregtechItemList.Electric_Motor_UV.get(1);
		CI.electricMotor_MAX = GregtechItemList.Electric_Motor_MAX.get(1);
		CI.electricPump_LuV = GregtechItemList.Electric_Pump_LuV.get(1);
		CI.electricPump_ZPM = GregtechItemList.Electric_Pump_ZPM.get(1);
		CI.electricPump_UV = GregtechItemList.Electric_Pump_UV.get(1);
		CI.electricPump_MAX = GregtechItemList.Electric_Pump_MAX.get(1);
		CI.electricPiston_LuV = GregtechItemList.Electric_Piston_LuV.get(1);
		CI.electricPiston_ZPM = GregtechItemList.Electric_Piston_ZPM.get(1);
		CI.electricPiston_UV = GregtechItemList.Electric_Piston_UV.get(1);
		CI.electricPiston_MAX = GregtechItemList.Electric_Piston_MAX.get(1);
		CI.robotArm_LuV = GregtechItemList.Robot_Arm_LuV.get(1);
		CI.robotArm_ZPM = GregtechItemList.Robot_Arm_ZPM.get(1);
		CI.robotArm_UV = GregtechItemList.Robot_Arm_UV.get(1);
		CI.robotArm_MAX = GregtechItemList.Robot_Arm_MAX.get(1);
		CI.conveyorModule_LuV = GregtechItemList.Conveyor_Module_LuV.get(1);
		CI.conveyorModule_ZPM = GregtechItemList.Conveyor_Module_ZPM.get(1);
		CI.conveyorModule_UV = GregtechItemList.Conveyor_Module_UV.get(1);
		CI.conveyorModule_MAX = GregtechItemList.Conveyor_Module_MAX.get(1);
		CI.emitter_LuV = GregtechItemList.Emitter_LuV.get(1);
		CI.emitter_ZPM = GregtechItemList.Emitter_ZPM.get(1);
		CI.emitter_UV = GregtechItemList.Emitter_UV.get(1);
		CI.emitter_MAX = GregtechItemList.Emitter_MAX.get(1);
		CI.fieldGenerator_LuV = GregtechItemList.Field_Generator_LuV.get(1);
		CI.fieldGenerator_ZPM = GregtechItemList.Field_Generator_ZPM.get(1);
		CI.fieldGenerator_UV = GregtechItemList.Field_Generator_UV.get(1);
		CI.fieldGenerator_MAX = GregtechItemList.Field_Generator_MAX.get(1);
		CI.sensor_LuV = GregtechItemList.Sensor_LuV.get(1);
		CI.sensor_ZPM = GregtechItemList.Sensor_ZPM.get(1);
		CI.sensor_UV = GregtechItemList.Sensor_UV.get(1);
		CI.sensor_MAX = GregtechItemList.Sensor_MAX.get(1);

		registerComponentsULV();
	}

	private static void registerGTNHComponents(){
		//Machine Components
		CI.electricMotor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32606, 1);
		CI.electricMotor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32607, 1);
		CI.electricMotor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32608, 1);

		CI.electricPump_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32615, 1);
		CI.electricPump_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32616, 1);
		CI.electricPump_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32617, 1);

		CI.electricPiston_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32645, 1);
		CI.electricPiston_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32646, 1);
		CI.electricPiston_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32647, 1);

		CI.robotArm_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32655, 1);
		CI.robotArm_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32656, 1);
		CI.robotArm_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32657, 1);

		CI.conveyorModule_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32635, 1);
		CI.conveyorModule_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32636, 1);
		CI.conveyorModule_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32637, 1);

		CI.emitter_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32685, 1);
		CI.emitter_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32686, 1);
		CI.emitter_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32687, 1);

		CI.fieldGenerator_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32675, 1);
		CI.fieldGenerator_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32676, 1);
		CI.fieldGenerator_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32677, 1);

		CI.sensor_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32695, 1);
		CI.sensor_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32696, 1);
		CI.sensor_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32697, 1);

		// Thanks 0lafe
		CI.electricMotor_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32596, 1);
		CI.electricPump_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32618, 1);
		CI.electricPiston_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32648, 1);
		CI.robotArm_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32658, 1);
		CI.conveyorModule_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32638, 1);
		CI.emitter_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32688, 1);
		CI.fieldGenerator_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32678, 1);
		CI.sensor_MAX = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32698, 1);

		registerComponentsULV();
	}

	private static boolean registerComponentsULV(){

		CI.electricMotor_ULV = GregtechItemList.Electric_Motor_ULV.get(1);
		CI.electricPump_ULV = GregtechItemList.Electric_Pump_ULV.get(1);
		CI.electricPiston_ULV = GregtechItemList.Electric_Piston_ULV.get(1);
		CI.robotArm_ULV = GregtechItemList.Robot_Arm_ULV.get(1);
		CI.conveyorModule_ULV = GregtechItemList.Conveyor_Module_ULV.get(1);
		CI.emitter_ULV = GregtechItemList.Emitter_ULV.get(1);
		CI.fieldGenerator_ULV = GregtechItemList.Field_Generator_ULV.get(1);
		CI.sensor_ULV = GregtechItemList.Sensor_ULV.get(1);
		return true;
	}

	private static boolean registerComponentsMAX() {

		// Max Tier Components Blood Never added... Useless, lol.
		CI.electricMotor_MAX = GregtechItemList.Electric_Motor_MAX.get(1);
		CI.electricPump_MAX = GregtechItemList.Electric_Pump_MAX.get(1);
		CI.electricPiston_MAX = GregtechItemList.Electric_Piston_MAX.get(1);
		CI.robotArm_MAX = GregtechItemList.Robot_Arm_MAX.get(1);
		CI.conveyorModule_MAX = GregtechItemList.Conveyor_Module_MAX.get(1);
		CI.emitter_MAX = GregtechItemList.Emitter_MAX.get(1);
		CI.fieldGenerator_MAX = GregtechItemList.Field_Generator_MAX.get(1);
		CI.sensor_MAX = GregtechItemList.Sensor_MAX.get(1);

		return true;
	}

}
