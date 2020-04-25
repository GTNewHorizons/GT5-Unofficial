package com.github.technus.tectech.mechanics.structure;

import net.minecraft.world.World;

/**
 * Use StructureUtility to instantiate
 */
public interface IStructureElement<T> extends IStructureElementProvider<T> {
    boolean check(T t,World world,int x,int y,int z);

    default boolean spawnHint(T t,World world,int x,int y,int z){
        return false;
    }

    default boolean placeBlock(T t,World world,int x,int y,int z){
        return false;
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

    @Override
    default IStructureElement<T> getStructureElement(T object){
        return this;
    }
}
