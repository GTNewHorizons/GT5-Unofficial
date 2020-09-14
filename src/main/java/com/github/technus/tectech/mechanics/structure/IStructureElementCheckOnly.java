package com.github.technus.tectech.mechanics.structure;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IStructureElementCheckOnly<MultiBlock> extends IStructureElement<MultiBlock> {
    @Override
    default boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger){
        return false;
    }

    @Override
    default boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger){
        return false;
    }
}
