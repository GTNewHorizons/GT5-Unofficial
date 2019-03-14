package gtPlusPlus.core.item.base.itemblock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockMeta extends ItemBlockWithMetadata
{
	private final Block mBlock;

	public ItemBlockMeta(final Block p_i45326_1_)
	{
		super(p_i45326_1_, p_i45326_1_);
		this.mBlock = p_i45326_1_;
		this.setMaxDamage(15);
		this.setHasSubtypes(true);
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(final int p_77617_1_)
	{
		return this.mBlock.getIcon(2, p_77617_1_);
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
}