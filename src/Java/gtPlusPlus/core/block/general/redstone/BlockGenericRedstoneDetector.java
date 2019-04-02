package gtPlusPlus.core.block.general.redstone;

import java.util.List;
import java.util.Random;

import gtPlusPlus.core.tileentities.general.redstone.TileEntityRedstoneHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGenericRedstoneDetector extends BlockGenericRedstone {

	public BlockGenericRedstoneDetector() {
		super("detector", "Redstone Detector");
		setTickRandomly(true);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityRedstoneDetector();
	}
	
	public class TileEntityRedstoneDetector extends TileEntityRedstoneHandler {
		public TileEntityRedstoneDetector() {
			super(0);
		}		
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		// TODO Auto-generated method stub
		super.registerBlockIcons(p_149651_1_);
	}

	@Override
	public int getLightValue() {
		// TODO Auto-generated method stub
		return super.getLightValue();
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		return 0;
	}

	@Override
	public boolean canProvidePower() {
		return false;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		return 0;
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		// TODO Auto-generated method stub
		super.getSubBlocks(p_149666_1_, p_149666_2_, p_149666_3_);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return super.getLightValue(world, x, y, z);
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		// TODO Auto-generated method stub
		return super.shouldCheckWeakPower(world, x, y, z, side);
	}

	@Override
	public void updateTick(World aWorld, int aX, int aY, int aZ, Random aRand) {
		
		TileEntity aThisTile = aWorld.getTileEntity(aX, aY, aZ);		
		if (aThisTile != null) {
			TileEntityRedstoneHandler aRedstoneTile = (TileEntityRedstoneHandler) aThisTile;			
			aRedstoneTile.setCurrentTextureArray(null);			
		}
		
		
		super.updateTick(aWorld, aX, aY, aZ, aRand);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		// TODO Auto-generated method stub
		return null;
	}

}
