package gregtech.api.items.armor;

import java.util.HashMap;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;

public class MechArmorAugmentRegistries {

    public static final HashMap<String, Frames> framesMap = new HashMap<>();
    public static final HashMap<String, Cores> coresMap = new HashMap<>();
    public static final HashMap<String, Augments> augmentsMap = new HashMap<>();

    // Update this integer if you add a frame with more slots in a single category than the previous highest
    public static final int LARGEST_FRAME = 5;

    public enum Frames {

        None(0, 0, 0, 0, null, null, new short[] { 0, 0, 0, 0 }, 0),

        Light(1, 2, 0, 0, "Light", ItemList.Armor_Frame_Light, Materials.Iron.mRGBa, 8),
        Medium(1, 1, 1, 0, "Medium", ItemList.Armor_Frame_Medium, Materials.Bronze.mRGBa, 11),
        Heavy(2, 0, 1, 0, "Heavy", ItemList.Armor_Frame_Heavy, Materials.Steel.mRGBa, 15),

        Nimble(1, 3, 1, 0, "Nimble", ItemList.Armor_Frame_Nimble, Materials.VibrantAlloy.mRGBa, 10),
        Adaptive(2, 1, 1, 1, "Adaptive", ItemList.Armor_Frame_Adaptive, Materials.Titanium.mRGBa, 13),
        Tough(3, 0, 2, 0, "Tough", ItemList.Armor_Frame_Tough, Materials.TungstenSteel.mRGBa, 17),

        Lightning(1, 4, 1, 1, "Lightning", ItemList.Armor_Frame_Lightning, Materials.Electrum.mRGBa, 15),
        Morphic(2, 1, 2, 2, "Morphic", ItemList.Armor_Frame_Morphic, Materials.RedstoneAlloy.mRGBa, 17),
        Bulwark(4, 1, 1, 1, "Bulwark", ItemList.Armor_Frame_Bulwark, Materials.RedSteel.mRGBa, 20),

        Infinity(5, 5, 5, 5, "Infinity", ItemList.Armor_Frame_Infinity, Materials.Infinity.mRGBa, 30);

        public final int protectionSlots;
        public final int movementSlots;
        public final int utilitySlots;
        public final int prismaticSlots;

        public final String id;
        public final ItemList item;

        public final short[] color;

        public final int protection;

        Frames(int protectionSlots, int movementSlots, int utilitySlots, int prismaticSlots, String id, ItemList item,
            short[] color, int protection) {
            this.protectionSlots = protectionSlots;
            this.movementSlots = movementSlots;
            this.utilitySlots = utilitySlots;
            this.prismaticSlots = prismaticSlots;
            this.id = id;
            this.item = item;
            this.color = color;
            this.protection = protection;
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

        Nano("Nano", 1, ItemList.Armor_Core_T1, 1_000_000, VoltageIndex.HV),
        Quantum("Quantum", 2, ItemList.Armor_Core_T2, 10_000_000, VoltageIndex.IV),
        Living("Living", 3, ItemList.Armor_Core_T3, 100_000_000, VoltageIndex.ZPM),
        Singularity("Singularity", 4, ItemList.Armor_Core_T4, 0, VoltageIndex.UHV);

        public final String id;
        public final int tier;
        public final ItemList item;
        public final int chargeMax;
        public final int chargeTier;

        Cores(String id, int tier, ItemList item, int chargeMax, int chargeTier) {
            this.id = id;
            this.tier = tier;
            this.item = item;
            this.chargeMax = chargeMax;
            this.chargeTier = chargeTier;
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
        WaterBreathing("WaterBreathing", ItemList.Augment_WaterBreathing);

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
