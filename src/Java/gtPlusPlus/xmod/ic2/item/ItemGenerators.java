package gtPlusPlus.xmod.ic2.item;

import java.util.List;

import ic2.core.item.block.ItemBlockIC2;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemGenerators extends ItemBlockIC2 {
	public ItemGenerators(final Block block) {
		super(block);

		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List info, final boolean b) {
		final int meta = itemStack.getItemDamage();
		switch (meta) {
			case 0:
				info.add(StatCollector.translateToLocal("ic2.item.tooltip.PowerOutput") + " 1-32 EU/t "
						+ StatCollector.translateToLocal("ic2.item.tooltip.max"));
				break;
			case 1:
				info.add(StatCollector.translateToLocal("ic2.item.tooltip.PowerOutput") + " 1-512 EU/t "
						+ StatCollector.translateToLocal("ic2.item.tooltip.max"));
		}
	}

	@Override
	public int getMetadata(final int i) {
		return i;
	}

	@Override
	public String getUnlocalizedName(final ItemStack itemstack) {
		final int meta = itemstack.getItemDamage();
		switch (meta) {
			case 0:
				return "ic2.blockRTGenerator2";
			case 1:
				return "ic2.blockKineticGenerator2";
		}
		return null;
	}
}
