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
    public float getBlastProofLevel(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 10.0F;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean isGUIClickable(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return false;
    }

    @Override
    public boolean onCoverRightclick(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return false;
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        return true;
    }

    @Override
    public int doCoverThings(ForgeDirection aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return 0;
    }

    @Override
    public boolean isSimpleCover() {
        return true;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected ISerializableObject.LegacyCoverData doCoverThingsImpl(ForgeDirection aSide, byte aInputRedstone,
        int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        return false;
    }

    @Override
    protected ISerializableObject.LegacyCoverData onCoverScrewdriverClickImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverShiftRightClickImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    protected boolean onCoverRemovalImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        return true;
    }

    @Override
    protected String getDescriptionImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return E;
    }

    @Override
    protected float getBlastProofLevelImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return 10.0F;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFibreGoInImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFibreGoOutImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean isGUIClickableImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    @Override
    protected byte getRedstoneInputImpl(ForgeDirection aSide, byte aInputRedstone, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return aInputRedstone;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }

    @Override
    protected byte getLensColorImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return -1;
    }

    @Override
    protected ItemStack getDropImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return null;
    }
}
