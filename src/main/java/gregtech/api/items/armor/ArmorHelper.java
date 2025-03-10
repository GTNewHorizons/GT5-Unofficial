package gregtech.api.items.armor;

public class ArmorHelper {

    public static final String NIGHT_VISION_KEY = "NightVision";
    public static final String CREATIVE_FLIGHT_KEY = "Flight";
    public static final String JETPACK_KEY = "Jetpack";
    public static final String JETPACK_HOVER_KEY = "JetpackHover";
    public static final String JETPACK_PERFECT_HOVER_KEY = "JetpackPerfectHover";
    public static final String FIRE_IMMUNITY_KEY = "FireImmunity";

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
