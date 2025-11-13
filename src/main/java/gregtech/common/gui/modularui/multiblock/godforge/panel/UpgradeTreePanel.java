package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncActions;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.loader.ConfigHandler;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class UpgradeTreePanel {

    private static final int SIZE = 300;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.UPGRADE_TREE);

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_STAR)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // Debug widgets
        panel.child(getDebugWidgets(hypervisor));

        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.UPGRADE_CLICKED.registerFor(Panels.UPGRADE_TREE, hypervisor);
        // SyncValues.AVAILABLE_GRAVITON_SHARDS.registerFor(Panels.UPGRADE_TREE, hypervisor);

        SyncActions.RESPEC_UPGRADE.registerFor(Panels.UPGRADE_TREE, hypervisor);
        SyncActions.COMPLETE_UPGRADE.registerFor(Panels.UPGRADE_TREE, hypervisor);
    }

    private static ButtonWidget<?> createUpgradeButton(ForgeOfGodsUpgrade upgrade, SyncHypervisor hypervisor) {
        IPanelHandler individualPanel = Panels.INDIVIDUAL_UPGRADE.getFrom(Panels.UPGRADE_TREE, hypervisor);
        IPanelHandler manualInsertionPanel = Panels.MANUAL_INSERTION.getFrom(Panels.UPGRADE_TREE, hypervisor);

        ButtonWidget<?> widget = new ButtonWidget<>();
        return widget.size(40, 15)
            .background(new DynamicDrawable(() -> {
                ForgeOfGodsData data = hypervisor.getData();
                if (data.isUpgradeActive(upgrade)) {
                    return GTGuiTextures.BUTTON_SPACE_PRESSED_32x16;
                }
                return GTGuiTextures.BUTTON_SPACE_32x16;
            }))
            .overlay(
                IKey.str(EnumChatFormatting.GOLD + upgrade.getShortNameText())
                    .scale(0.8f)
                    .alignment(Alignment.CENTER))
            .onMousePressed(d -> {
                ForgeOfGodsData data = hypervisor.getData();
                if (d == 0) {
                    if (Interactable.hasShiftDown()) {
                        if (!upgrade.hasExtraCost() || data.getUpgrades()
                            .isCostPaid(upgrade)) {
                            SyncActions.COMPLETE_UPGRADE.callFrom(Panels.UPGRADE_TREE, hypervisor, upgrade);
                        } else {
                            if (!manualInsertionPanel.isPanelOpen()) {
                                manualInsertionPanel.openPanel();
                            }
                            if (individualPanel.isPanelOpen()) {
                                individualPanel.closePanel();
                            }
                            widget.getPanel()
                                .closeIfOpen();
                        }
                    } else {
                        EnumSyncValue<ForgeOfGodsUpgrade> syncer = SyncValues.UPGRADE_CLICKED
                            .lookupFrom(Panels.UPGRADE_TREE, hypervisor);
                        syncer.setValue(upgrade);
                        if (!individualPanel.isPanelOpen()) {
                            individualPanel.openPanel();
                        }
                    }
                } else if (d == 1) {
                    SyncActions.RESPEC_UPGRADE.callFrom(Panels.UPGRADE_TREE, hypervisor, upgrade);
                }
                return true;
            })
            .tooltip(t -> t.addLine(upgrade.getNameText()))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private static Flow getDebugWidgets(SyncHypervisor hypervisor) {
        GenericListSyncHandler<?> upgradeSyncer = SyncValues.UPGRADES_LIST.lookupFrom(Panels.MAIN, hypervisor);

        Flow row = new Column().coverChildren()
            .align(Alignment.TopLeft)
            .marginLeft(4)
            .marginTop(4)
            .setEnabledIf($ -> ConfigHandler.debug.DEBUG_MODE);

        // Reset upgrades
        row.child(new ButtonWidget<>().onMousePressed(d -> {
            hypervisor.getData()
                .resetAllUpgrades();
            upgradeSyncer.notifyUpdate();
            return true;
        })
            .overlay(
                IKey.lang("fog.debug.resetbutton.text")
                    .alignment(Alignment.CENTER)
                    .scale(0.57f))
            .size(40, 15)
            .tooltip(t -> t.addLine(translateToLocal("fog.debug.resetbutton.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Graviton shard amount
        row.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(0, 112)
                .setTextAlignment(Alignment.CENTER)
                .value(SyncValues.AVAILABLE_GRAVITON_SHARDS.create(hypervisor))
                .setScrollValues(1, 4, 64)
                .setTooltipOverride(true)
                .size(25, 18)
                .tooltip(t -> t.addLine(translateToLocal("fog.debug.gravitonshardsetter.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Unlock all upgrades
        row.child(new ButtonWidget<>().onMousePressed(d -> {
            hypervisor.getData()
                .unlockAllUpgrades();
            upgradeSyncer.notifyUpdate();
            return true;
        })
            .overlay(
                IKey.lang("fog.debug.unlockall.text")
                    .alignment(Alignment.CENTER)
                    .scale(0.57f))
            .size(40, 15)
            .tooltip(t -> t.addLine(translateToLocal("fog.debug.unlockall.text")))
            .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }
}
