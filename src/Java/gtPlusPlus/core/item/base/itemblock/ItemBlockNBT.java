package gtPlusPlus.core.item.base.itemblock;

import gtPlusPlus.core.util.nbt.NBTUtils;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockNBT extends ItemBlock {

	public ItemBlockNBT(final Block block, final CreativeTabs tab) {
		super(block);
		this.setCreativeTab(tab);
	}

	@Override
	public void onCreated(ItemStack item, World world, EntityPlayer player) {
		if (player != null){
			NBTUtils.setString(item, "mOwner", player.getDisplayName());
			NBTUtils.setString(item, "mUUID", ""+player.getUniqueID());
			boolean mOP = PlayerUtils.isPlayerOP(player);
			NBTUtils.setBoolean(item, "mOP", mOP);
		}		
		super.onCreated(item, world, player);
	}


}