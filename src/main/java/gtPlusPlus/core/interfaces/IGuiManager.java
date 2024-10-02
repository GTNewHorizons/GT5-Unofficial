package gtPlusPlus.core.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public interface IGuiManager extends IGuiManagerMiscUtils {

    ChunkCoordinates getCoordinates();

    World getWorld();

    Object getGui(EntityPlayer paramEntityPlayer, int paramInt);

    Object getContainer(EntityPlayer paramEntityPlayer, int paramInt);
}
