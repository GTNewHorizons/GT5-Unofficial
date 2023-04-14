package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.multitileentity.enums.GT_MultiTileCasing.*;
import static gregtech.api.multitileentity.enums.GT_MultiTileComplexCasing.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.MultiTileEntityBlock;
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
import gregtech.common.tileentities.casings.upgrade.Inventory;
import gregtech.common.tileentities.machines.multiblock.AdvChemicalReactor;
import gregtech.common.tileentities.machines.multiblock.CokeOven;
import gregtech.common.tileentities.machines.multiblock.Macerator;

public class GT_Loader_MultiTileEntities implements Runnable {

    public static int CASING_REGISTRY = 0;

    @Override
    public void run() {
        GT_FML_LOGGER.info("GT_Mod: Registering MultiTileEntities");
        registerMachines();
        registerCasings();
        registerComplexCasings();
    }

    private static void registerMachines() {
        final MultiTileEntityBlock machine = MultiTileEntityBlock
            .getOrCreate("GregTech", "machine", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
        final MultiTileEntityRegistry machineRegistry = new MultiTileEntityRegistry("gt.multitileentity.controllers");
        // Disable for now
        machineRegistry.create(1000, Macerator.class)
            .name("Large Macerator")
            .category("Multiblock Controller")
            .setBlock(machine)
            .material(Materials.Iron)
            .texture("metalwall")
            .tankCapacity(128000L)
            .inputInventorySize(16)
            .outputInventorySize(16)
            .register();
        machineRegistry.create(0, CokeOven.class)
            .name("Coke Oven")
            .category("MultiblockController")
            .setBlock(machine)
            .texture("cokeOven")
            .inputInventorySize(1)
            .outputInventorySize(1)
            .register();
        machineRegistry.create(1, AdvChemicalReactor.class)
            .name("Advanced Chemical Reactor")
            .category("MultiblockController")
            .setBlock(machine)
            // TODO: Texture
            .texture("chemicalWall")
            .inputInventorySize(16)
            .outputInventorySize(16)
            .tankCapacity(128000L)
            .register();
    }

    private static void registerCasings() {
        final MultiTileEntityRegistry casingRegistry = new MultiTileEntityRegistry("gt.multitileentity.casings");
        final MultiTileEntityBlock casing = MultiTileEntityBlock
            .getOrCreate("GregTech", "casing", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
        casingRegistry.create(CokeOven.getId(), WallShareablePart.class)
            .name("Coke Oven Bricks")
            .category("MultiBlock Casing")
            .setBlock(casing)
            .texture("cokeOven")
            .register();
        casingRegistry.create(Chemical.getId(), BasicCasing.class)
            .name("Chemical Casings")
            .category("MultiBlock Casing")
            .setBlock(casing)
            .texture("chemicalWall")
            .register();
        casingRegistry.create(18000, BasicCasing.class)
            .name("Test Casing")
            .category("Multiblock Casing")
            .setBlock(casing)
            .material(Materials.Cobalt)
            .texture("metalwall")
            .register();

    }

    private static void registerComplexCasings() {
        final MultiTileEntityRegistry complexCasingRegistry = new MultiTileEntityRegistry(
            "gt.multitileentity.complex.casings");
        final MultiTileEntityBlock complexCasing = MultiTileEntityBlock.getOrCreate(
            "GregTech",
            "complexCasing",
            Material.iron,
            Block.soundTypeMetal,
            "wrench",
            0,
            0,
            15,
            true,
            true);
        registerMotorCasings(complexCasingRegistry, complexCasing);
        registerPumpCasings(complexCasingRegistry, complexCasing);
        registerPistonCasings(complexCasingRegistry, complexCasing);
        registerRobotArmCasings(complexCasingRegistry, complexCasing);
        registerConveyorCasings(complexCasingRegistry, complexCasing);
        registerEmitterCasings(complexCasingRegistry, complexCasing);
        registerSensorCasings(complexCasingRegistry, complexCasing);
        registerFieldGeneratorCasings(complexCasingRegistry, complexCasing);

        complexCasingRegistry.create(20001, Inventory.class)
            .name("Inventory Upgrade LV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(complexCasing)
            .material(Materials.SpaceTime)
            .texture("metalwall")
            .upgradeInventorySize(16)
            .tier(1)
            .register();
        complexCasingRegistry.create(20002, Inventory.class)
            .name("Inventory Upgrade MV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(complexCasing)
            .material(Materials.Neutronium)
            .texture("metalwall")
            .upgradeInventorySize(24)
            .tier(2)
            .register();
    }

    private static void registerMotorCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_Motor.getId(), Motor.class)
            .name("Motor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MV_Motor.getId(), Motor.class)
            .name("Motor Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(HV_Motor.getId(), Motor.class)
            .name("Motor Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(EV_Motor.getId(), Motor.class)
            .name("Motor Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(IV_Motor.getId(), Motor.class)
            .name("Motor Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_Motor.getId(), Motor.class)
            .name("Motor Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(ZPM_Motor.getId(), Motor.class)
            .name("Motor Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UV_Motor.getId(), Motor.class)
            .name("Motor Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UHV_Motor.getId(), Motor.class)
            .name("Motor Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UEV_Motor.getId(), Motor.class)
            .name("Motor Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UIV_Motor.getId(), Motor.class)
            .name("Motor Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UMV_Motor.getId(), Motor.class)
            .name("Motor Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UXV_Motor.getId(), Motor.class)
            .name("Motor Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MAX_Motor.getId(), Motor.class)
            .name("Motor Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
    }

    private static void registerPumpCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_Pump.getId(), Pump.class)
            .name("Pump Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MV_Pump.getId(), Pump.class)
            .name("Pump Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(HV_Pump.getId(), Pump.class)
            .name("Pump Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(EV_Pump.getId(), Pump.class)
            .name("Pump Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(IV_Pump.getId(), Pump.class)
            .name("Pump Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_Pump.getId(), Pump.class)
            .name("Pump Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_Pump.getId(), Pump.class)
            .name("Pump Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(ZPM_Pump.getId(), Pump.class)
            .name("Pump Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UV_Pump.getId(), Pump.class)
            .name("Pump Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UHV_Pump.getId(), Pump.class)
            .name("Pump Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UEV_Pump.getId(), Pump.class)
            .name("Pump Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UIV_Pump.getId(), Pump.class)
            .name("Pump Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UMV_Pump.getId(), Pump.class)
            .name("Pump Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UXV_Pump.getId(), Pump.class)
            .name("Pump Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MAX_Pump.getId(), Pump.class)
            .name("Pump Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
    }

    private static void registerPistonCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_Piston.getId(), Piston.class)
            .name("Piston Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MV_Piston.getId(), Piston.class)
            .name("Piston Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(HV_Piston.getId(), Piston.class)
            .name("Piston Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(EV_Piston.getId(), Piston.class)
            .name("Piston Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(IV_Piston.getId(), Piston.class)
            .name("Piston Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_Piston.getId(), Piston.class)
            .name("Piston Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(ZPM_Piston.getId(), Piston.class)
            .name("Piston Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UV_Piston.getId(), Piston.class)
            .name("Piston Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UHV_Piston.getId(), Piston.class)
            .name("Piston Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UEV_Piston.getId(), Piston.class)
            .name("Piston Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UIV_Piston.getId(), Piston.class)
            .name("Piston Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UMV_Piston.getId(), Piston.class)
            .name("Piston Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UXV_Piston.getId(), Piston.class)
            .name("Piston Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MAX_Piston.getId(), Piston.class)
            .name("Piston Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
    }

    private static void registerConveyorCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing LV")
            .tier(1)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing MV")
            .tier(2)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(HV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing HV")
            .tier(3)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(EV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing EV")
            .tier(4)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(IV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing IV")
            .tier(5)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing LuV")
            .tier(6)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(ZPM_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing ZPM")
            .tier(7)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UV")
            .tier(8)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UHV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UHV")
            .tier(9)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UEV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UEV")
            .tier(10)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UIV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UIV")
            .tier(11)
            .category("MultiBlock Structural Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
    }

    private static void registerRobotArmCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(HV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(EV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(IV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(ZPM_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UHV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UEV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UIV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UMV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UXV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MAX_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
    }

    private static void registerSensorCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(MV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(HV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(EV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(IV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(LuV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(ZPM_Sensor.getId(), Sensor.class)
            .name("Sensor Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(UV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(UHV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(UEV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(UIV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(UMV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(UXV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
        complexCasingRegistry.create(MAX_Sensor.getId(), Sensor.class)
            .name("Sensor Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .register();
    }

    private static void registerEmitterCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(HV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(EV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(IV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(ZPM_Emitter.getId(), Emitter.class)
            .name("Emitter Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UHV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UEV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UIV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UMV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UXV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MAX_Emitter.getId(), Emitter.class)
            .name("Emitter Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
    }

    private static void registerFieldGeneratorCasings(MultiTileEntityRegistry complexCasingRegistry,
        MultiTileEntityBlock complexCasing) {
        complexCasingRegistry.create(LV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator LV")
            .tier(1)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator MV")
            .tier(2)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(HV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator HV")
            .tier(3)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(EV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator EV")
            .tier(4)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(IV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator IV")
            .tier(5)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(LuV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator LuV")
            .tier(6)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(ZPM_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator ZPM")
            .tier(7)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UV")
            .tier(8)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UHV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UHV")
            .tier(9)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UEV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UEV")
            .tier(10)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UIV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UIV")
            .tier(11)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UMV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UMV")
            .tier(12)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(UXV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UXV")
            .tier(13)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
        complexCasingRegistry.create(MAX_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator MAX")
            .tier(14)
            .category("Multiblock Energy Field")
            .setBlock(complexCasing)
            .texture("metalwall")
            .register();
    }
}
