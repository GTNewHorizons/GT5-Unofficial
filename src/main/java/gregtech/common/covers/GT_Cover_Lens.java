package gregtech.common.covers;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;

public class GT_Cover_Lens extends GT_CoverBehavior {

    private final byte mColor;

    public GT_Cover_Lens(byte aColor, ITexture coverTexture) {
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
