package gregtech.api.objects;

import static gregtech.api.enums.GT_Values.E;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.ISerializableObject;

public class GT_Cover_None extends GT_CoverBehavior {

    /**
     * This is the Dummy, if there is no Cover
     */
    public GT_Cover_None() {}

    @Override
    public float getBlastProofLevel(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 10.0F;
    }

    @Override
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean isGUIClickable(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return false;
    }

    @Override
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return false;
    }

    @Override
    public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        return true;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return 0;
    }

    @Override
    public boolean isSimpleCover() {
        return true;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected ISerializableObject.LegacyCoverData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(byte aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        return false;
    }

    @Override
    protected ISerializableObject.LegacyCoverData onCoverScrewdriverClickImpl(byte aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverShiftRightClickImpl(byte aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    protected boolean onCoverRemovalImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity, boolean aForced) {
        return true;
    }

    @Override
    protected String getDescriptionImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return E;
    }

    @Override
    protected float getBlastProofLevelImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return 10.0F;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFibreGoInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFibreGoOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean isGUIClickableImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(byte aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected byte getRedstoneInputImpl(byte aSide, byte aInputRedstone, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return aInputRedstone;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return 0;
    }

    @Override
    protected byte getLensColorImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return -1;
    }

    @Override
    protected ItemStack getDropImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return null;
    }
}
