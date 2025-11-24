package gregtech.common.gui.modularui.multiblock.godforge.panel;

import com.cleanroommc.modularui.screen.ModularPanel;

import gregtech.common.gui.modularui.multiblock.godforge.data.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;

public class VoltageConfigPanel {

    private static final int SIZE_W = 138;
    private static final int SIZE_H = 105;

    public static ModularPanel openModulePanel(SyncHypervisor hypervisor, Modules<?> module) {
        ModularPanel panel = hypervisor.getModularPanel(module, Panels.VOLTAGE_CONFIG);

        registerSyncValues(module, hypervisor);

        panel.size(SIZE_W, SIZE_H);

        return panel;
    }

    private static void registerSyncValues(Modules<?> module, SyncHypervisor hypervisor) {

    }
}
