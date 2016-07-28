package miscutil.core.item.base.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockGtFrameBox extends ItemBlock{

	protected int blockColour;
	
	public ItemBlockGtFrameBox(Block block) {
		super(block);
		this.blockColour = block.getBlockColor();
       // this.setCreativeTab(AddToCreativeTab.tabBlock);
	}

    public int getRenderColor(int aMeta) {
        return blockColour;
    }

}
