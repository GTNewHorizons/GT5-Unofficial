package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.Annotations;

// This should either move to gt5u tiles, or get moved to GTNH-Intergalactic
public class TileLaser extends TileEntityAdvanced {

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRender = false;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public float red = 0, green = 0, blue = 0;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public float counter = 0F;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean realism = false;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public double rotAxisX = 0, rotAxisY = 0, rotAxisZ = 0, rotationAngle = 0;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("rgb_red", red);
        compound.setFloat("rgb_green", green);
        compound.setFloat("rgb_blue", blue);
        compound.setBoolean("shouldRender", shouldRender);
        compound.setDouble("rotAxisX", rotAxisX);
        compound.setDouble("rotAxisY", rotAxisY);
        compound.setDouble("rotAxisZ", rotAxisZ);
        compound.setDouble("rotationAngle", rotationAngle);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        red = compound.getFloat("rgb_red");
        blue = compound.getFloat("rgb_blue");
        green = compound.getFloat("rgb_green");
        shouldRender = compound.getBoolean("shouldRender");
        rotAxisX = compound.getDouble("rotAxisX");
        rotAxisY = compound.getDouble("rotAxisY");
        rotAxisZ = compound.getDouble("rotAxisZ");
        rotationAngle = compound.getDouble("rotationAngle");
    }

    public void setColors(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setRotationFields(ForgeDirection direction, Rotation rotation, Flip flip) {
        setRotationAngle(rotation, flip);
        setRotationAxis(direction);
    }

    private void setRotationAngle(Rotation rotation, Flip flip) {
        int invert = (flip == Flip.HORIZONTAL || flip == Flip.VERTICAL) ? 1 : -1;
        switch (rotation) {
            case NORMAL -> rotationAngle = 0;
            case CLOCKWISE -> rotationAngle = 90 * invert;
            case COUNTER_CLOCKWISE -> rotationAngle = -90 * invert;
            case UPSIDE_DOWN -> rotationAngle = 180;
        }
    }

    public void setRotationAxis(ForgeDirection direction) {
        rotAxisX = direction.offsetX;
        rotAxisY = direction.offsetY;
        rotAxisZ = direction.offsetZ;
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    public Boolean getShouldRender() {
        return shouldRender;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public double getPacketRange() {
        return 128;
    }

    @Override
    public int getPacketCooldown() {
        return 20;
    }

    @Override
    public boolean isNetworkedTile() {
        return true;
    }
}
