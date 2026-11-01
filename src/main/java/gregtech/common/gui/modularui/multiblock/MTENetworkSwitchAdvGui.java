package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.common.gui.modularui.adapter.QuantumDataPacketAdapter;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.thing.metaTileEntity.multi.MTENetworkSwitchAdv;

public class MTENetworkSwitchAdvGui extends TTMultiblockBaseGui<MTENetworkSwitchAdv> {

    public MTENetworkSwitchAdvGui(MTENetworkSwitchAdv multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        @SuppressWarnings("unchecked")
        GenericSyncValue<QuantumDataPacket, ?> pendingPacketSyncer = syncManager
            .findSyncHandler("pendingPacket", GenericSyncValue.class);
        LongSyncValue wastedComputationSyncer = syncManager.findSyncHandler("wastedComputation", LongSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(IKey.dynamic(() -> {
            QuantumDataPacket packet = pendingPacketSyncer.getValue();
            return StatCollector.translateToLocalFormatted(
                "GT5U.machines.computation_hatch.pending_computation",
                formatNumber(packet == null ? 0 : packet.getContent()));
        })
            .color(Color.WHITE.main)
            .asWidget()
            .marginTop(2))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "GT5U.machines.computation_hatch.wasted_computation",
                        formatNumber(wastedComputationSyncer.getLongValue())))
                    .color(Color.WHITE.main)
                    .asWidget());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue(
            "pendingPacket",
            GenericSyncValue.builder(QuantumDataPacket.class)
                .getter(multiblock::getPendingPacket)
                .setter(multiblock::setPendingPacket)
                .adapter(new QuantumDataPacketAdapter())
                .build());
        syncManager.syncValue("wastedComputation", new LongSyncValue(multiblock::getWastedComputation));
    }
}
