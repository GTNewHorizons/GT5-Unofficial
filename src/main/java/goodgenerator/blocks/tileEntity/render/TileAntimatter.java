
package goodgenerator.blocks.tileEntity.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;

import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;

public class TileAntimatter extends TileEntity {

    public boolean shouldRender = true;
    private AxisAlignedBB boundingBox;

    // Antimatter Core settings
    public static final float spikeR = 0.153f, spikeG = 0.435f, spikeB = 1f;
    public static final float coreR = 0.435f, coreG = 0.718f, coreB = 1f;
    public static final float maximalRadius = 7; // Includes core radius + spike length
    // Due to being partially managed by a global timer, rotationSpeedMultiplier shouldn't change
    // Otherwise it'll cause a snapping effect
    public final float rotationSpeedMultiplier = 1;

    public float coreScale = 1f;
    public float coreScaleSnapshot = 1f;
    public final float coreScaleTransitionTime = 2.5f;
    public float timeSnapshot;
    public float spikeFactor = .01f;

    // Protomatter Settings
    public static float protoSpiralMaxRadius = .5f;
    public static final float protoR = 0.2f, protoG = 0.2f, protoB = 0.2f;
    public float protomatterScale = .2f;
    public boolean protomatterRender = false;
    public float rotX = 0, rotY = 0, rotZ = 0;
    public float rotationAngle = 0;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("coreScale", coreScale);
        compound.setFloat("coreScaleSnapshot", coreScaleSnapshot);
        compound.setFloat("currentTime", timeSnapshot);
        compound.setFloat("spikeFactor", spikeFactor);
        compound.setBoolean("protomatterRender", protomatterRender);

        compound.setFloat("rotX", rotX);
        compound.setFloat("rotY", rotY);
        compound.setFloat("rotZ", rotZ);
        compound.setFloat("rotationAngle", rotationAngle);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        coreScale = compound.getFloat("coreScale");
        coreScaleSnapshot = compound.getFloat("coreScaleSnapshot");
        timeSnapshot = compound.getFloat("currentTime");
        spikeFactor = compound.getFloat("spikeFactor");
        protomatterRender = compound.getBoolean("protomatterRender");

        rotX = compound.getFloat("rotX");
        rotY = compound.getFloat("rotY");
        rotZ = compound.getFloat("rotZ");
        rotationAngle = compound.getFloat("rotationAngle");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (boundingBox == null) {
            boundingBox = AxisAlignedBB.getBoundingBox(
                xCoord - maximalRadius - 1,
                yCoord - maximalRadius - 1,
                zCoord - maximalRadius - 1,
                xCoord + maximalRadius + 1,
                yCoord + maximalRadius + 1,
                zCoord + maximalRadius + 1);
        }
        return boundingBox;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    public void setProtomatterRender(boolean flag) {
        if (protomatterRender == flag) return;
        protomatterRender = flag;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public float getSpiralRadius(float normalization) {
        return Math.min(coreScale / 2.0f * normalization, protoSpiralMaxRadius);
    }

    public void setCoreSize(float diameter) {
        coreScaleSnapshot = coreScale;
        coreScale = diameter;
        timeSnapshot = this.getWorldObj()
            .getWorldInfo()
            .getWorldTotalTime();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setRotationFields(ForgeDirection direction, Rotation rotation) {
        Matrix4f rotationMatrix = new Matrix4f().identity();

        float localAngle = switch (rotation) {
            case NORMAL -> 0;
            case CLOCKWISE -> 90;
            case COUNTER_CLOCKWISE -> -90;
            case UPSIDE_DOWN -> 180;
        };
        localAngle = (float) Math.toRadians(localAngle);
        rotationMatrix.rotate(localAngle, direction.offsetX, direction.offsetY, direction.offsetZ);

        float x = 0, y = 0;
        float angle = switch (direction) {
            case DOWN -> {
                x = 1;
                yield 90;
            }
            case UP -> {
                x = 1;
                yield -90;
            }
            case EAST, SOUTH -> {
                y = 1;
                yield 90;
            }
            case WEST, NORTH -> {
                y = 1;
                yield -90;
            }
            case UNKNOWN -> 0.0F;
        };
        angle = (float) Math.toRadians(angle);
        rotationMatrix.rotate(angle, x, y, 0);

        AxisAngle4f rotationVector = new AxisAngle4f();
        rotationMatrix.getRotation(rotationVector);

        rotationAngle = rotationVector.angle / (float) Math.PI * 180;
        rotX = rotationVector.x;
        rotY = rotationVector.y;
        rotZ = rotationVector.z;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

}
