package gregtech.common.hydrodam;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.GregTechAPI;

/// Relights dam water whose light opacity changed because its volume's level changed. Level changes don't change any
/// blocks, so the world doesn't relight them by itself. Used on both sides.
final class DamLighting {

    private DamLighting() {}

    /// Relights the new top layer, and the previous top layer if it moved, wherever their opacity changed. The layers
    /// are approximated by the entry's bounds.
    static void relightChangedLayers(@NotNull World world, @NotNull GTPacketDamLevels.Entry entry,
        @Nullable DamLevel previous) {
        int topY = entry.level.topY;

        if (hasOpacityChanged(entry.level, previous, topY)) relightLayer(world, entry, topY);

        if (previous != null && previous.topY != topY && hasOpacityChanged(entry.level, previous, previous.topY)) {
            relightLayer(world, entry, previous.topY);
        }
    }

    private static boolean hasOpacityChanged(DamLevel level, @Nullable DamLevel previous, int y) {
        if (previous == null) return true;

        return DamWaterHeights.getLightOpacity(level.getFill(y))
            != DamWaterHeights.getLightOpacity(previous.getFill(y));
    }

    private static void relightLayer(World world, GTPacketDamLevels.Entry entry, int y) {
        for (int x = entry.minX; x <= entry.maxX; x++) {
            for (int z = entry.minZ; z <= entry.maxZ; z++) {
                if (!world.blockExists(x, y, z)) continue;
                if (world.getBlock(x, y, z) != GregTechAPI.sBlockDamWater) continue;

                world.func_147451_t(x, y, z);
            }
        }
    }
}
