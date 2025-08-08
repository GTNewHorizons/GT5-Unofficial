package tectech.mechanics.enderStorage;

import java.io.Serializable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.base.Objects;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

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

    private EnderLinkTank(int x, int y, int z, int d) {
        X = x;
        Y = y;
        Z = z;
        D = d;
    }

    public boolean shouldSave() {
        World world = DimensionManager.getWorld(D);

        // If world is unloaded (null) or block doesn't exist (chunk unloaded), not safe to remove fluid handler
        if (world == null || !world.blockExists(X, Y, Z)) return true;

        TileEntity tile = world.getTileEntity(X, Y, Z);
        return tile instanceof IFluidHandler;
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

    public NBTTagCompound save() {
        NBTTagCompound data = new NBTTagCompound();

        data.setLong("coords", CoordinatePacker.pack(X, Y, Z));
        data.setInteger("dim", D);

        return data;
    }

    public static EnderLinkTank load(NBTTagCompound data) {
        long coords = data.getLong("coords");
        int dim = data.getInteger("dim");

        return new EnderLinkTank(
            CoordinatePacker.unpackX(coords),
            CoordinatePacker.unpackY(coords),
            CoordinatePacker.unpackZ(coords),
            dim);
    }
}
