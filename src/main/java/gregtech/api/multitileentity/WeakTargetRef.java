package gregtech.api.multitileentity;

import java.lang.ref.WeakReference;

import com.gtnewhorizons.mutecore.api.data.Coordinates;
import com.gtnewhorizons.mutecore.api.data.WorldContainer;
import dev.dominion.ecs.api.Entity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class WeakTargetRef<T extends Entity> {

    protected final ChunkCoordinates position = new ChunkCoordinates(0, -1, 0);
    protected final Class<?> targetClass;
    protected boolean shouldCache;
    protected WeakReference<T> target = new WeakReference<>(null);
    protected World world = null;

    public WeakTargetRef(Class<?> targetClass, boolean shouldCache) {
        this.targetClass = targetClass;
        this.shouldCache = shouldCache;
    }

    public WeakTargetRef(T target, boolean shouldCache) {
        this(target.getClass(), shouldCache);
        setTarget(target);
    }

    public void setTarget(T newTarget) {
        // Needs to be reworked in another way likely as it will be always true rn
        if (!targetClass.isInstance(newTarget)) {
            throw new IllegalArgumentException("Target is not of the correct type");
        }
        Coordinates coords = newTarget.get(Coordinates.class);
        position.set(coords.getX(), coords.getY(), coords.getZ());
        world = newTarget.get(WorldContainer.class).getWorld();
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setPosition(ChunkCoordinates position) {
        this.position.set(position.posX, position.posY, position.posZ);
    }

    public void setPosition(int x, int y, int z) {
        this.position.set(x, y, z);
    }

    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    public T get() {
        if (!shouldCache) {
            return resolveTarget();
        }
        T result = target.get();
        if (result == null) {
            result = resolveTarget();
            if (result != null) {
                target = new WeakReference<>(result);
            } else {
                target.clear();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected T resolveTarget() {
        if (world != null && position.posX >= 0 && world.blockExists(position.posX, position.posY, position.posZ)) {
            final TileEntity te = world.getTileEntity(position.posX, position.posY, position.posZ);
            return this.targetClass.isInstance(te) ? (T) te : null;
        }
        return null;
    }

    public ChunkCoordinates getPosition() {
        return position;
    }

    public void invalidate() {
        target.clear();
        world = null;
        position.set(0, -1, 0);
    }

}
