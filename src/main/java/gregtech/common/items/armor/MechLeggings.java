package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.ApiaristBehavior;
import gregtech.api.items.armor.behaviors.FireImmunityBehavior;
import gregtech.api.items.armor.behaviors.KnockbackResistBehavior;

public class MechLeggings extends MechArmorBase {

    public MechLeggings() {
        super(REGISTER_LEGS);
        type = "leggings";
        behaviors.add(FireImmunityBehavior.INSTANCE);
        behaviors.add(ApiaristBehavior.INSTANCE);
        behaviors.add(KnockbackResistBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_LEGS;
    }
}
