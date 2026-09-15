package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTECleanroom;

public class MTECleanRoomGui extends MTEMultiBlockBaseGui<MTECleanroom> {

    public MTECleanRoomGui(MTECleanroom multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue efficency = syncManager.findSyncHandler("efficiency", IntSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                    + ": "
                    + formatNumber(efficency.getValue() / 100D)
                    + "%"
            // enabled if active
            )
                .asWidget()
                .setEnabledIf(
                    (w) -> multiblock.getBaseMetaTileEntity()
                        .isActive()));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        IntSyncValue efficencySync = new IntSyncValue(multiblock::getCleanness);
        syncManager.syncValue("efficiency", efficencySync);
    }
}
