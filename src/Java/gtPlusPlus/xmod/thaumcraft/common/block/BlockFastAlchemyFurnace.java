package gtPlusPlus.xmod.thaumcraft.common.block;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.xmod.thaumcraft.common.tile.TileFastAlchemyFurnace;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockAlchemyFurnace;

public class BlockFastAlchemyFurnace extends BlockAlchemyFurnace {

	public BlockFastAlchemyFurnace() {
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setBlockName("blockFastAlchemyFurnace");
		GameRegistry.registerBlock(this, "blockFastAlchemyFurnace");
		LanguageRegistry.addName(this, "Upgraded Alchemical Furnace");
	}

	public int func_149692_a(int metadata) {
		if ((metadata == 1) || (metadata == 4))
			return 3;
		if (metadata == 3)
			return 9;
		if (metadata == 2)
			return 1;
		return 0;
	}

	public TileEntity createTileEntity(World world, int metadata) {
		if (metadata == 0){
			return new TileFastAlchemyFurnace();
		}
		return null;
	}

	public int getLightValue(IBlockAccess world, int x, int y, int z){
		Block block = world.getBlock(x, y, z);
        if (block != this)
        {
            return block.getLightValue(world, x, y, z);
        }
        return getLightValue();
	}

	/*public int func_149736_g(World world, int x, int y, int z, int rs) {

	}*/

	public TileEntity func_149915_a(World var1, int md) {
		return null;
	}

	/*	public void func_149749_a(World world, int x, int y, int z, Block bl, int md) {

	}*/
	
	@SideOnly(Side.CLIENT)
	public void func_149734_b(final World world, final int x, final int y, final int z, final Random rand) {
		/*final int meta = world.func_72805_g(x, y, z);
		if (meta == 0) {
			final TileAlchemyFurnaceAdvanced tile = (TileAlchemyFurnaceAdvanced) world.func_147438_o(x, y, z);
			if (tile != null && tile.vis > 0) {
				final FXSlimyBubble ef = new FXSlimyBubble(world, (double) (x + rand.nextFloat()), (double) (y + 1),
						(double) (z + rand.nextFloat()), 0.06f + rand.nextFloat() * 0.06f);
				ef.func_82338_g(0.8f);
				ef.func_70538_b(0.6f - rand.nextFloat() * 0.2f, 0.0f, 0.6f + rand.nextFloat() * 0.2f);
				ParticleEngine.instance.addEffect(world, (EntityFX) ef);
				if (rand.nextInt(50) == 0) {
					final double var21 = x + rand.nextFloat();
					final double var22 = y + this.field_149756_F;
					final double var23 = z + rand.nextFloat();
					world.func_72980_b(var21, var22, var23, "liquid.lavapop", 0.1f + rand.nextFloat() * 0.1f,
							0.9f + rand.nextFloat() * 0.15f, false);
				}
				final int q = rand.nextInt(2);
				final int w = rand.nextInt(2);
				final FXSlimyBubble ef2 = new FXSlimyBubble(world, x - 0.6 + rand.nextFloat() * 0.2 + q * 2,
						(double) (y + 2), z - 0.6 + rand.nextFloat() * 0.2 + w * 2, 0.06f + rand.nextFloat() * 0.06f);
				ef2.func_82338_g(0.8f);
				ef2.func_70538_b(0.6f - rand.nextFloat() * 0.2f, 0.0f, 0.6f + rand.nextFloat() * 0.2f);
				ParticleEngine.instance.addEffect(world, (EntityFX) ef2);
			}
		}
		super.func_149734_b(world, x, y, z, rand);*/
	}

}