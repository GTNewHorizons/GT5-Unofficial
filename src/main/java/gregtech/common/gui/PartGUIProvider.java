package gregtech.common.gui;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.gui.GUIHost;
import gregtech.api.gui.GUIProvider;
import gregtech.api.logic.interfaces.FluidInventoryLogicHost;
import gregtech.api.logic.interfaces.ItemInventoryLogicHost;
import gregtech.api.logic.interfaces.PowerLogicHost;

public class PartGUIProvider<T extends GUIHost & ItemInventoryLogicHost & PowerLogicHost & FluidInventoryLogicHost>
    extends GUIProvider<T> {

    public PartGUIProvider(@Nonnull T host) {
        super(host);
    }

    @Override
    protected void attachSynchHandlers(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {}

    @Override
    protected void addWidgets(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext) {}

}
