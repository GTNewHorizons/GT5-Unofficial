package gregtech.api.items;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.items.armor.MechArmorAugmentRegistries;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorLoader;

public class ItemAugmentFrame extends ItemAugmentAbstract {

    public final MechArmorAugmentRegistries.Frames frameData;

    public ItemAugmentFrame(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, MechArmorAugmentRegistries.Frames frameData) {
        super(
            aUnlocalized,
            aEnglish,
            aEnglishTooltip,
            MechArmorLoader.AllMechArmor,
            behaviors,
            Collections.emptyList(),
            Collections.emptyList(),
            0);
        this.frameData = frameData;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        aList.add(StatCollector.translateToLocal("GT5U.armor.tooltip.slots"));
        if (frameData.protectionSlots > 0) aList
            .add(frameData.protectionSlots + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.protection"));
        if (frameData.movementSlots > 0)
            aList.add(frameData.movementSlots + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.movement"));
        if (frameData.utilitySlots > 0)
            aList.add(frameData.utilitySlots + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.utility"));
        if (frameData.prismaticSlots > 0)
            aList.add(frameData.prismaticSlots + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.prismatic"));
    }
}
