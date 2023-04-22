package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.multitileentity.enums.GT_MultiTileCasing.*;
import static gregtech.api.multitileentity.enums.GT_MultiTileComponentCasing.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
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

    public static final String COMPONENT_CASING_REGISTRY = "gt.multitileentity.component.casings";
    public static final String CASING_REGISTRY = "gt.multitileentity.casings";
    public static final String MACHINE_REGISTRY = "gt.multitileentity.controllers";

    @Override
    public void run() {
        if (Mods.NewHorizonsCoreMod.isModLoaded()) {
            return;
        }
        GT_FML_LOGGER.info("GT_Mod: Registering MultiTileEntities");
        registerMachines();
        registerCasings();
        registerComponentCasings();
    }

    private static void registerMachines() {
        final MultiTileEntityRegistry machineRegistry = new MultiTileEntityRegistry(MACHINE_REGISTRY);
        final MultiTileEntityBlock machine = MultiTileEntityBlock
            .getOrCreate("GregTech", "machine", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
        // Disable for now
        machineRegistry.create(1000, Macerator.class)
            .name("Large Macerator")
            .category("Multiblock Controller")
            .setBlock(machine)
            .material(Materials.Iron)
            .textureFolder("macerator")
            .tankCapacity(128000L)
            .inputInventorySize(16)
            .outputInventorySize(16)
            .register();
        machineRegistry.create(0, CokeOven.class)
            .name("Coke Oven")
            .category("MultiblockController")
            .setBlock(machine)
            .textureFolder("cokeOven")
            .inputInventorySize(1)
            .outputInventorySize(1)
            .register();
        machineRegistry.create(1, AdvChemicalReactor.class)
            .name("Advanced Chemical Reactor")
            .category("MultiblockController")
            .setBlock(machine)
            // TODO: Texture
            .textureFolder("advChemicalReactor")
            .inputInventorySize(16)
            .outputInventorySize(16)
            .tankCapacity(128000L)
            .register();
    }

    private static void registerCasings() {
        final MultiTileEntityRegistry casingRegistry = new MultiTileEntityRegistry(CASING_REGISTRY);
        final MultiTileEntityBlock casing = MultiTileEntityBlock
            .getOrCreate("GregTech", "casing", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
        casingRegistry.create(CokeOven.getId(), WallShareablePart.class)
            .name("Coke Oven Bricks")
            .category("MultiBlock Casing")
            .setBlock(casing)
            .textureFolder("cokeOven")
            .register();
        casingRegistry.create(Chemical.getId(), BasicCasing.class)
            .name("Chemical Casing")
            .category("MultiBlock Casing")
            .setBlock(casing)
            .textureFolder("advChemicalReactor")
            .register();
        casingRegistry.create(18000, BasicCasing.class)
            .name("Test Casing")
            .category("Multiblock Casing")
            .setBlock(casing)
            .material(Materials.Cobalt)
            .textureFolder("macerator")
            .register();

    }

    private static void registerComponentCasings() {
        final MultiTileEntityRegistry componentCasingRegistry = new MultiTileEntityRegistry(COMPONENT_CASING_REGISTRY);
        final MultiTileEntityBlock componentCasing = MultiTileEntityBlock.getOrCreate(
            "GregTech",
            "componentCasing",
            Material.iron,
            Block.soundTypeMetal,
            "wrench",
            0,
            0,
            15,
            true,
            true);
        registerMotorCasings(componentCasingRegistry, componentCasing);
        registerPumpCasings(componentCasingRegistry, componentCasing);
        registerPistonCasings(componentCasingRegistry, componentCasing);
        registerRobotArmCasings(componentCasingRegistry, componentCasing);
        registerConveyorCasings(componentCasingRegistry, componentCasing);
        registerEmitterCasings(componentCasingRegistry, componentCasing);
        registerSensorCasings(componentCasingRegistry, componentCasing);
        registerFieldGeneratorCasings(componentCasingRegistry, componentCasing);

        componentCasingRegistry.create(20001, Inventory.class)
            .name("Inventory Upgrade LV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(componentCasing)
            .material(MaterialsUEVplus.SpaceTime)
            .textureFolder("macerator")
            .upgradeInventorySize(16)
            .tier(1)
            .register();
        componentCasingRegistry.create(20002, Inventory.class)
            .name("Inventory Upgrade MV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(componentCasing)
            .material(Materials.Neutronium)
            .textureFolder("macerator")
            .upgradeInventorySize(24)
            .tier(2)
            .register();
    }

    private static void registerMotorCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_Motor.getId(), Motor.class)
            .name("Motor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(MV_Motor.getId(), Motor.class)
            .name("Motor Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(HV_Motor.getId(), Motor.class)
            .name("Motor Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(EV_Motor.getId(), Motor.class)
            .name("Motor Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(IV_Motor.getId(), Motor.class)
            .name("Motor Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(LuV_Motor.getId(), Motor.class)
            .name("Motor Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(ZPM_Motor.getId(), Motor.class)
            .name("Motor Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(UV_Motor.getId(), Motor.class)
            .name("Motor Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(UHV_Motor.getId(), Motor.class)
            .name("Motor Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(UEV_Motor.getId(), Motor.class)
            .name("Motor Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(UIV_Motor.getId(), Motor.class)
            .name("Motor Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(UMV_Motor.getId(), Motor.class)
            .name("Motor Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(UXV_Motor.getId(), Motor.class)
            .name("Motor Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
        registry.create(MAX_Motor.getId(), Motor.class)
            .name("Motor Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("motor")
            .register();
    }

    private static void registerPumpCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_Pump.getId(), Pump.class)
            .name("Pump Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(MV_Pump.getId(), Pump.class)
            .name("Pump Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(HV_Pump.getId(), Pump.class)
            .name("Pump Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(EV_Pump.getId(), Pump.class)
            .name("Pump Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(IV_Pump.getId(), Pump.class)
            .name("Pump Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(LuV_Pump.getId(), Pump.class)
            .name("Pump Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(LuV_Pump.getId(), Pump.class)
            .name("Pump Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(ZPM_Pump.getId(), Pump.class)
            .name("Pump Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(UV_Pump.getId(), Pump.class)
            .name("Pump Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(UHV_Pump.getId(), Pump.class)
            .name("Pump Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(UEV_Pump.getId(), Pump.class)
            .name("Pump Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(UIV_Pump.getId(), Pump.class)
            .name("Pump Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(UMV_Pump.getId(), Pump.class)
            .name("Pump Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(UXV_Pump.getId(), Pump.class)
            .name("Pump Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
        registry.create(MAX_Pump.getId(), Pump.class)
            .name("Pump Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("pump")
            .register();
    }

    private static void registerPistonCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_Piston.getId(), Piston.class)
            .name("Piston Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(MV_Piston.getId(), Piston.class)
            .name("Piston Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(HV_Piston.getId(), Piston.class)
            .name("Piston Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(EV_Piston.getId(), Piston.class)
            .name("Piston Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(IV_Piston.getId(), Piston.class)
            .name("Piston Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(LuV_Piston.getId(), Piston.class)
            .name("Piston Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(ZPM_Piston.getId(), Piston.class)
            .name("Piston Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(UV_Piston.getId(), Piston.class)
            .name("Piston Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(UHV_Piston.getId(), Piston.class)
            .name("Piston Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(UEV_Piston.getId(), Piston.class)
            .name("Piston Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(UIV_Piston.getId(), Piston.class)
            .name("Piston Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(UMV_Piston.getId(), Piston.class)
            .name("Piston Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(UXV_Piston.getId(), Piston.class)
            .name("Piston Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
        registry.create(MAX_Piston.getId(), Piston.class)
            .name("Piston Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("piston")
            .register();
    }

    private static void registerConveyorCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing LV")
            .tier(1)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(MV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing MV")
            .tier(2)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(HV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing HV")
            .tier(3)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(EV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing EV")
            .tier(4)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(IV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing IV")
            .tier(5)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(LuV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing LuV")
            .tier(6)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(ZPM_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing ZPM")
            .tier(7)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(UV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UV")
            .tier(8)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(UHV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UHV")
            .tier(9)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(UEV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UEV")
            .tier(10)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
        registry.create(UIV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UIV")
            .tier(11)
            .category("MultiBlock Structural Casing")
            .setBlock(casing)
            .textureFolder("conveyor")
            .register();
    }

    private static void registerRobotArmCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(MV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(HV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(EV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(IV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(LuV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(ZPM_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(UV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(UHV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(UEV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(UIV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(UMV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(UXV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
        registry.create(MAX_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("robotArm")
            .register();
    }

    private static void registerSensorCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(MV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(HV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(EV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(IV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(LuV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(ZPM_Sensor.getId(), Sensor.class)
            .name("Sensor Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(UV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(UHV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(UEV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(UIV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(UMV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(UXV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
        registry.create(MAX_Sensor.getId(), Sensor.class)
            .name("Sensor Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("sensor")
            .register();
    }

    private static void registerEmitterCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(MV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(HV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(EV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(IV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(LuV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(ZPM_Emitter.getId(), Emitter.class)
            .name("Emitter Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(UV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(UHV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(UEV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(UIV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(UMV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(UXV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
        registry.create(MAX_Emitter.getId(), Emitter.class)
            .name("Emitter Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .textureFolder("emitter")
            .register();
    }

    private static void registerFieldGeneratorCasings(MultiTileEntityRegistry registry, MultiTileEntityBlock casing) {
        registry.create(LV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator LV")
            .tier(1)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(MV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator MV")
            .tier(2)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(HV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator HV")
            .tier(3)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(EV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator EV")
            .tier(4)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(IV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator IV")
            .tier(5)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(LuV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator LuV")
            .tier(6)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(ZPM_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator ZPM")
            .tier(7)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(UV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UV")
            .tier(8)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(UHV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UHV")
            .tier(9)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(UEV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UEV")
            .tier(10)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(UIV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UIV")
            .tier(11)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(UMV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UMV")
            .tier(12)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(UXV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UXV")
            .tier(13)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
        registry.create(MAX_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator MAX")
            .tier(14)
            .category("Multiblock Energy Field")
            .setBlock(casing)
            .textureFolder("fieldGenerator")
            .register();
    }
}
