package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.core.block.general.BlockTankXpConverter;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class ItemBlockEntityBase extends ItemBlock {

	public ItemBlockEntityBase(final Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	@Override
	public int getColorFromItemStack(final ItemStack p_82790_1_, final int p_82790_2_) {
		return super.getColorFromItemStack(p_82790_1_, p_82790_2_);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (Block.getBlockFromItem(stack.getItem()) instanceof BlockTankXpConverter){
			list.add(EnumChatFormatting.GRAY+"Can convert Liquid Xp to Mob Essence and back.");
			list.add(EnumChatFormatting.GRAY+"Right click with a Screwdriver to change mode.");
		}
		else if (Block.getBlockFromItem(stack.getItem()) instanceof BlockTankXpConverter){
			//list.add(EnumChatFormatting.GRAY+"A pile of " + materialName + " dust.");
		}
		super.addInformation(stack, aPlayer, list, bool);
	}


}