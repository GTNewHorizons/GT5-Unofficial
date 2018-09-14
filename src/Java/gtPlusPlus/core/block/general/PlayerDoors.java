package gtPlusPlus.core.block.general;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityPlayerDoorBase;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PlayerDoors extends BlockDoor {

	private boolean mHasDrops = true;

	public PlayerDoors(Material aMaterial, String aTextureName, boolean vanillaType) {
		this (aMaterial, aTextureName, vanillaType, 0f, null, null);
	}
	
	public PlayerDoors(Material aMaterial, String aTextureName, boolean vanillaType, float aHardness, SoundType aStepSound, String aBlockExtensionName) {
		super(aMaterial);
		this.disableStats();
		this.setBlockName("playerDoor_"+aTextureName);
		if (aMaterial == Material.wood) {
			setHardness(3.0F);
			setStepSound(soundTypeWood);
			setBlockName("playerDoor"+"Wood");
			this.setHarvestLevel("axe", 1);
		}
		else if (aMaterial == Material.iron) {
			setHardness(5.0F);
			setStepSound(Block.soundTypeMetal);
			setBlockName("playerDoor"+"Iron");
			this.setHarvestLevel("pickaxe", 1);
			
		}
		else if (aMaterial == Material.glass) {
			setHardness(0.1F);
			setStepSound(Block.soundTypeGlass);
			setBlockName("playerDoor"+"Glass");
			this.setHarvestLevel("pickaxe", 1);
			mHasDrops = false;
			
		}
		else if (aMaterial == Material.ice) {
			setHardness(0.5F);
			setStepSound(Block.soundTypeSnow);
			setBlockName("playerDoor"+"Ice");
			this.setHarvestLevel("pickaxe", 1);
			mHasDrops = false;
			
		}
		else {
			setHardness(aHardness);
			setStepSound(aStepSound);
			setBlockName("playerDoor"+aBlockExtensionName);
			this.setHarvestLevel("axe", 1);
			
		}		
		this.setBlockTextureName(vanillaType ? aTextureName : CORE.MODID+":"+aTextureName);
		GameRegistry.registerBlock(this, Utils.sanitizeString(this.getUnlocalizedName()));
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityPlayerDoorBase(this, metadata);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		// TODO Auto-generated method stub
		return super.getItemDropped(p_149650_1_, p_149650_2_, p_149650_3_);
	}

	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		// TODO Auto-generated method stub
		return super.getItem(p_149694_1_, p_149694_2_, p_149694_3_, p_149694_4_);
	}

	@Override
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_,
			EntityPlayer p_149681_6_) {
		// TODO Auto-generated method stub
		super.onBlockHarvested(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, p_149681_6_);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		// TODO Auto-generated method stub
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	@Override
	protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_,
			ItemStack p_149642_5_) {
		// TODO Auto-generated method stub
		super.dropBlockAsItem(p_149642_1_, p_149642_2_, p_149642_3_, p_149642_4_, p_149642_5_);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {		
		return mHasDrops ? super.getDrops(world, x, y, z, metadata, fortune) : new ArrayList<ItemStack>();
	}

	
	
}
