package gregtech.common.gui.modularui.hatch;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchInputBusDebugGui extends MTEHatchBaseGui<MTEHatchInputBusDebug> {

    public MTEHatchInputBusDebugGui(MTEHatchInputBusDebug base) {
        super(base);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            SlotGroupWidget.builder()
                .matrix("IIII", "IIII", "IIII", "IIII")
                .key('I', index -> {
                    return new PhantomItemSlot().slot(
                        new ModularSlot(hatch.phantomHolder, index).singletonSlotGroup(50 + index)
                            .accessibility(true, false));
                })
                .build()
                .marginTop(4)
                .align(Alignment.TopCenter));
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        BooleanSyncValue finiteModeSyncer = new BooleanSyncValue(() -> hatch.finiteMode, val -> hatch.finiteMode = val);
        syncManager.syncValue("finiteMode", finiteModeSyncer);
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftCornerFlow(panel, syncManager).child(createInfoButton())
            .child(createFiniteModePanelButton(syncManager, panel));
    }

    protected IWidget createFiniteModePanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler finiteModePanel = syncManager.syncedPanel(
            "finiteModePanel",
            true,
            (p_syncManager, syncHandler) -> openFiniteModeControlPanel(syncManager, parent));
        return new ButtonWidget<>().size(18, 18)
            .marginLeft(4)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_FINITE_MODE_PANEL)
            .onMousePressed(d -> {
                if (!finiteModePanel.isPanelOpen()) {
                    finiteModePanel.openPanel();
                } else {
                    finiteModePanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.finite_mode_panel")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openFiniteModeControlPanel(PanelSyncManager syncManager, ModularPanel parent) {
        return new ModularPanel("finiteModePanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 85)
            .child(
                Flow.column()
                    .top(5)
                    .sizeRel(1)
                    .padding(3)
                    .child(
                        IKey.lang("GT5U.gui.button.finite_mode.stack_size_parse")
                            .asWidget()
                            .marginBottom(4))
                    .child(makeStackSizeParseConfigurator())
                    .child(makeFiniteModeButtonRow(syncManager)))
            .child(createFiniteModeInfoButton());
    }

    private IWidget makeFiniteModeButtonRow(PanelSyncManager syncManager) {
        BooleanSyncValue finiteModeSyncer = syncManager.findSyncHandler("finiteMode", BooleanSyncValue.class);
        return Flow.row()
            .sizeRel(1)
            .padding(3)
            .height(30)
            .child(
                IKey.lang("GT5U.gui.button.finite_mode")
                    .asWidget())
            .child(
                new ToggleButton().size(20)
                    .value(finiteModeSyncer)
                    .overlay(
                        new DynamicDrawable(
                            () -> finiteModeSyncer.getValue() ? GTGuiTextures.OVERLAY_BUTTON_FINITE_MODE_ON
                                : GTGuiTextures.OVERLAY_BUTTON_FINITE_MODE_OFF))
                    .tooltip(t -> t.add(IKey.lang("GT5U.gui.button.finite_mode_toggle"))));
    }

    private IWidget makeStackSizeParseConfigurator() {
        IntSyncValue stackSizeParseSyncer = new IntSyncValue(
            () -> hatch.stackSizeParseAs,
            val -> hatch.stackSizeParseAs = val);
        return Flow.row()
            .widthRel(1)
            .marginBottom(4)
            .height(18)
            .paddingLeft(3)
            .paddingRight(3)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .child(makeStackSizeParseConfiguratorTextFieldWidget(stackSizeParseSyncer));
    }

    private IWidget makeStackSizeParseConfiguratorTextFieldWidget(IntSyncValue stackSizeParseConfiguratorSyncer) {
        return new TextFieldWidget().value(stackSizeParseConfiguratorSyncer)
            .setTextAlignment(Alignment.Center)
            .setNumbers(0, Integer.MAX_VALUE)
            .size(70, 14)
            .marginBottom(4)
            .marginRight(16);
    }

    private IWidget createFiniteModeInfoButton() {
        return new IDrawable.DrawableWidget(GuiTextures.BUBBLE).background(GTGuiTextures.BUTTON_STANDARD)
            .size(20)
            .bottom(5)
            .right(5)
            .tooltip(t -> { t.addLine(IKey.lang("GT5U.gui.button.finite_mode_info")); });
    }

    private IDrawable.DrawableWidget createInfoButton() {
        return new IDrawable.DrawableWidget(GuiTextures.BUBBLE).background(GTGuiTextures.BUTTON_STANDARD)
            .size(18)
            .tooltip(t -> {
                t.addLine("Drag Items into the Item Slots");
                t.addLine("Items in the slots will not be consumed");
            });
    }
}
