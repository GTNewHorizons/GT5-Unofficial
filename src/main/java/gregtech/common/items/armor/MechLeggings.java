package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.FireImmunityBehavior;

public class MechLeggings extends MechArmorBase {

    public MechLeggings() {
        super(REGISTER_LEGS);
        type = "leggings";
        behaviors.add(FireImmunityBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_LEGS;
    }
}
