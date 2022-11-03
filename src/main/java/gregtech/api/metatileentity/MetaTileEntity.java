package gregtech.api.metatileentity;

import static gregtech.api.enums.GT_Values.V;

import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.pathing.IPathingGrid;
import appeng.api.util.AECableType;
import appeng.me.helpers.AENetworkProxy;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.Column;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.metatileentity.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMachineCallback;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.net.GT_Packet_SetConfigurationCircuit;
import gregtech.api.net.GT_Packet_TileEntityCoverGUI;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_TooltipDataCache;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
import gregtech.common.gui.modularui.uifactory.SelectItemUIFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaMachine
 * Call the Constructor with the desired ID at the load-phase (not preload and also not postload!)
 * Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it.
 * "new GT_MetaTileEntity_E_Furnace(54, "GT_E_Furnace", "Automatic E-Furnace");"
 */
@SuppressWarnings("unused")
public abstract class MetaTileEntity implements IMetaTileEntity, IMachineCallback<MetaTileEntity> {
    /**
     * Only assigned for the MetaTileEntity in the List! Also only used to get the localized Name for the ItemStack and for getInvName.
     */
    public final String mName;
    /**
     * The Inventory of the MetaTileEntity. Amount of Slots can be larger than 256. HAYO!
     */
    public final ItemStack[] mInventory;

    public boolean doTickProfilingInThisTick = true;

    private MetaTileEntity mCallBackTile;

    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

    public long mSoundRequests = 0;

    /**
     * This registers your Machine at the List.
     * Use only ID's larger than 2048, because i reserved these ones.
     * See also the List in the API, as it has a Description containing all the reservations.
     *
     * @param aID the ID
     * @example for Constructor overload.
     * <p/>
     * public GT_MetaTileEntity_EBench(int aID, String mName, String mNameRegional) {
     * super(aID, mName, mNameRegional);
     * }
     */
    public MetaTileEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        if (GregTech_API.sPostloadStarted || !GregTech_API.sPreloadStarted)
            throw new IllegalAccessError("This Constructor has to be called in the load Phase");
        if (GregTech_API.METATILEENTITIES[aID] == null) {
            GregTech_API.METATILEENTITIES[aID] = this;
        } else {
            throw new IllegalArgumentException("MetaMachine-Slot Nr. " + aID + " is already occupied!");
        }
        mName = aBasicName.replace(" ", "_").toLowerCase(Locale.ENGLISH);
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
        colorOverride = new GT_GUIColorOverride(getBackground().location.getResourcePath());
    }

    /**
     * This method will only be called on client side
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
            mBaseMetaTileEntity.getMetaTileEntity().inValidate();
            mBaseMetaTileEntity.setMetaTileEntity(null);
        }
        mBaseMetaTileEntity = aBaseMetaTileEntity;
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.setMetaTileEntity(this);
        }
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        return new ItemStack(
                GregTech_API.sBlockMachines,
                (int) aAmount,
                getBaseMetaTileEntity().getMetaTileID());
    }

    public String getLocalName() {
        return GT_LanguageManager.getTranslation("gt.blockmachines." + mName + ".name");
    }

    @Override
    public void onServerStart() {
        /*Do nothing*/
    }

    @Override
    public void onWorldSave(File aSaveDirectory) {
        /*Do nothing*/
    }

    @Override
    public void onWorldLoad(File aSaveDirectory) {
        /*Do nothing*/
    }

    @Override
    public void onConfigLoad(GT_Config aConfig) {
        /*Do nothing*/
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        /*Do nothing*/
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        /*Do nothing*/
    }

    @Override
    public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
        return true;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        /*Do nothing*/
    }

    @Override
    public boolean onWrenchRightClick(
            byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isValidFacing(aWrenchingSide)) {
            getBaseMetaTileEntity().setFrontFacing(aWrenchingSide);
            return true;
        }
        return false;
    }

    @Override
    public boolean onWireCutterRightClick(
            byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!aPlayer.isSneaking()) return false;
        byte tSide = GT_Utility.getOppositeSide(aWrenchingSide);
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(aWrenchingSide);
        if ((tTileEntity instanceof IGregTechTileEntity)
                && (((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable)) {
            // The tile entity we're facing is a cable, let's try to connect to it
            return ((IGregTechTileEntity) tTileEntity)
                    .getMetaTileEntity()
                    .onWireCutterRightClick(aWrenchingSide, tSide, aPlayer, aX, aY, aZ);
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(
            byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!aPlayer.isSneaking()) return false;
        byte tSide = GT_Utility.getOppositeSide(aWrenchingSide);
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(aWrenchingSide);
        if ((tTileEntity instanceof IGregTechTileEntity)
                && (((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable)) {
            // The tile entity we're facing is a cable, let's try to connect to it
            return ((IGregTechTileEntity) tTileEntity)
                    .getMetaTileEntity()
                    .onSolderingToolRightClick(aWrenchingSide, tSide, aPlayer, aX, aY, aZ);
        }
        return false;
    }

    @Override
    public void onExplosion() {
        GT_Log.exp.println("Machine at " + this.getBaseMetaTileEntity().getXCoord() + " | "
                + this.getBaseMetaTileEntity().getYCoord() + " | "
                + this.getBaseMetaTileEntity().getZCoord() + " DIMID: "
                + this.getBaseMetaTileEntity().getWorld().provider.dimensionId + " exploded.");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        /*Do nothing*/
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        /*Do nothing*/
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            /* Client tick counter that is set to 5 on hiding pipes and covers.
             * It triggers a texture update next client tick when reaching 4, with provision for 3 more update tasks,
             * spreading client change detection related work and network traffic on different ticks, until it reaches 0.
             */
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public void inValidate() {
        /*Do nothing*/
    }

    @Override
    public void onRemoval() {
        /*Do nothing*/
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        /*Do nothing*/
    }

    /**
     * When a GUI is opened
     */
    public void onOpenGUI() {
        /*Do nothing*/
    }

    /**
     * When a GUI is closed
     */
    public void onCloseGUI() {
        /*Do nothing*/
    }

    /**
     * a Player rightclicks the Machine
     * Sneaky rightclicks are not getting passed to this!
     */
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean onRightclick(
            IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        return onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        /*Do nothing*/
    }

    @Override
    public void onValueUpdate(byte aValue) {
        /*Do nothing*/
    }

    @Override
    public byte getUpdateData() {
        return 0;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        /*Do nothing*/
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        /*Do nothing*/
    }

    @Override
    public void stopSoundLoop(byte aValue, double aX, double aY, double aZ) {
        /*Do nothing*/
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
     * @return the amount of E-net Impulses of the maxEUOutput size, which can be outputted by this Device.
     * Default is 1 Pulse, this shouldn't be set to smaller Values than 1, as it won't output anything in that Case!
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
    public boolean isOutputFacing(byte aSide) {
        return false;
    }

    /**
     * @return true if that Side is an Input.
     */
    public boolean isInputFacing(byte aSide) {
        return false;
    }

    /**
     * @return true if Transformer Upgrades increase Packet Amount.
     */
    public boolean isTransformingLowEnergy() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
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
     * @return the amount of EU, which this Device stores before starting to emit Energy.
     * useful if you don't want to emit stored Energy until a certain Level is reached.
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
    public ArrayList<String> getSpecialDebugInfo(
            IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, int aLogLevel, ArrayList<String> aList) {
        return aList;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return true;
    }

    @Override
    public boolean isLiquidOutput(byte aSide) {
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
        /*Do nothing*/
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        /*Do nothing*/
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
     * If this TileEntity makes use of Sided Redstone behaviors.
     * Determines only, if the Output Redstone Array is getting filled with 0 for true, or 15 for false.
     */
    public boolean hasSidedRedstoneOutputBehavior() {
        return false;
    }

    /**
     * When the Facing gets changed.
     */
    public void onFacingChange() {
        /*Do nothing*/
    }

    /**
     * if the IC2 Teleporter can drain from this.
     */
    public boolean isTeleporterCompatible() {
        return isEnetOutput()
                && getBaseMetaTileEntity().getOutputVoltage() >= 128
                && getBaseMetaTileEntity().getUniversalEnergyCapacity() >= 500000;
    }

    /**
     * Gets the Output for the comparator on the given Side
     */
    @Override
    public byte getComparatorValue(byte aSide) {
        return 0;
    }

    @Override
    public boolean acceptsRotationalEnergy(byte aSide) {
        return false;
    }

    @Override
    public boolean injectRotationalEnergy(byte aSide, long aSpeed, long aEnergy) {
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
        /*Do nothing*/
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
        if (this instanceof IConfigurationCircuitSupport) {
            IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport) this;
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
    public int[] getAccessibleSlotsFromSide(int aSide) {
        TIntList tList = new TIntArrayList();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        boolean tSkip = tTileEntity
                        .getCoverBehaviorAtSideNew((byte) aSide)
                        .letsItemsIn(
                                (byte) aSide,
                                tTileEntity.getCoverIDAtSide((byte) aSide),
                                tTileEntity.getComplexCoverDataAtSide((byte) aSide),
                                -2,
                                tTileEntity)
                || tTileEntity
                        .getCoverBehaviorAtSideNew((byte) aSide)
                        .letsItemsOut(
                                (byte) aSide,
                                tTileEntity.getCoverIDAtSide((byte) aSide),
                                tTileEntity.getComplexCoverDataAtSide((byte) aSide),
                                -2,
                                tTileEntity);
        for (int i = 0; i < getSizeInventory(); i++)
            if (isValidSlot(i)
                    && (tSkip
                            || tTileEntity
                                    .getCoverBehaviorAtSideNew((byte) aSide)
                                    .letsItemsOut(
                                            (byte) aSide,
                                            tTileEntity.getCoverIDAtSide((byte) aSide),
                                            tTileEntity.getComplexCoverDataAtSide((byte) aSide),
                                            i,
                                            tTileEntity)
                            || tTileEntity
                                    .getCoverBehaviorAtSideNew((byte) aSide)
                                    .letsItemsIn(
                                            (byte) aSide,
                                            tTileEntity.getCoverIDAtSide((byte) aSide),
                                            tTileEntity.getComplexCoverDataAtSide((byte) aSide),
                                            i,
                                            tTileEntity))) tList.add(i);
        return tList.toArray();
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
        return isValidSlot(aIndex)
                && aStack != null
                && aIndex < mInventory.length
                && (mInventory[aIndex] == null || GT_Utility.areStacksEqual(aStack, mInventory[aIndex]))
                && allowPutStack(getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return isValidSlot(aIndex)
                && aStack != null
                && aIndex < mInventory.length
                && allowPullStack(getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
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
        return new FluidTankInfo[] {getInfo()};
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
                            getBaseMetaTileEntity().getSteamCapacity()
                                    - getBaseMetaTileEntity().getStoredSteam()));
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
     * @deprecated Use {@link #createWindow}
     */
    @Deprecated
    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    /**
     * @deprecated Use {@link #createWindow}
     */
    @Deprecated
    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public boolean connectsToItemPipe(byte aSide) {
        return false;
    }

    @Override
    public float getExplosionResistance(byte aSide) {
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
        for (byte aSide = 0; aSide < 6; aSide++) {
            // Flag surrounding pipes/cables to revaluate their connection with us if we got painted
            final TileEntity tTileEntity = meta.getTileEntityAtSide(aSide);
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
        float tStrength = aExplosionPower < V[0]
                ? 1.0F
                : aExplosionPower < V[1]
                        ? 2.0F
                        : aExplosionPower < V[2]
                                ? 3.0F
                                : aExplosionPower < V[3]
                                        ? 4.0F
                                        : aExplosionPower < V[4]
                                                ? 5.0F
                                                : aExplosionPower < V[4] * 2
                                                        ? 6.0F
                                                        : aExplosionPower < V[5]
                                                                ? 7.0F
                                                                : aExplosionPower < V[6]
                                                                        ? 8.0F
                                                                        : aExplosionPower < V[7]
                                                                                ? 9.0F
                                                                                : aExplosionPower < V[8]
                                                                                        ? 10.0F
                                                                                        : aExplosionPower < V[8] * 2
                                                                                                ? 11.0F
                                                                                                : aExplosionPower < V[9]
                                                                                                        ? 12.0F
                                                                                                        : aExplosionPower
                                                                                                                        < V[
                                                                                                                                10]
                                                                                                                ? 13.0F
                                                                                                                : aExplosionPower
                                                                                                                                < V[
                                                                                                                                        11]
                                                                                                                        ? 14.0F
                                                                                                                        : aExplosionPower
                                                                                                                                        < V[
                                                                                                                                                12]
                                                                                                                                ? 15.0F
                                                                                                                                : aExplosionPower
                                                                                                                                                < V[
                                                                                                                                                                12]
                                                                                                                                                        * 2
                                                                                                                                        ? 16.0F
                                                                                                                                        : aExplosionPower
                                                                                                                                                        < V[
                                                                                                                                                                13]
                                                                                                                                                ? 17.0F
                                                                                                                                                : aExplosionPower
                                                                                                                                                                < V[
                                                                                                                                                                        14]
                                                                                                                                                        ? 18.0F
                                                                                                                                                        : aExplosionPower
                                                                                                                                                                        < V[
                                                                                                                                                                                15]
                                                                                                                                                                ? 19.0F
                                                                                                                                                                : 20.0F;
        int tX = getBaseMetaTileEntity().getXCoord(),
                tY = getBaseMetaTileEntity().getYCoord(),
                tZ = getBaseMetaTileEntity().getZCoord();
        World tWorld = getBaseMetaTileEntity().getWorld();
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
    public void addCollisionBoxesToList(
            World aWorld,
            int aX,
            int aY,
            int aZ,
            AxisAlignedBB inputAABB,
            List<AxisAlignedBB> outputAABB,
            Entity collider) {
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

    // === GUI stuff ===

    /**
     * Inventory wrapper for ModularUI
     */
    protected final ItemStackHandler inventoryHandler;

    protected GT_TooltipDataCache mTooltipCache = new GT_TooltipDataCache();
    protected GT_GUIColorOverride colorOverride;

    // Tooltip localization keys
    protected static final String BATTERY_SLOT_TOOLTIP = "GT5U.machines.battery_slot.tooltip",
            BATTERY_SLOT_TOOLTIP_ALT = "GT5U.machines.battery_slot.tooltip.alternative",
            UNUSED_SLOT_TOOLTIP = "GT5U.machines.unused_slot.tooltip",
            SPECIAL_SLOT_TOOLTIP = "GT5U.machines.special_slot.tooltip",
            FLUID_INPUT_TOOLTIP = "GT5U.machines.fluid_input_slot.tooltip",
            FLUID_OUTPUT_TOOLTIP = "GT5U.machines.fluid_output_slot.tooltip",
            STALLED_STUTTERING_TOOLTIP = "GT5U.machines.stalled_stuttering.tooltip",
            STALLED_VENT_TOOLTIP = "GT5U.machines.stalled_vent.tooltip",
            FLUID_TRANSFER_TOOLTIP = "GT5U.machines.fluid_transfer.tooltip",
            ITEM_TRANSFER_TOOLTIP = "GT5U.machines.item_transfer.tooltip",
            POWER_SOURCE_KEY = "GT5U.machines.powersource.";

    protected static final int TOOLTIP_DELAY = 5;

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext) {
        buildContext.setValidator(() ->
                getBaseMetaTileEntity() != null && !getBaseMetaTileEntity().isDead());
        ModularWindow.Builder builder = ModularWindow.builder(getGUIWidth(), getGUIHeight());
        builder.setBackground(getBackground());
        builder.setGuiTint(getGUIColorization());
        if (doesBindPlayerInventory()) {
            builder.bindPlayerInventory(buildContext.getPlayer(), 7, getSlotBackground());
        }
        addUIWidgets(builder, buildContext);
        addTitleToUI(builder);
        addCoverTabs(builder, buildContext);
        if (this instanceof IConfigurationCircuitSupport
                && ((IConfigurationCircuitSupport) this).allowSelectCircuit()) {
            addConfigurationCircuitSlot(builder);
        } else {
            addGregTechLogo(builder);
        }
        return builder.build();
    }

    /**
     * Override this to add {@link com.gtnewhorizons.modularui.api.widget.Widget}s for your UI.
     */
    protected void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {}

    protected void addTitleToUI(ModularWindow.Builder builder) {
        addTitleToUI(builder, getLocalName());
    }

    protected void addTitleToUI(ModularWindow.Builder builder, String title) {
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 2) {
            addTitleItemIconStyle(builder, title);
        } else {
            addTitleTextStyle(builder, title);
        }
    }

    protected void addTitleTextStyle(ModularWindow.Builder builder, String title) {
        final int TAB_PADDING = 3;
        final int TITLE_PADDING = 2;
        int titleWidth = 0, titleHeight = 0;
        if (NetworkUtils.isClient()) {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            //noinspection unchecked
            List<String> titleLines =
                    fontRenderer.listFormattedStringToWidth(title, getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
            titleWidth = titleLines.size() > 1
                    ? getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
                    : fontRenderer.getStringWidth(title);
            //noinspection PointlessArithmeticExpression
            titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1) * 1;
        }

        DrawableWidget tab = new DrawableWidget();
        TextWidget text = new TextWidget(title)
                .setDefaultColor(getTitleColor())
                .setTextAlignment(Alignment.CenterLeft)
                .setMaxWidth(titleWidth);
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 1) {
            tab.setDrawable(getTabIconSet().titleNormal)
                    .setPos(0, -(titleHeight + TAB_PADDING) + 1)
                    .setSize(getGUIWidth(), titleHeight + TAB_PADDING * 2);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            tab.setDrawable(getTabIconSet().titleDark)
                    .setPos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                    .setSize(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        builder.widget(tab).widget(text);
    }

    protected void addTitleItemIconStyle(ModularWindow.Builder builder, String title) {
        builder.widget(new MultiChildWidget()
                .addChild(new DrawableWidget()
                        .setDrawable(getTabIconSet().titleNormal)
                        .setPos(0, 0)
                        .setSize(24, 24))
                .addChild(new ItemDrawable(getStackForm(1)).asWidget().setPos(4, 4))
                .addTooltip(title)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(0, -24 + 3));
    }

    protected GT_GuiTabIconSet getTabIconSet() {
        return new GT_GuiTabIconSet(
                GT_UITextures.TAB_COVER_NORMAL,
                GT_UITextures.TAB_COVER_HIGHLIGHT,
                GT_UITextures.TAB_COVER_DISABLED,
                GT_UITextures.TAB_TITLE,
                GT_UITextures.TAB_TITLE_DARK);
    }

    protected int getTitleColor() {
        return COLOR_TITLE.get();
    }

    protected void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(new DrawableWidget()
                .setDrawable(getGregTechLogo())
                .setSize(17, 17)
                .setPos(152, 63));
    }

    protected IDrawable getGregTechLogo() {
        return GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT;
    }

    protected UITexture getBackground() {
        return GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT;
    }

    protected int getGUIWidth() {
        return 176;
    }

    protected int getGUIHeight() {
        return 166;
    }

    protected boolean doesBindPlayerInventory() {
        return true;
    }

    protected IDrawable getSlotBackground() {
        return ModularUITextures.ITEM_SLOT;
    }

    protected void add1by1Slot(ModularWindow.Builder builder, IDrawable... background) {
        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 1)
                .startFromSlot(0)
                .endAtSlot(0)
                .background(background)
                .build()
                .setPos(79, 34));
    }

    protected void add2by2Slots(ModularWindow.Builder builder, IDrawable... background) {
        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 2)
                .startFromSlot(0)
                .endAtSlot(3)
                .background(background)
                .build()
                .setPos(70, 25));
    }

    protected void add3by3Slots(ModularWindow.Builder builder, IDrawable... background) {
        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 3)
                .startFromSlot(0)
                .endAtSlot(8)
                .background(background)
                .build()
                .setPos(61, 16));
    }

    protected void add4by4Slots(ModularWindow.Builder builder, IDrawable... background) {
        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(15)
                .background(background)
                .build()
                .setPos(52, 7));
    }

    private static final int COVER_WINDOW_ID_START = 1;

    private void addCoverTabs(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final int COVER_TAB_LEFT = -16,
                COVER_TAB_TOP = 1,
                COVER_TAB_HEIGHT = 20,
                COVER_TAB_WIDTH = 18,
                COVER_TAB_SPACING = 2,
                ICON_SIZE = 16;
        final boolean flipHorizontally = GT_Mod.gregtechproxy.mCoverTabsFlipped;

        Column columnWidget = new Column();
        builder.widget(columnWidget);
        int xPos = flipHorizontally ? (getGUIWidth() - COVER_TAB_LEFT - COVER_TAB_WIDTH) : COVER_TAB_LEFT;
        if (GT_Mod.gregtechproxy.mCoverTabsVisible) {
            columnWidget.setPos(xPos, COVER_TAB_TOP);
        } else {
            columnWidget.setEnabled(false);
        }
        columnWidget.setAlignment(MainAxisAlignment.SPACE_BETWEEN).setSpace(COVER_TAB_SPACING);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            final byte side = (byte) direction.ordinal();
            buildContext.addSyncedWindow(side + COVER_WINDOW_ID_START, player -> createCoverWindow(player, side));
            columnWidget.addChild(new MultiChildWidget()
                    .addChild(
                            new ButtonWidget() {
                                @Override
                                public IDrawable[] getBackground() {
                                    List<IDrawable> backgrounds = new ArrayList<>();
                                    final GT_GuiTabIconSet tabIconSet = getTabIconSet();
                                    if (getBaseMetaTileEntity() == null) return new IDrawable[] {};

                                    if (getBaseMetaTileEntity()
                                            .getCoverBehaviorAtSideNew(side)
                                            .hasCoverGUI()) {
                                        if (isHovering()) {
                                            backgrounds.add(
                                                    flipHorizontally
                                                            ? tabIconSet.coverHighlightFlipped
                                                            : tabIconSet.coverHighlight);
                                        } else {
                                            backgrounds.add(
                                                    flipHorizontally
                                                            ? tabIconSet.coverNormalFlipped
                                                            : tabIconSet.coverNormal);
                                        }
                                    } else {
                                        backgrounds.add(
                                                flipHorizontally
                                                        ? tabIconSet.coverDisabledFlipped
                                                        : tabIconSet.coverDisabled);
                                    }
                                    return backgrounds.toArray(new IDrawable[] {});
                                }
                            }.setOnClick((clickData, widget) -> onTabClicked(clickData, widget, side))
                                    .dynamicTooltip(() -> getCoverTabTooltip(side))
                                    .setEnabled(widget -> {
                                        if (getBaseMetaTileEntity() == null) return false;
                                        return getBaseMetaTileEntity().getCoverItemAtSide(side) != null;
                                    })
                                    .setSize(COVER_TAB_WIDTH, COVER_TAB_HEIGHT))
                    .addChild(new ItemDrawable(() -> {
                                if (getBaseMetaTileEntity() == null) return null;
                                return getBaseMetaTileEntity().getCoverItemAtSide(side);
                            })
                            .asWidget()
                            .setPos(
                                    (COVER_TAB_WIDTH - ICON_SIZE) / 2 + (flipHorizontally ? -1 : 1),
                                    (COVER_TAB_HEIGHT - ICON_SIZE) / 2)));
        }
    }

    /**
     * Defines a set of textures a tab line can use to render its tab backgrounds
     */
    protected static class GT_GuiTabIconSet {
        protected final UITexture coverNormal;
        protected final UITexture coverHighlight;
        protected final UITexture coverDisabled;
        protected final UITexture coverNormalFlipped;
        protected final UITexture coverHighlightFlipped;
        protected final UITexture coverDisabledFlipped;
        protected final AdaptableUITexture titleNormal;
        protected final AdaptableUITexture titleDark;

        public GT_GuiTabIconSet(
                UITexture coverNormal,
                UITexture coverHighlight,
                UITexture coverDisabled,
                AdaptableUITexture titleNormal,
                AdaptableUITexture titleDark) {
            this.coverNormal = coverNormal;
            this.coverHighlight = coverHighlight;
            this.coverDisabled = coverDisabled;
            this.coverNormalFlipped = coverNormal.getFlipped(true, false);
            this.coverHighlightFlipped = coverHighlight.getFlipped(true, false);
            this.coverDisabledFlipped = coverDisabled.getFlipped(true, false);
            this.titleNormal = titleNormal;
            this.titleDark = titleDark;
        }
    }

    @SideOnly(Side.CLIENT)
    private List<String> getCoverTabTooltip(byte side) {
        final String[] SIDE_TOOLTIPS = new String[] {
            "GT5U.interface.coverTabs.down",
            "GT5U.interface.coverTabs.up",
            "GT5U.interface.coverTabs.north",
            "GT5U.interface.coverTabs.south",
            "GT5U.interface.coverTabs.west",
            "GT5U.interface.coverTabs.east"
        };
        if (getBaseMetaTileEntity() == null) return Collections.emptyList();
        final ItemStack coverItem = getBaseMetaTileEntity().getCoverItemAtSide(side);
        if (coverItem == null) return Collections.emptyList();
        boolean coverHasGUI =
                getBaseMetaTileEntity().getCoverBehaviorAtSideNew(side).hasCoverGUI();

        //noinspection unchecked
        List<String> tooltip = coverItem.getTooltip(Minecraft.getMinecraft().thePlayer, true);
        for (int i = 0; i < tooltip.size(); i++) {
            if (i == 0) {
                tooltip.set(
                        0,
                        (coverHasGUI ? EnumChatFormatting.UNDERLINE : EnumChatFormatting.DARK_GRAY)
                                + StatCollector.translateToLocal(SIDE_TOOLTIPS[side])
                                + (coverHasGUI ? EnumChatFormatting.RESET + ": " : ": " + EnumChatFormatting.RESET)
                                + tooltip.get(0));
            } else {
                tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
            }
        }
        return tooltip;
    }

    private void onTabClicked(Widget.ClickData clickData, Widget widget, byte side) {
        if (getBaseMetaTileEntity() == null || getBaseMetaTileEntity().isClientSide()) return;

        final GT_CoverBehaviorBase<?> coverBehavior = getBaseMetaTileEntity().getCoverBehaviorAtSideNew(side);
        if (coverBehavior.useModularUI()) {
            widget.getContext().openSyncedWindow(side + COVER_WINDOW_ID_START);
        } else {
            GT_Packet_TileEntityCoverGUI packet = new GT_Packet_TileEntityCoverGUI(
                    getBaseMetaTileEntity().getXCoord(),
                    getBaseMetaTileEntity().getYCoord(),
                    getBaseMetaTileEntity().getZCoord(),
                    side,
                    getBaseMetaTileEntity().getCoverIDAtSide(side),
                    getBaseMetaTileEntity().getComplexCoverDataAtSide(side),
                    getBaseMetaTileEntity().getWorld().provider.dimensionId,
                    widget.getContext().getPlayer().getEntityId(),
                    0);
            GT_Values.NW.sendToPlayer(
                    packet, (EntityPlayerMP) widget.getContext().getPlayer());
        }
    }

    private ModularWindow createCoverWindow(EntityPlayer player, byte side) {
        if (getBaseMetaTileEntity() == null) return null;
        GT_CoverBehaviorBase<?> coverBehavior = getBaseMetaTileEntity().getCoverBehaviorAtSideNew(side);
        GT_CoverUIBuildContext buildContext = new GT_CoverUIBuildContext(
                player, getBaseMetaTileEntity().getCoverIDAtSide(side), side, getBaseMetaTileEntity(), true);
        return coverBehavior.createWindow(buildContext);
    }

    private void addConfigurationCircuitSlot(ModularWindow.Builder builder) {
        IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport) this;
        AtomicBoolean dialogOpened = new AtomicBoolean(false);
        builder.widget(
                new SlotWidget(new BaseSlot(inventoryHandler, ccs.getCircuitSlot(), true)) {
                    @Override
                    protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                        ItemStack newCircuit;
                        if (clickData.shift) {
                            if (clickData.mouseButton == 0) {
                                if (NetworkUtils.isClient() && !dialogOpened.get()) {
                                    openSelectCircuitDialog(getContext(), dialogOpened);
                                }
                                return;
                            } else {
                                newCircuit = null;
                            }
                        } else {
                            List<ItemStack> tCircuits = ccs.getConfigurationCircuits();
                            int index = GT_Utility.findMatchingStackInList(tCircuits, cursorStack);
                            if (index < 0) {
                                int curIndex = GT_Utility.findMatchingStackInList(
                                                tCircuits, getStackInSlot(ccs.getCircuitSlot()))
                                        + 1;
                                if (clickData.mouseButton == 0) {
                                    curIndex += 1;
                                } else {
                                    curIndex -= 1;
                                }
                                curIndex = Math.floorMod(curIndex, tCircuits.size() + 1) - 1;
                                newCircuit = curIndex < 0 ? null : tCircuits.get(curIndex);
                            } else {
                                // set to whatever it is
                                newCircuit = tCircuits.get(index);
                            }
                        }
                        getBaseMetaTileEntity().setInventorySlotContents(ccs.getCircuitSlot(), newCircuit);
                    }

                    @Override
                    protected void phantomScroll(int direction) {
                        phantomClick(new ClickData(direction > 0 ? 1 : 0, false, false, false));
                    }

                    @Override
                    public List<String> getExtraTooltip() {
                        return Arrays.asList(
                                EnumChatFormatting.DARK_GRAY
                                        + EnumChatFormatting.getTextWithoutFormattingCodes(
                                                StatCollector.translateToLocal(
                                                        "GT5U.machines.select_circuit.tooltip.1")),
                                EnumChatFormatting.DARK_GRAY
                                        + EnumChatFormatting.getTextWithoutFormattingCodes(
                                                StatCollector.translateToLocal(
                                                        "GT5U.machines.select_circuit.tooltip.2")),
                                EnumChatFormatting.DARK_GRAY
                                        + EnumChatFormatting.getTextWithoutFormattingCodes(
                                                StatCollector.translateToLocal(
                                                        "GT5U.machines.select_circuit.tooltip.3")));
                    }
                }.setOverwriteItemStackTooltip(list -> {
                            list.removeIf(line ->
                                    line.contains(StatCollector.translateToLocal("gt.integrated_circuit.tooltip.0"))
                                            || line.contains(
                                                    StatCollector.translateToLocal("gt.integrated_circuit.tooltip.1")));
                            return list;
                        })
                        .disableShiftInsert()
                        .setHandlePhantomActionClient(true)
                        .setBackground(getSlotBackground(), GT_UITextures.OVERLAY_SLOT_INT_CIRCUIT)
                        .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.select_circuit.tooltip"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY)
                        .setPos(ccs.getCircuitSlotX() - 1, ccs.getCircuitSlotY() - 1));
    }

    private void openSelectCircuitDialog(ModularUIContext uiContext, AtomicBoolean dialogOpened) {
        IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport) this;
        List<ItemStack> circuits = ccs.getConfigurationCircuits();
        uiContext.openClientWindow(player -> new SelectItemUIFactory(
                        StatCollector.translateToLocal("GT5U.machines.select_circuit"),
                        getStackForm(0),
                        this::onCircuitSelected,
                        circuits,
                        GT_Utility.findMatchingStackInList(circuits, getStackInSlot(ccs.getCircuitSlot())))
                .setAnotherWindow(true, dialogOpened)
                .setGuiTint(getGUIColorization())
                .createWindow(new UIBuildContext(player)));
    }

    private void onCircuitSelected(ItemStack selected) {
        GT_Values.NW.sendToServer(new GT_Packet_SetConfigurationCircuit(getBaseMetaTileEntity(), selected));
        // we will not do any validation on client side
        // it doesn't get to actually decide what inventory contains anyway
        IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport) this;
        getBaseMetaTileEntity().setInventorySlotContents(ccs.getCircuitSlot(), selected);
    }

    protected int getTextColorOrDefault(String textType, int defaultColor) {
        return colorOverride.getTextColorOrDefault(textType, defaultColor);
    }

    protected Supplier<Integer> COLOR_TITLE = () -> getTextColorOrDefault("title", 0x404040);
    protected Supplier<Integer> COLOR_TITLE_WHITE = () -> getTextColorOrDefault("title_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_WHITE = () -> getTextColorOrDefault("text_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_GRAY = () -> getTextColorOrDefault("text_gray", 0x404040);
    protected Supplier<Integer> COLOR_TEXT_RED = () -> getTextColorOrDefault("text_red", 0xff0000);

    @Override
    public int getGUIColorization() {
        Dyes dye = Dyes.dyeWhite;
        if (this.colorOverride.sLoaded()) {
            if (this.colorOverride.sGuiTintingEnabled() && getBaseMetaTileEntity() != null) {
                dye = getDyeFromIndex(getBaseMetaTileEntity().getColorization());
                return this.colorOverride.getGuiTintOrDefault(dye.mName, GT_Util.getRGBInt(dye.getRGBA()));
            }
        } else if (GregTech_API.sColoredGUI) {
            if (GregTech_API.sMachineMetalGUI) {
                dye = Dyes.MACHINE_METAL;
            } else if (getBaseMetaTileEntity() != null) {
                dye = getDyeFromIndex(getBaseMetaTileEntity().getColorization());
            }
        }
        return GT_Util.getRGBInt(dye.getRGBA());
    }

    private Dyes getDyeFromIndex(short index) {
        return index != -1 ? Dyes.get(index) : Dyes.MACHINE_METAL;
    }

    // === AE2 compat ===

    @Optional.Method(modid = "appliedenergistics2")
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.NONE;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public AENetworkProxy getProxy() {
        return null;
    }

    @Optional.Method(modid = "appliedenergistics2")
    public void gridChanged() {}

    // === Waila compat ===

    @Override
    public void getWailaBody(
            ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        currenttip.add(String.format(
                "Facing: %s",
                ForgeDirection.getOrientation(mBaseMetaTileEntity.getFrontFacing())
                        .name()));
    }

    @Override
    public void getWailaNBTData(
            EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        /* Empty */
    }

    @Optional.Method(modid = "appliedenergistics2")
    protected String getAEDiagnostics() {
        try {
            if (getProxy() == null) return "(proxy)";
            if (getProxy().getNode() == null) return "(node)";
            if (getProxy().getNode().getGrid() == null) return "(grid)";
            if (!getProxy().getNode().meetsChannelRequirements()) return "(channels)";
            IPathingGrid pg = getProxy().getNode().getGrid().getCache(IPathingGrid.class);
            if (!pg.isNetworkBooting()) return "(booting)";
            IEnergyGrid eg = getProxy().getNode().getGrid().getCache(IEnergyGrid.class);
            if (!eg.isNetworkPowered()) return "(power)";
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
