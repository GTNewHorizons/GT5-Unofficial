package gregtech.api.items.armor;

import java.util.HashMap;

import gregtech.api.enums.Materials;

public class MechArmorAugmentRegistries {

    public static final HashMap<Integer, Frames> framesMap = new HashMap<>();
    // Update this integer if you add a frame with more slots in a single category than the previous highest
    public static final int LARGEST_FRAME = 5;

    public enum Frames {

        Iron(2, 0, 1, 0, 1, Materials.Iron.mRGBa),
        Titanium(3, 0, 0, 0, 2, Materials.Titanium.mRGBa),
        Infinity(0, 0, 0, 5, 3, Materials.Infinity.mRGBa),
        Gold(0, 2, 1, 0, 4, Materials.Gold.mRGBa),;

        public final int protectionSlots;
        public final int movementSlots;
        public final int utilitySlots;
        public final int prismaticSlots;

        public final int id;

        public final short[] color;

        Frames(int protectionSlots, int movementSlots, int utilitySlots, int prismaticSlots, int id, short[] color) {
            this.protectionSlots = protectionSlots;
            this.movementSlots = movementSlots;
            this.utilitySlots = utilitySlots;
            this.prismaticSlots = prismaticSlots;
            this.id = id;
            this.color = color;
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
