package gregtech.api.metatileentity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.GTMod.GT_FML_LOGGER;

import java.util.Arrays;
import java.util.Collections;
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
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;

public abstract class CommonBaseMetaTileEntity extends CoverableTileEntity implements IGregTechTileEntity {

    protected boolean mNeedsBlockUpdate = true, mNeedsUpdate = true, mNeedsTileUpdate = false, mSendClientData = false,
        mInventoryChanged = false, mTickDisabled = false;

    protected NBTTagCompound pendingDescriptionPacket;

    private boolean mIgnoreNextUnload = false;

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
            // If we have a pending description packet that was received before the MTE was created, load it
            if (pendingDescriptionPacket != null) {
                getMetaTileEntity().onDescriptionPacket(pendingDescriptionPacket);
                pendingDescriptionPacket = null;
            }
            return true;
        }
        return false;
    }

    @Override
    public void enableTicking() {
        if (!mTickDisabled) return;
        getWorld().func_147448_a(Collections.singleton(this));
        mTickDisabled = false;
    }

    @Override
    public void disableTicking() {
        if (mTickDisabled) return;
        getWorld().func_147457_a(this);
        mIgnoreNextUnload = true;
        hasTimeStatisticsStarted = false;
        Arrays.fill(mTimeStatistics, 0);
        mTickDisabled = true;
    }

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

    protected abstract void updateEntityProfiled();

    @Override
    public final void updateEntity() {
        super.updateEntity();

        long tTime;
        if (hasTimeStatisticsStarted) {
            tTime = System.nanoTime();
        } else {
            tTime = 0;
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

        if (isServerSide() && hasTimeStatisticsStarted && hasValidMetaTileEntity()) {
            tTime = System.nanoTime() - tTime;
            mTimeStatisticsIndex = (mTimeStatisticsIndex + 1) % mTimeStatistics.length;
            mTimeStatistics[mTimeStatisticsIndex] = (int) tTime;
            if (tTime > 0 && tTime > (GregTechAPI.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING * 1_000_000L)
                && mTickTimer > 1000
                && getMetaTileEntity().doTickProfilingMessageDuringThisTick()
                && mLagWarningCount++ < 10)
                GT_FML_LOGGER.warn(
                    "WARNING: Possible Lag Source at [" + xCoord
                        + ", "
                        + yCoord
                        + ", "
                        + zCoord
                        + "] in Dimension "
                        + worldObj.provider.dimensionId
                        + " with "
                        + tTime
                        + " ns caused by an instance of "
                        + getMetaTileEntity().getClass());
        }

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
                    GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.");
                    GTMod.logStackTrace(e);
                }
            }
        } catch (Exception e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.");
            GTMod.logStackTrace(e);
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

    @Override
    public Packet getDescriptionPacket() {
        issueClientUpdate();
        sendClientData();

        IMetaTileEntity imte = getMetaTileEntity();

        if (imte == null) return null;

        NBTTagCompound data = imte.getDescriptionData();

        if (data == null) return null;

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, data);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        IMetaTileEntity imte = getMetaTileEntity();

        if (imte == null) {
            // If we don't have a meta tile yet, it's likely because it hasn't been created on the client yet
            // Let's just store a reference to the data and process it once the meta tile has been created
            // If this tile entity is about to be destroyed then we won't be causing a memory leak here so this is safe
            pendingDescriptionPacket = pkt.func_148857_g();
            return;
        }

        imte.onDescriptionPacket(pkt.func_148857_g());
    }

    @Override
    public void issueTextureUpdate() {
        mNeedsUpdate = true;
    }

    @Override
    public void issueClientUpdate() {
        mSendClientData = true;
        if (mTickDisabled) {
            sendClientData();
        }
    }

    abstract protected void sendClientData();

    @Override
    public void issueBlockUpdate() {
        mNeedsBlockUpdate = true;
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
    public boolean canAccessData() {
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

    @Override
    public void issueCoverUpdate(ForgeDirection side) {
        super.issueCoverUpdate(side);
        issueClientUpdate();
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
