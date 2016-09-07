package gtPlusPlus.core.recipe;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class ShapedRecipeObject {

	public Object object_A;
	public Object object_B;
	public Object object_C;
	public Object object_D;
	public Object object_E;
	public Object object_F;
	public Object object_G;
	public Object object_H;
	public Object object_I;
	public ItemStack object_OUTPUT;
	
	public ShapedRecipeObject(
			Object input_A,Object input_B,Object input_C,
			Object input_D,Object input_E,Object input_F,
			Object input_G,Object input_H,Object input_I,
			ItemStack input_Output){
		this.object_A = input_A;
		this.object_B = input_B;
		this.object_C = input_C;
		this.object_D = input_D;
		this.object_E = input_E;
		this.object_F = input_F;
		this.object_G = input_G;
		this.object_H = input_H;
		this.object_I = input_I;
		this.object_OUTPUT = input_Output;
		Utils.LOG_SPECIFIC_WARNING("ShapedRecipeObject", "New object created.", 36);
	}
	
	public void buildRecipe(){
		UtilsRecipe.recipeBuilder(object_A, object_B, object_C, object_D, object_E, object_F, object_G, object_H, object_I, object_OUTPUT);
	}
	
}
