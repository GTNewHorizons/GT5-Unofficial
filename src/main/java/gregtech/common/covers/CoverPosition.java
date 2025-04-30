package gregtech.common.covers;

import java.util.Objects;

import net.minecraft.util.ChunkCoordinates;

public class CoverPosition {

    public final int x, y, z;
    public final String dimName;
    public final int dim;
    public final int side;
    private final int hash;

    public CoverPosition(ChunkCoordinates coords, String dimName, int dim, int side) {
        this.x = coords.posX;
        this.y = coords.posY;
        this.z = coords.posZ;
        this.dimName = dimName;
        this.dim = dim;
        this.side = side;
        this.hash = Objects.hash(x, y, z, dimName, side);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoverPosition that = (CoverPosition) o;
        return this.x == that.x && this.y == that.y
            && this.z == that.z
            && Objects.equals(this.dimName, that.dimName)
            && this.side == that.side;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    public String getDimName() {
        return this.dimName;
    }
}
