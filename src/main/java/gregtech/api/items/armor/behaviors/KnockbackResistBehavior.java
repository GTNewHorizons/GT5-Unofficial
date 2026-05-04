package gregtech.api.items.armor.behaviors;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import gregtech.api.items.armor.ArmorContext;

public class KnockbackResistBehavior implements IArmorBehavior {

    public static final KnockbackResistBehavior INSTANCE = new KnockbackResistBehavior();

    Multimap<String, AttributeModifier> attributes = HashMultimap.create();

    protected KnockbackResistBehavior() {
        attributes.put(
            SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
            new AttributeModifier(UUID.randomUUID(), "KnockbackResistanceBehavior", 1, 0));
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.KnockbackResistance;
    }

    @Override
    public void onArmorEquip(@NotNull ArmorContext context) {
        context.getPlayer()
            .getAttributeMap()
            .applyAttributeModifiers(attributes);
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        context.getPlayer()
            .getAttributeMap()
            .removeAttributeModifiers(attributes);
    }
}
