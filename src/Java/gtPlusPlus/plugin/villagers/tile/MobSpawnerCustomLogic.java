package gtPlusPlus.plugin.villagers.tile;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MobSpawnerCustomLogic extends MobSpawnerBaseLogic {

	private TileEntity mTile;

	public MobSpawnerCustomLogic(TileEntity tile) {
		if (tile != null) {
			mTile = tile;
		}
	}

	@Override
	public void func_98267_a(int eventID) {
		if (mTile != null) mTile.getWorldObj().addBlockEvent(mTile.xCoord, mTile.yCoord, mTile.zCoord, Blocks.mob_spawner, eventID, 0);
	}

	@Override
	public World getSpawnerWorld() {
		if (mTile != null) return mTile.getWorldObj();
		return null;
	}

	@Override
	public int getSpawnerX() {
		if (mTile != null) return mTile.xCoord;
		return 0;
	}

	@Override
	public int getSpawnerY() {
		if (mTile != null) return mTile.yCoord;
		return 0;
	}

	@Override
	public int getSpawnerZ() {
		if (mTile != null) return mTile.zCoord;
		return 0;
	}

	@Override
	public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
		super.setRandomEntity(p_98277_1_);
		if (mTile != null) {
			if (this.getSpawnerWorld() != null) {
				this.getSpawnerWorld().markBlockForUpdate(mTile.xCoord, mTile.yCoord, mTile.zCoord);
			}
		}
	}

}
