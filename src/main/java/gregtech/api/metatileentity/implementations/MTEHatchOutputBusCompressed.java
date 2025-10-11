package gregtech.api.metatileentity.implementations;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizon.gtnhlib.capability.item.IItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.IItemSource;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.OutputBusType;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.widgets.PhantomItemButton;
import gregtech.api.interfaces.IOutputBusTransaction;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.gui.modularui.widget.AEBaseSlot;
import gregtech.common.gui.modularui.widget.AESlotWidget;
import gregtech.common.inventory.AEInventory;

public class MTEHatchOutputBusCompressed extends MTEHatchOutputBus implements IMEMonitor<IAEItemStack> {

    public final int slotCount;
    public final long stackCapacity;
    public long stackLimitOverride;

    private final AEInventory inventory;

    private final int[] busSlots;

    public MTEHatchOutputBusCompressed(int id, String name, String nameRegional, int tier, int slots,
        long stackCapacity) {
        super(
            id,
            name,
            nameRegional,
            tier,
            ArrayExt.of(
                "Item Output for Multiblocks",
                "Capacity: " + slots + " slots, " + GTUtility.formatNumbers(stackCapacity) + " stacks each",
                "Left click with data stick to save bus config",
                "Right click with data stick to load bus config",
                "Stores more than 1 stack per slot",
                "Items cannot be extracted or inserted via the GUI",
                "Can only be automated by GT and AE, low throughput solutions like Ender IO do not work!"),
            0);

        this.slotCount = slots;
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
    public void addListener(IMEMonitorHandlerReceiver<IAEItemStack> l, Object verificationToken) {
        inventory.addListener(l, verificationToken);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver<IAEItemStack> l) {
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
        return inventory.extractItems(request, mode, src);
    }

    @Override
    public StorageChannel getChannel() {
        return inventory.getChannel();
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
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        List<AEBaseSlot> slots = new ArrayList<>();

        SlotGroup slotGroup = SlotGroup.ofItemHandler(inventory, 4)
            .startFromSlot(0)
            .endAtSlot(slotCount - 1)
            .slotCreator(index -> {
                AEBaseSlot slot = new AEBaseSlot(inventory, index) {

                    @Override
                    public void putStack(ItemStack stack) {
                        // no-op to disable MC's slot syncing, which truncates the >int max size, if present
                    }
                };
                slots.add(slot);
                return slot;
            })
            .widgetCreator(slot -> new AESlotWidget(slot) {

                @Override
                protected String getAmountTooltip() {
                    return GTUtility.translate(
                        "GT5U.gui.text.amount_out_of",
                        getAEStack().getStackSize(),
                        inventory.getAESlotLimit(slot.getSlotIndex()));
                }
            })
            .background(getGUITextureSet().getItemSlot())
            .canInsert(false)
            .canTake(false)
            .build();

        builder.widget(slotGroup.setPos(52, 7));

        builder.widget(new FakeSyncWidget<>(() -> {
            slots.forEach(AEBaseSlot::onSlotChanged);
            return inventory.writeToNBT(new NBTTagCompound());
        }, tag -> {
            inventory.readFromNBT(tag);
            slots.forEach(AEBaseSlot::onSlotChanged);
        }, (buffer, tag) -> {
            try {
                buffer.writeNBTTagCompoundToBuffer(tag);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, buffer -> {
            try {
                return buffer.readNBTTagCompoundFromBuffer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        if (acceptsItemLock()) {
            builder.widget(
                new PhantomItemButton(this).setPos(getGUIWidth() - 25, 40)
                    .setBackground(PhantomItemButton.FILTER_BACKGROUND));
        }

        builder.widget(createSettingsButton());
        buildContext.addSyncedWindow(SETTINGS_PANEL_WINDOW_ID, this::createSettingsPanel);
    }

    private static final int SETTINGS_PANEL_WINDOW_ID = 8;

    private ButtonWidget createSettingsButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(SETTINGS_PANEL_WINDOW_ID);
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                ret.add(GTUITextures.SCREWDRIVER);
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(translateToLocal("GT5U.gui.button.compressed_bus_settings"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(new Pos2d(151, 6))
            .setSize(18, 18);
        return (ButtonWidget) button;
    }

    public ModularWindow createSettingsPanel(EntityPlayer player) {
        final int w = 120;
        final int h = 130;
        final int parentW = getGUIWidth();
        final int parentH = getGUIHeight();

        ModularWindow.Builder builder = ModularWindow.builder(w, h);

        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(parentW, parentH))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(parentW, parentH), new Size(w, h))
                        .add(w - 3, 0)));

        builder.widget(
            new TextWidget(EnumChatFormatting.UNDERLINE + GTUtility.translate("GT5U.gui.text.bus_settings"))
                .setPos(0, 2)
                .setSize(120, 18));

        builder.widget(new FakeSyncWidget.LongSyncer(() -> stackLimitOverride, val -> stackLimitOverride = val));

        builder.widget(
            TextWidget.localised("GT5U.gui.text.stack_capacity")
                .setPos(0, 24)
                .setSize(120, 18));

        NumericWidget textField = (NumericWidget) new NumericWidget().setSetter(val -> stackLimitOverride = (long) val)
            .setGetter(() -> stackLimitOverride)
            .setValidator(val -> GTUtility.clamp((long) val, 1, stackCapacity))
            .setDefaultValue(stackCapacity)
            .setScrollValues(1, 4, 64)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .dynamicTooltip(
                () -> Collections.singletonList(GTUtility.translate("GT5U.gui.text.rangedvalue", 1, stackCapacity)))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(w - 12 * 2, 18)
            .setPos(12, 40)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);

        builder.widget(textField);

        return builder.build();
    }

    @Override
    public IOutputBusTransaction createTransaction() {
        return new CompressedOutputBusTransaction();
    }

    @Override
    protected IItemSource getItemSource(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing() ? inventory.getItemIO() : null;
    }

    @Override
    protected IItemSink getItemSink(ForgeDirection side) {
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
        protected BaseActionSource getActionSource() {
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
}
