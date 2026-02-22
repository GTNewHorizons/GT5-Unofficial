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
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.gui.widgets.CommonWidgets;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class MTEForgeOfGodsGui extends TTMultiblockBaseGui<MTEForgeOfGods> {

    private final ForgeOfGodsData data;
    private final SyncHypervisor hypervisor;

    public MTEForgeOfGodsGui(MTEForgeOfGods multiblock) {
        super(multiblock);
        this.data = multiblock.getData();
        this.hypervisor = new SyncHypervisor(Panels.MAIN);

        hypervisor.setMultiblock(multiblock);
    }

    @Override
    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel panel = super.getBasePanel(guiData, syncManager, uiSettings);
        hypervisor.setModularPanel(Panels.MAIN, panel);
        panel.child(
            GTGuiTextures.PICTURE_HEAT_SINK_16x8.asWidget()
                .size(16, 8)
                .align(Alignment.BottomRight)
                .marginRight(8)
                .marginBottom(1));
        return panel;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        hypervisor.setSyncManager(Panels.MAIN, syncManager);

        SyncValues.BATTERY_CHARGING.registerFor(Panels.MAIN, hypervisor);
        SyncValues.INTERNAL_BATTERY.registerFor(Panels.MAIN, hypervisor);
        SyncValues.MAX_BATTERY_CHARGE.registerFor(Panels.MAIN, hypervisor);
        SyncValues.NEEDED_STARTUP_FUEL.registerFor(Panels.MAIN, hypervisor);
        SyncValues.FUEL_AMOUNT.registerFor(Panels.MAIN, hypervisor);
        SyncValues.SHARD_EJECTION.registerFor(Panels.MAIN, hypervisor);
        SyncValues.FORMATTER.registerFor(Panels.MAIN, hypervisor);
        SyncValues.UPGRADES_LIST.registerFor(Panels.MAIN, hypervisor);
        SyncValues.RING_AMOUNT.registerFor(Panels.MAIN, hypervisor);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -3, 1)
            .childPadding(3)
            .mainAxisAlignment(MainAxis.END)
            .child(createMilestonePanelButton())
            .child(createFuelConfigPanelButton())
            .child(createBatteryConfigPanelButton())
            .child(createStarCosmeticsPanelButton())
            .child(createUpgradeTreePanelButton());
    }

    @Override
    protected Flow createPanelGap(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().collapseDisabledChild()
            .widthRel(1)
            .paddingRight(6)
            .paddingLeft(5)
            .childPadding(2)
            .height(getTextBoxToInventoryGap())
            .child(createModuleRefreshButton())
            .child(createStatisticsPanelButton())
            .child(createShardEjectionButton());
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .rightRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .child(createGeneralInfoPanelButton());
    }

    @Override
    protected Flow createTerminalLeftCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .leftRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .child(createSpecialThanksPanelButton());
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return new ListWidget<>().widthRel(1)
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
                .marginTop(1)
                .widthRel(1))
            .child(IKey.dynamic(() -> {
                Formatters formatter = data.getFormatter();
                if (data.getInternalBattery() == 0) {
                    return formatter.format(data.getStellarFuelAmount()) + "/"
                        + formatter.format(data.getNeededStartupFuel());
                }
                return formatter.format(data.getInternalBattery()) + "/" + formatter.format(data.getMaxBatteryCharge());
            })
                .color(Color.WHITE.main)
                .alignment(Alignment.CENTER)
                .asWidget()
                .marginTop(2)
                .alignX(0.5f)
                .widthRel(1));
    }

    @Override
    protected int getTextBoxToInventoryGap() {
        return 20;
    }

    @Override
    protected ToggleButton createMuffleButton() {
        return CommonWidgets.createMuffleButton("mufflerSyncer")
            .size(7)
            .background(IDrawable.EMPTY)
            .overlay(true, GTGuiTextures.GODFORGE_SOUND_OFF)
            .overlay(false, GTGuiTextures.GODFORGE_SOUND_ON)
            .top(8)
            .right(8)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    private IWidget createMilestonePanelButton() {
        IPanelHandler milestonePanel = Panels.MILESTONE.getFrom(Panels.MAIN, hypervisor);
        return new ButtonWidget<>().size(16)
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.milestones.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createFuelConfigPanelButton() {
        IPanelHandler fuelConfigPanel = Panels.FUEL_CONFIG.getFrom(Panels.MAIN, hypervisor);
        return new ButtonWidget<>().size(16)
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.fuelconfig.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createBatteryConfigPanelButton() {
        IPanelHandler batteryConfigPanel = Panels.BATTERY_CONFIG.getFrom(Panels.MAIN, hypervisor);
        BooleanSyncValue batteryChargingSyncer = SyncValues.BATTERY_CHARGING.lookupFrom(Panels.MAIN, hypervisor);

        return new ButtonWidget<>().size(16)
            .overlay(new DynamicDrawable(() -> {
                boolean batteryActive = batteryChargingSyncer.getBoolValue();
                if (batteryActive) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_BATTERY_ON;
                }
                return GTGuiTextures.TT_OVERLAY_BUTTON_BATTERY_OFF;
            }))
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (d == 0) {
                    batteryChargingSyncer.setBoolValue(!data.isBatteryCharging());
                } else if (d == 1 && data.isUpgradeActive(REC)) {
                    if (!batteryConfigPanel.isPanelOpen()) {
                        batteryConfigPanel.openPanel();
                    } else {
                        batteryConfigPanel.closePanel();
                    }
                }
                return true;
            })
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> {
                t.addLine(translateToLocal("fog.button.battery.tooltip.01"));
                t.addLine(EnumChatFormatting.GRAY + translateToLocal("fog.button.battery.tooltip.02"));
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createStarCosmeticsPanelButton() {
        IPanelHandler starCosmeticsPanel = Panels.STAR_COSMETICS.getFrom(Panels.MAIN, hypervisor);
        return new ButtonWidget<>().size(16)
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.color.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createUpgradeTreePanelButton() {
        IPanelHandler upgradeTreePanel = Panels.UPGRADE_TREE.getFrom(Panels.MAIN, hypervisor);
        return new ButtonWidget<>().size(16)
            .marginBottom(2)
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.upgradetree.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createModuleRefreshButton() {
        BooleanSyncValue refreshSyncer = SyncValues.STRUCTURE_UPDATE.lookupFrom(Panels.MAIN, hypervisor);

        return new ButtonWidget<>().size(16)
            .overlay(GTGuiTextures.TT_OVERLAY_CYCLIC_BLUE)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .disableHoverBackground()
            .onMousePressed(d -> {
                refreshSyncer.setBoolValue(!refreshSyncer.getBoolValue());
                return true;
            })
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.structurecheck.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createStatisticsPanelButton() {
        IPanelHandler statisticsPanel = Panels.STATISTICS.getFrom(Panels.MAIN, hypervisor);
        return new ButtonWidget<>().size(16)
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.statistics.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createShardEjectionButton() {
        BooleanSyncValue shardEjectionSyncer = SyncValues.SHARD_EJECTION.lookupFrom(Panels.MAIN, hypervisor);
        return new ButtonWidget<>().size(16)
            .setEnabledIf($ -> data.isUpgradeActive(END))
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.ejection.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createGeneralInfoPanelButton() {
        IPanelHandler generalInfoPanel = Panels.GENERAL_INFO.getFrom(Panels.MAIN, hypervisor);
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.clickhere")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private IWidget createSpecialThanksPanelButton() {
        IPanelHandler specialThanksPanel = Panels.SPECIAL_THANKS.getFrom(Panels.MAIN, hypervisor);
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
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .tooltip(t -> t.addLine(translateToLocal("fog.button.thanks.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
