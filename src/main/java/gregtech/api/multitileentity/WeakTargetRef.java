package gregtech.api.multitileentity;

import java.lang.ref.WeakReference;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import gregtech.api.multitileentity.interfaces.IMultiTileEntity;

public class WeakTargetRef<T extends IMultiTileEntity> {

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
        if (!targetClass.isInstance(newTarget)) {
            throw new IllegalArgumentException("Target is not of the correct type");
        }
        position.set(newTarget.getXCoord(), newTarget.getYCoord(), newTarget.getZCoord());
        world = newTarget.getWorld();
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
        if (result == null || result.isDead()) {
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
