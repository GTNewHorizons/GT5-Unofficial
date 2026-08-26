package gregtech.common.gui.modularui.multiblock.godforge.sync;

import java.util.function.Function;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.multiblock.godforge.MTEBaseModuleGui;
import gregtech.common.gui.modularui.multiblock.godforge.data.TriFunction;
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
    private final TriFunction<SyncHypervisor, Modules<?>, Integer, ModularPanel> panelSupplier;

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
        this.panelSupplier = (hypervisor, module, moduleIndex) -> panelSupplier.apply(hypervisor);
        this.hasModuleSubpanel = false;
    }

    Panels(TriFunction<SyncHypervisor, Modules<?>, Integer, ModularPanel> modulePanelSupplier) {
        this.panelSupplier = modulePanelSupplier;
        this.hasModuleSubpanel = false;
    }

    public String getPanelId(Modules<?> module, int moduleIndex) {
        if (moduleIndex >= 0) {
            return module.getModuleId() + "/" + panelId + "." + moduleIndex;
        }

        return module.getModuleId() + "/" + panelId;
    }

    public IPanelHandler getFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        return getFrom(hypervisor.getMainModule(), -1, fromPanel, hypervisor);
    }

    public IPanelHandler getFrom(Modules<?> fromModule, int moduleIndex, Panels fromPanel, SyncHypervisor hypervisor) {
        if (this == hypervisor.getMainPanel()) {
            throw new IllegalStateException("Cannot get panel handler of main panel!");
        }

        if (hasModuleSubpanel) {
            throw new IllegalStateException("Cannot call on module subpanel, call getModuleSubpanel instead!");
        }

        PanelSyncManager syncManager = hypervisor.getSyncManager(fromModule, moduleIndex, fromPanel);

        return syncManager.syncedPanel(getPanelId(fromModule, moduleIndex), true, (p_syncManager, panelHandler) -> {
            ModularPanel panel = createPanel(fromModule, hypervisor, moduleIndex);
            hypervisor.setModularPanel(fromModule, moduleIndex, this, panel);
            hypervisor.setSyncManager(fromModule, moduleIndex, this, p_syncManager);

            return panelSupplier.apply(hypervisor, fromModule, moduleIndex);
        });
    }

    /**
     * Used for opening a panel on the main gorge UI from a button or similar in a module subpanel.
     * Prevents things like allowing multiple info panels being open for panels shared between modules and
     * the main godforge multiblock.
     */
    public IPanelHandler getGlobalFrom(Modules<?> fromModule, int moduleIndex, Panels fromPanel,
        SyncHypervisor hypervisor, boolean isSubpanel) {
        if (isSubpanel && fromModule != Modules.CORE) {
            // Open it from main panel instead
            fromModule = Modules.CORE;
            fromPanel = Panels.MAIN;
            moduleIndex = -1;
        }
        return getFrom(fromModule, moduleIndex, fromPanel, hypervisor);
    }

    public IPanelHandler getModuleSubpanel(Supplier<MTEBaseModule> module, int moduleIndex, SyncHypervisor hypervisor) {
        if (!hasModuleSubpanel) {
            throw new IllegalStateException("Cannot call on normal subpanel, call getFrom instead!");
        }

        Modules<?> fromModule = hypervisor.getMainModule();
        Panels fromPanel = hypervisor.getMainPanel();
        PanelSyncManager syncManager = hypervisor.getSyncManager(fromModule, -1, fromPanel);

        return syncManager.syncedPanel(getPanelId(fromModule, moduleIndex), true, (p_syncManager, panelHandler) -> {
            MTEBaseModule multiblock = module.get();
            Modules<?> openingModule = Modules.getModule(multiblock);

            ModularPanel panel = createPanel(openingModule, hypervisor, moduleIndex);
            hypervisor.setModularPanel(openingModule, moduleIndex, this, panel);
            hypervisor.setSyncManager(openingModule, moduleIndex, this, p_syncManager);

            MTEBaseModuleGui<?> gui = Modules.createSubpanelGui(multiblock, moduleIndex, hypervisor);
            return gui.openSubpanel();
        });
    }

    private ModularPanel createPanel(Modules<?> fromModule, SyncHypervisor hypervisor, int moduleIndex) {
        return new ModularPanel(getPanelId(fromModule, moduleIndex)) {

            @Override
            public void dispose() {
                hypervisor.onPanelDispose(fromModule, moduleIndex, Panels.this);
                super.dispose();
            }
        };
    }
}
