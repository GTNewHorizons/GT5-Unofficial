package gregtech.common.items.armor;

public class MechChestplate extends MechArmorBase {

    public MechChestplate() {
        super(REGISTER_CHEST);
        type = "chestplate";
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_CHEST;
    }
}
