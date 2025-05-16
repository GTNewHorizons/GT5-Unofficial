package goodgenerator.blocks.tileEntity.gui;

import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.numberFormat;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import goodgenerator.blocks.tileEntity.MTENeutronActivator;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import tectech.thing.metaTileEntity.multi.base.gui.TTMultiBlockBaseGui;

public class MTENeutronActivatorGui extends TTMultiBlockBaseGui {

    private final MTENeutronActivator activator;

    public MTENeutronActivatorGui(MTEMultiBlockBase base) {
        super(base);
        this.activator = (MTENeutronActivator) base;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        IntSyncValue evSyncer = new IntSyncValue(() -> activator.eV, val -> activator.eV = val);
        syncManager.syncValue("ev", evSyncer);
        return super.createTerminalTextWidget(syncManager)
            .child(
                new TextWidget(StatCollector.translateToLocal("gui.NeutronActivator.0")).alignment(Alignment.CenterLeft)
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> activator.getErrorDisplayID() == 0 && (activator.getBaseMetaTileEntity()
                            .isAllowedToWork()
                            || activator.getBaseMetaTileEntity()
                                .isActive())))
            .child(
                IKey.dynamic(() -> numberFormat.format(activator.eV / 1_000_000d) + " MeV")
                    .asWidget()
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(
                        w -> activator.getErrorDisplayID() == 0 && (activator.getBaseMetaTileEntity()
                            .isAllowedToWork()
                            || activator.getBaseMetaTileEntity()
                                .isActive())));
    }
}
