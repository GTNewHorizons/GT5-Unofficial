package itemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class IB_TFFTMultiHatch extends ItemBlock {
	
	public IB_TFFTMultiHatch(Block block) {
		super(block);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
		lines.add("Special hatch for the T.F.F.T");
		lines.add("Allows for automated interaction with all stored fluids in a single place");
	}
}
