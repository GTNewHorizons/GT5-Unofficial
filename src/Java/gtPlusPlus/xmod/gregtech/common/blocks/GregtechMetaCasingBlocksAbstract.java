package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gtPlusPlus.core.creative.AddToCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class GregtechMetaCasingBlocksAbstract extends GT_Block_Casings_Abstract {
	public GregtechMetaCasingBlocksAbstract(final Class<? extends ItemBlock> aItemClass, final String aName,
			final Material aMaterial) {
		super(aItemClass, aName, aMaterial);
		this.setStepSound(Block.soundTypeMetal);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		GregTech_API.registerMachineBlock(this, -1);
		GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + "." + 32767 + ".name",
				"Any Sub Block of this");
	}

	@Override
	public void breakBlock(final World aWorld, final int aX, final int aY, final int aZ, final Block aBlock,
			final int aMetaData) {
		if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
			GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
		}
	}

	@Override
	public boolean canBeReplacedByLeaves(final IBlockAccess aWorld, final int aX, final int aY, final int aZ) {
		return false;
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
			final int z) {
		return false;
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public int damageDropped(final int par1) {
		return par1;
	}

	@Override
	public float getBlockHardness(final World aWorld, final int aX, final int aY, final int aZ) {
		return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
	}

	@Override
	public int getDamageValue(final World par1World, final int par2, final int par3, final int par4) {
		return par1World.getBlockMetadata(par2, par3, par4);
	}

	@Override
	public float getExplosionResistance(final Entity aTNT) {
		return Blocks.iron_block.getExplosionResistance(aTNT);
	}

	@Override
	public int getHarvestLevel(final int aMeta) {
		return 2;
	}

	@Override
	public String getHarvestTool(final int aMeta) {
		return "wrench";
	}

	@Override
	public Item getItemDropped(final int par1, final Random par2Random, final int par3) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public String getLocalizedName() {
		return StatCollector.translateToLocal(this.mUnlocalizedName + ".name");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item aItem, final CreativeTabs par2CreativeTabs, final List aList) {
		for (int i = 0; i < 16; i++) {
			aList.add(new ItemStack(aItem, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName() {
		return this.mUnlocalizedName;
	}

	@Override
	public boolean isNormalCube(final IBlockAccess aWorld, final int aX, final int aY, final int aZ) {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return true;
	}

	@Override
	public void onBlockAdded(final World aWorld, final int aX, final int aY, final int aZ) {
		if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
			GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
		}
	}

	@Override
	public int quantityDropped(final Random par1Random) {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister aIconRegister) {
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}
}
