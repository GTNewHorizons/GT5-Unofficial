package com.github.technus.tectech.mechanics.enderStorage;

import java.io.Serializable;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.base.Objects;

public class EnderLinkTank implements Serializable {

    private static final long serialVersionUID = 1030297456736434221L;
    private final int X;
    private final int Y;
    private final int Z;
    private final int D;

    public EnderLinkTank(IFluidHandler fluidHandler) {
        TileEntity tile = (TileEntity) fluidHandler;
        X = tile.xCoord;
        Y = tile.yCoord;
        Z = tile.zCoord;
        D = tile.getWorldObj().provider.dimensionId;
    }

    public IFluidHandler getFluidHandler() {
        IFluidHandler fluidHandler = null;
        TileEntity tile = DimensionManager.getWorld(D).getTileEntity(X, Y, Z);
        if (tile instanceof IFluidHandler) fluidHandler = (IFluidHandler) tile;
        return fluidHandler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnderLinkTank that = (EnderLinkTank) o;
        return X == that.X && Y == that.Y && Z == that.Z && D == that.D;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(X, Y, Z, D);
    }
}
