package tectech.loader.thing;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.MetaTileEntityIDs.ActiveTransformer;
import static gregtech.api.enums.MetaTileEntityIDs.AdvancedTeslaTransceiver1by1;
import static gregtech.api.enums.MetaTileEntityIDs.AdvancedTeslaTransceiver2by2;
import static gregtech.api.enums.MetaTileEntityIDs.AdvancedTeslaTransceiver3by3;
import static gregtech.api.enums.MetaTileEntityIDs.AdvancedTeslaTransceiver4by4;
import static gregtech.api.enums.MetaTileEntityIDs.AssemblylineSlaveConnector;
import static gregtech.api.enums.MetaTileEntityIDs.AutoTapingMaintenanceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.BasicTeslaTransceiver1by1;
import static gregtech.api.enums.MetaTileEntityIDs.BasicTeslaTransceiver2by2;
import static gregtech.api.enums.MetaTileEntityIDs.BasicTeslaTransceiver3by3;
import static gregtech.api.enums.MetaTileEntityIDs.BasicTeslaTransceiver4by4;
import static gregtech.api.enums.MetaTileEntityIDs.CapacitorHatch;
import static gregtech.api.enums.MetaTileEntityIDs.CloudComputationClientHatch;
import static gregtech.api.enums.MetaTileEntityIDs.CloudComputationServerHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ComputerRack;
import static gregtech.api.enums.MetaTileEntityIDs.DataBank;
import static gregtech.api.enums.MetaTileEntityIDs.DataBankMasterConnector;
import static gregtech.api.enums.MetaTileEntityIDs.DebugDataHatch;
import static gregtech.api.enums.MetaTileEntityIDs.DebugPollutionGenerator;
import static gregtech.api.enums.MetaTileEntityIDs.DebugPowerGenerator;
import static gregtech.api.enums.MetaTileEntityIDs.DebugStructureWriter;
import static gregtech.api.enums.MetaTileEntityIDs.EV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.EnergyInfuser;
import static gregtech.api.enums.MetaTileEntityIDs.EpycTeslaTransceiver1by1;
import static gregtech.api.enums.MetaTileEntityIDs.EpycTeslaTransceiver2by2;
import static gregtech.api.enums.MetaTileEntityIDs.EpycTeslaTransceiver3by3;
import static gregtech.api.enums.MetaTileEntityIDs.EpycTeslaTransceiver4by4;
import static gregtech.api.enums.MetaTileEntityIDs.ExtendedMegaUltimateBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.ExtremelyUltimateBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.EyeofHarmony;
import static gregtech.api.enums.MetaTileEntityIDs.ForgeoftheGods;
import static gregtech.api.enums.MetaTileEntityIDs.HelioflarePowerForge;
import static gregtech.api.enums.MetaTileEntityIDs.HeliofluxMeltingCore;
import static gregtech.api.enums.MetaTileEntityIDs.HeliofusionExoticizer;
import static gregtech.api.enums.MetaTileEntityIDs.HeliothermalPlasmaFabricator;
import static gregtech.api.enums.MetaTileEntityIDs.HighlyUltimateBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.IV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.IV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.InsaneBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.InsaneTeslaTransceiver1by1;
import static gregtech.api.enums.MetaTileEntityIDs.InsaneTeslaTransceiver2by2;
import static gregtech.api.enums.MetaTileEntityIDs.InsaneTeslaTransceiver3by3;
import static gregtech.api.enums.MetaTileEntityIDs.InsaneTeslaTransceiver4by4;
import static gregtech.api.enums.MetaTileEntityIDs.InsanelyUltimateBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.LaserVacuumMirror;
import static gregtech.api.enums.MetaTileEntityIDs.LaserVacuumPipe;
import static gregtech.api.enums.MetaTileEntityIDs.LaserVacuumPipeCasing;
import static gregtech.api.enums.MetaTileEntityIDs.LegendaryLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LegendaryLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LuV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.LudicrousBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.MAX16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.MAX4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.MAX64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.MegaUltimateBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.MicrowaveGrinder;
import static gregtech.api.enums.MetaTileEntityIDs.NetworkSwitchAdv;
import static gregtech.api.enums.MetaTileEntityIDs.NetworkSwitchWithQoS;
import static gregtech.api.enums.MetaTileEntityIDs.ObjectHolder;
import static gregtech.api.enums.MetaTileEntityIDs.OpticalFiberCable;
import static gregtech.api.enums.MetaTileEntityIDs.OpticalFiberCableCasing;
import static gregtech.api.enums.MetaTileEntityIDs.OpticalMasterConnector;
import static gregtech.api.enums.MetaTileEntityIDs.OpticalSlaveConnector;
import static gregtech.api.enums.MetaTileEntityIDs.Ownerdetector;
import static gregtech.api.enums.MetaTileEntityIDs.QuantumComputer;
import static gregtech.api.enums.MetaTileEntityIDs.Researchstation;
import static gregtech.api.enums.MetaTileEntityIDs.TeslaTower;
import static gregtech.api.enums.MetaTileEntityIDs.TestFactoryHatch;
import static gregtech.api.enums.MetaTileEntityIDs.TestFactoryPipe;
import static gregtech.api.enums.MetaTileEntityIDs.UEV1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV16384AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV16384AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV256ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV262144AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV262144AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV4096AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV4096AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV65536AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UEV65536AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV16384AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV16384AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV256ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV4096AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV4096AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV65536AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UHV65536AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV1048576AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV1048576AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV16384AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV16384AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV256ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV262144AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV262144AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV4096AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV4096AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV65536AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UIV65536AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV1048576AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV1048576AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV16384AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV16384AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV256ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV262144AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV262144AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV4096AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV4096AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV65536AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UMV65536AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV16384AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV16384AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV256ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV4096AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV4096AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV1024AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV1048576AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV1048576AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV1048576AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV16384AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV16384AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV16384AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV256ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV256AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV262144AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV262144AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV262144AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV4096AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV4096AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV4096AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV65536AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV65536AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UXV65536AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.UltimatePowerBuckConverter;
import static gregtech.api.enums.MetaTileEntityIDs.UltimatePowerTeslaTransceiver1by1;
import static gregtech.api.enums.MetaTileEntityIDs.UltimatePowerTeslaTransceiver2by2;
import static gregtech.api.enums.MetaTileEntityIDs.UltimatePowerTeslaTransceiver3by3;
import static gregtech.api.enums.MetaTileEntityIDs.UltimatePowerTeslaTransceiver4by4;
import static gregtech.api.enums.MetaTileEntityIDs.UncertaintyResolution;
import static gregtech.api.enums.MetaTileEntityIDs.UncertaintyResolver;
import static gregtech.api.enums.MetaTileEntityIDs.UncertaintyResolverX;
import static gregtech.api.enums.MetaTileEntityIDs.WirelessAssemblylineSlaveConnector;
import static gregtech.api.enums.MetaTileEntityIDs.WirelessDataBankMasterConnector;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM1024AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM1024AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM16ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM16AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM16AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM256AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM256AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM4096AtLaserSourceHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM4096AtLaserTargetHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM4ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM4AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM4AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM64ADynamoHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM64AEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPM64AWirelessEnergyHatch;
import static gregtech.api.enums.MetaTileEntityIDs.ZPMVoltageBuckConverter;
import static tectech.thing.CustomItemList.DATApipe;
import static tectech.thing.CustomItemList.DATApipeBlock;
import static tectech.thing.CustomItemList.LASERpipe;
import static tectech.thing.CustomItemList.LASERpipeBlock;
import static tectech.thing.CustomItemList.LASERpipeSmart;
import static tectech.thing.CustomItemList.Machine_BuckConverter_IV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_LuV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_UEV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_UHV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_UIV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_UMV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_UV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_UXV;
import static tectech.thing.CustomItemList.Machine_BuckConverter_ZPM;
import static tectech.thing.CustomItemList.Machine_DebugGenny;
import static tectech.thing.CustomItemList.Machine_DebugPollutor;
import static tectech.thing.CustomItemList.Machine_DebugWriter;
import static tectech.thing.CustomItemList.Machine_Multi_Computer;
import static tectech.thing.CustomItemList.Machine_Multi_DataBank;
import static tectech.thing.CustomItemList.Machine_Multi_EyeOfHarmony;
import static tectech.thing.CustomItemList.Machine_Multi_ForgeOfGods;
import static tectech.thing.CustomItemList.Machine_Multi_Infuser;
import static tectech.thing.CustomItemList.Machine_Multi_Microwave;
import static tectech.thing.CustomItemList.Machine_Multi_MoltenModule;
import static tectech.thing.CustomItemList.Machine_Multi_PlasmaModule;
import static tectech.thing.CustomItemList.Machine_Multi_QuarkGluonPlasmaModule;
import static tectech.thing.CustomItemList.Machine_Multi_Research;
import static tectech.thing.CustomItemList.Machine_Multi_SmeltingModule;
import static tectech.thing.CustomItemList.Machine_Multi_Switch;
import static tectech.thing.CustomItemList.Machine_Multi_Switch_Adv;
import static tectech.thing.CustomItemList.Machine_Multi_TeslaCoil;
import static tectech.thing.CustomItemList.Machine_Multi_Transformer;
import static tectech.thing.CustomItemList.Machine_OwnerDetector;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_1by1_EV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_1by1_HV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_1by1_IV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_1by1_LV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_1by1_MV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_2by2_EV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_2by2_HV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_2by2_IV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_2by2_LV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_2by2_MV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_3by3_EV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_3by3_HV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_3by3_IV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_3by3_LV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_3by3_MV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_4by4_EV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_4by4_HV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_4by4_IV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_4by4_LV;
import static tectech.thing.CustomItemList.Machine_TeslaCoil_4by4_MV;
import static tectech.thing.CustomItemList.TestHatch;
import static tectech.thing.CustomItemList.TestPipe;
import static tectech.thing.CustomItemList.UncertaintyX_Hatch;
import static tectech.thing.CustomItemList.Uncertainty_Hatch;
import static tectech.thing.CustomItemList.UnusedStuff;
import static tectech.thing.CustomItemList.capacitor_Hatch;
import static tectech.thing.CustomItemList.dataInAss_Hatch;
import static tectech.thing.CustomItemList.dataInAss_Wireless_Hatch;
import static tectech.thing.CustomItemList.dataIn_Hatch;
import static tectech.thing.CustomItemList.dataIn_Wireless_Hatch;
import static tectech.thing.CustomItemList.dataOutAss_Hatch;
import static tectech.thing.CustomItemList.dataOutAss_Wireless_Hatch;
import static tectech.thing.CustomItemList.dataOut_Hatch;
import static tectech.thing.CustomItemList.dataOut_Wireless_Hatch;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_EV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_IV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_LuV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_UEV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_UHV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_UIV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_UMV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_UV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_UXV;
import static tectech.thing.CustomItemList.eM_dynamoMulti16_ZPM;
import static tectech.thing.CustomItemList.eM_dynamoMulti256_UEV;
import static tectech.thing.CustomItemList.eM_dynamoMulti256_UHV;
import static tectech.thing.CustomItemList.eM_dynamoMulti256_UIV;
import static tectech.thing.CustomItemList.eM_dynamoMulti256_UMV;
import static tectech.thing.CustomItemList.eM_dynamoMulti256_UV;
import static tectech.thing.CustomItemList.eM_dynamoMulti256_UXV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_EV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_IV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_LuV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_UEV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_UHV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_UIV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_UMV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_UV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_UXV;
import static tectech.thing.CustomItemList.eM_dynamoMulti4_ZPM;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_EV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_IV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_LuV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_UEV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_UHV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_UIV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_UMV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_UV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_UXV;
import static tectech.thing.CustomItemList.eM_dynamoMulti64_ZPM;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_IV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_LuV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_UEV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_UHV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_UIV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_UV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel1_ZPM;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_LuV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_UEV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_UHV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_UIV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_UV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel2_ZPM;
import static tectech.thing.CustomItemList.eM_dynamoTunnel3_UEV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel3_UHV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel3_UIV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel3_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel3_UV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel3_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel3_ZPM;
import static tectech.thing.CustomItemList.eM_dynamoTunnel4_UEV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel4_UHV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel4_UIV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel4_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel4_UV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel4_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel5_UEV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel5_UHV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel5_UIV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel5_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel5_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel6_UEV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel6_UIV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel6_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel6_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel7_UIV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel7_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel7_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel8_UMV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel8_UXV;
import static tectech.thing.CustomItemList.eM_dynamoTunnel9001;
import static tectech.thing.CustomItemList.eM_dynamoTunnel9_UXV;
import static tectech.thing.CustomItemList.eM_dynamoWirelessMulti;
import static tectech.thing.CustomItemList.eM_energyMulti16_EV;
import static tectech.thing.CustomItemList.eM_energyMulti16_IV;
import static tectech.thing.CustomItemList.eM_energyMulti16_LuV;
import static tectech.thing.CustomItemList.eM_energyMulti16_UEV;
import static tectech.thing.CustomItemList.eM_energyMulti16_UHV;
import static tectech.thing.CustomItemList.eM_energyMulti16_UIV;
import static tectech.thing.CustomItemList.eM_energyMulti16_UMV;
import static tectech.thing.CustomItemList.eM_energyMulti16_UV;
import static tectech.thing.CustomItemList.eM_energyMulti16_UXV;
import static tectech.thing.CustomItemList.eM_energyMulti16_ZPM;
import static tectech.thing.CustomItemList.eM_energyMulti4_EV;
import static tectech.thing.CustomItemList.eM_energyMulti4_IV;
import static tectech.thing.CustomItemList.eM_energyMulti4_LuV;
import static tectech.thing.CustomItemList.eM_energyMulti4_UEV;
import static tectech.thing.CustomItemList.eM_energyMulti4_UHV;
import static tectech.thing.CustomItemList.eM_energyMulti4_UIV;
import static tectech.thing.CustomItemList.eM_energyMulti4_UMV;
import static tectech.thing.CustomItemList.eM_energyMulti4_UV;
import static tectech.thing.CustomItemList.eM_energyMulti4_UXV;
import static tectech.thing.CustomItemList.eM_energyMulti4_ZPM;
import static tectech.thing.CustomItemList.eM_energyMulti64_EV;
import static tectech.thing.CustomItemList.eM_energyMulti64_IV;
import static tectech.thing.CustomItemList.eM_energyMulti64_LuV;
import static tectech.thing.CustomItemList.eM_energyMulti64_UEV;
import static tectech.thing.CustomItemList.eM_energyMulti64_UHV;
import static tectech.thing.CustomItemList.eM_energyMulti64_UIV;
import static tectech.thing.CustomItemList.eM_energyMulti64_UMV;
import static tectech.thing.CustomItemList.eM_energyMulti64_UV;
import static tectech.thing.CustomItemList.eM_energyMulti64_UXV;
import static tectech.thing.CustomItemList.eM_energyMulti64_ZPM;
import static tectech.thing.CustomItemList.eM_energyTunnel1_IV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_LuV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_UEV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_UHV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_UIV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_UV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel1_ZPM;
import static tectech.thing.CustomItemList.eM_energyTunnel2_LuV;
import static tectech.thing.CustomItemList.eM_energyTunnel2_UEV;
import static tectech.thing.CustomItemList.eM_energyTunnel2_UHV;
import static tectech.thing.CustomItemList.eM_energyTunnel2_UIV;
import static tectech.thing.CustomItemList.eM_energyTunnel2_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel2_UV;
import static tectech.thing.CustomItemList.eM_energyTunnel2_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel2_ZPM;
import static tectech.thing.CustomItemList.eM_energyTunnel3_UEV;
import static tectech.thing.CustomItemList.eM_energyTunnel3_UHV;
import static tectech.thing.CustomItemList.eM_energyTunnel3_UIV;
import static tectech.thing.CustomItemList.eM_energyTunnel3_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel3_UV;
import static tectech.thing.CustomItemList.eM_energyTunnel3_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel3_ZPM;
import static tectech.thing.CustomItemList.eM_energyTunnel4_UEV;
import static tectech.thing.CustomItemList.eM_energyTunnel4_UHV;
import static tectech.thing.CustomItemList.eM_energyTunnel4_UIV;
import static tectech.thing.CustomItemList.eM_energyTunnel4_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel4_UV;
import static tectech.thing.CustomItemList.eM_energyTunnel4_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel5_UEV;
import static tectech.thing.CustomItemList.eM_energyTunnel5_UHV;
import static tectech.thing.CustomItemList.eM_energyTunnel5_UIV;
import static tectech.thing.CustomItemList.eM_energyTunnel5_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel5_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel6_UEV;
import static tectech.thing.CustomItemList.eM_energyTunnel6_UIV;
import static tectech.thing.CustomItemList.eM_energyTunnel6_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel6_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel7_UIV;
import static tectech.thing.CustomItemList.eM_energyTunnel7_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel7_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel8_UMV;
import static tectech.thing.CustomItemList.eM_energyTunnel8_UXV;
import static tectech.thing.CustomItemList.eM_energyTunnel9001;
import static tectech.thing.CustomItemList.eM_energyTunnel9_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_EV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_IV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_LuV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_MAX;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_UEV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_UHV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_UIV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_UMV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_UV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti16_ZPM;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_EV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_IV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_LuV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_MAX;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_UEV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_UHV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_UIV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_UMV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_UV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti4_ZPM;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_EV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_IV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_LuV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_MAX;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_UEV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_UHV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_UIV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_UMV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_UV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessMulti64_ZPM;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel1_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel2_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel3_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel4_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel5_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel6_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel7_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel8_UXV;
import static tectech.thing.CustomItemList.eM_energyWirelessTunnel9_UXV;
import static tectech.thing.CustomItemList.hatch_CreativeData;
import static tectech.thing.CustomItemList.hatch_CreativeMaintenance;
import static tectech.thing.CustomItemList.hatch_CreativeUncertainty;
import static tectech.thing.CustomItemList.holder_Hatch;
import static tectech.thing.CustomItemList.rack_Hatch;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.factory.test.TestFactoryHatch;
import gregtech.api.factory.test.TestFactoryPipe;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import tectech.thing.metaTileEntity.hatch.MTEHatchCapacitor;
import tectech.thing.metaTileEntity.hatch.MTEHatchCreativeData;
import tectech.thing.metaTileEntity.hatch.MTEHatchCreativeMaintenance;
import tectech.thing.metaTileEntity.hatch.MTEHatchCreativeUncertainty;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchObjectHolder;
import tectech.thing.metaTileEntity.hatch.MTEHatchRack;
import tectech.thing.metaTileEntity.hatch.MTEHatchUncertainty;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessComputationInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessComputationOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessDataItemsInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessDataItemsOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessMulti;
import tectech.thing.metaTileEntity.multi.MTEActiveTransformer;
import tectech.thing.metaTileEntity.multi.MTEDataBank;
import tectech.thing.metaTileEntity.multi.MTEEnergyInfuser;
import tectech.thing.metaTileEntity.multi.MTEEyeOfHarmony;
import tectech.thing.metaTileEntity.multi.MTEMicrowave;
import tectech.thing.metaTileEntity.multi.MTENetworkSwitch;
import tectech.thing.metaTileEntity.multi.MTENetworkSwitchAdv;
import tectech.thing.metaTileEntity.multi.MTEQuantumComputer;
import tectech.thing.metaTileEntity.multi.MTEResearchStation;
import tectech.thing.metaTileEntity.multi.MTETeslaTower;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;
import tectech.thing.metaTileEntity.pipe.MTEPipeBlockData;
import tectech.thing.metaTileEntity.pipe.MTEPipeBlockLaser;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaserMirror;
import tectech.thing.metaTileEntity.single.MTEBuckConverter;
import tectech.thing.metaTileEntity.single.MTEDebugPollutor;
import tectech.thing.metaTileEntity.single.MTEDebugPowerGenerator;
import tectech.thing.metaTileEntity.single.MTEDebugStructureWriter;
import tectech.thing.metaTileEntity.single.MTEOwnerDetector;
import tectech.thing.metaTileEntity.single.MTETeslaCoil;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class MachineLoader implements Runnable {

    @Override
    public void run() {

        // ===================================================================================================
        // Multi AMP Power INPUTS
        // ===================================================================================================
        eM_energyMulti4_EV.set(
            new MTEHatchEnergyMulti(EV4AEnergyHatch.ID, "hatch.energymulti04.tier.04", "EV 4A Energy Hatch", 4, 4)
                .getStackForm(1L));
        eM_energyMulti16_EV.set(
            new MTEHatchEnergyMulti(EV16AEnergyHatch.ID, "hatch.energymulti16.tier.04", "EV 16A Energy Hatch", 4, 16)
                .getStackForm(1L));
        eM_energyMulti64_EV.set(
            new MTEHatchEnergyMulti(EV64AEnergyHatch.ID, "hatch.energymulti64.tier.04", "EV 64A Energy Hatch", 4, 64)
                .getStackForm(1L));

        eM_energyMulti4_IV.set(
            new MTEHatchEnergyMulti(IV4AEnergyHatch.ID, "hatch.energymulti04.tier.05", "IV 4A Energy Hatch", 5, 4)
                .getStackForm(1L));
        eM_energyMulti16_IV.set(
            new MTEHatchEnergyMulti(IV16AEnergyHatch.ID, "hatch.energymulti16.tier.05", "IV 16A Energy Hatch", 5, 16)
                .getStackForm(1L));
        eM_energyMulti64_IV.set(
            new MTEHatchEnergyMulti(IV64AEnergyHatch.ID, "hatch.energymulti64.tier.05", "IV 64A Energy Hatch", 5, 64)
                .getStackForm(1L));

        eM_energyMulti4_LuV.set(
            new MTEHatchEnergyMulti(LuV4AEnergyHatch.ID, "hatch.energymulti04.tier.06", "LuV 4A Energy Hatch", 6, 4)
                .getStackForm(1L));
        eM_energyMulti16_LuV.set(
            new MTEHatchEnergyMulti(LuV16AEnergyHatch.ID, "hatch.energymulti16.tier.06", "LuV 16A Energy Hatch", 6, 16)
                .getStackForm(1L));
        eM_energyMulti64_LuV.set(
            new MTEHatchEnergyMulti(LuV64AEnergyHatch.ID, "hatch.energymulti64.tier.06", "LuV 64A Energy Hatch", 6, 64)
                .getStackForm(1L));

        eM_energyMulti4_ZPM.set(
            new MTEHatchEnergyMulti(ZPM4AEnergyHatch.ID, "hatch.energymulti04.tier.07", "ZPM 4A Energy Hatch", 7, 4)
                .getStackForm(1L));
        eM_energyMulti16_ZPM.set(
            new MTEHatchEnergyMulti(ZPM16AEnergyHatch.ID, "hatch.energymulti16.tier.07", "ZPM 16A Energy Hatch", 7, 16)
                .getStackForm(1L));
        eM_energyMulti64_ZPM.set(
            new MTEHatchEnergyMulti(ZPM64AEnergyHatch.ID, "hatch.energymulti64.tier.07", "ZPM 64A Energy Hatch", 7, 64)
                .getStackForm(1L));

        eM_energyMulti4_UV.set(
            new MTEHatchEnergyMulti(UV4AEnergyHatch.ID, "hatch.energymulti04.tier.08", "UV 4A Energy Hatch", 8, 4)
                .getStackForm(1L));
        eM_energyMulti16_UV.set(
            new MTEHatchEnergyMulti(UV16AEnergyHatch.ID, "hatch.energymulti16.tier.08", "UV 16A Energy Hatch", 8, 16)
                .getStackForm(1L));
        eM_energyMulti64_UV.set(
            new MTEHatchEnergyMulti(UV64AEnergyHatch.ID, "hatch.energymulti64.tier.08", "UV 64A Energy Hatch", 8, 64)
                .getStackForm(1L));

        eM_energyMulti4_UHV.set(
            new MTEHatchEnergyMulti(UHV4AEnergyHatch.ID, "hatch.energymulti04.tier.09", "UHV 4A Energy Hatch", 9, 4)
                .getStackForm(1L));
        eM_energyMulti16_UHV.set(
            new MTEHatchEnergyMulti(UHV16AEnergyHatch.ID, "hatch.energymulti16.tier.09", "UHV 16A Energy Hatch", 9, 16)
                .getStackForm(1L));
        eM_energyMulti64_UHV.set(
            new MTEHatchEnergyMulti(UHV64AEnergyHatch.ID, "hatch.energymulti64.tier.09", "UHV 64A Energy Hatch", 9, 64)
                .getStackForm(1L));

        eM_energyMulti4_UEV.set(
            new MTEHatchEnergyMulti(UEV4AEnergyHatch.ID, "hatch.energymulti04.tier.10", "UEV 4A Energy Hatch", 10, 4)
                .getStackForm(1L));
        eM_energyMulti16_UEV.set(
            new MTEHatchEnergyMulti(UEV16AEnergyHatch.ID, "hatch.energymulti16.tier.10", "UEV 16A Energy Hatch", 10, 16)
                .getStackForm(1L));
        eM_energyMulti64_UEV.set(
            new MTEHatchEnergyMulti(UEV64AEnergyHatch.ID, "hatch.energymulti64.tier.10", "UEV 64A Energy Hatch", 10, 64)
                .getStackForm(1L));

        eM_energyMulti4_UIV.set(
            new MTEHatchEnergyMulti(UIV4AEnergyHatch.ID, "hatch.energymulti04.tier.11", "UIV 4A Energy Hatch", 11, 4)
                .getStackForm(1L));
        eM_energyMulti16_UIV.set(
            new MTEHatchEnergyMulti(UIV16AEnergyHatch.ID, "hatch.energymulti16.tier.11", "UIV 16A Energy Hatch", 11, 16)
                .getStackForm(1L));
        eM_energyMulti64_UIV.set(
            new MTEHatchEnergyMulti(UIV64AEnergyHatch.ID, "hatch.energymulti64.tier.11", "UIV 64A Energy Hatch", 11, 64)
                .getStackForm(1L));

        eM_energyMulti4_UMV.set(
            new MTEHatchEnergyMulti(UMV4AEnergyHatch.ID, "hatch.energymulti04.tier.12", "UMV 4A Energy Hatch", 12, 4)
                .getStackForm(1L));
        eM_energyMulti16_UMV.set(
            new MTEHatchEnergyMulti(UMV16AEnergyHatch.ID, "hatch.energymulti16.tier.12", "UMV 16A Energy Hatch", 12, 16)
                .getStackForm(1L));
        eM_energyMulti64_UMV.set(
            new MTEHatchEnergyMulti(UMV64AEnergyHatch.ID, "hatch.energymulti64.tier.12", "UMV 64A Energy Hatch", 12, 64)
                .getStackForm(1L));

        eM_energyMulti4_UXV.set(
            new MTEHatchEnergyMulti(UXV4AEnergyHatch.ID, "hatch.energymulti04.tier.13", "UXV 4A Energy Hatch", 13, 4)
                .getStackForm(1L));
        eM_energyMulti16_UXV.set(
            new MTEHatchEnergyMulti(UXV16AEnergyHatch.ID, "hatch.energymulti16.tier.13", "UXV 16A Energy Hatch", 13, 16)
                .getStackForm(1L));
        eM_energyMulti64_UXV.set(
            new MTEHatchEnergyMulti(UXV64AEnergyHatch.ID, "hatch.energymulti64.tier.13", "UXV 64A Energy Hatch", 13, 64)
                .getStackForm(1L));
        // ===================================================================================================
        // Multi AMP Wireless INPUTS
        // ===================================================================================================
        eM_energyWirelessMulti4_EV.set(
            new MTEHatchWirelessMulti(
                EV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.04",
                "EV 4A Wireless Energy Hatch",
                4,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_EV.set(
            new MTEHatchWirelessMulti(
                EV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.04",
                "EV 16A Wireless Energy Hatch",
                4,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_EV.set(
            new MTEHatchWirelessMulti(
                EV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.04",
                "EV 64A Wireless Energy Hatch",
                4,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_IV.set(
            new MTEHatchWirelessMulti(
                IV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.05",
                "IV 4A Wireless Energy Hatch",
                5,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_IV.set(
            new MTEHatchWirelessMulti(
                IV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.05",
                "IV 16A Wireless Energy Hatch",
                5,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_IV.set(
            new MTEHatchWirelessMulti(
                IV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.05",
                "IV 64A Wireless Energy Hatch",
                5,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_LuV.set(
            new MTEHatchWirelessMulti(
                LuV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.06",
                "LuV 4A Wireless Energy Hatch",
                6,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_LuV.set(
            new MTEHatchWirelessMulti(
                LuV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.06",
                "LuV 16A Wireless Energy Hatch",
                6,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_LuV.set(
            new MTEHatchWirelessMulti(
                LuV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.06",
                "LuV 64A Wireless Energy Hatch",
                6,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_ZPM.set(
            new MTEHatchWirelessMulti(
                ZPM4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.07",
                "ZPM 4A Wireless Energy Hatch",
                7,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_ZPM.set(
            new MTEHatchWirelessMulti(
                ZPM16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.07",
                "ZPM 16A Wireless Energy Hatch",
                7,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_ZPM.set(
            new MTEHatchWirelessMulti(
                ZPM64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.07",
                "ZPM 64A Wireless Energy Hatch",
                7,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_UV.set(
            new MTEHatchWirelessMulti(
                UV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.08",
                "UV 4A Wireless Energy Hatch",
                8,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_UV.set(
            new MTEHatchWirelessMulti(
                UV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.08",
                "UV 16A Wireless Energy Hatch",
                8,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_UV.set(
            new MTEHatchWirelessMulti(
                UV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.08",
                "UV 64A Wireless Energy Hatch",
                8,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_UHV.set(
            new MTEHatchWirelessMulti(
                UHV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.09",
                "UHV 4A Wireless Energy Hatch",
                9,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_UHV.set(
            new MTEHatchWirelessMulti(
                UHV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.09",
                "UHV 16A Wireless Energy Hatch",
                9,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_UHV.set(
            new MTEHatchWirelessMulti(
                UHV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.09",
                "UHV 64A Wireless Energy Hatch",
                9,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_UEV.set(
            new MTEHatchWirelessMulti(
                UEV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.10",
                "UEV 4A Wireless Energy Hatch",
                10,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_UEV.set(
            new MTEHatchWirelessMulti(
                UEV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.10",
                "UEV 16A Wireless Energy Hatch",
                10,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_UEV.set(
            new MTEHatchWirelessMulti(
                UEV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.10",
                "UEV 64A Wireless Energy Hatch",
                10,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_UIV.set(
            new MTEHatchWirelessMulti(
                UIV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.11",
                "UIV 4A Wireless Energy Hatch",
                11,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_UIV.set(
            new MTEHatchWirelessMulti(
                UIV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.11",
                "UIV 16A Wireless Energy Hatch",
                11,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_UIV.set(
            new MTEHatchWirelessMulti(
                UIV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.11",
                "UIV 64A Wireless Energy Hatch",
                11,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_UMV.set(
            new MTEHatchWirelessMulti(
                UMV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.12",
                "UMV 4A Wireless Energy Hatch",
                12,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_UMV.set(
            new MTEHatchWirelessMulti(
                UMV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.12",
                "UMV 16A Wireless Energy Hatch",
                12,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_UMV.set(
            new MTEHatchWirelessMulti(
                UMV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.12",
                "UMV 64A Wireless Energy Hatch",
                12,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_UXV.set(
            new MTEHatchWirelessMulti(
                UXV4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.13",
                "UXV 4A Wireless Energy Hatch",
                13,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_UXV.set(
            new MTEHatchWirelessMulti(
                UXV16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.13",
                "UXV 16A Wireless Energy Hatch",
                13,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_UXV.set(
            new MTEHatchWirelessMulti(
                UXV64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.13",
                "UXV 64A Wireless Energy Hatch",
                13,
                64).getStackForm(1L));
        eM_energyWirelessMulti4_MAX.set(
            new MTEHatchWirelessMulti(
                MAX4AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti04.tier.14",
                "MAX 4A Wireless Energy Hatch",
                14,
                4).getStackForm(1L));
        eM_energyWirelessMulti16_MAX.set(
            new MTEHatchWirelessMulti(
                MAX16AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti16.tier.14",
                "MAX 16A Wireless Energy Hatch",
                14,
                16).getStackForm(1L));
        eM_energyWirelessMulti64_MAX.set(
            new MTEHatchWirelessMulti(
                MAX64AWirelessEnergyHatch.ID,
                "hatch.energywirelessmulti64.tier.14",
                "MAX 64A Wireless Energy Hatch",
                14,
                64).getStackForm(1L));
        eM_energyWirelessTunnel1_UXV.set(
            new MTEHatchWirelessMulti(
                UXV256AWirelessEnergyHatch.ID,
                "hatch.energywirelesstunnel1.tier.13",
                "UXV 256A Wireless Energy Hatch",
                13,
                256).getStackForm(1L));
        eM_energyWirelessTunnel2_UXV.set(
            new MTEHatchWirelessMulti(
                UXV1024AWirelessEnergyHatch.ID,
                "hatch.energywirelesstunnel2.tier.13",
                "UXV 1,024A Wireless Energy Hatch",
                13,
                1024).getStackForm(1L));
        eM_energyWirelessTunnel3_UXV.set(
            new MTEHatchWirelessMulti(
                UXV4096AWirelessEnergyHatch.ID,
                "hatch.energywirelesstunnel3.tier.13",
                "UXV 4,096A Wireless Energy Hatch",
                13,
                4096).getStackForm(1L));
        eM_energyWirelessTunnel4_UXV.set(
            new MTEHatchWirelessMulti(
                UXV16384AWirelessEnergyHatch.ID,
                "hatch.energywirelesstunnel4.tier.13",
                "UXV 16,384A Wireless Energy Hatch",
                13,
                16384).getStackForm(1L));
        eM_energyWirelessTunnel5_UXV.set(
            new MTEHatchWirelessMulti(
                UXV65536AWirelessEnergyHatch.ID,
                "hatch.energywirelesstunnel5.tier.13",
                "UXV 65,536A Wireless Energy Hatch",
                13,
                65536).getStackForm(1L));
        eM_energyWirelessTunnel6_UXV.set(
            new MTEHatchWirelessMulti(
                UXV262144AWirelessEnergyHatch.ID,
                "hatch.energywirelesstunnel6.tier.13",
                "UXV 262,144A Wireless Energy Hatch",
                13,
                262144).getStackForm(1L));
        eM_energyWirelessTunnel7_UXV.set(
            new MTEHatchWirelessMulti(
                UXV1048576AWirelessEnergyHatch.ID,
                "hatch.energywirelesstunnel7.tier.13",
                "UXV 1,048,576A Wireless Energy Hatch",
                13,
                1048576).getStackForm(1L));
        eM_energyWirelessTunnel8_UXV.set(
            new MTEHatchWirelessMulti(
                MetaTileEntityIDs.WIRELESS_LASER_UXV8.ID,
                "hatch.energywirelesstunnel8.tier.13",
                "UXV 4,194,304A Wireless Energy Hatch",
                13,
                4194304).getStackForm(1L));
        eM_energyWirelessTunnel9_UXV.set(
            new MTEHatchWirelessMulti(
                MetaTileEntityIDs.WIRELESS_LASER_UXV9.ID,
                "hatch.energywirelesstunnel9.tier.13",
                "UXV 16,777,216A Wireless Energy Hatch",
                13,
                16777216).getStackForm(1L));
        // ===================================================================================================
        // Multi AMP Laser INPUTS
        // ===================================================================================================

        eM_energyTunnel1_IV.set(
            new MTEHatchEnergyTunnel(
                IV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.05",
                "IV 256A/t Laser Target Hatch",
                5,
                256).getStackForm(1L));
        eM_energyTunnel1_LuV.set(
            new MTEHatchEnergyTunnel(
                LuV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.06",
                "LuV 256A/t Laser Target Hatch",
                6,
                256).getStackForm(1L));
        eM_energyTunnel2_LuV.set(
            new MTEHatchEnergyTunnel(
                LuV1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.06",
                "LuV 1,024A/t Laser Target Hatch",
                6,
                1024).getStackForm(1L));
        eM_energyTunnel1_ZPM.set(
            new MTEHatchEnergyTunnel(
                ZPM256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.07",
                "ZPM 256A/t Laser Target Hatch",
                7,
                256).getStackForm(1L));
        eM_energyTunnel2_ZPM.set(
            new MTEHatchEnergyTunnel(
                ZPM1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.07",
                "ZPM 1,024A/t Laser Target Hatch",
                7,
                1024).getStackForm(1L));
        eM_energyTunnel3_ZPM.set(
            new MTEHatchEnergyTunnel(
                ZPM4096AtLaserTargetHatch.ID,
                "hatch.energytunnel3.tier.07",
                "ZPM 4,096A/t Laser Target Hatch",
                7,
                4096).getStackForm(1L));
        eM_energyTunnel1_UV.set(
            new MTEHatchEnergyTunnel(
                UV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.08",
                "UV 256A/t Laser Target Hatch",
                8,
                256).getStackForm(1L));
        eM_energyTunnel2_UV.set(
            new MTEHatchEnergyTunnel(
                UV1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.08",
                "UV 1,024A/t Laser Target Hatch",
                8,
                1024).getStackForm(1L));
        eM_energyTunnel3_UV.set(
            new MTEHatchEnergyTunnel(
                UV4096AtLaserTargetHatch.ID,
                "hatch.energytunnel3.tier.08",
                "UV 4,096A/t Laser Target Hatch",
                8,
                4096).getStackForm(1L));
        eM_energyTunnel4_UV.set(
            new MTEHatchEnergyTunnel(
                UV16384AtLaserTargetHatch.ID,
                "hatch.energytunnel4.tier.08",
                "UV 16,384A/t Laser Target Hatch",
                8,
                16384).getStackForm(1L));
        eM_energyTunnel1_UHV.set(
            new MTEHatchEnergyTunnel(
                UHV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.09",
                "UHV 256A/t Laser Target Hatch",
                9,
                256).getStackForm(1L));
        eM_energyTunnel2_UHV.set(
            new MTEHatchEnergyTunnel(
                UHV1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.09",
                "UHV 1,024A/t Laser Target Hatch",
                9,
                1024).getStackForm(1L));
        eM_energyTunnel3_UHV.set(
            new MTEHatchEnergyTunnel(
                UHV4096AtLaserTargetHatch.ID,
                "hatch.energytunnel3.tier.09",
                "UHV 4,096A/t Laser Target Hatch",
                9,
                4096).getStackForm(1L));
        eM_energyTunnel4_UHV.set(
            new MTEHatchEnergyTunnel(
                UHV16384AtLaserTargetHatch.ID,
                "hatch.energytunnel4.tier.09",
                "UHV 16,384A/t Laser Target Hatch",
                9,
                16384).getStackForm(1L));
        eM_energyTunnel5_UHV.set(
            new MTEHatchEnergyTunnel(
                UHV65536AtLaserTargetHatch.ID,
                "hatch.energytunnel5.tier.09",
                "UHV 65,536A/t Laser Target Hatch",
                9,
                65536).getStackForm(1L));
        eM_energyTunnel1_UEV.set(
            new MTEHatchEnergyTunnel(
                UEV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.10",
                "UEV 256A/t Laser Target Hatch",
                10,
                256).getStackForm(1L));
        eM_energyTunnel2_UEV.set(
            new MTEHatchEnergyTunnel(
                UEV1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.10",
                "UEV 1,024A/t Laser Target Hatch",
                10,
                1024).getStackForm(1L));
        eM_energyTunnel3_UEV.set(
            new MTEHatchEnergyTunnel(
                UEV4096AtLaserTargetHatch.ID,
                "hatch.energytunnel3.tier.10",
                "UEV 4,096A/t Laser Target Hatch",
                10,
                4096).getStackForm(1L));
        eM_energyTunnel4_UEV.set(
            new MTEHatchEnergyTunnel(
                UEV16384AtLaserTargetHatch.ID,
                "hatch.energytunnel4.tier.10",
                "UEV 16,384A/t Laser Target Hatch",
                10,
                16384).getStackForm(1L));
        eM_energyTunnel5_UEV.set(
            new MTEHatchEnergyTunnel(
                UEV65536AtLaserTargetHatch.ID,
                "hatch.energytunnel5.tier.10",
                "UEV 65,536A/t Laser Target Hatch",
                10,
                65536).getStackForm(1L));
        eM_energyTunnel6_UEV.set(
            new MTEHatchEnergyTunnel(
                UEV262144AtLaserTargetHatch.ID,
                "hatch.energytunnel6.tier.10",
                "UEV 262,144A/t Laser Target Hatch",
                10,
                262144).getStackForm(1L));
        eM_energyTunnel1_UIV.set(
            new MTEHatchEnergyTunnel(
                UIV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.11",
                "UIV 256A/t Laser Target Hatch",
                11,
                256).getStackForm(1L));
        eM_energyTunnel2_UIV.set(
            new MTEHatchEnergyTunnel(
                UIV1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.11",
                "UIV 1,024A/t Laser Target Hatch",
                11,
                1024).getStackForm(1L));
        eM_energyTunnel3_UIV.set(
            new MTEHatchEnergyTunnel(
                UIV4096AtLaserTargetHatch.ID,
                "hatch.energytunnel3.tier.11",
                "UIV 4,096A/t Laser Target Hatch",
                11,
                4096).getStackForm(1L));
        eM_energyTunnel4_UIV.set(
            new MTEHatchEnergyTunnel(
                UIV16384AtLaserTargetHatch.ID,
                "hatch.energytunnel4.tier.11",
                "UIV 16,384A/t Laser Target Hatch",
                11,
                16384).getStackForm(1L));
        eM_energyTunnel5_UIV.set(
            new MTEHatchEnergyTunnel(
                UIV65536AtLaserTargetHatch.ID,
                "hatch.energytunnel5.tier.11",
                "UIV 65,536A/t Laser Target Hatch",
                11,
                65536).getStackForm(1L));
        eM_energyTunnel6_UIV.set(
            new MTEHatchEnergyTunnel(
                UIV262144AtLaserTargetHatch.ID,
                "hatch.energytunnel6.tier.11",
                "UIV 262,144A/t Laser Target Hatch",
                11,
                262144).getStackForm(1L));
        eM_energyTunnel7_UIV.set(
            new MTEHatchEnergyTunnel(
                UIV1048576AtLaserTargetHatch.ID,
                "hatch.energytunnel7.tier.11",
                "UIV 1,048,576A/t Laser Target Hatch",
                11,
                1048576).getStackForm(1L));
        eM_energyTunnel1_UMV.set(
            new MTEHatchEnergyTunnel(
                UMV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.12",
                "UMV 256A/t Laser Target Hatch",
                12,
                256).getStackForm(1L));
        eM_energyTunnel2_UMV.set(
            new MTEHatchEnergyTunnel(
                UMV1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.12",
                "UMV 1,024A/t Laser Target Hatch",
                12,
                1024).getStackForm(1L));
        eM_energyTunnel3_UMV.set(
            new MTEHatchEnergyTunnel(
                UMV4096AtLaserTargetHatch.ID,
                "hatch.energytunnel3.tier.12",
                "UMV 4,096A/t Laser Target Hatch",
                12,
                4096).getStackForm(1L));
        eM_energyTunnel4_UMV.set(
            new MTEHatchEnergyTunnel(
                UMV16384AtLaserTargetHatch.ID,
                "hatch.energytunnel4.tier.12",
                "UMV 16,384A/t Laser Target Hatch",
                12,
                16384).getStackForm(1L));
        eM_energyTunnel5_UMV.set(
            new MTEHatchEnergyTunnel(
                UMV65536AtLaserTargetHatch.ID,
                "hatch.energytunnel5.tier.12",
                "UMV 65,536A/t Laser Target Hatch",
                12,
                65536).getStackForm(1L));
        eM_energyTunnel6_UMV.set(
            new MTEHatchEnergyTunnel(
                UMV262144AtLaserTargetHatch.ID,
                "hatch.energytunnel6.tier.12",
                "UMV 262,144A/t Laser Target Hatch",
                12,
                262144).getStackForm(1L));
        eM_energyTunnel7_UMV.set(
            new MTEHatchEnergyTunnel(
                UMV1048576AtLaserTargetHatch.ID,
                "hatch.energytunnel7.tier.12",
                "UMV 1,048,576A/t Laser Target Hatch",
                12,
                1048576).getStackForm(1L));
        eM_energyTunnel8_UMV.set(
            new MTEHatchEnergyTunnel(
                MetaTileEntityIDs.LASER_TARGET_UMV8.ID,
                "hatch.energytunnel8.tier.12",
                "UMV 4,194,304A/t Laser Target Hatch",
                12,
                4194304).getStackForm(1L));
        eM_energyTunnel1_UXV.set(
            new MTEHatchEnergyTunnel(
                UXV256AtLaserTargetHatch.ID,
                "hatch.energytunnel1.tier.13",
                "UXV 256A/t Laser Target Hatch",
                13,
                256).getStackForm(1L));
        eM_energyTunnel2_UXV.set(
            new MTEHatchEnergyTunnel(
                UXV1024AtLaserTargetHatch.ID,
                "hatch.energytunnel2.tier.13",
                "UXV 1,024A/t Laser Target Hatch",
                13,
                1024).getStackForm(1L));
        eM_energyTunnel3_UXV.set(
            new MTEHatchEnergyTunnel(
                UXV4096AtLaserTargetHatch.ID,
                "hatch.energytunnel3.tier.13",
                "UXV 4,096A/t Laser Target Hatch",
                13,
                4096).getStackForm(1L));
        eM_energyTunnel4_UXV.set(
            new MTEHatchEnergyTunnel(
                UXV16384AtLaserTargetHatch.ID,
                "hatch.energytunnel4.tier.13",
                "UXV 16,384A/t Laser Target Hatch",
                13,
                16384).getStackForm(1L));
        eM_energyTunnel5_UXV.set(
            new MTEHatchEnergyTunnel(
                UXV65536AtLaserTargetHatch.ID,
                "hatch.energytunnel5.tier.13",
                "UXV 65,536A/t Laser Target Hatch",
                13,
                65536).getStackForm(1L));
        eM_energyTunnel6_UXV.set(
            new MTEHatchEnergyTunnel(
                UXV262144AtLaserTargetHatch.ID,
                "hatch.energytunnel6.tier.13",
                "UXV 262,144A/t Laser Target Hatch",
                13,
                262144).getStackForm(1L));
        eM_energyTunnel7_UXV.set(
            new MTEHatchEnergyTunnel(
                UXV1048576AtLaserTargetHatch.ID,
                "hatch.energytunnel7.tier.13",
                "UXV 1,048,576A/t Laser Target Hatch",
                13,
                1048576).getStackForm(1L));
        eM_energyTunnel8_UXV.set(
            new MTEHatchEnergyTunnel(
                MetaTileEntityIDs.LASER_TARGET_UXV8.ID,
                "hatch.energytunnel8.tier.13",
                "UXV 4,194,304A/t Laser Target Hatch",
                13,
                4194304).getStackForm(1L));
        eM_energyTunnel9_UXV.set(
            new MTEHatchEnergyTunnel(
                MetaTileEntityIDs.LASER_TARGET_UXV9.ID,
                "hatch.energytunnel9.tier.13",
                "UXV 16,777,216A/t Laser Target Hatch",
                13,
                16777216).getStackForm(1L));
        eM_energyTunnel9001.set(
            new MTEHatchEnergyTunnel(
                LegendaryLaserTargetHatch.ID,
                "hatch.energytunnel.tier.14",
                "Legendary Laser Target Hatch",
                13,
                (int) V[13]).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Wireless OUTPUTS
        // ===================================================================================================
        eM_dynamoWirelessMulti.set(
            new MTEHatchWirelessDynamoMulti(
                MetaTileEntityIDs.WIRELESS_DYNAMO_ENERGIZED.ID,
                "hatch.wireless.transmitter.energized.tier.12",
                "Energized Wireless Dynamo Hatch",
                12,
                65536).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Power OUTPUTS
        // ===================================================================================================
        eM_dynamoMulti4_EV.set(
            new MTEHatchDynamoMulti(EV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.04", "EV 4A Dynamo Hatch", 4, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_EV.set(
            new MTEHatchDynamoMulti(EV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.04", "EV 16A Dynamo Hatch", 4, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_EV.set(
            new MTEHatchDynamoMulti(EV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.04", "EV 64A Dynamo Hatch", 4, 64)
                .getStackForm(1L));
        eM_dynamoMulti4_IV.set(
            new MTEHatchDynamoMulti(IV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.05", "IV 4A Dynamo Hatch", 5, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_IV.set(
            new MTEHatchDynamoMulti(IV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.05", "IV 16A Dynamo Hatch", 5, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_IV.set(
            new MTEHatchDynamoMulti(IV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.05", "IV 64A Dynamo Hatch", 5, 64)
                .getStackForm(1L));
        eM_dynamoMulti4_LuV.set(
            new MTEHatchDynamoMulti(LuV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.06", "LuV 4A Dynamo Hatch", 6, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_LuV.set(
            new MTEHatchDynamoMulti(LuV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.06", "LuV 16A Dynamo Hatch", 6, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_LuV.set(
            new MTEHatchDynamoMulti(LuV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.06", "LuV 64A Dynamo Hatch", 6, 64)
                .getStackForm(1L));
        eM_dynamoMulti4_ZPM.set(
            new MTEHatchDynamoMulti(ZPM4ADynamoHatch.ID, "hatch.dynamomulti04.tier.07", "ZPM 4A Dynamo Hatch", 7, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_ZPM.set(
            new MTEHatchDynamoMulti(ZPM16ADynamoHatch.ID, "hatch.dynamomulti16.tier.07", "ZPM 16A Dynamo Hatch", 7, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_ZPM.set(
            new MTEHatchDynamoMulti(ZPM64ADynamoHatch.ID, "hatch.dynamomulti64.tier.07", "ZPM 64A Dynamo Hatch", 7, 64)
                .getStackForm(1L));
        eM_dynamoMulti4_UV.set(
            new MTEHatchDynamoMulti(UV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.08", "UV 4A Dynamo Hatch", 8, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_UV.set(
            new MTEHatchDynamoMulti(UV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.08", "UV 16A Dynamo Hatch", 8, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_UV.set(
            new MTEHatchDynamoMulti(UV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.08", "UV 64A Dynamo Hatch", 8, 64)
                .getStackForm(1L));
        eM_dynamoMulti256_UV.set(
            new MTEHatchDynamoMulti(
                UV256ADynamoHatch.ID,
                "hatch.dynamomulti256.tier.08",
                "UV 256A Dynamo Hatch",
                8,
                256).getStackForm(1L));
        eM_dynamoMulti4_UHV.set(
            new MTEHatchDynamoMulti(UHV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.09", "UHV 4A Dynamo Hatch", 9, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_UHV.set(
            new MTEHatchDynamoMulti(UHV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.09", "UHV 16A Dynamo Hatch", 9, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_UHV.set(
            new MTEHatchDynamoMulti(UHV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.09", "UHV 64A Dynamo Hatch", 9, 64)
                .getStackForm(1L));
        eM_dynamoMulti256_UHV.set(
            new MTEHatchDynamoMulti(
                UHV256ADynamoHatch.ID,
                "hatch.dynamomulti256.tier.09",
                "UHV 256A Dynamo Hatch",
                9,
                256).getStackForm(1L));
        eM_dynamoMulti4_UEV.set(
            new MTEHatchDynamoMulti(UEV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.10", "UEV 4A Dynamo Hatch", 10, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_UEV.set(
            new MTEHatchDynamoMulti(UEV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.10", "UEV 16A Dynamo Hatch", 10, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_UEV.set(
            new MTEHatchDynamoMulti(UEV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.10", "UEV 64A Dynamo Hatch", 10, 64)
                .getStackForm(1L));
        eM_dynamoMulti256_UEV.set(
            new MTEHatchDynamoMulti(
                UEV256ADynamoHatch.ID,
                "hatch.dynamomulti256.tier.10",
                "UEV 256A Dynamo Hatch",
                10,
                256).getStackForm(1L));
        eM_dynamoMulti4_UIV.set(
            new MTEHatchDynamoMulti(UIV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.11", "UIV 4A Dynamo Hatch", 11, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_UIV.set(
            new MTEHatchDynamoMulti(UIV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.11", "UIV 16A Dynamo Hatch", 11, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_UIV.set(
            new MTEHatchDynamoMulti(UIV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.11", "UIV 64A Dynamo Hatch", 11, 64)
                .getStackForm(1L));
        eM_dynamoMulti256_UIV.set(
            new MTEHatchDynamoMulti(
                UIV256ADynamoHatch.ID,
                "hatch.dynamomulti256.tier.11",
                "UIV 256A Dynamo Hatch",
                11,
                256).getStackForm(1L));
        eM_dynamoMulti4_UMV.set(
            new MTEHatchDynamoMulti(UMV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.12", "UMV 4A Dynamo Hatch", 12, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_UMV.set(
            new MTEHatchDynamoMulti(UMV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.12", "UMV 16A Dynamo Hatch", 12, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_UMV.set(
            new MTEHatchDynamoMulti(UMV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.12", "UMV 64A Dynamo Hatch", 12, 64)
                .getStackForm(1L));
        eM_dynamoMulti256_UMV.set(
            new MTEHatchDynamoMulti(
                UMV256ADynamoHatch.ID,
                "hatch.dynamomulti256.tier.12",
                "UMV 256A Dynamo Hatch",
                12,
                256).getStackForm(1L));
        eM_dynamoMulti4_UXV.set(
            new MTEHatchDynamoMulti(UXV4ADynamoHatch.ID, "hatch.dynamomulti04.tier.13", "UXV 4A Dynamo Hatch", 13, 4)
                .getStackForm(1L));
        eM_dynamoMulti16_UXV.set(
            new MTEHatchDynamoMulti(UXV16ADynamoHatch.ID, "hatch.dynamomulti16.tier.13", "UXV 16A Dynamo Hatch", 13, 16)
                .getStackForm(1L));
        eM_dynamoMulti64_UXV.set(
            new MTEHatchDynamoMulti(UXV64ADynamoHatch.ID, "hatch.dynamomulti64.tier.13", "UXV 64A Dynamo Hatch", 13, 64)
                .getStackForm(1L));
        eM_dynamoMulti256_UXV.set(
            new MTEHatchDynamoMulti(
                UXV256ADynamoHatch.ID,
                "hatch.dynamomulti256.tier.13",
                "UXV 256A Dynamo Hatch",
                13,
                256).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Laser OUTPUTS
        // ===================================================================================================

        eM_dynamoTunnel1_IV.set(
            new MTEHatchDynamoTunnel(
                IV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.05",
                "IV 256A/t Laser Source Hatch",
                5,
                256).getStackForm(1L));
        eM_dynamoTunnel1_LuV.set(
            new MTEHatchDynamoTunnel(
                LuV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.06",
                "LuV 256A/t Laser Source Hatch",
                6,
                256).getStackForm(1L));
        eM_dynamoTunnel2_LuV.set(
            new MTEHatchDynamoTunnel(
                LuV1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.06",
                "LuV 1,024A/t Laser Source Hatch",
                6,
                1024).getStackForm(1L));
        eM_dynamoTunnel1_ZPM.set(
            new MTEHatchDynamoTunnel(
                ZPM256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.07",
                "ZPM 256A/t Laser Source Hatch",
                7,
                256).getStackForm(1L));
        eM_dynamoTunnel2_ZPM.set(
            new MTEHatchDynamoTunnel(
                ZPM1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.07",
                "ZPM 1,024A/t Laser Source Hatch",
                7,
                1024).getStackForm(1L));
        eM_dynamoTunnel3_ZPM.set(
            new MTEHatchDynamoTunnel(
                ZPM4096AtLaserSourceHatch.ID,
                "hatch.dynamotunnel3.tier.07",
                "ZPM 4,096A/t Laser Source Hatch",
                7,
                4096).getStackForm(1L));
        eM_dynamoTunnel1_UV.set(
            new MTEHatchDynamoTunnel(
                UV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.08",
                "UV 256A/t Laser Source Hatch",
                8,
                256).getStackForm(1L));
        eM_dynamoTunnel2_UV.set(
            new MTEHatchDynamoTunnel(
                UV1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.08",
                "UV 1,024A/t Laser Source Hatch",
                8,
                1024).getStackForm(1L));
        eM_dynamoTunnel3_UV.set(
            new MTEHatchDynamoTunnel(
                UV4096AtLaserSourceHatch.ID,
                "hatch.dynamotunnel3.tier.08",
                "UV 4,096A/t Laser Source Hatch",
                8,
                4096).getStackForm(1L));
        eM_dynamoTunnel4_UV.set(
            new MTEHatchDynamoTunnel(
                UV16384AtLaserSourceHatch.ID,
                "hatch.dynamotunnel4.tier.08",
                "UV 16,384A/t Laser Source Hatch",
                8,
                16384).getStackForm(1L));
        eM_dynamoTunnel1_UHV.set(
            new MTEHatchDynamoTunnel(
                UHV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.09",
                "UHV 256A/t Laser Source Hatch",
                9,
                256).getStackForm(1L));
        eM_dynamoTunnel2_UHV.set(
            new MTEHatchDynamoTunnel(
                UHV1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.09",
                "UHV 1,024A/t Laser Source Hatch",
                9,
                1024).getStackForm(1L));
        eM_dynamoTunnel3_UHV.set(
            new MTEHatchDynamoTunnel(
                UHV4096AtLaserSourceHatch.ID,
                "hatch.dynamotunnel3.tier.09",
                "UHV 4,096A/t Laser Source Hatch",
                9,
                4096).getStackForm(1L));
        eM_dynamoTunnel4_UHV.set(
            new MTEHatchDynamoTunnel(
                UHV16384AtLaserSourceHatch.ID,
                "hatch.dynamotunnel4.tier.09",
                "UHV 16,384A/t Laser Source Hatch",
                9,
                16384).getStackForm(1L));
        eM_dynamoTunnel5_UHV.set(
            new MTEHatchDynamoTunnel(
                UHV65536AtLaserSourceHatch.ID,
                "hatch.dynamotunnel5.tier.09",
                "UHV 65,536A/t Laser Source Hatch",
                9,
                65536).getStackForm(1L));
        eM_dynamoTunnel1_UEV.set(
            new MTEHatchDynamoTunnel(
                UEV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.10",
                "UEV 256A/t Laser Source Hatch",
                10,
                256).getStackForm(1L));
        eM_dynamoTunnel2_UEV.set(
            new MTEHatchDynamoTunnel(
                UEV1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.10",
                "UEV 1,024A/t Laser Source Hatch",
                10,
                1024).getStackForm(1L));
        eM_dynamoTunnel3_UEV.set(
            new MTEHatchDynamoTunnel(
                UEV4096AtLaserSourceHatch.ID,
                "hatch.dynamotunnel3.tier.10",
                "UEV 4,096A/t Laser Source Hatch",
                10,
                4096).getStackForm(1L));
        eM_dynamoTunnel4_UEV.set(
            new MTEHatchDynamoTunnel(
                UEV16384AtLaserSourceHatch.ID,
                "hatch.dynamotunnel4.tier.10",
                "UEV 16,384A/t Laser Source Hatch",
                10,
                16384).getStackForm(1L));
        eM_dynamoTunnel5_UEV.set(
            new MTEHatchDynamoTunnel(
                UEV65536AtLaserSourceHatch.ID,
                "hatch.dynamotunnel5.tier.10",
                "UEV 65,536A/t Laser Source Hatch",
                10,
                65536).getStackForm(1L));
        eM_dynamoTunnel6_UEV.set(
            new MTEHatchDynamoTunnel(
                UEV262144AtLaserSourceHatch.ID,
                "hatch.dynamotunnel6.tier.10",
                "UEV 262,144A/t Laser Source Hatch",
                10,
                262144).getStackForm(1L));
        eM_dynamoTunnel1_UIV.set(
            new MTEHatchDynamoTunnel(
                UIV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.11",
                "UIV 256A/t Laser Source Hatch",
                11,
                256).getStackForm(1L));
        eM_dynamoTunnel2_UIV.set(
            new MTEHatchDynamoTunnel(
                UIV1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.11",
                "UIV 1,024A/t Laser Source Hatch",
                11,
                1024).getStackForm(1L));
        eM_dynamoTunnel3_UIV.set(
            new MTEHatchDynamoTunnel(
                UIV4096AtLaserSourceHatch.ID,
                "hatch.dynamotunnel3.tier.11",
                "UIV 4,096A/t Laser Source Hatch",
                11,
                4096).getStackForm(1L));
        eM_dynamoTunnel4_UIV.set(
            new MTEHatchDynamoTunnel(
                UIV16384AtLaserSourceHatch.ID,
                "hatch.dynamotunnel4.tier.11",
                "UIV 16,384A/t Laser Source Hatch",
                11,
                16384).getStackForm(1L));
        eM_dynamoTunnel5_UIV.set(
            new MTEHatchDynamoTunnel(
                UIV65536AtLaserSourceHatch.ID,
                "hatch.dynamotunnel5.tier.11",
                "UIV 65,536A/t Laser Source Hatch",
                11,
                65536).getStackForm(1L));
        eM_dynamoTunnel6_UIV.set(
            new MTEHatchDynamoTunnel(
                UIV262144AtLaserSourceHatch.ID,
                "hatch.dynamotunnel6.tier.11",
                "UIV 262,144A/t Laser Source Hatch",
                11,
                262144).getStackForm(1L));
        eM_dynamoTunnel7_UIV.set(
            new MTEHatchDynamoTunnel(
                UIV1048576AtLaserSourceHatch.ID,
                "hatch.dynamotunnel7.tier.11",
                "UIV 1,048,576A/t Laser Source Hatch",
                11,
                1048576).getStackForm(1L));

        eM_dynamoTunnel1_UMV.set(
            new MTEHatchDynamoTunnel(
                UMV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.12",
                "UMV 256A/t Laser Source Hatch",
                12,
                256).getStackForm(1L));
        eM_dynamoTunnel2_UMV.set(
            new MTEHatchDynamoTunnel(
                UMV1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.12",
                "UMV 1,024A/t Laser Source Hatch",
                12,
                1024).getStackForm(1L));
        eM_dynamoTunnel3_UMV.set(
            new MTEHatchDynamoTunnel(
                UMV4096AtLaserSourceHatch.ID,
                "hatch.dynamotunnel3.tier.12",
                "UMV 4,096A/t Laser Source Hatch",
                12,
                4096).getStackForm(1L));
        eM_dynamoTunnel4_UMV.set(
            new MTEHatchDynamoTunnel(
                UMV16384AtLaserSourceHatch.ID,
                "hatch.dynamotunnel4.tier.12",
                "UMV 16,384A/t Laser Source Hatch",
                12,
                16384).getStackForm(1L));
        eM_dynamoTunnel5_UMV.set(
            new MTEHatchDynamoTunnel(
                UMV65536AtLaserSourceHatch.ID,
                "hatch.dynamotunnel5.tier.12",
                "UMV 65,536A/t Laser Source Hatch",
                12,
                65536).getStackForm(1L));
        eM_dynamoTunnel6_UMV.set(
            new MTEHatchDynamoTunnel(
                UMV262144AtLaserSourceHatch.ID,
                "hatch.dynamotunnel6.tier.12",
                "UMV 262,144A/t Laser Source Hatch",
                12,
                262144).getStackForm(1L));
        eM_dynamoTunnel7_UMV.set(
            new MTEHatchDynamoTunnel(
                UMV1048576AtLaserSourceHatch.ID,
                "hatch.dynamotunnel7.tier.12",
                "UMV 1,048,576A/t Laser Source Hatch",
                12,
                1048576).getStackForm(1L));
        eM_dynamoTunnel8_UMV.set(
            new MTEHatchDynamoTunnel(
                MetaTileEntityIDs.LASER_SOURCE_UMV8.ID,
                "hatch.dynamotunnel8.tier.12",
                "UMV 4,194,304A/t Laser Source Hatch",
                12,
                4194304).getStackForm(1L));

        eM_dynamoTunnel1_UXV.set(
            new MTEHatchDynamoTunnel(
                UXV256AtLaserSourceHatch.ID,
                "hatch.dynamotunnel1.tier.13",
                "UXV 256A/t Laser Source Hatch",
                13,
                256).getStackForm(1L));
        eM_dynamoTunnel2_UXV.set(
            new MTEHatchDynamoTunnel(
                UXV1024AtLaserSourceHatch.ID,
                "hatch.dynamotunnel2.tier.13",
                "UXV 1,024A/t Laser Source Hatch",
                13,
                1024).getStackForm(1L));
        eM_dynamoTunnel3_UXV.set(
            new MTEHatchDynamoTunnel(
                UXV4096AtLaserSourceHatch.ID,
                "hatch.dynamotunnel3.tier.13",
                "UXV 4,096A/t Laser Source Hatch",
                13,
                4096).getStackForm(1L));
        eM_dynamoTunnel4_UXV.set(
            new MTEHatchDynamoTunnel(
                UXV16384AtLaserSourceHatch.ID,
                "hatch.dynamotunnel4.tier.13",
                "UXV 16,384A/t Laser Source Hatch",
                13,
                16384).getStackForm(1L));
        eM_dynamoTunnel5_UXV.set(
            new MTEHatchDynamoTunnel(
                UXV65536AtLaserSourceHatch.ID,
                "hatch.dynamotunnel5.tier.13",
                "UXV 65,536A/t Laser Source Hatch",
                13,
                65536).getStackForm(1L));
        eM_dynamoTunnel6_UXV.set(
            new MTEHatchDynamoTunnel(
                UXV262144AtLaserSourceHatch.ID,
                "hatch.dynamotunnel6.tier.13",
                "UXV 262,144A/t Laser Source Hatch",
                13,
                262144).getStackForm(1L));
        eM_dynamoTunnel7_UXV.set(
            new MTEHatchDynamoTunnel(
                UXV1048576AtLaserSourceHatch.ID,
                "hatch.dynamotunnel7.tier.13",
                "UXV 1,048,576A/t Laser Source Hatch",
                13,
                1048576).getStackForm(1L));
        eM_dynamoTunnel8_UXV.set(
            new MTEHatchDynamoTunnel(
                MetaTileEntityIDs.LASER_SOURCE_UXV8.ID,
                "hatch.dynamotunnel8.tier.13",
                "UXV 4,194,304A/t Laser Source Hatch",
                13,
                4194304).getStackForm(1L));
        eM_dynamoTunnel9_UXV.set(
            new MTEHatchDynamoTunnel(
                MetaTileEntityIDs.LASER_SOURCE_UXV9.ID,
                "hatch.dynamotunnel9.tier.13",
                "UXV 16,777,216A/t Laser Source Hatch",
                13,
                16777216).getStackForm(1L));
        eM_dynamoTunnel9001.set(
            new MTEHatchDynamoTunnel(
                LegendaryLaserSourceHatch.ID,
                "hatch.dynamotunnel.tier.14",
                "Legendary Laser Source Hatch",
                13,
                (int) V[13]).getStackForm(1L));

        // ===================================================================================================
        // MULTIBLOCKS
        // ===================================================================================================

        Machine_Multi_Transformer.set(
            new MTEActiveTransformer(ActiveTransformer.ID, "multimachine.em.transformer", "Active Transformer")
                .getStackForm(1L));
        Machine_Multi_Microwave.set(
            new MTEMicrowave(MicrowaveGrinder.ID, "multimachine.tm.microwave", "Microwave Grinder").getStackForm(1L));
        Machine_Multi_TeslaCoil
            .set(new MTETeslaTower(TeslaTower.ID, "multimachine.tm.teslaCoil", "Tesla Tower").getStackForm(1L));
        Machine_Multi_Switch.set(
            new MTENetworkSwitch(NetworkSwitchWithQoS.ID, "multimachine.em.switch", "Weighted Network Switch With QoS")
                .getStackForm(1L));
        Machine_Multi_Switch_Adv.set(
            new MTENetworkSwitchAdv(NetworkSwitchAdv.ID, "multimachine.em.switch.adv", "Static Network Switch With QoS")
                .getStackForm(1L));
        Machine_Multi_Computer.set(
            new MTEQuantumComputer(QuantumComputer.ID, "multimachine.em.computer", "Quantum Computer")
                .getStackForm(1L));
        Machine_Multi_DataBank
            .set(new MTEDataBank(DataBank.ID, "multimachine.em.databank", "Data Bank").getStackForm(1L));
        Machine_Multi_Research.set(
            new MTEResearchStation(Researchstation.ID, "multimachine.em.research", "Research Station")
                .getStackForm(1L));
        Machine_Multi_Infuser
            .set(new MTEEnergyInfuser(EnergyInfuser.ID, "multimachine.em.infuser", "Energy Infuser").getStackForm(1));
        Machine_Multi_EyeOfHarmony.set(
            new MTEEyeOfHarmony(EyeofHarmony.ID, "multimachine.em.eye_of_harmony", "Eye of Harmony").getStackForm(1L));

        Machine_Multi_ForgeOfGods.set(
            new MTEForgeOfGods(ForgeoftheGods.ID, "multimachine.em.forge_of_gods", "Forge of the Gods")
                .getStackForm(1L));
        addItemTooltip(Machine_Multi_ForgeOfGods.get(1), GTValues.AuthorCloud);
        Machine_Multi_SmeltingModule.set(
            new MTESmeltingModule(HelioflarePowerForge.ID, "multimachine.em.smelting_module", "Helioflare Power Forge")
                .getStackForm(1L));
        addItemTooltip(Machine_Multi_SmeltingModule.get(1), GTValues.AuthorCloud);
        Machine_Multi_MoltenModule.set(
            new MTEMoltenModule(HeliofluxMeltingCore.ID, "multimachine.em.molten_module", "Helioflux Melting Core")
                .getStackForm(1L));
        addItemTooltip(Machine_Multi_MoltenModule.get(1), GTValues.AuthorCloud);
        Machine_Multi_PlasmaModule.set(
            new MTEPlasmaModule(
                HeliothermalPlasmaFabricator.ID,
                "multimachine.em.plasma_module",
                "Heliothermal Plasma Fabricator").getStackForm(1L));
        addItemTooltip(Machine_Multi_PlasmaModule.get(1), GTValues.AuthorCloud);
        Machine_Multi_QuarkGluonPlasmaModule.set(
            new MTEExoticModule(HeliofusionExoticizer.ID, "multimachine.em.exotic_module", "Heliofusion Exoticizer")
                .getStackForm(1L));
        addItemTooltip(Machine_Multi_QuarkGluonPlasmaModule.get(1), GTValues.AuthorCloud);

        // ===================================================================================================
        // Hatches
        // ===================================================================================================
        Uncertainty_Hatch.set(
            new MTEHatchUncertainty(UncertaintyResolver.ID, "hatch.certain.tier.05", "Uncertainty Resolver", 5)
                .getStackForm(1L));
        UncertaintyX_Hatch.set(
            new MTEHatchUncertainty(UncertaintyResolverX.ID, "hatch.certain.tier.07", "Uncertainty Resolver X", 7)
                .getStackForm(1L));
        dataIn_Hatch.set(
            new MTEHatchDataInput(OpticalSlaveConnector.ID, "hatch.datain.tier.07", "Optical Reception Connector", 7)
                .getStackForm(1L));
        dataOut_Hatch.set(
            new MTEHatchDataOutput(
                OpticalMasterConnector.ID,
                "hatch.dataout.tier.07",
                "Optical Transmission Connector",
                7).getStackForm(1L));
        dataInAss_Hatch.set(
            new MTEHatchDataItemsInput(
                AssemblylineSlaveConnector.ID,
                "hatch.datainass.tier.07",
                "Assembly line Reception Connector",
                7).getStackForm(1L));
        dataOutAss_Hatch.set(
            new MTEHatchDataItemsOutput(
                DataBankMasterConnector.ID,
                "hatch.dataoutass.tier.07",
                "Data Bank Transmission Connector",
                7).getStackForm(1L));
        dataOut_Wireless_Hatch.set(
            new MTEHatchWirelessComputationOutput(
                CloudComputationServerHatch.ID,
                "hatch.dataout.wireless.tier.12",
                "Cloud Computation Server Hatch",
                12).getStackForm(1L));
        dataIn_Wireless_Hatch.set(
            new MTEHatchWirelessComputationInput(
                CloudComputationClientHatch.ID,
                "hatch.datain.wireless.tier.12",
                "Cloud Computation Client Hatch",
                12).getStackForm(1L));
        dataInAss_Wireless_Hatch.set(
            new MTEHatchWirelessDataItemsInput(
                WirelessAssemblylineSlaveConnector.ID,
                "hatch.datainass.wireless.tier.12",
                "Wireless Assembly line Reception Connector",
                10).getStackForm(1L));
        dataOutAss_Wireless_Hatch.set(
            new MTEHatchWirelessDataItemsOutput(
                WirelessDataBankMasterConnector.ID,
                "hatch.dataoutass.wireless.tier.12",
                "Wireless Data Bank Transmission Connector",
                10).getStackForm(1L));
        rack_Hatch.set(new MTEHatchRack(ComputerRack.ID, "hatch.rack.tier.08", "Computer Rack", 8).getStackForm(1L));
        holder_Hatch.set(
            new MTEHatchObjectHolder(ObjectHolder.ID, "hatch.holder.tier.09", "Object Holder", 8).getStackForm(1L));
        capacitor_Hatch.set(
            new MTEHatchCapacitor(CapacitorHatch.ID, "hatch.capacitor.tier.03", "Capacitor Hatch", 3).getStackForm(1L));

        // ===================================================================================================
        // Pipes
        // ===================================================================================================

        LASERpipe.set(new MTEPipeLaser(LaserVacuumPipe.ID, "pipe.energystream", "Laser Vacuum Pipe").getStackForm(1L));
        LASERpipeSmart.set(
            new MTEPipeLaserMirror(LaserVacuumMirror.ID, "pipe.energymirror", "Laser Vacuum Mirror").getStackForm(1L));
        DATApipe.set(new MTEPipeData(OpticalFiberCable.ID, "pipe.datastream", "Optical Fiber Cable").getStackForm(1L));

        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            TestPipe.set(new TestFactoryPipe(TestFactoryPipe.ID, "pipe.test", "Test Factory Pipe").getStackForm(1));
            TestHatch
                .set(new TestFactoryHatch(TestFactoryHatch.ID, "hatch.test", "Test Factory Hatch", 7).getStackForm(1));
        }

        LASERpipeBlock.set(
            new MTEPipeBlockLaser(LaserVacuumPipeCasing.ID, "pipe.energystream.block", "Laser Vacuum Pipe Casing")
                .getStackForm(1L));
        DATApipeBlock.set(
            new MTEPipeBlockData(OpticalFiberCableCasing.ID, "pipe.datastream.block", "Optical Fiber Cable Casing")
                .getStackForm(1L));

        // ===================================================================================================
        // Single Blocks
        // ===================================================================================================

        Machine_OwnerDetector.set(
            new MTEOwnerDetector(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Ownerdetector.ID)
                    .translateKey("machine.tt.ownerdetector")
                    .nameEnglish("Owner detector")
                    .tier(3)
                    .build()).getStackForm(1L));

        // ===================================================================================================
        // Buck Converters
        // ===================================================================================================

        Machine_BuckConverter_IV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(InsaneBuckConverter.ID)
                    .translateKey("machine.tt.buck.05")
                    .nameEnglish("Insane Buck Converter")
                    .tier(5)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_LuV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(LudicrousBuckConverter.ID)
                    .translateKey("machine.tt.buck.06")
                    .nameEnglish("Ludicrous Buck Converter")
                    .tier(6)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_ZPM.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(ZPMVoltageBuckConverter.ID)
                    .translateKey("machine.tt.buck.07")
                    .nameEnglish("ZPM Voltage Buck Converter")
                    .tier(7)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_UV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(UltimatePowerBuckConverter.ID)
                    .translateKey("machine.tt.buck.08")
                    .nameEnglish("Ultimate Power Buck Converter")
                    .tier(8)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_UHV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(HighlyUltimateBuckConverter.ID)
                    .translateKey("machine.tt.buck.09")
                    .nameEnglish("Highly Ultimate Buck Converter")
                    .tier(9)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_UEV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(ExtremelyUltimateBuckConverter.ID)
                    .translateKey("machine.tt.buck.10")
                    .nameEnglish("Extremely Ultimate Buck Converter")
                    .tier(10)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_UIV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(InsanelyUltimateBuckConverter.ID)
                    .translateKey("machine.tt.buck.11")
                    .nameEnglish("Insanely Ultimate Buck Converter")
                    .tier(11)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_UMV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(MegaUltimateBuckConverter.ID)
                    .translateKey("machine.tt.buck.12")
                    .nameEnglish("Mega Ultimate Buck Converter")
                    .tier(12)
                    .build()).getStackForm(1L));
        Machine_BuckConverter_UXV.set(
            new MTEBuckConverter(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(ExtendedMegaUltimateBuckConverter.ID)
                    .translateKey("machine.tt.buck.13")
                    .nameEnglish("Extended Mega Ultimate Buck Converter")
                    .tier(13)
                    .build()).getStackForm(1L));

        // ===================================================================================================
        // Tesla Transceiver
        // ===================================================================================================

        Machine_TeslaCoil_1by1_LV.set(
            new MTETeslaCoil(BasicTeslaTransceiver1by1.ID, "machine.tt.tesla.01", "Basic Tesla Transceiver", 1, 1)
                .getStackForm(1L));
        Machine_TeslaCoil_1by1_MV.set(
            new MTETeslaCoil(AdvancedTeslaTransceiver1by1.ID, "machine.tt.tesla.02", "Advanced Tesla Transceiver", 2, 1)
                .getStackForm(1L));
        Machine_TeslaCoil_1by1_HV.set(
            new MTETeslaCoil(EpycTeslaTransceiver1by1.ID, "machine.tt.tesla.03", "Epyc Tesla Transceiver", 3, 1)
                .getStackForm(1L));
        Machine_TeslaCoil_1by1_EV.set(
            new MTETeslaCoil(
                UltimatePowerTeslaTransceiver1by1.ID,
                "machine.tt.tesla.04",
                "Ultimate Power Tesla Transceiver",
                4,
                1).getStackForm(1L));
        Machine_TeslaCoil_1by1_IV.set(
            new MTETeslaCoil(InsaneTeslaTransceiver1by1.ID, "machine.tt.tesla.05", "Insane Tesla Transceiver", 5, 1)
                .getStackForm(1L));

        Machine_TeslaCoil_2by2_LV.set(
            new MTETeslaCoil(BasicTeslaTransceiver2by2.ID, "machine.tt.tesla.01", "Basic Tesla Transceiver", 1, 4)
                .getStackForm(1L));
        Machine_TeslaCoil_2by2_MV.set(
            new MTETeslaCoil(AdvancedTeslaTransceiver2by2.ID, "machine.tt.tesla.02", "Advanced Tesla Transceiver", 2, 4)
                .getStackForm(1L));
        Machine_TeslaCoil_2by2_HV.set(
            new MTETeslaCoil(EpycTeslaTransceiver2by2.ID, "machine.tt.tesla.03", "Epyc Tesla Transceiver", 3, 4)
                .getStackForm(1L));
        Machine_TeslaCoil_2by2_EV.set(
            new MTETeslaCoil(
                UltimatePowerTeslaTransceiver2by2.ID,
                "machine.tt.tesla.04",
                "Ultimate Power Tesla Transceiver",
                4,
                4).getStackForm(1L));
        Machine_TeslaCoil_2by2_IV.set(
            new MTETeslaCoil(InsaneTeslaTransceiver2by2.ID, "machine.tt.tesla.05", "Insane Tesla Transceiver", 5, 4)
                .getStackForm(1L));

        Machine_TeslaCoil_3by3_LV.set(
            new MTETeslaCoil(BasicTeslaTransceiver3by3.ID, "machine.tt.tesla.01", "Basic Tesla Transceiver", 1, 9)
                .getStackForm(1L));
        Machine_TeslaCoil_3by3_MV.set(
            new MTETeslaCoil(AdvancedTeslaTransceiver3by3.ID, "machine.tt.tesla.02", "Advanced Tesla Transceiver", 2, 9)
                .getStackForm(1L));
        Machine_TeslaCoil_3by3_HV.set(
            new MTETeslaCoil(EpycTeslaTransceiver3by3.ID, "machine.tt.tesla.03", "Epyc Tesla Transceiver", 3, 9)
                .getStackForm(1L));
        Machine_TeslaCoil_3by3_EV.set(
            new MTETeslaCoil(
                UltimatePowerTeslaTransceiver3by3.ID,
                "machine.tt.tesla.04",
                "Ultimate Power Tesla Transceiver",
                4,
                9).getStackForm(1L));
        Machine_TeslaCoil_3by3_IV.set(
            new MTETeslaCoil(InsaneTeslaTransceiver3by3.ID, "machine.tt.tesla.05", "Insane Tesla Transceiver", 5, 9)
                .getStackForm(1L));

        Machine_TeslaCoil_4by4_LV.set(
            new MTETeslaCoil(BasicTeslaTransceiver4by4.ID, "machine.tt.tesla.01", "Basic Tesla Transceiver", 1, 16)
                .getStackForm(1L));
        Machine_TeslaCoil_4by4_MV.set(
            new MTETeslaCoil(
                AdvancedTeslaTransceiver4by4.ID,
                "machine.tt.tesla.02",
                "Advanced Tesla Transceiver",
                2,
                16).getStackForm(1L));
        Machine_TeslaCoil_4by4_HV.set(
            new MTETeslaCoil(EpycTeslaTransceiver4by4.ID, "machine.tt.tesla.03", "Epyc Tesla Transceiver", 3, 16)
                .getStackForm(1L));
        Machine_TeslaCoil_4by4_EV.set(
            new MTETeslaCoil(
                UltimatePowerTeslaTransceiver4by4.ID,
                "machine.tt.tesla.04",
                "Ultimate Power Tesla Transceiver",
                4,
                16).getStackForm(1L));
        Machine_TeslaCoil_4by4_IV.set(
            new MTETeslaCoil(InsaneTeslaTransceiver4by4.ID, "machine.tt.tesla.05", "Insane Tesla Transceiver", 5, 16)
                .getStackForm(1L));

        // ===================================================================================================
        // Debug Stuff
        // ===================================================================================================
        Machine_DebugPollutor.set(
            new MTEDebugPollutor(DebugPollutionGenerator.ID, "debug.tt.pollutor", "Debug Pollution Generator", 14)
                .getStackForm(1));
        hatch_CreativeData
            .set(new MTEHatchCreativeData(DebugDataHatch.ID, "debug.tt.data", "Debug Data Hatch", 14).getStackForm(1));
        hatch_CreativeMaintenance.set(
            new MTEHatchCreativeMaintenance(
                AutoTapingMaintenanceHatch.ID,
                "debug.tt.maintenance",
                "Auto-Taping Maintenance Hatch",
                8).getStackForm(1L));
        Machine_DebugGenny.set(
            new MTEDebugPowerGenerator(DebugPowerGenerator.ID, "debug.tt.genny", "Debug Power Generator", 14)
                .getStackForm(1L));
        Machine_DebugWriter.set(
            new MTEDebugStructureWriter(DebugStructureWriter.ID, "debug.tt.writer", "Debug Structure Writer", 14)
                .getStackForm(1L));
        UnusedStuff.set(new ItemStack(Blocks.air));
        hatch_CreativeUncertainty.set(
            new MTEHatchCreativeUncertainty(UncertaintyResolution.ID, "debug.tt.certain", "Uncertainty Resolution", 11)
                .getStackForm(1));

        // ===================================================================================================
        // MetaTE init
        // ===================================================================================================

        MTEHatchRack.run();

        MTEHatchCapacitor.run();
    }
}
