package gregtech.common.tileentities.machines.outputme.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.common.covers.modes.FilterType.BLACKLIST;
import static gregtech.common.covers.modes.FilterType.WHITELIST;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.IncludeExclude;
import appeng.api.networking.GridFlags;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.storage.IBaseMonitor;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.ICellInventory;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.MEMonitorHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IAEStackType;
import appeng.api.util.AEColor;
import appeng.items.AEBaseCell;
import appeng.items.contents.CellConfig;
import appeng.items.storage.ItemVoidStorageCell;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.me.storage.CellInventory;
import appeng.me.storage.CellInventoryHandler;
import appeng.me.storage.MEInventoryHandler;
import appeng.util.IterationCounter;
import appeng.util.Platform;
import appeng.util.ReadableNumberConverter;
import appeng.util.prioitylist.OreFilteredList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.outputme.util.AECacheCounter;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEHatchOutputMEBase<T extends IAEStack<T>> {

    public interface Environment<T extends IAEStack<T>> {

        @Nullable
        IGregTechTileEntity getBaseMetaTileEntity();

        IGridProxyable getIGridProxyable();

        StorageChannel getChannel();

        ItemStack getCellStack();

        ISaveProvider getISaveProvider();

        BaseActionSource getActionSource();

        EntityPlayer getLastClickedPlayer();

        IMEInventory<T> getNetworkInvtory() throws GridAccessException;

        NBTTagCompound saveStackToNBT(T stack);

        T loadStackFromNBT(NBTTagCompound tag);

        byte getColor();

        String getCopiedDataIdentifier(EntityPlayer player);

        ItemStack getVisual();

        void dispatchMarkDirty();

        MTEHatchOutputMEBase<T> getProvider();

        String getEnableKey();

        String getDisableKey();
    }

    private final Environment<T> env;
    protected AENetworkProxy proxy;
    protected final AECacheCounter<T> cache = new AECacheCounter<>();
    public static long DEFAULT_CAPACITY;
    protected long baseCapacity;
    protected long cacheCapacity;

    public MTEHatchOutputMEBase(Environment<T> env, final long baseCapacity) {
        this.env = env;
        DEFAULT_CAPACITY = baseCapacity;
        this.baseCapacity = baseCapacity;
        updateState();
    }

    public AENetworkProxy getProxy() {
        if (proxy == null) {
            proxy = new AENetworkProxy(env.getIGridProxyable(), "proxy", env.getVisual(), true);
            proxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            proxy.setValidSides(EnumSet.noneOf(ForgeDirection.class));
            updateValidGridProxySides();
            var bmte = env.getBaseMetaTileEntity();
            if (bmte != null && bmte.getWorld() != null) proxy.setOwner(
                bmte.getWorld()
                    .getPlayerEntityByName(bmte.getOwnerName()));
        }
        return proxy;
    }

    private boolean wasActive = false;

    public void updateCell() {
        final boolean currentActive = getProxy().isActive();
        if (this.wasActive != currentActive) {
            this.wasActive = currentActive;
            this.updateCellArray();
        }
    }

    public List<IMEInventoryHandler> getCellArray(final StorageChannel channel) {
        if (cacheMode && this.getProxy()
            .isActive() && channel == env.getChannel()) {
            if (cellRead != null) return Collections.singletonList(cellRead);
        }
        return Collections.emptyList();
    }

    public int getPriority() {
        return myPriority;
    }

    public void setPriority(int newValue) {
        myPriority = newValue;
        isCached = false;
        updateState();
        env.dispatchMarkDirty();
    }

    @Nullable
    OutputMonitorHandler<T> cell;
    @Nullable
    OutputMonitorHandler<T> cellRead;
    @Nullable
    CellInventoryHandler<T> handler;
    private ItemStack oldCellStack = null;
    private int myPriority = 0;

    boolean cacheMode = false;
    boolean isCached = false;
    boolean checkMode = false;

    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            setCheckMode(!checkMode);
        } else {
            setCacheMode(!cacheMode);
            updateState();
            cellToCacheTransfer();
            this.updateCellArray();
        }
        env.dispatchMarkDirty();
    }

    boolean additionalConnection = false;

    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        setAdditionalConnection(!additionalConnection);
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation("GT5U.hatch.additionalConnection." + additionalConnection));
        return true;
    }

    public void updateValidGridProxySides() {
        if (additionalConnection) {
            getProxy().setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
        } else {
            getProxy().setValidSides(
                EnumSet.of(
                    env.getBaseMetaTileEntity()
                        .getFrontFacing()));
        }
    }

    public boolean getAdditionalConnection() {
        return additionalConnection;
    }

    public void setAdditionalConnection(boolean connects) {
        additionalConnection = connects;
        updateValidGridProxySides();
    }

    public void updateState() {
        if (this.isCached) {
            return;
        }
        this.isCached = true;
        this.cell = null;
        this.cellRead = null;
        this.handler = null;
        final ItemStack is = env.getCellStack();
        if (is != null) {
            final IMEInventoryHandler<T> cell = AEApi.instance()
                .registries()
                .cell()
                .getCellInventory(is, env.getISaveProvider(), env.getChannel());
            if (cell != null) {
                this.cell = this.wrap(cell, AccessRestriction.READ_WRITE);
                this.cellRead = this.wrap(cell, AccessRestriction.READ);
                if (this.cell != null) this.handler = this.cell.getCellInventoryHandler();
                env.dispatchMarkDirty();
            }
        }
        updateCacheCapacity(is);
    }

    private static class OutputMonitorHandler<T extends IAEStack<T>> extends MEMonitorHandler<T> {

        public OutputMonitorHandler(final IMEInventoryHandler<T> t) {
            super(t);
        }

        public @Nullable CellInventoryHandler<T> getCellInventoryHandler() {
            var inv = getHandler().getInternal();
            if (inv instanceof CellInventoryHandler ci) return (CellInventoryHandler<T>) ci;
            return null;
        }
    }

    private <StackType extends IAEStack<StackType>> OutputMonitorHandler<StackType> wrap(
        final IMEInventoryHandler<StackType> h, final AccessRestriction myAccess) {
        if (h == null) {
            return null;
        }

        final MEInventoryHandler<StackType> ih = new MEInventoryHandler<>(
            h,
            (IAEStackType<StackType>) h.getStackType());
        ih.setPriority(this.myPriority);
        ih.setBaseAccess(myAccess);

        final OutputMonitorHandler<StackType> g = new OutputMonitorHandler<>(ih);
        g.addListener(new OutputNetNotifier(h.getChannel()), g);

        return g;
    }

    private class OutputNetNotifier implements IMEMonitorHandlerReceiver<IAEStack<?>> {

        private final StorageChannel chan;

        public OutputNetNotifier(final StorageChannel chan) {
            this.chan = chan;
        }

        @Override
        public boolean isValid(final Object verificationToken) {
            if (this.chan == env.getChannel()) {
                return verificationToken == cell;
            }
            return false;
        }

        @Override
        public void postChange(final IBaseMonitor<IAEStack<?>> monitor, final Iterable<IAEStack<?>> change,
            final BaseActionSource source) {
            try {
                if (getProxy().isActive()) {
                    getProxy().getStorage()
                        .postAlterationOfStoredItems(this.chan, change, env.getActionSource());
                }
            } catch (final GridAccessException e) {
                // :(
            }
        }

        @Override
        public void onListUpdate() {
            try {
                MTEHatchOutputMEBase.this.updateCellArray();
                final IStorageGrid gs = MTEHatchOutputMEBase.this.getProxy()
                    .getStorage();
                Platform.postChanges(gs, null, env.getCellStack(), env.getActionSource());
            } catch (GridAccessException e) {
                // :(
            }
        }
    }

    public void onContentsChanged(int slot) {
        if (slot != 0) return;

        ItemStack upgradeItemStack = env.getCellStack();
        if (GTUtility.areStacksEqualOrNull(oldCellStack, upgradeItemStack)) {
            return;
        }

        if (upgradeItemStack != null) {
            Item item = upgradeItemStack.getItem();
            boolean isCell = item instanceof AEBaseCell || item instanceof ItemVoidStorageCell;
            if (!isCell) return;
        }

        if (this.isCached) {
            this.isCached = false;
            updateState();
        }
        sendFilterMessage();

        if (cacheMode) {
            try {
                this.updateCellArray();
                final IStorageGrid gs = this.getProxy()
                    .getStorage();
                Platform.postChanges(gs, oldCellStack, upgradeItemStack, env.getActionSource());
            } catch (final GridAccessException ignored) {}
        }

        oldCellStack = upgradeItemStack;
        env.dispatchMarkDirty();
    }

    private void sendFilterMessage() {
        if (env.getLastClickedPlayer() == null || !GTUtility.isServer()) {
            return;
        }
        ItemStack upgradeItemStack = env.getCellStack();
        if (upgradeItemStack == null || !(upgradeItemStack.getItem() instanceof ICellWorkbenchItem cellWorkbenchItem)
            || !isFiltered()) {
            IChatComponent msg = new ChatComponentTranslation(env.getDisableKey());
            GTUtility.sendChatComp(env.getLastClickedPlayer(), msg);
            return;
        }
        CellConfig cfg = (CellConfig) cellWorkbenchItem.getConfigAEInventory(upgradeItemStack);

        String modeKey = isWhiteList() ? WHITELIST.getKey() : BLACKLIST.getKey();
        IChatComponent msg = new ChatComponentTranslation(env.getEnableKey())
            .appendSibling(new ChatComponentTranslation(modeKey).appendText(": "));
        if (handler.getPartitionList() instanceof OreFilteredList) {
            ICellInventory<T> ci = handler.getCellInv();
            if (ci != null) {
                msg.appendText(ci.getOreFilter());
            }
        } else {
            for (int i = 0; i < cfg.getSizeInventory(); i++) {
                IAEStack<?> stack = cfg.getAEStackInSlot(i);
                if (stack != null) {
                    msg.appendSibling(stack.getChatComponent());
                }
            }
        }
        GTUtility.sendChatComp(env.getLastClickedPlayer(), msg);
    }

    boolean isVoidCell = false;

    private void updateCacheCapacity(ItemStack stack) {
        isVoidCell = false;
        cacheCapacity = baseCapacity;

        if (stack == null) return;

        var cell = stack.getItem();
        if (cell instanceof ItemVoidStorageCell) {
            isVoidCell = true;
            cacheCapacity = Long.MAX_VALUE;
            return;
        }

        if (handler != null && handler.getCellInv() instanceof CellInventory<?>cellInv) {
            cacheCapacity = cellInv.getRemainingItemCount() + cellInv.getStoredItemCount();
        }
    }

    long lastOutputTick = 0;
    long tickCounter = 0;

    public final long getTickCounter() {
        return tickCounter;
    }

    public boolean hasAvailableSpace() {
        return getCachedAmount() < getCacheCapacity();
    }

    public long getAvailableSpace() {
        return getCacheCapacity() - getCachedAmount();
    }

    public long getCachedAmount() {
        return cache.getTotal();
    }

    public long getCachedAmount(T key) {
        return cache.get(key);
    }

    /**
     * Note: this may return {@link Long#MAX_VALUE} for void cells.
     *
     * @return the cache capacity.
     */
    public long getCacheCapacity() {
        return cacheCapacity;
    }

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            tickCounter = aTick;
            if (tickCounter > (lastOutputTick + 40)) flushCachedStack();
            if (tickCounter % 20 == 0) aBaseMetaTileEntity.setActive(getProxy().isActive());
        }
    }

    public void setItemNBT(NBTTagCompound aNBT) {
        if (baseCapacity != DEFAULT_CAPACITY) aNBT.setLong("baseCapacity", baseCapacity);
    }

    public void flushCachedStack() {
        var proxy = getProxy();
        if (!proxy.isActive() || cache.isEmpty()) return;
        try {
            final IEnergySource energy = proxy.getEnergy();
            IMEInventory<T> sg = (cacheMode && cell != null) ? cell : env.getNetworkInvtory();
            BaseActionSource source = env.getActionSource();
            cache.updateAll((stack, amount) -> {
                stack.setStackSize(amount);
                T rest = Platform.poweredInsert(energy, sg, stack, source);
                if (rest == null) return 0L;
                return rest.getStackSize();
            });
        } catch (final GridAccessException ignored) {}
        lastOutputTick = tickCounter;
    }

    public void cellToCacheTransfer() {
        if (!cacheMode && cell != null) {
            final Iterable<T> iter = cell.getAvailableItems(
                cell.getStackType()
                    .createPrimitiveList(),
                IterationCounter.fetchNewId());
            iter.forEach(stack -> addToCache(cell.extractItems(stack, Actionable.MODULATE, env.getActionSource())));
        }
    }

    public boolean shouldCheck() {
        return checkMode && cacheMode && cell != null;
    }

    public boolean getCheckMode() {
        return checkMode;
    }

    public void setCheckMode(boolean cacheMode) {
        this.checkMode = cacheMode;
        EntityPlayer p = env.getLastClickedPlayer();
        if (p != null && GTUtility.isServer()) {
            GTUtility.sendChatTrans(p, "GT5U.hatch.outputme.checkMode." + this.checkMode);
            if (checkMode) {
                GTUtility.sendChatTrans(p, "GT5U.hatch.outputme.checkMode.desc");
            }
        }
    }

    public void addToCache(@NotNull T stack) {
        if (!isVoidCell) {
            cache.insert(stack, stack.getStackSize());
        }
    }

    /**
     * Attempt to store as many stacks as possible into the storage of this output.
     *
     * @param input    The stack to insert. Will be modified by this method (will contain whatever stacks could not be
     *                 inserted; stackSize will be 0 when everything was inserted).
     * @param simulate When true this output will not be modified.
     * @return True if the stack was fully inserted into the output, false otherwise.
     */
    public boolean storePartial(@NotNull T input, boolean simulate) {
        if (simulate && shouldCheck()) {
            input.setStackSize(input.getStackSize() + cache.get(input));
            final T rejected = cell.injectItems(input, Actionable.SIMULATE, env.getActionSource());
            input.setStackSize(Math.min(input.getStackSize(), rejected == null ? 0 : rejected.getStackSize()));
            return input.getStackSize() == 0;
        }
        if (simulate && !hasAvailableSpace()) return false;
        if (!canStore(input)) return false;
        if (!simulate) {
            addToCache(input);
            env.dispatchMarkDirty();
        }
        input.setStackSize(0);
        return true;
    }

    public boolean isFiltered() {
        if (handler == null) return false;
        return handler.isPreformatted();
    }

    public boolean isWhiteList() {
        if (handler == null) return false;
        return handler.getWhitelist() == IncludeExclude.WHITELIST;
    }

    public boolean canStore(@NotNull T input) {
        if (handler == null || !handler.isPreformatted()) return true;
        return handler.canAccept(input);
    }

    public boolean getCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(boolean cacheMode) {
        this.cacheMode = cacheMode;
        updateState();
        EntityPlayer p = env.getLastClickedPlayer();
        if (p != null && GTUtility.isServer()) {
            GTUtility.sendChatTrans(p, "GT5U.hatch.outputme.cacheMode." + this.cacheMode);
            if (cacheMode) {
                GTUtility.sendChatTrans(p, "GT5U.hatch.outputme.cacheMode.desc");
            }
        }
    }

    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagList cacheTag = new NBTTagList();
        cache.iterateAll((s, amount) -> {
            NBTTagCompound tag = env.saveStackToNBT(s.setStackSize(amount));
            cacheTag.appendTag(tag);
        });

        aNBT.setBoolean("additionalConnection", additionalConnection);
        aNBT.setTag("cache", cacheTag);
        aNBT.setLong("baseCapacity", baseCapacity);
        aNBT.setBoolean("cacheMode", cacheMode);
        aNBT.setBoolean("checkMode", checkMode);
        aNBT.setInteger("myPriority", myPriority);
        getProxy().writeToNBT(aNBT);
    }

    public void loadNBTData(NBTTagCompound aNBT) {
        NBTBase cacheTag = aNBT.getTag("cache");
        if (cacheTag instanceof NBTTagList cacheTagList) {
            for (int i = 0; i < cacheTagList.tagCount(); ++i) {
                NBTTagCompound tag = cacheTagList.getCompoundTagAt(i);
                var input = env.loadStackFromNBT(tag);
                if (input == null) continue;
                cache.insert(input, input.getStackSize());
            }
        }

        additionalConnection = aNBT.getBoolean("additionalConnection");
        baseCapacity = aNBT.getLong("baseCapacity");
        if (baseCapacity == 0) baseCapacity = DEFAULT_CAPACITY;
        cacheMode = aNBT.getBoolean("cacheMode");
        checkMode = aNBT.getBoolean("checkMode");
        myPriority = aNBT.getInteger("myPriority");
        this.isCached = false;
        getProxy().readFromNBT(aNBT);
        oldCellStack = env.getCellStack();
        updateState();
        updateAE2ProxyColor();
    }

    public void updateAE2ProxyColor() {
        AENetworkProxy proxy = getProxy();
        byte color = env.getColor();
        if (color == -1) {
            proxy.setColor(AEColor.Transparent);
        } else {
            proxy.setColor(AEColor.values()[Dyes.transformDyeIndex(color)]);
        }
        if (proxy.getNode() != null) {
            proxy.getNode()
                .updateState();
        }
    }

    public void updateCellArray() {
        try {
            this.getProxy()
                .getGrid()
                .postEvent(new MENetworkCellArrayUpdate());
        } catch (Exception ignored) {}
    }

    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", env.getCopiedDataIdentifier(player));
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setByte("color", env.getColor());
        tag.setBoolean("cacheMode", getCacheMode());
        tag.setBoolean("checkMode", getCheckMode());
        tag.setInteger("myPriority", getPriority());
        return tag;
    }

    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !env.getCopiedDataIdentifier(player)
            .equals(nbt.getString("type"))) return false;
        setAdditionalConnection(nbt.getBoolean("additionalConnection"));
        byte color = nbt.getByte("color");
        env.getBaseMetaTileEntity()
            .setColorization(color);
        setCacheMode(nbt.getBoolean("cacheMode"));
        setCheckMode(nbt.getBoolean("checkMode"));
        setPriority(nbt.getInteger("myPriority"));
        return true;
    }

    public void writeToClientPacket(NBTTagCompound tag) {
        tag.setLong("baseCapacity", baseCapacity);
    }

    public void readFromClientPacket(NBTTagCompound data) {
        baseCapacity = data.getLong("baseCapacity");
    }

    public List<T> getCacheList() {
        List<T> stackList = new ArrayList<>();

        cache.iterateAll((stack, amount) -> stackList.add(stack.setStackSize(amount)));

        return stackList;
    }

    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (ItemStackNBT.hasKey(stack, "baseCapacity")) {
            tooltip.add(
                translateToLocalFormatted(
                    "GT5U.hatch.outputme.cache_capacity_label",
                    ReadableNumberConverter.INSTANCE
                        .toWideReadableForm(stack.stackTagCompound.getLong("baseCapacity"))));
        }
    }

    private void processWailaNBTData(NBTTagCompound tag, String listKey, String countKey, List<T> stacks) {
        tag.setInteger(countKey, stacks.size());

        NBTTagList tagList = new NBTTagList();
        tag.setTag(listKey, tagList);

        stacks.sort((a, b) -> Long.compare(b.getStackSize(), a.getStackSize()));
        stacks.stream()
            .limit(10)
            .forEach(stack -> {
                NBTTagCompound stackTag = new NBTTagCompound();
                stack.writeToNBT(stackTag);
                stackTag.setString("Name", stack.getDisplayName());
                stackTag.setLong("Amount", stack.getStackSize());
                tagList.appendTag(stackTag);
            });
    }

    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setLong("cacheCapacity", getCacheCapacity());
        processWailaNBTData(tag, "stacks", "stackCount", getCacheList());

        if (cacheMode && cell != null) {
            List<T> cacheList = new ArrayList<>();
            final Iterable<T> iter = cell.getAvailableItems(
                cell.getStackType()
                    .createPrimitiveList(),
                IterationCounter.fetchNewId());
            iter.forEach(cacheList::add);
            processWailaNBTData(tag, "cacheStacks", "cacheCount", cacheList);
        }
    }

    private void processInfoData(String langBaseKey, Function<T, String> nameGetter, List<T> list, List<String> ss) {
        if (list.isEmpty()) {
            ss.add(StatCollector.translateToLocal(langBaseKey + ".empty"));
        } else {
            ss.add(StatCollector.translateToLocalFormatted(langBaseKey + ".contains", list.size()));
            list.stream()
                .limit(100)
                .forEach(s -> {
                    ss.add(
                        nameGetter.apply(s) + ": "
                            + EnumChatFormatting.GOLD
                            + formatNumber(s.getStackSize())
                            + " L"
                            + EnumChatFormatting.RESET);
                });
        }
    }

    public String[] getInfoData(String AEDiagnostics, String langBaseKey, Function<T, String> nameGetter) {
        List<String> ss = new ArrayList<>();
        ss.add(
            (getProxy() != null && getProxy().isActive())
                ? StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.bus.online")
                : StatCollector
                    .translateToLocalFormatted("GT5U.infodata.hatch.crafting_input_me.bus.offline", AEDiagnostics));
        ss.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.hatch.output_me.cache_capacity",
                EnumChatFormatting.GOLD + formatNumber(getCacheCapacity()) + " L" + EnumChatFormatting.RESET));
        processInfoData(langBaseKey, nameGetter, getCacheList(), ss);
        if (cacheMode && cell != null) {
            List<T> cacheList = new ArrayList<>();
            final Iterable<T> iter = cell.getAvailableItems(
                cell.getStackType()
                    .createPrimitiveList(),
                IterationCounter.fetchNewId());
            iter.forEach(cacheList::add);
            ss.add(translateToLocal("GT5U.waila.hatch.outputme.storage_cache"));
            processInfoData(langBaseKey, nameGetter, cacheList, ss);
        }
        return ss.toArray(new String[0]);
    }

    @SideOnly(Side.CLIENT)
    public static class WailaHelper {

        private static void processWailaAdvancedBody(String prefix, List<String> ss, String listKey, String countKey,
            NBTTagCompound tag) {
            NBTTagList stacks = tag.getTagList(listKey, 10);
            int stackCount = tag.getInteger(countKey);

            if (stackCount == 0) {
                ss.add(translateToLocal("GT5U.waila.hatch.outputme." + prefix + "_cache_empty"));
                return;
            }
            ss.add(
                translateToLocalFormatted(
                    "GT5U.waila.hatch.outputme." + prefix + "_cache_detail",
                    stackCount,
                    stackCount > 1 ? "s" : ""));

            for (int i = 0; i < stacks.tagCount(); i++) {
                NBTTagCompound stackTag = stacks.getCompoundTagAt(i);

                ss.add(
                    String.format(
                        "%s: %s%s%s",
                        stackTag.getString("Name"),
                        EnumChatFormatting.GOLD,
                        formatNumber(stackTag.getLong("Amount")),
                        EnumChatFormatting.RESET));
            }

            if (stackCount > stacks.tagCount()) {
                ss.add(
                    translateToLocalFormatted(
                        "GT5U.waila.hatch.outputme." + prefix + "_cache_detail.more",
                        stackCount - stacks.tagCount()));
            }
        }

        public static void getWailaAdvancedBody(String prefix, List<String> ss, IWailaDataAccessor accessor) {
            NBTTagCompound tag = accessor.getNBTData();
            processWailaAdvancedBody(prefix, ss, "stacks", "stackCount", tag);
            if (tag.hasKey("cacheCount")) {
                ss.add(translateToLocal("GT5U.waila.hatch.outputme.storage_cache"));
                processWailaAdvancedBody(prefix, ss, "cacheStacks", "cacheCount", tag);
            }
        }
    }
}
