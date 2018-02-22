package gtPlusPlus.xmod.bop.blocks.base;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class LeavesBase extends BlockLeaves {

	protected IIcon[][] leafTextures = new IIcon[2][];
	protected String[][] leafType = new String[][] {{}, {}};
	protected String[] treeType = new String[] {};
	protected ItemStack[] bonusDrops;

	@SuppressWarnings("deprecation")
	public LeavesBase(String blockNameLocalized, String blockNameUnlocalized, ItemStack[] bonusDrops){
		this.bonusDrops = bonusDrops;
		String blockName = "block"+Utils.sanitizeString(blockNameLocalized)+"Leaves";
		GameRegistry.registerBlock(this, ItemBlock.class, blockName);
		this.setBlockName(blockName);
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(this), "treeLeaves");
		this.setCreativeTab(AddToCreativeTab.tabBOP);
		LanguageRegistry.addName(this, blockNameLocalized+" Leaves");
		Blocks.fire.setFireInfo(this, 80, 150);
	}

	private final void setVanillaVariable(Object toSet, Object value){
		toSet = value;
	}

	@Override
	public int quantityDropped(Random p_149745_1_){
		return p_149745_1_.nextInt(20) == 0 ? 1 : 0;
	}


	@Override//Drops when Leaf is broken
	protected void func_150124_c(World world, int x, int y, int z, int meta, int randomChance){
		if (this.treeType.length == this.bonusDrops.length){
			for (int i = 0; i < this.treeType.length; ++i){
				if (this.bonusDrops[i] != null && world.rand.nextInt(randomChance) == 0){
					this.dropBlockAsItem(world, x, y, z, ItemUtils.getSimpleStack(this.bonusDrops[i], 1));
				}
			}
		}
		else {
			Logger.WARNING("Unable to drop anything, Leaf Type array and Loot array are different sizes.");
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List metaList){
		for (int i = 0; i < this.treeType.length; ++i){
			metaList.add(new ItemStack(item, 1, i));
		}
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int metaID){    	
		return (metaID & 3) == 1 ? this.leafTextures[this.field_150127_b][1] : this.leafTextures[this.field_150127_b][0];
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIcon){
		for (int i = 0; i < leafType.length; ++i){
			this.leafTextures[i] = new IIcon[leafType[i].length];
			for (int j = 0; j < leafType[i].length; ++j){
				this.leafTextures[i][j] = iIcon.registerIcon(CORE.MODID + ":" + "trees/" + "leaves/" + "leaves_" + leafType[i][j]);
			}
		}
		setVanillaVariable(this.field_150129_M, this.leafTextures);
	}

	@Override
	public String[] func_150125_e()
	{
		return treeType;
	}
}