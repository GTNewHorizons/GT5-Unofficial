package gregtech.api.items.armor.behaviors;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import gregtech.api.items.armor.ArmorHelper;

public class KnockbackResistBehavior implements IArmorBehavior {

    public static final KnockbackResistBehavior INSTANCE = new KnockbackResistBehavior();

    Multimap<String, AttributeModifier> attributes = HashMultimap.create();

    protected KnockbackResistBehavior() {
        attributes.put(
            SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
            new AttributeModifier(UUID.randomUUID(), "KnockbackResistanceBehavior", 1, 0));
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.KNOCKBACK_RESISTANCE_KEY;
    }

    @Override
    public void onArmorEquip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (getOrCreateNbtCompound(stack).getBoolean(ArmorHelper.KNOCKBACK_RESISTANCE_KEY)) {
            player.getAttributeMap()
                .applyAttributeModifiers(attributes);
        }
    }

    @Override
    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (getOrCreateNbtCompound(stack).getBoolean(ArmorHelper.KNOCKBACK_RESISTANCE_KEY)) {
            player.getAttributeMap()
                .removeAttributeModifiers(attributes);
        }
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.KNOCKBACK_RESISTANCE_KEY, true);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.knockbackresistance");
    }
}
