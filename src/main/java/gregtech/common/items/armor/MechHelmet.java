package gregtech.common.items.armor;

public class MechHelmet extends MechArmorBase {

    public MechHelmet() {
        super(REGISTER_HELMET);
        type = "helmet";
    }

    @Override
    protected int getEquipmentSlot() {
        return SLOT_HELMET;
    }
}
