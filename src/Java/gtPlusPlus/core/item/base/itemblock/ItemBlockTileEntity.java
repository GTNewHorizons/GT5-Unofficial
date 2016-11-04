package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockTileEntity extends ItemBlock {

	String[] description;

	public ItemBlockTileEntity(final Block block) {
		super(block);
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		for (int i = 0; i < this.description.length; i++) {
			if (!this.description[i].equals("")) {
				list.add(this.description[i]);
			}
		}

		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
			final boolean p_77663_5_) {

	}

	public void setDecription(final String[] description) {
		for (int i = 0; i < description.length; i++) {
			this.description[i] = description[i];
		}
	}

}
