package gregtech.api.util;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCoverGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import static gregtech.api.enums.GT_Values.E;

/**
 * For Covers with a special behavior. Has fixed storage format of 4 byte. Not very convenient...
 */
public abstract class GT_CoverBehavior extends GT_CoverBehaviorBase<ISerializableObject.LegacyCoverData> {

    public EntityPlayer lastPlayer = null;

    public GT_CoverBehavior() {
        super(ISerializableObject.LegacyCoverData.class);
    }

    private static int convert(ISerializableObject.LegacyCoverData data) {
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
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return isRedstoneSensitive(aSide, aCoverID, aCoverVariable.get(), aTileEntity, aTimer);
    }

    @Override
    protected ISerializableObject.LegacyCoverData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aCoverVariable == null)
            aCoverVariable = new ISerializableObject.LegacyCoverData();
        aCoverVariable.set(doCoverThings(aSide, aInputRedstone, aCoverID, aCoverVariable.get(), aTileEntity, aTimer));
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return onCoverRightclick(aSide, aCoverID, convert(aCoverVariable), aTileEntity, aPlayer, aX, aY, aZ);
    }

    @Override
    protected ISerializableObject.LegacyCoverData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aCoverVariable == null)
            aCoverVariable = new ISerializableObject.LegacyCoverData();
        aCoverVariable.set(onCoverScrewdriverclick(aSide, aCoverID, convert(aCoverVariable), aTileEntity, aPlayer, aX, aY, aZ));
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverShiftRightClickImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer) {
        return onCoverShiftRightclick(aSide, aCoverID, convert(aCoverVariable), aTileEntity, aPlayer);
    }

    @Override
    protected Object getClientGUIImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return getClientGUI(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean onCoverRemovalImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        return onCoverRemoval(aSide, aCoverID, convert(aCoverVariable), aTileEntity, aForced);
    }

    @Override
    protected String getDescriptionImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getDescription(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected float getBlastProofLevelImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getBlastProofLevel(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsRedstoneGoInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoIn(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoOut(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsFibreGoInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsFibreGoIn(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsFibreGoOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsFibreGoOut(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsEnergyIn(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return letsEnergyOut(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return letsFluidIn(aSide, aCoverID, convert(aCoverVariable), aFluid, aTileEntity);
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return letsFluidOut(aSide, aCoverID, convert(aCoverVariable), aFluid, aTileEntity);
    }

    @Override
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return letsItemsIn(aSide, aCoverID, convert(aCoverVariable), aSlot, aTileEntity);
    }

    @Override
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return letsItemsOut(aSide, aCoverID, convert(aCoverVariable), aSlot, aTileEntity);
    }

    @Override
    protected boolean isGUIClickableImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return isGUIClickable(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return manipulatesSidedRedstoneOutput(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected boolean alwaysLookConnectedImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return alwaysLookConnected(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected byte getRedstoneInputImpl(byte aSide, byte aInputRedstone, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getRedstoneInput(aSide, aInputRedstone, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getTickRate(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected byte getLensColorImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getLensColor(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    @Override
    protected ItemStack getDropImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return getDrop(aSide, aCoverID, convert(aCoverVariable), aTileEntity);
    }

    // endregion

    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return true;
    }

    /**
     * Called by updateEntity inside the covered TileEntity. aCoverVariable is the Value you returned last time.
     */
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    /**
     * Called when someone rightclicks this Cover.
     * <p/>
     * return true, if something actually happens.
     */
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return false;
    }

    /**
     * Called when someone rightclicks this Cover with a Screwdriver. Doesn't call @onCoverRightclick in this Case.
     * <p/>
     * return the new Value of the Cover Variable
     */
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return aCoverVariable;
    }

    /**
     * Called when someone shift-rightclicks this Cover with no tool. Doesn't call @onCoverRightclick in this Case.
     */
    public boolean onCoverShiftRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer) {
        if(hasCoverGUI() && aPlayer instanceof EntityPlayerMP) {
            lastPlayer = aPlayer;
            GT_Values.NW.sendToPlayer(new GT_Packet_TileEntityCoverGUI(aSide, aCoverID, aCoverVariable, aTileEntity, (EntityPlayerMP) aPlayer), (EntityPlayerMP) aPlayer);
            return true;
        }
        return false;
    }

    public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity) {
        return null;
    }

    /**
     * Removes the Cover if this returns true, or if aForced is true.
     * Doesn't get called when the Machine Block is getting broken, only if you break the Cover away from the Machine.
     */
    public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        return true;
    }

    /**
     * Gives a small Text for the status of the Cover.
     */
    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return E;
    }

    /**
     * How Blast Proof the Cover is. 30 is normal.
     */
    public float getBlastProofLevel(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 10.0F;
    }

    /**
     * If it lets RS-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets RS-Signals out of the Block
     */
    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Fibre-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    public boolean letsFibreGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Fibre-Signals out of the Block
     */
    public boolean letsFibreGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Energy into the Block
     */
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Energy out of the Block
     */
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Liquids into the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Liquids out of the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Items into the Block, aSlot = -1 means if it is generally accepting Items (return false for no Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each Slot).
     */
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Items out of the Block, aSlot = -1 means if it is generally accepting Items (return false for no Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each Slot).
     */
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets you rightclick the Machine normally
     */
    public boolean isGUIClickable(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * Needs to return true for Covers, which have a Redstone Output on their Facing.
     */
    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * if this Cover should let Pipe Connections look connected even if it is not the case.
     */
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * Called to determine the incoming Redstone Signal of a Machine.
     * Returns the original Redstone per default.
     * The Cover should @letsRedstoneGoIn or the aInputRedstone Parameter is always 0.
     */
    public byte getRedstoneInput(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoIn(aSide, aCoverID, aCoverVariable, aTileEntity) ? aInputRedstone : 0;
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     * <p/>
     * 0 = No Ticks! Yes, 0 is Default, you have to override this
     */
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }

    /**
     * The MC Color of this Lens. -1 for no Color (meaning this isn't a Lens then).
     */
    public byte getLensColor(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return -1;
    }

    /**
     * @return the ItemStack dropped by this Cover
     */
    public ItemStack getDrop(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return GT_OreDictUnificator.get(true, aTileEntity.getCoverItemAtSide(aSide));
    }
}
