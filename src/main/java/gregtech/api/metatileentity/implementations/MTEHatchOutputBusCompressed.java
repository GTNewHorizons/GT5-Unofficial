package gregtech.api.metatileentity.implementations;

import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSource;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IExternalStorageHandler;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.InventoryAdaptor;
import appeng.util.inv.IMEAdaptor;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.OutputBusType;
import gregtech.api.interfaces.IOutputBusTransaction;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.gui.modularui.hatch.MTEHatchOutputBusCompressedGui;
import gregtech.common.inventory.AEInventory;

public class MTEHatchOutputBusCompressed extends MTEHatchOutputBus implements IMEMonitor<IAEItemStack> {

    public final int slotCount;
    public final long stackCapacity;
    public long stackLimitOverride;

    private final AEInventory inventory;

    private final int[] busSlots;

    public MTEHatchOutputBusCompressed(int id, String name, String nameRegional, int tier, long stackCapacity) {
        super(
            id,
            name,
            nameRegional,
            tier,
            ArrayExt.of(
                "Item Output for Multiblocks",
                "Capacity: " + GOLD + getSlots(tier) + GRAY + " slots",
                "Each slot can hold " + GOLD + NumberFormat.AMOUNT_TEXT.format(stackCapacity) + GRAY + " stacks",
                "Bus config can be saved and loaded via a data stick",
                "Can only be automated with GT and AE",
                "Contents are kept when broken"),
            0);

        this.slotCount = getSlots(tier);
        this.stackCapacity = stackCapacity;
        this.inventory = null;
        this.busSlots = IntStream.range(0, slotCount)
            .toArray();
    }

    protected MTEHatchOutputBusCompressed(MTEHatchOutputBusCompressed prototype) {
        super(prototype.mName, prototype.mTier, 0, prototype.mDescriptionArray, prototype.mTextures);

        this.slotCount = prototype.slotCount;
        this.stackCapacity = prototype.stackCapacity;
        this.stackLimitOverride = prototype.stackCapacity;
        this.busSlots = prototype.busSlots;

        this.inventory = new BusInventory(slotCount);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputBusCompressed(this);
    }

    public AEInventory getAEInventory() {
        return inventory;
    }

    public long getStackLimitOverride() {
        return stackLimitOverride;
    }

    public void setStackLimitOverride(long stackLimitOverride) {
        this.stackLimitOverride = stackLimitOverride;
    }

    @Override
    public boolean connectsToItemPipe(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        return GTUtility.longToInt(this.inventory.getAESlotLimit(slot, AEItemStack.create(stack)));
    }

    @Override
    public int getInventoryStackLimit() {
        return getStackSizeLimit(-1, null);
    }

    @Override
    protected int getStackTransferAmount() {
        return slotCount;
    }

    @Override
    public OutputBusType getBusType() {
        return lockedItem == null ? OutputBusType.CompressedUnfiltered : OutputBusType.CompressedFiltered;
    }

    @Override
    public boolean storePartial(ItemStack stack, boolean simulate) {
        if (!simulate) markDirty();

        IAEItemStack rejected = inventory.injectItems(
            AEItemStack.create(stack),
            simulate ? Actionable.SIMULATE : Actionable.MODULATE,
            new MachineSource((BaseMetaTileEntity) getBaseMetaTileEntity()));

        stack.stackSize = (int) (rejected == null ? 0 : rejected.getStackSize());

        return stack.stackSize == 0;
    }

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        return inventory;
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        return inventory.getStorageList();
    }

    @Override
    public IItemList<IAEItemStack> getAvailableItems(final IItemList<IAEItemStack> out, int iteration) {
        return inventory.getAvailableItems(out, iteration);
    }

    @Override
    public <T> @Nullable T getCapability(@NotNull Class<T> capability, @NotNull ForgeDirection side) {
        if (capability == InventoryAdaptor.class && side == getBaseMetaTileEntity().getFrontFacing()) {
            return capability.cast(new IMEAdaptor(inventory, inventory.getActionSource()));
        }

        return super.getCapability(capability, side);
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver l, Object verificationToken) {
        inventory.addListener(l, verificationToken);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver l) {
        inventory.removeListener(l);
    }

    @Override
    public AccessRestriction getAccess() {
        return inventory.getAccess();
    }

    @Override
    public boolean isPrioritized(IAEItemStack input) {
        return inventory.isPrioritized(input);
    }

    @Override
    public boolean canAccept(IAEItemStack input) {
        return inventory.canAccept(input);
    }

    @Override
    public int getPriority() {
        return inventory.getPriority();
    }

    @Override
    public int getSlot() {
        return inventory.getSlot();
    }

    @Override
    public boolean validForPass(int i) {
        return inventory.validForPass(i);
    }

    @Override
    public IAEItemStack injectItems(IAEItemStack input, Actionable type, BaseActionSource src) {
        // Don't allow insertions
        return input;
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
        return inventory.extractItems(request, mode, src);
    }

    @Override
    public StorageChannel getChannel() {
        return inventory.getChannel();
    }

    @Override
    public void setItemNBT(NBTTagCompound nbt) {
        super.setItemNBT(nbt);

        if (!inventory.getStorageList()
            .isEmpty()) {
            nbt.setTag("inv", inventory.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);

        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null && tag.hasKey("inv")) {
            BusInventory inv = new BusInventory(this.slotCount);
            inv.readFromNBT(tag.getCompoundTag("inv"));

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                for (IAEItemStack stored : inv.getStorageList()) {
                    tooltip.add(stored.getDisplayName() + " x " + NumberFormatUtil.formatNumber(stored.getStackSize()));
                }
            } else {
                tooltip.add(GTUtility.translate("GT5U.gui.text.compressed_bus_stored_items"));
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        inventory.readFromNBT(aNBT.getCompoundTag("inv"));
        loadBusConfig(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setTag("inv", inventory.writeToNBT(new NBTTagCompound()));
        saveBusConfig(aNBT);
    }

    protected void loadBusConfig(NBTTagCompound tag) {
        stackLimitOverride = tag.getLong("stackLimitOverride");
    }

    protected void saveBusConfig(NBTTagCompound tag) {
        tag.setLong("stackLimitOverride", stackLimitOverride);
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        final NBTTagCompound nbt = super.getCopiedData(player);
        saveBusConfig(nbt);
        return nbt;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (super.pasteCopiedData(player, nbt)) {
            loadBusConfig(nbt);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchOutputBusCompressedGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public IOutputBusTransaction createTransaction() {
        return new CompressedOutputBusTransaction();
    }

    @Override
    protected ItemSource getItemSource(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing() ? inventory.getItemIO() : null;
    }

    @Override
    protected ItemSink getItemSink(ForgeDirection side) {
        return null;
    }

    class BusInventory extends AEInventory {

        public BusInventory(int slotCount) {
            super(slotCount);
        }

        @Override
        public long getAESlotLimit(int slot, @Nullable IAEItemStack stack) {
            int maxStack = stack == null ? 64
                : stack.getItemStack()
                    .getMaxStackSize();

            return maxStack * stackLimitOverride;
        }

        @Override
        protected AEInventory copyImpl() {
            return new BusInventory(slotCount);
        }

        @Override
        public BaseActionSource getActionSource() {
            return new MachineSource((BaseMetaTileEntity) getBaseMetaTileEntity());
        }
    }

    class CompressedOutputBusTransaction implements IOutputBusTransaction {

        private final AEInventory inventory;

        private boolean active = true;

        CompressedOutputBusTransaction() {
            inventory = getBus().inventory.copy();
        }

        @Override
        public MTEHatchOutputBusCompressed getBus() {
            return MTEHatchOutputBusCompressed.this;
        }

        @Override
        public boolean storePartial(GTUtility.ItemId id, ItemStack stack) {
            if (!active) throw new IllegalStateException("Cannot add to a transaction after committing it");

            IAEItemStack rejected = inventory.injectItems(AEItemStack.create(stack), Actionable.MODULATE, null);

            stack.stackSize = rejected == null ? 0 : (int) rejected.getStackSize();

            return false;
        }

        @Override
        public void completeItem(GTUtility.ItemId id) {
            // Do nothing
        }

        @Override
        public boolean hasAvailableSpace() {
            return true;
        }

        @Override
        public void commit() {
            for (int i = 0; i < slotCount; i++) {
                getBus().inventory.setStackInSlot(i, this.inventory.getAEStackInSlot(i));
            }

            getBus().markDirty();

            active = false;
        }
    }

    public static void registerAEIntegration() {
        AEApi.instance()
            .registries()
            .externalStorage()
            .addExternalStorageInterface(new StorageHandler());
    }

    private static class StorageHandler implements IExternalStorageHandler {

        @Override

        public boolean canHandle(TileEntity te, ForgeDirection d, StorageChannel channel, BaseActionSource mySrc) {
            return channel == StorageChannel.ITEMS && te instanceof BaseMetaTileEntity
                && ((BaseMetaTileEntity) te).getMetaTileEntity() instanceof MTEHatchOutputBusCompressed;
        }

        @Override

        public IMEInventory<IAEItemStack> getInventory(TileEntity te, ForgeDirection d, StorageChannel channel,
            BaseActionSource src) {
            if (channel == StorageChannel.ITEMS) {
                return (MTEHatchOutputBusCompressed) ((BaseMetaTileEntity) te).getMetaTileEntity();
            }
            return null;
        }
    }
}
