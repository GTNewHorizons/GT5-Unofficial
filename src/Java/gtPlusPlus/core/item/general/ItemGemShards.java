package gtPlusPlus.core.item.general;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.core.item.base.BaseItemColourable;
import gtPlusPlus.core.util.Utils;

public class ItemGemShards extends BaseItemColourable{

	public ItemGemShards(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg,
			String description, EnumRarity regRarity, EnumChatFormatting colour, boolean Effect, int rgb) {
		super(unlocalizedName, creativeTab, stackSize, maxDmg, description, regRarity, colour, Effect, rgb);
	}
	
	public ItemGemShards(String unlocalizedName, String displayName, CreativeTabs creativeTab, int stackSize,
			int maxDmg, String description, EnumRarity regRarity, EnumChatFormatting colour, boolean Effect, int rgb) {
		super(unlocalizedName, displayName, creativeTab, stackSize, maxDmg, description, regRarity, colour, Effect, rgb);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 4; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	//0 - Diamond
	//1 - Emerald
	//2 - Ruby
	//3 - Sapphire
	
	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		String suffix = " Shards";
		String gemType = "";
		if (tItem.getItemDamage() == 0){
			gemType = "Diamond";
		}
		else if (tItem.getItemDamage() == 1){
			gemType = "Emerald";
		}
		else if (tItem.getItemDamage() == 2){
			gemType = "Ruby";
		}
		else if (tItem.getItemDamage() == 3){
			gemType = "Sapphire";
		}
		return (gemType+suffix);
		
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (this.getDamage(stack)==0){
			return Utils.rgbtoHexValue(150, 150, 220);
		}
		else if (this.getDamage(stack)==1){
			return Utils.rgbtoHexValue(75, 182, 75);
		}
		else if (this.getDamage(stack)==2){
			return Utils.rgbtoHexValue(182, 77, 77);
		}
		else {
			return Utils.rgbtoHexValue(77, 75, 182);			
		}
	}
	
	

	

}
