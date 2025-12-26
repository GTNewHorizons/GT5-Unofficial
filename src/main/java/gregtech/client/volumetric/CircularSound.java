package gregtech.client.volumetric;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;

public class CircularSound implements ISoundPosition {

    private final MTEEnhancedMultiBlockBase<?> multi;
    /// The center in machine-local coordinates
    private final Vec3Impl center;
    /// The circle's normal in machine-local coordinates
    private final Vec3Impl normal;
    /// The circle's radius in blocks
    private final float radius;
    /// The sound's rotation about the normal (0 = closest point, pi = farthest point, pi / 2 = right-most, 3pi/2 =
    /// left-most)
    private final float rotation;
    /// The max range beyond which the sound will not have a position
    private final float maxRange;

    public CircularSound(MTEEnhancedMultiBlockBase<?> multi, int cx, int cy, int cz, int nx, int ny, int nz,
        float radius, float rotation) {
        this(multi, cx, cy, cz, nx, ny, nz, radius, rotation, 64 * 64);
    }

    public CircularSound(MTEEnhancedMultiBlockBase<?> multi, int cx, int cy, int cz, int nx, int ny, int nz,
        float radius, float rotation, float range) {
        this.multi = multi;
        // Invert the Y components because StructureLib uses positive Y=down for some reason
        this.center = new Vec3Impl(cx, -cy, cz);
        this.normal = new Vec3Impl(nx, -ny, nz);
        this.radius = radius;
        this.rotation = rotation;
        this.maxRange = range * range;
    }

    private static final Vector3f ZERO = new Vector3f();
    private static final Vector3f FORWARD = new Vector3f(1, 0, 0);
    private static final Vector3f UP = new Vector3f(0, 1, 0);

    @Override
    @Nullable
    public Vector3f getPosition() {
        EntityPlayer player = GTMod.proxy.getThePlayer();

        IGregTechTileEntity igte = multi.getBaseMetaTileEntity();

        if (igte == null) return null;

        // spotless:off

        double dist2 = ISoundPosition.lengthSquared(
            player.posX - igte.getXCoord(),
            player.posY - igte.getYCoord(),
            player.posZ - igte.getZCoord());

        if (dist2 > maxRange) return null;

        var machine = new Vector3f(igte.getXCoord(), igte.getYCoord(), igte.getZCoord());

        // Transform the center and normal into world coordinates
        Vector3f center = ISoundPosition.toVector(multi.getExtendedFacing().getWorldOffset(this.center)).add(machine);
        Vector3f normal = ISoundPosition.toVector(multi.getExtendedFacing().getWorldOffset(this.normal));

        Vector3f pos = new Vector3f((float) player.posX, (float) player.posY, (float) player.posZ);

        // Get the center->player pos vector
        pos.sub(center);

        // Project the player position onto the normal
        Vector3f projectedPosition = new Vector3f(normal).mul(normal.dot(pos));

        // Subtract the projected vector from the player position to get the position on the circle's plane
        pos.sub(projectedPosition);

        // If the player is at the center of the sound, just pick a spot on the circle arbitrarily
        if (pos.equals(ZERO, 0.001f)) {
            if (normal.maxComponent() == 1) {
                // If the normal faces up, get its cross product with FORWARD
                pos.set(normal)
                    .cross(FORWARD);
            } else {
                // If the normal is horizontal, get its cross produce with UP
                pos.set(normal)
                    .cross(UP);
            }
        }

        // Normalize to a point on the circle with radius=1, then scale it to the proper radius
        pos.normalize().mul(radius);

        if (rotation != 0) {
            AxisAngle4f rotation = new AxisAngle4f(this.rotation, normal);

            // Rotate if needed
            rotation.transform(pos);
        }

        // Translate back to world coordinates
        return pos.add(center);

        // spotless:on
    }
}
