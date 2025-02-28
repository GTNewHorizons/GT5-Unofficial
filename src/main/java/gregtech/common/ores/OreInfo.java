package gregtech.common.ores;

import org.jetbrains.annotations.ApiStatus;

import com.gtnewhorizon.gtnhlib.util.ObjectPooler;

import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;

/**
 * This class represents an ore block. Not all combinations are valid, it depends on what the ore adapters support.
 *
 * <p>
 *
 * This class comes with a built-in pooling mechanism since these objects are used in several hot-paths.
 * Any time the {@link OreManager} returns an {@link OreInfo}, it should be enclosed in a try-with-resources to release
 * the object back to the pool automatically. If a try-with-resources clutters the code significantly, you can call
 * {@link OreInfo#release()} manually. You should call {@link OreInfo#release()} unless you have a good reason not to
 * (like making thousands of objects at a time, which would cause a memory leak).
 *
 * <p>
 *
 * If you don't know for certain what kind of ore you're dealing with, everything should be done through the
 * {@link OreManager} where possible, and the ore adapter should be retrieved via
 * {@link OreManager#getAdapter(OreInfo)}. It's fine to refer to a concrete ore adapter if you know the ore type for
 * certain.
 *
 * <pre>
 * {@code
 *  void foo(Block block, int meta) {
 *   try (OreInfo<?> info = OreManager.getOreInfo(block, meta)) {
 *     // do something with info
 *   }
 * }
 * </pre>
 */
public class OreInfo<TMat extends IOreMaterial> implements AutoCloseable {

    /**
     * The cached adapter that produced this info. May not be valid, so use {@link OreManager#getAdapter(OreInfo)}
     * instead.
     */
    @ApiStatus.Internal
    IOreAdapter<TMat> cachedAdapter;

    public TMat material;
    public IStoneType stoneType;
    public boolean isSmall;
    /** Natural ores can be mined by machines and can be fortuned. Ores placed by the player aren't natural. */
    public boolean isNatural;

    static final ObjectPooler<OreInfo<?>> ORE_INFO_POOL = new ObjectPooler<>(OreInfo::new);

    @SuppressWarnings("unchecked")
    public static <T extends IOreMaterial> OreInfo<T> getNewInfo() {
        synchronized (ORE_INFO_POOL) {
            return (OreInfo<T>) ORE_INFO_POOL.getInstance();
        }
    }

    public static void releaseInfo(OreInfo<?> info) {
        synchronized (ORE_INFO_POOL) {
            ORE_INFO_POOL.releaseInstance(info.reset());
        }
    }

    @Override
    public void close() {
        release();
    }

    public void release() {
        releaseInfo(this);
    }

    public OreInfo<TMat> reset() {
        cachedAdapter = null;
        material = null;
        stoneType = null;
        isSmall = false;
        isNatural = false;
        return this;
    }

    public OreInfo<TMat> clone() {
        OreInfo<TMat> dup = getNewInfo();
        dup.cachedAdapter = this.cachedAdapter;
        dup.material = this.material;
        dup.stoneType = this.stoneType;
        dup.isSmall = this.isSmall;
        dup.isNatural = this.isNatural;
        return dup;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((material == null) ? 0 : material.hashCode());
        result = prime * result + ((stoneType == null) ? 0 : stoneType.hashCode());
        result = prime * result + (isSmall ? 1231 : 1237);
        result = prime * result + (isNatural ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        OreInfo<?> other = (OreInfo<?>) obj;
        if (material == null) {
            if (other.material != null) return false;
        } else if (!material.equals(other.material)) return false;
        if (stoneType == null) {
            if (other.stoneType != null) return false;
        } else if (!stoneType.equals(other.stoneType)) return false;
        if (isSmall != other.isSmall) return false;
        if (isNatural != other.isNatural) return false;
        return true;
    }

    @Override
    public String toString() {
        return "OreInfo [material=" + material
            + ", stoneType="
            + stoneType
            + ", isSmall="
            + isSmall
            + ", isNatural="
            + isNatural
            + "]";
    }
}
