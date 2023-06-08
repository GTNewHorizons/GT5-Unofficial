package gregtech.api.util;

import static gregtech.api.enums.GT_Values.E;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

/**
 * For Covers with a special behavior. Has fixed storage format of 4 byte. Not very convenient...
 */
public abstract class GT_CoverBehavior extends GT_CoverBehaviorBase<ISerializableObject.LegacyCoverData> {

    public EntityPlayer lastPlayer = null;
    public boolean mPlayerNotified = false;

    public GT_CoverBehavior() {
        this(null);
    }

    public GT_CoverBehavior(ITexture coverTexture) {
        super(ISerializableObject.LegacyCoverData.class, coverTexture);
    }

    protected static int convert(ISerializableObject.LegacyCoverData data) {
        return data == null ? 0 : data.get();
    }

    // region bridge the parent call to legacy calls

    @Override
    public final ISerializableObject.LegacyCoverData createDataObject() {
        return new ISerializableObject.LegacyCoverData();
    }

    @Override
    public ISerializableObject.LegacyCoverData createDataObject(int aLegacyData) {
        return new ISerializableObject.LegacyCoverData(aLegacyData);
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return isRedstoneSensitive(side, aCoverID, aCoverVariable.get(), aTileEntity, aTimer);
    }

    @Override
    protected ISerializableObject.LegacyCoverData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone,
        int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aCoverVariable == null) aCoverVariable = new ISerializableObject.LegacyCoverData();
        aCoverVariable.set(doCoverThings(side, aInputRedstone, aCoverID, aCoverVariable.get(), aTileEntity, aTimer));
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        return onCoverRightclick(side, aCoverID, convert(aCoverVariable), aTileEntity, aPlayer, aX, aY, aZ);
    }

    @Override
    protected ISerializableObject.LegacyCoverData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        if (aCoverVariable == null) aCoverVariable = new ISerializableObject.LegacyCoverData();
        aCoverVariable
            .set(onCoverScrewdriverclick(side, aCoverID, convert(aCoverVariable), aTileEntity, aPlayer, aX, aY, aZ));
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverShiftRightClickImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer) {
        return onCoverShiftRightclick(side, aCoverID, convert(aCoverVariable), aTileEntity, aPlayer);
    }

    @Override
    protected boolean onCoverRemovalImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        return onCoverRemoval(side, aCoverID, convert(aCoverVariable), aTileEntity, aForced);
    }

    @Override
    protected String getDescriptionImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getDescription(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected float getBlastProofLevelImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getBlastProofLevel(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsRedstoneGoInImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoIn(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoOut(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsEnergyIn(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsEnergyOut(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return letsFluidIn(side, aCoverID, convert(aCoverVariable), aFluid, aTileEntity);
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return letsFluidOut(side, aCoverID, convert(aCoverVariable), aFluid, aTileEntity);
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return letsItemsIn(side, aCoverID, convert(aCoverVariable), aSlot, aTileEntity);
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return letsItemsOut(side, aCoverID, convert(aCoverVariable), aSlot, aTileEntity);
    }

    @Override
    protected boolean isGUIClickableImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return isGUIClickable(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return manipulatesSidedRedstoneOutput(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean alwaysLookConnectedImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return alwaysLookConnected(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected byte getRedstoneInputImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getRedstoneInput(side, aInputRedstone, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable,
        ICoverable aTileEntity) {
        return getTickRate(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected byte getLensColorImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getLensColor(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected ItemStack getDropImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getDrop(side, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    // endregion

    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return true;
    }

    /**
     * Called by updateEntity inside the covered TileEntity. aCoverVariable is the Value you returned last time.
     */
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    /**
     * Called when someone rightclicks this Cover.
     * <p/>
     * return true, if something actually happens.
     */
    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return false;
    }

    /**
     * Called when someone rightclicks this Cover with a Screwdriver. Doesn't call @onCoverRightclick in this Case.
     * <p/>
     * return the new Value of the Cover Variable
     */
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return aCoverVariable;
    }

    /**
     * Called when someone shift-rightclicks this Cover with no tool. Doesn't call @onCoverRightclick in this Case.
     */
    public boolean onCoverShiftRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer) {
        if (hasCoverGUI() && aPlayer instanceof EntityPlayerMP) {
            lastPlayer = aPlayer;
            mPlayerNotified = false;
            GT_UIInfos.openCoverUI(aTileEntity, aPlayer, side);
            return true;
        }
        return false;
    }

    /**
     * Removes the Cover if this returns true, or if aForced is true. Doesn't get called when the Machine Block is
     * getting broken, only if you break the Cover away from the Machine.
     */
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        return true;
    }

    /**
     * Gives a small Text for the status of the Cover.
     */
    public String getDescription(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return E;
    }

    /**
     * How Blast Proof the Cover is. 30 is normal.
     */
    public float getBlastProofLevel(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 10.0F;
    }

    /**
     * If it lets RS-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets RS-Signals out of the Block
     */
    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Energy into the Block
     */
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Energy out of the Block
     */
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Liquids into the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Liquids out of the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Items into the Block, aSlot = -1 means if it is generally accepting Items (return false for no
     * Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each
     * Slot).
     */
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Items out of the Block, aSlot = -1 means if it is generally accepting Items (return false for no
     * Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each
     * Slot).
     */
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets you rightclick the Machine normally
     */
    public boolean isGUIClickable(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    /**
     * Needs to return true for Covers, which have a Redstone Output on their Facing.
     */
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return false;
    }

    /**
     * if this Cover should let Pipe Connections look connected even if it is not the case.
     */
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * Called to determine the incoming Redstone Signal of a Machine. Returns the original Redstone per default. The
     * Cover should @letsRedstoneGoIn or the aInputRedstone Parameter is always 0.
     */
    public byte getRedstoneInput(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return letsRedstoneGoIn(side, aCoverID, aCoverVariable, aTileEntity) ? aInputRedstone : 0;
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     * <p/>
     * 0 = No Ticks! Yes, 0 is Default, you have to override this
     */
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }

    /**
     * The MC Color of this Lens. -1 for no Color (meaning this isn't a Lens then).
     */
    public byte getLensColor(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return -1;
    }

    /**
     * @return the ItemStack dropped by this Cover
     */
    public ItemStack getDrop(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return GT_OreDictUnificator.get(true, aTileEntity.getCoverItemAtSide(side));
    }
}
