package items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
	/*
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
		
		
	}*/
}
