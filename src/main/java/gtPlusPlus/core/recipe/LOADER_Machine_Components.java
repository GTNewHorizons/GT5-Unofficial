package gtPlusPlus.core.recipe;

import gregtech.api.enums.ItemList;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class LOADER_Machine_Components {

    public static void initialise() {
        registerDefaultComponents();

        registerGTNHComponents();
    }

    private static void registerDefaultComponents() {
        // Machine Components
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
        CI.fluidRegulator_LV = ItemList.FluidRegulator_LV.get(1);
        CI.fluidRegulator_MV = ItemList.FluidRegulator_MV.get(1);
        CI.fluidRegulator_HV = ItemList.FluidRegulator_HV.get(1);
        CI.fluidRegulator_EV = ItemList.FluidRegulator_EV.get(1);
        CI.fluidRegulator_IV = ItemList.FluidRegulator_IV.get(1);
    }

    private static void registerGTNHComponents() {
        // Machine Components
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

        CI.fluidRegulator_LuV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32665, 1);
        CI.fluidRegulator_ZPM = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32666, 1);
        CI.fluidRegulator_UV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32667, 1);

        // Thanks 0lafe
        CI.electricMotor_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32596, 1);
        CI.electricPump_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32618, 1);
        CI.electricPiston_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32648, 1);
        CI.robotArm_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32658, 1);
        CI.conveyorModule_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32638, 1);
        CI.emitter_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32688, 1);
        CI.fieldGenerator_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32678, 1);
        CI.sensor_UHV = ItemUtils.simpleMetaStack("gregtech:gt.metaitem.01", 32698, 1);

    }

}
