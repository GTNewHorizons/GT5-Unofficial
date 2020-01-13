package gtPlusPlus.core.block.general.antigrief;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

public class BlockWitherProof extends Block{

	public BlockWitherProof(){
		super(Material.redstoneLight);
		this.setBlockName(Utils.sanitizeString("blockBlackGate"));
		this.setBlockTextureName(CORE.MODID + ":" + "blockFrameGt");
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setHardness(-1F);
		this.setResistance(5000.0F);
		this.setHarvestLevel("pickaxe", 3);
		this.setStepSound(soundTypeMetal);
		//LanguageRegistry.addName(this, "Wither Cage");
		GameRegistry.registerBlock(this, Utils.sanitizeString("blockBlackGate"));
	}

	public String GetProperName(){
		return "Wither Cage";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iIcon){
		this.blockIcon = iIcon.registerIcon(CORE.MODID + ":" + "blockFrameGt");
	}

	@Override
	public void onBlockExploded(final World world, final int x, final int y, final int z, final Explosion explosion){
		//prevent from being destroyed by wither and nukes.
	}

	@Override
	public void onBlockDestroyedByExplosion(final World p_149723_1_, final int p_149723_2_,
			final int p_149723_3_, final int p_149723_4_, final Explosion p_149723_5_) {

	}

	@Override
	public boolean canDropFromExplosion(final Explosion p_149659_1_) {
		return false;
	}

	@Override
	public boolean canEntityDestroy(final IBlockAccess world, final int x, final int y, final int z,
			final Entity entity) {
		if ((entity == null) || !entity.isEntityAlive()){
			return false;
		}
		if ((entity instanceof EntityWither) || (entity instanceof EntityDragon) || (entity instanceof IBossDisplayData)){
			return false;
		}
		else {
			return super.canEntityDestroy(world, x, y, z, entity);
		}
	}


	//Colour Handling
	private static final int mWitherColour = Utils.rgbtoHexValue(32, 32, 32);

	@Override
	public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4){
		return mWitherColour;
	}

	@Override
	public int getRenderColor(final int aMeta) {
		return mWitherColour;
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, World p_149737_2_, int p_149737_3_, int p_149737_4_, int p_149737_5_) {
		if (aPlayer != null && aPlayer instanceof EntityPlayerMP) {
			return 1f;
		}
		return -1f;
	}

	@Override
	public float getExplosionResistance(Entity p_149638_1_) {
		return Float.MAX_VALUE;
	}

	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_) {
		super.onBlockClicked(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_, p_149699_5_);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {		
		if ((entity == null) || !entity.isEntityAlive()){
			return;
		}
		if ((entity instanceof EntityWither) || (entity instanceof EntityDragon) || (entity instanceof IBossDisplayData)){
			return;
		}
		else {
			super.onEntityCollidedWithBlock(world, x, y, z, entity);
		}
	}

	@Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {
		super.harvestBlock(	p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_);
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		if (player != null && player instanceof EntityPlayerMP) {
			return true;
		}
		return super.canHarvestBlock(player, meta);
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		return Float.MAX_VALUE;
	}



}
