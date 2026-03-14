package gregtech.api.metatileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.capability.item.ItemIO;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSource;
import com.gtnewhorizon.gtnhlib.item.InventoryItemSource;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.implementation.items.GTItemSink;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTModularScreen;
import gregtech.api.modularui2.MetaTileEntityGuiHandler;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;
import gregtech.common.covers.Cover;

/**
 * {@link IMetaTileEntity} implementation combining both machine-like ({@link MetaTileEntity}) and pipe-like
 * ({@link MetaPipeEntity}).
 */
public abstract class CommonMetaTileEntity implements IMetaTileEntity {

    /**
     * Inventory of this block.
     */
    public final ItemStack[] mInventory;

    /**
     * Internal name of this block, mainly used for localization keys.
     */
    public final String mName;

    /**
     * While this is set to false, lag caused by this block won't be reported to console. Use it while the block is
     * intentionally doing something that lags, such as scanning multiple chunks or file IO. Don't forget to set it back
     * to true on the next tick.
     */
    public boolean doTickProfilingInThisTick = true;

    /**
     * For debugging how many sounds get requested to be played in given time.
     */
    public long mSoundRequests = 0;

    protected CommonMetaTileEntity(int id, String basicName, String regionalName, int invSlotCount) {
        if (GregTechAPI.sPostloadStarted || !GregTechAPI.sPreloadStarted)
            throw new IllegalAccessError("This Constructor has to be called in the load Phase");
        if (GregTechAPI.METATILEENTITIES[id] == null) {
            GregTechAPI.METATILEENTITIES[id] = this;
        } else {
            var existing = GregTechAPI.METATILEENTITIES[id];
            throw new IllegalArgumentException(
                "MetaTileEntity id " + id
                    + " is already occupied! Existing MTE is "
                    + existing.getMetaName()
                    + "("
                    + existing.getClass()
                        .getCanonicalName()
                    + ").");
        }
        mInventory = new ItemStack[invSlotCount];
        mName = basicName.replace(" ", "_")
            .toLowerCase(Locale.ENGLISH);
        GTLanguageManager.addStringLocalization("gt.blockmachines." + mName + ".name", regionalName);
    }

    protected CommonMetaTileEntity(String name, int invSlotCount) {
        mInventory = new ItemStack[invSlotCount];
        mName = name;
    }

    /**
     * @inheritDoc
     */
    @Nullable
    @Override
    public <T> T getCapability(@NotNull Class<T> capability, @NotNull ForgeDirection side) {
        if (capability == ItemSink.class) {
            return capability.cast(getItemSink(side));
        }
        if (capability == ItemSource.class) {
            return capability.cast(getItemSource(side));
        }
        if (capability == ItemIO.class) {
            return capability.cast(getItemIO(side));
        }

        return null;
    }

    protected ItemSink getItemSink(ForgeDirection side) {
        return getSizeInventory() == 0 ? null : new GTItemSink(this, side);
    }

    protected ItemSource getItemSource(ForgeDirection side) {
        return getSizeInventory() == 0 ? null : new InventoryItemSource(this, side);
    }

    protected ItemIO getItemIO(ForgeDirection side) {
        return null;
    }

    @Override
    public void onServerStart() {}

    @Override
    public void onConfigLoad() {}

    @Override
    public void setItemNBT(NBTTagCompound nbt) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister blockIconRegister) {}

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {}

    @Override
    public void onPreTick(IGregTechTileEntity baseMetaTileEntity, long tick) {}

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (baseMetaTileEntity.isClientSide() && GTMod.clientProxy()
            .changeDetected() == 4) {
            /*
             * Client tick counter that is set to 5 on hiding pipes and covers. It triggers a texture update next client
             * tick when reaching 4, with provision for 3 more update tasks, spreading client change detection related
             * work and network traffic on different ticks, until it reaches 0.
             */
            baseMetaTileEntity.issueTextureUpdate();
        }
    }

    public void onTickFail(IGregTechTileEntity baseMetaTileEntity, long tick) {}

    public void onSetActive(boolean active) {}

    public void onEnableWorking() {}

    public void onDisableWorking() {}

    @Override
    public void inValidate() {}

    @Override
    public void onRemoval() {}

    @Override
    public void initDefaultModes(NBTTagCompound nbt) {}

    /**
     * Called when a player right-clicks this block. Shift-right-clicks will not get passed to this!
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player, ForgeDirection side,
        float x, float y, float z) {
        return false;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player) {}

    @Override
    public void onValueUpdate(byte value) {}

    @Override
    public byte getUpdateData() {
        return 0;
    }

    @Override
    public void doSound(byte index, double x, double y, double z) {}

    @Override
    public void startSoundLoop(byte index, double x, double y, double z) {}

    @Override
    public void stopSoundLoop(byte value, double x, double y, double z) {}

    @Override
    public final void sendSound(byte aIndex) {
        if (!getBaseMetaTileEntity().isMuffled()) {
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.DO_SOUND, aIndex);
        }
    }

    public final void sendLoopStart(byte aIndex) {
        if (!getBaseMetaTileEntity().isMuffled()) {
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.START_SOUND_LOOP, aIndex);
        }
        mSoundRequests++;
    }

    @Override
    public final void sendLoopEnd(byte aIndex) {
        if (!getBaseMetaTileEntity().isMuffled()) {
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.STOP_SOUND_LOOP, aIndex);
        }
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isValidSlot(int index) {
        return true;
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return true;
    }

    @Override
    public boolean setStackToZeroInsteadOfNull(int index) {
        return false;
    }

    @Override
    public ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player,
        int logLevel, ArrayList<String> list) {
        return list;
    }

    @Override
    public ArrayList<ItemStack> getDroppedItem() {
        return null;
    }

    /**
     * Returns the fluid this block contains.
     */
    @Override
    public FluidStack getFluid() {
        return null;
    }

    /**
     * Tries to fill this tank.
     */
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    /**
     * Tries to empty this tank.
     */
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    /**
     * Returns capacity of the fluid.
     */
    @Override
    public int getCapacity() {
        return 0;
    }

    /**
     * Returns progress in ticks this machine has already made.
     */
    public int getProgresstime() {
        return 0;
    }

    /**
     * Returns total ticks required for this machine to finish one cycle of the progress.
     */
    public int maxProgresstime() {
        return 0;
    }

    /**
     * Increases the progress, returns the overflown progress.
     */
    public int increaseProgress(int progress) {
        return 0;
    }

    @Override
    public void onMachineBlockUpdate() {}

    @Override
    public void receiveClientEvent(byte eventID, byte value) {}

    /**
     * Gets the output for the comparator on the given side
     */
    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return 0;
    }

    @Override
    public String getSpecialVoltageToolTip() {
        return null;
    }

    public boolean isDigitalChest() {
        return false;
    }

    public ItemStack[] getStoredItemData() {
        return null;
    }

    public void setItemCount(int count) {}

    public int getMaxItemCount() {
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return mInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index >= 0 && index < mInventory.length) {
            return mInventory[index];
        }
        return null;
    }

    /**
     * Gets the first ItemStack in the bus, reading from the top left to bottom right
     *
     * @return the first ItemStack in the bus
     */
    public ItemStack getFirstStack() {
        for (int index = 0; index < mInventory.length; index++) {
            ItemStack stackInSlot = getStackInSlot(index);
            if (stackInSlot != null) {
                return stackInSlot;
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
        if (index >= 0 && index < mInventory.length) {
            mInventory[index] = itemStack;
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        if (GregTechAPI.METATILEENTITIES[getBaseMetaTileEntity().getMetaTileID()] != null) {
            return GregTechAPI.METATILEENTITIES[getBaseMetaTileEntity().getMetaTileID()].getMetaName();
        }
        return "";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return getBaseMetaTileEntity().isValidSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        ItemStack tStack = getStackInSlot(index), rStack = GTUtility.copyOrNull(tStack);
        if (tStack != null) {
            if (tStack.stackSize <= amount) {
                if (setStackToZeroInsteadOfNull(index)) {
                    tStack.stackSize = 0;
                    markDirty();
                } else setInventorySlotContents(index, null);
            } else {
                rStack = tStack.splitStack(amount);
                markDirty();
                if (tStack.stackSize == 0 && !setStackToZeroInsteadOfNull(index)) setInventorySlotContents(index, null);
            }
        }
        return rStack;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final TIntList tList = new TIntArrayList();
        final IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        if (tTileEntity == null || tTileEntity.isDead()) return GTValues.emptyIntArray;
        final Cover tileCover = tTileEntity.getCoverAtSide(ForgeDirection.getOrientation(ordinalSide));
        final boolean tSkip = tileCover.letsItemsIn(-2) || tileCover.letsItemsOut(-2);
        for (int i = 0; i < getSizeInventory(); i++) {
            if (isValidSlot(i) && (tSkip || tileCover.letsItemsOut(i) || tileCover.letsItemsIn(i))) {
                tList.add(i);
            }
        }
        return tList.toArray();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, int ordinalSide) {
        return isValidSlot(index) && itemStack != null
            && index < mInventory.length
            && (mInventory[index] == null || GTUtility.areStacksEqual(itemStack, mInventory[index]))
            && allowPutStack(getBaseMetaTileEntity(), index, ForgeDirection.getOrientation(ordinalSide), itemStack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, int ordinalSide) {
        return isValidSlot(index) && itemStack != null
            && index < mInventory.length
            && allowPullStack(getBaseMetaTileEntity(), index, ForgeDirection.getOrientation(ordinalSide), itemStack);
    }

    @Override
    public boolean canFill(ForgeDirection side, Fluid fluid) {
        return fill(side, new FluidStack(fluid, 1), false) == 1;
    }

    @Override
    public boolean canDrain(ForgeDirection side, Fluid fluid) {
        return drain(side, new FluidStack(fluid, 1), false) != null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().isSteampowered()) {
            return GTValues.emptyFluidTankInfo;
        }
        return new FluidTankInfo[] { getInfo() };
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack fluidStack, boolean doDrain) {
        if (getFluid() != null && fluidStack != null && getFluid().isFluidEqual(fluidStack)) {
            return drain(fluidStack.amount, doDrain);
        }
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
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    public boolean doTickProfilingMessageDuringThisTick() {
        return doTickProfilingInThisTick;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean connectsToItemPipe(ForgeDirection side) {
        return false;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public ItemStack[] getRealInventory() {
        return mInventory;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInInventory(ISBRInventoryContext ctx) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInWorld(ISBRWorldContext ctx) {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        AxisAlignedBB axisalignedbb1 = getCollisionBoundingBoxFromPool(world, x, y, z);
        if (axisalignedbb1 != null && inputAABB.intersectsWith(axisalignedbb1)) outputAABB.add(axisalignedbb1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity collider) {}

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {}

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

    /**
     * Opens GUI for the specified player. Currently, we have two ways to create GUI: MUI1 and MUI2. We're gradually
     * migrating to MUI2. However, since cover panel is not supported for the time being, leave support for MUI1
     * ({@link IAddUIWidgets#addUIWidgets}) as well.
     */
    @SuppressWarnings("deprecation")
    public void openGui(EntityPlayer player) {
        if ((GTGuis.GLOBAL_SWITCH_MUI2 && useMui2()) || forceUseMui2()) {
            if (!NetworkUtils.isClient(player)) {
                MetaTileEntityGuiHandler.open(player, this);
            }
        } else {
            GTUIInfos.openGTTileEntityUI(getBaseMetaTileEntity(), player);
        }

        onGuiOpened(player);
    }

    protected void onGuiOpened(EntityPlayer player) {

    }

    /**
     * Whether to use MUI2 for creating GUI. Use {@link #buildUI} for MUI2, and {@link IAddUIWidgets#addUIWidgets} for
     * MUI1.
     */
    protected boolean useMui2() {
        return false;
    }

    /**
     * Returning true means opening GUI with MUI2, regardless of {@link GTGuis#GLOBAL_SWITCH_MUI2}. Note that cover tabs
     * are currently unfinished.
     */
    protected boolean forceUseMui2() {
        return false;
    }

    @Override
    public final String getGuiId() {
        return mName;
    }

    /**
     * Specifies theme of this GUI. {@link GTGuiThemes} lists all the themes you can use.
     */
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.STANDARD;
    }

    /**
     * Override to create GUI with MUI2. You also need to override {@link #useMui2}.
     * <p>
     * Called on server and client. Create only the main panel here. Only here you can add sync handlers to widgets
     * directly. If the widget to be synced is not in this panel yet (f.e. in another panel) the sync handler must be
     * registered here with {@link PanelSyncManager}.
     *
     * @inheritDoc
     */
    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModularScreen createScreen(PosGuiData data, ModularPanel mainPanel) {
        return new GTModularScreen(mainPanel, getColoredTheme());
    }

    private GTGuiTheme getColoredTheme() {
        GTGuiTheme baseTheme = getGuiTheme();
        if (baseTheme != GTGuiThemes.STANDARD) return baseTheme;
        if (Client.iface.showGuiColorIndicator) return baseTheme;
        byte color = this.getBaseMetaTileEntity()
            .getColorization();
        Dyes dye = Dyes.get(color);
        return dye.mui2Theme.get();
    }

    /**
     * Returns the effective MUI2 theme for this machine (including dye-derived theme for STANDARD machines).
     */
    public GTGuiTheme getEffectiveGuiTheme() {
        return getColoredTheme();
    }
}
