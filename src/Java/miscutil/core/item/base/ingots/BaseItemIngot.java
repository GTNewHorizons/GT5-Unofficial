package miscutil.core.item.base.ingots;

import gregtech.api.util.GT_OreDictUnificator;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemIngot extends Item{

	protected int colour;
	protected String materialName;
	protected String unlocalName;

	public BaseItemIngot(String unlocalizedName, String materialName, int colour) {
		setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.unlocalName = unlocalizedName;
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemIngot");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
		String temp = "";
		if (unlocalName.contains("itemIngot")){
			temp = unlocalName.replace("itemI", "i");
		}
		else if (unlocalName.contains("itemHotIngot")){
			temp = unlocalName.replace("itemHotIngot", "ingotHot");
		}
		if (temp != null && temp != ""){
			GT_OreDictUnificator.registerOre(temp, UtilsItems.getSimpleStack(this));
		}		
		//addBendingRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Ingot");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("") && !unlocalName.contains("HotIngot")){
			list.add(EnumChatFormatting.GRAY+"A solid ingot of " + materialName + ".");		
		}
		else if (materialName != null && materialName != "" && !materialName.equals("") && unlocalName.toLowerCase().contains("ingothot")){
			list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.RED+"Very hot! "+EnumChatFormatting.GRAY+" Avoid direct handling..");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return Utils.generateSingularRandomHexValue();
		}
		return colour;

	}

}
