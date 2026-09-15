package gregtech.api.factory;

import java.lang.ref.WeakReference;
import java.util.Iterator;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import org.joml.Vector3i;
import org.joml.Vector3ic;

public class BlockGroup implements Iterable<Vector3ic> {

    private int worldId;
    private WeakReference<World> world;

    private int[] coords;

    public int size() {
        return coords.length / 3;
    }

    public World getWorld() {
        World world = this.world.get();

        if (world != null) return world;

        world = DimensionManager.getWorld(worldId);

        this.world = new WeakReference<World>(world);

        return world;
    }

    private final Vector3i pooledVec = new Vector3i();

    public Vector3ic get(int i) {
        i *= 3;
        pooledVec.x = coords[i];
        pooledVec.y = coords[i + 1];
        pooledVec.z = coords[i + 2];
        return pooledVec;
    }

    @Override
    public Iterator<Vector3ic> iterator() {
        int total = coords.length / 3;

        return new Iterator<Vector3ic>() {

            int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < total;
            }

            @Override
            public Vector3ic next() {
                return get(counter++);
            }
        };
    }
}
