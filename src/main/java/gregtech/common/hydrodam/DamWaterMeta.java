package gregtech.common.hydrodam;

import org.jetbrains.annotations.Contract;

/// Layout of the dam water block's metadata. EndlessIDs gives blocks 16 bits of metadata.
///
/// - Bits 0-11: the volume id. 0 means the block has not been assigned to a volume yet.
/// - Bit 12: the hull flag, set on blocks that touch a non-volume block.
/// - Bits 13-15: unused.
public final class DamWaterMeta {

    public static final int ID_BITS = 12;
    public static final int MAX_ID = (1 << ID_BITS) - 1;
    public static final int UNASSIGNED_ID = 0;

    private static final int ID_MASK = MAX_ID;
    private static final int HULL_FLAG = 1 << ID_BITS;

    private DamWaterMeta() {}

    @Contract(pure = true)
    public static int getVolumeId(int meta) {
        return meta & ID_MASK;
    }

    @Contract(pure = true)
    public static boolean isHull(int meta) {
        return (meta & HULL_FLAG) != 0;
    }

    @Contract(pure = true)
    public static int of(int volumeId, boolean hull) {
        return (volumeId & ID_MASK) | (hull ? HULL_FLAG : 0);
    }
}
