package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.pack;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackY;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackZ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet3D;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;

/// Finds the separate pieces of a volume whose cells are no longer all connected. Only reads the volume's bookkeeping,
/// never the world.
final class VolumeComponents {

    private VolumeComponents() {}

    /// The face-connected groups of the volume's cells, largest first. Returns a single group if the volume is
    /// connected.
    ///
    /// Assumption: the volume is not empty.
    @NotNull
    static List<HashSet3D> find(@NotNull WaterVolume volume) {
        HashSet3D visited = new HashSet3D();
        List<HashSet3D> components = new ArrayList<>();

        volume.forEachCell((x, y, z) -> {
            if (visited.contains(x, y, z)) return;

            components.add(collectComponent(volume, pack(x, y, z), visited));
        });

        components.sort(
            Comparator.comparingInt(HashSet3D::size)
                .reversed());

        return components;
    }

    private static HashSet3D collectComponent(WaterVolume volume, long start, HashSet3D visited) {
        HashSet3D component = new HashSet3D();
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue();

        visited.add(start);
        queue.enqueue(start);

        while (!queue.isEmpty()) {
            long cell = queue.dequeueLong();

            component.add(cell);

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                int x = unpackX(cell) + dir.offsetX;
                int y = unpackY(cell) + dir.offsetY;
                int z = unpackZ(cell) + dir.offsetZ;

                if (volume.contains(x, y, z) && visited.add(x, y, z)) queue.enqueue(pack(x, y, z));
            }
        }

        return component;
    }
}
