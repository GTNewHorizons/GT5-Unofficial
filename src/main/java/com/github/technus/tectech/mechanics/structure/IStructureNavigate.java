package com.github.technus.tectech.mechanics.structure;

import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
interface IStructureNavigate<T> extends IStructureElement<T> {
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

    default int getStepA(){
        return 1;
    }

    default int getStepB(){
        return 0;
    }

    default int getStepC(){
        return 0;
    }

    default boolean resetA(){
        return false;
    }

    default boolean resetB(){
        return false;
    }

    default boolean resetC(){
        return false;
    }
}
