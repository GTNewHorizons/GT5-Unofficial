package items;

import common.blocks.Block_ThaumiumReinforcedJar;
import common.tileentities.TE_ThaumiumReinforcedJar;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.ItemJarFilled;

public class Item_ThaumiumReinforcedJarFilled extends ItemJarFilled {
	
	private static final Item_ThaumiumReinforcedJarFilled instance = new Item_ThaumiumReinforcedJarFilled();
	
	private Item_ThaumiumReinforcedJarFilled() {
		super();
	}
	
	public static Item_ThaumiumReinforcedJarFilled getInstance() {
		return instance;
	}
	
	public void registerItem() {
		super.setHasSubtypes(false);
		final String unlocalizedName = "kekztech_thaumiumreinforcedjarfilled_item";
		super.setUnlocalizedName(unlocalizedName);
		GameRegistry.registerItem(getInstance(), unlocalizedName);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float par8, float par9, float par10) {
		
		final Block block = world.getBlock(x, y, z);
		if(block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) {
			side = 1;
		} else if(block != Blocks.vine && block != Blocks.tallgrass 
				&& block != Blocks.deadbush && !block.isReplaceable(world, x, y, z)) {
			// Displace target location if original target can't be replaced
			if(side == 0) {
				y--;
			}
			if(side == 1) {
				y++;
			}
			if(side == 2) {
				z--;
			}
			if(side == 3) {
				z++;
			}
			if(side == 4) {
				x--;
			}
			if(side == 5) {
				x++;
			}
		}
		
		if(stack.stackSize == 0) {
			return false;
		} else if(!player.canPlayerEdit(x, y, z, side, stack)) {
			return false;
		} else if(world.canPlaceEntityOnSide(Block_ThaumiumReinforcedJar.getInstance(), x, y, z, 
				false, side, player, stack)) {
			
			final Block jar = Block_ThaumiumReinforcedJar.getInstance();
			final int meta = this.getMetadata(stack.getItemDamage());
			final int idk = block.onBlockPlaced(world, x, y, z, side, par8, par9, par10, meta);
			
			if(placeBlockAt(stack, player, world, x, y, z, side, par9, par9, par10, idk)) {
				
				final TileEntity te = world.getTileEntity(x, y, z);
				if(te != null && te instanceof TE_ThaumiumReinforcedJar && stack.hasTagCompound()) {
					
					final AspectList aspects = getAspects(stack);
					if(aspects != null && aspects.size() == 1) {
						((TE_ThaumiumReinforcedJar) te).amount = aspects.getAmount(aspects.getAspects()[0]);
						((TE_ThaumiumReinforcedJar) te).aspect = aspects.getAspects()[0];
					}
					
					final String filter = stack.stackTagCompound.getString("AspectFilter");
					if(filter != null) {
						((TE_ThaumiumReinforcedJar) te).aspectFilter = Aspect.getAspect(filter);
					}
				}
				
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F),
						(double) ((float) z + 0.5F), jar.stepSound.func_150496_b(),
						(jar.stepSound.getVolume() + 1.0F) / 2.0F, jar.stepSound.getPitch() * 0.8F);
				--stack.stackSize;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata) {
		if (!world.setBlock(x, y, z, Block_ThaumiumReinforcedJar.getInstance(), metadata, 3)) {
			return false;
		} else {
			if (world.getBlock(x, y, z) == Block_ThaumiumReinforcedJar.getInstance()) {
				Block_ThaumiumReinforcedJar.getInstance().onBlockPlacedBy(world, x, y, z, player, stack);
				Block_ThaumiumReinforcedJar.getInstance().onPostBlockPlaced(world, x, y, z, metadata);
			}

			return true;
		}
	}
}
