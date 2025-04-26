package gregtech.api.items.armor;

import java.util.HashMap;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class MechArmorAugmentRegistries {

    public static final HashMap<String, Frames> framesMap = new HashMap<>();
    public static final HashMap<String, Cores> coresMap = new HashMap<>();
    public static final HashMap<String, Augments> augmentsMap = new HashMap<>();

    // Update this integer if you add a frame with more slots in a single category than the previous highest
    public static final int LARGEST_FRAME = 5;

    public enum Frames {

        None(0, 0, 0, 0, null, null, new short[] { 0, 0, 0, 0 }),
        Light(0, 2, 1, 0, "Light", ItemList.Armor_Frame_Light, Materials.Steel.mRGBa),
        Medium(1, 1, 1, 0, "Medium", ItemList.Armor_Frame_Medium, Materials.Titanium.mRGBa),
        Heavy(3, 0, 0, 0, "Heavy", ItemList.Armor_Frame_Heavy, Materials.TungstenSteel.mRGBa),
        Infinity(5, 5, 5, 5, "Infinity", ItemList.Armor_Frame_Infinity, Materials.Infinity.mRGBa);

        public final int protectionSlots;
        public final int movementSlots;
        public final int utilitySlots;
        public final int prismaticSlots;

        public final String id;
        public final ItemList item;

        public final short[] color;

        Frames(int protectionSlots, int movementSlots, int utilitySlots, int prismaticSlots, String id, ItemList item,
            short[] color) {
            this.protectionSlots = protectionSlots;
            this.movementSlots = movementSlots;
            this.utilitySlots = utilitySlots;
            this.prismaticSlots = prismaticSlots;
            this.id = id;
            this.item = item;
            this.color = color;
        }

        public static int getProtectionSlots(Frames frame) {
            return frame.protectionSlots;
        }

        public static int getMovementSlots(Frames frame) {
            return frame.movementSlots;
        }

        public static int getUtilitySlots(Frames frame) {
            return frame.utilitySlots;
        }

        public static int getPrismaticSlots(Frames frame) {
            return frame.prismaticSlots;
        }

        static {
            for (Frames f : Frames.values()) framesMap.put(f.id, f);
        }
    }

    public enum Cores {

        Nano("Nano", 1, ItemList.Armor_Core_T1),
        Quantum("Quantum", 2, ItemList.Armor_Core_T2),
        Living("Living", 3, ItemList.Armor_Core_T3),
        Singularity("Singularity", 4, ItemList.Armor_Core_T4);

        public final String id;
        public final int tier;
        public final ItemList item;

        Cores(String id, int tier, ItemList item) {
            this.id = id;
            this.tier = tier;
            this.item = item;
        }

        static {
            for (Cores c : Cores.values()) coresMap.put(c.id, c);
        }
    }

    public enum Augments {

        NightVision("NightVision", ItemList.Augment_NightVision),
        CreativeFlight("CreativeFlight", ItemList.Augment_CreativeFlight),
        Jetpack("Jetpack", ItemList.Augment_Jetpack),
        JetpackPerfectHover("JetpackPerfectHover", ItemList.Augment_Jetpack_PerfectHover),
        FireImmunity("FireImmunity", ItemList.Augment_FireImmunity),
        StepAssist("StepAssist", ItemList.Augment_StepAssist),
        GogglesOfRevealing("GogglesOfRevealing", ItemList.Augment_GogglesOfRevealing),
        InertiaCanceling("InertiaCanceling", ItemList.Augment_InertiaCanceling),
        Hazmat("Hazmat", ItemList.Augment_Hazmat),
        Apiarist("Apiarist", ItemList.Augment_Apiarist),
        SwimSpeed("SwimSpeed", ItemList.Augment_SwimSpeed),
        KnockbackResistance("KnockbackResistance", ItemList.Augment_KnockbackResistance),
        SpeedBoost("SpeedBoost", ItemList.Augment_SpeedBoost),
        JumpBoost("JumpBoost", ItemList.Augment_JumpBoost),
        FallProtection("FallProtection", ItemList.Augment_FallProtection),
        SpaceSuit("SpaceSuit", ItemList.Augment_SpaceSuit),
        ForceField("ForceField", ItemList.Augment_ForceField),
        OmniMovement("OmniMovement", ItemList.Augment_OmniMovement),
        WaterBreathing("WaterBreathing", ItemList.Augment_WaterBreathing),
        Teleporter("Teleporter", ItemList.Augment_Teleporter);

        public final String id;
        public final ItemList item;

        Augments(String id, ItemList item) {
            this.id = id;
            this.item = item;
        }

        static {
            for (Augments a : Augments.values()) augmentsMap.put(a.id, a);
        }
    }
}
