package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.multitileentity.enums.GT_MultiTileCasing.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.multiblock.base.WallShareablePart;
import gregtech.api.multitileentity.multiblock.casing.BasicCasing;
import gregtech.common.tileentities.casings.functional.Motor;
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
        final MultiTileEntityRegistry complexCasingRegistry = new MultiTileEntityRegistry("gt.multitileentity.complex.casings");
        final MultiTileEntityBlock casing = MultiTileEntityBlock
            .getOrCreate("GregTech", "complexCasing", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
    
        complexCasingRegistry.create(20001, Inventory.class)
            .name("Inventory Upgrade LV")
            .category("MultiBlock Special Casing")
            .setBlock(casing)
            .material(Materials.SpaceTime)
            .texture("metalwall")
            .upgradeInventorySize(16)
            .tier(1)
            .register();
        complexCasingRegistry.create(20002, Inventory.class)
            .name("Inventory Upgrade MV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(casing)
            .material(Materials.Neutronium)
            .texture("metalwall")
            .upgradeInventorySize(24)
            .tier(2)
            .register();
        complexCasingRegistry.create(0, Motor.class)
            .name("Motor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .material(Materials.Arsenic)
            .texture("metalwall")
            .register();
    }
}
