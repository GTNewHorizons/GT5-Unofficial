package gtPlusPlus.core.item.base.itemblock;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockGtFrameBox extends ItemBlock{

	protected int blockColour;
	
	public ItemBlockGtFrameBox(Block block) {
		super(block);
		BlockBaseModular baseBlock = (BlockBaseModular) block;
		this.blockColour = baseBlock.getRenderColor(1);
		GT_OreDictUnificator.registerOre("frameGt"+block.getUnlocalizedName().replace("tile.", "").replace("tile.BlockGtFrame", "").replace("-", "").replace("_", "").replace(" ", "").replace("FrameBox", ""), ItemUtils.getSimpleStack(this));
	}

    public int getRenderColor(int aMeta) {
        return blockColour;
    }

}
