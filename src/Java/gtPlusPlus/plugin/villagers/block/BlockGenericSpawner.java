package gtPlusPlus.plugin.villagers.block;

import gtPlusPlus.plugin.villagers.tile.TileEntityGenericSpawner;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGenericSpawner extends BlockMobSpawner {
	
	public BlockGenericSpawner () {
		
	}
	
	/**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityGenericSpawner(meta);
    }
	
}
