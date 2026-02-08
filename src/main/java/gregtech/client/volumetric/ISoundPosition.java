package gregtech.client.volumetric;

import net.minecraft.util.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

public interface ISoundPosition {

    /// Gets the position for a sound. Returns null when the sound shouldn't play (such as when it's too far away, to
    /// avoid any pointless calculations).
    @Nullable
    Vector3f getPosition();

    static Vector3f toVector(Vec3 v) {
        return new Vector3f((float) v.xCoord, (float) v.yCoord, (float) v.zCoord);
    }

    static Vector3f toVector(Vec3Impl v) {
        return new Vector3f(v.get0(), v.get1(), v.get2());
    }

    static double lengthSquared(double dx, double dy, double dz) {
        return dx * dx + dy * dy + dz * dz;
    }
}
