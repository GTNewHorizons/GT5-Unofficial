package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchIONodeController;

public class MTEHatchIONodeControllerGui extends MTEHatchBaseGui<MTEHatchIONodeController> {

    public MTEHatchIONodeControllerGui(MTEHatchIONodeController machine) {
        super(machine);
    }

    protected EnumSyncValue<MTEHatchIONodeController.Control, ?> getControlSync(PanelSyncManager syncManager) {
        return syncManager.getOrCreateSyncHandler(
            "control",
            EnumSyncValue.class,
            () -> new EnumSyncValue<>(MTEHatchIONodeController.Control.class, machine::getControl, machine::setControl)
                .allowC2S());
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        // spotless:off
        var controlSync = getControlSync(syncManager);

        Flow tabButtons = Flow.row()
            .coverChildren()
            .childPadding(2)
            .horizontalCenter()
            .child(
                new SelectButton().value(LinkedBoolValue.of(controlSync, MTEHatchIONodeController.Control.PAUSE))
                    .size(78, 18)
                    .overlay(IKey.lang("GT5U.gui.text.control-pause"))
                    .tooltip(new RichTooltip().add(MTEHatchIONodeController.Control.PAUSE.getTooltip())))
            .child(
                new SelectButton().value(LinkedBoolValue.of(controlSync, MTEHatchIONodeController.Control.PARALLEL))
                    .size(78, 18)
                    .overlay(IKey.lang("GT5U.gui.text.control-parallel"))
                    .tooltip(new RichTooltip().add(MTEHatchIONodeController.Control.PARALLEL.getTooltip())));

        Flow pages = Flow.column()
            .fullWidth()
            .coverChildrenHeight()
            .childPadding(4)
            .collapseDisabledChild()
            .child(tabButtons)
            .child(
                createPauseControlPage(panel, syncManager)
                    .setEnabledIf(widget -> controlSync.getValue() == MTEHatchIONodeController.Control.PAUSE))
            .child(
                createParallelControlPage(panel, syncManager)
                    .setEnabledIf(widget -> controlSync.getValue() == MTEHatchIONodeController.Control.PARALLEL));

        return super.createContentSection(panel, syncManager)
            .child(pages);
        // spotless:on
    }

    private ParentWidget<?> createPauseControlPage(ModularPanel panel, PanelSyncManager syncManager) {
        // spotless:off
        return new ParentWidget<>().fullWidth()
            .child(SettingsPanel.builder()
                .addEnumCycleButton(
                    IKey.lang("GT5U.gui.text.bec-mode"),
                    MTEHatchIONodeController.PauseMode.class,
                    machine::getPauseMode,
                    machine::setPauseMode,
                    (_, _, button) -> button.tooltip(
                        (MTEHatchIONodeController.PauseMode pauseMode) -> new RichTooltip().add(pauseMode.getTooltip())))
                .build(panel, syncManager, getContentHolderHeight())
                .horizontalCenter());
        // spotless:on
    }

    private ParentWidget<?> createParallelControlPage(ModularPanel panel, PanelSyncManager syncManager) {
        // spotless:off
        return new ParentWidget<>().fullWidth()
            .child(SettingsPanel.builder()
                .addEnumCycleButton(
                    IKey.lang("GT5U.gui.text.bec-mode"),
                    MTEHatchIONodeController.ParallelMode.class,
                    machine::getParallelMode,
                    machine::setParallelMode,
                    (_, _, button) -> button.tooltip(
                        (MTEHatchIONodeController.ParallelMode parallelMode) -> new RichTooltip().add(parallelMode.getTooltip())))
                .addReadout(
                    IKey.lang("GT5U.gui.text.control-parallel.parallel"),
                    new IntSyncValue(machine::signalReceived),
                    amount -> IKey.str(NumberFormatUtil.formatNumber(amount))
                )
                .build(panel, syncManager, getContentHolderHeight())
                .horizontalCenter());
        // spotless:on
    }
}
