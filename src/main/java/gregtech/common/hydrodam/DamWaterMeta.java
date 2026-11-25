package gregtech.common.hydrodam;

import org.jetbrains.annotations.Contract;

/// Layout of the dam water block's metadata.
///
/// - Bits 0-11: the volume id. 0 means the block has not been assigned to a volume yet.
/// - Bits 12-23: unused. Bit 12 is reserved for marking blocks on the volume's hull, should that become useful.
public final class DamWaterMeta {

    public static final int ID_BITS = 12;
    public static final int MAX_ID = (1 << ID_BITS) - 1;
    public static final int UNASSIGNED_ID = 0;

    private static final int ID_MASK = MAX_ID;

    private DamWaterMeta() {}

    @Contract(pure = true)
    public static int getVolumeId(int meta) {
        return meta & ID_MASK;
    }

    @Contract(pure = true)
    public static int of(int volumeId) {
        return volumeId & ID_MASK;
    }
}
