package gregtech.common.gui.modularui.multiblock.godforge.data;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.multiblock.godforge.panel.BatteryConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.FuelConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.GeneralInfoPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.IndividualMilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.MilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.SpecialThanksPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StarCosmeticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StatisticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.UpgradeTreePanel;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public enum Panels {

    MAIN(null),
    MILESTONE(MilestonePanel::openPanel),
    INDIVIDUAL_MILESTONE(IndividualMilestonePanel::openPanel),
    FUEL_CONFIG(FuelConfigPanel::openPanel),
    BATTERY_CONFIG(BatteryConfigPanel::openPanel),
    STAR_COSMETICS(StarCosmeticsPanel::openPanel),
    UPGRADE_TREE(UpgradeTreePanel::openPanel),
    STATISTICS(StatisticsPanel::openPanel),
    GENERAL_INFO(GeneralInfoPanel::openPanel),
    SPECIAL_THANKS(SpecialThanksPanel::openPanel),

    ;

    private final String panelId = "fog.panel." + name().toLowerCase();
    private final IForgeOfGodsPanel panelSupplier;

    Panels(IForgeOfGodsPanel panelSupplier) {
        this.panelSupplier = panelSupplier;
    }

    public String getPanelId() {
        return panelId;
    }

    public IPanelHandler get(ModularPanel parent, PanelSyncManager syncManager, ForgeOfGodsData data) {
        if (this == MAIN) {
            throw new IllegalStateException("Cannot get panel handler of main panel!");
        }
        return syncManager.panel(getPanelId(), (p_syncManager, syncHandler) -> {
            ModularPanel panel = new ModularPanel(getPanelId());
            return panelSupplier.openPanel(p_syncManager, data, panel, parent);
        }, true);
    }

    @FunctionalInterface
    private interface IForgeOfGodsPanel {

        ModularPanel openPanel(PanelSyncManager syncManager, ForgeOfGodsData data, ModularPanel panel,
            ModularPanel parent);
    }
}
