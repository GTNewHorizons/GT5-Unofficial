package gtPlusPlus.api.objects.minecraft;

import java.io.Serializable;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class BlockPos implements Serializable {

    private static final long serialVersionUID = -7271947491316682006L;
    public final int xPos;
    public final int yPos;
    public final int zPos;
    public final int dim;

    public static @NotNull BlockPos generateBlockPos(@NotNull String sUUID) {
        String[] s2 = sUUID.split("@");
        return new BlockPos(s2);
    }

    public BlockPos(String @NotNull [] s) {
        this(Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[0]));
    }

    public BlockPos(int x, int y, int z, int dim) {
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
        this.dim = dim;
    }

    public BlockPos(int x, int y, int z, @Nullable World world) {
        this(x, y, z, world == null ? 0 : world.provider.dimensionId);
    }

    public BlockPos(@NotNull IGregTechTileEntity b) {
        this(b.getXCoord(), b.getYCoord(), b.getZCoord(), b.getWorld());
    }

    public @NotNull String getLocationString() {
        return "[X: " + this.xPos + "][Y: " + this.yPos + "][Z: " + this.zPos + "][Dim: " + this.dim + "]";
    }

    public @NotNull String getUniqueIdentifier() {
        return this.dim + "@" + this.xPos + "@" + this.yPos + "@" + this.zPos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash += (13 * this.xPos);
        hash += (19 * this.yPos);
        hash += (31 * this.zPos);
        hash += (17 * this.dim);
        return hash;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof BlockPos otherPoint)) {
            return false;
        }
        return this.xPos == otherPoint.xPos && this.yPos == otherPoint.yPos
            && this.zPos == otherPoint.zPos
            && this.dim == otherPoint.dim;
    }

    public @NotNull BlockPos getUp() {
        return new BlockPos(this.xPos, this.yPos + 1, this.zPos, this.dim);
    }

}
