package kubatech.gui.modularui2;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import kubatech.api.implementations.KubaTechGTMultiBlockBaseGUI;
import kubatech.tileentity.gregtech.multiblock.MTEHighTempGasCooledReactor;

public class MTEHighTempGasCooledReactorGui extends KubaTechGTMultiBlockBaseGUI<MTEHighTempGasCooledReactor> {

    public MTEHighTempGasCooledReactorGui(MTEHighTempGasCooledReactor multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(
                () -> Arrays.stream(
                    multiblock.getReactorInfoText()
                        .split("\n"))
                    .map(IGregTechDeviceInformation::decode)
                    .collect(Collectors.joining("\n")))
                .asWidget()
                .textAlign(Alignment.CenterLeft)
                .fullWidth()
                .setEnabledIf(w -> multiblock.mMachine));
    }
}
