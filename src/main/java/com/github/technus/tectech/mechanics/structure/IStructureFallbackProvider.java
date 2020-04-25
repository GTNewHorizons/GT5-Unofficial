package com.github.technus.tectech.mechanics.structure;

import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
public interface IStructureFallbackProvider<T> extends IStructureElement<T> {
    IStructureElementProvider<T>[] fallbacks();

    @Override
    default boolean check(T t,World world, int x, int y, int z){
        for (IStructureElementProvider<T> fallback : fallbacks()) {
            if (fallback.getStructureElement(t).check(t, world, x, y, z)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean spawnHint(T t,World world, int x, int y, int z) {
        for (IStructureElementProvider<T> fallback : fallbacks()) {
            if (fallback.getStructureElement(t).spawnHint(t, world, x, y, z)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean placeBlock(T t,World world, int x, int y, int z) {
        for (IStructureElementProvider<T> fallback : fallbacks()) {
            if (fallback.getStructureElement(t).placeBlock(t, world, x, y, z)) {
                return true;
            }
        }
        return false;
    }
}
