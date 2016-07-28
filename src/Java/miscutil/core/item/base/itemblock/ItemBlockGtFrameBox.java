package miscutil.core.item.base.itemblock;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockGtFrameBox extends ItemBlock{

	protected int blockColour;
	
	public ItemBlockGtFrameBox(Block block) {
		super(block);
		this.blockColour = block.getBlockColor();
		GT_OreDictUnificator.registerOre(block.getUnlocalizedName().replace("BlockGtFrame", "frameGt"), UtilsItems.getSimpleStack(this));
	}

    public int getRenderColor(int aMeta) {
        return blockColour;
    }

}
