package gregtech.common.gui;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.gui.GUIHost;
import gregtech.api.interfaces.tileentity.MachineProgress;
import gregtech.api.logic.ComplexParallelProcessingLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;

/**
 * Default GUI class for machines with complex parallel
 */
public class ComplexParallelMachineGUIProvider<T extends GUIHost & ProcessingLogicHost<? extends ComplexParallelProcessingLogic<?>> & PowerLogicHost & MachineProgress>
    extends MachineGUIProvider<T> {

    public ComplexParallelMachineGUIProvider(@Nonnull T host) {
        super(host);
    }

    @Override
    protected void attachSynchHandlers(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {

    }

    @Override
    protected void addWidgets(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {

    }
}
