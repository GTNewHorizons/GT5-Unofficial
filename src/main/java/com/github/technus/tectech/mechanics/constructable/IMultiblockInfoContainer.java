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
public interface IMultiblockInfoContainer {
    HashMap<String, IMultiblockInfoContainer> MULTIBLOCK_MAP = new HashMap<>();

    static void registerTileClass(Class<? extends TileEntity> clazz, IMultiblockInfoContainer info){
        MULTIBLOCK_MAP.put(clazz.getCanonicalName(),info);
    }

    static void registerMetaClass(Class<? extends IMetaTileEntity> clazz, IMultiblockInfoContainer info){
        MULTIBLOCK_MAP.put(clazz.getCanonicalName(),info);
    }

    void construct(ItemStack stackSize, boolean hintsOnly, TileEntity tileEntity, ExtendedFacing aSide);

    @SideOnly(Side.CLIENT)
    String[] getDescription(int stackSize);
}
