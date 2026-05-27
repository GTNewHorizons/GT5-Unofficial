package gregtech.api.metatileentity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.GTMod.GT_FML_LOGGER;

import java.util.Arrays;
import java.util.Collections;
import java.util.IllegalFormatException;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import appeng.api.interfaces.IInterfaceNameProvider;
import appeng.api.util.WorldCoord;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.modularui.IBindPlayerInventoryUI;
import gregtech.api.interfaces.modularui.IGetTitleColor;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.blockupdate.BlockUpdateHandler;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Gregtech;

public abstract class CommonBaseMetaTileEntity extends CoverableTileEntity
    implements IGregTechTileEntity, IInterfaceNameProvider {

    protected boolean mNeedsBlockUpdate = true, mNeedsUpdate = true, mNeedsTileUpdate = false,
        mInventoryChanged = false, mTickDisabled = false;

    private boolean mIgnoreNextUnload = false;

    protected int oldX = 0, oldY = 0, oldZ = 0;
    protected byte oldStrongRedstone = 0, oldRedstoneData = 63, oldUpdateData = 0;

    private byte mColor = 0;

    // Profiling
    private final int[] mTimeStatistics = new int[GregTechAPI.TICKS_FOR_LAG_AVERAGING];
    private boolean hasTimeStatisticsStarted;
    private int mTimeStatisticsIndex = 0;
    private int mLagWarningCount = 0;

    protected boolean createNewMetatileEntity(short aID) {
        if (aID <= 0 || aID >= GregTechAPI.METATILEENTITIES.length || GregTechAPI.METATILEENTITIES[aID] == null) {
            GTLog.err.println("MetaID " + aID + " not loadable => locking TileEntity!");
        } else {
            if (hasValidMetaTileEntity()) getMetaTileEntity().setBaseMetaTileEntity(null);
            GregTechAPI.METATILEENTITIES[aID].newMetaEntity(this)
                .setBaseMetaTileEntity(this);
            mTickTimer = 0;
            mID = aID;
            return true;
        }
        return false;
    }

    public boolean isTickDisabled() {
        return mTickDisabled;
    }

    // Re-enable ticking after disable.
    @Override
    public void enableTicking() {
        if (!mTickDisabled) return;
        getWorld().func_147448_a(Collections.singleton(this));
        mTickDisabled = false;
    }

    // Effectively triggers unloading of the current tile entity, this does not invalidate the tile entity.
    // After unloading, the tile entity will be in the same state as a non-tickable tile entity.
    // This method may fail silently due to various reason, and calling enableTicking in the same tick will
    // cause the disable tick call to be effectively ignored, as adding tile entity take precedence over unloading.
    @Override
    public void tryDisableTicking() {
        if (mTickDisabled) return;
        if (getValidCoversMask() != 0) return;
        if (mIgnoreNextUnload) return;
        getWorld().func_147457_a(this);
        mIgnoreNextUnload = true;
        hasTimeStatisticsStarted = false;
        Arrays.fill(mTimeStatistics, 0);
        mTickDisabled = true;
    }

    // This method is called both when the ticking is disabled for this block and the block is unloaded.
    @Override
    public final void onChunkUnload() {
        if (mIgnoreNextUnload) {
            mIgnoreNextUnload = false;
            return;
        }
        onUnload();
    }

    public void onUnload() {
        super.onChunkUnload();
    }

    protected final void writeCommonNBT(NBTTagCompound nbt) {
        nbt.setByte("mColor", mColor);
    }

    protected final void readCommonNBT(NBTTagCompound nbt) {
        mColor = nbt.getByte("mColor");
    }

    @Override
    public final void updateEntity() {
        super.updateEntity();

        final long timeStart;
        if (hasTimeStatisticsStarted) {
            timeStart = System.nanoTime();
        } else {
            timeStart = 0;
        }

        try {
            updateEntityProfiled();
        } catch (Exception e) {
            GT_FML_LOGGER.error(
                "Error ticking meta tile entity {} at ({}, {}, {}) in world {}",
                getMetaTileID(),
                xCoord,
                yCoord,
                zCoord,
                worldObj.provider.dimensionId,
                e);
            try {
                onTickFail();
            } catch (Exception ex) {
                GT_FML_LOGGER.error(
                    "Error calling tick fail on meta tile entity {} at ({}, {}, {}) in world {}",
                    getMetaTileID(),
                    xCoord,
                    yCoord,
                    zCoord,
                    worldObj.provider.dimensionId,
                    e);
            }
        }

        if (hasTimeStatisticsStarted && isServerSide() && hasValidMetaTileEntity()) {
            final long duration = System.nanoTime() - timeStart;
            mTimeStatisticsIndex = (mTimeStatisticsIndex + 1) % mTimeStatistics.length;
            mTimeStatistics[mTimeStatisticsIndex] = (int) duration;
            if (duration > 0 && duration > (GregTechAPI.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING * 1_000_000L)
                && mTickTimer > 1000
                && getMetaTileEntity().doTickProfilingMessageDuringThisTick()
                && mLagWarningCount++ < 10)
                GT_FML_LOGGER.warn(
                    "WARNING: Possible Lag Source at [{}, {}, {}] in Dimension {} with {} ns caused by an instance of {}",
                    xCoord,
                    yCoord,
                    zCoord,
                    worldObj.provider.dimensionId,
                    duration,
                    getMetaTileEntity().getClass());
        }

    }

    protected abstract void updateEntityProfiled();

    /**
     * Handles setting data on the first tick
     */
    protected final void handleFirstTick(boolean isServerSide) {
        oldX = xCoord;
        oldY = yCoord;
        oldZ = zCoord;
        if (isServerSide) {
            checkDropCover();
        } else {
            requestCoverDataIfNeeded();
        }
        worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
        getMetaTileEntity().onFirstTick(this);
    }

    /**
     * Colorless is 0 for this function. Prefer {@link #getColorization()} for usual cases
     *
     * @return color from 0 to 16, 0 means colorless.
     */
    protected final byte getColorRaw() {
        return mColor;
    }

    /**
     * Colorless is 0 for this function. Prefer {@link #setColorization(byte)} for usual cases
     */
    protected final void setColorRaw(byte color) {
        if (mColor == color) return;
        mColor = color;
        if (isClientSide()) {
            getMetaTileEntity().onColorChangeClient(mColor);
            issueTextureUpdate();
        } else {
            getMetaTileEntity().onColorChangeServer(mColor);
            sendBlockEvent(GregTechTileClientEvents.CHANGE_COLOR, mColor);
        }
    }

    /**
     * Handles marking the tile entity's block for an update on the client side
     */
    protected final void handleBlockUpdateClient() {
        if (!mNeedsUpdate) {
            return;
        }
        if (GTMod.proxy.mUseBlockUpdateHandler) {
            BlockUpdateHandler.Instance.enqueueBlockUpdate(worldObj, new WorldCoord(this));
        } else {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        getMetaTileEntity().onTextureUpdate();
        mNeedsUpdate = false;
    }

    /**
     * Handles the tile entity's position changing
     */
    protected final void handlePositionChange() {
        if (xCoord == oldX && yCoord == oldY && zCoord == oldZ) {
            return;
        }
        oldX = xCoord;
        oldY = yCoord;
        oldZ = zCoord;
        issueTileUpdate();
        clearTileEntityBuffer();
    }

    /**
     * Handles the update data changing
     */
    protected final void handleUpdateDataChangeServer() {
        byte updateData = getMetaTileEntity().getUpdateData();
        if (updateData == oldUpdateData) {
            return;
        }
        oldUpdateData = updateData;
        sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, oldUpdateData);
    }

    /**
     * Handles sided Redstone changing
     */
    protected final void handleSidedRedstoneChangeServer() {
        byte redstone = getSidedRedstoneMask();
        if (redstone == oldRedstoneData) {
            return;
        }
        oldRedstoneData = redstone;
        sendBlockEvent(GregTechTileClientEvents.CHANGE_REDSTONE_OUTPUT, oldRedstoneData);
    }

    protected abstract void onTickFail();

    protected void saveMetaTileNBT(NBTTagCompound aNBT) {
        try {
            if (hasValidMetaTileEntity()) {
                aNBT.setInteger("nbtVersion", GTMod.NBT_VERSION);
                final NBTTagList tItemList = new NBTTagList();
                for (int i = 0; i < getMetaTileEntity().getRealInventory().length; i++) {
                    final ItemStack tStack = getMetaTileEntity().getRealInventory()[i];
                    if (tStack != null) {
                        final NBTTagCompound tTag = new NBTTagCompound();
                        tTag.setInteger("IntSlot", i);
                        tStack.writeToNBT(tTag);
                        if (tStack.stackSize > Byte.MAX_VALUE) {
                            tTag.setInteger("Count", tStack.stackSize);
                        }
                        tItemList.appendTag(tTag);
                    }
                }
                aNBT.setTag("Inventory", tItemList);

                try {
                    getMetaTileEntity().saveNBTData(aNBT);
                } catch (Exception e) {
                    GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.", e);
                }
            }
        } catch (Exception e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.", e);
        }
    }

    protected void loadMetaTileNBT(NBTTagCompound aNBT) {
        final int nbtVersion = aNBT.getInteger("nbtVersion");
        if (mID != 0 && createNewMetatileEntity(mID)) {
            final NBTTagList tItemList = aNBT.getTagList("Inventory", 10);
            for (int i = 0; i < tItemList.tagCount(); i++) {
                final NBTTagCompound tTag = tItemList.getCompoundTagAt(i);
                final int tSlot = migrateInventoryIndex(tTag.getInteger("IntSlot"), nbtVersion);
                if (tSlot >= 0 && tSlot < getMetaTileEntity().getRealInventory().length) {
                    ItemStack loadedStack = GTUtility.loadItem(tTag);
                    // We move away from fluid display item in TEs
                    if (loadedStack != null && loadedStack.getItem() == ItemList.Display_Fluid.getItem()) {
                        loadedStack = null;
                    }
                    getMetaTileEntity().getRealInventory()[tSlot] = loadedStack;
                }
            }

            try {
                getMetaTileEntity().loadNBTData(aNBT);
            } catch (Exception e) {
                GT_FML_LOGGER.error("Encountered Exception while loading MetaTileEntity.");
                GTMod.logStackTrace(e);
            }
        }
    }

    protected void sendSoundToPlayers(SoundResource sound, float soundStrength, int soundModulation) {
        GTUtility
            .sendSoundToPlayers(worldObj, sound, soundStrength, soundModulation, xCoord + .5, yCoord + .5, zCoord + .5);
    }

    /**
     * Shifts the machine Inventory index according to the change in Input/Output Slots. Default implementation does not
     * do anything to the slotIndex.
     */
    protected int migrateInventoryIndex(int slotIndex, int nbtVersion) {
        return slotIndex;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        mInventoryChanged = true;
    }

    @Override
    public void startTimeStatistics() {
        hasTimeStatisticsStarted = true;
    }

    protected void addProfilingInformation(List<String> tList) {
        if (mTickDisabled) {
            tList.add("Tick Disabled");
        } else if (hasTimeStatisticsStarted) {
            double tAverageTime = 0;
            double tWorstTime = 0;
            int amountOfZero = 0;
            for (int tTime : mTimeStatistics) {
                tAverageTime += tTime;
                if (tTime > tWorstTime) {
                    tWorstTime = tTime;
                }
                if (tTime == 0) {
                    amountOfZero += 1;
                }
                // Uncomment this line to print out tick-by-tick times.
                // tList.add("tTime " + tTime);
            }
            // tick time zero means it has not been updated yet
            int samples = mTimeStatistics.length - amountOfZero;
            if (samples > 0) {
                tList.add(
                    "Average CPU load of ~" + formatNumber(tAverageTime / samples)
                        + "ns over "
                        + formatNumber(samples)
                        + " ticks with worst time of "
                        + formatNumber(tWorstTime)
                        + "ns.");
            }
        } else {
            startTimeStatistics();
            tList.add("Just started tick time statistics.");
        }
        if (mLagWarningCount > 0) {
            tList.add(
                "Caused " + (mLagWarningCount >= 10 ? "more than 10" : mLagWarningCount)
                    + " Lag Spike Warnings (anything taking longer than "
                    + GregTechAPI.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING
                    + "ms) on the Server.");
        }
    }

    @Override
    public int[] getTimeStatistics() {
        return mTimeStatistics;
    }

    @Override
    public boolean hasInventoryBeenModified() {
        return mInventoryChanged;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        if (canAccessData()) return getMetaTileEntity().isValidSlot(aIndex);
        return false;
    }

    /**
     * Run on the server when the block is marked for a full resync, e.g. when loading a chunk.
     */
    abstract byte[] getInitialDataForClient();

    /**
     * Runs on the client to receive full resync data from the server.
     */
    abstract void receiveInitialDataOnClient(byte[] data);

    @Override
    public Packet getDescriptionPacket() {
        byte[] base = getInitialDataForClient();
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByteArray("base", base);
        S35PacketUpdateTileEntity pkt = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);

        IMetaTileEntity imte = getMetaTileEntity();

        if (imte == null) return pkt;

        NBTTagCompound data = imte.getDescriptionData();

        if (data == null) return pkt;

        // Yeah we delay a bit of modification after packet construction
        // it's fine... it's clear this won't cause problems
        nbt.setTag("mte", data);

        return pkt;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        NBTTagCompound nbt = pkt.func_148857_g();
        // Receive and create the mte if it doesn't exist
        receiveInitialDataOnClient(nbt.getByteArray("base"));

        NBTTagCompound data = nbt.getCompoundTag("mte");
        IMetaTileEntity mte = getMetaTileEntity();

        // The mte sent from server is invalid
        if (mte == null) return;

        mte.onDescriptionPacket(data);
    }

    @Override
    public void issueTextureUpdate() {
        mNeedsUpdate = true;
    }

    @Override
    public void issueBlockUpdate() {
        mNeedsBlockUpdate = true;
        if (mTickDisabled) {
            doBlockUpdateServer();
        }
    }

    public final void doBlockUpdateServer() {
        updateNeighbours(mStrongRedstone, oldStrongRedstone);
        oldStrongRedstone = mStrongRedstone;
        mNeedsBlockUpdate = false;
    }

    @Override
    public void issueTileUpdate() {
        mNeedsTileUpdate = true;
        if (mTickDisabled) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            mNeedsTileUpdate = false;
        }
    }

    @Override
    public boolean isValidFacing(ForgeDirection side) {
        if (canAccessData()) return getMetaTileEntity().isFacingValid(side);
        return false;
    }

    @Override
    public final boolean canAccessData() {
        return !isDead && hasValidMetaTileEntity();
    }

    protected abstract boolean hasValidMetaTileEntity();

    @Override
    public String[] getDescription() {
        if (canAccessData()) return getMetaTileEntity().getDescription();
        return GTValues.emptyStringArray;
    }

    @Override
    public boolean isStillValid() {
        return hasValidMetaTileEntity();
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return hasValidMetaTileEntity() && getMetaTileEntity().allowCoverOnSide(side, coverItem);
    }

    /*
     * IC2 Energy Compat
     */
    @Override
    public boolean shouldJoinIc2Enet() {
        final IMetaTileEntity meta = getMetaTileEntity();
        return meta != null && meta.shouldJoinIc2Enet();
    }

    /*
     * Modular UI Support
     */

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IAddUIWidgets) {
            ((IAddUIWidgets) getMetaTileEntity()).addUIWidgets(builder, buildContext);
            return;
        }
        super.addUIWidgets(builder, buildContext);
    }

    @Override
    public void bindPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IBindPlayerInventoryUI) {
            ((IBindPlayerInventoryUI) getMetaTileEntity()).bindPlayerInventoryUI(builder, buildContext);
            return;
        }
        super.bindPlayerInventoryUI(builder, buildContext);
    }

    @Override
    public IConfigurationCircuitSupport getConfigurationCircuitSupport() {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IConfigurationCircuitSupport) {
            return (IConfigurationCircuitSupport) getMetaTileEntity();
        }
        return null;
    }

    @Override
    public String getInterfaceNameSuffix() {
        final IConfigurationCircuitSupport ccs = getConfigurationCircuitSupport();
        if (ccs == null || !ccs.allowSelectCircuit()) return null;
        ItemStack stack = getStackInSlot(ccs.getCircuitSlot());
        if (stack == null || stack.getItemDamage() <= 0) return null;
        try {
            return String.format(Gregtech.machines.ghostCircuitSuffixFormat, stack.getItemDamage());
        } catch (IllegalFormatException e) {
            return "";
        }
    }

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getInventoryHandler();
        }
        return null;
    }

    @Override
    public boolean useModularUI() {
        return hasValidMetaTileEntity();
    }

    @Override
    public String getLocalName() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().getLocalName();
        return super.getLocalName();
    }

    @Override
    protected int getGUIWidth() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().getGUIWidth();

        return super.getGUIWidth();
    }

    @Override
    protected int getGUIHeight() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().getGUIHeight();

        return super.getGUIHeight();
    }

    @Override
    protected boolean doesBindPlayerInventory() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().doesBindPlayerInventory();

        return super.doesBindPlayerInventory();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IAddGregtechLogo) {
            ((IAddGregtechLogo) getMetaTileEntity()).addGregTechLogo(builder);
            return;
        }
        super.addGregTechLogo(builder);
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getStackForm(aAmount);
        }
        return super.getStackForm(aAmount);
    }

    @Override
    public int getTitleColor() {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IGetTitleColor) {
            return ((IGetTitleColor) getMetaTileEntity()).getTitleColor();
        }
        return super.getTitleColor();
    }

    @Override
    public int getGUIColorization() {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getGUIColorization();
        }
        return super.getGUIColorization();
    }

    @Override
    protected int getTextColorOrDefault(String textType, int defaultColor) {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getTextColorOrDefault(textType, defaultColor);
        }
        return defaultColor;
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getGUITextureSet();
        }
        return super.getGUITextureSet();
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext) {
        if (!useModularUI()) return null;

        buildContext.setValidator(getValidator());
        final ModularWindow.Builder builder = ModularWindow.builder(getGUIWidth(), getGUIHeight());
        builder.setBackground(getGUITextureSet().getMainBackground());
        builder.setGuiTint(getGUIColorization());
        if (doesBindPlayerInventory()) {
            bindPlayerInventoryUI(builder, buildContext);
        }
        addUIWidgets(builder, buildContext);
        addTitleToUI(builder);
        addCoverTabs(builder, buildContext);
        final IConfigurationCircuitSupport csc = getConfigurationCircuitSupport();
        if (csc != null && csc.allowSelectCircuit()) {
            addConfigurationCircuitSlot(builder);
        } else {
            addGregTechLogo(builder);
        }
        return builder.build();
    }
}
