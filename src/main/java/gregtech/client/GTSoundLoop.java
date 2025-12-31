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
import gregtech.client.volumetric.ISoundPosition;

@SideOnly(Side.CLIENT)
public class GTSoundLoop extends MovingSound {

    private static final float VOLUME_RAMP = 0.0625f;
    private final boolean whileActive;
    private final boolean whileInactive;
    private final int worldID;

    private boolean fadeMe = false;
    private final int tileX;
    private final int tileY;
    private final int tileZ;

    private float targetVolume = 1;
    private ISoundPosition position = null;

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
        volume = VOLUME_RAMP;
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
        if (donePlaying) return;

        if (position != null) {
            Vector3f pos = position.getPosition();

            if (pos != null) {
                setPosition(new Vector3f(pos).add(0.5f, 0.5f, 0.5f));
            }
        }

        if (fadeMe) {
            volume -= VOLUME_RAMP * targetVolume;
            if (volume <= 0) {
                volume = 0;
                stop();
                return;
            }
        } else if (volume < targetVolume) {
            volume += VOLUME_RAMP * targetVolume;
        }

        World world = Minecraft.getMinecraft().thePlayer.worldObj;

        if (world.provider.dimensionId != worldID) {
            stop();
            return;
        }

        if (!world.checkChunksExist(tileX, tileY, tileZ, tileX, tileY, tileZ)) {
            stop();
            return;
        }

        TileEntity tile = world.getTileEntity(tileX, tileY, tileZ);

        if (!(tile instanceof IGregTechTileEntity igte)) {
            stop();
            return;
        }

        fadeMe |= igte.isActive() ? whileActive : whileInactive;
    }

    public void stop() {
        donePlaying = true;
    }

    public GTSoundLoop setFadeMe(boolean value) {
        fadeMe = value;

        return this;
    }

    public GTSoundLoop setPosition(float x, float y, float z) {
        xPosF = x;
        yPosF = y;
        zPosF = z;

        return this;
    }

    public GTSoundLoop setPosition(Vector3f v) {
        xPosF = v.x;
        yPosF = v.y;
        zPosF = v.z;

        return this;
    }

    public boolean fades() {
        return fadeMe;
    }

    public GTSoundLoop setVolume(float volume) {
        targetVolume = volume;

        return this;
    }

    public GTSoundLoop setPosition(ISoundPosition position) {
        this.position = position;

        if (position != null) {
            Vector3f pos = position.getPosition();

            if (pos != null) {
                setPosition(new Vector3f(pos).add(0.5f, 0.5f, 0.5f));
            }
        } else {
            setPosition(tileX + 0.5f, tileY + 0.5f, tileZ + 0.5f);
        }

        return this;
    }
}
