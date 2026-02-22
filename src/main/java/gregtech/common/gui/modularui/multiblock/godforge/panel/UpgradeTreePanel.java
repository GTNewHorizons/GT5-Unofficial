package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.*;

import java.util.Arrays;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widget.ScrollWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.data.UpgradeColor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncActions;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import gregtech.common.gui.modularui.widget.RotatedDrawable;
import tectech.loader.ConfigHandler;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class UpgradeTreePanel {

    private static final int SIZE = 300;
    private static final int OFFSET_SIZE = 292;
    private static final int SCROLL_SIZE = 957;

    private static final int BUTTON_W = 40;
    private static final int BUTTON_H = 15;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.UPGRADE_TREE);

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .padding(4, 0, 4, 0)
            .background(GTGuiTextures.BACKGROUND_STAR)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // Debug widgets
        panel.child(getDebugWidgets(hypervisor));

        // ListWidget doesn't work here due to how the tree is built
        VerticalScrollData scrollData = new VerticalScrollData();
        scrollData.setScrollSize(SCROLL_SIZE);
        ScrollWidget<?> treeList = new ScrollWidget<>(scrollData).size(OFFSET_SIZE);

        // Connector lines
        treeList.child(createConnectorLine(UpgradeColor.BLUE, START, IGCC, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, IGCC, STEM, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, IGCC, CFCE, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, STEM, GISS, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, STEM, FDIM, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, CFCE, FDIM, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, CFCE, SA, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, FDIM, GPCI, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, GPCI, GEM, hypervisor))
            .child(createConnectorLine(UpgradeColor.RED, GISS, REC, hypervisor))
            .child(createConnectorLine(UpgradeColor.RED, GPCI, REC, hypervisor))
            .child(createConnectorLine(UpgradeColor.RED, SA, CTCDD, hypervisor))
            .child(createConnectorLine(UpgradeColor.RED, GPCI, CTCDD, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, REC, QGPIU, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, CTCDD, QGPIU, hypervisor))
            .child(createConnectorLine(UpgradeColor.ORANGE, QGPIU, TCT, hypervisor))
            .child(createConnectorLine(UpgradeColor.ORANGE, TCT, EPEC, hypervisor))
            .child(createConnectorLine(UpgradeColor.ORANGE, EPEC, POS, hypervisor))
            .child(createConnectorLine(UpgradeColor.ORANGE, POS, NGMS, hypervisor))
            .child(createConnectorLine(UpgradeColor.PURPLE, QGPIU, SEFCP, hypervisor))
            .child(createConnectorLine(UpgradeColor.PURPLE, SEFCP, CNTI, hypervisor))
            .child(createConnectorLine(UpgradeColor.PURPLE, CNTI, NDPE, hypervisor))
            .child(createConnectorLine(UpgradeColor.PURPLE, NDPE, NGMS, hypervisor))
            .child(createConnectorLine(UpgradeColor.PURPLE, CNTI, DOP, hypervisor))
            .child(createConnectorLine(UpgradeColor.GREEN, QGPIU, GGEBE, hypervisor))
            .child(createConnectorLine(UpgradeColor.GREEN, GGEBE, IMKG, hypervisor))
            .child(createConnectorLine(UpgradeColor.GREEN, IMKG, DOR, hypervisor))
            .child(createConnectorLine(UpgradeColor.GREEN, DOR, NGMS, hypervisor))
            .child(createConnectorLine(UpgradeColor.GREEN, GGEBE, TPTP, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, NGMS, SEDS, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, SEDS, PA, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, PA, CD, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, CD, TSE, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, TSE, TBF, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, TBF, EE, hypervisor))
            .child(createConnectorLine(UpgradeColor.BLUE, EE, END, hypervisor));

        // Upgrade buttons
        Arrays.stream(ForgeOfGodsUpgrade.VALUES)
            .sequential()
            .map(upgrade -> createUpgradeButton(upgrade, hypervisor))
            .forEach(treeList::child);

        // Secret upgrade
        treeList.child(createSecretUpgrade(hypervisor));

        panel.child(treeList);
        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.UPGRADE_CLICKED.registerFor(Panels.UPGRADE_TREE, hypervisor);
        SyncValues.SECRET_UPGRADE.registerFor(Panels.UPGRADE_TREE, hypervisor);

        SyncActions.RESPEC_UPGRADE.registerFor(Panels.UPGRADE_TREE, hypervisor);
        SyncActions.COMPLETE_UPGRADE.registerFor(Panels.UPGRADE_TREE, hypervisor);
        SyncActions.REFRESH_DYNAMIC.registerFor(Panels.UPGRADE_TREE, hypervisor, hypervisor);
    }

    private static ButtonWidget<?> createUpgradeButton(ForgeOfGodsUpgrade upgrade, SyncHypervisor hypervisor) {
        IPanelHandler individualPanel = Panels.INDIVIDUAL_UPGRADE.getFrom(Panels.UPGRADE_TREE, hypervisor);
        IPanelHandler manualInsertionPanel = Panels.MANUAL_INSERTION.getFrom(Panels.UPGRADE_TREE, hypervisor);

        ButtonWidget<?> widget = new ButtonWidget<>();
        return widget.size(BUTTON_W, BUTTON_H)
            .pos(upgrade.getTreeX(), upgrade.getTreeY())
            // skip background since the connector lines draw over the background layer
            // no matter the order widgets are added to the panel
            .background(IDrawable.EMPTY)
            .overlay(new DynamicDrawable(() -> {
                ForgeOfGodsData data = hypervisor.getData();
                if (data.isUpgradeActive(upgrade)) {
                    return GTGuiTextures.BUTTON_SPACE_PRESSED_32x16;
                }
                return GTGuiTextures.BUTTON_SPACE_32x16;
            }),
                IKey.lang(upgrade.getShortNameKey())
                    .style(EnumChatFormatting.GOLD)
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
                            EnumSyncValue<ForgeOfGodsUpgrade> syncer = SyncValues.UPGRADE_CLICKED
                                .lookupFrom(Panels.UPGRADE_TREE, hypervisor);
                            syncer.setValue(upgrade);
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
                        SyncActions.REFRESH_DYNAMIC
                            .callFrom(Panels.UPGRADE_TREE, hypervisor, Panels.INDIVIDUAL_UPGRADE);
                    }
                } else if (d == 1) {
                    SyncActions.RESPEC_UPGRADE.callFrom(Panels.UPGRADE_TREE, hypervisor, upgrade);
                }
                return true;
            })
            .tooltip(t -> t.addLine(translateToLocal(upgrade.getNameKey())))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    private static Widget<?> createConnectorLine(UpgradeColor color, ForgeOfGodsUpgrade fromUpgrade,
        ForgeOfGodsUpgrade toUpgrade, SyncHypervisor hypervisor) {
        // Calculate the two widget center points
        int fromCenterX = fromUpgrade.getTreeX() + (BUTTON_W / 2);
        int fromCenterY = fromUpgrade.getTreeY() + (BUTTON_H / 2);
        int toCenterX = toUpgrade.getTreeX() + (BUTTON_W / 2);
        int toCenterY = toUpgrade.getTreeY() + (BUTTON_H / 2);

        // Distance formula to determine the height of the line widget
        int width = 6;
        int height = (int) Math.sqrt(Math.pow(toCenterX - fromCenterX, 2) + Math.pow(toCenterY - fromCenterY, 2));

        // Find the angle of the line between the two points
        float rotation = (float) (Math.atan2(toCenterY - fromCenterY, toCenterX - fromCenterX) - (Math.PI / 2));

        // Midpoint formula to find the center point between the two points
        int x = (fromCenterX + toCenterX) / 2;
        int y = (fromCenterY + toCenterY) / 2;

        // Offset to be at the top-left of the widget instead of the center
        x -= width / 2;
        y -= height / 2;

        return new DynamicDrawable(() -> {
            ForgeOfGodsData data = hypervisor.getData();
            UITexture texture = color.getConnector();
            if (data.isUpgradeActive(fromUpgrade) && data.isUpgradeActive(toUpgrade)) {
                texture = color.getOpaqueConnector();
            }
            return new RotatedDrawable(texture).rotationRadian(rotation);
        }).asWidget()
            .pos(x, y)
            .size(width, height);
    }

    private static Flow createSecretUpgrade(SyncHypervisor hypervisor) {
        Flow row = new Row().size(60, 15)
            .pos(START.getTreeX() - 60, START.getTreeY());

        // Upgrade button
        row.child(
            new ButtonWidget<>().size(40, 15)
                .background(new DynamicDrawable(() -> {
                    ForgeOfGodsData data = hypervisor.getData();
                    if (data.isSecretUpgrade()) {
                        return GTGuiTextures.BUTTON_SPACE_PRESSED_32x16;
                    }
                    return IDrawable.EMPTY;
                }))
                .overlay(new DynamicDrawable(() -> {
                    ForgeOfGodsData data = hypervisor.getData();
                    if (data.isSecretUpgrade()) {
                        return IKey.lang("fog.upgrade.tt.short.secret")
                            .style(EnumChatFormatting.GOLD)
                            .scale(0.8f)
                            .alignment(Alignment.CENTER);
                    }
                    return IDrawable.EMPTY;
                }))
                .onMousePressed(d -> {
                    BooleanSyncValue syncer = SyncValues.SECRET_UPGRADE.lookupFrom(Panels.UPGRADE_TREE, hypervisor);
                    syncer.setBoolValue(!syncer.getBoolValue());
                    return true;
                })
                .tooltip(t -> t.addLine(translateToLocal("fog.upgrade.tt.secret")))
                .tooltipShowUpTimer(20)
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound()));

        // Connector line
        row.child(
            GTGuiTextures.PICTURE_UPGRADE_CONNECTOR_BLUE_OPAQUE.asWidget()
                .size(20, 6)
                .alignY(0.5f)
                .setEnabledIf($ -> {
                    ForgeOfGodsData data = hypervisor.getData();
                    return data.isSecretUpgrade();
                }));

        return row;
    }

    private static Flow getDebugWidgets(SyncHypervisor hypervisor) {
        Flow row = new Column().coverChildren()
            .align(Alignment.TopLeft)
            .setEnabledIf($ -> ConfigHandler.debug.DEBUG_MODE);

        // Reset upgrades
        row.child(new ButtonWidget<>().onMousePressed(d -> {
            hypervisor.getData()
                .resetAllUpgrades();
            SyncValues.UPGRADES_LIST.notifyUpdateFrom(Panels.MAIN, hypervisor);
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
                .size(25, 18)
                .tooltip(t -> t.addLine(translateToLocal("fog.debug.gravitonshardsetter.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Unlock all upgrades
        row.child(new ButtonWidget<>().onMousePressed(d -> {
            hypervisor.getData()
                .unlockAllUpgrades();
            SyncValues.UPGRADES_LIST.notifyUpdateFrom(Panels.MAIN, hypervisor);
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
