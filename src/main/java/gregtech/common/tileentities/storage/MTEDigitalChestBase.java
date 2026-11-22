package gregtech.common.tileentities.storage;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST_GLOW;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.capability.item.ItemIO;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSource;
import com.gtnewhorizon.gtnhlib.item.AbstractInventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;
import com.gtnewhorizon.gtnhlib.item.SimpleItemIO;
import com.gtnewhorizon.gtnhlib.util.ItemUtil;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.common.Optional;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import fox.spiteful.avaritia.items.ItemStackWrapper;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IItemLockable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEDigitalChestBaseGui;
import gregtech.crossmod.ae2.IMEAwareItemInventory;
import gregtech.crossmod.ae2.MEItemInventoryHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEDigitalChestBase extends MTETieredMachineBlock
    implements IMEMonitor<IAEItemStack>, IMEAwareItemInventory, IDataCopyable, IItemLockable {

    protected boolean mVoidOverflow = false;
    private boolean mVoidFull;
    private boolean passThroughOtherItems;
    private boolean inputFilterDisabled;
    private boolean autoOutputItems;
    private ItemStack lockedItem;
    private boolean lockNextItem;
    private boolean blockExternalAccess;
    private final MEItemInventoryHandler<?> meInventoryHandler = new MEItemInventoryHandler<>(this) {

        @Override
        protected IItemList<IAEItemStack> gatherItems(IItemList<IAEItemStack> out) {
            super.gatherItems(out);
            if (mInventory[0] != null) out.add(AEItemStack.create(mInventory[0]));
            return out;
        }
    };

    private long lastTrueCount;
    private ItemStack lastStoredItem;
    private ItemStack lastExtraItem;
    private ItemStack lastInputItem;
    private boolean deferInventoryChanges;

    private final InsertionState liveInsertionState = new InsertionState(false);

    private final class InsertionState {

        private final boolean simulated;
        private final boolean borrowInputTag;
        private ItemStack item;
        private int count;
        private ItemStack output;
        private boolean outputDetached;
        private ItemStack input;
        private boolean inputDetached;

        private InsertionState(boolean simulated) {
            this(simulated, false);
        }

        private InsertionState(boolean simulated, boolean borrowInputTag) {
            this.simulated = simulated;
            this.borrowInputTag = borrowInputTag;
            if (simulated) {
                item = getItemStack();
                count = getItemCount();
                output = mInventory[1];
                input = mInventory[0];
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
                output = GTUtility.copy(output);
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

        private ItemStack getInput() {
            if (simulated && !inputDetached) {
                input = GTUtility.copy(input);
                inputDetached = true;
            }
            return simulated ? input : mInventory[0];
        }

        private void setInput(ItemStack stack) {
            if (simulated) {
                input = stack;
                inputDetached = true;
            } else mInventory[0] = stack;
        }

        private @Nullable ItemStack getTargetItemType() {
            if (lockedItem != null) return lockedItem;
            if (getStoredCount() > 0) return getStoredItem();
            ItemStack currentOutput = getOutput();
            return currentOutput != null && !isMatterCluster(currentOutput) ? currentOutput : null;
        }

        private boolean canReclaimOutput(ItemStack storedItem, long storageLimit) {
            ItemStack currentOutput = getOutput();
            return currentOutput == null || (sameItemType(currentOutput, storedItem)
                && (long) getStoredCount() + currentOutput.stackSize <= storageLimit);
        }

        private void reclaimOutput() {
            ItemStack currentOutput = getOutput();
            if (currentOutput == null) return;
            storeItems(currentOutput, currentOutput.stackSize);
            setOutput(null);
        }

        private void storeItems(ItemStack storedItem, long itemCount) {
            if (getStoredCount() <= 0) {
                setStoredItem(simulated && borrowInputTag ? storedItem : GTUtility.copyAmount(1, storedItem));
                if (!simulated && lockNextItem) {
                    lockedItem = GTUtility.copyAmount(1, storedItem);
                    lockNextItem = false;
                }
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
            GTUtility.breakLines(
                translateToLocalFormatted("GT5U.machines.digitalchest.tooltip", formatNumber(commonSizeCompute(aTier))),
                translateToLocalFormatted(
                    "GT5U.machines.digitalchest.autooutput.rate",
                    formatNumber(autoOutputLimit(aTier))),
                translateToLocal("GT5U.machines.digitalchest.tooltip1"),
                translateToLocal("GT5U.machines.digitaltank.tooltip1")));
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
        if (ItemStackNBT.hasKey(stack, "lockedItem")) {
            ItemStack locked = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("lockedItem"));
            if (locked != null) {
                tooltip.add(translateToLocalFormatted("GT5U.item.tank.locked_to", locked.getDisplayName()));
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
            ItemStack targetItem = getMatterClusterTargetItem();
            return targetItem != null && getMatterClusterEntry(stack, targetItem) != null;
        }
        return iaeItemStack != null && matchesAERequest(iaeItemStack, getTargetItemType());
    }

    @Override
    public boolean canAccept(IAEItemStack iaeItemStack) {
        if (iaeItemStack == null || iaeItemStack.getStackSize() <= 0) return false;
        IAEItemStack remainder = injectItems(iaeItemStack, Actionable.SIMULATE, null);
        return remainder == null || remainder.getStackSize() < iaeItemStack.getStackSize();
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
        if (input == null || input.getStackSize() <= 0 || getBaseMetaTileEntity() == null) return input;
        ItemStack item = input.getItemStack();
        InsertionState state = mode == Actionable.SIMULATE ? new InsertionState(true) : liveInsertionState;
        long remaining;
        if (isMatterCluster(item)) {
            if (!inputFilterDisabled && !passThroughOtherItems
                && getMatterClusterEntry(item, state.getTargetItemType()) == null) return input;
            // AE accepts the original item identity, then normal ticking unpacks the cluster
            remaining = bufferInputItems(item, input.getStackSize(), state);
            if (mode == Actionable.MODULATE) notifyInventoryChanges();
        } else {
            if (item.getTagCompound() == null) {
                ItemStack target = getTargetItemType();
                // Restore the empty tag stripped by AE
                if (target != null && target.getTagCompound() != null && matchesAERequest(input, target)) {
                    item = item.copy();
                    item.setTagCompound(new NBTTagCompound());
                }
            }
            remaining = insertExternalItems(item, input.getStackSize(), state, false);
        }
        return remaining == 0 ? null
            : input.copy()
                .setStackSize(remaining);
    }

    private static boolean isMatterCluster(ItemStack stack) {
        return Mods.Avaritia.isModLoaded() && isMatterClusterLoaded(stack);
    }

    @Optional.Method(modid = Mods.ModIDs.AVARITIA)
    private static boolean isMatterClusterLoaded(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemMatterCluster;
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
        ItemStack currentItem = state.getTargetItemType();
        MatterClusterEntry entry = getMatterClusterEntry(cluster, currentItem);
        long storageLimit = forced ? Integer.MAX_VALUE : getItemCapacity();
        if (entry == null) return forced || passThroughOtherItems
            ? passMatterClusterThrough(cluster, clusterCount, currentItem, storageLimit, state)
            : clusterCount;
        long storedCount = state.getStoredCount();
        ItemStack output = state.getOutput();
        if (sameItemType(output, entry.item)) storedCount += output.stackSize;
        long availableSpace = Math.max(0, storageLimit - storedCount);
        long wholeClusters = getWholeClustersToConsume(entry, clusterCount, availableSpace);
        if (wholeClusters > 0) {
            long itemsToStore = Math.min(availableSpace, saturatedMultiply(wholeClusters, entry.itemCount));
            if (itemsToStore > 0) state.storeItems(entry.item, itemsToStore);
            if (!state.simulated) markDirty();
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
        if (currentItem == null || !state.canReclaimOutput(currentItem, storageLimit)) return clusterCount;
        state.reclaimOutput();
        ItemStack output = cluster.copy();
        output.stackSize = 1;
        state.setOutput(output);
        if (!state.simulated) {
            markDirty();
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
        if (needsOutput && !state.canReclaimOutput(entry.item, storageLimit)) return clusterCount;
        int itemsToStore = (int) Math.min(availableSpace, itemsToConsume);
        if (itemsToStore > 0) state.storeItems(entry.item, itemsToStore);
        if (needsOutput) {
            state.reclaimOutput();
            ItemStack remainder = cluster.copy();
            remainder.stackSize = 1;
            removeMatterClusterItems(remainder, entry.item, itemsToConsume, remainderCount);
            state.setOutput(remainder);
        }
        if (!state.simulated) {
            markDirty();
        }
        return clusterCount - 1;
    }

    private @Nullable ItemStack getMatterClusterTargetItem() {
        return liveInsertionState.getTargetItemType();
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
        if (targetItem != null) return sameItemType(targetItem, candidate.getKey().stack);
        return selected == null || candidate.getValue() > selected.getValue();
    }

    @Optional.Method(modid = Mods.ModIDs.AVARITIA)
    private static void removeMatterClusterItems(ItemStack cluster, ItemStack item, int amount, int remainderCount) {
        Map<ItemStackWrapper, Integer> contents = ItemMatterCluster.getClusterData(cluster);
        for (Iterator<Map.Entry<ItemStackWrapper, Integer>> iterator = contents.entrySet()
            .iterator(); iterator.hasNext();) {
            Map.Entry<ItemStackWrapper, Integer> content = iterator.next();
            if (!sameItemType(content.getKey().stack, item)) continue;

            int remaining = content.getValue() - amount;
            if (remaining > 0) content.setValue(remaining);
            else iterator.remove();
            break;
        }
        ItemMatterCluster.setClusterData(cluster, contents, remainderCount);
    }

    private void notifyInventoryChanges() {
        if (deferInventoryChanges) return;
        ItemStack stored = getItemCount() > 0 ? getItemStack() : null;
        long count = getItemCount();
        ItemStack extra = mInventory[1];
        ItemStack input = mInventory[0];
        if (sameItemType(stored, input)) {
            count += input.stackSize;
            input = null;
        }
        if (sameItemType(stored, extra)) {
            count += extra.stackSize;
            extra = null;
        }
        ItemStack previousStored = lastStoredItem;
        long previousCount = lastTrueCount;
        ItemStack previousExtra = lastExtraItem;
        ItemStack previousInput = lastInputItem;
        if (!sameItemType(lastStoredItem, stored)) lastStoredItem = GTUtility.copyAmount(1, stored);
        if (extra == null) lastExtraItem = null;
        else if (!sameItemType(lastExtraItem, extra) || lastExtraItem.stackSize != extra.stackSize) {
            lastExtraItem = extra.copy();
        }
        if (input == null) lastInputItem = null;
        else if (!sameItemType(lastInputItem, input) || lastInputItem.stackSize != input.stackSize) {
            lastInputItem = input.copy();
        }
        lastTrueCount = count;
        stored = lastStoredItem;
        extra = lastExtraItem;
        input = lastInputItem;
        if (sameItemType(previousStored, stored)) {
            notifyItemDelta(count - previousCount, stored);
        } else {
            notifyItemDelta(-previousCount, previousStored);
            notifyItemDelta(count, stored);
        }
        notifyStackChange(previousExtra, extra);
        notifyStackChange(previousInput, input);
    }

    private void notifyStackChange(@Nullable ItemStack previous, @Nullable ItemStack current) {
        if (sameItemType(previous, current)) {
            notifyItemDelta((long) current.stackSize - previous.stackSize, current);
        } else {
            if (previous != null) notifyItemDelta(-previous.stackSize, previous);
            if (current != null) notifyItemDelta(current.stackSize, current);
        }
    }

    private void notifyItemDelta(long amount, @Nullable ItemStack item) {
        while (amount != 0 && item != null) {
            int delta = (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, amount));
            meInventoryHandler.notifyListeners(delta, item);
            amount -= delta;
        }
    }

    private static boolean sameItemType(@Nullable ItemStack left, @Nullable ItemStack right) {
        return left != null && right != null
            && left.getItem() == right.getItem()
            && ItemUtil.getStackMeta(left) == ItemUtil.getStackMeta(right)
            && Objects.equals(left.getTagCompound(), right.getTagCompound());
    }

    private static boolean matchesAERequest(IAEItemStack request, @Nullable ItemStack stored) {
        if (stored == null || request.getItem() != stored.getItem()
            || request.getItemDamage() != ItemUtil.getStackMeta(stored)) return false;
        NBTTagCompound tag = stored.getTagCompound();
        // AE represents an empty compound as no tag
        return request.getTagCompound() == null ? tag == null || tag.hasNoTags()
            : request.getTagCompound()
                .equals(tag);
    }

    private static long saturatedMultiply(long left, int right) {
        return left > Long.MAX_VALUE / right ? Long.MAX_VALUE : left * right;
    }

    @Override
    public IAEItemStack extractItems(final IAEItemStack request, final Actionable mode, final BaseActionSource src) {
        if (request == null || request.getStackSize() <= 0 || getBaseMetaTileEntity() == null) return null;
        int stored = matchesAERequest(request, getItemStack()) ? getItemCount() : 0;
        int output = matchesAERequest(request, mInventory[1]) ? mInventory[1].stackSize : 0;
        int input = matchesAERequest(request, mInventory[0]) ? mInventory[0].stackSize : 0;
        long extracted = Math.min(request.getStackSize(), (long) stored + output + input);
        if (extracted <= 0) return null;
        if (mode == Actionable.MODULATE) {
            int fromStorage = (int) Math.min(stored, extracted);
            setItemCount(getItemCount() - fromStorage);
            int fromOutput = (int) Math.min(output, extracted - fromStorage);
            if (fromOutput > 0) {
                mInventory[1].stackSize -= fromOutput;
                if (mInventory[1].stackSize <= 0) mInventory[1] = null;
            }
            int fromInput = (int) (extracted - fromStorage - fromOutput);
            if (fromInput > 0) {
                mInventory[0].stackSize -= fromInput;
                if (mInventory[0].stackSize <= 0) mInventory[0] = null;
            }
            if (getItemCount() <= 0) setItemStack(null);
            markDirty();
            notifyInventoryChanges();
        }
        return request.copy()
            .setStackSize(extracted);
    }

    @Override
    public StorageChannel getChannel() {
        return meInventoryHandler.getChannel();
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        setVoidOverflow(!mVoidOverflow);
        GTUtility.sendChatTrans(
            aPlayer,
            mVoidOverflow ? "GT5U.machines.digitalchest.voidoverflow.enabled"
                : "GT5U.machines.digitalchest.voidoverflow.disabled");
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        setInputFilterDisabled(!isInputFilterDisabled());
        GTUtility.sendChatTrans(
            aPlayer,
            inputFilterDisabled ? "GT5U.machines.digitalchest.inputfilter.disabled"
                : "GT5U.machines.digitalchest.inputfilter.enabled");
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (mVoidFull) {
            if (getItemCount() != 0 || getItemStack() != null
                || mInventory[0] != null
                || mInventory[1] != null
                || mInventory[2] != null) {
                setItemCount(0);
                setItemStack(null);
                Arrays.fill(mInventory, null);
                markDirty();
            }
            notifyInventoryChanges();
            return;
        }
        if (getItemCount() <= 0) {
            setItemStack(null);
            setItemCount(0);
        }
        // Publish routing changes only after both source and destination have been updated
        deferInventoryChanges = true;
        insertInputItems();
        ItemStack stored = getItemStack();
        if (!isOutputReserved() && getItemCount() > 0
            && stored != null
            && (mInventory[1] == null || sameItemType(stored, mInventory[1]))) {
            int outputCount = mInventory[1] == null ? 0 : mInventory[1].stackSize;
            int toOutput = Math.min(
                getItemCount(),
                Math.max(0, Math.min(stored.getMaxStackSize(), getInventoryStackLimit()) - outputCount));
            if (toOutput > 0) {
                if (mInventory[1] == null) mInventory[1] = GTUtility.copyAmount(toOutput, stored);
                else mInventory[1].stackSize += toOutput;
                setItemCount(getItemCount() - toOutput);
                markDirty();
            }
        }
        mInventory[2] = null;
        pushOutputItems(aBaseMetaTileEntity, aTimer);
        deferInventoryChanges = false;
        notifyInventoryChanges();
    }

    private void pushOutputItems(IGregTechTileEntity baseMetaTileEntity, long timer) {
        if (!autoOutputItems || timer % 20 != 0 || mInventory[1] == null || isExternalAccessBlocked()) return;

        int limit = autoOutputLimit(mTier);
        GTItemTransfer transfer = new GTItemTransfer();
        transfer.outOfMachine(this, baseMetaTileEntity.getFrontFacing());
        transfer.setSourceSlots(1);
        transfer.setStacksToTransfer(1);
        transfer.setMaxItemsPerTransfer(limit);
        transfer.setMaxTotalTransferred(limit);
        int remaining = limit - transfer.transfer();
        if (remaining <= 0 || mInventory[1] != null || getItemCount() <= 0 || isOutputReserved()) return;

        transfer.setSourceSlots(0);
        transfer.setMaxItemsPerTransfer(remaining);
        transfer.setMaxTotalTransferred(remaining);
        transfer.transfer();
    }

    private static int autoOutputLimit(int tier) {
        return 64 << Math.max(0, tier - 1);
    }

    private boolean isOutputReserved() {
        ItemStack pending = mInventory[0];
        return pending != null && (isMatterCluster(pending)
            ? getItemCount() < getItemCapacity()
                && (passThroughOtherItems || getMatterClusterEntry(pending, getMatterClusterTargetItem()) != null)
            : passThroughOtherItems && !sameItemType(pending, getTargetItemType()));
    }

    private void insertInputItems() {
        if (mInventory[0] == null) return;

        if (isMatterCluster(mInventory[0])) {
            mInventory[0].stackSize = (int) insertMatterClusters(mInventory[0], mInventory[0].stackSize, false, false);
            if (mInventory[0].stackSize <= 0) mInventory[0] = null;
            return;
        }

        int previous = mInventory[0].stackSize;
        mInventory[0].stackSize = (int) insertOrdinaryItems(mInventory[0], previous, false, false);
        if (previous != mInventory[0].stackSize) markDirty();
        if (mInventory[0].stackSize <= 0) mInventory[0] = null;
    }

    private long insertOrdinaryItems(ItemStack item, long amount, boolean simulate, boolean forced) {
        return insertOrdinaryItems(item, amount, simulate ? new InsertionState(true) : liveInsertionState, forced);
    }

    private long insertExternalItems(ItemStack item, long amount, InsertionState state, boolean forced) {
        if (item == null || item.getItem() == null || amount <= 0) return amount;
        long remaining = isMatterCluster(item) ? insertMatterClusters(item, amount, state, forced)
            : insertOrdinaryItems(item, amount, state, forced);
        if (remaining > 0 && !forced && inputFilterDisabled) {
            remaining = bufferInputItems(item, remaining, state);
        }
        if (!state.simulated) notifyInventoryChanges();
        return remaining;
    }

    private long bufferInputItems(ItemStack item, long amount, InsertionState state) {
        ItemStack input = state.getInput();
        if (input != null && !sameItemType(input, item)) return amount;
        int existing = input == null ? 0 : input.stackSize;
        int accepted = (int) Math
            .min(amount, Math.max(0, Math.min(item.getMaxStackSize(), getInventoryStackLimit()) - existing));
        if (accepted > 0) {
            if (input == null) state.setInput(GTUtility.copyAmount(accepted, item));
            else input.stackSize += accepted;
            if (!state.simulated) {
                markDirty();
            }
        }
        return amount - accepted;
    }

    private long insertOrdinaryItems(ItemStack item, long amount, InsertionState state, boolean forced) {
        if (item == null || item.getItem() == null || amount <= 0) return amount;
        ItemStack target = forced ? (state.getStoredCount() > 0 ? state.getStoredItem() : null)
            : state.getTargetItemType();
        if ((target != null && !sameItemType(target, item))
            || (state.getStoredCount() > 0 && !sameItemType(state.getStoredItem(), item))) {
            return !forced && passThroughOtherItems ? passOrdinaryItemsThrough(item, amount, state) : amount;
        }
        long space = Math.max(0L, (forced ? Integer.MAX_VALUE : getItemCapacity()) - (long) state.getStoredCount());
        int stored = (int) Math.min(space, amount);
        if (stored > 0) {
            state.storeItems(item, stored);
            if (!state.simulated) {
                markDirty();
            }
        }
        return !forced && mVoidOverflow ? 0 : amount - stored;
    }

    private long passOrdinaryItemsThrough(ItemStack item, long amount, InsertionState state) {
        ItemStack output = state.getOutput();
        ItemStack target = state.getTargetItemType();
        boolean reclaim = output != null && sameItemType(output, target);
        if (reclaim) {
            if (!state.canReclaimOutput(target, getItemCapacity())) return amount;
        } else if (output != null && !sameItemType(output, item)) return amount;
        int existing = output == null || reclaim ? 0 : output.stackSize;
        int accepted = (int) Math
            .min(amount, Math.max(0, Math.min(item.getMaxStackSize(), getInventoryStackLimit()) - existing));
        if (accepted > 0) {
            if (reclaim) state.reclaimOutput();
            if (state.getOutput() == null) state.setOutput(GTUtility.copyAmount(accepted, item));
            else state.getOutput().stackSize += accepted;
            if (!state.simulated) {
                markDirty();
            }
        }
        return amount - accepted;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            if (aBaseMetaTileEntity.isServerSide()) {
                boolean pasted = pasteCopiedData(aPlayer, dataStick.stackTagCompound);
                aPlayer.addChatMessage(
                    new ChatComponentTranslation(
                        pasted ? "GT5U.gui.text.data_stick.loaded" : "GT5U.gui.text.data_stick.invalid"));
            }
            return true;
        }
        openGui(aPlayer);
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player) {
        if (!baseMetaTileEntity.isServerSide()) return;
        ItemStack dataStick = player.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            super.onLeftclick(baseMetaTileEntity, player);
            return;
        }
        dataStick.stackTagCompound = getCopiedData(player);
        dataStick.setStackDisplayName(translateToLocal("GT5U.machines.digitalchest.data_stick.name"));
        player.addChatMessage(new ChatComponentTranslation("GT5U.gui.text.data_stick.saved"));
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound data = new NBTTagCompound();
        data.setString("type", getCopiedDataIdentifier(player));
        data.setBoolean("blockExternalAccess", blockExternalAccess);
        return data;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, @Nullable NBTTagCompound nbt) {
        if (nbt == null || !getCopiedDataIdentifier(player).equals(nbt.getString("type"))
            || !nbt.hasKey("blockExternalAccess", Constants.NBT.TAG_BYTE)) return false;
        setBlockExternalAccess(nbt.getBoolean("blockExternalAccess"));
        return true;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return "digitalChestAccess";
    }

    public void setBlockExternalAccess(boolean value) {
        blockExternalAccess = value;
        markDirty();
    }

    public boolean isBlockExternalAccess() {
        return blockExternalAccess;
    }

    public boolean isPassThroughEnabled() {
        return passThroughOtherItems;
    }

    public void setPassThroughEnabled(boolean enabled) {
        passThroughOtherItems = enabled;
        markDirty();
    }

    public boolean isInputFilterDisabled() {
        return inputFilterDisabled;
    }

    public void setInputFilterDisabled(boolean disabled) {
        inputFilterDisabled = disabled;
        markDirty();
    }

    public boolean isAutoOutputEnabled() {
        return autoOutputItems;
    }

    public void setAutoOutputEnabled(boolean enabled) {
        autoOutputItems = enabled;
        markDirty();
    }

    public boolean isVoidOverflow() {
        return mVoidOverflow;
    }

    public void setVoidOverflow(boolean enabled) {
        mVoidOverflow = enabled;
        if (enabled) mVoidFull = false;
        markDirty();
    }

    public boolean isVoidFull() {
        return mVoidFull;
    }

    public void setVoidFull(boolean enabled) {
        mVoidFull = enabled;
        if (enabled) {
            mVoidOverflow = false;
            lockedItem = null;
            lockNextItem = false;
        }
        markDirty();
    }

    @Override
    public @Nullable ItemStack getLockedItem() {
        return lockedItem;
    }

    @Override
    public void setLockedItem(@Nullable ItemStack item) {
        if (mVoidFull && !ItemUtil.isStackEmpty(item)) return;
        ItemStack storedType = getStoredItemType();
        if (!ItemUtil.isStackEmpty(item)
            && (isMatterCluster(item) || (storedType != null && !sameItemType(storedType, item)))) return;
        lockedItem = ItemUtil.isStackEmpty(item) ? null : GTUtility.copyAmount(1, item);
        lockNextItem = false;
        markDirty();
    }

    private @Nullable ItemStack getStoredItemType() {
        if (getItemCount() > 0) return getItemStack();
        ItemStack output = mInventory[1];
        if (output != null && !isMatterCluster(output) && (lockedItem == null || sameItemType(lockedItem, output)))
            return output;
        return null;
    }

    private @Nullable ItemStack getTargetItemType() {
        return lockedItem != null ? lockedItem : getStoredItemType();
    }

    @Override
    public void clearLock() {
        setLockedItem(null);
    }

    @Override
    public boolean isLocked() {
        return lockNextItem || lockedItem != null;
    }

    public void setItemLockEnabled(boolean enabled) {
        if (enabled == isLocked() || (enabled && mVoidFull)) return;
        if (!enabled) {
            clearLock();
            return;
        }
        ItemStack stored = getStoredItemType();
        if (stored != null) {
            setLockedItem(stored);
        } else {
            lockNextItem = true;
            markDirty();
        }
    }

    private boolean isExternalAccessBlocked() {
        return blockExternalAccess && meInventoryHandler.hasActiveMEConnection();
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
        ItemStack[] contents = mInventory.clone();
        ItemStack stored = getItemStack();
        int count = getItemCount();
        contents[2] = null;
        if (stored != null && count > 0) {
            contents[2] = stored.copy();
            contents[2].stackSize = Math.min(stored.getMaxStackSize(), count);
        }
        return contents;
    }

    protected abstract String localizedChestName();

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mItemCount", getItemCount());
        if (getItemStack() != null) aNBT.setTag("mItemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        aNBT.setBoolean("mVoidOverflow", mVoidOverflow);
        aNBT.setBoolean("mVoidFull", mVoidFull);
        aNBT.setBoolean("mDisableFilter", inputFilterDisabled);
        aNBT.setBoolean("passThroughOtherItems", passThroughOtherItems);
        aNBT.setBoolean("autoOutputItems", autoOutputItems);
        aNBT.setBoolean("blockExternalAccess", blockExternalAccess);
        if (lockedItem != null) aNBT.setTag("lockedItem", lockedItem.writeToNBT(new NBTTagCompound()));
        if (lockNextItem) aNBT.setBoolean("lockNextItem", true);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (mVoidFull) aNBT.setBoolean("mVoidFull", true);
        if (inputFilterDisabled) aNBT.setBoolean("mDisableFilter", true);
        if (passThroughOtherItems) aNBT.setBoolean("passThroughOtherItems", true);
        if (autoOutputItems) aNBT.setBoolean("autoOutputItems", true);
        if (blockExternalAccess) aNBT.setBoolean("blockExternalAccess", true);
        if (lockedItem != null) aNBT.setTag("lockedItem", lockedItem.writeToNBT(new NBTTagCompound()));
        if (lockNextItem) aNBT.setBoolean("lockNextItem", true);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mItemCount")) setItemCount(aNBT.getInteger("mItemCount"));
        if (aNBT.hasKey("mItemStack"))
            setItemStack(ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack")));
        mVoidOverflow = aNBT.getBoolean("mVoidOverflow");
        mVoidFull = aNBT.getBoolean("mVoidFull");
        inputFilterDisabled = aNBT.getBoolean("mDisableFilter");
        passThroughOtherItems = aNBT.getBoolean("passThroughOtherItems");
        autoOutputItems = aNBT.getBoolean("autoOutputItems");
        blockExternalAccess = aNBT.getBoolean("blockExternalAccess");
        lockedItem = aNBT.hasKey("lockedItem", Constants.NBT.TAG_COMPOUND)
            ? GTUtility.copyAmount(1, ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("lockedItem")))
            : null;
        lockNextItem = !mVoidFull && lockedItem == null && aNBT.getBoolean("lockNextItem");
        if (mVoidFull) {
            mVoidOverflow = false;
            lockedItem = null;
        }
        deferInventoryChanges = false;
        lastStoredItem = getItemCount() > 0 ? GTUtility.copyAmount(1, getItemStack()) : null;
        lastTrueCount = getItemCount();
        if (sameItemType(lastStoredItem, mInventory[1])) {
            lastTrueCount += mInventory[1].stackSize;
        }
        lastExtraItem = sameItemType(lastStoredItem, mInventory[1]) ? null : GTUtility.copy(mInventory[1]);
        if (sameItemType(lastStoredItem, mInventory[0])) {
            lastTrueCount += mInventory[0].stackSize;
        }
        lastInputItem = sameItemType(lastStoredItem, mInventory[0]) ? null : GTUtility.copy(mInventory[0]);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (isExternalAccessBlocked()) return false;
        return aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (isExternalAccessBlocked()) return false;
        if (aIndex != 0) return false;
        if (isMatterCluster(aStack)) return mInventory[0] == null && (inputFilterDisabled || passThroughOtherItems
            || getMatterClusterEntry(aStack, getMatterClusterTargetItem()) != null);
        if ((mInventory[0] != null && !sameItemType(mInventory[0], aStack))) return false;
        ItemStack target = getTargetItemType();
        return inputFilterDisabled || passThroughOtherItems || target == null || sameItemType(target, aStack);
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
        if (side == aFacing)
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_PIPE) };
        ForgeDirection displaySide = aFacing == ForgeDirection.UP ? ForgeDirection.NORTH : ForgeDirection.UP;
        if (side != displaySide) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
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
        if (GTUtility.isStackValid(mInventory[1]) && sameItemType(mInventory[1], is))
            realItemCount += mInventory[1].stackSize;
        tag.setLong("itemCount", realItemCount);
        tag.setTag("itemType", is.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEDigitalChestBaseGui(this).build(data, syncManager, uiSettings);
    }

    class ItemIOImpl extends SimpleItemIO {

        private static final int[] SLOTS = { 0, 1 };

        @Override
        protected @NotNull InventoryIterator iterator(int[] allowedSlots) {
            return new MTEDigitalChestBaseInventoryIterator(ItemIOImpl.SLOTS, allowedSlots, false);
        }

        @Override
        public @Nullable InventoryIterator simulatedSinkIterator() {
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
            private final InsertionState insertionState;

            public MTEDigitalChestBaseInventoryIterator(int[] slots, int[] allowedSlots, boolean simulated) {
                this(slots, allowedSlots, simulated, false);
            }

            private MTEDigitalChestBaseInventoryIterator(int[] slots, int[] allowedSlots, boolean simulated,
                boolean borrowInputTag) {
                super(slots, allowedSlots);
                this.simulated = simulated;
                insertionState = simulated ? new InsertionState(true, borrowInputTag) : liveInsertionState;
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

            @Override
            public ItemStack extract(int amount, boolean forced) {
                if (!forced && isExternalAccessBlocked()) return null;
                if (amount <= 0) return null;
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

                            notifyInventoryChanges();
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
                            notifyInventoryChanges();
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
                if (!forced && isExternalAccessBlocked()) return stack.getStackSize();
                if (stack.getStackSize() <= 0) return stack.getStackSize();
                ItemStack item = stack.toStackFast();
                if (ItemUtil.getStackMeta(item) != stack.getItemMeta()) {
                    item = GTUtility.copyMetaData(stack.getItemMeta(), item);
                }
                if (getCurrentSlot() == 0) {
                    return (int) insertExternalItems(item, stack.getStackSize(), insertionState, forced);
                }
                if (getCurrentSlot() != 1) return stack.getStackSize();
                ItemStack target = insertionState.getTargetItemType();
                if (!forced && target != null && !sameItemType(item, target)) return stack.getStackSize();
                ItemStack output = insertionState.getOutput();
                if (output != null && !sameItemType(item, output)) return stack.getStackSize();
                int existing = output == null ? 0 : output.stackSize;
                int inserted = Math.min(stack.getStackSize(), Math.max(0, getStackSizeLimit(1, item) - existing));
                if (inserted > 0) {
                    if (output == null) insertionState.setOutput(GTUtility.copyAmount(inserted, item));
                    else output.stackSize += inserted;
                    if (!simulated) {
                        MTEDigitalChestBase.this.markDirty();
                        notifyInventoryChanges();
                    }
                }
                return stack.getStackSize() - inserted;
            }

        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }
}
