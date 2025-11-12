package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.Syncers;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.loader.ConfigHandler;

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

    public static void registerSyncValues(SyncHypervisor hypervisor) {
        Syncers.UPGRADES_LIST.registerFor(Panels.UPGRADE_TREE, hypervisor);
        // Syncers.AVAILABLE_GRAVITON_SHARDS.registerFor(Panels.UPGRADE_TREE, hypervisor);
    }

    private static Flow getDebugWidgets(SyncHypervisor hypervisor) {
        GenericListSyncHandler<?> upgradeSyncer = Syncers.UPGRADES_LIST.lookupFrom(Panels.UPGRADE_TREE, hypervisor);

        Flow row = new Column().coverChildren()
            .align(Alignment.TopLeft)
            .marginLeft(4)
            .marginTop(4)
            .setEnabledIf($ -> ConfigHandler.debug.DEBUG_MODE);

        // Reset upgrades
        row.child(new ButtonWidget<>().onMousePressed(d -> {
            hypervisor.getData()
                .resetAllUpgrades();
            // todo sync
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
                .value(Syncers.AVAILABLE_GRAVITON_SHARDS.create(hypervisor))
                .setScrollValues(1, 4, 64)
                .setTooltipOverride(true)
                .size(25, 18)
                .tooltip(t -> t.addLine(translateToLocal("fog.debug.gravitonshardsetter.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Unlock all upgrades
        row.child(new ButtonWidget<>().onMousePressed(d -> {
            hypervisor.getData()
                .unlockAllUpgrades();
            // todo sync
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
