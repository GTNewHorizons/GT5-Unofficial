package gtPlusPlus.plugin.villagers.tile;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MobSpawnerCustomLogic extends MobSpawnerBaseLogic {

	private final TileEntity mTile;
	
	public MobSpawnerCustomLogic(TileEntity tile) {
		mTile = tile;
	}
	
	
	@Override
	public void func_98267_a(int p_98267_1_){
		mTile.getWorldObj().addBlockEvent(
				mTile.xCoord,
				mTile.yCoord,
				mTile.zCoord,
				Blocks.mob_spawner,
				p_98267_1_,
				0);
    }

	@Override
	public World getSpawnerWorld() {
		return mTile.getWorldObj();
	}

	@Override
	public int getSpawnerX() {
		return mTile.xCoord;
	}

	@Override
	public int getSpawnerY() {
		return mTile.yCoord;
	}

	@Override
	public int getSpawnerZ() {
		return mTile.zCoord;
	}

}
