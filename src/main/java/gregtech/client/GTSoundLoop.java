package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

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
        if (donePlaying) {
            return;
        }
        if (fadeMe) {
            volume -= VOLUME_RAMP;
            if (volume <= 0) {
                volume = 0;
                donePlaying = true;
            }
        } else if (volume < 1) {
            volume += VOLUME_RAMP;
        }
        World world = Minecraft.getMinecraft().thePlayer.worldObj;
        donePlaying = world.provider.dimensionId != worldID
            || !world.checkChunksExist(tileX, tileY, tileZ, tileX, tileY, tileZ);
        if (donePlaying) return;
        TileEntity tile = world.getTileEntity(tileX, tileY, tileZ);
        if ((tile instanceof IGregTechTileEntity iGregTechTileEntity)) {
            fadeMe |= iGregTechTileEntity.isActive() ? whileActive : whileInactive;
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
}
