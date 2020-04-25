package com.github.technus.tectech.mechanics.structure;

import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
public interface IStructureFallback<T> extends IStructureElement<T> {
    IStructureElement<T>[] fallbacks();

    @Override
    default boolean check(T t, World world, int x, int y, int z){
        for (IStructureElement<T> fallback : fallbacks()) {
            if (fallback.check(t, world, x, y, z)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean spawnHint(T t, World world, int x, int y, int z) {
        for (IStructureElement<T> fallback : fallbacks()) {
            if (fallback.spawnHint(t, world, x, y, z)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean placeBlock(T t, World world, int x, int y, int z) {
        for (IStructureElement<T> fallback : fallbacks()) {
            if (fallback.placeBlock(t, world, x, y, z)) {
                return true;
            }
        }
        return false;
    }
}
