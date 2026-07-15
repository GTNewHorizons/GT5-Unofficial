package gregtech.api.items.armor.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.items.armor.ArmorState;

public class SoulboundBehavior implements IArmorBehavior {

    public static final SoulboundBehavior INSTANCE = new SoulboundBehavior();
    private static final short SOULBOUND_ENCHANTMENT_ID = 8;

    @Override
    public BehaviorName getName() {
        return BehaviorName.Soulbound;
    }

    @Override
    public void onAugmentAdded(@NotNull ArmorState state, ItemStack armor, ItemStack augment) {
        if (armor == null) return;
        short level = 1;

        if (augment != null) {
            ItemStackNBT.enchant(armor, SOULBOUND_ENCHANTMENT_ID, level);
        } else {
            if (ItemStackNBT.hasKey(armor, "ench")) {
                NBTTagList enchantList = ItemStackNBT.getTagList(armor, "ench", 10);
                if (enchantList != null) {
                    for (int i = enchantList.tagCount() - 1; i >= 0; i--) {
                        NBTTagCompound enchantment = enchantList.getCompoundTagAt(i);
                        if (enchantment.getShort("id") == SOULBOUND_ENCHANTMENT_ID) {
                            enchantList.removeTag(i);
                        }
                    }
                    if (enchantList.tagCount() == 0) {
                        ItemStackNBT.removeTag(armor, "ench");
                    }
                }
            }
        }
    }

    @Override
    public void onAugmentRemoved(@NotNull ArmorState state, ItemStack armor, @Nullable ItemStack augment) {
        if (!ItemStackNBT.hasKey(armor, "ench")) {
            return;
        }

        NBTTagList enchantList = ItemStackNBT.getTagList(armor, "ench", 10);

        if (enchantList != null) {
            for (int i = enchantList.tagCount() - 1; i >= 0; i--) {
                NBTTagCompound enchantment = enchantList.getCompoundTagAt(i);

                if (enchantment.getShort("id") == SOULBOUND_ENCHANTMENT_ID) {
                    enchantList.removeTag(i);
                }
            }

            if (enchantList.tagCount() == 0) {
                ItemStackNBT.removeTag(armor, "ench");
            }
        }
    }
}
