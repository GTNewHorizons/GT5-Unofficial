package gregtech.common.gui;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.gui.GUIHost;
import gregtech.api.gui.GUIProvider;
import gregtech.api.logic.MuTEProcessingLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;

public class MachineGUIProvider<G extends GUIHost<G> & ProcessingLogicHost<? extends MuTEProcessingLogic<?>> & PowerLogicHost>
    extends GUIProvider<G> {

    public MachineGUIProvider(G host) {
        super(host);
    }

    @Override
    protected void attachSynchHandlers(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attachSynchHandlers'");
    }

    @Override
    protected void addWidgets(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addWidgets'");
    }

}
