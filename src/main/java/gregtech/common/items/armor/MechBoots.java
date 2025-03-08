package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.StepAssistBehavior;

public class MechBoots extends MechArmorBase {

    public MechBoots() {
        super(REGISTER_BOOTS);
        type = "boots";
        behaviors.add(StepAssistBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_BOOTS;
    }
}
