package net.glease.ggfab.util;

import java.util.StringJoiner;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GGUtils {

    public static boolean isValidTile(IGregTechTileEntity tile) {
        return tile != null && !tile.isDead()
                && tile.getMetaTileEntity() != null
                && tile.getMetaTileEntity().getBaseMetaTileEntity() == tile;
    }

    public static boolean isValidTile(IMetaTileEntity mte) {
        return mte != null && mte.getBaseMetaTileEntity() != null
                && mte.getBaseMetaTileEntity().getMetaTileEntity() == mte
                && !mte.getBaseMetaTileEntity().isDead();
    }

    public static ChunkCoordinates translate(ChunkCoordinates origin, ForgeDirection direction) {
        return new ChunkCoordinates(
                origin.posX + direction.offsetX,
                origin.posY + direction.offsetY,
                origin.posZ + direction.offsetZ);
    }

    public static String formatTileInfo(String prefix, IMetaTileEntity mte, String delimiter, String suffix) {
        if (!isValidTile(mte)) return prefix + "N/A" + suffix;
        StringJoiner sj = new StringJoiner(delimiter, prefix, suffix);
        IGregTechTileEntity til = mte.getBaseMetaTileEntity();
        sj.add(String.valueOf(til.getXCoord()));
        sj.add(String.valueOf(til.getYCoord()));
        sj.add(String.valueOf(til.getZCoord()));
        return sj.toString();
    }

    public static String formatTileInfo(String prefix, IGregTechTileEntity tile, String delimiter, String suffix) {
        if (!isValidTile(tile)) return prefix + "N/A" + suffix;
        StringJoiner sj = new StringJoiner(delimiter, prefix, suffix);
        sj.add(String.valueOf(tile.getXCoord()));
        sj.add(String.valueOf(tile.getYCoord()));
        sj.add(String.valueOf(tile.getZCoord()));
        return sj.toString();
    }
}
