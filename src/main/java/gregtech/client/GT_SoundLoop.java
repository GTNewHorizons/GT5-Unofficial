package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.multitileentity.machine.MultiTileBasicMachine;

@SideOnly(Side.CLIENT)
public class GT_SoundLoop extends MovingSound {

    private static final float VOLUME_RAMP = 0.0625f;
    private final boolean whileActive;
    private final boolean whileInactive;
    private final int worldID;
    private boolean fadeMe = false;

    public GT_SoundLoop(ResourceLocation p_i45104_1_, IGregTechTileEntity base, boolean stopWhenActive,
        boolean stopWhenInactive) {
        super(p_i45104_1_);
        this.whileActive = stopWhenActive;
        this.whileInactive = stopWhenInactive;
        xPosF = base.getXCoord();
        yPosF = base.getYCoord();
        zPosF = base.getZCoord();
        worldID = base.getWorld().provider.dimensionId;
        repeat = true;
        volume = VOLUME_RAMP;
    }

    public GT_SoundLoop(ResourceLocation sound, MultiTileBasicMachine<?> base, boolean stopWhenActive,
        boolean stopWhenInactive) {
        super(sound);
        this.whileActive = stopWhenActive;
        this.whileInactive = stopWhenInactive;
        final ChunkCoordinates coords = base.getCoords();
        xPosF = coords.posX;
        yPosF = coords.posY;
        zPosF = coords.posZ;
        worldID = base.getWorldObj().provider.dimensionId;
        repeat = true;
        volume = VOLUME_RAMP;
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
            || !world.checkChunksExist((int) xPosF, (int) yPosF, (int) zPosF, (int) xPosF, (int) yPosF, (int) zPosF);
        if (donePlaying) return;
        TileEntity tile = world.getTileEntity((int) xPosF, (int) yPosF, (int) zPosF);
        if ((tile instanceof IGregTechTileEntity)) {
            fadeMe |= ((IGregTechTileEntity) tile).isActive() ? whileActive : whileInactive;
            return;
        }

        if ((tile instanceof MultiTileBasicMachine)) {
            fadeMe |= ((MultiTileBasicMachine<?>) tile).isActive() ? whileActive : whileInactive;
            return;
        }

        donePlaying = true;
    }
}
