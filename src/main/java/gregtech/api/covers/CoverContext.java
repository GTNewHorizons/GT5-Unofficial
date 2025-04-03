package gregtech.api.covers;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;

public final class CoverContext {

    private final int coverId;
    private final ForgeDirection side;
    private final ICoverable coverable;
    private final Object coverInitializer;

    public CoverContext(int coverId, ForgeDirection side, ICoverable coverable, Object coverInitializer) {
        this.side = side;
        this.coverId = coverId;
        this.coverable = coverable;
        this.coverInitializer = coverInitializer;
    }

    public ForgeDirection getSide() {
        return side;
    }

    public int getCoverId() {
        return coverId;
    }

    public ICoverable getCoverable() {
        return coverable;
    }

    public Object getCoverInitializer() {
        return coverInitializer;
    }
}
