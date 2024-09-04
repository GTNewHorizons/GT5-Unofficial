package gregtech.common.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.covers.IControlsWorkCover;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;

public class CoverRedstoneTransmitterExternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneTransmitterExternal(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        // TODO remove next line after 2.3.0
        if (!IControlsWorkCover.makeSureOnlyOne(side, aTileEntity)) return aCoverVariable;
        GregTechAPI.sWirelessRedstone.put(aCoverVariable, aInputRedstone);
        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        if (!super.isCoverPlaceable(side, aStack, aTileEntity)) return false;
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            if (aTileEntity.getCoverBehaviorAtSideNew(tSide) instanceof IControlsWorkCover) {
                return false;
            }
        }
        return true;
    }
}
