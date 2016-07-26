package miscutil.core.item.base.ingots;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

import java.util.List;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.UtilsText;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class BaseItemIngotHot extends BaseItemIngot{
	
	private ItemStack outputIngot;

	public BaseItemIngotHot(String unlocalizedName, String materialName, ItemStack coldIngot) {
		super(unlocalizedName, materialName, Utils.rgbtoHexValue(225, 225, 225));
		this.setTextureName(CORE.MODID + ":" + "itemIngotHot");
		this.outputIngot = coldIngot;
		generateRecipe();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
				list.add(EnumChatFormatting.GRAY+"A "+UtilsText.red.colour()+"burning hot"+UtilsText.lightGray.colour()+" ingot of " + materialName + ".");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		return Utils.rgbtoHexValue(225, 225, 225);
	}
	
	private void generateRecipe(){
	    GT_Values.RA.addVacuumFreezerRecipe(GT_Utility.copyAmount(1L, new Object[]{this}), GT_Utility.copyAmount(1L, new Object[]{outputIngot}), 500);
	}
}
