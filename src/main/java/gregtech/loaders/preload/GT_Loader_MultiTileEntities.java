package gregtech.loaders.preload;

import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;
import gregtech.api.util.GT_Util;
import gregtech.common.tileentities.machines.multiblock.MultiBlock_Macerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;


import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.NBT;
import static gregtech.api.util.GT_Util.tuple;

public class GT_Loader_MultiTileEntities implements Runnable {
    @Override
    public void run() {
        GT_FML_LOGGER.info("GT_Mod: Regisering MultiTileEntities");
        final MultiTileEntityRegistry aRegistry = new MultiTileEntityRegistry("gt.multitileentity");
        final MultiTileEntityBlock aMachine = MultiTileEntityBlock.getOrCreate("GregTech", "machine", Material.iron, Block.soundTypeMetal, "wrench", 0, 0, 15, true, true);

        Class<? extends TileEntity> aClass;

        aClass = MultiBlock_Macerator.class; aRegistry.add(
            "Large Macerator", "Multiblock Controller", 1000,  aClass, 1, 64, aMachine,
            GT_Util.makeNBT(tuple(NBT.MATERIAL, Material.iron),  tuple(NBT.TEXTURE, "metalwall"), tuple(NBT.TANK_CAPACITY, 128000L))
        );

        aClass = MultiBlockPart.class; aRegistry.add(
         "Test Casing", "Multiblock Casing", 18000, aClass, 1, 64, aMachine,
          GT_Util.makeNBT(tuple(NBT.MATERIAL, Material.iron),  tuple(NBT.TEXTURE, "metalwall"))
        );

    }
}
