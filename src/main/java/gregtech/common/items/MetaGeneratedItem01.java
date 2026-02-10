package gregtech.common.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.COVER_WOOD_PLATE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ACTIVITYDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ACTIVITYDETECTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ARM;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CONTROLLER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CONVEYOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CRAFTING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DRAIN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGYDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUIDDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR0;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ITEMDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MAINTENANCE_DETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PUMP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_REDSTONE_RECEIVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_REDSTONE_TRANSMITTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SHUTTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VALVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_WIRELESS_CONTROLLER;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_8V;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_EV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_HV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_IV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_LV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_LuV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_MV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_ZPM;
import static gregtech.client.GTTooltipHandler.Tier.ERV;
import static gregtech.client.GTTooltipHandler.Tier.EV;
import static gregtech.client.GTTooltipHandler.Tier.HV;
import static gregtech.client.GTTooltipHandler.Tier.IV;
import static gregtech.client.GTTooltipHandler.Tier.LV;
import static gregtech.client.GTTooltipHandler.Tier.LuV;
import static gregtech.client.GTTooltipHandler.Tier.MAX;
import static gregtech.client.GTTooltipHandler.Tier.MV;
import static gregtech.client.GTTooltipHandler.Tier.UEV;
import static gregtech.client.GTTooltipHandler.Tier.UHV;
import static gregtech.client.GTTooltipHandler.Tier.UIV;
import static gregtech.client.GTTooltipHandler.Tier.ULV;
import static gregtech.client.GTTooltipHandler.Tier.UMV;
import static gregtech.client.GTTooltipHandler.Tier.UV;
import static gregtech.client.GTTooltipHandler.Tier.UXV;
import static gregtech.client.GTTooltipHandler.Tier.ZPM;
import static gregtech.client.GTTooltipHandler.registerTieredTooltip;
import static gregtech.common.items.IDMetaItem01.Axon_Bus;
import static gregtech.common.items.IDMetaItem01.BatteryHull_EV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_EV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_IV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_IV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_LuV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_LuV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UEV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UEV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UHV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UHV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UIV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UIV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UMV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UMV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UxV;
import static gregtech.common.items.IDMetaItem01.BatteryHull_UxV_Full;
import static gregtech.common.items.IDMetaItem01.BatteryHull_ZPM;
import static gregtech.common.items.IDMetaItem01.BatteryHull_ZPM_Full;
import static gregtech.common.items.IDMetaItem01.Battery_Hull_HV;
import static gregtech.common.items.IDMetaItem01.Battery_Hull_LV;
import static gregtech.common.items.IDMetaItem01.Battery_Hull_MV;
import static gregtech.common.items.IDMetaItem01.Battery_RE_HV_Cadmium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_HV_Lithium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_HV_Sodium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_LV_Cadmium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_LV_Lithium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_LV_Sodium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_MV_Cadmium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_MV_Lithium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_MV_Sodium;
import static gregtech.common.items.IDMetaItem01.Battery_RE_ULV_Tantalum;
import static gregtech.common.items.IDMetaItem01.Battery_SU_HV_Mercury;
import static gregtech.common.items.IDMetaItem01.Battery_SU_HV_Sulfuric_Acid;
import static gregtech.common.items.IDMetaItem01.Battery_SU_LV_Mercury;
import static gregtech.common.items.IDMetaItem01.Battery_SU_LV_Sulfuric_Acid;
import static gregtech.common.items.IDMetaItem01.Battery_SU_MV_Mercury;
import static gregtech.common.items.IDMetaItem01.Battery_SU_MV_Sulfuric_Acid;
import static gregtech.common.items.IDMetaItem01.Bio_Computing_Core;
import static gregtech.common.items.IDMetaItem01.Black_Hole_Closer;
import static gregtech.common.items.IDMetaItem01.Black_Hole_Opener;
import static gregtech.common.items.IDMetaItem01.Black_Hole_Stabilizer;
import static gregtech.common.items.IDMetaItem01.Book_Written_01;
import static gregtech.common.items.IDMetaItem01.Book_Written_02;
import static gregtech.common.items.IDMetaItem01.Book_Written_03;
import static gregtech.common.items.IDMetaItem01.Circuit_Advanced;
import static gregtech.common.items.IDMetaItem01.Circuit_Basic;
import static gregtech.common.items.IDMetaItem01.Circuit_Board_Advanced;
import static gregtech.common.items.IDMetaItem01.Circuit_Board_Basic;
import static gregtech.common.items.IDMetaItem01.Circuit_Board_Elite;
import static gregtech.common.items.IDMetaItem01.Circuit_Data;
import static gregtech.common.items.IDMetaItem01.Circuit_Elite;
import static gregtech.common.items.IDMetaItem01.Circuit_Good;
import static gregtech.common.items.IDMetaItem01.Circuit_Master;
import static gregtech.common.items.IDMetaItem01.Circuit_Parts_Advanced;
import static gregtech.common.items.IDMetaItem01.Circuit_Parts_Crystal_Chip_Elite;
import static gregtech.common.items.IDMetaItem01.Circuit_Parts_Crystal_Chip_Master;
import static gregtech.common.items.IDMetaItem01.Circuit_Parts_Crystal_Chip_Wetware;
import static gregtech.common.items.IDMetaItem01.Circuit_Parts_Wiring_Advanced;
import static gregtech.common.items.IDMetaItem01.Circuit_Parts_Wiring_Basic;
import static gregtech.common.items.IDMetaItem01.Circuit_Parts_Wiring_Elite;
import static gregtech.common.items.IDMetaItem01.Circuit_Primitive;
import static gregtech.common.items.IDMetaItem01.Circuit_Tissue;
import static gregtech.common.items.IDMetaItem01.Component_Filter;
import static gregtech.common.items.IDMetaItem01.Component_Grinder_Diamond;
import static gregtech.common.items.IDMetaItem01.Component_Grinder_Tungsten;
import static gregtech.common.items.IDMetaItem01.Component_Minecraft_Wheels_Iron;
import static gregtech.common.items.IDMetaItem01.Component_Minecraft_Wheels_Steel;
import static gregtech.common.items.IDMetaItem01.Component_Sawblade_Diamond;
import static gregtech.common.items.IDMetaItem01.Compressed_Fireclay;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_EV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_HV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_IV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_LV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_LuV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_MAX;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_MV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_UEV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_UHV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_UIV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_UMV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_UV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_UXV;
import static gregtech.common.items.IDMetaItem01.Conveyor_Module_ZPM;
import static gregtech.common.items.IDMetaItem01.Cover_ActivityDetector;
import static gregtech.common.items.IDMetaItem01.Cover_Chest_Advanced;
import static gregtech.common.items.IDMetaItem01.Cover_Chest_Basic;
import static gregtech.common.items.IDMetaItem01.Cover_Chest_Good;
import static gregtech.common.items.IDMetaItem01.Cover_Controller;
import static gregtech.common.items.IDMetaItem01.Cover_Crafting;
import static gregtech.common.items.IDMetaItem01.Cover_Drain;
import static gregtech.common.items.IDMetaItem01.Cover_EnergyDetector;
import static gregtech.common.items.IDMetaItem01.Cover_FLuidStorageMonitor;
import static gregtech.common.items.IDMetaItem01.Cover_FluidDetector;
import static gregtech.common.items.IDMetaItem01.Cover_FluidLimiter;
import static gregtech.common.items.IDMetaItem01.Cover_ItemDetector;
import static gregtech.common.items.IDMetaItem01.Cover_NeedsMaintenance;
import static gregtech.common.items.IDMetaItem01.Cover_PlayerDetector;
import static gregtech.common.items.IDMetaItem01.Cover_RedstoneReceiver;
import static gregtech.common.items.IDMetaItem01.Cover_RedstoneTransmitter;
import static gregtech.common.items.IDMetaItem01.Cover_RedstoneTransmitterInternal;
import static gregtech.common.items.IDMetaItem01.Cover_Screen;
import static gregtech.common.items.IDMetaItem01.Cover_Shutter;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_8V;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_EV;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_HV;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_IV;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_LV;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_LuV;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_MV;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_UV;
import static gregtech.common.items.IDMetaItem01.Cover_SolarPanel_ZPM;
import static gregtech.common.items.IDMetaItem01.Cover_WirelessController;
import static gregtech.common.items.IDMetaItem01.Cover_Wireless_Energy_Debug;
import static gregtech.common.items.IDMetaItem01.Cover_Wireless_Energy_LV;
import static gregtech.common.items.IDMetaItem01.Duct_Tape;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_EV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_HV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_IV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_LV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_LuV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_MAX;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_MV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_UEV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_UHV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_UIV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_UMV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_UV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_UXV;
import static gregtech.common.items.IDMetaItem01.Electric_Motor_ZPM;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_EV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_HV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_IV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_LV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_LuV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_MAX;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_MV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_UEV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_UHV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_UIV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_UMV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_UV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_UXV;
import static gregtech.common.items.IDMetaItem01.Electric_Piston_ZPM;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_EV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_HV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_IV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_LV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_LuV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_MAX;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_MV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_UEV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_UHV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_UIV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_UMV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_UV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_UXV;
import static gregtech.common.items.IDMetaItem01.Electric_Pump_ZPM;
import static gregtech.common.items.IDMetaItem01.Electromagnet_Iron;
import static gregtech.common.items.IDMetaItem01.Electromagnet_Neodymium;
import static gregtech.common.items.IDMetaItem01.Electromagnet_Samarium;
import static gregtech.common.items.IDMetaItem01.Electromagnet_Steel;
import static gregtech.common.items.IDMetaItem01.Electromagnet_Tengam;
import static gregtech.common.items.IDMetaItem01.Emitter_EV;
import static gregtech.common.items.IDMetaItem01.Emitter_HV;
import static gregtech.common.items.IDMetaItem01.Emitter_IV;
import static gregtech.common.items.IDMetaItem01.Emitter_LV;
import static gregtech.common.items.IDMetaItem01.Emitter_LuV;
import static gregtech.common.items.IDMetaItem01.Emitter_MAX;
import static gregtech.common.items.IDMetaItem01.Emitter_MV;
import static gregtech.common.items.IDMetaItem01.Emitter_UEV;
import static gregtech.common.items.IDMetaItem01.Emitter_UHV;
import static gregtech.common.items.IDMetaItem01.Emitter_UIV;
import static gregtech.common.items.IDMetaItem01.Emitter_UMV;
import static gregtech.common.items.IDMetaItem01.Emitter_UV;
import static gregtech.common.items.IDMetaItem01.Emitter_UXV;
import static gregtech.common.items.IDMetaItem01.Emitter_ZPM;
import static gregtech.common.items.IDMetaItem01.Empty_Board_Basic;
import static gregtech.common.items.IDMetaItem01.Empty_Board_Elite;
import static gregtech.common.items.IDMetaItem01.EnergisedTesseract;
import static gregtech.common.items.IDMetaItem01.Energy_Cluster;
import static gregtech.common.items.IDMetaItem01.Energy_Lapotronic_Orb;
import static gregtech.common.items.IDMetaItem01.Energy_Lapotronic_orb_2;
import static gregtech.common.items.IDMetaItem01.Energy_Module;
import static gregtech.common.items.IDMetaItem01.Field_Generator_EV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_HV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_IV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_LV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_LuV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_MAX;
import static gregtech.common.items.IDMetaItem01.Field_Generator_MV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_UEV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_UHV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_UIV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_UMV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_UV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_UXV;
import static gregtech.common.items.IDMetaItem01.Field_Generator_ZPM;
import static gregtech.common.items.IDMetaItem01.Firebrick;
import static gregtech.common.items.IDMetaItem01.FluidFilter;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_EV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_HV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_IV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_LV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_LuV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_MAX;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_MV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_UEV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_UHV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_UIV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_UMV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_UV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_UXV;
import static gregtech.common.items.IDMetaItem01.FluidRegulator_ZPM;
import static gregtech.common.items.IDMetaItem01.Fuel_Can_Plastic_Empty;
import static gregtech.common.items.IDMetaItem01.Fuel_Can_Plastic_Filled;
import static gregtech.common.items.IDMetaItem01.GigaChad;
import static gregtech.common.items.IDMetaItem01.Gravistar;
import static gregtech.common.items.IDMetaItem01.Immortal_Cell;
import static gregtech.common.items.IDMetaItem01.Infinite_Evolution_Matrix;
import static gregtech.common.items.IDMetaItem01.Ingot_Heavy1;
import static gregtech.common.items.IDMetaItem01.Ingot_Heavy2;
import static gregtech.common.items.IDMetaItem01.Ingot_Heavy3;
import static gregtech.common.items.IDMetaItem01.Ingot_Iridium_Alloy;
import static gregtech.common.items.IDMetaItem01.ItemFilter_Export;
import static gregtech.common.items.IDMetaItem01.ItemFilter_Import;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_Aluminium;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_Chrome;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_Iridium;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_Neutronium;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_Osmium;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_StainlessSteel;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_Steel;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_Titanium;
import static gregtech.common.items.IDMetaItem01.Large_Fluid_Cell_TungstenSteel;
import static gregtech.common.items.IDMetaItem01.McGuffium_239;
import static gregtech.common.items.IDMetaItem01.Muscle_Cell_Cluster;
import static gregtech.common.items.IDMetaItem01.NC_SensorKit;
import static gregtech.common.items.IDMetaItem01.NaquadriaSupersolid;
import static gregtech.common.items.IDMetaItem01.Neural_Electronic_Interface;
import static gregtech.common.items.IDMetaItem01.Neuron_Cell_Cluster;
import static gregtech.common.items.IDMetaItem01.Nutrient_Paste;
import static gregtech.common.items.IDMetaItem01.Paper_Magic_Empty;
import static gregtech.common.items.IDMetaItem01.Paper_Magic_Page;
import static gregtech.common.items.IDMetaItem01.Paper_Magic_Pages;
import static gregtech.common.items.IDMetaItem01.Paper_Printed_Pages;
import static gregtech.common.items.IDMetaItem01.Paper_Punch_Card_Empty;
import static gregtech.common.items.IDMetaItem01.Paper_Punch_Card_Encoded;
import static gregtech.common.items.IDMetaItem01.QuantumEye;
import static gregtech.common.items.IDMetaItem01.QuantumStar;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_EV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_HV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_IV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_LV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_LuV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_MAX;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_MV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_UEV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_UHV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_UIV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_UMV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_UV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_UXV;
import static gregtech.common.items.IDMetaItem01.Robot_Arm_ZPM;
import static gregtech.common.items.IDMetaItem01.Schematic;
import static gregtech.common.items.IDMetaItem01.Schematic_1by1;
import static gregtech.common.items.IDMetaItem01.Schematic_2by2;
import static gregtech.common.items.IDMetaItem01.Schematic_3by3;
import static gregtech.common.items.IDMetaItem01.Schematic_Crafting;
import static gregtech.common.items.IDMetaItem01.Schematic_Dust;
import static gregtech.common.items.IDMetaItem01.Schematic_Dust_Small;
import static gregtech.common.items.IDMetaItem01.Self_Healing_Conductor;
import static gregtech.common.items.IDMetaItem01.Sensor_EV;
import static gregtech.common.items.IDMetaItem01.Sensor_HV;
import static gregtech.common.items.IDMetaItem01.Sensor_IV;
import static gregtech.common.items.IDMetaItem01.Sensor_LV;
import static gregtech.common.items.IDMetaItem01.Sensor_LuV;
import static gregtech.common.items.IDMetaItem01.Sensor_MAX;
import static gregtech.common.items.IDMetaItem01.Sensor_MV;
import static gregtech.common.items.IDMetaItem01.Sensor_UEV;
import static gregtech.common.items.IDMetaItem01.Sensor_UHV;
import static gregtech.common.items.IDMetaItem01.Sensor_UIV;
import static gregtech.common.items.IDMetaItem01.Sensor_UMV;
import static gregtech.common.items.IDMetaItem01.Sensor_UV;
import static gregtech.common.items.IDMetaItem01.Sensor_UXV;
import static gregtech.common.items.IDMetaItem01.Sensor_ZPM;
import static gregtech.common.items.IDMetaItem01.Shape_Empty;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Axe;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Block;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Bolt;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Bottle;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Casing;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Cell;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_File;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Gear;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Hammer;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Hoe;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Ingot;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Pickaxe;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Pipe_Huge;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Pipe_Large;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Pipe_Medium;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Pipe_Small;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Pipe_Tiny;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Plate;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Ring;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Rod;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Rotor;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Saw;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Shovel;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Small_Gear;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Sword;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Tool_Head_Drill;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Turbine_Blade;
import static gregtech.common.items.IDMetaItem01.Shape_Extruder_Wire;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Anvil;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Arrow;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Baguette;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Ball;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Block;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Bolt;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Bottle;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Bread;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Bun;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Casing;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Credit;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Cylinder;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Gear;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Gear_Small;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Ingot;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Name;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Nugget;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Pipe_Huge;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Pipe_Large;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Pipe_Medium;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Pipe_Small;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Pipe_Tiny;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Plate;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Ring;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Rod;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Rod_Long;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Rotor;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Round;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Screw;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Tool_Head_Drill;
import static gregtech.common.items.IDMetaItem01.Shape_Mold_Turbine_Blade;
import static gregtech.common.items.IDMetaItem01.Shape_Slicer_Flat;
import static gregtech.common.items.IDMetaItem01.Shape_Slicer_Stripes;
import static gregtech.common.items.IDMetaItem01.Skin_Cell_Cluster;
import static gregtech.common.items.IDMetaItem01.Spray_Color_0;
import static gregtech.common.items.IDMetaItem01.Spray_Color_1;
import static gregtech.common.items.IDMetaItem01.Spray_Color_10;
import static gregtech.common.items.IDMetaItem01.Spray_Color_11;
import static gregtech.common.items.IDMetaItem01.Spray_Color_12;
import static gregtech.common.items.IDMetaItem01.Spray_Color_13;
import static gregtech.common.items.IDMetaItem01.Spray_Color_14;
import static gregtech.common.items.IDMetaItem01.Spray_Color_15;
import static gregtech.common.items.IDMetaItem01.Spray_Color_2;
import static gregtech.common.items.IDMetaItem01.Spray_Color_3;
import static gregtech.common.items.IDMetaItem01.Spray_Color_4;
import static gregtech.common.items.IDMetaItem01.Spray_Color_5;
import static gregtech.common.items.IDMetaItem01.Spray_Color_6;
import static gregtech.common.items.IDMetaItem01.Spray_Color_7;
import static gregtech.common.items.IDMetaItem01.Spray_Color_8;
import static gregtech.common.items.IDMetaItem01.Spray_Color_9;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Infinite;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Remover;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Remover_Empty;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_0;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_1;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_10;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_11;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_12;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_13;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_14;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_15;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_2;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_3;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_4;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_5;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_6;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_7;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_8;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_9;
import static gregtech.common.items.IDMetaItem01.Spray_Color_Used_Remover;
import static gregtech.common.items.IDMetaItem01.Spray_Empty;
import static gregtech.common.items.IDMetaItem01.StableAdhesive;
import static gregtech.common.items.IDMetaItem01.Steam_Regulator_EV;
import static gregtech.common.items.IDMetaItem01.Steam_Regulator_HV;
import static gregtech.common.items.IDMetaItem01.Steam_Regulator_IV;
import static gregtech.common.items.IDMetaItem01.Steam_Regulator_LV;
import static gregtech.common.items.IDMetaItem01.Steam_Regulator_MV;
import static gregtech.common.items.IDMetaItem01.Steam_Valve_EV;
import static gregtech.common.items.IDMetaItem01.Steam_Valve_HV;
import static gregtech.common.items.IDMetaItem01.Steam_Valve_IV;
import static gregtech.common.items.IDMetaItem01.Steam_Valve_LV;
import static gregtech.common.items.IDMetaItem01.Steam_Valve_MV;
import static gregtech.common.items.IDMetaItem01.SuperconductorComposite;
import static gregtech.common.items.IDMetaItem01.Tesseract;
import static gregtech.common.items.IDMetaItem01.Thermos_Can_Empty;
import static gregtech.common.items.IDMetaItem01.Tool_Cheat;
import static gregtech.common.items.IDMetaItem01.Tool_Cover_Copy_Paste;
import static gregtech.common.items.IDMetaItem01.Tool_DataOrb;
import static gregtech.common.items.IDMetaItem01.Tool_DataStick;
import static gregtech.common.items.IDMetaItem01.Tool_Lighter_Invar_Empty;
import static gregtech.common.items.IDMetaItem01.Tool_Lighter_Invar_Full;
import static gregtech.common.items.IDMetaItem01.Tool_Lighter_Invar_Used;
import static gregtech.common.items.IDMetaItem01.Tool_Lighter_Platinum_Empty;
import static gregtech.common.items.IDMetaItem01.Tool_Lighter_Platinum_Full;
import static gregtech.common.items.IDMetaItem01.Tool_Lighter_Platinum_Used;
import static gregtech.common.items.IDMetaItem01.Tool_MatchBox_Full;
import static gregtech.common.items.IDMetaItem01.Tool_MatchBox_Used;
import static gregtech.common.items.IDMetaItem01.Tool_Matches;
import static gregtech.common.items.IDMetaItem01.Tool_Scanner;
import static gregtech.common.items.IDMetaItem01.Upgrade_Lock;
import static gregtech.common.items.IDMetaItem01.ZPM2;
import static gregtech.common.items.IDMetaItem01.ZPM3;
import static gregtech.common.items.IDMetaItem01.ZPM4;
import static gregtech.common.items.IDMetaItem01.ZPM5;
import static gregtech.common.items.IDMetaItem01.ZPM6;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverPlacer;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTFoodStat;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Other;
import gregtech.common.covers.CoverArm;
import gregtech.common.covers.CoverChest;
import gregtech.common.covers.CoverControlsWork;
import gregtech.common.covers.CoverConveyor;
import gregtech.common.covers.CoverCrafting;
import gregtech.common.covers.CoverDoesWork;
import gregtech.common.covers.CoverDrain;
import gregtech.common.covers.CoverEUMeter;
import gregtech.common.covers.CoverEnergyWireless;
import gregtech.common.covers.CoverEnergyWirelessDebug;
import gregtech.common.covers.CoverFluidLimiter;
import gregtech.common.covers.CoverFluidRegulator;
import gregtech.common.covers.CoverFluidStorageMonitor;
import gregtech.common.covers.CoverFluidfilter;
import gregtech.common.covers.CoverItemFilter;
import gregtech.common.covers.CoverItemMeter;
import gregtech.common.covers.CoverLiquidMeter;
import gregtech.common.covers.CoverNeedMaintainance;
import gregtech.common.covers.CoverPlayerDetector;
import gregtech.common.covers.CoverPump;
import gregtech.common.covers.CoverRedstoneReceiverExternal;
import gregtech.common.covers.CoverRedstoneTransmitterExternal;
import gregtech.common.covers.CoverRedstoneTransmitterInternal;
import gregtech.common.covers.CoverShutter;
import gregtech.common.covers.CoverSolarPanel;
import gregtech.common.covers.CoverSteamRegulator;
import gregtech.common.covers.CoverSteamValve;
import gregtech.common.covers.CoverWirelessController;
import gregtech.common.items.behaviors.BehaviourCoverTool;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import gregtech.common.items.behaviors.BehaviourDataStick;
import gregtech.common.items.behaviors.BehaviourLighter;
import gregtech.common.items.behaviors.BehaviourPrintedPages;
import gregtech.common.items.behaviors.BehaviourScanner;
import gregtech.common.items.behaviors.BehaviourSensorKit;
import gregtech.common.items.behaviors.BehaviourSprayColor;
import gregtech.common.items.behaviors.BehaviourSprayColorInfinite;
import gregtech.common.items.behaviors.BehaviourSprayColorRemover;
import gregtech.common.items.behaviors.BehaviourWrittenBook;
import gregtech.common.render.items.CosmicNeutroniumMetaItemRenderer;
import gregtech.common.render.items.InfinityMetaItemRenderer;
import gregtech.common.render.items.TranscendentalMetaItemRenderer;
import gregtech.common.render.items.WireFrameTesseractRenderer;
import gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator.MagnetTiers;
import mods.railcraft.common.items.firestone.IItemFirestoneBurning;

@Optional.Interface(
    iface = "mods.railcraft.common.items.firestone.IItemFirestoneBurning",
    modid = Mods.ModIDs.RAILCRAFT)
public class MetaGeneratedItem01 extends MetaGeneratedItemX32 implements IItemFirestoneBurning {

    public static MetaGeneratedItem01 INSTANCE;
    private static final String aTextEmptyRow = "   ";
    private static final String aTextShape = " P ";
    private static final String PartCoverText = " L/t (";
    private static final String PartCoverText2 = " L/s) as Cover";
    private static final String PartNotCoverText = "Cannot be used as a Cover";
    private static final String RAText = "Grabs from and inserts into specific slots";
    private static final String FRText1 = "Configurable up to ";
    private static final String FRText2 = " L/sec (as Cover)/n Rightclick/Screwdriver-rightclick/Shift-screwdriver-rightclick/n to adjust the pump speed by 1/16/256 L/sec per click/n Can not transfer more than 2.1B L per Operation";
    private static final int[] Spray_Colors = new int[] { Spray_Color_0.ID, Spray_Color_1.ID, Spray_Color_2.ID,
        Spray_Color_3.ID, Spray_Color_4.ID, Spray_Color_5.ID, Spray_Color_6.ID, Spray_Color_7.ID, Spray_Color_8.ID,
        Spray_Color_9.ID, Spray_Color_10.ID, Spray_Color_11.ID, Spray_Color_12.ID, Spray_Color_13.ID, Spray_Color_14.ID,
        Spray_Color_15.ID };
    private static final int[] Spray_Colors_Used = new int[] { Spray_Color_Used_0.ID, Spray_Color_Used_1.ID,
        Spray_Color_Used_2.ID, Spray_Color_Used_3.ID, Spray_Color_Used_4.ID, Spray_Color_Used_5.ID,
        Spray_Color_Used_6.ID, Spray_Color_Used_7.ID, Spray_Color_Used_8.ID, Spray_Color_Used_9.ID,
        Spray_Color_Used_10.ID, Spray_Color_Used_11.ID, Spray_Color_Used_12.ID, Spray_Color_Used_13.ID,
        Spray_Color_Used_14.ID, Spray_Color_Used_15.ID };

    public MetaGeneratedItem01() {
        super(
            "metaitem.01",
            OrePrefixes.dustTiny,
            OrePrefixes.dustSmall,
            OrePrefixes.dust,
            OrePrefixes.dustImpure,
            OrePrefixes.dustPure,
            OrePrefixes.crushed,
            OrePrefixes.crushedPurified,
            OrePrefixes.crushedCentrifuged,
            OrePrefixes.gem,
            OrePrefixes.nugget,
            null,
            OrePrefixes.ingot,
            OrePrefixes.ingotHot,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.plate,
            OrePrefixes.plateDouble,
            OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple,
            OrePrefixes.plateQuintuple,
            OrePrefixes.plateDense,
            OrePrefixes.stick,
            OrePrefixes.lens,
            OrePrefixes.round,
            OrePrefixes.bolt,
            OrePrefixes.screw,
            OrePrefixes.ring,
            OrePrefixes.foil,
            OrePrefixes.cell,
            OrePrefixes.cellPlasma);
        INSTANCE = this;

        ItemList.Credit_Greg_Copper
            .set(addItem(IDMetaItem01.Credit_Greg_Copper.ID, "Copper GT Credit", "0.125 Credits"));
        ItemList.Credit_Greg_Cupronickel.set(
            addItem(
                IDMetaItem01.Credit_Greg_Cupronickel.ID,
                "Cupronickel GT Credit",
                "1 Credit",
                new ItemData(Materials.Cupronickel, 907200L)));
        ItemList.Credit_Greg_Silver.set(
            addItem(
                IDMetaItem01.Credit_Greg_Silver.ID,
                "Silver GT Credit",
                "8 Credits",
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Credit_Greg_Gold.set(addItem(IDMetaItem01.Credit_Greg_Gold.ID, "Gold GT Credit", "64 Credits"));
        ItemList.Credit_Greg_Platinum
            .set(addItem(IDMetaItem01.Credit_Greg_Platinum.ID, "Platinum GT Credit", "512 Credits"));
        ItemList.Credit_Greg_Osmium
            .set(addItem(IDMetaItem01.Credit_Greg_Osmium.ID, "Osmium GT Credit", "4,096 Credits"));
        ItemList.Credit_Greg_Naquadah
            .set(addItem(IDMetaItem01.Credit_Greg_Naquadah.ID, "Naquadah GT Credit", "32,768 Credits"));
        ItemList.Credit_Greg_Neutronium
            .set(addItem(IDMetaItem01.Credit_Greg_Neutronium.ID, "Neutronium GT Credit", "262,144 Credits"));
        ItemList.Coin_Gold_Ancient.set(
            addItem(
                IDMetaItem01.Coin_Gold_Ancient.ID,
                "Ancient Gold Coin",
                "Found in ancient Ruins",
                new ItemData(Materials.Gold, 907200L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 8L)));
        ItemList.Coin_Doge.set(
            addItem(
                IDMetaItem01.Coin_Doge.ID,
                "Doge Coin",
                "wow much coin how money so crypto plz mine v rich very currency wow",
                new ItemData(Materials.Brass, 907200L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Coin_Chocolate.set(
            addItem(
                IDMetaItem01.Coin_Chocolate.ID,
                "Chocolate Coin",
                "Wrapped in Gold",
                new ItemData(Materials.Gold, OrePrefixes.foil.getMaterialAmount()),
                new GTFoodStat(
                    1,
                    0.1F,
                    EnumAction.eat,
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 1L),
                    true,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    200,
                    1,
                    100)));
        ItemList.Credit_Copper.set(addItem(IDMetaItem01.Credit_Copper.ID, "Industrial Copper Credit", "0.125 Credits"));

        ItemList.Credit_Silver.set(
            addItem(
                IDMetaItem01.Credit_Silver.ID,
                "Industrial Silver Credit",
                "8 Credits",
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Credit_Gold.set(addItem(IDMetaItem01.Credit_Gold.ID, "Industrial Gold Credit", "64 Credits"));
        ItemList.Credit_Platinum
            .set(addItem(IDMetaItem01.Credit_Platinum.ID, "Industrial Platinum Credit", "512 Credits"));
        ItemList.Credit_Osmium.set(addItem(IDMetaItem01.Credit_Osmium.ID, "Industrial Osmium Credit", "4096 Credits"));

        ItemList.NandChipArray.set(
            addItem(
                IDMetaItem01.NandChipArray.ID,
                "NAND Chip Array",
                "Chips on Board",
                "circuitPrimitiveArray",
                SubTag.NO_UNIFICATION));
        ItemList.Component_Minecart_Wheels_Iron.set(
            addItem(
                Component_Minecraft_Wheels_Iron.ID,
                "Iron Minecart Wheels",
                "To get things rolling",
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L)));
        ItemList.Component_Minecart_Wheels_Steel.set(
            addItem(
                Component_Minecraft_Wheels_Steel.ID,
                "Steel Minecart Wheels",
                "To get things rolling",
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L)));

        GTModHandler.addCraftingRecipe(
            ItemList.Component_Minecart_Wheels_Iron.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " h ", "RSR", " w ", 'R', OrePrefixes.ring.get(Materials.AnyIron), 'S',
                OrePrefixes.stick.get(Materials.AnyIron) });
        GTModHandler.addCraftingRecipe(
            ItemList.Component_Minecart_Wheels_Steel.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " h ", "RSR", " w ", 'R', OrePrefixes.ring.get(Materials.Steel), 'S',
                OrePrefixes.stick.get(Materials.Steel) });

        ItemList.CompressedFireclay.set(addItem(Compressed_Fireclay.ID, "Compressed Fireclay", "Brick-shaped"));
        GTOreDictUnificator.addItemDataFromInputs(ItemList.CompressedFireclay.get(1), Materials.Fireclay.getDust(1));

        ItemList.Firebrick.set(addItem(Firebrick.ID, "Firebrick", "Heat resistant"));
        GTOreDictUnificator.addItemDataFromInputs(ItemList.Firebrick.get(1), Materials.Fireclay.getDust(1));

        ItemList.Shape_Empty.set(
            addItem(
                Shape_Empty.ID,
                "Empty Shape Plate",
                "Raw Plate to make Molds and Extruder Shapes",
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L)));

        ItemList.Shape_Mold_Plate.set(addItem(Shape_Mold_Plate.ID, "Mold (Plate)", "Mold for making Plates"));
        ItemList.Shape_Mold_Casing.set(addItem(Shape_Mold_Casing.ID, "Mold (Casing)", "Mold for making Item Casings"));
        ItemList.Shape_Mold_Gear.set(addItem(Shape_Mold_Gear.ID, "Mold (Gear)", "Mold for making Gears"));
        ItemList.Shape_Mold_Credit
            .set(addItem(Shape_Mold_Credit.ID, "Mold (Coinage)", "Secure Mold for making Coins (Don't lose it!)"));
        ItemList.Shape_Mold_Bottle.set(addItem(Shape_Mold_Bottle.ID, "Mold (Bottle)", "Mold for making Bottles"));
        ItemList.Shape_Mold_Ingot.set(addItem(Shape_Mold_Ingot.ID, "Mold (Ingot)", "Mold for making Ingots"));
        ItemList.Shape_Mold_Ball.set(addItem(Shape_Mold_Ball.ID, "Mold (Ball)", "Mold for making Balls"));
        ItemList.Shape_Mold_Block.set(addItem(Shape_Mold_Block.ID, "Mold (Block)", "Mold for making Blocks"));
        ItemList.Shape_Mold_Nugget.set(addItem(Shape_Mold_Nugget.ID, "Mold (Nuggets)", "Mold for making Nuggets"));
        ItemList.Shape_Mold_Bun.set(addItem(Shape_Mold_Bun.ID, "Mold (Buns)", "Mold for shaping Buns"));
        ItemList.Shape_Mold_Bread.set(addItem(Shape_Mold_Bread.ID, "Mold (Bread)", "Mold for shaping Breads"));
        ItemList.Shape_Mold_Baguette
            .set(addItem(Shape_Mold_Baguette.ID, "Mold (Baguette)", "Mold for shaping Baguettes"));
        ItemList.Shape_Mold_Cylinder
            .set(addItem(Shape_Mold_Cylinder.ID, "Mold (Cylinder)", "Mold for shaping Cylinders"));
        ItemList.Shape_Mold_Anvil.set(addItem(Shape_Mold_Anvil.ID, "Mold (Anvil)", "Mold for shaping Anvils"));
        ItemList.Shape_Mold_Name
            .set(addItem(Shape_Mold_Name.ID, "Mold (Name)", "Mold for naming Items (rename Mold with Anvil)"));
        ItemList.Shape_Mold_Arrow.set(addItem(Shape_Mold_Arrow.ID, "Mold (Arrow Head)", "Mold for making Arrow Heads"));
        ItemList.Shape_Mold_Gear_Small
            .set(addItem(Shape_Mold_Gear_Small.ID, "Mold (Small Gear)", "Mold for making small Gears"));
        ItemList.Shape_Mold_Rod.set(addItem(Shape_Mold_Rod.ID, "Mold (Rod)", "Mold for making Rods"));
        ItemList.Shape_Mold_Bolt.set(addItem(Shape_Mold_Bolt.ID, "Mold (Bolt)", "Mold for making Bolts"));
        ItemList.Shape_Mold_Round.set(addItem(Shape_Mold_Round.ID, "Mold (Round)", "Mold for making Rounds"));
        ItemList.Shape_Mold_Screw.set(addItem(Shape_Mold_Screw.ID, "Mold (Screw)", "Mold for making Screws"));
        ItemList.Shape_Mold_Ring.set(addItem(Shape_Mold_Ring.ID, "Mold (Ring)", "Mold for making Rings"));
        ItemList.Shape_Mold_Rod_Long
            .set(addItem(Shape_Mold_Rod_Long.ID, "Mold (Long Rod)", "Mold for making Long Rods"));
        ItemList.Shape_Mold_Rotor.set(addItem(Shape_Mold_Rotor.ID, "Mold (Rotor)", "Mold for making a Rotor"));
        ItemList.Shape_Mold_Turbine_Blade
            .set(addItem(Shape_Mold_Turbine_Blade.ID, "Mold (Turbine Blade)", "Mold for making a Turbine Blade"));
        ItemList.Shape_Mold_Pipe_Tiny
            .set(addItem(Shape_Mold_Pipe_Tiny.ID, "Mold (Tiny Pipe)", "Mold for making tiny Pipes"));
        ItemList.Shape_Mold_Pipe_Small
            .set(addItem(Shape_Mold_Pipe_Small.ID, "Mold (Small Pipe)", "Mold for making small Pipes"));
        ItemList.Shape_Mold_Pipe_Medium
            .set(addItem(Shape_Mold_Pipe_Medium.ID, "Mold (Normal Pipe)", "Mold for making Pipes"));
        ItemList.Shape_Mold_Pipe_Large
            .set(addItem(Shape_Mold_Pipe_Large.ID, "Mold (Large Pipe)", "Mold for making large Pipes"));
        ItemList.Shape_Mold_Pipe_Huge
            .set(addItem(Shape_Mold_Pipe_Huge.ID, "Mold (Huge Pipe)", "Mold for making full Block Pipes"));
        ItemList.Shape_Mold_ToolHeadDrill
            .set(addItem(Shape_Mold_Tool_Head_Drill.ID, "Mold (Drill Head)", "Mold for making Drill Heads"));

        ItemList.Shape_Extruder_Plate
            .set(addItem(Shape_Extruder_Plate.ID, "Extruder Shape (Plate)", "Extruder Shape for making Plates"));
        ItemList.Shape_Extruder_Rod
            .set(addItem(Shape_Extruder_Rod.ID, "Extruder Shape (Rod)", "Extruder Shape for making Rods"));
        ItemList.Shape_Extruder_Bolt
            .set(addItem(Shape_Extruder_Bolt.ID, "Extruder Shape (Bolt)", "Extruder Shape for making Bolts"));
        ItemList.Shape_Extruder_Ring
            .set(addItem(Shape_Extruder_Ring.ID, "Extruder Shape (Ring)", "Extruder Shape for making Rings"));
        ItemList.Shape_Extruder_Cell
            .set(addItem(Shape_Extruder_Cell.ID, "Extruder Shape (Cell)", "Extruder Shape for making Cells"));
        ItemList.Shape_Extruder_Ingot.set(
            addItem(
                Shape_Extruder_Ingot.ID,
                "Extruder Shape (Ingot)",
                "Extruder Shape for, wait, can't we just use a Furnace?"));
        ItemList.Shape_Extruder_Wire.set(
            addItem(
                Shape_Extruder_Wire.ID,
                "Extruder Shape (Wire)",
                EnumChatFormatting.YELLOW + "DEPRECATED! Will be removed in next major update."));
        ItemList.Shape_Extruder_Casing.set(
            addItem(Shape_Extruder_Casing.ID, "Extruder Shape (Casing)", "Extruder Shape for making Item Casings"));
        ItemList.Shape_Extruder_Pipe_Tiny.set(
            addItem(Shape_Extruder_Pipe_Tiny.ID, "Extruder Shape (Tiny Pipe)", "Extruder Shape for making tiny Pipes"));
        ItemList.Shape_Extruder_Pipe_Small.set(
            addItem(
                Shape_Extruder_Pipe_Small.ID,
                "Extruder Shape (Small Pipe)",
                "Extruder Shape for making small Pipes"));
        ItemList.Shape_Extruder_Pipe_Medium.set(
            addItem(Shape_Extruder_Pipe_Medium.ID, "Extruder Shape (Normal Pipe)", "Extruder Shape for making Pipes"));
        ItemList.Shape_Extruder_Pipe_Large.set(
            addItem(
                Shape_Extruder_Pipe_Large.ID,
                "Extruder Shape (Large Pipe)",
                "Extruder Shape for making large Pipes"));
        ItemList.Shape_Extruder_Pipe_Huge.set(
            addItem(
                Shape_Extruder_Pipe_Huge.ID,
                "Extruder Shape (Huge Pipe)",
                "Extruder Shape for making full Block Pipes"));
        ItemList.Shape_Extruder_Block
            .set(addItem(Shape_Extruder_Block.ID, "Extruder Shape (Block)", "Extruder Shape for making Blocks"));
        ItemList.Shape_Extruder_Sword
            .set(addItem(Shape_Extruder_Sword.ID, "Extruder Shape (Sword Blade)", "Extruder Shape for making Swords"));
        ItemList.Shape_Extruder_Pickaxe.set(
            addItem(Shape_Extruder_Pickaxe.ID, "Extruder Shape (Pickaxe Head)", "Extruder Shape for making Pickaxes"));
        ItemList.Shape_Extruder_Shovel.set(
            addItem(Shape_Extruder_Shovel.ID, "Extruder Shape (Shovel Head)", "Extruder Shape for making Shovels"));
        ItemList.Shape_Extruder_Axe
            .set(addItem(Shape_Extruder_Axe.ID, "Extruder Shape (Axe Head)", "Extruder Shape for making Axes"));
        ItemList.Shape_Extruder_Hoe
            .set(addItem(Shape_Extruder_Hoe.ID, "Extruder Shape (Hoe Head)", "Extruder Shape for making Hoes"));
        ItemList.Shape_Extruder_Hammer.set(
            addItem(Shape_Extruder_Hammer.ID, "Extruder Shape (Hammer Head)", "Extruder Shape for making Hammers"));
        ItemList.Shape_Extruder_File
            .set(addItem(Shape_Extruder_File.ID, "Extruder Shape (File Head)", "Extruder Shape for making Files"));
        ItemList.Shape_Extruder_Saw
            .set(addItem(Shape_Extruder_Saw.ID, "Extruder Shape (Saw Blade)", "Extruder Shape for making Saws"));
        ItemList.Shape_Extruder_Gear
            .set(addItem(Shape_Extruder_Gear.ID, "Extruder Shape (Gear)", "Extruder Shape for making Gears"));
        ItemList.Shape_Extruder_Bottle
            .set(addItem(Shape_Extruder_Bottle.ID, "Extruder Shape (Bottle)", "Extruder Shape for making Bottles"));
        ItemList.Shape_Extruder_Rotor
            .set(addItem(Shape_Extruder_Rotor.ID, "Extruder Shape (Rotor)", "Extruder Shape for a Rotor"));
        ItemList.Shape_Extruder_Small_Gear.set(
            addItem(Shape_Extruder_Small_Gear.ID, "Extruder Shape (Small Gear)", "Extruder Shape for a Small Gear"));
        ItemList.Shape_Extruder_Turbine_Blade.set(
            addItem(
                Shape_Extruder_Turbine_Blade.ID,
                "Extruder Shape (Turbine Blade)",
                "Extruder Shape for a Turbine Blade"));
        ItemList.Shape_Extruder_ToolHeadDrill.set(
            addItem(
                Shape_Extruder_Tool_Head_Drill.ID,
                "Extruder Shape (Drill Head)",
                "Extruder Shape for a Drill Head"));

        ItemList.Shape_Slicer_Flat
            .set(addItem(Shape_Slicer_Flat.ID, "Slicer Blade (Flat)", "Slicer Blade for cutting Flat"));
        ItemList.Shape_Slicer_Stripes
            .set(addItem(Shape_Slicer_Stripes.ID, "Slicer Blade (Stripes)", "Slicer Blade for cutting Stripes"));

        ItemList.Fuel_Can_Plastic_Empty.set(
            addItem(
                Fuel_Can_Plastic_Empty.ID,
                "Empty Plastic Fuel Can",
                "Used to store Fuels",
                new ItemData(Materials.Polyethylene, OrePrefixes.plate.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));
        ItemList.Fuel_Can_Plastic_Filled.set(
            addItem(
                Fuel_Can_Plastic_Filled.ID,
                "Plastic Fuel Can",
                "Burns well in Diesel Generators",
                new ItemData(Materials.Polyethylene, OrePrefixes.plate.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));

        ItemList.Spray_Empty.set(
            addItem(
                Spray_Empty.ID,
                "Empty Spray Can",
                "Used for making Sprays",
                new ItemData(
                    Materials.Tin,
                    OrePrefixes.plate.getMaterialAmount() * 2L,
                    Materials.Redstone,
                    OrePrefixes.dust.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));

        ItemList.ThermosCan_Empty.set(
            addItem(
                Thermos_Can_Empty.ID,
                "Empty Thermos Can",
                "Keeping hot things hot and cold things cold",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.getMaterialAmount() + 2L * OrePrefixes.ring.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L)));

        ItemList.Large_Fluid_Cell_Steel.set(
            addItem(
                Large_Fluid_Cell_Steel.ID,
                "Large Steel Fluid Cell",
                "",
                new ItemData(
                    Materials.Steel,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Bronze, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));

        ItemList.Large_Fluid_Cell_TungstenSteel.set(
            addItem(
                Large_Fluid_Cell_TungstenSteel.ID,
                "Large Tungstensteel Fluid Cell",
                "",
                new ItemData(
                    Materials.TungstenSteel,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Platinum, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 9L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 7L)));

        ItemList.Large_Fluid_Cell_Aluminium.set(
            addItem(
                Large_Fluid_Cell_Aluminium.ID,
                "Large Aluminium Fluid Cell",
                "",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Silver, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 5L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 3L)));

        ItemList.Large_Fluid_Cell_StainlessSteel.set(
            addItem(
                Large_Fluid_Cell_StainlessSteel.ID,
                "Large Stainless Steel Fluid Cell",
                "",
                new ItemData(
                    Materials.StainlessSteel,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Electrum, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 6L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));

        ItemList.Large_Fluid_Cell_Titanium.set(
            addItem(
                Large_Fluid_Cell_Titanium.ID,
                "Large Titanium Fluid Cell",
                "",
                new ItemData(
                    Materials.Titanium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.RoseGold, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 7L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 5L)));

        ItemList.Large_Fluid_Cell_Chrome.set(
            addItem(
                Large_Fluid_Cell_Chrome.ID,
                "Large Chrome Fluid Cell",
                "",
                new ItemData(
                    Materials.Chrome,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Palladium, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 6L)));

        ItemList.Large_Fluid_Cell_Iridium.set(
            addItem(
                Large_Fluid_Cell_Iridium.ID,
                "Large Iridium Fluid Cell",
                "",
                new ItemData(
                    Materials.Iridium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Naquadah, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));

        ItemList.Large_Fluid_Cell_Osmium.set(
            addItem(
                Large_Fluid_Cell_Osmium.ID,
                "Large Osmium Fluid Cell",
                "",
                new ItemData(
                    Materials.Osmium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.ElectrumFlux, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 11L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 9L)));

        ItemList.Large_Fluid_Cell_Neutronium.set(
            addItem(
                Large_Fluid_Cell_Neutronium.ID,
                "Large Neutronium Fluid Cell",
                "",
                new ItemData(
                    Materials.Neutronium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Draconium, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 12L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 10L)));

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            ItemList.SPRAY_CAN_DYES[i].set(
                addItem(
                    Spray_Colors[i],
                    "Spray Can (" + Dyes.get(i).mName + ")",
                    "Full",
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4L)));
            ItemList.SPRAY_CAN_DYES_USED[i].set(
                addItem(
                    Spray_Colors_Used[i],
                    "Spray Can (" + Dyes.get(i).mName + ")",
                    "Used",
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 3L),
                    SubTag.INVISIBLE));
        }

        ItemList.Spray_Color_Remover.set(
            addItem(
                Spray_Color_Remover.ID,
                "Spray Can Solvent",
                "Full",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 8L)));
        ItemList.Spray_Color_Used_Remover.set(
            addItem(
                Spray_Color_Used_Remover.ID,
                "Spray Can Solvent",
                "Used",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 3L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 6L),
                SubTag.INVISIBLE));

        ItemList.Spray_Color_Remover_Empty.set(
            addItem(
                Spray_Color_Remover_Empty.ID,
                "Empty Spray Can Solvent Canister",
                "Used for making Spray Can Solvent",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    Materials.Redstone,
                    OrePrefixes.dust.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));

        ItemList.Spray_Color_Infinite.set(
            addItem(
                Spray_Color_Infinite.ID,
                "Infinite Spray Can",
                "Contains all sixteen colors, as well as solvent!",
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 16),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 8),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 8),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 4)));

        ItemList.Tool_Matches.set(
            addItem(
                Tool_Matches.ID,
                "Match",
                "",
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));

        ItemList.Tool_MatchBox_Used.set(
            addItem(
                Tool_MatchBox_Used.ID,
                "Match Box",
                "This is not a Car",
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                SubTag.INVISIBLE));

        ItemList.Tool_MatchBox_Full.set(
            addItem(
                Tool_MatchBox_Full.ID,
                "Match Box (Full)",
                "This is not a Car",
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Tool_Lighter_Invar_Empty.set(
            addItem(
                Tool_Lighter_Invar_Empty.ID,
                "Lighter (Empty)",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Invar_Used.set(
            addItem(
                Tool_Lighter_Invar_Used.ID,
                "Lighter",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Invar_Full.set(
            addItem(
                Tool_Lighter_Invar_Full.ID,
                "Lighter (Full)",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Tool_Lighter_Platinum_Empty.set(
            addItem(
                Tool_Lighter_Platinum_Empty.ID,
                "Platinum Lighter (Empty)",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Platinum_Used.set(
            addItem(
                Tool_Lighter_Platinum_Used.ID,
                "Platinum Lighter",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Platinum_Full.set(
            addItem(
                Tool_Lighter_Platinum_Full.ID,
                "Platinum Lighter (Full)",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Ingot_Heavy1
            .set(addItem(Ingot_Heavy1.ID, "Heavy Duty Alloy Ingot T1", "Used to make Heavy Duty Plates T1"));
        ItemList.Ingot_Heavy2
            .set(addItem(Ingot_Heavy2.ID, "Heavy Duty Alloy Ingot T2", "Used to make Heavy Duty Plates T2"));
        ItemList.Ingot_Heavy3
            .set(addItem(Ingot_Heavy3.ID, "Heavy Duty Alloy Ingot T3", "Used to make Heavy Duty Plates T3"));

        ItemList.Ingot_IridiumAlloy.set(
            addItem(
                Ingot_Iridium_Alloy.ID,
                "Iridium Alloy Ingot",
                "Used to make Iridium Plates",
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L)));

        ItemList.Paper_Printed_Pages.set(
            addItem(
                Paper_Printed_Pages.ID,
                "Printed Pages",
                "Used to make written Books",
                new ItemData(Materials.Paper, 10886400L),
                new BehaviourPrintedPages(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Paper_Magic_Empty.set(
            addItem(
                Paper_Magic_Empty.ID,
                "Magic Paper",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1L)));
        ItemList.Paper_Magic_Page.set(
            addItem(
                Paper_Magic_Page.ID,
                "Enchanted Page",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 2L)));
        ItemList.Paper_Magic_Pages.set(
            addItem(
                Paper_Magic_Pages.ID,
                "Enchanted Pages",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 10886400L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 4L)));
        ItemList.Paper_Punch_Card_Empty.set(
            addItem(
                Paper_Punch_Card_Empty.ID,
                "Punch Card",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L)));
        ItemList.Paper_Punch_Card_Encoded.set(
            addItem(
                Paper_Punch_Card_Encoded.ID,
                "Punched Card",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Book_Written_01.set(
            addItem(
                Book_Written_01.ID,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new BehaviourWrittenBook(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Book_Written_02.set(
            addItem(
                Book_Written_02.ID,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new BehaviourWrittenBook(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Book_Written_03.set(
            addItem(
                Book_Written_03.ID,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new BehaviourWrittenBook(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));

        ItemList.Schematic.set(
            addItem(
                Schematic.ID,
                "Schematic",
                "EMPTY",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 1L)));
        ItemList.Schematic_Crafting.set(
            addItem(
                Schematic_Crafting.ID,
                "Schematic (Crafting)",
                "Crafts the Programmed Recipe",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_1by1.set(
            addItem(
                Schematic_1by1.ID,
                "Schematic (1x1)",
                "Crafts 1 Items as 1x1 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_2by2.set(
            addItem(
                Schematic_2by2.ID,
                "Schematic (2x2)",
                "Crafts 4 Items as 2x2 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_3by3.set(
            addItem(
                Schematic_3by3.ID,
                "Schematic (3x3)",
                "Crafts 9 Items as 3x3 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_Dust.set(
            addItem(
                Schematic_Dust.ID,
                "Schematic (Dusts)",
                "Combines Dusts (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_Dust_Small.set(
            addItem(
                Schematic_Dust_Small.ID,
                "Schematic (Small Dusts)",
                "Splits Dusts into 4 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Battery_Hull_LV.set(
            addItem(
                Battery_Hull_LV.ID,
                "Small Battery Hull",
                "An empty LV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Battery_Hull_MV.set(
            addItem(
                Battery_Hull_MV.ID,
                "Medium Battery Hull",
                "An empty MV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.getMaterialAmount() * 3L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Battery_Hull_HV.set(
            addItem(
                Battery_Hull_HV.ID,
                "Large Battery Hull",
                "An empty HV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.getMaterialAmount() * 9L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));

        // ULV Batteries
        ItemList.Battery_RE_ULV_Tantalum.set(
            addItem(
                Battery_RE_ULV_Tantalum.ID,
                "Tantalum Capacitor",
                "Reusable",
                "batteryULV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));

        // LV Batteries
        ItemList.Battery_SU_LV_SulfuricAcid.set(
            addItem(
                Battery_SU_LV_Sulfuric_Acid.ID,
                "Small Acid Battery",
                "Single Use",
                "batteryLV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_SU_LV_Mercury.set(
            addItem(
                Battery_SU_LV_Mercury.ID,
                "Small Mercury Battery",
                "Single Use",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_RE_LV_Cadmium.set(
            addItem(
                Battery_RE_LV_Cadmium.ID,
                "Small Cadmium Battery",
                "Reusable",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                "batteryLV"));

        ItemList.Battery_RE_LV_Lithium.set(
            addItem(
                Battery_RE_LV_Lithium.ID,
                "Small Lithium Battery",
                "Reusable",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                "batteryLV"));

        ItemList.Battery_RE_LV_Sodium.set(
            addItem(
                Battery_RE_LV_Sodium.ID,
                "Small Sodium Battery",
                "Reusable",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                "batteryLV"));

        // MV Batteries
        ItemList.Battery_SU_MV_SulfuricAcid.set(
            addItem(
                Battery_SU_MV_Sulfuric_Acid.ID,
                "Medium Acid Battery",
                "Single Use",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_SU_MV_Mercury.set(
            addItem(
                Battery_SU_MV_Mercury.ID,
                "Medium Mercury Battery",
                "Single Use",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_RE_MV_Cadmium.set(
            addItem(
                Battery_RE_MV_Cadmium.ID,
                "Medium Cadmium Battery",
                "Reusable",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_RE_MV_Lithium.set(
            addItem(
                Battery_RE_MV_Lithium.ID,
                "Medium Lithium Battery",
                "Reusable",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_RE_MV_Sodium.set(
            addItem(
                Battery_RE_MV_Sodium.ID,
                "Medium Sodium Battery",
                "Reusable",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        // HV Batteries
        ItemList.Battery_SU_HV_SulfuricAcid.set(
            addItem(
                Battery_SU_HV_Sulfuric_Acid.ID,
                "Large Acid Battery",
                "Single Use",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 8L)));

        ItemList.Battery_SU_HV_Mercury.set(
            addItem(
                Battery_SU_HV_Mercury.ID,
                "Large Mercury Battery",
                "Single Use",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 8L)));

        ItemList.Battery_RE_HV_Cadmium.set(
            addItem(
                Battery_RE_HV_Cadmium.ID,
                "Large Cadmium Battery",
                "Reusable",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_RE_HV_Lithium.set(
            addItem(
                Battery_RE_HV_Lithium.ID,
                "Large Lithium Battery",
                "Reusable",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_RE_HV_Sodium.set(
            addItem(
                Battery_RE_HV_Sodium.ID,
                "Large Sodium Battery",
                "Reusable",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        // IV Battery
        ItemList.Energy_LapotronicOrb.set(
            addItem(
                Energy_Lapotronic_Orb.ID,
                "Lapotronic Energy Orb",
                "Reusable battery",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.IV)));

        // ZPM Module
        ItemList.ZPM.set(
            addItem(
                IDMetaItem01.ZPM.ID,
                "Zero Point Module",
                "Single use battery",
                "batteryZPM",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // LuV Lapotron orb cluster battery
        ItemList.Energy_LapotronicOrb2.set(
            addItem(
                Energy_Lapotronic_orb_2.ID,
                "Lapotronic Energy Orb Cluster",
                "Reusable battery",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.LuV)));

        // UV Battery
        ItemList.ZPM2.set(
            addItem(
                ZPM2.ID,
                "Ultimate Battery",
                "Fill this to win minecraft",
                "batteryUV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // UMV Battery
        ItemList.ZPM3.set(
            addItem(
                ZPM3.ID,
                "Really Ultimate Battery",
                "Fill this to be way older",
                "batteryUMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // UXV Battery
        ItemList.ZPM4.set(
            addItem(
                ZPM4.ID,
                "Extremely Ultimate Battery",
                "Fill this to be older",
                "batteryUXV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // MAX Battery
        ItemList.ZPM5.set(
            addItem(
                ZPM5.ID,
                "Insanely Ultimate Battery",
                "Fill this for fun",
                "batteryMAX",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // ERROR Battery
        ItemList.ZPM6.set(
            addItem(
                ZPM6.ID,
                "Mega Ultimate Battery",
                "Fill the capacitor to reach enlightenment",
                "batteryERV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // ZPM Cluster
        ItemList.Energy_Module.set(
            addItem(
                Energy_Module.ID,
                "Energy Module",
                "Reusable battery",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.ZPM)));

        // UV Cluster
        ItemList.Energy_Cluster.set(
            addItem(
                Energy_Cluster.ID,
                "Energy Cluster",
                "Reusable battery",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                OrePrefixes.battery.get(Materials.UV)));

        // UIV, UMV, UXV and MAX component textures backported from gregicality.
        ItemList.Electric_Motor_LV.set(
            addItem(
                Electric_Motor_LV.ID,
                "Electric Motor (LV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));
        ItemList.Electric_Motor_MV.set(
            addItem(
                Electric_Motor_MV.ID,
                "Electric Motor (MV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L)));
        ItemList.Electric_Motor_HV.set(
            addItem(
                Electric_Motor_HV.ID,
                "Electric Motor (HV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 4L)));
        ItemList.Electric_Motor_EV.set(
            addItem(
                Electric_Motor_EV.ID,
                "Electric Motor (EV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 8L)));
        ItemList.Electric_Motor_IV.set(
            addItem(
                Electric_Motor_IV.ID,
                "Electric Motor (IV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 16L)));
        ItemList.Electric_Motor_LuV.set(
            addItem(
                Electric_Motor_LuV.ID,
                "Electric Motor (LuV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 32L)));
        ItemList.Electric_Motor_ZPM.set(
            addItem(
                Electric_Motor_ZPM.ID,
                "Electric Motor (ZPM)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 64L)));
        ItemList.Electric_Motor_UV.set(
            addItem(
                Electric_Motor_UV.ID,
                "Electric Motor (UV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 128L)));
        ItemList.Electric_Motor_UHV
            .set(
                addItem(
                    Electric_Motor_UHV.ID,
                    "Electric Motor (UHV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Electric_Motor_UEV
            .set(
                addItem(
                    Electric_Motor_UEV.ID,
                    "Electric Motor (UEV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Electric_Motor_UIV
            .set(
                addItem(
                    Electric_Motor_UIV.ID,
                    "Electric Motor (UIV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Electric_Motor_UMV.set(
            addItem(
                Electric_Motor_UMV.ID,
                "Electric Motor (UMV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UXV.set(
            addItem(
                Electric_Motor_UXV.ID,
                "Electric Motor (UXV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Motor_MAX.set(
            addItem(
                Electric_Motor_MAX.ID,
                "Electric Motor (MAX)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));

        ItemList.ElectronicsLump.set(
            addItem(
                414,
                "Lump of Electronics",
                "How did they even produce this?",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));

        ItemList.Tesseract
            .set(
                addItem(
                    Tesseract.ID,
                    "Raw Tesseract",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)))
            .setRender(new WireFrameTesseractRenderer(0, 0, 0));
        ItemList.GigaChad.set(
            addItem(
                GigaChad.ID,
                "Giga Chad Token",
                "You are worthy",
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1000L)));
        ItemList.EnergisedTesseract
            .set(
                addItem(
                    EnergisedTesseract.ID,
                    "Energised Tesseract",
                    "Higher dimensional engineering",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)))
            .setRender(new WireFrameTesseractRenderer(23, 129, 166));

        ItemList.Electric_Piston_LV.set(
            addItem(
                Electric_Piston_LV.ID,
                "Electric Piston (LV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));
        ItemList.Electric_Piston_MV.set(
            addItem(
                Electric_Piston_MV.ID,
                "Electric Piston (MV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L)));
        ItemList.Electric_Piston_HV.set(
            addItem(
                Electric_Piston_HV.ID,
                "Electric Piston (HV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 4L)));
        ItemList.Electric_Piston_EV.set(
            addItem(
                Electric_Piston_EV.ID,
                "Electric Piston (EV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 8L)));
        ItemList.Electric_Piston_IV.set(
            addItem(
                Electric_Piston_IV.ID,
                "Electric Piston (IV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 16L)));
        ItemList.Electric_Piston_LuV.set(
            addItem(
                Electric_Piston_LuV.ID,
                "Electric Piston (LuV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 32L)));
        ItemList.Electric_Piston_ZPM.set(
            addItem(
                Electric_Piston_ZPM.ID,
                "Electric Piston (ZPM)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 64L)));
        ItemList.Electric_Piston_UV.set(
            addItem(
                Electric_Piston_UV.ID,
                "Electric Piston (UV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 128L)));
        ItemList.Electric_Piston_UHV
            .set(
                addItem(
                    Electric_Piston_UHV.ID,
                    "Electric Piston (UHV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Electric_Piston_UEV
            .set(
                addItem(
                    Electric_Piston_UEV.ID,
                    "Electric Piston (UEV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Electric_Piston_UIV
            .set(
                addItem(
                    Electric_Piston_UIV.ID,
                    "Electric Piston (UIV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Electric_Piston_UMV.set(
            addItem(
                Electric_Piston_UMV.ID,
                "Electric Piston (UMV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UXV.set(
            addItem(
                Electric_Piston_UXV.ID,
                "Electric Piston (UXV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Piston_MAX.set(
            addItem(
                Electric_Piston_MAX.ID,
                "Electric Piston (MAX)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));

        ItemList.Electric_Pump_LV.set(
            addItem(
                Electric_Pump_LV.ID,
                "Electric Pump (LV)",
                formatNumber(32) + PartCoverText + formatNumber(32 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Electric_Pump_MV.set(
            addItem(
                Electric_Pump_MV.ID,
                "Electric Pump (MV)",
                formatNumber(128) + PartCoverText + formatNumber(128 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.Electric_Pump_HV.set(
            addItem(
                Electric_Pump_HV.ID,
                "Electric Pump (HV)",
                formatNumber(512) + PartCoverText + formatNumber(512 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));
        ItemList.Electric_Pump_EV.set(
            addItem(
                Electric_Pump_EV.ID,
                "Electric Pump (EV)",
                formatNumber(2048) + PartCoverText + formatNumber(2048 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));
        ItemList.Electric_Pump_IV.set(
            addItem(
                Electric_Pump_IV.ID,
                "Electric Pump (IV)",
                formatNumber(8192) + PartCoverText + formatNumber(8192 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 16L)));
        ItemList.Electric_Pump_LuV.set(
            addItem(
                Electric_Pump_LuV.ID,
                "Electric Pump (LuV)",
                formatNumber(32768) + PartCoverText + formatNumber(32768 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 32L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 32L)));
        ItemList.Electric_Pump_ZPM.set(
            addItem(
                Electric_Pump_ZPM.ID,
                "Electric Pump (ZPM)",
                formatNumber(131072) + PartCoverText + formatNumber(131072 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 64L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 64L)));
        ItemList.Electric_Pump_UV.set(
            addItem(
                Electric_Pump_UV.ID,
                "Electric Pump (UV)",
                formatNumber(524288) + PartCoverText + formatNumber(524288 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 128L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 128L)));
        ItemList.Electric_Pump_UHV
            .set(
                addItem(
                    Electric_Pump_UHV.ID,
                    "Electric Pump (UHV)",
                    formatNumber(8388608) + PartCoverText + formatNumber(8388608 * 20) + PartCoverText2,
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Electric_Pump_UEV
            .set(
                addItem(
                    Electric_Pump_UEV.ID,
                    "Electric Pump (UEV)",
                    formatNumber(16777216) + PartCoverText + formatNumber(16777216 * 20) + PartCoverText2,
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Electric_Pump_UIV
            .set(
                addItem(
                    Electric_Pump_UIV.ID,
                    "Electric Pump (UIV)",
                    formatNumber(33554432) + PartCoverText + formatNumber(33554432 * 20) + PartCoverText2,
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Electric_Pump_UMV.set(
            addItem(
                Electric_Pump_UMV.ID,
                "Electric Pump (UMV)",
                formatNumber(67108864) + PartCoverText + formatNumber(67108864 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)));
        ItemList.Electric_Pump_UXV.set(
            addItem(
                Electric_Pump_UXV.ID,
                "Electric Pump (UXV)",
                formatNumber(134217728) + PartCoverText + formatNumber(134217728 * 20L) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)));
        ItemList.Electric_Pump_MAX.set(
            addItem(
                Electric_Pump_MAX.ID,
                "Electric Pump (MAX)",
                formatNumber(268435456) + PartCoverText + formatNumber(268435456 * 20L) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)));

        ItemList.Steam_Valve_LV.set(
            addItem(
                Steam_Valve_LV.ID,
                "Steam Valve (LV)",
                formatNumber(1024) + PartCoverText + formatNumber(1024 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Steam_Valve_MV.set(
            addItem(
                Steam_Valve_MV.ID,
                "Steam Valve (MV)",
                formatNumber(2048) + PartCoverText + formatNumber(2048 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.Steam_Valve_HV.set(
            addItem(
                Steam_Valve_HV.ID,
                "Steam Valve (HV)",
                formatNumber(4096) + PartCoverText + formatNumber(4096 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));
        ItemList.Steam_Valve_EV.set(
            addItem(
                Steam_Valve_EV.ID,
                "Steam Valve (EV)",
                formatNumber(8192) + PartCoverText + formatNumber(8192 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));
        ItemList.Steam_Valve_IV.set(
            addItem(
                Steam_Valve_IV.ID,
                "Steam Valve (IV)",
                formatNumber(16384) + PartCoverText + formatNumber(16384 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 16L)));

        ItemList.FluidRegulator_LV
            .set(addItem(FluidRegulator_LV.ID, "Fluid Regulator (LV)", FRText1 + formatNumber(32 * 20) + FRText2));
        ItemList.FluidRegulator_MV
            .set(addItem(FluidRegulator_MV.ID, "Fluid Regulator (MV)", FRText1 + formatNumber(128 * 20) + FRText2));
        ItemList.FluidRegulator_HV
            .set(addItem(FluidRegulator_HV.ID, "Fluid Regulator (HV)", FRText1 + formatNumber(512 * 20) + FRText2));
        ItemList.FluidRegulator_EV
            .set(addItem(FluidRegulator_EV.ID, "Fluid Regulator (EV)", FRText1 + formatNumber(2048 * 20) + FRText2));
        ItemList.FluidRegulator_IV
            .set(addItem(FluidRegulator_IV.ID, "Fluid Regulator (IV)", FRText1 + formatNumber(8192 * 20) + FRText2));
        ItemList.FluidRegulator_LuV
            .set(addItem(FluidRegulator_LuV.ID, "Fluid Regulator (LuV)", FRText1 + formatNumber(32768 * 20) + FRText2));
        ItemList.FluidRegulator_ZPM.set(
            addItem(FluidRegulator_ZPM.ID, "Fluid Regulator (ZPM)", FRText1 + formatNumber(131072 * 20) + FRText2));
        ItemList.FluidRegulator_UV
            .set(addItem(FluidRegulator_UV.ID, "Fluid Regulator (UV)", FRText1 + formatNumber(524288 * 20) + FRText2));
        ItemList.FluidRegulator_UHV
            .set(
                addItem(FluidRegulator_UHV.ID, "Fluid Regulator (UHV)", FRText1 + formatNumber(8388608 * 20) + FRText2))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.FluidRegulator_UEV.set(
            addItem(FluidRegulator_UEV.ID, "Fluid Regulator (UEV)", FRText1 + formatNumber(16777216 * 20) + FRText2))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.FluidRegulator_UIV.set(
            addItem(FluidRegulator_UIV.ID, "Fluid Regulator (UIV)", FRText1 + formatNumber(33554432 * 20) + FRText2))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.FluidRegulator_UMV.set(
            addItem(FluidRegulator_UMV.ID, "Fluid Regulator (UMV)", FRText1 + formatNumber(67108864 * 20) + FRText2));
        ItemList.FluidRegulator_UXV.set(
            addItem(FluidRegulator_UXV.ID, "Fluid Regulator (UXV)", FRText1 + formatNumber(134217728 * 20L) + FRText2));
        ItemList.FluidRegulator_MAX.set(
            addItem(FluidRegulator_MAX.ID, "Fluid Regulator (MAX)", FRText1 + formatNumber(268435456 * 20L) + FRText2));

        ItemList.FluidFilter.set(
            addItem(FluidFilter.ID, "Fluid Filter Cover", "Set with Fluid Container to only accept one Fluid Type"));

        ItemList.ItemFilter_Export.set(
            addItem(
                ItemFilter_Export.ID,
                "Filtered Conveyor Cover (Export)",
                "Right click with an item to set filter (Only supports Export Mode)"));

        ItemList.ItemFilter_Import.set(
            addItem(
                ItemFilter_Import.ID,
                "Filtered Conveyor Cover (Import)",
                "Right click with an item to set filter (Only supports Import Mode)"));

        ItemList.Cover_FluidLimiter
            .set(addItem(Cover_FluidLimiter.ID, "Fluid Limiter Cover", "Limits fluid input depending on fill level"));

        ItemList.Conveyor_Module_LV.set(
            addItem(
                Conveyor_Module_LV.ID,
                "Conveyor Module (LV)",
                "1 stack of 16 items every 5 secs (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));
        ItemList.Conveyor_Module_MV.set(
            addItem(
                Conveyor_Module_MV.ID,
                "Conveyor Module (MV)",
                "1 stack of 64 items every 5 secs (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L)));
        ItemList.Conveyor_Module_HV.set(
            addItem(
                Conveyor_Module_HV.ID,
                "Conveyor Module (HV)",
                "1 stack of 64 items every 1 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L)));
        ItemList.Conveyor_Module_EV.set(
            addItem(
                Conveyor_Module_EV.ID,
                "Conveyor Module (EV)",
                "1 stack of 64 items every 1/5 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L)));
        ItemList.Conveyor_Module_IV.set(
            addItem(
                Conveyor_Module_IV.ID,
                "Conveyor Module (IV)",
                "1 stack of 64 items every 1/20 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L)));
        ItemList.Conveyor_Module_LuV.set(
            addItem(
                Conveyor_Module_LuV.ID,
                "Conveyor Module (LuV)",
                "4 stacks of 64 items every 1/20 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 32L)));
        ItemList.Conveyor_Module_ZPM.set(
            addItem(
                Conveyor_Module_ZPM.ID,
                "Conveyor Module (ZPM)",
                "16 stacks of 64 items every 1/20 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 64L)));
        ItemList.Conveyor_Module_UV.set(
            addItem(
                Conveyor_Module_UV.ID,
                "Conveyor Module (UV)",
                "16 stacks of 256 items every 1/20 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 128L)));
        ItemList.Conveyor_Module_UHV
            .set(
                addItem(
                    Conveyor_Module_UHV.ID,
                    "Conveyor Module (UHV)",
                    "16 stacks of 1,024 items every 1/20 sec (as Cover)",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Conveyor_Module_UEV
            .set(
                addItem(
                    Conveyor_Module_UEV.ID,
                    "Conveyor Module (UEV)",
                    "16 stacks of 4,096 items every 1/20 sec (as Cover)",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Conveyor_Module_UIV
            .set(
                addItem(
                    Conveyor_Module_UIV.ID,
                    "Conveyor Module (UIV)",
                    "16 stacks of 16,384 items every 1/20 sec (as Cover)",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Conveyor_Module_UMV.set(
            addItem(
                Conveyor_Module_UMV.ID,
                "Conveyor Module (UMV)",
                "16 stacks of 65,536 items every 1/20 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)));
        ItemList.Conveyor_Module_UXV.set(
            addItem(
                Conveyor_Module_UXV.ID,
                "Conveyor Module (UXV)",
                "16 stacks of 262,144 items every 1/20 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)));
        ItemList.Conveyor_Module_MAX.set(
            addItem(
                Conveyor_Module_MAX.ID,
                "Conveyor Module (MAX)",
                "16 stacks of 2.1B items every 1/20 sec (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)));

        ItemList.Robot_Arm_LV.set(
            addItem(
                Robot_Arm_LV.ID,
                "Robot Arm (LV)",
                "1 stack every 20 secs (as Cover)/n " + RAText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L)));
        ItemList.Robot_Arm_MV.set(
            addItem(
                Robot_Arm_MV.ID,
                "Robot Arm (MV)",
                "1 stack every 5 secs (as Cover)/n " + RAText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Robot_Arm_HV.set(
            addItem(
                Robot_Arm_HV.ID,
                "Robot Arm (HV)",
                "1 stack every 1 sec (as Cover)/n " + RAText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 4L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 4L)));
        ItemList.Robot_Arm_EV.set(
            addItem(
                Robot_Arm_EV.ID,
                "Robot Arm (EV)",
                "1 stack every 1/5 sec (as Cover)/n " + RAText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 8L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 8L)));
        ItemList.Robot_Arm_IV.set(
            addItem(
                Robot_Arm_IV.ID,
                "Robot Arm (IV)",
                "1 stack every 1/20 sec (as Cover)/n " + RAText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 16L)));
        ItemList.Robot_Arm_LuV.set(
            addItem(
                Robot_Arm_LuV.ID,
                "Robot Arm (LuV)",
                PartNotCoverText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 32L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 32L)));
        ItemList.Robot_Arm_ZPM.set(
            addItem(
                Robot_Arm_ZPM.ID,
                "Robot Arm (ZPM)",
                PartNotCoverText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 64L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 64L)));
        ItemList.Robot_Arm_UV.set(
            addItem(
                Robot_Arm_UV.ID,
                "Robot Arm (UV)",
                PartNotCoverText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 128L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 128L)));
        ItemList.Robot_Arm_UHV
            .set(
                addItem(
                    Robot_Arm_UHV.ID,
                    "Robot Arm (UHV)",
                    PartNotCoverText,
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Robot_Arm_UEV
            .set(
                addItem(
                    Robot_Arm_UEV.ID,
                    "Robot Arm (UEV)",
                    PartNotCoverText,
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Robot_Arm_UIV
            .set(
                addItem(
                    Robot_Arm_UIV.ID,
                    "Robot Arm (UIV)",
                    PartNotCoverText,
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Robot_Arm_UMV.set(
            addItem(
                Robot_Arm_UMV.ID,
                "Robot Arm (UMV)",
                PartNotCoverText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UXV.set(
            addItem(
                Robot_Arm_UXV.ID,
                "Robot Arm (UXV)",
                PartNotCoverText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_MAX.set(
            addItem(
                Robot_Arm_MAX.ID,
                "Robot Arm (MAX)",
                PartNotCoverText,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)));

        ItemList.QuantumEye.set(addItem(QuantumEye.ID, "Quantum Eye", "Improved Ender Eye"));
        ItemList.QuantumStar.set(addItem(QuantumStar.ID, "Quantum Star", "Improved Nether Star"));
        ItemList.Gravistar.set(addItem(Gravistar.ID, "Gravi Star", "Ultimate Nether Star"));

        ItemList.Emitter_LV.set(
            addItem(
                Emitter_LV.ID,
                "Emitter (LV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 1L)));
        ItemList.Emitter_MV.set(
            addItem(
                Emitter_MV.ID,
                "Emitter (MV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 2L)));
        ItemList.Emitter_HV.set(
            addItem(
                Emitter_HV.ID,
                "Emitter (HV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 4L)));
        ItemList.Emitter_EV.set(
            addItem(
                Emitter_EV.ID,
                "Emitter (EV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 8L)));
        ItemList.Emitter_IV.set(
            addItem(
                Emitter_IV.ID,
                "Emitter (IV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 16L)));
        ItemList.Emitter_LuV.set(
            addItem(
                Emitter_LuV.ID,
                "Emitter (LuV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 32L)));
        ItemList.Emitter_ZPM.set(
            addItem(
                Emitter_ZPM.ID,
                "Emitter (ZPM)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 64L)));
        ItemList.Emitter_UV.set(
            addItem(
                Emitter_UV.ID,
                "Emitter (UV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 128L)));
        ItemList.Emitter_UHV
            .set(
                addItem(
                    Emitter_UHV.ID,
                    "Emitter (UHV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.LUX, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Emitter_UEV
            .set(
                addItem(
                    Emitter_UEV.ID,
                    "Emitter (UEV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Emitter_UIV
            .set(
                addItem(
                    Emitter_UIV.ID,
                    "Emitter (UIV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Emitter_UMV.set(
            addItem(
                Emitter_UMV.ID,
                "Emitter (UMV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)));
        ItemList.Emitter_UXV.set(
            addItem(
                Emitter_UXV.ID,
                "Emitter (UXV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)));
        ItemList.Emitter_MAX.set(
            addItem(
                Emitter_MAX.ID,
                "Emitter (MAX)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)));

        ItemList.Sensor_LV.set(
            addItem(
                Sensor_LV.ID,
                "Sensor (LV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L)));

        ItemList.Sensor_MV.set(
            addItem(
                Sensor_MV.ID,
                "Sensor (MV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L)));
        ItemList.Sensor_HV.set(
            addItem(
                Sensor_HV.ID,
                "Sensor (HV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4L)));
        ItemList.Sensor_EV.set(
            addItem(
                Sensor_EV.ID,
                "Sensor (EV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 8L)));
        ItemList.Sensor_IV.set(
            addItem(
                Sensor_IV.ID,
                "Sensor (IV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 16L)));
        ItemList.Sensor_LuV.set(
            addItem(
                Sensor_LuV.ID,
                "Sensor (LuV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 32L)));
        ItemList.Sensor_ZPM.set(
            addItem(
                Sensor_ZPM.ID,
                "Sensor (ZPM)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 64L)));
        ItemList.Sensor_UV.set(
            addItem(
                Sensor_UV.ID,
                "Sensor (UV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 128L)));
        ItemList.Sensor_UHV
            .set(
                addItem(
                    Sensor_UHV.ID,
                    "Sensor (UHV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Sensor_UEV
            .set(
                addItem(
                    Sensor_UEV.ID,
                    "Sensor (UEV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Sensor_UIV
            .set(
                addItem(
                    Sensor_UIV.ID,
                    "Sensor (UIV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Sensor_UMV.set(
            addItem(
                Sensor_UMV.ID,
                "Sensor (UMV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)));
        ItemList.Sensor_UXV.set(
            addItem(
                Sensor_UXV.ID,
                "Sensor (UXV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)));
        ItemList.Sensor_MAX.set(
            addItem(
                Sensor_MAX.ID,
                "Sensor (MAX)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)));

        ItemList.Field_Generator_LV.set(
            addItem(
                Field_Generator_LV.ID,
                "Field Generator (LV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1L)));
        ItemList.Field_Generator_MV.set(
            addItem(
                Field_Generator_MV.ID,
                "Field Generator (MV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 2L)));
        ItemList.Field_Generator_HV.set(
            addItem(
                Field_Generator_HV.ID,
                "Field Generator (HV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 4L)));
        ItemList.Field_Generator_EV.set(
            addItem(
                Field_Generator_EV.ID,
                "Field Generator (EV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 8L)));
        ItemList.Field_Generator_IV.set(
            addItem(
                Field_Generator_IV.ID,
                "Field Generator (IV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 16L)));
        ItemList.Field_Generator_LuV.set(
            addItem(
                Field_Generator_LuV.ID,
                "Field Generator (LuV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 32L)));
        ItemList.Field_Generator_ZPM.set(
            addItem(
                Field_Generator_ZPM.ID,
                "Field Generator (ZPM)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 64L)));
        ItemList.Field_Generator_UV.set(
            addItem(
                Field_Generator_UV.ID,
                "Field Generator (UV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 128L)));
        ItemList.Field_Generator_UHV
            .set(
                addItem(
                    Field_Generator_UHV.ID,
                    "Field Generator (UHV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Field_Generator_UEV
            .set(
                addItem(
                    Field_Generator_UEV.ID,
                    "Field Generator (UEV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Field_Generator_UIV
            .set(
                addItem(
                    Field_Generator_UIV.ID,
                    "Field Generator (UIV)",
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Field_Generator_UMV.set(
            addItem(
                Field_Generator_UMV.ID,
                "Field Generator (UMV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UXV.set(
            addItem(
                Field_Generator_UXV.ID,
                "Field Generator (UXV)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_MAX.set(
            addItem(
                Field_Generator_MAX.ID,
                "Field Generator (MAX)",
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)));

        ItemList.StableAdhesive.set(
            addItem(
                StableAdhesive.ID,
                "Hyper-Stable Self-Healing Adhesive",
                "Complete and selective adhesion, even when torn or damaged",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 30L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.LIMUS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.VINCULUM, 5L)));
        ItemList.SuperconductorComposite.set(
            addItem(
                SuperconductorComposite.ID,
                "Superconductor Rare-Earth Composite",
                "Zero resistance to electrical and quantum flow, regardless of temperature",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 50L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 25L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 15L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 10L)));
        ItemList.NaquadriaSupersolid.set(
            addItem(
                NaquadriaSupersolid.ID,
                "Black Body Naquadria Supersolid",
                "Flows like a fluid and reflects nothing, perfect absorption and transfer",
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 100L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 60L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 40L),
                new TCAspects.TC_AspectStack(TCAspects.RADIO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 10L)));

        // Circuits ULV - LuV.
        ItemList.Circuit_Primitive.set(
            addItem(
                Circuit_Primitive.ID,
                "Vacuum Tube",
                "A very simple Circuit",
                OrePrefixes.circuit.get(Materials.ULV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Basic.set(
            addItem(
                Circuit_Basic.ID,
                "Integrated Logic Circuit",
                "A Basic Circuit",
                OrePrefixes.circuit.get(Materials.LV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Good.set(
            addItem(
                Circuit_Good.ID,
                "Good Electronic Circuit",
                "A Good Circuit",
                OrePrefixes.circuit.get(Materials.MV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Advanced.set(
            addItem(
                Circuit_Advanced.ID,
                "Processor Assembly",
                "An Advanced Circuit",
                OrePrefixes.circuit.get(Materials.HV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Data.set(
            addItem(
                Circuit_Data.ID,
                "Workstation",
                "An Extreme Circuit",
                OrePrefixes.circuit.get(Materials.EV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Elite.set(
            addItem(
                Circuit_Elite.ID,
                "Mainframe",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Master.set(
            addItem(
                Circuit_Master.ID,
                "Nano Mainframe",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        // Backwards compatibility.
        ItemList.Circuit_Parts_Vacuum_Tube.set(ItemList.Circuit_Primitive.get(1));
        ItemList.Circuit_Computer.set(ItemList.Circuit_Advanced.get(1));

        ItemList.Tool_DataOrb.set(
            addItem(
                Tool_DataOrb.ID,
                "Data Orb",
                "A High Capacity Data Storage",
                SubTag.NO_UNIFICATION,
                new BehaviourDataOrb()));

        ItemList.Tool_DataStick.set(
            addItem(
                Tool_DataStick.ID,
                "Data Stick",
                "A Low Capacity Data Storage",
                SubTag.NO_UNIFICATION,
                new BehaviourDataStick()));

        ItemList.Tool_Cover_Copy_Paste.set(
            addItem(
                Tool_Cover_Copy_Paste.ID,
                "Cover Copy/Paste tool",
                "Set Cover Massively.",
                BehaviourCoverTool.INSTANCE,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 6L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 6L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 6L)));

        ItemList.Circuit_Board_Basic.set(addItem(Circuit_Board_Basic.ID, "Coated Circuit Board", "A Basic Board"));
        ItemList.Circuit_Board_Coated.set(ItemList.Circuit_Board_Basic.get(1));
        ItemList.Circuit_Board_Advanced
            .set(addItem(Circuit_Board_Advanced.ID, "Epoxy Circuit Board", "An Advanced Board"));
        ItemList.Circuit_Board_Epoxy.set(ItemList.Circuit_Board_Advanced.get(1));
        ItemList.Circuit_Board_Elite
            .set(addItem(Circuit_Board_Elite.ID, "Multilayer Fiber-Reinforced Circuit Board", "An Elite Board"));
        ItemList.Circuit_Board_Multifiberglass.set(ItemList.Circuit_Board_Elite.get(1));
        ItemList.Circuit_Parts_Crystal_Chip_Elite
            .set(addItem(Circuit_Parts_Crystal_Chip_Elite.ID, "Engraved Crystal Chip", "Needed for Circuits"));
        ItemList.Circuit_Parts_Crystal_Chip_Master
            .set(addItem(Circuit_Parts_Crystal_Chip_Master.ID, "Engraved Lapotron Chip", "Needed for Circuits"));
        ItemList.Circuit_Parts_Crystal_Chip_Wetware
            .set(addItem(Circuit_Parts_Crystal_Chip_Wetware.ID, "Living Crystal Chip", "Needed for Circuits"));
        ItemList.Circuit_Parts_Advanced.set(addItem(Circuit_Parts_Advanced.ID, "Diode", "Basic Electronic Component"));
        ItemList.Circuit_Parts_Diode.set(ItemList.Circuit_Parts_Advanced.get(1));
        ItemList.Circuit_Parts_Wiring_Basic
            .set(addItem(Circuit_Parts_Wiring_Basic.ID, "Resistor", "Basic Electronic Component"));
        ItemList.Circuit_Parts_Resistor.set(ItemList.Circuit_Parts_Wiring_Basic.get(1));
        ItemList.Circuit_Parts_Wiring_Advanced
            .set(addItem(Circuit_Parts_Wiring_Advanced.ID, "Transistor", "Basic Electronic Component"));
        ItemList.Circuit_Parts_Transistor.set(ItemList.Circuit_Parts_Wiring_Advanced.get(1));
        ItemList.Circuit_Parts_Wiring_Elite
            .set(addItem(Circuit_Parts_Wiring_Elite.ID, "Capacitor", "Electronic Component"));
        ItemList.Circuit_Parts_Capacitor.set(ItemList.Circuit_Parts_Wiring_Elite.get(1));
        ItemList.Empty_Board_Basic.set(addItem(Empty_Board_Basic.ID, "Phenolic Circuit Board", "A Good Board"));
        ItemList.Circuit_Board_Phenolic.set(ItemList.Empty_Board_Basic.get(1));
        ItemList.Empty_Board_Elite
            .set(addItem(Empty_Board_Elite.ID, "Fiber-Reinforced Circuit Board", "An Extreme Board"));
        ItemList.Circuit_Board_Fiberglass.set(ItemList.Empty_Board_Elite.get(1));

        ItemList.Component_Sawblade_Diamond.set(
            addItem(
                Component_Sawblade_Diamond.ID,
                "Diamond Sawblade",
                "",
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L),
                OreDictNames.craftingDiamondBlade));
        ItemList.Component_Grinder_Diamond.set(
            addItem(
                Component_Grinder_Diamond.ID,
                "Diamond Grinding Head",
                "",
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 6L),
                OreDictNames.craftingGrinder));
        ItemList.Component_Grinder_Tungsten.set(
            addItem(
                Component_Grinder_Tungsten.ID,
                "Tungsten Grinding Head",
                "",
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 6L),
                OreDictNames.craftingGrinder));

        ItemList.Upgrade_Lock.set(
            addItem(
                Upgrade_Lock.ID,
                "Lock Upgrade",
                "Protects your Machines",
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 4L)));

        ItemList.Component_Filter.set(
            addItem(
                Component_Filter.ID,
                "Item Filter",
                "",
                new ItemData(Materials.Zinc, OrePrefixes.foil.getMaterialAmount() * 16L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                OreDictNames.craftingFilter));

        ItemList.Cover_Controller.set(
            addItem(
                Cover_Controller.ID,
                "Machine Controller Cover",
                "Turns Machines ON/OFF",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_ActivityDetector.set(
            addItem(
                Cover_ActivityDetector.ID,
                "Activity Detector Cover",
                "Gives out Activity as Redstone",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_FluidDetector.set(
            addItem(
                Cover_FluidDetector.ID,
                "Fluid Detector Cover",
                "Gives out Fluid Amount as Redstone",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Cover_ItemDetector.set(
            addItem(
                Cover_ItemDetector.ID,
                "Item Detector Cover",
                "Gives out Item Amount as Redstone",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 1L)));
        ItemList.Cover_EnergyDetector.set(
            addItem(
                Cover_EnergyDetector.ID,
                "Energy Detector Cover",
                "Gives out Energy Amount as Redstone",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));
        ItemList.Cover_PlayerDetector.set(
            addItem(
                Cover_PlayerDetector.ID,
                "Player Detector Cover",
                "Gives out close Players as Redstone",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_FluidStorageMonitor.set(
            addItem(
                Cover_FLuidStorageMonitor.ID,
                "Fluid Storage Monitor Cover",
                "Displays the fluid stored in the Tank",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Cover_Chest_Basic.set(
            addItem(
                Cover_Chest_Basic.ID,
                "Basic Item Holder",
                "Holds 9 item for use within machine GUI (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));
        ItemList.Cover_Chest_Good.set(
            addItem(
                Cover_Chest_Good.ID,
                "Good Item Holder",
                "Holds 12 item for use within machine GUI (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));
        ItemList.Cover_Chest_Advanced.set(
            addItem(
                Cover_Chest_Advanced.ID,
                "Advanced Item Holder",
                "Holds 15 item for use within machine GUI (as Cover)",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));

        for (int i = 1; i < 15; i++) {
            ItemList.WIRELESS_ENERGY_COVERS[i - 1].set(
                addItem(
                    Cover_Wireless_Energy_LV.ID + i - 1,
                    GTValues.VN[i] + " Wireless Energy Cover",
                    String.join(
                        "/n ",
                        "Stores energy globally in a network, up to 2^(2^31) EU.",
                        "Does not connect to wires. This cover withdraws EU from the network.",
                        "Ignores voltage limitations (no explosions).",
                        "Amperage: " + EnumChatFormatting.YELLOW + "2" + EnumChatFormatting.GRAY,
                        "Voltage IN: " + EnumChatFormatting.GREEN
                            + formatNumber(GTValues.V[i])
                            + " ("
                            + GTUtility.getColoredTierNameFromTier((byte) (i))
                            + EnumChatFormatting.GREEN
                            + ")"),
                    new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));
        }
        ItemList.Cover_Wireless_Energy_Debug.set(
            addItem(
                Cover_Wireless_Energy_Debug.ID,
                "Debug Wireless Energy Cover",
                "Infinite Power. Ignores Voltage Limitations.",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 9999L)));

        ItemList.Cover_Screen.set(
            addItem(
                Cover_Screen.ID,
                "Computer Monitor Cover",
                "Displays Data and GUI",
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L)));
        ItemList.Cover_Crafting.set(
            addItem(
                Cover_Crafting.ID,
                "Crafting Table Cover",
                "Better than a wooden Workbench",
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 4L)));
        ItemList.Cover_Drain.set(
            addItem(
                Cover_Drain.ID,
                "Drain Module Cover",
                "Absorbs Fluids and collects Rain",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));

        ItemList.Cover_Shutter.set(
            addItem(
                Cover_Shutter.ID,
                "Shutter Module Cover",
                "Blocks Inventory/Tank Side. Use together with Machine Controller.",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));

        ItemList.Cover_SolarPanel.set(
            addItem(
                Cover_SolarPanel.ID,
                "Solar Panel",
                "May the Sun be with you (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 1L)));
        ItemList.Cover_SolarPanel_8V.set(
            addItem(
                Cover_SolarPanel_8V.ID,
                "Solar Panel (8V)",
                "8 Volt Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 2L)));
        ItemList.Cover_SolarPanel_LV.set(
            addItem(
                Cover_SolarPanel_LV.ID,
                "Solar Panel (LV)",
                "Low Voltage Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 4L)));
        ItemList.Cover_SolarPanel_MV.set(
            addItem(
                Cover_SolarPanel_MV.ID,
                "Solar Panel (MV)",
                "Medium Voltage Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 8L)));
        ItemList.Cover_SolarPanel_HV.set(
            addItem(
                Cover_SolarPanel_HV.ID,
                "Solar Panel (HV)",
                "High Voltage Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 16L)));
        ItemList.Cover_SolarPanel_EV.set(
            addItem(
                Cover_SolarPanel_EV.ID,
                "Solar Panel (EV)",
                "Extreme Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 32L)));
        ItemList.Cover_SolarPanel_IV.set(
            addItem(
                Cover_SolarPanel_IV.ID,
                "Solar Panel (IV)",
                "Insane Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_LuV.set(
            addItem(
                Cover_SolarPanel_LuV.ID,
                "Solar Panel (LuV)",
                "Ludicrous Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_ZPM.set(
            addItem(
                Cover_SolarPanel_ZPM.ID,
                "Solar Panel (ZPM)",
                "ZPM Voltage Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_UV.set(
            addItem(
                Cover_SolarPanel_UV.ID,
                "Solar Panel (UV)",
                "Ultimate Solar Panel (Needs cleaning with right click)",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));

        ItemList.Tool_Cheat.set(
            addItem(
                Tool_Cheat.ID,
                "Debug Scanner",
                "Also an Infinite Energy Source",
                BehaviourScanner.INSTANCE,
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 64L)));
        ItemList.Tool_Scanner.set(
            addItem(
                Tool_Scanner.ID,
                "Portable Scanner",
                "Tricorder",
                BehaviourScanner.INSTANCE,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 6L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 6L)));

        ItemList.NC_SensorKit.set(addItem(NC_SensorKit.ID, "GregTech Sensor Kit", "", new BehaviourSensorKit()));
        ItemList.Duct_Tape.set(
            addItem(
                Duct_Tape.ID,
                "BrainTech Aerospace Advanced Reinforced Duct Tape FAL-84",
                "If you can't fix it with this, use more of it!",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                OreDictNames.craftingDuctTape));
        ItemList.McGuffium_239.set(
            addItem(
                McGuffium_239.ID,
                "Mc Guffium 239",
                "42% better than Phlebotnium",
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 8L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 8L),
                new TCAspects.TC_AspectStack(TCAspects.SPIRITUS, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AURAM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.VITIUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.RADIO, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 8L)));

        ItemList.Cover_RedstoneTransmitter.set(
            addItem(
                Cover_RedstoneTransmitter.ID,
                "Redstone Transmitter",
                "Transfers Redstone signals wireless",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneTransmitterInternal.set(
            addItem(
                Cover_RedstoneTransmitterInternal.ID,
                "Redstone Transmitter (Internal)",
                "Transfers Redstone signals wireless/n §cDEPRECATED! This will be removed in the next major update.",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneReceiver.set(
            addItem(
                Cover_RedstoneReceiver.ID,
                "Redstone Receiver",
                "Transfers Redstone signals wireless",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_WirelessController.set(
            addItem(
                Cover_WirelessController.ID,
                "Wireless Machine Controller Cover",
                "Turns Machines ON/OFF wirelessly/n Can only connect with advanced wireless covers",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));

        ItemList.Cover_NeedsMaintainance.set(
            addItem(
                Cover_NeedsMaintenance.ID,
                "Needs Maintenance Cover",
                "Attach to Multiblock Controller. Emits Redstone Signal if needs Maintenance",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));

        ItemList.Steam_Regulator_LV.set(
            addItem(
                Steam_Regulator_LV.ID,
                "Steam Regulator (LV)",
                formatNumber(1024) + PartCoverText + formatNumber(1024 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Steam_Regulator_MV.set(
            addItem(
                Steam_Regulator_MV.ID,
                "Steam Regulator (MV)",
                formatNumber(2048) + PartCoverText + formatNumber(2048 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.Steam_Regulator_HV.set(
            addItem(
                Steam_Regulator_HV.ID,
                "Steam Regulator (HV)",
                formatNumber(4096) + PartCoverText + formatNumber(4096 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));
        ItemList.Steam_Regulator_EV.set(
            addItem(
                Steam_Regulator_EV.ID,
                "Steam Regulator (EV)",
                formatNumber(8192) + PartCoverText + formatNumber(8192 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));
        ItemList.Steam_Regulator_IV.set(
            addItem(
                Steam_Regulator_IV.ID,
                "Steam Regulator (IV)",
                formatNumber(16384) + PartCoverText + formatNumber(16384 * 20) + PartCoverText2,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 16L)));
        ItemList.Electromagnet_Iron.set(
            addItem(
                Electromagnet_Iron.ID,
                "Iron Electromagnet",
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Iron),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 8)));
        ItemList.Electromagnet_Steel.set(
            addItem(
                Electromagnet_Steel.ID,
                "Steel Electromagnet",
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Steel),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 16)));
        ItemList.Electromagnet_Neodymium.set(
            addItem(
                Electromagnet_Neodymium.ID,
                "Neodymium Electromagnet",
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Neodymium),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 24)));
        ItemList.Electromagnet_Samarium.set(
            addItem(
                Electromagnet_Samarium.ID,
                EnumChatFormatting.YELLOW + "Samarium Electromagnet",
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Samarium),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 32)));
        ItemList.Electromagnet_Tengam.set(
            addItem(
                Electromagnet_Tengam.ID,
                EnumChatFormatting.GREEN + "Tengam Electromagnet",
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Tengam),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 40)));
        ItemList.Black_Hole_Opener.set(
            addItem(
                Black_Hole_Opener.ID,
                "Black Hole Seed",
                "Opens a pseudostable black hole",
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 32),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 64)));
        ItemList.Black_Hole_Closer.set(
            addItem(
                Black_Hole_Closer.ID,
                "Black Hole Collapser",
                "Safely closes a pseudostable black hole",
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 32),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 64)));
        ItemList.Black_Hole_Stabilizer.set(
            addItem(
                Black_Hole_Stabilizer.ID,
                "Superstable Black Hole Seed",
                "Opens a superstable black hole/n Black hole will never destabilize and will operate at maximum efficiency",
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 32),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 128)));

        // AOs
        ItemList.Bio_Computing_Core.set(
            addItem(
                Bio_Computing_Core.ID,
                "Bio-computing Core",
                "Highly integrated core of neural computation and storage",
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 4),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 4)));
        ItemList.Neural_Electronic_Interface.set(
            addItem(
                Neural_Electronic_Interface.ID,
                "Neural Interface",
                "Living converter that interprets between neural impulses and logarithmic ones",
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 4),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        ItemList.Axon_Bus.set(
            addItem(
                Axon_Bus.ID,
                "Axon Bus",
                "Bio-synthetic neural backbone that transmits neural signals",
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 4),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 4)));
        ItemList.Self_Healing_Conductor.set(
            addItem(
                Self_Healing_Conductor.ID,
                "Self Healing Conductor",
                "Seaweed-based neural gel that repairs and transmits bioelectric signals",
                new TCAspects.TC_AspectStack(TCAspects.LIMUS, 4),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2)));
        ItemList.Circuit_Tissue.set(
            addItem(
                Circuit_Tissue.ID,
                "Circuit Tissue",
                "Living flesh designed to hold biological circuit components",
                new TCAspects.TC_AspectStack(TCAspects.VICTUS, 8),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 4)));
        ItemList.Neuron_Cell_Cluster.set(
            addItem(
                Neuron_Cell_Cluster.ID,
                "Neuron Cell Cluster",
                "A collection of living brain cells",
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2)));
        ItemList.Skin_Cell_Cluster.set(
            addItem(
                Skin_Cell_Cluster.ID,
                "Skin Cell Cluster",
                "Living skin tissue",
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2)));
        ItemList.Muscle_Cell_Cluster.set(
            addItem(
                Muscle_Cell_Cluster.ID,
                "Muscle Cell Cluster",
                "Living muscle tissue",
                new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 2)));
        ItemList.Nutrient_Paste.set(
            addItem(Nutrient_Paste.ID, "Nutrient Paste", "Tasty!", new TCAspects.TC_AspectStack(TCAspects.CORPUS, 4)));
        ItemList.Immortal_Cell.set(
            addItem(
                Immortal_Cell.ID,
                "Immortal Cell",
                "Even better than Steve",
                new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 2)));
        ItemList.Infinite_Evolution_Matrix.set(
            addItem(
                Infinite_Evolution_Matrix.ID,
                "Infinite Evolution Matrix",
                "It learns. It mutates. It remembers what you forget",
                new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 16)));
        // Empty battery hulls
        ItemList.BatteryHull_EV.set(
            addItem(
                BatteryHull_EV.ID,
                "Small Sunnarium Battery (Empty)",
                "An empty EV Battery Container",
                new ItemData(Materials.BlueSteel, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 8L)));
        ItemList.BatteryHull_IV.set(
            addItem(
                BatteryHull_IV.ID,
                "Medium Sunnarium Battery (Empty)",
                "An empty IV Battery Container",
                new ItemData(Materials.RoseGold, OrePrefixes.plate.getMaterialAmount() * 6L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 16L)));
        ItemList.BatteryHull_LuV.set(
            addItem(
                BatteryHull_LuV.ID,
                "Large Sunnarium Battery (Empty)",
                "An empty LuV Battery Container",
                new ItemData(Materials.RedSteel, OrePrefixes.plate.getMaterialAmount() * 18L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 32L)));
        ItemList.BatteryHull_ZPM.set(
            addItem(
                BatteryHull_ZPM.ID,
                "Medium Naquadria Battery (Empty)",
                "An empty ZPM Energy Storage",
                new ItemData(Materials.Europium, OrePrefixes.plate.getMaterialAmount() * 6L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 64L)));
        ItemList.BatteryHull_UV.set(
            addItem(
                BatteryHull_UV.ID,
                "Large Naquadria Battery (Empty)",
                "An empty UV Energy Storage",
                new ItemData(Materials.Americium, OrePrefixes.plate.getMaterialAmount() * 18L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 128L)));
        ItemList.BatteryHull_UHV.set(
            addItem(
                BatteryHull_UHV.ID,
                "Small Neutronium Battery (Empty)",
                "An empty UHV Energy Storage",
                new ItemData(Materials.Naquadah, OrePrefixes.plate.getMaterialAmount() * 24L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 256L)));
        ItemList.BatteryHull_UEV.set(
            addItem(
                BatteryHull_UEV.ID,
                "Medium Neutronium Battery (Empty)",
                "An empty UEV Energy Storage",
                new ItemData(Materials.NaquadahEnriched, OrePrefixes.plate.getMaterialAmount() * 36L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 512L)));
        ItemList.BatteryHull_UIV.set(
            addItem(
                BatteryHull_UIV.ID,
                "Large Neutronium Battery (Empty)",
                "An empty UIV Energy Storage",
                new ItemData(Materials.NaquadahAlloy, OrePrefixes.plate.getMaterialAmount() * 48L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1024L)));
        ItemList.BatteryHull_UMV.set(
            addItem(
                BatteryHull_UMV.ID,
                "Medium Plasma Battery (Empty)",
                "An empty UMV Energy Storage",
                new ItemData(Materials.Neutronium, OrePrefixes.plate.getMaterialAmount() * 56L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2048L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2048L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2048L)));
        ItemList.BatteryHull_UxV.set(
            addItem(
                BatteryHull_UxV.ID,
                "Large Plasma Battery (Empty)",
                "An empty UXV Energy Storage",
                new ItemData(Materials.DraconiumAwakened, OrePrefixes.plate.getMaterialAmount() * 64L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4096L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4096L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 4096L)));

        ItemList.BatteryHull_EV_Full.set(
            addItem(
                BatteryHull_EV_Full.ID,
                "Small Sunnarium Battery",
                "Reusable",
                "batteryEV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_IV_Full.set(
            addItem(
                BatteryHull_IV_Full.ID,
                "Medium Sunnarium Battery",
                "Reusable",
                "batteryIV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_LuV_Full.set(
            addItem(
                BatteryHull_LuV_Full.ID,
                "Large Sunnarium Battery",
                "Reusable",
                "batteryLuV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_ZPM_Full.set(
            addItem(
                BatteryHull_ZPM_Full.ID,
                "Medium Naquadria Battery",
                "Reusable",
                "batteryZPM",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UV_Full.set(
            addItem(
                BatteryHull_UV_Full.ID,
                "Large Naquadria Battery",
                "Reusable",
                "batteryUV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UHV_Full.set(
            addItem(
                BatteryHull_UHV_Full.ID,
                "Small Neutronium Battery",
                "Reusable",
                "batteryUHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UEV_Full.set(
            addItem(
                BatteryHull_UEV_Full.ID,
                "Medium Neutronium Battery",
                "Reusable",
                "batteryUEV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UIV_Full.set(
            addItem(
                BatteryHull_UIV_Full.ID,
                "Large Neutronium Battery",
                "Reusable",
                "batteryUIV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UMV_Full.set(
            addItem(
                BatteryHull_UMV_Full.ID,
                "Medium Infinity Battery",
                "Reusable",
                "batteryUMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UxV_Full.set(
            addItem(
                BatteryHull_UxV_Full.ID,
                "Large Infinity Battery",
                "Reusable",
                "batteryUXV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        removeRecipes();
        setBurnValues();
        oredictBlacklistEntries();
        registerCovers();
        registerBehaviors();
        setAllFluidContainerStats();
        setAllElectricStats();
        registerTieredTooltips();
        registerSubIcons();

        craftingShapedRecipes();
        craftingShapelessRecipes();
    }

    private static final Map<Materials, Materials> cauldronRemap = new HashMap<>();

    public static void registerCauldronCleaningFor(Materials in, Materials out) {
        cauldronRemap.put(in, out);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem aItemEntity) {
        int aDamage = aItemEntity.getEntityItem()
            .getItemDamage();
        if ((aDamage < 32000) && (aDamage >= 0) && (!aItemEntity.worldObj.isRemote)) {
            Materials aMaterial = GregTechAPI.sGeneratedMaterials[(aDamage % 1000)];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                int tX = MathHelper.floor_double(aItemEntity.posX);
                int tY = MathHelper.floor_double(aItemEntity.posY);
                int tZ = MathHelper.floor_double(aItemEntity.posZ);
                OrePrefixes aPrefix = this.mGeneratedPrefixList[(aDamage / 1000)];
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    int tMetaData = aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {

                        aMaterial = cauldronRemap.getOrDefault(aMaterial, aMaterial);

                        aItemEntity.setEntityItemStack(
                            GTOreDictUnificator
                                .get(OrePrefixes.dust, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.delayBeforeCanPickup = 0;
                        cancelMovementAndTeleport(aItemEntity, tX, tY, tZ);
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if (aPrefix == OrePrefixes.crushed) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    int tMetaData = aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(
                            GTOreDictUnificator
                                .get(OrePrefixes.crushedPurified, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.delayBeforeCanPickup = 0;
                        cancelMovementAndTeleport(aItemEntity, tX, tY, tZ);
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if (aPrefix == OrePrefixes.dust && aMaterial == Materials.Wheat) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    int tMetaData = aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(ItemList.Food_Dough.get(aItemEntity.getEntityItem().stackSize));
                        aItemEntity.delayBeforeCanPickup = 0;
                        cancelMovementAndTeleport(aItemEntity, tX, tY, tZ);
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(final ItemStack oldItemStack, final EntityPlayer player, final World world, final int x,
        final int y, final int z, final int ordinalSide, final float hitX, final float hitY, final float hitZ) {
        final Block blockClicked = world.getBlock(x, y, z);
        final int metadata = world.getBlockMetadata(x, y, z);

        if (blockClicked == Blocks.cauldron && metadata > 0) {
            final int damage = oldItemStack.getItemDamage();

            if ((damage < 32000) && (damage >= 0)) {
                final Materials oldMaterial = GregTechAPI.sGeneratedMaterials[(damage % 1000)];
                final OrePrefixes oldPrefix = this.mGeneratedPrefixList[(damage / 1000)];
                final ItemStack newItemStack = getCauldronWashingResult(oldPrefix, oldMaterial, oldItemStack.stackSize);

                if (newItemStack != null) {
                    world.setBlockMetadataWithNotify(x, y, z, metadata - 1, 3);
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, newItemStack);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns a function to get a new ItemStack after washing.
     *
     * @param oldPrefix   The old prefix of the stack being converted
     * @param oldMaterial The old material to be converted
     * @param stackSize   The stack size to be returned
     * @return the new ItemStack after washing, or null if the material/prefix was invalid
     */
    static ItemStack getCauldronWashingResult(final OrePrefixes oldPrefix, final Materials oldMaterial,
        final int stackSize) {
        if ((oldMaterial != null) && (oldMaterial != Materials.Empty) && (oldMaterial != Materials._NULL)) {
            switch (oldPrefix.getName()) {
                case "dustImpure":
                case "dustPure":
                    return GTOreDictUnificator
                        .get(OrePrefixes.dust, cauldronRemap.getOrDefault(oldMaterial, oldMaterial), stackSize);
                case "crushed":
                    return GTOreDictUnificator.get(OrePrefixes.crushedPurified, oldMaterial, stackSize);
                case "dust":
                    if (oldMaterial == Materials.Wheat) {
                        return ItemList.Food_Dough.get(stackSize);
                    }
            }
        }

        return null;
    }

    /**
     * Cancels the movement of an EntityItem and teleports it above the cauldron.
     *
     * @param entityItem The item entity to move
     * @param x          The X coordinate of the cauldron
     * @param y          The Y coordinate of the cauldron
     * @param z          The Z coordinate of the cauldron
     */
    static void cancelMovementAndTeleport(EntityItem entityItem, int x, int y, int z) {
        entityItem.motionX = 0;
        entityItem.motionY = 0;
        entityItem.motionZ = 0;
        entityItem.posX = x + 0.5;
        entityItem.posY = y + 1.5;
        entityItem.posZ = z + 0.5;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        int aDamage = aStack.getItemDamage();
        if ((aDamage < 32000) && (aDamage >= 0)) {
            Materials aMaterial = GregTechAPI.sGeneratedMaterials[(aDamage % 1000)];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                OrePrefixes aPrefix = this.mGeneratedPrefixList[(aDamage / 1000)];
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    aList.add(translateToLocal("GT5U.tooltip.purify.1"));
                }
            }
        }
    }

    public boolean isPlasmaCellUsed(OrePrefixes aPrefix, Materials aMaterial) {
        // Materials has a plasma fluid
        if (aPrefix == OrePrefixes.cellPlasma && aMaterial.getPlasma(1L) != null) {
            if (aMaterial.hasPlasma()) return true;
            // Loop through fusion recipes
            for (GTRecipe recipe : RecipeMaps.fusionRecipes.getAllRecipes()) {
                // Make sure fluid output can't be null (not sure if possible)
                if (recipe.getFluidOutput(0) != null) {
                    // Fusion recipe output matches current plasma cell fluid
                    if (recipe.getFluidOutput(0)
                        .isFluidEqual(aMaterial.getPlasma(1L))) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return (aDoShowAllItems) || (((aPrefix != OrePrefixes.gem) || (!aMaterial.mName.startsWith("Infused")))
            && (aPrefix != OrePrefixes.dustTiny)
            && (aPrefix != OrePrefixes.dustSmall)
            && (aPrefix != OrePrefixes.dustImpure)
            && (aPrefix != OrePrefixes.dustPure)
            && (aPrefix != OrePrefixes.crushed)
            && (aPrefix != OrePrefixes.crushedPurified)
            && (aPrefix != OrePrefixes.crushedCentrifuged)
            && (aPrefix != OrePrefixes.ingotHot)
            && !(aPrefix == OrePrefixes.cellPlasma && !isPlasmaCellUsed(aPrefix, aMaterial)));
    }

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        if (((aDamage >= 32430) && (aDamage <= 32461)) || (aDamage == 32465 || aDamage == 32466)) {
            return ItemList.Spray_Empty.get(1L);
        }
        if ((aDamage == 32479) || (aDamage == 32476)) {
            return new ItemStack(this, 1, aDamage - 2);
        }
        if (aDamage == 32401) {
            return new ItemStack(this, 1, aDamage - 1);
        }
        return super.getContainerItem(aStack);
    }

    @Override
    public boolean doesMaterialAllowGeneration(OrePrefixes aPrefix, Materials aMaterial) {
        return (super.doesMaterialAllowGeneration(aPrefix, aMaterial));
    }

    private void setBurnValues() {
        setBurnValue(17000 + Materials.Wood.mMetaItemSubID, 1600);
    }

    private void setAllFluidContainerStats() {
        setFluidContainerStats(32000 + Large_Fluid_Cell_Steel.ID, 8_000L, 64L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_TungstenSteel.ID, 512_000L, 32L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_Aluminium.ID, 32_000L, 64L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_StainlessSteel.ID, 64_000L, 64L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_Titanium.ID, 128_000L, 64L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_Chrome.ID, 2_048_000L, 8L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_Iridium.ID, 8_192_000L, 2L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_Osmium.ID, 32_768_000L, 1L);
        setFluidContainerStats(32000 + Large_Fluid_Cell_Neutronium.ID, 131_072_000L, 1L);
        setFluidContainerStats(32000 + Electromagnet_Iron.ID, 0L, 1L);
        setFluidContainerStats(32000 + Electromagnet_Steel.ID, 0L, 1L);
        setFluidContainerStats(32000 + Electromagnet_Neodymium.ID, 0L, 1L);
        setFluidContainerStats(32000 + Electromagnet_Samarium.ID, 0L, 1L);
        setFluidContainerStats(32000 + Electromagnet_Tengam.ID, 0L, 1L);
    }

    private void oredictBlacklistEntries() {
        GTOreDictUnificator.addToBlacklist(new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID));
    }

    private void registerCovers() {
        final ITexture doesWorkCoverTexture = TextureFactory.of(
            TextureFactory.of(OVERLAY_ACTIVITYDETECTOR),
            TextureFactory.builder()
                .addIcon(OVERLAY_ACTIVITYDETECTOR_GLOW)
                .glow()
                .build());

        final ITexture playerDectectorCoverTexture = TextureFactory.of(
            TextureFactory.of(OVERLAY_ACTIVITYDETECTOR),
            TextureFactory.builder()
                .addIcon(OVERLAY_ACTIVITYDETECTOR_GLOW)
                .glow()
                .build());
        final ITexture screenCoverTexture = TextureFactory.of(
            TextureFactory.of(OVERLAY_SCREEN),
            TextureFactory.builder()
                .addIcon(OVERLAY_SCREEN_GLOW)
                .glow()
                .build());

        CoverRegistry.registerDecorativeCover(
            new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID),
            TextureFactory.of(COVER_WOOD_PLATE));

        CoverRegistry.registerCover(
            ItemList.Electric_Pump_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 32, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 128, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 512, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 2048, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 8192, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 32768, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 131072, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 524288, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_UHV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[9][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 8388608, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_UEV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[10][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 16777216, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_UIV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[11][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 33554432, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_UMV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[12][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 67108864, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_UXV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[13][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 134217728, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.Electric_Pump_MAX.get(1L),
            TextureFactory.of(MACHINE_CASINGS[14][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverPump(context, 268435456, TextureFactory.of(OVERLAY_PUMP)));

        CoverRegistry.registerCover(
            ItemList.Steam_Valve_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamValve(context, 1024, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Valve_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamValve(context, 2048, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Valve_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamValve(context, 4096, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Valve_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamValve(context, 8192, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Valve_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamValve(context, 16384, TextureFactory.of(OVERLAY_VALVE)));

        CoverRegistry.registerCover(
            ItemList.FluidRegulator_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 32, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 128, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 512, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 2048, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 8192, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 32768, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 131072, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 524288, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_UHV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[9][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 8388608, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_UEV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[10][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 16777216, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_UIV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[11][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 33554432, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_UMV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[12][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 67108864, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_UXV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[12][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 134217728, TextureFactory.of(OVERLAY_PUMP)));
        CoverRegistry.registerCover(
            ItemList.FluidRegulator_MAX.get(1L),
            TextureFactory.of(MACHINE_CASINGS[13][0], TextureFactory.of(OVERLAY_PUMP)),
            context -> new CoverFluidRegulator(context, 268435456, TextureFactory.of(OVERLAY_PUMP)));

        CoverRegistry.registerCover(
            ItemList.FluidFilter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            context -> new CoverFluidfilter(context, TextureFactory.of(OVERLAY_SHUTTER)));

        CoverRegistry.registerCover(
            ItemList.ItemFilter_Export.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverItemFilter(context, true, TextureFactory.of(OVERLAY_CONVEYOR)));

        CoverRegistry.registerCover(
            ItemList.ItemFilter_Import.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverItemFilter(context, false, TextureFactory.of(OVERLAY_CONVEYOR)));

        CoverRegistry.registerCover(
            ItemList.Cover_FluidLimiter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            context -> new CoverFluidLimiter(context, TextureFactory.of(OVERLAY_SHUTTER)));

        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 100, 1, 16, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 100, 1, 64, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 20, 1, 64, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 4, 1, 64, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 1, 64, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 4, 64, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, 64, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, 256, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_UHV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[9][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, 1024, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_UEV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[10][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, 4096, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_UIV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[11][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, 16384, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_UMV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[12][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, 65536, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_UXV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[13][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, 262144, TextureFactory.of(OVERLAY_CONVEYOR)));
        CoverRegistry.registerCover(
            ItemList.Conveyor_Module_MAX.get(1L),
            TextureFactory.of(MACHINE_CASINGS[14][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            context -> new CoverConveyor(context, 1, 16, Integer.MAX_VALUE, TextureFactory.of(OVERLAY_CONVEYOR)));

        CoverRegistry.registerCover(
            ItemList.Robot_Arm_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_ARM)),
            context -> new CoverArm(context, 400, TextureFactory.of(OVERLAY_ARM)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Robot_Arm_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ARM)),
            context -> new CoverArm(context, 100, TextureFactory.of(OVERLAY_ARM)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Robot_Arm_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_ARM)),
            context -> new CoverArm(context, 20, TextureFactory.of(OVERLAY_ARM)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Robot_Arm_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_ARM)),
            context -> new CoverArm(context, 4, TextureFactory.of(OVERLAY_ARM)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Robot_Arm_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_ARM)),
            context -> new CoverArm(context, 1, TextureFactory.of(OVERLAY_ARM)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);

        CoverRegistry.registerCover(
            ItemList.Cover_Controller.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_CONTROLLER)),
            context -> new CoverControlsWork(context, TextureFactory.of(OVERLAY_CONTROLLER)),
            CoverPlacer.builder()
                .onlyPlaceIf(CoverControlsWork::isCoverPlaceable)
                .build());

        CoverRegistry.registerCover(
            ItemList.Cover_Chest_Basic.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(Textures.BlockIcons.OVERLAY_COVER_CHEST_1)),
            context -> new CoverChest(context, 9, TextureFactory.of(Textures.BlockIcons.OVERLAY_COVER_CHEST_1)),
            CoverRegistry.PRIMITIVE_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_Chest_Good.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(Textures.BlockIcons.OVERLAY_COVER_CHEST_2)),
            context -> new CoverChest(context, 12, TextureFactory.of(Textures.BlockIcons.OVERLAY_COVER_CHEST_2)),
            CoverRegistry.PRIMITIVE_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_Chest_Advanced.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(Textures.BlockIcons.OVERLAY_COVER_CHEST_3)),
            context -> new CoverChest(context, 15, TextureFactory.of(Textures.BlockIcons.OVERLAY_COVER_CHEST_3)),
            CoverRegistry.PRIMITIVE_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_ActivityDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], doesWorkCoverTexture),
            context -> new CoverDoesWork(context, doesWorkCoverTexture));
        CoverRegistry.registerCover(
            ItemList.Cover_FluidDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_FLUIDDETECTOR)),
            context -> new CoverLiquidMeter(context, TextureFactory.of(OVERLAY_FLUIDDETECTOR)));
        CoverRegistry.registerCover(
            ItemList.Cover_ItemDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ITEMDETECTOR)),
            context -> new CoverItemMeter(context, TextureFactory.of(OVERLAY_ITEMDETECTOR)));
        CoverRegistry.registerCover(
            ItemList.Cover_EnergyDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ENERGYDETECTOR)),
            context -> new CoverEUMeter(context, TextureFactory.of(OVERLAY_ENERGYDETECTOR)));

        CoverRegistry.registerCover(
            ItemList.Cover_PlayerDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], playerDectectorCoverTexture),
            context -> new CoverPlayerDetector(context, playerDectectorCoverTexture));
        CoverRegistry.registerCover(
            ItemList.Cover_FluidStorageMonitor.get(1L),
            TextureFactory.of(OVERLAY_FLUID_STORAGE_MONITOR0),
            CoverFluidStorageMonitor::new);

        CoverRegistry.registerDecorativeCover(
            ItemList.Cover_Screen.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], screenCoverTexture));
        CoverRegistry.registerCover(
            ItemList.Cover_Crafting.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_CRAFTING)),
            context -> new CoverCrafting(context, TextureFactory.of(OVERLAY_CRAFTING)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_Drain.get(1L),
            TextureFactory.of(MACHINE_CASINGS[0][0], TextureFactory.of(OVERLAY_DRAIN)),
            context -> new CoverDrain(context, TextureFactory.of(OVERLAY_DRAIN)));
        CoverRegistry.registerCover(
            ItemList.Cover_Shutter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            context -> new CoverShutter(context, TextureFactory.of(OVERLAY_SHUTTER)));

        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel.get(1L),
            TextureFactory.of(SOLARPANEL),
            context -> new CoverSolarPanel(context, 1),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_8V.get(1L),
            TextureFactory.of(SOLARPANEL_8V),
            context -> new CoverSolarPanel(context, 8),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_LV.get(1L),
            TextureFactory.of(SOLARPANEL_LV),
            context -> new CoverSolarPanel(context, 32),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_MV.get(1L),
            TextureFactory.of(SOLARPANEL_MV),
            context -> new CoverSolarPanel(context, 128),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_HV.get(1L),
            TextureFactory.of(SOLARPANEL_HV),
            context -> new CoverSolarPanel(context, 512),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_EV.get(1L),
            TextureFactory.of(SOLARPANEL_EV),
            context -> new CoverSolarPanel(context, 2048),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_IV.get(1L),
            TextureFactory.of(SOLARPANEL_IV),
            context -> new CoverSolarPanel(context, 8192),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_LuV.get(1L),
            TextureFactory.of(SOLARPANEL_LuV),
            context -> new CoverSolarPanel(context, 32768),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_ZPM.get(1L),
            TextureFactory.of(SOLARPANEL_ZPM),
            context -> new CoverSolarPanel(context, 131072),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_SolarPanel_UV.get(1L),
            TextureFactory.of(SOLARPANEL_UV),
            context -> new CoverSolarPanel(context, 524288),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);

        CoverRegistry.registerCover(
            ItemList.Cover_RedstoneTransmitter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            context -> new CoverRedstoneTransmitterExternal(context, TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_RedstoneTransmitterInternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            context -> new CoverRedstoneTransmitterInternal(context, TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_RedstoneReceiver.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)),
            context -> new CoverRedstoneReceiverExternal(context, TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        CoverRegistry.registerCover(
            ItemList.Cover_WirelessController.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_WIRELESS_CONTROLLER)),
            context -> new CoverWirelessController(context, TextureFactory.of(OVERLAY_WIRELESS_CONTROLLER)),
            CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);

        CoverRegistry.registerCover(
            ItemList.Steam_Regulator_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamRegulator(context, 1024, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Regulator_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamRegulator(context, 2048, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Regulator_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamRegulator(context, 4096, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Regulator_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamRegulator(context, 8192, TextureFactory.of(OVERLAY_VALVE)));
        CoverRegistry.registerCover(
            ItemList.Steam_Regulator_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_VALVE)),
            context -> new CoverSteamRegulator(context, 16384, TextureFactory.of(OVERLAY_VALVE)));

        CoverRegistry.registerCover(
            ItemList.Cover_NeedsMaintainance.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_MAINTENANCE_DETECTOR)),
            context -> new CoverNeedMaintainance(context, TextureFactory.of(OVERLAY_MAINTENANCE_DETECTOR)));

        for (int i = 0; i < 14; i++) {
            int tier = i + 1;
            CoverRegistry.registerCover(
                ItemList.WIRELESS_ENERGY_COVERS[i].get(1),
                TextureFactory.of(MACHINE_CASINGS[i + 1][0], Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS[0]),
                context -> new CoverEnergyWireless(context, (int) GTValues.V[tier]),
                CoverRegistry.INTERCEPTS_RIGHT_CLICK_COVER_PLACER);
        }

        CoverRegistry.registerCover(
            ItemList.Cover_Wireless_Energy_Debug.get(1L),
            TextureFactory.of(MACHINE_CASINGS[14][0], TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG)),
            CoverEnergyWirelessDebug::new);

    }

    private void removeRecipes() {
        GTModHandler.removeRecipe(
            new ItemStack(Blocks.glass),
            null,
            new ItemStack(Blocks.glass),
            null,
            new ItemStack(Blocks.glass));
    }

    private void craftingShapedRecipes() {
        ItemStack tStack = new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID);
        tStack.setStackDisplayName("The holy Planks of Sengir");
        GTUtility.ItemNBT.addEnchantment(tStack, Enchantment.smite, 10);
        GTModHandler.addCraftingRecipe(
            tStack,
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "XXX", "XDX", "XXX", 'X', OrePrefixes.gem.get(Materials.NetherStar), 'D',
                new ItemStack(Blocks.dragon_egg, 1, 32767) });

        GTModHandler.addCraftingRecipe(
            ItemList.Shape_Slicer_Flat.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", aTextShape, "fXd", 'P', ItemList.Shape_Extruder_Block, 'X',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Shape_Slicer_Stripes.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", "XPX", "fXd", 'P', ItemList.Shape_Extruder_Block, 'X',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel) });

        GTModHandler.addCraftingRecipe(
            ItemList.Fuel_Can_Plastic_Empty.get(7L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " PP", "P P", "PPP", 'P', OrePrefixes.plate.get(Materials.Polyethylene) });

        GTModHandler.addCraftingRecipe(
            ItemList.Schematic_1by1.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "d  ", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GTModHandler.addCraftingRecipe(
            ItemList.Schematic_2by2.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " d ", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GTModHandler.addCraftingRecipe(
            ItemList.Schematic_3by3.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  d", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GTModHandler.addCraftingRecipe(
            ItemList.Schematic_Dust.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { aTextEmptyRow, aTextShape, "  d", 'P', ItemList.Schematic });
        GTModHandler.addCraftingRecipe(
            ItemList.Schematic_Dust_Small.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { aTextEmptyRow, aTextShape, " d ", 'P', ItemList.Schematic });

        GTModHandler.addCraftingRecipe(
            ItemList.Battery_Hull_LV.get(1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "Cf ", "Ph ", "Ps ", 'P', OrePrefixes.plate.get(Materials.BatteryAlloy), 'C',
                OreDictNames.craftingWireTin });

        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.IronMagnetic), 'R',
                OrePrefixes.stick.get(Materials.AnyIron), 'W', OrePrefixes.wireGt01.get(Materials.AnyCopper), 'C',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Steel), 'W', OrePrefixes.wireGt01.get(Materials.AnyCopper), 'C',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Aluminium), 'W', OrePrefixes.wireGt02.get(Materials.Cupronickel), 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.StainlessSteel), 'W', OrePrefixes.wireGt04.get(Materials.Electrum), 'C',
                OrePrefixes.cableGt02.get(Materials.Silver) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.NeodymiumMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Titanium), 'W', OrePrefixes.wireGt04.get(Materials.BlackSteel), 'C',
                OrePrefixes.cableGt02.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.NeodymiumMagnetic), 'R',
                OrePrefixes.stick.get(Materials.TungstenSteel), 'W', OrePrefixes.wireGt04.get(Materials.Graphene), 'C',
                OrePrefixes.cableGt02.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Steel), 'S',
                OrePrefixes.stick.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'M',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'S',
                OrePrefixes.stick.get(Materials.Aluminium), 'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'M',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.stick.get(Materials.StainlessSteel), 'G',
                OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Titanium), 'S',
                OrePrefixes.stick.get(Materials.Titanium), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium), 'M',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'S',
                OrePrefixes.stick.get(Materials.TungstenSteel), 'G',
                OrePrefixes.gearGtSmall.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_LV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Tin), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'P',
                OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_MV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Bronze), 'S',
                OrePrefixes.screw.get(Materials.Bronze), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'P',
                OrePrefixes.pipeMedium.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_HV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Steel), 'S',
                OrePrefixes.screw.get(Materials.Steel), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'P',
                OrePrefixes.pipeMedium.get(Materials.StainlessSteel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_EV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium),
                'P', OrePrefixes.pipeMedium.get(Materials.Titanium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_IV, 'O',
                OrePrefixes.ring.get(Materials.AnySyntheticRubber), 'X', OrePrefixes.rotor.get(Materials.TungstenSteel),
                'S', OrePrefixes.screw.get(Materials.TungstenSteel), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten),
                'P', OrePrefixes.pipeMedium.get(Materials.TungstenSteel) });

        GTModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tin), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GTModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_MV, 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GTModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GTModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GTModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'R',
                OrePrefixes.plate.get(Materials.AnySyntheticRubber) });

        GTModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Steel), 'M',
                ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                OrePrefixes.circuit.get(Materials.LV), 'C', OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Aluminium), 'M',
                ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                OrePrefixes.circuit.get(Materials.MV), 'C', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.StainlessSteel), 'M',
                ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                OrePrefixes.circuit.get(Materials.HV), 'C', OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Titanium), 'M',
                ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                OrePrefixes.circuit.get(Materials.EV), 'C', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.TungstenSteel), 'M',
                ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                OrePrefixes.circuit.get(Materials.IV), 'C', OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Emitter_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.CertusQuartz), 'S',
                OrePrefixes.stick.get(Materials.Brass), 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GTModHandler.addCraftingRecipe(
            ItemList.Emitter_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.EnderPearl), 'S',
                OrePrefixes.stick.get(Materials.Electrum), 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GTModHandler.addCraftingRecipe(
            ItemList.Emitter_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.EnderEye), 'S',
                OrePrefixes.stick.get(Materials.Chrome), 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GTModHandler.addCraftingRecipe(
            ItemList.Emitter_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', ItemList.QuantumEye, 'S',
                OrePrefixes.stick.get(Materials.Platinum), 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            ItemList.Emitter_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', ItemList.QuantumStar, 'S',
                OrePrefixes.stick.get(Materials.Iridium), 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GTModHandler.addCraftingRecipe(
            ItemList.Sensor_LV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gem.get(Materials.CertusQuartz), 'S',
                OrePrefixes.stick.get(Materials.Brass), 'P', OrePrefixes.plate.get(Materials.Steel), 'C',
                OrePrefixes.circuit.get(Materials.LV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Sensor_MV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gemFlawless.get(Materials.Emerald), 'S',
                OrePrefixes.stick.get(Materials.Electrum), 'P', OrePrefixes.plate.get(Materials.Aluminium), 'C',
                OrePrefixes.circuit.get(Materials.MV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Sensor_HV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gem.get(Materials.EnderEye), 'S',
                OrePrefixes.stick.get(Materials.Chrome), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'C',
                OrePrefixes.circuit.get(Materials.HV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Sensor_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', ItemList.QuantumEye, 'S',
                OrePrefixes.stick.get(Materials.Platinum), 'P', OrePrefixes.plate.get(Materials.Titanium), 'C',
                OrePrefixes.circuit.get(Materials.EV) });
        GTModHandler.addCraftingRecipe(
            ItemList.Sensor_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', ItemList.QuantumStar, 'S',
                OrePrefixes.stick.get(Materials.Iridium), 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'C',
                OrePrefixes.circuit.get(Materials.IV) });

        GTModHandler.addCraftingRecipe(
            ItemList.Component_Sawblade_Diamond.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " D ", "DGD", " D ", 'D', OrePrefixes.dustSmall.get(Materials.Diamond), 'G',
                OrePrefixes.gearGt.get(Materials.CobaltBrass) });
        GTModHandler.addCraftingRecipe(
            ItemList.Component_Grinder_Diamond.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "DSD", "SIS", "DSD", 'I', OrePrefixes.gem.get(Materials.Diamond), 'D',
                OrePrefixes.dust.get(Materials.Diamond), 'S', OrePrefixes.plateDouble.get(Materials.Steel) });
        GTModHandler.addCraftingRecipe(
            ItemList.Component_Grinder_Tungsten.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "TST", "SIS", "TST", 'I', OreDictNames.craftingIndustrialDiamond, 'T',
                OrePrefixes.plate.get(Materials.Tungsten), 'S', OrePrefixes.plateDouble.get(Materials.Steel) });

        GTModHandler.addCraftingRecipe(
            ItemList.Cover_Screen.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "AGA", "RPB", "ALA", 'A', OrePrefixes.plate.get(Materials.Aluminium), 'L',
                OrePrefixes.dust.get(Materials.Glowstone), 'R', Dyes.dyeRed, 'G', Dyes.dyeLime, 'B', Dyes.dyeBlue, 'P',
                OrePrefixes.plate.get(Materials.Glass) });

        GTModHandler.addCraftingRecipe(
            ItemList.Tool_Scanner.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "EPR", "CSC", "PBP", 'C', OrePrefixes.circuit.get(Materials.HV), 'P',
                OrePrefixes.plate.get(Materials.Aluminium), 'E', ItemList.Emitter_MV, 'R', ItemList.Sensor_MV, 'S',
                ItemList.Cover_Screen, 'B', ItemList.Battery_RE_MV_Lithium });

        GTModHandler.addCraftingRecipe(
            ItemList.ItemFilter_Export.get(1L),
            new Object[] { "SPS", "dIC", "SPS", 'P', OrePrefixes.plate.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Iron), 'I', ItemList.Component_Filter, 'C',
                ItemList.Conveyor_Module_LV });
        GTModHandler.addCraftingRecipe(
            ItemList.ItemFilter_Import.get(1L),
            new Object[] { "SPS", "CId", "SPS", 'P', OrePrefixes.plate.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Iron), 'I', ItemList.Component_Filter, 'C',
                ItemList.Conveyor_Module_LV });

        GTModHandler.addCraftingRecipe(
            ItemList.Tool_Cover_Copy_Paste.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PSP", "PCP", "PBP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'S',
                ItemList.Tool_DataStick.get(1L), 'C', ItemList.Cover_Screen.get(1L), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });
    }

    private void craftingShapelessRecipes() {

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Coin_Chocolate.get(1L),
            new Object[] { OrePrefixes.dust.get(Materials.Cocoa), OrePrefixes.dust.get(Materials.Milk),
                OrePrefixes.dust.get(Materials.Sugar), OrePrefixes.foil.get(Materials.Gold) });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Copper.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Iron });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Iron.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Silver });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Silver.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Gold });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Gold.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Platinum });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Platinum.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Osmium });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Iron.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper,
                ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper,
                ItemList.Credit_Copper });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Silver.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron,
                ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Gold.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver,
                ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver,
                ItemList.Credit_Silver });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Platinum.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold,
                ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Osmium.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum,
                ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum,
                ItemList.Credit_Platinum });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Copper.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Cupronickel });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Cupronickel.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Silver });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Silver.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Gold });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Gold.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Platinum });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Platinum.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Osmium });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Osmium.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Naquadah });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Naquadah.get(8L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Neutronium });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Cupronickel.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper,
                ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper,
                ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Silver.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel,
                ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel,
                ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Gold.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver,
                ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver,
                ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Platinum.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold,
                ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold,
                ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Osmium.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum,
                ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum,
                ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Naquadah.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium,
                ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium,
                ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Neutronium.get(1L),
            GTModHandler.RecipeBits.KEEPNBT | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah,
                ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah,
                ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_Crafting });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_1by1 });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_2by2 });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_3by3 });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_Dust });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_Dust_Small });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Tool_DataOrb.get(1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Tool_DataOrb.get(1L) });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.Tool_DataStick.get(1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Tool_DataStick.get(1L) });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.ItemFilter_Export.get(1L),
            new Object[] { ItemList.ItemFilter_Import.get(1L) });
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.ItemFilter_Import.get(1L),
            new Object[] { ItemList.ItemFilter_Export.get(1L) });

    }

    private void registerBehaviors() {
        IItemBehaviour<MetaBaseItem> behaviourSprayColorRemover = new BehaviourSprayColorRemover(
            ItemList.Spray_Empty.get(1L),
            ItemList.Spray_Color_Used_Remover.get(1L),
            ItemList.Spray_Color_Remover.get(1L),
            1024L);
        addItemBehavior(32000 + Spray_Color_Remover.ID, behaviourSprayColorRemover);
        addItemBehavior(32000 + Spray_Color_Used_Remover.ID, behaviourSprayColorRemover);

        addItemBehavior(
            32000 + Spray_Color_Infinite.ID,
            new BehaviourSprayColorInfinite(ItemList.Spray_Color_Infinite.get(1L)));

        IItemBehaviour<MetaBaseItem> behaviourMatches = new BehaviourLighter(
            null,
            ItemList.Tool_Matches.get(1L),
            ItemList.Tool_Matches.get(1L),
            1L);
        addItemBehavior(32000 + Tool_Matches.ID, behaviourMatches);
        IItemBehaviour<MetaBaseItem> behaviourMatchBox = new BehaviourLighter(
            null,
            ItemList.Tool_MatchBox_Used.get(1L),
            ItemList.Tool_MatchBox_Full.get(1L),
            16L);
        addItemBehavior(32000 + Tool_MatchBox_Used.ID, behaviourMatchBox);
        addItemBehavior(32000 + Tool_MatchBox_Full.ID, behaviourMatchBox);

        IItemBehaviour<MetaBaseItem> behaviourLighterInvar = new BehaviourLighter(
            ItemList.Tool_Lighter_Invar_Empty.get(1L),
            ItemList.Tool_Lighter_Invar_Used.get(1L),
            ItemList.Tool_Lighter_Invar_Full.get(1L),
            100L);
        addItemBehavior(32000 + Tool_Lighter_Invar_Used.ID, behaviourLighterInvar);
        addItemBehavior(32000 + Tool_Lighter_Invar_Full.ID, behaviourLighterInvar);

        IItemBehaviour<MetaBaseItem> behaviourLighterPlatinum = new BehaviourLighter(
            ItemList.Tool_Lighter_Platinum_Empty.get(1L),
            ItemList.Tool_Lighter_Platinum_Used.get(1L),
            ItemList.Tool_Lighter_Platinum_Full.get(1L),
            1000L);
        addItemBehavior(32000 + Tool_Lighter_Platinum_Used.ID, behaviourLighterPlatinum);
        addItemBehavior(32000 + Tool_Lighter_Platinum_Full.ID, behaviourLighterPlatinum);

        for (int i = 0; i < 16; i++) {
            IItemBehaviour<MetaBaseItem> behaviourSprayColor = new BehaviourSprayColor(
                ItemList.Spray_Empty.get(1L),
                ItemList.SPRAY_CAN_DYES_USED[i].get(1L),
                ItemList.SPRAY_CAN_DYES[i].get(1L),
                Other.sprayCanUses,
                i);
            addItemBehavior(32000 + Spray_Colors[i], behaviourSprayColor);
            addItemBehavior(32000 + Spray_Colors_Used[i], behaviourSprayColor);
        }
    }

    private void setAllElectricStats() {
        setElectricStats(32000 + Battery_RE_ULV_Tantalum.ID, 1000L, GTValues.V[0], 0L, -3L, false);
        setElectricStats(32000 + Battery_SU_LV_Sulfuric_Acid.ID, 18000L, GTValues.V[1], 1L, -2L, true);
        setElectricStats(32000 + Battery_SU_LV_Mercury.ID, 32000L, GTValues.V[1], 1L, -2L, true);
        setElectricStats(32000 + Battery_RE_LV_Cadmium.ID, 75000L, GTValues.V[1], 1L, -3L, true);
        setElectricStats(32000 + Battery_RE_LV_Lithium.ID, 100000L, GTValues.V[1], 1L, -3L, true);
        setElectricStats(32000 + Battery_RE_LV_Sodium.ID, 50000L, GTValues.V[1], 1L, -3L, true);
        setElectricStats(32000 + Battery_SU_MV_Sulfuric_Acid.ID, 72000L, GTValues.V[2], 2L, -2L, true);
        setElectricStats(32000 + Battery_SU_MV_Mercury.ID, 128000L, GTValues.V[2], 2L, -2L, true);
        setElectricStats(32000 + Battery_RE_MV_Cadmium.ID, 300000L, GTValues.V[2], 2L, -3L, true);
        setElectricStats(32000 + Battery_RE_MV_Lithium.ID, 400000L, GTValues.V[2], 2L, -3L, true);
        setElectricStats(32000 + Battery_RE_MV_Sodium.ID, 200000L, GTValues.V[2], 2L, -3L, true);
        setElectricStats(32000 + Battery_SU_HV_Sulfuric_Acid.ID, 288000L, GTValues.V[3], 3L, -2L, true);
        setElectricStats(32000 + Battery_SU_HV_Mercury.ID, 512000L, GTValues.V[3], 3L, -2L, true);
        setElectricStats(32000 + Battery_RE_HV_Cadmium.ID, 1200000L, GTValues.V[3], 3L, -3L, true);
        setElectricStats(32000 + Battery_RE_HV_Lithium.ID, 1600000L, GTValues.V[3], 3L, -3L, true);
        setElectricStats(32000 + Battery_RE_HV_Sodium.ID, 800000L, GTValues.V[3], 3L, -3L, true);
        setElectricStats(32000 + Energy_Lapotronic_Orb.ID, 100000000L, GTValues.V[5], 5L, -3L, true);
        setElectricStats(32000 + IDMetaItem01.ZPM.ID, 2000000000000L, GTValues.V[7], 7L, -2L, true);
        setElectricStats(32000 + Energy_Lapotronic_orb_2.ID, 1000000000L, GTValues.V[6], 6L, -3L, true);
        setElectricStats(32000 + ZPM2.ID, Long.MAX_VALUE, GTValues.V[8], 8L, -3L, true);
        setElectricStats(32000 + ZPM3.ID, Long.MAX_VALUE, GTValues.V[12], 12L, -3L, true);
        setElectricStats(32000 + ZPM4.ID, Long.MAX_VALUE, GTValues.V[13], 13L, -3L, true);
        setElectricStats(32000 + ZPM5.ID, Long.MAX_VALUE, GTValues.V[14], 14L, -3L, true);
        setElectricStats(32000 + ZPM6.ID, Long.MAX_VALUE, GTValues.V[15], 15L, -3L, true);
        setElectricStats(32000 + Energy_Module.ID, 10000000000L, GTValues.V[7], 7L, -3L, true);
        setElectricStats(32000 + Energy_Cluster.ID, 100000000000L, GTValues.V[8], 8L, -3L, true);
        setElectricStats(32000 + Tool_Cover_Copy_Paste.ID, 400000L, GTValues.V[2], 2L, -1L, false);
        setElectricStats(32000 + Tool_Cheat.ID, -2000000000L, 1000000000L, -1L, -3L, false);
        setElectricStats(32000 + Tool_Scanner.ID, 400000L, GTValues.V[2], 2L, -1L, false);
        setElectricStats(32000 + BatteryHull_EV_Full.ID, 6400000L, GTValues.V[4], 4L, -3L, true);
        setElectricStats(32000 + BatteryHull_IV_Full.ID, 25600000L, GTValues.V[5], 5L, -3L, true);
        setElectricStats(32000 + BatteryHull_LuV_Full.ID, 102400000L, GTValues.V[6], 6L, -3L, true);
        setElectricStats(32000 + BatteryHull_ZPM_Full.ID, 409600000L, GTValues.V[7], 7L, -3L, true);
        setElectricStats(32000 + BatteryHull_UV_Full.ID, 1638400000L, GTValues.V[8], 8L, -3L, true);
        setElectricStats(32000 + BatteryHull_UHV_Full.ID, 6553600000L, GTValues.V[9], 9L, -3L, true);
        setElectricStats(32000 + BatteryHull_UEV_Full.ID, 26214400000L, GTValues.V[10], 10L, -3L, true);
        setElectricStats(32000 + BatteryHull_UIV_Full.ID, 104857600000L, GTValues.V[11], 11L, -3L, true);
        setElectricStats(32000 + BatteryHull_UMV_Full.ID, 419430400000L, GTValues.V[12], 12L, -3L, true);
        setElectricStats(32000 + BatteryHull_UxV_Full.ID, 1677721600000L, GTValues.V[13], 13L, -3L, true);
    }

    private void registerSubIcons() {
        setSubIcons(Spray_Color_Infinite.ID, 3);
    }

    private void registerTieredTooltips() {
        registerTieredTooltip(ItemList.Battery_RE_ULV_Tantalum.get(1), ULV);
        registerTieredTooltip(ItemList.Battery_SU_LV_SulfuricAcid.get(1), LV);
        registerTieredTooltip(ItemList.Battery_SU_LV_Mercury.get(1), LV);
        registerTieredTooltip(ItemList.Battery_RE_LV_Cadmium.get(1), LV);
        registerTieredTooltip(ItemList.Battery_RE_LV_Lithium.get(1), LV);
        registerTieredTooltip(ItemList.Battery_RE_LV_Sodium.get(1), LV);
        registerTieredTooltip(ItemList.Battery_SU_MV_SulfuricAcid.get(1), MV);
        registerTieredTooltip(ItemList.Battery_SU_MV_Mercury.get(1), MV);
        registerTieredTooltip(ItemList.Battery_RE_MV_Cadmium.get(1), MV);
        registerTieredTooltip(ItemList.Battery_RE_MV_Lithium.get(1), MV);
        registerTieredTooltip(ItemList.Battery_RE_MV_Sodium.get(1), MV);
        registerTieredTooltip(ItemList.Battery_SU_HV_SulfuricAcid.get(1), HV);
        registerTieredTooltip(ItemList.Battery_SU_HV_Mercury.get(1), HV);
        registerTieredTooltip(ItemList.Battery_RE_HV_Cadmium.get(1), HV);
        registerTieredTooltip(ItemList.Battery_RE_HV_Lithium.get(1), HV);
        registerTieredTooltip(ItemList.Battery_RE_HV_Sodium.get(1), HV);
        registerTieredTooltip(ItemList.Energy_LapotronicOrb.get(1), IV);
        registerTieredTooltip(ItemList.ZPM.get(1), ZPM);
        registerTieredTooltip(ItemList.Energy_LapotronicOrb2.get(1), LuV);
        registerTieredTooltip(ItemList.ZPM2.get(1), UV);
        registerTieredTooltip(ItemList.ZPM3.get(1), UMV);
        registerTieredTooltip(ItemList.ZPM4.get(1), UXV);
        registerTieredTooltip(ItemList.ZPM5.get(1), MAX);
        registerTieredTooltip(ItemList.ZPM6.get(1), ERV);
        registerTieredTooltip(ItemList.Energy_Module.get(1), ZPM);
        registerTieredTooltip(ItemList.Energy_Cluster.get(1), UV);
        registerTieredTooltip(ItemList.Circuit_Primitive.get(1), ULV);
        registerTieredTooltip(ItemList.Circuit_Basic.get(1), LV);
        registerTieredTooltip(ItemList.Circuit_Good.get(1), MV);
        registerTieredTooltip(ItemList.Circuit_Advanced.get(1), HV);
        registerTieredTooltip(ItemList.Circuit_Data.get(1), EV);
        registerTieredTooltip(ItemList.Circuit_Elite.get(1), IV);
        registerTieredTooltip(ItemList.Circuit_Master.get(1), LuV);
        registerTieredTooltip(ItemList.BatteryHull_EV_Full.get(1), EV);
        registerTieredTooltip(ItemList.BatteryHull_IV_Full.get(1), IV);
        registerTieredTooltip(ItemList.BatteryHull_LuV_Full.get(1), LuV);
        registerTieredTooltip(ItemList.BatteryHull_ZPM_Full.get(1), ZPM);
        registerTieredTooltip(ItemList.BatteryHull_UV_Full.get(1), UV);
        registerTieredTooltip(ItemList.BatteryHull_UHV_Full.get(1), UHV);
        registerTieredTooltip(ItemList.BatteryHull_UEV_Full.get(1), UEV);
        registerTieredTooltip(ItemList.BatteryHull_UIV_Full.get(1), UIV);
        registerTieredTooltip(ItemList.BatteryHull_UMV_Full.get(1), UMV);
        registerTieredTooltip(ItemList.BatteryHull_UxV_Full.get(1), UXV);

    }

    @Override
    @Optional.Method(modid = Mods.ModIDs.RAILCRAFT)
    public boolean shouldBurn(ItemStack itemStack) {
        ItemData data = GTOreDictUnificator.getAssociation(itemStack);
        if (data == null || data.mMaterial == null
            || data.mMaterial.mMaterial != Materials.Firestone
            || data.mPrefix == null) {
            return false;
        }
        return data.mPrefix == OrePrefixes.dustTiny || data.mPrefix == OrePrefixes.dustSmall
            || data.mPrefix == OrePrefixes.dust
            || data.mPrefix == OrePrefixes.dustImpure
            || data.mPrefix == OrePrefixes.dustRefined
            || data.mPrefix == OrePrefixes.dustPure
            || data.mPrefix == OrePrefixes.crushed
            || data.mPrefix == OrePrefixes.crushedPurified
            || data.mPrefix == OrePrefixes.crushedCentrifuged
            || data.mPrefix == OrePrefixes.gem;
    }
}
