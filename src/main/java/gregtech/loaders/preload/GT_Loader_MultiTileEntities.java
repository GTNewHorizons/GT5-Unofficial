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
import gregtech.common.tileentities.machines.multiblock.AdvChemicalProcessor;
import gregtech.common.tileentities.machines.multiblock.CokeOven;
import gregtech.common.tileentities.machines.multiblock.Macerator;

public class GT_Loader_MultiTileEntities implements Runnable {

    public static final String COMPONENT_CASING_REGISTRY_NAME = "gt.multitileentity.component.casings";
    public static final String CASING_REGISTRY_NAME = "gt.multitileentity.casings";
    public static final String MACHINE_REGISTRY_NAME = "gt.multitileentity.controllers";
    public static final MultiTileEntityRegistry MACHINE_REGISTRY = new MultiTileEntityRegistry(MACHINE_REGISTRY_NAME);
    public static final MultiTileEntityBlock MACHINE_BLOCK = MultiTileEntityBlock
        .getOrCreate("GregTech", "machine", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
    public static final MultiTileEntityRegistry CASING_REGISTRY = new MultiTileEntityRegistry(CASING_REGISTRY_NAME);
    public static final MultiTileEntityBlock CASING_BLOCK = MultiTileEntityBlock
        .getOrCreate("GregTech", "casing", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
    public static final MultiTileEntityRegistry COMPONENT_CASING_REGISTRY = new MultiTileEntityRegistry(
        COMPONENT_CASING_REGISTRY_NAME);
    public static final MultiTileEntityBlock COMPONENT_CASING_BLOCK = MultiTileEntityBlock.getOrCreate(
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
        // Disable for now
        MACHINE_REGISTRY.create(1000, Macerator.class)
            .name("Large Macerator")
            .category("Multiblock Controller")
            .setBlock(MACHINE_BLOCK)
            .material(Materials.Iron)
            .textureFolder("macerator")
            .tankCapacity(128000L)
            .inputInventorySize(16)
            .outputInventorySize(16)
            .register();
        MACHINE_REGISTRY.create(0, CokeOven.class)
            .name("Coke Oven")
            .category("MultiblockController")
            .setBlock(MACHINE_BLOCK)
            .textureFolder("cokeOven")
            .inputInventorySize(1)
            .outputInventorySize(1)
            .register();
        MACHINE_REGISTRY.create(1, AdvChemicalProcessor.class)
            .name("Advanced Chemical Reactor")
            .category("MultiblockController")
            .setBlock(MACHINE_BLOCK)
            // TODO: Texture
            .textureFolder("advChemicalReactor")
            .inputInventorySize(16)
            .outputInventorySize(16)
            .tankCapacity(128000L)
            .register();
    }

    private static void registerCasings() {

        CASING_REGISTRY.create(CokeOven.getId(), WallShareablePart.class)
            .name("Coke Oven Bricks")
            .category("MultiBlock Casing")
            .setBlock(CASING_BLOCK)
            .textureFolder("cokeOven")
            .register();
        CASING_REGISTRY.create(Chemical.getId(), BasicCasing.class)
            .name("Chemical Casing")
            .category("MultiBlock Casing")
            .setBlock(CASING_BLOCK)
            .textureFolder("advChemicalReactor")
            .register();
        CASING_REGISTRY.create(18000, BasicCasing.class)
            .name("Test Casing")
            .category("Multiblock Casing")
            .setBlock(CASING_BLOCK)
            .material(Materials.Cobalt)
            .textureFolder("macerator")
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

        COMPONENT_CASING_REGISTRY.create(20001, Inventory.class)
            .name("Inventory Upgrade LV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .material(MaterialsUEVplus.SpaceTime)
            .textureFolder("macerator")
            .upgradeInventorySize(16)
            .tier(1)
            .register();
        COMPONENT_CASING_REGISTRY.create(20002, Inventory.class)
            .name("Inventory Upgrade MV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .material(Materials.Neutronium)
            .textureFolder("macerator")
            .upgradeInventorySize(24)
            .tier(2)
            .register();
    }

    private static void registerMotorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Motor.getId(), Motor.class)
            .name("Motor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Motor.getId(), Motor.class)
            .name("Motor Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Motor.getId(), Motor.class)
            .name("Motor Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Motor.getId(), Motor.class)
            .name("Motor Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Motor.getId(), Motor.class)
            .name("Motor Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Motor.getId(), Motor.class)
            .name("Motor Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Motor.getId(), Motor.class)
            .name("Motor Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Motor.getId(), Motor.class)
            .name("Motor Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Motor.getId(), Motor.class)
            .name("Motor Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Motor.getId(), Motor.class)
            .name("Motor Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Motor.getId(), Motor.class)
            .name("Motor Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Motor.getId(), Motor.class)
            .name("Motor Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Motor.getId(), Motor.class)
            .name("Motor Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Motor.getId(), Motor.class)
            .name("Motor Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("motor")
            .register();
    }

    private static void registerPumpCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Pump.getId(), Pump.class)
            .name("Pump Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Pump.getId(), Pump.class)
            .name("Pump Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Pump.getId(), Pump.class)
            .name("Pump Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Pump.getId(), Pump.class)
            .name("Pump Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Pump.getId(), Pump.class)
            .name("Pump Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Pump.getId(), Pump.class)
            .name("Pump Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Pump.getId(), Pump.class)
            .name("Pump Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Pump.getId(), Pump.class)
            .name("Pump Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Pump.getId(), Pump.class)
            .name("Pump Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Pump.getId(), Pump.class)
            .name("Pump Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Pump.getId(), Pump.class)
            .name("Pump Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Pump.getId(), Pump.class)
            .name("Pump Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Pump.getId(), Pump.class)
            .name("Pump Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Pump.getId(), Pump.class)
            .name("Pump Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Pump.getId(), Pump.class)
            .name("Pump Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("pump")
            .register();
    }

    private static void registerPistonCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Piston.getId(), Piston.class)
            .name("Piston Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Piston.getId(), Piston.class)
            .name("Piston Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Piston.getId(), Piston.class)
            .name("Piston Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Piston.getId(), Piston.class)
            .name("Piston Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Piston.getId(), Piston.class)
            .name("Piston Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Piston.getId(), Piston.class)
            .name("Piston Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Piston.getId(), Piston.class)
            .name("Piston Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Piston.getId(), Piston.class)
            .name("Piston Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Piston.getId(), Piston.class)
            .name("Piston Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Piston.getId(), Piston.class)
            .name("Piston Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Piston.getId(), Piston.class)
            .name("Piston Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Piston.getId(), Piston.class)
            .name("Piston Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Piston.getId(), Piston.class)
            .name("Piston Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Piston.getId(), Piston.class)
            .name("Piston Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("piston")
            .register();
    }

    private static void registerConveyorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing LV")
            .tier(1)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing MV")
            .tier(2)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing HV")
            .tier(3)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing EV")
            .tier(4)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing IV")
            .tier(5)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing LuV")
            .tier(6)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing ZPM")
            .tier(7)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UV")
            .tier(8)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UHV")
            .tier(9)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UEV")
            .tier(10)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Conveyor.getId(), Conveyor.class)
            .name("Conveyor Casing UIV")
            .tier(11)
            .category("MultiBlock Structural Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("conveyor")
            .register();
    }

    private static void registerRobotArmCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_RobotArm.getId(), RobotArm.class)
            .name("Robot Arm Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("robotArm")
            .register();
    }

    private static void registerSensorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Sensor.getId(), Sensor.class)
            .name("Sensor Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Sensor.getId(), Sensor.class)
            .name("Sensor Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Sensor.getId(), Sensor.class)
            .name("Sensor Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("sensor")
            .register();
    }

    private static void registerEmitterCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing MV")
            .tier(2)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing HV")
            .tier(3)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing EV")
            .tier(4)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing IV")
            .tier(5)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing LuV")
            .tier(6)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_Emitter.getId(), Emitter.class)
            .name("Emitter Casing ZPM")
            .tier(7)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UV")
            .tier(8)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UHV")
            .tier(9)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UEV")
            .tier(10)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UIV")
            .tier(11)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UMV")
            .tier(12)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_Emitter.getId(), Emitter.class)
            .name("Emitter Casing UXV")
            .tier(13)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_Emitter.getId(), Emitter.class)
            .name("Emitter Casing MAX")
            .tier(14)
            .category("MultiBlock Functional Casing")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("emitter")
            .register();
    }

    private static void registerFieldGeneratorCasings() {
        COMPONENT_CASING_REGISTRY.create(LV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator LV")
            .tier(1)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(MV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator MV")
            .tier(2)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(HV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator HV")
            .tier(3)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(EV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator EV")
            .tier(4)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(IV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator IV")
            .tier(5)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(LuV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator LuV")
            .tier(6)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(ZPM_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator ZPM")
            .tier(7)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UV")
            .tier(8)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UHV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UHV")
            .tier(9)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UEV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UEV")
            .tier(10)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UIV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UIV")
            .tier(11)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UMV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UMV")
            .tier(12)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(UXV_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator UXV")
            .tier(13)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
        COMPONENT_CASING_REGISTRY.create(MAX_FieldGenerator.getId(), FieldGenerator.class)
            .name("Field Generator MAX")
            .tier(14)
            .category("Multiblock Energy Field")
            .setBlock(COMPONENT_CASING_BLOCK)
            .textureFolder("fieldGenerator")
            .register();
    }
}
