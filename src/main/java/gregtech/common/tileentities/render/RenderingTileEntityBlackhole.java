package gregtech.common.tileentities.render;

import net.minecraft.nbt.NBTTagCompound;

public class RenderingTileEntityBlackhole extends AbstractRenderingTileEntity {

    // Should run from 0 to 1, >.5 starts showing changes
    private float stability = 1;
    // true = growing, false = shrinking
    private boolean scaling = true;
    private long startTime = 0;
    private float laserR = 0.318f, laserG = 0.157f, laserB = 0.533f;
    private boolean laserRender = false;

    private static final String NBT_TAG = "BLACKHOLE";

    private static final String STABILITY_NBT_TAG = NBT_TAG + "STABILITY";
    private static final String START_TIME_NBT_TAG = NBT_TAG + "START_TIME";
    private static final String SCALING_NBT_TAG = NBT_TAG + "SCALING";
    private static final String COLOR_RED_NBT_TAG = NBT_TAG + "COLOR_RED";
    private static final String COLOR_GREEN_NBT_TAG = NBT_TAG + "COLOR_GREEN";
    private static final String COLOR_BLUE_NBT_TAG = NBT_TAG + "COLOR_BLUE";
    private static final String RENDER_NBT_TAG = NBT_TAG + "LASER_RENDER";

    public RenderingTileEntityBlackhole() {
        super(10);
    }

    public void setLaserColor(float r, float g, float b) {
        if (!worldObj.isRemote) {
            laserR = r;
            laserG = g;
            laserB = b;
            updateToClient();
        }
    }

    public void toggleLaser(boolean toggle) {
        if (!worldObj.isRemote) {
            laserRender = toggle;
            updateToClient();
        }
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

    public boolean getLaserRender() {
        return laserRender;
    }

    public void startScaleChange(boolean scaling) {
        if (!worldObj.isRemote) {
            this.startTime = worldObj.getTotalWorldTime();
            this.scaling = scaling;
            updateToClient();
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean getScaling() {
        return scaling;
    }

    public void setStability(float stability) {
        // Can probably be simplified, maps stability > .5 as 1, and stability <.5 from 0 to 1
        if (!worldObj.isRemote) {
            this.stability = ((float) Math.min(stability + .5, 1f) - .5f) * 2f;
            updateToClient();
        }
    }

    public float getStability() {
        return stability;
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat(STABILITY_NBT_TAG, stability);
        compound.setBoolean(SCALING_NBT_TAG, scaling);
        compound.setLong(START_TIME_NBT_TAG, startTime);
        compound.setFloat(COLOR_RED_NBT_TAG, laserR);
        compound.setFloat(COLOR_GREEN_NBT_TAG, laserG);
        compound.setFloat(COLOR_BLUE_NBT_TAG, laserB);
        compound.setBoolean(RENDER_NBT_TAG, laserRender);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        stability = compound.getFloat(STABILITY_NBT_TAG);
        scaling = compound.getBoolean(SCALING_NBT_TAG);
        startTime = compound.getLong(START_TIME_NBT_TAG);
        laserR = compound.getFloat(COLOR_RED_NBT_TAG);
        laserG = compound.getFloat(COLOR_GREEN_NBT_TAG);
        laserB = compound.getFloat(COLOR_BLUE_NBT_TAG);
        laserRender = compound.getBoolean(RENDER_NBT_TAG);
        super.readFromNBT(compound);

    }

}
