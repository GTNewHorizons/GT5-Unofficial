package com.github.technus.tectech.mechanics.structure;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Arrays;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;

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
                basePositionA, basePositionB, basePositionC,false,forceCheckAllBlocks);
    }

    default boolean hints(T object, ItemStack trigger,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC) {
        return iterate(object, trigger, getStructureFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,true,null);
    }

    default boolean build(T object, ItemStack trigger,String piece, World world, ExtendedFacing extendedFacing,
                          int basePositionX, int basePositionY, int basePositionZ,
                          int basePositionA, int basePositionB, int basePositionC) {
        return iterate(object, trigger, getStructureFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,false,null);
    }

    default boolean buildOrHints(T object, ItemStack trigger,String piece, World world, ExtendedFacing extendedFacing,
                                 int basePositionX, int basePositionY, int basePositionZ,
                                 int basePositionA, int basePositionB, int basePositionC,
                                 boolean hintsOnly){
        return iterate(object, trigger, getStructureFor(piece), world, extendedFacing, basePositionX, basePositionY, basePositionZ,
                basePositionA, basePositionB, basePositionC,hintsOnly,null);
    }

    static <T> boolean iterate(T object, ItemStack trigger, IStructureElement<T>[] elements, World world, ExtendedFacing extendedFacing,
                               int basePositionX, int basePositionY, int basePositionZ,
                               int basePositionA, int basePositionB, int basePositionC,
                               boolean hintsOnly, Boolean checkBlocksIfNotNullForceCheckAllIfTrue){
        if(world.isRemote ^ hintsOnly){
            return false;
        }

        //change base position to base offset
        basePositionA=-basePositionA;
        basePositionB=-basePositionB;
        basePositionC=-basePositionC;

        int[] abc = new int[]{basePositionA,basePositionB,basePositionC};
        int[] xyz = new int[3];

        if(checkBlocksIfNotNullForceCheckAllIfTrue!=null){
            if(checkBlocksIfNotNullForceCheckAllIfTrue){
                for (IStructureElement<T> element : elements) {
                    if(element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    }else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            if(!element.check(object, world, xyz[0], xyz[1], xyz[2])){
                                if(DEBUG_MODE){
                                    TecTech.LOGGER.info("Multi ["+basePositionX+", "+basePositionY+", "+basePositionZ+"] failed @ "+
                                            Arrays.toString(xyz)+" "+Arrays.toString(abc));
                                }
                                return false;
                            }
                        } else {
                            if(DEBUG_MODE){
                                TecTech.LOGGER.info("Multi ["+basePositionX+", "+basePositionY+", "+basePositionZ+"] !blockExists @ "+
                                        Arrays.toString(xyz)+" "+Arrays.toString(abc));
                            }
                            return false;
                        }
                        abc[0]+=1;
                    }
                }
            } else {
                for (IStructureElement<T> element : elements) {
                    if(element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    }else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            if(!element.check(object, world, xyz[0], xyz[1], xyz[2])){
                                if(DEBUG_MODE){
                                    TecTech.LOGGER.info("Multi ["+basePositionX+", "+basePositionY+", "+basePositionZ+"] failed @ "+
                                            Arrays.toString(xyz)+" "+Arrays.toString(abc));
                                }
                                return false;
                            }
                        } else {
                            if(DEBUG_MODE){
                                TecTech.LOGGER.info("Multi ["+basePositionX+", "+basePositionY+", "+basePositionZ+"] !blockExists @ "+
                                        Arrays.toString(xyz)+" "+Arrays.toString(abc));
                            }
                        }
                        abc[0]+=1;
                    }
                }
            }
        }else {
            if(hintsOnly) {
                for (IStructureElement<T> element : elements) {
                    if(element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    }else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        element.spawnHint(object, world, xyz[0], xyz[1], xyz[2], trigger);

                        abc[0]+=1;
                    }
                }
            } else {
                for (IStructureElement<T> element : elements) {
                    if(element.isNavigating()) {
                        abc[0] = (element.resetA() ? basePositionA : abc[0]) + element.getStepA();
                        abc[1] = (element.resetB() ? basePositionB : abc[1]) + element.getStepB();
                        abc[2] = (element.resetC() ? basePositionC : abc[2]) + element.getStepC();
                    }else {
                        extendedFacing.getWorldOffset(abc, xyz);
                        xyz[0] += basePositionX;
                        xyz[1] += basePositionY;
                        xyz[2] += basePositionZ;

                        if (world.blockExists(xyz[0], xyz[1], xyz[2])) {
                            element.placeBlock(object, world, xyz[0], xyz[1], xyz[2], trigger);
                        }
                        abc[0]+=1;
                    }
                }
            }
        }
        return true;
    }
}
