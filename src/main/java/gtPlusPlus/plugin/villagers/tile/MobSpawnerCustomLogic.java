package gtPlusPlus.plugin.villagers.tile;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import gtPlusPlus.core.block.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MobSpawnerCustomLogic extends MobSpawnerBaseLogic {

	private TileEntityGenericSpawner mTile;

	public MobSpawnerCustomLogic(TileEntityGenericSpawner tile) {
		if (tile != null) {
			mTile = tile;
		}
		
		if (TileEntityGenericSpawner.mSpawners.get(mTile.getID()) != null) {
			Class<Entity> c = TileEntityGenericSpawner.mSpawners.get(mTile.getID());
			EntityRegistration x = EntityRegistry.instance().lookupModSpawn(c, false);
			if (x != null) {
				this.setEntityName(x.getEntityName());
			}
		}
		
	}

	@Override
	public void func_98267_a(int eventID) {
		if (mTile != null) mTile.getWorldObj().addBlockEvent(mTile.xCoord, mTile.yCoord, mTile.zCoord, ModBlocks.blockCustomMobSpawner, eventID, 0);
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
