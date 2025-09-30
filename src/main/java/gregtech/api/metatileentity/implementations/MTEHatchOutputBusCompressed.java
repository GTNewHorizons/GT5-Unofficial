package gregtech.api.metatileentity.implementations;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemList;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
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
import gregtech.api.enums.OutputBusType;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.widgets.PhantomItemButton;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.gui.modularui.util.ProxiedItemHandlerModifiable;

public class MTEHatchOutputBusCompressed extends MTEHatchOutputBus implements IMEMonitor<IAEItemStack> {

    public final int slotCount;
    public final int stackCapacity;

    public int stackLimitOverride;

    private final IItemHandlerModifiable proxiedHandler;

    private final Map<IMEMonitorHandlerReceiver<IAEItemStack>, Object> listeners = new HashMap<>();

    private static final BaseActionSource ACTION_SOURCE = new BaseActionSource();

    public MTEHatchOutputBusCompressed(int id, String name, String nameRegional, int tier, int slots,
        int stackCapacity) {
        super(
            id,
            name,
            nameRegional,
            tier,
            ArrayExt.of(
                "Item Output for Multiblocks",
                "Capacity: " + slots + " slots, " + GTUtility.formatNumbers(stackCapacity) + " stacks each",
                "Items cannot be extracted or inserted via the GUI",
                "Left click with data stick to save filter config",
                "Right click with data stick to load filter config"),
            0);

        this.slotCount = slots;
        this.stackCapacity = stackCapacity;
        this.proxiedHandler = null;
    }

    protected MTEHatchOutputBusCompressed(MTEHatchOutputBusCompressed prototype) {
        super(prototype.mName, prototype.mTier, prototype.slotCount, prototype.mDescriptionArray, prototype.mTextures);
        this.slotCount = prototype.slotCount;
        this.stackCapacity = prototype.stackCapacity;
        this.stackLimitOverride = prototype.stackCapacity;
        this.proxiedHandler = new ProxiedItemHandlerModifiable(inventoryHandler) {

            @Override
            public int getSlotLimit(int slot) {
                return 64 * stackCapacity;
            }
        };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutputBusCompressed(this);
    }

    @Override
    public int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        return GTUtility.longToInt((stack == null ? 64 : stack.getMaxStackSize()) * (long) Math.min(stackCapacity, stackLimitOverride));
    }

    @Override
    public int getInventoryStackLimit() {
        return getStackSizeLimit(-1, null);
    }

    @Override
    public OutputBusType getBusType() {
        return lockedItem == null ? OutputBusType.CompressedUnfiltered : OutputBusType.CompressedFiltered;
    }

    @Override
    public boolean storePartial(ItemStack stack, boolean simulate) {
        if (!simulate) markDirty();

        int initial = stack.stackSize;

        boolean success = super.storePartial(stack, simulate);

        if (!simulate) {
            int inserted = initial - stack.stackSize;

            if (inserted > 0) {
                Iterable<IAEItemStack> changes = GTDataUtils.singletonIterable(
                    AEItemStack.create(stack)
                        .setStackSize(inserted));

                listeners.forEach((handler, o) -> {
                    if (handler.isValid(o)) {
                        handler.postChange(this, changes, ACTION_SOURCE);
                    }
                });
            }
        }

        return success;
    }

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        return proxiedHandler;
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        ItemList list = new ItemList();

        for (ItemStack stack : mInventory) {
            if (GTUtility.isStackValid(stack)) list.add(AEItemStack.create(stack));
        }

        return list;
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver<IAEItemStack> l, Object verificationToken) {
        listeners.put(l, verificationToken);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver<IAEItemStack> l) {
        listeners.remove(l);
    }

    @Override
    public AccessRestriction getAccess() {
        // Can only ever be extracted from
        return AccessRestriction.READ;
    }

    @Override
    public boolean isPrioritized(IAEItemStack input) {
        return false;
    }

    @Override
    public boolean canAccept(IAEItemStack input) {
        return false;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return false;
    }

    @Override
    public IAEItemStack injectItems(IAEItemStack input, Actionable type, BaseActionSource src) {
        return input;
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
        IAEItemStack extracted = null;

        for (int i = 0; i < slotCount; i++) {
            ItemStack slot = mInventory[i];

            if (GTUtility.isStackInvalid(slot) || !request.isSameType(slot)) continue;

            int toConsume = (int) Math.min(Integer.MAX_VALUE, Math.min(slot.stackSize, request.getStackSize()));

            if (mode == Actionable.MODULATE) {
                slot.stackSize -= toConsume;

                if (slot.stackSize <= 0) {
                    mInventory[i] = null;
                }
            }

            if (extracted == null) {
                extracted = AEItemStack.create(slot)
                    .setStackSize(0);
            }

            extracted.incStackSize(toConsume);
        }

        if (extracted != null && mode == Actionable.MODULATE) {
            Iterable<IAEItemStack> changes = GTDataUtils.singletonIterable(
                extracted.copy()
                    .setStackSize(-extracted.getStackSize()));

            listeners.forEach((handler, o) -> {
                if (handler.isValid(o)) {
                    handler.postChange(this, changes, src);
                }
            });
        }

        return extracted;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(slotCount - 1)
                .background(getGUITextureSet().getItemSlot())
                .canInsert(false)
                .canTake(false)
                .build()
                .setPos(52, 7));

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
                    widget.getContext().openSyncedWindow(SETTINGS_PANEL_WINDOW_ID);
                }
            })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                ret.add(GTUITextures.SCREWDRIVER);
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.compressed_bus_settings"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(new Pos2d(174, 91))
            .setSize(16, 16);
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

        // Window header
        builder.widget(
            new TextWidget(EnumChatFormatting.UNDERLINE + translateToLocal("GT5U.gui.text.bus_settings")).setPos(0, 2)
                .setSize(120, 18));

        // Syncing widgets
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> stackLimitOverride, val -> stackLimitOverride = val));

        // "Max parallel" header text
        builder.widget(
            TextWidget.localised("GT5U.gui.text.stack_capacity")
                .setPos(0, 24)
                .setSize(120, 18));

        // Max parallel setter text box
        NumericWidget textField = (NumericWidget) new NumericWidget()
            .setSetter(val -> stackLimitOverride = (int) val)
            .setGetter(() -> stackLimitOverride)
            .setValidator(val -> GTUtility.clamp((int) val, 1, stackCapacity))
            .setDefaultValue(stackCapacity)
            .setScrollValues(1, 4, 64)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .dynamicTooltip(
                () -> Collections.singletonList(translateToLocalFormatted("GT5U.gui.text.rangedvalue", 1, stackCapacity)))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(72, 18)
            .setPos(12, 40)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);

        builder.widget(textField);

        return builder.build();
    }

}
