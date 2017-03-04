package gtPlusPlus.core.block.general;

import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.block.base.BasicBlock;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.tileentities.general.TileEntityFirepit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FirePit extends BasicBlock{
	private static IIcon[] TEXTURE;
	public static final int META_ANTIBUILDER = 2;
	private int meta;

	@SuppressWarnings("deprecation")
	public FirePit() {
		super("blockFirePit", Material.wood);
		this.setBlockName("blockFirePit");
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setHardness(10.0F);
		this.setResistance(35.0F);
		this.setStepSound(Block.soundTypeWood);
		GameRegistry.registerBlock(this, "blockFirePit");
		LanguageRegistry.addName(this, "Fire Pit");
	}

	@Override
	public int tickRate(final World aParWorld)    {
		return 30;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int i) {
		return new TileEntityFirepit();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister par1IconRegister){
		TEXTURE = new IIcon[] {par1IconRegister.registerIcon(this.getTextureName() + "_layer_0"), par1IconRegister.registerIcon(this.getTextureName() + "_layer_1")};
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List){
		par3List.add(new ItemStack(par1, 1, 2));
	}

	@Override
	public void updateTick(final World par1World, final int x, final int y, final int z, final Random par5Random){
		if (!par1World.isRemote){
			//Sets meta.
			this.meta = par1World.getBlockMetadata(x, y, z);
			//If Raining, Put out.
			if (par1World.isRaining()
					&& (par1World.canLightningStrikeAt(x, y, z)
							|| par1World.canLightningStrikeAt(x - 1, y, z)
							|| par1World.canLightningStrikeAt(x + 1, y, z)
							|| par1World.canLightningStrikeAt(x, y, z - 1)
							|| par1World.canLightningStrikeAt(x, y, z + 1))){
				//Fire goes out
				par1World.setBlockMetadataWithNotify(x, y, z, 1, 4);
			}
			if (isNeighborBurning(par1World, x, y, z)){
				//Fire can ignite from a nearby flame source.
				par1World.setBlockMetadataWithNotify(x, y, z, 2, 4);
			}
		}
	}

	@Override
	public Item getItemDropped(final int meta, final Random par2Random, final int par3){
		switch (meta){
		case 0:
			return null;
		default:
			break;
		}
		return Item.getItemFromBlock(this);
	}

	@Override
	public int damageDropped(final int meta){
		return meta;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public int getRenderType(){
		return -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	/*@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World aParWorld, int x, int y, int z){
        return null;
    }*/

	@Override
	protected boolean canSilkHarvest(){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World p_149734_1_, final int p_149734_2_, final int p_149734_3_, final int p_149734_4_, final Random p_149734_5_){
		int l;
		float f;
		float f1;
		float f2;
		if (this.meta == 2) {
			if (p_149734_5_.nextInt(24) == 0){
				p_149734_1_.playSound(p_149734_2_ + 0.5F, p_149734_3_ + 0.5F, p_149734_4_ + 0.5F, "fire.fire", 1.0F + p_149734_5_.nextFloat(), (p_149734_5_.nextFloat() * 0.7F) + 0.3F, false);
			}
		}
		if (this.meta == 2) {
			if (!World.doesBlockHaveSolidTopSurface(p_149734_1_, p_149734_2_, p_149734_3_ - 1, p_149734_4_) && !Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_ - 1, p_149734_4_, UP)){
				if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_ - 1, p_149734_3_, p_149734_4_, EAST)){
					for (l = 0; l < 2; ++l){
						f = p_149734_2_ + (p_149734_5_.nextFloat() * 0.1F);
						f1 = p_149734_3_ + p_149734_5_.nextFloat();
						f2 = p_149734_4_ + p_149734_5_.nextFloat();
						p_149734_1_.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
					}
				}
				if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_ + 1, p_149734_3_, p_149734_4_, WEST)){
					for (l = 0; l < 2; ++l){
						f = p_149734_2_ + 1 - (p_149734_5_.nextFloat() * 0.1F);
						f1 = p_149734_3_ + p_149734_5_.nextFloat();
						f2 = p_149734_4_ + p_149734_5_.nextFloat();
						p_149734_1_.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
					}
				}
				if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_ - 1, SOUTH)){
					for (l = 0; l < 2; ++l){
						f = p_149734_2_ + p_149734_5_.nextFloat();
						f1 = p_149734_3_ + p_149734_5_.nextFloat();
						f2 = p_149734_4_ + (p_149734_5_.nextFloat() * 0.1F);
						p_149734_1_.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
					}
				}
				if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_ + 1, NORTH)){
					for (l = 0; l < 2; ++l){
						f = p_149734_2_ + p_149734_5_.nextFloat();
						f1 = p_149734_3_ + p_149734_5_.nextFloat();
						f2 = p_149734_4_ + 1 - (p_149734_5_.nextFloat() * 0.1F);
						p_149734_1_.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
					}
				}
				if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_ + 1, p_149734_4_, DOWN)){
					for (l = 0; l < 2; ++l){
						f = p_149734_2_ + p_149734_5_.nextFloat();
						f1 = p_149734_3_ + 1 - (p_149734_5_.nextFloat() * 0.1F);
						f2 = p_149734_4_ + p_149734_5_.nextFloat();
						p_149734_1_.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
					}
				}
			}
			else{
				if (this.meta == 2) {
					for (l = 0; l < 3; ++l){
						f = p_149734_2_ + p_149734_5_.nextFloat();
						f1 = p_149734_3_ + (p_149734_5_.nextFloat() * 0.5F) + 0.5F;
						f2 = p_149734_4_ + p_149734_5_.nextFloat();
						p_149734_1_.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static IIcon getFireIcon(final int p_149840_1_){
		return FirePit.TEXTURE[p_149840_1_];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_){
		return FirePit.TEXTURE[0];
	}

	private static boolean isNeighborBurning(final World world, final int x, final int y, final int z){
		return canCatchFire(world, x + 1, y, z, WEST ) ||
				canCatchFire(world, x - 1, y, z, EAST ) ||
				canCatchFire(world, x, y - 1, z, UP   ) ||
				canCatchFire(world, x, y + 1, z, DOWN ) ||
				canCatchFire(world, x, y, z - 1, SOUTH) ||
				canCatchFire(world, x, y, z + 1, NORTH);
	}

	public static boolean canCatchFire(final World world, final int x, final int y, final int z, final ForgeDirection face)
	{
		return world.getBlock(x, y, z).isFireSource(world, x, y, z, face);
	}

}
