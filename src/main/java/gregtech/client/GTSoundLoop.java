package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.joml.Vector3f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@SideOnly(Side.CLIENT)
public class GTSoundLoop extends MovingSound {

    public static final float VOLUME_RAMP = 0.0625f;
    private final boolean whileActive;
    private final boolean whileInactive;
    private final int worldID;

    private boolean fadeMe = false;
    private final int tileX;
    private final int tileY;
    private final int tileZ;
    private float targetVolume = 1, volumeRamp;

    /**
     * Constructs a GTSoundLoop.
     *
     * @param soundResource    the sound file location
     * @param tileEntity       the tile entity associated with this sound
     * @param stopWhenActive   flag to stop the sound when the block is active
     * @param stopWhenInactive flag to stop the sound when the block is inactive
     * @param soundX           positional sound X coordinate
     * @param soundY           positional sound Y coordinate
     * @param soundZ           positional sound Z coordinate
     */
    public GTSoundLoop(ResourceLocation soundResource, IGregTechTileEntity tileEntity, boolean stopWhenActive,
        boolean stopWhenInactive, float soundX, float soundY, float soundZ) {

        super(soundResource);
        this.whileActive = stopWhenActive;
        this.whileInactive = stopWhenInactive;
        tileX = tileEntity.getXCoord();
        tileY = tileEntity.getYCoord();
        tileZ = tileEntity.getZCoord();
        xPosF = soundX;
        yPosF = soundY;
        zPosF = soundZ;
        worldID = tileEntity.getWorld().provider.dimensionId;
        repeat = true;
        volumeRamp = VOLUME_RAMP;
        volume = VOLUME_RAMP;
        if (base.getMetaTileEntity() instanceof ISoundLoopAware loopAware) {
            loopAware.modifySoundLoop(this);
        }
    }

    public GTSoundLoop(ResourceLocation p_i45104_1_, IGregTechTileEntity base, boolean stopWhenActive,
        boolean stopWhenInactive, float volumeRamp) {
        super(p_i45104_1_);
        this.whileActive = stopWhenActive;
        this.whileInactive = stopWhenInactive;
        xPosF = machineX = base.getXCoord();
        yPosF = machineY = base.getYCoord();
        zPosF = machineZ = base.getZCoord();
        worldID = base.getWorld().provider.dimensionId;
        repeat = true;
        this.volumeRamp = volumeRamp;
        volume = volumeRamp;
        if (base.getMetaTileEntity() instanceof ISoundLoopAware loopAware) {
            loopAware.modifySoundLoop(this);
        }
    }

    /**
     * Constructs a GTSoundLoop.
     *
     * @param soundResource    the sound file location
     * @param tileEntity       the tile entity associated with this sound
     * @param stopWhenActive   flag to stop the sound when the block is active
     * @param stopWhenInactive flag to stop the sound when the block is inactive
     *
     * @implNote positional sound coordinates centred on tile
     */
    public GTSoundLoop(ResourceLocation soundResource, IGregTechTileEntity tileEntity, boolean stopWhenActive,
        boolean stopWhenInactive) {
        this(
            soundResource,
            tileEntity,
            stopWhenActive,
            stopWhenInactive,
            tileEntity.getXCoord() + .5f,
            tileEntity.getYCoord() + .5f,
            tileEntity.getZCoord() + .5f);
    }

    /**
     * Constructs a GTSoundLoop.
     *
     * @param soundResource    the sound file location
     * @param tileEntity       the tile entity associated with this sound
     * @param stopWhenActive   flag to stop the sound when the block is active
     * @param stopWhenInactive flag to stop the sound when the block is inactive
     *
     * @implNote positional sound coordinates centred on tile
     */
    public GTSoundLoop(ResourceLocation soundResource, IGregTechTileEntity tileEntity, boolean stopWhenActive,
        boolean stopWhenInactive) {
        this(
            soundResource,
            tileEntity,
            stopWhenActive,
            stopWhenInactive,
            tileEntity.getXCoord() + .5f,
            tileEntity.getYCoord() + .5f,
            tileEntity.getZCoord() + .5f);
    }

    @Override
    public void update() {
        if (donePlaying) {
            return;
        }
        if (fadeMe) {
            volume -= volumeRamp * targetVolume;
            if (volume <= 0) {
                volume = 0;
                donePlaying = true;
            }
        } else if (volume < targetVolume) {
            volume += volumeRamp * targetVolume;
        }
        World world = Minecraft.getMinecraft().thePlayer.worldObj;
        donePlaying = world.provider.dimensionId != worldID
            || !world.checkChunksExist(tileX, tileY, tileZ, tileX, tileY, tileZ);
        if (donePlaying) return;
        TileEntity tile = world.getTileEntity(tileX, tileY, tileZ);
        if ((tile instanceof IGregTechTileEntity iGregTechTileEntity)) {
            fadeMe |= iGregTechTileEntity.isActive() ? whileActive : whileInactive;

            if (iGregTechTileEntity.getMetaTileEntity() instanceof ISoundLoopAware loopAware) {
                loopAware.onSoundLoopTicked(this);
            }
            return;
        }

        donePlaying = true;
    }

    public void setDonePlaying(boolean value) {
        donePlaying = value;
    }

    public void setFadeMe(boolean value) {
        fadeMe = value;
    }

    public void setPosition(float x, float y, float z) {
        xPosF = x;
        yPosF = y;
        zPosF = z;
    }

    public void setPosition(Vector3f v) {
        xPosF = v.x;
        yPosF = v.y;
        zPosF = v.z;
    }

    public boolean isFading() {
        return fadeMe;
    }

    public void setVolume(float volume) {
        if (isFading()) {
            targetVolume = volume;
        } else {
            this.volume = volume;
        }
    }
}
