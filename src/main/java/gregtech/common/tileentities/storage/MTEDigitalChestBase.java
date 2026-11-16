package gregtech.common.tileentities.storage;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST_GLOW;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.capability.item.ItemIO;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSource;
import com.gtnewhorizon.gtnhlib.item.AbstractInventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;
import com.gtnewhorizon.gtnhlib.item.SimpleItemIO;
import com.gtnewhorizon.gtnhlib.util.ItemUtil;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import cpw.mods.fml.common.Optional;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import fox.spiteful.avaritia.items.ItemStackWrapper;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.crossmod.ae2.IMEAwareItemInventory;
import gregtech.crossmod.ae2.MEItemInventoryHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEDigitalChestBase extends MTETieredMachineBlock
    implements IMEMonitor<IAEItemStack>, IMEAwareItemInventory, IAddUIWidgets {

    protected boolean mVoidOverflow = false;
    protected boolean mDisableFilter;
    private final MEItemInventoryHandler<?> meInventoryHandler = new MEItemInventoryHandler<>(this);

    private int lastTrueCount;
    private ItemStack lastMatterClusterOutput;
    private final InsertionState liveInsertionState = new InsertionState(false);

    private final class InsertionState {

        private final boolean simulated;
        private ItemStack item;
        private int count;
        private ItemStack output;
        private boolean outputDetached;

        private InsertionState(boolean simulated) {
            this.simulated = simulated;
            if (simulated) {
                item = getItemStack();
                count = getItemCount();
                output = mInventory[1];
            }
        }

        private ItemStack getStoredItem() {
            return simulated ? item : getItemStack();
        }

        private void setStoredItem(ItemStack stack) {
            if (simulated) item = stack;
            else setItemStack(stack);
        }

        private int getStoredCount() {
            return simulated ? count : getItemCount();
        }

        private void setStoredCount(int amount) {
            if (simulated) count = amount;
            else setItemCount(amount);
        }

        private ItemStack getOutput() {
            if (simulated && !outputDetached) {
                output = GTUtility.copyOrNull(output);
                outputDetached = true;
            }
            return simulated ? output : mInventory[1];
        }

        private void setOutput(ItemStack stack) {
            if (simulated) {
                output = stack;
                outputDetached = true;
            } else mInventory[1] = stack;
        }

        private boolean canReplaceOutputWithMatterCluster(ItemStack storedItem, long storageLimit) {
            ItemStack currentOutput = getOutput();
            if (currentOutput == null) return true;
            return GTUtility.areStacksEqual(currentOutput, storedItem)
                && (long) getStoredCount() + currentOutput.stackSize <= storageLimit;
        }

        private void reclaimOutput() {
            ItemStack currentOutput = getOutput();
            if (currentOutput == null) return;
            storeMatterClusterContents(currentOutput, currentOutput.stackSize);
            setOutput(null);
        }

        private @Nullable ItemStack getMatterClusterTargetItem() {
            if (getStoredCount() > 0 && getStoredItem() != null) return getStoredItem();
            ItemStack currentOutput = getOutput();
            return currentOutput != null && !isMatterCluster(currentOutput) ? currentOutput : null;
        }

        private void storeMatterClusterContents(ItemStack storedItem, long itemCount) {
            if (getStoredCount() <= 0) {
                ItemStack storedItemCopy = storedItem.copy();
                storedItemCopy.stackSize = 1;
                setStoredItem(storedItemCopy);
            }
            setStoredCount((int) (getStoredCount() + itemCount));
        }
    }

    private static final class MatterClusterEntry {

        private final ItemStack item;
        private final int itemCount;
        private final int totalItemCount;
        private final int entryCount;

        private MatterClusterEntry(ItemStack item, int itemCount, int totalItemCount, int entryCount) {
            this.item = item;
            this.itemCount = itemCount;
            this.totalItemCount = totalItemCount;
            this.entryCount = entryCount;
        }
    }

    public MTEDigitalChestBase(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] {
                translateToLocalFormatted("GT5U.machines.digitalchest.tooltip", formatNumber(commonSizeCompute(aTier))),
                translateToLocal("GT5U.machines.digitalchest.tooltip1"),
                translateToLocal("GT5U.machines.digitaltank.tooltip1"), });
    }

    protected static int commonSizeCompute(int tier) {
        return switch (tier) {
            case 1 -> 4000000;
            case 2 -> 8000000;
            case 3 -> 16000000;
            case 4 -> 32000000;
            case 5 -> 64000000;
            case 6 -> 128000000;
            case 7 -> 256000000;
            case 8 -> 512000000;
            case 9 -> 1024000000;
            case 10 -> 2147483640;
            default -> 0;
        };
    }

    public MTEDigitalChestBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (ItemStackNBT.hasKey(stack, "mItemStack")) {
            final ItemStack tContents = ItemStack
                .loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("mItemStack"));
            final int tSize = stack.stackTagCompound.getInteger("mItemCount");
            if (tContents != null && tSize > 0) {
                tooltip.add(
                    StatCollector.translateToLocalFormatted("gt.tileentity.chest_info", tContents.getDisplayName()));
                tooltip.add(StatCollector.translateToLocalFormatted("gt.tileentity.chest_amount", formatNumber(tSize)));
            }
        }
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver imeMonitorHandlerReceiver, Object o) {
        meInventoryHandler.addListener(imeMonitorHandlerReceiver, o);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver imeMonitorHandlerReceiver) {
        meInventoryHandler.removeListener(imeMonitorHandlerReceiver);
    }

    @Override
    public AccessRestriction getAccess() {
        return meInventoryHandler.getAccess();
    }

    @Override
    public boolean isPrioritized(IAEItemStack iaeItemStack) {
        ItemStack stack = iaeItemStack == null ? null : iaeItemStack.getItemStack();
        if (isMatterCluster(stack)) {
            ItemStack targetItem = liveInsertionState.getMatterClusterTargetItem();
            return targetItem != null && getMatterClusterEntry(stack, targetItem) != null;
        }
        return meInventoryHandler.isPrioritized(iaeItemStack);
    }

    @Override
    public boolean canAccept(IAEItemStack iaeItemStack) {
        ItemStack stack = iaeItemStack == null ? null : iaeItemStack.getItemStack();
        if (isMatterCluster(stack)) {
            return getMatterClusterEntry(stack, liveInsertionState.getMatterClusterTargetItem()) != null;
        }
        return meInventoryHandler.canAccept(iaeItemStack);
    }

    @Override
    public int getPriority() {
        return meInventoryHandler.getPriority();
    }

    @Override
    public int getSlot() {
        return meInventoryHandler.getSlot();
    }

    @Override
    public boolean validForPass(int i) {
        return meInventoryHandler.validForPass(i);
    }

    @Override
    public IItemList<IAEItemStack> getAvailableItems(final IItemList<IAEItemStack> out, int iteration) {
        return meInventoryHandler.getAvailableItems(out, iteration);
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        return meInventoryHandler.getStorageList();
    }

    @Override
    public int getMaxItemCount() {
        return commonSizeCompute(mTier);
    }

    @Override
    public int getItemCapacity() {
        return getMaxItemCount();
    }

    @Override
    public ItemStack getExtraItemStack() {
        return mInventory[1];
    }

    @Override
    public void setExtraItemStack(ItemStack stack) {
        mInventory[1] = stack;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public IAEItemStack injectItems(final IAEItemStack input, final Actionable mode, final BaseActionSource src) {
        if (isMatterCluster(input.getItemStack())) {
            if (getBaseMetaTileEntity() == null) return input;

            int previousCount = getItemCount();
            long remaining = insertMatterClusters(
                input.getItemStack(),
                input.getStackSize(),
                mode == Actionable.SIMULATE,
                false);

            if (mode == Actionable.MODULATE && getItemCount() != previousCount) {
                getBaseMetaTileEntity().markDirty();
            }
            if (remaining <= 0) return null;
            return input.copy()
                .setStackSize(remaining);
        }

        IAEItemStack returnStack = meInventoryHandler.injectItems(input, mode, src);
        if (mVoidOverflow) {
            return null;
        }
        return returnStack;
    }

    private static boolean isMatterCluster(ItemStack stack) {
        return stack != null && isMatterCluster(stack.getItem());
    }

    private static boolean isMatterCluster(Item item) {
        return Mods.Avaritia.isModLoaded() && isMatterClusterLoaded(item);
    }

    @Optional.Method(modid = Mods.ModIDs.AVARITIA)
    private static boolean isMatterClusterLoaded(Item item) {
        return item instanceof ItemMatterCluster;
    }

    private long insertMatterClusters(ItemStack cluster, long clusterCount, boolean simulate, boolean forced) {
        return insertMatterClusters(
            cluster,
            clusterCount,
            simulate ? new InsertionState(true) : liveInsertionState,
            forced);
    }

    private long insertMatterClusters(ItemStack cluster, long clusterCount, InsertionState state, boolean forced) {
        if (clusterCount <= 0) return 0;

        ItemStack currentItem = state.getMatterClusterTargetItem();
        MatterClusterEntry entry = getMatterClusterEntry(cluster, currentItem);
        long storageLimit = forced ? Integer.MAX_VALUE : getItemCapacity();
        if (entry == null) return passMatterClusterThrough(cluster, clusterCount, currentItem, storageLimit, state);

        long storedCount = state.getStoredCount();
        ItemStack currentOutput = state.getOutput();
        if (GTUtility.areStacksEqual(currentOutput, entry.item)) storedCount += currentOutput.stackSize;
        long availableSpace = Math.max(0, storageLimit - storedCount);

        long wholeClusters = getWholeClustersToConsume(entry, clusterCount, availableSpace);
        if (wholeClusters > 0) {
            long itemsToStore = Math.min(availableSpace, saturatedMultiply(wholeClusters, entry.itemCount));
            if (itemsToStore > 0) state.storeMatterClusterContents(entry.item, itemsToStore);
            if (!state.simulated) getBaseMetaTileEntity().markDirty();
            clusterCount -= wholeClusters;
            availableSpace -= itemsToStore;
            if (clusterCount <= 0 || mVoidOverflow) return clusterCount;
        }

        return insertPartialMatterCluster(
            cluster,
            clusterCount,
            currentItem,
            entry,
            availableSpace,
            storageLimit,
            state);
    }

    private long passMatterClusterThrough(ItemStack cluster, long clusterCount, @Nullable ItemStack currentItem,
        long storageLimit, InsertionState state) {
        if (currentItem == null || !state.canReplaceOutputWithMatterCluster(currentItem, storageLimit))
            return clusterCount;

        state.reclaimOutput();
        ItemStack output = cluster.copy();
        output.stackSize = 1;
        state.setOutput(output);
        if (!state.simulated) {
            notifyMatterClusterOutputChange();
            getBaseMetaTileEntity().markDirty();
        }
        return clusterCount - 1;
    }

    private long getWholeClustersToConsume(MatterClusterEntry entry, long clusterCount, long availableSpace) {
        if (entry.entryCount != 1) return 0;
        if (mVoidOverflow) return clusterCount;
        return Math.min(clusterCount, availableSpace / entry.itemCount);
    }

    private long insertPartialMatterCluster(ItemStack cluster, long clusterCount, @Nullable ItemStack currentItem,
        MatterClusterEntry entry, long availableSpace, long storageLimit, InsertionState state) {
        int itemsToConsume = mVoidOverflow ? entry.itemCount : (int) Math.min(entry.itemCount, availableSpace);
        if (itemsToConsume <= 0) return clusterCount;

        int remainderCount = entry.totalItemCount - itemsToConsume;
        boolean needsOutput = remainderCount > 0;
        if (needsOutput && !state.canReplaceOutputWithMatterCluster(entry.item, storageLimit)) return clusterCount;

        int itemsToStore = (int) Math.min(availableSpace, itemsToConsume);
        if (itemsToStore > 0) state.storeMatterClusterContents(entry.item, itemsToStore);

        if (needsOutput) {
            state.reclaimOutput();
            ItemStack remainder = cluster.copy();
            remainder.stackSize = 1;
            removeMatterClusterItems(remainder, entry.item, itemsToConsume, remainderCount);
            state.setOutput(remainder);
        }

        if (!state.simulated) {
            notifyMatterClusterOutputChange();
            getBaseMetaTileEntity().markDirty();
        }
        return clusterCount - 1;
    }

    @Optional.Method(modid = Mods.ModIDs.AVARITIA)
    private static @Nullable MatterClusterEntry getMatterClusterEntry(ItemStack cluster,
        @Nullable ItemStack targetItem) {
        Map<ItemStackWrapper, Integer> contents = ItemMatterCluster.getClusterData(cluster);
        if (contents.isEmpty()) return null;

        Map.Entry<ItemStackWrapper, Integer> selected = null;
        long totalItemCount = 0;
        for (Map.Entry<ItemStackWrapper, Integer> content : contents.entrySet()) {
            ItemStack item = content.getKey().stack;
            int itemCount = content.getValue();
            if (item == null || item.getItem() == null || itemCount <= 0) return null;

            totalItemCount += itemCount;
            if (totalItemCount > Integer.MAX_VALUE) return null;

            if (shouldSelectMatterClusterEntry(content, selected, targetItem)) selected = content;
        }

        if (selected == null || ItemMatterCluster.getClusterSize(cluster) != totalItemCount) return null;
        return new MatterClusterEntry(
            selected.getKey().stack,
            selected.getValue(),
            (int) totalItemCount,
            contents.size());
    }

    private static boolean shouldSelectMatterClusterEntry(Map.Entry<ItemStackWrapper, Integer> candidate,
        @Nullable Map.Entry<ItemStackWrapper, Integer> selected, @Nullable ItemStack targetItem) {
        if (targetItem != null) return GTUtility.areStacksEqual(targetItem, candidate.getKey().stack);
        return selected == null || candidate.getValue() > selected.getValue();
    }

    @Optional.Method(modid = Mods.ModIDs.AVARITIA)
    private static void removeMatterClusterItems(ItemStack cluster, ItemStack item, int amount, int remainderCount) {
        Map<ItemStackWrapper, Integer> contents = ItemMatterCluster.getClusterData(cluster);
        for (Iterator<Map.Entry<ItemStackWrapper, Integer>> iterator = contents.entrySet()
            .iterator(); iterator.hasNext();) {
            Map.Entry<ItemStackWrapper, Integer> content = iterator.next();
            if (!GTUtility.areStacksEqual(content.getKey().stack, item)) continue;

            int remaining = content.getValue() - amount;
            if (remaining > 0) content.setValue(remaining);
            else iterator.remove();
            break;
        }
        ItemMatterCluster.setClusterData(cluster, contents, remainderCount);
    }

    private void notifyMatterClusterOutputChange() {
        ItemStack currentOutput = isMatterCluster(mInventory[1]) ? mInventory[1] : null;
        if (lastMatterClusterOutput != null && !GTUtility.areStacksEqual(lastMatterClusterOutput, currentOutput)) {
            meInventoryHandler.notifyListeners(-lastMatterClusterOutput.stackSize, lastMatterClusterOutput);
        }
        if (currentOutput != null && !GTUtility.areStacksEqual(currentOutput, lastMatterClusterOutput)) {
            meInventoryHandler.notifyListeners(currentOutput.stackSize, currentOutput);
        }
        lastMatterClusterOutput = GTUtility.copyOrNull(currentOutput);
    }

    private static long saturatedMultiply(long left, int right) {
        return left > Long.MAX_VALUE / right ? Long.MAX_VALUE : left * right;
    }

    @Override
    public IAEItemStack extractItems(final IAEItemStack request, final Actionable mode, final BaseActionSource src) {
        return meInventoryHandler.extractItems(request, mode, src);
    }

    @Override
    public StorageChannel getChannel() {
        return meInventoryHandler.getChannel();
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        mVoidOverflow = !mVoidOverflow;
        GTUtility.sendChatTrans(
            aPlayer,
            mVoidOverflow ? "GT5U.machines.digitalchest.voidoverflow.enabled"
                : "GT5U.machines.digitalchest.voidoverflow.disabled");
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        mDisableFilter = !mDisableFilter;
        GTUtility.sendChatTrans(
            aPlayer,
            mDisableFilter ? "GT5U.machines.digitalchest.inputfilter.disabled"
                : "GT5U.machines.digitalchest.inputfilter.enabled");
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {

        if (getBaseMetaTileEntity().isServerSide()) {
            if ((getItemCount() <= 0)) {
                setItemStack(null);
                setItemCount(0);
            }
            int savedCount = getItemCount();
            insertInputItems();
            int count = getItemCount();
            ItemStack stack = getItemStack();
            if (mInventory[1] == null && stack != null) {
                mInventory[1] = stack.copy();
                mInventory[1].stackSize = Math.min(stack.getMaxStackSize(), count);
                count -= mInventory[1].stackSize;
            } else if ((count > 0) && GTUtility.areStacksEqual(mInventory[1], stack)
                && mInventory[1].getMaxStackSize() > mInventory[1].stackSize) {
                    int tmp = Math.min(count, mInventory[1].getMaxStackSize() - mInventory[1].stackSize);
                    mInventory[1].stackSize += tmp;
                    count -= tmp;
                }
            setItemCount(count);
            if (stack != null) {
                mInventory[2] = stack.copy();
                mInventory[2].stackSize = Math.min(stack.getMaxStackSize(), count);
            } else {
                mInventory[2] = null;
            }

            int extraCount = 0;
            if (mInventory[1] != null) {
                if (GTUtility.areStacksEqual(mInventory[1], stack)) {
                    extraCount = mInventory[1].stackSize;
                } else if (!isMatterCluster(mInventory[1]) && (stack == null || stack.stackSize <= 0)) {
                    extraCount = mInventory[1].stackSize;
                    stack = mInventory[1];
                }
            }

            // notifyListeners has a null check on the stack arg
            meInventoryHandler.notifyListeners(count + extraCount - lastTrueCount, stack);
            lastTrueCount = count + extraCount;
            notifyMatterClusterOutputChange();
            if (count != savedCount) getBaseMetaTileEntity().markDirty();
        }
    }

    private void insertInputItems() {
        if (mInventory[0] == null) return;

        if (isMatterCluster(mInventory[0])) {
            mInventory[0].stackSize = (int) insertMatterClusters(mInventory[0], mInventory[0].stackSize, false, false);
            if (mInventory[0].stackSize <= 0) mInventory[0] = null;
            return;
        }

        if (getItemStack() == null) setItemStack(mInventory[0].copy());
        int availableSpace = mVoidOverflow ? getMaxItemCount() : getMaxItemCount() - getItemCount();
        if (availableSpace <= 0 || !GTUtility.areStacksEqual(mInventory[0], getItemStack())) return;

        int stackToMove = Math.min(mInventory[0].stackSize, availableSpace);
        setItemCount((int) Math.min((long) getItemCount() + stackToMove, getMaxItemCount()));
        mInventory[0].stackSize -= stackToMove;
        if (mInventory[0].stackSize <= 0) mInventory[0] = null;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex != 2;
    }

    @Override
    public int getProgresstime() {
        return getItemCount() + (mInventory[0] == null ? 0 : mInventory[0].stackSize)
            + (mInventory[1] == null ? 0 : mInventory[1].stackSize);
    }

    @Override
    public int maxProgresstime() {
        return getMaxItemCount();
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {

        if (getItemStack() == null) {
            return new String[] { EnumChatFormatting.BLUE + localizedChestName() + EnumChatFormatting.RESET,
                "GT5U.infodata.digital_chest.stored_items", "GT5U.infodata.digital_chest.stored_items.empty",
                EnumChatFormatting.GREEN + "0"
                    + EnumChatFormatting.RESET
                    + " "
                    + EnumChatFormatting.YELLOW
                    + formatNumber(getMaxItemCount())
                    + EnumChatFormatting.RESET };
        }
        return new String[] { EnumChatFormatting.BLUE + localizedChestName() + EnumChatFormatting.RESET,
            "GT5U.infodata.digital_chest.stored_items",
            EnumChatFormatting.GOLD + getItemStack().getDisplayName() + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + formatNumber(getItemCount())
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + formatNumber(getMaxItemCount())
                + EnumChatFormatting.RESET };
    }

    @Override
    public ItemStack[] getStoredItemData() {
        return mInventory;
    }

    protected abstract String localizedChestName();

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mItemCount", getItemCount());
        if (getItemStack() != null) aNBT.setTag("mItemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        aNBT.setBoolean("mVoidOverflow", mVoidOverflow);
        aNBT.setBoolean("mDisableFilter", mDisableFilter);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mItemCount")) setItemCount(aNBT.getInteger("mItemCount"));
        if (aNBT.hasKey("mItemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack")));
        mVoidOverflow = aNBT.getBoolean("mVoidOverflow");
        mDisableFilter = aNBT.getBoolean("mDisableFilter");
        lastTrueCount = getItemCount();
        if (GTUtility.areStacksEqual(getItemStack(), mInventory[1])) {
            lastTrueCount += mInventory[1].stackSize;
        }
        lastMatterClusterOutput = isMatterCluster(mInventory[1]) ? GTUtility.copy(mInventory[1]) : null;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GTValues.disableDigitalChestsExternalAccess && meInventoryHandler.hasActiveMEConnection()) return false;
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GTValues.disableDigitalChestsExternalAccess && meInventoryHandler.hasActiveMEConnection()) return false;
        if (aIndex != 0) return false;
        if (isMatterCluster(aStack)) return mInventory[0] == null;
        if ((mInventory[0] != null && !GTUtility.areStacksEqual(mInventory[0], aStack))) return false;
        if (mDisableFilter) return true;
        if (getItemStack() == null) return mInventory[1] == null || GTUtility.areStacksEqual(mInventory[1], aStack);
        return GTUtility.areStacksEqual(getItemStack(), aStack);
    }

    @Override
    protected ItemSink getItemSink(ForgeDirection side) {
        return new ItemIOImpl();
    }

    @Override
    protected ItemSource getItemSource(ForgeDirection side) {
        return new ItemIOImpl();
    }

    @Override
    protected ItemIO getItemIO(ForgeDirection side) {
        return new ItemIOImpl();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side != aFacing) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_SCHEST),
            TextureFactory.builder()
                .addIcon(OVERLAY_SCHEST_GLOW)
                .glow()
                .build() };
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("itemType", Constants.NBT.TAG_COMPOUND)) {
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.digital_chest.count",
                    formatNumber(tag.getLong("itemCount"))));
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.digital_chest.type",
                    ItemStack.loadItemStackFromNBT(tag.getCompoundTag("itemType"))
                        .getDisplayName()));
        } else {
            currenttip.add(StatCollector.translateToLocal("GT5U.waila.digital_chest.empty"));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        ItemStack is = getItemStack();
        if (GTUtility.isStackInvalid(is)) return;
        long realItemCount = getItemCount();
        if (GTUtility.isStackValid(mInventory[1]) && GTUtility.areStacksEqual(mInventory[1], is))
            realItemCount += mInventory[1].stackSize;
        tag.setLong("itemCount", realItemCount);
        tag.setTag("itemType", is.writeToNBT(new NBTTagCompound()));
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();
    protected int clientItemCount;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new SlotWidget(inventoryHandler, 0)
                    .setChangeListener(() -> { if (!isMatterCluster(mInventory[0])) insertInputItems(); })
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 52))
            .widget(
                SlotWidget.phantom(inventoryHandler, 2)
                    .disableInteraction()
                    .setBackground(GTUITextures.TRANSPARENT)
                    .setPos(59, 42))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.item_amount"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(clientItemCount))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> this instanceof MTEQuantumChest ? ((MTEQuantumChest) this).mItemCount : 0,
                    value -> clientItemCount = value));

    }

    class ItemIOImpl extends SimpleItemIO {

        private static final int[] SLOTS = { 0, 1 };

        @Override
        protected @NotNull InventoryIterator iterator(int[] allowedSlots) {
            return new MTEDigitalChestBaseInventoryIterator(ItemIOImpl.SLOTS, allowedSlots, false);
        }

        @Override
        public @NotNull InventoryIterator simulatedSinkIterator() {
            return new MTEDigitalChestBaseInventoryIterator(ItemIOImpl.SLOTS, allowedSinkSlots, true);
        }

        @Override
        public OptionalInt simulateStore(ImmutableItemStack stack) {
            if (stack == null || stack.isEmpty()) return ZERO;
            // Borrowed input tags stay within this synchronous call
            return OptionalInt.of(
                store(stack, new MTEDigitalChestBaseInventoryIterator(ItemIOImpl.SLOTS, allowedSinkSlots, true, true)));
        }

        private class MTEDigitalChestBaseInventoryIterator extends AbstractInventoryIterator {

            private final boolean simulated;
            private final boolean borrowInputTag;
            private final InsertionState insertionState;

            public MTEDigitalChestBaseInventoryIterator(int[] slots, int[] allowedSlots, boolean simulated) {
                this(slots, allowedSlots, simulated, false);
            }

            private MTEDigitalChestBaseInventoryIterator(int[] slots, int[] allowedSlots, boolean simulated,
                boolean borrowInputTag) {
                super(slots, allowedSlots);
                this.simulated = simulated;
                this.borrowInputTag = borrowInputTag;
                insertionState = simulated ? new InsertionState(true) : liveInsertionState;
            }

            @Override
            protected ItemStack getStackInSlot(int slot) {
                switch (slot) {
                    case 0 -> {
                        return GTUtility
                            .copyAmountUnsafe(insertionState.getStoredCount(), insertionState.getStoredItem());
                    }
                    case 1 -> {
                        return GTUtility.copy(insertionState.getOutput());
                    }
                    default -> {
                        return null;
                    }
                }
            }

            private boolean hasStack() {
                ItemStack stack = insertionState.getStoredItem();
                return stack != null && stack.getItem() != null && insertionState.getStoredCount() > 0;
            }

            @Override
            public ItemStack extract(int amount, boolean forced) {
                switch (getCurrentSlot()) {
                    case 0 -> {
                        int toExtract = Math.min(amount, getItemCount());

                        if (toExtract <= 0) return null;

                        ItemStack extracted = GTUtility.copyAmountUnsafe(toExtract, getItemStack());

                        if (!simulated) {
                            setItemCount(getItemCount() - toExtract);

                            if (getItemCount() <= 0) {
                                setItemStack(null);
                            }

                            meInventoryHandler.notifyListeners(-toExtract, extracted);

                            MTEDigitalChestBase.this.markDirty();
                        }

                        return extracted;
                    }
                    case 1 -> {
                        ItemStack inSlot = mInventory[1];

                        if (inSlot == null) return null;

                        int toExtract = Math.min(amount, inSlot.stackSize);

                        ItemStack extracted;
                        if (!simulated) {
                            extracted = decrStackSize(1, toExtract);

                            MTEDigitalChestBase.this.markDirty();
                        } else {
                            extracted = GTUtility.copyOrNull(getStackInSlot(1));

                            if (extracted != null && extracted.stackSize > toExtract) {
                                extracted.stackSize = toExtract;
                            }
                        }

                        return extracted;
                    }
                    default -> {
                        return null;
                    }
                }
            }

            @Override
            public int insert(ImmutableItemStack stack, boolean forced) {
                Integer matterClusterRemainder = tryInsertMatterClusters(stack, forced);
                if (matterClusterRemainder != null) return matterClusterRemainder;

                int remaining = stack.getStackSize();
                if (getCurrentSlot() == 1) {
                    int max = getStackSizeLimit(1, stack.toStackFast());

                    ItemStack stored = insertionState.getOutput();

                    if (!ItemUtil.isStackEmpty(stored) && !stack.matches(stored)) {
                        return remaining;
                    }

                    int storedAmount = stored == null ? 0 : stored.stackSize;

                    int toInsert = Math.min(stack.getStackSize(), max - storedAmount);

                    if (stored == null) insertionState.setOutput(stack.toStackFast(0));
                    insertionState.getOutput().stackSize += toInsert;
                    remaining -= toInsert;
                }

                if (hasStack() && !stack.matches(insertionState.getStoredItem())) {
                    return remaining;
                }

                int insertable = Math
                    .min((forced ? Integer.MAX_VALUE : getItemCapacity()) - insertionState.getStoredCount(), remaining);

                if (insertionState.getStoredCount() <= 0) {
                    insertionState.setStoredItem(borrowInputTag ? stack.toStackFast(0) : stack.toStack(0));
                }

                insertionState.setStoredCount(insertionState.getStoredCount() + insertable);

                if (!simulated) {
                    meInventoryHandler.notifyListeners(insertable, getItemStack());

                    MTEDigitalChestBase.this.markDirty();
                }
                remaining -= insertable;

                return remaining;
            }

            private @Nullable Integer tryInsertMatterClusters(ImmutableItemStack stack, boolean forced) {
                if (getCurrentSlot() != 0) return null;
                if (!isMatterCluster(stack.getItem())) return null;

                ItemStack inputStack = stack.toStackFast();

                int previousCount = insertionState.getStoredCount();
                int remaining = (int) MTEDigitalChestBase.this
                    .insertMatterClusters(inputStack, stack.getStackSize(), insertionState, forced);
                if (!simulated && getItemCount() != previousCount) {
                    meInventoryHandler.notifyListeners(getItemCount() - previousCount, getItemStack());
                    MTEDigitalChestBase.this.markDirty();
                }
                return remaining;
            }
        }
    }

    @Override
    protected boolean useMui2() {
        return false;
    }
}
