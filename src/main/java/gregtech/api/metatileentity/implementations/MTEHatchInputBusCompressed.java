package gregtech.api.metatileentity.implementations;

import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;

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
import com.gtnewhorizon.gtnhlib.util.ItemUtil;

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
import appeng.util.item.AEItemStack;
import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusCompressedGui;
import gregtech.common.gui.modularui.util.ProxiedItemHandlerModifiable;
import gregtech.common.inventory.AEInventory;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;

public class MTEHatchInputBusCompressed extends MTEHatchInputBus
    implements IMEMonitor<IAEItemStack>, IRecipeProcessingAwareHatch, IDataCopyable {

    public final int slotCount;
    public final long stackCapacity;
    public long stackLimitOverride;

    private final AEInventory inventory;
    private final IItemHandlerModifiable itemHandler;

    private final int[] busSlots;

    private int processing = 0;

    private ItemStack[] originalStacks = null, containedStacks = null;

    public MTEHatchInputBusCompressed(int id, String name, String nameRegional, int tier, long stackCapacity) {
        super(
            id,
            name,
            nameRegional,
            tier,
            0,
            ArrayExt.of(
                "Item Input for Multiblocks",
                "Capacity: " + GOLD + getSlots(tier) + GRAY + " slots",
                "Each slot can hold " + GOLD + NumberFormat.AMOUNT_TEXT.format(stackCapacity) + GRAY + " stacks",
                "Bus config can be saved and loaded via a data stick",
                "Can only be automated with GT and AE",
                "Contents are kept when broken"));

        this.slotCount = getSlots(tier);
        this.stackCapacity = stackCapacity;
        this.inventory = null;
        this.busSlots = IntStream.range(0, slotCount)
            .toArray();
        this.itemHandler = null;
    }

    protected MTEHatchInputBusCompressed(MTEHatchInputBusCompressed prototype) {
        super(prototype.mName, prototype.mTier, 1, prototype.mDescriptionArray, prototype.mTextures);

        this.slotCount = prototype.slotCount;
        this.stackCapacity = prototype.stackCapacity;
        this.stackLimitOverride = prototype.stackCapacity;
        this.busSlots = prototype.busSlots;

        this.inventory = new BusInventory(slotCount);

        this.itemHandler = new ProxiedItemHandlerModifiable(inventory) {

            @Override
            public int getSlots() {
                return super.getSlots() + 1;
            }

            @Override
            public @Nullable ItemStack getStackInSlot(int slot) {
                if (slot == getCircuitSlot()) return mInventory[0];

                return super.getStackInSlot(slot);
            }

            @Override
            public void setStackInSlot(int slot, @Nullable ItemStack stack) {
                if (slot == getCircuitSlot()) {
                    mInventory[0] = GTUtility.copyAmount(0, stack);
                    return;
                }

                super.setStackInSlot(slot, stack);
            }
        };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputBusCompressed(this);
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
    public int getCircuitSlot() {
        return slotCount;
    }

    @Override
    public void startRecipeProcessing() {
        if (processing == 0) {
            originalStacks = GTDataUtils
                .mapToArray(inventory.inventory, ItemStack[]::new, s -> s == null ? null : s.getItemStack());
            containedStacks = GTDataUtils.mapToArray(originalStacks, ItemStack[]::new, GTUtility::copy);
        }

        processing++;
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        processing--;

        if (processing == 0) {
            for (int slotIndex = 0; slotIndex < slotCount; slotIndex++) {
                ItemStack original = originalStacks[slotIndex];
                ItemStack contained = containedStacks[slotIndex];

                if (original == null) continue;

                int delta = original.stackSize - (contained == null ? 0 : contained.stackSize);

                if (delta == 0) continue;

                if (delta < 0) {
                    GTMod.GT_FML_LOGGER.error(
                        "Compressed input bus somehow has more items in it than was original stored; this recipe will be cancelled (slot index={}, original={}, contained={}, delta={})",
                        slotIndex,
                        original,
                        contained,
                        delta);
                    controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                    return CheckRecipeResultRegistry.CRASH;
                }

                IAEItemStack stack = inventory.getAEStackInSlot(slotIndex);

                if (stack == null || delta > stack.getStackSize()) {
                    GTMod.GT_FML_LOGGER.error(
                        "Compressed input bus somehow consumed more items than were available for this slot; this recipe will be cancelled (slot index={}, original={}, contained={}, delta={})",
                        slotIndex,
                        original,
                        contained,
                        delta);
                    controller.stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                    return CheckRecipeResultRegistry.CRASH;
                }

                stack.decStackSize(delta);

                inventory.setStackInSlot(slotIndex, stack.getStackSize() == 0 ? null : stack);
            }

            originalStacks = null;
            containedStacks = null;
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public int getSizeInventory() {
        return processing > 0 ? slotCount + 1 : 0;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        if (processing > 0) {
            if (slotIndex == getCircuitSlot()) return mInventory[0];

            return GTDataUtils.getIndexSafe(containedStacks, slotIndex);
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack toInsert) {
        if (slotIndex == getCircuitSlot()) {
            mInventory[0] = GTUtility.copyAmount(0, toInsert);
            markDirty();
            return;
        }

        if (GTUtility.isStackValid(toInsert)) {
            inventory.insertItem(slotIndex, toInsert, false);
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        return inventory.extractItem(index, amount, false);
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
        return allowPutStack(getBaseMetaTileEntity(), slotIndex, ForgeDirection.UNKNOWN, itemStack);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity igte, int slotIndex, ForgeDirection side, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity igte, int slotIndex, ForgeDirection side, ItemStack toInsert) {
        if (slotIndex == getCircuitSlot()) return false;
        if (side != ForgeDirection.UNKNOWN && side != getBaseMetaTileEntity().getFrontFacing()) return false;
        if (mRecipeMap != null && !disableFilter && !mRecipeMap.containsInput(toInsert)) return false;

        IAEItemStack existing = inventory.getAEStackInSlot(slotIndex);

        if (existing != null) {
            if (existing.getStackSize() >= inventory.getAESlotLimit(slotIndex, existing)) return false;
        }

        if (!disableLimited) {
            int containingSlot = inventory.indexOf(toInsert);

            if (containingSlot != -1) {
                return containingSlot == slotIndex;
            }
        }

        return existing == null || existing.isSameType(toInsert);
    }

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        return itemHandler;
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
        return inventory.injectItems(input, type, src);
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
        // Don't allow extractions
        return null;
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

            tooltip.add("");

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                for (IAEItemStack stored : inv.getStorageList()) {
                    tooltip
                        .add(stored.getDisplayName() + " x " + GOLD + GTUtility.formatNumbers(stored.getStackSize()));
                }
            } else {
                tooltip.add(GTUtility.translate("GT5U.gui.text.compressed_bus_stored_items"));
            }

            tooltip.add("");
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        super.loadNBTData(tag);

        inventory.readFromNBT(tag.getCompoundTag("inv"));
        stackLimitOverride = tag.getLong("stackLimitOverride");
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        super.saveNBTData(tag);

        tag.setTag("inv", inventory.writeToNBT(new NBTTagCompound()));
        tag.setLong("stackLimitOverride", stackLimitOverride);
    }

    protected void loadBusConfig(NBTTagCompound tag) {
        stackLimitOverride = tag.getLong("stackLimitOverride");
        disableLimited = tag.getBoolean("disableLimited");
        disableFilter = tag.getBoolean("disableFilter");
        disableSort = tag.getBoolean("disableSort");

        int circuit = tag.getInteger("circuit");

        mInventory[0] = circuit == -1 ? null : GTUtility.getIntegratedCircuit(circuit);
    }

    protected void saveBusConfig(NBTTagCompound tag) {
        tag.setLong("stackLimitOverride", stackLimitOverride);
        tag.setBoolean("disableLimited", disableLimited);
        tag.setBoolean("disableFilter", disableFilter);
        tag.setBoolean("disableSort", disableSort);

        tag.setInteger("circuit", mInventory[0] == null ? -1 : ItemUtil.getStackMeta(mInventory[0]));
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity igte, EntityPlayer player) {
        final ItemStack dataStick = player.inventory.getCurrentItem();

        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            openGui(player);
            return true;
        }

        if (!pasteCopiedData(player, dataStick.stackTagCompound)) {
            if (player.worldObj.isRemote) {
                player.addChatMessage(new ChatComponentTranslation("GT5U.machines.output_bus.invalid"));
            }

            return false;
        } else {
            if (player.worldObj.isRemote) {
                player.addChatMessage(new ChatComponentTranslation("GT5U.machines.output_bus.loaded"));
            }

            return true;
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity igte, EntityPlayer player) {
        final ItemStack held = player.inventory.getCurrentItem();

        if (!ItemList.Tool_DataStick.isStackEqual(held, false, true)) {
            return;
        }

        held.stackTagCompound = getCopiedData(player);
        held.setStackDisplayName("Output Bus Configuration");
        player.addChatMessage(new ChatComponentTranslation("GT5U.machines.output_bus.saved"));
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", mName);
        saveBusConfig(nbt);
        return nbt;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !mName.equals(nbt.getString("type"))) return false;
        loadBusConfig(nbt);
        return true;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return mName;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputBusCompressedGui(this).build(data, syncManager, uiSettings);
    }

    public AEInventory getAEInventory() {
        return inventory;
    }

    @Override
    protected ItemSource getItemSource(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing() ? inventory.getItemIO() : null;
    }

    @Override
    protected ItemSink getItemSink(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing() ? inventory.getItemIO() : null;
    }

    public long getStackLimitOverride() {
        return stackLimitOverride;
    }

    public void setStackLimitOverride(long stackLimitOverride) {
        this.stackLimitOverride = stackLimitOverride;
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
        protected boolean allowPutStack(int slotIndex, IAEItemStack toInsert) {
            IGregTechTileEntity igte = getBaseMetaTileEntity();

            return MTEHatchInputBusCompressed.this.allowPutStack(
                igte,
                slotIndex,
                ForgeDirection.UNKNOWN,
                toInsert == null ? null : toInsert.getItemStack());
        }

        @Override
        protected boolean allowPullStack(int slotIndex) {
            return false;
        }

        @Override
        protected AEInventory copyImpl() {
            return new MTEHatchInputBusCompressed.BusInventory(slotCount);
        }

        @Override
        public BaseActionSource getActionSource() {
            return new MachineSource((BaseMetaTileEntity) getBaseMetaTileEntity());
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
                && ((BaseMetaTileEntity) te).getMetaTileEntity() instanceof MTEHatchInputBusCompressed;
        }

        @Override

        public IMEInventory<IAEItemStack> getInventory(TileEntity te, ForgeDirection d, StorageChannel channel,
            BaseActionSource src) {
            if (channel == StorageChannel.ITEMS) {
                return (MTEHatchInputBusCompressed) ((BaseMetaTileEntity) te).getMetaTileEntity();
            }
            return null;
        }
    }
}
