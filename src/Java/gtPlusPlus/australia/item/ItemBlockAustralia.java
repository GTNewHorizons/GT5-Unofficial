package gtPlusPlus.australia.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gtPlusPlus.api.interfaces.ITileTooltip;

public class ItemBlockAustralia extends ItemBlock {
	
	protected final int mID;
	
	public ItemBlockAustralia(final Block block) {
		super(block);
		this.mID = ((ITileTooltip) block).getTooltipID();
	}	

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.mID == 0){ //blockDarkWorldPortalFrame
			list.add("Use this to access Australia.");
			list.add("Assembled in the same shape as the Nether Portal.");			
		}
		else if (this.mID == 1){ //blockDarkWorldPortal
			list.add("Place this if you are lazy to create the portal structure, slacker.");			
		}
		else if (this.mID == 2){ //blockDarkWorldGround
			list.add("Pure Australian Outback.");			
		}
		else if (this.mID == 3){ //blockDarkWorldPollutedDirt
			list.add("Maybe you can do something with this?.");			
		}
	}
	

}
