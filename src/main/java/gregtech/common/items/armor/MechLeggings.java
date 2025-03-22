package gregtech.common.items.armor;

public class MechLeggings extends MechArmorBase {

    public MechLeggings() {
        super(REGISTER_LEGS);
        type = "leggings";
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_LEGS;
    }
}
