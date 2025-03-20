package gregtech.common.items.armor;

public class MechBoots extends MechArmorBase {

    public MechBoots() {
        super(REGISTER_BOOTS);
        type = "boots";
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_BOOTS;
    }
}
