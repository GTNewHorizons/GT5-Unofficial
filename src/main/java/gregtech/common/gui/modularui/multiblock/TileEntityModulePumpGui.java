package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.TileEntityModuleBaseGui;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;

public class TileEntityModulePumpGui extends TileEntityModuleBaseGui<TileEntityModulePump> {

    public TileEntityModulePumpGui(TileEntityModulePump multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> pumpInfo = new ListWidget<>();

        pumpInfo.child(
            IKey.lang("gt.blockmachines.multimachine.ig.elevator.gui.config")
                .asWidget()
                .setEnabledIf(w -> baseMetaTileEntity.isAllowedToWork() || baseMetaTileEntity.isActive())
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE)
                .fullWidth()
                .marginBottom(2));

        for (int i = 0; i < multiblock.getParallelRecipes(); i++) {
            final int index = i;

            pumpInfo.child(IKey.dynamic(() -> {
                String fluidName = multiblock.getPumpedFluid(index);
                if (fluidName != null) {
                    return " - " + fluidName;
                }
                return "";
            })
                .asWidget()
                .setEnabledIf(
                    w -> (baseMetaTileEntity.isAllowedToWork() || baseMetaTileEntity.isActive())
                        && multiblock.getPumpedFluid(index) != null)
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE)
                .fullWidth()
                .marginBottom(2));
        }

        return pumpInfo.children(super.createTerminalTextWidget(syncManager, parent).getChildren());
    }
}
