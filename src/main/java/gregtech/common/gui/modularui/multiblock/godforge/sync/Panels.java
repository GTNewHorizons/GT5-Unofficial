package gregtech.common.gui.modularui.multiblock.godforge.sync;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.multiblock.godforge.MTEBaseModuleGui;
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
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;

public enum Panels {

    // Main panels
    MAIN,
    MAIN_SMELTING(true),
    MAIN_MOLTEN(true),
    MAIN_PLASMA(true),
    MAIN_EXOTIC(true),

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

    private final boolean hasModuleSubpanel;

    Panels() {
        this.panelSupplier = null;
        this.hasModuleSubpanel = false;
    }

    Panels(boolean hasModuleSubpanel) {
        this.panelSupplier = null;
        this.hasModuleSubpanel = hasModuleSubpanel;
    }

    Panels(Function<SyncHypervisor, ModularPanel> panelSupplier) {
        this.panelSupplier = (hypervisor, module) -> panelSupplier.apply(hypervisor);
        this.hasModuleSubpanel = false;
    }

    Panels(BiFunction<SyncHypervisor, Modules<?>, ModularPanel> modulePanelSupplier) {
        this.panelSupplier = modulePanelSupplier;
        this.hasModuleSubpanel = false;
    }

    public String getPanelId(Modules<?> module, int moduleIndex) {
        if (hasModuleSubpanel && moduleIndex >= 0) {
            return module.getModuleId() + "/" + panelId + "." + moduleIndex;
        }

        return module.getModuleId() + "/" + panelId;
    }

    public IPanelHandler getFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        return getFrom(hypervisor.getMainModule(), fromPanel, hypervisor);
    }

    public IPanelHandler getFrom(Modules<?> fromModule, Panels fromPanel, SyncHypervisor hypervisor) {
        if (this == hypervisor.getMainPanel()) {
            throw new IllegalStateException("Cannot get panel handler of main panel!");
        }

        if (hasModuleSubpanel) {
            throw new IllegalStateException("Cannot call on module subpanel, call getModuleSubpanel instead!");
        }

        PanelSyncManager syncManager = hypervisor.getSyncManager(fromModule, fromPanel);

        return syncManager.panel(getPanelId(fromModule, -1), (p_syncManager, syncHandler) -> {
            ModularPanel panel = createPanel(fromModule, hypervisor, -1);
            hypervisor.setModularPanel(fromModule, this, panel);
            hypervisor.setSyncManager(fromModule, this, p_syncManager);

            // noinspection ConstantConditions
            return panelSupplier.apply(hypervisor, fromModule);
        }, true);
    }

    public IPanelHandler getModuleSubpanel(Supplier<MTEBaseModule> module, int moduleIndex, SyncHypervisor hypervisor) {
        if (!hasModuleSubpanel) {
            throw new IllegalStateException("Cannot call on normal subpanel, call getFrom instead!");
        }

        Modules<?> fromModule = hypervisor.getMainModule();
        Panels fromPanel = hypervisor.getMainPanel();
        PanelSyncManager syncManager = hypervisor.getSyncManager(fromModule, fromPanel);

        return syncManager.panel(getPanelId(fromModule, moduleIndex), (p_syncManager, syncHandler) -> {
            MTEBaseModule multiblock = module.get();
            Modules<?> openingModule = Modules.getModule(multiblock);

            ModularPanel panel = createPanel(openingModule, hypervisor, moduleIndex);
            hypervisor.setModularPanel(openingModule, this, panel);
            hypervisor.setSyncManager(openingModule, this, p_syncManager);
            hypervisor.setOpenModuleId(openingModule, moduleIndex);
            panel.onCloseAction(() -> hypervisor.clearOpenModuleId(openingModule));

            MTEBaseModuleGui<?> gui = Modules.createSubpanelGui(multiblock, hypervisor);
            return gui.openSubpanel();
        }, true);
    }

    private ModularPanel createPanel(Modules<?> fromModule, SyncHypervisor hypervisor, int moduleIndex) {
        return new ModularPanel(getPanelId(fromModule, moduleIndex)) {

            @Override
            public void dispose() {
                hypervisor.onPanelDispose(fromModule, Panels.this);
                super.dispose();
            }
        };
    }
}
