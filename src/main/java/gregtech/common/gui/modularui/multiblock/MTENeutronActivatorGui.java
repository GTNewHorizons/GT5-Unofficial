package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import goodgenerator.blocks.tileEntity.MTENeutronActivator;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;

public class MTENeutronActivatorGui extends TTMultiblockBaseGui<MTENeutronActivator> {

    public MTENeutronActivatorGui(MTENeutronActivator multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue eVSyncer = syncManager.findSyncHandler("eV", IntSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(
                () -> StatCollector
                    .translateToLocalFormatted(
                        "gui.NeutronActivator.0",
                        formatNumber(eVSyncer.getDoubleValue() / 1_000_000))
                    .replace("\\n", "\n"))
                .color(Color.WHITE.main)
                .alignment(Alignment.CenterLeft)
                .asWidget()
                .marginTop(2));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("eV", new IntSyncValue(multiblock::geteV));
    }
}
