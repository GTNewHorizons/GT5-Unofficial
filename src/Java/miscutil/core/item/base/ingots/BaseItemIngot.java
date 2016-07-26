package miscutil.core.item.base.ingots;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

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
		if (unlocalizedName.contains("RaisinBread")){
			this.setTextureName(CORE.MODID + ":" + "itemBread");				
		}
		else {
			this.setTextureName(CORE.MODID + ":" + "itemIngot");				
		}	
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
		addBendingRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Ingot");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("") && !unlocalName.contains("HotIngot") && !unlocalName.toLowerCase().contains("raisinbread")){
			list.add(EnumChatFormatting.GRAY+"A solid ingot of " + materialName + ".");		
		}
		else if (materialName != null && materialName != "" && !materialName.equals("") && unlocalName.toLowerCase().contains("ingothot") && !unlocalName.toLowerCase().contains("raisinbread")){
			list.add(EnumChatFormatting.GRAY+"Warning: Very hot! Avoid direct handling..");		
		}
		else if (materialName != null && materialName != "" && !materialName.equals("") && !unlocalName.toLowerCase().contains("ingothot") && unlocalName.toLowerCase().contains("raisinbread")){
			list.add(EnumChatFormatting.GRAY+"We all know that " + materialName + " is delicious!");		
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

	private void addBendingRecipe(){
		if (!unlocalName.toLowerCase().contains("ingothot") && !unlocalName.toLowerCase().contains("raisinbread")){
			GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(1L, new Object[]{this}),
					UtilsItems.getItemStackOfAmountFromOreDict("plate"+materialName, 1),
					1200, 24);
		}
	}

}
