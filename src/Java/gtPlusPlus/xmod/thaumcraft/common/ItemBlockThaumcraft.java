package gtPlusPlus.xmod.thaumcraft.common;

import gtPlusPlus.core.item.base.itemblock.ItemBlockEntityBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockThaumcraft extends ItemBlockEntityBase{

	public ItemBlockThaumcraft(final Block block) {
		super(block);
	}

	@Override
	public int getMetadata(final int par1) {
		return par1;
	}

	@Override
	public String getUnlocalizedName(final ItemStack par1ItemStack) {
		return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
	}

	@Override
	public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final int x,
			final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ,
			final int metadata) {
		final boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		return ret;
	}



}
