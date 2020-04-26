package com.github.technus.tectech.mechanics.structure;

import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
public interface IStructureElement<T> {
    boolean check(T t,World world,int x,int y,int z);

    default boolean spawnHint(T t,World world,int x,int y,int z){
        return false;
    }

    default boolean placeBlock(T t,World world,int x,int y,int z){
        return false;
    }
}
