package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.multitileentity.enums.GT_MultiTileCasing.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.multiblock.base.WallShareablePart;
import gregtech.api.multitileentity.multiblock.casing.BasicCasing;
import gregtech.common.tileentities.casings.functional.MotorCasing;
import gregtech.common.tileentities.casings.upgrade.InventoryUpgrade;
import gregtech.common.tileentities.machines.multiblock.MultiBlock_CokeOven;
import gregtech.common.tileentities.machines.multiblock.MultiBlock_Macerator;

public class GT_Loader_MultiTileEntities implements Runnable {

    public static int CASING_REGISTRY = 0;

    @Override
    public void run() {
        GT_FML_LOGGER.info("GT_Mod: Registering MultiTileEntities");
        // registerMachines();
        // registerCasings();

    }

    private static void registerMachines() {
        final MultiTileEntityBlock machine = MultiTileEntityBlock
            .getOrCreate("GregTech", "machine", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);
        final MultiTileEntityRegistry machineRegistry = new MultiTileEntityRegistry("gt.multitileentity.controllers");
        // Disable for now
        machineRegistry.create(1000, MultiBlock_Macerator.class)
            .name("Large Macerator")
            .category("Multiblock Controller")
            .setBlock(machine)
            .material(Materials.Iron)
            .texture("metalwall")
            .tankCapacity(128000L)
            .inputInventorySize(16)
            .outputInventorySize(16)
            .register();
        machineRegistry.create(0, MultiBlock_CokeOven.class)
            .name("Coke Oven")
            .category("MultiblockController")
            .setBlock(machine)
            .texture("cokeOven")
            .inputInventorySize(1)
            .outputInventorySize(1)
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
        casingRegistry.create(18000, BasicCasing.class)
            .name("Test Casing")
            .category("Multiblock Casing")
            .setBlock(casing)
            .material(Materials.Cobalt)
            .texture("metalwall")
            .register();
        casingRegistry.create(20001, InventoryUpgrade.class)
            .name("Inventory Upgrade LV")
            .category("MultiBlock Special Casing")
            .setBlock(casing)
            .material(MaterialsUEVplus.SpaceTime)
            .texture("metalwall")
            .upgradeInventorySize(16)
            .tier(1)
            .register();
        casingRegistry.create(20002, InventoryUpgrade.class)
            .name("Inventory Upgrade MV")
            .category("MultiBlock Upgrade Casing")
            .setBlock(casing)
            .material(Materials.Neutronium)
            .texture("metalwall")
            .upgradeInventorySize(24)
            .tier(2)
            .register();
        casingRegistry.create(10000, MotorCasing.class)
            .name("Motor Casing LV")
            .tier(1)
            .category("MultiBlock Functional Casing")
            .setBlock(casing)
            .material(Materials.Arsenic)
            .texture("metalwall")
            .register();
    }
}
