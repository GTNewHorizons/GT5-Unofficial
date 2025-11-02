package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static tectech.thing.metaTileEntity.multi.MTEActiveTransformer.formatUIAmperage;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.util.GTTextBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import tectech.thing.metaTileEntity.multi.MTEActiveTransformer;

public class MTEActiveTransformerGui extends TTMultiblockBaseGui<MTEActiveTransformer> {

    public MTEActiveTransformerGui(MTEActiveTransformer multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        DoubleSyncValue transferredLast5Secs = new DoubleSyncValue(
            multiblock::getTransferredLast5Secs,
            multiblock::setTransferredLast5Secs);
        DoubleSyncValue transferredLast30Secs = new DoubleSyncValue(
            multiblock::getTransferredLast30Secs,
            multiblock::setTransferredLast30Secs);
        DoubleSyncValue transferredLast1Min = new DoubleSyncValue(
            multiblock::getTransferredLast1Min,
            multiblock::setTransferredLast1Min);
        syncManager.syncValue("transferredLast5Secs", transferredLast5Secs);
        syncManager.syncValue("transferredLast30Secs", transferredLast30Secs);
        syncManager.syncValue("transferredLast1Min", transferredLast1Min);

        IntSyncValue startupCheckSyncer = new IntSyncValue(multiblock::getmStartUpCheck);
        syncManager.syncValue("startupCheck", startupCheckSyncer);
        IntSyncValue hatchTierSyncer = new IntSyncValue(multiblock::calculateHatchTier);
        syncManager.syncValue("hatchTier", hatchTierSyncer);

        return new ListWidget<>().widthRel(1)
            .child(
                IKey.lang("GT5U.multiblock.startup")
                    .color(Color.WHITE.main)
                    .asWidget()
                    .alignment(Alignment.CenterLeft)
                    .setEnabledIf(w -> startupCheckSyncer.getValue() > 0)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                IKey.lang("GT5U.gui.text.routing")
                    .asWidget()
                    .height(9)
                    .marginBottom(2)
                    .widthRel(1)
                    .setEnabledIf(widget -> multiblock.getErrorDisplayID() == 0 && baseMetaTileEntity.isActive()))
            .child(
                IKey.lang("GT5U.gui.text.at_eu_transferred")
                    .asWidget()
                    .marginBottom(2))
            .child(createThroughputColumn(transferredLast5Secs, hatchTierSyncer, "GT5U.gui.text.at_past_5secs.header"))
            .child(
                createThroughputColumn(transferredLast30Secs, hatchTierSyncer, "GT5U.gui.text.at_past_30secs.header"))
            .child(createThroughputColumn(transferredLast1Min, hatchTierSyncer, "GT5U.gui.text.at_past_min.header"));

    }

    private Flow createThroughputColumn(DoubleSyncValue transferSyncer, IntSyncValue hatchTierSyncer,
        String headerLangKey) {
        return Flow.column()
            .height(18)
            .widthRel(1)
            .marginBottom(2)
            .child(
                IKey.lang(headerLangKey)
                    .asWidget()
                    .anchorLeft(0))
            .child(
                IKey.dynamic(
                    () -> new GTTextBuilder("GT5U.gui.text.at_history.values").setBase(EnumChatFormatting.GRAY)
                        .addNumber(formatUIEUt(transferSyncer.getDoubleValue()))
                        .addNumber(formatUIAmperage(transferSyncer.getDoubleValue() / V[hatchTierSyncer.getValue()]))
                        .add(TIER_COLORS[hatchTierSyncer.getValue()], VN[hatchTierSyncer.getValue()])
                        .toString())
                    .asWidget()
                    .anchorLeft(0));
    }

    private static String formatUIEUt(double eut) {
        if (eut < 1_000_000_000) return GTUtility.formatNumbers(eut);

        int exp = 0;

        while (eut > 1_000) {
            eut /= 1000d;
            exp += 3;
        }

        return GTUtility.formatNumbers(eut) + "e" + exp;
    }
}
