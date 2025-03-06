package gregtech.api.items.armor;

import gregtech.api.items.armor.behaviors.IArmorBehavior;

public class ArmorHelper {

    public static final String NIGHT_VISION_KEY = "NightVision";



    public enum ArmorType {
        HELMET(1),
        CHESTPLATE(2),
        LEGGINGS(3),
        BOOTS(4);
        public final int id;

        ArmorType(int id) {
            this.id = id;
        }
    }
}
