package gregtech.common.ores;

import com.gtnewhorizon.gtnhlib.util.ObjectPooler;

import gregtech.api.interfaces.IMaterial;
import gregtech.api.interfaces.IStoneType;

public class OreInfo<TMat extends IMaterial> implements AutoCloseable {

    public IOreAdapter<TMat> cachedAdapter;
    public TMat material;
    public IStoneType stoneType;
    public boolean isSmall;
    public boolean isNatural;

    static final ObjectPooler<OreInfo<?>> ORE_INFO_POOL = new ObjectPooler<>(OreInfo::new);

    @SuppressWarnings("unchecked")
    public static <T extends IMaterial> OreInfo<T> getNewInfo() {
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
