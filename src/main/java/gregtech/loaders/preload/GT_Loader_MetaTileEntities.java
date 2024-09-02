package gregtech.loaders.preload;

import static gregtech.api.enums.MetaTileEntityIDs.*;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_DATA_ACCESS_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_DEBUG_STRUCTURE_WRITTER;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ALLOY_SMELTER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ALLOY_SMELTER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ALLOY_SMELTER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ALLOY_SMELTER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ALLOY_SMELTER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ALLOY_SMELTER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ALLOY_SMELTER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ARC_FURNACE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ARC_FURNACE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ARC_FURNACE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ARC_FURNACE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ARC_FURNACE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ARC_FURNACE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ARC_FURNACE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_LINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOCLAVE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOCLAVE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOCLAVE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOCLAVE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOCLAVE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOCLAVE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOCLAVE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOMATABLE_DATA_ACCESS_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.AUTO_MAINTENANCE_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_4_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_4_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_4_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_4_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_CHARGER_4_BY_4_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BENDING_MACHINE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BENDING_MACHINE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BENDING_MACHINE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BENDING_MACHINE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BENDING_MACHINE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BENDING_MACHINE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BENDING_MACHINE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BETTER_JUKEBOX_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BETTER_JUKEBOX_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BETTER_JUKEBOX_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BETTER_JUKEBOX_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BETTER_JUKEBOX_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BLACKHOLE_COMPRESSOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BRICKED_BLAST_FURNACE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CANNING_MACHINE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.CANNING_MACHINE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.CANNING_MACHINE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CANNING_MACHINE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.CANNING_MACHINE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CANNING_MACHINE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.CANNING_MACHINE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.CENTRIFUGE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.CENTRIFUGE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.CENTRIFUGE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CENTRIFUGE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.CENTRIFUGE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CENTRIFUGE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.CENTRIFUGE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.CHARCOAL_PILE_IGNITER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_BATH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_BATH_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_BATH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_BATH_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_BATH_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_BATH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_BATH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_REACTOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_REACTOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_REACTOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_REACTOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_REACTOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_REACTOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEMICAL_REACTOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.CLEANROOM_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_ENGINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_GENERATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_GENERATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_GENERATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.COMPRESSOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.COMPRESSOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.COMPRESSOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.COMPRESSOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.COMPRESSOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.COMPRESSOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.COMPRESSOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.CONCRETE_BACKFILLER_II_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CONCRETE_BACKFILLER_I_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_ME;
import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_ME_BUS;
import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_SLAVE;
import static gregtech.api.enums.MetaTileEntityIDs.CUTTING_MACHINE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.CUTTING_MACHINE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.CUTTING_MACHINE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CUTTING_MACHINE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.CUTTING_MACHINE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CUTTING_MACHINE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.CUTTING_MACHINE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.DATA_ACCESS_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLATION_TOWER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLERY_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLERY_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLERY_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLERY_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLERY_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLERY_UV;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLERY_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.DTPF_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.DroneDownLink;
import static gregtech.api.enums.MetaTileEntityIDs.Drone_Centre;
import static gregtech.api.enums.MetaTileEntityIDs.EBF_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTRIC_FURNACE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTRIC_FURNACE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTRIC_FURNACE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTRIC_FURNACE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTRIC_FURNACE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTRIC_FURNACE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTRIC_FURNACE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROLYZER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROLYZER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROLYZER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROLYZER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROLYZER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROLYZER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROLYZER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROMAGNETIC_SEPARATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROMAGNETIC_SEPARATOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROMAGNETIC_SEPARATOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROMAGNETIC_SEPARATOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROMAGNETIC_SEPARATOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROMAGNETIC_SEPARATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ELECTROMAGNETIC_SEPARATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRACTOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRACTOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRACTOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRACTOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRACTOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRACTOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRACTOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.EXTREME_COMBUSTION_ENGINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRUDER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRUDER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRUDER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRUDER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRUDER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRUDER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.EXTRUDER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FERMENTER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.FERMENTER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.FERMENTER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.FERMENTER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.FERMENTER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.FERMENTER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.FERMENTER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_CANNER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_CANNER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_CANNER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_CANNER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_CANNER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_CANNER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_CANNER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_EXTRACTOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_EXTRACTOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_EXTRACTOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_EXTRACTOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_EXTRACTOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_EXTRACTOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_EXTRACTOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_HEATER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_HEATER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_HEATER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_HEATER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_HEATER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_HEATER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_HEATER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_SOLIDIFIER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_SOLIDIFIER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_SOLIDIFIER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_SOLIDIFIER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_SOLIDIFIER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_SOLIDIFIER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.FLUID_SOLIDIFIER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FORGE_HAMMER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.FORGE_HAMMER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.FORGE_HAMMER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.FORGE_HAMMER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.FORGE_HAMMER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.FORGE_HAMMER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.FORGE_HAMMER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FORMING_PRESS_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.FORMING_PRESS_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.FORMING_PRESS_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.FORMING_PRESS_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.FORMING_PRESS_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.FORMING_PRESS_UV;
import static gregtech.api.enums.MetaTileEntityIDs.FORMING_PRESS_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.FUSION_CONTROLLER_MKI;
import static gregtech.api.enums.MetaTileEntityIDs.FUSION_CONTROLLER_MKII;
import static gregtech.api.enums.MetaTileEntityIDs.FUSION_CONTROLLER_MKIII;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_EV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_IV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_MV;
import static gregtech.api.enums.MetaTileEntityIDs.HATCH_DEGASIFIER_CONTROL;
import static gregtech.api.enums.MetaTileEntityIDs.HATCH_LENS_HOUSING;
import static gregtech.api.enums.MetaTileEntityIDs.HATCH_LENS_INDICATOR;
import static gregtech.api.enums.MetaTileEntityIDs.HATCH_PH_SENSOR;
import static gregtech.api.enums.MetaTileEntityIDs.HEAT_DETECTOR_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_AMP_TRANSFORMER_MAX_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_AMP_TRANSFORMER_UEV_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_AMP_TRANSFORMER_UIV_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_AMP_TRANSFORMER_UMV_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_AMP_TRANSFORMER_UXV_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_PRESSURE_COAL_BOILER;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_PRESSURE_LAVA_BOILER;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_PRESSURE_SOLAR_BOILER;
import static gregtech.api.enums.MetaTileEntityIDs.HIP_COMPRESSOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.HP_STEAM_ALLOY_SMELTER;
import static gregtech.api.enums.MetaTileEntityIDs.HP_STEAM_COMPRESSOR;
import static gregtech.api.enums.MetaTileEntityIDs.HP_STEAM_EXTRACTOR;
import static gregtech.api.enums.MetaTileEntityIDs.HP_STEAM_FORGE_HAMMER;
import static gregtech.api.enums.MetaTileEntityIDs.HP_STEAM_FURNACE;
import static gregtech.api.enums.MetaTileEntityIDs.HP_STEAM_MACERATOR;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_BRICKED_BRONZE;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_BRONZE;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_EV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_HV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_IV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_LV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_MV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_STEEL;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_WROUGHT_IRON;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.IMPLOSION_COMPRESSOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.INDUSTRIAL_APIARY;
import static gregtech.api.enums.MetaTileEntityIDs.INDUSTRIAL_COMPRESSOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.INDUSTRIAL_LASER_ENGRAVER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_EV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_HV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_IV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_LV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_ME;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_ME_ADVANCED;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_MV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_UV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_BUS_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_ME;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_ME_ADVANCED;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.INTEGRATED_ORE_FACTORY_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_DISTRIBUTOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ITEM_FILTER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_ADVANCED_GAS_TURBINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_BRONZE_BOILER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_GAS_TURBINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_HEAT_EXCHANGER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_HP_STEAM_TURBINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_PLASMA_TURBINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_STEAM_TURBINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_STEEL_BOILER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_TITANIUM_BOILER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LARGE_TUNGSTENSTEEL_BOILER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LASER_ENGRAVER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LATHE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LATHE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.LATHE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.LATHE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.LATHE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.LATHE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.LATHE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LCR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.LIGHTNING_ROD_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LIGHTNING_ROD_HV;
import static gregtech.api.enums.MetaTileEntityIDs.LIGHTNING_ROD_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.LOCKER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.LONG_DISTANCE_PIPELINE_FLUID;
import static gregtech.api.enums.MetaTileEntityIDs.LONG_DISTANCE_PIPELINE_ITEM;
import static gregtech.api.enums.MetaTileEntityIDs.MACERATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MACERATOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.MACERATOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.MACERATOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.MACERATOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.MACERATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MACERATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_CONVERTER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_CONVERTER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_CONVERTER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MAG_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.MAINTENANCE_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_AMPLIFIER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_AMPLIFIER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_AMPLIFIER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_AMPLIFIER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_AMPLIFIER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_AMPLIFIER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_AMPLIFIER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_FABRICATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_FABRICATOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_FABRICATOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_FABRICATOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_FABRICATOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_FABRICATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_FABRICATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_REPLICATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_REPLICATOR_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_REPLICATOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_REPLICATOR_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_REPLICATOR_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_REPLICATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MATTER_REPLICATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MINER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MINER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MINER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MIXER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MIXER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.MIXER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.MIXER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.MIXER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.MIXER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MIXER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MONSTER_REPELLATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MUFFLER_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MULTIBLOCK_PUMP_INFINITE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.MULTIBLOCK_PUMP_MKI_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.MULTILOCK_PUMP_MKIII_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.MULTILOCK_PUMP_MKII_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.MULTILOCK_PUMP_MKIV_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.MULTI_CANNER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.MULTI_LATHE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.MULTI_SMELTER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.NANO_FORGE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.NEUTRONIUM_COMPRESSOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.OIL_CRACKER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKIII_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKII_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKIV_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKI_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_WASHING_PLANT_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_WASHING_PLANT_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_WASHING_PLANT_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_WASHING_PLANT_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_WASHING_PLANT_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_WASHING_PLANT_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_WASHING_PLANT_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_EV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_HV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_IV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_LV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_ME;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_MV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_UV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_BUS_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_ME;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.PACKAGER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.PCB_FACTORY_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_ARC_FURNACE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_ARC_FURNACE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_ARC_FURNACE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_ARC_FURNACE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_ARC_FURNACE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_ARC_FURNACE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_ARC_FURNACE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.POLARIZER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.POLARIZER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.POLARIZER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.POLARIZER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.POLARIZER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.POLARIZER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.POLARIZER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.PRECISION_LASER_ENGRAVER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.PRECISION_LASER_ENGRAVER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.PRECISION_LASER_ENGRAVER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.PRECISION_LASER_ENGRAVER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.PRECISION_LASER_ENGRAVER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.PRECISION_LASER_ENGRAVER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.PRECISION_LASER_ENGRAVER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.PROCESSING_ARRAY_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_EV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_HV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_IV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_LV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_MV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_PLANT_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_CLARIFIER;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_DEGASIFIER;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_FLOCCULATOR;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_OZONATION;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_PARTICLE_EXTRACTOR;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_PH_ADJUSTMENT;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_PLASMA_HEATER;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_UV_TREATMENT;
import static gregtech.api.enums.MetaTileEntityIDs.PYROLYSE_OVEN_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_EV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_IV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_UV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.QUADRUPLE_INPUT_HATCHES_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_CHEST_EV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_CHEST_HV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_CHEST_IV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_CHEST_LV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_CHEST_MV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_TANK_EV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_TANK_HV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_TANK_IV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_TANK_LV;
import static gregtech.api.enums.MetaTileEntityIDs.QUANTUM_TANK_MV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.RECIPE_FILTER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.RECYCLER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.RECYCLER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.RECYCLER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.RECYCLER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.RECYCLER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.RECYCLER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.RECYCLER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.SIFTING_MACHINE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.SIFTING_MACHINE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.SIFTING_MACHINE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.SIFTING_MACHINE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.SIFTING_MACHINE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.SIFTING_MACHINE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.SIFTING_MACHINE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.SIMPLE_SOLAR_BOILER;
import static gregtech.api.enums.MetaTileEntityIDs.SLICING_MACHINE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.SLICING_MACHINE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.SLICING_MACHINE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.SLICING_MACHINE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.SLICING_MACHINE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.SLICING_MACHINE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.SLICING_MACHINE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.SMALL_COAL_BOILER;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_ALLOY_SMELTER;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_COMPRESSOR;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_EXTRACTOR;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_FORGE_HAMMER;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_FURNACE;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_MACERATOR;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_TURBINE_HV;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_TURBINE_LV;
import static gregtech.api.enums.MetaTileEntityIDs.STEAM_TURBINE_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_BUFFER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_CHEST_EV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_CHEST_HV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_CHEST_IV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_CHEST_LV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_CHEST_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_TANK_EV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_TANK_HV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_TANK_IV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_TANK_LV;
import static gregtech.api.enums.MetaTileEntityIDs.SUPER_TANK_MV;
import static gregtech.api.enums.MetaTileEntityIDs.TELEPORTER;
import static gregtech.api.enums.MetaTileEntityIDs.THERMAL_CENTRIFUGE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.THERMAL_CENTRIFUGE_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.THERMAL_CENTRIFUGE_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.THERMAL_CENTRIFUGE_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.THERMAL_CENTRIFUGE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.THERMAL_CENTRIFUGE_UV;
import static gregtech.api.enums.MetaTileEntityIDs.THERMAL_CENTRIFUGE_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.TRANSCENDENT_PLASMA_MIXER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.TRANSFORMER_MAX_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.TRANSFORMER_UEV_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.TRANSFORMER_UIV_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.TRANSFORMER_UMV_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.TRANSFORMER_UXV_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.TURBO_CHARGER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.TYPE_FILTER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.VACUUM_FREEZER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.VOLTAGE_REGULATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_EV_HV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_HV_MV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_IV_EV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_LV_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_LuV_IV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_MAX_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_MV_LV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_UEV_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_UHV_UV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_UIV_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_UMV_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_UV_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_UXV_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.WET_TRANSFORMER_ZPM_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGY_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_EV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_HV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_IV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_LV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_MV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_UV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_UXV;
import static gregtech.api.enums.MetaTileEntityIDs.WIRELESS_HATCH_ENERGY_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.WIREMILL_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.WIREMILL_UEV;
import static gregtech.api.enums.MetaTileEntityIDs.WIREMILL_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.WIREMILL_UIV;
import static gregtech.api.enums.MetaTileEntityIDs.WIREMILL_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.WIREMILL_UV;
import static gregtech.api.enums.MetaTileEntityIDs.WIREMILL_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.WORLD_ACCELERATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.WORMHOLE_GENERATOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_EV_HV;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_HV_MV;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_IV_EV;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_LV_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_LuV_IV;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_MV_LV;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_UHV_UV;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_UV_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.transformer_ZPM_LuV;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.amplifierRecipes;
import static gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fermentingRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.furnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.microwaveRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.plasmaArcFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.polarizerRecipes;
import static gregtech.api.recipe.RecipeMaps.recyclerRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.slicerRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_QuadrupleHumongous;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MagHatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_WetTransformer;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Wireless_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Wireless_Hatch;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_FrameBox;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_ChestBuffer;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_Filter;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_ItemDistributor;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_RecipeFilter;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_Regulator;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_SuperBuffer;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_TypeFilter;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Bronze;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Lava;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Solar;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Solar_Steel;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Steel;
import gregtech.common.tileentities.debug.GT_MetaTileEntity_AdvDebugStructureWriter;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_DieselGenerator;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_GasTurbine;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_LightningRod;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_MagicEnergyConverter;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_MagicalEnergyAbsorber;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_NaquadahReactor;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_PlasmaGenerator;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_SteamTurbine;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_Bronze;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_BronzeBricks;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_Steel;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_SteelBricks;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_CraftingInput_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_CraftingInput_Slave;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Input_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_AdvSeismicProspector;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_BetterJukebox;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Boxinator;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Charger;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Massfabricator;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_MicrowaveEnergyTransmitter;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Miner;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_MonsterRepellent;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_PotionBrewer;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Pump;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Replicator;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_RockBreaker;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Scanner;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Teleporter;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_TurboCharger;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_WorldAccelerator;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineFluid;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineItem;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_AssemblyLine;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_BrickedBlastFurnace;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_Charcoal_Pit;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_Cleanroom;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ConcreteBackfiller1;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ConcreteBackfiller2;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DieselEngine;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DistillationTower;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ExtremeDieselEngine;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer1;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer2;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer3;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_HeatExchanger;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ImplosionCompressor;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_IndustrialElectromagneticSeparator;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_IndustrialExtractor;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_IndustrialLaserEngraver;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_IntegratedOreFactory;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_Bronze;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_Steel;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_Titanium;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_TungstenSteel;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeChemicalReactor;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeFluidExtractor;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Gas;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_GasAdvanced;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_HPSteam;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Plasma;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Steam;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_MultiAutoclave;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_MultiCanner;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_MultiFurnace;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_MultiLathe;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_NanoForge;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OilCracker;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OilDrill1;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OilDrill2;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OilDrill3;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OilDrill4;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OilDrillInfinite;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OreDrillingPlant1;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OreDrillingPlant2;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OreDrillingPlant3;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_OreDrillingPlant4;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PCBFactory;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PlasmaForge;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ProcessingArray;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PyrolyseOven;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_TranscendentPlasmaMixer;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_VacuumFreezer;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_WormholeGenerator;
import gregtech.common.tileentities.machines.multi.compressor.GT_MetaTileEntity_BlackHoleCompressor;
import gregtech.common.tileentities.machines.multi.compressor.GT_MetaTileEntity_HIPCompressor;
import gregtech.common.tileentities.machines.multi.compressor.GT_MetaTileEntity_HeatSensor;
import gregtech.common.tileentities.machines.multi.compressor.GT_MetaTileEntity_IndustrialCompressor;
import gregtech.common.tileentities.machines.multi.compressor.GT_MetaTileEntity_NeutroniumCompressor;
import gregtech.common.tileentities.machines.multi.drone.GT_MetaTileEntity_DroneCentre;
import gregtech.common.tileentities.machines.multi.drone.GT_MetaTileEntity_Hatch_DroneDownLink;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_Hatch_DegasifierControlHatch;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_LensHousing;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_LensIndicator;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitClarifier;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitDegasifier;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitFlocculation;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitOzonation;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitParticleExtractor;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitPhAdjustment;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitPlasmaHeater;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitUVTreatment;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_pHSensor;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_AlloySmelter_Bronze;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_AlloySmelter_Steel;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Compressor_Bronze;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Compressor_Steel;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Extractor_Bronze;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Extractor_Steel;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_ForgeHammer_Bronze;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_ForgeHammer_Steel;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Furnace_Bronze;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Furnace_Steel;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Macerator_Bronze;
import gregtech.common.tileentities.machines.steam.GT_MetaTileEntity_Macerator_Steel;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_Locker;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumChest;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumTank;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperChest;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperTank;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTransformerHiAmp;

// Free IDs left for machines in GT as of 29th of July 2022 - Colen. Please try use them up in order.
// 366
// 367
// 368
// 369
// 370
// 376
// 377
// 378
// 379
// 386
// 387
// 388
// 389
// 390
// 396
// 397
// 398
// 399
// 410
// 419
// 426
// 427
// 428
// 429
// 430
// 436
// 437
// 438
// 439
// 446
// 447
// 448
// 449
// 450
// 456
// 457
// 458
// 459
// 466
// 467
// 468
// 469
// 470
// 476
// 477
// 478
// 479
// 486
// 487
// 488
// 489
// 496
// 497
// 498
// 499
// 506
// 507
// 508
// 509
// 518
// 519
// 526
// 530
// 537
// 538
// 539
// 546
// 547
// 548
// 549
// 550
// 556
// 557
// 558
// 559
// 566
// 567
// 576
// 577
// 578
// 579
// 586
// 587
// 588
// 589
// 590
// 596
// 597
// 598
// 599
// 607
// 608
// 609
// 610
// 616
// 617
// 618
// 619
// 626
// 627
// 628
// 629
// 630
// 636
// 637
// 639
// 646
// 647
// 648
// 649
// 650
// 656
// 657
// 658
// 659
// 666
// 667
// 668
// 669
// 670
// 676
// 677
// 678
// 682
// 683
// 684
// 686
// 687
// 688
// 689
// 702
// 703
// 704
// 705
// 706
// 707
// 708
// 709
// 714
// 715
// 716
// 717
// 718
// 719
// 721
// 722
// 723
// 724
// 725
// 726
// 727
// 728
// 729
// 730
// 731
// 732
// 733
// 734
// 735
// 736
// 737
// 738
// 739
// 741
// 742
// 743
// 744
// 745
// 746
// 747
// 748
// 749

// TODO Some GT MetaTileEntity registrations are done in load/GT_Loader_MetaTileEntities_Recipes.java due to joint
// registration+recipe methods, they should be split and brought here to register all in preload.

public class GT_Loader_MetaTileEntities implements Runnable { // TODO CHECK CIRCUIT RECIPES AND USAGES

    private static final String aTextWire1 = "wire.";
    private static final String aTextCable1 = "cable.";
    private static final String aTextWire2 = " Wire";
    private static final String aTextCable2 = " Cable";
    public static final String imagination = EnumChatFormatting.RESET + "You just need "
        + EnumChatFormatting.DARK_PURPLE
        + "I"
        + EnumChatFormatting.LIGHT_PURPLE
        + "m"
        + EnumChatFormatting.DARK_RED
        + "a"
        + EnumChatFormatting.RED
        + "g"
        + EnumChatFormatting.YELLOW
        + "i"
        + EnumChatFormatting.GREEN
        + "n"
        + EnumChatFormatting.AQUA
        + "a"
        + EnumChatFormatting.DARK_AQUA
        + "t"
        + EnumChatFormatting.BLUE
        + "i"
        + EnumChatFormatting.DARK_BLUE
        + "o"
        + EnumChatFormatting.DARK_PURPLE
        + "n"
        + EnumChatFormatting.RESET
        + " to use this.";

    private static void registerMultiblockControllers() {
        ItemList.Machine_Bricked_BlastFurnace.set(
            new GT_MetaTileEntity_BrickedBlastFurnace(
                BRICKED_BLAST_FURNACE_CONTROLLER.ID,
                "multimachine.brickedblastfurnace",
                "Bricked Blast Furnace").getStackForm(1L));

        ItemList.Machine_Multi_BlastFurnace.set(
            new GT_MetaTileEntity_ElectricBlastFurnace(
                EBF_CONTROLLER.ID,
                "multimachine.blastfurnace",
                "Electric Blast Furnace").getStackForm(1L));
        ItemList.Machine_Multi_ImplosionCompressor.set(
            new GT_MetaTileEntity_ImplosionCompressor(
                IMPLOSION_COMPRESSOR_CONTROLLER.ID,
                "multimachine.implosioncompressor",
                "Implosion Compressor").getStackForm(1L));
        ItemList.Machine_Multi_VacuumFreezer.set(
            new GT_MetaTileEntity_VacuumFreezer(
                VACUUM_FREEZER_CONTROLLER.ID,
                "multimachine.vacuumfreezer",
                "Vacuum Freezer").getStackForm(1L));
        ItemList.Machine_Multi_Furnace.set(
            new GT_MetaTileEntity_MultiFurnace(
                MULTI_SMELTER_CONTROLLER.ID,
                "multimachine.multifurnace",
                "Multi Smelter").getStackForm(1L));
        ItemList.Machine_Multi_PlasmaForge.set(
            new GT_MetaTileEntity_PlasmaForge(
                DTPF_CONTROLLER.ID,
                "multimachine.plasmaforge",
                "Dimensionally Transcendent Plasma Forge").getStackForm(1L));
        ItemList.Machine_Multi_PurificationPlant.set(
            new GT_MetaTileEntity_PurificationPlant(
                PURIFICATION_PLANT_CONTROLLER.ID,
                "multimachine.purificationplant",
                "Water Purification Plant").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitClarifier.set(
            new GT_MetaTileEntity_PurificationUnitClarifier(
                PURIFICATION_UNIT_CLARIFIER.ID,
                "multimachine.purificationunitclarifier",
                "Clarifier Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitFlocculator.set(
            new GT_MetaTileEntity_PurificationUnitFlocculation(
                PURIFICATION_UNIT_FLOCCULATOR.ID,
                "multimachine.purificationunitflocculator",
                "Flocculation Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitPhAdjustment.set(
            new GT_MetaTileEntity_PurificationUnitPhAdjustment(
                PURIFICATION_UNIT_PH_ADJUSTMENT.ID,
                "multimachine.purificationunitphadjustment",
                "pH Neutralization Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitOzonation.set(
            new GT_MetaTileEntity_PurificationUnitOzonation(
                PURIFICATION_UNIT_OZONATION.ID,
                "multimachine.purificationunitozonation",
                "Ozonation Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitPlasmaHeater.set(
            new GT_MetaTileEntity_PurificationUnitPlasmaHeater(
                PURIFICATION_UNIT_PLASMA_HEATER.ID,
                "multimachine.purificationunitplasmaheater",
                "Extreme Temperature Fluctuation Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitUVTreatment.set(
            new GT_MetaTileEntity_PurificationUnitUVTreatment(
                PURIFICATION_UNIT_UV_TREATMENT.ID,
                "multimachine.purificationunituvtreatment",
                "High Energy Laser Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitDegasifier.set(
            new GT_MetaTileEntity_PurificationUnitDegasifier(
                PURIFICATION_UNIT_DEGASIFIER.ID,
                "multimachine.purificationunitdegasifier",
                "Residual Decontaminant Degasser Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitParticleExtractor.set(
            new GT_MetaTileEntity_PurificationUnitParticleExtractor(
                PURIFICATION_UNIT_PARTICLE_EXTRACTOR.ID,
                "multimachine.purificationunitextractor",
                "Absolute Baryonic Perfection Purification Unit").getStackForm(1L));
        ItemList.Hatch_DegasifierControl.set(
            new GT_MetaTileEntity_Hatch_DegasifierControlHatch(
                HATCH_DEGASIFIER_CONTROL.ID,
                "hatch.degasifiercontrol",
                "Degasser Control Hatch",
                8).getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Bronze.set(
            new GT_MetaTileEntity_LargeBoiler_Bronze(
                LARGE_BRONZE_BOILER_CONTROLLER.ID,
                "multimachine.boiler.bronze",
                "Large Bronze Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Steel.set(
            new GT_MetaTileEntity_LargeBoiler_Steel(
                LARGE_STEEL_BOILER_CONTROLLER.ID,
                "multimachine.boiler.steel",
                "Large Steel Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Titanium.set(
            new GT_MetaTileEntity_LargeBoiler_Titanium(
                LARGE_TITANIUM_BOILER_CONTROLLER.ID,
                "multimachine.boiler.titanium",
                "Large Titanium Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_TungstenSteel.set(
            new GT_MetaTileEntity_LargeBoiler_TungstenSteel(
                LARGE_TUNGSTENSTEEL_BOILER_CONTROLLER.ID,
                "multimachine.boiler.tungstensteel",
                "Large Tungstensteel Boiler").getStackForm(1L));
        ItemList.FusionComputer_LuV.set(
            new GT_MetaTileEntity_FusionComputer1(
                FUSION_CONTROLLER_MKI.ID,
                "fusioncomputer.tier.06",
                "Fusion Control Computer Mark I").getStackForm(1L));
        ItemList.FusionComputer_ZPMV.set(
            new GT_MetaTileEntity_FusionComputer2(
                FUSION_CONTROLLER_MKII.ID,
                "fusioncomputer.tier.07",
                "Fusion Control Computer Mark II").getStackForm(1L));
        ItemList.FusionComputer_UV.set(
            new GT_MetaTileEntity_FusionComputer3(
                FUSION_CONTROLLER_MKIII.ID,
                "fusioncomputer.tier.08",
                "Fusion Control Computer Mark III").getStackForm(1L));

        ItemList.Processing_Array.set(
            new GT_MetaTileEntity_ProcessingArray(
                PROCESSING_ARRAY_CONTROLLER.ID,
                "multimachine.processingarray",
                "Processing Array").getStackForm(1L));
        ItemList.Distillation_Tower.set(
            new GT_MetaTileEntity_DistillationTower(
                DISTILLATION_TOWER_CONTROLLER.ID,
                "multimachine.distillationtower",
                "Distillation Tower").getStackForm(1L));
        ItemList.Ore_Processor.set(
            new GT_MetaTileEntity_IntegratedOreFactory(
                INTEGRATED_ORE_FACTORY_CONTROLLER.ID,
                "multimachine.oreprocessor",
                "Integrated Ore Factory").getStackForm(1L));

        ItemList.LargeSteamTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_Steam(
                LARGE_STEAM_TURBINE_CONTROLLER.ID,
                "multimachine.largeturbine",
                "Large Steam Turbine").getStackForm(1L));
        ItemList.LargeGasTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_Gas(
                LARGE_GAS_TURBINE_CONTROLLER.ID,
                "multimachine.largegasturbine",
                "Large Gas Turbine").getStackForm(1L));
        ItemList.LargeHPSteamTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_HPSteam(
                LARGE_HP_STEAM_TURBINE_CONTROLLER.ID,
                "multimachine.largehpturbine",
                "Large HP Steam Turbine").getStackForm(1L));
        ItemList.LargeAdvancedGasTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_GasAdvanced(
                LARGE_ADVANCED_GAS_TURBINE_CONTROLLER.ID,
                "multimachine.largeadvancedgasturbine",
                "Large Advanced Gas Turbine").getStackForm(1L));
        ItemList.Machine_Multi_TranscendentPlasmaMixer.set(
            new GT_MetaTileEntity_TranscendentPlasmaMixer(
                TRANSCENDENT_PLASMA_MIXER_CONTROLLER.ID,
                "multimachine.transcendentplasmamixer",
                "Transcendent Plasma Mixer").getStackForm(1));

        ItemList.LargePlasmaTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_Plasma(
                LARGE_PLASMA_TURBINE_CONTROLLER.ID,
                "multimachine.largeplasmaturbine",
                "Large Plasma Generator").getStackForm(1L));
        ItemList.Machine_Multi_HeatExchanger.set(
            new GT_MetaTileEntity_HeatExchanger(
                LARGE_HEAT_EXCHANGER_CONTROLLER.ID,
                "multimachine.heatexchanger",
                "Large Heat Exchanger").getStackForm(1L));
        ItemList.Charcoal_Pile.set(
            new GT_MetaTileEntity_Charcoal_Pit(
                CHARCOAL_PILE_IGNITER_CONTROLLER.ID,
                "multimachine.charcoalpile",
                "Charcoal Pile Igniter").getStackForm(1));

        // Converter recipes in case you had old one lying around
        ItemList.OilDrill1.set(
            new GT_MetaTileEntity_OilDrill1(
                MULTIBLOCK_PUMP_MKI_CONTROLLER.ID,
                "multimachine.oildrill1",
                "Oil/Gas/Fluid Drilling Rig").getStackForm(1));
        ItemList.OilDrill2.set(
            new GT_MetaTileEntity_OilDrill2(
                MULTILOCK_PUMP_MKII_CONTROLLER.ID,
                "multimachine.oildrill2",
                "Oil/Gas/Fluid Drilling Rig II").getStackForm(1));
        ItemList.OilDrill3.set(
            new GT_MetaTileEntity_OilDrill3(
                MULTILOCK_PUMP_MKIII_CONTROLLER.ID,
                "multimachine.oildrill3",
                "Oil/Gas/Fluid Drilling Rig III").getStackForm(1));
        ItemList.OilDrill4.set(
            new GT_MetaTileEntity_OilDrill4(
                MULTILOCK_PUMP_MKIV_CONTROLLER.ID,
                "multimachine.oildrill4",
                "Oil/Gas/Fluid Drilling Rig IV").getStackForm(1));
        ItemList.OilDrillInfinite.set(
            new GT_MetaTileEntity_OilDrillInfinite(
                MULTIBLOCK_PUMP_INFINITE_CONTROLLER.ID,
                "multimachine.oildrillinfinite",
                "Infinite Oil/Gas/Fluid Drilling Rig").getStackForm(1));

        ItemList.ConcreteBackfiller1.set(
            new GT_MetaTileEntity_ConcreteBackfiller1(
                CONCRETE_BACKFILLER_I_CONTROLLER.ID,
                "multimachine.concretebackfiller1",
                "Concrete Backfiller").getStackForm(1));
        ItemList.ConcreteBackfiller2.set(
            new GT_MetaTileEntity_ConcreteBackfiller2(
                CONCRETE_BACKFILLER_II_CONTROLLER.ID,
                "multimachine.concretebackfiller3",
                "Advanced Concrete Backfiller").getStackForm(1));
        ItemList.OreDrill1.set(
            new GT_MetaTileEntity_OreDrillingPlant1(
                ORE_DRILL_MKI_CONTROLLER.ID,
                "multimachine.oredrill1",
                "Ore Drilling Plant").getStackForm(1));
        ItemList.OreDrill2.set(
            new GT_MetaTileEntity_OreDrillingPlant2(
                ORE_DRILL_MKII_CONTROLLER.ID,
                "multimachine.oredrill2",
                "Ore Drilling Plant II").getStackForm(1));
        ItemList.OreDrill3.set(
            new GT_MetaTileEntity_OreDrillingPlant3(
                ORE_DRILL_MKIII_CONTROLLER.ID,
                "multimachine.oredrill3",
                "Ore Drilling Plant III").getStackForm(1));
        ItemList.OreDrill4.set(
            new GT_MetaTileEntity_OreDrillingPlant4(
                ORE_DRILL_MKIV_CONTROLLER.ID,
                "multimachine.oredrill4",
                "Ore Drilling Plant IV").getStackForm(1));

        ItemList.PyrolyseOven.set(
            new GT_MetaTileEntity_PyrolyseOven(PYROLYSE_OVEN_CONTROLLER.ID, "multimachine.pyro", "Pyrolyse Oven")
                .getStackForm(1));
        ItemList.OilCracker.set(
            new GT_MetaTileEntity_OilCracker(OIL_CRACKER_CONTROLLER.ID, "multimachine.cracker", "Oil Cracking Unit")
                .getStackForm(1));

        ItemList.Machine_Multi_Assemblyline.set(
            new GT_MetaTileEntity_AssemblyLine(
                ASSEMBLING_LINE_CONTROLLER.ID,
                "multimachine.assemblyline",
                "Assembling Line").getStackForm(1L));
        ItemList.Machine_Multi_DieselEngine.set(
            new GT_MetaTileEntity_DieselEngine(
                COMBUSTION_ENGINE_CONTROLLER.ID,
                "multimachine.dieselengine",
                "Combustion Engine").getStackForm(1L));
        ItemList.Machine_Multi_ExtremeDieselEngine.set(
            new GT_MetaTileEntity_ExtremeDieselEngine(
                EXTREME_COMBUSTION_ENGINE_CONTROLLER.ID,
                "multimachine.extremedieselengine",
                "Extreme Combustion Engine").getStackForm(1L));
        ItemList.Machine_Multi_Cleanroom.set(
            new GT_MetaTileEntity_Cleanroom(CLEANROOM_CONTROLLER.ID, "multimachine.cleanroom", "Cleanroom Controller")
                .getStackForm(1));

        ItemList.Machine_Multi_LargeChemicalReactor.set(
            new GT_MetaTileEntity_LargeChemicalReactor(
                LCR_CONTROLLER.ID,
                "multimachine.chemicalreactor",
                "Large Chemical Reactor").getStackForm(1));
        ItemList.PCBFactory.set(
            new GT_MetaTileEntity_PCBFactory(PCB_FACTORY_CONTROLLER.ID, "multimachine.pcbfactory", "PCB Factory")
                .getStackForm(1));
        ItemList.NanoForge.set(
            new GT_MetaTileEntity_NanoForge(NANO_FORGE_CONTROLLER.ID, "multimachine.nanoforge", "Nano Forge")
                .getStackForm(1));
        ItemList.Machine_Multi_DroneCentre.set(
            new GT_MetaTileEntity_DroneCentre(Drone_Centre.ID, "multimachine_DroneCentre", "Drone Centre")
                .getStackForm(1));

        ItemList.Machine_Multi_IndustrialElectromagneticSeparator.set(
            new GT_MetaTileEntity_IndustrialElectromagneticSeparator(
                INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR_CONTROLLER.ID,
                "multimachine.electromagneticseparator",
                "Magnetic Flux Exhibitor").getStackForm(1));
        ItemList.Machine_Multi_Canner.set(
            new GT_MetaTileEntity_MultiCanner(MULTI_CANNER_CONTROLLER.ID, "multimachine.canner", "TurboCan Pro")
                .getStackForm(1));

        ItemList.WormholeGenerator.set(
            new GT_MetaTileEntity_WormholeGenerator(
                WORMHOLE_GENERATOR_CONTROLLER.ID,
                "multimachine.wormhole",
                "Miniature Wormhole Generator").getStackForm(1));

        ItemList.Machine_Multi_IndustrialLaserEngraver.set(
            new GT_MetaTileEntity_IndustrialLaserEngraver(
                INDUSTRIAL_LASER_ENGRAVER_CONTROLLER.ID,
                "multimachine.engraver",
                "Hyper-Intensity Laser Engraver").getStackForm(1));

        ItemList.Machine_Multi_IndustrialExtractor.set(
            new GT_MetaTileEntity_IndustrialExtractor(
                INDUSTRIAL_EXTRACTOR_CONTROLLER.ID,
                "multimachine.extractor",
                "Dissection Apparatus").getStackForm(1));

        ItemList.Machine_Multi_Lathe.set(
            new GT_MetaTileEntity_MultiLathe(
                MULTI_LATHE_CONTROLLER.ID,
                "multimachine.lathe",
                "Industrial Precision Lathe").getStackForm(1));

        ItemList.Machine_Multi_IndustrialCompressor.set(
            new GT_MetaTileEntity_IndustrialCompressor(
                INDUSTRIAL_COMPRESSOR_CONTROLLER.ID,
                "multimachine.basiccompressor",
                "Large Electric Compressor").getStackForm(1));
        ItemList.Machine_Multi_HIPCompressor.set(
            new GT_MetaTileEntity_HIPCompressor(
                HIP_COMPRESSOR_CONTROLLER.ID,
                "multimachine.hipcompressor",
                "Hot Isostatic Pressurization Unit").getStackForm(1));
        ItemList.Machine_Multi_NeutroniumCompressor.set(
            new GT_MetaTileEntity_NeutroniumCompressor(
                NEUTRONIUM_COMPRESSOR_CONTROLLER.ID,
                "multimachine.neutroniumcompressor",
                "Neutronium Compressor").getStackForm(1));
        ItemList.Machine_Multi_BlackHoleCompressor.set(
            new GT_MetaTileEntity_BlackHoleCompressor(
                BLACKHOLE_COMPRESSOR_CONTROLLER.ID,
                "multimachine.blackholecompressor",
                "Semi-Stable Black Hole Containment Field").getStackForm(1));

        ItemList.Machine_Multi_Autoclave.set(
            new GT_MetaTileEntity_MultiAutoclave(
                MULTI_AUTOCLAVE_CONTROLLER.ID,
                "multimachine.autoclave",
                "Industrial Autoclave").getStackForm(1));

        ItemList.LargeFluidExtractor.set(
            new GT_MetaTileEntity_LargeFluidExtractor(
                LARGE_FLUID_EXTRACTOR.ID,
                "multimachine.fluidextractor",
                "Large Fluid Extractor").getStackForm(1));
    }

    private static void registerSteamMachines() {
        ItemList.Machine_Bronze_Furnace.set(
            new GT_MetaTileEntity_Furnace_Bronze(STEAM_FURNACE.ID, "bronzemachine.furnace", "Steam Furnace")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Macerator.set(
            new GT_MetaTileEntity_Macerator_Bronze(STEAM_MACERATOR.ID, "bronzemachine.macerator", "Steam Macerator")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Extractor.set(
            new GT_MetaTileEntity_Extractor_Bronze(STEAM_EXTRACTOR.ID, "bronzemachine.extractor", "Steam Extractor")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Hammer.set(
            new GT_MetaTileEntity_ForgeHammer_Bronze(
                STEAM_FORGE_HAMMER.ID,
                "bronzemachine.hammer",
                "Steam Forge Hammer").getStackForm(1L));
        ItemList.Machine_Bronze_Compressor.set(
            new GT_MetaTileEntity_Compressor_Bronze(STEAM_COMPRESSOR.ID, "bronzemachine.compressor", "Steam Compressor")
                .getStackForm(1L));
        ItemList.Machine_Bronze_AlloySmelter.set(
            new GT_MetaTileEntity_AlloySmelter_Bronze(
                STEAM_ALLOY_SMELTER.ID,
                "bronzemachine.alloysmelter",
                "Steam Alloy Smelter").getStackForm(1L));

    }

    private static void registerUnpackager() {
        ItemList.Machine_LV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_LV.ID,
                "basicmachine.unboxinator.tier.01",
                "Basic Unpackager",
                1,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));

        ItemList.Machine_MV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_MV.ID,
                "basicmachine.unboxinator.tier.02",
                "Advanced Unpackager",
                2,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));

        ItemList.Machine_HV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_HV.ID,
                "basicmachine.unboxinator.tier.03",
                "Advanced Unpackager II",
                3,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));

        ItemList.Machine_EV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_EV.ID,
                "basicmachine.unboxinator.tier.04",
                "Advanced Unpackager III",
                4,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));

        ItemList.Machine_IV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_IV.ID,
                "basicmachine.unboxinator.tier.05",
                "Unboxinator",
                5,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));

        ItemList.Machine_LuV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_LuV.ID,
                "basicmachine.unboxinator.tier.06",
                "Unboxinator",
                6,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));

        ItemList.Machine_ZPM_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_ZPM.ID,
                "basicmachine.unboxinator.tier.07",
                "Unboxinator",
                7,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));

        ItemList.Machine_UV_Unboxinator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                UNPACKAGER_UV.ID,
                "basicmachine.unboxinator.tier.08",
                "Unboxinator",
                8,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "UNBOXINATOR",
                null).getStackForm(1L));
    }

    private static void registerAssemblingMachine() {

        ItemList.Machine_LV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLER_LV.ID,
                "basicmachine.assembler.tier.01",
                "Basic Assembling Machine",
                1,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLER_MV.ID,
                "basicmachine.assembler.tier.02",
                "Advanced Assembling Machine",
                2,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLER_HV.ID,
                "basicmachine.assembler.tier.03",
                "Advanced Assembling Machine II",
                3,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLER_EV.ID,
                "basicmachine.assembler.tier.04",
                "Advanced Assembling Machine III",
                4,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Assembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLER_IV.ID,
                "basicmachine.assembler.tier.05",
                "Advanced Assembling Machine IV",
                5,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.AssemblingMachineLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLING_MACHINE_LuV.ID,
                "basicmachine.assembler.tier.06",
                "Elite Assembling Machine",
                6,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.AssemblingMachineZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLING_MACHINE_ZPM.ID,
                "basicmachine.assembler.tier.07",
                "Elite Assembling Machine II",
                7,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.AssemblingMachineUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLING_MACHINE_UV.ID,
                "basicmachine.assembler.tier.08",
                "Ultimate Assembly Constructor",
                8,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.AssemblingMachineUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLING_MACHINE_UHV.ID,
                "basicmachine.assembler.tier.09",
                "Epic Assembly Constructor",
                9,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.AssemblingMachineUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLING_MACHINE_UEV.ID,
                "basicmachine.assembler.tier.10",
                "Epic Assembly Constructor II",
                10,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.AssemblingMachineUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLING_MACHINE_UIV.ID,
                "basicmachine.assembler.tier.11",
                "Epic Assembly Constructor III",
                11,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));

        ItemList.AssemblingMachineUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ASSEMBLING_MACHINE_UMV.ID,
                "basicmachine.assembler.tier.12",
                "Epic Assembly Constructor IV",
                12,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ASSEMBLER",
                null).getStackForm(1L));
    }

    private static void registerMatterAmplifier() {
        ItemList.Machine_LV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_LV.ID,
                "basicmachine.amplifab.tier.01",
                "Basic Amplifabricator",
                1,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.Machine_MV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_MV.ID,
                "basicmachine.amplifab.tier.02",
                "Advanced Amplifabricator",
                2,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.Machine_HV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_HV.ID,
                "basicmachine.amplifab.tier.03",
                "Advanced Amplifabricator II",
                3,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.Machine_EV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_EV.ID,
                "basicmachine.amplifab.tier.04",
                "Advanced Amplifabricator III",
                4,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.Machine_IV_Amplifab.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_IV.ID,
                "basicmachine.amplifab.tier.05",
                "Advanced Amplifabricator IV",
                5,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.AmplifabricatorLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_LuV.ID,
                "basicmachine.amplifab.tier.06",
                "Elite Amplifabricator",
                6,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.AmplifabricatorZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_ZPM.ID,
                "basicmachine.amplifab.tier.07",
                "Elite Amplifabricator II",
                7,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.AmplifabricatorUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_UV.ID,
                "basicmachine.amplifab.tier.08",
                "Ultimate Amplicreator",
                8,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.AmplifabricatorUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_UHV.ID,
                "basicmachine.amplifab.tier.09",
                "Epic Amplicreator",
                9,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.AmplifabricatorUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_UEV.ID,
                "basicmachine.amplifab.tier.10",
                "Epic Amplicreator II",
                10,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.AmplifabricatorUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_UIV.ID,
                "basicmachine.amplifab.tier.11",
                "Epic Amplicreator III",
                11,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));

        ItemList.AmplifabricatorUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MATTER_AMPLIFIER_UMV.ID,
                "basicmachine.amplifab.tier.12",
                "Epic Amplicreator IV",
                12,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AMPLIFAB",
                null).getStackForm(1L));
    }

    private static void registerAlloySmelter() {
        ItemList.Machine_LV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_LV.ID,
                "basicmachine.alloysmelter.tier.01",
                "Basic Alloy Smelter",
                1,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.Machine_MV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_MV.ID,
                "basicmachine.alloysmelter.tier.02",
                "Advanced Alloy Smelter",
                2,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.Machine_HV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_HV.ID,
                "basicmachine.alloysmelter.tier.03",
                "Advanced Alloy Smelter II",
                3,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.Machine_EV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_EV.ID,
                "basicmachine.alloysmelter.tier.04",
                "Advanced Alloy Smelter III",
                4,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.Machine_IV_AlloySmelter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_IV.ID,
                "basicmachine.alloysmelter.tier.05",
                "Advanced Alloy Smelter IV",
                5,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.AlloySmelterLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_LuV.ID,
                "basicmachine.alloysmelter.tier.06",
                "Elite Alloy Smelter",
                6,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.AlloySmelterZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_ZPM.ID,
                "basicmachine.alloysmelter.tier.07",
                "Elite Alloy Smelter II",
                7,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.AlloySmelterUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_UV.ID,
                "basicmachine.alloysmelter.tier.08",
                "Ultimate Alloy Integrator",
                8,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.AlloySmelterUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_UHV.ID,
                "basicmachine.alloysmelter.tier.09",
                "Epic Alloy Integrator",
                9,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.AlloySmelterUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_UEV.ID,
                "basicmachine.alloysmelter.tier.10",
                "Epic Alloy Integrator II",
                10,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.AlloySmelterUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_UIV.ID,
                "basicmachine.alloysmelter.tier.11",
                "Epic Alloy Integrator III",
                11,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));

        ItemList.AlloySmelterUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ALLOY_SMELTER_UMV.ID,
                "basicmachine.alloysmelter.tier.12",
                "Epic Alloy Integrator IV",
                12,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ALLOY_SMELTER",
                null).getStackForm(1L));
    }

    private static void registerHPSteamMachines() {
        ItemList.Machine_HP_Extractor.set(
            new GT_MetaTileEntity_Extractor_Steel(
                HP_STEAM_EXTRACTOR.ID,
                "hpmachine.extractor",
                "High Pressure Extractor").getStackForm(1L));
        ItemList.Machine_HP_Furnace.set(
            new GT_MetaTileEntity_Furnace_Steel(HP_STEAM_FURNACE.ID, "hpmachine.furnace", "High Pressure Furnace")
                .getStackForm(1L));
        ItemList.Machine_HP_Macerator.set(
            new GT_MetaTileEntity_Macerator_Steel(
                HP_STEAM_MACERATOR.ID,
                "hpmachine.macerator",
                "High Pressure Macerator").getStackForm(1L));
        ItemList.Machine_HP_Hammer.set(
            new GT_MetaTileEntity_ForgeHammer_Steel(
                HP_STEAM_FORGE_HAMMER.ID,
                "hpmachine.hammer",
                "High Pressure Forge Hammer").getStackForm(1L));
        ItemList.Machine_HP_Compressor.set(
            new GT_MetaTileEntity_Compressor_Steel(
                HP_STEAM_COMPRESSOR.ID,
                "hpmachine.compressor",
                "High Pressure Compressor").getStackForm(1L));
        ItemList.Machine_HP_AlloySmelter.set(
            new GT_MetaTileEntity_AlloySmelter_Steel(
                HP_STEAM_ALLOY_SMELTER.ID,
                "hpmachine.alloysmelter",
                "High Pressure Alloy Smelter").getStackForm(1L));
    }

    private static void registerLocker() {
        ItemList.Locker_ULV.set(
            new GT_MetaTileEntity_Locker(LOCKER_ULV.ID, "locker.tier.00", "Ultra Low Voltage Locker", 0)
                .getStackForm(1L));
        ItemList.Locker_LV.set(
            new GT_MetaTileEntity_Locker(LOCKER_LV.ID, "locker.tier.01", "Low Voltage Locker", 1).getStackForm(1L));
        ItemList.Locker_MV.set(
            new GT_MetaTileEntity_Locker(LOCKER_MV.ID, "locker.tier.02", "Medium Voltage Locker", 2).getStackForm(1L));
        ItemList.Locker_HV.set(
            new GT_MetaTileEntity_Locker(LOCKER_HV.ID, "locker.tier.03", "High Voltage Locker", 3).getStackForm(1L));
        ItemList.Locker_EV.set(
            new GT_MetaTileEntity_Locker(LOCKER_EV.ID, "locker.tier.04", "Extreme Voltage Locker", 4).getStackForm(1L));
        ItemList.Locker_IV.set(
            new GT_MetaTileEntity_Locker(LOCKER_IV.ID, "locker.tier.05", "Insane Voltage Locker", 5).getStackForm(1L));
        ItemList.Locker_LuV.set(
            new GT_MetaTileEntity_Locker(LOCKER_LuV.ID, "locker.tier.06", "Ludicrous Voltage Locker", 6)
                .getStackForm(1L));
        ItemList.Locker_ZPM.set(
            new GT_MetaTileEntity_Locker(LOCKER_ZPM.ID, "locker.tier.07", "ZPM Voltage Locker", 7).getStackForm(1L));
        ItemList.Locker_UV.set(
            new GT_MetaTileEntity_Locker(LOCKER_UV.ID, "locker.tier.08", "Ultimate Voltage Locker", 8)
                .getStackForm(1L));
        ItemList.Locker_MAX.set(
            new GT_MetaTileEntity_Locker(LOCKER_UHV.ID, "locker.tier.09", "Highly Ultimate Voltage Locker", 9)
                .getStackForm(1L));
    }

    private static void registerScanner() {
        ItemList.Machine_LV_Scanner.set(
            new GT_MetaTileEntity_Scanner(SCANNER_LV.ID, "basicmachine.scanner.tier.01", "Basic Scanner", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Scanner.set(
            new GT_MetaTileEntity_Scanner(SCANNER_MV.ID, "basicmachine.scanner.tier.02", "Advanced Scanner", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Scanner.set(
            new GT_MetaTileEntity_Scanner(SCANNER_HV.ID, "basicmachine.scanner.tier.03", "Advanced Scanner II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Scanner.set(
            new GT_MetaTileEntity_Scanner(SCANNER_EV.ID, "basicmachine.scanner.tier.04", "Advanced Scanner III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Scanner.set(
            new GT_MetaTileEntity_Scanner(SCANNER_IV.ID, "basicmachine.scanner.tier.05", "Advanced Scanner IV", 5)
                .getStackForm(1L));
        ItemList.ScannerLuV.set(
            new GT_MetaTileEntity_Scanner(SCANNER_LuV.ID, "basicmachine.scanner.tier.06", "Elite Scanner", 6)
                .getStackForm(1L));
        ItemList.ScannerZPM.set(
            new GT_MetaTileEntity_Scanner(SCANNER_ZPM.ID, "basicmachine.scanner.tier.07", "Elite Scanner II", 7)
                .getStackForm(1L));
        ItemList.ScannerUV.set(
            new GT_MetaTileEntity_Scanner(
                SCANNER_UV.ID,
                "basicmachine.scanner.tier.08",
                "Ultimate Electron Microscope",
                8).getStackForm(1L));
        ItemList.ScannerUHV.set(
            new GT_MetaTileEntity_Scanner(SCANNER_UHV.ID, "basicmachine.scanner.tier.09", "Epic Electron Microscope", 9)
                .getStackForm(1L));
        ItemList.ScannerUEV.set(
            new GT_MetaTileEntity_Scanner(
                SCANNER_UEV.ID,
                "basicmachine.scanner.tier.10",
                "Epic Electron Microscope II",
                10).getStackForm(1L));
        ItemList.ScannerUIV.set(
            new GT_MetaTileEntity_Scanner(
                SCANNER_UIV.ID,
                "basicmachine.scanner.tier.11",
                "Epic Electron Microscope III",
                11).getStackForm(1L));
        ItemList.ScannerUMV.set(
            new GT_MetaTileEntity_Scanner(
                SCANNER_UMV.ID,
                "basicmachine.scanner.tier.12",
                "Epic Electron Microscope IV",
                12).getStackForm(1L));
    }

    private static void registerPackager() {
        ItemList.Machine_LV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(PACKAGER_LV.ID, "basicmachine.boxinator.tier.01", "Basic Packager", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(PACKAGER_MV.ID, "basicmachine.boxinator.tier.02", "Advanced Packager", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(PACKAGER_HV.ID, "basicmachine.boxinator.tier.03", "Advanced Packager II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(
                PACKAGER_EV.ID,
                "basicmachine.boxinator.tier.04",
                "Advanced Packager III",
                4).getStackForm(1L));
        ItemList.Machine_IV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(PACKAGER_IV.ID, "basicmachine.boxinator.tier.05", "Boxinator", 5)
                .getStackForm(1L));
        ItemList.Machine_LuV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(PACKAGER_LuV.ID, "basicmachine.boxinator.tier.06", "Boxinator", 6)
                .getStackForm(1L));
        ItemList.Machine_ZPM_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(PACKAGER_ZPM.ID, "basicmachine.boxinator.tier.07", "Boxinator", 7)
                .getStackForm(1L));
        ItemList.Machine_UV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(PACKAGER_UV.ID, "basicmachine.boxinator.tier.08", "Boxinator", 8)
                .getStackForm(1L));
    }

    private static void registerRockBreaker() {
        ItemList.Machine_LV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_LV.ID,
                "basicmachine.rockbreaker.tier.01",
                "Basic Rock Breaker",
                1).getStackForm(1L));
        ItemList.Machine_MV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_MV.ID,
                "basicmachine.rockbreaker.tier.02",
                "Advanced Rock Breaker",
                2).getStackForm(1L));
        ItemList.Machine_HV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_HV.ID,
                "basicmachine.rockbreaker.tier.03",
                "Advanced Rock Breaker II",
                3).getStackForm(1L));
        ItemList.Machine_EV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_EV.ID,
                "basicmachine.rockbreaker.tier.04",
                "Advanced Rock Breaker III",
                4).getStackForm(1L));
        ItemList.Machine_IV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_IV.ID,
                "basicmachine.rockbreaker.tier.05",
                "Cryogenic Magma Solidifier R-8200",
                5).getStackForm(1L));
        ItemList.RockBreakerLuV.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_LuV.ID,
                "rockbreaker.tier.06",
                "Cryogenic Magma Solidifier R-9200",
                6).getStackForm(1L));

        ItemList.RockBreakerZPM.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_ZPM.ID,
                "rockbreaker.tier.07",
                "Cryogenic Magma Solidifier R-10200",
                7).getStackForm(1L));

        ItemList.RockBreakerUV.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_UV.ID,
                "rockbreaker.tier.08",
                "Cryogenic Magma Solidifier R-11200",
                8).getStackForm(1L));

        ItemList.RockBreakerUHV.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_UHV.ID,
                "rockbreaker.tier.09",
                "Cryogenic Magma Solidifier R-12200",
                9).getStackForm(1L));

        ItemList.RockBreakerUEV.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_UEV.ID,
                "rockbreaker.tier.10",
                "Cryogenic Magma Solidifier R-13200",
                10).getStackForm(1L));

        ItemList.RockBreakerUIV.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_UIV.ID,
                "rockbreaker.tier.11",
                "Cryogenic Magma Solidifier R-14200",
                11).getStackForm(1L));

        ItemList.RockBreakerUMV.set(
            new GT_MetaTileEntity_RockBreaker(
                ROCK_BREAKER_UMV.ID,
                "rockbreaker.tier.12",
                "Cryogenic Magma Solidifier R-15200",
                12).getStackForm(1L));
    }

    private static void registerIndustrialApiary() {
        if (Forestry.isModLoaded()) {
            ItemList.Machine_IndustrialApiary.set(
                new GT_MetaTileEntity_IndustrialApiary(
                    INDUSTRIAL_APIARY.ID,
                    "basicmachine.industrialapiary",
                    "Industrial Apiary",
                    8).getStackForm(1L));
        }
    }

    private static void registerMassFab() {
        ItemList.Machine_LV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(
                MASS_FABRICATOR_LV.ID,
                "basicmachine.massfab.tier.01",
                "Basic Mass Fabricator",
                1).getStackForm(1L));
        ItemList.Machine_MV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(
                MASS_FABRICATOR_MV.ID,
                "basicmachine.massfab.tier.02",
                "Advanced Mass Fabricator",
                2).getStackForm(1L));
        ItemList.Machine_HV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(
                MASS_FABRICATOR_HV.ID,
                "basicmachine.massfab.tier.03",
                "Advanced Mass Fabricator II",
                3).getStackForm(1L));
        ItemList.Machine_EV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(
                MASS_FABRICATOR_EV.ID,
                "basicmachine.massfab.tier.04",
                "Advanced Mass Fabricator III",
                4).getStackForm(1L));
        ItemList.Machine_IV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(
                MASS_FABRICATOR_IV.ID,
                "basicmachine.massfab.tier.05",
                "Advanced Mass Fabricator IV",
                5).getStackForm(1L));
    }

    private static void registerReplicator() {
        ItemList.Machine_LV_Replicator.set(
            new GT_MetaTileEntity_Replicator(REPLICATOR_LV.ID, "basicmachine.replicator.tier.01", "Basic Replicator", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Replicator.set(
            new GT_MetaTileEntity_Replicator(
                REPLICATOR_MV.ID,
                "basicmachine.replicator.tier.02",
                "Advanced Replicator",
                2).getStackForm(1L));
        ItemList.Machine_HV_Replicator.set(
            new GT_MetaTileEntity_Replicator(
                REPLICATOR_HV.ID,
                "basicmachine.replicator.tier.03",
                "Advanced Replicator II",
                3).getStackForm(1L));
        ItemList.Machine_EV_Replicator.set(
            new GT_MetaTileEntity_Replicator(
                REPLICATOR_EV.ID,
                "basicmachine.replicator.tier.04",
                "Advanced Replicator III",
                4).getStackForm(1L));
        ItemList.Machine_IV_Replicator.set(
            new GT_MetaTileEntity_Replicator(
                REPLICATOR_IV.ID,
                "basicmachine.replicator.tier.05",
                "Advanced Replicator IV",
                5).getStackForm(1L));

        ItemList.ReplicatorLuV.set(
            new GT_MetaTileEntity_Replicator(
                MATTER_REPLICATOR_LuV.ID,
                "basicmachine.replicator.tier.06",
                "Elite Replicator",
                6).getStackForm(1L));
        ItemList.ReplicatorZPM.set(
            new GT_MetaTileEntity_Replicator(
                MATTER_REPLICATOR_ZPM.ID,
                "basicmachine.replicator.tier.07",
                "Elite Replicator II",
                7).getStackForm(1L));
        ItemList.ReplicatorUV.set(
            new GT_MetaTileEntity_Replicator(
                MATTER_REPLICATOR_UV.ID,
                "basicmachine.replicator.tier.08",
                "Ultimate Elemental Composer",
                8).getStackForm(1L));
        ItemList.ReplicatorUHV.set(
            new GT_MetaTileEntity_Replicator(
                MATTER_REPLICATOR_UHV.ID,
                "basicmachine.replicator.tier.09",
                "Epic Elemental Composer",
                9).getStackForm(1L));
        ItemList.ReplicatorUEV.set(
            new GT_MetaTileEntity_Replicator(
                MATTER_REPLICATOR_UEV.ID,
                "basicmachine.replicator.tier.10",
                "Epic Elemental Composer II",
                10).getStackForm(1L));
        ItemList.ReplicatorUIV.set(
            new GT_MetaTileEntity_Replicator(
                MATTER_REPLICATOR_UIV.ID,
                "basicmachine.replicator.tier.11",
                "Epic Elemental Composer III",
                11).getStackForm(1L));
        ItemList.ReplicatorUMV.set(
            new GT_MetaTileEntity_Replicator(
                MATTER_REPLICATOR_UMV.ID,
                "basicmachine.replicator.tier.12",
                "Epic Elemental Composer IV",
                12).getStackForm(1L));
    }

    private static void registerBrewery() {
        ItemList.Machine_LV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_LV.ID, "basicmachine.brewery.tier.01", "Basic Brewery", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_MV.ID, "basicmachine.brewery.tier.02", "Advanced Brewery", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_HV.ID, "basicmachine.brewery.tier.03", "Advanced Brewery II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_EV.ID, "basicmachine.brewery.tier.04", "Advanced Brewery III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_IV.ID, "basicmachine.brewery.tier.05", "Advanced Brewery IV", 5)
                .getStackForm(1L));

        ItemList.BreweryLuV.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_LuV.ID, "basicmachine.brewery.tier.06", "Elite Brewery", 6)
                .getStackForm(1L));
        ItemList.BreweryZPM.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_ZPM.ID, "basicmachine.brewery.tier.07", "Elite Brewery II", 7)
                .getStackForm(1L));
        ItemList.BreweryUV.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_UV.ID, "basicmachine.brewery.tier.08", "Ultimate Brew Rusher", 8)
                .getStackForm(1L));
        ItemList.BreweryUHV.set(
            new GT_MetaTileEntity_PotionBrewer(BREWERY_UHV.ID, "basicmachine.brewery.tier.09", "Epic Brew Rusher", 9)
                .getStackForm(1L));
        ItemList.BreweryUEV.set(
            new GT_MetaTileEntity_PotionBrewer(
                BREWERY_UEV.ID,
                "basicmachine.brewery.tier.10",
                "Epic Brew Rusher II",
                10).getStackForm(1L));
        ItemList.BreweryUIV.set(
            new GT_MetaTileEntity_PotionBrewer(
                BREWERY_UIV.ID,
                "basicmachine.brewery.tier.11",
                "Epic Brew Rusher III",
                11).getStackForm(1L));
        ItemList.BreweryUMV.set(
            new GT_MetaTileEntity_PotionBrewer(
                BREWERY_UMV.ID,
                "basicmachine.brewery.tier.12",
                "Epic Brew Rusher IV",
                12).getStackForm(1L));
    }

    private static void registerMiner() {
        ItemList.Machine_LV_Miner.set(
            new GT_MetaTileEntity_Miner(MINER_LV.ID, "basicmachine.miner.tier.01", "Basic Miner", 1).getStackForm(1L));
        ItemList.Machine_MV_Miner.set(
            new GT_MetaTileEntity_Miner(MINER_MV.ID, "basicmachine.miner.tier.02", "Good Miner", 2).getStackForm(1L));
        ItemList.Machine_HV_Miner.set(
            new GT_MetaTileEntity_Miner(MINER_HV.ID, "basicmachine.miner.tier.03", "Advanced Miner", 3)
                .getStackForm(1L));
    }

    private static void registerPump() {
        ItemList.Pump_LV
            .set(new GT_MetaTileEntity_Pump(PUMP_LV.ID, "basicmachine.pump.tier.01", "Basic Pump", 1).getStackForm(1L));
        ItemList.Pump_MV.set(
            new GT_MetaTileEntity_Pump(PUMP_MV.ID, "basicmachine.pump.tier.02", "Advanced Pump", 2).getStackForm(1L));
        ItemList.Pump_HV.set(
            new GT_MetaTileEntity_Pump(PUMP_HV.ID, "basicmachine.pump.tier.03", "Advanced Pump II", 3)
                .getStackForm(1L));
        ItemList.Pump_EV.set(
            new GT_MetaTileEntity_Pump(PUMP_EV.ID, "basicmachine.pump.tier.04", "Advanced Pump III", 4)
                .getStackForm(1L));
        ItemList.Pump_IV.set(
            new GT_MetaTileEntity_Pump(PUMP_IV.ID, "basicmachine.pump.tier.05", "Advanced Pump IV", 5)
                .getStackForm(1L));
        ItemList.PumpLuV.set(
            new GT_MetaTileEntity_Pump(PUMP_LuV.ID, "basicmachine.pump.tier.06", "Lake Dislocator", 6)
                .getStackForm(1L));
        ItemList.PumpZPM.set(
            new GT_MetaTileEntity_Pump(PUMP_ZPM.ID, "basicmachine.pump.tier.07", "Ocean Transposer", 7)
                .getStackForm(1L));
    }

    private static void registerTeleporter() {
        ItemList.Teleporter.set(
            new GT_MetaTileEntity_Teleporter(TELEPORTER.ID, "basicmachine.teleporter", "Teleporter", 9)
                .getStackForm(1L));
    }

    private static void registerMonsterRepellator() {
        ItemList.MobRep_LV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_LV.ID,
                "basicmachine.mobrep.tier.01",
                "Basic Monster Repellator",
                1).getStackForm(1L));
        ItemList.MobRep_MV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_MV.ID,
                "basicmachine.mobrep.tier.02",
                "Advanced Monster Repellator",
                2).getStackForm(1L));
        ItemList.MobRep_HV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_HV.ID,
                "basicmachine.mobrep.tier.03",
                "Advanced Monster Repellator II",
                3).getStackForm(1L));
        ItemList.MobRep_EV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_EV.ID,
                "basicmachine.mobrep.tier.04",
                "Advanced Monster Repellator III",
                4).getStackForm(1L));
        ItemList.MobRep_IV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_IV.ID,
                "basicmachine.mobrep.tier.05",
                "Advanced Monster Repellator IV",
                5).getStackForm(1L));
        ItemList.MobRep_LuV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_LuV.ID,
                "basicmachine.mobrep.tier.06",
                "Advanced Monster Repellator V",
                6).getStackForm(1L));
        ItemList.MobRep_ZPM.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_ZPM.ID,
                "basicmachine.mobrep.tier.07",
                "Advanced Monster Repellator VI",
                7).getStackForm(1L));
        ItemList.MobRep_UV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                MONSTER_REPELLATOR_UV.ID,
                "basicmachine.mobrep.tier.08",
                "Advanced Monster Repellator VII",
                8).getStackForm(1L));
    }

    private void registerWorldAccelerator() {
        ItemList.AcceleratorLV.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_LV.ID,
                "basicmachine.accelerator.tier.01",
                "Basic World Accelerator",
                1).getStackForm(1L));
        ItemList.AcceleratorMV.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_MV.ID,
                "basicmachine.accelerator.tier.02",
                "Advanced World Accelerator",
                2).getStackForm(1L));
        ItemList.AcceleratorHV.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_HV.ID,
                "basicmachine.accelerator.tier.03",
                "Advanced World Accelerator II",
                3).getStackForm(1L));
        ItemList.AcceleratorEV.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_EV.ID,
                "basicmachine.accelerator.tier.04",
                "Advanced World Accelerator III",
                4).getStackForm(1L));
        ItemList.AcceleratorIV.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_IV.ID,
                "basicmachine.accelerator.tier.05",
                "Advanced World Accelerator IV",
                5).getStackForm(1L));
        ItemList.AcceleratorLuV.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_LuV.ID,
                "basicmachine.accelerator.tier.06",
                "Elite World Accelerator",
                6).getStackForm(1L));
        ItemList.AcceleratorZPM.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_ZPM.ID,
                "basicmachine.accelerator.tier.07",
                "Elite World Accelerator II",
                7).getStackForm(1L));
        ItemList.AcceleratorUV.set(
            new GT_MetaTileEntity_WorldAccelerator(
                WORLD_ACCELERATOR_UV.ID,
                "basicmachine.accelerator.tier.08",
                "Ultimate Time Anomaly",
                8).getStackForm(1L));

    }

    private static void registerAdvancedSeismicProspector() {
        ItemList.Seismic_Prospector_Adv_LV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_LV.ID,
                "basicmachine.seismicprospector.07",
                "Advanced Seismic Prospector LV",
                1,
                5 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_MV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_MV.ID,
                "basicmachine.seismicprospector.06",
                "Advanced Seismic Prospector MV",
                2,
                7 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_HV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_HV.ID,
                "basicmachine.seismicprospector.05",
                "Advanced Seismic Prospector HV",
                3,
                9 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_EV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_EV.ID,
                "basicmachine.seismicprospector.04",
                "Advanced Seismic Prospector EV",
                4,
                11 * 16 / 2,
                2).getStackForm(1));
    }

    private static void registerMicrowaveEnergyTransmitter() {
        ItemList.MicroTransmitter_HV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_HV.ID,
                "basicmachine.microtransmitter.03",
                "HV Microwave Energy Transmitter",
                3).getStackForm(1L));
        ItemList.MicroTransmitter_EV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_EV.ID,
                "basicmachine.microtransmitter.04",
                "EV Microwave Energy Transmitter",
                4).getStackForm(1L));
        ItemList.MicroTransmitter_IV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_IV.ID,
                "basicmachine.microtransmitter.05",
                "IV Microwave Energy Transmitter",
                5).getStackForm(1L));
        ItemList.MicroTransmitter_LUV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_LuV.ID,
                "basicmachine.microtransmitter.06",
                "LuV Microwave Energy Transmitter",
                6).getStackForm(1L));
        ItemList.MicroTransmitter_ZPM.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_ZPM.ID,
                "basicmachine.microtransmitter.07",
                "ZPM Microwave Energy Transmitter",
                7).getStackForm(1L));
        ItemList.MicroTransmitter_UV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_UV.ID,
                "basicmachine.microtransmitter.08",
                "UV Microwave Energy Transmitter",
                8).getStackForm(1L));
    }

    private static void registerBetterJukebox() {
        ItemList.BetterJukebox_LV.set(
            new GT_MetaTileEntity_BetterJukebox(
                BETTER_JUKEBOX_LV.ID,
                "basicmachine.betterjukebox.tier.01",
                "Basic Electric Jukebox",
                1).getStackForm(1L));
        ItemList.BetterJukebox_MV.set(
            new GT_MetaTileEntity_BetterJukebox(
                BETTER_JUKEBOX_MV.ID,
                "basicmachine.betterjukebox.tier.02",
                "Advanced Electric Jukebox",
                2).getStackForm(1L));
        ItemList.BetterJukebox_HV.set(
            new GT_MetaTileEntity_BetterJukebox(
                BETTER_JUKEBOX_HV.ID,
                "basicmachine.betterjukebox.tier.03",
                "Advanced Electric Jukebox II",
                3).getStackForm(1L));
        ItemList.BetterJukebox_EV.set(
            new GT_MetaTileEntity_BetterJukebox(
                BETTER_JUKEBOX_EV.ID,
                "basicmachine.betterjukebox.tier.04",
                "Extreme Music Mixer",
                4).getStackForm(1L));
        ItemList.BetterJukebox_IV.set(
            new GT_MetaTileEntity_BetterJukebox(
                BETTER_JUKEBOX_IV.ID,
                "basicmachine.betterjukebox.tier.05",
                "Duke Mix'em 3D",
                5).getStackForm(1L));
    }

    private static void registerChestBuffer() {
        ItemList.Automation_ChestBuffer_ULV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_ULV.ID,
                "automation.chestbuffer.tier.00",
                "Ultra Low Voltage Chest Buffer",
                0).getStackForm(1L));
        ItemList.Automation_ChestBuffer_LV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_LV.ID,
                "automation.chestbuffer.tier.01",
                "Low Voltage Chest Buffer",
                1).getStackForm(1L));
        ItemList.Automation_ChestBuffer_MV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_MV.ID,
                "automation.chestbuffer.tier.02",
                "Medium Voltage Chest Buffer",
                2).getStackForm(1L));
        ItemList.Automation_ChestBuffer_HV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_HV.ID,
                "automation.chestbuffer.tier.03",
                "High Voltage Chest Buffer",
                3).getStackForm(1L));
        ItemList.Automation_ChestBuffer_EV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_EV.ID,
                "automation.chestbuffer.tier.04",
                "Extreme Voltage Chest Buffer",
                4).getStackForm(1L));
        ItemList.Automation_ChestBuffer_IV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_IV.ID,
                "automation.chestbuffer.tier.05",
                "Insane Voltage Chest Buffer",
                5).getStackForm(1L));
        ItemList.Automation_ChestBuffer_LuV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_LuV.ID,
                "automation.chestbuffer.tier.06",
                "Ludicrous Voltage Chest Buffer",
                6).getStackForm(1L));
        ItemList.Automation_ChestBuffer_ZPM.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_ZPM.ID,
                "automation.chestbuffer.tier.07",
                "ZPM Voltage Chest Buffer",
                7).getStackForm(1L));
        ItemList.Automation_ChestBuffer_UV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_UV.ID,
                "automation.chestbuffer.tier.08",
                "Ultimate Voltage Chest Buffer",
                8).getStackForm(1L));
        ItemList.Automation_ChestBuffer_UHV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_UHV.ID,
                "automation.chestbuffer.tier.09",
                "Highly Ultimate Voltage Chest Buffer",
                9).getStackForm(1L));

        ItemList.Automation_ChestBuffer_UEV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_UEV.ID,
                "automation.chestbuffer.tier.10",
                "Ultra High Voltage Chest Buffer",
                10).getStackForm(1L));

        ItemList.Automation_ChestBuffer_UIV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_UIV.ID,
                "automation.chestbuffer.tier.11",
                "UIV Voltage Chest Buffer",
                11).getStackForm(1L));

        ItemList.Automation_ChestBuffer_UMV.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_UMV.ID,
                "automation.chestbuffer.tier.12",
                "UMV Voltage Chest Buffer",
                12).getStackForm(1L));
    }

    private static void registerItemFilter() {
        ItemList.Automation_Filter_ULV.set(
            new GT_MetaTileEntity_Filter(
                ITEM_FILTER_ULV.ID,
                "automation.filter.tier.00",
                "Ultra Low Voltage Item Filter",
                0).getStackForm(1L));
        ItemList.Automation_Filter_LV.set(
            new GT_MetaTileEntity_Filter(ITEM_FILTER_LV.ID, "automation.filter.tier.01", "Low Voltage Item Filter", 1)
                .getStackForm(1L));
        ItemList.Automation_Filter_MV.set(
            new GT_MetaTileEntity_Filter(
                ITEM_FILTER_MV.ID,
                "automation.filter.tier.02",
                "Medium Voltage Item Filter",
                2).getStackForm(1L));
        ItemList.Automation_Filter_HV.set(
            new GT_MetaTileEntity_Filter(ITEM_FILTER_HV.ID, "automation.filter.tier.03", "High Voltage Item Filter", 3)
                .getStackForm(1L));
        ItemList.Automation_Filter_EV.set(
            new GT_MetaTileEntity_Filter(
                ITEM_FILTER_EV.ID,
                "automation.filter.tier.04",
                "Extreme Voltage Item Filter",
                4).getStackForm(1L));
        ItemList.Automation_Filter_IV.set(
            new GT_MetaTileEntity_Filter(
                ITEM_FILTER_IV.ID,
                "automation.filter.tier.05",
                "Insane Voltage Item Filter",
                5).getStackForm(1L));
        ItemList.Automation_Filter_LuV.set(
            new GT_MetaTileEntity_Filter(
                ITEM_FILTER_LuV.ID,
                "automation.filter.tier.06",
                "Ludicrous Voltage Item Filter",
                6).getStackForm(1L));
        ItemList.Automation_Filter_ZPM.set(
            new GT_MetaTileEntity_Filter(ITEM_FILTER_ZPM.ID, "automation.filter.tier.07", "ZPM Voltage Item Filter", 7)
                .getStackForm(1L));
        ItemList.Automation_Filter_UV.set(
            new GT_MetaTileEntity_Filter(
                ITEM_FILTER_UV.ID,
                "automation.filter.tier.08",
                "Ultimate Voltage Item Filter",
                8).getStackForm(1L));
        ItemList.Automation_Filter_MAX.set(
            new GT_MetaTileEntity_Filter(
                ITEM_FILTER_UHV.ID,
                "automation.filter.tier.09",
                "Highly Ultimate Voltage Item Filter",
                9).getStackForm(1L));
    }

    private static void registerTypeFilter() {
        ItemList.Automation_TypeFilter_ULV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_ULV.ID,
                "automation.typefilter.tier.00",
                "Ultra Low Voltage Type Filter",
                0).getStackForm(1L));
        ItemList.Automation_TypeFilter_LV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_LV.ID,
                "automation.typefilter.tier.01",
                "Low Voltage Type Filter",
                1).getStackForm(1L));
        ItemList.Automation_TypeFilter_MV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_MV.ID,
                "automation.typefilter.tier.02",
                "Medium Voltage Type Filter",
                2).getStackForm(1L));
        ItemList.Automation_TypeFilter_HV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_HV.ID,
                "automation.typefilter.tier.03",
                "High Voltage Type Filter",
                3).getStackForm(1L));
        ItemList.Automation_TypeFilter_EV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_EV.ID,
                "automation.typefilter.tier.04",
                "Extreme Voltage Type Filter",
                4).getStackForm(1L));
        ItemList.Automation_TypeFilter_IV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_IV.ID,
                "automation.typefilter.tier.05",
                "Insane Voltage Type Filter",
                5).getStackForm(1L));
        ItemList.Automation_TypeFilter_LuV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_LuV.ID,
                "automation.typefilter.tier.06",
                "Ludicrous Voltage Type Filter",
                6).getStackForm(1L));
        ItemList.Automation_TypeFilter_ZPM.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_ZPM.ID,
                "automation.typefilter.tier.07",
                "ZPM Voltage Type Filter",
                7).getStackForm(1L));
        ItemList.Automation_TypeFilter_UV.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_UV.ID,
                "automation.typefilter.tier.08",
                "Ultimate Voltage Type Filter",
                8).getStackForm(1L));
        ItemList.Automation_TypeFilter_MAX.set(
            new GT_MetaTileEntity_TypeFilter(
                TYPE_FILTER_UHV.ID,
                "automation.typefilter.tier.09",
                "Highly Ultimate Voltage Type Filter",
                9).getStackForm(1L));
    }

    private static void registerRegulator() {
        ItemList.Automation_Regulator_ULV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_ULV.ID,
                "automation.regulator.tier.00",
                "Ultra Low Voltage Regulator",
                0).getStackForm(1L));
        ItemList.Automation_Regulator_LV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_LV.ID,
                "automation.regulator.tier.01",
                "Low Voltage Regulator",
                1).getStackForm(1L));
        ItemList.Automation_Regulator_MV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_MV.ID,
                "automation.regulator.tier.02",
                "Medium Voltage Regulator",
                2).getStackForm(1L));
        ItemList.Automation_Regulator_HV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_HV.ID,
                "automation.regulator.tier.03",
                "High Voltage Regulator",
                3).getStackForm(1L));
        ItemList.Automation_Regulator_EV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_EV.ID,
                "automation.regulator.tier.04",
                "Extreme Voltage Regulator",
                4).getStackForm(1L));
        ItemList.Automation_Regulator_IV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_IV.ID,
                "automation.regulator.tier.05",
                "Insane Voltage Regulator",
                5).getStackForm(1L));
        ItemList.Automation_Regulator_LuV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_LuV.ID,
                "automation.regulator.tier.06",
                "Ludicrous Voltage Regulator",
                6).getStackForm(1L));
        ItemList.Automation_Regulator_ZPM.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_ZPM.ID,
                "automation.regulator.tier.07",
                "ZPM Voltage Regulator",
                7).getStackForm(1L));
        ItemList.Automation_Regulator_UV.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_UV.ID,
                "automation.regulator.tier.08",
                "Ultimate Voltage Regulator",
                8).getStackForm(1L));
        ItemList.Automation_Regulator_MAX.set(
            new GT_MetaTileEntity_Regulator(
                VOLTAGE_REGULATOR_UHV.ID,
                "automation.regulator.tier.09",
                "Highly Ultimate Voltage Regulator",
                9).getStackForm(1L));
    }

    private static void registerSuperBuffer() {
        ItemList.Automation_SuperBuffer_ULV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_ULV.ID,
                "automation.superbuffer.tier.00",
                "Ultra Low Voltage Super Buffer",
                0).getStackForm(1L));
        ItemList.Automation_SuperBuffer_LV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_LV.ID,
                "automation.superbuffer.tier.01",
                "Low Voltage Super Buffer",
                1).getStackForm(1L));
        ItemList.Automation_SuperBuffer_MV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_MV.ID,
                "automation.superbuffer.tier.02",
                "Medium Voltage Super Buffer",
                2).getStackForm(1L));
        ItemList.Automation_SuperBuffer_HV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_HV.ID,
                "automation.superbuffer.tier.03",
                "High Voltage Super Buffer",
                3).getStackForm(1L));
        ItemList.Automation_SuperBuffer_EV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_EV.ID,
                "automation.superbuffer.tier.04",
                "Extreme Voltage Super Buffer",
                4).getStackForm(1L));
        ItemList.Automation_SuperBuffer_IV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_IV.ID,
                "automation.superbuffer.tier.05",
                "Insane Voltage Super Buffer",
                5).getStackForm(1L));
        ItemList.Automation_SuperBuffer_LuV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_LuV.ID,
                "automation.superbuffer.tier.06",
                "Ludicrous Voltage Super Buffer",
                6).getStackForm(1L));
        ItemList.Automation_SuperBuffer_ZPM.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_ZPM.ID,
                "automation.superbuffer.tier.07",
                "ZPM Voltage Super Buffer",
                7).getStackForm(1L));
        ItemList.Automation_SuperBuffer_UV.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_UV.ID,
                "automation.superbuffer.tier.08",
                "Ultimate Voltage Super Buffer",
                8).getStackForm(1L));
        ItemList.Automation_SuperBuffer_MAX.set(
            new GT_MetaTileEntity_SuperBuffer(
                SUPER_BUFFER_UHV.ID,
                "automation.superbuffer.tier.09",
                "Highly Ultimate Voltage Super Buffer",
                9).getStackForm(1L));
    }

    private static void registerItemDistributor() {
        ItemList.Automation_ItemDistributor_ULV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_ULV.ID,
                "automation.itemdistributor.tier.00",
                "Ultra Low Voltage Item Distributor",
                0).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_LV.ID,
                "automation.itemdistributor.tier.01",
                "Low Voltage Item Distributor",
                1).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_MV.ID,
                "automation.itemdistributor.tier.02",
                "Medium Voltage Item Distributor",
                2).getStackForm(1L));
        ItemList.Automation_ItemDistributor_HV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_HV.ID,
                "automation.itemdistributor.tier.03",
                "High Voltage Item Distributor",
                3).getStackForm(1L));
        ItemList.Automation_ItemDistributor_EV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_EV.ID,
                "automation.itemdistributor.tier.04",
                "Extreme Voltage Item Distributor",
                4).getStackForm(1L));
        ItemList.Automation_ItemDistributor_IV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_IV.ID,
                "automation.itemdistributor.tier.05",
                "Insane Voltage Item Distributor",
                5).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LuV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_LuV.ID,
                "automation.itemdistributor.tier.06",
                "Ludicrous Voltage Item Distributor",
                6).getStackForm(1L));
        ItemList.Automation_ItemDistributor_ZPM.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_ZPM.ID,
                "automation.itemdistributor.tier.07",
                "ZPM Voltage Item Distributor",
                7).getStackForm(1L));
        ItemList.Automation_ItemDistributor_UV.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_UV.ID,
                "automation.itemdistributor.tier.08",
                "Ultimate Voltage Item Distributor",
                8).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MAX.set(
            new GT_MetaTileEntity_ItemDistributor(
                ITEM_DISTRIBUTOR_UHV.ID,
                "automation.itemdistributor.tier.09",
                "MAX Voltage Item Distributor",
                9).getStackForm(1L));
    }

    private static void registerRecipeFilter() {
        ItemList.Automation_RecipeFilter_ULV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_ULV.ID,
                "automation.recipefilter.tier.00",
                "Ultra Low Voltage Recipe Filter",
                0).getStackForm(1L));
        ItemList.Automation_RecipeFilter_LV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_LV.ID,
                "automation.recipefilter.tier.01",
                "Low Voltage Recipe Filter",
                1).getStackForm(1L));
        ItemList.Automation_RecipeFilter_MV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_MV.ID,
                "automation.recipefilter.tier.02",
                "Medium Voltage Recipe Filter",
                2).getStackForm(1L));
        ItemList.Automation_RecipeFilter_HV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_HV.ID,
                "automation.recipefilter.tier.03",
                "High Voltage Recipe Filter",
                3).getStackForm(1L));
        ItemList.Automation_RecipeFilter_EV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_EV.ID,
                "automation.recipefilter.tier.04",
                "Extreme Voltage Recipe Filter",
                4).getStackForm(1L));
        ItemList.Automation_RecipeFilter_IV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_IV.ID,
                "automation.recipefilter.tier.05",
                "Insane Voltage Recipe Filter",
                5).getStackForm(1L));
        ItemList.Automation_RecipeFilter_LuV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_LuV.ID,
                "automation.recipefilter.tier.06",
                "Ludicrous Voltage Recipe Filter",
                6).getStackForm(1L));
        ItemList.Automation_RecipeFilter_ZPM.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_ZPM.ID,
                "automation.recipefilter.tier.07",
                "ZPM Voltage Recipe Filter",
                7).getStackForm(1L));
        ItemList.Automation_RecipeFilter_UV.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_UV.ID,
                "automation.recipefilter.tier.08",
                "Ultimate Voltage Recipe Filter",
                8).getStackForm(1L));
        ItemList.Automation_RecipeFilter_MAX.set(
            new GT_MetaTileEntity_RecipeFilter(
                RECIPE_FILTER_UHV.ID,
                "automation.recipefilter.tier.09",
                "Highly Ultimate Voltage Recipe Filter",
                9).getStackForm(1L));
    }

    private static void registerMachineHull() {
        ItemList.Hull_Bronze.set(
            new GT_MetaTileEntity_BasicHull_Bronze(
                HULL_BRONZE.ID,
                "hull.bronze",
                "Bronze Hull",
                0,
                "For your first Steam Machines").getStackForm(1L));
        ItemList.Hull_Bronze_Bricks.set(
            new GT_MetaTileEntity_BasicHull_BronzeBricks(
                HULL_BRICKED_BRONZE.ID,
                "hull.bronze_bricked",
                "Bricked Bronze Hull",
                0,
                "For your first Steam Machines").getStackForm(1L));
        ItemList.Hull_HP.set(
            new GT_MetaTileEntity_BasicHull_Steel(
                HULL_STEEL.ID,
                "hull.steel",
                "Steel Hull",
                0,
                "For improved Steam Machines").getStackForm(1L));
        ItemList.Hull_HP_Bricks.set(
            new GT_MetaTileEntity_BasicHull_SteelBricks(
                HULL_WROUGHT_IRON.ID,
                "hull.steel_bricked",
                "Bricked Wrought Iron Hull",
                0,
                "For improved Steam Machines").getStackForm(1L));

        ItemList.Hull_ULV.set(
            new GT_MetaTileEntity_BasicHull(HULL_ULV.ID, "hull.tier.00", "ULV Machine Hull", 0, imagination)
                .getStackForm(1L));
        ItemList.Hull_LV.set(
            new GT_MetaTileEntity_BasicHull(HULL_LV.ID, "hull.tier.01", "LV Machine Hull", 1, imagination)
                .getStackForm(1L));
        ItemList.Hull_MV.set(
            new GT_MetaTileEntity_BasicHull(HULL_MV.ID, "hull.tier.02", "MV Machine Hull", 2, imagination)
                .getStackForm(1L));
        ItemList.Hull_HV.set(
            new GT_MetaTileEntity_BasicHull(HULL_HV.ID, "hull.tier.03", "HV Machine Hull", 3, imagination)
                .getStackForm(1L));
        ItemList.Hull_EV.set(
            new GT_MetaTileEntity_BasicHull(HULL_EV.ID, "hull.tier.04", "EV Machine Hull", 4, imagination)
                .getStackForm(1L));
        ItemList.Hull_IV.set(
            new GT_MetaTileEntity_BasicHull(HULL_IV.ID, "hull.tier.05", "IV Machine Hull", 5, imagination)
                .getStackForm(1L));
        ItemList.Hull_LuV.set(
            new GT_MetaTileEntity_BasicHull(HULL_LuV.ID, "hull.tier.06", "LuV Machine Hull", 6, imagination)
                .getStackForm(1L));
        ItemList.Hull_ZPM.set(
            new GT_MetaTileEntity_BasicHull(HULL_ZPM.ID, "hull.tier.07", "ZPM Machine Hull", 7, imagination)
                .getStackForm(1L));
        ItemList.Hull_UV.set(
            new GT_MetaTileEntity_BasicHull(HULL_UV.ID, "hull.tier.08", "UV Machine Hull", 8, imagination)
                .getStackForm(1L));
        ItemList.Hull_MAX.set(
            new GT_MetaTileEntity_BasicHull(HULL_UHV.ID, "hull.tier.09", "UHV Machine Hull", 9, imagination)
                .getStackForm(1L));

        ItemList.Hull_UEV.set(
            new GT_MetaTileEntity_BasicHull(
                HULL_UEV.ID,
                "hull.tier.10",
                "UEV Machine Hull",
                10,
                GT_Loader_MetaTileEntities.imagination).getStackForm(1L));

        ItemList.Hull_UIV.set(
            new GT_MetaTileEntity_BasicHull(
                HULL_UIV.ID,
                "hull.tier.11",
                "UIV Machine Hull",
                11,
                GT_Loader_MetaTileEntities.imagination).getStackForm(1L));

        ItemList.Hull_UMV.set(
            new GT_MetaTileEntity_BasicHull(
                HULL_UMV.ID,
                "hull.tier.12",
                "UMV Machine Hull",
                12,
                GT_Loader_MetaTileEntities.imagination).getStackForm(1L));

        ItemList.Hull_UXV.set(
            new GT_MetaTileEntity_BasicHull(
                HULL_UXV.ID,
                "hull.tier.13",
                "UXV Machine Hull",
                13,
                GT_Loader_MetaTileEntities.imagination).getStackForm(1L));

        ItemList.Hull_MAXV.set(
            new GT_MetaTileEntity_BasicHull(
                HULL_MAX.ID,
                "hull.tier.14",
                "MAX Machine Hull",
                14,
                GT_Loader_MetaTileEntities.imagination).getStackForm(1L));
    }

    private static void registerTransformer() {
        ItemList.Transformer_LV_ULV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_LV_ULV.ID,
                "transformer.tier.00",
                "Ultra Low Voltage Transformer",
                0,
                "LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_MV_LV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_MV_LV.ID,
                "transformer.tier.01",
                "Low Voltage Transformer",
                1,
                "MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HV_MV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_HV_MV.ID,
                "transformer.tier.02",
                "Medium Voltage Transformer",
                2,
                "HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_EV_HV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_EV_HV.ID,
                "transformer.tier.03",
                "High Voltage Transformer",
                3,
                "EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_IV_EV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_IV_EV.ID,
                "transformer.tier.04",
                "Extreme Transformer",
                4,
                "IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_LuV_IV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_LuV_IV.ID,
                "transformer.tier.05",
                "Insane Transformer",
                5,
                "LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_ZPM_LuV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_ZPM_LuV.ID,
                "transformer.tier.06",
                "Ludicrous Transformer",
                6,
                "ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_UV_ZPM.set(
            new GT_MetaTileEntity_Transformer(
                transformer_UV_ZPM.ID,
                "transformer.tier.07",
                "ZPM Voltage Transformer",
                7,
                "UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_MAX_UV.set(
            new GT_MetaTileEntity_Transformer(
                transformer_UHV_UV.ID,
                "transformer.tier.08",
                "Ultimate Transformer",
                8,
                "UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_UEV_UHV.set(
            new GT_MetaTileEntity_Transformer(
                TRANSFORMER_UEV_UHV.ID,
                "transformer.tier.09",
                "Highly Ultimate Transformer",
                9,
                "UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_UIV_UEV.set(
            new GT_MetaTileEntity_Transformer(
                TRANSFORMER_UIV_UEV.ID,
                "transformer.tier.10",
                "Extremely Ultimate Transformer",
                10,
                "UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_UMV_UIV.set(
            new GT_MetaTileEntity_Transformer(
                TRANSFORMER_UMV_UIV.ID,
                "transformer.tier.11",
                "Insanely Ultimate Transformer",
                11,
                "UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_UXV_UMV.set(
            new GT_MetaTileEntity_Transformer(
                TRANSFORMER_UXV_UMV.ID,
                "transformer.tier.12",
                "Mega Ultimate Transformer",
                12,
                "UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_MAX_UXV.set(
            new GT_MetaTileEntity_Transformer(
                TRANSFORMER_MAX_UXV.ID,
                "transformer.tier.13",
                "Extended Mega Ultimate Transformer",
                13,
                "MAX -> UXV (Use Soft Mallet to invert)").getStackForm(1L));
    }

    private void registerChemicalBath() {
        ItemList.Machine_LV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_LV.ID,
                "basicmachine.chemicalbath.tier.01",
                "Basic Chemical Bath",
                1,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.Machine_MV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_MV.ID,
                "basicmachine.chemicalbath.tier.02",
                "Advanced Chemical Bath",
                2,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.Machine_HV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_HV.ID,
                "basicmachine.chemicalbath.tier.03",
                "Advanced Chemical Bath II",
                3,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.Machine_EV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_EV.ID,
                "basicmachine.chemicalbath.tier.04",
                "Advanced Chemical Bath III",
                4,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.Machine_IV_ChemicalBath.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_IV.ID,
                "basicmachine.chemicalbath.tier.05",
                "Advanced Chemical Bath IV",
                5,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.ChemicalBathLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_LuV.ID,
                "basicmachine.chemicalbath.tier.06",
                "Elite Chemical Bath",
                6,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.ChemicalBathZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_ZPM.ID,
                "basicmachine.chemicalbath.tier.07",
                "Elite Chemical Bath II",
                7,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.ChemicalBathUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_UV.ID,
                "basicmachine.chemicalbath.tier.08",
                "Ultimate Chemical Dunktron",
                8,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.ChemicalBathUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_UHV.ID,
                "basicmachine.chemicalbath.tier.09",
                "Epic Chemical Dunktron",
                9,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.ChemicalBathUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_UEV.ID,
                "basicmachine.chemicalbath.tier.10",
                "Epic Chemical Dunktron II",
                10,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.ChemicalBathUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_UIV.ID,
                "basicmachine.chemicalbath.tier.11",
                "Epic Chemical Dunktron III",
                11,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));

        ItemList.ChemicalBathUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_BATH_UMV.ID,
                "basicmachine.chemicalbath.tier.12",
                "Epic Chemical Dunktron IV",
                12,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_BATH",
                null).getStackForm(1L));
    }

    private void registerChemicalReactor() {
        ItemList.Machine_LV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_LV.ID,
                "basicmachine.chemicalreactor.tier.01",
                "Basic Chemical Reactor",
                1,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.Machine_MV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_MV.ID,
                "basicmachine.chemicalreactor.tier.02",
                "Advanced Chemical Reactor",
                2,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.Machine_HV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_HV.ID,
                "basicmachine.chemicalreactor.tier.03",
                "Advanced Chemical Reactor II",
                3,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.Machine_EV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_EV.ID,
                "basicmachine.chemicalreactor.tier.04",
                "Advanced Chemical Reactor III",
                4,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.Machine_IV_ChemicalReactor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_IV.ID,
                "basicmachine.chemicalreactor.tier.05",
                "Advanced Chemical Reactor IV",
                5,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.ChemicalReactorLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_LuV.ID,
                "basicmachine.chemicalreactor.tier.06",
                "Elite Chemical Reactor",
                6,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.ChemicalReactorZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_ZPM.ID,
                "basicmachine.chemicalreactor.tier.07",
                "Elite Chemical Reactor II",
                7,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.ChemicalReactorUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_UV.ID,
                "basicmachine.chemicalreactor.tier.08",
                "Ultimate Chemical Perforer",
                8,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.ChemicalReactorUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_UHV.ID,
                "basicmachine.chemicalreactor.tier.09",
                "Epic Chemical Performer",
                9,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.ChemicalReactorUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_UEV.ID,
                "basicmachine.chemicalreactor.tier.10",
                "Epic Chemical Performer II",
                10,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.ChemicalReactorUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_UIV.ID,
                "basicmachine.chemicalreactor.tier.11",
                "Epic Chemical Performer III",
                11,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

        ItemList.ChemicalReactorUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CHEMICAL_REACTOR_UMV.ID,
                "basicmachine.chemicalreactor.tier.12",
                "Epic Chemical Performer IV",
                12,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR",
                null).getStackForm(1L));

    }

    private void registerFermenter() {
        ItemList.Machine_LV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_LV.ID,
                "basicmachine.fermenter.tier.01",
                "Basic Fermenter",
                1,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_MV.ID,
                "basicmachine.fermenter.tier.02",
                "Advanced Fermenter",
                2,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_HV.ID,
                "basicmachine.fermenter.tier.03",
                "Advanced Fermenter II",
                3,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_EV.ID,
                "basicmachine.fermenter.tier.04",
                "Advanced Fermenter III",
                4,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Fermenter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_IV.ID,
                "basicmachine.fermenter.tier.05",
                "Advanced Fermenter IV",
                5,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.FermenterLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_LuV.ID,
                "basicmachine.fermenter.tier.06",
                "Elite Fermenter",
                6,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.FermenterZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_ZPM.ID,
                "basicmachine.fermenter.tier.07",
                "Elite Fermenter II",
                7,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.FermenterUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_UV.ID,
                "basicmachine.fermenter.tier.08",
                "Ultimate Fermentation Hastener",
                8,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.FermenterUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_UHV.ID,
                "basicmachine.fermenter.tier.09",
                "Epic Fermentation Hastener",
                9,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.FermenterUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_UEV.ID,
                "basicmachine.fermenter.tier.10",
                "Epic Fermentation Hastener II",
                10,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.FermenterUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_UIV.ID,
                "basicmachine.fermenter.tier.11",
                "Epic Fermentation Hastener III",
                11,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));

        ItemList.FermenterUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FERMENTER_UMV.ID,
                "basicmachine.fermenter.tier.12",
                "Epic Fermentation Hastener IV",
                12,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FERMENTER",
                null).getStackForm(1L));
    }

    private void registerFluidCanner() {
        ItemList.Machine_LV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_LV.ID,
                "basicmachine.fluidcanner.tier.01",
                "Basic Fluid Canner",
                1,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.Machine_MV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_MV.ID,
                "basicmachine.fluidcanner.tier.02",
                "Advanced Fluid Canner",
                2,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.Machine_HV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_HV.ID,
                "basicmachine.fluidcanner.tier.03",
                "Quick Fluid Canner",
                3,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.Machine_EV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_EV.ID,
                "basicmachine.fluidcanner.tier.04",
                "Turbo Fluid Canner",
                4,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.Machine_IV_FluidCanner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_IV.ID,
                "basicmachine.fluidcanner.tier.05",
                "Instant Fluid Canner",
                5,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.FluidCannerLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_LuV.ID,
                "basicmachine.fluidcanner.tier.06",
                "Elite Fluid Canner",
                6,
                MachineType.FLUID_CANNER.tooltipDescription(),
                fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.FluidCannerZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_ZPM.ID,
                "basicmachine.fluidcanner.tier.07",
                "Elite Fluid Canner II",
                7,
                MachineType.FLUID_CANNER.tooltipDescription(),
                fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.FluidCannerUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_UV.ID,
                "basicmachine.fluidcanner.tier.08",
                "Ultimate Liquid Can Actuator",
                8,
                MachineType.FLUID_CANNER.tooltipDescription(),
                fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.FluidCannerUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_UHV.ID,
                "basicmachine.fluidcanner.tier.09",
                "Epic Liquid Can Actuator",
                9,
                MachineType.FLUID_CANNER.tooltipDescription(),
                fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.FluidCannerUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_UEV.ID,
                "basicmachine.fluidcanner.tier.10",
                "Epic Liquid Can Actuator II",
                10,
                MachineType.FLUID_CANNER.tooltipDescription(),
                fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.FluidCannerUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_UIV.ID,
                "basicmachine.fluidcanner.tier.11",
                "Epic Liquid Can Actuator III",
                11,
                MachineType.FLUID_CANNER.tooltipDescription(),
                fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));

        ItemList.FluidCannerUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_CANNER_UMV.ID,
                "basicmachine.fluidcanner.tier.12",
                "Epic Liquid Can Actuator IV",
                12,
                MachineType.FLUID_CANNER.tooltipDescription(),
                fluidCannerRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_CANNER",
                null).getStackForm(1L));
    }

    private void registerFluidExtractor() {
        ItemList.Machine_LV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_LV.ID,
                "basicmachine.fluidextractor.tier.01",
                "Basic Fluid Extractor",
                1,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_MV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_MV.ID,
                "basicmachine.fluidextractor.tier.02",
                "Advanced Fluid Extractor",
                2,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_HV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_HV.ID,
                "basicmachine.fluidextractor.tier.03",
                "Advanced Fluid Extractor II",
                3,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_EV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_EV.ID,
                "basicmachine.fluidextractor.tier.04",
                "Advanced Fluid Extractor III",
                4,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_IV_FluidExtractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_IV.ID,
                "basicmachine.fluidextractor.tier.05",
                "Advanced Fluid Extractor IV",
                5,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.FluidExtractorLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_LuV.ID,
                "basicmachine.fluidextractor.tier.06",
                "Elite Fluid Extractor",
                6,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.FluidExtractorZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_ZPM.ID,
                "basicmachine.fluidextractor.tier.07",
                "Elite Fluid Extractor II",
                7,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.FluidExtractorUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_UV.ID,
                "basicmachine.fluidextractor.tier.08",
                "Ultimate Liquefying Sucker",
                8,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.FluidExtractorUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_UHV.ID,
                "basicmachine.fluidextractor.tier.09",
                "Epic Liquefying Sucker",
                9,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.FluidExtractorUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_UEV.ID,
                "basicmachine.fluidextractor.tier.10",
                "Epic Liquefying Sucker II",
                10,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.FluidExtractorUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_UIV.ID,
                "basicmachine.fluidextractor.tier.11",
                "Epic Liquefying Sucker III",
                11,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));

        ItemList.FluidExtractorUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_EXTRACTOR_UMV.ID,
                "basicmachine.fluidextractor.tier.12",
                "Epic Liquefying Sucker IV",
                12,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR",
                null).getStackForm(1L));
    }

    private void registerFluidHeater() {
        ItemList.Machine_LV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_LV.ID,
                "basicmachine.fluidheater.tier.01",
                "Basic Fluid Heater",
                1,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.Machine_MV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_MV.ID,
                "basicmachine.fluidheater.tier.02",
                "Advanced Fluid Heater",
                2,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.Machine_HV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_HV.ID,
                "basicmachine.fluidheater.tier.03",
                "Advanced Fluid Heater II",
                3,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.Machine_EV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_EV.ID,
                "basicmachine.fluidheater.tier.04",
                "Advanced Fluid Heater III",
                4,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.Machine_IV_FluidHeater.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_IV.ID,
                "basicmachine.fluidheater.tier.05",
                "Advanced Fluid Heater IV",
                5,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.FluidHeaterLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_LuV.ID,
                "basicmachine.fluidheater.tier.06",
                "Elite Fluid Heater",
                6,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.FluidHeaterZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_ZPM.ID,
                "basicmachine.fluidheater.tier.07",
                "Elite Fluid Heater II",
                7,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.FluidHeaterUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_UV.ID,
                "basicmachine.fluidheater.tier.08",
                "Ultimate Heat Infuser",
                8,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.FluidHeaterUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_UHV.ID,
                "basicmachine.fluidheater.tier.09",
                "Epic Heat Infuser",
                9,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.FluidHeaterUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_UEV.ID,
                "basicmachine.fluidheater.tier.10",
                "Epic Heat Infuser II",
                10,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.FluidHeaterUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_UIV.ID,
                "basicmachine.fluidheater.tier.11",
                "Epic Heat Infuser III",
                11,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));

        ItemList.FluidHeaterUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_HEATER_UMV.ID,
                "basicmachine.fluidheater.tier.12",
                "Epic Heat Infuser IV",
                12,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                0,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_HEATER",
                null).getStackForm(1L));
    }

    private void registerMixer() {
        ItemList.Machine_LV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_LV.ID,
                "basicmachine.mixer.tier.01",
                "Basic Mixer",
                1,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_MV.ID,
                "basicmachine.mixer.tier.02",
                "Advanced Mixer",
                2,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_HV.ID,
                "basicmachine.mixer.tier.03",
                "Advanced Mixer II",
                3,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                6,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_EV.ID,
                "basicmachine.mixer.tier.04",
                "Advanced Mixer III",
                4,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Mixer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_IV.ID,
                "basicmachine.mixer.tier.05",
                "Advanced Mixer IV",
                5,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.MixerLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_LuV.ID,
                "basicmachine.mixer.tier.06",
                "Elite Mixer",
                6,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.MixerZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_ZPM.ID,
                "basicmachine.mixer.tier.07",
                "Elite Mixer II",
                7,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.MixerUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_UV.ID,
                "basicmachine.mixer.tier.08",
                "Ultimate Matter Organizer",
                8,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.MixerUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_UHV.ID,
                "basicmachine.mixer.tier.09",
                "Epic Matter Organizer",
                9,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.MixerUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_UEV.ID,
                "basicmachine.mixer.tier.10",
                "Epic Matter Organizer II",
                10,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.MixerUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_UIV.ID,
                "basicmachine.mixer.tier.11",
                "Epic Matter Organizer III",
                11,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));

        ItemList.MixerUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MIXER_UMV.ID,
                "basicmachine.mixer.tier.12",
                "Epic Matter Organizer IV",
                12,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MIXER",
                null).getStackForm(1L));
    }

    private void registerAutoclave() {
        ItemList.Machine_LV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_LV.ID,
                "basicmachine.autoclave.tier.01",
                "Basic Autoclave",
                1,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                2,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.Machine_MV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_MV.ID,
                "basicmachine.autoclave.tier.02",
                "Advanced Autoclave",
                2,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                2,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.Machine_HV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_HV.ID,
                "basicmachine.autoclave.tier.03",
                "Advanced Autoclave II",
                3,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.Machine_EV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_EV.ID,
                "basicmachine.autoclave.tier.04",
                "Advanced Autoclave III",
                4,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.Machine_IV_Autoclave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_IV.ID,
                "basicmachine.autoclave.tier.05",
                "Advanced Autoclave IV",
                5,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.AutoclaveLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_LuV.ID,
                "basicmachine.autoclave.tier.06",
                "Elite Autoclave",
                6,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.AutoclaveZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_ZPM.ID,
                "basicmachine.autoclave.tier.07",
                "Elite Autoclave II",
                7,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.AutoclaveUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_UV.ID,
                "basicmachine.autoclave.tier.08",
                "Ultimate Pressure Cooker",
                8,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.AutoclaveUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_UHV.ID,
                "basicmachine.autoclave.tier.09",
                "Epic Pressure Cooker",
                9,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.AutoclaveUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_UEV.ID,
                "basicmachine.autoclave.tier.10",
                "Epic Pressure Cooker II",
                10,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.AutoclaveUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_UIV.ID,
                "basicmachine.autoclave.tier.11",
                "Epic Pressure Cooker III",
                11,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

        ItemList.AutoclaveUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                AUTOCLAVE_UMV.ID,
                "basicmachine.autoclave.tier.12",
                "Epic Pressure Cooker IV",
                12,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "AUTOCLAVE",
                null).getStackForm(1L));

    }

    private void registerBendingMachine() {
        ItemList.Machine_LV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_LV.ID,
                "basicmachine.bender.tier.01",
                "Basic Bending Machine",
                1,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_MV.ID,
                "basicmachine.bender.tier.02",
                "Advanced Bending Machine",
                2,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_HV.ID,
                "basicmachine.bender.tier.03",
                "Advanced Bending Machine II",
                3,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_EV.ID,
                "basicmachine.bender.tier.04",
                "Advanced Bending Machine III",
                4,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Bender.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_IV.ID,
                "basicmachine.bender.tier.05",
                "Advanced Bending Machine IV",
                5,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.BendingMachineLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_LuV.ID,
                "basicmachine.bender.tier.06",
                "Elite Bending Machine",
                6,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.BendingMachineZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_ZPM.ID,
                "basicmachine.bender.tier.07",
                "Elite Bending Machine II",
                7,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.BendingMachineUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_UV.ID,
                "basicmachine.bender.tier.08",
                "Ultimate Bending Unit",
                8,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.BendingMachineUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_UHV.ID,
                "basicmachine.bender.tier.09",
                "Epic Bending Unit",
                9,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.BendingMachineUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_UEV.ID,
                "basicmachine.bender.tier.10",
                "Epic Bending Unit II",
                10,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.BendingMachineUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_UIV.ID,
                "basicmachine.bender.tier.11",
                "Epic Bending Unit III",
                11,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));

        ItemList.BendingMachineUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                BENDING_MACHINE_UMV.ID,
                "basicmachine.bender.tier.12",
                "Epic Bending Unit IV",
                12,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "BENDER",
                null).getStackForm(1L));
    }

    private void registerCompressor() {
        ItemList.Machine_LV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_LV.ID,
                "basicmachine.compressor.tier.01",
                "Basic Compressor",
                1,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.Machine_MV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_MV.ID,
                "basicmachine.compressor.tier.02",
                "Advanced Compressor",
                2,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.Machine_HV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_HV.ID,
                "basicmachine.compressor.tier.03",
                "Advanced Compressor II",
                3,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.Machine_EV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_EV.ID,
                "basicmachine.compressor.tier.04",
                "Advanced Compressor III",
                4,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.Machine_IV_Compressor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_IV.ID,
                "basicmachine.compressor.tier.05",
                "Singularity Compressor",
                5,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.CompressorLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_LuV.ID,
                "basicmachine.compressor.tier.06",
                "Elite Compressor",
                6,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.CompressorZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_ZPM.ID,
                "basicmachine.compressor.tier.07",
                "Elite Compressor II",
                7,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.CompressorUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_UV.ID,
                "basicmachine.compressor.tier.08",
                "Ultimate Matter Constrictor",
                8,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.CompressorUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_UHV.ID,
                "basicmachine.compressor.tier.09",
                "Epic Matter Constrictor",
                9,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.CompressorUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_UEV.ID,
                "basicmachine.compressor.tier.10",
                "Epic Matter Constrictor II",
                10,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.CompressorUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_UIV.ID,
                "basicmachine.compressor.tier.11",
                "Epic Matter Constrictor III",
                11,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

        ItemList.CompressorUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                COMPRESSOR_UMV.ID,
                "basicmachine.compressor.tier.12",
                "Epic Matter Constrictor IV",
                12,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "COMPRESSOR",
                null).getStackForm(1L));

    }

    private void registerCuttingMachine() {
        ItemList.Machine_LV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_LV.ID,
                "basicmachine.cutter.tier.01",
                "Basic Cutting Machine",
                1,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                1,
                2,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_MV.ID,
                "basicmachine.cutter.tier.02",
                "Advanced Cutting Machine",
                2,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                2,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_HV.ID,
                "basicmachine.cutter.tier.03",
                "Advanced Cutting Machine II",
                3,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_EV.ID,
                "basicmachine.cutter.tier.04",
                "Advanced Cutting Machine III",
                4,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Cutter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_IV.ID,
                "basicmachine.cutter.tier.05",
                "Advanced Cutting Machine IV",
                5,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.CuttingMachineLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_LuV.ID,
                "basicmachine.cutter.tier.06",
                "Elite Cutting Machine",
                6,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.CuttingMachineZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_ZPM.ID,
                "basicmachine.cutter.tier.07",
                "Elite Cutting Machine II",
                7,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.CuttingMachineUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_UV.ID,
                "basicmachine.cutter.tier.08",
                "Ultimate Object Divider",
                8,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.CuttingMachineUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_UHV.ID,
                "basicmachine.cutter.tier.09",
                "Epic Object Divider",
                9,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.CuttingMachineUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_UEV.ID,
                "basicmachine.cutter.tier.10",
                "Epic Object Divider II",
                10,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.CuttingMachineUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_UIV.ID,
                "basicmachine.cutter.tier.11",
                "Epic Object Divider III",
                11,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

        ItemList.CuttingMachineUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CUTTING_MACHINE_UMV.ID,
                "basicmachine.cutter.tier.12",
                "Epic Object Divider IV",
                12,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CUTTER",
                null).getStackForm(1L));

    }

    private void registerDistillery() {
        ItemList.Machine_LV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_LV.ID,
                "basicmachine.distillery.tier.01",
                "Basic Distillery",
                1,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.Machine_MV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_MV.ID,
                "basicmachine.distillery.tier.02",
                "Advanced Distillery",
                2,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.Machine_HV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_HV.ID,
                "basicmachine.distillery.tier.03",
                "Advanced Distillery II",
                3,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.Machine_EV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_EV.ID,
                "basicmachine.distillery.tier.04",
                "Advanced Distillery III",
                4,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.Machine_IV_Distillery.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_IV.ID,
                "basicmachine.distillery.tier.05",
                "Advanced Distillery IV",
                5,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.DistilleryLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_LuV.ID,
                "basicmachine.distillery.tier.06",
                "Elite Distillery",
                6,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.DistilleryZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_ZPM.ID,
                "basicmachine.distillery.tier.07",
                "Elite Distillery II",
                7,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.DistilleryUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_UV.ID,
                "basicmachine.distillery.tier.08",
                "Ultimate Fraction Splitter",
                8,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.DistilleryUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_UHV.ID,
                "basicmachine.distillery.tier.09",
                "Epic Fraction Splitter",
                9,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.DistilleryUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_UEV.ID,
                "basicmachine.distillery.tier.10",
                "Epic Fraction Splitter II",
                10,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.DistilleryUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_UIV.ID,
                "basicmachine.distillery.tier.11",
                "Epic Fraction Splitter III",
                11,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

        ItemList.DistilleryUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                DISTILLERY_UMV.ID,
                "basicmachine.distillery.tier.12",
                "Epic Fraction Splitter IV",
                12,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "DISTILLERY",
                null).getStackForm(1L));

    }

    private void registerElectricFurnace() {
        ItemList.Machine_LV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_LV.ID,
                "basicmachine.e_furnace.tier.01",
                "Basic Electric Furnace",
                1,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_MV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_MV.ID,
                "basicmachine.e_furnace.tier.02",
                "Advanced Electric Furnace",
                2,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_HV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_HV.ID,
                "basicmachine.e_furnace.tier.03",
                "Advanced Electric Furnace II",
                3,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_EV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_EV.ID,
                "basicmachine.e_furnace.tier.04",
                "Advanced Electric Furnace III",
                4,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_IV_E_Furnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_IV.ID,
                "basicmachine.e_furnace.tier.05",
                "Electron Exitement Processor",
                5,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.ElectricFurnaceLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_LuV.ID,
                "basicmachine.e_furnace.tier.06",
                "Elite Electric Furnace",
                6,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).getStackForm(1L));

        ItemList.ElectricFurnaceZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_ZPM.ID,
                "basicmachine.e_furnace.tier.07",
                "Elite Electric Furnace II",
                7,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).getStackForm(1L));

        ItemList.ElectricFurnaceUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_UV.ID,
                "basicmachine.e_furnace.tier.08",
                "Ultimate Atom Stimulator",
                8,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).getStackForm(1L));

        ItemList.ElectricFurnaceUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_UHV.ID,
                "basicmachine.e_furnace.tier.09",
                "Epic Atom Stimulator",
                9,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).getStackForm(1L));

        ItemList.ElectricFurnaceUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_UEV.ID,
                "basicmachine.e_furnace.tier.10",
                "Epic Atom Stimulator II",
                10,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).getStackForm(1L));

        ItemList.ElectricFurnaceUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_UIV.ID,
                "basicmachine.e_furnace.tier.11",
                "Epic Atom Stimulator III",
                11,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).getStackForm(1L));

        ItemList.ElectricFurnaceUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTRIC_FURNACE_UMV.ID,
                "basicmachine.e_furnace.tier.12",
                "Epic Atom Stimulator IV",
                12,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE",
                null).getStackForm(1L));
    }

    private void registerElectrolyzer() {
        ItemList.Machine_LV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYSER_LV.ID,
                "basicmachine.electrolyzer.tier.01",
                "Basic Electrolyzer",
                1,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYSER_MV.ID,
                "basicmachine.electrolyzer.tier.02",
                "Advanced Electrolyzer",
                2,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYSER_HV.ID,
                "basicmachine.electrolyzer.tier.03",
                "Advanced Electrolyzer II",
                3,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYSER_EV.ID,
                "basicmachine.electrolyzer.tier.04",
                "Advanced Electrolyzer III",
                4,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Electrolyzer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYSER_IV.ID,
                "basicmachine.electrolyzer.tier.05",
                "Molecular Disintegrator E-4908",
                5,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.ElectrolyzerLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYZER_LuV.ID,
                "basicmachine.electrolyzer.tier.06",
                "Elite Electrolyzer",
                6,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.ElectrolyzerZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYZER_ZPM.ID,
                "basicmachine.electrolyzer.tier.07",
                "Elite Electrolyzer II",
                7,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.ElectrolyzerUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYZER_UV.ID,
                "basicmachine.electrolyzer.tier.08",
                "Ultimate Ionizer",
                8,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.ElectrolyzerUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYZER_UHV.ID,
                "basicmachine.electrolyzer.tier.09",
                "Epic Ionizer",
                9,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.ElectrolyzerUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYZER_UEV.ID,
                "basicmachine.electrolyzer.tier.10",
                "Epic Ionizer II",
                10,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.ElectrolyzerUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYZER_UIV.ID,
                "basicmachine.electrolyzer.tier.11",
                "Epic Ionizer III",
                11,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

        ItemList.ElectrolyzerUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROLYZER_UMV.ID,
                "basicmachine.electrolyzer.tier.12",
                "Epic Ionizer IV",
                12,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROLYZER",
                null).getStackForm(1L));

    }

    private void registerElectromagneticSeparator() {
        ItemList.Machine_LV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_LV.ID,
                "basicmachine.electromagneticseparator.tier.01",
                "Basic Electromagnetic Separator",
                1,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.Machine_MV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_MV.ID,
                "basicmachine.electromagneticseparator.tier.02",
                "Advanced Electromagnetic Separator",
                2,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.Machine_HV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_HV.ID,
                "basicmachine.electromagneticseparator.tier.03",
                "Advanced Electromagnetic Separator II",
                3,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.Machine_EV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_EV.ID,
                "basicmachine.electromagneticseparator.tier.04",
                "Advanced Electromagnetic Separator III",
                4,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.Machine_IV_ElectromagneticSeparator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_IV.ID,
                "basicmachine.electromagneticseparator.tier.05",
                "Advanced Electromagnetic Separator IV",
                5,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.ElectromagneticSeparatorLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_LuV.ID,
                "basicmachine.electromagneticseparator.tier.06",
                "Elite Electromagnetic Separator",
                6,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.ElectromagneticSeparatorZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_ZPM.ID,
                "basicmachine.electromagneticseparator.tier.07",
                "Elite Electromagnetic Separator II",
                7,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.ElectromagneticSeparatorUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_UV.ID,
                "basicmachine.electromagneticseparator.tier.08",
                "Ultimate Magnetar Separator",
                8,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.ElectromagneticSeparatorUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_UHV.ID,
                "basicmachine.electromagneticseparator.tier.09",
                "Epic Magnetar Separator",
                9,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.ElectromagneticSeparatorUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_UEV.ID,
                "basicmachine.electromagneticseparator.tier.10",
                "Epic Magnetar Separator II",
                10,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.ElectromagneticSeparatorUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_UIV.ID,
                "basicmachine.electromagneticseparator.tier.11",
                "Epic Magnetar Separator III",
                11,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

        ItemList.ElectromagneticSeparatorUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ELECTROMAGNETIC_SEPARATOR_UMV.ID,
                "basicmachine.electromagneticseparator.tier.12",
                "Epic Magnetar Separator IV",
                12,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR",
                null).getStackForm(1L));

    }

    private void registerExtractor() {
        ItemList.Machine_LV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_LV.ID,
                "basicmachine.extractor.tier.01",
                "Basic Extractor",
                1,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_MV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_MV.ID,
                "basicmachine.extractor.tier.02",
                "Advanced Extractor",
                2,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_HV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_HV.ID,
                "basicmachine.extractor.tier.03",
                "Advanced Extractor II",
                3,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_EV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_EV.ID,
                "basicmachine.extractor.tier.04",
                "Advanced Extractor III",
                4,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.Machine_IV_Extractor.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_IV.ID,
                "basicmachine.extractor.tier.05",
                "Vacuum Extractor",
                5,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.ExtractorLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_LuV.ID,
                "basicmachine.extractor.tier.06",
                "Elite Extractor",
                6,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.ExtractorZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_ZPM.ID,
                "basicmachine.extractor.tier.07",
                "Elite Extractor II",
                7,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.ExtractorUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_UV.ID,
                "basicmachine.extractor.tier.08",
                "Ultimate Extractinator",
                8,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.ExtractorUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_UHV.ID,
                "basicmachine.extractor.tier.09",
                "Epic Extractinator",
                9,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.ExtractorUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_UEV.ID,
                "basicmachine.extractor.tier.10",
                "Epic Extractinator II",
                10,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.ExtractorUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_UIV.ID,
                "basicmachine.extractor.tier.11",
                "Epic Extractinator III",
                11,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

        ItemList.ExtractorUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRACTOR_UMV.ID,
                "basicmachine.extractor.tier.12",
                "Epic Extractinator IV",
                12,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRACTOR",
                null).getStackForm(1L));

    }

    private void registerExtruder() {
        ItemList.Machine_LV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_LV.ID,
                "basicmachine.extruder.tier.01",
                "Basic Extruder",
                1,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_MV.ID,
                "basicmachine.extruder.tier.02",
                "Advanced Extruder",
                2,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_HV.ID,
                "basicmachine.extruder.tier.03",
                "Advanced Extruder II",
                3,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_EV.ID,
                "basicmachine.extruder.tier.04",
                "Advanced Extruder III",
                4,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Extruder.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_IV.ID,
                "basicmachine.extruder.tier.05",
                "Advanced Extruder IV",
                5,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.ExtruderLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_LuV.ID,
                "basicmachine.extruder.tier.06",
                "Elite Extruder",
                6,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.ExtruderZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_ZPM.ID,
                "basicmachine.extruder.tier.07",
                "Elite Extruder II",
                7,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.ExtruderUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_UV.ID,
                "basicmachine.extruder.tier.08",
                "Ultimate Shape Driver",
                8,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.ExtruderUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_UHV.ID,
                "basicmachine.extruder.tier.09",
                "Epic Shape Driver",
                9,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.ExtruderUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_UEV.ID,
                "basicmachine.extruder.tier.10",
                "Epic Shape Driver II",
                10,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.ExtruderUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_UIV.ID,
                "basicmachine.extruder.tier.11",
                "Epic Shape Driver III",
                11,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

        ItemList.ExtruderUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                EXTRUDER_UMV.ID,
                "basicmachine.extruder.tier.12",
                "Epic Shape Driver IV",
                12,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "EXTRUDER",
                null).getStackForm(1L));

    }

    private void registerFluidSolidifier() {
        ItemList.Machine_LV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_LV.ID,
                "basicmachine.fluidsolidifier.tier.01",
                "Basic Fluid Solidifier",
                1,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.Machine_MV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_MV.ID,
                "basicmachine.fluidsolidifier.tier.02",
                "Advanced Fluid Solidifier",
                2,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.Machine_HV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_HV.ID,
                "basicmachine.fluidsolidifier.tier.03",
                "Advanced Fluid Solidifier II",
                3,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.Machine_EV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_EV.ID,
                "basicmachine.fluidsolidifier.tier.04",
                "Advanced Fluid Solidifier III",
                4,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.Machine_IV_FluidSolidifier.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_IV.ID,
                "basicmachine.fluidsolidifier.tier.05",
                "Advanced Fluid Solidifier IV",
                5,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.FluidSolidifierLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_LuV.ID,
                "basicmachine.fluidsolidifier.tier.06",
                "Elite Fluid Solidifier",
                6,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.FluidSolidifierZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_ZPM.ID,
                "basicmachine.fluidsolidifier.tier.07",
                "Elite Fluid Solidifier II",
                7,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.FluidSolidifierUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_UV.ID,
                "basicmachine.fluidsolidifier.tier.08",
                "Ultimate Fluid Petrificator",
                8,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.FluidSolidifierUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_UHV.ID,
                "basicmachine.fluidsolidifier.tier.09",
                "Epic Fluid Petrificator",
                9,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.FluidSolidifierUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_UEV.ID,
                "basicmachine.fluidsolidifier.tier.10",
                "Epic Fluid Petrificator II",
                10,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.FluidSolidifierUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_UIV.ID,
                "basicmachine.fluidsolidifier.tier.11",
                "Epic Fluid Petrificator III",
                11,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

        ItemList.FluidSolidifierUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FLUID_SOLIDIFIER_UMV.ID,
                "basicmachine.fluidsolidifier.tier.12",
                "Epic Fluid Petrificator IV",
                12,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER",
                null).getStackForm(1L));

    }

    private void registerFormingPress() {
        ItemList.Machine_LV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_LV.ID,
                "basicmachine.press.tier.01",
                "Basic Forming Press",
                1,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.Machine_MV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_MV.ID,
                "basicmachine.press.tier.02",
                "Advanced Forming Press",
                2,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.Machine_HV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_HV.ID,
                "basicmachine.press.tier.03",
                "Advanced Forming Press II",
                3,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                4,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.Machine_EV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_EV.ID,
                "basicmachine.press.tier.04",
                "Advanced Forming Press III",
                4,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                4,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.Machine_IV_Press.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_IV.ID,
                "basicmachine.press.tier.05",
                "Advanced Forming Press IV",
                5,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.FormingPressLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_LuV.ID,
                "basicmachine.press.tier.06",
                "Elite Forming Press",
                6,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.FormingPressZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_ZPM.ID,
                "basicmachine.press.tier.07",
                "Elite Forming Press II",
                7,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.FormingPressUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_UV.ID,
                "basicmachine.press.tier.08",
                "Ultimate Surface Shifter",
                8,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.FormingPressUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_UHV.ID,
                "basicmachine.press.tier.09",
                "Epic Surface Shifter",
                9,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.FormingPressUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_UEV.ID,
                "basicmachine.press.tier.10",
                "Epic Surface Shifter II",
                10,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.FormingPressUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_UIV.ID,
                "basicmachine.press.tier.11",
                "Epic Surface Shifter III",
                11,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

        ItemList.FormingPressUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORMING_PRESS_UMV.ID,
                "basicmachine.press.tier.12",
                "Epic Surface Shifter IV",
                12,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                false,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PRESS",
                null).getStackForm(1L));

    }

    private void registerForgeHammer() {
        ItemList.Machine_LV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_LV.ID,
                "basicmachine.hammer.tier.01",
                "Basic Forge Hammer",
                1,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_MV.ID,
                "basicmachine.hammer.tier.02",
                "Advanced Forge Hammer",
                2,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_HV.ID,
                "basicmachine.hammer.tier.03",
                "Advanced Forge Hammer II",
                3,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_EV.ID,
                "basicmachine.hammer.tier.04",
                "Advanced Forge Hammer III",
                4,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Hammer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_IV.ID,
                "basicmachine.hammer.tier.05",
                "Advanced Forge Hammer IV",
                5,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.ForgeHammerLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_LuV.ID,
                "basicmachine.hammer.tier.06",
                "Elite Forge Hammer",
                6,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.ForgeHammerZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_ZPM.ID,
                "basicmachine.hammer.tier.07",
                "Elite Forge Hammer II",
                7,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.ForgeHammerUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_UV.ID,
                "basicmachine.hammer.tier.08",
                "Ultimate Impact Modulator",
                8,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.ForgeHammerUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_UHV.ID,
                "basicmachine.hammer.tier.09",
                "Epic Impact Modulator",
                9,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.ForgeHammerUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_UEV.ID,
                "basicmachine.hammer.tier.10",
                "Epic Impact Modulator II",
                10,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.ForgeHammerUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_UIV.ID,
                "basicmachine.hammer.tier.11",
                "Epic Impact Modulator III",
                11,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "HAMMER",
                null).getStackForm(1L));

        ItemList.ForgeHammerUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                FORGE_HAMMER_UMV.ID,
                "basicmachine.hammer.tier.12",
                "Epic Impact Modulator IV",
                12,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.RANDOM_ANVIL_USE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "HAMMER",
                null).getStackForm(1L));

    }

    private void registerLathe() {
        ItemList.Machine_LV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_LV.ID,
                "basicmachine.lathe.tier.01",
                "Basic Lathe",
                1,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.Machine_MV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_MV.ID,
                "basicmachine.lathe.tier.02",
                "Advanced Lathe",
                2,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.Machine_HV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_HV.ID,
                "basicmachine.lathe.tier.03",
                "Advanced Lathe II",
                3,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.Machine_EV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_EV.ID,
                "basicmachine.lathe.tier.04",
                "Advanced Lathe III",
                4,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.Machine_IV_Lathe.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_IV.ID,
                "basicmachine.lathe.tier.05",
                "Advanced Lathe IV",
                5,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.LatheLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_LuV.ID,
                "basicmachine.lathe.tier.06",
                "Elite Lathe",
                6,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.LatheZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_ZPM.ID,
                "basicmachine.lathe.tier.07",
                "Elite Lathe II",
                7,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.LatheUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_UV.ID,
                "basicmachine.lathe.tier.08",
                "Ultimate Turn-O-Matic",
                8,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.LatheUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_UHV.ID,
                "basicmachine.lathe.tier.09",
                "Epic Turn-O-Matic",
                9,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.LatheUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_UEV.ID,
                "basicmachine.lathe.tier.10",
                "Epic Turn-O-Matic II",
                10,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.LatheUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_UIV.ID,
                "basicmachine.lathe.tier.11",
                "Epic Turn-O-Matic III",
                11,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

        ItemList.LatheUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LATHE_UMV.ID,
                "basicmachine.lathe.tier.12",
                "Epic Turn-O-Matic IV",
                12,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LATHE",
                null).getStackForm(1L));

    }

    private void registerPrecisionLaserEngraver() {
        ItemList.Machine_LV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LASER_ENGRAVER_LV.ID,
                "basicmachine.laserengraver.tier.01",
                "Basic Precision Laser Engraver",
                1,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                2,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.Machine_MV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LASER_ENGRAVER_MV.ID,
                "basicmachine.laserengraver.tier.02",
                "Advanced Precision Laser Engraver",
                2,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                2,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.Machine_HV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LASER_ENGRAVER_HV.ID,
                "basicmachine.laserengraver.tier.03",
                "Advanced Precision Laser Engraver II",
                3,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                2,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.Machine_EV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LASER_ENGRAVER_EV.ID,
                "basicmachine.laserengraver.tier.04",
                "Advanced Precision Laser Engraver III",
                4,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.Machine_IV_LaserEngraver.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                LASER_ENGRAVER_IV.ID,
                "basicmachine.laserengraver.tier.05",
                "Advanced Precision Laser Engraver IV",
                5,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.PrecisionLaserEngraverLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRECISION_LASER_ENGRAVER_LuV.ID,
                "basicmachine.laserengraver.tier.06",
                "Elite Precision Laser Engraver",
                6,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.PrecisionLaserEngraverZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRECISION_LASER_ENGRAVER_ZPM.ID,
                "basicmachine.laserengraver.tier.07",
                "Elite Precision Laser Engraver II",
                7,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.PrecisionLaserEngraverUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRECISION_LASER_ENGRAVER_UV.ID,
                "basicmachine.laserengraver.tier.08",
                "Ultimate Exact Photon Cannon",
                8,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.PrecisionLaserEngraverUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRECISION_LASER_ENGRAVER_UHV.ID,
                "basicmachine.laserengraver.tier.09",
                "Epic Exact Photon Cannon",
                9,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.PrecisionLaserEngraverUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRECISION_LASER_ENGRAVER_UEV.ID,
                "basicmachine.laserengraver.tier.10",
                "Epic Exact Photon Cannon II",
                10,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.PrecisionLaserEngraverUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRECISION_LASER_ENGRAVER_UIV.ID,
                "basicmachine.laserengraver.tier.11",
                "Epic Exact Photon Cannon III",
                11,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

        ItemList.PrecisionLaserEngraverUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRECISION_LASER_ENGRAVER_UMV.ID,
                "basicmachine.laserengraver.tier.12",
                "Epic Exact Photon Cannon IV",
                12,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "LASER_ENGRAVER",
                null).getStackForm(1L));

    }

    private void registerMacerator() {
        ItemList.Machine_LV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_LV.ID,
                "basicmachine.macerator.tier.01",
                "Basic Macerator",
                1,
                MachineType.MACERATOR.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "MACERATOR",
                null).getStackForm(1L));

        ItemList.Machine_MV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_MV.ID,
                "basicmachine.macerator.tier.02",
                "Advanced Macerator",
                2,
                MachineType.MACERATOR.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "MACERATOR",
                null).getStackForm(1L));

        ItemList.Machine_HV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_HV.ID,
                "basicmachine.macerator.tier.03",
                "Universal Macerator",
                3,
                MachineType.MACERATOR_PULVERIZER.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                2,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_EV.ID,
                "basicmachine.macerator.tier.04",
                "Universal Pulverizer",
                4,
                MachineType.MACERATOR_PULVERIZER.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Macerator.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_IV.ID,
                "basicmachine.macerator.tier.05",
                "Blend-O-Matic 9001",
                5,
                MachineType.MACERATOR_PULVERIZER.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.MaceratorLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_LuV.ID,
                "basicmachine.macerator.tier.06",
                "Elite Pulverizer",
                6,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.MaceratorZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_ZPM.ID,
                "basicmachine.macerator.tier.07",
                "Elite Pulverizer II",
                7,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.MaceratorUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_UV.ID,
                "basicmachine.macerator.tier.08",
                "Ultimate Shape Eliminator",
                8,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.MaceratorUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_UHV.ID,
                "basicmachine.macerator.tier.09",
                "Epic Shape Eliminator",
                9,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.MaceratorUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_UEV.ID,
                "basicmachine.macerator.tier.10",
                "Epic Shape Eliminator II",
                10,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.MaceratorUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_UIV.ID,
                "basicmachine.macerator.tier.11",
                "Epic Shape Eliminator III",
                11,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

        ItemList.MaceratorUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MACERATOR_UMV.ID,
                "basicmachine.macerator.tier.12",
                "Epic Shape Eliminator IV",
                12,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.IC2_MACHINES_MACERATOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER",
                null).getStackForm(1L));

    }

    private void registerMatterFabricator() {

        ItemList.MassFabricatorLuV.set(
            new GT_MetaTileEntity_Massfabricator(
                MATTER_FABRICATOR_LuV.ID,
                "basicmachine.massfab.tier.06",
                "Elite Mass Fabricator",
                6).getStackForm(1L));
        ItemList.MassFabricatorZPM.set(
            new GT_MetaTileEntity_Massfabricator(
                MATTER_FABRICATOR_ZPM.ID,
                "basicmachine.massfab.tier.07",
                "Elite Mass Fabricator II",
                7).getStackForm(1L));
        ItemList.MassFabricatorUV.set(
            new GT_MetaTileEntity_Massfabricator(
                MATTER_FABRICATOR_UV.ID,
                "basicmachine.massfab.tier.08",
                "Ultimate Existence Initiator",
                8).getStackForm(1L));
        ItemList.MassFabricatorUHV.set(
            new GT_MetaTileEntity_Massfabricator(
                MATTER_FABRICATOR_UHV.ID,
                "basicmachine.massfab.tier.09",
                "Epic Existence Initiator",
                9).getStackForm(1L));
        ItemList.MassFabricatorUEV.set(
            new GT_MetaTileEntity_Massfabricator(
                MATTER_FABRICATOR_UEV.ID,
                "basicmachine.massfab.tier.10",
                "Epic Existence Initiator II",
                10).getStackForm(1L));
        ItemList.MassFabricatorUIV.set(
            new GT_MetaTileEntity_Massfabricator(
                MATTER_FABRICATOR_UIV.ID,
                "basicmachine.massfab.tier.11",
                "Epic Existence Initiator III",
                11).getStackForm(1L));
        ItemList.MassFabricatorUMV.set(
            new GT_MetaTileEntity_Massfabricator(
                MATTER_FABRICATOR_UMV.ID,
                "basicmachine.massfab.tier.12",
                "Epic Existence Initiator IV",
                12).getStackForm(1L));
    }

    private void registerMicrowave() {
        ItemList.Machine_LV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_OVEN_LV.ID,
                "basicmachine.microwave.tier.01",
                "Basic Microwave",
                1,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.Machine_MV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_OVEN_MV.ID,
                "basicmachine.microwave.tier.02",
                "Advanced Microwave",
                2,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.Machine_HV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_OVEN_HV.ID,
                "basicmachine.microwave.tier.03",
                "Advanced Microwave II",
                3,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.Machine_EV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_OVEN_EV.ID,
                "basicmachine.microwave.tier.04",
                "Advanced Microwave III",
                4,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.Machine_IV_Microwave.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_OVEN_IV.ID,
                "basicmachine.microwave.tier.05",
                "Advanced Microwave IV",
                5,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.MicrowaveLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_LuV.ID,
                "basicmachine.microwave.tier.06",
                "Elite Microwave",
                6,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.MicrowaveZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_ZPM.ID,
                "basicmachine.microwave.tier.07",
                "Elite Microwave II",
                7,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.MicrowaveUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_UV.ID,
                "basicmachine.microwave.tier.08",
                "Ultimate UFO Engine",
                8,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.MicrowaveUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_UHV.ID,
                "basicmachine.microwave.tier.09",
                "Epic UFO Engine",
                9,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.MicrowaveUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_UEV.ID,
                "basicmachine.microwave.tier.10",
                "Epic UFO Engine II",
                10,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.MicrowaveUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_UIV.ID,
                "basicmachine.microwave.tier.11",
                "Epic UFO Engine III",
                11,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

        ItemList.MicrowaveUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                MICROWAVE_UMV.ID,
                "basicmachine.microwave.tier.12",
                "Epic UFO Engine IV",
                12,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "MICROWAVE",
                null).getStackForm(1L));

    }

    private static void registerOven() {
        ItemList.Machine_LV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                OVEN_LV.ID,
                "basicmachine.e_oven.tier.01",
                "Basic Electric Oven",
                1,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                null).setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_MV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                OVEN_MV.ID,
                "basicmachine.e_oven.tier.02",
                "Advanced Electric Oven",
                2,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                null).setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_HV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                OVEN_HV.ID,
                "basicmachine.e_oven.tier.03",
                "Advanced Electric Oven II",
                3,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                null).setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_EV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                OVEN_EV.ID,
                "basicmachine.e_oven.tier.04",
                "Advanced Electric Oven III",
                4,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                null).setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_IV_Oven.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                OVEN_IV.ID,
                "basicmachine.e_oven.tier.05",
                "Advanced Electric Oven IV",
                5,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN",
                null).setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));
    }

    private void registerOreWashingPlant() {
        ItemList.Machine_LV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHER_LV.ID,
                "basicmachine.orewasher.tier.01",
                "Basic Ore Washing Plant",
                1,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.Machine_MV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHER_MV.ID,
                "basicmachine.orewasher.tier.02",
                "Advanced Ore Washing Plant",
                2,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.Machine_HV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHER_HV.ID,
                "basicmachine.orewasher.tier.03",
                "Advanced Ore Washing Plant II",
                3,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.Machine_EV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHER_EV.ID,
                "basicmachine.orewasher.tier.04",
                "Advanced Ore Washing Plant III",
                4,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.Machine_IV_OreWasher.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHER_IV.ID,
                "basicmachine.orewasher.tier.05",
                "Repurposed Laundry-Washer I-360",
                5,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.OreWashingPlantLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHING_PLANT_LuV.ID,
                "basicmachine.orewasher.tier.06",
                "Elite Ore Washing Plant",
                6,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.OreWashingPlantZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHING_PLANT_ZPM.ID,
                "basicmachine.orewasher.tier.07",
                "Elite Ore Washing Plant II",
                7,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.OreWashingPlantUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHING_PLANT_UV.ID,
                "basicmachine.orewasher.tier.08",
                "Ultimate Ore Washing Machine",
                8,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.OreWashingPlantUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHING_PLANT_UHV.ID,
                "basicmachine.orewasher.tier.09",
                "Epic Ore Washing Machine",
                9,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.OreWashingPlantUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHING_PLANT_UEV.ID,
                "basicmachine.orewasher.tier.10",
                "Epic Ore Washing Machine II",
                10,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.OreWashingPlantUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHING_PLANT_UIV.ID,
                "basicmachine.orewasher.tier.11",
                "Epic Ore Washing Machine III",
                11,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

        ItemList.OreWashingPlantUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ORE_WASHING_PLANT_UMV.ID,
                "basicmachine.orewasher.tier.12",
                "Epic Ore Washing Machine IV",
                12,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ORE_WASHER",
                null).getStackForm(1L));

    }

    private void registerPolarizer() {
        ItemList.Machine_LV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_LV.ID,
                "basicmachine.polarizer.tier.01",
                "Basic Polarizer",
                1,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_MV.ID,
                "basicmachine.polarizer.tier.02",
                "Advanced Polarizer",
                2,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_HV.ID,
                "basicmachine.polarizer.tier.03",
                "Advanced Polarizer II",
                3,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_EV.ID,
                "basicmachine.polarizer.tier.04",
                "Advanced Polarizer III",
                4,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Polarizer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_IV.ID,
                "basicmachine.polarizer.tier.05",
                "Advanced Polarizer IV",
                5,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.PolarizerLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_LuV.ID,
                "basicmachine.polarizer.tier.06",
                "Elite Polarizer",
                6,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.PolarizerZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_ZPM.ID,
                "basicmachine.polarizer.tier.07",
                "Elite Polarizer II",
                7,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.PolarizerUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_UV.ID,
                "basicmachine.polarizer.tier.08",
                "Ultimate Magnetism Inducer",
                8,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.PolarizerUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_UHV.ID,
                "basicmachine.polarizer.tier.09",
                "Epic Magnetism Inducer",
                9,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.PolarizerUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_UEV.ID,
                "basicmachine.polarizer.tier.10",
                "Epic Magnetism Inducer II",
                10,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.PolarizerUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_UIV.ID,
                "basicmachine.polarizer.tier.11",
                "Epic Magnetism Inducer III",
                11,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

        ItemList.PolarizerUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                POLARIZER_UMV.ID,
                "basicmachine.polarizer.tier.12",
                "Epic Magnetism Inducer IV",
                12,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "POLARIZER",
                null).getStackForm(1L));

    }

    private static void registerPrinter() {
        ItemList.Machine_LV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_LV.ID,
                "basicmachine.printer.tier.01",
                "Basic Printer",
                1,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_MV.ID,
                "basicmachine.printer.tier.02",
                "Advanced Printer",
                2,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_HV.ID,
                "basicmachine.printer.tier.03",
                "Advanced Printer II",
                3,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_EV.ID,
                "basicmachine.printer.tier.04",
                "Advanced Printer III",
                4,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_IV.ID,
                "basicmachine.printer.tier.05",
                "Advanced Printer IV",
                5,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));

        ItemList.Machine_LuV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_LuV.ID,
                "basicmachine.printer.tier.06",
                "Advanced Printer V",
                6,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));

        ItemList.Machine_ZPM_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_ZPM.ID,
                "basicmachine.printer.tier.07",
                "Advanced Printer VI",
                7,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));

        ItemList.Machine_UV_Printer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PRINTER_UV.ID,
                "basicmachine.printer.tier.08",
                "Advanced Printer VII",
                8,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.TOP_SMOKE,
                "PRINTER",
                null).getStackForm(1L));
    }

    private void registerRecycler() {
        ItemList.Machine_LV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_LV.ID,
                "basicmachine.recycler.tier.01",
                "Basic Recycler",
                1,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_MV.ID,
                "basicmachine.recycler.tier.02",
                "Advanced Recycler",
                2,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_HV.ID,
                "basicmachine.recycler.tier.03",
                "Advanced Recycler II",
                3,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_EV.ID,
                "basicmachine.recycler.tier.04",
                "Advanced Recycler III",
                4,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Recycler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_IV.ID,
                "basicmachine.recycler.tier.05",
                "The Oblitterator",
                5,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.RecyclerLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_LuV.ID,
                "basicmachine.recycler.tier.06",
                "Elite Recycler",
                6,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.RecyclerZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_ZPM.ID,
                "basicmachine.recycler.tier.07",
                "Elite Recycler II",
                7,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.RecyclerUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_UV.ID,
                "basicmachine.recycler.tier.08",
                "Ultimate Scrap-O-Matic",
                8,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.RecyclerUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_UHV.ID,
                "basicmachine.recycler.tier.09",
                "Epic Scrap-O-Matic",
                9,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.RecyclerUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_UEV.ID,
                "basicmachine.recycler.tier.10",
                "Epic Scrap-O-Matic II",
                10,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.RecyclerUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_UIV.ID,
                "basicmachine.recycler.tier.11",
                "Epic Scrap-O-Matic III",
                11,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

        ItemList.RecyclerUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                RECYCLER_UMV.ID,
                "basicmachine.recycler.tier.12",
                "Epic Scrap-O-Matic IV",
                12,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "RECYCLER",
                null).getStackForm(1L));

    }

    private void registerSiftingMachine() {
        ItemList.Machine_LV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTER_LV.ID,
                "basicmachine.sifter.tier.01",
                "Basic Sifting Machine",
                1,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTER_MV.ID,
                "basicmachine.sifter.tier.02",
                "Advanced Sifting Machine",
                2,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTER_HV.ID,
                "basicmachine.sifter.tier.03",
                "Advanced Sifting Machine II",
                3,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTER_EV.ID,
                "basicmachine.sifter.tier.04",
                "Advanced Sifting Machine III",
                4,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Sifter.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTER_IV.ID,
                "basicmachine.sifter.tier.05",
                "Advanced Sifting Machine IV",
                5,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.SiftingMachineLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTING_MACHINE_LuV.ID,
                "basicmachine.sifter.tier.06",
                "Elite Sifting Machine",
                6,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.SiftingMachineZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTING_MACHINE_ZPM.ID,
                "basicmachine.sifter.tier.07",
                "Elite Sifting Machine II",
                7,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.SiftingMachineUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTING_MACHINE_UV.ID,
                "basicmachine.sifter.tier.08",
                "Ultimate Pulsation Filter",
                8,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.SiftingMachineUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTING_MACHINE_UHV.ID,
                "basicmachine.sifter.tier.09",
                "Epic Pulsation Filter",
                9,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.SiftingMachineUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTING_MACHINE_UEV.ID,
                "basicmachine.sifter.tier.10",
                "Epic Pulsation Filter II",
                10,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.SiftingMachineUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTING_MACHINE_UIV.ID,
                "basicmachine.sifter.tier.11",
                "Epic Pulsation Filter III",
                11,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

        ItemList.SiftingMachineUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SIFTING_MACHINE_UMV.ID,
                "basicmachine.sifter.tier.12",
                "Epic Pulsation Filter IV",
                12,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SIFTER",
                null).getStackForm(1L));

    }

    private void registerSlicingMachine() {
        ItemList.Machine_LV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICER_LV.ID,
                "basicmachine.slicer.tier.01",
                "Basic Slicing Machine",
                1,
                MachineType.SLICER.tooltipDescription(),
                RecipeMaps.slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICER_MV.ID,
                "basicmachine.slicer.tier.02",
                "Advanced Slicing Machine",
                2,
                MachineType.SLICER.tooltipDescription(),
                RecipeMaps.slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICER_HV.ID,
                "basicmachine.slicer.tier.03",
                "Advanced Slicing Machine II",
                3,
                MachineType.SLICER.tooltipDescription(),
                RecipeMaps.slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICER_EV.ID,
                "basicmachine.slicer.tier.04",
                "Advanced Slicing Machine III",
                4,
                MachineType.SLICER.tooltipDescription(),
                RecipeMaps.slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Slicer.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICER_IV.ID,
                "basicmachine.slicer.tier.05",
                "Advanced Slicing Machine IV",
                5,
                MachineType.SLICER.tooltipDescription(),
                RecipeMaps.slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.SlicingMachineLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICING_MACHINE_LuV.ID,
                "basicmachine.slicer.tier.06",
                "Elite Slicing Machine",
                6,
                MachineType.SLICER.tooltipDescription(),
                slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.SlicingMachineZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICING_MACHINE_ZPM.ID,
                "basicmachine.slicer.tier.07",
                "Elite Slicing Machine II",
                7,
                MachineType.SLICER.tooltipDescription(),
                slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.SlicingMachineUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICING_MACHINE_UV.ID,
                "basicmachine.slicer.tier.08",
                "Ultimate Quantum Slicer",
                8,
                MachineType.SLICER.tooltipDescription(),
                slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.SlicingMachineUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICING_MACHINE_UHV.ID,
                "basicmachine.slicer.tier.09",
                "Epic Quantum Slicer",
                9,
                MachineType.SLICER.tooltipDescription(),
                slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.SlicingMachineUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICING_MACHINE_UEV.ID,
                "basicmachine.slicer.tier.10",
                "Epic Quantum Slicer II",
                10,
                MachineType.SLICER.tooltipDescription(),
                slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.SlicingMachineUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICING_MACHINE_UIV.ID,
                "basicmachine.slicer.tier.11",
                "Epic Quantum Slicer III",
                11,
                MachineType.SLICER.tooltipDescription(),
                slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

        ItemList.SlicingMachineUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                SLICING_MACHINE_UMV.ID,
                "basicmachine.slicer.tier.12",
                "Epic Quantum Slicer IV",
                12,
                MachineType.SLICER.tooltipDescription(),
                slicerRecipes,
                2,
                1,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "SLICER",
                null).getStackForm(1L));

    }

    private void registerThermalCentrifuge() {
        ItemList.Machine_LV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_LV.ID,
                "basicmachine.thermalcentrifuge.tier.01",
                "Basic Thermal Centrifuge",
                1,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_MV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_MV.ID,
                "basicmachine.thermalcentrifuge.tier.02",
                "Advanced Thermal Centrifuge",
                2,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_HV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_HV.ID,
                "basicmachine.thermalcentrifuge.tier.03",
                "Advanced Thermal Centrifuge II",
                3,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_EV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_EV.ID,
                "basicmachine.thermalcentrifuge.tier.04",
                "Advanced Thermal Centrifuge III",
                4,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_IV_ThermalCentrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_IV.ID,
                "basicmachine.thermalcentrifuge.tier.05",
                "Blaze Sweatshop T-6350",
                5,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.ThermalCentrifugeLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_LuV.ID,
                "basicmachine.thermalcentrifuge.tier.06",
                "Elite Thermal Centrifuge",
                6,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.ThermalCentrifugeZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_ZPM.ID,
                "basicmachine.thermalcentrifuge.tier.07",
                "Elite Thermal Centrifuge II",
                7,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.ThermalCentrifugeUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_UV.ID,
                "basicmachine.thermalcentrifuge.tier.08",
                "Ultimate Fire Cyclone",
                8,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.ThermalCentrifugeUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_UHV.ID,
                "basicmachine.thermalcentrifuge.tier.09",
                "Epic Fire Cyclone",
                9,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.ThermalCentrifugeUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_UEV.ID,
                "basicmachine.thermalcentrifuge.tier.10",
                "Epic Fire Cyclone II",
                10,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.ThermalCentrifugeUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_UIV.ID,
                "basicmachine.thermalcentrifuge.tier.11",
                "Epic Fire Cyclone III",
                11,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.ThermalCentrifugeUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                THERMAL_CENTRIFUGE_UMV.ID,
                "basicmachine.thermalcentrifuge.tier.12",
                "Epic Fire Cyclone IV",
                12,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE",
                null).getStackForm(1L));
    }

    private void registerWiremill() {
        ItemList.Machine_LV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_LV.ID,
                "basicmachine.wiremill.tier.01",
                "Basic Wiremill",
                1,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.Machine_MV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_MV.ID,
                "basicmachine.wiremill.tier.02",
                "Advanced Wiremill",
                2,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.Machine_HV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_HV.ID,
                "basicmachine.wiremill.tier.03",
                "Advanced Wiremill II",
                3,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.Machine_EV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_EV.ID,
                "basicmachine.wiremill.tier.04",
                "Advanced Wiremill III",
                4,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.Machine_IV_Wiremill.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_IV.ID,
                "basicmachine.wiremill.tier.05",
                "Advanced Wiremill IV",
                5,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.WiremillLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_LuV.ID,
                "basicmachine.wiremill.tier.06",
                "Elite Wiremill",
                6,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.WiremillZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_ZPM.ID,
                "basicmachine.wiremill.tier.07",
                "Elite Wiremill II",
                7,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.WiremillUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_UV.ID,
                "basicmachine.wiremill.tier.08",
                "Ultimate Wire Transfigurator",
                8,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.WiremillUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_UHV.ID,
                "basicmachine.wiremill.tier.09",
                "Epic Wire Transfigurator",
                9,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.WiremillUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_UEV.ID,
                "basicmachine.wiremill.tier.10",
                "Epic Wire Transfigurator II",
                10,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.WiremillUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_UIV.ID,
                "basicmachine.wiremill.tier.11",
                "Epic Wire Transfigurator III",
                11,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

        ItemList.WiremillUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                WIREMILL_UMV.ID,
                "basicmachine.wiremill.tier.12",
                "Epic Wire Transfigurator IV",
                12,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "WIREMILL",
                null).getStackForm(1L));

    }

    private void registerArcFurnace() {
        ItemList.Machine_LV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_LV.ID,
                "basicmachine.arcfurnace.tier.01",
                "Basic Arc Furnace",
                1,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_MV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_MV.ID,
                "basicmachine.arcfurnace.tier.02",
                "Advanced Arc Furnace",
                2,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_HV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_HV.ID,
                "basicmachine.arcfurnace.tier.03",
                "Advanced Arc Furnace II",
                3,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_EV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_EV.ID,
                "basicmachine.arcfurnace.tier.04",
                "Advanced Arc Furnace III",
                4,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_IV_ArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_IV.ID,
                "basicmachine.arcfurnace.tier.05",
                "Advanced Arc Furnace IV",
                5,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.ArcFurnaceLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_LuV.ID,
                "basicmachine.arcfurnace.tier.06",
                "Elite Arc Furnace",
                6,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.ArcFurnaceZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_ZPM.ID,
                "basicmachine.arcfurnace.tier.07",
                "Elite Arc Furnace II",
                7,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.ArcFurnaceUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_UV.ID,
                "basicmachine.arcfurnace.tier.08",
                "Ultimate Short Circuit Heater",
                8,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.ArcFurnaceUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_UHV.ID,
                "basicmachine.arcfurnace.tier.09",
                "Epic Short Circuit Heater",
                9,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.ArcFurnaceUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_UEV.ID,
                "basicmachine.arcfurnace.tier.10",
                "Epic Short Circuit Heater II",
                10,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.ArcFurnaceUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_UIV.ID,
                "basicmachine.arcfurnace.tier.11",
                "Epic Short Circuit Heater III",
                11,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.ArcFurnaceUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                ARC_FURNACE_UMV.ID,
                "basicmachine.arcfurnace.tier.12",
                "Epic Short Circuit Heater IV",
                12,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "ARC_FURNACE",
                null).getStackForm(1L));

    }

    private void registerCentrifuge() {
        ItemList.Machine_LV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_LV.ID,
                "basicmachine.centrifuge.tier.01",
                "Basic Centrifuge",
                1,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_MV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_MV.ID,
                "basicmachine.centrifuge.tier.02",
                "Advanced Centrifuge",
                2,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_HV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_HV.ID,
                "basicmachine.centrifuge.tier.03",
                "Turbo Centrifuge",
                3,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_EV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_EV.ID,
                "basicmachine.centrifuge.tier.04",
                "Molecular Separator",
                4,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.Machine_IV_Centrifuge.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_IV.ID,
                "basicmachine.centrifuge.tier.05",
                "Molecular Cyclone",
                5,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.CentrifugeLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_LuV.ID,
                "basicmachine.centrifuge.tier.06",
                "Elite Centrifuge",
                6,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.CentrifugeZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_ZPM.ID,
                "basicmachine.centrifuge.tier.07",
                "Elite Centrifuge II",
                7,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.CentrifugeUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_UV.ID,
                "basicmachine.centrifuge.tier.08",
                "Ultimate Molecular Tornado",
                8,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.CentrifugeUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_UHV.ID,
                "basicmachine.centrifuge.tier.09",
                "Epic Molecular Tornado",
                9,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.CentrifugeUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_UEV.ID,
                "basicmachine.centrifuge.tier.10",
                "Epic Molecular Tornado II",
                10,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.CentrifugeUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_UIV.ID,
                "basicmachine.centrifuge.tier.11",
                "Epic Molecular Tornado III",
                11,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

        ItemList.CentrifugeUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CENTRIFUGE_UMV.ID,
                "basicmachine.centrifuge.tier.12",
                "Epic Molecular Tornado IV",
                12,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CENTRIFUGE",
                null).getStackForm(1L));

    }

    private void registerPlasmaArcFurnace() {
        ItemList.Machine_LV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_LV.ID,
                "basicmachine.plasmaarcfurnace.tier.01",
                "Basic Plasma Arc Furnace",
                1,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_MV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_MV.ID,
                "basicmachine.plasmaarcfurnace.tier.02",
                "Advanced Plasma Arc Furnace",
                2,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_HV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_HV.ID,
                "basicmachine.plasmaarcfurnace.tier.03",
                "Advanced Plasma Arc Furnace II",
                3,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_EV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_EV.ID,
                "basicmachine.plasmaarcfurnace.tier.04",
                "Advanced Plasma Arc Furnace III",
                4,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.Machine_IV_PlasmaArcFurnace.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_IV.ID,
                "basicmachine.plasmaarcfurnace.tier.05",
                "Advanced Plasma Arc Furnace IV",
                5,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.PlasmaArcFurnaceLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_LuV.ID,
                "basicmachine.plasmaarcfurnace.tier.06",
                "Elite Plasma Arc Furnace",
                6,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.PlasmaArcFurnaceZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_ZPM.ID,
                "basicmachine.plasmaarcfurnace.tier.07",
                "Elite Plasma Arc Furnace II",
                7,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.PlasmaArcFurnaceUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_UV.ID,
                "basicmachine.plasmaarcfurnace.tier.08",
                "Ultimate Plasma Discharge Heater",
                8,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.PlasmaArcFurnaceUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_UHV.ID,
                "basicmachine.plasmaarcfurnace.tier.09",
                "Epic Plasma Discharge Heater",
                9,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.PlasmaArcFurnaceUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_UEV.ID,
                "basicmachine.plasmaarcfurnace.tier.10",
                "Epic Plasma Discharge Heater II",
                10,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.PlasmaArcFurnaceUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_UIV.ID,
                "basicmachine.plasmaarcfurnace.tier.11",
                "Epic Plasma Discharge Heater III",
                11,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));

        ItemList.PlasmaArcFurnaceUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                PLASMA_ARC_FURNACE_UMV.ID,
                "basicmachine.plasmaarcfurnace.tier.12",
                "Epic Plasma Discharge Heater IV",
                12,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE",
                null).getStackForm(1L));
    }

    private void registerCanningMachine() {
        ItemList.Machine_LV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNER_LV.ID,
                "basicmachine.canner.tier.01",
                "Basic Canning Machine",
                1,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.Machine_MV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNER_MV.ID,
                "basicmachine.canner.tier.02",
                "Advanced Canning Machine",
                2,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.Machine_HV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNER_HV.ID,
                "basicmachine.canner.tier.03",
                "Advanced Canning Machine II",
                3,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.Machine_EV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNER_EV.ID,
                "basicmachine.canner.tier.04",
                "Advanced Canning Machine III",
                4,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.Machine_IV_Canner.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNER_IV.ID,
                "basicmachine.canner.tier.05",
                "Advanced Canning Machine IV",
                5,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.CanningMachineLuV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNING_MACHINE_LuV.ID,
                "basicmachine.canner.tier.06",
                "Elite Canning Machine",
                6,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.CanningMachineZPM.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNING_MACHINE_ZPM.ID,
                "basicmachine.canner.tier.07",
                "Elite Canning Machine II",
                7,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.CanningMachineUV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNING_MACHINE_UV.ID,
                "basicmachine.canner.tier.08",
                "Ultimate Can Operator",
                8,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.CanningMachineUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNING_MACHINE_UHV.ID,
                "basicmachine.canner.tier.09",
                "Epic Can Operator",
                9,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.CanningMachineUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNING_MACHINE_UEV.ID,
                "basicmachine.canner.tier.10",
                "Epic Can Operator II",
                10,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.CanningMachineUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNING_MACHINE_UIV.ID,
                "basicmachine.canner.tier.11",
                "Epic Can Operator III",
                11,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));

        ItemList.CanningMachineUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CANNING_MACHINE_UMV.ID,
                "basicmachine.canner.tier.12",
                "Epic Can Operator IV",
                12,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CANNER",
                null).getStackForm(1L));
    }

    private static void registerDynamoHatch() {
        ItemList.Hatch_Dynamo_ULV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_ULV.ID, "hatch.dynamo.tier.00", "ULV Dynamo Hatch", 0)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_LV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_LV.ID, "hatch.dynamo.tier.01", "LV Dynamo Hatch", 1)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_MV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_MV.ID, "hatch.dynamo.tier.02", "MV Dynamo Hatch", 2)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_HV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_HV.ID, "hatch.dynamo.tier.03", "HV Dynamo Hatch", 3)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_EV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_EV.ID, "hatch.dynamo.tier.04", "EV Dynamo Hatch", 4)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_IV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_IV.ID, "hatch.dynamo.tier.05", "IV Dynamo Hatch", 5)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_LuV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_LuV.ID, "hatch.dynamo.tier.06", "LuV Dynamo Hatch", 6)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_ZPM.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_ZPM.ID, "hatch.dynamo.tier.07", "ZPM Dynamo Hatch", 7)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_UV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_UV.ID, "hatch.dynamo.tier.08", "UV Dynamo Hatch", 8)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_UHV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_UHV.ID, "hatch.dynamo.tier.09", "UHV Dynamo Hatch", 9)
                .getStackForm(1L));
        ItemList.Hatch_Dynamo_UEV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_UEV.ID, "hatch.dynamo.tier.10", "UEV Dynamo Hatch", 10)
                .getStackForm(1L));

        ItemList.Hatch_Dynamo_UIV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_UIV.ID, "hatch.dynamo.tier.11", "UIV Dynamo Hatch", 11)
                .getStackForm(1L));

        ItemList.Hatch_Dynamo_UMV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_UMV.ID, "hatch.dynamo.tier.12", "UMV Dynamo Hatch", 12)
                .getStackForm(1L));

        ItemList.Hatch_Dynamo_UXV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_UXV.ID, "hatch.dynamo.tier.13", "UXV Dynamo Hatch", 13)
                .getStackForm(1L));
    }

    private static void registerEnergyHatch() {
        ItemList.Hatch_Energy_ULV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_ULV.ID, "hatch.energy.tier.00", "ULV Energy Hatch", 0)
                .getStackForm(1L));
        ItemList.Hatch_Energy_LV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_LV.ID, "hatch.energy.tier.01", "LV Energy Hatch", 1)
                .getStackForm(1L));
        ItemList.Hatch_Energy_MV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_MV.ID, "hatch.energy.tier.02", "MV Energy Hatch", 2)
                .getStackForm(1L));
        ItemList.Hatch_Energy_HV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_HV.ID, "hatch.energy.tier.03", "HV Energy Hatch", 3)
                .getStackForm(1L));
        ItemList.Hatch_Energy_EV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_EV.ID, "hatch.energy.tier.04", "EV Energy Hatch", 4)
                .getStackForm(1L));
        ItemList.Hatch_Energy_IV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_IV.ID, "hatch.energy.tier.05", "IV Energy Hatch", 5)
                .getStackForm(1L));
        ItemList.Hatch_Energy_LuV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_LuV.ID, "hatch.energy.tier.06", "LuV Energy Hatch", 6)
                .getStackForm(1L));
        ItemList.Hatch_Energy_ZPM.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_ZPM.ID, "hatch.energy.tier.07", "ZPM Energy Hatch", 7)
                .getStackForm(1L));
        ItemList.Hatch_Energy_UV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_UV.ID, "hatch.energy.tier.08", "UV Energy Hatch", 8)
                .getStackForm(1L));
        ItemList.Hatch_Energy_UHV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_UHV.ID, "hatch.energy.tier.09", "UHV Energy Hatch", 9)
                .getStackForm(1L));
        ItemList.Hatch_Energy_UEV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_UEV.ID, "hatch.energy.tier.10", "UEV Energy Hatch", 10)
                .getStackForm(1L));

        ItemList.Hatch_Energy_UIV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_UIV.ID, "hatch.energy.tier.11", "UIV Energy Hatch", 11)
                .getStackForm(1L));

        ItemList.Hatch_Energy_UMV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_UMV.ID, "hatch.energy.tier.12", "UMV Energy Hatch", 12)
                .getStackForm(1L));

        ItemList.Hatch_Energy_UXV.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_UXV.ID, "hatch.energy.tier.13", "UXV Energy Hatch", 13)
                .getStackForm(1L));
    }

    private static void registerInputHatch() {
        ItemList.Hatch_Input_ULV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_ULV.ID, "hatch.input.tier.00", "Input Hatch (ULV)", 0)
                .getStackForm(1L));
        ItemList.Hatch_Input_LV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_LV.ID, "hatch.input.tier.01", "Input Hatch (LV)", 1)
                .getStackForm(1L));
        ItemList.Hatch_Input_MV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_MV.ID, "hatch.input.tier.02", "Input Hatch (MV)", 2)
                .getStackForm(1L));
        ItemList.Hatch_Input_HV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_HV.ID, "hatch.input.tier.03", "Input Hatch (HV)", 3)
                .getStackForm(1L));
        ItemList.Hatch_Input_EV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_EV.ID, "hatch.input.tier.04", "Input Hatch (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Input_IV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_IV.ID, "hatch.input.tier.05", "Input Hatch (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Input_LuV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_LuV.ID, "hatch.input.tier.06", "Input Hatch (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Input_ZPM.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_ZPM.ID, "hatch.input.tier.07", "Input Hatch (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Input_UV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_UV.ID, "hatch.input.tier.08", "Input Hatch (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Input_UHV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_UHV.ID, "hatch.input.tier.09", "Input Hatch (UHV)", 9)
                .getStackForm(1L));
        ItemList.Hatch_Input_UEV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_UEV.ID, "hatch.input.tier.10", "Input Hatch (UEV)", 10)
                .getStackForm(1L));
        ItemList.Hatch_Input_UIV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_UIV.ID, "hatch.input.tier.11", "Input Hatch (UIV)", 11)
                .getStackForm(1L));
        ItemList.Hatch_Input_UMV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_UMV.ID, "hatch.input.tier.12", "Input Hatch (UMV)", 12)
                .getStackForm(1L));
        ItemList.Hatch_Input_UXV.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_UXV.ID, "hatch.input.tier.13", "Input Hatch (UXV)", 13)
                .getStackForm(1L));
        ItemList.Hatch_Input_MAX.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_MAX.ID, "hatch.input.tier.14", "Input Hatch (MAX)", 14)
                .getStackForm(1L));
    }

    private static void registerQuadrupleInputHatch() {
        ItemList.Hatch_Input_Multi_2x2_EV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_EV.ID,
                4,
                "hatch.multi.input.tier.01",
                "Quadruple Input Hatch (EV)",
                4).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_IV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_IV.ID,
                4,
                "hatch.multi.input.tier.02",
                "Quadruple Input Hatch (IV)",
                5).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_LuV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_LuV.ID,
                4,
                "hatch.multi.input.tier.03",
                "Quadruple Input Hatch (LuV)",
                6).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_ZPM.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_ZPM.ID,
                4,
                "hatch.multi.input.tier.04",
                "Quadruple Input Hatch (ZPM)",
                7).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_UV.ID,
                4,
                "hatch.multi.input.tier.05",
                "Quadruple Input Hatch (UV)",
                8).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UHV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_UHV.ID,
                4,
                "hatch.multi.input.tier.06",
                "Quadruple Input Hatch (UHV)",
                9).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UEV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_UEV.ID,
                4,
                "hatch.multi.input.tier.07",
                "Quadruple Input Hatch (UEV)",
                10).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UIV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_UIV.ID,
                4,
                "hatch.multi.input.tier.08",
                "Quadruple Input Hatch (UIV)",
                11).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UMV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_UMV.ID,
                4,
                "hatch.multi.input.tier.09",
                "Quadruple Input Hatch (UMV)",
                12).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UXV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                QUADRUPLE_INPUT_HATCHES_UXV.ID,
                4,
                "hatch.multi.input.tier.10",
                "Quadruple Input Hatch (UXV)",
                13).getStackForm(1L));

        ItemList.Hatch_Input_Multi_2x2_Humongous.set(
            new GT_MetaTileEntity_Hatch_QuadrupleHumongous(
                QUADRUPLE_INPUT_HATCHES_MAX.ID,
                4,
                "hatch.multi.input.tier.11",
                "Humongous Quadruple Input Hatch").getStackForm(1L));
    }

    private static void registerOutputHatch() {
        ItemList.Hatch_Output_ULV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_ULV.ID, "hatch.output.tier.00", "Output Hatch (ULV)", 0)
                .getStackForm(1L));
        ItemList.Hatch_Output_LV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_LV.ID, "hatch.output.tier.01", "Output Hatch (LV)", 1)
                .getStackForm(1L));
        ItemList.Hatch_Output_MV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_MV.ID, "hatch.output.tier.02", "Output Hatch (MV)", 2)
                .getStackForm(1L));
        ItemList.Hatch_Output_HV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_HV.ID, "hatch.output.tier.03", "Output Hatch (HV)", 3)
                .getStackForm(1L));
        ItemList.Hatch_Output_EV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_EV.ID, "hatch.output.tier.04", "Output Hatch (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Output_IV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_IV.ID, "hatch.output.tier.05", "Output Hatch (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Output_LuV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_LuV.ID, "hatch.output.tier.06", "Output Hatch (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Output_ZPM.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_ZPM.ID, "hatch.output.tier.07", "Output Hatch (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Output_UV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_UV.ID, "hatch.output.tier.08", "Output Hatch (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Output_UHV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_UHV.ID, "hatch.output.tier.09", "Output Hatch (UHV)", 9)
                .getStackForm(1L));
        ItemList.Hatch_Output_UEV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_UEV.ID, "hatch.output.tier.10", "Output Hatch (UEV)", 10)
                .getStackForm(1L));
        ItemList.Hatch_Output_UIV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_UIV.ID, "hatch.output.tier.11", "Output Hatch (UIV)", 11)
                .getStackForm(1L));
        ItemList.Hatch_Output_UMV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_UMV.ID, "hatch.output.tier.12", "Output Hatch (UMV)", 12)
                .getStackForm(1L));
        ItemList.Hatch_Output_UXV.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_UXV.ID, "hatch.output.tier.13", "Output Hatch (UXV)", 13)
                .getStackForm(1L));
        ItemList.Hatch_Output_MAX.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_MAX.ID, "hatch.output.tier.14", "Output Hatch (MAX)", 14)
                .getStackForm(1L));
    }

    private static void registerQuantumTank() {
        ItemList.Quantum_Tank_LV.set(
            new GT_MetaTileEntity_QuantumTank(QUANTUM_TANK_LV.ID, "quantum.tank.tier.06", "Quantum Tank I", 6)
                .getStackForm(1L));
        ItemList.Quantum_Tank_MV.set(
            new GT_MetaTileEntity_QuantumTank(QUANTUM_TANK_MV.ID, "quantum.tank.tier.07", "Quantum Tank II", 7)
                .getStackForm(1L));
        ItemList.Quantum_Tank_HV.set(
            new GT_MetaTileEntity_QuantumTank(QUANTUM_TANK_HV.ID, "quantum.tank.tier.08", "Quantum Tank III", 8)
                .getStackForm(1L));
        ItemList.Quantum_Tank_EV.set(
            new GT_MetaTileEntity_QuantumTank(QUANTUM_TANK_EV.ID, "quantum.tank.tier.09", "Quantum Tank IV", 9)
                .getStackForm(1L));
        ItemList.Quantum_Tank_IV.set(
            new GT_MetaTileEntity_QuantumTank(QUANTUM_TANK_IV.ID, "quantum.tank.tier.10", "Quantum Tank V", 10)
                .getStackForm(1L));
    }

    private static void registerQuantumChest() {
        ItemList.Quantum_Chest_LV.set(
            new GT_MetaTileEntity_QuantumChest(QUANTUM_CHEST_LV.ID, "quantum.chest.tier.06", "Quantum Chest I", 6)
                .getStackForm(1L));
        ItemList.Quantum_Chest_MV.set(
            new GT_MetaTileEntity_QuantumChest(QUANTUM_CHEST_MV.ID, "quantum.chest.tier.07", "Quantum Chest II", 7)
                .getStackForm(1L));
        ItemList.Quantum_Chest_HV.set(
            new GT_MetaTileEntity_QuantumChest(QUANTUM_CHEST_HV.ID, "quantum.chest.tier.08", "Quantum Chest III", 8)
                .getStackForm(1L));
        ItemList.Quantum_Chest_EV.set(
            new GT_MetaTileEntity_QuantumChest(QUANTUM_CHEST_EV.ID, "quantum.chest.tier.09", "Quantum Chest IV", 9)
                .getStackForm(1L));
        ItemList.Quantum_Chest_IV.set(
            new GT_MetaTileEntity_QuantumChest(QUANTUM_CHEST_IV.ID, "quantum.chest.tier.10", "Quantum Chest V", 10)
                .getStackForm(1L));
    }

    private static void registerSuperTank() {
        ItemList.Super_Tank_LV.set(
            new GT_MetaTileEntity_SuperTank(SUPER_TANK_LV.ID, "super.tank.tier.01", "Super Tank I", 1)
                .getStackForm(1L));
        ItemList.Super_Tank_MV.set(
            new GT_MetaTileEntity_SuperTank(SUPER_TANK_MV.ID, "super.tank.tier.02", "Super Tank II", 2)
                .getStackForm(1L));
        ItemList.Super_Tank_HV.set(
            new GT_MetaTileEntity_SuperTank(SUPER_TANK_HV.ID, "super.tank.tier.03", "Super Tank III", 3)
                .getStackForm(1L));
        ItemList.Super_Tank_EV.set(
            new GT_MetaTileEntity_SuperTank(SUPER_TANK_EV.ID, "super.tank.tier.04", "Super Tank IV", 4)
                .getStackForm(1L));
        ItemList.Super_Tank_IV.set(
            new GT_MetaTileEntity_SuperTank(SUPER_TANK_IV.ID, "super.tank.tier.05", "Super Tank V", 5)
                .getStackForm(1L));
    }

    private static void registerSuperChest() {
        ItemList.Super_Chest_LV.set(
            new GT_MetaTileEntity_SuperChest(SUPER_CHEST_LV.ID, "super.chest.tier.01", "Super Chest I", 1)
                .getStackForm(1L));
        ItemList.Super_Chest_MV.set(
            new GT_MetaTileEntity_SuperChest(SUPER_CHEST_MV.ID, "super.chest.tier.02", "Super Chest II", 2)
                .getStackForm(1L));
        ItemList.Super_Chest_HV.set(
            new GT_MetaTileEntity_SuperChest(SUPER_CHEST_HV.ID, "super.chest.tier.03", "Super Chest III", 3)
                .getStackForm(1L));
        ItemList.Super_Chest_EV.set(
            new GT_MetaTileEntity_SuperChest(SUPER_CHEST_EV.ID, "super.chest.tier.04", "Super Chest IV", 4)
                .getStackForm(1L));
        ItemList.Super_Chest_IV.set(
            new GT_MetaTileEntity_SuperChest(SUPER_CHEST_IV.ID, "super.chest.tier.05", "Super Chest V", 5)
                .getStackForm(1L));
    }

    private static void registerLongDistancePipe() {
        ItemList.Long_Distance_Pipeline_Fluid.set(
            new GT_MetaTileEntity_LongDistancePipelineFluid(
                LONG_DISTANCE_PIPELINE_FLUID.ID,
                "long.distance.pipeline.fluid",
                "Long Distance Fluid Pipeline",
                1).getStackForm(1L));
        ItemList.Long_Distance_Pipeline_Item.set(
            new GT_MetaTileEntity_LongDistancePipelineItem(
                LONG_DISTANCE_PIPELINE_ITEM.ID,
                "long.distance.pipeline.item",
                "Long Distance Item Pipeline",
                1).getStackForm(1L));
    }

    private static void registerAE2Hatches() {
        ItemList.Hatch_Output_Bus_ME.set(
            new GT_MetaTileEntity_Hatch_OutputBus_ME(OUTPUT_BUS_ME.ID, "hatch.output_bus.me", "Output Bus (ME)")
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_ME.set(
            new GT_MetaTileEntity_Hatch_InputBus_ME(
                INPUT_BUS_ME.ID,
                false,
                "hatch.input_bus.me.basic",
                "Stocking Input Bus (ME)").getStackForm(1L));
        ItemList.Hatch_Input_Bus_ME_Advanced.set(
            new GT_MetaTileEntity_Hatch_InputBus_ME(
                INPUT_BUS_ME_ADVANCED.ID,
                true,
                "hatch.input_bus.me",
                "Advanced Stocking Input Bus (ME)").getStackForm(1L));
        ItemList.Hatch_Input_ME.set(
            new GT_MetaTileEntity_Hatch_Input_ME(
                INPUT_HATCH_ME.ID,
                false,
                "hatch.input.me.basic",
                "Stocking Input Hatch (ME)").getStackForm(1L));
        ItemList.Hatch_Input_ME_Advanced.set(
            new GT_MetaTileEntity_Hatch_Input_ME(
                INPUT_HATCH_ME_ADVANCED.ID,
                true,
                "hatch.input.me",
                "Advanced Stocking Input Hatch (ME)").getStackForm(1L));
        ItemList.Hatch_Output_ME.set(
            new GT_MetaTileEntity_Hatch_Output_ME(OUTPUT_HATCH_ME.ID, "hatch.output.me", "Output Hatch (ME)")
                .getStackForm(1L));
        ItemList.Hatch_CraftingInput_Bus_ME.set(
            new GT_MetaTileEntity_Hatch_CraftingInput_ME(
                CRAFTING_INPUT_ME.ID,
                "hatch.crafting_input.me",
                "Crafting Input Buffer (ME)",
                true).getStackForm(1L));
        ItemList.Hatch_CraftingInput_Bus_ME_ItemOnly.set(
            new GT_MetaTileEntity_Hatch_CraftingInput_ME(
                CRAFTING_INPUT_ME_BUS.ID,
                "hatch.crafting_input.me.item_only",
                "Crafting Input Bus (ME)",
                false).getStackForm(1L));
        ItemList.Hatch_CraftingInput_Bus_Slave.set(
            new GT_MetaTileEntity_Hatch_CraftingInput_Slave(
                CRAFTING_INPUT_SLAVE.ID,
                "hatch.crafting_input.proxy",
                "Crafting Input Proxy").getStackForm(1L));
    }

    private static void registerMagHatch() {
        ItemList.Hatch_Electromagnet.set(
            new GT_MetaTileEntity_MagHatch(MAG_HATCH.ID, "hatch.mag_hatch", "Electromagnet Housing").getStackForm(1L));
    }

    private static void registerInputBus() {
        ItemList.Hatch_Input_Bus_ULV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_ULV.ID, "hatch.input_bus.tier.00", "Input Bus (ULV)", 0)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_LV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_LV.ID, "hatch.input_bus.tier.01", "Input Bus (LV)", 1)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_MV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_MV.ID, "hatch.input_bus.tier.02", "Input Bus (MV)", 2)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_HV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_HV.ID, "hatch.input_bus.tier.03", "Input Bus (HV)", 3)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_EV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_EV.ID, "hatch.input_bus.tier.04", "Input Bus (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_IV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_IV.ID, "hatch.input_bus.tier.05", "Input Bus (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_LuV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_LuV.ID, "hatch.input_bus.tier.06", "Input Bus (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_ZPM.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_ZPM.ID, "hatch.input_bus.tier.07", "Input Bus (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_UV.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_UV.ID, "hatch.input_bus.tier.08", "Input Bus (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_MAX.set(
            new GT_MetaTileEntity_Hatch_InputBus(INPUT_BUS_UHV.ID, "hatch.input_bus.tier.09", "Input Bus (UHV)", 9)
                .getStackForm(1L));
    }

    private static void registerOutputBus() {
        ItemList.Hatch_Output_Bus_ULV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_ULV.ID, "hatch.output_bus.tier.00", "Output Bus (ULV)", 0)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_LV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_LV.ID, "hatch.output_bus.tier.01", "Output Bus (LV)", 1)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_MV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_MV.ID, "hatch.output_bus.tier.02", "Output Bus (MV)", 2)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_HV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_HV.ID, "hatch.output_bus.tier.03", "Output Bus (HV)", 3)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_EV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_EV.ID, "hatch.output_bus.tier.04", "Output Bus (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_IV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_IV.ID, "hatch.output_bus.tier.05", "Output Bus (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_LuV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_LuV.ID, "hatch.output_bus.tier.06", "Output Bus (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_ZPM.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_ZPM.ID, "hatch.output_bus.tier.07", "Output Bus (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_UV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_UV.ID, "hatch.output_bus.tier.08", "Output Bus (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_MAX.set(
            new GT_MetaTileEntity_Hatch_OutputBus(OUTPUT_BUS_UHV.ID, "hatch.output_bus.tier.09", "Output Bus (UHV)", 9)
                .getStackForm(1L));
    }

    private static void registerMufflerHatch() {
        ItemList.Hatch_Muffler_LV.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_LV.ID, "hatch.muffler.tier.01", "Muffler Hatch (LV)", 1)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_MV.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_MV.ID, "hatch.muffler.tier.02", "Muffler Hatch (MV)", 2)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_HV.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_HV.ID, "hatch.muffler.tier.03", "Muffler Hatch (HV)", 3)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_EV.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_EV.ID, "hatch.muffler.tier.04", "Muffler Hatch (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_IV.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_IV.ID, "hatch.muffler.tier.05", "Muffler Hatch (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_LuV.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_LuV.ID, "hatch.muffler.tier.06", "Muffler Hatch (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_ZPM.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_ZPM.ID, "hatch.muffler.tier.07", "Muffler Hatch (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_UV.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_UV.ID, "hatch.muffler.tier.08", "Muffler Hatch (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_MAX.set(
            new GT_MetaTileEntity_Hatch_Muffler(MUFFLER_HATCH_UHV.ID, "hatch.muffler.tier.09", "Muffler Hatch (UHV)", 9)
                .getStackForm(1L));
    }

    private static void registerBoiler() {
        ItemList.Machine_Bronze_Boiler.set(
            new GT_MetaTileEntity_Boiler_Bronze(SMALL_COAL_BOILER.ID, "boiler.bronze", "Small Coal Boiler")
                .getStackForm(1L));
        ItemList.Machine_Steel_Boiler.set(
            new GT_MetaTileEntity_Boiler_Steel(
                HIGH_PRESSURE_COAL_BOILER.ID,
                "boiler.steel",
                "High Pressure Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler_Lava.set(
            new GT_MetaTileEntity_Boiler_Lava(HIGH_PRESSURE_LAVA_BOILER.ID, "boiler.lava", "High Pressure Lava Boiler")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Boiler_Solar.set(
            new GT_MetaTileEntity_Boiler_Solar(SIMPLE_SOLAR_BOILER.ID, "boiler.solar", "Simple Solar Boiler")
                .getStackForm(1L));
        ItemList.Machine_HP_Solar.set(
            new GT_MetaTileEntity_Boiler_Solar_Steel(
                HIGH_PRESSURE_SOLAR_BOILER.ID,
                "boiler.steel.solar",
                "High Pressure Solar Boiler").getStackForm(1L));
    }

    private static void registerBatteryBuffer1x1() {
        ItemList.Battery_Buffer_1by1_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_ULV.ID,
                "batterybuffer.01.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_LV.ID,
                "batterybuffer.01.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_MV.ID,
                "batterybuffer.01.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_HV.ID,
                "batterybuffer.01.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_EV.ID,
                "batterybuffer.01.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_IV.ID,
                "batterybuffer.01.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_LuV.ID,
                "batterybuffer.01.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_ZPM.ID,
                "batterybuffer.01.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UV.ID,
                "batterybuffer.01.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UHV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UHV.ID,
                "batterybuffer.01.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UEV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UEV.ID,
                "batterybuffer.01.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UIV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UIV.ID,
                "batterybuffer.01.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UMV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UMV.ID,
                "batterybuffer.01.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UXV.ID,
                "batterybuffer.01.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_MAXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_MAX.ID,
                "batterybuffer.01.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                1).getStackForm(1L));
    }

    private static void registerBatteryBuffer2x2() {
        ItemList.Battery_Buffer_2by2_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_ULV.ID,
                "batterybuffer.04.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_LV.ID,
                "batterybuffer.04.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_MV.ID,
                "batterybuffer.04.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_HV.ID,
                "batterybuffer.04.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_EV.ID,
                "batterybuffer.04.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_IV.ID,
                "batterybuffer.04.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_LuV.ID,
                "batterybuffer.04.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_ZPM.ID,
                "batterybuffer.04.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UV.ID,
                "batterybuffer.04.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UHV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UHV.ID,
                "batterybuffer.04.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UEV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UEV.ID,
                "batterybuffer.04.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UIV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UIV.ID,
                "batterybuffer.04.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UMV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UMV.ID,
                "batterybuffer.04.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UXV.ID,
                "batterybuffer.04.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_MAXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_MAX.ID,
                "batterybuffer.04.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                4).getStackForm(1L));
    }

    private static void registerBatteryBuffer3x3() {
        ItemList.Battery_Buffer_3by3_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_ULV.ID,
                "batterybuffer.09.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_LV.ID,
                "batterybuffer.09.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_MV.ID,
                "batterybuffer.09.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_HV.ID,
                "batterybuffer.09.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_EV.ID,
                "batterybuffer.09.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_IV.ID,
                "batterybuffer.09.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_LuV.ID,
                "batterybuffer.09.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_ZPM.ID,
                "batterybuffer.09.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UV.ID,
                "batterybuffer.09.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UHV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UHV.ID,
                "batterybuffer.09.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UEV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UEV.ID,
                "batterybuffer.09.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UIV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UIV.ID,
                "batterybuffer.09.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UMV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UMV.ID,
                "batterybuffer.09.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UXV.ID,
                "batterybuffer.09.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_MAXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_MAX.ID,
                "batterybuffer.09.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                9).getStackForm(1L));
    }

    private static void registerBatteryBuffer4x4() {
        ItemList.Battery_Buffer_4by4_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_ULV.ID,
                "batterybuffer.16.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_LV.ID,
                "batterybuffer.16.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_MV.ID,
                "batterybuffer.16.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_HV.ID,
                "batterybuffer.16.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_EV.ID,
                "batterybuffer.16.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_IV.ID,
                "batterybuffer.16.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_LuV.ID,
                "batterybuffer.16.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_ZPM.ID,
                "batterybuffer.16.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UV.ID,
                "batterybuffer.16.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UHV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UHV.ID,
                "batterybuffer.16.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UEV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UEV.ID,
                "batterybuffer.16.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_UIV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UIV.ID,
                "batterybuffer.16.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_UMV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UMV.ID,
                "batterybuffer.16.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_UXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UXV.ID,
                "batterybuffer.16.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_MAXV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_MAX.ID,
                "batterybuffer.16.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                16).getStackForm(1L));
    }

    private static void registerCharger4x4() {
        ItemList.Battery_Charger_4by4_ULV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_ULV.ID,
                "batterycharger.16.tier.00",
                "Ultra Low Voltage Battery Charger",
                0,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_LV.ID,
                "batterycharger.16.tier.01",
                "Low Voltage Battery Charger",
                1,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_MV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_MV.ID,
                "batterycharger.16.tier.02",
                "Medium Voltage Battery Charger",
                2,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_HV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_HV.ID,
                "batterycharger.16.tier.03",
                "High Voltage Battery Charger",
                3,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_EV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_EV.ID,
                "batterycharger.16.tier.04",
                "Extreme Voltage Battery Charger",
                4,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_IV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_IV.ID,
                "batterycharger.16.tier.05",
                "Insane Voltage Battery Charger",
                5,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LuV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_LuV.ID,
                "batterycharger.16.tier.06",
                "Ludicrous Voltage Battery Charger",
                6,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_ZPM.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_ZPM.ID,
                "batterycharger.16.tier.07",
                "ZPM Voltage Battery Charger",
                7,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_UV.ID,
                "batterycharger.16.tier.08",
                "Ultimate Voltage Battery Charger",
                8,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UHV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_UHV.ID,
                "batterycharger.16.tier.09",
                "Highly Ultimate Voltage Battery Charger",
                9,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UEV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_4_UEV.ID,
                "batterycharger.16.tier.10",
                "Extremely Ultimate Battery Charger",
                10,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UIV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_4_UIV.ID,
                "batterycharger.16.tier.11",
                "Insanely Ultimate Battery Charger",
                11,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UMV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_4_UMV.ID,
                "batterycharger.16.tier.12",
                "Mega Ultimate Battery Charger",
                12,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UXV.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_4_UXV.ID,
                "batterycharger.16.tier.13",
                "Extended Mega Ultimate Battery Charger",
                13,
                "",
                4).getStackForm(1L));
    }

    private void registerCircuitAssembler() {
        ItemList.Machine_LV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_LV.ID,
                "basicmachine.circuitassembler.tier.01",
                "Basic Circuit Assembler",
                1,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_MV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_MV.ID,
                "basicmachine.circuitassembler.tier.02",
                "Advanced Circuit Assembler",
                2,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_HV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_HV.ID,
                "basicmachine.circuitassembler.tier.03",
                "Advanced Circuit Assembler II",
                3,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_EV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_EV.ID,
                "basicmachine.circuitassembler.tier.04",
                "Advanced Circuit Assembler III",
                4,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_IV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_IV.ID,
                "basicmachine.circuitassembler.tier.05",
                "Advanced Circuit Assembler IV",
                5,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_LuV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_LuV.ID,
                "basicmachine.circuitassembler.tier.06",
                "Advanced Circuit Assembler V",
                6,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_ZPM_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_ZPM.ID,
                "basicmachine.circuitassembler.tier.07",
                "Advanced Circuit Assembler VI",
                7,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.Machine_UV_CircuitAssembler.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_UV.ID,
                "basicmachine.circuitassembler.tier.08",
                "Advanced Circuit Assembler VII",
                8,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.CircuitAssemblerUHV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_UHV.ID,
                "basicmachine.circuitassembler.tier.09",
                "Ultimate Circuit Assembling Machine",
                9,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.CircuitAssemblerUEV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_UEV.ID,
                "basicmachine.circuitassembler.tier.10",
                "Ultimate Circuit Assembling Machine II",
                10,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.CircuitAssemblerUIV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_UIV.ID,
                "basicmachine.circuitassembler.tier.11",
                "Ultimate Circuit Assembling Machine III",
                11,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.CircuitAssemblerUMV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_UMV.ID,
                "basicmachine.circuitassembler.tier.12",
                "Ultimate Circuit Assembling Machine IV",
                12,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.CircuitAssemblerUXV.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_UXV.ID,
                "basicmachine.circuitassembler.tier.13",
                "Ultimate Circuit Assembling Machine V",
                13,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));

        ItemList.CircuitAssemblerMAX.set(
            new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                CIRCUIT_ASSEMBLER_MAX.ID,
                "basicmachine.circuitassembler.tier.14",
                "MAX Circuit Assembling Machine",
                14,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.NONE,
                GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER",
                null).getStackForm(1L));
    }

    private void registerWetTransformer() {
        ItemList.WetTransformer_LV_ULV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_LV_ULV.ID,
                "wettransformer.tier.00",
                "Ultra Low Voltage Power Transformer",
                0,
                "LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_MV_LV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_MV_LV.ID,
                "wetransformer.tier.01",
                "Low Voltage Power Transformer",
                1,
                "MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_HV_MV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_HV_MV.ID,
                "wettransformer.tier.02",
                "Medium Voltage Power Transformer",
                2,
                "HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_EV_HV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_EV_HV.ID,
                "wettransformer.tier.03",
                "High Voltage Power Transformer",
                3,
                "EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_IV_EV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_IV_EV.ID,
                "wettransformer.tier.04",
                "Extreme Power Transformer",
                4,
                "IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_LuV_IV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_LuV_IV.ID,
                "wettransformer.tier.05",
                "Insane Power Transformer",
                5,
                "LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_ZPM_LuV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_ZPM_LuV.ID,
                "wettransformer.tier.06",
                "Ludicrous Power Transformer",
                6,
                "ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UV_ZPM.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_UV_ZPM.ID,
                "wettransformer.tier.07",
                "ZPM Voltage Power Transformer",
                7,
                "UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UHV_UV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_UHV_UV.ID,
                "wettransformer.tier.08",
                "Ultimate Power Transformer",
                8,
                "UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UEV_UHV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_UEV_UHV.ID,
                "wettransformer.tier.09",
                "Highly Ultimate Power Transformer",
                9,
                "UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UIV_UEV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_UIV_UEV.ID,
                "wettransformer.tier.10",
                "Extremely Ultimate Power Transformer",
                10,
                "UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UMV_UIV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_UMV_UIV.ID,
                "wettransformer.tier.11",
                "Insanely Ultimate Power Transformer",
                11,
                "UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UXV_UMV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_UXV_UMV.ID,
                "wettransformer.tier.12",
                "Mega Ultimate Power Transformer",
                12,
                "UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_MAX_UXV.set(
            new GT_MetaTileEntity_WetTransformer(
                WET_TRANSFORMER_MAX_UXV.ID,
                "wettransformer.tier.13",
                "Extended Mega Ultimate Power Transformer",
                13,
                "MAX -> UXV (Use Soft Mallet to invert)").getStackForm(1L));
    }

    private void registerHighAmpTransformer() {
        ItemList.Transformer_HA_UEV_UHV.set(
            new GregtechMetaTransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UEV_UHV.ID,
                "transformer.ha.tier.09",
                "Highly Ultimate Hi-Amp Transformer",
                9,
                "UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_UIV_UEV.set(
            new GregtechMetaTransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UIV_UEV.ID,
                "transformer.ha.tier.10",
                "Extremely Ultimate Hi-Amp Transformer",
                10,
                "UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_UMV_UIV.set(
            new GregtechMetaTransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UMV_UIV.ID,
                "transformer.ha.tier.11",
                "Insanely Ultimate Hi-Amp Transformer",
                11,
                "UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_UXV_UMV.set(
            new GregtechMetaTransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UXV_UMV.ID,
                "transformer.ha.tier.12",
                "Mega Ultimate Hi-Amp Transformer",
                12,
                "UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_MAX_UXV.set(
            new GregtechMetaTransformerHiAmp(
                HIGH_AMP_TRANSFORMER_MAX_UXV.ID,
                "transformer.ha.tier.13",
                "Extended Mega Ultimate Hi-Amp Transformer",
                13,
                "MAX -> UXV (Use Soft Mallet to invert)").getStackForm(1L));

    }

    private void registerTurboCharger4By4() {
        ItemList.Battery_TurboCharger_4by4_ULV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_ULV.ID,
                "batteryturbocharger.16.tier.00",
                "Ultra Low Voltage Turbo Charger",
                0,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_LV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_LV.ID,
                "batteryturbocharger.16.tier.01",
                "Low Voltage Turbo Charger",
                1,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_MV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_MV.ID,
                "batteryturbocharger.16.tier.02",
                "Medium Voltage Turbo Charger",
                2,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_HV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_HV.ID,
                "batteryturbocharger.16.tier.03",
                "High Voltage Turbo Charger",
                3,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_EV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_EV.ID,
                "batteryturbocharger.16.tier.04",
                "Extreme Voltage Turbo Charger",
                4,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_IV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_IV.ID,
                "batteryturbocharger.16.tier.05",
                "Insane Voltage Turbo Charger",
                5,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_LuV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_LuV.ID,
                "batteryturbocharger.16.tier.06",
                "Ludicrous Voltage Turbo Charger",
                6,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_ZPM.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_ZPM.ID,
                "batteryturbocharger.16.tier.07",
                "ZPM Voltage Turbo Charger",
                7,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_UV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_UV.ID,
                "batteryturbocharger.16.tier.08",
                "Ultimate Voltage Turbo Charger",
                8,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_UHV.set(
            new GT_MetaTileEntity_TurboCharger(
                TURBO_CHARGER_UHV.ID,
                "batteryturbocharger.16.tier.09",
                "Highly Ultimate Voltage Turbo Charger",
                9,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

    }

    private static void registerWirelessEnergyHatch() {
        ItemList.Wireless_Hatch_Energy_ULV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_ULV.ID,
                "hatch.wireless.receiver.tier.00",
                "ULV Wireless Energy Hatch",
                0).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_LV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_LV.ID,
                "hatch.wireless.receiver.tier.01",
                "LV Wireless Energy Hatch",
                1).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_MV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_MV.ID,
                "hatch.wireless.receiver.tier.02",
                "MV Wireless Energy Hatch",
                2).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_HV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_HV.ID,
                "hatch.wireless.receiver.tier.03",
                "HV Wireless Energy Hatch",
                3).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_EV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_EV.ID,
                "hatch.wireless.receiver.tier.04",
                "EV Wireless Energy Hatch",
                4).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_IV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_IV.ID,
                "hatch.wireless.receiver.tier.05",
                "IV Wireless Energy Hatch",
                5).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_LuV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_LuV.ID,
                "hatch.wireless.receiver.tier.06",
                "LuV Wireless Energy Hatch",
                6).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_ZPM.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_ZPM.ID,
                "hatch.wireless.receiver.tier.07",
                "ZPM Wireless Energy Hatch",
                7).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_UV.ID,
                "hatch.wireless.receiver.tier.08",
                "UV Wireless Energy Hatch",
                8).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UHV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_UHV.ID,
                "hatch.wireless.receiver.tier.09",
                "UHV Wireless Energy Hatch",
                9).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UEV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_UEV.ID,
                "hatch.wireless.receiver.tier.10",
                "UEV Wireless Energy Hatch",
                10).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UIV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_UIV.ID,
                "hatch.wireless.receiver.tier.11",
                "UIV Wireless Energy Hatch",
                11).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UMV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_UMV.ID,
                "hatch.wireless.receiver.tier.12",
                "UMV Wireless Energy Hatch",
                12).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UXV.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_UXV.ID,
                "hatch.wireless.receiver.tier.13",
                "UXV Wireless Energy Hatch",
                13).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_MAX.set(
            new GT_MetaTileEntity_Wireless_Hatch(
                WIRELESS_HATCH_ENERGY_MAX.ID,
                "hatch.wireless.receiver.tier.14",
                "MAX Wireless Energy Hatch",
                14).getStackForm(1L));
    }

    private static void registerWirelessDynamoHatch() {
        ItemList.Wireless_Dynamo_Energy_ULV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_ULV.ID,
                "hatch.wireless.transmitter.tier.00",
                "ULV Wireless Energy Dynamo",
                0).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_LV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_LV.ID,
                "hatch.wireless.transmitter.tier.01",
                "LV Wireless Energy Dynamo",
                1).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_MV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_MV.ID,
                "hatch.wireless.transmitter.tier.02",
                "MV Wireless Energy Dynamo",
                2).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_HV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_HV.ID,
                "hatch.wireless.transmitter.tier.03",
                "HV Wireless Energy Dynamo",
                3).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_EV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_EV.ID,
                "hatch.wireless.transmitter.tier.04",
                "EV Wireless Energy Dynamo",
                4).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_IV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_IV.ID,
                "hatch.wireless.transmitter.tier.05",
                "IV Wireless Energy Dynamo",
                5).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_LuV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_LuV.ID,
                "hatch.wireless.transmitter.tier.06",
                "LuV Wireless Energy Dynamo",
                6).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_ZPM.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_ZPM.ID,
                "hatch.wireless.transmitter.tier.07",
                "ZPM Wireless Energy Dynamo",
                7).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_UV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_UV.ID,
                "hatch.wireless.transmitter.tier.08",
                "UV Wireless Energy Dynamo",
                8).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_UHV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_UHV.ID,
                "hatch.wireless.transmitter.tier.09",
                "UHV Wireless Energy Dynamo",
                9).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_UEV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_UEV.ID,
                "hatch.wireless.transmitter.tier.10",
                "UEV Wireless Energy Dynamo",
                10).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_UIV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_UIV.ID,
                "hatch.wireless.transmitter.tier.11",
                "UIV Wireless Energy Dynamo",
                11).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_UMV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_UMV.ID,
                "hatch.wireless.transmitter.tier.12",
                "UMV Wireless Energy Dynamo",
                12).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_UXV.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_UXV.ID,
                "hatch.wireless.transmitter.tier.13",
                "UXV Wireless Energy Dynamo",
                13).getStackForm(1L));
        ItemList.Wireless_Dynamo_Energy_MAX.set(
            new GT_MetaTileEntity_Wireless_Dynamo(
                WIRELESS_DYNAMO_ENERGY_HATCH_MAX.ID,
                "hatch.wireless.transmitter.tier.14",
                "MAX Wireless Energy Dynamo",
                14).getStackForm(1L));
    }

    private static void registerLightningRods() {
        ItemList.Machine_HV_LightningRod.set(
            new GT_MetaTileEntity_LightningRod(
                LIGHTNING_ROD_HV.ID,
                "basicgenerator.lightningrod.03",
                "Lightning Rod",
                3).getStackForm(1));
        ItemList.Machine_EV_LightningRod.set(
            new GT_MetaTileEntity_LightningRod(
                LIGHTNING_ROD_EV.ID,
                "basicgenerator.lightningrod.04",
                "Lightning Rod II",
                4).getStackForm(1));
        ItemList.Machine_IV_LightningRod.set(
            new GT_MetaTileEntity_LightningRod(
                LIGHTNING_ROD_IV.ID,
                "basicgenerator.lightningrod.05",
                "Lightning Rod III",
                5).getStackForm(1));
    }

    private static void registerCombustionGenerators() {
        ItemList.Generator_Diesel_LV.set(
            new GT_MetaTileEntity_DieselGenerator(
                COMBUSTION_GENERATOR_LV.ID,
                "basicgenerator.diesel.tier.01",
                "Basic Combustion Generator",
                1).getStackForm(1L));
        ItemList.Generator_Diesel_MV.set(
            new GT_MetaTileEntity_DieselGenerator(
                COMBUSTION_GENERATOR_MV.ID,
                "basicgenerator.diesel.tier.02",
                "Advanced Combustion Generator",
                2).getStackForm(1L));
        ItemList.Generator_Diesel_HV.set(
            new GT_MetaTileEntity_DieselGenerator(
                COMBUSTION_GENERATOR_HV.ID,
                "basicgenerator.diesel.tier.03",
                "Turbo Combustion Generator",
                3).getStackForm(1L));
    }

    private static void registerGasTurbines() {
        ItemList.Generator_Gas_Turbine_LV.set(
            new GT_MetaTileEntity_GasTurbine(
                GAS_TURBINE_LV.ID,
                "basicgenerator.gasturbine.tier.01",
                "Basic Gas Turbine",
                1,
                95).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_MV.set(
            new GT_MetaTileEntity_GasTurbine(
                GAS_TURBINE_MV.ID,
                "basicgenerator.gasturbine.tier.02",
                "Advanced Gas Turbine",
                2,
                90).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_HV.set(
            new GT_MetaTileEntity_GasTurbine(
                GAS_TURBINE_HV.ID,
                "basicgenerator.gasturbine.tier.03",
                "Turbo Gas Turbine",
                3,
                85).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_EV.set(
            new GT_MetaTileEntity_GasTurbine(
                GAS_TURBINE_EV.ID,
                "basicgenerator.gasturbine.tier.04",
                "Turbo Gas Turbine II",
                4,
                60).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_IV.set(
            new GT_MetaTileEntity_GasTurbine(
                GAS_TURBINE_IV.ID,
                "basicgenerator.gasturbine.tier.05",
                "Turbo Gas Turbine III",
                5,
                50).getStackForm(1L));
    }

    private static void registerSteamTurbines() {
        ItemList.Generator_Steam_Turbine_LV.set(
            new GT_MetaTileEntity_SteamTurbine(
                STEAM_TURBINE_LV.ID,
                "basicgenerator.steamturbine.tier.01",
                "Basic Steam Turbine",
                1).getStackForm(1L));
        ItemList.Generator_Steam_Turbine_MV.set(
            new GT_MetaTileEntity_SteamTurbine(
                STEAM_TURBINE_MV.ID,
                "basicgenerator.steamturbine.tier.02",
                "Advanced Steam Turbine",
                2).getStackForm(1L));
        ItemList.Generator_Steam_Turbine_HV.set(
            new GT_MetaTileEntity_SteamTurbine(
                STEAM_TURBINE_HV.ID,
                "basicgenerator.steamturbine.tier.03",
                "Turbo Steam Turbine",
                3).getStackForm(1L));
    }

    private static void registerNaquadahReactors() {
        ItemList.Generator_Naquadah_Mark_I.set(
            new GT_MetaTileEntity_NaquadahReactor(
                NAQUADAH_REACTOR_EV.ID,
                "basicgenerator.naquadah.tier.04",
                new String[] { "Requires Enriched Naquadah Bolts" },
                "Naquadah Reactor Mark I",
                4).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_II.set(
            new GT_MetaTileEntity_NaquadahReactor(
                NAQUADAH_REACTOR_IV.ID,
                "basicgenerator.naquadah.tier.05",
                new String[] { "Requires Enriched Naquadah Rods" },
                "Naquadah Reactor Mark II",
                5).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_III.set(
            new GT_MetaTileEntity_NaquadahReactor(
                NAQUADAH_REACTOR_LuV.ID,
                "basicgenerator.naquadah.tier.06",
                new String[] { "Requires Enriched Naquadah Long Rods" },
                "Naquadah Reactor Mark III",
                6).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_IV.set(
            new GT_MetaTileEntity_NaquadahReactor(
                NAQUADAH_REACTOR_ZPM.ID,
                "basicgenerator.naquadah.tier.07",
                new String[] { "Requires Naquadria Bolts" },
                "Naquadah Reactor Mark IV",
                7).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_V.set(
            new GT_MetaTileEntity_NaquadahReactor(
                NAQUADAH_REACTOR_UV.ID,
                "basicgenerator.naquadah.tier.08",
                new String[] { "Requires Naquadria Rods" },
                "Naquadah Reactor Mark V",
                8).getStackForm(1L));
    }

    private static void registerMagicEnergyConverters() {
        ItemList.MagicEnergyConverter_LV.set(
            new GT_MetaTileEntity_MagicEnergyConverter(
                MAGIC_ENERGY_CONVERTER_LV.ID,
                "basicgenerator.magicenergyconverter.tier.01",
                "Novice Magic Energy Converter",
                1).getStackForm(1L));
        ItemList.MagicEnergyConverter_MV.set(
            new GT_MetaTileEntity_MagicEnergyConverter(
                MAGIC_ENERGY_CONVERTER_MV.ID,
                "basicgenerator.magicenergyconverter.tier.02",
                "Adept Magic Energy Converter",
                2).getStackForm(1L));
        ItemList.MagicEnergyConverter_HV.set(
            new GT_MetaTileEntity_MagicEnergyConverter(
                MAGIC_ENERGY_CONVERTER_HV.ID,
                "basicgenerator.magicenergyconverter.tier.03",
                "Master Magic Energy Converter",
                3).getStackForm(1L));
    }

    private static void registerMagicEnergyAbsorbers() {
        ItemList.MagicEnergyAbsorber_LV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_LV.ID,
                "basicgenerator.magicenergyabsorber.tier.01",
                "Novice Magic Energy Absorber",
                1).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_MV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_MV.ID,
                "basicgenerator.magicenergyabsorber.tier.02",
                "Adept Magic Energy Absorber",
                2).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_HV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_HV.ID,
                "basicgenerator.magicenergyabsorber.tier.03",
                "Master Magic Energy Absorber",
                3).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_EV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_EV.ID,
                "basicgenerator.magicenergyabsorber.tier.04",
                "Grandmaster Magic Energy Absorber",
                4).getStackForm(1L));
    }

    private static void registerPlasmaGenerators() {
        ItemList.Generator_Plasma_EV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_EV.ID,
                "basicgenerator.plasmagenerator.tier.05",
                "Plasma Generator Mark I",
                4).getStackForm(1L));
        ItemList.Generator_Plasma_IV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_IV.ID,
                "basicgenerator.plasmagenerator.tier.06",
                "Plasma Generator Mark II",
                5).getStackForm(1L));
        ItemList.Generator_Plasma_LuV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_LuV.ID,
                "basicgenerator.plasmagenerator.tier.07",
                "Plasma Generator Mark III",
                6).getStackForm(1L));

        ItemList.Generator_Plasma_ZPMV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_ZPM.ID,
                "basicgenerator.plasmagenerator.tier.08",
                "Plasma Generator Mark IV",
                7).getStackForm(1L));

        ItemList.Generator_Plasma_UV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_UV.ID,
                "basicgenerator.plasmagenerator.tier.09",
                "Ultimate Pocket Sun",
                8).getStackForm(1L));
    }

    private static void generateWiresAndPipes() {
        for (int meta = 0; meta < GregTech_API.sGeneratedMaterials.length; meta++) {
            Materials material = GregTech_API.sGeneratedMaterials[meta];
            // This check is separated out because IntelliJ thinks Materials.Wood can be null.
            if (material == null) continue;
            if ((material.mTypes & 0x2) != 0 || material == Materials.Wood) {
                new GT_MetaPipeEntity_Frame(
                    4096 + meta,
                    "GT_Frame_" + material,
                    (GT_LanguageManager.i18nPlaceholder ? "%material" : material.mDefaultLocalName)
                        + " Frame Box (TileEntity)",
                    material);

                // Generate recipes for frame box
                GT_Block_FrameBox block = (GT_Block_FrameBox) GregTech_API.sBlockFrames;
                GT_OreDictUnificator.registerOre(OrePrefixes.frameGt, material, block.getStackForm(1, meta));
                if (material.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        block.getStackForm(2, meta),
                        GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "SSS", "SwS", "SSS", 'S', OrePrefixes.stick.get(material) });
                }

                if (!material.contains(SubTag.NO_RECIPES)
                    && GT_OreDictUnificator.get(OrePrefixes.stick, material, 1) != null) {
                    // Auto generate frame box recipe in an assembler.
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, material, 4),
                            GT_Utility.getIntegratedCircuit(4))
                        .itemOutputs(block.getStackForm(1, meta))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(calculateRecipeEU(material, 7))
                        .addTo(assemblerRecipes);
                }
            }
        }

        makeWires(Materials.RedAlloy, 2000, 0L, 1L, 1L, gregtech.api.enums.GT_Values.V[0], true, false);

        makeWires(Materials.Cobalt, 1200, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.Lead, 1220, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.Tin, 1240, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);

        makeWires(Materials.Zinc, 1260, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.SolderingAlloy, 1280, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);

        makeWires(Materials.Iron, 1300, 3L, 6L, 2L, gregtech.api.enums.GT_Values.V[2], true, false);
        makeWires(Materials.Nickel, 1320, 3L, 6L, 3L, gregtech.api.enums.GT_Values.V[2], true, false);
        makeWires(Materials.Cupronickel, 1340, 3L, 6L, 2L, gregtech.api.enums.GT_Values.V[2], true, false);
        makeWires(Materials.Copper, 1360, 2L, 4L, 1L, gregtech.api.enums.GT_Values.V[2], true, false);
        makeWires(Materials.AnnealedCopper, 1380, 2L, 4L, 1L, gregtech.api.enums.GT_Values.V[2], true, false);

        makeWires(Materials.Kanthal, 1400, 3L, 6L, 4L, gregtech.api.enums.GT_Values.V[3], true, false);
        makeWires(Materials.Gold, 1420, 2L, 4L, 3L, gregtech.api.enums.GT_Values.V[3], true, false);
        makeWires(Materials.Electrum, 1440, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[3], true, false);
        makeWires(Materials.Silver, 1460, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[3], true, false);
        makeWires(Materials.BlueAlloy, 1480, 1L, 2L, 2L, gregtech.api.enums.GT_Values.V[3], true, false);

        makeWires(Materials.Nichrome, 1500, 4L, 8L, 3L, gregtech.api.enums.GT_Values.V[4], true, false);
        makeWires(Materials.Steel, 1520, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[4], true, false);
        makeWires(Materials.BlackSteel, 1540, 2L, 4L, 3L, gregtech.api.enums.GT_Values.V[4], true, false);
        makeWires(Materials.Titanium, 1560, 2L, 4L, 4L, gregtech.api.enums.GT_Values.V[4], true, false);
        makeWires(Materials.Aluminium, 1580, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[4], true, false);

        makeWires(Materials.Graphene, 1600, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[5], false, true);
        makeWires(Materials.Osmium, 1620, 2L, 4L, 4L, gregtech.api.enums.GT_Values.V[5], true, false);
        makeWires(Materials.Platinum, 1640, 1L, 2L, 2L, gregtech.api.enums.GT_Values.V[5], true, false);
        makeWires(Materials.TungstenSteel, 1660, 2L, 4L, 3L, gregtech.api.enums.GT_Values.V[5], true, false);
        makeWires(Materials.Tungsten, 1680, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[5], true, false);

        makeWires(Materials.HSSG, 1700, 2L, 4L, 4L, gregtech.api.enums.GT_Values.V[6], true, false);
        makeWires(Materials.NiobiumTitanium, 1720, 2L, 4L, 4L, gregtech.api.enums.GT_Values.V[6], true, false);
        makeWires(Materials.VanadiumGallium, 1740, 2L, 4L, 4L, gregtech.api.enums.GT_Values.V[6], true, false);
        makeWires(Materials.YttriumBariumCuprate, 1760, 4L, 8L, 4L, gregtech.api.enums.GT_Values.V[6], true, false);

        makeWires(Materials.Naquadah, 1780, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[7], true, false);

        makeWires(Materials.NaquadahAlloy, 1800, 4L, 8L, 2L, gregtech.api.enums.GT_Values.V[8], true, false);
        makeWires(Materials.Duranium, 1820, 8L, 16L, 1L, gregtech.api.enums.GT_Values.V[8], true, false);
        makeWires(Materials.TPV, 1840, 1L, 2L, 6L, gregtech.api.enums.GT_Values.V[4], true, false);

        // Superconductor base.
        makeWires(
            Materials.Pentacadmiummagnesiumhexaoxid,
            2200,
            1L,
            2L,
            1L,
            gregtech.api.enums.GT_Values.V[2],
            false,
            false);
        makeWires(
            Materials.Titaniumonabariumdecacoppereikosaoxid,
            2220,
            1L,
            8L,
            2L,
            gregtech.api.enums.GT_Values.V[3],
            false,
            false);
        makeWires(Materials.Uraniumtriplatinid, 2240, 1L, 16L, 3L, gregtech.api.enums.GT_Values.V[4], false, false);
        makeWires(Materials.Vanadiumtriindinid, 2260, 1L, 64L, 4L, gregtech.api.enums.GT_Values.V[5], false, false);
        makeWires(
            Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            2280,
            2L,
            256L,
            6L,
            gregtech.api.enums.GT_Values.V[6],
            false,
            false);
        makeWires(
            Materials.Tetranaquadahdiindiumhexaplatiumosminid,
            2300,
            2L,
            1024L,
            8L,
            gregtech.api.enums.GT_Values.V[7],
            false,
            false);
        makeWires(
            Materials.Longasssuperconductornameforuvwire,
            2500,
            2L,
            4096L,
            12L,
            gregtech.api.enums.GT_Values.V[8],
            false,
            false);
        makeWires(
            Materials.Longasssuperconductornameforuhvwire,
            2520,
            2L,
            16384L,
            16L,
            gregtech.api.enums.GT_Values.V[9],
            false,
            false);
        makeWires(
            Materials.SuperconductorUEVBase,
            2032,
            2L,
            65536L,
            24L,
            gregtech.api.enums.GT_Values.V[10],
            false,
            false);
        makeWires(
            Materials.SuperconductorUIVBase,
            2052,
            2L,
            262144L,
            32L,
            gregtech.api.enums.GT_Values.V[11],
            false,
            false);
        makeWires(Materials.SuperconductorUMVBase, 2072, 2L, 1048576L, 32L, GT_Values.V[12], false, false);

        // Actual superconductors.
        makeWires(Materials.SuperconductorMV, 2320, 0L, 0L, 4L, gregtech.api.enums.GT_Values.V[2], false, true);
        makeWires(Materials.SuperconductorHV, 2340, 0L, 0L, 6L, gregtech.api.enums.GT_Values.V[3], false, true);
        makeWires(Materials.SuperconductorEV, 2360, 0L, 0L, 8L, gregtech.api.enums.GT_Values.V[4], false, true);
        makeWires(Materials.SuperconductorIV, 2380, 0L, 0L, 12L, gregtech.api.enums.GT_Values.V[5], false, true);
        makeWires(Materials.SuperconductorLuV, 2400, 0L, 0L, 16L, gregtech.api.enums.GT_Values.V[6], false, true);
        makeWires(Materials.SuperconductorZPM, 2420, 0L, 0L, 24L, gregtech.api.enums.GT_Values.V[7], false, true);
        makeWires(Materials.SuperconductorUV, 2440, 0L, 0L, 32L, gregtech.api.enums.GT_Values.V[8], false, true);
        makeWires(Materials.SuperconductorUHV, 2020, 0L, 0L, 48L, gregtech.api.enums.GT_Values.V[9], false, true);
        makeWires(Materials.SuperconductorUEV, 2026, 0L, 0L, 64L, gregtech.api.enums.GT_Values.V[10], false, true);
        makeWires(Materials.SuperconductorUIV, 2081, 0L, 0L, 64L, gregtech.api.enums.GT_Values.V[11], false, true);
        makeWires(Materials.SuperconductorUMV, 2089, 0L, 0L, 64L, gregtech.api.enums.GT_Values.V[12], false, true);

        makeWires(Materials.Ichorium, 2600, 2L, 2L, 12L, GT_Values.V[9], false, true);
        makeWires(MaterialsUEVplus.SpaceTime, 2606, 0L, 0L, 1_000_000L, GT_Values.V[14], false, true);

        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(Materials.Wood),
            new GT_MetaPipeEntity_Fluid(
                5101,
                "GT_Pipe_Wood_Small",
                "Small Wooden Fluid Pipe",
                0.375F,
                Materials.Wood,
                10,
                350,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(Materials.Wood),
            new GT_MetaPipeEntity_Fluid(5102, "GT_Pipe_Wood", "Wooden Fluid Pipe", 0.5F, Materials.Wood, 30, 350, false)
                .getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(Materials.Wood),
            new GT_MetaPipeEntity_Fluid(
                5103,
                "GT_Pipe_Wood_Large",
                "Large Wooden Fluid Pipe",
                0.75F,
                Materials.Wood,
                60,
                350,
                false).getStackForm(1L));

        generateFluidPipes(Materials.Copper, Materials.Copper.mName, 5110, 20, 1000, true);
        generateFluidMultiPipes(Materials.Copper, Materials.Copper.mName, 5115, 20, 1000, true);
        generateFluidPipes(Materials.Bronze, Materials.Bronze.mName, 5120, 120, 2000, true);
        generateFluidMultiPipes(Materials.Bronze, Materials.Bronze.mName, 5125, 120, 2000, true);
        generateFluidPipes(Materials.Steel, Materials.Steel.mName, 5130, 240, 2500, true);
        generateFluidMultiPipes(Materials.Steel, Materials.Steel.mName, 5135, 240, 2500, true);
        generateFluidPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5140, 360, 3000, true);
        generateFluidMultiPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5145, 360, 3000, true);
        generateFluidPipes(Materials.Titanium, Materials.Titanium.mName, 5150, 480, 5000, true);
        generateFluidMultiPipes(Materials.Titanium, Materials.Titanium.mName, 5155, 480, 5000, true);
        generateFluidPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5160, 600, 7500, true);
        generateFluidMultiPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5270, 600, 7500, true);
        generateFluidPipes(
            Materials.Polybenzimidazole,
            Materials.Polybenzimidazole.mName,
            "PBI",
            5280,
            600,
            1000,
            true);
        generateFluidMultiPipes(
            Materials.Polybenzimidazole,
            Materials.Polybenzimidazole.mName,
            "PBI",
            5290,
            600,
            1000,
            true);
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(Materials.Ultimate),
            new GT_MetaPipeEntity_Fluid(
                5165,
                "GT_Pipe_HighPressure_Small",
                "Small High Pressure Fluid Pipe",
                0.375F,
                Materials.Redstone,
                4800,
                1500,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(Materials.Ultimate),
            new GT_MetaPipeEntity_Fluid(
                5166,
                "GT_Pipe_HighPressure",
                "High Pressure Fluid Pipe",
                0.5F,
                Materials.Redstone,
                7200,
                1500,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(Materials.Ultimate),
            new GT_MetaPipeEntity_Fluid(
                5167,
                "GT_Pipe_HighPressure_Large",
                "Large High Pressure Fluid Pipe",
                0.75F,
                Materials.Redstone,
                9600,
                1500,
                true).getStackForm(1L));
        generateFluidPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5170, 360, 350, true);
        generateFluidMultiPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5175, 360, 350, true);
        generateFluidPipes(Materials.NiobiumTitanium, Materials.NiobiumTitanium.mName, 5180, 900, 2900, true);
        generateFluidMultiPipes(Materials.NiobiumTitanium, Materials.NiobiumTitanium.mName, 5185, 900, 2900, true);
        generateFluidPipes(Materials.Enderium, Materials.Enderium.mName, 5190, 1800, 15000, true);
        generateFluidMultiPipes(Materials.Enderium, Materials.Enderium.mName, 5195, 1800, 15000, true);
        generateFluidPipes(Materials.Naquadah, Materials.Naquadah.mName, 5200, 9000, 19000, true);
        generateFluidMultiPipes(Materials.Naquadah, Materials.Naquadah.mName, 5205, 9000, 19000, true);
        generateFluidPipes(Materials.Neutronium, Materials.Neutronium.mName, 5210, 16800, 1000000, true);
        generateFluidMultiPipes(Materials.Neutronium, Materials.Neutronium.mName, 5215, 16800, 1000000, true);
        generateFluidPipes(Materials.NetherStar, Materials.NetherStar.mName, 5220, 19200, 1000000, true);
        generateFluidMultiPipes(Materials.NetherStar, Materials.NetherStar.mName, 5225, 19200, 1000000, true);
        generateFluidPipes(Materials.MysteriousCrystal, Materials.MysteriousCrystal.mName, 5230, 24000, 1000000, true);
        generateFluidMultiPipes(
            Materials.MysteriousCrystal,
            Materials.MysteriousCrystal.mName,
            5235,
            24000,
            1000000,
            true);
        generateFluidPipes(Materials.DraconiumAwakened, Materials.DraconiumAwakened.mName, 5240, 45000, 10000000, true);
        generateFluidMultiPipes(
            Materials.DraconiumAwakened,
            Materials.DraconiumAwakened.mName,
            5245,
            45000,
            10000000,
            true);
        generateFluidPipes(Materials.Infinity, Materials.Infinity.mName, 5250, 60000, 10000000, true);
        generateFluidMultiPipes(Materials.Infinity, Materials.Infinity.mName, 5255, 60000, 10000000, true);
        generateFluidPipes(Materials.WroughtIron, Materials.WroughtIron.mName, 5260, 180, 2250, true);
        generateFluidMultiPipes(Materials.WroughtIron, Materials.WroughtIron.mName, 5265, 180, 2250, true);
        generateFluidPipes(
            Materials.Polytetrafluoroethylene,
            Materials.Polytetrafluoroethylene.mName,
            "PTFE",
            5680,
            480,
            600,
            true);
        generateFluidMultiPipes(
            Materials.Polytetrafluoroethylene,
            Materials.Polytetrafluoroethylene.mName,
            "PTFE",
            5685,
            480,
            600,
            true);
        generateFluidPipes(
            MaterialsUEVplus.SpaceTime,
            MaterialsUEVplus.SpaceTime.mName,
            5300,
            250000,
            2147483647,
            true);
        generateFluidMultiPipes(
            MaterialsUEVplus.SpaceTime,
            MaterialsUEVplus.SpaceTime.mName,
            5305,
            250000,
            2147483647,
            true);
        generateFluidPipes(
            MaterialsUEVplus.TranscendentMetal,
            MaterialsUEVplus.TranscendentMetal.mName,
            5310,
            220000,
            2147483647,
            true);
        generateFluidMultiPipes(
            MaterialsUEVplus.TranscendentMetal,
            MaterialsUEVplus.TranscendentMetal.mName,
            5315,
            220000,
            2147483647,
            true);

        generateItemPipes(Materials.Brass, Materials.Brass.mName, 5602, 1);
        generateItemPipes(Materials.Electrum, Materials.Electrum.mName, 5612, 2);
        generateItemPipes(Materials.Platinum, Materials.Platinum.mName, 5622, 4);
        generateItemPipes(Materials.Osmium, Materials.Osmium.mName, 5632, 8);
        generateItemPipes(Materials.PolyvinylChloride, Materials.PolyvinylChloride.mName, "PVC", 5690, 4);
        generateItemPipes(Materials.Nickel, Materials.Nickel.mName, 5700, 1);
        generateItemPipes(Materials.Cobalt, Materials.Cobalt.mName, 5710, 2);
        generateItemPipes(Materials.Aluminium, Materials.Aluminium.mName, 5720, 2);
        if (NewHorizonsCoreMod.isModLoaded()) {
            generateFluidPipes(Materials.get("RadoxPoly"), "RadoxPoly", 5760, 5000, 1500, true);
        }
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    private static void makeWires(Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage,
        long aVoltage, boolean aInsulatable, boolean aAutoInsulated) {
        String name = GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName;
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt01,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 0,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".01",
                "1x " + name + aTextWire2,
                0.125F,
                aMaterial,
                aLoss,
                1L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt02,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 1,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".02",
                "2x " + name + aTextWire2,
                0.25F,
                aMaterial,
                aLoss,
                2L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt04,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 2,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".04",
                "4x " + name + aTextWire2,
                0.375F,
                aMaterial,
                aLoss,
                4L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt08,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 3,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".08",
                "8x " + name + aTextWire2,
                0.5F,
                aMaterial,
                aLoss,
                8L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt12,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 4,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".12",
                "12x " + name + aTextWire2,
                0.625F,
                aMaterial,
                aLoss,
                12L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt16,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 5,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".16",
                "16x " + name + aTextWire2,
                0.75F,
                aMaterial,
                aLoss,
                16L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        if (aInsulatable) {
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt01,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 6,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".01",
                    "1x " + name + aTextCable2,
                    0.25F,
                    aMaterial,
                    aLossInsulated,
                    1L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt02,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 7,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".02",
                    "2x " + name + aTextCable2,
                    0.375F,
                    aMaterial,
                    aLossInsulated,
                    2L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt04,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 8,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".04",
                    "4x " + name + aTextCable2,
                    0.5F,
                    aMaterial,
                    aLossInsulated,
                    4L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt08,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 9,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".08",
                    "8x " + name + aTextCable2,
                    0.625F,
                    aMaterial,
                    aLossInsulated,
                    8L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt12,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 10,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".12",
                    "12x " + name + aTextCable2,
                    0.75F,
                    aMaterial,
                    aLossInsulated,
                    12L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt16,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 11,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".16",
                    "16x " + name + aTextCable2,
                    0.875F,
                    aMaterial,
                    aLossInsulated,
                    16L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
        }
    }

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Registering MetaTileEntities.");
        registerMachineHull();
        registerTransformer();
        registerDynamoHatch();
        registerEnergyHatch();
        registerInputHatch();
        registerQuadrupleInputHatch();
        registerOutputHatch();
        registerQuantumTank();
        registerQuantumChest();
        registerSuperTank();
        registerSuperChest();
        registerLongDistancePipe();
        registerAE2Hatches();
        registerMagHatch();
        registerInputBus();
        registerOutputBus();
        registerMufflerHatch();
        registerBoiler();
        registerBatteryBuffer1x1();
        registerBatteryBuffer2x2();
        registerBatteryBuffer3x3();
        registerBatteryBuffer4x4();
        registerCharger4x4();
        registerWirelessEnergyHatch();
        registerWirelessDynamoHatch();
        registerSteamMachines();
        registerHPSteamMachines();
        registerLocker();
        registerScanner();
        registerPackager();
        registerRockBreaker();
        registerIndustrialApiary();
        registerMassFab();
        registerReplicator();
        registerBrewery();
        registerMiner();
        registerPump();
        registerTeleporter();
        registerMonsterRepellator();
        registerAdvancedSeismicProspector();
        registerMicrowaveEnergyTransmitter();
        registerChestBuffer();
        registerItemFilter();
        registerTypeFilter();
        registerRegulator();
        registerSuperBuffer();
        registerItemDistributor();
        registerRecipeFilter();
        registerLightningRods();
        registerCombustionGenerators();
        registerGasTurbines();
        registerSteamTurbines();
        registerNaquadahReactors();
        registerMagicEnergyAbsorbers();
        registerMagicEnergyConverters();
        registerPlasmaGenerators();
        registerMultiblockControllers();
        registerWorldAccelerator();
        registerAlloySmelter();
        registerMatterAmplifier();
        registerAssemblingMachine();
        registerWetTransformer();
        registerHighAmpTransformer();
        registerChemicalBath();
        registerChemicalReactor();
        registerFermenter();
        registerFluidCanner();
        registerFluidExtractor();
        registerFluidHeater();
        registerMixer();
        registerAutoclave();
        registerBendingMachine();
        registerCompressor();
        registerCuttingMachine();
        registerDistillery();
        registerElectricFurnace();
        registerElectromagneticSeparator();
        registerExtractor();
        registerExtruder();
        registerFluidSolidifier();
        registerFormingPress();
        registerForgeHammer();
        registerLathe();
        registerPrecisionLaserEngraver();
        registerMacerator();
        registerMatterFabricator();
        registerMicrowave();
        registerOreWashingPlant();
        registerPolarizer();
        registerRecycler();
        registerSiftingMachine();
        registerSlicingMachine();
        registerThermalCentrifuge();
        registerWiremill();
        registerArcFurnace();
        registerCentrifuge();
        registerPlasmaArcFurnace();
        registerCanningMachine();
        registerElectrolyzer();
        registerCircuitAssembler();
        registerTurboCharger4By4();
        registerBetterJukebox();
        registerUnpackager();
        registerPrinter();
        registerOven();

        ItemList.AdvDebugStructureWriter.set(
            new GT_MetaTileEntity_AdvDebugStructureWriter(
                ADVANCED_DEBUG_STRUCTURE_WRITTER.ID,
                "advdebugstructurewriter",
                "Advanced Debug Structure Writer",
                5).getStackForm(1L));
        ItemList.Hatch_Maintenance.set(
            new GT_MetaTileEntity_Hatch_Maintenance(MAINTENANCE_HATCH.ID, "hatch.maintenance", "Maintenance Hatch", 1)
                .getStackForm(1L));
        ItemList.Hatch_AutoMaintenance.set(
            new GT_MetaTileEntity_Hatch_Maintenance(
                AUTO_MAINTENANCE_HATCH.ID,
                "hatch.maintenance.auto",
                "Auto Maintenance Hatch",
                6,
                true).getStackForm(1L));
        ItemList.Hatch_DroneDownLink.set(
            new GT_MetaTileEntity_Hatch_DroneDownLink(
                DroneDownLink.ID,
                "hatch.dronedownlink",
                "Drone DownLink Module",
                5).getStackForm(1));
        ItemList.Hatch_DataAccess_EV.set(
            new GT_MetaTileEntity_Hatch_DataAccess(DATA_ACCESS_HATCH.ID, "hatch.dataaccess", "Data Access Hatch", 4)
                .getStackForm(1L));
        ItemList.Hatch_DataAccess_LuV.set(
            new GT_MetaTileEntity_Hatch_DataAccess(
                ADVANCED_DATA_ACCESS_HATCH.ID,
                "hatch.dataaccess.adv",
                "Advanced Data Access Hatch",
                6).getStackForm(1L));
        ItemList.Hatch_DataAccess_UV.set(
            new GT_MetaTileEntity_Hatch_DataAccess(
                AUTOMATABLE_DATA_ACCESS_HATCH.ID,
                "hatch.dataaccess.auto",
                "Automatable Data Access Hatch",
                8).getStackForm(1L));
        ItemList.Hatch_HeatSensor.set(
            new GT_MetaTileEntity_HeatSensor(HEAT_DETECTOR_HATCH.ID, "hatch.heatsensor", "Heat Sensor Hatch", 7)
                .getStackForm(1));
        ItemList.Hatch_pHSensor.set(
            new GT_MetaTileEntity_pHSensor(HATCH_PH_SENSOR.ID, "hatch.phsensor", "pH Sensor Hatch", 7).getStackForm(1));
        ItemList.Hatch_LensHousing.set(
            new GT_MetaTileEntity_LensHousing(HATCH_LENS_HOUSING.ID, "hatch.lenshousing", "Lens Housing")
                .getStackForm(1L));
        ItemList.Hatch_LensIndicator.set(
            new GT_MetaTileEntity_LensIndicator(
                HATCH_LENS_INDICATOR.ID,
                "hatch.lensindicator",
                "Lens Indicator Hatch",
                8).getStackForm(1L));
        generateWiresAndPipes();
    }

    private static void generateItemPipes(Materials aMaterial, String name, int startID, int baseInvSlots) {
        generateItemPipes(
            aMaterial,
            name,
            GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName,
            startID,
            baseInvSlots);
    }

    private static void generateItemPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseInvSlots) {
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID,
                "GT_Pipe_" + name,
                displayName + " Item Pipe",
                0.50F,
                aMaterial,
                baseInvSlots,
                32768 / baseInvSlots,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 1,
                "GT_Pipe_" + name + "_Large",
                "Large " + displayName + " Item Pipe",
                0.75F,
                aMaterial,
                baseInvSlots * 2,
                16384 / baseInvSlots,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 2,
                "GT_Pipe_" + name + "_Huge",
                "Huge " + displayName + " Item Pipe",
                1.00F,
                aMaterial,
                baseInvSlots * 4,
                8192 / baseInvSlots,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveMedium.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 3,
                "GT_Pipe_Restrictive_" + name,
                "Restrictive " + displayName + " Item Pipe",
                0.50F,
                aMaterial,
                baseInvSlots,
                3276800 / baseInvSlots,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveLarge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 4,
                "GT_Pipe_Restrictive_" + name + "_Large",
                "Large Restrictive " + displayName + " Item Pipe",
                0.75F,
                aMaterial,
                baseInvSlots * 2,
                1638400 / baseInvSlots,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveHuge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 5,
                "GT_Pipe_Restrictive_" + name + "_Huge",
                "Huge Restrictive " + displayName + " Item Pipe",
                0.875F,
                aMaterial,
                baseInvSlots * 4,
                819200 / baseInvSlots,
                true).getStackForm(1L));
    }

    @SuppressWarnings("SameParameterValue")
    private static void generateFluidPipes(Materials aMaterial, String name, int startID, int baseCapacity,
        int heatCapacity, boolean gasProof) {
        generateFluidPipes(
            aMaterial,
            name,
            GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName,
            startID,
            baseCapacity,
            heatCapacity,
            gasProof);
    }

    private static void generateFluidPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseCapacity, int heatCapacity, boolean gasProof) {
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeTiny.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID,
                "GT_Pipe_" + name + "_Tiny",
                "Tiny " + displayName + " Fluid Pipe",
                0.25F,
                aMaterial,
                baseCapacity / 6,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 1,
                "GT_Pipe_" + name + "_Small",
                "Small " + displayName + " Fluid Pipe",
                0.375F,
                aMaterial,
                baseCapacity / 3,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 2,
                "GT_Pipe_" + name,
                displayName + " Fluid Pipe",
                0.5F,
                aMaterial,
                baseCapacity,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 3,
                "GT_Pipe_" + name + "_Large",
                "Large " + displayName + " Fluid Pipe",
                0.75F,
                aMaterial,
                baseCapacity * 2,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 4,
                "GT_Pipe_" + name + "_Huge",
                "Huge " + displayName + " Fluid Pipe",
                0.875F,
                aMaterial,
                baseCapacity * 4,
                heatCapacity,
                gasProof).getStackForm(1L));
    }

    @SuppressWarnings("SameParameterValue")
    private static void generateFluidMultiPipes(Materials aMaterial, String name, int startID, int baseCapacity,
        int heatCapacity, boolean gasProof) {
        generateFluidMultiPipes(aMaterial, name, "%material", startID, baseCapacity, heatCapacity, gasProof);
    }

    private static void generateFluidMultiPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseCapacity, int heatCapacity, boolean gasProof) {
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeQuadruple.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID,
                "GT_Pipe_" + name + "_Quadruple",
                "Quadruple " + displayName + " Fluid Pipe",
                1.0F,
                aMaterial,
                baseCapacity,
                heatCapacity,
                gasProof,
                4).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeNonuple.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 1,
                "GT_Pipe_" + name + "_Nonuple",
                "Nonuple " + displayName + " Fluid Pipe",
                1.0F,
                aMaterial,
                baseCapacity / 3,
                heatCapacity,
                gasProof,
                9).getStackForm(1L));
    }
}
