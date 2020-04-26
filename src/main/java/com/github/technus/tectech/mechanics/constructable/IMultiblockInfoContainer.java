package com.github.technus.tectech.mechanics.constructable;

import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

/**
 * To implement IConstructable on not own TileEntities
 */
public interface IMultiblockInfoContainer<T> {
    HashMap<String, IMultiblockInfoContainer<?>> MULTIBLOCK_MAP = new HashMap<>();

    static <T extends TileEntity> void  registerTileClass(Class<T> clazz, IMultiblockInfoContainer<?> info){
        MULTIBLOCK_MAP.put(clazz.getCanonicalName(),info);
    }

    static <T extends IMetaTileEntity> void registerMetaClass(Class<T> clazz, IMultiblockInfoContainer<?> info){
        MULTIBLOCK_MAP.put(clazz.getCanonicalName(),info);
    }

    @SuppressWarnings("unchecked")
    static <T> IMultiblockInfoContainer<T> get(Class<?> tClass){
        return (IMultiblockInfoContainer<T>)MULTIBLOCK_MAP.get(tClass.getCanonicalName());
    }

    static boolean contains(Class<?> tClass){
        return MULTIBLOCK_MAP.containsKey(tClass.getCanonicalName());
    }

    void construct(ItemStack stackSize, boolean hintsOnly, T tileEntity, ExtendedFacing aSide);

    @SideOnly(Side.CLIENT)
    String[] getDescription(ItemStack stackSize);
}
