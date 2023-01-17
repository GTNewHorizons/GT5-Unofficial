package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GT_Loader_MultiTileEntities implements Runnable {
    @Override
    public void run() {
        GT_FML_LOGGER.info("GT_Mod: Regisering MultiTileEntities");
        final MultiTileEntityRegistry aRegistry = new MultiTileEntityRegistry("gt.multitileentity");
        final MultiTileEntityBlock aMachine = MultiTileEntityBlock.getOrCreate(
                "GregTech", "machine", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);

        // Disable for now
        //        aRegistry
        //                .create(1000, MultiBlock_Macerator.class)
        //                .name("Large Macerator")
        //                .category("Multiblock Controller")
        //                .setBlock(aMachine)
        //                .material(Materials.Iron)
        //                .texture("metalwall")
        //                .tankCapacity(128000L)
        //                .register();
        //
        //        aRegistry
        //                .create(18000, MultiBlockPart.class)
        //                .name("Test Casing")
        //                .category("Multiblock Casing")
        //                .setBlock(aMachine)
        //                .material(Materials.Cobalt)
        //                .texture("metalwall")
        //                .register();
    }
}
