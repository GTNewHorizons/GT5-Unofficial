package gtPlusPlus.core.item.base.itemblock;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockRoundRobinator extends ItemBlockWithMetadata
{
	private final Block mBlock;

	public ItemBlockRoundRobinator(final Block aBlock){
		super(aBlock, aBlock);
		this.mBlock = aBlock;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("Attempts to output items evenly on all four horizontal planes");		
		if (stack.getItemDamage() == 0) {
			list.add("1 Item per enabled side every 400 ticks");			
		}
		else if (stack.getItemDamage() == 1) {
			list.add("1 Item per enabled side every 100 ticks");			
		}
		else if (stack.getItemDamage() == 2) {
			list.add("1 Item per enabled side every 20 ticks");			
		}
		else if (stack.getItemDamage() == 3) {
			list.add("1 Item per enabled side every 10 ticks");			
		}
		else if (stack.getItemDamage() == 4) {
			list.add("1 Item per enabled side every tick");			
		}		
		list.add("Top and bottom do not pull, so you must push items in");
		list.add("Sides can also be disabled with a screwdriver");	
		list.add("Shift+RMB with empty hand to view inventory contents");	
		super.addInformation(stack, aPlayer, list, bool);		
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(final int p_77617_1_)
	{
		return this.mBlock.getIcon(0, p_77617_1_);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(final int p_77647_1_)
	{
		return p_77647_1_;
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		return this.getUnlocalizedName() + "." + stack.getItemDamage();
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 0;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 0;
	}
}