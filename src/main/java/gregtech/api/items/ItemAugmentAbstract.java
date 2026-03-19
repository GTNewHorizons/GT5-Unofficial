package gregtech.api.items;

import static gregtech.api.util.GTUtility.addSeparatorIfNeeded;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext.ArmorContextImpl;
import gregtech.api.items.armor.ArmorHelper;
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
    protected boolean showElectricTier() {
        return false;
    }

    protected boolean showAllInfo() {
        return ArmorHelper.isShiftPressed();
    }

    @Override
    protected void addAdditionalToolTips(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        boolean showAllInfo = showAllInfo();

        ArmorContextImpl context = new ArmorContextImpl(player, augmentStack, null);

        ArmorState.load(context);

        addSeparatorIfNeeded(desc);

        if (showAllInfo && !part.getProvidedBehaviors()
            .isEmpty()) {
            desc.add(EnumChatFormatting.GREEN + GTUtility.translate("GT5U.armor.tooltip.effects"));

            for (IArmorBehavior behavior : part.getProvidedBehaviors()) {
                if (!behavior.hasDisplayName()) continue;

                desc.add(GRAY + "- " + behavior.getDisplayName());
            }
        }

        addSeparatorIfNeeded(desc);

        if (showAllInfo && !part.getRequiredBehaviors()
            .isEmpty()) {
            desc.add(EnumChatFormatting.DARK_AQUA + GTUtility.translate("GT5U.armor.tooltip.requires"));

            for (BehaviorName behavior : part.getRequiredBehaviors()) {
                if (!behavior.hasDisplayName()) continue;

                desc.add(GRAY + "- " + behavior.getDisplayName());
            }
        }

        addSeparatorIfNeeded(desc);

        if (showAllInfo && !part.getIncompatibleBehaviors()
            .isEmpty()) {
            desc.add(EnumChatFormatting.RED + GTUtility.translate("GT5U.armor.tooltip.incompatible"));

            for (BehaviorName behavior : part.getIncompatibleBehaviors()) {
                if (!behavior.hasDisplayName()) continue;

                desc.add(GRAY + "- " + behavior.getDisplayName());
            }
        }

        addSeparatorIfNeeded(desc);

        if (showAllInfo && !part.getProvidedBehaviors()
            .isEmpty()) {
            for (IArmorBehavior behavior : part.getProvidedBehaviors()) {
                behavior.addPartInformation(desc, augmentStack, player);
            }
        }

        addSeparatorIfNeeded(desc);

        super.addAdditionalToolTips(desc, augmentStack, player);

        if (!showAllInfo) {
            desc.add(GRAY + GTUtility.translate("GT5U.armor.tooltip.hold_shift"));
        }

        desc.replaceAll(GTUtility::processFormatStacks);
    }

    public @NotNull IArmorPart getPart() {
        return part;
    }
}
