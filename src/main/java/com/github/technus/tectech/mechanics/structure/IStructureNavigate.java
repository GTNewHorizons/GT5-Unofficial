package com.github.technus.tectech.mechanics.structure;

import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
public interface IStructureNavigate<T> extends IStructureElement<T> {
    @Override
    default boolean check(T t, World world, int x, int y, int z){
        return true;
    }

    @Override
    default boolean spawnHint(T t, World world, int x, int y, int z) {
        return true;
    }

    @Override
    default boolean placeBlock(T t, World world, int x, int y, int z) {
        return true;
    }
}
