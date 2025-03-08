package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.GogglesOfRevealingBehavior;
import gregtech.api.items.armor.behaviors.NightVisionBehavior;

public class MechHelmet extends MechArmorBase {

    public MechHelmet() {
        super(REGISTER_HELMET);
        type = "helmet";
        behaviors.add(NightVisionBehavior.INSTANCE);
        behaviors.add(GogglesOfRevealingBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_HELMET;
    }
}
