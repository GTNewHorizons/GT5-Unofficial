package gregtech.common.gui.modularui.singleblock.kinetic;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.common.gui.modularui.singleblock.base.MTEKineticRotorBaseGui;
import gregtech.common.tileentities.machines.basic.MTEKineticWaterGenerator;

public class MTEKineticWaterGeneratorGui extends MTEKineticRotorBaseGui<MTEKineticWaterGenerator> {

    public MTEKineticWaterGeneratorGui(MTEKineticWaterGenerator machine) {
        super(machine);
    }

    @Override
    protected ListWidget<IWidget, ?> getKineticInfoText(PanelSyncManager syncManager) {
        FloatSyncValue biomeMultiSync = syncManager.findSyncHandler("biomeMulti", FloatSyncValue.class);
        DoubleSyncValue flowMultiSync = syncManager.findSyncHandler("flowMulti", DoubleSyncValue.class);
        return super.getKineticInfoText(syncManager)
            .child(
                new TextWidget<>(
                    EnumChatFormatting.BLUE + ""
                        + EnumChatFormatting.UNDERLINE
                        + StatCollector.translateToLocal("GT5U.gui.text.biome_multi")).textAlign(Alignment.CENTER)
                            .fullWidth()
                            .marginBottom(1))
            .childIf(
                biomeMultiSync.getFloatValue() == 1f,
                () -> new TextWidget<>(
                    EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.gui.text.no_biome_multi"))
                        .textAlign(Alignment.CENTER)
                        .fullWidth()
                        .marginBottom(2))
            .childIf(
                biomeMultiSync.getFloatValue() == 4f,
                () -> new TextWidget<>(
                    EnumChatFormatting.BLUE + String.format(
                        StatCollector.translateToLocal("GT5U.gui.text.biome_valid"),
                        "4",
                        StatCollector.translateToLocal("GT5U.gui.text.biome_river"))).textAlign(Alignment.CENTER)
                            .fullWidth()
                            .marginBottom(2))
            .child(
                new TextWidget<>(
                    EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("GT5U.gui.text.flow_multi_header"))
                        .color(Color.BLUE.main)
                        .textAlign(Alignment.CENTER)
                        .fullWidth()
                        .marginBottom(1))
            .child(IKey.dynamic(() -> {
                double flow = flowMultiSync.getDoubleValue();
                return String
                    .format(StatCollector.translateToLocal("GT5U.gui.text.flow_multi"), String.format("%.2f", flow));
            })
                .color(Color.BLUE.main)
                .asWidget()
                .fullWidth());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        FloatSyncValue biomeMultiSyncer = new FloatSyncValue(machine::getBiomeMulti);
        DoubleSyncValue flowMultiSyncer = new DoubleSyncValue(machine::getFlowMulti);
        syncManager.syncValue("biomeMulti", biomeMultiSyncer);
        syncManager.syncValue("flowMulti", flowMultiSyncer);
    }
}
