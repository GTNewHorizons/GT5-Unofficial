package gregtech.api.items.armor;

import java.util.HashMap;

import gregtech.api.enums.Materials;

public class MechArmorAugmentRegistries {

    public static final HashMap<String, Frames> framesMap = new HashMap<>();
    // Update this integer if you add a frame with more slots in a single category than the previous highest
    public static final int LARGEST_FRAME = 5;

    public enum Frames {

        None(0, 0, 0, 0, null, new short[] { 0, 0, 0, 0 }),
        Iron(2, 0, 1, 0, "Iron", Materials.Iron.mRGBa),
        Titanium(3, 0, 0, 0, "Titanium", Materials.Titanium.mRGBa),
        Infinity(0, 0, 0, 5, "Infinity", Materials.Infinity.mRGBa),
        Gold(0, 2, 1, 0, "Gold", Materials.Gold.mRGBa),;

        public final int protectionSlots;
        public final int movementSlots;
        public final int utilitySlots;
        public final int prismaticSlots;

        public final String id;

        public final short[] color;

        Frames(int protectionSlots, int movementSlots, int utilitySlots, int prismaticSlots, String id, short[] color) {
            this.protectionSlots = protectionSlots;
            this.movementSlots = movementSlots;
            this.utilitySlots = utilitySlots;
            this.prismaticSlots = prismaticSlots;
            this.id = id;
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

    }

    public enum Augments {

    }
}
