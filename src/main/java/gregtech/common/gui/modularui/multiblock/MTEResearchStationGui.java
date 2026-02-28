package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import tectech.thing.metaTileEntity.multi.MTEResearchStation;

public class MTEResearchStationGui extends MTEMultiBlockBaseGui<MTEResearchStation> {

    public MTEResearchStationGui(MTEResearchStation multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        GenericSyncValue<ItemStack> outputSyncer = GenericSyncValue
            .forItem(() -> multiblock.researchOutputForGUI, val -> multiblock.researchOutputForGUI = val);

        LongSyncValue computationReqSyncer = new LongSyncValue(
            () -> multiblock.computationRequired,
            val -> multiblock.computationRequired = val);
        LongSyncValue computationRemSyncer = new LongSyncValue(
            () -> multiblock.computationRemaining,
            val -> multiblock.computationRemaining = val);
        IntSyncValue ticksUntilPacketLossFailSyncger = new IntSyncValue(
            () -> multiblock.ticksUntilPacketLossFail,
            val -> multiblock.ticksUntilPacketLossFail = val);

        syncManager.syncValue("outputName", outputSyncer);
        syncManager.syncValue("computationRequired", computationReqSyncer);
        syncManager.syncValue("computationRemaining", computationRemSyncer);
        syncManager.syncValue("ticksUntilPacketLossFail", ticksUntilPacketLossFailSyncger);

        ListWidget<IWidget, ?> terminal = super.createTerminalTextWidget(syncManager, parent);
        terminal.child(IKey.dynamic(() -> {
            if (multiblock.researchOutputForGUI == null) return "";
            return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.researching_item",
                multiblock.researchOutputForGUI.getDisplayName());
        })
            .asWidget()
            .setEnabledIf(ignored -> outputSyncer.getValue() != null))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.research_progress",
                        multiblock.getComputationConsumed(),
                        multiblock.getComputationRequired(),
                        formatNumber(getComputationProgress())))
                    .asWidget()
                    .setEnabledIf(
                        ignored -> multiblock.computationRequired > 0 && multiblock.researchOutputForGUI != null))
            .child(IKey.dynamic(() -> {
                if (multiblock.ticksUntilPacketLossFail >= MTEResearchStation.PACKET_LOSS_DECAY_WINDOW) {
                    return EnumChatFormatting.YELLOW
                        + translateToLocalFormatted("tt.infodata.multi.connection_health.waiting")
                        + EnumChatFormatting.RESET;
                }
                return EnumChatFormatting.RED
                    + translateToLocalFormatted("tt.infodata.multi.connection_health.decoherence")
                    + EnumChatFormatting.RESET;

            })
                .asWidget()
                .setEnabledIf(
                    ignored -> multiblock.computationRequired > 0 && multiblock.researchOutputForGUI != null
                        && multiblock.ticksUntilPacketLossFail < MTEResearchStation.PACKET_LOSS_FULL_WINDOW));
        return terminal;
    }

    private double getComputationProgress() {
        return 100d * (multiblock.getComputationRequired() > 0d
            ? (double) multiblock.getComputationConsumed() / multiblock.getComputationRequired()
            : 0d);
    }

}
