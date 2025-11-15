package gregtech.common.gui.modularui.multiblock.godforge.panel;

import com.cleanroommc.modularui.widgets.Dialog;

import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;

public class ManualInsertionPanel {

    public static Dialog<?> openDialog(SyncHypervisor hypervisor) {
        Dialog<?> dialog = hypervisor.getDialog(Panels.MANUAL_INSERTION);

        // todo

        return dialog;
    }
}
