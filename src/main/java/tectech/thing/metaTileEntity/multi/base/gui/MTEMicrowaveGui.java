package tectech.thing.metaTileEntity.multi.base.gui;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import tectech.thing.metaTileEntity.multi.MTEMicrowave;

public class MTEMicrowaveGui extends TTMultiBlockBaseGui {

    private final MTEMicrowave microwave;

    public MTEMicrowaveGui(MTEMultiBlockBase base) {
        super(base);
        this.microwave = (MTEMicrowave) base;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        IntSyncValue damageFactorSyncer = new IntSyncValue(() -> microwave.maxDamagePerSecond);
        IntSyncValue remainingTimeSyncer = new IntSyncValue(() -> microwave.remainingTime);
        syncManager.syncValue("damageFactor", damageFactorSyncer);
        syncManager.syncValue("remainingTime", remainingTimeSyncer);
        return super.createTerminalTextWidget(syncManager)

            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.WHITE + "Up to "
                        + EnumChatFormatting.RED
                        + damageFactorSyncer.getValue()
                        + EnumChatFormatting.WHITE
                        + " damage per second")
                    .asWidget()
                    .setEnabledIf(
                        widget -> microwave.getErrorDisplayID() == 0 && microwave.getBaseMetaTileEntity()
                            .isActive())
                    .widthRel(1)
                    .marginBottom(2))
            .child(
                IKey.dynamic(
                    () -> EnumChatFormatting.WHITE + "Remaining time: "
                        + EnumChatFormatting.GREEN
                        + remainingTimeSyncer.getValue())
                    .asWidget()
                    .setEnabledIf(
                        widget -> microwave.getErrorDisplayID() == 0 && microwave.getBaseMetaTileEntity()
                            .isActive())
                    .widthRel(1)
                    .marginBottom(2));
    }
}
