package gtPlusPlus.core.recipe;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.recipe.RecipeUtils;
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
			final Object input_A,final Object input_B,final Object input_C,
			final Object input_D,final Object input_E,final Object input_F,
			final Object input_G,final Object input_H,final Object input_I,
			final ItemStack input_Output){
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
		Logger.SPECIFIC_WARNING("ShapedRecipeObject", "New object created.", 36);
	}

	public void buildRecipe(){
		RecipeUtils.recipeBuilder(this.object_A, this.object_B, this.object_C, this.object_D, this.object_E, this.object_F, this.object_G, this.object_H, this.object_I, this.object_OUTPUT);
	}

}
