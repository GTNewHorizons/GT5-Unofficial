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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizon.gtnhlib.capability.item.IItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.IItemSource;
import com.gtnewhorizon.gtnhlib.util.ItemUtil;
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
import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
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
import gregtech.common.gui.modularui.util.ProxiedItemHandlerModifiable;
import gregtech.common.gui.modularui.widget.AEBaseSlot;
import gregtech.common.gui.modularui.widget.AESlotWidget;
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

    public MTEHatchInputBusCompressed(int id, String name, String nameRegional, int tier, int slots,
        long stackCapacity) {
        super(
            id,
            name,
            nameRegional,
            tier,
            0,
            ArrayExt.of(
                "Item Input for Multiblocks",
                "Shift + right click with screwdriver to turn Sort mode on/off",
                "Capacity: " + slots + " slots, " + GTUtility.formatNumbers(stackCapacity) + " stacks each",
                "Left click with data stick to save bus config",
                "Right click with data stick to load bus config",
                "Stores more than 1 stack per slot",
                "Items cannot be extracted or inserted via the GUI",
                "Can only be automated by GT and AE, low throughput solutions like Ender IO do not work!"));

        this.slotCount = slots;
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
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addInputBusUIWidgets(builder, buildContext);

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
    protected IItemSource getItemSource(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing() ? inventory.getItemIO() : null;
    }

    @Override
    protected IItemSink getItemSink(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing() ? inventory.getItemIO() : null;
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
        protected BaseActionSource getActionSource() {
            return new MachineSource((BaseMetaTileEntity) getBaseMetaTileEntity());
        }
    }
}
