package gtPlusPlus.xmod.bop.blocks.base;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class SaplingBase extends BlockSapling
{
	protected String[] saplingTypes = new String[] {};
	protected IIcon[] saplingTextures = new IIcon[] {};

	//Sapling types - field_149882_a
	//Iicons - field_149881_b

	protected SaplingBase(String blockNameLocalized, String blockNameUnlocalized, String[] saplingTypes){
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		this.saplingTypes = saplingTypes;
		this.saplingTextures = new IIcon[saplingTypes.length];
		String blockName = "block"+Utils.sanitizeString(blockNameLocalized);
		GameRegistry.registerBlock(this, ItemBlock.class, blockName);
		this.setBlockName(blockName);
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(this), "treeSapling");
		this.setCreativeTab(AddToCreativeTab.tabBOP);
		LanguageRegistry.addName(this, blockNameLocalized);
	}

	private final void setVanillaVariable(Object toSet, Object value){
		toSet = value;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int someInt, int meta){
		/* p_149691_2_ &= 7;
        return saplingTextures[MathHelper.clamp_int(p_149691_2_, 0, 5)];*/
		//return this.saplingTextures[meta % this.saplingTextures.length];
		try {
			return this.saplingTextures[meta];  
		}catch(Throwable T){
			Utils.LOG_WARNING("Invalid Sapling meta is "+meta);
			return this.saplingTextures[0];
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand){
		if (!world.isRemote){
			super.updateTick(world, x, y, z, rand);
			if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(7) == 0){
				Utils.LOG_WARNING("Update Tick");
				this.updateMeta(world, x, y, z, rand);
			}
			else {
				Utils.LOG_WARNING("Tried to Tick.");
			}
		}
	}

	//Dunno - Think it is doGrow || doGrowthTick
	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z){
		Utils.LOG_WARNING("Please find what calls me - func_149853_b");
		this.updateMeta(world, x, y, z, rand);
	}

	public void updateMeta(World world, int x, int y, int z, Random rand){
		func_149879_c(world, x, y, z, rand);
	}

	@Override
	public void func_149879_c(World world, int x, int y, int z, Random rand){
		Utils.LOG_WARNING("func_149879_c - 1");
		int l = world.getBlockMetadata(x, y, z);

		if ((l & 8) == 0){
			Utils.LOG_WARNING("func_149879_c - 2");
			world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
		}
		else{
			Utils.LOG_WARNING("func_149879_c - 3");
			this.func_149878_d(world, x, y, z, rand);
		}
	}

	@Override
	public void func_149878_d(World world, int x, int y, int z, Random rand){
		Utils.LOG_WARNING("func_149878_d - 1");
		if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;
		int l = world.getBlockMetadata(x, y, z) & 7;
		Object object = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
		int i1 = 0;
		int j1 = 0;
		boolean flag = false;

		switch (l)
		{
			case 0:
			default:
				Utils.LOG_WARNING("Case 0 - Grow Tree");
				break;
			
		}

		Block block = Blocks.air;

		if (flag)
		{
			world.setBlock(x + i1, y, z + j1, block, 0, 4);
			world.setBlock(x + i1 + 1, y, z + j1, block, 0, 4);
			world.setBlock(x + i1, y, z + j1 + 1, block, 0, 4);
			world.setBlock(x + i1 + 1, y, z + j1 + 1, block, 0, 4);
		}
		else
		{
			world.setBlock(x, y, z, block, 0, 4);
		}
		
		if (!((WorldGenerator)object).generate(world, rand, x + i1, y, z + j1))
		{
			if (flag)
			{
				world.setBlock(x + i1, y, z + j1, this, l, 4);
				world.setBlock(x + i1 + 1, y, z + j1, this, l, 4);
				world.setBlock(x + i1, y, z + j1 + 1, this, l, 4);
				world.setBlock(x + i1 + 1, y, z + j1 + 1, this, l, 4);
			}
			else
			{
				world.setBlock(x, y, z, this, l, 4);
			}
		}
	}

	@Override
	public boolean func_149880_a(World world, int p_149880_2_, int p_149880_3_, int p_149880_4_, int p_149880_5_){
		return world.getBlock(p_149880_2_, p_149880_3_, p_149880_4_) == this && (world.getBlockMetadata(p_149880_2_, p_149880_3_, p_149880_4_) & 7) == p_149880_5_;
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	@Override
	public int damageDropped(int meta){
		return MathHelper.clamp_int(meta & 7, 0, 5);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List metaList){
		for (int i = 0; i < this.saplingTextures.length; ++i){
			metaList.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIcon){
		for (int i = 0; i < saplingTextures.length; ++i){
			saplingTextures[i] = iIcon.registerIcon(CORE.MODID + ":" + "trees/" + "saplings/" + "sapling_" + saplingTypes[i]);
		}
	}

}