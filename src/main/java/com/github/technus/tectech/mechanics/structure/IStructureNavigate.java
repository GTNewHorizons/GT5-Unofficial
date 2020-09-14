package com.github.technus.tectech.mechanics.structure;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
interface IStructureNavigate<MultiBlock> extends IStructureElement<MultiBlock> {
    @Override
    default boolean check(MultiBlock multiBlock, World world, int x, int y, int z){
        return true;
    }

    @Override
    default boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
        return true;
    }

    @Override
    default boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
        return true;
    }

    default boolean isNavigating(){
        return true;
    }
}
