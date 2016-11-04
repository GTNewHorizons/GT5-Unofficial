package gtPlusPlus.core.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public abstract interface IGuiManager extends IGuiManagerMiscUtils {

	public abstract Object getContainer(EntityPlayer paramEntityPlayer, int paramInt);

	public abstract ChunkCoordinates getCoordinates();

	public abstract Object getGui(EntityPlayer paramEntityPlayer, int paramInt);

	public abstract World getWorld();
}
