package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.multitileentity.enums.GT_MultiTileCasing.*;
import static gregtech.api.multitileentity.enums.GT_MultiTileComponentCasing.*;
import static gregtech.api.multitileentity.enums.GT_MultiTileUpgradeCasing.*;
import static gregtech.api.multitileentity.enums.GT_MultiTileUpgradeCasing.Insulator_OmegaType;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.multiblock.base.WallShareablePart;
import gregtech.api.multitileentity.multiblock.casing.BasicCasing;
import gregtech.common.tileentities.casings.functional.Conveyor;
import gregtech.common.tileentities.casings.functional.Emitter;
import gregtech.common.tileentities.casings.functional.FieldGenerator;
import gregtech.common.tileentities.casings.functional.Motor;
import gregtech.common.tileentities.casings.functional.Piston;
import gregtech.common.tileentities.casings.functional.Pump;
import gregtech.common.tileentities.casings.functional.RobotArm;
import gregtech.common.tileentities.casings.functional.Sensor;
import gregtech.common.tileentities.casings.upgrade.Ampere;
import gregtech.common.tileentities.casings.upgrade.Cleanroom;
import gregtech.common.tileentities.casings.upgrade.Heater;
import gregtech.common.tileentities.casings.upgrade.Insulator;
import gregtech.common.tileentities.casings.upgrade.Inventory;
import gregtech.common.tileentities.casings.upgrade.Laser;
import gregtech.common.tileentities.casings.upgrade.Tank;
import gregtech.common.tileentities.casings.upgrade.Wireless;
import gregtech.common.tileentities.machines.multiblock.AdvChemicalProcessor;
import gregtech.common.tileentities.machines.multiblock.CokeOven;
import gregtech.common.tileentities.machines.multiblock.DistillationTower;
import gregtech.common.tileentities.machines.multiblock.LaserEngraver;
import gregtech.common.tileentities.machines.multiblock.LayeredCokeBattery;
import gregtech.common.tileentities.machines.multiblock.Macerator;

public class GT_Loader_MultiTileEntities implements Runnable {

    public static final String COMPONENT_CASING_REGISTRY_NAME = "gt.multitileentity.component.casings";
    public static final String UPGRADE_CASING_REGISTRY_NAME = "gt.multitileentity.upgrade.casings";
    public static final String CASING_REGISTRY_NAME = "gt.multitileentity.casings";
    public static final String MACHINE_REGISTRY_NAME = "gt.multitileentity.controllers";
    public static final MultiTileEntityRegistry MACHINE_REGISTRY = new MultiTileEntityRegistry(MACHINE_REGISTRY_NAME);
    public static final MultiTileEntityRegistry CASING_REGISTRY = new MultiTileEntityRegistry(CASING_REGISTRY_NAME);
    public static final MultiTileEntityRegistry COMPONENT_CASING_REGISTRY = new MultiTileEntityRegistry(
        COMPONENT_CASING_REGISTRY_NAME);

    public static final MultiTileEntityRegistry UPGRADE_CASING_REGISTRY = new MultiTileEntityRegistry(
        UPGRADE_CASING_REGISTRY_NAME);

    @Override
    public void run() {
        GT_FML_LOGGER.info("GT_Mod: Registering MultiTileEntities");
        registerMachines();
        registerCasings();
        registerComponentCasings();
    }

    private static void registerMachines() {
        // Disable for now
        MACHINE_REGISTRY.create(1000, Macerator.class)

            .category("Multiblock Controller")
            .material(Materials.Iron)
            .textureFolder("macerator")
            .tankCapacity(128000L)
            .inputInventorySize(16)
            .outputInventorySize(16)
            .register();
        MACHINE_REGISTRY.create(0, CokeOven.class)

            .category("MultiblockController")
            .textureFolder("cokeOven")
            .inputInventorySize(1)
            .outputInventorySize(1)
            .register();
        MACHINE_REGISTRY.create(1, AdvChemicalProcessor.class)

            .category("MultiblockController")
            // TODO: Texture
            .textureFolder("advChemicalProcessor")
            .inputInventorySize(16)
            .outputInventorySize(16)
            .tankCapacity(128000L)
            .register();
        MACHINE_REGISTRY.create(2, DistillationTower.class)

            .category("MultiblockController")
            .textureFolder("distillationTower")
            .inputInventorySize(16)
            .outputInventorySize(16)
            .tankCapacity(128000L)
            .register();
        MACHINE_REGISTRY.create(3, LayeredCokeBattery.class)

            .category("Multiblock Controller")
            .material(Materials.Iron)
            .textureFolder("macerator")
            .tankCapacity(128000L)
            .inputInventorySize(16)
            .outputInventorySize(16)
            .register();
        MACHINE_REGISTRY.create(4, LaserEngraver.class)

            .category("Multiblock Controller")
            .textureFolder("BigLaserEngraver")
            .inputInventorySize(16)
            .outputInventorySize(16)
            .tankCapacity(128000L)
            .register();
    }

    private static void registerCasings() {

        CASING_REGISTRY.create(CokeOven.getId(), WallShareablePart.class)

            .category("MultiBlock Casing")
            .textureFolder("cokeOven")
            .register();
        CASING_REGISTRY.create(Chemical.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("advChemicalProcessor")
            .register();
        CASING_REGISTRY.create(Distillation.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("distillationTower")
            .register();
        CASING_REGISTRY.create(18000, BasicCasing.class)

            .category("Multiblock Casing")
            .material(Materials.Cobalt)
            .textureFolder("macerator")
            .register();
        CASING_REGISTRY.create(LaserEngraver.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("BigLaserEngraver")
            .register();
        CASING_REGISTRY.create(Mirror.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("Laserblock")
            .register();
        CASING_REGISTRY.create(BlackLaserEngraverCasing.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("BlackLaserEngraver")
            .register();
        CASING_REGISTRY.create(LaserEngraverUpgrade1.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("laserengraverupgrade1")
            .register();
        CASING_REGISTRY.create(LaserEngraverUpgrade2.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("laserengraverupgrade2")
            .register();
        CASING_REGISTRY.create(LaserEngraverUpgrade3.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("laserengraverupgrade3")
            .register();
        CASING_REGISTRY.create(LaserEngraverUpgrade4.getId(), BasicCasing.class)

            .category("MultiBlock Casing")
            .textureFolder("laserengraverupgrade4")
            .register();

    }

    private static void registerComponentCasings() {

        registerMotorCasings();
        registerPumpCasings();
        registerPistonCasings();
        registerRobotArmCasings();
        registerConveyorCasings();
        registerEmitterCasings();
        registerSensorCasings();
        registerFieldGeneratorCasings();

        UPGRADE_CASING_REGISTRY.create(ULV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")
            .textureFolder("inventory")
            .upgradeInventorySize(1)
            .tier(0)
            .register();
        UPGRADE_CASING_REGISTRY.create(LV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")
            .textureFolder("inventory")
            .upgradeInventorySize(4)
            .tier(1)
            .register();
        UPGRADE_CASING_REGISTRY.create(MV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")
            .textureFolder("inventory")
            .upgradeInventorySize(8)
            .tier(2)
            .register();
        UPGRADE_CASING_REGISTRY.create(HV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")
            .textureFolder("inventory")
            .upgradeInventorySize(16)
            .tier(3)
            .register();
        UPGRADE_CASING_REGISTRY.create(EV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(32)
            .tier(4)
            .register();
        UPGRADE_CASING_REGISTRY.create(IV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(64)
            .tier(5)
            .register();
        UPGRADE_CASING_REGISTRY.create(LuV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(128)
            .tier(6)
            .register();
        UPGRADE_CASING_REGISTRY.create(ZPM_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(7)
            .register();
        UPGRADE_CASING_REGISTRY.create(UV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(8)
            .register();
        UPGRADE_CASING_REGISTRY.create(UHV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(9)
            .register();
        UPGRADE_CASING_REGISTRY.create(UEV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(10)
            .register();
        UPGRADE_CASING_REGISTRY.create(UIV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(11)
            .register();
        UPGRADE_CASING_REGISTRY.create(UMV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(12)
            .register();
        UPGRADE_CASING_REGISTRY.create(UXV_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(13)
            .register();
        UPGRADE_CASING_REGISTRY.create(MAX_Inventory.getId(), Inventory.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("inventory")
            .upgradeInventorySize(256)
            .tier(14)
            .register();

        UPGRADE_CASING_REGISTRY.create(ULV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(8_000L)
            .upgradeTankCount(1)
            .tier(0)
            .register();
        UPGRADE_CASING_REGISTRY.create(LV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(16_000L)
            .upgradeTankCount(2)
            .tier(1)
            .register();
        UPGRADE_CASING_REGISTRY.create(MV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(32_000L)
            .upgradeTankCount(4)
            .tier(2)
            .register();
        UPGRADE_CASING_REGISTRY.create(HV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(64_000L)
            .upgradeTankCount(6)
            .tier(3)
            .register();
        UPGRADE_CASING_REGISTRY.create(EV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(128_000L)
            .upgradeTankCount(8)
            .tier(4)
            .register();
        UPGRADE_CASING_REGISTRY.create(IV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(256_000L)
            .upgradeTankCount(10)
            .tier(5)
            .register();
        UPGRADE_CASING_REGISTRY.create(LuV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(512_000L)
            .upgradeTankCount(12)
            .tier(6)
            .register();
        UPGRADE_CASING_REGISTRY.create(ZPM_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(1_024_000L)
            .upgradeTankCount(14)
            .tier(7)
            .register();
        UPGRADE_CASING_REGISTRY.create(UV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(2_048_000L)
            .upgradeTankCount(16)
            .tier(8)
            .register();
        UPGRADE_CASING_REGISTRY.create(UHV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(4_096_000L)
            .upgradeTankCount(16)
            .tier(9)
            .register();
        UPGRADE_CASING_REGISTRY.create(UEV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(8_192_000L)
            .upgradeTankCount(16)
            .tier(10)
            .register();
        UPGRADE_CASING_REGISTRY.create(UIV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(16_384_000L)
            .upgradeTankCount(16)
            .tier(11)
            .register();
        UPGRADE_CASING_REGISTRY.create(UMV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(32_768_000L)
            .upgradeTankCount(16)
            .tier(12)
            .register();
        UPGRADE_CASING_REGISTRY.create(UXV_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(65_536_000L)
            .upgradeTankCount(16)
            .tier(13)
            .register();
        UPGRADE_CASING_REGISTRY.create(MAX_Tank.getId(), Tank.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("tank")
            .upgradeTankCapacity(131_072_000L)
            .upgradeTankCount(16)
            .tier(14)
            .register();

        UPGRADE_CASING_REGISTRY.create(Amp_4.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(4)
            .tier(1)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_16.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(16)
            .tier(2)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_64.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(64)
            .tier(3)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_256.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(256)
            .tier(4)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_1_024.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(1_024)
            .tier(5)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_4_096.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(4_096)
            .tier(6)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_16_384.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(16_384)
            .tier(7)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_65_536.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(65_536)
            .tier(8)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_262_144.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(262_144)
            .tier(9)
            .register();
        UPGRADE_CASING_REGISTRY.create(Amp_1_048_576.getId(), Ampere.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("amperage")
            .upgradeAmperage(1_048_576)
            .tier(10)
            .register();

        UPGRADE_CASING_REGISTRY.create(Cleanroom.getId(), Cleanroom.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("cleanroom")
            .tier(1)
            .register();
        UPGRADE_CASING_REGISTRY.create(Laser.getId(), Laser.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("laser")
            .tier(8)
            .register();
        UPGRADE_CASING_REGISTRY.create(Wireless.getId(), Wireless.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("wireless")
            .tier(11)
            .register();

        UPGRADE_CASING_REGISTRY.create(Heater_Prototype.getId(), Heater.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("heater")
            .tier(1)
            .register();
        UPGRADE_CASING_REGISTRY.create(Heater_IndustrialGrade.getId(), Heater.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("heater")
            .tier(4)
            .register();
        UPGRADE_CASING_REGISTRY.create(Heater_NextGen.getId(), Heater.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("heater")
            .tier(7)
            .register();
        UPGRADE_CASING_REGISTRY.create(Heater_Omnipotent.getId(), Heater.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("heater")
            .tier(10)
            .register();
        UPGRADE_CASING_REGISTRY.create(Heater_OmegaType.getId(), Heater.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("heater")
            .tier(12)
            .register();

        UPGRADE_CASING_REGISTRY.create(Insulator_Prototype.getId(), Insulator.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("insulator")
            .tier(1)
            .register();
        UPGRADE_CASING_REGISTRY.create(Insulator_IndustrialGrade.getId(), Insulator.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("insulator")
            .tier(4)
            .register();
        UPGRADE_CASING_REGISTRY.create(Insulator_NextGen.getId(), Insulator.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("insulator")
            .tier(7)
            .register();
        UPGRADE_CASING_REGISTRY.create(Insulator_Omnipotent.getId(), Insulator.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("insulator")
            .tier(10)
            .register();
        UPGRADE_CASING_REGISTRY.create(Insulator_OmegaType.getId(), Insulator.class)

            .category("MultiBlock Upgrade Casing")

            .textureFolder("insulator")
            .tier(12)
            .register();
    }

    private static void registerMotorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Motor.getId(), Motor.class)

            .tier(1)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Motor.getId(), Motor.class)

            .tier(2)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Motor.getId(), Motor.class)

            .tier(3)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Motor.getId(), Motor.class)

            .tier(4)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Motor.getId(), Motor.class)

            .tier(5)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Motor.getId(), Motor.class)

            .tier(6)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Motor.getId(), Motor.class)

            .tier(7)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Motor.getId(), Motor.class)

            .tier(8)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Motor.getId(), Motor.class)

            .tier(9)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Motor.getId(), Motor.class)

            .tier(10)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Motor.getId(), Motor.class)

            .tier(11)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Motor.getId(), Motor.class)

            .tier(12)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Motor.getId(), Motor.class)

            .tier(13)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Motor.getId(), Motor.class)

            .tier(14)
            .category("MultiBlock Functional Casing")

            .textureFolder("motor")
            .register();
    }

    private static void registerPumpCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Pump.getId(), Pump.class)

            .tier(1)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Pump.getId(), Pump.class)

            .tier(2)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Pump.getId(), Pump.class)

            .tier(3)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Pump.getId(), Pump.class)

            .tier(4)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Pump.getId(), Pump.class)

            .tier(5)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Pump.getId(), Pump.class)

            .tier(6)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Pump.getId(), Pump.class)

            .tier(6)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Pump.getId(), Pump.class)

            .tier(7)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Pump.getId(), Pump.class)

            .tier(8)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Pump.getId(), Pump.class)

            .tier(9)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Pump.getId(), Pump.class)

            .tier(10)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Pump.getId(), Pump.class)

            .tier(11)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Pump.getId(), Pump.class)

            .tier(12)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Pump.getId(), Pump.class)

            .tier(13)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Pump.getId(), Pump.class)

            .tier(14)
            .category("MultiBlock Functional Casing")

            .textureFolder("pump")
            .register();
    }

    private static void registerPistonCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Piston.getId(), Piston.class)

            .tier(1)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Piston.getId(), Piston.class)

            .tier(2)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Piston.getId(), Piston.class)

            .tier(3)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Piston.getId(), Piston.class)

            .tier(4)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Piston.getId(), Piston.class)

            .tier(5)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Piston.getId(), Piston.class)

            .tier(6)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Piston.getId(), Piston.class)

            .tier(7)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Piston.getId(), Piston.class)

            .tier(8)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Piston.getId(), Piston.class)

            .tier(9)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Piston.getId(), Piston.class)

            .tier(10)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Piston.getId(), Piston.class)

            .tier(11)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Piston.getId(), Piston.class)

            .tier(12)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Piston.getId(), Piston.class)

            .tier(13)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Piston.getId(), Piston.class)

            .tier(14)
            .category("MultiBlock Functional Casing")

            .textureFolder("piston")
            .register();
    }

    private static void registerConveyorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Conveyor.getId(), Conveyor.class)

            .tier(1)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Conveyor.getId(), Conveyor.class)

            .tier(2)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Conveyor.getId(), Conveyor.class)

            .tier(3)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Conveyor.getId(), Conveyor.class)

            .tier(4)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Conveyor.getId(), Conveyor.class)

            .tier(5)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Conveyor.getId(), Conveyor.class)

            .tier(6)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Conveyor.getId(), Conveyor.class)

            .tier(7)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Conveyor.getId(), Conveyor.class)

            .tier(8)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Conveyor.getId(), Conveyor.class)

            .tier(9)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Conveyor.getId(), Conveyor.class)

            .tier(10)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Conveyor.getId(), Conveyor.class)

            .tier(11)
            .category("MultiBlock Structural Casing")

            .textureFolder("conveyor")
            .register();
    }

    private static void registerRobotArmCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_RobotArm.getId(), RobotArm.class)

            .tier(1)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_RobotArm.getId(), RobotArm.class)

            .tier(2)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_RobotArm.getId(), RobotArm.class)

            .tier(3)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_RobotArm.getId(), RobotArm.class)

            .tier(4)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_RobotArm.getId(), RobotArm.class)

            .tier(5)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_RobotArm.getId(), RobotArm.class)

            .tier(6)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_RobotArm.getId(), RobotArm.class)

            .tier(7)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_RobotArm.getId(), RobotArm.class)

            .tier(8)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_RobotArm.getId(), RobotArm.class)

            .tier(9)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_RobotArm.getId(), RobotArm.class)

            .tier(10)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_RobotArm.getId(), RobotArm.class)

            .tier(11)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_RobotArm.getId(), RobotArm.class)

            .tier(12)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_RobotArm.getId(), RobotArm.class)

            .tier(13)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_RobotArm.getId(), RobotArm.class)

            .tier(14)
            .category("MultiBlock Functional Casing")

            .textureFolder("robotArm")
            .register();
    }

    private static void registerSensorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Sensor.getId(), Sensor.class)

            .tier(1)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Sensor.getId(), Sensor.class)

            .tier(2)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Sensor.getId(), Sensor.class)

            .tier(3)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Sensor.getId(), Sensor.class)

            .tier(4)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Sensor.getId(), Sensor.class)

            .tier(5)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Sensor.getId(), Sensor.class)

            .tier(6)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Sensor.getId(), Sensor.class)

            .tier(7)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Sensor.getId(), Sensor.class)

            .tier(8)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Sensor.getId(), Sensor.class)

            .tier(9)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Sensor.getId(), Sensor.class)

            .tier(10)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Sensor.getId(), Sensor.class)

            .tier(11)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Sensor.getId(), Sensor.class)

            .tier(12)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Sensor.getId(), Sensor.class)

            .tier(13)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Sensor.getId(), Sensor.class)

            .tier(14)
            .category("MultiBlock Functional Casing")

            .textureFolder("sensor")
            .register();
    }

    private static void registerEmitterCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Emitter.getId(), Emitter.class)

            .tier(1)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Emitter.getId(), Emitter.class)

            .tier(2)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Emitter.getId(), Emitter.class)

            .tier(3)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Emitter.getId(), Emitter.class)

            .tier(4)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Emitter.getId(), Emitter.class)

            .tier(5)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Emitter.getId(), Emitter.class)

            .tier(6)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Emitter.getId(), Emitter.class)

            .tier(7)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Emitter.getId(), Emitter.class)

            .tier(8)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Emitter.getId(), Emitter.class)

            .tier(9)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Emitter.getId(), Emitter.class)

            .tier(10)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Emitter.getId(), Emitter.class)

            .tier(11)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Emitter.getId(), Emitter.class)

            .tier(12)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Emitter.getId(), Emitter.class)

            .tier(13)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Emitter.getId(), Emitter.class)

            .tier(14)
            .category("MultiBlock Functional Casing")

            .textureFolder("emitter")
            .register();
    }

    private static void registerFieldGeneratorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(1)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(2)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(3)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(4)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(5)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(6)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_FieldGenerator.getId(), FieldGenerator.class)

            .tier(7)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(8)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(9)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(10)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(11)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(12)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_FieldGenerator.getId(), FieldGenerator.class)

            .tier(13)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_FieldGenerator.getId(), FieldGenerator.class)

            .tier(14)
            .category("Multiblock Energy Field")

            .textureFolder("fieldGenerator")
            .register();
    }
}
