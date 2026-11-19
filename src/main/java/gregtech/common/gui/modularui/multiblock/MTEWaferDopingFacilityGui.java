package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEWaferDopingFacility;

public class MTEWaferDopingFacilityGui extends MTEMultiBlockBaseGui<MTEWaferDopingFacility> {

    public MTEWaferDopingFacilityGui(MTEWaferDopingFacility machine) {
        super(machine);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("waferStructureTier", new IntSyncValue(multiblock::getStructureTier));
        syncManager.syncValue("waferProgress", new IntSyncValue(multiblock::getProgressTicks));
        syncManager.syncValue(
            "waferParallel",
            new IntSyncValue(multiblock::getRequestedParallel, multiblock::setRequestedParallel).allowC2S());
        syncManager.syncValue("waferMultiplier", new IntSyncValue(multiblock::getActiveParticleMultiplier));
        syncManager.syncValue("waferTemperature", new IntSyncValue(multiblock::getCurrentTemperature));
        syncManager.syncValue("waferMinimumTemperature", new IntSyncValue(multiblock::getMinimumTemperature));
        syncManager.syncValue("waferMaximumTemperature", new IntSyncValue(multiblock::getMaximumTemperature));
        syncManager.syncValue("waferSignalPower", new IntSyncValue(multiblock::getActiveSignalPower));
        syncManager.syncValue("waferSignalTime", new IntSyncValue(multiblock::getSignalTimeRemainingSeconds));
        syncManager.syncValue("waferTempFailed", new IntSyncValue(() -> multiblock.hasTemperatureFailure() ? 1 : 0));
        syncManager.syncValue("waferSignalFailed", new IntSyncValue(() -> multiblock.hasSignalFailure() ? 1 : 0));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue tier = syncManager.findSyncHandler("waferStructureTier", IntSyncValue.class);
        IntSyncValue progress = syncManager.findSyncHandler("waferProgress", IntSyncValue.class);
        IntSyncValue parallel = syncManager.findSyncHandler("waferParallel", IntSyncValue.class);
        IntSyncValue multiplier = syncManager.findSyncHandler("waferMultiplier", IntSyncValue.class);
        IntSyncValue temperature = syncManager.findSyncHandler("waferTemperature", IntSyncValue.class);
        IntSyncValue minTemperature = syncManager.findSyncHandler("waferMinimumTemperature", IntSyncValue.class);
        IntSyncValue maxTemperature = syncManager.findSyncHandler("waferMaximumTemperature", IntSyncValue.class);
        IntSyncValue signal = syncManager.findSyncHandler("waferSignalPower", IntSyncValue.class);
        IntSyncValue signalTime = syncManager.findSyncHandler("waferSignalTime", IntSyncValue.class);
        IntSyncValue tempFailed = syncManager.findSyncHandler("waferTempFailed", IntSyncValue.class);
        IntSyncValue signalFailed = syncManager.findSyncHandler("waferSignalFailed", IntSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted("GT5U.text.structure_tier", tier.getIntValue()))))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector
                            .translateToLocalFormatted("GT5U.wafer_doping.gui.progress", progress.getIntValue()))))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector
                            .translateToLocalFormatted("GT5U.wafer_doping.gui.parallel", parallel.getIntValue()))))
            .child(
                new TextFieldWidget().setFormatAsInteger(true)
                    .value(parallel)
                    .width(48)
                    .height(14))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.wafer_doping.gui.particle_multiplier",
                            multiplier.getIntValue()))))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.wafer_doping.gui.temperature",
                            temperature.getIntValue(),
                            minTemperature.getIntValue(),
                            maxTemperature.getIntValue()))))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.wafer_doping.gui.chemical_signal",
                            signal.getIntValue(),
                            signalTime.getIntValue()))))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.wafer_doping.gui.temperature_failed",
                            StatCollector.translateToLocal(
                                tempFailed.getIntValue() == 0 ? "GT5U.generic.false" : "GT5U.generic.true")))))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.wafer_doping.gui.signal_failed",
                            StatCollector.translateToLocal(
                                signalFailed.getIntValue() == 0 ? "GT5U.generic.false" : "GT5U.generic.true")))));
    }
}
