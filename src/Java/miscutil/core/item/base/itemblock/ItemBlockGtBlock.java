package miscutil.core.item.base.itemblock;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockGtBlock extends ItemBlock{

	protected int blockColour;
	
	public ItemBlockGtBlock(Block block) {
		super(block);
		this.blockColour = block.getBlockColor();
		GT_OreDictUnificator.registerOre("block"+block.getUnlocalizedName().replace("tile.block", "").replace("tile.", "").replace("of", "").replace("Of", "").replace("Block", "").replace("-", "").replace("_", "").replace(" ", ""), UtilsItems.getSimpleStack(this));
	}

    public int getRenderColor(int aMeta) {
        return blockColour;
    }

}
