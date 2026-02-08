package gregtech.client.volumetric;

import net.minecraft.entity.player.EntityPlayer;

import org.joml.Vector3f;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.util.GTUtility;

public class LinearSound implements ISoundPosition {

    private final MTEEnhancedMultiBlockBase<?> multi;
    private final Vec3Impl start, end;
    private final float distance;
    private float maxRange;
    private float posWeight = 1;
    private float centerWeight = 0;
    private float center = 0;

    public LinearSound(MTEEnhancedMultiBlockBase<?> multi, int x1, int y1, int z1, int x2, int y2, int z2,
        float range) {
        this.multi = multi;
        // Invert the Y components because StructureLib uses positive Y=down for some reason
        start = new Vec3Impl(x1, -y1, z1);
        end = new Vec3Impl(x2, -y2, z2);
        distance = ISoundPosition.toVector(end)
            .sub(ISoundPosition.toVector(start))
            .length();
        this.maxRange = range * range;
    }

    public LinearSound setRange(float range) {
        this.maxRange = range * range;

        return this;
    }

    public LinearSound setCentre(float posWeight, float centerWeight, float center) {
        this.posWeight = posWeight;
        this.centerWeight = centerWeight;
        this.center = center;

        return this;
    }

    @Override
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

        Vector3f machine = new Vector3f(igte.getXCoord(), igte.getYCoord(), igte.getZCoord());

        Vector3f start = ISoundPosition.toVector(multi.getExtendedFacing().getWorldOffset(this.start)).add(machine);
        Vector3f end = ISoundPosition.toVector(multi.getExtendedFacing().getWorldOffset(this.end)).add(machine);

        Vector3f playerPos = new Vector3f((float) player.posX, (float) player.posY, (float) player.posZ);

        // Calculate the start -> end unit vector
        Vector3f dir = new Vector3f(end).sub(start).normalize();

        // Project the player position onto the start + unit ray to get the ray scalar
        // start + ray * k = projected player pos
        float k = new Vector3f(playerPos).sub(start).dot(dir);

        // Clamp it to (0, distance)
        k = GTUtility.clamp(k, 0, distance);

        if (centerWeight > 0) {
            // Shift the position towards the center, if set
            k = (k * posWeight + center * centerWeight) / (posWeight + centerWeight);
        }

        // Calculate the position on the start->end vector
        return new Vector3f(start.x + dir.x * k, start.y + dir.y * k, start.z + dir.z * k);

        // spotless:on
    }
}
