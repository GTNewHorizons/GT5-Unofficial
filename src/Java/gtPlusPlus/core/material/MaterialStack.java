package gtPlusPlus.core.material;

import gtPlusPlus.core.util.item.UtilsItems;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.item.ItemStack;

public class MaterialStack {

	final int[] vAmount;
	final Material stackMaterial;
	final double percentageToUse;

	public MaterialStack(Material inputs, double partOutOf100){

		this.stackMaterial = inputs;
		this.percentageToUse = partOutOf100;
		this.vAmount = math(partOutOf100);


	}

	private int[] math(double val){	
		//Cast to a BigDecimal to round it.
		BigDecimal bd = new BigDecimal(val).setScale(2, RoundingMode.HALF_EVEN);
		val = bd.doubleValue();
		//Split the string into xx.xx
		String[] arr=String.valueOf(val).split("\\.");
		int[] intArr=new int[2];
		intArr[0]=Integer.parseInt(arr[0]);
		intArr[1]=Integer.parseInt(arr[1]);
		return intArr;
	}

	public ItemStack getDustStack(){
		return this.stackMaterial.getDust(this.vAmount[0]);
	}
	public int getPartsPerOneHundred(){
		if (this.vAmount != null){
			if (this.vAmount[0] >= 1 && this.vAmount[0] <= 100){
				return this.vAmount[0];
			}
		}
		return 100;
	}
	public ItemStack getLeftOverStacksFromDecimalValue(){
		int temp = this.vAmount[1];
		int getCount;
		if (temp >= 25 && temp <=99){
			getCount = temp/25;
			return this.stackMaterial.getSmallDust(getCount);
		}
		else if (temp >= 11 && temp <= 24){
			getCount = temp/11;
			return this.stackMaterial.getTinyDust(getCount);
		}
		else {
			return null;
		}		
	}

	/*public ItemStack getDustStack(){
		int caseStatus = 0;
		int amount = 0;
		if (percentageToUse >= 0 && percentageToUse <= 0.99){
			caseStatus = 1;
			amount = (int) (1/percentageToUse);
			//amount = Integer.valueOf(String.valueOf(percentageToUse).charAt(2));
		}
		else if (percentageToUse >= 1 && percentageToUse <= 9.99){
			caseStatus = 2;
			amount = (int) (percentageToUse);
			//amount = Integer.valueOf(String.valueOf(percentageToUse).charAt(0));
		}
		else if (percentageToUse >= 10 && percentageToUse <= 99.99){
			caseStatus = 3;
			amount = (int) (percentageToUse/10);
			//amount = Integer.valueOf(String.valueOf(percentageToUse).charAt(0));
		}
		else if (percentageToUse == 100){
			caseStatus = 4;
			amount = 10;
		}
		else {
			amount = 0;
		}
		switch (caseStatus) {		
		case 1:	{
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+stackMaterial.unlocalizedName, amount);
		}
		case 2: {
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+stackMaterial.unlocalizedName, amount);
		}
		case 3: {
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dust"+stackMaterial.unlocalizedName, amount);
		}
		case 4: {
			return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dust"+stackMaterial.unlocalizedName, amount);
		}
		default:
			return null;
		}

	}*/

	/*public int getDustCount(){
		int amount = 0;

		//No Dust
		if (percentageToUse >= 0 && percentageToUse <= 11.1111111111111111111111110){
			//amount = (int) (1/percentageToUse); 
			amount = 0; //Less than a tiny dust.
		}

		//Tiny Dust
		else if (percentageToUse >= 11.1111111111111111111111111 && percentageToUse <= 25){
			amount = (int) (percentageToUse);
		}

		//Small Dust
		else if (percentageToUse >= 10 && percentageToUse <= 99.99){
			amount = (int) (percentageToUse/10);
		}

		//Dust
		else if (percentageToUse == 100){
			amount = 10;
		}

		//Error - Nothing
		else {
			amount = 0;
		}
		return amount;
	}*/

	public ItemStack[] getValidItemStacks(){
		return UtilsItems.validItemsForOreDict(stackMaterial.unlocalizedName);
	}






}
