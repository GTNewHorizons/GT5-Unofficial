package gregtech.common.items;

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
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.client.GT_TooltipHandler.Tier.ERV;
import static gregtech.client.GT_TooltipHandler.Tier.EV;
import static gregtech.client.GT_TooltipHandler.Tier.HV;
import static gregtech.client.GT_TooltipHandler.Tier.IV;
import static gregtech.client.GT_TooltipHandler.Tier.LV;
import static gregtech.client.GT_TooltipHandler.Tier.LuV;
import static gregtech.client.GT_TooltipHandler.Tier.MAX;
import static gregtech.client.GT_TooltipHandler.Tier.MV;
import static gregtech.client.GT_TooltipHandler.Tier.ULV;
import static gregtech.client.GT_TooltipHandler.Tier.UMV;
import static gregtech.client.GT_TooltipHandler.Tier.UV;
import static gregtech.client.GT_TooltipHandler.Tier.UXV;
import static gregtech.client.GT_TooltipHandler.Tier.ZPM;
import static gregtech.client.GT_TooltipHandler.registerTieredTooltip;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Empty;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Holy_Water;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Poison;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Poison_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Poison_Strong;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Slowness;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Slowness_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Weakness;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Head_Glass_Weakness_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Empty;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Holy_Water;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Poison;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Poison_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Poison_Strong;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Slowness;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Slowness_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Weakness;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Plastic_Glass_Weakness_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Empty;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Holy_Water;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Poison;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Poison_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Poison_Strong;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Slowness;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Slowness_Long;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Weakness;
import static gregtech.common.items.ID_MetaItem_01.Arrow_Wooden_Glass_Weakness_Long;
import static gregtech.common.items.ID_MetaItem_01.Battery_Hull_HV;
import static gregtech.common.items.ID_MetaItem_01.Battery_Hull_LV;
import static gregtech.common.items.ID_MetaItem_01.Battery_Hull_MV;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_HV_Cadmium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_HV_Lithium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_HV_Sodium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_LV_Cadmium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_LV_Lithium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_LV_Sodium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_MV_Cadmium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_MV_Lithium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_MV_Sodium;
import static gregtech.common.items.ID_MetaItem_01.Battery_RE_ULV_Tantalum;
import static gregtech.common.items.ID_MetaItem_01.Battery_SU_HV_Mercury;
import static gregtech.common.items.ID_MetaItem_01.Battery_SU_HV_Sulfuric_Acid;
import static gregtech.common.items.ID_MetaItem_01.Battery_SU_LV_Mercury;
import static gregtech.common.items.ID_MetaItem_01.Battery_SU_LV_Sulfuric_Acid;
import static gregtech.common.items.ID_MetaItem_01.Battery_SU_MV_Mercury;
import static gregtech.common.items.ID_MetaItem_01.Battery_SU_MV_Sulfuric_Acid;
import static gregtech.common.items.ID_MetaItem_01.Book_Written_01;
import static gregtech.common.items.ID_MetaItem_01.Book_Written_02;
import static gregtech.common.items.ID_MetaItem_01.Book_Written_03;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Advanced;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Basic;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Board_Advanced;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Board_Basic;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Board_Elite;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Data;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Elite;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Good;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Master;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Parts_Advanced;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Parts_Crystal_Chip_Elite;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Parts_Crystal_Chip_Master;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Parts_Crystal_Chip_Wetware;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Parts_Wiring_Advanced;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Parts_Wiring_Basic;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Parts_Wiring_Elite;
import static gregtech.common.items.ID_MetaItem_01.Circuit_Primitive;
import static gregtech.common.items.ID_MetaItem_01.Component_Filter;
import static gregtech.common.items.ID_MetaItem_01.Component_Grinder_Diamond;
import static gregtech.common.items.ID_MetaItem_01.Component_Grinder_Tungsten;
import static gregtech.common.items.ID_MetaItem_01.Component_Minecraft_Wheels_Iron;
import static gregtech.common.items.ID_MetaItem_01.Component_Minecraft_Wheels_Steel;
import static gregtech.common.items.ID_MetaItem_01.Component_Sawblade_Diamond;
import static gregtech.common.items.ID_MetaItem_01.Compressed_Fireclay;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_EV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_HV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_IV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_LV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_LuV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_MAX;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_MV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_UEV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_UHV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_UIV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_UMV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_UV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_UXV;
import static gregtech.common.items.ID_MetaItem_01.Conveyor_Module_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Cover_ActivityDetector;
import static gregtech.common.items.ID_MetaItem_01.Cover_Controller;
import static gregtech.common.items.ID_MetaItem_01.Cover_Crafting;
import static gregtech.common.items.ID_MetaItem_01.Cover_Drain;
import static gregtech.common.items.ID_MetaItem_01.Cover_EnergyDetector;
import static gregtech.common.items.ID_MetaItem_01.Cover_FLuidStorageMonitor;
import static gregtech.common.items.ID_MetaItem_01.Cover_FluidDetector;
import static gregtech.common.items.ID_MetaItem_01.Cover_FluidLimiter;
import static gregtech.common.items.ID_MetaItem_01.Cover_ItemDetector;
import static gregtech.common.items.ID_MetaItem_01.Cover_NeedsMaintenance;
import static gregtech.common.items.ID_MetaItem_01.Cover_PlayerDetector;
import static gregtech.common.items.ID_MetaItem_01.Cover_RedstoneReceiverExternal;
import static gregtech.common.items.ID_MetaItem_01.Cover_RedstoneReceiverInternal;
import static gregtech.common.items.ID_MetaItem_01.Cover_RedstoneTransmitterExternal;
import static gregtech.common.items.ID_MetaItem_01.Cover_RedstoneTransmitterInternal;
import static gregtech.common.items.ID_MetaItem_01.Cover_Screen;
import static gregtech.common.items.ID_MetaItem_01.Cover_Shutter;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_8V;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_EV;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_HV;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_IV;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_LV;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_LuV;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_MV;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_UV;
import static gregtech.common.items.ID_MetaItem_01.Cover_SolarPanel_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Crate_Empty;
import static gregtech.common.items.ID_MetaItem_01.Duct_Tape;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_EV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_HV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_IV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_LV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_LuV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_MAX;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_MV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_UEV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_UHV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_UIV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_UMV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_UV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_UXV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Motor_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_EV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_HV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_IV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_LV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_LuV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_MAX;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_MV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_UEV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_UHV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_UIV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_UMV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_UV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_UXV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Piston_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_EV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_HV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_IV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_LV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_LuV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_MAX;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_MV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_UEV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_UHV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_UIV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_UMV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_UV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_UXV;
import static gregtech.common.items.ID_MetaItem_01.Electric_Pump_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Emitter_EV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_HV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_IV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_LV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_LuV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_MAX;
import static gregtech.common.items.ID_MetaItem_01.Emitter_MV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_UEV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_UHV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_UIV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_UMV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_UV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_UXV;
import static gregtech.common.items.ID_MetaItem_01.Emitter_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Empty_Board_Basic;
import static gregtech.common.items.ID_MetaItem_01.Empty_Board_Elite;
import static gregtech.common.items.ID_MetaItem_01.EnergisedTesseract;
import static gregtech.common.items.ID_MetaItem_01.Energy_Cluster;
import static gregtech.common.items.ID_MetaItem_01.Energy_Lapotronic_Orb;
import static gregtech.common.items.ID_MetaItem_01.Energy_Lapotronic_orb_2;
import static gregtech.common.items.ID_MetaItem_01.Energy_Module;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_EV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_HV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_IV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_LV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_LuV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_MAX;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_MV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_UEV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_UHV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_UIV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_UMV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_UV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_UXV;
import static gregtech.common.items.ID_MetaItem_01.Field_Generator_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Firebrick;
import static gregtech.common.items.ID_MetaItem_01.FluidFilter;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_EV;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_HV;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_IV;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_LV;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_LuV;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_MV;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_UV;
import static gregtech.common.items.ID_MetaItem_01.FluidRegulator_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Fuel_Can_Plastic_Empty;
import static gregtech.common.items.ID_MetaItem_01.Fuel_Can_Plastic_Filled;
import static gregtech.common.items.ID_MetaItem_01.GigaChad;
import static gregtech.common.items.ID_MetaItem_01.Gravistar;
import static gregtech.common.items.ID_MetaItem_01.Ingot_Heavy1;
import static gregtech.common.items.ID_MetaItem_01.Ingot_Heavy2;
import static gregtech.common.items.ID_MetaItem_01.Ingot_Heavy3;
import static gregtech.common.items.ID_MetaItem_01.Ingot_Iridium_Alloy;
import static gregtech.common.items.ID_MetaItem_01.ItemFilter_Export;
import static gregtech.common.items.ID_MetaItem_01.ItemFilter_Import;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_Aluminium;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_Chrome;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_Iridium;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_Neutronium;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_Osmium;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_StainlessSteel;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_Steel;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_Titanium;
import static gregtech.common.items.ID_MetaItem_01.Large_Fluid_Cell_TungstenSteel;
import static gregtech.common.items.ID_MetaItem_01.McGuffium_239;
import static gregtech.common.items.ID_MetaItem_01.NC_SensorKit;
import static gregtech.common.items.ID_MetaItem_01.NaquadriaSupersolid;
import static gregtech.common.items.ID_MetaItem_01.Paper_Magic_Empty;
import static gregtech.common.items.ID_MetaItem_01.Paper_Magic_Page;
import static gregtech.common.items.ID_MetaItem_01.Paper_Magic_Pages;
import static gregtech.common.items.ID_MetaItem_01.Paper_Printed_Pages;
import static gregtech.common.items.ID_MetaItem_01.Paper_Punch_Card_Empty;
import static gregtech.common.items.ID_MetaItem_01.Paper_Punch_Card_Encoded;
import static gregtech.common.items.ID_MetaItem_01.QuantumEye;
import static gregtech.common.items.ID_MetaItem_01.QuantumStar;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_EV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_HV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_IV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_LV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_LuV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_MAX;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_MV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_UEV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_UHV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_UIV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_UMV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_UV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_UXV;
import static gregtech.common.items.ID_MetaItem_01.Robot_Arm_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Schematic;
import static gregtech.common.items.ID_MetaItem_01.Schematic_1by1;
import static gregtech.common.items.ID_MetaItem_01.Schematic_2by2;
import static gregtech.common.items.ID_MetaItem_01.Schematic_3by3;
import static gregtech.common.items.ID_MetaItem_01.Schematic_Crafting;
import static gregtech.common.items.ID_MetaItem_01.Schematic_Dust;
import static gregtech.common.items.ID_MetaItem_01.Sensor_EV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_HV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_IV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_LV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_LuV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_MAX;
import static gregtech.common.items.ID_MetaItem_01.Sensor_MV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_UEV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_UHV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_UIV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_UMV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_UV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_UXV;
import static gregtech.common.items.ID_MetaItem_01.Sensor_ZPM;
import static gregtech.common.items.ID_MetaItem_01.Shape_Empty;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Axe;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Block;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Bolt;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Bottle;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Casing;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Cell;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_File;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Gear;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Hammer;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Hoe;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Ingot;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Pickaxe;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Pipe_Huge;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Pipe_Large;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Pipe_Medium;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Pipe_Small;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Pipe_Tiny;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Plate;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Ring;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Rod;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Rotor;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Saw;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Shovel;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Small_Gear;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Sword;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Tool_Head_Drill;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Turbine_Blade;
import static gregtech.common.items.ID_MetaItem_01.Shape_Extruder_Wire;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Anvil;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Arrow;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Baguette;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Ball;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Block;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Bolt;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Bottle;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Bread;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Bun;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Casing;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Credit;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Cylinder;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Gear;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Gear_Small;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Ingot;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Name;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Nugget;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Pipe_Huge;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Pipe_Large;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Pipe_Medium;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Pipe_Small;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Pipe_Tiny;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Plate;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Ring;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Rod;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Rod_Long;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Rotor;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Round;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Screw;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Tool_Head_Drill;
import static gregtech.common.items.ID_MetaItem_01.Shape_Mold_Turbine_Blade;
import static gregtech.common.items.ID_MetaItem_01.Shape_Slicer_Flat;
import static gregtech.common.items.ID_MetaItem_01.Shape_Slicer_Stripes;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_0;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_1;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_10;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_11;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_12;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_13;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_14;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_15;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_2;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_3;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_4;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_5;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_6;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_7;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_8;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_9;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Remover;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Remover_Empty;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_0;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_1;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_10;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_11;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_12;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_13;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_14;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_15;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_2;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_3;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_4;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_5;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_6;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_7;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_8;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_9;
import static gregtech.common.items.ID_MetaItem_01.Spray_Color_Used_Remover;
import static gregtech.common.items.ID_MetaItem_01.Spray_Empty;
import static gregtech.common.items.ID_MetaItem_01.StableAdhesive;
import static gregtech.common.items.ID_MetaItem_01.Steam_Regulator_EV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Regulator_HV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Regulator_IV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Regulator_LV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Regulator_MV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Valve_EV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Valve_HV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Valve_IV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Valve_LV;
import static gregtech.common.items.ID_MetaItem_01.Steam_Valve_MV;
import static gregtech.common.items.ID_MetaItem_01.SuperconductorComposite;
import static gregtech.common.items.ID_MetaItem_01.Tesseract;
import static gregtech.common.items.ID_MetaItem_01.Thermos_Can_Empty;
import static gregtech.common.items.ID_MetaItem_01.Tool_Cheat;
import static gregtech.common.items.ID_MetaItem_01.Tool_Cover_Copy_Paste;
import static gregtech.common.items.ID_MetaItem_01.Tool_DataOrb;
import static gregtech.common.items.ID_MetaItem_01.Tool_DataStick;
import static gregtech.common.items.ID_MetaItem_01.Tool_Lighter_Invar_Empty;
import static gregtech.common.items.ID_MetaItem_01.Tool_Lighter_Invar_Full;
import static gregtech.common.items.ID_MetaItem_01.Tool_Lighter_Invar_Used;
import static gregtech.common.items.ID_MetaItem_01.Tool_Lighter_Platinum_Empty;
import static gregtech.common.items.ID_MetaItem_01.Tool_Lighter_Platinum_Full;
import static gregtech.common.items.ID_MetaItem_01.Tool_Lighter_Platinum_Used;
import static gregtech.common.items.ID_MetaItem_01.Tool_MatchBox_Full;
import static gregtech.common.items.ID_MetaItem_01.Tool_MatchBox_Used;
import static gregtech.common.items.ID_MetaItem_01.Tool_Matches;
import static gregtech.common.items.ID_MetaItem_01.Tool_Scanner;
import static gregtech.common.items.ID_MetaItem_01.Tool_Sonictron;
import static gregtech.common.items.ID_MetaItem_01.Upgrade_Lock;
import static gregtech.common.items.ID_MetaItem_01.Upgrade_Muffler;
import static gregtech.common.items.ID_MetaItem_01.ZPM2;
import static gregtech.common.items.ID_MetaItem_01.ZPM3;
import static gregtech.common.items.ID_MetaItem_01.ZPM4;
import static gregtech.common.items.ID_MetaItem_01.ZPM5;
import static gregtech.common.items.ID_MetaItem_01.ZPM6;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Item_X32;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_FoodStat;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.GT_Cover_Arm;
import gregtech.common.covers.GT_Cover_ControlsWork;
import gregtech.common.covers.GT_Cover_Conveyor;
import gregtech.common.covers.GT_Cover_Crafting;
import gregtech.common.covers.GT_Cover_DoesWork;
import gregtech.common.covers.GT_Cover_Drain;
import gregtech.common.covers.GT_Cover_EUMeter;
import gregtech.common.covers.GT_Cover_FluidLimiter;
import gregtech.common.covers.GT_Cover_FluidRegulator;
import gregtech.common.covers.GT_Cover_FluidStorageMonitor;
import gregtech.common.covers.GT_Cover_Fluidfilter;
import gregtech.common.covers.GT_Cover_ItemFilter;
import gregtech.common.covers.GT_Cover_ItemMeter;
import gregtech.common.covers.GT_Cover_LiquidMeter;
import gregtech.common.covers.GT_Cover_NeedMaintainance;
import gregtech.common.covers.GT_Cover_PlayerDetector;
import gregtech.common.covers.GT_Cover_Pump;
import gregtech.common.covers.GT_Cover_RedstoneReceiverExternal;
import gregtech.common.covers.GT_Cover_RedstoneReceiverInternal;
import gregtech.common.covers.GT_Cover_RedstoneTransmitterExternal;
import gregtech.common.covers.GT_Cover_RedstoneTransmitterInternal;
import gregtech.common.covers.GT_Cover_Screen;
import gregtech.common.covers.GT_Cover_Shutter;
import gregtech.common.covers.GT_Cover_SolarPanel;
import gregtech.common.covers.GT_Cover_SteamRegulator;
import gregtech.common.covers.GT_Cover_SteamValve;
import gregtech.common.items.behaviors.Behaviour_Arrow_Potion;
import gregtech.common.items.behaviors.Behaviour_Cover_Tool;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gregtech.common.items.behaviors.Behaviour_DataStick;
import gregtech.common.items.behaviors.Behaviour_Lighter;
import gregtech.common.items.behaviors.Behaviour_PrintedPages;
import gregtech.common.items.behaviors.Behaviour_Scanner;
import gregtech.common.items.behaviors.Behaviour_SensorKit;
import gregtech.common.items.behaviors.Behaviour_Sonictron;
import gregtech.common.items.behaviors.Behaviour_Spray_Color;
import gregtech.common.items.behaviors.Behaviour_Spray_Color_Remover;
import gregtech.common.items.behaviors.Behaviour_WrittenBook;

enum ID_MetaItem_01 {
    // Please pretty please, add your entries while conserving the order

    Credit_Greg_Copper(0),
    Credit_Greg_Cupronickel(1),
    Credit_Greg_Silver(2),
    Credit_Greg_Gold(3),
    Credit_Greg_Platinum(4),
    Credit_Greg_Osmium(5),
    Credit_Greg_Naquadah(6),
    Credit_Greg_Neutronium(7),
    Coin_Gold_Ancient(8),
    Coin_Doge(9),
    Coin_Chocolate(10),
    Credit_Copper(11),
    Credit_Silver(13),
    Credit_Gold(14),
    Credit_Platinum(15),
    Credit_Osmium(16),
    Electric_Motor_UIV(17),
    Electric_Motor_UMV(18),
    Electric_Motor_UXV(19),
    Electric_Motor_MAX(20),
    Electric_Piston_UIV(21),
    Electric_Piston_UMV(22),
    Electric_Piston_UXV(23),
    Electric_Piston_MAX(24),
    Electric_Pump_UIV(25),
    Electric_Pump_UMV(26),
    Electric_Pump_UXV(27),
    Electric_Pump_MAX(28),
    Conveyor_Module_UIV(29),
    Conveyor_Module_UMV(30),
    Conveyor_Module_UXV(31),
    Conveyor_Module_MAX(32),
    Robot_Arm_UIV(33),
    Robot_Arm_UMV(34),
    Robot_Arm_UXV(35),
    Robot_Arm_MAX(36),
    Emitter_UIV(37),
    Emitter_UMV(38),
    Emitter_UXV(39),
    Emitter_MAX(40),
    Sensor_UIV(41),
    Sensor_UMV(42),
    Sensor_UXV(43),
    Sensor_MAX(44),
    Field_Generator_UIV(45),
    Field_Generator_UMV(46),
    Field_Generator_UXV(47),
    Field_Generator_MAX(48),
    Component_Minecraft_Wheels_Iron(100),
    Component_Minecraft_Wheels_Steel(101),
    Compressed_Fireclay(110),
    Firebrick(111),
    ZPM5(145),
    ZPM6(146),
    Arrow_Head_Glass_Empty(200),
    Arrow_Head_Glass_Poison(201),
    Arrow_Head_Glass_Poison_Long(202),
    Arrow_Head_Glass_Poison_Strong(203),
    Arrow_Head_Glass_Slowness(204),
    Arrow_Head_Glass_Slowness_Long(205),
    Arrow_Head_Glass_Weakness(206),
    Arrow_Head_Glass_Weakness_Long(207),
    Arrow_Head_Glass_Holy_Water(208),
    Arrow_Wooden_Glass_Empty(225),
    Arrow_Wooden_Glass_Poison(226),
    Arrow_Wooden_Glass_Poison_Long(227),
    Arrow_Wooden_Glass_Poison_Strong(228),
    Arrow_Wooden_Glass_Slowness(229),
    Arrow_Wooden_Glass_Slowness_Long(230),
    Arrow_Wooden_Glass_Weakness(231),
    Arrow_Wooden_Glass_Weakness_Long(232),
    Arrow_Wooden_Glass_Holy_Water(233),
    Arrow_Plastic_Glass_Empty(250),
    Arrow_Plastic_Glass_Poison(251),
    Arrow_Plastic_Glass_Poison_Long(252),
    Arrow_Plastic_Glass_Poison_Strong(253),
    Arrow_Plastic_Glass_Slowness(254),
    Arrow_Plastic_Glass_Slowness_Long(255),
    Arrow_Plastic_Glass_Weakness(256),
    Arrow_Plastic_Glass_Weakness_Long(257),
    Arrow_Plastic_Glass_Holy_Water(258),
    ItemFilter_Export(270),
    ItemFilter_Import(271),
    Cover_FluidLimiter(272),
    Shape_Empty(300),
    Shape_Mold_Plate(301),
    Shape_Mold_Casing(302),
    Shape_Mold_Gear(303),
    Shape_Mold_Credit(304),
    Shape_Mold_Bottle(305),
    Shape_Mold_Ingot(306),
    Shape_Mold_Ball(307),
    Shape_Mold_Block(308),
    Shape_Mold_Nugget(309),
    Shape_Mold_Bun(310),
    Shape_Mold_Bread(311),
    Shape_Mold_Baguette(312),
    Shape_Mold_Cylinder(313),
    Shape_Mold_Anvil(314),
    Shape_Mold_Name(315),
    Shape_Mold_Arrow(316),
    Shape_Mold_Gear_Small(317),
    Shape_Mold_Rod(318),
    Shape_Mold_Bolt(319),
    Shape_Mold_Round(320),
    Shape_Mold_Screw(321),
    Shape_Mold_Ring(322),
    Shape_Mold_Rod_Long(323),
    Shape_Mold_Rotor(324),
    Shape_Mold_Turbine_Blade(325),
    Shape_Mold_Pipe_Tiny(326),
    Shape_Mold_Pipe_Small(327),
    Shape_Mold_Pipe_Medium(328),
    Shape_Mold_Pipe_Large(329),
    Shape_Mold_Pipe_Huge(330),
    Shape_Mold_Tool_Head_Drill(331),
    Steam_Regulator_LV(332),
    Steam_Regulator_MV(333),
    Steam_Regulator_HV(334),
    Steam_Regulator_EV(335),
    Steam_Regulator_IV(336),
    Shape_Extruder_Plate(350),
    Shape_Extruder_Rod(351),
    Shape_Extruder_Bolt(352),
    Shape_Extruder_Ring(353),
    Shape_Extruder_Cell(354),
    Shape_Extruder_Ingot(355),
    Shape_Extruder_Wire(356),
    Shape_Extruder_Casing(357),
    Shape_Extruder_Pipe_Tiny(358),
    Shape_Extruder_Pipe_Small(359),
    Shape_Extruder_Pipe_Medium(360),
    Shape_Extruder_Pipe_Large(361),
    Shape_Extruder_Pipe_Huge(362),
    Shape_Extruder_Block(363),
    Shape_Extruder_Sword(364),
    Shape_Extruder_Pickaxe(365),
    Shape_Extruder_Shovel(366),
    Shape_Extruder_Axe(367),
    Shape_Extruder_Hoe(368),
    Shape_Extruder_Hammer(369),
    Shape_Extruder_File(370),
    Shape_Extruder_Saw(371),
    Shape_Extruder_Gear(372),
    Shape_Extruder_Bottle(373),
    Shape_Extruder_Rotor(374),
    Shape_Extruder_Small_Gear(375),
    Shape_Extruder_Turbine_Blade(376),
    Shape_Extruder_Tool_Head_Drill(377),
    Shape_Slicer_Flat(398),
    Shape_Slicer_Stripes(399),
    Fuel_Can_Plastic_Empty(400),
    Fuel_Can_Plastic_Filled(401),
    Spray_Empty(402),
    Crate_Empty(403),
    Thermos_Can_Empty(404),
    Large_Fluid_Cell_Steel(405),
    Large_Fluid_Cell_TungstenSteel(406),
    Large_Fluid_Cell_Aluminium(407),
    Large_Fluid_Cell_StainlessSteel(408),
    Large_Fluid_Cell_Titanium(409),
    Large_Fluid_Cell_Chrome(410),
    Large_Fluid_Cell_Iridium(411),
    Large_Fluid_Cell_Osmium(412),
    Large_Fluid_Cell_Neutronium(413),
    Tesseract(415),
    GigaChad(416),
    EnergisedTesseract(417),
    StableAdhesive(427),
    SuperconductorComposite(428),
    NaquadriaSupersolid(429),
    Spray_Color_0(430),
    Spray_Color_Used_0(431),
    Spray_Color_1(432),
    Spray_Color_Used_1(433),
    Spray_Color_2(434),
    Spray_Color_Used_2(435),
    Spray_Color_3(436),
    Spray_Color_Used_3(437),
    Spray_Color_4(438),
    Spray_Color_Used_4(439),
    Spray_Color_5(440),
    Spray_Color_Used_5(441),
    Spray_Color_6(442),
    Spray_Color_Used_6(443),
    Spray_Color_7(444),
    Spray_Color_Used_7(445),
    Spray_Color_8(446),
    Spray_Color_Used_8(447),
    Spray_Color_9(448),
    Spray_Color_Used_9(449),
    Spray_Color_10(450),
    Spray_Color_Used_10(451),
    Spray_Color_11(452),
    Spray_Color_Used_11(453),
    Spray_Color_12(454),
    Spray_Color_Used_12(455),
    Spray_Color_13(456),
    Spray_Color_Used_13(457),
    Spray_Color_14(458),
    Spray_Color_Used_14(459),
    Spray_Color_15(460),
    Spray_Color_Used_15(461),
    Ingot_Heavy1(462),
    Ingot_Heavy2(463),
    Ingot_Heavy3(464),
    Spray_Color_Remover(465),
    Spray_Color_Used_Remover(466),
    Spray_Color_Remover_Empty(467),
    Tool_Matches(471),
    Tool_MatchBox_Used(472),
    Tool_MatchBox_Full(473),
    Tool_Lighter_Invar_Empty(474),
    Tool_Lighter_Invar_Used(475),
    Tool_Lighter_Invar_Full(476),
    Tool_Lighter_Platinum_Empty(477),
    Tool_Lighter_Platinum_Used(478),
    Tool_Lighter_Platinum_Full(479),
    Ingot_Iridium_Alloy(480),
    Paper_Printed_Pages(481),
    Paper_Magic_Empty(482),
    Paper_Magic_Page(483),
    Paper_Magic_Pages(484),
    Paper_Punch_Card_Empty(485),
    Paper_Punch_Card_Encoded(486),
    Book_Written_01(487),
    Book_Written_02(488),
    Book_Written_03(489),
    Schematic(490),
    Schematic_Crafting(491),
    Schematic_1by1(495),
    Schematic_2by2(496),
    Schematic_3by3(497),
    Schematic_Dust(498),
    Battery_RE_ULV_Tantalum(499),
    Battery_Hull_LV(500),
    Battery_Hull_MV(501),
    Battery_Hull_HV(502),
    Battery_SU_LV_Sulfuric_Acid(510),
    Battery_SU_LV_Mercury(511),
    Battery_RE_LV_Cadmium(517),
    Battery_RE_LV_Lithium(518),
    Battery_RE_LV_Sodium(519),
    Battery_SU_MV_Sulfuric_Acid(520),
    Battery_SU_MV_Mercury(521),
    Battery_RE_MV_Cadmium(527),
    Battery_RE_MV_Lithium(528),
    Battery_RE_MV_Sodium(529),
    Battery_SU_HV_Sulfuric_Acid(530),
    Battery_SU_HV_Mercury(531),
    Battery_RE_HV_Cadmium(537),
    Battery_RE_HV_Lithium(538),
    Battery_RE_HV_Sodium(539),
    Cover_FLuidStorageMonitor(577),
    ZPM4(594),
    Electric_Motor_UEV(595),
    Electric_Motor_UHV(596),
    Energy_Lapotronic_Orb(597),
    ZPM(598),
    Energy_Lapotronic_orb_2(599),
    Electric_Motor_LV(600),
    Electric_Motor_MV(601),
    Electric_Motor_HV(602),
    Electric_Motor_EV(603),
    Electric_Motor_IV(604),
    ZPM2(605),
    Electric_Motor_LuV(606),
    Electric_Motor_ZPM(607),
    Electric_Motor_UV(608),
    ZPM3(609),
    Electric_Pump_LV(610),
    Electric_Pump_MV(611),
    Electric_Pump_HV(612),
    Electric_Pump_EV(613),
    Electric_Pump_IV(614),
    Electric_Pump_LuV(615),
    Electric_Pump_ZPM(616),
    Electric_Pump_UV(617),
    Electric_Pump_UHV(618),
    Electric_Pump_UEV(619),
    Steam_Valve_LV(620),
    Steam_Valve_MV(621),
    Steam_Valve_HV(622),
    Steam_Valve_EV(623),
    Steam_Valve_IV(624),
    Conveyor_Module_LV(630),
    Conveyor_Module_MV(631),
    Conveyor_Module_HV(632),
    Conveyor_Module_EV(633),
    Conveyor_Module_IV(634),
    Conveyor_Module_LuV(635),
    Conveyor_Module_ZPM(636),
    Conveyor_Module_UV(637),
    Conveyor_Module_UHV(638),
    Conveyor_Module_UEV(639),
    Electric_Piston_LV(640),
    Electric_Piston_MV(641),
    Electric_Piston_HV(642),
    Electric_Piston_EV(643),
    Electric_Piston_IV(644),
    Electric_Piston_LuV(645),
    Electric_Piston_ZPM(646),
    Electric_Piston_UV(647),
    Electric_Piston_UHV(648),
    Electric_Piston_UEV(649),
    Robot_Arm_LV(650),
    Robot_Arm_MV(651),
    Robot_Arm_HV(652),
    Robot_Arm_EV(653),
    Robot_Arm_IV(654),
    Robot_Arm_LuV(655),
    Robot_Arm_ZPM(656),
    Robot_Arm_UV(657),
    Robot_Arm_UHV(658),
    Robot_Arm_UEV(659),
    FluidRegulator_LV(660),
    FluidRegulator_MV(661),
    FluidRegulator_HV(662),
    FluidRegulator_EV(663),
    FluidRegulator_IV(664),
    FluidRegulator_LuV(665),
    FluidRegulator_ZPM(666),
    FluidRegulator_UV(667),
    Circuit_Parts_Crystal_Chip_Wetware(668),
    FluidFilter(669),
    Field_Generator_LV(670),
    Field_Generator_MV(671),
    Field_Generator_HV(672),
    Field_Generator_EV(673),
    Field_Generator_IV(674),
    Field_Generator_LuV(675),
    Field_Generator_ZPM(676),
    Field_Generator_UV(677),
    Field_Generator_UHV(678),
    Field_Generator_UEV(679),
    Emitter_LV(680),
    Emitter_MV(681),
    Emitter_HV(682),
    Emitter_EV(683),
    Emitter_IV(684),
    Emitter_LuV(685),
    Emitter_ZPM(686),
    Emitter_UV(687),
    Emitter_UHV(688),
    Emitter_UEV(689),
    Sensor_LV(690),
    Sensor_MV(691),
    Sensor_HV(692),
    Sensor_EV(693),
    Sensor_IV(694),
    Sensor_LuV(695),
    Sensor_ZPM(696),
    Sensor_UV(697),
    Sensor_UHV(698),
    Sensor_UEV(699),
    Circuit_Primitive(700),
    Circuit_Basic(701),
    Circuit_Good(702),
    Circuit_Advanced(703),
    Circuit_Data(704),
    Circuit_Elite(705),
    Circuit_Master(706),
    Tool_DataOrb(707),
    Tool_DataStick(708),
    Tool_Cover_Copy_Paste(709),
    Circuit_Board_Basic(710),
    Circuit_Board_Advanced(711),
    Circuit_Board_Elite(712),
    Circuit_Parts_Crystal_Chip_Elite(713),
    Circuit_Parts_Crystal_Chip_Master(714),
    Circuit_Parts_Advanced(715),
    Circuit_Parts_Wiring_Basic(716),
    Circuit_Parts_Wiring_Advanced(717),
    Circuit_Parts_Wiring_Elite(718),
    Empty_Board_Basic(719),
    Empty_Board_Elite(720),
    Component_Sawblade_Diamond(721),
    Component_Grinder_Diamond(722),
    Component_Grinder_Tungsten(723),
    QuantumEye(724),
    QuantumStar(725),
    Gravistar(726),
    Upgrade_Muffler(727),
    Upgrade_Lock(728),
    Component_Filter(729),
    Cover_Controller(730),
    Cover_ActivityDetector(731),
    Cover_FluidDetector(732),
    Cover_ItemDetector(733),
    Cover_EnergyDetector(734),
    Cover_PlayerDetector(735),
    Energy_Module(736),
    Energy_Cluster(737),
    Cover_Screen(740),
    Cover_RedstoneTransmitterExternal(741),
    Cover_RedstoneTransmitterInternal(742),
    Cover_Crafting(744),
    Cover_Drain(745),
    Cover_RedstoneReceiverExternal(746),
    Cover_RedstoneReceiverInternal(747),
    Cover_NeedsMaintenance(748),
    Cover_Shutter(749),
    Cover_SolarPanel(750),
    Cover_SolarPanel_8V(751),
    Cover_SolarPanel_LV(752),
    Cover_SolarPanel_MV(753),
    Cover_SolarPanel_HV(754),
    Cover_SolarPanel_EV(755),
    Cover_SolarPanel_IV(756),
    Cover_SolarPanel_LuV(757),
    Cover_SolarPanel_ZPM(758),
    Cover_SolarPanel_UV(759),
    Tool_Sonictron(760),
    Tool_Cheat(761),
    Tool_Scanner(762),
    NC_SensorKit(763),
    Duct_Tape(764),
    McGuffium_239(765);

    final int ID;

    ID_MetaItem_01(int ID) {
        this.ID = ID;
    }
}

public class GT_MetaGenerated_Item_01 extends GT_MetaGenerated_Item_X32 {

    public static GT_MetaGenerated_Item_01 INSTANCE;
    private final String mToolTipPurify = GT_LanguageManager
        .addStringLocalization("metaitem.01.tooltip.purify", "Throw into Cauldron to get clean Dust");
    private static final String aTextArrow = "  A";
    private static final String aTextStick = " S ";
    private static final String aTextFeather = "F  ";
    private static final String aTextEmptyRow = "   ";
    private static final String aTextShape = " P ";
    private static final String PartCoverText = " L/t (";
    private static final String PartCoverText2 = " L/s) as Cover";
    private static final String PartNotCoverText = "Cannot be used as a Cover";
    private static final String RAText = "Grabs from and inserts into specific slots";
    private static final String FRText1 = "Configurable up to ";
    private static final String FRText2 = " L/sec (as Cover)/n Rightclick/Screwdriver-rightclick/Shift-screwdriver-rightclick/n to adjust the pump speed by 1/16/256 L/sec per click";

    private static final int[] Spray_Colors = new int[] { Spray_Color_0.ID, Spray_Color_1.ID, Spray_Color_2.ID,
        Spray_Color_3.ID, Spray_Color_4.ID, Spray_Color_5.ID, Spray_Color_6.ID, Spray_Color_7.ID, Spray_Color_8.ID,
        Spray_Color_9.ID, Spray_Color_10.ID, Spray_Color_11.ID, Spray_Color_12.ID, Spray_Color_13.ID, Spray_Color_14.ID,
        Spray_Color_15.ID };
    private static final int[] Spray_Colors_Used = new int[] { Spray_Color_Used_0.ID, Spray_Color_Used_1.ID,
        Spray_Color_Used_2.ID, Spray_Color_Used_3.ID, Spray_Color_Used_4.ID, Spray_Color_Used_5.ID,
        Spray_Color_Used_6.ID, Spray_Color_Used_7.ID, Spray_Color_Used_8.ID, Spray_Color_Used_9.ID,
        Spray_Color_Used_10.ID, Spray_Color_Used_11.ID, Spray_Color_Used_12.ID, Spray_Color_Used_13.ID,
        Spray_Color_Used_14.ID, Spray_Color_Used_15.ID };

    public GT_MetaGenerated_Item_01() {
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
            OrePrefixes.ingotDouble,
            OrePrefixes.ingotTriple,
            OrePrefixes.ingotQuadruple,
            OrePrefixes.ingotQuintuple,
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
            .set(addItem(ID_MetaItem_01.Credit_Greg_Copper.ID, "Copper GT Credit", "0.125 Credits"));
        ItemList.Credit_Greg_Cupronickel.set(
            addItem(
                ID_MetaItem_01.Credit_Greg_Cupronickel.ID,
                "Cupronickel GT Credit",
                "1 Credit",
                new ItemData(Materials.Cupronickel, 907200L)));
        ItemList.Credit_Greg_Silver.set(
            addItem(
                ID_MetaItem_01.Credit_Greg_Silver.ID,
                "Silver GT Credit",
                "8 Credits",
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)));
        ItemList.Credit_Greg_Gold.set(addItem(ID_MetaItem_01.Credit_Greg_Gold.ID, "Gold GT Credit", "64 Credits"));
        ItemList.Credit_Greg_Platinum
            .set(addItem(ID_MetaItem_01.Credit_Greg_Platinum.ID, "Platinum GT Credit", "512 Credits"));
        ItemList.Credit_Greg_Osmium
            .set(addItem(ID_MetaItem_01.Credit_Greg_Osmium.ID, "Osmium GT Credit", "4,096 Credits"));
        ItemList.Credit_Greg_Naquadah
            .set(addItem(ID_MetaItem_01.Credit_Greg_Naquadah.ID, "Naquadah GT Credit", "32,768 Credits"));
        ItemList.Credit_Greg_Neutronium
            .set(addItem(ID_MetaItem_01.Credit_Greg_Neutronium.ID, "Neutronium GT Credit", "262,144 Credits"));
        ItemList.Coin_Gold_Ancient.set(
            addItem(
                ID_MetaItem_01.Coin_Gold_Ancient.ID,
                "Ancient Gold Coin",
                "Found in ancient Ruins",
                new ItemData(Materials.Gold, 907200L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 8L)));
        ItemList.Coin_Doge.set(
            addItem(
                ID_MetaItem_01.Coin_Doge.ID,
                "Doge Coin",
                "wow much coin how money so crypto plz mine v rich very currency wow",
                new ItemData(Materials.Brass, 907200L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)));
        ItemList.Coin_Chocolate.set(
            addItem(
                ID_MetaItem_01.Coin_Chocolate.ID,
                "Chocolate Coin",
                "Wrapped in Gold",
                new ItemData(Materials.Gold, OrePrefixes.foil.mMaterialAmount),
                new GT_FoodStat(
                    1,
                    0.1F,
                    EnumAction.eat,
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 1L),
                    true,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    200,
                    1,
                    100)));
        ItemList.Credit_Copper
            .set(addItem(ID_MetaItem_01.Credit_Copper.ID, "Industrial Copper Credit", "0.125 Credits"));

        ItemList.Credit_Silver.set(
            addItem(
                ID_MetaItem_01.Credit_Silver.ID,
                "Industrial Silver Credit",
                "8 Credits",
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)));
        ItemList.Credit_Gold.set(addItem(ID_MetaItem_01.Credit_Gold.ID, "Industrial Gold Credit", "64 Credits"));
        ItemList.Credit_Platinum
            .set(addItem(ID_MetaItem_01.Credit_Platinum.ID, "Industrial Platinum Credit", "512 Credits"));
        ItemList.Credit_Osmium
            .set(addItem(ID_MetaItem_01.Credit_Osmium.ID, "Industrial Osmium Credit", "4096 Credits"));

        ItemList.Component_Minecart_Wheels_Iron.set(
            addItem(
                Component_Minecraft_Wheels_Iron.ID,
                "Iron Minecart Wheels",
                "To get things rolling",
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L)));
        ItemList.Component_Minecart_Wheels_Steel.set(
            addItem(
                Component_Minecraft_Wheels_Steel.ID,
                "Steel Minecart Wheels",
                "To get things rolling",
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Minecart_Wheels_Iron.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " h ", "RSR", " w ", 'R', OrePrefixes.ring.get(Materials.AnyIron), 'S',
                OrePrefixes.stick.get(Materials.AnyIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Minecart_Wheels_Steel.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " h ", "RSR", " w ", 'R', OrePrefixes.ring.get(Materials.Steel), 'S',
                OrePrefixes.stick.get(Materials.Steel) });

        ItemList.CompressedFireclay.set(addItem(Compressed_Fireclay.ID, "Compressed Fireclay", "Brick-shaped"));
        ItemList.Firebrick.set(addItem(Firebrick.ID, "Firebrick", "Heat resistant"));

        ItemList.Arrow_Head_Glass_Emtpy.set(
            addItem(
                Arrow_Head_Glass_Empty.ID,
                "Empty Glass Arrow Head",
                "Fill with Potions before use",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Arrow_Head_Glass_Poison.set(
            addItem(
                Arrow_Head_Glass_Poison.ID,
                "Poison Glass Arrow Head",
                "Glass Arrow filled with Poison",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Poison_Long.set(
            addItem(
                Arrow_Head_Glass_Poison_Long.ID,
                "Poison Glass Arrow Head",
                "Glass Arrow filled with stretched Poison",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Poison_Strong.set(
            addItem(
                Arrow_Head_Glass_Poison_Strong.ID,
                "Poison Glass Arrow Head",
                "Glass Arrow filled with strong Poison",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Slowness.set(
            addItem(
                Arrow_Head_Glass_Slowness.ID,
                "Slowness Glass Arrow Head",
                "Glass Arrow filled with Laming Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Slowness_Long.set(
            addItem(
                Arrow_Head_Glass_Slowness_Long.ID,
                "Slowness Glass Arrow Head",
                "Glass Arrow filled with stretched Laming Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Weakness.set(
            addItem(
                Arrow_Head_Glass_Weakness.ID,
                "Weakness Glass Arrow Head",
                "Glass Arrow filled with Weakening Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Weakness_Long.set(
            addItem(
                Arrow_Head_Glass_Weakness_Long.ID,
                "Weakness Glass Arrow Head",
                "Glass Arrow filled with stretched Weakening Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Holy_Water.set(
            addItem(
                Arrow_Head_Glass_Holy_Water.ID,
                "Holy Water Glass Arrow Head",
                "Glass Arrow filled with Holy Water",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 1L)));

        ItemList.Arrow_Wooden_Glass_Emtpy.set(
            addItem(
                Arrow_Wooden_Glass_Empty.ID,
                "Regular Glass Vial Arrow",
                "Empty Glass Arrow",
                new Behaviour_Arrow_Potion(1.0F, 6.0F),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Arrow_Wooden_Glass_Poison.set(
            addItem(
                Arrow_Wooden_Glass_Poison.ID,
                "Regular Poison Arrow",
                "Glass Arrow filled with Poison",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.poison.id, 450, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Poison_Long.set(
            addItem(
                Arrow_Wooden_Glass_Poison_Long.ID,
                "Regular Poison Arrow",
                "Glass Arrow filled with stretched Poison",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.poison.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Poison_Strong.set(
            addItem(
                Arrow_Wooden_Glass_Poison_Strong.ID,
                "Regular Poison Arrow",
                "Glass Arrow filled with strong Poison",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.poison.id, 450, 1, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Slowness.set(
            addItem(
                Arrow_Wooden_Glass_Slowness.ID,
                "Regular Slowness Arrow",
                "Glass Arrow filled with Laming Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.moveSlowdown.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Slowness_Long.set(
            addItem(
                Arrow_Wooden_Glass_Slowness_Long.ID,
                "Regular Slowness Arrow",
                "Glass Arrow filled with stretched Laming Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.moveSlowdown.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Weakness.set(
            addItem(
                Arrow_Wooden_Glass_Weakness.ID,
                "Regular Weakness Arrow",
                "Glass Arrow filled with Weakening Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.weakness.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Weakness_Long.set(
            addItem(
                Arrow_Wooden_Glass_Weakness_Long.ID,
                "Regular Weakness Arrow",
                "Glass Arrow filled with stretched Weakening Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.weakness.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Holy_Water.set(
            addItem(
                Arrow_Wooden_Glass_Holy_Water.ID,
                "Regular Holy Water Arrow",
                "Glass Arrow filled with Holy Water",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Enchantment.smite, 10),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 1L)));

        ItemList.Arrow_Plastic_Glass_Emtpy.set(
            addItem(
                Arrow_Plastic_Glass_Empty.ID,
                "Light Glass Vial Arrow",
                "Empty Glass Arrow",
                new Behaviour_Arrow_Potion(1.5F, 6.0F),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Arrow_Plastic_Glass_Poison.set(
            addItem(
                Arrow_Plastic_Glass_Poison.ID,
                "Light Poison Arrow",
                "Glass Arrow filled with Poison",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.poison.id, 450, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Poison_Long.set(
            addItem(
                Arrow_Plastic_Glass_Poison_Long.ID,
                "Light Poison Arrow",
                "Glass Arrow filled with stretched Poison",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.poison.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Poison_Strong.set(
            addItem(
                Arrow_Plastic_Glass_Poison_Strong.ID,
                "Light Poison Arrow",
                "Glass Arrow filled with strong Poison",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.poison.id, 450, 1, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Slowness.set(
            addItem(
                Arrow_Plastic_Glass_Slowness.ID,
                "Light Slowness Arrow",
                "Glass Arrow filled with Laming Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.moveSlowdown.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Slowness_Long.set(
            addItem(
                Arrow_Plastic_Glass_Slowness_Long.ID,
                "Light Slowness Arrow",
                "Glass Arrow filled with stretched Laming Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.moveSlowdown.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Weakness.set(
            addItem(
                Arrow_Plastic_Glass_Weakness.ID,
                "Light Weakness Arrow",
                "Glass Arrow filled with Weakening Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.weakness.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Weakness_Long.set(
            addItem(
                Arrow_Plastic_Glass_Weakness_Long.ID,
                "Light Weakness Arrow",
                "Glass Arrow filled with stretched Weakening Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.weakness.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Holy_Water.set(
            addItem(
                Arrow_Plastic_Glass_Holy_Water.ID,
                "Light Holy Water Arrow",
                "Glass Arrow filled with Holy Water",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Enchantment.smite, 10),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 1L)));

        ItemList.Shape_Empty.set(
            addItem(
                Shape_Empty.ID,
                "Empty Shape Plate",
                "Raw Plate to make Molds and Extruder Shapes",
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)));

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
        ItemList.Shape_Extruder_Wire
            .set(addItem(Shape_Extruder_Wire.ID, "Extruder Shape (Wire)", "Extruder Shape for making Wires"));
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
                new ItemData(Materials.Plastic, OrePrefixes.plate.mMaterialAmount * 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));
        ItemList.Fuel_Can_Plastic_Filled.set(
            addItem(
                Fuel_Can_Plastic_Filled.ID,
                "Plastic Fuel Can",
                "Burns well in Diesel Generators",
                new ItemData(Materials.Plastic, OrePrefixes.plate.mMaterialAmount * 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));

        ItemList.Spray_Empty.set(
            addItem(
                Spray_Empty.ID,
                "Empty Spray Can",
                "Used for making Sprays",
                new ItemData(
                    Materials.Tin,
                    OrePrefixes.plate.mMaterialAmount * 2L,
                    Materials.Redstone,
                    OrePrefixes.dust.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));

        ItemList.Crate_Empty.set(
            addItem(
                Crate_Empty.ID,
                "Empty Crate",
                "To Package lots of Material",
                new ItemData(Materials.Wood, 3628800L, Materials.Iron, OrePrefixes.screw.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L)));

        ItemList.ThermosCan_Empty.set(
            addItem(
                Thermos_Can_Empty.ID,
                "Empty Thermos Can",
                "Keeping hot things hot and cold things cold",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.mMaterialAmount * 1L + 2L * OrePrefixes.ring.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 1L)));

        ItemList.Large_Fluid_Cell_Steel.set(
            addItem(
                Large_Fluid_Cell_Steel.ID,
                "Large Steel Fluid Cell",
                "",
                new ItemData(
                    Materials.Steel,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Bronze, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));

        ItemList.Large_Fluid_Cell_TungstenSteel.set(
            addItem(
                Large_Fluid_Cell_TungstenSteel.ID,
                "Large Tungstensteel Fluid Cell",
                "",
                new ItemData(
                    Materials.TungstenSteel,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Platinum, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 9L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 7L)));

        ItemList.Large_Fluid_Cell_Aluminium.set(
            addItem(
                Large_Fluid_Cell_Aluminium.ID,
                "Large Aluminium Fluid Cell",
                "",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Silver, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 5L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 3L)));

        ItemList.Large_Fluid_Cell_StainlessSteel.set(
            addItem(
                Large_Fluid_Cell_StainlessSteel.ID,
                "Large Stainless Steel Fluid Cell",
                "",
                new ItemData(
                    Materials.StainlessSteel,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Electrum, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));

        ItemList.Large_Fluid_Cell_Titanium.set(
            addItem(
                Large_Fluid_Cell_Titanium.ID,
                "Large Titanium Fluid Cell",
                "",
                new ItemData(
                    Materials.Titanium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.RoseGold, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 7L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 5L)));

        ItemList.Large_Fluid_Cell_Chrome.set(
            addItem(
                Large_Fluid_Cell_Chrome.ID,
                "Large Chrome Fluid Cell",
                "",
                new ItemData(
                    Materials.Chrome,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Palladium, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 6L)));

        ItemList.Large_Fluid_Cell_Iridium.set(
            addItem(
                Large_Fluid_Cell_Iridium.ID,
                "Large Iridium Fluid Cell",
                "",
                new ItemData(
                    Materials.Iridium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Naquadah, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 10L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));

        ItemList.Large_Fluid_Cell_Osmium.set(
            addItem(
                Large_Fluid_Cell_Osmium.ID,
                "Large Osmium Fluid Cell",
                "",
                new ItemData(
                    Materials.Osmium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.ElectrumFlux, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 11L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 9L)));

        ItemList.Large_Fluid_Cell_Neutronium.set(
            addItem(
                Large_Fluid_Cell_Neutronium.ID,
                "Large Neutronium Fluid Cell",
                "",
                new ItemData(
                    Materials.Neutronium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Draconium, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 12L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 10L)));

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            ItemList.SPRAY_CAN_DYES[i].set(
                addItem(
                    Spray_Colors[i],
                    "Spray Can (" + Dyes.get(i).mName + ")",
                    "Full",
                    new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L)));
            ItemList.SPRAY_CAN_DYES_USED[i].set(
                addItem(
                    Spray_Colors_Used[i],
                    "Spray Can (" + Dyes.get(i).mName + ")",
                    "Used",
                    new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 3L),
                    SubTag.INVISIBLE));
        }

        ItemList.Spray_Color_Remover.set(
            addItem(
                Spray_Color_Remover.ID,
                "Spray Can Solvent",
                "Full",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 8L)));
        ItemList.Spray_Color_Used_Remover.set(
            addItem(
                Spray_Color_Used_Remover.ID,
                "Spray Can Solvent",
                "Used",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 3L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 6L),
                SubTag.INVISIBLE));

        ItemList.Spray_Color_Remover_Empty.set(
            addItem(
                Spray_Color_Remover_Empty.ID,
                "Empty Spray Can Solvent Cannister",
                "Used for making Spray Can Solvent",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    Materials.Redstone,
                    OrePrefixes.dust.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));

        ItemList.Tool_Matches.set(
            addItem(
                Tool_Matches.ID,
                "Match",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));

        ItemList.Tool_MatchBox_Used.set(
            addItem(
                Tool_MatchBox_Used.ID,
                "Match Box",
                "This is not a Car",
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                SubTag.INVISIBLE));

        ItemList.Tool_MatchBox_Full.set(
            addItem(
                Tool_MatchBox_Full.ID,
                "Match Box (Full)",
                "This is not a Car",
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        ItemList.Tool_Lighter_Invar_Empty.set(
            addItem(
                Tool_Lighter_Invar_Empty.ID,
                "Lighter (Empty)",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Invar_Used.set(
            addItem(
                Tool_Lighter_Invar_Used.ID,
                "Lighter",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Invar_Full.set(
            addItem(
                Tool_Lighter_Invar_Full.ID,
                "Lighter (Full)",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        ItemList.Tool_Lighter_Platinum_Empty.set(
            addItem(
                Tool_Lighter_Platinum_Empty.ID,
                "Platinum Lighter (Empty)",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Platinum_Used.set(
            addItem(
                Tool_Lighter_Platinum_Used.ID,
                "Platinum Lighter",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Platinum_Full.set(
            addItem(
                Tool_Lighter_Platinum_Full.ID,
                "Platinum Lighter (Full)",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

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
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)));

        ItemList.Paper_Printed_Pages.set(
            addItem(
                Paper_Printed_Pages.ID,
                "Printed Pages",
                "Used to make written Books",
                new ItemData(Materials.Paper, 10886400L),
                new Behaviour_PrintedPages(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Paper_Magic_Empty.set(
            addItem(
                Paper_Magic_Empty.ID,
                "Magic Paper",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 1L)));
        ItemList.Paper_Magic_Page.set(
            addItem(
                Paper_Magic_Page.ID,
                "Enchanted Page",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 2L)));
        ItemList.Paper_Magic_Pages.set(
            addItem(
                Paper_Magic_Pages.ID,
                "Enchanted Pages",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 10886400L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 4L)));
        ItemList.Paper_Punch_Card_Empty.set(
            addItem(
                Paper_Punch_Card_Empty.ID,
                "Punch Card",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L)));
        ItemList.Paper_Punch_Card_Encoded.set(
            addItem(
                Paper_Punch_Card_Encoded.ID,
                "Punched Card",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Book_Written_01.set(
            addItem(
                Book_Written_01.ID,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new Behaviour_WrittenBook(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Book_Written_02.set(
            addItem(
                Book_Written_02.ID,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new Behaviour_WrittenBook(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Book_Written_03.set(
            addItem(
                Book_Written_03.ID,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new Behaviour_WrittenBook(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));

        ItemList.Schematic.set(
            addItem(
                Schematic.ID,
                "Schematic",
                "EMPTY",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 1L)));
        ItemList.Schematic_Crafting.set(
            addItem(
                Schematic_Crafting.ID,
                "Schematic (Crafting)",
                "Crafts the Programmed Recipe",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_1by1.set(
            addItem(
                Schematic_1by1.ID,
                "Schematic (1x1)",
                "Crafts 1 Items as 1x1 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_2by2.set(
            addItem(
                Schematic_2by2.ID,
                "Schematic (2x2)",
                "Crafts 4 Items as 2x2 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_3by3.set(
            addItem(
                Schematic_3by3.ID,
                "Schematic (3x3)",
                "Crafts 9 Items as 3x3 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_Dust.set(
            addItem(
                Schematic_Dust.ID,
                "Schematic (Dusts)",
                "Combines Dusts (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));

        ItemList.Battery_Hull_LV.set(
            addItem(
                Battery_Hull_LV.ID,
                "Small Battery Hull",
                "An empty LV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.mMaterialAmount * 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Battery_Hull_MV.set(
            addItem(
                Battery_Hull_MV.ID,
                "Medium Battery Hull",
                "An empty MV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.mMaterialAmount * 3L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Battery_Hull_HV.set(
            addItem(
                Battery_Hull_HV.ID,
                "Large Battery Hull",
                "An empty HV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.mMaterialAmount * 9L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));

        // ULV Batteries
        ItemList.Battery_RE_ULV_Tantalum.set(
            addItem(
                Battery_RE_ULV_Tantalum.ID,
                "Tantalum Capacitor",
                "Reusable",
                "batteryULV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L)));

        // LV Batteries
        ItemList.Battery_SU_LV_SulfuricAcid.set(
            addItem(
                Battery_SU_LV_Sulfuric_Acid.ID,
                "Small Acid Battery",
                "Single Use",
                "batteryLV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        ItemList.Battery_SU_LV_Mercury.set(
            addItem(
                Battery_SU_LV_Mercury.ID,
                "Small Mercury Battery",
                "Single Use",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        ItemList.Battery_RE_LV_Cadmium.set(
            addItem(
                Battery_RE_LV_Cadmium.ID,
                "Small Cadmium Battery",
                "Reusable",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                "batteryLV"));

        ItemList.Battery_RE_LV_Lithium.set(
            addItem(
                Battery_RE_LV_Lithium.ID,
                "Small Lithium Battery",
                "Reusable",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                "batteryLV"));

        ItemList.Battery_RE_LV_Sodium.set(
            addItem(
                Battery_RE_LV_Sodium.ID,
                "Small Sodium Battery",
                "Reusable",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                "batteryLV"));

        // MV Batteries
        ItemList.Battery_SU_MV_SulfuricAcid.set(
            addItem(
                Battery_SU_MV_Sulfuric_Acid.ID,
                "Medium Acid Battery",
                "Single Use",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));

        ItemList.Battery_SU_MV_Mercury.set(
            addItem(
                Battery_SU_MV_Mercury.ID,
                "Medium Mercury Battery",
                "Single Use",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));

        ItemList.Battery_RE_MV_Cadmium.set(
            addItem(
                Battery_RE_MV_Cadmium.ID,
                "Medium Cadmium Battery",
                "Reusable",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        ItemList.Battery_RE_MV_Lithium.set(
            addItem(
                Battery_RE_MV_Lithium.ID,
                "Medium Lithium Battery",
                "Reusable",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        ItemList.Battery_RE_MV_Sodium.set(
            addItem(
                Battery_RE_MV_Sodium.ID,
                "Medium Sodium Battery",
                "Reusable",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        // HV Batteries
        ItemList.Battery_SU_HV_SulfuricAcid.set(
            addItem(
                Battery_SU_HV_Sulfuric_Acid.ID,
                "Large Acid Battery",
                "Single Use",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 8L)));

        ItemList.Battery_SU_HV_Mercury.set(
            addItem(
                Battery_SU_HV_Mercury.ID,
                "Large Mercury Battery",
                "Single Use",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 8L)));

        ItemList.Battery_RE_HV_Cadmium.set(
            addItem(
                Battery_RE_HV_Cadmium.ID,
                "Large Cadmium Battery",
                "Reusable",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));

        ItemList.Battery_RE_HV_Lithium.set(
            addItem(
                Battery_RE_HV_Lithium.ID,
                "Large Lithium Battery",
                "Reusable",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));

        ItemList.Battery_RE_HV_Sodium.set(
            addItem(
                Battery_RE_HV_Sodium.ID,
                "Large Sodium Battery",
                "Reusable",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));

        // IV Battery
        ItemList.Energy_LapotronicOrb.set(
            addItem(
                Energy_Lapotronic_Orb.ID,
                "Lapotronic Energy Orb",
                "Reusable battery",
                "batteryIV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.Ultimate)));

        // ZPM Module
        ItemList.ZPM.set(
            addItem(
                ID_MetaItem_01.ZPM.ID,
                "Zero Point Module",
                "Single use battery",
                "batteryZPM",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));

        // LuV Lapotron orb cluster battery
        ItemList.Energy_LapotronicOrb2.set(
            addItem(
                Energy_Lapotronic_orb_2.ID,
                "Lapotronic Energy Orb Cluster",
                "Reusable battery",
                "batteryLuV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.Ultimate)));

        // UV Battery
        ItemList.ZPM2.set(
            addItem(
                ZPM2.ID,
                "Ultimate Battery",
                "Fill this to win minecraft",
                "batteryUV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));

        // UMV Battery
        ItemList.ZPM3.set(
            addItem(
                ZPM3.ID,
                "Really Ultimate Battery",
                "Fill this to be way older",
                "batteryUMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));

        // UXV Battery
        ItemList.ZPM4.set(
            addItem(
                ZPM4.ID,
                "Extremely Ultimate Battery",
                "Fill this to be older",
                "batteryUXV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));

        // MAX Battery
        ItemList.ZPM5.set(
            addItem(
                ZPM5.ID,
                "Insanely Ultimate Battery",
                "Fill this for fun",
                "batteryMAX",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));

        // ERROR Battery
        ItemList.ZPM6.set(
            addItem(
                ZPM6.ID,
                "Mega Ultimate Battery",
                "Fill the capacitor to reach enlightenment",
                "batteryERV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));

        // ZPM Cluster
        ItemList.Energy_Module.set(
            addItem(
                Energy_Module.ID,
                "Energy Module",
                "Reusable battery",
                "batteryZPM",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.Ultimate)));

        // UV Cluster
        ItemList.Energy_Cluster.set(
            addItem(
                Energy_Cluster.ID,
                "Energy Cluster",
                "Reusable battery",
                "batteryUV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));

        // UIV, UMV, UXV and MAX component textures backported from gregicality.
        ItemList.Electric_Motor_LV.set(
            addItem(
                Electric_Motor_LV.ID,
                "Electric Motor (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));
        ItemList.Electric_Motor_MV.set(
            addItem(
                Electric_Motor_MV.ID,
                "Electric Motor (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L)));
        ItemList.Electric_Motor_HV.set(
            addItem(
                Electric_Motor_HV.ID,
                "Electric Motor (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 4L)));
        ItemList.Electric_Motor_EV.set(
            addItem(
                Electric_Motor_EV.ID,
                "Electric Motor (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 8L)));
        ItemList.Electric_Motor_IV.set(
            addItem(
                Electric_Motor_IV.ID,
                "Electric Motor (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 16L)));
        ItemList.Electric_Motor_LuV.set(
            addItem(
                Electric_Motor_LuV.ID,
                "Electric Motor (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 32L)));
        ItemList.Electric_Motor_ZPM.set(
            addItem(
                Electric_Motor_ZPM.ID,
                "Electric Motor (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 64L)));
        ItemList.Electric_Motor_UV.set(
            addItem(
                Electric_Motor_UV.ID,
                "Electric Motor (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 128L)));
        ItemList.Electric_Motor_UHV.set(
            addItem(
                Electric_Motor_UHV.ID,
                "Electric Motor (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 256L)));
        ItemList.Electric_Motor_UEV.set(
            addItem(
                Electric_Motor_UEV.ID,
                "Electric Motor (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UIV.set(
            addItem(
                Electric_Motor_UIV.ID,
                "Electric Motor (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UMV.set(
            addItem(
                Electric_Motor_UMV.ID,
                "Electric Motor (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UXV.set(
            addItem(
                Electric_Motor_UXV.ID,
                "Electric Motor (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_MAX.set(
            addItem(
                Electric_Motor_MAX.ID,
                "Electric Motor (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));

        ItemList.Tesseract.set(
            addItem(
                Tesseract.ID,
                "Raw Tesseract",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));
        ItemList.GigaChad.set(
            addItem(
                GigaChad.ID,
                "Giga Chad Token",
                "You are worthy",
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1000L)));
        ItemList.EnergisedTesseract.set(
            addItem(
                EnergisedTesseract.ID,
                "Energised Tesseract",
                "Higher dimensional engineering",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));

        ItemList.Electric_Piston_LV.set(
            addItem(
                Electric_Piston_LV.ID,
                "Electric Piston (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));
        ItemList.Electric_Piston_MV.set(
            addItem(
                Electric_Piston_MV.ID,
                "Electric Piston (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L)));
        ItemList.Electric_Piston_HV.set(
            addItem(
                Electric_Piston_HV.ID,
                "Electric Piston (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 4L)));
        ItemList.Electric_Piston_EV.set(
            addItem(
                Electric_Piston_EV.ID,
                "Electric Piston (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 8L)));
        ItemList.Electric_Piston_IV.set(
            addItem(
                Electric_Piston_IV.ID,
                "Electric Piston (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 16L)));
        ItemList.Electric_Piston_LuV.set(
            addItem(
                Electric_Piston_LuV.ID,
                "Electric Piston (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 32L)));
        ItemList.Electric_Piston_ZPM.set(
            addItem(
                Electric_Piston_ZPM.ID,
                "Electric Piston (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 64L)));
        ItemList.Electric_Piston_UV.set(
            addItem(
                Electric_Piston_UV.ID,
                "Electric Piston (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 128L)));
        ItemList.Electric_Piston_UHV.set(
            addItem(
                Electric_Piston_UHV.ID,
                "Electric Piston (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 256L)));
        ItemList.Electric_Piston_UEV.set(
            addItem(
                Electric_Piston_UEV.ID,
                "Electric Piston (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UIV.set(
            addItem(
                Electric_Piston_UIV.ID,
                "Electric Piston (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UMV.set(
            addItem(
                Electric_Piston_UMV.ID,
                "Electric Piston (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UXV.set(
            addItem(
                Electric_Piston_UXV.ID,
                "Electric Piston (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_MAX.set(
            addItem(
                Electric_Piston_MAX.ID,
                "Electric Piston (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));

        ItemList.Electric_Pump_LV.set(
            addItem(
                Electric_Pump_LV.ID,
                "Electric Pump (LV)",
                GT_Utility.formatNumbers(32) + PartCoverText + GT_Utility.formatNumbers(32 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Electric_Pump_MV.set(
            addItem(
                Electric_Pump_MV.ID,
                "Electric Pump (MV)",
                GT_Utility.formatNumbers(128) + PartCoverText + GT_Utility.formatNumbers(128 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));
        ItemList.Electric_Pump_HV.set(
            addItem(
                Electric_Pump_HV.ID,
                "Electric Pump (HV)",
                GT_Utility.formatNumbers(512) + PartCoverText + GT_Utility.formatNumbers(512 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));
        ItemList.Electric_Pump_EV.set(
            addItem(
                Electric_Pump_EV.ID,
                "Electric Pump (EV)",
                GT_Utility.formatNumbers(2048) + PartCoverText + GT_Utility.formatNumbers(2048 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));
        ItemList.Electric_Pump_IV.set(
            addItem(
                Electric_Pump_IV.ID,
                "Electric Pump (IV)",
                GT_Utility.formatNumbers(8192) + PartCoverText + GT_Utility.formatNumbers(8192 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 16L)));
        ItemList.Electric_Pump_LuV.set(
            addItem(
                Electric_Pump_LuV.ID,
                "Electric Pump (LuV)",
                GT_Utility.formatNumbers(32768) + PartCoverText + GT_Utility.formatNumbers(32768 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 32L)));
        ItemList.Electric_Pump_ZPM.set(
            addItem(
                Electric_Pump_ZPM.ID,
                "Electric Pump (ZPM)",
                GT_Utility.formatNumbers(131072) + PartCoverText
                    + GT_Utility.formatNumbers(131072 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 64L)));
        ItemList.Electric_Pump_UV.set(
            addItem(
                Electric_Pump_UV.ID,
                "Electric Pump (UV)",
                GT_Utility.formatNumbers(524288) + PartCoverText
                    + GT_Utility.formatNumbers(524288 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 128L)));
        ItemList.Electric_Pump_UHV.set(
            addItem(
                Electric_Pump_UHV.ID,
                "Electric Pump (UHV)",
                GT_Utility.formatNumbers(1048576) + PartCoverText
                    + GT_Utility.formatNumbers(1048576 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 256L)));
        ItemList.Electric_Pump_UEV.set(
            addItem(
                Electric_Pump_UEV.ID,
                "Electric Pump (UEV)",
                GT_Utility.formatNumbers(2097152) + PartCoverText
                    + GT_Utility.formatNumbers(2097152 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_UIV.set(
            addItem(
                Electric_Pump_UIV.ID,
                "Electric Pump (UIV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_UMV.set(
            addItem(
                Electric_Pump_UMV.ID,
                "Electric Pump (UMV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_UXV.set(
            addItem(
                Electric_Pump_UXV.ID,
                "Electric Pump (UXV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_MAX.set(
            addItem(
                Electric_Pump_MAX.ID,
                "Electric Pump (MAX)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));

        ItemList.Steam_Valve_LV.set(
            addItem(
                Steam_Valve_LV.ID,
                "Steam Valve (LV)",
                GT_Utility.formatNumbers(1024) + PartCoverText + GT_Utility.formatNumbers(1024 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Steam_Valve_MV.set(
            addItem(
                Steam_Valve_MV.ID,
                "Steam Valve (MV)",
                GT_Utility.formatNumbers(2048) + PartCoverText + GT_Utility.formatNumbers(2048 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));
        ItemList.Steam_Valve_HV.set(
            addItem(
                Steam_Valve_HV.ID,
                "Steam Valve (HV)",
                GT_Utility.formatNumbers(4096) + PartCoverText + GT_Utility.formatNumbers(4096 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));
        ItemList.Steam_Valve_EV.set(
            addItem(
                Steam_Valve_EV.ID,
                "Steam Valve (EV)",
                GT_Utility.formatNumbers(8192) + PartCoverText + GT_Utility.formatNumbers(8192 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));
        ItemList.Steam_Valve_IV.set(
            addItem(
                Steam_Valve_IV.ID,
                "Steam Valve (IV)",
                GT_Utility.formatNumbers(16384) + PartCoverText + GT_Utility.formatNumbers(16384 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 16L)));

        ItemList.FluidRegulator_LV.set(
            addItem(FluidRegulator_LV.ID, "Fluid Regulator (LV)", FRText1 + GT_Utility.formatNumbers(640) + FRText2));
        ItemList.FluidRegulator_MV.set(
            addItem(FluidRegulator_MV.ID, "Fluid Regulator (MV)", FRText1 + GT_Utility.formatNumbers(2560) + FRText2));
        ItemList.FluidRegulator_HV.set(
            addItem(FluidRegulator_HV.ID, "Fluid Regulator (HV)", FRText1 + GT_Utility.formatNumbers(10240) + FRText2));
        ItemList.FluidRegulator_EV.set(
            addItem(FluidRegulator_EV.ID, "Fluid Regulator (EV)", FRText1 + GT_Utility.formatNumbers(40960) + FRText2));
        ItemList.FluidRegulator_IV.set(
            addItem(
                FluidRegulator_IV.ID,
                "Fluid Regulator (IV)",
                FRText1 + GT_Utility.formatNumbers(163840) + FRText2));
        ItemList.FluidRegulator_LuV.set(
            addItem(
                FluidRegulator_LuV.ID,
                "Fluid Regulator (LuV)",
                FRText1 + GT_Utility.formatNumbers(655360) + FRText2));
        ItemList.FluidRegulator_ZPM.set(
            addItem(
                FluidRegulator_ZPM.ID,
                "Fluid Regulator (ZPM)",
                FRText1 + GT_Utility.formatNumbers(2621440) + FRText2));
        ItemList.FluidRegulator_UV.set(
            addItem(
                FluidRegulator_UV.ID,
                "Fluid Regulator (UV)",
                FRText1 + GT_Utility.formatNumbers(10485760) + FRText2));

        ItemList.FluidFilter.set(
            addItem(FluidFilter.ID, "Fluid Filter Cover", "Set with Fluid Container to only accept one Fluid Type"));

        ItemList.ItemFilter_Export.set(
            addItem(
                ItemFilter_Export.ID,
                "Item Filter Cover (Export)",
                "Right click with an item to set filter (Only supports Export Mode)"));

        ItemList.ItemFilter_Import.set(
            addItem(
                ItemFilter_Import.ID,
                "Item Filter Cover (Import)",
                "Right click with an item to set filter (Only supports Import Mode)"));

        ItemList.Cover_FluidLimiter
            .set(addItem(Cover_FluidLimiter.ID, "Fluid Limiter Cover", "Limits fluid input depending on fill level"));

        ItemList.Conveyor_Module_LV.set(
            addItem(
                Conveyor_Module_LV.ID,
                "Conveyor Module (LV)",
                "1 stack every 20 secs (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));
        ItemList.Conveyor_Module_MV.set(
            addItem(
                Conveyor_Module_MV.ID,
                "Conveyor Module (MV)",
                "1 stack every 5 secs (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L)));
        ItemList.Conveyor_Module_HV.set(
            addItem(
                Conveyor_Module_HV.ID,
                "Conveyor Module (HV)",
                "1 stack every 1 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L)));
        ItemList.Conveyor_Module_EV.set(
            addItem(
                Conveyor_Module_EV.ID,
                "Conveyor Module (EV)",
                "1 stack every 1/5 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L)));
        ItemList.Conveyor_Module_IV.set(
            addItem(
                Conveyor_Module_IV.ID,
                "Conveyor Module (IV)",
                "1 stack every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L)));
        ItemList.Conveyor_Module_LuV.set(
            addItem(
                Conveyor_Module_LuV.ID,
                "Conveyor Module (LuV)",
                "2 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 32L)));
        ItemList.Conveyor_Module_ZPM.set(
            addItem(
                Conveyor_Module_ZPM.ID,
                "Conveyor Module (ZPM)",
                "4 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 64L)));
        ItemList.Conveyor_Module_UV.set(
            addItem(
                Conveyor_Module_UV.ID,
                "Conveyor Module (UV)",
                "8 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 128L)));
        ItemList.Conveyor_Module_UHV.set(
            addItem(
                Conveyor_Module_UHV.ID,
                "Conveyor Module (UHV)",
                "16 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 256L)));
        ItemList.Conveyor_Module_UEV.set(
            addItem(
                Conveyor_Module_UEV.ID,
                "Conveyor Module (UEV)",
                "32 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_UIV.set(
            addItem(
                Conveyor_Module_UIV.ID,
                "Conveyor Module (UIV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_UMV.set(
            addItem(
                Conveyor_Module_UMV.ID,
                "Conveyor Module (UMV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_UXV.set(
            addItem(
                Conveyor_Module_UXV.ID,
                "Conveyor Module (UXV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_MAX.set(
            addItem(
                Conveyor_Module_MAX.ID,
                "Conveyor Module (MAX)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));

        ItemList.Robot_Arm_LV.set(
            addItem(
                Robot_Arm_LV.ID,
                "Robot Arm (LV)",
                "1 stack every 20 secs (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L)));
        ItemList.Robot_Arm_MV.set(
            addItem(
                Robot_Arm_MV.ID,
                "Robot Arm (MV)",
                "1 stack every 5 secs (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Robot_Arm_HV.set(
            addItem(
                Robot_Arm_HV.ID,
                "Robot Arm (HV)",
                "1 stack every 1 sec (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 4L)));
        ItemList.Robot_Arm_EV.set(
            addItem(
                Robot_Arm_EV.ID,
                "Robot Arm (EV)",
                "1 stack every 1/5 sec (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 8L)));
        ItemList.Robot_Arm_IV.set(
            addItem(
                Robot_Arm_IV.ID,
                "Robot Arm (IV)",
                "1 stack every 1/20 sec (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 16L)));
        ItemList.Robot_Arm_LuV.set(
            addItem(
                Robot_Arm_LuV.ID,
                "Robot Arm (LuV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 32L)));
        ItemList.Robot_Arm_ZPM.set(
            addItem(
                Robot_Arm_ZPM.ID,
                "Robot Arm (ZPM)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 64L)));
        ItemList.Robot_Arm_UV.set(
            addItem(
                Robot_Arm_UV.ID,
                "Robot Arm (UV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 128L)));
        ItemList.Robot_Arm_UHV.set(
            addItem(
                Robot_Arm_UHV.ID,
                "Robot Arm (UHV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 256L)));
        ItemList.Robot_Arm_UEV.set(
            addItem(
                Robot_Arm_UEV.ID,
                "Robot Arm (UEV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UIV.set(
            addItem(
                Robot_Arm_UIV.ID,
                "Robot Arm (UIV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UMV.set(
            addItem(
                Robot_Arm_UMV.ID,
                "Robot Arm (UMV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UXV.set(
            addItem(
                Robot_Arm_UXV.ID,
                "Robot Arm (UXV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_MAX.set(
            addItem(
                Robot_Arm_MAX.ID,
                "Robot Arm (MAX)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));

        ItemList.QuantumEye.set(addItem(QuantumEye.ID, "Quantum Eye", "Improved Ender Eye"));
        ItemList.QuantumStar.set(addItem(QuantumStar.ID, "Quantum Star", "Improved Nether Star"));
        ItemList.Gravistar.set(addItem(Gravistar.ID, "Gravi Star", "Ultimate Nether Star"));

        ItemList.Emitter_LV.set(
            addItem(
                Emitter_LV.ID,
                "Emitter (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 1L)));
        ItemList.Emitter_MV.set(
            addItem(
                Emitter_MV.ID,
                "Emitter (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 2L)));
        ItemList.Emitter_HV.set(
            addItem(
                Emitter_HV.ID,
                "Emitter (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 4L)));
        ItemList.Emitter_EV.set(
            addItem(
                Emitter_EV.ID,
                "Emitter (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 8L)));
        ItemList.Emitter_IV.set(
            addItem(
                Emitter_IV.ID,
                "Emitter (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 16L)));
        ItemList.Emitter_LuV.set(
            addItem(
                Emitter_LuV.ID,
                "Emitter (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 32L)));
        ItemList.Emitter_ZPM.set(
            addItem(
                Emitter_ZPM.ID,
                "Emitter (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 64L)));
        ItemList.Emitter_UV.set(
            addItem(
                Emitter_UV.ID,
                "Emitter (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 128L)));
        ItemList.Emitter_UHV.set(
            addItem(
                Emitter_UHV.ID,
                "Emitter (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 256L)));
        ItemList.Emitter_UEV.set(
            addItem(
                Emitter_UEV.ID,
                "Emitter (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_UIV.set(
            addItem(
                Emitter_UIV.ID,
                "Emitter (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_UMV.set(
            addItem(
                Emitter_UMV.ID,
                "Emitter (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_UXV.set(
            addItem(
                Emitter_UXV.ID,
                "Emitter (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_MAX.set(
            addItem(
                Emitter_MAX.ID,
                "Emitter (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));

        ItemList.Sensor_LV.set(
            addItem(
                Sensor_LV.ID,
                "Sensor (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 1L)));

        ItemList.Sensor_MV.set(
            addItem(
                Sensor_MV.ID,
                "Sensor (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L)));
        ItemList.Sensor_HV.set(
            addItem(
                Sensor_HV.ID,
                "Sensor (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L)));
        ItemList.Sensor_EV.set(
            addItem(
                Sensor_EV.ID,
                "Sensor (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 8L)));
        ItemList.Sensor_IV.set(
            addItem(
                Sensor_IV.ID,
                "Sensor (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 16L)));
        ItemList.Sensor_LuV.set(
            addItem(
                Sensor_LuV.ID,
                "Sensor (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 32L)));
        ItemList.Sensor_ZPM.set(
            addItem(
                Sensor_ZPM.ID,
                "Sensor (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 64L)));
        ItemList.Sensor_UV.set(
            addItem(
                Sensor_UV.ID,
                "Sensor (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 128L)));
        ItemList.Sensor_UHV.set(
            addItem(
                Sensor_UHV.ID,
                "Sensor (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 256L)));
        ItemList.Sensor_UEV.set(
            addItem(
                Sensor_UEV.ID,
                "Sensor (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_UIV.set(
            addItem(
                Sensor_UIV.ID,
                "Sensor (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_UMV.set(
            addItem(
                Sensor_UMV.ID,
                "Sensor (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_UXV.set(
            addItem(
                Sensor_UXV.ID,
                "Sensor (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_MAX.set(
            addItem(
                Sensor_MAX.ID,
                "Sensor (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));

        ItemList.Field_Generator_LV.set(
            addItem(
                Field_Generator_LV.ID,
                "Field Generator (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 1L)));
        ItemList.Field_Generator_MV.set(
            addItem(
                Field_Generator_MV.ID,
                "Field Generator (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 2L)));
        ItemList.Field_Generator_HV.set(
            addItem(
                Field_Generator_HV.ID,
                "Field Generator (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 4L)));
        ItemList.Field_Generator_EV.set(
            addItem(
                Field_Generator_EV.ID,
                "Field Generator (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 8L)));
        ItemList.Field_Generator_IV.set(
            addItem(
                Field_Generator_IV.ID,
                "Field Generator (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 16L)));
        ItemList.Field_Generator_LuV.set(
            addItem(
                Field_Generator_LuV.ID,
                "Field Generator (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 32L)));
        ItemList.Field_Generator_ZPM.set(
            addItem(
                Field_Generator_ZPM.ID,
                "Field Generator (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 64L)));
        ItemList.Field_Generator_UV.set(
            addItem(
                Field_Generator_UV.ID,
                "Field Generator (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 128L)));
        ItemList.Field_Generator_UHV.set(
            addItem(
                Field_Generator_UHV.ID,
                "Field Generator (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 256L)));
        ItemList.Field_Generator_UEV.set(
            addItem(
                Field_Generator_UEV.ID,
                "Field Generator (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UIV.set(
            addItem(
                Field_Generator_UIV.ID,
                "Field Generator (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UMV.set(
            addItem(
                Field_Generator_UMV.ID,
                "Field Generator (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UXV.set(
            addItem(
                Field_Generator_UXV.ID,
                "Field Generator (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_MAX.set(
            addItem(
                Field_Generator_MAX.ID,
                "Field Generator (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));

        ItemList.StableAdhesive.set(
            addItem(
                StableAdhesive.ID,
                "Hyper-Stable Self-Healing Adhesive",
                "Complete and selective adhesion, even when torn or damaged",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 30L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 20L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 10L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VINCULUM, 5L)));
        ItemList.SuperconductorComposite.set(
            addItem(
                SuperconductorComposite.ID,
                "Superconductor Rare-Earth Composite",
                "Zero resistance to electrical and quantum flow, regardless of temperature",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 50L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 25L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 15L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 10L)));
        ItemList.NaquadriaSupersolid.set(
            addItem(
                NaquadriaSupersolid.ID,
                "Black Body Naquadria Supersolid",
                "Flows like a fluid and reflects nothing, perfect absorption and transfer",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 100L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 60L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 40L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 20L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 10L)));

        // Circuits ULV - LuV.
        ItemList.Circuit_Primitive.set(
            addItem(
                Circuit_Primitive.ID,
                "Vacuum Tube",
                "A very simple Circuit",
                OrePrefixes.circuit.get(Materials.Primitive),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Basic.set(
            addItem(
                Circuit_Basic.ID,
                "Integrated Logic Circuit",
                "A Basic Circuit",
                OrePrefixes.circuit.get(Materials.Basic),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Good.set(
            addItem(
                Circuit_Good.ID,
                "Good Electronic Circuit",
                "A Good Circuit",
                OrePrefixes.circuit.get(Materials.Good),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Advanced.set(
            addItem(
                Circuit_Advanced.ID,
                "Processor Assembly",
                "An Advanced Circuit",
                OrePrefixes.circuit.get(Materials.Advanced),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Data.set(
            addItem(
                Circuit_Data.ID,
                "Workstation",
                "An Extreme Circuit",
                OrePrefixes.circuit.get(Materials.Data),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Elite.set(
            addItem(
                Circuit_Elite.ID,
                "Mainframe",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.Elite),
                SubTag.NO_UNIFICATION));
        ItemList.Circuit_Master.set(
            addItem(
                Circuit_Master.ID,
                "Nanoprocessor Mainframe",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.Master),
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
                new Behaviour_DataOrb()));

        ItemList.Tool_DataStick.set(
            addItem(
                Tool_DataStick.ID,
                "Data Stick",
                "A Low Capacity Data Storage",
                SubTag.NO_UNIFICATION,
                new Behaviour_DataStick()));

        ItemList.Tool_Cover_Copy_Paste.set(
            addItem(
                Tool_Cover_Copy_Paste.ID,
                "Cover Copy/Paste tool",
                "Set Cover Massively.",
                Behaviour_Cover_Tool.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 6L)));

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
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L),
                OreDictNames.craftingDiamondBlade));
        ItemList.Component_Grinder_Diamond.set(
            addItem(
                Component_Grinder_Diamond.ID,
                "Diamond Grinding Head",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 6L),
                OreDictNames.craftingGrinder));
        ItemList.Component_Grinder_Tungsten.set(
            addItem(
                Component_Grinder_Tungsten.ID,
                "Tungsten Grinding Head",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 6L),
                OreDictNames.craftingGrinder));

        ItemList.Upgrade_Muffler.set(
            addItem(
                Upgrade_Muffler.ID,
                "Muffler Upgrade",
                "Makes Machines silent",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L)));
        ItemList.Upgrade_Lock.set(
            addItem(
                Upgrade_Lock.ID,
                "Lock Upgrade",
                "Protects your Machines",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 4L)));

        ItemList.Component_Filter.set(
            addItem(
                Component_Filter.ID,
                "Item Filter",
                "",
                new ItemData(Materials.Zinc, OrePrefixes.foil.mMaterialAmount * 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                OreDictNames.craftingFilter));

        ItemList.Cover_Controller.set(
            addItem(
                Cover_Controller.ID,
                "Machine Controller Cover",
                "Turns Machines ON/OFF",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_ActivityDetector.set(
            addItem(
                Cover_ActivityDetector.ID,
                "Activity Detector Cover",
                "Gives out Activity as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_FluidDetector.set(
            addItem(
                Cover_FluidDetector.ID,
                "Fluid Detector Cover",
                "Gives out Fluid Amount as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Cover_ItemDetector.set(
            addItem(
                Cover_ItemDetector.ID,
                "Item Detector Cover",
                "Gives out Item Amount as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1L)));
        ItemList.Cover_EnergyDetector.set(
            addItem(
                Cover_EnergyDetector.ID,
                "Energy Detector Cover",
                "Gives out Energy Amount as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L)));
        ItemList.Cover_PlayerDetector.set(
            addItem(
                Cover_PlayerDetector.ID,
                "Player Detector Cover",
                "Gives out close Players as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_FluidStorageMonitor.set(
            addItem(
                Cover_FLuidStorageMonitor.ID,
                "Fluid Storage Monitor Cover",
                "Displays the fluid stored in the Tank",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));

        ItemList.Cover_Screen.set(
            addItem(
                Cover_Screen.ID,
                "Computer Monitor Cover",
                "Displays Data and GUI",
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1L)));
        ItemList.Cover_Crafting.set(
            addItem(
                Cover_Crafting.ID,
                "Crafting Table Cover",
                "Better than a wooden Workbench",
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 4L)));
        ItemList.Cover_Drain.set(
            addItem(
                Cover_Drain.ID,
                "Drain Module Cover",
                "Absorbs Fluids and collects Rain",
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));

        ItemList.Cover_Shutter.set(
            addItem(
                Cover_Shutter.ID,
                "Shutter Module Cover",
                "Blocks Inventory/Tank Side. Use together with Machine Controller.",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));

        ItemList.Cover_SolarPanel.set(
            addItem(
                Cover_SolarPanel.ID,
                "Solar Panel",
                "May the Sun be with you (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 1L)));
        ItemList.Cover_SolarPanel_8V.set(
            addItem(
                Cover_SolarPanel_8V.ID,
                "Solar Panel (8V)",
                "8 Volt Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 2L)));
        ItemList.Cover_SolarPanel_LV.set(
            addItem(
                Cover_SolarPanel_LV.ID,
                "Solar Panel (LV)",
                "Low Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 4L)));
        ItemList.Cover_SolarPanel_MV.set(
            addItem(
                Cover_SolarPanel_MV.ID,
                "Solar Panel (MV)",
                "Medium Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 8L)));
        ItemList.Cover_SolarPanel_HV.set(
            addItem(
                Cover_SolarPanel_HV.ID,
                "Solar Panel (HV)",
                "High Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 16L)));
        ItemList.Cover_SolarPanel_EV.set(
            addItem(
                Cover_SolarPanel_EV.ID,
                "Solar Panel (EV)",
                "Extreme Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 32L)));
        ItemList.Cover_SolarPanel_IV.set(
            addItem(
                Cover_SolarPanel_IV.ID,
                "Solar Panel (IV)",
                "Insane Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_LuV.set(
            addItem(
                Cover_SolarPanel_LuV.ID,
                "Solar Panel (LuV)",
                "Ludicrous Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_ZPM.set(
            addItem(
                Cover_SolarPanel_ZPM.ID,
                "Solar Panel (ZPM)",
                "ZPM Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_UV.set(
            addItem(
                Cover_SolarPanel_UV.ID,
                "Solar Panel (UV)",
                "Ultimate Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));

        ItemList.Tool_Sonictron.set(
            addItem(
                Tool_Sonictron.ID,
                "Sonictron",
                "Bring your Music with you",
                Behaviour_Sonictron.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L)));
        ItemList.Tool_Cheat.set(
            addItem(
                Tool_Cheat.ID,
                "Debug Scanner",
                "Also an Infinite Energy Source",
                Behaviour_Scanner.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 64L)));
        ItemList.Tool_Scanner.set(
            addItem(
                Tool_Scanner.ID,
                "Portable Scanner",
                "Tricorder",
                Behaviour_Scanner.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 6L)));

        ItemList.NC_SensorKit.set(addItem(NC_SensorKit.ID, "GregTech Sensor Kit", "", new Behaviour_SensorKit()));
        ItemList.Duct_Tape.set(
            addItem(
                Duct_Tape.ID,
                "BrainTech Aerospace Advanced Reinforced Duct Tape FAL-84",
                "If you can't fix it with this, use more of it!",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                OreDictNames.craftingDuctTape));
        ItemList.McGuffium_239.set(
            addItem(
                McGuffium_239.ID,
                "Mc Guffium 239",
                "42% better than Phlebotnium",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SPIRITUS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITIUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 8L)));

        ItemList.Cover_RedstoneTransmitterExternal.set(
            addItem(
                Cover_RedstoneTransmitterExternal.ID,
                "Redstone Transmitter (External)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneTransmitterInternal.set(
            addItem(
                Cover_RedstoneTransmitterInternal.ID,
                "Redstone Transmitter (Internal)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneReceiverExternal.set(
            addItem(
                Cover_RedstoneReceiverExternal.ID,
                "Redstone Receiver (External)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneReceiverInternal.set(
            addItem(
                Cover_RedstoneReceiverInternal.ID,
                "Redstone Receiver (Internal)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));

        ItemList.Cover_NeedsMaintainance.set(
            addItem(
                Cover_NeedsMaintenance.ID,
                "Needs Maintenance Cover",
                "Attach to Multiblock Controller. Emits Redstone Signal if needs Maintenance",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));

        ItemList.Steam_Regulator_LV.set(
            addItem(
                Steam_Regulator_LV.ID,
                "Steam Regulator (LV)",
                GT_Utility.formatNumbers(1024) + PartCoverText + GT_Utility.formatNumbers(1024 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Steam_Regulator_MV.set(
            addItem(
                Steam_Regulator_MV.ID,
                "Steam Regulator (MV)",
                GT_Utility.formatNumbers(2048) + PartCoverText + GT_Utility.formatNumbers(2048 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));
        ItemList.Steam_Regulator_HV.set(
            addItem(
                Steam_Regulator_HV.ID,
                "Steam Regulator (HV)",
                GT_Utility.formatNumbers(4096) + PartCoverText + GT_Utility.formatNumbers(4096 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));
        ItemList.Steam_Regulator_EV.set(
            addItem(
                Steam_Regulator_EV.ID,
                "Steam Regulator (EV)",
                GT_Utility.formatNumbers(8192) + PartCoverText + GT_Utility.formatNumbers(8192 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));
        ItemList.Steam_Regulator_IV.set(
            addItem(
                Steam_Regulator_IV.ID,
                "Steam Regulator (IV)",
                GT_Utility.formatNumbers(16384) + PartCoverText + GT_Utility.formatNumbers(16384 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 16L)));

        removeRecipes();
        setBurnValues();
        oredictBlacklistEntries();
        registerCovers();
        registerBehaviors();
        setAllFluidContainerStats();
        setAllElectricStats();
        registerTieredTooltips();

        compressorRecipes();
        craftingShapedRecipes();
        craftingShapelessRecipes();
        assemblerRecipes();
        cannerRecipes();
        packagerRecipes();
        extractorRecipes();
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
            Materials aMaterial = GregTech_API.sGeneratedMaterials[(aDamage % 1000)];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                int tX = MathHelper.floor_double(aItemEntity.posX);
                int tY = MathHelper.floor_double(aItemEntity.posY);
                int tZ = MathHelper.floor_double(aItemEntity.posZ);
                OrePrefixes aPrefix = this.mGeneratedPrefixList[(aDamage / 1000)];
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {

                        aMaterial = cauldronRemap.getOrDefault(aMaterial, aMaterial);

                        aItemEntity.setEntityItemStack(
                            GT_OreDictUnificator
                                .get(OrePrefixes.dust, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if (aPrefix == OrePrefixes.crushed) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(
                            GT_OreDictUnificator
                                .get(OrePrefixes.crushedPurified, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if (aPrefix == OrePrefixes.dust && aMaterial == Materials.Wheat) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(ItemList.Food_Dough.get(aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        int aDamage = aStack.getItemDamage();
        if ((aDamage < 32000) && (aDamage >= 0)) {
            Materials aMaterial = GregTech_API.sGeneratedMaterials[(aDamage % 1000)];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                OrePrefixes aPrefix = this.mGeneratedPrefixList[(aDamage / 1000)];
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    aList.add(this.mToolTipPurify);
                }
            }
        }
    }

    public boolean isPlasmaCellUsed(OrePrefixes aPrefix, Materials aMaterial) {
        Collection<GT_Recipe> fusionRecipes = RecipeMaps.fusionRecipes.getAllRecipes();
        if (aPrefix == OrePrefixes.cellPlasma && aMaterial.getPlasma(1L) != null) { // Materials has a plasma fluid
            for (GT_Recipe recipe : fusionRecipes) { // Loop through fusion recipes
                if (recipe.getFluidOutput(0) != null) { // Make sure fluid output can't be null (not sure if possible)
                    if (recipe.getFluidOutput(0)
                        .isFluidEqual(aMaterial.getPlasma(1L))) return true; // Fusion recipe
                                                                             // output matches
                                                                             // current plasma
                                                                             // cell fluid
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
    }

    private void oredictBlacklistEntries() {
        GT_OreDictUnificator.addToBlacklist(new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID));
    }

    private void compressorRecipes() {
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 8L))
            .itemOutputs(new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
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

        GregTech_API.registerCover(
            new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID),
            TextureFactory.of(COVER_WOOD_PLATE),
            null);

        GregTech_API.registerCover(
            ItemList.Electric_Pump_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(32, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(128, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(512, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(2048, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(8192, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(32768, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(131072, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(524288, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_UHV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[9][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(1048576, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_UEV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[10][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(2097152, TextureFactory.of(OVERLAY_PUMP)));

        GregTech_API.registerCover(
            ItemList.Steam_Valve_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(1024, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(2048, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(4096, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(8192, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(16384, TextureFactory.of(OVERLAY_VALVE)));

        GregTech_API.registerCover(
            ItemList.FluidRegulator_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(32, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(128, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(512, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(2048, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(8192, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(32768, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(131072, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(524288, TextureFactory.of(OVERLAY_PUMP)));

        GregTech_API.registerCover(
            ItemList.FluidFilter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            new GT_Cover_Fluidfilter(TextureFactory.of(OVERLAY_SHUTTER)));

        GregTech_API.registerCover(
            ItemList.ItemFilter_Export.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_ItemFilter(true, TextureFactory.of(OVERLAY_CONVEYOR)));

        GregTech_API.registerCover(
            ItemList.ItemFilter_Import.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_ItemFilter(false, TextureFactory.of(OVERLAY_CONVEYOR)));

        GregTech_API.registerCover(
            ItemList.Cover_FluidLimiter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            new GT_Cover_FluidLimiter(TextureFactory.of(OVERLAY_SHUTTER)));

        GregTech_API.registerCover(
            ItemList.Conveyor_Module_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(400, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(100, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(20, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(4, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 2, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 4, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 8, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_UHV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[9][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 16, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_UEV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[10][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 32, TextureFactory.of(OVERLAY_CONVEYOR)));

        GregTech_API.registerCover(
            ItemList.Robot_Arm_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(400, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(100, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(20, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(4, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(1, TextureFactory.of(OVERLAY_ARM)));

        GregTech_API.registerCover(
            ItemList.Cover_Controller.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_CONTROLLER)),
            new GT_Cover_ControlsWork(TextureFactory.of(OVERLAY_CONTROLLER)));

        GregTech_API.registerCover(
            ItemList.Cover_ActivityDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], doesWorkCoverTexture),
            new GT_Cover_DoesWork(doesWorkCoverTexture));
        GregTech_API.registerCover(
            ItemList.Cover_FluidDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_FLUIDDETECTOR)),
            new GT_Cover_LiquidMeter(TextureFactory.of(OVERLAY_FLUIDDETECTOR)));
        GregTech_API.registerCover(
            ItemList.Cover_ItemDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ITEMDETECTOR)),
            new GT_Cover_ItemMeter(TextureFactory.of(OVERLAY_ITEMDETECTOR)));
        GregTech_API.registerCover(
            ItemList.Cover_EnergyDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ENERGYDETECTOR)),
            new GT_Cover_EUMeter(TextureFactory.of(OVERLAY_ENERGYDETECTOR)));

        GregTech_API.registerCover(
            ItemList.Cover_PlayerDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], playerDectectorCoverTexture),
            new GT_Cover_PlayerDetector(playerDectectorCoverTexture));
        GregTech_API.registerCover(
            ItemList.Cover_FluidStorageMonitor.get(1L),
            TextureFactory.of(OVERLAY_FLUID_STORAGE_MONITOR0),
            new GT_Cover_FluidStorageMonitor());

        GregTech_API.registerCover(
            ItemList.Cover_Screen.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], screenCoverTexture),
            new GT_Cover_Screen(screenCoverTexture));
        GregTech_API.registerCover(
            ItemList.Cover_Crafting.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_CRAFTING)),
            new GT_Cover_Crafting(TextureFactory.of(OVERLAY_CRAFTING)));
        GregTech_API.registerCover(
            ItemList.Cover_Drain.get(1L),
            TextureFactory.of(MACHINE_CASINGS[0][0], TextureFactory.of(OVERLAY_DRAIN)),
            new GT_Cover_Drain(TextureFactory.of(OVERLAY_DRAIN)));
        GregTech_API.registerCover(
            ItemList.Cover_Shutter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            new GT_Cover_Shutter(TextureFactory.of(OVERLAY_SHUTTER)));

        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel.get(1L),
            TextureFactory.of(SOLARPANEL),
            new GT_Cover_SolarPanel(1));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_8V.get(1L),
            TextureFactory.of(SOLARPANEL_8V),
            new GT_Cover_SolarPanel(8));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_LV.get(1L),
            TextureFactory.of(SOLARPANEL_LV),
            new GT_Cover_SolarPanel(32));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_MV.get(1L),
            TextureFactory.of(SOLARPANEL_MV),
            new GT_Cover_SolarPanel(128));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_HV.get(1L),
            TextureFactory.of(SOLARPANEL_HV),
            new GT_Cover_SolarPanel(512));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_EV.get(1L),
            TextureFactory.of(SOLARPANEL_EV),
            new GT_Cover_SolarPanel(2048));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_IV.get(1L),
            TextureFactory.of(SOLARPANEL_IV),
            new GT_Cover_SolarPanel(8192));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_LuV.get(1L),
            TextureFactory.of(SOLARPANEL_LuV),
            new GT_Cover_SolarPanel(32768));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_ZPM.get(1L),
            TextureFactory.of(SOLARPANEL_ZPM),
            new GT_Cover_SolarPanel(131072));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_UV.get(1L),
            TextureFactory.of(SOLARPANEL_UV),
            new GT_Cover_SolarPanel(524288));

        GregTech_API.registerCover(
            ItemList.Cover_RedstoneTransmitterExternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            new GT_Cover_RedstoneTransmitterExternal(TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)));
        GregTech_API.registerCover(
            ItemList.Cover_RedstoneTransmitterInternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            new GT_Cover_RedstoneTransmitterInternal(TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)));
        GregTech_API.registerCover(
            ItemList.Cover_RedstoneReceiverExternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)),
            new GT_Cover_RedstoneReceiverExternal(TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)));
        GregTech_API.registerCover(
            ItemList.Cover_RedstoneReceiverInternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)),
            new GT_Cover_RedstoneReceiverInternal(TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)));

        GregTech_API.registerCover(
            ItemList.Steam_Regulator_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(1024, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(2048, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(4096, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(8192, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(16384, TextureFactory.of(OVERLAY_VALVE)));

        GregTech_API.registerCover(
            ItemList.Cover_NeedsMaintainance.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_MAINTENANCE_DETECTOR)),
            new GT_Cover_NeedMaintainance(TextureFactory.of(OVERLAY_MAINTENANCE_DETECTOR)));

    }

    private void removeRecipes() {
        GT_ModHandler.removeRecipe(
            new ItemStack(Blocks.glass),
            null,
            new ItemStack(Blocks.glass),
            null,
            new ItemStack(Blocks.glass));
    }

    private void assemblerRecipes() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Spray_Empty.get(1L))
            .duration(40 * SECONDS)
            .eut(1)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Aluminium, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.ThermosCan_Empty.get(1L))
            .duration(40 * SECONDS)
            .eut(1)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Steel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.AnyBronze, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Steel.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.TungstenSteel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Platinum, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Aluminium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Silver, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Aluminium.get(1L))
            .duration(10 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Electrum, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_StainlessSteel.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Titanium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.RoseGold, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Titanium.get(1L))
            .duration(10 * SECONDS)
            .eut(256)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Chrome, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Palladium, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Chrome.get(1L))
            .duration(10 * SECONDS)
            .eut(1024)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Naquadah, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Iridium.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.ElectrumFlux, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Osmium.get(1L))
            .duration(10 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Draconium, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Neutronium.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                ItemList.Large_Fluid_Cell_Aluminium.get(1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Spray_Color_Remover_Empty.get(1L))
            .duration(40 * SECONDS)
            .eut(1)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Phosphorus, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(1L))
            .duration(16 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.TricalciumPhosphate, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(1L))
            .duration(16 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(4L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(4L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Invar, 2L),
                new ItemStack(Items.flint, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Lighter_Invar_Empty.get(1L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Platinum, 2L),
                new ItemStack(Items.flint, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Lighter_Platinum_Empty.get(1L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(256)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Lock.get(1L))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Lock.get(1L))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Lock.get(1L))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Sensor_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_PlayerDetector.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                ItemList.Cover_Drain.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Shutter.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                ItemList.Cover_Drain.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Shutter.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                ItemList.Cover_Drain.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Shutter.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2L),
                new ItemStack(Blocks.iron_bars, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Drain.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 2L),
                new ItemStack(Blocks.iron_bars, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Drain.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 2L),
                new ItemStack(Blocks.iron_bars, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Drain.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                new ItemStack(Blocks.crafting_table, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Crafting.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                new ItemStack(Blocks.crafting_table, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Crafting.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                new ItemStack(Blocks.crafting_table, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Crafting.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Cover_Shutter.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.FluidFilter.get(1L))
            .duration(40 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Cover_Screen.get(1L),
                ItemList.Cover_FluidDetector.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_FluidStorageMonitor.get(1L))
            .duration(40 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Sensor_LV.get(1L), ItemList.Emitter_LV.get(1L), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.NC_SensorKit.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Emitter_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_RedstoneTransmitterExternal.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Sensor_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_RedstoneReceiverExternal.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Emitter_MV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_NeedsMaintainance.get(1L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 2L),
                ItemList.Component_Filter.get(1L),
                ItemList.Conveyor_Module_LV.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.ItemFilter_Export.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 2L),
                ItemList.Component_Filter.get(1L),
                ItemList.Conveyor_Module_LV.get(1L),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.ItemFilter_Import.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    private void cannerRecipes() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Cadmium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Lithium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Sodium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Cadmium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Lithium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Sodium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Cadmium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Lithium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Sodium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
    }

    private void packagerRecipes() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tool_Matches.get(16L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L))
            .itemOutputs(ItemList.Tool_MatchBox_Full.get(1L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Tool_MatchBox_Full.get(1L))
            .itemOutputs(ItemList.Tool_Matches.get(16L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);
    }

    private void extractorRecipes() {
        GT_ModHandler
            .addExtractionRecipe(ItemList.Battery_SU_LV_SulfuricAcid.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_SU_LV_Mercury.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler
            .addExtractionRecipe(ItemList.Battery_SU_MV_SulfuricAcid.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_SU_MV_Mercury.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler
            .addExtractionRecipe(ItemList.Battery_SU_HV_SulfuricAcid.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_SU_HV_Mercury.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_LV_Cadmium.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_LV_Lithium.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_LV_Sodium.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_MV_Cadmium.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_MV_Lithium.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_MV_Sodium.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_HV_Cadmium.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_HV_Lithium.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_HV_Sodium.get(1L), ItemList.Battery_Hull_HV.get(1L));
    }

    private void craftingShapedRecipes() {
        ItemStack tStack = new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID);
        tStack.setStackDisplayName("The holy Planks of Sengir");
        GT_Utility.ItemNBT.addEnchantment(tStack, Enchantment.smite, 10);
        GT_ModHandler.addCraftingRecipe(
            tStack,
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "XXX", "XDX", "XXX", 'X', OrePrefixes.gem.get(Materials.NetherStar), 'D',
                new ItemStack(Blocks.dragon_egg, 1, 32767) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Shape_Slicer_Flat.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", aTextShape, "fXd", 'P', ItemList.Shape_Extruder_Block, 'X',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Shape_Slicer_Stripes.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", "XPX", "fXd", 'P', ItemList.Shape_Extruder_Block, 'X',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Fuel_Can_Plastic_Empty.get(7L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " PP", "P P", "PPP", 'P', OrePrefixes.plate.get(Materials.Plastic) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Crate_Empty.get(4L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "SWS", "WdW", "SWS", 'W', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.screw.get(Materials.AnyIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Crate_Empty.get(4L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "SWS", "WdW", "SWS", 'W', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.screw.get(Materials.Steel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_1by1.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "d  ", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_2by2.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " d ", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_3by3.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  d", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_Dust.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { aTextEmptyRow, aTextShape, "  d", 'P', ItemList.Schematic });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Hull_LV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "Cf ", "Ph ", "Ps ", 'P', OrePrefixes.plate.get(Materials.BatteryAlloy), 'C',
                OreDictNames.craftingWireTin });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.IronMagnetic), 'R',
                OrePrefixes.stick.get(Materials.AnyIron), 'W', OrePrefixes.wireGt01.get(Materials.AnyCopper), 'C',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Steel), 'W', OrePrefixes.wireGt01.get(Materials.AnyCopper), 'C',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Aluminium), 'W', OrePrefixes.wireGt02.get(Materials.Cupronickel), 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.StainlessSteel), 'W', OrePrefixes.wireGt04.get(Materials.Electrum), 'C',
                OrePrefixes.cableGt02.get(Materials.Silver) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.NeodymiumMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Titanium), 'W', OrePrefixes.wireGt04.get(Materials.BlackSteel), 'C',
                OrePrefixes.cableGt02.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.NeodymiumMagnetic), 'R',
                OrePrefixes.stick.get(Materials.TungstenSteel), 'W', OrePrefixes.wireGt04.get(Materials.Graphene), 'C',
                OrePrefixes.cableGt02.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Steel), 'S',
                OrePrefixes.stick.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'M',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'S',
                OrePrefixes.stick.get(Materials.Aluminium), 'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'M',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.stick.get(Materials.StainlessSteel), 'G',
                OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Titanium), 'S',
                OrePrefixes.stick.get(Materials.Titanium), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium), 'M',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'S',
                OrePrefixes.stick.get(Materials.TungstenSteel), 'G',
                OrePrefixes.gearGtSmall.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_LV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Tin), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'P',
                OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_MV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Bronze), 'S',
                OrePrefixes.screw.get(Materials.Bronze), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'P',
                OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_HV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Steel), 'S',
                OrePrefixes.screw.get(Materials.Steel), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'P',
                OrePrefixes.pipeMedium.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_EV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium),
                'P', OrePrefixes.pipeMedium.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_IV, 'O',
                OrePrefixes.ring.get(Materials.AnySyntheticRubber), 'X', OrePrefixes.rotor.get(Materials.TungstenSteel),
                'S', OrePrefixes.screw.get(Materials.TungstenSteel), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten),
                'P', OrePrefixes.pipeMedium.get(Materials.TungstenSteel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tin), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_MV, 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'R',
                OrePrefixes.plate.get(Materials.AnySyntheticRubber) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Steel), 'M',
                ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                OrePrefixes.circuit.get(Materials.Basic), 'C', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Aluminium), 'M',
                ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                OrePrefixes.circuit.get(Materials.Good), 'C', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.StainlessSteel), 'M',
                ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                OrePrefixes.circuit.get(Materials.Advanced), 'C', OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Titanium), 'M',
                ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                OrePrefixes.circuit.get(Materials.Data), 'C', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.TungstenSteel), 'M',
                ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                OrePrefixes.circuit.get(Materials.Elite), 'C', OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.CertusQuartz), 'S',
                OrePrefixes.stick.get(Materials.Brass), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.EnderPearl), 'S',
                OrePrefixes.stick.get(Materials.Electrum), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.EnderEye), 'S',
                OrePrefixes.stick.get(Materials.Chrome), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', ItemList.QuantumEye, 'S',
                OrePrefixes.stick.get(Materials.Platinum), 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', ItemList.QuantumStar, 'S',
                OrePrefixes.stick.get(Materials.Iridium), 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gem.get(Materials.CertusQuartz), 'S',
                OrePrefixes.stick.get(Materials.Brass), 'P', OrePrefixes.plate.get(Materials.Steel), 'C',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gemFlawless.get(Materials.Emerald), 'S',
                OrePrefixes.stick.get(Materials.Electrum), 'P', OrePrefixes.plate.get(Materials.Aluminium), 'C',
                OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gem.get(Materials.EnderEye), 'S',
                OrePrefixes.stick.get(Materials.Chrome), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'C',
                OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', ItemList.QuantumEye, 'S',
                OrePrefixes.stick.get(Materials.Platinum), 'P', OrePrefixes.plate.get(Materials.Titanium), 'C',
                OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', ItemList.QuantumStar, 'S',
                OrePrefixes.stick.get(Materials.Iridium), 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'C',
                OrePrefixes.circuit.get(Materials.Elite) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Sawblade_Diamond.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " D ", "DGD", " D ", 'D', OrePrefixes.dustSmall.get(Materials.Diamond), 'G',
                OrePrefixes.gearGt.get(Materials.CobaltBrass) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Grinder_Diamond.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "DSD", "SIS", "DSD", 'I', OrePrefixes.gem.get(Materials.Diamond), 'D',
                OrePrefixes.dust.get(Materials.Diamond), 'S', OrePrefixes.plateDouble.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Grinder_Tungsten.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "TST", "SIS", "TST", 'I', OreDictNames.craftingIndustrialDiamond, 'T',
                OrePrefixes.plate.get(Materials.Tungsten), 'S', OrePrefixes.plateDouble.get(Materials.Steel) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Cover_Screen.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "AGA", "RPB", "ALA", 'A', OrePrefixes.plate.get(Materials.Aluminium), 'L',
                OrePrefixes.dust.get(Materials.Glowstone), 'R', Dyes.dyeRed, 'G', Dyes.dyeLime, 'B', Dyes.dyeBlue, 'P',
                OrePrefixes.plate.get(Materials.Glass) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Tool_Scanner.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "EPR", "CSC", "PBP", 'C', OrePrefixes.circuit.get(Materials.Advanced), 'P',
                OrePrefixes.plate.get(Materials.Aluminium), 'E', ItemList.Emitter_MV, 'R', ItemList.Sensor_MV, 'S',
                ItemList.Cover_Screen, 'B', ItemList.Battery_RE_MV_Lithium });

        GT_ModHandler.addCraftingRecipe(
            ItemList.ItemFilter_Export.get(1L),
            new Object[] { "SPS", "dIC", "SPS", 'P', OrePrefixes.plate.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Iron), 'I', ItemList.Component_Filter, 'C',
                ItemList.Conveyor_Module_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.ItemFilter_Import.get(1L),
            new Object[] { "SPS", "CId", "SPS", 'P', OrePrefixes.plate.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Iron), 'I', ItemList.Component_Filter, 'C',
                ItemList.Conveyor_Module_LV });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Tool_Cover_Copy_Paste.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PSP", "PCP", "PBP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'S',
                ItemList.Tool_DataStick.get(1L), 'C', ItemList.Cover_Screen.get(1L), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Emtpy.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Emtpy, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Poison.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Poison_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Poison_Strong.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Strong, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Slowness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Slowness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Weakness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Weakness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Holy_Water.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Holy_Water, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Emtpy.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Emtpy, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Poison.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Poison_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Poison_Strong.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Strong, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Slowness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Slowness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Weakness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Weakness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Holy_Water.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Holy_Water, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });

    }

    private void craftingShapelessRecipes() {

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Coin_Chocolate.get(1L),
            new Object[] { OrePrefixes.dust.get(Materials.Cocoa), OrePrefixes.dust.get(Materials.Milk),
                OrePrefixes.dust.get(Materials.Sugar), OrePrefixes.foil.get(Materials.Gold) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Copper.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Iron });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Iron.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Silver.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Gold.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Platinum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Platinum.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Osmium });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Iron.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper,
                ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper,
                ItemList.Credit_Copper });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Silver.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron,
                ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Gold.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver,
                ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver,
                ItemList.Credit_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Platinum.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold,
                ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Osmium.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum,
                ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum,
                ItemList.Credit_Platinum });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Copper.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Cupronickel });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Cupronickel.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Silver.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Gold.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Platinum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Platinum.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Osmium });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Osmium.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Naquadah });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Naquadah.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Neutronium });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Cupronickel.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper,
                ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper,
                ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Silver.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel,
                ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel,
                ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Gold.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver,
                ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver,
                ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Platinum.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold,
                ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold,
                ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Osmium.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum,
                ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum,
                ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Naquadah.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium,
                ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium,
                ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Neutronium.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah,
                ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah,
                ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_Crafting });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_1by1 });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_2by2 });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_3by3 });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_Dust });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Tool_DataOrb.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Tool_DataOrb.get(1L) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Tool_DataStick.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Tool_DataStick.get(1L) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneTransmitterInternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneTransmitterExternal.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneReceiverInternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneReceiverExternal.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneTransmitterExternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneTransmitterInternal.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneReceiverExternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneReceiverInternal.get(1L) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.ItemFilter_Export.get(1L),
            new Object[] { ItemList.ItemFilter_Import.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.ItemFilter_Import.get(1L),
            new Object[] { ItemList.ItemFilter_Export.get(1L) });

    }

    private void registerBehaviors() {
        IItemBehaviour<GT_MetaBase_Item> behaviourSprayColorRemover = new Behaviour_Spray_Color_Remover(
            ItemList.Spray_Empty.get(1L),
            ItemList.Spray_Color_Used_Remover.get(1L),
            ItemList.Spray_Color_Remover.get(1L),
            1024L);
        addItemBehavior(32000 + Spray_Color_Remover.ID, behaviourSprayColorRemover);
        addItemBehavior(32000 + Spray_Color_Used_Remover.ID, behaviourSprayColorRemover);

        IItemBehaviour<GT_MetaBase_Item> behaviourMatches = new Behaviour_Lighter(
            null,
            ItemList.Tool_Matches.get(1L),
            ItemList.Tool_Matches.get(1L),
            1L);
        addItemBehavior(32000 + Tool_Matches.ID, behaviourMatches);
        IItemBehaviour<GT_MetaBase_Item> behaviourMatchBox = new Behaviour_Lighter(
            null,
            ItemList.Tool_MatchBox_Used.get(1L),
            ItemList.Tool_MatchBox_Full.get(1L),
            16L);
        addItemBehavior(32000 + Tool_MatchBox_Used.ID, behaviourMatchBox);
        addItemBehavior(32000 + Tool_MatchBox_Full.ID, behaviourMatchBox);

        IItemBehaviour<GT_MetaBase_Item> behaviourLighterInvar = new Behaviour_Lighter(
            ItemList.Tool_Lighter_Invar_Empty.get(1L),
            ItemList.Tool_Lighter_Invar_Used.get(1L),
            ItemList.Tool_Lighter_Invar_Full.get(1L),
            100L);
        addItemBehavior(32000 + Tool_Lighter_Invar_Used.ID, behaviourLighterInvar);
        addItemBehavior(32000 + Tool_Lighter_Invar_Full.ID, behaviourLighterInvar);

        IItemBehaviour<GT_MetaBase_Item> behaviourLighterPlatinum = new Behaviour_Lighter(
            ItemList.Tool_Lighter_Platinum_Empty.get(1L),
            ItemList.Tool_Lighter_Platinum_Used.get(1L),
            ItemList.Tool_Lighter_Platinum_Full.get(1L),
            1000L);
        addItemBehavior(32000 + Tool_Lighter_Platinum_Used.ID, behaviourLighterPlatinum);
        addItemBehavior(32000 + Tool_Lighter_Platinum_Full.ID, behaviourLighterPlatinum);

        for (int i = 0; i < 16; i++) {
            IItemBehaviour<GT_MetaBase_Item> behaviourSprayColor = new Behaviour_Spray_Color(
                ItemList.Spray_Empty.get(1L),
                ItemList.SPRAY_CAN_DYES_USED[i].get(1L),
                ItemList.SPRAY_CAN_DYES[i].get(1L),
                512L,
                i);
            addItemBehavior(32000 + Spray_Colors[i], behaviourSprayColor);
            addItemBehavior(32001 + Spray_Colors_Used[i], behaviourSprayColor);
        }
    }

    private void setAllElectricStats() {
        setElectricStats(32000 + Battery_RE_ULV_Tantalum.ID, 1000L, GT_Values.V[0], 0L, -3L, false);
        setElectricStats(32000 + Battery_SU_LV_Sulfuric_Acid.ID, 18000L, GT_Values.V[1], 1L, -2L, true);
        setElectricStats(32000 + Battery_SU_LV_Mercury.ID, 32000L, GT_Values.V[1], 1L, -2L, true);
        setElectricStats(32000 + Battery_RE_LV_Cadmium.ID, 75000L, GT_Values.V[1], 1L, -3L, true);
        setElectricStats(32000 + Battery_RE_LV_Lithium.ID, 100000L, GT_Values.V[1], 1L, -3L, true);
        setElectricStats(32000 + Battery_RE_LV_Sodium.ID, 50000L, GT_Values.V[1], 1L, -3L, true);
        setElectricStats(32000 + Battery_SU_MV_Sulfuric_Acid.ID, 72000L, GT_Values.V[2], 2L, -2L, true);
        setElectricStats(32000 + Battery_SU_MV_Mercury.ID, 128000L, GT_Values.V[2], 2L, -2L, true);
        setElectricStats(32000 + Battery_RE_MV_Cadmium.ID, 300000L, GT_Values.V[2], 2L, -3L, true);
        setElectricStats(32000 + Battery_RE_MV_Lithium.ID, 400000L, GT_Values.V[2], 2L, -3L, true);
        setElectricStats(32000 + Battery_RE_MV_Sodium.ID, 200000L, GT_Values.V[2], 2L, -3L, true);
        setElectricStats(32000 + Battery_SU_HV_Sulfuric_Acid.ID, 288000L, GT_Values.V[3], 3L, -2L, true);
        setElectricStats(32000 + Battery_SU_HV_Mercury.ID, 512000L, GT_Values.V[3], 3L, -2L, true);
        setElectricStats(32000 + Battery_RE_HV_Cadmium.ID, 1200000L, GT_Values.V[3], 3L, -3L, true);
        setElectricStats(32000 + Battery_RE_HV_Lithium.ID, 1600000L, GT_Values.V[3], 3L, -3L, true);
        setElectricStats(32000 + Battery_RE_HV_Sodium.ID, 800000L, GT_Values.V[3], 3L, -3L, true);
        setElectricStats(32000 + Energy_Lapotronic_Orb.ID, 100000000L, GT_Values.V[5], 5L, -3L, true);
        setElectricStats(32000 + ID_MetaItem_01.ZPM.ID, 2000000000000L, GT_Values.V[7], 7L, -2L, true);
        setElectricStats(32000 + Energy_Lapotronic_orb_2.ID, 1000000000L, GT_Values.V[6], 6L, -3L, true);
        setElectricStats(32000 + ZPM2.ID, Long.MAX_VALUE, GT_Values.V[8], 8L, -3L, true);
        setElectricStats(32000 + ZPM3.ID, Long.MAX_VALUE, GT_Values.V[12], 12L, -3L, true);
        setElectricStats(32000 + ZPM4.ID, Long.MAX_VALUE, GT_Values.V[13], 13L, -3L, true);
        setElectricStats(32000 + ZPM5.ID, Long.MAX_VALUE, GT_Values.V[14], 14L, -3L, true);
        setElectricStats(32000 + ZPM6.ID, Long.MAX_VALUE, GT_Values.V[15], 15L, -3L, true);
        setElectricStats(32000 + Energy_Module.ID, 10000000000L, GT_Values.V[7], 7L, -3L, true);
        setElectricStats(32000 + Energy_Cluster.ID, 100000000000L, GT_Values.V[8], 8L, -3L, true);
        setElectricStats(32000 + Tool_Cover_Copy_Paste.ID, 400000L, GT_Values.V[2], 2L, -1L, false);
        setElectricStats(32000 + Tool_Cheat.ID, -2000000000L, 1000000000L, -1L, -3L, false);
        setElectricStats(32000 + Tool_Scanner.ID, 400000L, GT_Values.V[2], 2L, -1L, false);
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

    }
}
