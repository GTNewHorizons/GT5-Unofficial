package miscutil.core.multiblock.abstracts;

import miscutil.core.multiblock.abstracts.interfaces.IAbstractControllerInternal;
import miscutil.core.multiblock.base.BaseFakeMultiblockController;
import miscutil.core.multiblock.base.interfaces.IBaseMultiblockController;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.biome.BiomeGenBase;

import com.mojang.authlib.GameProfile;

public class AbstractFakeMultiblockController
extends BaseFakeMultiblockController
implements IAbstractControllerInternal
{
	public static final AbstractFakeMultiblockController instance = new AbstractFakeMultiblockController();


	public int getBlockLightValue()
	{
		return 0;
	}

	public boolean canBlockSeeTheSky()
	{
		return false;
	}

	public GameProfile getOwner()
	{
		return null;
	}

	public ChunkCoordinates getCoordinates()
	{
		return null;
	}

	public BiomeGenBase getBiome()
	{
		return null;
	}

	@Override
	public void assimilate(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
	}

	@Override
	public void _onAssimilated(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
	}

	@Override
	public void onAssimilated(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
	}

	@Override
	public boolean shouldConsume(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
		return false;
	}

	@Override
	public int getHealthScaled(int paramInt) {
		return 0;
	}
}
