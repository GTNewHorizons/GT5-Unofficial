package gregtech.common.tileentities.machines.outputme.base;

import static gregtech.common.covers.modes.FilterType.BLACKLIST;
import static gregtech.common.covers.modes.FilterType.WHITELIST;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.storage.IBaseMonitor;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.MEMonitorHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.AEColor;
import appeng.core.localization.GuiText;
import appeng.items.AEBaseCell;
import appeng.items.storage.ItemVoidStorageCell;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.me.storage.CellInventory;
import appeng.me.storage.CellInventoryHandler;
import appeng.me.storage.MEInventoryHandler;
import appeng.util.Platform;
import appeng.util.ReadableNumberConverter;
import gregtech.api.enums.Dyes;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.outputme.filter.MEFilterBase;
import gregtech.common.tileentities.machines.outputme.util.AECacheCounter;

public abstract class MTEHatchOutputMEBase<T extends IAEStack<T>, F extends MEFilterBase<T, ?, I>, I> {

    public interface Environment<T extends IAEStack<T>, F extends MEFilterBase<T, ?, I>, I> {

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

        MTEHatchOutputMEBase<T, F, I> getProvider();
    }

    private final Environment<T, F, I> env;
    protected AENetworkProxy proxy;
    protected final F filter;
    protected final AECacheCounter<T> cache = new AECacheCounter<>();
    public static long DEFAULT_CAPACITY;
    protected long baseCapacity;
    protected long cacheCapacity;

    public MTEHatchOutputMEBase(Environment<T, F, I> env, final F filter, final long baseCapacity) {
        this.env = env;
        this.filter = filter;
        DEFAULT_CAPACITY = baseCapacity;
        this.baseCapacity = baseCapacity;
        updateCacheCapacity();
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
            try {
                this.getProxy()
                    .getGrid()
                    .postEvent(new MENetworkCellArrayUpdate());
            } catch (final GridAccessException e) {
                // :P
            }
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
    private ItemStack oldCellStack = null;
    private int myPriority = 0;

    boolean cacheMode = false;
    boolean isCached = false;
    boolean checkMode = false;

    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            checkMode = !checkMode;
            GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.outputme.checkMode." + this.checkMode);
            if (checkMode) {
                GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.outputme.checkMode.desc");
            }
        } else {
            cacheMode = !cacheMode;
            GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.outputme.cacheMode." + this.cacheMode);
            if (cacheMode) {
                GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.outputme.cacheMode.desc");
            }
            updateState();
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
        final ItemStack is = env.getCellStack();
        if (is == null) {
            return;
        }
        final IMEInventoryHandler<T> cell = AEApi.instance()
            .registries()
            .cell()
            .getCellInventory(is, env.getISaveProvider(), env.getChannel());
        if (cell != null) {
            this.cell = this.wrap(cell, AccessRestriction.READ_WRITE);
            this.cellRead = this.wrap(cell, AccessRestriction.READ);
            env.dispatchMarkDirty();
        }
    }

    private static class OutputMonitorHandler<T extends IAEStack<T>> extends MEMonitorHandler<T> {

        public OutputMonitorHandler(final IMEInventoryHandler<T> t) {
            super(t);
        }

        public CellInventory<T> getCellInventory() {
            var inv = getHandler().getInternal();
            if (inv instanceof CellInventory ci) return (CellInventory<T>) ci;
            return null;
        }
    }

    private <StackType extends IAEStack<StackType>> OutputMonitorHandler<StackType> wrap(
        final IMEInventoryHandler<StackType> h, final AccessRestriction myAccess) {
        if (h == null) {
            return null;
        }

        final MEInventoryHandler<StackType> ih = new MEInventoryHandler<>(h, h.getChannel());
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
                MTEHatchOutputMEBase.this.getProxy()
                    .getGrid()
                    .postEvent(new MENetworkCellArrayUpdate());
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

        updateFilter();
        updateCacheCapacity();
        if (this.isCached) {
            this.isCached = false;
            updateState();
        }

        if (cacheMode) {
            try {
                this.getProxy()
                    .getGrid()
                    .postEvent(new MENetworkCellArrayUpdate());

                final IStorageGrid gs = this.getProxy()
                    .getStorage();
                Platform.postChanges(gs, oldCellStack, upgradeItemStack, env.getActionSource());
            } catch (final GridAccessException ignored) {}
        }

        oldCellStack = upgradeItemStack;
        env.dispatchMarkDirty();
    }

    private void updateFilter() {
        ItemStack upgradeItemStack = env.getCellStack();

        if (upgradeItemStack != null && upgradeItemStack.getItem() instanceof ICellWorkbenchItem cellWorkbenchItem) {
            if (filter.isFiltered()) {
                return;
            }
            String msg = filter.updateFilterFromCell(cellWorkbenchItem, upgradeItemStack);
            if (!msg.isEmpty() && env.getLastClickedPlayer() != null) {
                String modeKey = filter.getIsBlackList() ? BLACKLIST.getKey() : WHITELIST.getKey();
                GTUtility.sendChatToPlayer(
                    env.getLastClickedPlayer(),
                    StatCollector.translateToLocal(modeKey)
                        + StatCollector.translateToLocalFormatted(filter.getEnableKey(), msg));
            }
            env.dispatchMarkDirty();
        } else {
            if (!filter.isFiltered()) {
                return;
            }
            filter.clear();
            if (env.getLastClickedPlayer() != null) {
                GTUtility.sendChatToPlayer(
                    env.getLastClickedPlayer(),
                    StatCollector.translateToLocal(filter.getDisableKey()));
            }
            env.dispatchMarkDirty();
        }
    }

    boolean isVoidCell = false;

    private void updateCacheCapacity() {
        ItemStack cellStack = env.getCellStack();
        isVoidCell = false;
        cacheCapacity = baseCapacity;

        if (cellStack == null) return;

        var cell = cellStack.getItem();
        if (cell instanceof ItemVoidStorageCell) {
            isVoidCell = true;
            cacheCapacity = Long.MAX_VALUE;
            return;
        }

        if (cell instanceof AEBaseCell) {
            final IMEInventoryHandler<?> inventory = AEApi.instance()
                .registries()
                .cell()
                .getCellInventory(cellStack, env.getISaveProvider(), env.getChannel());

            if (inventory instanceof CellInventoryHandler<?>handler
                && handler.getCellInv() instanceof CellInventory<?>cellInv) {
                cacheCapacity = cellInv.getRemainingItemCount() + cellInv.getStoredItemCount();
            }
        }
    }

    long lastOutputTick = 0;
    long lastInputTick = 0;
    long tickCounter = 0;

    public final long getLastInputTick() {
        return lastInputTick;
    }

    public final long getTickCounter() {
        return tickCounter;
    }

    public boolean hasAvailableSpace() {
        return getCachedAmount() < getCacheCapacity();
    }

    public long getAvailableSpace() {
        return getCacheCapacity() - getCachedAmount();
    }

    public boolean canAcceptAnyInput() {
        if (shouldCheck()) return false;
        return lastInputTick == tickCounter || hasAvailableSpace();
    }

    public long getCachedAmount() {
        return cache.getTotal();
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

    protected void flushCachedStack() {
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

    public boolean shouldCheck() {
        return checkMode && cacheMode && cell != null;
    }

    public boolean getCheckMode() {
        return checkMode;
    }

    public void setCheckMode(boolean cacheMode) {
        this.checkMode = cacheMode;
    }

    public boolean canStore(@NotNull I stack) {
        if (shouldCheck()) {
            T input = filter.fromNative(stack);
            input.setStackSize(input.getStackSize() + cache.get(input));
            final T returns = cell.injectItems(input, Actionable.SIMULATE, env.getActionSource());
            return returns == null || returns.getStackSize() == 0;
        }
        return lastInputTick == tickCounter || hasAvailableSpace() && filter.isAllowed(stack);
    }

    public boolean canStore(@NotNull I stack, long size) {
        if (shouldCheck()) {
            T input = filter.fromNative(stack);
            input.setStackSize(size);
            final T returns = cell.injectItems(input, Actionable.SIMULATE, env.getActionSource());
            return returns == null || returns.getStackSize() == 0;
        }
        return lastInputTick == tickCounter || hasAvailableSpace() && filter.isAllowed(stack);
    }

    public void addToCache(I stack) {
        addToCache(filter.fromNative(stack));
    }

    public void addToCache(T stack) {
        if (!isVoidCell) cache.insert(stack, stack.getStackSize());
        lastInputTick = tickCounter;
    }

    public boolean storePartial(I stack, boolean simulate) {
        if (!canStore(stack)) {
            return false;
        }
        if (!simulate) {
            addToCache(stack);
        }
        return true;
    }

    public boolean isFiltered() {
        return filter.isFiltered();
    }

    public F getFilter() {
        return filter;
    }

    public boolean getCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(boolean cacheMode) {
        this.cacheMode = cacheMode;
        updateState();
    }

    public void saveNBTData(NBTTagCompound aNBT) {
        filter.saveNBTData(aNBT);

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
        filter.loadNBTData(aNBT);
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
        updateCacheCapacity();
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

    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", env.getCopiedDataIdentifier(player));
        tag.setBoolean("additionalConnection", additionalConnection);
        tag.setByte("color", env.getColor());
        return tag;
    }

    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !env.getCopiedDataIdentifier(player)
            .equals(nbt.getString("type"))) return false;
        setAdditionalConnection(nbt.getBoolean("additionalConnection"));
        byte color = nbt.getByte("color");
        env.getBaseMetaTileEntity()
            .setColorization(color);
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
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("baseCapacity")) {
            tooltip.add(
                translateToLocalFormatted(
                    "GT5U.hatch.outputme.cache_capacity_label",
                    ReadableNumberConverter.INSTANCE
                        .toWideReadableForm(stack.stackTagCompound.getLong("baseCapacity"))));
        }
    }

    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setLong("cacheCapacity", getCacheCapacity());
        List<T> stackList = getCacheList();
        tag.setInteger("stackCount", stackList.size());

        stackList.sort((a, b) -> Long.compare(b.getStackSize(), a.getStackSize()));

        NBTTagList tagList = new NBTTagList();
        tag.setTag("stacks", tagList);

        stackList.stream()
            .limit(10)
            .forEach(stack -> {
                NBTTagCompound stackTag = new NBTTagCompound();
                stack.writeToNBT(stackTag);
                stackTag.setLong("Amount", stack.getStackSize());
                tagList.appendTag(stackTag);
            });
    }

    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        env.getBaseMetaTileEntity()
            .add1by1Slot(builder);
        builder.widget(
            new TextFieldWidget().setSynced(true, true)
                .setNumbers(1, Integer.MAX_VALUE)
                .setGetterInt(this::getPriority)
                .setSetterInt(this::setPriority)
                .setTextAlignment(Alignment.Center)
                .setTextColor(Color.WHITE.dark(1))
                .setFocusOnGuiOpen(false)
                .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
                .addTooltip(GuiText.Priority.getLocal())
                .setEnabled(widget -> getCacheMode())
                .setPos(7, 63)
                .setSize(40, 14))
            .widget(new FakeSyncWidget.BooleanSyncer(this::getCacheMode, this::setCacheMode));
    }
}
