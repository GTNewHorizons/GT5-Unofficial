package gtPlusPlus.core.material;

import java.math.BigDecimal;
import java.math.RoundingMode;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;

public class MaterialStack {

	private final transient int[] vAmount;
	private final Material stackMaterial;
	private final double vPercentageToUse;

	public MaterialStack(final Material inputs, final double partOutOf100){
		this.stackMaterial = inputs;
		//Logger.INFO("Tried getting MaterialStack for "+inputs.getLocalizedName());
		this.vPercentageToUse = partOutOf100;
		this.vAmount = this.math(partOutOf100);
	}

	@SuppressWarnings("static-method")
	private int[] math(final double val){
		double i;
		//Cast to a BigDecimal to round it.
		final BigDecimal bd = new BigDecimal(val).setScale(2, RoundingMode.HALF_EVEN);
		i = bd.doubleValue();
		//Split the string into xx.xx
		final String[] arr=String.valueOf(i).split("\\.");
		final int[] intArr=new int[2];
		intArr[0]=Integer.parseInt(arr[0]);
		intArr[1]=Integer.parseInt(arr[1]);
		return intArr;
	}

	public ItemStack getValidStack(){
		if (this.stackMaterial.getDust(1) == null){
			//if (this.stackMaterial.getCell(1) == null){
			return null;
			//}
			//return this.stackMaterial.getCell(this.vAmount[0]);
		}
		return this.stackMaterial.getDust(this.vAmount[0]);
	}

	public ItemStack getDustStack(){
		return this.stackMaterial.getDust(this.vAmount[0]);
	}

	public ItemStack getDustStack(final int amount){
		return this.stackMaterial.getDust(amount);
	}

	public Material getStackMaterial(){
		if (this.stackMaterial == null){
			Logger.MATERIALS("Tried getStackMaterial, got an invalid material.");
			Logger.MATERIALS(ReflectionUtils.getMethodName(0));
			Logger.MATERIALS(ReflectionUtils.getMethodName(1));
			Logger.MATERIALS(ReflectionUtils.getMethodName(2));
			Logger.MATERIALS(ReflectionUtils.getMethodName(3));
			return null;
		}
		return this.stackMaterial;
	}

	public double getvPercentageToUse(){
		return this.vPercentageToUse;
	}

	public long[] getSmallestStackSizes(){
		return this.stackMaterial.getSmallestRatio(this.stackMaterial.getComposites());
	}

	public int getPartsPerOneHundred(){
		if (this.vAmount != null){
			if ((this.vAmount[0] >= 1) && (this.vAmount[0] <= 100)){
				return this.vAmount[0];
			}
		}
		return 100;
	}
	public ItemStack getLeftOverStacksFromDecimalValue(){
		final int temp = this.vAmount[1];
		int getCount;
		if ((temp >= 25) && (temp <=99)){
			getCount = temp/25;
			return this.stackMaterial.getSmallDust(getCount);
		}
		else if ((temp >= 11) && (temp <= 24)){
			getCount = temp/11;
			return this.stackMaterial.getTinyDust(getCount);
		}
		else {
			return null;
		}
	}

	public ItemStack[] getValidItemStacks(){
		return ItemUtils.validItemsForOreDict(this.stackMaterial.getUnlocalizedName());
	}






}
