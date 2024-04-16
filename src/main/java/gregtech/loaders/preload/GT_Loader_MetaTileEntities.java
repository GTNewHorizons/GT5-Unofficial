package gregtech.loaders.preload;

import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_DATA_ACCESS_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_DEBUG_STRUCTURE_WRITTER;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ADVANCED_SEISMIC_PROSPECTOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_LINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.AUTOMATABLE_DATA_ACCESS_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.AUTO_MAINTENANCE_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_1_BY_1_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_2_BY_2_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_3_BY_3_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_UV;
import static gregtech.api.enums.MetaTileEntityIDs.BATTERY_BUFFER_4_BY_4_ZPM;
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
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_EV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_HV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_IV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_LV;
import static gregtech.api.enums.MetaTileEntityIDs.BREWERY_MV;
import static gregtech.api.enums.MetaTileEntityIDs.BRICKED_BLAST_FURNACE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CHARCOAL_PILE_IGNITER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.CHEST_BUFFER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.CLEANROOM_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_ENGINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_GENERATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_GENERATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.COMBUSTION_GENERATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.CONCRETE_BACKFILLER_II_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CONCRETE_BACKFILLER_I_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_ME;
import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_ME_BUS;
import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_SLAVE;
import static gregtech.api.enums.MetaTileEntityIDs.DATA_ACCESS_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.DISTILLATION_TOWER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.DTPF_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.DYNAMO_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.DroneDownLink;
import static gregtech.api.enums.MetaTileEntityIDs.Drone_Centre;
import static gregtech.api.enums.MetaTileEntityIDs.EBF_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_UV;
import static gregtech.api.enums.MetaTileEntityIDs.ENERGY_HATCH_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.EXTREME_COMBUSTION_ENGINE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.FUSION_CONTROLLER_MKI;
import static gregtech.api.enums.MetaTileEntityIDs.FUSION_CONTROLLER_MKII;
import static gregtech.api.enums.MetaTileEntityIDs.FUSION_CONTROLLER_MKIII;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_EV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_IV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GAS_TURBINE_MV;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_PRESSURE_COAL_BOILER;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_PRESSURE_LAVA_BOILER;
import static gregtech.api.enums.MetaTileEntityIDs.HIGH_PRESSURE_SOLAR_BOILER;
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
import static gregtech.api.enums.MetaTileEntityIDs.HULL_MV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_STEEL;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_UV;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_WROUGHT_IRON;
import static gregtech.api.enums.MetaTileEntityIDs.HULL_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.IMPLOSION_COMPRESSOR_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.INDUSTRIAL_APIARY;
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
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_ME;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_ME_ADVANCED;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.INPUT_HATCH_UV;
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
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_ABSORBER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_CONVERTER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_CONVERTER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MAGIC_ENERGY_CONVERTER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MAINTENANCE_HATCH;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MASS_FABRICATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_UV;
import static gregtech.api.enums.MetaTileEntityIDs.MICROWAVE_ENERGY_TRANSMITTER_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.MINER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.MINER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.MINER_MV;
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
import static gregtech.api.enums.MetaTileEntityIDs.MULTI_SMELTER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.NANO_FORGE_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_UV;
import static gregtech.api.enums.MetaTileEntityIDs.NAQUADAH_REACTOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.OIL_CRACKER_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKIII_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKII_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKIV_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.ORE_DRILL_MKI_CONTROLLER;
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
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_ME;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_MV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UHV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.OUTPUT_HATCH_UV;
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
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.PLASMA_GENERATOR_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.PROCESSING_ARRAY_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_EV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_HV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_IV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_LV;
import static gregtech.api.enums.MetaTileEntityIDs.PUMP_MV;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_PLANT_CONTROLLER;
import static gregtech.api.enums.MetaTileEntityIDs.PURIFICATION_UNIT_SIFTER;
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
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_EV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_HV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_IV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_LV;
import static gregtech.api.enums.MetaTileEntityIDs.REPLICATOR_MV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ROCK_BREAKER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_EV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_HV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.SCANNER_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SIMPLE_SOLAR_BOILER;
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
import static gregtech.api.enums.MetaTileEntityIDs.TRANSCENDENT_PLASMA_MIXER_CONTROLLER;
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

import net.minecraft.util.EnumChatFormatting;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Wireless_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Wireless_Hatch;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
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
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_IntegratedOreFactory;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_Bronze;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_Steel;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_Titanium;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeBoiler_TungstenSteel;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeChemicalReactor;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Gas;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_GasAdvanced;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_HPSteam;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Plasma;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Steam;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_MultiFurnace;
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
import gregtech.common.tileentities.machines.multi.drone.GT_MetaTileEntity_DroneCentre;
import gregtech.common.tileentities.machines.multi.drone.GT_MetaTileEntity_Hatch_DroneDownLink;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitSifter;
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

// Free IDs left for machines in GT as of 29th of July 2022 - Colen. Please try use them up in order.
// 358
// 359
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
        ItemList.Machine_Multi_PurificationUnitSifter.set(
            new GT_MetaTileEntity_PurificationUnitSifter(
                PURIFICATION_UNIT_SIFTER.ID,
                "multimachine.purificationunitsifter",
                "Sifter Purification Unit").getStackForm(1L));
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
        ItemList.Automation_ChestBuffer_MAX.set(
            new GT_MetaTileEntity_ChestBuffer(
                CHEST_BUFFER_UHV.ID,
                "automation.chestbuffer.tier.09",
                "Highly Ultimate Voltage Chest Buffer",
                9).getStackForm(1L));
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
        ItemList.Hatch_Dynamo_MAX.set(
            new GT_MetaTileEntity_Hatch_Dynamo(DYNAMO_HATCH_UHV.ID, "hatch.dynamo.tier.09", "UHV Dynamo Hatch", 9)
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
        ItemList.Hatch_Energy_MAX.set(
            new GT_MetaTileEntity_Hatch_Energy(ENERGY_HATCH_UHV.ID, "hatch.energy.tier.09", "UHV Energy Hatch", 9)
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
        ItemList.Hatch_Input_MAX.set(
            new GT_MetaTileEntity_Hatch_Input(INPUT_HATCH_UHV.ID, "hatch.input.tier.09", "Input Hatch (UHV)", 9)
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
        ItemList.Hatch_Output_MAX.set(
            new GT_MetaTileEntity_Hatch_Output(OUTPUT_HATCH_UHV.ID, "hatch.output.tier.09", "Output Hatch (UHV)", 9)
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
                "hatch.crafting_input.slave",
                "Crafting Input Slave").getStackForm(1L));
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
        ItemList.Battery_Buffer_1by1_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UHV.ID,
                "batterybuffer.01.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
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
        ItemList.Battery_Buffer_2by2_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UHV.ID,
                "batterybuffer.04.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
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
        ItemList.Battery_Buffer_3by3_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UHV.ID,
                "batterybuffer.09.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
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
        ItemList.Battery_Buffer_4by4_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UHV.ID,
                "batterybuffer.16.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
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
        ItemList.Battery_Charger_4by4_MAX.set(
            new GT_MetaTileEntity_Charger(
                BATTERY_CHARGER_4_BY_4_UHV.ID,
                "batterycharger.16.tier.09",
                "Highly Ultimate Voltage Battery Charger",
                9,
                "Each battery gives 8A in/4A out (min 4A/2A)",
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
        ItemList.Generator_Plasma_IV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_IV.ID,
                "basicgenerator.plasmagenerator.tier.05",
                "Plasma Generator Mark I",
                4).getStackForm(1L));
        ItemList.Generator_Plasma_LuV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_LuV.ID,
                "basicgenerator.plasmagenerator.tier.06",
                "Plasma Generator Mark II",
                5).getStackForm(1L));
        ItemList.Generator_Plasma_ZPMV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                PLASMA_GENERATOR_ZPM.ID,
                "basicgenerator.plasmagenerator.tier.07",
                "Plasma Generator Mark III",
                6).getStackForm(1L));
    }

    private static void generateWiresAndPipes() {
        for (int i = 0; i < GregTech_API.sGeneratedMaterials.length; i++) {
            if (((GregTech_API.sGeneratedMaterials[i] != null)
                && ((GregTech_API.sGeneratedMaterials[i].mTypes & 0x2) != 0))
                || (GregTech_API.sGeneratedMaterials[i] == Materials.Wood)) {
                new GT_MetaPipeEntity_Frame(
                    4096 + i,
                    "GT_Frame_" + GregTech_API.sGeneratedMaterials[i],
                    (GT_LanguageManager.i18nPlaceholder ? "%material"
                        : GregTech_API.sGeneratedMaterials[i] != null
                            ? GregTech_API.sGeneratedMaterials[i].mDefaultLocalName
                            : "")
                        + " Frame Box",
                    GregTech_API.sGeneratedMaterials[i]);
            }
        }
        boolean bEC = !GT_Mod.gregtechproxy.mHardcoreCables;

        makeWires(Materials.RedAlloy, 2000, 0L, 1L, 1L, gregtech.api.enums.GT_Values.V[0], true, false);

        makeWires(Materials.Cobalt, 1200, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.Lead, 1220, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.Tin, 1240, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);

        makeWires(Materials.Zinc, 1260, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.SolderingAlloy, 1280, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);

        makeWires(
            Materials.Iron,
            1300,
            bEC ? 3L : 4L,
            bEC ? 6L : 8L,
            2L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.Nickel,
            1320,
            bEC ? 3L : 5L,
            bEC ? 6L : 10L,
            3L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.Cupronickel,
            1340,
            bEC ? 3L : 4L,
            bEC ? 6L : 8L,
            2L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.Copper,
            1360,
            bEC ? 2L : 3L,
            bEC ? 4L : 6L,
            1L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.AnnealedCopper,
            1380,
            bEC ? 1L : 2L,
            bEC ? 2L : 4L,
            1L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);

        makeWires(
            Materials.Kanthal,
            1400,
            bEC ? 3L : 8L,
            bEC ? 6L : 16L,
            4L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.Gold,
            1420,
            bEC ? 2L : 6L,
            bEC ? 4L : 12L,
            3L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.Electrum,
            1440,
            bEC ? 2L : 5L,
            bEC ? 4L : 10L,
            2L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.Silver,
            1460,
            bEC ? 1L : 4L,
            bEC ? 2L : 8L,
            1L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.BlueAlloy,
            1480,
            bEC ? 1L : 4L,
            bEC ? 2L : 8L,
            2L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);

        makeWires(
            Materials.Nichrome,
            1500,
            bEC ? 4L : 32L,
            bEC ? 8L : 64L,
            3L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.Steel,
            1520,
            bEC ? 2L : 16L,
            bEC ? 4L : 32L,
            2L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.BlackSteel,
            1540,
            bEC ? 2L : 14L,
            bEC ? 4L : 28L,
            3L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.Titanium,
            1560,
            bEC ? 2L : 12L,
            bEC ? 4L : 24L,
            4L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.Aluminium,
            1580,
            bEC ? 1L : 8L,
            bEC ? 2L : 16L,
            1L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);

        makeWires(
            Materials.Graphene,
            1600,
            bEC ? 1L : 16L,
            bEC ? 2L : 32L,
            1L,
            gregtech.api.enums.GT_Values.V[5],
            false,
            true);
        makeWires(
            Materials.Osmium,
            1620,
            bEC ? 2L : 32L,
            bEC ? 4L : 64L,
            4L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);
        makeWires(
            Materials.Platinum,
            1640,
            bEC ? 1L : 16L,
            bEC ? 2L : 32L,
            2L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);
        makeWires(
            Materials.TungstenSteel,
            1660,
            bEC ? 2L : 14L,
            bEC ? 4L : 28L,
            3L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);
        makeWires(
            Materials.Tungsten,
            1680,
            bEC ? 2L : 12L,
            bEC ? 4L : 24L,
            2L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);

        makeWires(
            Materials.HSSG,
            1700,
            bEC ? 2L : 128L,
            bEC ? 4L : 256L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);
        makeWires(
            Materials.NiobiumTitanium,
            1720,
            bEC ? 2L : 128L,
            bEC ? 4L : 256L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);
        makeWires(
            Materials.VanadiumGallium,
            1740,
            bEC ? 2L : 128L,
            bEC ? 4L : 256L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);
        makeWires(
            Materials.YttriumBariumCuprate,
            1760,
            bEC ? 4L : 256L,
            bEC ? 8L : 512L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);

        makeWires(
            Materials.Naquadah,
            1780,
            bEC ? 2L : 64L,
            bEC ? 4L : 128L,
            2L,
            gregtech.api.enums.GT_Values.V[7],
            true,
            false);

        makeWires(
            Materials.NaquadahAlloy,
            1800,
            bEC ? 4L : 64L,
            bEC ? 8L : 128L,
            2L,
            gregtech.api.enums.GT_Values.V[8],
            true,
            false);
        makeWires(
            Materials.Duranium,
            1820,
            bEC ? 8L : 64L,
            bEC ? 16L : 128L,
            1L,
            gregtech.api.enums.GT_Values.V[8],
            true,
            false);
        makeWires(
            Materials.TPV,
            1840,
            bEC ? 1L : 14L,
            bEC ? 2L : 28L,
            6L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);

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
