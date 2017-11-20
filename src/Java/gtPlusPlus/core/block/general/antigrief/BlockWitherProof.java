package gtPlusPlus.core.block.general.antigrief;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
		LanguageRegistry.addName(this, "Wither Cage");
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

	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion){
		//prevent from being destroyed by wither and nukes.
	}

	@Override
	public void onBlockDestroyedByExplosion(World p_149723_1_, int p_149723_2_,
			int p_149723_3_, int p_149723_4_, Explosion p_149723_5_) {
		
	}

	@Override
	public boolean canDropFromExplosion(Explosion p_149659_1_) {
		return false;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z,
			Entity entity) {
		if (entity == null || !entity.isEntityAlive()){
			return false;
		}		
		if (entity instanceof EntityWither || entity instanceof EntityDragon || entity instanceof IBossDisplayData){
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



}
