package gregtech.common.hydrodam;

import net.minecraft.world.World;

import gregtech.api.GregTechAPI;

/// Answers how full a dam water block is, on either side. Used by the block itself and by mixins into vanilla liquid
/// logic.
public final class DamWaterHeights {

    /// The light opacity of a full dam water block, the same as vanilla water.
    public static final int FULL_LIGHT_OPACITY = 3;

    private DamWaterHeights() {}

    /// The light opacity of a dam water block that is filled by the given fraction. Never 0, so that changing the level
    /// never changes the sky light height map.
    public static int getLightOpacity(float fill) {
        return Math.max(1, Math.round(fill * FULL_LIGHT_OPACITY));
    }

    /// How full the dam water block at the given position is, in [0, 1], or NaN if the block isn't dam water. The fill
    /// is quantized like {@link DamLevel} on both sides, so that the server and clients agree.
    ///
    /// Assumption: the position is loaded.
    public static float getFill(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) != GregTechAPI.sBlockDamWater) return Float.NaN;

        if (world.isRemote) {
            int volumeId = DamWaterMeta.getVolumeId(world.getBlockMetadata(x, y, z));

            return ClientDamLevels.getFill(world.provider.dimensionId, volumeId, y);
        }

        WaterVolume volume = DamVolumes.get(world)
            .getVolumeAt(x, y, z);

        if (volume == null) return 1;

        return DamLevel.quantize(Math.max(0, Math.min(1, volume.getLevel() - y)));
    }
}
