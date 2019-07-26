package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gtPlusPlus.api.interfaces.ITileTooltip;

public class ItemBlockBasicTile extends ItemBlock {

	private final int mID;

	public ItemBlockBasicTile(final Block block) {
		super(block);
		this.mID = ((ITileTooltip) block).getTooltipID();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.mID == 0) { // Fish trap
			list.add("This trap catches fish faster if surrounded by more water blocks");
			list.add("Can also be placed beside upto 4 other fish traps");
			list.add("Requires at least two faces touching water");
			list.add("1/1000 chance to produce triple loot.");
		} else if (this.mID == 1) { // Modularity
			list.add("Used to construct modular armour & bauble upgrades..");
		} else if (this.mID == 2) { // Trade
			list.add("Allows for SMP trade-o-mat type trading.");
		} else if (this.mID == 3) { // Project
			list.add("Scan any crafting recipe in this to mass fabricate them in the Autocrafter..");
		} else if (this.mID == 4) { // Circuit Table
			list.add("Easy Circuit Configuration");
			list.add("Change default setting with a Screwdriver");
			list.add("Default is used to select slot for auto-insertion");
		} else if (this.mID == 5) { // Decayables Chest
			list.add("Chest which holds radioactive materials");
			list.add("Items which decay will tick while inside");
			list.add("Place with right click");
		} else if (this.mID == 6) { // Butterfly Killer
			list.add("Kills Forestry Butterflies, Bats and other pests");
			list.add("Use either Formaldehyde or Hydrogen cyanide");
			list.add("Be weary of your neighbours");
		} else {
			list.add("Bad Tooltip ID - " + mID);

		}
		super.addInformation(stack, aPlayer, list, bool);
	}

}
