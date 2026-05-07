package gregtech.common.gui.modularui.multiblock;

import java.util.List;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.gui.modularui.multiblock.base.TileEntityModuleBaseGui;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;
import tectech.thing.metaTileEntity.multi.base.parameter.IParametrized;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;

public class TileEntityModulePumpGui extends TileEntityModuleBaseGui<TileEntityModulePump> {

    private static final int PARAMS_PER_RECIPE = 3;

    public TileEntityModulePumpGui(TileEntityModulePump multiblock) {
        super(multiblock);
    }

    @Override
    protected Widget<?> getParameterEditor(ModularPanel panel, PanelSyncManager syncManager) {
        List<Parameter<?>> parameters = ((IParametrized) multiblock).getParameters();
        int totalRecipes = multiblock.getParallelRecipes();

        Flow column = Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START);

        addParameterButton(column, parameters.get(parameters.size() - 1), syncManager);

        for (int i = 0; i < totalRecipes; i++) {
            if (totalRecipes > 1) {
                column.child(
                    IKey.lang("gt.blockmachines.multimachine.project.ig.pump.cfgi.recipe", i + 1)
                        .asWidget()
                        .color(Color.WHITE.main)
                        .marginTop(4)
                        .marginBottom(2));
            }
            for (int j = 0; j < PARAMS_PER_RECIPE; j++) {
                addParameterButton(column, parameters.get(i * PARAMS_PER_RECIPE + j), syncManager);
            }
        }

        if (totalRecipes > 1) {
            int maxHeight = getBasePanelHeight() - 8;
            @SuppressWarnings("unchecked")
            ListWidget<IWidget, ?> scrollable = (ListWidget<IWidget, ?>) new ListWidget<>().size(110, maxHeight)
                .scrollDirection(new VerticalScrollData());
            scrollable.child(column);
            return scrollable;
        }

        return column;
    }

    private void addParameterButton(Flow column, Parameter<?> parameter, PanelSyncManager syncManager) {
        if (!parameter.shouldShowInGui()) return;

        ButtonWidget<?> parameterEditButton = new ButtonWidget<>()
            .overlay(IKey.lang(parameter.getLangKey(), parameter.getLangArgs()))
            .tooltip(t -> t.addLine(IKey.dynamic(() -> String.valueOf(parameter.getValue()))))
            .width(100)
            .marginBottom(2);

        IPanelHandler editParameterPanel = syncManager.syncedPanel(
            "parameterEditPanel_" + parameter.getNbtKey(),
            true,
            (s, h) -> openParameterEditPanel(parameterEditButton, parameter, syncManager));

        column.child(parameterEditButton.onMousePressed(d -> {
            if (!editParameterPanel.isPanelOpen()) {
                editParameterPanel.openPanel();
            } else {
                editParameterPanel.closePanel();
            }
            return true;
        }));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> pumpInfo = new ListWidget<>();

        pumpInfo.child(
            IKey.lang("gt.blockmachines.multimachine.ig.elevator.gui.config")
                .asWidget()
                .setEnabledIf(
                    w -> multiblock.getBaseMetaTileEntity()
                        .isAllowedToWork()
                        || multiblock.getBaseMetaTileEntity()
                            .isActive())
                .color(Color.WHITE.main)
                .widthRel(1)
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
                    w -> (multiblock.getBaseMetaTileEntity()
                        .isAllowedToWork()
                        || multiblock.getBaseMetaTileEntity()
                            .isActive())
                        && multiblock.getPumpedFluid(index) != null)
                .color(Color.WHITE.main)
                .widthRel(1)
                .marginBottom(2));
        }

        return pumpInfo.children(super.createTerminalTextWidget(syncManager, parent).getChildren());
    }
}
