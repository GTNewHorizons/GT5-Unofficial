package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;

public class MTEDigitalChestBaseGui extends MTETieredMachineBlockBaseGui<MTEDigitalChestBase> {

    private static final int SCREEN_WIDTH = 71;
    private static final int STORED_SCREEN_WIDTH = SCREEN_WIDTH + SLOT_SIZE;
    private static final int SCREEN_HEIGHT = 54;
    private static final String ITEM_INVENTORY = "item_inv";

    private final IntSyncValue storedCount;

    public MTEDigitalChestBaseGui(MTEDigitalChestBase machine) {
        super(machine);
        storedCount = new IntSyncValue(machine::getItemCount);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        syncManager.syncValue("storedCount", storedCount);
        syncManager.registerSlotGroup(ITEM_INVENTORY, 1);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected int getBasePanelWidth() {
        return super.getBasePanelWidth() + SLOT_SIZE;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1)
            .child(createStoredItemScreen())
            .child(createItemIO())
            .child(createItemLockScreen());

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private ParentWidget<?> createStoredItemScreen() {
        GenericSyncValue<ItemStack, ?> storedItem = GenericSyncValue.builder(ItemStack.class)
            .getter(machine::getItemStack)
            .adapter(ByteBufAdapters.ITEM_STACK)
            .copy(ItemStack::copy)
            .build();
        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(
                IKey.lang("GT5U.gui.text.item_amount")
                    .asWidget()
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE))
            .child(
                IKey.dynamic(() -> formatNumber(storedCount.getIntValue()))
                    .asWidget()
                    .width(STORED_SCREEN_WIDTH - 6)
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE))
            .child(
                IKey.str("/ " + formatNumber(machine.getMaxItemCount()))
                    .asWidget()
                    .width(STORED_SCREEN_WIDTH - 6)
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        return CommonWidgets.createHatchScreen(STORED_SCREEN_WIDTH, SCREEN_HEIGHT)
            .child(textColumn)
            .child(
                new ItemDisplayWidget().item(storedItem)
                    .displayAmount(false)
                    .bottomRel(0)
                    .rightRel(0)
                    .background(GTGuiTextures.SLOT_FLUID_TANK));
    }

    private Flow createItemIO() {
        ModularSlot inputSlot = new ModularSlot(machine.inventoryHandler, 0).slotGroup(ITEM_INVENTORY);

        return Flow.column()
            .coverChildrenWidth()
            .fullHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
            .child(
                new ItemSlot().slot(inputSlot)
                    .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_IN_STANDARD))
            .child(
                new ItemSlot().slot(
                    new ModularSlot(machine.inventoryHandler, 1).slotGroup(ITEM_INVENTORY)
                        .canPut(false))
                    .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD));
    }

    private ParentWidget<?> createItemLockScreen() {
        ItemStackHandler lockInventory = new ItemStackHandler(1) {

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
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
        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(
                IKey.lang("GT5U.machines.digitalchest.lockitem.label")
                    .asWidget()
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE))
            .child(IKey.dynamic(() -> {
                ItemStack lockedItem = lockInventory.getStackInSlot(0);
                return lockedItem == null ? translateToLocal("GT5U.machines.digitalchest.lockitem.empty")
                    : lockedItem.getDisplayName();
            })
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        return CommonWidgets.createHatchScreen(SCREEN_WIDTH, SCREEN_HEIGHT)
            .child(textColumn)
            .child(
                new PhantomItemSlot().syncHandler(lockSyncer)
                    .bottomRel(0)
                    .rightRel(0)
                    .background(GTGuiTextures.SLOT_FLUID_TANK));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager)
            .child(
                new ToggleButton()
                    .value(new BooleanSyncValue(machine::isAutoOutputEnabled, machine::setAutoOutputEnabled).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM)
                    .tooltip(t -> t.addLine(translateToLocal("GT5U.machines.digitalchest.autooutput"))))
            .child(
                new ToggleButton()
                    .value(
                        new BooleanSyncValue(machine::isPassThroughEnabled, machine::setPassThroughEnabled).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough.1"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough.2"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough.3"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough.4"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough.5"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough.6"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.passthrough.7"));
                    }))
            .child(
                new ToggleButton().value(
                    new BooleanSyncValue(machine::isInputFilterDisabled, machine::setInputFilterDisabled).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.disableinputfilter"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.disableinputfilter.1"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.disableinputfilter.2"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.disableinputfilter.3"));
                    }))
            .child(
                new ToggleButton().value(
                    new BooleanSyncValue(machine::isBlockExternalAccess, machine::setBlockExternalAccess).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_LOCK)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.1"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.2"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.3"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.4"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.5"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.6"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.7"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.block_external_access.8"));
                    }));
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow buttonRow = Flow.row()
            .coverChildren()
            .marginRight(SLOT_SIZE)
            .child(
                new ToggleButton()
                    .value(new BooleanSyncValue(machine::isVoidOverflow, machine::setVoidOverflow).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.voidoverflow.tooltip"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.voidoverflow.tooltip.1"));
                    }))
            .child(
                new ToggleButton().value(new BooleanSyncValue(machine::isVoidFull, machine::setVoidFull).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.voidfull.tooltip"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.voidfull.tooltip.1"));
                    }));

        return super.createBottomRightCornerFlow(panel, syncManager).child(buttonRow);
    }
}
