package gregtech.common.items.armor;

import gregtech.api.items.armor.behaviors.FallProtectionBehavior;
import gregtech.api.items.armor.behaviors.JumpBoostBehavior;
import gregtech.api.items.armor.behaviors.KnockbackResistBehavior;
import gregtech.api.items.armor.behaviors.SpeedBoostBehavior;
import gregtech.api.items.armor.behaviors.StepAssistBehavior;
import gregtech.api.items.armor.behaviors.SwimSpeedBehavior;

public class MechBoots extends MechArmorBase {

    public MechBoots() {
        super(REGISTER_BOOTS);
        type = "boots";
        behaviors.add(StepAssistBehavior.INSTANCE);
        behaviors.add(SwimSpeedBehavior.INSTANCE);
        behaviors.add(KnockbackResistBehavior.INSTANCE);
        behaviors.add(SpeedBoostBehavior.MECH_ARMOR_INSTANCE);
        behaviors.add(JumpBoostBehavior.MECH_ARMOR_INSTANCE);
        behaviors.add(FallProtectionBehavior.INSTANCE);
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_BOOTS;
    }
}
