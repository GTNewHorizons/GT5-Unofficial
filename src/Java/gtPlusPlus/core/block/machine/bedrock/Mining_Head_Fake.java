package gtPlusPlus.core.block.machine.bedrock;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;

public class Mining_Head_Fake extends Block{

	public Mining_Head_Fake(){
		super(Material.lava);
		this.setBlockName(Utils.sanitizeString("blockMiningHeadFake"));
		this.setBlockTextureName(CORE.MODID + ":" + "blockFrameGt");
		this.setCreativeTab(AddToCreativeTab.tabBlock);
		this.setHardness(-1F);
		this.setResistance(50000.0F);
		this.setHarvestLevel("pickaxe", 10);
		this.setStepSound(soundTypeMetal);
		//LanguageRegistry.addName(this, "Wither Cage");
		GameRegistry.registerBlock(this, Utils.sanitizeString("blockMiningHeadFake"));
	}

	public String GetProperName(){
		return "Hardened Mining Head";
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
		return false;
	}


	//Colour Handling
	private static final int mWitherColour = Utils.rgbtoHexValue(175, 64, 32);

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
	public boolean isCollidable() {
		return true;
	}

	@Override
	public void randomDisplayTick(World world, int posX, int posY, int posZ,
			Random rand) {
		generateVoidParticlesAroundBlockPos(new BlockPos(posX, posY, posZ, world), 2);
		super.randomDisplayTick(world, posX, posY, posZ, rand);
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_,
			Entity ent) {
		EntityUtils.doDamage(ent, DamageSource.outOfWorld, 20);
		EntityUtils.setEntityOnFire(ent, 100);
		super.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, ent);
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		return false;
	}

	public static void generateVoidParticlesAroundBlockPos(BlockPos Pos, int range){
		for (BlockPos G : Pos.getSurroundingBlocks()) {
			int i1 = G.xPos + MathUtils.randInt(0, range) - MathUtils.randInt(0, range);
			int j1 = G.yPos + MathUtils.randInt(0, range) - MathUtils.randInt(0, range);
			int k1 = G.zPos + MathUtils.randInt(0, range) - MathUtils.randInt(0, range);
			
			Block block = Pos.world.getBlock(i1, j1, k1);			
			
			if (block.getMaterial() == Material.air){
					
					//lava
					if (Pos.yPos <= 7) {
						Pos.world.spawnParticle("portal", (double)((float)i1 + CORE.RANDOM.nextFloat()), (double)((float)j1 + CORE.RANDOM.nextFloat()), (double)((float)k1 + CORE.RANDOM.nextFloat()), 0.0D, 0.0D, 0.0D);
						Pos.world.spawnParticle("lava", (double)((float)i1 + CORE.RANDOM.nextFloat()), (double)((float)j1 + CORE.RANDOM.nextFloat()), (double)((float)k1 + CORE.RANDOM.nextFloat()), 0.0D, 0.0D, 0.0D);
					}
					else {
						Pos.world.spawnParticle("portal", (double)((float)i1 + CORE.RANDOM.nextFloat()), (double)((float)j1 + CORE.RANDOM.nextFloat()), (double)((float)k1 + CORE.RANDOM.nextFloat()), 0.0D, 0.0D, 0.0D);	
					}					
				
			}
		}
	}


}
