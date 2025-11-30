package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncActions;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.UpgradeStorage;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class IndividualUpgradePanel {

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.INDIVIDUAL_UPGRADE);

        registerSyncValues(hypervisor);

        // registered on the Upgrade Tree panel, look up from there
        EnumSyncValue<ForgeOfGodsUpgrade> upgradeSyncer = SyncValues.UPGRADE_CLICKED
            .lookupFrom(Panels.UPGRADE_TREE, hypervisor);

        // Do this here since doing it inside the dynamic sync handler crashes on panel close.
        // I need to get this "from the upgrade tree panel" because of MUI being weird and dumb and
        // getting mad at me about sync handler mismatch
        IPanelHandler manualInsertionPanel = Panels.MANUAL_INSERTION.getFrom(Panels.UPGRADE_TREE, hypervisor);

        DynamicSyncHandler handler = new DynamicSyncHandler().widgetProvider(($, $$) -> {
            ForgeOfGodsUpgrade upgrade = upgradeSyncer.getValue();
            panel.size(upgrade.getPanelSize());
            panel.background(upgrade.getBackground());
            panel.disableHoverBackground();
            return buildPanel(upgrade.getPanelSize(), upgrade, hypervisor, manualInsertionPanel);
        });

        upgradeSyncer.setChangeListener(() -> {
            if (handler.isValid()) {
                handler.notifyUpdate($ -> {});
            }
        });

        panel.child(
            new DynamicSyncedWidget<>().coverChildren()
                .syncHandler(handler));

        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.AVAILABLE_GRAVITON_SHARDS.registerFor(Panels.INDIVIDUAL_UPGRADE, hypervisor);

        SyncActions.COMPLETE_UPGRADE.registerFor(Panels.INDIVIDUAL_UPGRADE, hypervisor);
        SyncActions.RESPEC_UPGRADE.registerFor(Panels.INDIVIDUAL_UPGRADE, hypervisor);
    }

    private static ParentWidget<?> buildPanel(int size, ForgeOfGodsUpgrade upgrade, SyncHypervisor hypervisor,
        IPanelHandler manualInsertionPanel) {
        ParentWidget<?> parent = new ParentWidget<>().size(size)
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // Background symbol
        parent.child(
            upgrade.getSymbol()
                .asWidget()
                .size((int) (size / 2.0f * upgrade.getSymbolWidthRatio()), size / 2)
                .align(Alignment.CENTER));

        // Background overlay
        parent.child(
            upgrade.getOverlay()
                .asWidget()
                .size(size / 2)
                .align(Alignment.CENTER));

        Flow column = new Column().size(size - 16, size - 26) // 16 top, 10 bottom
            .marginTop(15)
            .alignX(0.5f);

        // Title
        column.child(
            IKey.lang(upgrade.getNameKey())
                .style(EnumChatFormatting.GOLD)
                .alignment(Alignment.CENTER)
                .asWidget()
                .alignX(0.5f)
                .widthRel(1));

        // Body text
        column.child(
            IKey.lang(upgrade.getBodyKey())
                .style(EnumChatFormatting.WHITE)
                .alignment(Alignment.CENTER)
                .asWidget()
                .alignX(0.5f)
                .widthRel(1)
                .height(upgrade.getBodySize())
                .marginTop(7));

        // Lore text
        column.child(
            IKey.lang(upgrade.getLoreKey())
                .style(EnumChatFormatting.ITALIC)
                .color(0xFFBBBDBD)
                .alignment(Alignment.CENTER)
                .asWidget()
                .alignX(0.5f)
                .widthRel(1)
                .height(upgrade.getLoreSize())
                .marginTop(5));

        // Bottom row widgets
        ParentWidget<?> bottomRow = new ParentWidget<>().widthRel(1)
            .height(15)
            .align(Alignment.BottomCenter);

        // Shard cost
        bottomRow.child(IKey.dynamic(() -> {
            String cost = " " + EnumChatFormatting.BLUE + upgrade.getShardCost();
            return translateToLocal("gt.blockmachines.multimachine.FOG.shardcost") + cost;
        })
            .alignment(Alignment.CENTER)
            .scale(0.7f)
            .color(0xFF9C9C9C)
            .asWidget()
            .size(70, 15)
            .alignX(0));

        // Available shards
        bottomRow.child(IKey.dynamic(() -> {
            int shardsAvailable = hypervisor.getData()
                .getGravitonShardsAvailable();
            Formatters formatter = hypervisor.getData()
                .getFormatter();

            EnumChatFormatting enoughShards = EnumChatFormatting.RED;
            if (shardsAvailable >= upgrade.getShardCost()) {
                enoughShards = EnumChatFormatting.GREEN;
            }

            String available = " " + enoughShards + formatter.format(shardsAvailable);
            return translateToLocal("gt.blockmachines.multimachine.FOG.availableshards") + available;
        })
            .alignment(Alignment.CENTER)
            .scale(0.7f)
            .color(0xFF9C9C9C)
            .asWidget()
            .size(70, 15)
            .alignX(1));

        Flow buttonRow = new Row().size(78, 15)
            .alignX(0.5f);

        // Complete/Respec button
        buttonRow.child(
            new ButtonWidget<>().size(40, 15)
                .background(new DynamicDrawable(() -> {
                    ForgeOfGodsData data = hypervisor.getData();
                    if (data.isUpgradeActive(upgrade)) {
                        return GTGuiTextures.BUTTON_OUTLINE_HOLLOW_PRESSED;
                    }
                    return GTGuiTextures.BUTTON_OUTLINE_HOLLOW;
                }))
                .overlay(new DynamicDrawable(() -> {
                    ForgeOfGodsData data = hypervisor.getData();
                    if (data.isUpgradeActive(upgrade)) {
                        return IKey.lang("fog.upgrade.respec")
                            .alignment(Alignment.CENTER)
                            .scale(0.7f);
                    }
                    return IKey.lang("fog.upgrade.confirm")
                        .alignment(Alignment.CENTER)
                        .scale(0.7f);
                }))
                .onMousePressed(d -> {
                    ForgeOfGodsData data = hypervisor.getData();
                    if (data.isUpgradeActive(upgrade)) {
                        SyncActions.RESPEC_UPGRADE.callFrom(Panels.INDIVIDUAL_UPGRADE, hypervisor, upgrade);
                    } else {
                        SyncActions.COMPLETE_UPGRADE.callFrom(Panels.INDIVIDUAL_UPGRADE, hypervisor, upgrade);
                    }
                    return true;
                })
                .tooltipDynamic(t -> {
                    ForgeOfGodsData data = hypervisor.getData();
                    if (data.isUpgradeActive(upgrade)) {
                        t.addLine(translateToLocal("fog.upgrade.respec"));
                    } else {
                        t.addLine(translateToLocal("fog.upgrade.confirm"));
                    }
                })
                .tooltipAutoUpdate(true)
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .alignX(0.5f));

        // Extra cost button
        buttonRow.child(
            new ButtonWidget<>().size(15)
                .background(new DynamicDrawable(() -> {
                    UpgradeStorage storage = hypervisor.getData()
                        .getUpgrades();
                    if (storage.isCostPaid(upgrade)) {
                        return GTGuiTextures.BUTTON_BOXED_CHECKMARK_18x18;
                    }
                    return GTGuiTextures.BUTTON_BOXED_EXCLAMATION_POINT_18x18;
                }).asIcon()
                    .size(15)) // for some reason this is needed
                .onMousePressed(d -> {
                    ModularPanel upgradeTreePanel = hypervisor.getModularPanel(Panels.UPGRADE_TREE);
                    if (upgradeTreePanel != null) {
                        upgradeTreePanel.closeIfOpen();
                    }
                    parent.getPanel()
                        .closeIfOpen();
                    if (!manualInsertionPanel.isPanelOpen()) {
                        manualInsertionPanel.openPanel();
                    }
                    return true;
                })
                .tooltipDynamic(t -> {
                    UpgradeStorage storage = hypervisor.getData()
                        .getUpgrades();
                    if (storage.isCostPaid(upgrade)) {
                        t.addLine(translateToLocal("fog.button.materialrequirementsmet.tooltip"));
                    } else {
                        t.addLine(translateToLocal("fog.button.materialrequirements.tooltip"));
                    }
                    t.addLine(
                        EnumChatFormatting.GRAY
                            + translateToLocal("fog.button.materialrequirements.tooltip.clickhere"));
                })
                .tooltipAutoUpdate(true)
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .setEnabledIf($ -> upgrade.hasExtraCost())
                .alignX(0));

        bottomRow.child(buttonRow);
        column.child(bottomRow);
        parent.child(column);
        return parent;
    }
}
