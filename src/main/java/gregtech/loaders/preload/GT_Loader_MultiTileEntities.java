package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;
import gregtech.api.multitileentity.multiblock.casing.InventoryUpgrade;
import gregtech.common.tileentities.machines.multiblock.MultiBlock_Macerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Tuple;

public class GT_Loader_MultiTileEntities implements Runnable {
    @Override
    public void run() {
        GT_FML_LOGGER.info("GT_Mod: Regisering MultiTileEntities");
        final MultiTileEntityRegistry aRegistry = new MultiTileEntityRegistry("gt.multitileentity");
        final MultiTileEntityBlock aMachine = MultiTileEntityBlock.getOrCreate(
                "GregTech", "machine", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);

        // Disable for now
        //aRegistry
        //        .create(1000, MultiBlock_Macerator.class)
        //        .name("Large Macerator")
        //        .category("Multiblock Controller")
        //        .setBlock(aMachine)
        //        .material(Materials.Iron)
        //        .texture("metalwall")
        //        .tankCapacity(128000L)
        //        .register();
        //aRegistry
        //        .create(18000, MultiBlockPart.class)
        //        .name("Test Casing")
        //        .category("Multiblock Casing")
        //        .setBlock(aMachine)
        //        .material(Materials.Cobalt)
        //        .texture("metalwall")
        //        .register();
        //aRegistry
        //        .create(20001, InventoryUpgrade.class)
        //        .name("Inventory Upgrade LV")
        //        .category("MultiBlock Special Casing")
        //        .setBlock(aMachine)
        //        .material(Materials.SpaceTime)
        //        .texture("metalwall")
        //        .setNBT(new Tuple("mInventorySize", 16), new Tuple("mTier", 1))
        //        .register();
    }
}
