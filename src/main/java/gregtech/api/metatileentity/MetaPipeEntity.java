package gregtech.api.metatileentity;

import static gregtech.api.enums.GT_Values.GT;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.GT_Client;
import gregtech.common.covers.CoverInfo;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaPipe Call the Constructor with the desired ID at the load-phase (not preload and
 * also not postload!) Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it. "new
 * GT_MetaTileEntity_E_Furnace(54, "GT_E_Furnace", "Automatic E-Furnace");"
 */
public abstract class MetaPipeEntity implements IMetaTileEntity, IConnectable {

    /**
     * The Inventory of the MetaTileEntity. Amount of Slots can be larger than 256. HAYO!
     */
    public final ItemStack[] mInventory;
    /**
     * This variable tells, which directions the Block is connected to. It is a Bitmask.
     */
    public byte mConnections = 0;

    protected boolean mCheckConnections = false;
    /**
     * Only assigned for the MetaTileEntity in the List! Also only used to get the localized Name for the ItemStack and
     * for getInvName.
     */
    public String mName;

    public boolean doTickProfilingInThisTick = true;
    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

    /**
     * This registers your Machine at the List. Use only ID's larger than 2048 - the ones lower are reserved by GT.
     * See also the list in the API package - it has a description that contains all the reservations.
     * <p>
     * The constructor can be overloaded as follows:
     * <blockquote>
     *
     * <pre>
     *
     * public GT_MetaTileEntity_EBench(int id, String name, String nameRegional) {
     *     super(id, name, nameRegional);
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @param aID the machine ID
     */
    public MetaPipeEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        this(aID, aBasicName, aRegionalName, aInvSlotCount, true);
    }

    public MetaPipeEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount, boolean aAddInfo) {
        if (GregTech_API.sPostloadStarted || !GregTech_API.sPreloadStarted)
            throw new IllegalAccessError("This Constructor has to be called in the load Phase");
        if (GregTech_API.METATILEENTITIES[aID] == null) {
            GregTech_API.METATILEENTITIES[aID] = this;
        } else {
            throw new IllegalArgumentException("MetaMachine-Slot Nr. " + aID + " is already occupied!");
        }
        mName = aBasicName.replaceAll(" ", "_")
            .toLowerCase(Locale.ENGLISH);
        setBaseMetaTileEntity(new BaseMetaPipeEntity());
        getBaseMetaTileEntity().setMetaTileID((short) aID);
        GT_LanguageManager.addStringLocalization("gt.blockmachines." + mName + ".name", aRegionalName);
        mInventory = new ItemStack[aInvSlotCount];

        if (aAddInfo && GT.isClientSide()) {
            addInfo(aID);
        }
    }

    protected final void addInfo(int aID) {
        if (!GT.isClientSide()) return;

        ItemStack tStack = new ItemStack(GregTech_API.sBlockMachines, 1, aID);
        Objects.requireNonNull(tStack.getItem())
            .addInformation(tStack, null, new ArrayList<>(), true);
    }

    /**
     * This is the normal Constructor.
     */
    public MetaPipeEntity(String aName, int aInvSlotCount) {
        mInventory = new ItemStack[aInvSlotCount];
        mName = aName;
    }

    /**
     * For Pipe Rendering
     */
    public abstract float getThickNess();

    /**
     * For Pipe Rendering
     */
    public abstract boolean renderInside(ForgeDirection side);

    public boolean isDisplaySecondaryDescription() {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.ERROR_RENDERING;
    }

    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection, int connections,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.ERROR_RENDERING;
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

    public boolean isCoverOnSide(BaseMetaPipeEntity aPipe, EntityLivingBase aEntity) {
        ForgeDirection side = ForgeDirection.UNKNOWN;
        double difference = aEntity.posY - (double) aPipe.yCoord;
        if (difference > 0.6 && difference < 0.99) {
            side = ForgeDirection.UP;
        }
        if (difference < -1.5 && difference > -1.99) {
            side = ForgeDirection.DOWN;
        }
        difference = aEntity.posZ - (double) aPipe.zCoord;
        if (difference < -0.05 && difference > -0.4) {
            side = ForgeDirection.NORTH;
        }
        if (difference > 1.05 && difference < 1.4) {
            side = ForgeDirection.SOUTH;
        }
        difference = aEntity.posX - (double) aPipe.xCoord;
        if (difference < -0.05 && difference > -0.4) {
            side = ForgeDirection.WEST;
        }
        if (difference > 1.05 && difference < 1.4) {
            side = ForgeDirection.EAST;
        }
        boolean tCovered = side != ForgeDirection.UNKNOWN && mBaseMetaTileEntity.getCoverIDAtSide(side) > 0;
        if (isConnectedAtSide(side)) {
            tCovered = true;
        }
        // GT_FML_LOGGER.info("Cover: "+mBaseMetaTileEntity.getCoverIDAtSide(aSide));
        // toDo: filter cover ids that actually protect against temperature (rubber/plastic maybe?, more like asbestos)
        return tCovered;
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
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCoverID) {
        return true;
    }

    @Deprecated
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        /* Do nothing */
    }

    @Deprecated
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ) {
        return false;
    }

    @Deprecated
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        return false;
    }

    @Deprecated
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
        float aY, float aZ, ItemStack aTool) {
        return onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public void onExplosion() {
        /* Do nothing */
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
     * Called when a Player rightclicks the Machine. Sneaky rightclicks are not getting passed to this!
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        return false;
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
    public final void sendSound(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.DO_SOUND, aIndex);
    }

    @Override
    public final void sendLoopStart(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.START_SOUND_LOOP, aIndex);
    }

    @Override
    public final void sendLoopEnd(byte aIndex) {
        if (!getBaseMetaTileEntity().hasMufflerUpgrade())
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.STOP_SOUND_LOOP, aIndex);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
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

    @Override
    public ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer,
        int aLogLevel, ArrayList<String> aList) {
        return aList;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return false;
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

    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return 0;
    }

    @Override
    public boolean acceptsRotationalEnergy(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean injectRotationalEnergy(ForgeDirection side, long aSpeed, long aEnergy) {
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
    public ItemStack getStackInSlot(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < mInventory.length) return mInventory[slotIndex];
        return null;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
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
                if (setStackToZeroInsteadOfNull(aIndex)) tStack.stackSize = 0;
                else setInventorySlotContents(aIndex, null);
            } else {
                rStack = tStack.splitStack(aAmount);
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
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int ordinalSide) {
        return isValidSlot(slotIndex) && itemStack != null
            && slotIndex < mInventory.length
            && (mInventory[slotIndex] == null || GT_Utility.areStacksEqual(itemStack, mInventory[slotIndex]))
            && allowPutStack(getBaseMetaTileEntity(), slotIndex, ForgeDirection.getOrientation(ordinalSide), itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int ordinalSide) {
        return isValidSlot(slotIndex) && itemStack != null
            && slotIndex < mInventory.length
            && allowPullStack(
                getBaseMetaTileEntity(),
                slotIndex,
                ForgeDirection.getOrientation(ordinalSide),
                itemStack);
    }

    @Override
    public boolean canFill(ForgeDirection side, Fluid aFluid) {
        return fill(side, new FluidStack(aFluid, 1), false) == 1;
    }

    @Override
    public boolean canDrain(ForgeDirection side, Fluid aFluid) {
        return drain(side, new FluidStack(aFluid, 1), false) != null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[] {};
        return new FluidTankInfo[] { getInfo() };
    }

    public int fill_default(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        return fill(aFluid, doFill);
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        return fill_default(side, aFluid, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean doDrain) {
        if (getFluid() != null && aFluid != null && getFluid().isFluidEqual(aFluid))
            return drain(aFluid.amount, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int maxDrain, boolean doDrain) {
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
    public boolean doTickProfilingMessageDuringThisTick() {
        return doTickProfilingInThisTick;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return false;
    }

    @Override
    public boolean connectsToItemPipe(ForgeDirection side) {
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

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public float getExplosionResistance(ForgeDirection side) {
        return (mConnections & IConnectable.HAS_FOAM) != 0 ? 50.0F : 5.0F;
    }

    @Override
    public ItemStack[] getRealInventory() {
        return mInventory;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public void markDirty() {
        //
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        setCheckConnections();
    }

    @Override
    public void onColorChangeClient(byte aColor) {
        // Do nothing apparently
    }

    public void setCheckConnections() {
        mCheckConnections = true;
    }

    public long injectEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        return 0;
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
        int tX = getBaseMetaTileEntity().getXCoord(), tY = getBaseMetaTileEntity().getYCoord(),
            tZ = getBaseMetaTileEntity().getZCoord();
        World tWorld = getBaseMetaTileEntity().getWorld();
        tWorld.setBlock(tX, tY, tZ, Blocks.air);
        if (GregTech_API.sMachineExplosions) {
            new WorldSpawnedEventBuilder.ExplosionEffectEventBuilder().setStrength(tStrength)
                .setSmoking(true)
                .setPosition(tX + 0.5, tY + 0.5, tZ + 0.5)
                .setWorld(tWorld)
                .run();
        }
    }

    @Override
    public int getLightOpacity() {
        return 0;
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

    @Override
    public boolean hasAlternativeModeText() {
        return false;
    }

    @Override
    public String getAlternativeModeText() {
        return "";
    }

    @Deprecated
    public String trans(String aKey, String aEnglish) {
        return GT_Utility.trans(aKey, aEnglish);
    }

    protected boolean connectableColor(TileEntity tTileEntity) {
        // Determine if two entities are connectable based on their colorization:
        // Uncolored can connect to anything
        // If both are colored they must be the same color to connect.
        if (tTileEntity instanceof IColoredTileEntity) {
            if (getBaseMetaTileEntity().getColorization() >= 0) {
                final byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                return tColor < 0 || tColor == getBaseMetaTileEntity().getColorization();
            }
        }

        return true;
    }

    @Override
    public int connect(ForgeDirection side) {
        if (side == ForgeDirection.UNKNOWN) return 0;

        final ForgeDirection oppositeSide = side.getOpposite();
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        if (baseMetaTile == null || !baseMetaTile.isServerSide()) return 0;

        final CoverInfo coverInfo = baseMetaTile.getCoverInfoAtSide(side);

        final boolean alwaysLookConnected = coverInfo.alwaysLookConnected();
        final boolean letsIn = letsIn(coverInfo);
        final boolean letsOut = letsOut(coverInfo);

        // Careful - tTileEntity might be null, and that's ok -- so handle it
        final TileEntity tTileEntity = baseMetaTile.getTileEntityAtSide(side);
        if (!connectableColor(tTileEntity)) return 0;

        if ((alwaysLookConnected || letsIn || letsOut)) {
            // Are we trying to connect to a pipe? let's do it!
            final IMetaTileEntity tPipe = tTileEntity instanceof IGregTechTileEntity
                ? ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()
                : null;
            if (getClass().isInstance(tPipe) || (tPipe != null && tPipe.getClass()
                .isInstance(this))) {
                connectAtSide(side);
                if (!((IConnectable) tPipe).isConnectedAtSide(oppositeSide)) {
                    // Make sure pipes all get together -- connect back to us if we're connecting to a pipe
                    ((IConnectable) tPipe).connect(oppositeSide);
                }
                return 1;
            } else if ((getGT6StyleConnection() && baseMetaTile.getAirAtSide(side)) || canConnect(side, tTileEntity)) {
                // Allow open connections to Air, if the GT6 style pipe/cables are enabled, so that it'll connect to
                // the next block placed down next to it
                connectAtSide(side);
                return 1;
            }
            if (!baseMetaTile.getWorld()
                .getChunkProvider()
                .chunkExists(baseMetaTile.getOffsetX(side, 1) >> 4, baseMetaTile.getOffsetZ(side, 1) >> 4)) {
                // Target chunk unloaded
                return -1;
            }
        }
        return 0;
    }

    protected void checkConnections() {
        // Verify connections around us. If GT6 style cables are not enabled then revert to old behavior and try
        // connecting to everything around us
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((!getGT6StyleConnection() || isConnectedAtSide(side)) && connect(side) == 0) {
                disconnect(side);
            }
        }
        mCheckConnections = false;
    }

    private void connectAtSide(ForgeDirection side) {
        mConnections |= side.flag;
    }

    @Override
    public void disconnect(ForgeDirection side) {
        if (side == ForgeDirection.UNKNOWN) return;
        mConnections &= ~side.flag;
        final ForgeDirection oppositeSide = side.getOpposite();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSide(side);
        IMetaTileEntity tPipe = tTileEntity == null ? null : tTileEntity.getMetaTileEntity();
        if ((this.getClass()
            .isInstance(tPipe)
            || (tPipe != null && tPipe.getClass()
                .isInstance(this)))
            && ((IConnectable) tPipe).isConnectedAtSide(oppositeSide)) {
            ((IConnectable) tPipe).disconnect(oppositeSide);
        }
    }

    @Override
    public boolean isConnectedAtSide(ForgeDirection sideDirection) {
        return (mConnections & sideDirection.flag) != 0;
    }

    public boolean letsIn(GT_CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return false;
    }

    public boolean letsIn(CoverInfo coverInfo) {
        return false;
    }

    public boolean letsOut(GT_CoverBehavior coverBehavior, ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return false;
    }

    public boolean letsOut(CoverInfo coverInfo) {
        return false;
    }

    public boolean letsIn(GT_CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    public boolean letsOut(GT_CoverBehaviorBase<?> coverBehavior, ForgeDirection side, int aCoverID,
        ISerializableObject aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    public boolean canConnect(ForgeDirection side, TileEntity tTileEntity) {
        return false;
    }

    public boolean getGT6StyleConnection() {
        return false;
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return false;
    }

    public void reloadLocks() {}

    @Override
    public int getGUIColorization() {
        Dyes dye = Dyes.dyeWhite;
        if (GregTech_API.sColoredGUI) {
            if (GregTech_API.sMachineMetalGUI) {
                dye = Dyes.MACHINE_METAL;
            } else if (getBaseMetaTileEntity() != null) {
                dye = Dyes.getDyeFromIndex(getBaseMetaTileEntity().getColorization());
            }
        }
        return GT_Util.getRGBInt(dye.getRGBA());
    }
}
