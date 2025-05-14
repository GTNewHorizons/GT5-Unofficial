package gregtech.common.data.maglev;

import java.util.Objects;

public final class Tether {

    private final int sourceX;
    private final int sourceY;
    private final int sourceZ;
    private final int dimID;
    private int range;
    private byte tier;

    public Tether(int sourceX, int sourceY, int sourceZ, int dimID, int range, byte tier) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.dimID = dimID;
        this.range = range;
        this.tier = tier;
    }

    public int sourceX() {
        return sourceX;
    }

    public int sourceY() {
        return sourceY;
    }

    public int sourceZ() {
        return sourceZ;
    }

    public int dimID() {
        return dimID;
    }

    public int range() {
        return range;
    }

    public void range(int value) {
        range = value;
    }

    public byte tier() {
        return tier;
    }

    public void tier(byte value) {
        tier = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Tether) obj;
        return this.sourceX == that.sourceX && this.sourceY == that.sourceY
            && this.sourceZ == that.sourceZ
            && this.dimID == that.dimID
            && this.range == that.range
            && this.tier == that.tier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceX, sourceY, sourceZ, dimID, range, tier);
    }

    @Override
    public String toString() {
        return "Tether[" + "sourceX="
            + sourceX
            + ", "
            + "sourceY="
            + sourceY
            + ", "
            + "sourceZ="
            + sourceZ
            + ", "
            + "dimID="
            + dimID
            + ", "
            + "range="
            + range
            + ", "
            + "tier="
            + tier
            + ']';
    }

}
