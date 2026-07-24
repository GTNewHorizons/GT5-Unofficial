package gregtech.common.gui.modularui.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.UUID;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverAdvancedWirelessController;
import gregtech.common.covers.conditions.RedstoneCondition;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneReceiverBase;
import gregtech.common.gui.modularui.cover.base.CoverAdvancedWirelessRedstoneBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import gregtech.common.modularui2.widget.builder.EnumSeriesBuilder;

public class CoverAdvancedWirelessControllerGui
    extends CoverAdvancedWirelessRedstoneBaseGui<CoverAdvancedWirelessController> {

    public CoverAdvancedWirelessControllerGui(CoverAdvancedWirelessController cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.advanced_wireless_controller";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<RedstoneCondition, ?> conditionModeSyncValue = new EnumSyncValue<>(
            RedstoneCondition.class,
            cover::getRedstoneCondition,
            cover::setRedstoneCondition).allowC2S();
        syncManager.syncValue("condition_mode", conditionModeSyncValue);
        EnumSyncValue<CoverAdvancedRedstoneReceiverBase.GateMode, ?> gateModeSync = new EnumSyncValue<>(
            CoverAdvancedRedstoneReceiverBase.GateMode.class,
            cover::getGateMode,
            cover::setMode).allowC2S();
        syncManager.syncValue("gateMode", gateModeSync);
        BooleanSyncValue safeModeSyncValue = new BooleanSyncValue(cover::isSafeMode, cover::setSafeMode).allowC2S();
        StringSyncValue frequencySyncer = new StringSyncValue(cover::getFrequency, cover::setFrequency).allowC2S();
        syncManager.syncValue("frequency", frequencySyncer);
        UUID uuid = data.getPlayer()
            .getUniqueID();
        column.crossAxisAlignment(Alignment.CrossAxis.START)
            .child(
                Flow.row()
                    .coverChildren()
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child(
                        Flow.column()
                            .coverChildren()
                            .crossAxisAlignment(Alignment.CrossAxis.START)
                            .child(makeFrequencyRow(frequencySyncer))
                            .child(makeButtonRow(uuid))
                            .child(
                                new Grid().coverChildren()
                                    .minElementMarginRight(WIDGET_MARGIN)
                                    .minElementMarginBottom(2)
                                    .minElementMarginTop(0)
                                    .minElementMarginLeft(0)
                                    .alignment(Alignment.CenterLeft)
                                    .row(
                                        new SelectButton()
                                            .value(
                                                LinkedBoolValue
                                                    .of(conditionModeSyncValue, RedstoneCondition.ENABLE_WITH_REDSTONE))
                                            .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                            .size(16),
                                        IKey.lang("gt.interact.desc.MachContr.EnRedstone")
                                            .asWidget())
                                    .row(
                                        new SelectButton().value(
                                            LinkedBoolValue
                                                .of(conditionModeSyncValue, RedstoneCondition.DISABLE_WITH_REDSTONE))
                                            .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                            .size(16),
                                        IKey.lang("gt.interact.desc.MachContr.DisRedstone")
                                            .asWidget())
                                    .row(
                                        new SelectButton()
                                            .value(
                                                LinkedBoolValue.of(conditionModeSyncValue, RedstoneCondition.DISABLE))
                                            .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                                            .size(16),
                                        IKey.lang("gt.interact.desc.MachContr.DisMachine")
                                            .asWidget())
                                    .row(
                                        new ToggleButton().value(safeModeSyncValue)
                                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                                            .size(16),
                                        IKey.lang("gt.interact.desc.MachContr.SafeMode")
                                            .asWidget())))
                    .childPadding(20)
                    .child(makeThirdFlow(syncManager, data)));
    }

    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        EnumSyncValue<CoverAdvancedRedstoneReceiverBase.GateMode, ?> gateMode = (EnumSyncValue<CoverAdvancedRedstoneReceiverBase.GateMode, ?>) syncManager
            .getSyncHandlerFromMapKey("gateMode:0");
        return Flow.column()
            .size(50, 110)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .child(new TextWidget(translateToLocal("gt.interact.desc.gatemode")))
            .childPadding(10)
            .child(
                new EnumSeriesBuilder<>(CoverAdvancedRedstoneReceiverBase.GateMode.class).value(gateMode)
                    .overlay(
                        GTGuiTextures.OVERLAY_BUTTON_GATE_AND,
                        GTGuiTextures.OVERLAY_BUTTON_GATE_NAND,
                        GTGuiTextures.OVERLAY_BUTTON_GATE_OR,
                        GTGuiTextures.OVERLAY_BUTTON_GATE_NOR,
                        GTGuiTextures.OVERLAY_BUTTON_ANALOG)
                    .build(GuiAxis.Y)
                    .width(18)
                    .marginRight(1)

            );
    }
}
