package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.adapter.WormholeLinkAdapter;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEWormholeGenerator;

public class MTEWormholeGeneratorGui extends MTEMultiBlockBaseGui<MTEWormholeGenerator> {

    public MTEWormholeGeneratorGui(MTEWormholeGenerator multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        GenericSyncValue<MTEWormholeGenerator.WormholeLink, ?> wormholeSyncer = GenericSyncValue
            .builder(MTEWormholeGenerator.WormholeLink.class)
            .getter(multiblock::getLink)
            .adapter(new WormholeLinkAdapter())
            .nullable()
            .build();

        syncManager.syncValue("wormhole", wormholeSyncer);

        Flow textColumn = Flow.col()
            .coverChildren()
            .marginTop(2)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        // status
        textColumn.child(IKey.dynamic(() -> {
            MTEWormholeGenerator.WormholeLink link = wormholeSyncer.getValue();
            if (link == null) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.missing_entangled_singularity");
            }

            if (!link.isFormed()) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.no_destination");
            }

            if (link.mWormholeEnergy > 0 && !link.isActive()) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.decaying");
            }

            if (link.mWormholeEnergy > 0) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.active");
            }

            return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.inactive");
        })
            .asWidget());

        // diameter
        textColumn.child(IKey.dynamic(() -> {
            MTEWormholeGenerator.WormholeLink link = wormholeSyncer.getValue();
            if (link == null) {
                return "";
            }

            // LV power = 1 angstrom in diameter
            double radius = Math.sqrt(link.mWormholeEnergy / 20.0 / 32.0);

            return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.wormhole_generator.diameter",
                formatNumber((long) (radius * 2)));
        })
            .asWidget()
            .setEnabledIf(_ -> {
                MTEWormholeGenerator.WormholeLink link = wormholeSyncer.getValue();
                return link != null && link.mWormholeEnergy > 0;
            }));

        // max i/o
        textColumn.child(IKey.dynamic(() -> {
            MTEWormholeGenerator.WormholeLink link = wormholeSyncer.getValue();
            if (link == null) {
                return "";
            }

            return StatCollector
                .translateToLocalFormatted(
                    "GT5U.gui.text.wormhole_generator.max_io",
                    formatNumber((long) (link.mWormholeEnergy / 20)))
                .replace("\\n", "\n");
        })
            .asWidget());

        return super.createTerminalTextWidget(syncManager, parent).child(textColumn);
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        BooleanSyncValue overclockSyncer = new BooleanSyncValue(
            multiblock::isAllowOverclocks,
            multiblock::setAllowOverclocks).allowC2S();

        return super.createRightPanelGapRow(parent, syncManager).child(
            createDisableableToggleButton(
                overclockSyncer,
                () -> true,
                GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_OFF,
                GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_ON,
                t -> createOverclockTooltip(t, overclockSyncer)));
    }

    private void createOverclockTooltip(RichTooltip t, BooleanSyncValue overclockSyncer) {
        t.add(
            StatCollector.translateToLocal(
                overclockSyncer.getBoolValue() ? "GT5U.chat.worm_hole_generator.overclocks.enable"
                    : "GT5U.chat.worm_hole_generator.overclocks.disable"));
    }
}
