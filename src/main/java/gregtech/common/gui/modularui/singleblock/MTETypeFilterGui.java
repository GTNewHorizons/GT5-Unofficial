package gregtech.common.gui.modularui.singleblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTESpecialFilterBaseGui;
import gregtech.common.modularui2.factory.SelectItemGuiBuilder;
import gregtech.common.tileentities.automation.MTETypeFilter;

public class MTETypeFilterGui extends MTESpecialFilterBaseGui<MTETypeFilter> {

    private static final String SELECTOR_PANEL_ID = "typeFilterSelect";
    private static final int SELECTOR_VISIBLE_ROWS = 6;

    public MTETypeFilterGui(MTETypeFilter machine) {
        super(machine);
    }

    @Override
    protected List<String> getEmptyFilterSlotTooltip(ModularPanel panel, PanelSyncManager syncManager) {
        // this will never be used since orePrefix will never be null
        return Collections.emptyList();
    }

    @Override
    protected List<String> getFilledFilterSlotTooltip(ModularPanel panel, PanelSyncManager syncManager) {
        OrePrefixes orePrefix = OrePrefixes.getOrePrefix(
            syncManager.findSyncHandler("orePrefix", StringSyncValue.class)
                .getValue());

        List<String> replacementTooltip = new ArrayList<>();
        replacementTooltip.add(
            StatCollector.translateToLocalFormatted("GT5U.tooltip.typefilter.set_to", orePrefix.getDefaultLocalName()));
        replacementTooltip.add(
            StatCollector.translateToLocalFormatted("GT5U.tooltip.typefilter.ore_prefix", "§e" + orePrefix + "§r"));
        replacementTooltip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.tooltip.typefilter.size",
                "§e" + orePrefix.mPrefixedItems.size() + "§r"));
        replacementTooltip.addAll(machine.mTooltipCache.getData("GT5U.type_filter.representation_slot.tooltip").text);
        return replacementTooltip;
    }

    @Override
    protected ItemSlot createFilterSlotBase(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue rotationIndexSyncer = syncManager.findSyncHandler("rotationIndex", IntSyncValue.class);
        StringSyncValue orePrefixSyncer = syncManager.findSyncHandler("orePrefix", StringSyncValue.class);
        IntSyncValue orePrefixIndexSyncer = syncManager.findSyncHandler("orePrefixIndex", IntSyncValue.class);
        IPanelHandler selectorPanel = syncManager.syncedPanel(
            "typeFilterSelectPanel",
            true,
            (_, _) -> buildSelectorPanel(orePrefixIndexSyncer));

        return new PhantomItemSlot() {

            @Override
            public @NotNull Result onMousePressed(int mouseButton) {
                if (Interactable.hasShiftDown()) {
                    // shit + left/right click cycles the prefix, shift + l click with an item copies its prefix
                    MouseData mouseData = MouseData.create(mouseButton);
                    getSyncHandler().syncToServer(PhantomItemSlotSH.SYNC_CLICK, mouseData::writeToPacket);
                } else if (mouseButton == 0 && !selectorPanel.isPanelOpen()) {
                    selectorPanel.openPanel();
                }
                return Result.SUCCESS;
            }

            @Override
            public PhantomItemSlot slot(ModularSlot slot) {
                return syncHandler(new PhantomItemSlotSH(slot) {

                    private void clickTypeIcon(boolean aRightClick, ItemStack aHandStack) {
                        if (baseMetaTileEntity.isServerSide()) {
                            if (aHandStack != null) {
                                copyHeldItemPrefix(aHandStack);
                            } else {
                                cyclePrefix(aRightClick);
                            }
                        }
                    }

                    private void copyHeldItemPrefix(ItemStack handStack) {
                        ItemData data = GTOreDictUnificator.getAssociation(handStack);
                        if (data != null && data.hasValidPrefixData()) {
                            orePrefixSyncer.setValue(data.mPrefix.toString());
                            rotationIndexSyncer.setValue(-1);
                        }
                    }

                    private void cyclePrefix(boolean rightClick) {
                        rotationIndexSyncer.setValue(-1);

                        final int start = IntStream.range(0, OrePrefixes.VALUES.length)
                            .filter(
                                i -> orePrefixSyncer.getValue()
                                    .equals(OrePrefixes.VALUES[i].toString()))
                            .findFirst()
                            .orElse(0);

                        orePrefixSyncer.setValue(
                            IntStream.range(1, OrePrefixes.VALUES.length)
                                // search up/down from start
                                .map(offset -> start + (rightClick ? -offset : offset))
                                // wrap around
                                .map(index -> (index + OrePrefixes.VALUES.length) % OrePrefixes.VALUES.length)
                                // map to prefix
                                .mapToObj(index -> OrePrefixes.VALUES[index])
                                // only prefixes with items
                                .filter(prefix -> !prefix.mPrefixedItems.isEmpty())
                                // map to string
                                .map(OrePrefixes::toString)
                                .findFirst()
                                // fallback to current prefix
                                .orElse(orePrefixSyncer.getValue()));
                    }

                    @Override
                    protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
                        clickTypeIcon(mouseData.mouseButton != 0, cursorStack);
                    }
                });
            }
        };
    }

    @Override
    protected boolean supportsStocking() {
        return false;
    }

    private ModularPanel buildSelectorPanel(IntSyncValue orePrefixIndexSyncer) {
        List<OrePrefixes> prefixes = MTETypeFilter.getSelectablePrefixes();
        List<ItemStack> choices = prefixes.stream()
            .map(prefix -> toDisplayStack(prefix.mPrefixedItems.getFirst()))
            .collect(Collectors.toList());

        ModularPanel selectorPanel = GTGuis.createPopUpPanel(SELECTOR_PANEL_ID);
        return new SelectItemGuiBuilder(selectorPanel, choices).setHeaderItem(machine.getStackForm(1))
            .setTitle(IKey.lang("GT5U.machines.type_filter.select"))
            .setMaxVisibleRows(SELECTOR_VISIBLE_ROWS)
            .setSelectedSyncHandler(orePrefixIndexSyncer)
            .setOnSelectedClientAction((selected, mouseData) -> {
                orePrefixIndexSyncer.setValue(selected);
                if (mouseData.shift) {
                    selectorPanel.closeIfOpen();
                }
            })
            .setChoiceWidgetCustomizer((index, widget) -> widget.tooltipBuilder(tooltip -> {
                OrePrefixes prefix = prefixes.get(index);
                tooltip.addLine(IKey.str(prefix.getDefaultLocalName()))
                    .addLine(IKey.lang("GT5U.tooltip.typefilter.ore_prefix", "§e" + prefix + "§r"))
                    .addLine(IKey.lang("GT5U.tooltip.typefilter.size", "§e" + prefix.mPrefixedItems.size() + "§r"));
            })
                .tooltipShowUpTimer(TOOLTIP_DELAY))
            .build();
    }

    private static ItemStack toDisplayStack(ItemStack stack) {
        // wildcard dmg values cannot be rendered (fixes crash)
        ItemStack displayStack = GTUtility.copyAmount(1, stack);
        if (displayStack != null && displayStack.getItemDamage() == WILDCARD) {
            displayStack.setItemDamage(0);
        }
        return displayStack;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue(
            "rotationIndex",
            new IntSyncValue(machine::getRotationIndex, machine::setRotationIndex).allowC2S());
        syncManager.syncValue("orePrefix", new StringSyncValue(machine::getPrefix, machine::setPrefix).allowC2S());
        syncManager
            .syncValue("orePrefixIndex", new IntSyncValue(machine::getPrefixIndex, machine::setPrefixIndex).allowC2S());
    }
}
