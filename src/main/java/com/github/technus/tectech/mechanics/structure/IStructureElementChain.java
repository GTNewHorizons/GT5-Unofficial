package com.github.technus.tectech.mechanics.structure;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
public interface IStructureElementChain<MultiBlock> extends IStructureElement<MultiBlock> {
    IStructureElement<MultiBlock>[] fallbacks();

    @Override
    default boolean check(MultiBlock multiBlock, World world, int x, int y, int z){
        for (IStructureElement<MultiBlock> fallback : fallbacks()) {
            if (fallback.check(multiBlock, world, x, y, z)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean spawnHint(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
        for (IStructureElement<MultiBlock> fallback : fallbacks()) {
            if (fallback.spawnHint(multiBlock, world, x, y, z, trigger)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean placeBlock(MultiBlock multiBlock, World world, int x, int y, int z, ItemStack trigger) {
        for (IStructureElement<MultiBlock> fallback : fallbacks()) {
            if (fallback.placeBlock(multiBlock, world, x, y, z, trigger)) {
                return true;
            }
        }
        return false;
    }
}
