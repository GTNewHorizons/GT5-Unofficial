package gtPlusPlus.core.item.base.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import gtPlusPlus.core.block.base.BlockBaseModular;

public class ItemBlockGtFrameBox extends ItemBlock{

	protected int blockColour;

	public ItemBlockGtFrameBox(final Block block) {
		super(block);
		final BlockBaseModular baseBlock = (BlockBaseModular) block;
		this.blockColour = baseBlock.getRenderColor(1);
		//GT_OreDictUnificator.registerOre("frameGt"+block.getUnlocalizedName().replace("tile.", "").replace("tile.BlockGtFrame", "").replace("-", "").replace("_", "").replace(" ", "").replace("FrameBox", ""), ItemUtils.getSimpleStack(this));
	}

	public int getRenderColor(final int aMeta) {
		return this.blockColour;
	}

}
