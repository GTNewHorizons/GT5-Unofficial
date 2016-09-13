package gtPlusPlus.core.material;

import gtPlusPlus.core.util.item.UtilsItems;
import net.minecraft.item.ItemStack;

public class MaterialStack {
	
	final Material materialInput;
	final double percentageToUse;
	
	public MaterialStack(Material inputs, int percentage){

		this.materialInput = inputs;
		this.percentageToUse = percentage;
		
		
	}
	
	public ItemStack getDustStack(){
		int caseStatus = 0;
		int amount = 0;
		if (percentageToUse >= 0 && percentageToUse <= 0.99){
			caseStatus = 1;
			amount = Integer.valueOf(String.valueOf(percentageToUse).charAt(2));
		}
		else if (percentageToUse >= 1 && percentageToUse <= 9.99){
			caseStatus = 2;
			amount = Integer.valueOf(String.valueOf(percentageToUse).charAt(0));
		}
		else if (percentageToUse >= 10 && percentageToUse <= 99.99){
			caseStatus = 3;
			amount = Integer.valueOf(String.valueOf(percentageToUse).charAt(0));
		}
		else if (percentageToUse == 100){
			caseStatus = 4;
			amount = 10;
		}
		switch (caseStatus) {		
		case 1:	{
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+materialInput.unlocalizedName, amount);
		}
		case 2: {
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+materialInput.unlocalizedName, amount);
		}
		case 3: {
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dust"+materialInput.unlocalizedName, amount);
		}
		case 4: {
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dust"+materialInput.unlocalizedName, amount);
		}
		default:
			return null;
		}
		
	}
	
	public ItemStack[] getValidItemStacks(){
		return UtilsItems.validItemsForOreDict(materialInput.unlocalizedName);
	}
	
	
	
	
	

}
