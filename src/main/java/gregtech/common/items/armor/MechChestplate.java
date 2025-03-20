package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.CreativeFlightBehavior;
import gregtech.api.items.armor.behaviors.InertiaCancelingBehavior;
import gregtech.api.items.armor.behaviors.JetpackBehavior;
import gregtech.api.items.armor.behaviors.JetpackPerfectHoverBehavior;

public class MechChestplate extends MechArmorBase {

    public MechChestplate() {
        super(REGISTER_CHEST);
        type = "chestplate";
        behaviors.add(CreativeFlightBehavior.INSTANCE);
        behaviors.add(InertiaCancelingBehavior.INSTANCE);
        behaviors.add(JetpackBehavior.INSTANCE);
        behaviors.add(JetpackPerfectHoverBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_CHEST;
    }
}
