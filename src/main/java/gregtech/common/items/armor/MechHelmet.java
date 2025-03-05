package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.items.armor.behaviors.NightVisionBehavior;

import java.util.List;

public class MechHelmet extends MechArmorBase {

    public MechHelmet() {
        super(REGISTER_HELMET);
        type = "helmet";
        behaviors.add(NightVisionBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_HELMET;
    }
}
