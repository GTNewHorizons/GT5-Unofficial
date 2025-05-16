package tectech.thing.metaTileEntity.multi.base.gui;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import tectech.thing.metaTileEntity.multi.MTEQuantumComputer;

public class MTEQuantumComputerGui extends TTMultiBlockBaseGui {

    private final MTEQuantumComputer quantumComputer;

    public MTEQuantumComputerGui(MTEMultiBlockBase base) {
        super(base);
        this.quantumComputer = (MTEQuantumComputer) base;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        DoubleSyncValue tempSyncer = new DoubleSyncValue(() -> quantumComputer.currentTemp);
        LongSyncValue computationSyncer = new LongSyncValue(() -> quantumComputer.computation);
        syncManager.syncValue("temp", tempSyncer);
        syncManager.syncValue("computation", computationSyncer);

        return super.createTerminalTextWidget(syncManager)

            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.WHITE + "Current temperature: "
                        + EnumChatFormatting.YELLOW
                        + tempSyncer.getValue())
                    .asWidget()
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> base.getErrorDisplayID() == 0 && base.getBaseMetaTileEntity()
                            .isActive()))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.WHITE + "Computation/s: "
                        + EnumChatFormatting.GREEN
                        + computationSyncer.getValue())
                    .asWidget()
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> base.getErrorDisplayID() == 0 && base.getBaseMetaTileEntity()
                            .isActive()));
    }
}
