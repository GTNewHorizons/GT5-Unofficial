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
import static gregtech.common.items.IDMetaItem01.NC_SensorKit;
import static gregtech.common.items.IDMetaItem01.NaquadriaSupersolid;
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

        ItemList.Credit_Greg_Copper.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Copper.ID,
                "gt.item.credit.copper.name",
                "gt.item.credit.copper.tooltip"));
        ItemList.Credit_Greg_Cupronickel.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Cupronickel.ID,
                "gt.item.credit.cupronickel.name",
                "gt.item.credit.cupronickel.tooltip",
                new ItemData(Materials.Cupronickel, 907200L)));
        ItemList.Credit_Greg_Silver.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Silver.ID,
                "gt.item.credit.silver.name",
                "gt.item.credit.silver.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Credit_Greg_Gold.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Gold.ID,
                "gt.item.credit.gold.name",
                "gt.item.credit.gold.tooltip"));
        ItemList.Credit_Greg_Platinum.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Platinum.ID,
                "gt.item.credit.platinum.name",
                "gt.item.credit.platinum.tooltip"));
        ItemList.Credit_Greg_Osmium.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Osmium.ID,
                "gt.item.credit.osmium.name",
                "gt.item.credit.osmium.tooltip"));
        ItemList.Credit_Greg_Naquadah.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Naquadah.ID,
                "gt.item.credit.naquadah.name",
                "gt.item.credit.naquadah.tooltip"));
        ItemList.Credit_Greg_Neutronium.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Greg_Neutronium.ID,
                "gt.item.credit.neutronium.name",
                "gt.item.credit.neutronium.tooltip"));
        ItemList.Coin_Gold_Ancient.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Coin_Gold_Ancient.ID,
                "gt.item.coin.gold_ancient.name",
                "gt.item.coin.gold_ancient.tooltip",
                new ItemData(Materials.Gold, 907200L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 8L)));
        ItemList.Coin_Doge.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Coin_Doge.ID,
                "gt.item.coin.doge.name",
                "gt.item.coin.doge.tooltip",
                new ItemData(Materials.Brass, 907200L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Coin_Chocolate.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Coin_Chocolate.ID,
                "gt.item.coin.chocolate.name",
                "gt.item.coin.chocolate.tooltip",
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
        ItemList.Credit_Copper.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Copper.ID,
                "gt.item.credit.industrial.copper.name",
                "gt.item.credit.industrial.copper.tooltip"));

        ItemList.Credit_Silver.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Silver.ID,
                "gt.item.credit.industrial.silver.name",
                "gt.item.credit.industrial.silver.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Credit_Gold.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Gold.ID,
                "gt.item.credit.industrial.gold.name",
                "gt.item.credit.industrial.gold.tooltip"));
        ItemList.Credit_Platinum.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Platinum.ID,
                "gt.item.credit.industrial.platinum.name",
                "gt.item.credit.industrial.platinum.tooltip"));
        ItemList.Credit_Osmium.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.Credit_Osmium.ID,
                "gt.item.credit.industrial.osmium.name",
                "gt.item.credit.industrial.osmium.tooltip"));

        ItemList.NandChipArray.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.NandChipArray.ID,
                "gt.item.nand_chip_array.name",
                "gt.item.nand_chip_array.tooltip",
                "circuitPrimitiveArray",
                SubTag.NO_UNIFICATION));
        ItemList.Component_Minecart_Wheels_Iron.set(
            addItemWithLocalizationKeys(
                Component_Minecraft_Wheels_Iron.ID,
                "gt.item.minecart_wheels.iron.name",
                "gt.item.minecart_wheels.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L)));
        ItemList.Component_Minecart_Wheels_Steel.set(
            addItemWithLocalizationKeys(
                Component_Minecraft_Wheels_Steel.ID,
                "gt.item.minecart_wheels.steel.name",
                "gt.item.minecart_wheels.tooltip",
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

        ItemList.CompressedFireclay.set(
            addItemWithLocalizationKeys(
                Compressed_Fireclay.ID,
                "gt.item.compressed_fireclay.name",
                "gt.item.compressed_fireclay.tooltip"));
        GTOreDictUnificator.addItemDataFromInputs(ItemList.CompressedFireclay.get(1), Materials.Fireclay.getDust(1));

        ItemList.Firebrick
            .set(addItemWithLocalizationKeys(Firebrick.ID, "gt.item.firebrick.name", "gt.item.firebrick.tooltip"));
        GTOreDictUnificator.addItemDataFromInputs(ItemList.Firebrick.get(1), Materials.Fireclay.getDust(1));

        ItemList.Shape_Empty.set(
            addItemWithLocalizationKeys(
                Shape_Empty.ID,
                "gt.item.shape_empty.name",
                "gt.item.shape_empty.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L)));

        ItemList.Shape_Mold_Plate.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Plate.ID,
                "gt.item.shape_mold.plate.name",
                "gt.item.shape_mold.plate.tooltip"));
        ItemList.Shape_Mold_Casing.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Casing.ID,
                "gt.item.shape_mold.casing.name",
                "gt.item.shape_mold.casing.tooltip"));
        ItemList.Shape_Mold_Gear.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Gear.ID,
                "gt.item.shape_mold.gear.name",
                "gt.item.shape_mold.gear.tooltip"));
        ItemList.Shape_Mold_Credit.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Credit.ID,
                "gt.item.shape_mold.credit.name",
                "gt.item.shape_mold.credit.tooltip"));
        ItemList.Shape_Mold_Bottle.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Bottle.ID,
                "gt.item.shape_mold.bottle.name",
                "gt.item.shape_mold.bottle.tooltip"));
        ItemList.Shape_Mold_Ingot.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Ingot.ID,
                "gt.item.shape_mold.ingot.name",
                "gt.item.shape_mold.ingot.tooltip"));
        ItemList.Shape_Mold_Ball.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Ball.ID,
                "gt.item.shape_mold.ball.name",
                "gt.item.shape_mold.ball.tooltip"));
        ItemList.Shape_Mold_Block.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Block.ID,
                "gt.item.shape_mold.block.name",
                "gt.item.shape_mold.block.tooltip"));
        ItemList.Shape_Mold_Nugget.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Nugget.ID,
                "gt.item.shape_mold.nugget.name",
                "gt.item.shape_mold.nugget.tooltip"));
        ItemList.Shape_Mold_Bun.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Bun.ID,
                "gt.item.shape_mold.bun.name",
                "gt.item.shape_mold.bun.tooltip"));
        ItemList.Shape_Mold_Bread.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Bread.ID,
                "gt.item.shape_mold.bread.name",
                "gt.item.shape_mold.bread.tooltip"));
        ItemList.Shape_Mold_Baguette.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Baguette.ID,
                "gt.item.shape_mold.baguette.name",
                "gt.item.shape_mold.baguette.tooltip"));
        ItemList.Shape_Mold_Cylinder.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Cylinder.ID,
                "gt.item.shape_mold.cylinder.name",
                "gt.item.shape_mold.cylinder.tooltip"));
        ItemList.Shape_Mold_Anvil.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Anvil.ID,
                "gt.item.shape_mold.anvil.name",
                "gt.item.shape_mold.anvil.tooltip"));
        ItemList.Shape_Mold_Name.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Name.ID,
                "gt.item.shape_mold.name.name",
                "gt.item.shape_mold.name.tooltip"));
        ItemList.Shape_Mold_Arrow.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Arrow.ID,
                "gt.item.shape_mold.arrow.name",
                "gt.item.shape_mold.arrow.tooltip"));
        ItemList.Shape_Mold_Gear_Small.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Gear_Small.ID,
                "gt.item.shape_mold.gear_small.name",
                "gt.item.shape_mold.gear_small.tooltip"));
        ItemList.Shape_Mold_Rod.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Rod.ID,
                "gt.item.shape_mold.rod.name",
                "gt.item.shape_mold.rod.tooltip"));
        ItemList.Shape_Mold_Bolt.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Bolt.ID,
                "gt.item.shape_mold.bolt.name",
                "gt.item.shape_mold.bolt.tooltip"));
        ItemList.Shape_Mold_Round.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Round.ID,
                "gt.item.shape_mold.round.name",
                "gt.item.shape_mold.round.tooltip"));
        ItemList.Shape_Mold_Screw.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Screw.ID,
                "gt.item.shape_mold.screw.name",
                "gt.item.shape_mold.screw.tooltip"));
        ItemList.Shape_Mold_Ring.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Ring.ID,
                "gt.item.shape_mold.ring.name",
                "gt.item.shape_mold.ring.tooltip"));
        ItemList.Shape_Mold_Rod_Long.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Rod_Long.ID,
                "gt.item.shape_mold.rod_long.name",
                "gt.item.shape_mold.rod_long.tooltip"));
        ItemList.Shape_Mold_Rotor.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Rotor.ID,
                "gt.item.shape_mold.rotor.name",
                "gt.item.shape_mold.rotor.tooltip"));
        ItemList.Shape_Mold_Turbine_Blade.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Turbine_Blade.ID,
                "gt.item.shape_mold.turbine_blade.name",
                "gt.item.shape_mold.turbine_blade.tooltip"));
        ItemList.Shape_Mold_Pipe_Tiny.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Pipe_Tiny.ID,
                "gt.item.shape_mold.pipe_tiny.name",
                "gt.item.shape_mold.pipe_tiny.tooltip"));
        ItemList.Shape_Mold_Pipe_Small.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Pipe_Small.ID,
                "gt.item.shape_mold.pipe_small.name",
                "gt.item.shape_mold.pipe_small.tooltip"));
        ItemList.Shape_Mold_Pipe_Medium.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Pipe_Medium.ID,
                "gt.item.shape_mold.pipe_medium.name",
                "gt.item.shape_mold.pipe_medium.tooltip"));
        ItemList.Shape_Mold_Pipe_Large.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Pipe_Large.ID,
                "gt.item.shape_mold.pipe_large.name",
                "gt.item.shape_mold.pipe_large.tooltip"));
        ItemList.Shape_Mold_Pipe_Huge.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Pipe_Huge.ID,
                "gt.item.shape_mold.pipe_huge.name",
                "gt.item.shape_mold.pipe_huge.tooltip"));
        ItemList.Shape_Mold_ToolHeadDrill.set(
            addItemWithLocalizationKeys(
                Shape_Mold_Tool_Head_Drill.ID,
                "gt.item.shape_mold.tool_head_drill.name",
                "gt.item.shape_mold.tool_head_drill.tooltip"));

        ItemList.Shape_Extruder_Plate.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Plate.ID,
                "gt.item.shape_extruder.plate.name",
                "gt.item.shape_extruder.plate.tooltip"));
        ItemList.Shape_Extruder_Rod.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Rod.ID,
                "gt.item.shape_extruder.rod.name",
                "gt.item.shape_extruder.rod.tooltip"));
        ItemList.Shape_Extruder_Bolt.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Bolt.ID,
                "gt.item.shape_extruder.bolt.name",
                "gt.item.shape_extruder.bolt.tooltip"));
        ItemList.Shape_Extruder_Ring.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Ring.ID,
                "gt.item.shape_extruder.ring.name",
                "gt.item.shape_extruder.ring.tooltip"));
        ItemList.Shape_Extruder_Cell.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Cell.ID,
                "gt.item.shape_extruder.cell.name",
                "gt.item.shape_extruder.cell.tooltip"));
        ItemList.Shape_Extruder_Ingot.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Ingot.ID,
                "gt.item.shape_extruder.ingot.name",
                "gt.item.shape_extruder.ingot.tooltip"));
        ItemList.Shape_Extruder_Wire.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Wire.ID,
                "gt.item.shape_extruder.wire.name",
                "gt.item.shape_extruder.wire.tooltip"));
        ItemList.Shape_Extruder_Casing.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Casing.ID,
                "gt.item.shape_extruder.casing.name",
                "gt.item.shape_extruder.casing.tooltip"));
        ItemList.Shape_Extruder_Pipe_Tiny.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Pipe_Tiny.ID,
                "gt.item.shape_extruder.pipe_tiny.name",
                "gt.item.shape_extruder.pipe_tiny.tooltip"));
        ItemList.Shape_Extruder_Pipe_Small.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Pipe_Small.ID,
                "gt.item.shape_extruder.pipe_small.name",
                "gt.item.shape_extruder.pipe_small.tooltip"));
        ItemList.Shape_Extruder_Pipe_Medium.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Pipe_Medium.ID,
                "gt.item.shape_extruder.pipe_medium.name",
                "gt.item.shape_extruder.pipe_medium.tooltip"));
        ItemList.Shape_Extruder_Pipe_Large.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Pipe_Large.ID,
                "gt.item.shape_extruder.pipe_large.name",
                "gt.item.shape_extruder.pipe_large.tooltip"));
        ItemList.Shape_Extruder_Pipe_Huge.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Pipe_Huge.ID,
                "gt.item.shape_extruder.pipe_huge.name",
                "gt.item.shape_extruder.pipe_huge.tooltip"));
        ItemList.Shape_Extruder_Block.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Block.ID,
                "gt.item.shape_extruder.block.name",
                "gt.item.shape_extruder.block.tooltip"));
        ItemList.Shape_Extruder_Sword.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Sword.ID,
                "gt.item.shape_extruder.sword.name",
                "gt.item.shape_extruder.sword.tooltip"));
        ItemList.Shape_Extruder_Pickaxe.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Pickaxe.ID,
                "gt.item.shape_extruder.pickaxe.name",
                "gt.item.shape_extruder.pickaxe.tooltip"));
        ItemList.Shape_Extruder_Shovel.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Shovel.ID,
                "gt.item.shape_extruder.shovel.name",
                "gt.item.shape_extruder.shovel.tooltip"));
        ItemList.Shape_Extruder_Axe.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Axe.ID,
                "gt.item.shape_extruder.axe.name",
                "gt.item.shape_extruder.axe.tooltip"));
        ItemList.Shape_Extruder_Hoe.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Hoe.ID,
                "gt.item.shape_extruder.hoe.name",
                "gt.item.shape_extruder.hoe.tooltip"));
        ItemList.Shape_Extruder_Hammer.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Hammer.ID,
                "gt.item.shape_extruder.hammer.name",
                "gt.item.shape_extruder.hammer.tooltip"));
        ItemList.Shape_Extruder_File.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_File.ID,
                "gt.item.shape_extruder.file.name",
                "gt.item.shape_extruder.file.tooltip"));
        ItemList.Shape_Extruder_Saw.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Saw.ID,
                "gt.item.shape_extruder.saw.name",
                "gt.item.shape_extruder.saw.tooltip"));
        ItemList.Shape_Extruder_Gear.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Gear.ID,
                "gt.item.shape_extruder.gear.name",
                "gt.item.shape_extruder.gear.tooltip"));
        ItemList.Shape_Extruder_Bottle.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Bottle.ID,
                "gt.item.shape_extruder.bottle.name",
                "gt.item.shape_extruder.bottle.tooltip"));
        ItemList.Shape_Extruder_Rotor.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Rotor.ID,
                "gt.item.shape_extruder.rotor.name",
                "gt.item.shape_extruder.rotor.tooltip"));
        ItemList.Shape_Extruder_Small_Gear.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Small_Gear.ID,
                "gt.item.shape_extruder.small_gear.name",
                "gt.item.shape_extruder.small_gear.tooltip"));
        ItemList.Shape_Extruder_Turbine_Blade.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Turbine_Blade.ID,
                "gt.item.shape_extruder.turbine_blade.name",
                "gt.item.shape_extruder.turbine_blade.tooltip"));
        ItemList.Shape_Extruder_ToolHeadDrill.set(
            addItemWithLocalizationKeys(
                Shape_Extruder_Tool_Head_Drill.ID,
                "gt.item.shape_extruder.tool_head_drill.name",
                "gt.item.shape_extruder.tool_head_drill.tooltip"));

        ItemList.Shape_Slicer_Flat.set(
            addItemWithLocalizationKeys(
                Shape_Slicer_Flat.ID,
                "gt.item.shape_slicer.flat.name",
                "gt.item.shape_slicer.flat.tooltip"));
        ItemList.Shape_Slicer_Stripes.set(
            addItemWithLocalizationKeys(
                Shape_Slicer_Stripes.ID,
                "gt.item.shape_slicer.stripes.name",
                "gt.item.shape_slicer.stripes.tooltip"));

        ItemList.Fuel_Can_Plastic_Empty.set(
            addItemWithLocalizationKeys(
                Fuel_Can_Plastic_Empty.ID,
                "gt.item.fuel_can.plastic.empty.name",
                "gt.item.fuel_can.plastic.empty.tooltip",
                new ItemData(Materials.Polyethylene, OrePrefixes.plate.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));
        ItemList.Fuel_Can_Plastic_Filled.set(
            addItemWithLocalizationKeys(
                Fuel_Can_Plastic_Filled.ID,
                "gt.item.fuel_can.plastic.filled.name",
                "gt.item.fuel_can.plastic.filled.tooltip",
                new ItemData(Materials.Polyethylene, OrePrefixes.plate.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));

        ItemList.Spray_Empty.set(
            addItemWithLocalizationKeys(
                Spray_Empty.ID,
                "gt.item.spray.empty.name",
                "gt.item.spray.empty.tooltip",
                new ItemData(
                    Materials.Tin,
                    OrePrefixes.plate.getMaterialAmount() * 2L,
                    Materials.Redstone,
                    OrePrefixes.dust.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));

        ItemList.ThermosCan_Empty.set(
            addItemWithLocalizationKeys(
                Thermos_Can_Empty.ID,
                "gt.item.thermos_can.empty.name",
                "gt.item.thermos_can.empty.tooltip",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.getMaterialAmount() + 2L * OrePrefixes.ring.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L)));

        ItemList.Large_Fluid_Cell_Steel.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_Steel.ID,
                "gt.item.large_fluid_cell.steel.name",
                "gt.item.large_fluid_cell.steel.tooltip",
                new ItemData(
                    Materials.Steel,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Bronze, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));

        ItemList.Large_Fluid_Cell_TungstenSteel.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_TungstenSteel.ID,
                "gt.item.large_fluid_cell.tungstensteel.name",
                "gt.item.large_fluid_cell.tungstensteel.tooltip",
                new ItemData(
                    Materials.TungstenSteel,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Platinum, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 9L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 7L)));

        ItemList.Large_Fluid_Cell_Aluminium.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_Aluminium.ID,
                "gt.item.large_fluid_cell.aluminium.name",
                "gt.item.large_fluid_cell.aluminium.tooltip",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Silver, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 5L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 3L)));

        ItemList.Large_Fluid_Cell_StainlessSteel.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_StainlessSteel.ID,
                "gt.item.large_fluid_cell.stainless_steel.name",
                "gt.item.large_fluid_cell.stainless_steel.tooltip",
                new ItemData(
                    Materials.StainlessSteel,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Electrum, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 6L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));

        ItemList.Large_Fluid_Cell_Titanium.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_Titanium.ID,
                "gt.item.large_fluid_cell.titanium.name",
                "gt.item.large_fluid_cell.titanium.tooltip",
                new ItemData(
                    Materials.Titanium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.RoseGold, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 7L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 5L)));

        ItemList.Large_Fluid_Cell_Chrome.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_Chrome.ID,
                "gt.item.large_fluid_cell.chrome.name",
                "gt.item.large_fluid_cell.chrome.tooltip",
                new ItemData(
                    Materials.Chrome,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Palladium, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 6L)));

        ItemList.Large_Fluid_Cell_Iridium.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_Iridium.ID,
                "gt.item.large_fluid_cell.iridium.name",
                "gt.item.large_fluid_cell.iridium.tooltip",
                new ItemData(
                    Materials.Iridium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Naquadah, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));

        ItemList.Large_Fluid_Cell_Osmium.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_Osmium.ID,
                "gt.item.large_fluid_cell.osmium.name",
                "gt.item.large_fluid_cell.osmium.tooltip",
                new ItemData(
                    Materials.Osmium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.ElectrumFlux, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 11L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 9L)));

        ItemList.Large_Fluid_Cell_Neutronium.set(
            addItemWithLocalizationKeys(
                Large_Fluid_Cell_Neutronium.ID,
                "gt.item.large_fluid_cell.neutronium.name",
                "gt.item.large_fluid_cell.neutronium.tooltip",
                new ItemData(
                    Materials.Neutronium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    new MaterialStack(Materials.Draconium, OrePrefixes.ring.getMaterialAmount() * 4L)),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 12L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 10L)));

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            ItemList.SPRAY_CAN_DYES[i].set(
                addItemWithLocalizationKeys(
                    Spray_Colors[i],
                    GTUtility.translate("gt.item.spray_can.dye.name", Dyes.get(i).mName),
                    GTUtility.translate("gt.item.spray_can.dye.tooltip.full"),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4L)));
            ItemList.SPRAY_CAN_DYES_USED[i].set(
                addItemWithLocalizationKeys(
                    Spray_Colors_Used[i],
                    GTUtility.translate("gt.item.spray_can.dye.name", Dyes.get(i).mName),
                    GTUtility.translate("gt.item.spray_can.dye.tooltip.used"),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 3L),
                    SubTag.INVISIBLE));
        }

        ItemList.Spray_Color_Remover.set(
            addItemWithLocalizationKeys(
                Spray_Color_Remover.ID,
                GTUtility.translate("gt.item.spray_can.solvent.name"),
                GTUtility.translate("gt.item.spray_can.solvent.tooltip.full"),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 8L)));
        ItemList.Spray_Color_Used_Remover.set(
            addItemWithLocalizationKeys(
                Spray_Color_Used_Remover.ID,
                GTUtility.translate("gt.item.spray_can.solvent.name"),
                GTUtility.translate("gt.item.spray_can.solvent.tooltip.used"),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 3L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 6L),
                SubTag.INVISIBLE));

        ItemList.Spray_Color_Remover_Empty.set(
            addItemWithLocalizationKeys(
                Spray_Color_Remover_Empty.ID,
                "gt.item.spray_can.solvent.empty.name",
                "gt.item.spray_can.solvent.empty.tooltip",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.getMaterialAmount() * 4L,
                    Materials.Redstone,
                    OrePrefixes.dust.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));

        ItemList.Spray_Color_Infinite.set(
            addItemWithLocalizationKeys(
                Spray_Color_Infinite.ID,
                "gt.item.spray_can.infinite.name",
                "gt.item.spray_can.infinite.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 16),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 8),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 8),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 4)));

        ItemList.Tool_Matches.set(
            addItemWithLocalizationKeys(
                Tool_Matches.ID,
                "gt.item.matches.name",
                "gt.item.matches.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));

        ItemList.Tool_MatchBox_Used.set(
            addItemWithLocalizationKeys(
                Tool_MatchBox_Used.ID,
                "gt.item.matchbox.used.name",
                "gt.item.matchbox.used.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                SubTag.INVISIBLE));

        ItemList.Tool_MatchBox_Full.set(
            addItemWithLocalizationKeys(
                Tool_MatchBox_Full.ID,
                "gt.item.matchbox.full.name",
                "gt.item.matchbox.full.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Tool_Lighter_Invar_Empty.set(
            addItemWithLocalizationKeys(
                Tool_Lighter_Invar_Empty.ID,
                "gt.item.lighter.invar.empty.name",
                "gt.item.lighter.invar.empty.tooltip",
                new ItemData(Materials.Invar, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Invar_Used.set(
            addItemWithLocalizationKeys(
                Tool_Lighter_Invar_Used.ID,
                "gt.item.lighter.invar.used.name",
                "gt.item.lighter.invar.used.tooltip",
                new ItemData(Materials.Invar, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Invar_Full.set(
            addItemWithLocalizationKeys(
                Tool_Lighter_Invar_Full.ID,
                "gt.item.lighter.invar.full.name",
                "gt.item.lighter.invar.full.tooltip",
                new ItemData(Materials.Invar, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Tool_Lighter_Platinum_Empty.set(
            addItemWithLocalizationKeys(
                Tool_Lighter_Platinum_Empty.ID,
                "gt.item.lighter.platinum.empty.name",
                "gt.item.lighter.platinum.empty.tooltip",
                new ItemData(Materials.Platinum, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Platinum_Used.set(
            addItemWithLocalizationKeys(
                Tool_Lighter_Platinum_Used.ID,
                "gt.item.lighter.platinum.used.name",
                "gt.item.lighter.platinum.used.tooltip",
                new ItemData(Materials.Platinum, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Platinum_Full.set(
            addItemWithLocalizationKeys(
                Tool_Lighter_Platinum_Full.ID,
                "gt.item.lighter.platinum.full.name",
                "gt.item.lighter.platinum.full.tooltip",
                new ItemData(Materials.Platinum, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Ingot_Heavy1.set(
            addItemWithLocalizationKeys(
                Ingot_Heavy1.ID,
                "gt.item.ingot.heavy_duty.t1.name",
                "gt.item.ingot.heavy_duty.t1.tooltip"));
        ItemList.Ingot_Heavy2.set(
            addItemWithLocalizationKeys(
                Ingot_Heavy2.ID,
                "gt.item.ingot.heavy_duty.t2.name",
                "gt.item.ingot.heavy_duty.t2.tooltip"));
        ItemList.Ingot_Heavy3.set(
            addItemWithLocalizationKeys(
                Ingot_Heavy3.ID,
                "gt.item.ingot.heavy_duty.t3.name",
                "gt.item.ingot.heavy_duty.t3.tooltip"));

        ItemList.Ingot_IridiumAlloy.set(
            addItemWithLocalizationKeys(
                Ingot_Iridium_Alloy.ID,
                "gt.item.ingot.iridium_alloy.name",
                "gt.item.ingot.iridium_alloy.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L)));

        ItemList.Paper_Printed_Pages.set(
            addItemWithLocalizationKeys(
                Paper_Printed_Pages.ID,
                "gt.item.paper.printed_pages.name",
                "gt.item.paper.printed_pages.tooltip",
                new ItemData(Materials.Paper, 10886400L),
                new BehaviourPrintedPages(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Paper_Magic_Empty.set(
            addItemWithLocalizationKeys(
                Paper_Magic_Empty.ID,
                GTUtility.translate("gt.item.paper.magic.empty.name"),
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1L)));
        ItemList.Paper_Magic_Page.set(
            addItemWithLocalizationKeys(
                Paper_Magic_Page.ID,
                GTUtility.translate("gt.item.paper.magic.page.name"),
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 2L)));
        ItemList.Paper_Magic_Pages.set(
            addItemWithLocalizationKeys(
                Paper_Magic_Pages.ID,
                GTUtility.translate("gt.item.paper.magic.pages.name"),
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 10886400L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 4L)));
        ItemList.Paper_Punch_Card_Empty.set(
            addItemWithLocalizationKeys(
                Paper_Punch_Card_Empty.ID,
                GTUtility.translate("gt.item.paper.punch_card.empty.name"),
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L)));
        ItemList.Paper_Punch_Card_Encoded.set(
            addItemWithLocalizationKeys(
                Paper_Punch_Card_Encoded.ID,
                GTUtility.translate("gt.item.paper.punch_card.encoded.name"),
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Book_Written_01.set(
            addItemWithLocalizationKeys(
                Book_Written_01.ID,
                GTUtility.translate("gt.item.book.written.01.name"),
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new BehaviourWrittenBook(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Book_Written_02.set(
            addItemWithLocalizationKeys(
                Book_Written_02.ID,
                GTUtility.translate("gt.item.book.written.02.name"),
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new BehaviourWrittenBook(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Book_Written_03.set(
            addItemWithLocalizationKeys(
                Book_Written_03.ID,
                GTUtility.translate("gt.item.book.written.03.name"),
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new BehaviourWrittenBook(),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));

        ItemList.Schematic.set(
            addItemWithLocalizationKeys(
                Schematic.ID,
                "gt.item.schematic.empty.name",
                "gt.item.schematic.empty.tooltip",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 1L)));
        ItemList.Schematic_Crafting.set(
            addItemWithLocalizationKeys(
                Schematic_Crafting.ID,
                "gt.item.schematic.crafting.name",
                "gt.item.schematic.crafting.tooltip",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_1by1.set(
            addItemWithLocalizationKeys(
                Schematic_1by1.ID,
                "gt.item.schematic.1x1.name",
                "gt.item.schematic.1x1.tooltip",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_2by2.set(
            addItemWithLocalizationKeys(
                Schematic_2by2.ID,
                "gt.item.schematic.2x2.name",
                "gt.item.schematic.2x2.tooltip",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_3by3.set(
            addItemWithLocalizationKeys(
                Schematic_3by3.ID,
                "gt.item.schematic.3x3.name",
                "gt.item.schematic.3x3.tooltip",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_Dust.set(
            addItemWithLocalizationKeys(
                Schematic_Dust.ID,
                "gt.item.schematic.dust.name",
                "gt.item.schematic.dust.tooltip",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));
        ItemList.Schematic_Dust_Small.set(
            addItemWithLocalizationKeys(
                Schematic_Dust_Small.ID,
                "gt.item.schematic.dust_small.name",
                "gt.item.schematic.dust_small.tooltip",
                new ItemData(Materials.Steel, 7257600L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)));

        ItemList.Battery_Hull_LV.set(
            addItemWithLocalizationKeys(
                Battery_Hull_LV.ID,
                "gt.item.battery_hull.lv.name",
                "gt.item.battery_hull.lv.tooltip",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.getMaterialAmount()),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Battery_Hull_MV.set(
            addItemWithLocalizationKeys(
                Battery_Hull_MV.ID,
                "gt.item.battery_hull.mv.name",
                "gt.item.battery_hull.mv.tooltip",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.getMaterialAmount() * 3L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));
        ItemList.Battery_Hull_HV.set(
            addItemWithLocalizationKeys(
                Battery_Hull_HV.ID,
                "gt.item.battery_hull.hv.name",
                "gt.item.battery_hull.hv.tooltip",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.getMaterialAmount() * 9L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1L)));

        // ULV Batteries
        ItemList.Battery_RE_ULV_Tantalum.set(
            addItemWithLocalizationKeys(
                Battery_RE_ULV_Tantalum.ID,
                "gt.item.battery.re.ulv.tantalum.name",
                "gt.item.battery.re.tooltip",
                "batteryULV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));

        // LV Batteries
        ItemList.Battery_SU_LV_SulfuricAcid.set(
            addItemWithLocalizationKeys(
                Battery_SU_LV_Sulfuric_Acid.ID,
                "gt.item.battery.su.lv.sulfuric_acid.name",
                "gt.item.battery.su.tooltip",
                "batteryLV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_SU_LV_Mercury.set(
            addItemWithLocalizationKeys(
                Battery_SU_LV_Mercury.ID,
                "gt.item.battery.su.lv.mercury.name",
                "gt.item.battery.su.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_RE_LV_Cadmium.set(
            addItemWithLocalizationKeys(
                Battery_RE_LV_Cadmium.ID,
                "gt.item.battery.re.lv.cadmium.name",
                "gt.item.battery.re.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                "batteryLV"));

        ItemList.Battery_RE_LV_Lithium.set(
            addItemWithLocalizationKeys(
                Battery_RE_LV_Lithium.ID,
                "gt.item.battery.re.lv.lithium.name",
                "gt.item.battery.re.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                "batteryLV"));

        ItemList.Battery_RE_LV_Sodium.set(
            addItemWithLocalizationKeys(
                Battery_RE_LV_Sodium.ID,
                "gt.item.battery.re.lv.sodium.name",
                "gt.item.battery.re.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                "batteryLV"));

        // MV Batteries
        ItemList.Battery_SU_MV_SulfuricAcid.set(
            addItemWithLocalizationKeys(
                Battery_SU_MV_Sulfuric_Acid.ID,
                "gt.item.battery.su.mv.sulfuric_acid.name",
                "gt.item.battery.su.tooltip",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_SU_MV_Mercury.set(
            addItemWithLocalizationKeys(
                Battery_SU_MV_Mercury.ID,
                "gt.item.battery.su.mv.mercury.name",
                "gt.item.battery.su.tooltip",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_RE_MV_Cadmium.set(
            addItemWithLocalizationKeys(
                Battery_RE_MV_Cadmium.ID,
                "gt.item.battery.re.mv.cadmium.name",
                "gt.item.battery.re.tooltip",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_RE_MV_Lithium.set(
            addItemWithLocalizationKeys(
                Battery_RE_MV_Lithium.ID,
                "gt.item.battery.re.mv.lithium.name",
                "gt.item.battery.re.tooltip",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        ItemList.Battery_RE_MV_Sodium.set(
            addItemWithLocalizationKeys(
                Battery_RE_MV_Sodium.ID,
                "gt.item.battery.re.mv.sodium.name",
                "gt.item.battery.re.tooltip",
                "batteryMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));

        // HV Batteries
        ItemList.Battery_SU_HV_SulfuricAcid.set(
            addItemWithLocalizationKeys(
                Battery_SU_HV_Sulfuric_Acid.ID,
                "gt.item.battery.su.hv.sulfuric_acid.name",
                "gt.item.battery.su.tooltip",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 8L)));

        ItemList.Battery_SU_HV_Mercury.set(
            addItemWithLocalizationKeys(
                Battery_SU_HV_Mercury.ID,
                "gt.item.battery.su.hv.mercury.name",
                "gt.item.battery.su.tooltip",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 8L)));

        ItemList.Battery_RE_HV_Cadmium.set(
            addItemWithLocalizationKeys(
                Battery_RE_HV_Cadmium.ID,
                "gt.item.battery.re.hv.cadmium.name",
                "gt.item.battery.re.tooltip",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_RE_HV_Lithium.set(
            addItemWithLocalizationKeys(
                Battery_RE_HV_Lithium.ID,
                "gt.item.battery.re.hv.lithium.name",
                "gt.item.battery.re.tooltip",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        ItemList.Battery_RE_HV_Sodium.set(
            addItemWithLocalizationKeys(
                Battery_RE_HV_Sodium.ID,
                "gt.item.battery.re.hv.sodium.name",
                "gt.item.battery.re.tooltip",
                "batteryHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L)));

        // IV Battery
        ItemList.Energy_LapotronicOrb.set(
            addItemWithLocalizationKeys(
                Energy_Lapotronic_Orb.ID,
                "gt.item.battery.lapotronic_orb.name",
                "gt.item.battery.re.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.IV)));

        // ZPM Module
        ItemList.ZPM.set(
            addItemWithLocalizationKeys(
                IDMetaItem01.ZPM.ID,
                "gt.item.battery.zpm.name",
                "gt.item.battery.su.tooltip",
                "batteryZPM",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // LuV Lapotron orb cluster battery
        ItemList.Energy_LapotronicOrb2.set(
            addItemWithLocalizationKeys(
                Energy_Lapotronic_orb_2.ID,
                "gt.item.battery.lapotronic_orb_cluster.name",
                "gt.item.battery.re.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.LuV)));

        // UV Battery
        ItemList.ZPM2.set(
            addItemWithLocalizationKeys(
                ZPM2.ID,
                "gt.item.battery.ultimate.name",
                "gt.item.battery.ultimate.tooltip",
                "batteryUV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // UMV Battery
        ItemList.ZPM3.set(
            addItemWithLocalizationKeys(
                ZPM3.ID,
                "gt.item.battery.really_ultimate.name",
                "gt.item.battery.really_ultimate.tooltip",
                "batteryUMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // UXV Battery
        ItemList.ZPM4.set(
            addItemWithLocalizationKeys(
                ZPM4.ID,
                "gt.item.battery.extremely_ultimate.name",
                "gt.item.battery.extremely_ultimate.tooltip",
                "batteryUXV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // MAX Battery
        ItemList.ZPM5.set(
            addItemWithLocalizationKeys(
                ZPM5.ID,
                "gt.item.battery.insanely_ultimate.name",
                "gt.item.battery.insanely_ultimate.tooltip",
                "batteryMAX",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // ERROR Battery
        ItemList.ZPM6.set(
            addItemWithLocalizationKeys(
                ZPM6.ID,
                "gt.item.battery.mega_ultimate.name",
                "gt.item.battery.mega_ultimate.tooltip",
                "batteryERV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L)));

        // ZPM Cluster
        ItemList.Energy_Module.set(
            addItemWithLocalizationKeys(
                Energy_Module.ID,
                "gt.item.battery.energy_module.name",
                "gt.item.battery.re.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.ZPM)));

        // UV Cluster
        ItemList.Energy_Cluster.set(
            addItemWithLocalizationKeys(
                Energy_Cluster.ID,
                "gt.item.battery.energy_cluster.name",
                "gt.item.battery.re.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                OrePrefixes.battery.get(Materials.UV)));

        // UIV, UMV, UXV and MAX component textures backported from gregicality.
        ItemList.Electric_Motor_LV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_LV.ID,
                GTUtility.translate("gt.item.electric_motor.lv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));
        ItemList.Electric_Motor_MV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_MV.ID,
                GTUtility.translate("gt.item.electric_motor.mv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L)));
        ItemList.Electric_Motor_HV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_HV.ID,
                GTUtility.translate("gt.item.electric_motor.hv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 4L)));
        ItemList.Electric_Motor_EV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_EV.ID,
                GTUtility.translate("gt.item.electric_motor.ev.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 8L)));
        ItemList.Electric_Motor_IV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_IV.ID,
                GTUtility.translate("gt.item.electric_motor.iv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 16L)));
        ItemList.Electric_Motor_LuV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_LuV.ID,
                GTUtility.translate("gt.item.electric_motor.luv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 32L)));
        ItemList.Electric_Motor_ZPM.set(
            addItemWithLocalizationKeys(
                Electric_Motor_ZPM.ID,
                GTUtility.translate("gt.item.electric_motor.zpm.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 64L)));
        ItemList.Electric_Motor_UV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_UV.ID,
                GTUtility.translate("gt.item.electric_motor.uv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 128L)));
        ItemList.Electric_Motor_UHV
            .set(
                addItemWithLocalizationKeys(
                    Electric_Motor_UHV.ID,
                    GTUtility.translate("gt.item.electric_motor.uhv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Electric_Motor_UEV
            .set(
                addItemWithLocalizationKeys(
                    Electric_Motor_UEV.ID,
                    GTUtility.translate("gt.item.electric_motor.uev.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Electric_Motor_UIV
            .set(
                addItemWithLocalizationKeys(
                    Electric_Motor_UIV.ID,
                    GTUtility.translate("gt.item.electric_motor.uiv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Electric_Motor_UMV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_UMV.ID,
                GTUtility.translate("gt.item.electric_motor.umv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UXV.set(
            addItemWithLocalizationKeys(
                Electric_Motor_UXV.ID,
                GTUtility.translate("gt.item.electric_motor.uxv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Motor_MAX.set(
            addItemWithLocalizationKeys(
                Electric_Motor_MAX.ID,
                GTUtility.translate("gt.item.electric_motor.max.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));

        ItemList.ElectronicsLump.set(
            addItemWithLocalizationKeys(
                414,
                "gt.item.electronics_lump.name",
                "gt.item.electronics_lump.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));

        ItemList.Tesseract
            .set(
                addItemWithLocalizationKeys(
                    Tesseract.ID,
                    GTUtility.translate("gt.item.tesseract.raw.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)))
            .setRender(new WireFrameTesseractRenderer(0, 0, 0));
        ItemList.GigaChad.set(
            addItemWithLocalizationKeys(
                GigaChad.ID,
                "gt.item.giga_chad_token.name",
                "gt.item.giga_chad_token.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1000L)));
        ItemList.EnergisedTesseract
            .set(
                addItemWithLocalizationKeys(
                    EnergisedTesseract.ID,
                    "gt.item.tesseract.energised.name",
                    "gt.item.tesseract.energised.tooltip",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)))
            .setRender(new WireFrameTesseractRenderer(23, 129, 166));

        ItemList.Electric_Piston_LV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_LV.ID,
                GTUtility.translate("gt.item.electric_piston.lv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));
        ItemList.Electric_Piston_MV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_MV.ID,
                GTUtility.translate("gt.item.electric_piston.mv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L)));
        ItemList.Electric_Piston_HV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_HV.ID,
                GTUtility.translate("gt.item.electric_piston.hv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 4L)));
        ItemList.Electric_Piston_EV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_EV.ID,
                GTUtility.translate("gt.item.electric_piston.ev.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 8L)));
        ItemList.Electric_Piston_IV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_IV.ID,
                GTUtility.translate("gt.item.electric_piston.iv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 16L)));
        ItemList.Electric_Piston_LuV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_LuV.ID,
                GTUtility.translate("gt.item.electric_piston.luv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 32L)));
        ItemList.Electric_Piston_ZPM.set(
            addItemWithLocalizationKeys(
                Electric_Piston_ZPM.ID,
                GTUtility.translate("gt.item.electric_piston.zpm.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 64L)));
        ItemList.Electric_Piston_UV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_UV.ID,
                GTUtility.translate("gt.item.electric_piston.uv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 128L)));
        ItemList.Electric_Piston_UHV
            .set(
                addItemWithLocalizationKeys(
                    Electric_Piston_UHV.ID,
                    GTUtility.translate("gt.item.electric_piston.uhv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Electric_Piston_UEV
            .set(
                addItemWithLocalizationKeys(
                    Electric_Piston_UEV.ID,
                    GTUtility.translate("gt.item.electric_piston.uev.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Electric_Piston_UIV
            .set(
                addItemWithLocalizationKeys(
                    Electric_Piston_UIV.ID,
                    GTUtility.translate("gt.item.electric_piston.uiv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Electric_Piston_UMV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_UMV.ID,
                GTUtility.translate("gt.item.electric_piston.umv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UXV.set(
            addItemWithLocalizationKeys(
                Electric_Piston_UXV.ID,
                GTUtility.translate("gt.item.electric_piston.uxv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));
        ItemList.Electric_Piston_MAX.set(
            addItemWithLocalizationKeys(
                Electric_Piston_MAX.ID,
                GTUtility.translate("gt.item.electric_piston.max.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L)));

        ItemList.Electric_Pump_LV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_LV.ID,
                GTUtility.translate("gt.item.electric_pump.lv.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(32), formatNumber(32 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Electric_Pump_MV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_MV.ID,
                GTUtility.translate("gt.item.electric_pump.mv.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(128), formatNumber(128 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.Electric_Pump_HV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_HV.ID,
                GTUtility.translate("gt.item.electric_pump.hv.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(512), formatNumber(512 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));
        ItemList.Electric_Pump_EV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_EV.ID,
                GTUtility.translate("gt.item.electric_pump.ev.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(2048), formatNumber(2048 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));
        ItemList.Electric_Pump_IV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_IV.ID,
                GTUtility.translate("gt.item.electric_pump.iv.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(8192), formatNumber(8192 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 16L)));
        ItemList.Electric_Pump_LuV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_LuV.ID,
                GTUtility.translate("gt.item.electric_pump.luv.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(32768), formatNumber(32768 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 32L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 32L)));
        ItemList.Electric_Pump_ZPM.set(
            addItemWithLocalizationKeys(
                Electric_Pump_ZPM.ID,
                GTUtility.translate("gt.item.electric_pump.zpm.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(131072), formatNumber(131072 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 64L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 64L)));
        ItemList.Electric_Pump_UV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_UV.ID,
                GTUtility.translate("gt.item.electric_pump.uv.name"),
                GTUtility.translate("gt.item.electric_pump.tooltip", formatNumber(524288), formatNumber(524288 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 128L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 128L)));
        ItemList.Electric_Pump_UHV
            .set(
                addItemWithLocalizationKeys(
                    Electric_Pump_UHV.ID,
                    GTUtility.translate("gt.item.electric_pump.uhv.name"),
                    GTUtility
                        .translate("gt.item.electric_pump.tooltip", formatNumber(8388608), formatNumber(8388608 * 20)),
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Electric_Pump_UEV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_UEV.ID,
                GTUtility.translate("gt.item.electric_pump.uev.name"),
                GTUtility
                    .translate("gt.item.electric_pump.tooltip", formatNumber(16777216), formatNumber(16777216 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Electric_Pump_UIV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_UIV.ID,
                GTUtility.translate("gt.item.electric_pump.uiv.name"),
                GTUtility
                    .translate("gt.item.electric_pump.tooltip", formatNumber(33554432), formatNumber(33554432 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Electric_Pump_UMV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_UMV.ID,
                GTUtility.translate("gt.item.electric_pump.umv.name"),
                GTUtility
                    .translate("gt.item.electric_pump.tooltip", formatNumber(67108864), formatNumber(67108864 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)));
        ItemList.Electric_Pump_UXV.set(
            addItemWithLocalizationKeys(
                Electric_Pump_UXV.ID,
                GTUtility.translate("gt.item.electric_pump.uxv.name"),
                GTUtility
                    .translate("gt.item.electric_pump.tooltip", formatNumber(134217728), formatNumber(134217728 * 20L)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)));
        ItemList.Electric_Pump_MAX.set(
            addItemWithLocalizationKeys(
                Electric_Pump_MAX.ID,
                GTUtility.translate("gt.item.electric_pump.max.name"),
                GTUtility
                    .translate("gt.item.electric_pump.tooltip", formatNumber(268435456), formatNumber(268435456 * 20L)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 512L)));

        ItemList.Steam_Valve_LV.set(
            addItemWithLocalizationKeys(
                Steam_Valve_LV.ID,
                GTUtility.translate("gt.item.steam_valve.lv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(1024), formatNumber(1024 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Steam_Valve_MV.set(
            addItemWithLocalizationKeys(
                Steam_Valve_MV.ID,
                GTUtility.translate("gt.item.steam_valve.mv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(2048), formatNumber(2048 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.Steam_Valve_HV.set(
            addItemWithLocalizationKeys(
                Steam_Valve_HV.ID,
                GTUtility.translate("gt.item.steam_valve.hv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(4096), formatNumber(4096 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));
        ItemList.Steam_Valve_EV.set(
            addItemWithLocalizationKeys(
                Steam_Valve_EV.ID,
                GTUtility.translate("gt.item.steam_valve.ev.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(8192), formatNumber(8192 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));
        ItemList.Steam_Valve_IV.set(
            addItemWithLocalizationKeys(
                Steam_Valve_IV.ID,
                GTUtility.translate("gt.item.steam_valve.iv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(16384), formatNumber(16384 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 16L)));

        ItemList.FluidRegulator_LV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_LV.ID,
                GTUtility.translate("gt.item.fluid_regulator.lv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(32 * 20))));
        ItemList.FluidRegulator_MV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_MV.ID,
                GTUtility.translate("gt.item.fluid_regulator.mv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(128 * 20))));
        ItemList.FluidRegulator_HV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_HV.ID,
                GTUtility.translate("gt.item.fluid_regulator.hv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(512 * 20))));
        ItemList.FluidRegulator_EV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_EV.ID,
                GTUtility.translate("gt.item.fluid_regulator.ev.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(2048 * 20))));
        ItemList.FluidRegulator_IV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_IV.ID,
                GTUtility.translate("gt.item.fluid_regulator.iv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(8192 * 20))));
        ItemList.FluidRegulator_LuV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_LuV.ID,
                GTUtility.translate("gt.item.fluid_regulator.luv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(32768 * 20))));
        ItemList.FluidRegulator_ZPM.set(
            addItemWithLocalizationKeys(
                FluidRegulator_ZPM.ID,
                GTUtility.translate("gt.item.fluid_regulator.zpm.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(131072 * 20))));
        ItemList.FluidRegulator_UV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_UV.ID,
                GTUtility.translate("gt.item.fluid_regulator.uv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(524288 * 20))));
        ItemList.FluidRegulator_UHV
            .set(
                addItemWithLocalizationKeys(
                    FluidRegulator_UHV.ID,
                    GTUtility.translate("gt.item.fluid_regulator.uhv.name"),
                    GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(8388608 * 20))))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.FluidRegulator_UEV
            .set(
                addItemWithLocalizationKeys(
                    FluidRegulator_UEV.ID,
                    GTUtility.translate("gt.item.fluid_regulator.uev.name"),
                    GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(16777216 * 20))))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.FluidRegulator_UIV
            .set(
                addItemWithLocalizationKeys(
                    FluidRegulator_UIV.ID,
                    GTUtility.translate("gt.item.fluid_regulator.uiv.name"),
                    GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(33554432 * 20))))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.FluidRegulator_UMV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_UMV.ID,
                GTUtility.translate("gt.item.fluid_regulator.umv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(67108864 * 20))));
        ItemList.FluidRegulator_UXV.set(
            addItemWithLocalizationKeys(
                FluidRegulator_UXV.ID,
                GTUtility.translate("gt.item.fluid_regulator.uxv.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(134217728 * 20L))));
        ItemList.FluidRegulator_MAX.set(
            addItemWithLocalizationKeys(
                FluidRegulator_MAX.ID,
                GTUtility.translate("gt.item.fluid_regulator.max.name"),
                GTUtility.translate("gt.item.fluid_regulator.tooltip", formatNumber(268435456 * 20L))));

        ItemList.FluidFilter.set(
            addItemWithLocalizationKeys(FluidFilter.ID, "gt.item.fluid_filter.name", "gt.item.fluid_filter.tooltip"));

        ItemList.ItemFilter_Export.set(
            addItemWithLocalizationKeys(
                ItemFilter_Export.ID,
                "gt.item.item_filter.export.name",
                "gt.item.item_filter.export.tooltip"));

        ItemList.ItemFilter_Import.set(
            addItemWithLocalizationKeys(
                ItemFilter_Import.ID,
                "gt.item.item_filter.import.name",
                "gt.item.item_filter.import.tooltip"));

        ItemList.Cover_FluidLimiter.set(
            addItemWithLocalizationKeys(
                Cover_FluidLimiter.ID,
                "gt.item.cover.fluid_limiter.name",
                "gt.item.cover.fluid_limiter.tooltip"));

        ItemList.Conveyor_Module_LV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_LV.ID,
                "gt.item.conveyor_module.lv.name",
                "gt.item.conveyor_module.lv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));
        ItemList.Conveyor_Module_MV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_MV.ID,
                "gt.item.conveyor_module.mv.name",
                "gt.item.conveyor_module.mv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L)));
        ItemList.Conveyor_Module_HV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_HV.ID,
                "gt.item.conveyor_module.hv.name",
                "gt.item.conveyor_module.hv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L)));
        ItemList.Conveyor_Module_EV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_EV.ID,
                "gt.item.conveyor_module.ev.name",
                "gt.item.conveyor_module.ev.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L)));
        ItemList.Conveyor_Module_IV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_IV.ID,
                "gt.item.conveyor_module.iv.name",
                "gt.item.conveyor_module.iv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L)));
        ItemList.Conveyor_Module_LuV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_LuV.ID,
                "gt.item.conveyor_module.luv.name",
                "gt.item.conveyor_module.luv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 32L)));
        ItemList.Conveyor_Module_ZPM.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_ZPM.ID,
                "gt.item.conveyor_module.zpm.name",
                "gt.item.conveyor_module.zpm.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 64L)));
        ItemList.Conveyor_Module_UV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_UV.ID,
                "gt.item.conveyor_module.uv.name",
                "gt.item.conveyor_module.uv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 128L)));
        ItemList.Conveyor_Module_UHV
            .set(
                addItemWithLocalizationKeys(
                    Conveyor_Module_UHV.ID,
                    "gt.item.conveyor_module.uhv.name",
                    "gt.item.conveyor_module.uhv.tooltip",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Conveyor_Module_UEV
            .set(
                addItemWithLocalizationKeys(
                    Conveyor_Module_UEV.ID,
                    "gt.item.conveyor_module.uev.name",
                    "gt.item.conveyor_module.uev.tooltip",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Conveyor_Module_UIV
            .set(
                addItemWithLocalizationKeys(
                    Conveyor_Module_UIV.ID,
                    "gt.item.conveyor_module.uiv.name",
                    "gt.item.conveyor_module.uiv.tooltip",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Conveyor_Module_UMV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_UMV.ID,
                "gt.item.conveyor_module.umv.name",
                "gt.item.conveyor_module.umv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)));
        ItemList.Conveyor_Module_UXV.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_UXV.ID,
                "gt.item.conveyor_module.uxv.name",
                "gt.item.conveyor_module.uxv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)));
        ItemList.Conveyor_Module_MAX.set(
            addItemWithLocalizationKeys(
                Conveyor_Module_MAX.ID,
                "gt.item.conveyor_module.max.name",
                "gt.item.conveyor_module.max.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 512L)));

        ItemList.Robot_Arm_LV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_LV.ID,
                "gt.item.robot_arm.lv.name",
                "gt.item.robot_arm.lv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L)));
        ItemList.Robot_Arm_MV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_MV.ID,
                "gt.item.robot_arm.mv.name",
                "gt.item.robot_arm.mv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L)));
        ItemList.Robot_Arm_HV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_HV.ID,
                "gt.item.robot_arm.hv.name",
                "gt.item.robot_arm.hv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 4L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 4L)));
        ItemList.Robot_Arm_EV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_EV.ID,
                "gt.item.robot_arm.ev.name",
                "gt.item.robot_arm.ev.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 8L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 8L)));
        ItemList.Robot_Arm_IV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_IV.ID,
                "gt.item.robot_arm.iv.name",
                "gt.item.robot_arm.iv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 16L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 16L)));
        ItemList.Robot_Arm_LuV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_LuV.ID,
                "gt.item.robot_arm.luv.name",
                "gt.item.part_not_cover.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 32L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 32L)));
        ItemList.Robot_Arm_ZPM.set(
            addItemWithLocalizationKeys(
                Robot_Arm_ZPM.ID,
                "gt.item.robot_arm.zpm.name",
                "gt.item.part_not_cover.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 64L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 64L)));
        ItemList.Robot_Arm_UV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_UV.ID,
                "gt.item.robot_arm.uv.name",
                "gt.item.part_not_cover.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 128L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 128L)));
        ItemList.Robot_Arm_UHV
            .set(
                addItemWithLocalizationKeys(
                    Robot_Arm_UHV.ID,
                    "gt.item.robot_arm.uhv.name",
                    "gt.item.part_not_cover.tooltip",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Robot_Arm_UEV
            .set(
                addItemWithLocalizationKeys(
                    Robot_Arm_UEV.ID,
                    "gt.item.robot_arm.uev.name",
                    "gt.item.part_not_cover.tooltip",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Robot_Arm_UIV
            .set(
                addItemWithLocalizationKeys(
                    Robot_Arm_UIV.ID,
                    "gt.item.robot_arm.uiv.name",
                    "gt.item.part_not_cover.tooltip",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Robot_Arm_UMV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_UMV.ID,
                "gt.item.robot_arm.umv.name",
                "gt.item.part_not_cover.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UXV.set(
            addItemWithLocalizationKeys(
                Robot_Arm_UXV.ID,
                "gt.item.robot_arm.uxv.name",
                "gt.item.part_not_cover.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_MAX.set(
            addItemWithLocalizationKeys(
                Robot_Arm_MAX.ID,
                "gt.item.robot_arm.max.name",
                "gt.item.part_not_cover.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 512L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 512L)));

        ItemList.QuantumEye
            .set(addItemWithLocalizationKeys(QuantumEye.ID, "gt.item.quantum_eye.name", "gt.item.quantum_eye.tooltip"));
        ItemList.QuantumStar.set(
            addItemWithLocalizationKeys(QuantumStar.ID, "gt.item.quantum_star.name", "gt.item.quantum_star.tooltip"));
        ItemList.Gravistar
            .set(addItemWithLocalizationKeys(Gravistar.ID, "gt.item.gravistar.name", "gt.item.gravistar.tooltip"));

        ItemList.Emitter_LV.set(
            addItemWithLocalizationKeys(
                Emitter_LV.ID,
                GTUtility.translate("gt.item.emitter.lv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 1L)));
        ItemList.Emitter_MV.set(
            addItemWithLocalizationKeys(
                Emitter_MV.ID,
                GTUtility.translate("gt.item.emitter.mv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 2L)));
        ItemList.Emitter_HV.set(
            addItemWithLocalizationKeys(
                Emitter_HV.ID,
                GTUtility.translate("gt.item.emitter.hv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 4L)));
        ItemList.Emitter_EV.set(
            addItemWithLocalizationKeys(
                Emitter_EV.ID,
                GTUtility.translate("gt.item.emitter.ev.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 8L)));
        ItemList.Emitter_IV.set(
            addItemWithLocalizationKeys(
                Emitter_IV.ID,
                GTUtility.translate("gt.item.emitter.iv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 16L)));
        ItemList.Emitter_LuV.set(
            addItemWithLocalizationKeys(
                Emitter_LuV.ID,
                GTUtility.translate("gt.item.emitter.luv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 32L)));
        ItemList.Emitter_ZPM.set(
            addItemWithLocalizationKeys(
                Emitter_ZPM.ID,
                GTUtility.translate("gt.item.emitter.zpm.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 64L)));
        ItemList.Emitter_UV.set(
            addItemWithLocalizationKeys(
                Emitter_UV.ID,
                GTUtility.translate("gt.item.emitter.uv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 128L)));
        ItemList.Emitter_UHV
            .set(
                addItemWithLocalizationKeys(
                    Emitter_UHV.ID,
                    GTUtility.translate("gt.item.emitter.uhv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.LUX, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Emitter_UEV
            .set(
                addItemWithLocalizationKeys(
                    Emitter_UEV.ID,
                    GTUtility.translate("gt.item.emitter.uev.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Emitter_UIV
            .set(
                addItemWithLocalizationKeys(
                    Emitter_UIV.ID,
                    GTUtility.translate("gt.item.emitter.uiv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Emitter_UMV.set(
            addItemWithLocalizationKeys(
                Emitter_UMV.ID,
                GTUtility.translate("gt.item.emitter.umv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)));
        ItemList.Emitter_UXV.set(
            addItemWithLocalizationKeys(
                Emitter_UXV.ID,
                GTUtility.translate("gt.item.emitter.uxv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)));
        ItemList.Emitter_MAX.set(
            addItemWithLocalizationKeys(
                Emitter_MAX.ID,
                GTUtility.translate("gt.item.emitter.max.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 512L)));

        ItemList.Sensor_LV.set(
            addItemWithLocalizationKeys(
                Sensor_LV.ID,
                GTUtility.translate("gt.item.sensor.lv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L)));

        ItemList.Sensor_MV.set(
            addItemWithLocalizationKeys(
                Sensor_MV.ID,
                GTUtility.translate("gt.item.sensor.mv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L)));
        ItemList.Sensor_HV.set(
            addItemWithLocalizationKeys(
                Sensor_HV.ID,
                GTUtility.translate("gt.item.sensor.hv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4L)));
        ItemList.Sensor_EV.set(
            addItemWithLocalizationKeys(
                Sensor_EV.ID,
                GTUtility.translate("gt.item.sensor.ev.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 8L)));
        ItemList.Sensor_IV.set(
            addItemWithLocalizationKeys(
                Sensor_IV.ID,
                GTUtility.translate("gt.item.sensor.iv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 16L)));
        ItemList.Sensor_LuV.set(
            addItemWithLocalizationKeys(
                Sensor_LuV.ID,
                GTUtility.translate("gt.item.sensor.luv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 32L)));
        ItemList.Sensor_ZPM.set(
            addItemWithLocalizationKeys(
                Sensor_ZPM.ID,
                GTUtility.translate("gt.item.sensor.zpm.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 64L)));
        ItemList.Sensor_UV.set(
            addItemWithLocalizationKeys(
                Sensor_UV.ID,
                GTUtility.translate("gt.item.sensor.uv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 128L)));
        ItemList.Sensor_UHV
            .set(
                addItemWithLocalizationKeys(
                    Sensor_UHV.ID,
                    GTUtility.translate("gt.item.sensor.uhv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Sensor_UEV
            .set(
                addItemWithLocalizationKeys(
                    Sensor_UEV.ID,
                    GTUtility.translate("gt.item.sensor.uev.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Sensor_UIV
            .set(
                addItemWithLocalizationKeys(
                    Sensor_UIV.ID,
                    GTUtility.translate("gt.item.sensor.uiv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Sensor_UMV.set(
            addItemWithLocalizationKeys(
                Sensor_UMV.ID,
                GTUtility.translate("gt.item.sensor.umv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)));
        ItemList.Sensor_UXV.set(
            addItemWithLocalizationKeys(
                Sensor_UXV.ID,
                GTUtility.translate("gt.item.sensor.uxv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)));
        ItemList.Sensor_MAX.set(
            addItemWithLocalizationKeys(
                Sensor_MAX.ID,
                GTUtility.translate("gt.item.sensor.max.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 512L)));

        ItemList.Field_Generator_LV.set(
            addItemWithLocalizationKeys(
                Field_Generator_LV.ID,
                GTUtility.translate("gt.item.field_generator.lv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1L)));
        ItemList.Field_Generator_MV.set(
            addItemWithLocalizationKeys(
                Field_Generator_MV.ID,
                GTUtility.translate("gt.item.field_generator.mv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 2L)));
        ItemList.Field_Generator_HV.set(
            addItemWithLocalizationKeys(
                Field_Generator_HV.ID,
                GTUtility.translate("gt.item.field_generator.hv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 4L)));
        ItemList.Field_Generator_EV.set(
            addItemWithLocalizationKeys(
                Field_Generator_EV.ID,
                GTUtility.translate("gt.item.field_generator.ev.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 8L)));
        ItemList.Field_Generator_IV.set(
            addItemWithLocalizationKeys(
                Field_Generator_IV.ID,
                GTUtility.translate("gt.item.field_generator.iv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 16L)));
        ItemList.Field_Generator_LuV.set(
            addItemWithLocalizationKeys(
                Field_Generator_LuV.ID,
                GTUtility.translate("gt.item.field_generator.luv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 32L)));
        ItemList.Field_Generator_ZPM.set(
            addItemWithLocalizationKeys(
                Field_Generator_ZPM.ID,
                GTUtility.translate("gt.item.field_generator.zpm.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 64L)));
        ItemList.Field_Generator_UV.set(
            addItemWithLocalizationKeys(
                Field_Generator_UV.ID,
                GTUtility.translate("gt.item.field_generator.uv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 128L)));
        ItemList.Field_Generator_UHV
            .set(
                addItemWithLocalizationKeys(
                    Field_Generator_UHV.ID,
                    GTUtility.translate("gt.item.field_generator.uhv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                    new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 256L)))
            .setRender(new CosmicNeutroniumMetaItemRenderer());
        ItemList.Field_Generator_UEV
            .set(
                addItemWithLocalizationKeys(
                    Field_Generator_UEV.ID,
                    GTUtility.translate("gt.item.field_generator.uev.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)))
            .setRender(new InfinityMetaItemRenderer());
        ItemList.Field_Generator_UIV
            .set(
                addItemWithLocalizationKeys(
                    Field_Generator_UIV.ID,
                    GTUtility.translate("gt.item.field_generator.uiv.name"),
                    "",
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                    new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)))
            .setRender(new TranscendentalMetaItemRenderer());
        ItemList.Field_Generator_UMV.set(
            addItemWithLocalizationKeys(
                Field_Generator_UMV.ID,
                GTUtility.translate("gt.item.field_generator.umv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UXV.set(
            addItemWithLocalizationKeys(
                Field_Generator_UXV.ID,
                GTUtility.translate("gt.item.field_generator.uxv.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_MAX.set(
            addItemWithLocalizationKeys(
                Field_Generator_MAX.ID,
                GTUtility.translate("gt.item.field_generator.max.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 512L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 512L)));

        ItemList.StableAdhesive.set(
            addItemWithLocalizationKeys(
                StableAdhesive.ID,
                "gt.item.material.stable_adhesive.name",
                "gt.item.material.stable_adhesive.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 30L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.LIMUS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.VINCULUM, 5L)));
        ItemList.SuperconductorComposite.set(
            addItemWithLocalizationKeys(
                SuperconductorComposite.ID,
                "gt.item.material.superconductor_composite.name",
                "gt.item.material.superconductor_composite.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 50L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 25L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 15L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 10L)));
        ItemList.NaquadriaSupersolid.set(
            addItemWithLocalizationKeys(
                NaquadriaSupersolid.ID,
                "gt.item.material.naquadria_supersolid.name",
                "gt.item.material.naquadria_supersolid.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 100L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 60L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 40L),
                new TCAspects.TC_AspectStack(TCAspects.RADIO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 10L)));

        // Circuits ULV - LuV.
        ItemList.Circuit_Primitive.set(
            addItemWithLocalizationKeys(
                Circuit_Primitive.ID,
                "gt.item.circuit.primitive.name",
                "gt.item.circuit.primitive.tooltip",
                OrePrefixes.circuit.get(Materials.ULV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Basic.set(
            addItemWithLocalizationKeys(
                Circuit_Basic.ID,
                "gt.item.circuit.basic.name",
                "gt.item.circuit.basic.tooltip",
                OrePrefixes.circuit.get(Materials.LV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Good.set(
            addItemWithLocalizationKeys(
                Circuit_Good.ID,
                "gt.item.circuit.good.name",
                "gt.item.circuit.good.tooltip",
                OrePrefixes.circuit.get(Materials.MV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Advanced.set(
            addItemWithLocalizationKeys(
                Circuit_Advanced.ID,
                "gt.item.circuit.advanced.name",
                "gt.item.circuit.advanced.tooltip",
                OrePrefixes.circuit.get(Materials.HV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Data.set(
            addItemWithLocalizationKeys(
                Circuit_Data.ID,
                "gt.item.circuit.data.name",
                "gt.item.circuit.data.tooltip",
                OrePrefixes.circuit.get(Materials.EV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Elite.set(
            addItemWithLocalizationKeys(
                Circuit_Elite.ID,
                "gt.item.circuit.elite.name",
                "gt.item.circuit.elite.tooltip",
                OrePrefixes.circuit.get(Materials.IV),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Master.set(
            addItemWithLocalizationKeys(
                Circuit_Master.ID,
                "gt.item.circuit.master.name",
                "gt.item.circuit.master.tooltip",
                OrePrefixes.circuit.get(Materials.LuV),
                SubTag.NO_UNIFICATION));

        // Backwards compatibility.
        ItemList.Circuit_Parts_Vacuum_Tube.set(ItemList.Circuit_Primitive.get(1));
        ItemList.Circuit_Computer.set(ItemList.Circuit_Advanced.get(1));

        ItemList.Tool_DataOrb.set(
            addItemWithLocalizationKeys(
                Tool_DataOrb.ID,
                "gt.item.tool.data_orb.name",
                "gt.item.tool.data_orb.tooltip",
                SubTag.NO_UNIFICATION,
                new BehaviourDataOrb()));

        ItemList.Tool_DataStick.set(
            addItemWithLocalizationKeys(
                Tool_DataStick.ID,
                "gt.item.tool.data_stick.name",
                "gt.item.tool.data_stick.tooltip",
                SubTag.NO_UNIFICATION,
                new BehaviourDataStick()));

        ItemList.Tool_Cover_Copy_Paste.set(
            addItemWithLocalizationKeys(
                Tool_Cover_Copy_Paste.ID,
                "gt.item.tool.cover_copy_paste.name",
                "gt.item.tool.cover_copy_paste.tooltip",
                BehaviourCoverTool.INSTANCE,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 6L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 6L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 6L)));

        ItemList.Circuit_Board_Basic.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Basic.ID,
                "gt.item.circuit_board.basic.name",
                "gt.item.circuit_board.basic.tooltip"));
        ItemList.Circuit_Board_Coated.set(ItemList.Circuit_Board_Basic.get(1));
        ItemList.Circuit_Board_Advanced.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Advanced.ID,
                "gt.item.circuit_board.advanced.name",
                "gt.item.circuit_board.advanced.tooltip"));
        ItemList.Circuit_Board_Epoxy.set(ItemList.Circuit_Board_Advanced.get(1));
        ItemList.Circuit_Board_Elite.set(
            addItemWithLocalizationKeys(
                Circuit_Board_Elite.ID,
                "gt.item.circuit_board.elite.name",
                "gt.item.circuit_board.elite.tooltip"));
        ItemList.Circuit_Board_Multifiberglass.set(ItemList.Circuit_Board_Elite.get(1));
        ItemList.Circuit_Parts_Crystal_Chip_Elite.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Crystal_Chip_Elite.ID,
                "gt.item.circuit_part.crystal_chip_elite.name",
                "gt.item.circuit_part.crystal_chip_elite.tooltip"));
        ItemList.Circuit_Parts_Crystal_Chip_Master.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Crystal_Chip_Master.ID,
                "gt.item.circuit_part.crystal_chip_master.name",
                "gt.item.circuit_part.crystal_chip_master.tooltip"));
        ItemList.Circuit_Parts_Crystal_Chip_Wetware.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Crystal_Chip_Wetware.ID,
                "gt.item.circuit_part.crystal_chip_wetware.name",
                "gt.item.circuit_part.crystal_chip_wetware.tooltip"));
        ItemList.Circuit_Parts_Advanced.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Advanced.ID,
                "gt.item.circuit_part.diode.name",
                "gt.item.circuit_part.diode.tooltip"));
        ItemList.Circuit_Parts_Diode.set(ItemList.Circuit_Parts_Advanced.get(1));
        ItemList.Circuit_Parts_Wiring_Basic.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Wiring_Basic.ID,
                "gt.item.circuit_part.resistor.name",
                "gt.item.circuit_part.resistor.tooltip"));
        ItemList.Circuit_Parts_Resistor.set(ItemList.Circuit_Parts_Wiring_Basic.get(1));
        ItemList.Circuit_Parts_Wiring_Advanced.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Wiring_Advanced.ID,
                "gt.item.circuit_part.transistor.name",
                "gt.item.circuit_part.transistor.tooltip"));
        ItemList.Circuit_Parts_Transistor.set(ItemList.Circuit_Parts_Wiring_Advanced.get(1));
        ItemList.Circuit_Parts_Wiring_Elite.set(
            addItemWithLocalizationKeys(
                Circuit_Parts_Wiring_Elite.ID,
                "gt.item.circuit_part.capacitor.name",
                "gt.item.circuit_part.capacitor.tooltip"));
        ItemList.Circuit_Parts_Capacitor.set(ItemList.Circuit_Parts_Wiring_Elite.get(1));
        ItemList.Empty_Board_Basic.set(
            addItemWithLocalizationKeys(
                Empty_Board_Basic.ID,
                "gt.item.circuit_board.phenolic.name",
                "gt.item.circuit_board.phenolic.tooltip"));
        ItemList.Circuit_Board_Phenolic.set(ItemList.Empty_Board_Basic.get(1));
        ItemList.Empty_Board_Elite.set(
            addItemWithLocalizationKeys(
                Empty_Board_Elite.ID,
                "gt.item.circuit_board.fiberglass.name",
                "gt.item.circuit_board.fiberglass.tooltip"));
        ItemList.Circuit_Board_Fiberglass.set(ItemList.Empty_Board_Elite.get(1));

        ItemList.Component_Sawblade_Diamond.set(
            addItemWithLocalizationKeys(
                Component_Sawblade_Diamond.ID,
                GTUtility.translate("gt.item.component.sawblade.diamond.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L),
                OreDictNames.craftingDiamondBlade));
        ItemList.Component_Grinder_Diamond.set(
            addItemWithLocalizationKeys(
                Component_Grinder_Diamond.ID,
                GTUtility.translate("gt.item.component.grinder.diamond.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 6L),
                OreDictNames.craftingGrinder));
        ItemList.Component_Grinder_Tungsten.set(
            addItemWithLocalizationKeys(
                Component_Grinder_Tungsten.ID,
                GTUtility.translate("gt.item.component.grinder.tungsten.name"),
                "",
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 6L),
                OreDictNames.craftingGrinder));

        ItemList.Upgrade_Lock.set(
            addItemWithLocalizationKeys(
                Upgrade_Lock.ID,
                "gt.item.upgrade.lock.name",
                "gt.item.upgrade.lock.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 4L)));

        ItemList.Component_Filter.set(
            addItemWithLocalizationKeys(
                Component_Filter.ID,
                GTUtility.translate("gt.item.component.filter.name"),
                "",
                new ItemData(Materials.Zinc, OrePrefixes.foil.getMaterialAmount() * 16L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                OreDictNames.craftingFilter));

        ItemList.Cover_Controller.set(
            addItemWithLocalizationKeys(
                Cover_Controller.ID,
                "gt.item.cover.controller.name",
                "gt.item.cover.controller.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_ActivityDetector.set(
            addItemWithLocalizationKeys(
                Cover_ActivityDetector.ID,
                "gt.item.cover.activity_detector.name",
                "gt.item.cover.activity_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_FluidDetector.set(
            addItemWithLocalizationKeys(
                Cover_FluidDetector.ID,
                "gt.item.cover.fluid_detector.name",
                "gt.item.cover.fluid_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Cover_ItemDetector.set(
            addItemWithLocalizationKeys(
                Cover_ItemDetector.ID,
                "gt.item.cover.item_detector.name",
                "gt.item.cover.item_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 1L)));
        ItemList.Cover_EnergyDetector.set(
            addItemWithLocalizationKeys(
                Cover_EnergyDetector.ID,
                "gt.item.cover.energy_detector.name",
                "gt.item.cover.energy_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));
        ItemList.Cover_PlayerDetector.set(
            addItemWithLocalizationKeys(
                Cover_PlayerDetector.ID,
                "gt.item.cover.player_detector.name",
                "gt.item.cover.player_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_FluidStorageMonitor.set(
            addItemWithLocalizationKeys(
                Cover_FLuidStorageMonitor.ID,
                "gt.item.cover.fluid_storage_monitor.name",
                "gt.item.cover.fluid_storage_monitor.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Cover_Chest_Basic.set(
            addItemWithLocalizationKeys(
                Cover_Chest_Basic.ID,
                "gt.item.cover.chest.basic.name",
                "gt.item.cover.chest.basic.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));
        ItemList.Cover_Chest_Good.set(
            addItemWithLocalizationKeys(
                Cover_Chest_Good.ID,
                "gt.item.cover.chest.good.name",
                "gt.item.cover.chest.good.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));
        ItemList.Cover_Chest_Advanced.set(
            addItemWithLocalizationKeys(
                Cover_Chest_Advanced.ID,
                "gt.item.cover.chest.advanced.name",
                "gt.item.cover.chest.advanced.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));

        for (int i = 1; i < 15; i++) {
            ItemList.WIRELESS_ENERGY_COVERS[i - 1].set(
                addItem(
                    Cover_Wireless_Energy_LV.ID + i - 1,
                    GTUtility.translate("gt.item.wireless_energy_cover.name", GTValues.VN[i]),
                    String.join(
                        "\\n",
                        GTUtility.translate("gt.item.wireless_energy_cover.tooltip"),
                        GTUtility.translate("gt.tileentity.amperage", EnumChatFormatting.YELLOW + "2"),
                        GTUtility.translate(
                            "gt.tileentity.eup_in",
                            EnumChatFormatting.GREEN + formatNumber(GTValues.V[i])
                                + " ("
                                + GTUtility.getColoredTierNameFromTier((byte) i)
                                + EnumChatFormatting.GREEN
                                + ")")),
                    new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L)));
        }
        ItemList.Cover_Wireless_Energy_Debug.set(
            addItemWithLocalizationKeys(
                Cover_Wireless_Energy_Debug.ID,
                GTUtility.translate("gt.item.wireless_energy_cover_debug.name"),
                GTUtility.translate("gt.item.wireless_energy_cover_debug.tooltip"),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 9999L)));

        ItemList.Cover_Screen.set(
            addItemWithLocalizationKeys(
                Cover_Screen.ID,
                "gt.item.cover.screen.name",
                "gt.item.cover.screen.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.LUX, 2L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L)));
        ItemList.Cover_Crafting.set(
            addItemWithLocalizationKeys(
                Cover_Crafting.ID,
                "gt.item.cover.crafting.name",
                "gt.item.cover.crafting.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 4L)));
        ItemList.Cover_Drain.set(
            addItemWithLocalizationKeys(
                Cover_Drain.ID,
                "gt.item.cover.drain.name",
                "gt.item.cover.drain.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));

        ItemList.Cover_Shutter.set(
            addItemWithLocalizationKeys(
                Cover_Shutter.ID,
                "gt.item.cover.shutter.name",
                "gt.item.cover.shutter.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L)));

        ItemList.Cover_SolarPanel.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel.ID,
                "gt.item.cover.solar_panel.basic.name",
                "gt.item.cover.solar_panel.basic.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 1L)));
        ItemList.Cover_SolarPanel_8V.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_8V.ID,
                "gt.item.cover.solar_panel.8v.name",
                "gt.item.cover.solar_panel.8v.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 2L)));
        ItemList.Cover_SolarPanel_LV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_LV.ID,
                "gt.item.cover.solar_panel.lv.name",
                "gt.item.cover.solar_panel.lv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 4L)));
        ItemList.Cover_SolarPanel_MV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_MV.ID,
                "gt.item.cover.solar_panel.mv.name",
                "gt.item.cover.solar_panel.mv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 8L)));
        ItemList.Cover_SolarPanel_HV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_HV.ID,
                "gt.item.cover.solar_panel.hv.name",
                "gt.item.cover.solar_panel.hv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 16L)));
        ItemList.Cover_SolarPanel_EV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_EV.ID,
                "gt.item.cover.solar_panel.ev.name",
                "gt.item.cover.solar_panel.ev.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 32L)));
        ItemList.Cover_SolarPanel_IV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_IV.ID,
                "gt.item.cover.solar_panel.iv.name",
                "gt.item.cover.solar_panel.iv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_LuV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_LuV.ID,
                "gt.item.cover.solar_panel.luv.name",
                "gt.item.cover.solar_panel.luv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_ZPM.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_ZPM.ID,
                "gt.item.cover.solar_panel.zpm.name",
                "gt.item.cover.solar_panel.zpm.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_UV.set(
            addItemWithLocalizationKeys(
                Cover_SolarPanel_UV.ID,
                "gt.item.cover.solar_panel.uv.name",
                "gt.item.cover.solar_panel.uv.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 64L)));

        ItemList.Tool_Cheat.set(
            addItemWithLocalizationKeys(
                Tool_Cheat.ID,
                "gt.item.tool.debug_scanner.name",
                "gt.item.tool.debug_scanner.tooltip",
                BehaviourScanner.INSTANCE,
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 64L)));
        ItemList.Tool_Scanner.set(
            addItemWithLocalizationKeys(
                Tool_Scanner.ID,
                "gt.item.tool.portable_scanner.name",
                "gt.item.tool.portable_scanner.tooltip",
                BehaviourScanner.INSTANCE,
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 6L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 6L)));

        ItemList.NC_SensorKit.set(
            addItemWithLocalizationKeys(
                NC_SensorKit.ID,
                GTUtility.translate("gt.item.tool.sensor_kit.name"),
                "",
                new BehaviourSensorKit()));
        ItemList.Duct_Tape.set(
            addItemWithLocalizationKeys(
                Duct_Tape.ID,
                "gt.item.tool.duct_tape.name",
                "gt.item.tool.duct_tape.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                OreDictNames.craftingDuctTape));
        ItemList.McGuffium_239.set(
            addItemWithLocalizationKeys(
                McGuffium_239.ID,
                "gt.item.mcguffium_239.name",
                "gt.item.mcguffium_239.tooltip",
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
            addItemWithLocalizationKeys(
                Cover_RedstoneTransmitter.ID,
                "gt.item.cover.redstone_transmitter.name",
                "gt.item.cover.redstone_transmitter.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneTransmitterInternal.set(
            addItemWithLocalizationKeys(
                Cover_RedstoneTransmitterInternal.ID,
                "gt.item.cover.redstone_transmitter_internal.name",
                "gt.item.cover.redstone_transmitter_internal.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneReceiver.set(
            addItemWithLocalizationKeys(
                Cover_RedstoneReceiver.ID,
                "gt.item.cover.redstone_receiver.name",
                "gt.item.cover.redstone_receiver.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));
        ItemList.Cover_WirelessController.set(
            addItemWithLocalizationKeys(
                Cover_WirelessController.ID,
                "gt.item.cover.wireless_controller.name",
                "gt.item.cover.wireless_controller.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));

        ItemList.Cover_NeedsMaintainance.set(
            addItemWithLocalizationKeys(
                Cover_NeedsMaintenance.ID,
                "gt.item.cover.needs_maintenance.name",
                "gt.item.cover.needs_maintenance.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L)));

        ItemList.Steam_Regulator_LV.set(
            addItemWithLocalizationKeys(
                Steam_Regulator_LV.ID,
                GTUtility.translate("gt.item.steam_regulator.lv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(1024), formatNumber(1024 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Steam_Regulator_MV.set(
            addItemWithLocalizationKeys(
                Steam_Regulator_MV.ID,
                GTUtility.translate("gt.item.steam_regulator.mv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(2048), formatNumber(2048 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.Steam_Regulator_HV.set(
            addItemWithLocalizationKeys(
                Steam_Regulator_HV.ID,
                GTUtility.translate("gt.item.steam_regulator.hv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(4096), formatNumber(4096 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)));
        ItemList.Steam_Regulator_EV.set(
            addItemWithLocalizationKeys(
                Steam_Regulator_EV.ID,
                GTUtility.translate("gt.item.steam_regulator.ev.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(8192), formatNumber(8192 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 8L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 8L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 8L)));
        ItemList.Steam_Regulator_IV.set(
            addItemWithLocalizationKeys(
                Steam_Regulator_IV.ID,
                GTUtility.translate("gt.item.steam_regulator.iv.name"),
                GTUtility.translate("gt.item.steam_valve.tooltip", formatNumber(16384), formatNumber(16384 * 20)),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 16L),
                new TCAspects.TC_AspectStack(TCAspects.ITER, 16L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 16L)));
        ItemList.Electromagnet_Iron.set(
            addItemWithLocalizationKeys(
                Electromagnet_Iron.ID,
                GTUtility.translate("gt.item.electromagnet.iron.name"),
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Iron),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 8)));
        ItemList.Electromagnet_Steel.set(
            addItemWithLocalizationKeys(
                Electromagnet_Steel.ID,
                GTUtility.translate("gt.item.electromagnet.steel.name"),
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Steel),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 16)));
        ItemList.Electromagnet_Neodymium.set(
            addItemWithLocalizationKeys(
                Electromagnet_Neodymium.ID,
                GTUtility.translate("gt.item.electromagnet.neodymium.name"),
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Neodymium),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 24)));
        ItemList.Electromagnet_Samarium.set(
            addItemWithLocalizationKeys(
                Electromagnet_Samarium.ID,
                EnumChatFormatting.YELLOW + GTUtility.translate("gt.item.electromagnet.samarium.name"),
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Samarium),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 32)));
        ItemList.Electromagnet_Tengam.set(
            addItemWithLocalizationKeys(
                Electromagnet_Tengam.ID,
                EnumChatFormatting.GREEN + GTUtility.translate("gt.item.electromagnet.tengam.name"),
                MagnetTiers.buildMagnetTooltip(MagnetTiers.Tengam),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 40)));

        ItemList.Black_Hole_Opener.set(
            addItemWithLocalizationKeys(
                Black_Hole_Opener.ID,
                "gt.item.black_hole.seed.name",
                "gt.item.black_hole.seed.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 32),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 64)));
        ItemList.Black_Hole_Closer.set(
            addItemWithLocalizationKeys(
                Black_Hole_Closer.ID,
                "gt.item.black_hole.collapser.name",
                "gt.item.black_hole.collapser.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 32),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 64)));
        ItemList.Black_Hole_Stabilizer.set(
            addItemWithLocalizationKeys(
                Black_Hole_Stabilizer.ID,
                "gt.item.black_hole.stabilizer.name",
                "gt.item.black_hole.stabilizer.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 32),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 128)));

        // Empty battery hulls
        ItemList.BatteryHull_EV.set(
            addItemWithLocalizationKeys(
                BatteryHull_EV.ID,
                "gt.item.battery_hull.ev.name",
                "gt.item.battery_hull.ev.tooltip",
                new ItemData(Materials.BlueSteel, OrePrefixes.plate.getMaterialAmount() * 2L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 8L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 8L)));
        ItemList.BatteryHull_IV.set(
            addItemWithLocalizationKeys(
                BatteryHull_IV.ID,
                "gt.item.battery_hull.iv.name",
                "gt.item.battery_hull.iv.tooltip",
                new ItemData(Materials.RoseGold, OrePrefixes.plate.getMaterialAmount() * 6L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 16L)));
        ItemList.BatteryHull_LuV.set(
            addItemWithLocalizationKeys(
                BatteryHull_LuV.ID,
                "gt.item.battery_hull.luv.name",
                "gt.item.battery_hull.luv.tooltip",
                new ItemData(Materials.RedSteel, OrePrefixes.plate.getMaterialAmount() * 18L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 32L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 32L)));
        ItemList.BatteryHull_ZPM.set(
            addItemWithLocalizationKeys(
                BatteryHull_ZPM.ID,
                "gt.item.battery_hull.zpm.name",
                "gt.item.battery_hull.zpm.tooltip",
                new ItemData(Materials.Europium, OrePrefixes.plate.getMaterialAmount() * 6L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 64L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 64L)));
        ItemList.BatteryHull_UV.set(
            addItemWithLocalizationKeys(
                BatteryHull_UV.ID,
                "gt.item.battery_hull.uv.name",
                "gt.item.battery_hull.uv.tooltip",
                new ItemData(Materials.Americium, OrePrefixes.plate.getMaterialAmount() * 18L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 128L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 128L)));
        ItemList.BatteryHull_UHV.set(
            addItemWithLocalizationKeys(
                BatteryHull_UHV.ID,
                "gt.item.battery_hull.uhv.name",
                "gt.item.battery_hull.uhv.tooltip",
                new ItemData(Materials.Naquadah, OrePrefixes.plate.getMaterialAmount() * 24L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 256L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 256L)));
        ItemList.BatteryHull_UEV.set(
            addItemWithLocalizationKeys(
                BatteryHull_UEV.ID,
                "gt.item.battery_hull.uev.name",
                "gt.item.battery_hull.uev.tooltip",
                new ItemData(Materials.NaquadahEnriched, OrePrefixes.plate.getMaterialAmount() * 36L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 512L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 512L)));
        ItemList.BatteryHull_UIV.set(
            addItemWithLocalizationKeys(
                BatteryHull_UIV.ID,
                "gt.item.battery_hull.uiv.name",
                "gt.item.battery_hull.uiv.tooltip",
                new ItemData(Materials.NaquadahAlloy, OrePrefixes.plate.getMaterialAmount() * 48L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1024L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 1024L)));
        ItemList.BatteryHull_UMV.set(
            addItemWithLocalizationKeys(
                BatteryHull_UMV.ID,
                "gt.item.battery_hull.umv.name",
                "gt.item.battery_hull.umv.tooltip",
                new ItemData(Materials.Neutronium, OrePrefixes.plate.getMaterialAmount() * 56L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2048L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2048L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2048L)));
        ItemList.BatteryHull_UxV.set(
            addItemWithLocalizationKeys(
                BatteryHull_UxV.ID,
                "gt.item.battery_hull.uxv.name",
                "gt.item.battery_hull.uxv.tooltip",
                new ItemData(Materials.DraconiumAwakened, OrePrefixes.plate.getMaterialAmount() * 64L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4096L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4096L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 4096L)));

        // Full batteries
        ItemList.BatteryHull_EV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_EV_Full.ID,
                "gt.item.battery.ev.name",
                "gt.item.battery.re.tooltip",
                "batteryEV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_IV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_IV_Full.ID,
                "gt.item.battery.iv.name",
                "gt.item.battery.re.tooltip",
                "batteryIV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_LuV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_LuV_Full.ID,
                "gt.item.battery.luv.name",
                "gt.item.battery.re.tooltip",
                "batteryLuV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_ZPM_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_ZPM_Full.ID,
                "gt.item.battery.zpm.name",
                "gt.item.battery.re.tooltip",
                "batteryZPM",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_UV_Full.ID,
                "gt.item.battery.uv.name",
                "gt.item.battery.re.tooltip",
                "batteryUV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UHV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_UHV_Full.ID,
                "gt.item.battery.uhv.name",
                "gt.item.battery.re.tooltip",
                "batteryUHV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UEV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_UEV_Full.ID,
                "gt.item.battery.uev.name",
                "gt.item.battery.re.tooltip",
                "batteryUEV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UIV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_UIV_Full.ID,
                "gt.item.battery.uiv.name",
                "gt.item.battery.re.tooltip",
                "batteryUIV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UMV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_UMV_Full.ID,
                "gt.item.battery.umv.name",
                "gt.item.battery.re.tooltip",
                "batteryUMV",
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 16L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L)));

        ItemList.BatteryHull_UxV_Full.set(
            addItemWithLocalizationKeys(
                BatteryHull_UxV_Full.ID,
                "gt.item.battery.uxv.name",
                "gt.item.battery.re.tooltip",
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
                    aList.add(GTUtility.translate("GT5U.tooltip.purify.1"));
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
        tStack.setStackDisplayName(GTUtility.translate("gt.item.sengir_planks.name"));
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
