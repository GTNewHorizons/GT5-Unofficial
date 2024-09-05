package gregtech.common.covers;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehavior;

public class CoverLens extends CoverBehavior {

    private final byte mColor;

    public CoverLens(byte aColor, ITexture coverTexture) {
        super(coverTexture);
        this.mColor = aColor;
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public byte getLensColor(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return this.mColor;
    }
}
