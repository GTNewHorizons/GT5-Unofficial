package gregtech.api.items;

import static gregtech.api.util.GTUtility.addSeparatorIfNeeded;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.util.GTTextBuilder;

public class ItemAugmentFrame extends ItemAugmentAbstract {

    public final Frames frame;

    public ItemAugmentFrame(Frames frame) {
        super(frame);
        this.frame = frame;
    }

    @Override
    protected boolean showAllInfo() {
        return true;
    }

    @Override
    protected void addAdditionalToolTips(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        super.addAdditionalToolTips(desc, augmentStack, player);

        desc.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.armor.tooltip.armorvalue",
                GTTextBuilder.NUMERIC.toString() + (int) (frame.getProtection() * 100)));

        addSeparatorIfNeeded(desc);

        desc.add(StatCollector.translateToLocal("GT5U.armor.tooltip.slots"));
        if (frame.getProtectionSlots() > 0) {
            desc.add(
                frame.getProtectionSlots() + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.protection"));
        }

        if (frame.getMovementSlots() > 0) {
            desc.add(frame.getMovementSlots() + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.movement"));
        }

        if (frame.getUtilitySlots() > 0) {
            desc.add(frame.getUtilitySlots() + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.utility"));
        }

        if (frame.getPrismaticSlots() > 0) {
            desc.add(frame.getPrismaticSlots() + " " + StatCollector.translateToLocal("GT5U.armor.tooltip.prismatic"));
        }
    }
}
