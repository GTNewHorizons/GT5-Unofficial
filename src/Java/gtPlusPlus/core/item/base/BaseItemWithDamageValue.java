package gtPlusPlus.core.item.base;

import gtPlusPlus.core.lib.CORE;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class BaseItemWithDamageValue extends Item{
	public BaseItemWithDamageValue(String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
	}
	@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, damage);
	}	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		list.add(EnumChatFormatting.GOLD+"");
		super.addInformation(stack, aPlayer, list, bool);
	}	
}