package gregtech.api.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.util.GTUtility;

public class ItemAugmentFrame extends ItemAugmentAbstract {

    public final Frames frame;

    public ItemAugmentFrame(Frames frame) {
        super(frame);
        this.frame = frame;
    }

    @Override
    protected void addAdditionalToolTips(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        super.addAdditionalToolTips(desc, augmentStack, player);
        desc.add(GTUtility.translate("GT5U.armor.tooltip.armorvalue", frame.getProtection()));
        desc.add(GTUtility.translate("GT5U.armor.tooltip.slots"));
        if (frame.getProtectionSlots() > 0) {
            desc.add(frame.getProtectionSlots() + " " + GTUtility.translate("GT5U.armor.tooltip.protection"));
        }
        if (frame.getMovementSlots() > 0) {
            desc.add(frame.getMovementSlots() + " " + GTUtility.translate("GT5U.armor.tooltip.movement"));
        }
        if (frame.getUtilitySlots() > 0) {
            desc.add(frame.getUtilitySlots() + " " + GTUtility.translate("GT5U.armor.tooltip.utility"));
        }
        if (frame.getPrismaticSlots() > 0) {
            desc.add(frame.getPrismaticSlots() + " " + GTUtility.translate("GT5U.armor.tooltip.prismatic"));
        }
    }
}
