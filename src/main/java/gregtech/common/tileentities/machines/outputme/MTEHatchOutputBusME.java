package gregtech.common.tileentities.machines.outputme;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_HATCH_ACTIVE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import appeng.api.AEApi;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.ICellContainer;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.ISaveProvider;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import appeng.helpers.IPriorityHost;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OutputBusType;
import gregtech.api.interfaces.IMEConnectable;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.IOutputBusTransaction;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;
import gregtech.common.tileentities.machines.outputme.filter.MEFilterItem;
import gregtech.common.tileentities.machines.outputme.util.AECacheCounter;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchOutputBusME extends MTEHatchOutputBus implements IPowerChannelState, IMEConnectable,
    ICellContainer, IGridProxyable, IPriorityHost, MTEHatchOutputMEBase.Environment<IAEItemStack> {

    public MTEHatchOutputBusME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            4,
            new String[] { "Item Output for Multiblocks", "Stores directly into ME", "Can cache 1600 items by default",
                "Change cache size by inserting a storage cell",
                "Change ME connection behavior by right-clicking with wire cutter",
                "Partition the inserted Storage Cell to filter accepted outputs",
                "Right click with screwdriver to toggle Cache Mode",
                "Shift right click with screwdriver to toggle Check Mode" },
            1);
    }

    public MTEHatchOutputBusME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputBusME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_HATCH) };
    }

    EntityPlayer lastClickedPlayer = null;

    private final MTEHatchOutputMEBase<IAEItemStack, MEFilterItem, ItemStack> provider = new MTEHatchOutputMEBase<IAEItemStack, MEFilterItem, ItemStack>(
        this,
        new MEFilterItem(),
        1_600) {};

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
        provider.updateState();
    }

    public ItemStack getVisual() {
        return ItemList.Hatch_Output_Bus_ME.get(1);
    }

    @Override
    public boolean storePartial(ItemStack stack, boolean simulate) {
        return provider.storePartial(stack, simulate);
    }

    @Override
    public boolean isFiltered() {
        return provider.isFiltered();
    }

    @Override
    public boolean isFilteredToItem(GTUtility.ItemId id) {
        return provider.getFilter()
            .isFilteredToItem(id);
    }

    @Override
    public OutputBusType getBusType() {
        if (provider.getCacheMode())
            return provider.isFiltered() ? OutputBusType.MECacheFiltered : OutputBusType.MECacheUnfiltered;
        else return provider.isFiltered() ? OutputBusType.MEFiltered : OutputBusType.MEUnfiltered;
    }

    @Override
    public IOutputBusTransaction createTransaction() {
        return new MEOutputBusTransaction();
    }

    @Override
    public IGridProxyable getIGridProxyable() {
        return this;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    @Override
    public ItemStack getCellStack() {
        return mInventory[0];
    }

    @Override
    public ISaveProvider getISaveProvider() {
        return this;
    }

    BaseActionSource requestSource;

    @Override
    public BaseActionSource getActionSource() {
        if (requestSource == null) requestSource = new MachineSource(this);
        return requestSource;
    }

    @Override
    public EntityPlayer getLastClickedPlayer() {
        return lastClickedPlayer;
    }

    @Override
    public IMEInventory<IAEItemStack> getNetworkInvtory() throws GridAccessException {
        return getProxy().getStorage()
            .getItemInventory();
    }

    class MEOutputBusTransaction implements IOutputBusTransaction {

        private final AECacheCounter<GTUtility.ItemId> cache = new AECacheCounter<>();
        private final long tick, availableSpace;
        private boolean active = true;

        public MEOutputBusTransaction() {
            long initialStored = provider.getCachedAmount();
            long capacity = provider.getCacheCapacity();
            tick = initialStored >= capacity ? provider.getLastInputTick() : provider.getTickCounter();
            availableSpace = capacity - initialStored;
        }

        @Override
        public IOutputBus getBus() {
            return MTEHatchOutputBusME.this;
        }

        @Override
        public boolean hasAvailableSpace() {
            // There's really no reason for the tick counter, it's just more accurate to the real bus's behaviour.
            // Transactions should never be kept around long enough for it to matter, but in case someone does something
            // stupid it's here to make sure nothing breaks.
            // This condition should always return true unless this transaction is kept around for more than one tick.
            return cache.getTotal() < availableSpace || provider.getTickCounter() == tick;
        }

        public boolean canStore(GTUtility.ItemId id, ItemStack stack) {
            if (provider.getCheckMode()) {
                return provider.canStore(stack, stack.stackSize + cache.get(id));
            }
            return hasAvailableSpace() && provider.getFilter()
                .isFilteredToItem(id);
        }

        @Override
        public boolean storePartial(GTUtility.ItemId id, ItemStack stack) {
            if (!active) throw new IllegalStateException("Cannot add to a transaction after committing it");

            if (!canStore(id, stack)) return false;

            cache.insert(id, stack.stackSize);
            stack.stackSize = 0;

            return true;
        }

        @Override
        public void completeItem(GTUtility.ItemId id) {
            // Do nothing
        }

        @Override
        public void commit() {
            cache.iterateAll(
                (id, amount) -> {
                    provider.addToCache(
                        provider.getFilter()
                            .fromNative(id.getItemStack())
                            .setStackSize(amount));
                });

            MTEHatchOutputBusME.this.markDirty();
            active = false;
        }
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public void onFacingChange() {
        provider.updateValidGridProxySides();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        this.lastClickedPlayer = aPlayer;
        openGui(aPlayer);
        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        provider.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return provider.onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public boolean connectsToAllSides() {
        return provider.getAdditionalConnection();
    }

    @Override
    public void setConnectsToAllSides(boolean connects) {
        provider.setAdditionalConnection(connects);
    }

    @Override
    public AENetworkProxy getProxy() {
        return provider.getProxy();
    }

    @Override
    public boolean isPowered() {
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean pushOutputInventory() {
        return false;
    }

    public boolean canAcceptAnyItem() {
        return provider.canAcceptAnyInput();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        provider.onPostTick(aBaseMetaTileEntity, aTick);
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        provider.updateAE2ProxyColor();
    }

    @Override
    public boolean isLocked() {
        return provider.isFiltered();
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        provider.addAdditionalTooltipInformation(stack, tooltip);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        provider.setItemNBT(aNBT);
    }

    public NBTTagCompound saveStackToNBT(IAEItemStack s) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("itemStack", GTUtility.saveItem(s.getItemStack()));
        tag.setLong("size", s.getStackSize());
        return tag;
    }

    public IAEItemStack loadStackFromNBT(NBTTagCompound t) {
        return AEItemStack.create(GTUtility.loadItem(t));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        provider.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        NBTBase t = aNBT.getTag("cachedStack"); // legacy
        if (t instanceof NBTTagCompound) {
            provider.addToCache(
                AEApi.instance()
                    .storage()
                    .createItemStack(GTUtility.loadItem((NBTTagCompound) t)));
        }
        t = aNBT.getTag("cachedItems");
        if (t instanceof NBTTagList l) {
            for (int i = 0; i < l.tagCount(); ++i) {
                NBTTagCompound tag = l.getCompoundTagAt(i);
                if (!tag.hasKey("itemStack")) { // legacy #868
                    provider.addToCache(
                        AEApi.instance()
                            .storage()
                            .createItemStack(GTUtility.loadItem(l.getCompoundTagAt(i))));
                    continue;
                }
                NBTTagCompound tagItemStack = tag.getCompoundTag("itemStack");
                final IAEItemStack s = AEApi.instance()
                    .storage()
                    .createItemStack(GTUtility.loadItem(tagItemStack));
                if (s != null) {
                    s.setStackSize(tag.getLong("size"));
                    provider.addToCache(s);
                } else {
                    GTMod.GT_FML_LOGGER.warn(
                        "An error occurred while loading contents of ME Output Bus. This item has been voided: "
                            + tagItemStack);
                }
            }
        }
        provider.loadNBTData(aNBT);
    }

    public static final String COPIED_DATA_IDENTIFIER = "outputBusME";

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        return provider.getCopiedData(player);
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        return provider.pasteCopiedData(player, nbt);
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = super.getDescriptionData();

        // Sync the hatch capacity to the client so that MM can show its exchanging preview properly
        // This is only called when the hatch is placed since it will never change over its lifetime

        provider.writeToClientPacket(tag);

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        provider.readFromClientPacket(data);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        provider.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaBody(ItemStack itemStack, List<String> ss, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, ss, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        ss.add(
            translateToLocalFormatted(
                "GT5U.waila.hatch.outputme.item_cache_capacity",
                formatNumber(tag.getLong("cacheCapacity"))));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasWailaAdvancedBody(ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getWailaAdvancedBody(ItemStack itemStack, List<String> ss, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaAdvancedBody(itemStack, ss, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        NBTTagList stacks = tag.getTagList("stacks", 10);
        int stackCount = tag.getInteger("stackCount");

        if (stackCount == 0) {
            ss.add(translateToLocal("GT5U.waila.hatch.outputme.item_cache_empty"));
        } else {
            ss.add(
                translateToLocalFormatted(
                    "GT5U.waila.hatch.outputme.item_cache_detail",
                    stackCount,
                    stackCount > 1 ? "s" : ""));

            for (int i = 0; i < stacks.tagCount(); i++) {
                IAEItemStack stack = AEItemStack.loadItemStackFromNBT(stacks.getCompoundTagAt(i));

                ss.add(
                    String.format(
                        "%s: %s%s%s",
                        stack.getItemStack()
                            .getDisplayName(),
                        EnumChatFormatting.GOLD,
                        formatNumber(stack.getStackSize()),
                        EnumChatFormatting.RESET));
            }

            if (stackCount > stacks.tagCount()) {
                ss.add(
                    translateToLocalFormatted(
                        "GT5U.waila.hatch.outputme.item_cache_detail.more",
                        stackCount - stacks.tagCount()));
            }
        }
    }

    @Override
    public String[] getInfoData() {
        List<String> ss = new ArrayList<>();
        ss.add(
            (getProxy() != null && getProxy().isActive())
                ? StatCollector.translateToLocal("GT5U.infodata.hatch.crafting_input_me.bus.online")
                : StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.hatch.crafting_input_me.bus.offline",
                    getAEDiagnostics()));
        ss.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.hatch.output_bus_me.cache_capacity",
                EnumChatFormatting.GOLD + formatNumber(provider.getCacheCapacity()) + EnumChatFormatting.RESET));
        List<IAEItemStack> itemList = provider.getCacheList();
        if (itemList.isEmpty()) {
            ss.add(StatCollector.translateToLocal("GT5U.infodata.hatch.output_bus_me.empty"));
        } else {
            ss.add(
                StatCollector.translateToLocalFormatted("GT5U.infodata.hatch.output_bus_me.contains", itemList.size()));
            itemList.stream()
                .limit(100)
                .forEach(s -> {
                    ss.add(
                        s.getItem()
                            .getItemStackDisplayName(s.getItemStack()) + ": "
                            + EnumChatFormatting.GOLD
                            + formatNumber(s.getStackSize())
                            + EnumChatFormatting.RESET);
                });
        }
        return ss.toArray(new String[Math.min(itemList.size(), 100) + 2]);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        provider.addUIWidgets(builder, buildContext);
    }

    @Override
    public boolean acceptsItemLock() {
        return false;
    }

    @Override
    public void onContentsChanged(int slot) {
        provider.onContentsChanged(slot);
    }

    @MENetworkEventSubscribe
    public void updateCell(final MENetworkChannelsChanged c) {
        provider.updateCell();
    }

    @Override
    public List<IMEInventoryHandler> getCellArray(final StorageChannel channel) {
        return provider.getCellArray(channel);
    }

    @Override
    public int getPriority() {
        return provider.getPriority();
    }

    @Override
    public void setPriority(int newValue) {
        provider.setPriority(newValue);
    }

    @Override
    public void saveChanges(IMEInventory cellInventory) {
        markDirty();
    }

    @Override
    public IGridNode getActionableNode() {
        return getProxy().getNode();
    }

    @Override
    public DimensionalCoord getLocation() {
        IGregTechTileEntity gtm = this.getBaseMetaTileEntity();
        return new DimensionalCoord(gtm.getWorld(), gtm.getXCoord(), gtm.getYCoord(), gtm.getZCoord());
    }

    @Override
    public void securityBreak() {}

    @Override
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        return getProxy().getNode();
    }

    public void dispatchMarkDirty() {
        this.markDirty();
    }
}
