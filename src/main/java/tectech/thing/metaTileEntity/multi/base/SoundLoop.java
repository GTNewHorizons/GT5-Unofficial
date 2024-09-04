package tectech.thing.metaTileEntity.multi.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@SideOnly(Side.CLIENT)
public class SoundLoop extends MovingSound {

    private final boolean whileActive;
    private final boolean whileInactive;
    private final int worldID;
    private boolean fadeMe = false;

    public SoundLoop(ResourceLocation p_i45104_1_, IGregTechTileEntity base, boolean stopWhenActive,
        boolean stopWhenInactive) {
        super(p_i45104_1_);
        this.whileActive = stopWhenActive;
        this.whileInactive = stopWhenInactive;
        xPosF = base.getXCoord();
        yPosF = base.getYCoord();
        zPosF = base.getZCoord();
        worldID = base.getWorld().provider.dimensionId;
        repeat = true;
        volume = 0.0625f;
    }

    @Override
    public void update() {
        if (donePlaying) {
            return;
        }
        if (fadeMe) {
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
            || !world.checkChunksExist((int) xPosF, (int) yPosF, (int) zPosF, (int) xPosF, (int) yPosF, (int) zPosF);
        if (donePlaying) return;
        TileEntity tile = world.getTileEntity((int) xPosF, (int) yPosF, (int) zPosF);
        if (!(tile instanceof IGregTechTileEntity)) {
            donePlaying = true;
            return;
        }
        fadeMe |= ((IGregTechTileEntity) tile).isActive() ? whileActive : whileInactive;
    }
}
