package gtPlusPlus.plugin.villagers.block;

import static gtPlusPlus.core.lib.CORE.RANDOM;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.itemblock.ItemBlockSpawner;
import gtPlusPlus.plugin.villagers.Core_VillagerAdditions;
import gtPlusPlus.plugin.villagers.tile.TileEntityGenericSpawner;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenericSpawner extends BlockMobSpawner {
	
	public BlockGenericSpawner() {
		this.disableStats();
		this.setHardness(5.0F);
		this.setStepSound(soundTypeMetal);
		this.setBlockName("blockMobSpawnerEx");
		this.setBlockTextureName("mob_spawner");
		this.setResistance(2000.0F);
		GameRegistry.registerBlock(this, ItemBlockSpawner.class, "blockMobSpawnerEx");
		Core_VillagerAdditions.mInstance.log("Registered Custom Spawner Block.");
	}
	
	/**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityGenericSpawner(meta);
    }
    
    @Override
    public int getExpDrop(IBlockAccess world, int metadata, int fortune){
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }
    
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        for (int i = 0; i < Math.max(1,TileEntityGenericSpawner.mSpawners.size()); ++i)
        {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
        }
    }
	
}
