package gregtech.api.util;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCoverGUI;
import gregtech.api.objects.GT_ItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import static gregtech.api.enums.GT_Values.E;

/**
 * For Covers with a special behavior.
 *
 * @author glease
 */
public abstract class GT_CoverBehaviorBase<T extends ISerializableObject> {

    public EntityPlayer lastPlayer = null;
    private final Class<T> typeToken;

    protected GT_CoverBehaviorBase(Class<T> typeToken) {
        this.typeToken = typeToken;
    }

    public abstract T createDataObject(int aLegacyData);

    public abstract T createDataObject();

    public final T createDataObject(NBTBase aNBT) {
        // Handle legacy data (migrating from GT_CoverBehavior to GT_CoverBehaviorBase)
        if (aNBT instanceof NBTTagInt) {
            return createDataObject(((NBTTagInt) aNBT).func_150287_d());
        }

        T ret = createDataObject();
        ret.loadDataFromNBT(aNBT);
        return ret;
    }

    public final T cast(ISerializableObject aData) {
        if (typeToken.isInstance(aData))
            return forceCast(aData);
        return null;
    }

    private T forceCast(ISerializableObject aData) {
        try {
            return typeToken.cast(aData);
        } catch (Exception e) {
            throw new RuntimeException("Casting data in " + this.getClass() + ", data " + aData, e);
        }
    }

    // region facade

    /**
     * Get target facade block. Does not affect rendering of **this** block. It is only used as a hint for other block
     * in case of CTM
     * @return null if none, otherwise return facade target block
     */
    public final Block getFacadeBlock(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getFacadeBlockImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Get target facade block. Does not affect rendering of **this** block. It is only used as a hint for other block
     * in case of CTM
     * @return 0 if none, otherwise return facade target meta
     */
    public final int getFacadeMeta(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getFacadeMetaImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Get the display stack. Default to {@code int2Stack(aCoverID)}
     */
    public final ItemStack getDisplayStack(int aCoverID, ISerializableObject aCoverVariable) {
        return getDisplayStackImpl(aCoverID,forceCast(aCoverVariable));
    }

    /**
     * Get the special cover texture associated with this cover. Return null if one should use the texture passed to
     * {@link gregtech.api.GregTech_API#registerCover(ItemStack, ITexture, GT_CoverBehaviorBase)} or its overloads.
     */
    public final ITexture getSpecialCoverTexture(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getSpecialCoverTextureImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Return whether cover data needs to be synced to client upon tile entity creation or cover placement.
     *
     * Note if you want to sync the data afterwards you will have to manually do it by calling {@link ICoverable#issueCoverUpdate(byte)}
     * This option only affects the initial sync.
     */
    public final boolean isDataNeededOnClient(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return isDataNeededOnClientImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Called upon receiving data from network. Use {@link ICoverable#isClientSide()} to determine the side.
     */
    public final void onDataChanged(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        onDataChangedImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Called upon cover being removed. Called on both server and client.
     */
    public final void onDropped(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        onDroppedImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    public final boolean isRedstoneSensitive(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return isRedstoneSensitiveImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity, aTimer);
    }

    /**
     * Called by updateEntity inside the covered TileEntity. aCoverVariable is the Value you returned last time.
     */
    public final T doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return doCoverThingsImpl(aSide, aInputRedstone, aCoverID, forceCast(aCoverVariable), aTileEntity, aTimer);
    }

    /**
     * Called when someone rightclicks this Cover.
     * <p/>
     * return true, if something actually happens.
     */
    public final boolean onCoverRightClick(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return onCoverRightClickImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity, aPlayer, aX, aY, aZ);
    }

    /**
     * Called when someone rightclicks this Cover with a Screwdriver. Doesn't call @onCoverRightclick in this Case.
     * <p/>
     * return the new Value of the Cover Variable
     */
    public final T onCoverScrewdriverClick(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return onCoverScrewdriverClickImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity, aPlayer, aX, aY, aZ);
    }

    /**
     * Called when someone shift-rightclicks this Cover with no tool. Doesn't call @onCoverRightclick in this Case.
     */
    public final boolean onCoverShiftRightClick(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer) {
        return onCoverShiftRightClickImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity, aPlayer);
    }

    public final Object getClientGUI(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return getClientGUIImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity, aPlayer, aWorld);
    }

    /**
     * Removes the Cover if this returns true, or if aForced is true.
     * Doesn't get called when the Machine Block is getting broken, only if you break the Cover away from the Machine.
     */
    public final boolean onCoverRemoval(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        return onCoverRemovalImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity, aForced);
    }

    /**
     * Gives a small Text for the status of the Cover.
     */
    public final String getDescription(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getDescriptionImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * How Blast Proof the Cover is. 30 is normal.
     */
    public final float getBlastProofLevel(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getBlastProofLevelImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * If it lets RS-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    public final boolean letsRedstoneGoIn(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoInImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * If it lets RS-Signals out of the Block
     */
    public final boolean letsRedstoneGoOut(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoOutImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * If it lets Fibre-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    public final boolean letsFibreGoIn(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return letsFibreGoInImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * If it lets Fibre-Signals out of the Block
     */
    public final boolean letsFibreGoOut(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return letsFibreGoOutImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * If it lets Energy into the Block
     */
    public final boolean letsEnergyIn(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return letsEnergyInImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * If it lets Energy out of the Block
     */
    public final boolean letsEnergyOut(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return letsEnergyOutImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * If it lets Liquids into the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public final boolean letsFluidIn(byte aSide, int aCoverID, ISerializableObject aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return letsFluidInImpl(aSide, aCoverID, forceCast(aCoverVariable), aFluid, aTileEntity);
    }

    /**
     * If it lets Liquids out of the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public final boolean letsFluidOut(byte aSide, int aCoverID, ISerializableObject aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return letsFluidOutImpl(aSide, aCoverID, forceCast(aCoverVariable), aFluid, aTileEntity);
    }

    /**
     * If it lets Items into the Block, aSlot = -1 means if it is generally accepting Items (return false for no reaction at all), aSlot = -2 means if it would accept for all Slots Impl(return true to skip the Checks for each Slot).
     */
    public final boolean letsItemsIn(byte aSide, int aCoverID, ISerializableObject aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return letsItemsInImpl(aSide, aCoverID, forceCast(aCoverVariable), aSlot, aTileEntity);
    }

    /**
     * If it lets Items out of the Block, aSlot = -1 means if it is generally accepting Items (return false for no reaction at all), aSlot = -2 means if it would accept for all Slots Impl(return true to skip the Checks for each Slot).
     */
    public final boolean letsItemsOut(byte aSide, int aCoverID, ISerializableObject aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return letsItemsOutImpl(aSide, aCoverID, forceCast(aCoverVariable), aSlot, aTileEntity);
    }

    /**
     * If it lets you rightclick the Machine normally
     */
    public final boolean isGUIClickable(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return isGUIClickableImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Needs to return true for Covers, which have a Redstone Output on their Facing.
     */
    public final boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return manipulatesSidedRedstoneOutputImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * if this Cover should let Pipe Connections look connected even if it is not the case.
     */
    public final boolean alwaysLookConnected(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return alwaysLookConnectedImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Called to determine the incoming Redstone Signal of a Machine.
     * Returns the original Redstone per default.
     * The Cover should @letsRedstoneGoIn or the aInputRedstone Parameter is always 0.
     */
    public final byte getRedstoneInput(byte aSide, byte aInputRedstone, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getRedstoneInputImpl(aSide, aInputRedstone, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     * <p/>
     * 0 = No Ticks! Yes, 0 is Default, you have to override this
     */
    public final int getTickRate(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getTickRateImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }


    /**
     * The MC Color of this Lens. -1 for no Color (meaning this isn't a Lens then).
     */
    public final byte getLensColor(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getLensColorImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }

    /**
     * @return the ItemStack dropped by this Cover
     */
    public final ItemStack getDrop(byte aSide, int aCoverID, ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return getDropImpl(aSide, aCoverID, forceCast(aCoverVariable), aTileEntity);
    }
    // endregion

    // region impl

    protected Block getFacadeBlockImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return null;
    }

    protected int getFacadeMetaImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }

    protected ItemStack getDisplayStackImpl(int aCoverID, T aCoverVariable) {
        return GT_Utility.intToStack(aCoverID);
    }

    protected ITexture getSpecialCoverTextureImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return null;
    }

    protected boolean isDataNeededOnClientImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    protected void onDataChangedImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
    }


    protected void onDroppedImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
    }

    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return true;
    }

    /**
     * Called by updateEntity inside the covered TileEntity. aCoverVariable is the Value you returned last time.
     */
    protected T doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, T aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    /**
     * Called when someone rightclicks this Cover.
     * <p/>
     * return true, if something actually happens.
     */
    protected boolean onCoverRightClickImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return false;
    }

    /**
     * Called when someone rightclicks this Cover with a Screwdriver. Doesn't call @onCoverRightclick in this Case.
     * <p/>
     * return the new Value of the Cover Variable
     */
    protected T onCoverScrewdriverClickImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return aCoverVariable;
    }

    /**
     * Called when someone shift-rightclicks this Cover with no tool. Doesn't call @onCoverRightclick in this Case.
     */
    protected boolean onCoverShiftRightClickImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer) {
        if (hasCoverGUI() && aPlayer instanceof EntityPlayerMP) {
            lastPlayer = aPlayer;
            GT_Values.NW.sendToPlayer(new GT_Packet_TileEntityCoverGUI(aSide, aCoverID, aCoverVariable, aTileEntity, (EntityPlayerMP) aPlayer), (EntityPlayerMP) aPlayer);
            return true;
        }
        return false;
    }

    protected Object getClientGUIImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return null;
    }

    /**
     * Removes the Cover if this returns true, or if aForced is true.
     * Doesn't get called when the Machine Block is getting broken, only if you break the Cover away from the Machine.
     */
    protected boolean onCoverRemovalImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        return true;
    }

    /**
     * Gives a small Text for the status of the Cover.
     */
    protected String getDescriptionImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return E;
    }

    /**
     * How Blast Proof the Cover is. 30 is normal.
     */
    protected float getBlastProofLevelImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return 10.0F;
    }

    /**
     * If it lets RS-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    protected boolean letsRedstoneGoInImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets RS-Signals out of the Block
     */
    protected boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Fibre-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    protected boolean letsFibreGoInImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Fibre-Signals out of the Block
     */
    protected boolean letsFibreGoOutImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Energy into the Block
     */
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Energy out of the Block
     */
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Liquids into the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, T aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Liquids out of the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, T aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Items into the Block, aSlot = -1 means if it is generally accepting Items (return false for no Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each Slot).
     */
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, T aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets Items out of the Block, aSlot = -1 means if it is generally accepting Items (return false for no Interaction at all), aSlot = -2 means if it would accept for all Slots (return true to skip the Checks for each Slot).
     */
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, T aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return false;
    }

    /**
     * If it lets you rightclick the Machine normally
     */
    protected boolean isGUIClickableImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * Needs to return true for Covers, which have a Redstone Output on their Facing.
     */
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * if this Cover should let Pipe Connections look connected even if it is not the case.
     */
    protected boolean alwaysLookConnectedImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    /**
     * Called to determine the incoming Redstone Signal of a Machine.
     * Returns the original Redstone per default.
     * The Cover should @letsRedstoneGoIn or the aInputRedstone Parameter is always 0.
     */
    protected byte getRedstoneInputImpl(byte aSide, byte aInputRedstone, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return letsRedstoneGoIn(aSide, aCoverID, aCoverVariable, aTileEntity) ? aInputRedstone : 0;
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     * <p/>
     * 0 = No Ticks! Yes, 0 is Default, you have to override this
     */
    protected int getTickRateImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }


    /**
     * The MC Color of this Lens. -1 for no Color (meaning this isn't a Lens then).
     */
    protected byte getLensColorImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return -1;
    }

    /**
     * @return the ItemStack dropped by this Cover
     */
    protected ItemStack getDropImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return GT_OreDictUnificator.get(true, aTileEntity.getCoverItemAtSide(aSide));
    }

    //endregion

    // region no data

    /**
     * Checks if the Cover can be placed on this.
     */
    public boolean isCoverPlaceable(byte aSide, ItemStack aStack, ICoverable aTileEntity) {
        return isCoverPlaceable(aSide, new GT_ItemStack(aStack), aTileEntity);
    }

    /**
     * Checks if the Cover can be placed on this. You will probably want to call {@link #isCoverPlaceable(byte, ItemStack, ICoverable)} instead.
     */
    @Deprecated
    public boolean isCoverPlaceable(byte aSide, GT_ItemStack aStack, ICoverable aTileEntity) {
        return true;
    }

    public boolean hasCoverGUI() {
        return false;
    }

    /**
     * Called when someone rightclicks this Cover Client Side
     * <p/>
     * return true, if something actually happens.
     */
    public boolean onCoverRightclickClient(byte aSide, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return false;
    }

    /**
     * If this is a simple Cover, which can also be used on Bronze Machines and similar.
     */
    public boolean isSimpleCover() {
        return false;
    }

    /**
     * sets the Cover upon placement.
     */
    public void placeCover(byte aSide, ItemStack aCover, ICoverable aTileEntity) {
        aTileEntity.setCoverIDAtSide(aSide, GT_Utility.stackToInt(aCover));
    }

    @Deprecated
    public String trans(String aNr, String aEnglish) {
        return GT_Utility.trans(aNr, aEnglish);
    }
    // endregion
}
