package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;

public class MTEDigitalChestBaseGui extends MTEMachineWithItemScreenBaseGui<MTEDigitalChestBase> {

    private static final String LANG_PREFIX = "GT5U.machines.digitalchest.";
    private static final int STORED_SCREEN_WIDTH = SCREEN_WIDTH + SLOT_SIZE;

    private final LongSyncValue storedCount;
    private final BooleanSyncValue itemLock;
    private final BooleanSyncValue voidFull;
    private final ItemStackHandler lockInventory = new ItemStackHandler(1) {

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public MTEDigitalChestBaseGui(MTEDigitalChestBase machine) {
        super(machine);
        storedCount = new LongSyncValue(machine::getTrueItemCount);
        itemLock = new BooleanSyncValue(
            machine::isLocked,
            enabled -> { if (baseMetaTileEntity.isServerSide()) machine.setItemLockEnabled(enabled); }).allowC2S();
        voidFull = new BooleanSyncValue(machine::isVoidFull, machine::setVoidFull).allowC2S();
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("storedCount", storedCount);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected int getBasePanelWidth() {
        return super.getBasePanelWidth() + 2 * SLOT_SIZE;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1)
            .child(createScreen(panel, syncManager))
            .child(createScreenIO(panel, syncManager))
            .child(createFilterScreen(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected ParentWidget<?> createScreen(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createScreen(panel, syncManager).width(STORED_SCREEN_WIDTH);
    }

    @Override
    protected TextWidget<?> createScreenAmount(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.dynamic(() -> formatNumber(storedCount.getLongValue()))
            .asWidget()
            .width(STORED_SCREEN_WIDTH - 6)
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    @Override
    protected void addScreenText(Flow textColumn, ModularPanel panel, PanelSyncManager syncManager) {
        super.addScreenText(textColumn, panel, syncManager);
        textColumn.child(
            IKey.lang(LANG_PREFIX + "bulkcapacity")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));
        textColumn.child(
            IKey.str(formatNumber(machine.getMaxItemCount()))
                .asWidget()
                .width(STORED_SCREEN_WIDTH - 6)
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));
    }

    @Override
    protected ItemDisplayWidget createSlot(ModularPanel panel, PanelSyncManager syncManager) {
        GenericSyncValue<ItemStack, ?> storedItem = GenericSyncValue.builder(ItemStack.class)
            .getter(machine::getStoredItemType)
            .adapter(ByteBufAdapters.ITEM_STACK)
            .copy(ItemStack::copy)
            .build();
        return new ItemDisplayWidget().item(storedItem)
            .displayAmount(false)
            .bottomRel(0)
            .rightRel(0)
            .background(GTGuiTextures.SLOT_FLUID_TANK);
    }

    @Override
    protected int getInputSlotIndex() {
        return 0;
    }

    @Override
    protected int getOutputSlotIndex() {
        return 1;
    }

    @Override
    protected TextWidget<?> createFilterScreenAmountLabel(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.lang(LANG_PREFIX + "lockitem.label")
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    @Override
    protected TextWidget<?> createFilterScreenAmount(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.dynamic(() -> {
            ItemStack lockedItem = lockInventory.getStackInSlot(0);
            if (lockedItem != null) return lockedItem.getDisplayName();
            return translateToLocal(
                itemLock.getBoolValue() ? LANG_PREFIX + "lockitem.waiting" : LANG_PREFIX + "lockitem.empty");
        })
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    @Override
    protected ItemSlot createFilterSlot(ModularPanel panel, PanelSyncManager syncManager) {
        lockInventory.setStackInSlot(0, GTUtility.copyAmount(1, machine.getLockedItem()));
        ModularSlot lockSlot = new ModularSlot(lockInventory, 0) {

            @Override
            public void putStack(ItemStack stack) {
                if (ItemStack.areItemStacksEqual(stack, getStack())) return;
                if (baseMetaTileEntity.isServerSide()) {
                    machine.setLockedItem(stack);
                    stack = machine.getLockedItem();
                }
                super.putStack(GTUtility.copyAmount(1, stack));
            }
        };
        PhantomItemSlotSH lockSyncer = new PhantomItemSlotSH(lockSlot) {

            @Override
            public void detectAndSendChanges(boolean init) {
                if (!getSyncManager().isClient()) {
                    ItemStack lockedItem = machine.getLockedItem();
                    if (!ItemStack.areItemStacksEqual(lockedItem, lockInventory.getStackInSlot(0))) {
                        lockInventory.setStackInSlot(0, GTUtility.copyAmount(1, lockedItem));
                    }
                }
                super.detectAndSendChanges(init);
            }
        };
        return new PhantomItemSlot().syncHandler(lockSyncer)
            .bottomRel(0)
            .rightRel(0)
            .background(GTGuiTextures.SLOT_FLUID_TANK);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager)
            .child(
                new ToggleButton()
                    .value(new BooleanSyncValue(machine::isAutoOutputEnabled, machine::setAutoOutputEnabled).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM)
                    .tooltip(t -> t.addLine(translateToLocal(LANG_PREFIX + "autooutput"))))
            .child(
                new ToggleButton().value(itemLock)
                    .setEnabledIf(button -> !voidFull.getBoolValue())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_LOCK)
                    .tooltip(t -> {
                        t.addLine(translateToLocal(LANG_PREFIX + "lockitem.tooltip.0"));
                        t.addLine(translateToLocal(LANG_PREFIX + "lockitem.tooltip.1"));
                    }))
            .child(
                new ToggleButton()
                    .value(
                        new BooleanSyncValue(machine::isPassThroughEnabled, machine::setPassThroughEnabled).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT)
                    .tooltip(
                        t -> {
                            for (int i = 0; i < 8; i++) t.addLine(translateToLocal(LANG_PREFIX + "passthrough." + i));
                        }))
            .child(
                new ToggleButton().value(
                    new BooleanSyncValue(machine::isInputFilterDisabled, machine::setInputFilterDisabled).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT)
                    .tooltip(
                        t -> {
                            for (int i = 0; i < 4; i++)
                                t.addLine(translateToLocal(LANG_PREFIX + "disableinputfilter." + i));
                        }))
            .child(
                new ToggleButton().value(
                    new BooleanSyncValue(machine::isBlockExternalAccess, machine::setBlockExternalAccess).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED)
                    .tooltip(
                        t -> {
                            for (int i = 0; i < 9; i++)
                                t.addLine(translateToLocal(LANG_PREFIX + "block_external_access." + i));
                        }))
            .child(
                new ToggleButton()
                    .value(
                        new BooleanSyncValue(machine::isAllowInputFromOutputSide, machine::setAllowInputFromOutputSide)
                            .allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                    .tooltip(t -> t.addLine(translateToLocal(LANG_PREFIX + "inputfromoutput.tooltip"))))
            .child(
                new ToggleButton()
                    .value(new BooleanSyncValue(machine::isOutputToSlot, machine::setOutputToSlot).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT)
                    .tooltip(t -> {
                        t.addLine(translateToLocal(LANG_PREFIX + "outputslot.tooltip"));
                        t.addLine(translateToLocal(LANG_PREFIX + "outputslot.tooltip.1"));
                    }));
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow buttonRow = Flow.row()
            .coverChildren()
            .child(
                new ToggleButton()
                    .value(new BooleanSyncValue(machine::isVoidOverflow, machine::setVoidOverflow).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                    .tooltip(t -> {
                        t.addLine(translateToLocal(LANG_PREFIX + "voidoverflow.tooltip.0"));
                        t.addLine(translateToLocal(LANG_PREFIX + "voidoverflow.tooltip.1"));
                    }))
            .child(
                new ToggleButton().value(voidFull)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                    .tooltip(t -> {
                        t.addLine(translateToLocal(LANG_PREFIX + "voidfull.tooltip.0"));
                        t.addLine(translateToLocal(LANG_PREFIX + "voidfull.tooltip.1"));
                    }));

        return super.createBottomRightCornerFlow(panel, syncManager).child(buttonRow);
    }
}
