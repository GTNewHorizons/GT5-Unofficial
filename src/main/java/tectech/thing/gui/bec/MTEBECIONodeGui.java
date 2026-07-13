package tectech.thing.gui.bec;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.WHITE;
import static gregtech.api.util.GTUtility.translate;
import static tectech.thing.metaTileEntity.multi.bec.MTEBECIONode.MAX_PARALLEL_PARAMETER;
import static tectech.thing.metaTileEntity.multi.bec.MTEBECIONode.MIN_PARALLEL_PARAMETER;
import static tectech.thing.metaTileEntity.multi.bec.MTEBECIONode.SPEED_DIVISOR_PARAMETER;

import java.util.Collections;
import java.util.function.Function;

import net.minecraft.util.ResourceLocation;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.CondensateType;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.gui.modularui.adapter.CondensateListAdapter;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.modularui2.sync.NaniteTierSyncValue;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;
import tectech.thing.metaTileEntity.multi.bec.MTEBECIONode;

public class MTEBECIONodeGui extends MTEBECMultiblockBaseGui<MTEBECIONode> {

    public MTEBECIONodeGui(MTEBECIONode multiblock) {
        super(multiblock);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue availableNanitesSyncer = syncManager.findSyncHandler("availableNanites", IntSyncValue.class);
        EnumSyncValue<MTEBECIONode.NodeState, ?> stateSyncer = syncManager
            .findSyncHandler("state", EnumSyncValue.class);
        NaniteTierSyncValue providedTierSyncer = syncManager.findSyncHandler("providedTier", NaniteTierSyncValue.class);
        NaniteTierSyncValue requiredTierSyncer = syncManager.findSyncHandler("requiredTier", NaniteTierSyncValue.class);
        GenericSyncValue<CondensateList, ?> requiredCondensateSyncer = syncManager
            .findSyncHandler("requiredCondensate", GenericSyncValue.class);
        GenericSyncValue<CondensateList, ?> consumedCondensateSyncer = syncManager
            .findSyncHandler("consumedCondensate", GenericSyncValue.class);

        TextWidget<?> contentsWidget = IKey.dynamic(() -> {
            StringBuilder ret = new StringBuilder();

            ret.append(
                translate(
                    "GT5U.gui.text.ionode_status",
                    translate(
                        "GT5U.gui.text.ionode_status." + stateSyncer.getValue()
                            .name())));
            ret.append("\n");

            ret.append(
                translate(
                    "GT5U.gui.text.provided_nanite",
                    providedTierSyncer.getEnumValue() == null ? translate("GT5U.gui.text.nil")
                        : translate(
                            "GT5U.gui.text.nanite_desc",
                            availableNanitesSyncer.getIntValue(),
                            providedTierSyncer.getEnumValue()
                                .describe())));
            ret.append("\n");

            ret.append(
                translate(
                    "GT5U.gui.text.required_nanite",
                    requiredTierSyncer.getEnumValue() == null ? translate("GT5U.gui.text.nil")
                        : GOLD + requiredTierSyncer.getEnumValue()
                            .describe() + WHITE));
            ret.append("\n");

            boolean hasAny = false;

            ret.append(translate("GT5U.gui.text.required_condensate"));
            ret.append("\n");

            if (requiredCondensateSyncer.getValue() != null && consumedCondensateSyncer.getValue() != null
                && multiblock.getMaxProgresstime() > 0) {
                for (var e : requiredCondensateSyncer.getValue()
                    .object2LongEntrySet()) {
                    hasAny = true;

                    long consumed = consumedCondensateSyncer.getValue()
                        .getLong(e.getKey());

                    ret.append(
                        translate(
                            "GT5U.gui.text.remaining_condensate",
                            CondensateType.getCondensateName(e.getKey()),
                            consumed,
                            e.getLongValue()));
                    ret.append("\n");
                }
            }

            if (!hasAny) {
                ret.append(translate("GT5U.gui.text.nil"));
            }

            return ret.toString();
        })
            .asWidget()
            .widthRel(1);

        return super.createTerminalTextWidget(syncManager, parent).child(contentsWidget);
    }

    @Override
    public boolean showMaxParallelRow() {
        // Handled by custom parallel system.
        // It's less confusing to put the existing max parallel option in the parameters window.
        return false;
    }

    @Override
    protected boolean showOutputRates() {
        return false;
    }

    @Override
    protected Function<Parameter<?, ?>, WidgetConfigurator<?>> getParameterWidgetConfigurator() {
        return parameter -> (_, _, widget) -> {
            if (widget instanceof TextFieldWidget textFieldWidget) {
                switch (parameter.getNbtKey()) {
                    case MIN_PARALLEL_PARAMETER -> textFieldWidget.tooltip(
                        t -> t.addStringLines(
                            MarkdownTooltipLoader.STANDARD.loadStandardPath(
                                new ResourceLocation("gregtech", "bec-ionode/min-parallels"),
                                Collections.emptyMap())));
                    case MAX_PARALLEL_PARAMETER -> textFieldWidget.tooltip(
                        t -> t.addStringLines(
                            MarkdownTooltipLoader.STANDARD.loadStandardPath(
                                new ResourceLocation("gregtech", "bec-ionode/max-parallels"),
                                Collections.emptyMap())));
                    case SPEED_DIVISOR_PARAMETER -> textFieldWidget.tooltip(
                        t -> t.addStringLines(
                            MarkdownTooltipLoader.STANDARD.loadStandardPath(
                                new ResourceLocation("gregtech", "bec-ionode/speed-divisor"),
                                Collections.emptyMap())));
                    default -> {}
                }
            }
        };
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        IntSyncValue availableNanitesSyncer = new IntSyncValue(
            multiblock::getAvailableNanites,
            multiblock::setAvailableNanites);
        EnumSyncValue<MTEBECIONode.NodeState, ?> stateSyncer = new EnumSyncValue<>(
            MTEBECIONode.NodeState.class,
            multiblock::getStateEnum,
            multiblock::setState);
        NaniteTierSyncValue providedTierSyncer = new NaniteTierSyncValue(
            multiblock::getProvidedTier,
            multiblock::setProvidedTier);
        NaniteTierSyncValue requiredTierSyncer = new NaniteTierSyncValue(
            multiblock::getRequiredTier,
            multiblock::setRequiredTier);
        GenericSyncValue<CondensateList, ?> requiredCondensateSyncer = GenericSyncValue.builder(CondensateList.class)
            .getter(multiblock::getRequiredCondensateSimple)
            .setter(multiblock::setRequiredCondensate)
            .adapter(new CondensateListAdapter())
            .build();
        GenericSyncValue<CondensateList, ?> consumedCondensateSyncer = GenericSyncValue.builder(CondensateList.class)
            .getter(multiblock::getConsumedCondensateSimple)
            .setter(multiblock::setConsumedCondensate)
            .adapter(new CondensateListAdapter())
            .build();

        syncManager.syncValue("availableNanites", availableNanitesSyncer);
        syncManager.syncValue("state", stateSyncer);
        syncManager.syncValue("providedTier", providedTierSyncer);
        syncManager.syncValue("requiredTier", requiredTierSyncer);
        syncManager.syncValue("requiredCondensate", requiredCondensateSyncer);
        syncManager.syncValue("consumedCondensate", consumedCondensateSyncer);
    }
}
