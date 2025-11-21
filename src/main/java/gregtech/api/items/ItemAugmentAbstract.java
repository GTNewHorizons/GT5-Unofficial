package gregtech.api.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext.ArmorContextImpl;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.items.armor.IArmorPart;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.util.GTUtility;

public abstract class ItemAugmentAbstract extends GTGenericItem {

    @NotNull
    private final IArmorPart part;

    public ItemAugmentAbstract(IArmorPart part) {
        super(part.getItemId());
        this.part = part;
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return part.getLocalizedName();
    }

    @Override
    protected void addAdditionalToolTips(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        ArmorContextImpl context = new ArmorContextImpl(player, augmentStack, null);

        ArmorState.load(context);

        if (!part.getProvidedBehaviors().isEmpty()) {
            desc.add(EnumChatFormatting.GREEN + GTUtility.translate("GT5U.armor.tooltip.effects"));

            for (IArmorBehavior behavior : part.getProvidedBehaviors()) {
                if (!behavior.hasDisplayName()) continue;

                desc.add("-" + behavior.getDisplayName());
            }
        }

        if (!part.getRequiredBehaviors().isEmpty()) {
            desc.add(EnumChatFormatting.AQUA + GTUtility.translate("GT5U.armor.tooltip.requires"));

            for (BehaviorName behavior : part.getRequiredBehaviors()) {
                if (!behavior.hasDisplayName()) continue;

                desc.add("-" + behavior.getDisplayName());
            }
        }

        if (!part.getIncompatibleBehaviors().isEmpty()) {
            desc.add(EnumChatFormatting.RED + GTUtility.translate("GT5U.armor.tooltip.incompatible"));

            for (BehaviorName behavior : part.getIncompatibleBehaviors()) {
                if (!behavior.hasDisplayName()) continue;

                desc.add("-" + behavior.getDisplayName());
            }
        }

        if (!part.getProvidedBehaviors().isEmpty()) {
            for (IArmorBehavior behavior : part.getProvidedBehaviors()) {
                behavior.addPartInformation(desc, augmentStack, player);
            }
        }

        super.addAdditionalToolTips(desc, augmentStack, player);
    }

    public @NotNull IArmorPart getPart() {
        return part;
    }
}
