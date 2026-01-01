package gregtech.common.gui.modularui.multiblock.godforge.sync;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.multiblock.godforge.panel.BatteryConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.CustomStarColorPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.ExoticInputsListPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.ExoticPossibleInputsListPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.FuelConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.GeneralInfoPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.IndividualMilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.IndividualUpgradePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.ManualInsertionPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.MilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.PlasmaDebugPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.SpecialThanksPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StarColorImportPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StarCosmeticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StatisticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.UpgradeTreePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.VoltageConfigPanel;

public enum Panels {

    // Main panels
    MAIN,
    MAIN_SMELTING,
    MAIN_MOLTEN,
    MAIN_PLASMA,
    MAIN_EXOTIC,

    // Shared panels
    GENERAL_INFO(GeneralInfoPanel::openModulePanel),
    VOLTAGE_CONFIG(VoltageConfigPanel::openModulePanel),

    // Godforge-specific panels
    MILESTONE(MilestonePanel::openPanel),
    INDIVIDUAL_MILESTONE(IndividualMilestonePanel::openPanel),
    FUEL_CONFIG(FuelConfigPanel::openPanel),
    BATTERY_CONFIG(BatteryConfigPanel::openPanel),
    STAR_COSMETICS(StarCosmeticsPanel::openPanel),
    CUSTOM_STAR_COLOR(CustomStarColorPanel::openPanel),
    STAR_COLOR_IMPORT(StarColorImportPanel::openPanel),
    UPGRADE_TREE(UpgradeTreePanel::openPanel),
    INDIVIDUAL_UPGRADE(IndividualUpgradePanel::openPanel),
    MANUAL_INSERTION(ManualInsertionPanel::openPanel),
    STATISTICS(StatisticsPanel::openPanel),
    SPECIAL_THANKS(SpecialThanksPanel::openPanel),

    // Module-specific panels
    EXOTIC_INPUTS_LIST(ExoticInputsListPanel::openPanel),
    EXOTIC_POSSIBLE_INPUTS_LIST(ExoticPossibleInputsListPanel::openPanel),
    PLASMA_DEBUG(PlasmaDebugPanel::openPanel),

    ;

    public static final Panels[] VALUES = values();

    private final String panelId = "fog.panel." + name().toLowerCase();
    private final BiFunction<SyncHypervisor, Modules<?>, ModularPanel> panelSupplier;

    Panels() {
        this.panelSupplier = null;
    }

    Panels(Function<SyncHypervisor, ModularPanel> panelSupplier) {
        this.panelSupplier = (hypervisor, module) -> panelSupplier.apply(hypervisor);
    }

    Panels(BiFunction<SyncHypervisor, Modules<?>, ModularPanel> modulePanelSupplier) {
        this.panelSupplier = modulePanelSupplier;
    }

    public String getPanelId(Modules<?> module, SyncHypervisor hypervisor) {
        if (module != hypervisor.getMainModule()) {
            return module.getModuleId() + "/" + panelId;
        }
        return panelId;
    }

    public IPanelHandler getFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        return getFrom(hypervisor.getMainModule(), fromPanel, hypervisor);
    }

    public IPanelHandler getFrom(Modules<?> fromModule, Panels fromPanel, SyncHypervisor hypervisor) {
        if (this == hypervisor.getMainPanel()) {
            throw new IllegalStateException("Cannot get panel handler of main panel!");
        }

        PanelSyncManager syncManager = hypervisor.getSyncManager(fromModule, fromPanel);

        return syncManager.panel(getPanelId(fromModule, hypervisor), (p_syncManager, syncHandler) -> {
            ModularPanel panel = createPanel(fromModule, hypervisor);
            hypervisor.setModularPanel(fromModule, this, panel);
            hypervisor.setSyncManager(fromModule, this, p_syncManager);

            // noinspection ConstantConditions
            return panelSupplier.apply(hypervisor, fromModule);
        }, true);
    }

    private ModularPanel createPanel(Modules<?> fromModule, SyncHypervisor hypervisor) {
        return new ModularPanel(getPanelId(fromModule, hypervisor)) {

            @Override
            public void dispose() {
                hypervisor.onPanelDispose(fromModule, Panels.this);
                super.dispose();
            }
        };
    }
}
