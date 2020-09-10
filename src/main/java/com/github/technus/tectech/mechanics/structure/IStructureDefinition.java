package com.github.technus.tectech.mechanics.structure;

import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.github.technus.tectech.mechanics.structure.StructureIterationType.*;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.iterate;

public interface IStructureDefinition<T> {
    /**
     * Used internally
     * @param name same name as for other methods here
     * @return the array of elements to process
     */
    IStructureElement<T>[] getStructureFor(String name);

    default boolean check(T object,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC,
                          boolean forceCheckAllBlocks){
        return iterate(object, null, getStructureFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,forceCheckAllBlocks? CHECK_FULLY:CHECK);
    }

    default boolean hints(T object, ItemStack trigger,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC) {
        return iterate(object, trigger, getStructureFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,SPAWN_HINTS);
    }

    default boolean build(T object, ItemStack trigger,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC) {
        return iterate(object, trigger, getStructureFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,BUILD_TEMPLATE);
    }

    default boolean buildOrHints(T object, ItemStack trigger,String piece, World world, ExtendedFacing extendedFacing,
                                 int basePositionX, int basePositionY, int basePositionZ,
                                 int basePositionA, int basePositionB, int basePositionC,
                                 boolean hints){
        return iterate(object, trigger, getStructureFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,hints?SPAWN_HINTS:BUILD_TEMPLATE);
    }
}
