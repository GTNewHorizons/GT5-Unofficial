package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.gui.modularui.util.FilterSlot;
import gregtech.common.tileentities.storage.MTESuperChest;

public class MTESuperChestGui extends MTETieredMachineBlockBaseGui<MTESuperChest> {

    private static final int ITEM_SCREEN_WIDTH = 71;
    private static final int ITEM_SCREEN_HEIGHT = 54;

    public MTESuperChestGui(MTESuperChest machine) {
        super(machine);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue itemCountSyncer = new IntSyncValue(machine::getItemCount);
        syncManager.syncValue("itemCount", itemCountSyncer);

        ParentWidget<?> screen = CommonWidgets.createFluidScreen(ITEM_SCREEN_WIDTH, ITEM_SCREEN_HEIGHT)
            .child(
                Flow.column()
                    .childPadding(1)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child(
                        IKey.lang("GT5U.gui.text.item_amount")
                            .asWidget()
                            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE))
                    .child(
                        IKey.dynamic(() -> formatNumber(itemCountSyncer.getIntValue()))
                            .asWidget()
                            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE)))
            .child(new ItemSlot() {

                @Override
                protected void drawSlotAmountText(int amount, String text) {
                    GuiDraw.drawStandardSlotAmountText(itemCountSyncer.getIntValue(), text, getArea());
                }
            }.slot(new ModularSlot(machine.inventoryHandler, 2).accessibility(false, false))
                .bottomRel(0)
                .rightRel(0));

        Flow ioColumn = Flow.column()
            .coverChildrenWidth()
            .fullHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
            .child(
                new ItemSlot().slot(
                    new ModularSlot(machine.inventoryHandler, 0).singletonSlotGroup()
                        .filter(machine::isItemInputAllowed))
                    .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_IN_STANDARD))
            .child(
                new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, 1).canPut(false))
                    .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD));

        ParentWidget<?> lockScreen = CommonWidgets.createFluidScreen(ITEM_SCREEN_WIDTH, ITEM_SCREEN_HEIGHT)
            .child(
                Flow.column()
                    .childPadding(1)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child(
                        IKey.lang("GT5U.machines.digitalchest.lockitem.label")
                            .asWidget()
                            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE))
                    .child(
                        IKey.dynamic(
                            () -> machine.getLockedItem() == null
                                ? translateToLocal("GT5U.machines.digitalchest.lockitem.empty")
                                : machine.getLockedItem()
                                    .getDisplayName())
                            .asWidget()
                            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE)))
            .child(
                new FilterSlot(machine::getLockedItem, machine::setLockedItem).bottomRel(0)
                    .rightRel(0));

        return super.createContentSection(panel, syncManager).child(
            Flow.row()
                .coverChildren()
                .childPadding(1)
                .child(screen)
                .child(ioColumn)
                .child(lockScreen));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager).child(
            new ToggleButton().value(new BooleanSyncValue(machine::isOutputItems, machine::setOutputItems).allowC2S())
                .overlay(GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.machines.digitalchest.autooutput.tooltip"))))
            .child(
                new ToggleButton().value(new BooleanSyncValue(machine::isLockItems, machine::lockItems).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_LOCK)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.lockitem.tooltip"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.lockitem.tooltip.1"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.lockitem.tooltip.2"));
                    }))
            .child(
                new ToggleButton()
                    .value(
                        new BooleanSyncValue(machine::isAllowInputFromOutputSide, machine::setAllowInputFromOutputSide)
                            .allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                    .tooltip(t -> t.addLine(translateToLocal("GT5U.machines.digitalchest.inputfromoutput.tooltip"))))
            .child(
                new ToggleButton()
                    .value(new BooleanSyncValue(machine::isOutputToSlot, machine::setOutputToSlot).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.outputslot.tooltip"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.outputslot.tooltip.1"));
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
                new ToggleButton().value(new BooleanSyncValue(machine::isVoidAll, machine::setVoidAll).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                    .tooltip(t -> {
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.voidfull.tooltip"));
                        t.addLine(translateToLocal("GT5U.machines.digitalchest.voidfull.tooltip.1"));
                    }));

        return super.createBottomRightCornerFlow(panel, syncManager).child(buttonRow);
    }
}
