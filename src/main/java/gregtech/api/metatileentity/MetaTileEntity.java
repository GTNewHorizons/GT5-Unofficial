package gregtech.api.metatileentity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.pathing.IPathingGrid;
import appeng.api.util.AECableType;
import appeng.core.localization.WailaText;
import appeng.me.helpers.AENetworkProxy;

import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMachineCallback;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_TooltipDataCache;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
import gregtech.common.covers.CoverInfo;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaMachine Call the Constructor with the desired ID at the load-phase (not preload
 * and also not postload!) Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it. "new
 * GT_MetaTileEntity_E_Furnace(54, "GT_E_Furnace", "Automatic E-Furnace");"
 */
@SuppressWarnings("unused")
public abstract class MetaTileEntity implements IMetaTileEntity, IMachineCallback<MetaTileEntity> {

    /**
     * Only assigned for the MetaTileEntity in the List! Also only used to get the localized Name for the ItemStack and
     * for getInvName.
     */
    public final String mName;
    /**
     * The Inventory of the MetaTileEntity. Amount of Slots can be larger than 256. HAYO!
     */
    public final ItemStack[] mInventory;

    /**
     * Inventory wrapper for ModularUI
     */
    public final ItemStackHandler inventoryHandler;

    protected GT_GUIColorOverride colorOverride;
    protected GT_TooltipDataCache mTooltipCache = new GT_TooltipDataCache();

    @Override
    public ItemStackHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public boolean doTickProfilingInThisTick = true;

    private MetaTileEntity mCallBackTile;

    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

    public long mSoundRequests = 0;

    /**
     * This registers your Machine at the List. Use only ID's larger than 2048, because i reserved these ones. See also
     * the List in the API, as it has a Description containing all the reservations.
     *
     * @param aID the ID
     * @example for Constructor overload.
     *          <p/>
     *          public GT_MetaTileEntity_EBench(int aID, String mName, String mNameRegional) { super(aID, mName,
     *          mNameRegional); }
     */
    public MetaTileEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        if (GregTech_API.sPostloadStarted || !GregTech_API.sPreloadStarted)
            throw new IllegalAccessError("This Constructor has to be called in the load Phase");
        if (GregTech_API.METATILEENTITIES[aID] == null) {
            GregTech_API.METATILEENTITIES[aID] = this;
        } else {
            throw new IllegalArgumentException("MetaMachine-Slot Nr. " + aID + " is already occupied!");
        }
        mName = aBasicName.replace(" ", "_")
            .toLowerCase(Locale.ENGLISH);
        setBaseMetaTileEntity(GregTech_API.constructBaseMetaTileEntity());
        getBaseMetaTileEntity().setMetaTileID((short) aID);
        GT_LanguageManager.addStringLocalization("gt.blockmachines." + mName + ".name", aRegionalName);
        mInventory = new ItemStack[aInvSlotCount];
        inventoryHandler = new ItemStackHandler(mInventory);
    }

    /**
     * This is the normal Constructor.
     */
    public MetaTileEntity(String aName, int aInvSlotCount) {
        mInventory = new ItemStack[aInvSlotCount];
        mName = aName;
        inventoryHandler = new ItemStackHandler(mInventory);
        colorOverride = GT_GUIColorOverride.get(getGUITextureSet().getMainBackground().location);
    }

    /**
     * This method will only be called on client side
     *
     * @return whether the secondary description should be display. default is false
     */
    @Deprecated
    public boolean isDisplaySecondaryDescription() {
        return false;
    }

    @Override
    public IGregTechTileEntity getBaseMetaTileEntity() {
        return mBaseMetaTileEntity;
    }

    @Override
    public void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mBaseMetaTileEntity != null && aBaseMetaTileEntity == null) {
            mBaseMetaTileEntity.getMetaTileEntity()
                .inValidate();
            mBaseMetaTileEntity.setMetaTileEntity(null);
        }
        mBaseMetaTileEntity = aBaseMetaTileEntity;
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.setMetaTileEntity(this);
        }
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        return new ItemStack(GregTech_API.sBlockMachines, (int) aAmount, getBaseMetaTileEntity().getMetaTileID());
    }

    @Override
    public String getLocalName() {
        return GT_LanguageManager.getTranslation("gt.blockmachines." + mName + ".name");
    }

    @Override
    public void onServerStart() {
        /* Do nothing */
    }

    @Override
    public void onWorldSave(File aSaveDirectory) {
        /* Do nothing */
    }

    @Override
    public void onWorldLoad(File aSaveDirectory) {
        /* Do nothing */
    }

    @Override
    public void onConfigLoad(GT_Config aConfig) {
        /* Do nothing */
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        /* Do nothing */
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection aSide, GT_ItemStack aStack) {
        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        /* Do nothing */
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection sideDirection, ForgeDirection wrenchingSideDirection, EntityPlayer entityPlayer,
                                      float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isValidFacing(wrenchingSideDirection)) {
            getBaseMetaTileEntity().setFrontFacing(wrenchingSideDirection);
            return true;
        }
        return false;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection sideDirection, ForgeDirection wrenchingSideDirection, EntityPlayer entityPlayer,
                                          float aX, float aY, float aZ) {
        if (!entityPlayer.isSneaking()) return false;
        ForgeDirection oppositeSide = wrenchingSideDirection.getOpposite();
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(wrenchingSideDirection);
        if ((tTileEntity instanceof IGregTechTileEntity)
            && (((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable)) {
            // The tile entity we're facing is a cable, let's try to connect to it
            return ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()
                .onWireCutterRightClick(wrenchingSideDirection, oppositeSide, entityPlayer, aX, aY, aZ);
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection sideDirection, ForgeDirection wrenchingSideDirection, EntityPlayer entityPlayer,
                                             float aX, float aY, float aZ) {
        if (!entityPlayer.isSneaking()) return false;
        ForgeDirection oppositeSide = wrenchingSideDirection.getOpposite();
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(wrenchingSideDirection);
        if ((tTileEntity instanceof IGregTechTileEntity)
            && (((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable)) {
            // The tile entity we're facing is a cable, let's try to connect to it
            return ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()
                .onSolderingToolRightClick(wrenchingSideDirection, oppositeSide, entityPlayer, aX, aY, aZ);
        }
        return false;
    }

    @Override
    public void onExplosion() {
        GT_Log.exp.println(
            "Machine at " + this.getBaseMetaTileEntity()
                .getXCoord()
                + " | "
                + this.getBaseMetaTileEntity()
                    .getYCoord()
                + " | "
                + this.getBaseMetaTileEntity()
                    .getZCoord()
                + " DIMID: "
                + this.getBaseMetaTileEntity()
                    .getWorld().provider.dimensionId
                + " exploded.");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        /* Do nothing */
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        /* Do nothing */
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            /*
             * Client tick counter that is set to 5 on hiding pipes and covers. It triggers a texture update next client
             * tick when reaching 4, with provision for 3 more update tasks, spreading client change detection related
             * work and network traffic on different ticks, until it reaches 0.
             */
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public void inValidate() {
        /* Do nothing */
    }

    @Override
    public void onRemoval() {
        /* Do nothing */
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    /**
     * When a GUI is opened
     */
    public void onOpenGUI() {
        /* Do nothing */
    }

    /**
     * When a GUI is closed
     */
    public void onCloseGUI() {
        /* Do nothing */
    }

    /**
     * a Player rightclicks the Machine Sneaky rightclicks are not getting passed to this!
     */
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection aSide,
        float aX, float aY, float aZ) {
        return onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        /* Do nothing */
    }

    @Override
    public void onValueUpdate(byte aValue) {
        /* Do nothing */
    }

    @Override
    public byte getUpdateData() {
        return 0;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        /* Do nothing */
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        /* Do nothing */
    }

    @Override
    public void stopSoundLoop(byte aValue, double aX, double aY, double aZ) {
        /* Do nothing */
    }

    @Override
    public MetaTileEntity getCallbackBase() {
        return mCallBackTile;
    }

    @Override
    public void setCallbackBase(MetaTileEntity callback) {
        this.mCallBackTile = callback;
    }

    @Override
    public Class<?> getType() {
        return null;
    }

    @Override
    public final void sendSound(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.DO_SOUND, aIndex);
    }

    @Override
    public final void sendLoopStart(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.START_SOUND_LOOP, aIndex);
        mSoundRequests++;
    }

    @Override
    public final void sendLoopEnd(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.STOP_SOUND_LOOP, aIndex);
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isElectric() {
        return true;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isPneumatic() {
        return false;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isSteampowered() {
        return false;
    }

    /**
     * @return what type of texture does this machine use for GUI, i.e. Bronze, Steel, or Primitive
     */
    public SteamVariant getSteamVariant() {
        return SteamVariant.NONE;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isEnetOutput() {
        return false;
    }

    /**
     * @return true if this Device consumes Energy at all
     */
    public boolean isEnetInput() {
        return false;
    }

    /**
     * @return the amount of EU, which can be stored in this Device. Default is 0 EU.
     */
    public long maxEUStore() {
        return 0;
    }

    /**
     * @return the amount of EU/t, which can be accepted by this Device before it explodes.
     */
    public long maxEUInput() {
        return 0;
    }

    /**
     * @return the amount of EU/t, which can be outputted by this Device.
     */
    public long maxEUOutput() {
        return 0;
    }

    /**
     * @return the amount of E-net Impulses of the maxEUOutput size, which can be outputted by this Device. Default is 1
     *         Pulse, this shouldn't be set to smaller Values than 1, as it won't output anything in that Case!
     */
    public long maxAmperesOut() {
        return 1;
    }

    /**
     * How many Amperes this Block can suck at max. Surpassing this value won't blow it up.
     */
    public long maxAmperesIn() {
        return 1;
    }

    /**
     * @return true if that Side is an Output.
     */
    public boolean isOutputFacing(ForgeDirection aSide) {
        return false;
    }

    /**
     * @return true if that Side is an Input.
     */
    public boolean isInputFacing(ForgeDirection aSide) {
        return false;
    }

    /**
     * @return true if Transformer Upgrades increase Packet Amount.
     */
    public boolean isTransformingLowEnergy() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facingDirection) {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return true;
    }

    @Override
    public boolean setStackToZeroInsteadOfNull(int aIndex) {
        return false;
    }

    /**
     * This is used to get the internal Energy. I use this for the IDSU.
     */
    public long getEUVar() {
        return ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy;
    }

    /**
     * This is used to set the internal Energy to the given Parameter. I use this for the IDSU.
     */
    public void setEUVar(long aEnergy) {
        if (aEnergy != ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy) {
            markDirty();
            ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy = aEnergy;
        }
    }

    /**
     * This is used to get the internal Steam Energy.
     */
    public long getSteamVar() {
        return ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam;
    }

    /**
     * This is used to set the internal Steam Energy to the given Parameter.
     */
    public void setSteamVar(long aSteam) {
        if (((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam != aSteam) {
            markDirty();
            ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam = aSteam;
        }
    }

    /**
     * @return the amount of Steam, which can be stored in this Device. Default is 0 EU.
     */
    public long maxSteamStore() {
        return 0;
    }

    /**
     * @return the amount of EU, which this Device stores before starting to emit Energy. useful if you don't want to
     *         emit stored Energy until a certain Level is reached.
     */
    public long getMinimumStoredEU() {
        return 512;
    }

    /**
     * Determines the Tier of the Machine, used for de-charging Tools.
     */
    public long getInputTier() {
        return GT_Utility.getTier(getBaseMetaTileEntity().getInputVoltage());
    }

    /**
     * Determines the Tier of the Machine, used for charging Tools.
     */
    public long getOutputTier() {
        return GT_Utility.getTier(getBaseMetaTileEntity().getOutputVoltage());
    }

    /**
     * gets the first RechargerSlot
     */
    public int rechargerSlotStartIndex() {
        return 0;
    }

    /**
     * gets the amount of RechargerSlots
     */
    public int rechargerSlotCount() {
        return 0;
    }

    /**
     * gets the first DechargerSlot
     */
    public int dechargerSlotStartIndex() {
        return 0;
    }

    /**
     * gets the amount of DechargerSlots
     */
    public int dechargerSlotCount() {
        return 0;
    }

    /**
     * gets if this is protected from other Players per default or not
     */
    public boolean ownerControl() {
        return false;
    }

    @Override
    public ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer,
        int aLogLevel, ArrayList<String> aList) {
        return aList;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection aSide) {
        return true;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection aSide) {
        return true;
    }

    /**
     * gets the contained Liquid
     */
    @Override
    public FluidStack getFluid() {
        return null;
    }

    /**
     * tries to fill this Tank
     */
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    /**
     * tries to empty this Tank
     */
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    /**
     * Tank pressure
     */
    public int getTankPressure() {
        return 0;
    }

    /**
     * Liquid Capacity
     */
    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public void onMachineBlockUpdate() {
        /* Do nothing */
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        /* Do nothing */
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    /**
     * If this accepts up to 4 Overclockers
     */
    public boolean isOverclockerUpgradable() {
        return false;
    }

    /**
     * If this accepts Transformer Upgrades
     */
    public boolean isTransformerUpgradable() {
        return false;
    }

    /**
     * Progress this machine has already made
     */
    public int getProgresstime() {
        return 0;
    }

    /**
     * Progress this Machine has to do to produce something
     */
    public int maxProgresstime() {
        return 0;
    }

    /**
     * Increases the Progress, returns the overflown Progress.
     */
    public int increaseProgress(int aProgress) {
        return 0;
    }

    /**
     * If this TileEntity makes use of Sided Redstone behaviors. Determines only, if the Output Redstone Array is
     * getting filled with 0 for true, or 15 for false.
     */
    public boolean hasSidedRedstoneOutputBehavior() {
        return false;
    }

    /**
     * When the Facing gets changed.
     */
    public void onFacingChange() {
        /* Do nothing */
    }

    /**
     * if the IC2 Teleporter can drain from this.
     */
    public boolean isTeleporterCompatible() {
        return isEnetOutput() && getBaseMetaTileEntity().getOutputVoltage() >= 128
            && getBaseMetaTileEntity().getUniversalEnergyCapacity() >= 500000;
    }

    /**
     * Flag if this MTE will exploding when its raining
     *
     * @return True if this will explode in rain, else false
     */
    public boolean willExplodeInRain() {
        return true;
    }

    /**
     * Gets the Output for the comparator on the given Side
     */
    @Override
    public byte getComparatorValue(ForgeDirection aSide) {
        return 0;
    }

    @Override
    public boolean acceptsRotationalEnergy(ForgeDirection aSide) {
        return false;
    }

    @Override
    public boolean injectRotationalEnergy(ForgeDirection aSide, long aSpeed, long aEnergy) {
        return false;
    }

    @Override
    public String getSpecialVoltageToolTip() {
        return null;
    }

    @Override
    public boolean isGivingInformation() {
        return false;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {};
    }

    public boolean isDigitalChest() {
        return false;
    }

    public ItemStack[] getStoredItemData() {
        return null;
    }

    public void setItemCount(int aCount) {
        /* Do nothing */
    }

    public int getMaxItemCount() {
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return mInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (aIndex >= 0 && aIndex < mInventory.length) return mInventory[aIndex];
        return null;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        markDirty();
        if (this instanceof IConfigurationCircuitSupport ccs) {
            if (ccs.allowSelectCircuit() && aIndex == ccs.getCircuitSlot() && aStack != null) {
                mInventory[aIndex] = GT_Utility.copyAmount(0, aStack);
                return;
            }
        }
        if (aIndex >= 0 && aIndex < mInventory.length) mInventory[aIndex] = aStack;
    }

    @Override
    public String getInventoryName() {
        if (GregTech_API.METATILEENTITIES[getBaseMetaTileEntity().getMetaTileID()] != null)
            return GregTech_API.METATILEENTITIES[getBaseMetaTileEntity().getMetaTileID()].getMetaName();
        return "";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return getBaseMetaTileEntity().isValidSlot(aIndex);
    }

    @Override
    public ItemStack decrStackSize(int aIndex, int aAmount) {
        ItemStack tStack = getStackInSlot(aIndex), rStack = GT_Utility.copyOrNull(tStack);
        if (tStack != null) {
            if (tStack.stackSize <= aAmount) {
                if (setStackToZeroInsteadOfNull(aIndex)) {
                    tStack.stackSize = 0;
                    markDirty();
                } else setInventorySlotContents(aIndex, null);
            } else {
                rStack = tStack.splitStack(aAmount);
                markDirty();
                if (tStack.stackSize == 0 && !setStackToZeroInsteadOfNull(aIndex))
                    setInventorySlotContents(aIndex, null);
            }
        }
        return rStack;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final TIntList tList = new TIntArrayList();
        final IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        final CoverInfo tileCoverInfo = tTileEntity.getCoverInfoAtSide(ForgeDirection.getOrientation(ordinalSide));
        final boolean tSkip = tileCoverInfo.letsItemsIn(-2) || tileCoverInfo.letsItemsOut(-2);
        for (int i = 0; i < getSizeInventory(); i++) {
            if (isValidSlot(i) && (tSkip || tileCoverInfo.letsItemsOut(i) || tileCoverInfo.letsItemsIn(i))) {
                tList.add(i);
            }
        }
        return tList.toArray();
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return isValidSlot(aIndex) && aStack != null
            && aIndex < mInventory.length
            && (mInventory[aIndex] == null || GT_Utility.areStacksEqual(aStack, mInventory[aIndex]))
            && allowPutStack(getBaseMetaTileEntity(), aIndex, ForgeDirection.getOrientation(ordinalSide), aStack);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return isValidSlot(aIndex) && aStack != null
            && aIndex < mInventory.length
            && allowPullStack(getBaseMetaTileEntity(), aIndex, ForgeDirection.getOrientation(ordinalSide), aStack);
    }

    @Override
    public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
        return fill(aSide, new FluidStack(aFluid, 1), false) == 1;
    }

    @Override
    public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
        return drain(aSide, new FluidStack(aFluid, 1), false) != null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[] {};
        return new FluidTankInfo[] { getInfo() };
    }

    public int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        markDirty();
        return fill(aFluid, doFill);
    }

    @Override
    public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        if (getBaseMetaTileEntity().hasSteamEngineUpgrade() && GT_ModHandler.isSteam(aFluid) && aFluid.amount > 1) {
            int tSteam = (int) Math.min(
                Integer.MAX_VALUE,
                Math.min(
                    aFluid.amount / 2,
                    getBaseMetaTileEntity().getSteamCapacity() - getBaseMetaTileEntity().getStoredSteam()));
            if (tSteam > 0) {
                markDirty();
                if (doFill) getBaseMetaTileEntity().increaseStoredSteam(tSteam, true);
                return tSteam * 2;
            }
        } else {
            return fill_default(aSide, aFluid, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
        if (getFluid() != null && aFluid != null && getFluid().isFluidEqual(aFluid))
            return drain(aFluid.amount, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
        return drain(maxDrain, doDrain);
    }

    @Override
    public int getFluidAmount() {
        return 0;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public String getMetaName() {
        return mName;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean doTickProfilingMessageDuringThisTick() {
        return doTickProfilingInThisTick;
    }

    @Override
    public void markDirty() {
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return false;
    }

    @Override
    public void openInventory() {
        //
    }

    @Override
    public void closeInventory() {
        //
    }

    /**
     * @deprecated Use ModularUI
     */
    @Deprecated
    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    /**
     * @deprecated Use ModularUI
     */
    @Deprecated
    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public boolean connectsToItemPipe(ForgeDirection aSide) {
        return false;
    }

    @Override
    public float getExplosionResistance(ForgeDirection aSide) {
        return 10.0F;
    }

    @Override
    public ItemStack[] getRealInventory() {
        return mInventory;
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        final IGregTechTileEntity meta = getBaseMetaTileEntity();
        final int aX = meta.getXCoord(), aY = meta.getYCoord(), aZ = meta.getZCoord();
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            // Flag surrounding pipes/cables to revaluate their connection with us if we got painted
            final TileEntity tTileEntity = meta.getTileEntityAtSide(side);
            if ((tTileEntity instanceof BaseMetaPipeEntity)) {
                ((BaseMetaPipeEntity) tTileEntity).onNeighborBlockChange(aX, aY, aZ);
            }
        }
    }

    @Override
    public void onColorChangeClient(byte aColor) {
        // Do nothing apparently
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInInventory(Block aBlock, int aMeta, RenderBlocks aRenderer) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInWorld(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
        return false;
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        float tStrength = GT_Values.getExplosionPowerForVoltage(aExplosionPower);
        final int tX = getBaseMetaTileEntity().getXCoord();
        final int tY = getBaseMetaTileEntity().getYCoord();
        final int tZ = getBaseMetaTileEntity().getZCoord();
        final World tWorld = getBaseMetaTileEntity().getWorld();
        GT_Utility.sendSoundToPlayers(tWorld, SoundResource.IC2_MACHINES_MACHINE_OVERLOAD, 1.0F, -1, tX, tY, tZ);
        tWorld.setBlock(tX, tY, tZ, Blocks.air);
        if (GregTech_API.sMachineExplosions)
            tWorld.createExplosion(null, tX + 0.5, tY + 0.5, tZ + 0.5, tStrength, true);
    }

    @Override
    public int getLightOpacity() {
        return ((BaseMetaTileEntity) getBaseMetaTileEntity()).getLightValue() > 0 ? 0 : 255;
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        AxisAlignedBB axisalignedbb1 = getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
        if (axisalignedbb1 != null && inputAABB.intersectsWith(axisalignedbb1)) outputAABB.add(axisalignedbb1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider) {
        //
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        //
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return false;
    }

    @Deprecated
    public String trans(String aKey, String aEnglish) {
        return GT_Utility.trans(aKey, aEnglish);
    }

    @Override
    public boolean hasAlternativeModeText() {
        return false;
    }

    @Override
    public String getAlternativeModeText() {
        return "";
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    public boolean shouldTriggerBlockUpdate() {
        return false;
    }

    // === AE2 compat ===

    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.NONE;
    }

    public AENetworkProxy getProxy() {
        return null;
    }

    public void gridChanged() {}

    // === Waila compat ===

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        currenttip.add(
            String.format(
                "Facing: %s",
                mBaseMetaTileEntity.getFrontFacing()
                    .name()));

        if (this instanceof IPowerChannelState state) {
            // adapted from PowerStateWailaDataProvider
            NBTTagCompound tag = accessor.getNBTData();
            final boolean isActive = tag.getBoolean("isActive");
            final boolean isPowered = tag.getBoolean("isPowered");
            final boolean isBooting = tag.getBoolean("isBooting");

            if (isBooting) {
                currenttip.add(WailaText.Booting.getLocal());
            } else if (isActive && isPowered) {
                currenttip.add(WailaText.DeviceOnline.getLocal());
            } else if (isPowered) {
                currenttip.add(WailaText.DeviceMissingChannel.getLocal());
            } else {
                currenttip.add(WailaText.DeviceOffline.getLocal());
            }
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        if (this instanceof IPowerChannelState state) {
            // adapted from PowerStateWailaDataProvider
            final boolean isActive = state.isActive();
            final boolean isPowered = state.isPowered();
            final boolean isBooting = state.isBooting();
            tag.setBoolean("isActive", isActive);
            tag.setBoolean("isPowered", isPowered);
            tag.setBoolean("isBooting", isBooting);
        }
    }

    protected String getAEDiagnostics() {
        try {
            if (getProxy() == null) return "(proxy)";
            if (getProxy().getNode() == null) return "(node)";
            if (getProxy().getNode()
                .getGrid() == null) return "(grid)";
            if (!getProxy().getNode()
                .meetsChannelRequirements()) return "(channels)";
            IPathingGrid pg = getProxy().getNode()
                .getGrid()
                .getCache(IPathingGrid.class);
            if (!pg.isNetworkBooting()) return "(booting)";
            IEnergyGrid eg = getProxy().getNode()
                .getGrid()
                .getCache(IEnergyGrid.class);
            if (!eg.isNetworkPowered()) return "(power)";
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.DEFAULT;
    }

    @Override
    public int getGUIColorization() {
        Dyes dye = Dyes.dyeWhite;
        if (this.colorOverride.sLoaded()) {
            if (this.colorOverride.sGuiTintingEnabled() && getBaseMetaTileEntity() != null) {
                dye = Dyes.getDyeFromIndex(getBaseMetaTileEntity().getColorization());
                return this.colorOverride.getGuiTintOrDefault(dye.mName, GT_Util.getRGBInt(dye.getRGBA()));
            }
        } else if (GregTech_API.sColoredGUI) {
            if (GregTech_API.sMachineMetalGUI) {
                dye = Dyes.MACHINE_METAL;
            } else if (getBaseMetaTileEntity() != null) {
                dye = Dyes.getDyeFromIndex(getBaseMetaTileEntity().getColorization());
            }
        }
        return GT_Util.getRGBInt(dye.getRGBA());
    }

    @Override
    public int getTextColorOrDefault(String textType, int defaultColor) {
        return colorOverride.getTextColorOrDefault(textType, defaultColor);
    }

    protected Supplier<Integer> COLOR_TITLE = () -> getTextColorOrDefault("title", 0x404040);
    protected Supplier<Integer> COLOR_TITLE_WHITE = () -> getTextColorOrDefault("title_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_WHITE = () -> getTextColorOrDefault("text_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_GRAY = () -> getTextColorOrDefault("text_gray", 0x404040);
    protected Supplier<Integer> COLOR_TEXT_RED = () -> getTextColorOrDefault("text_red", 0xff0000);
}
