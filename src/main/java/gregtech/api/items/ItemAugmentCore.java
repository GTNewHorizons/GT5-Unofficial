package gregtech.api.items;

import static gregtech.api.enums.GTValues.VN;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorLoader;

public class ItemAugmentCore extends ItemAugmentAbstract {

    public final Cores coreData;

    public ItemAugmentCore(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, Cores coredata) {
        super(
            aUnlocalized,
            aEnglish,
            aEnglishTooltip,
            MechArmorLoader.AllMechArmor,
            behaviors,
            Collections.emptyList(),
            Collections.emptyList(),
            0);
        this.coreData = coredata;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add(StatCollector.translateToLocalFormatted("GT5U.armor.tooltip.energycore", coreData.tier));
        aList.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.armor.tooltip.chargetier",
                GTValues.TIER_COLORS[coreData.chargeTier] + VN[coreData.chargeTier]));
        aList.add(StatCollector.translateToLocalFormatted("GT5U.armor.tooltip.maxenergy", coreData.charge));
        super.addAdditionalToolTips(aList, aStack, aPlayer);
    }
}
