package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.CreativeFlightBehavior;
import gregtech.api.items.armor.behaviors.JetpackBehavior;

public class MechChestplate extends MechArmorBase {

    public MechChestplate() {
        super(REGISTER_CHEST);
        type = "chestplate";
        behaviors.add(CreativeFlightBehavior.INSTANCE);
        behaviors.add(JetpackBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_CHEST;
    }
}
