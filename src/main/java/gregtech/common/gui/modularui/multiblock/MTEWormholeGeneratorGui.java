package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEWormholeGenerator;

public class MTEWormholeGeneratorGui extends MTEMultiBlockBaseGui<MTEWormholeGenerator> {

    public MTEWormholeGeneratorGui(MTEWormholeGenerator multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        StringSyncValue statusSyncer = new StringSyncValue(multiblock::getStatus);
        DoubleSyncValue wormholeEnergySyncer = new DoubleSyncValue(multiblock::getWormholeEnergy);
        syncManager.syncValue("status", statusSyncer);
        syncManager.syncValue("wormholeEnergy", wormholeEnergySyncer);

        Flow textColumn = Flow.col()
            .coverChildren()
            .marginTop(2)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        // status
        textColumn.child(
            IKey.dynamic(() -> StatCollector.translateToLocal(statusSyncer.getStringValue()))
                .asWidget());

        // diameter
        textColumn.child(
            IKey.dynamic(
                () -> StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.wormhole_generator.diameter",
                    formatNumber((long) multiblock.getWormholeDiameter(wormholeEnergySyncer.getDoubleValue()))))
                .asWidget()
                .setEnabledIf(_ -> wormholeEnergySyncer.getDoubleValue() > 0));

        // max i/o
        textColumn.child(
            IKey.dynamic(
                () -> StatCollector
                    .translateToLocalFormatted(
                        "GT5U.gui.text.wormhole_generator.max_io",
                        formatNumber(wormholeEnergySyncer.getDoubleValue() / 20))
                    .replace("\\n", "\n"))
                .asWidget()
                .setEnabledIf(_ -> wormholeEnergySyncer.getDoubleValue() > 0));

        return super.createTerminalTextWidget(syncManager, parent).child(textColumn);
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        BooleanSyncValue overclockSyncer = new BooleanSyncValue(
            multiblock::isAllowOverclocks,
            multiblock::setAllowOverclocks).allowC2S();

        return super.createRightPanelGapRow(parent, syncManager).child(
            new ToggleButton().value(overclockSyncer)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_OFF)
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_ON)
                .tooltipBuilder(t -> createOverclockTooltip(t, overclockSyncer))
                .tooltipShowUpTimer(TOOLTIP_DELAY));
    }

    private void createOverclockTooltip(RichTooltip t, BooleanSyncValue overclockSyncer) {
        t.add(
            StatCollector.translateToLocal(
                overclockSyncer.getBoolValue() ? "GT5U.chat.worm_hole_generator.overclocks.enable"
                    : "GT5U.chat.worm_hole_generator.overclocks.disable"));
    }
}
