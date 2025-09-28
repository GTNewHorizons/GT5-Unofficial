package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityModularSolidifierRenderer extends TileEntity {

    private AxisAlignedBB boundingBox;
    private boolean running = false;
    // [0] = r [1] = g [2] = b
    private float[] rgbm1 = new float[3];
    private float[] rgbm2 = new float[3];
    private float[] rgbm3 = new float[3];
    private float[] rgbm4 = new float[3];
    private boolean m1Init, m2Init, m3Init, m4Init;
    private static final String NBT_TAG = "FOUNDRY_";
    private static final String RUNNING_NBT_TAG = NBT_TAG + "RUNNING";
    private static final String M1_RED_NBT_TAG = NBT_TAG + "M1_RED";
    private static final String M1_GREEN_NBT_TAG = NBT_TAG + "M1_GREEN";
    private static final String M1_BLUE_NBT_TAG = NBT_TAG + "M1_BLUE";
    private static final String M2_RED_NBT_TAG = NBT_TAG + "M2_RED";
    private static final String M2_GREEN_NBT_TAG = NBT_TAG + "M2_GREEN";
    private static final String M2_BLUE_NBT_TAG = NBT_TAG + "M2_BLUE";
    private static final String M3_RED_NBT_TAG = NBT_TAG + "M3_RED";
    private static final String M3_GREEN_NBT_TAG = NBT_TAG + "M3_GREEN";
    private static final String M3_BLUE_NBT_TAG = NBT_TAG + "M3_BLUE";
    private static final String M4_RED_NBT_TAG = NBT_TAG + "M4_RED";
    private static final String M4_GREEN_NBT_TAG = NBT_TAG + "M4_GREEN";
    private static final String M4_BLUE_NBT_TAG = NBT_TAG + "M4_BLUE";
    private static final String M1_INIT = NBT_TAG + "M1_INIT";
    private static final String M2_INIT = NBT_TAG + "M2_INIT";
    private static final String M3_INIT = NBT_TAG + "M3_INIT";
    private static final String M4_INIT = NBT_TAG + "M4_INIT";
    // m1 - m4 is bottom to top.

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    // TODO: Figure out the range
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (boundingBox == null) {
            boundingBox = AxisAlignedBB
                .getBoundingBox(xCoord - 10, yCoord - 10, zCoord - 10, xCoord + 10, yCoord + 10, zCoord + 10);
        }
        return boundingBox;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setBoolean(RUNNING_NBT_TAG, running);
        compound.setFloat(M1_RED_NBT_TAG, rgbm1[0]);
        compound.setFloat(M1_GREEN_NBT_TAG, rgbm1[1]);
        compound.setFloat(M1_BLUE_NBT_TAG, rgbm1[2]);
        compound.setFloat(M2_RED_NBT_TAG, rgbm2[0]);
        compound.setFloat(M2_GREEN_NBT_TAG, rgbm2[1]);
        compound.setFloat(M2_BLUE_NBT_TAG, rgbm2[2]);
        compound.setFloat(M3_RED_NBT_TAG, rgbm3[0]);
        compound.setFloat(M3_GREEN_NBT_TAG, rgbm3[1]);
        compound.setFloat(M3_BLUE_NBT_TAG, rgbm3[2]);
        compound.setFloat(M4_RED_NBT_TAG, rgbm4[0]);
        compound.setFloat(M4_GREEN_NBT_TAG, rgbm4[1]);
        compound.setFloat(M4_BLUE_NBT_TAG, rgbm4[2]);
        compound.setBoolean(M1_INIT, m1Init);
        compound.setBoolean(M2_INIT, m2Init);
        compound.setBoolean(M3_INIT, m3Init);
        compound.setBoolean(M4_INIT, m4Init);

        super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        running = compound.getBoolean(RUNNING_NBT_TAG);
        rgbm1 = new float[] { compound.getFloat(M1_RED_NBT_TAG), compound.getFloat(M1_GREEN_NBT_TAG),
            compound.getFloat(M1_BLUE_NBT_TAG) };
        rgbm2 = new float[] { compound.getFloat(M2_RED_NBT_TAG), compound.getFloat(M2_GREEN_NBT_TAG),
            compound.getFloat(M2_BLUE_NBT_TAG) };
        rgbm3 = new float[] { compound.getFloat(M3_RED_NBT_TAG), compound.getFloat(M3_GREEN_NBT_TAG),
            compound.getFloat(M3_BLUE_NBT_TAG) };
        rgbm4 = new float[] { compound.getFloat(M4_RED_NBT_TAG), compound.getFloat(M4_GREEN_NBT_TAG),
            compound.getFloat(M4_BLUE_NBT_TAG) };
        m1Init = compound.getBoolean(M1_INIT);
        m2Init = compound.getBoolean(M2_INIT);
        m3Init = compound.getBoolean(M3_INIT);
        m4Init = compound.getBoolean(M4_INIT);
        super.readFromNBT(compound);
    }

    public void setRunning(boolean running) {
        if (!worldObj.isRemote && this.running != running) {
            this.running = running;
            updateToClient();
        }
    }

    public boolean getRunning() {
        return running;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

    // no elegance needed :P
    public void setModuleColorWithIndex(float[] rgbArray, int index) {
        if (!worldObj.isRemote) {
            switch (index) {
                case 1:
                    rgbm1 = rgbArray;

                    break;
                case 2:
                    rgbm2 = rgbArray;

                    break;
                case 3:
                    rgbm3 = rgbArray;

                    break;
                case 4:
                    rgbm4 = rgbArray;

                    break;

            }

            updateToClient();
        }
    }

    public void setModuleStatusWithIndex(boolean status, int index) {
        switch (index) {
            case 1:
                m1Init = status;
                break;
            case 2:
                m2Init = status;
                break;
            case 3:
                m3Init = status;
                break;
            case 4:
                m4Init = status;
                break;

        }
        updateToClient();
    }

    public float[] getRgbm1() {
        return rgbm1;
    }

    public float[] getRgbm2() {
        return rgbm2;
    }

    public float[] getRgbm3() {
        return rgbm3;
    }

    public float[] getRgbm4() {
        return rgbm4;
    }

    public boolean isM1Set() {
        return m1Init;
    }

    public boolean isM2Set() {
        return m2Init;
    }

    public boolean isM3Set() {
        return m3Init;
    }

    public boolean isM4Set() {
        return m4Init;
    }

}
