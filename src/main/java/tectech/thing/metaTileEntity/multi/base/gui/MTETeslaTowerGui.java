package tectech.thing.metaTileEntity.multi.base.gui;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import tectech.thing.metaTileEntity.multi.MTETeslaTower;

public class MTETeslaTowerGui extends TTMultiBlockBaseGui {

    private final MTETeslaTower tesla;

    public MTETeslaTowerGui(MTEMultiBlockBase base) {
        super(base);
        this.tesla = (MTETeslaTower) base;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        IntSyncValue outputVoltageSyncer = new IntSyncValue(() -> 0, () -> (int) tesla.outputVoltage);
        IntSyncValue outputCurrentSyncer = new IntSyncValue(() -> 0, () -> (int) tesla.outputCurrent);
        IntSyncValue usedAmpsSyncer = new IntSyncValue(() -> 0, () -> (int) tesla.usedAmps);
        syncManager.syncValue("outputVoltage", outputVoltageSyncer);
        syncManager.syncValue("outputCurrent", outputCurrentSyncer);
        syncManager.syncValue("usedAmps", usedAmpsSyncer);
        return super.createTerminalTextWidget(syncManager)
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.WHITE + "Output Voltage: "
                        + EnumChatFormatting.BLUE
                        + outputVoltageSyncer.getValue())
                    .asWidget()
                    .setEnabledIf(
                        w -> base.getBaseMetaTileEntity()
                            .isActive())
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.WHITE + "Used Amperage: "
                        + EnumChatFormatting.GREEN
                        + usedAmpsSyncer.getValue()
                        + "/"
                        + outputCurrentSyncer.getValue())
                    .asWidget()
                    .setEnabledIf(
                        w -> base.getBaseMetaTileEntity()
                            .isActive())
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2));
    }
}
