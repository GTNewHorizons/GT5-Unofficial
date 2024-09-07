package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBlackhole extends TileEntity {

    // Should run from 0 to 1, >.5 starts showing changes
    private float stability = 1;
    private float laserR = 0.318f, laserG = 0.157f, laserB = 0.533f;

    private static final String NBT_TAG = "BLACKHOLE";

    private static final String STABILITY_NBT_TAG = NBT_TAG + "STABILITY";
    private static final String COLOR_RED_NBT_TAG = NBT_TAG + "COLOR_RED";
    private static final String COLOR_GREEN_NBT_TAG = NBT_TAG + "COLOR_GREEN";
    private static final String COLOR_BLUE_NBT_TAG = NBT_TAG + "COLOR_BLUE";

    public void setLaserColor(float r, float g, float b) {
        laserR = r;
        laserG = g;
        laserB = b;
        updateToClient();
    }

    public float getLaserR() {
        return laserR;
    }

    public float getLaserG() {
        return laserG;
    }

    public float getLaserB() {
        return laserB;
    }

    public void setStability(float stability) {
        // Can probably be simplified, maps stability > .5 as 1, and stability <.5 from 0 to 1
        this.stability = ((float) Math.min(stability + .5, 1f) - .5f) * 2f;
        updateToClient();
    }

    public float getStability() {
        return stability;
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat(STABILITY_NBT_TAG, stability);
        compound.setFloat(COLOR_RED_NBT_TAG, laserR);
        compound.setFloat(COLOR_GREEN_NBT_TAG, laserG);
        compound.setFloat(COLOR_BLUE_NBT_TAG, laserB);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        stability = compound.getFloat(STABILITY_NBT_TAG);
        laserR = compound.getFloat(COLOR_RED_NBT_TAG);
        laserG = compound.getFloat(COLOR_GREEN_NBT_TAG);
        laserB = compound.getFloat(COLOR_BLUE_NBT_TAG);
        super.readFromNBT(compound);

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
    }

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

}
