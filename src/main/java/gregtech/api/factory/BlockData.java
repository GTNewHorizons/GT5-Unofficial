package gregtech.api.factory;

import java.util.HashMap;
import java.util.function.Function;

import net.minecraft.world.World;

import org.joml.Vector4i;

import com.gtnewhorizon.gtnhlib.util.ObjectPooler;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class BlockData<T> extends HashMap<Vector4i, T> {

    private final ObjectPooler<Vector4i> pooler = new ObjectPooler<>(Vector4i::new);

    public T get(IMetaTileEntity imte) {
        return imte.getBaseMetaTileEntity() != null ? get(imte.getBaseMetaTileEntity()) : null;
    }

    public T get(IGregTechTileEntity igte) {
        return get(igte.getWorld(), igte.getXCoord(), igte.getYCoord(), igte.getZCoord());
    }

    public T get(World world, int x, int y, int z) {
        Vector4i key = pooler.getInstance();

        key.w = world.provider.dimensionId;
        key.x = x;
        key.y = y;
        key.z = z;

        T t = this.get(key);

        pooler.releaseInstance(key);

        return t;
    }

    public void set(IMetaTileEntity imte, T t) {
        if (imte.getBaseMetaTileEntity() != null) {
            set(imte.getBaseMetaTileEntity(), t);
        }
    }

    public void set(IGregTechTileEntity igte, T t) {
        set(igte.getWorld(), igte.getXCoord(), igte.getYCoord(), igte.getZCoord(), t);
    }

    public T set(World world, int x, int y, int z, T t) {
        Vector4i key = pooler.getInstance();

        key.w = world.provider.dimensionId;
        key.x = x;
        key.y = y;
        key.z = z;

        T old = this.put(key, t);

        pooler.releaseInstance(key);

        return old;
    }

    public T remove(World world, int x, int y, int z) {
        Vector4i key = pooler.getInstance();

        key.w = world.provider.dimensionId;
        key.x = x;
        key.y = y;
        key.z = z;

        T removed = this.remove(key);

        pooler.releaseInstance(key);

        return removed;
    }

    public T remove(IMetaTileEntity mte) {
        IGregTechTileEntity igte = mte.getBaseMetaTileEntity();

        if (igte == null) return null;

        Vector4i key = pooler.getInstance();

        key.w = igte.getWorld().provider.dimensionId;
        key.x = igte.getXCoord();
        key.y = igte.getYCoord();
        key.z = igte.getZCoord();

        T removed = this.remove(key);

        pooler.releaseInstance(key);

        return removed;
    }

    public T computeIfAbsent(World world, int x, int y, int z, Function<Vector4i, T> ctor) {
        Vector4i key = pooler.getInstance();

        key.w = world.provider.dimensionId;
        key.x = x;
        key.y = y;
        key.z = z;

        T t = this.computeIfAbsent(key, ctor);

        pooler.releaseInstance(key);

        return t;
    }
}
