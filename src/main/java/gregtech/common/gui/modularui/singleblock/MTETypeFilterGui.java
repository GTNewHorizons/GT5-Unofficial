package gregtech.common.gui.modularui.singleblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

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
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.gui.modularui.singleblock.base.MTESpecialFilterBaseGui;
import gregtech.common.tileentities.automation.MTETypeFilter;

public class MTETypeFilterGui extends MTESpecialFilterBaseGui<MTETypeFilter> {

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

        return new PhantomItemSlot() {

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

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue(
            "rotationIndex",
            new IntSyncValue(machine::getRotationIndex, machine::setRotationIndex).allowC2S());
        syncManager.syncValue("orePrefix", new StringSyncValue(machine::getPrefix, machine::setPrefix).allowC2S());
    }
}
