package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import gtPlusPlus.api.interfaces.ITileTooltip;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBasicTooltip extends ItemBlock{

	protected final int mID;
	
	public ItemBlockBasicTooltip(final Block block) {
		super(block);
		this.mID = ((ITileTooltip) block).getTooltipID();
	}	
	

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.mID == 0){ //blockDarkWorldPortalFrame
			list.add("Assembled in the same shape as the Nether Portal.");			
		}
		else if (this.mID == 1){ //Modularity
			list.add("Used to construct modular armour & bauble upgrades..");			
		}
	}
	

}
