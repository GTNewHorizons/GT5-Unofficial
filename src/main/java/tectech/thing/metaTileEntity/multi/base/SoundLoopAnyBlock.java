package tectech.thing.metaTileEntity.multi.base;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@SideOnly(Side.CLIENT)
public class SoundLoopAnyBlock extends MovingSound {

    private final boolean stopWhenBlockActive;
    private final boolean stopWhenBlockInactive;
    private final int worldID;
    private boolean fadeOut = false;
    private final int tileX;
    private final int tileY;
    private final int tileZ;
    private Block blockToTriggerEnd = null;

    /**
     * Constructs a SoundLoopAnyBlock.
     *
     * @param soundResource    the sound file location
     * @param tileEntity       the tile entity associated with this sound
     * @param stopWhenActive   flag to stop the sound when the block is active
     * @param stopWhenInactive flag to stop the sound when the block is inactive
     * @param offset           positional offset for sound origin from the tile entity [x, y, z]
     * @param blockCheck       block that ends the sound when matched at the sound location
     */
    public SoundLoopAnyBlock(ResourceLocation soundResource, IGregTechTileEntity tileEntity, boolean stopWhenActive,
        boolean stopWhenInactive, int[] offset, Block blockCheck) {
        super(soundResource);
        this.stopWhenBlockActive = stopWhenActive;
        this.stopWhenBlockInactive = stopWhenInactive;
        tileX = tileEntity.getXCoord();
        tileY = tileEntity.getYCoord();
        tileZ = tileEntity.getZCoord();
        xPosF = tileX + offset[0] + .5f;
        yPosF = tileY + offset[1] + .5f;
        zPosF = tileZ + offset[2] + .5f;
        worldID = tileEntity.getWorld().provider.dimensionId;
        repeat = true;
        volume = 0.0625f;
        blockToTriggerEnd = blockCheck;
    }

    @Override
    public void update() {
        if (donePlaying) {
            return;
        }

        if (fadeOut) {
            volume -= 0.0625f;
            if (volume <= 0) {
                volume = 0;
                donePlaying = true;
            }
        } else if (volume < 1) {
            volume += 0.0625f;
        }

        World world = Minecraft.getMinecraft().thePlayer.worldObj;
        donePlaying = world.provider.dimensionId != worldID
            || !world.checkChunksExist(tileX, tileY, tileZ, tileX, tileY, tileZ);

        if (donePlaying) return;

        Block blockAtSoundLocation = world.getBlock(tileX, tileY, tileZ);
        if (blockToTriggerEnd != null) {
            donePlaying = blockAtSoundLocation == blockToTriggerEnd;
        }

        if (donePlaying) return;

        TileEntity tile = world.getTileEntity(tileX, tileY, tileZ);
        donePlaying = tile == null;

        if (donePlaying) return;

        // Adjust fading based on the activity state of the tile entity
        fadeOut |= ((IGregTechTileEntity) tile).isActive() ? stopWhenBlockActive : stopWhenBlockInactive;
    }
}
