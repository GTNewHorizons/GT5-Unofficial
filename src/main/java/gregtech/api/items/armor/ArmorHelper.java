package gregtech.api.items.armor;

import net.minecraft.item.ItemStack;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ArmorHelper {

    public static final String NIGHT_VISION_KEY = "NightVision";
    public static final String CREATIVE_FLIGHT_KEY = "Flight";
    public static final String INERTIA_CANCELING_KEY = "InertiaCanceling";
    public static final String OMNI_MOVEMENT_KEY = "OmniMovement";
    public static final String JETPACK_KEY = "Jetpack";
    public static final String JETPACK_HOVER_KEY = "JetpackHover";
    public static final String JETPACK_PERFECT_HOVER_KEY = "JetpackPerfectHover";
    public static final String FIRE_IMMUNITY_KEY = "FireImmunity";
    public static final String STEP_ASSIST_KEY = "StepAssist";
    public static final String GOGGLES_OF_REVEALING_KEY = "GogglesOfRevealing";
    public static final String HAZMAT_PROTECTION_KEY = "HazmatProtection";
    public static final String APIARIST_KEY = "Apiarist";
    public static final String SWIM_SPEED_KEY = "SwimSpeed";
    public static final String KNOCKBACK_RESISTANCE_KEY = "KnockbackResistance";
    public static final String SPEED_BOOST_MAX_KEY = "MaxSpeedBoost";
    public static final String SPEED_BOOST_CURRENT_KEY = "CurrentSpeedBoost";
    public static final String JUMP_BOOST_KEY = "JumpBoost";

    public static final String VIS_DISCOUNT_KEY = "VisDiscount";

    /**
     * Try to use EU from an item. Returns whether use was successful.
     * Note: If the item is not an electric item, this will always return true!
     * It assumes that if you add a behavior to a non-electric item, you want that behavior to always succeed.
     */
    public static boolean drainArmor(ItemStack stack, double amount) {
        if (!(stack.getItem() instanceof IElectricItem)) return true;
        return (ElectricItem.manager.discharge(stack, amount, Integer.MAX_VALUE, true, false, false) > 0);
    }

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
