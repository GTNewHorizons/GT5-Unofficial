package gregtech.common.gui.modularui.multiblock.godforge.data;

import java.util.function.Function;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.multiblock.godforge.panel.BatteryConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.FuelConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.GeneralInfoPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.IndividualMilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.IndividualUpgradePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.ManualInsertionPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.MilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.SpecialThanksPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StarCosmeticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StatisticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.UpgradeTreePanel;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;

public enum Panels {

    MAIN(null),
    MILESTONE(MilestonePanel::openPanel),
    INDIVIDUAL_MILESTONE(IndividualMilestonePanel::openPanel),
    FUEL_CONFIG(FuelConfigPanel::openPanel),
    BATTERY_CONFIG(BatteryConfigPanel::openPanel),
    STAR_COSMETICS(StarCosmeticsPanel::openPanel),
    UPGRADE_TREE(UpgradeTreePanel::openPanel),
    INDIVIDUAL_UPGRADE(IndividualUpgradePanel::openPanel),
    MANUAL_INSERTION(ManualInsertionPanel::openPanel),
    STATISTICS(StatisticsPanel::openPanel),
    GENERAL_INFO(GeneralInfoPanel::openPanel),
    SPECIAL_THANKS(SpecialThanksPanel::openPanel),

    ;

    private final String panelId = "fog.panel." + name().toLowerCase();
    private final Function<SyncHypervisor, ModularPanel> panelSupplier;

    Panels(Function<SyncHypervisor, ModularPanel> panelSupplier) {
        this.panelSupplier = panelSupplier;
    }

    public String getPanelId() {
        return panelId;
    }

    public IPanelHandler getFrom(Panels fromPanel, SyncHypervisor hypervisor) {
        if (this == MAIN) {
            throw new IllegalStateException("Cannot get panel handler of main panel!");
        }

        PanelSyncManager syncManager = hypervisor.getSyncManager(fromPanel);

        return syncManager.panel(getPanelId(), (p_syncManager, syncHandler) -> {
            ModularPanel panel = new ModularPanel(getPanelId()) {

                @Override
                public void dispose() {
                    hypervisor.onPanelDispose(Panels.this);
                    super.dispose();
                }
            };
            hypervisor.setModularPanel(this, panel);
            hypervisor.setSyncManager(this, p_syncManager);
            return panelSupplier.apply(hypervisor);
        }, true);
    }
}
