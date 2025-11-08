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
import gregtech.common.gui.modularui.multiblock.godforge.window.FuelConfigWindow;
import gregtech.common.gui.modularui.multiblock.godforge.window.GeneralInfoWindow;
import gregtech.common.gui.modularui.multiblock.godforge.window.IndividualUpgradeWindow;
import gregtech.common.gui.modularui.multiblock.godforge.window.MilestoneWindow;
import gregtech.common.gui.modularui.multiblock.godforge.window.SpecialThanksWindow;
import gregtech.common.gui.modularui.multiblock.godforge.window.StarCosmeticsWindow;
import gregtech.common.gui.modularui.multiblock.godforge.window.StatisticsWindow;
import gregtech.common.gui.modularui.multiblock.godforge.window.UpgradeTreeWindow;
import tectech.TecTech;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;

public class MTEForgeOfGodsGui extends TTMultiblockBaseGui<MTEForgeOfGods> {

    public static final String SYNC_BATTERY_CHARGING = "fog_batteryCharging";
    public static final String SYNC_SHARD_EJECTION = "fog_shardEjection";

    public MTEForgeOfGodsGui(MTEForgeOfGods multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        // todo try to reduce this, registering these when the panel is opened
        // todo rather than all the time like right now
        FuelConfigWindow.registerSyncValues(syncManager);
        GeneralInfoWindow.registerSyncValues(syncManager);
        IndividualUpgradeWindow.registerSyncValues(syncManager);
        MilestoneWindow.registerSyncValues(syncManager);
        SpecialThanksWindow.registerSyncValues(syncManager);
        StarCosmeticsWindow.registerSyncValues(syncManager);
        StatisticsWindow.registerSyncValues(syncManager);
        UpgradeTreeWindow.registerSyncValues(syncManager);

        syncManager.syncValue(
            SYNC_BATTERY_CHARGING,
            new BooleanSyncValue(multiblock::getBatteryCharging, multiblock::setBatteryCharging));
        syncManager.syncValue(
            SYNC_SHARD_EJECTION,
            new BooleanSyncValue(multiblock::getShardEjection, multiblock::setShardEjection));
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
            .childIf(() -> multiblock.isUpgradeActive(END), createEjectionButton(syncManager));
    }

    @Override
    protected Flow createTerminalCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
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
    // todo move the text methods into this class from the multi
    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return new ListWidget<>().coverChildren()
            .align(Alignment.TopCenter)
            .child(
                IKey.dynamic(multiblock::storedFuelHeaderText)
                    .color(Color.WHITE.main)
                    .alignment(Alignment.CENTER)
                    .asWidget()
                    .marginTop(1))
            .child(
                IKey.dynamic(multiblock::storedFuelText)
                    .color(Color.WHITE.main)
                    .alignment(Alignment.CENTER)
                    .asWidget()
                    .marginTop(2)
                    .alignX(0.5f));
    }

    protected IWidget createMilestoneWindowButton(PanelSyncManager syncManager) {
        IPanelHandler milestonePanel = syncManager
            .panel("fogMilestonePanel", (p_syncManager, syncHandler) -> MilestoneWindow.openPanel(), true);
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
            .panel("fogMilestonePanel", (p_syncManager, syncHandler) -> FuelConfigWindow.openPanel(), true);
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
            .panel("fogBatteryPanel", (p_syncManager, syncHandler) -> MilestoneWindow.openPanel(), true);
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
                    batteryConfigSyncer.setBoolValue(!multiblock.getBatteryCharging());
                } else if (d == 1 && multiblock.isUpgradeActive(REC)) {
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
            .panel("fogCosmeticsPanel", (p_syncManager, syncHandler) -> StarCosmeticsWindow.openPanel(), true);
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
            .panel("fogUpgradeTreePanel", (p_syncManager, syncHandler) -> UpgradeTreeWindow.openPanel(), true);
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
        BooleanSyncValue refreshSyncer = syncManager.findSyncHandler("structureUpdateButton", BooleanSyncValue.class);
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
            .panel("fogStatisticsPanel", (p_syncManager, syncHandler) -> StatisticsWindow.openPanel(), true);
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
                shardEjectionSyncer.setBoolValue(!multiblock.getShardEjection());
                return true;
            })
            .clickSound(() -> TecTech.proxy.playSound(multiblock.getBaseMetaTileEntity(), "fx_click"))
            .tooltip(t -> t.addLine(translateToLocal("fog.button.ejection.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createGeneralInfoWindowButton(PanelSyncManager syncManager) {
        IPanelHandler generalInfoPanel = syncManager.panel(
            "fogGeneralInfoPanel",
            (p_syncManager, syncHandler) -> GeneralInfoWindow.openPanel(multiblock),
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
            .panel("fogSpecialThanksPanel", (p_syncManager, syncHandler) -> SpecialThanksWindow.openPanel(), true);
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
