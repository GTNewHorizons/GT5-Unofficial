package gregtech.common.gui.modularui.multiblock.godforge;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.*;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.panel.BatteryConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.FuelConfigPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.GeneralInfoPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.IndividualMilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.IndividualUpgradePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.MilestonePanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.SpecialThanksPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StarCosmeticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.StatisticsPanel;
import gregtech.common.gui.modularui.multiblock.godforge.panel.UpgradeTreePanel;
import tectech.TecTech;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class MTEForgeOfGodsGui extends TTMultiblockBaseGui<MTEForgeOfGods> {

    // spotless:off
    public static final String PANEL_MILESTONE = "fog.panel.milestone";
    public static final String PANEL_INDIVIDUAL_MILESTONE = "fog.panel.milestone_individual";
    public static final String PANEL_FUEL_CONFIG = "fog.panel.fuel_config";
    public static final String PANEL_BATTERY_CONFIG = "fog.panel.battery_config";
    public static final String PANEL_STAR_COSMETICS = "fog.panel.star_cosmetics";
    public static final String PANEL_UPGRADE_TREE = "fog.panel.upgrade_tree";
    public static final String PANEL_STATISTICS = "fog.panel.statistics";
    public static final String PANEL_GENERAL_INFO = "fog.panel.general_info";
    public static final String PANEL_SPECIAL_THANKS = "fog.panel.special_thanks";

    public static final String SYNC_BATTERY_CHARGING = "fog.sync.battery_charging";
    public static final String SYNC_SHARD_EJECTION = "fog.sync.shard_ejection";
    public static final String SYNC_INVERSION = "fog.sync.inversion";

    public static final String SYNC_MILESTONE_CLICKED = "fog.sync.milestone_clicked";
    public static final String SYNC_MILESTONE_CHARGE_PROGRESS = "fog.sync.milestone_charge_progress";
    public static final String SYNC_MILESTONE_CHARGE_PROGRESS_INVERTED = "fog.sync.milestone_charge_progress_inverted";
    public static final String SYNC_MILESTONE_CONVERSION_PROGRESS = "fog.sync.milestone_conversion_progress";
    public static final String SYNC_MILESTONE_CONVERSION_PROGRESS_INVERTED = "fog.sync.milestone_conversion_progress_inverted";
    public static final String SYNC_MILESTONE_CATALYST_PROGRESS = "fog.sync.milestone_catalyst_progress";
    public static final String SYNC_MILESTONE_CATALYST_PROGRESS_INVERTED = "fog.sync.milestone_catalyst_progress_inverted";
    public static final String SYNC_MILESTONE_COMPOSITION_PROGRESS = "fog.sync.milestone_composition_progress";
    public static final String SYNC_MILESTONE_COMPOSITION_PROGRESS_INVERTED = "fog.sync.milestone_composition_progress_inverted";

    public static final String SYNC_STRUCTURE_UPDATE = "structureUpdateButton"; // From MTEMultiBlockBase
    // spotless:on

    private final ForgeOfGodsData data;

    public MTEForgeOfGodsGui(MTEForgeOfGods multiblock) {
        super(multiblock);
        this.data = multiblock.getData();
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        // todo try to reduce this, registering these when the panel is opened
        // todo rather than all the time like right now
        BatteryConfigPanel.registerSyncValues(syncManager);
        FuelConfigPanel.registerSyncValues(syncManager);
        IndividualMilestonePanel.registerSyncValues(syncManager);
        IndividualUpgradePanel.registerSyncValues(syncManager);
        StarCosmeticsPanel.registerSyncValues(syncManager);
        StatisticsPanel.registerSyncValues(syncManager);
        UpgradeTreePanel.registerSyncValues(syncManager);

        syncManager
            .syncValue(SYNC_BATTERY_CHARGING, new BooleanSyncValue(data::isBatteryCharging, data::setBatteryCharging));
        syncManager.syncValue(
            SYNC_SHARD_EJECTION,
            new BooleanSyncValue(data::isGravitonShardEjection, data::setGravitonShardEjection));
    }

    // todo fix column being shifted up 1 pixel
    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(MainAxis.END)
            .child(createMilestoneWindowButton(syncManager))
            .child(createFuelConfigWindowButton(syncManager))
            .child(createBatteryConfigWindowButton(syncManager))
            .child(createStarCosmeticsWindowButton(syncManager))
            .child(createUpgradeTreeWindowButton(syncManager));
    }

    // todo fix gap being too tall
    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createModuleRefreshButton(syncManager))
            .child(createStatisticsWindowButton(syncManager))
            .childIf(
                () -> multiblock.getData()
                    .isUpgradeActive(END),
                createEjectionButton(syncManager));
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .rightRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .child(createGeneralInfoWindowButton(syncManager));
    }

    @Override
    protected Flow createTerminalLeftCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .leftRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .child(createSpecialThanksWindowButton(syncManager));
    }

    // todo these probably need to be synced still
    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return new ListWidget<>().coverChildren()
            .align(Alignment.TopCenter)
            .child(IKey.dynamic(() -> {
                if (data.getInternalBattery() == 0) {
                    return translateToLocal("gt.blockmachines.multimachine.FOG.storedstartupfuel");
                }
                return translateToLocal("gt.blockmachines.multimachine.FOG.storedfuel");
            })
                .color(Color.WHITE.main)
                .alignment(Alignment.CENTER)
                .asWidget()
                .marginTop(1))
            .child(IKey.dynamic(() -> {
                if (data.getInternalBattery() == 0) {
                    return data.format(data.getStellarFuelAmount()) + "/" + data.format(data.getNeededStartupFuel());
                }
                return data.format(data.getInternalBattery()) + "/" + data.format(data.getMaxBatteryCharge());
            })
                .color(Color.WHITE.main)
                .alignment(Alignment.CENTER)
                .asWidget()
                .marginTop(2)
                .alignX(0.5f));
    }

    protected IWidget createMilestoneWindowButton(PanelSyncManager syncManager) {
        IPanelHandler milestonePanel = syncManager.panel(
            PANEL_MILESTONE,
            (p_syncManager, syncHandler) -> MilestonePanel.openPanel(p_syncManager, data),
            true);
        return new ButtonWidget<>().size(16)
            .marginBottom(3)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_FLAG)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!milestonePanel.isPanelOpen()) {
                    milestonePanel.openPanel();
                } else {
                    milestonePanel.closePanel();
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.milestones.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createFuelConfigWindowButton(PanelSyncManager syncManager) {
        IPanelHandler fuelConfigPanel = syncManager
            .panel(PANEL_FUEL_CONFIG, (p_syncManager, syncHandler) -> FuelConfigPanel.openPanel(), true);
        return new ButtonWidget<>().size(16)
            .marginBottom(3)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_HEAT_ON)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!fuelConfigPanel.isPanelOpen()) {
                    fuelConfigPanel.openPanel();
                } else {
                    fuelConfigPanel.closePanel();
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.fuelconfig.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createBatteryConfigWindowButton(PanelSyncManager syncManager) {
        IPanelHandler batteryConfigPanel = syncManager
            .panel(PANEL_BATTERY_CONFIG, (p_syncManager, syncHandler) -> BatteryConfigPanel.openPanel(), true);
        BooleanSyncValue batteryConfigSyncer = syncManager
            .findSyncHandler(SYNC_BATTERY_CHARGING, BooleanSyncValue.class);
        return new ButtonWidget<>().size(16)
            .marginBottom(3)
            .overlay(new DynamicDrawable(() -> {
                boolean batteryActive = batteryConfigSyncer.getBoolValue();
                if (batteryActive) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_BATTERY_ON;
                }
                return GTGuiTextures.TT_OVERLAY_BUTTON_BATTERY_OFF;
            }))
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (d == 0) {
                    batteryConfigSyncer.setBoolValue(!data.isBatteryCharging());
                } else if (d == 1 && data.isUpgradeActive(REC)) {
                    if (!batteryConfigPanel.isPanelOpen()) {
                        batteryConfigPanel.openPanel();
                    } else {
                        batteryConfigPanel.closePanel();
                    }
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> {
                t.addLine(translateToLocal("fog.button.battery.tooltip.01"));
                t.addLine(EnumChatFormatting.GRAY + translateToLocal("fog.button.battery.tooltip.02"));
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createStarCosmeticsWindowButton(PanelSyncManager syncManager) {
        IPanelHandler starCosmeticsPanel = syncManager
            .panel(PANEL_STAR_COSMETICS, (p_syncManager, syncHandler) -> StarCosmeticsPanel.openPanel(), true);
        return new ButtonWidget<>().size(16)
            .marginBottom(3)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_RAINBOW_SPIRAL)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!starCosmeticsPanel.isPanelOpen()) {
                    starCosmeticsPanel.openPanel();
                } else {
                    starCosmeticsPanel.closePanel();
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.color.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createUpgradeTreeWindowButton(PanelSyncManager syncManager) {
        IPanelHandler upgradeTreePanel = syncManager
            .panel(PANEL_UPGRADE_TREE, (p_syncManager, syncHandler) -> UpgradeTreePanel.openPanel(), true);
        return new ButtonWidget<>().size(16)
            .marginBottom(3)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_ARROW_BLUE_UP)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!upgradeTreePanel.isPanelOpen()) {
                    upgradeTreePanel.openPanel();
                } else {
                    upgradeTreePanel.closePanel();
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.upgradetree.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createModuleRefreshButton(PanelSyncManager syncManager) {
        BooleanSyncValue refreshSyncer = syncManager.findSyncHandler(SYNC_STRUCTURE_UPDATE, BooleanSyncValue.class);
        return new ButtonWidget<>().size(16)
            .marginRight(3)
            .overlay(GTGuiTextures.TT_OVERLAY_CYCLIC_BLUE)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                refreshSyncer.setBoolValue(!refreshSyncer.getBoolValue());
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.structurecheck.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createStatisticsWindowButton(PanelSyncManager syncManager) {
        IPanelHandler statisticsPanel = syncManager
            .panel(PANEL_STATISTICS, (p_syncManager, syncHandler) -> StatisticsPanel.openPanel(), true);
        return new ButtonWidget<>().size(16)
            .marginRight(3)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_STATISTICS)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!statisticsPanel.isPanelOpen()) {
                    statisticsPanel.openPanel();
                } else {
                    statisticsPanel.closePanel();
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.statistics.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createEjectionButton(PanelSyncManager syncManager) {
        BooleanSyncValue shardEjectionSyncer = syncManager.findSyncHandler(SYNC_SHARD_EJECTION, BooleanSyncValue.class);
        return new ButtonWidget<>().size(16)
            .marginRight(3)
            .overlay(new DynamicDrawable(() -> {
                boolean shardEjection = shardEjectionSyncer.getBoolValue();
                if (shardEjection) {
                    return GTGuiTextures.TT_OVERLAY_EJECTION_ON;
                }
                return GTGuiTextures.TT_OVERLAY_EJECTION_LOCKED;
            }))
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                shardEjectionSyncer.setBoolValue(!data.isGravitonShardEjection());
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.ejection.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createGeneralInfoWindowButton(PanelSyncManager syncManager) {
        IPanelHandler generalInfoPanel = syncManager.panel(
            PANEL_GENERAL_INFO,
            (p_syncManager, syncHandler) -> GeneralInfoPanel.openPanel(p_syncManager, data),
            true);
        return new ButtonWidget<>().size(18)
            .overlay(IDrawable.EMPTY)
            .background(GTGuiTextures.PICTURE_GODFORGE_LOGO)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!generalInfoPanel.isPanelOpen()) {
                    generalInfoPanel.openPanel();
                } else {
                    generalInfoPanel.closePanel();
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.clickhere")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createSpecialThanksWindowButton(PanelSyncManager syncManager) {
        IPanelHandler specialThanksPanel = syncManager
            .panel(PANEL_SPECIAL_THANKS, (p_syncManager, syncHandler) -> SpecialThanksPanel.openPanel(), true);
        return new ButtonWidget<>().size(16)
            .overlay(IDrawable.EMPTY)
            .background(GTGuiTextures.TT_OVERLAY_BUTTON_HEART)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!specialThanksPanel.isPanelOpen()) {
                    specialThanksPanel.openPanel();
                } else {
                    specialThanksPanel.closePanel();
                }
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.thanks.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
