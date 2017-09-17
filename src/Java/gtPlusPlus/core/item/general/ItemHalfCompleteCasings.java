package gtPlusPlus.core.item.general;

import java.util.List;

import gtPlusPlus.core.item.base.BaseItemColourable;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemHalfCompleteCasings extends BaseItemColourable{

	public ItemHalfCompleteCasings(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg,
			String description, EnumRarity regRarity, EnumChatFormatting colour, boolean Effect, int rgb) {
		super(unlocalizedName, creativeTab, stackSize, maxDmg, description, regRarity, colour, Effect, rgb);
	}
	
	public ItemHalfCompleteCasings(String unlocalizedName, String displayName, CreativeTabs creativeTab, int stackSize,
			int maxDmg, String description, EnumRarity regRarity, EnumChatFormatting colour, boolean Effect, int rgb) {
		super(unlocalizedName, displayName, creativeTab, stackSize, maxDmg, description, regRarity, colour, Effect, rgb);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 4; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		String prefix = "Half Complete Casing ";
		String casingType = "";
		if (tItem.getItemDamage() == 0){
			casingType = "I";
		}
		else if (tItem.getItemDamage() == 1){
			casingType = "II";
		}
		else if (tItem.getItemDamage() == 2){
			casingType = "III";
		}
		else if (tItem.getItemDamage() == 3){
			casingType = "IV";
		}
		return (prefix+casingType);
		
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (this.getDamage(stack)==0){
			return Utils.rgbtoHexValue(52, 52, 52);
		}
		else if (this.getDamage(stack)==1){
			return Utils.rgbtoHexValue(80, 90, 222);
		}
		else if (this.getDamage(stack)==2){
			return Utils.rgbtoHexValue(182, 77, 177);
		}
		else {
			return Utils.rgbtoHexValue(77, 175, 182);			
		}
	}
	
	

	

}
