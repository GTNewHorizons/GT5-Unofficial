package com.github.technus.tectech.thing.metaTileEntity.multi.base.render;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.CompressedSpacetimeCoilLevel;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.util.GT_StructureUtility;
import gregtech.common.blocks.GT_Block_Casings5;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class TestExtender {
    public static <T> IStructureElement<T> ofCoil(BiConsumer<T, CompressedSpacetimeCoilLevel> aHeatingCoilSetter, Function<T, CompressedSpacetimeCoilLevel> aHeatingCoilGetter) {
        return ofCoil((t, l) -> {
            aHeatingCoilSetter.accept(t, l);
            return true;
        }, aHeatingCoilGetter);
    }

    /**
     * Heating coil structure element.
     * @param aHeatingCoilSetter Notify the controller of this new coil.
     *                            Got called exactly once per coil.
     *                            Might be called less times if structure test fails.
     *                            If the setter returns false then it assumes the coil is rejected.
     * @param aHeatingCoilGetter Get the current heating level. Null means no coil recorded yet.
     */
    public static <T> IStructureElement<T> ofCoil(BiPredicate<T, CompressedSpacetimeCoilLevel> aHeatingCoilSetter, Function<T, CompressedSpacetimeCoilLevel> aHeatingCoilGetter) {
        if (aHeatingCoilSetter == null || aHeatingCoilGetter == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                if (!(block instanceof IHeatingCoil))
                    return false;
                CompressedSpacetimeCoilLevel existingLevel = aHeatingCoilGetter.apply(t),
                        newLevel = ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z));
                if (existingLevel == null || existingLevel == CompressedSpacetimeCoilLevel.None) {
                    return aHeatingCoilSetter.test(t, newLevel);
                } else {
                    return newLevel == existingLevel;
                }
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, GregTech_API.sBlockCasings5, getMeta(trigger));
                return true;
            }

            private int getMeta(ItemStack trigger) {
                return GT_Block_Casings5.getMetaFromCoilHeat(CompressedSpacetimeCoilLevel.getFromTier(Math.min(CompressedSpacetimeCoilLevel.getMaxTier(), Math.max(0, trigger.stackSize - 1))));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, GregTech_API.sBlockCasings5, getMeta(trigger), 3);
            }
        };
    }
}
