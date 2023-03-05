package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;

public class GT_Loader_MultiTileEntities implements Runnable {

    @Override
    public void run() {
        GT_FML_LOGGER.info("GT_Mod: Registering MultiTileEntities");
        final MultiTileEntityRegistry aRegistry = new MultiTileEntityRegistry("gt.multitileentity");
        final MultiTileEntityBlock aMachine = MultiTileEntityBlock.getOrCreate(
                "GregTech",
                "machine",
                Material.iron,
                Block.soundTypeMetal,
                "wrench",
                0,
                0,
                15,
                true,
                true);

        // Disable for now
        /*
         * aRegistry.create(1000, MultiBlock_Macerator.class).name("Large Macerator").category("Multiblock Controller")
         * .setBlock(aMachine).material(Materials.Iron).texture("metalwall").tankCapacity(128000L)
         * .inputInventorySize(16).outputInventorySize(16).register(); aRegistry.create(18000,
         * BasicCasing.class).name("Test Casing").category("Multiblock Casing").setBlock(aMachine)
         * .material(Materials.Cobalt).texture("metalwall").register(); aRegistry.create(20001,
         * InventoryUpgrade.class).name("Inventory Upgrade LV")
         * .category("MultiBlock Special Casing").setBlock(aMachine).material(Materials.SpaceTime)
         * .texture("metalwall").upgradeInventorySize(16).tier(1).register(); aRegistry.create(20002,
         * InventoryUpgrade.class).name("Inventory Upgrade MV")
         * .category("MultiBlock Upgrade Casing").setBlock(aMachine).material(Materials.Neutronium)
         * .texture("metalwall").upgradeInventorySize(24).tier(2).register(); aRegistry.create(10000,
         * MotorCasing.class).name("Motor Casing LV").tier(1)
         * .category("MultiBlock Functional Casing").setBlock(aMachine).material(Materials.Arsenic)
         * .texture("metalwall").register();
         */
    }
}
