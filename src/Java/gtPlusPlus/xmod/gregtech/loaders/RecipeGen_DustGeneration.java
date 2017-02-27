package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.material.*;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_DustGeneration  implements Runnable{

	final Material toGenerate;

	public RecipeGen_DustGeneration(final Material M){
		this.toGenerate = M;
	}

	@Override
	public void run() {
		generateRecipes(toGenerate);		
	}

	public static void generateRecipes(final Material material){
		generateRecipes(material, false);
	}

	public static void generateRecipes(final Material material, boolean disableOptional){
		final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 60 : 15;

		Utils.LOG_WARNING("Generating Shaped Crafting recipes for "+material.getLocalizedName()); //TODO
		//Ring Recipe

		if (RecipeUtils.addShapedGregtechRecipe(
				"craftingToolWrench", null, null,
				null, material.getRod(1), null,
				null, null, null,
				material.getRing(1))){
			Utils.LOG_WARNING("Ring Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Ring Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		final ItemStack normalDust = material.getDust(1);
		final ItemStack smallDust = material.getSmallDust(1);
		final ItemStack tinyDust = material.getTinyDust(1);

		final ItemStack[] inputStacks = material.getMaterialComposites();
		final ItemStack outputStacks = material.getDust(material.smallestStackSizeWhenProcessing);

		if (RecipeUtils.recipeBuilder(
				tinyDust,	tinyDust, tinyDust, 
				tinyDust, tinyDust, tinyDust, 
				tinyDust, tinyDust, tinyDust,
				normalDust)){
			Utils.LOG_WARNING("9 Tiny dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("9 Tiny dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");			
		}

		if (RecipeUtils.recipeBuilder(
				normalDust, null, null, 
				null, null, null, 
				null, null, null,
				material.getTinyDust(9))){
			Utils.LOG_WARNING("9 Tiny dust from 1 Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("9 Tiny dust from 1 Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		if (RecipeUtils.recipeBuilder(
				smallDust, smallDust, null, 
				smallDust, smallDust, null, 
				null, null, null,
				normalDust)){
			Utils.LOG_WARNING("4 Small dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("4 Small dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		if (RecipeUtils.recipeBuilder(
				null, normalDust, null, 
				null, null, null, 
				null, null, null,
				material.getSmallDust(4))){
			Utils.LOG_WARNING("4 Small dust from 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("4 Small dust from 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");			
		}

		//Macerate blocks back to dusts.
		ItemStack materialBlock = material.getBlock(1);
		ItemStack materialFrameBox = material.getFrameBox(1);

		if (materialBlock != null)
			GT_ModHandler.addPulverisationRecipe(materialBlock, material.getDust(9));

		if (materialFrameBox != null)
			GT_ModHandler.addPulverisationRecipe(materialFrameBox, material.getDust(2));

		//Is this a composite?
		if (inputStacks != null && !disableOptional){			
			//Is this a composite?
			Utils.LOG_INFO("mixer length: "+inputStacks.length);
			if (inputStacks.length != 0 && inputStacks.length <= 4){								
				//Log Input items
				Utils.LOG_WARNING(ItemUtils.getArrayStackNames(inputStacks));
				final long[] inputStackSize = material.vSmallestRatio;
				Utils.LOG_INFO("mixer is stacksizeVar null? "+(inputStackSize != null));
				//Is smallest ratio invalid?
				if (inputStackSize != null){
					//set stack sizes on an input ItemStack[]
					for (short x=0;x<inputStacks.length;x++){
						if (inputStacks[x] != null && inputStackSize[x] != 0){
							inputStacks[x].stackSize = (int) inputStackSize[x];							
						}
					}
					//Relog input values, with stack sizes
					Utils.LOG_WARNING(ItemUtils.getArrayStackNames(inputStacks));	

					//Get us four ItemStacks to input into the mixer
					ItemStack input1, input2, input3, input4;
					input1 = (inputStacks.length >= 1) ? (input1 = (inputStacks[0] == null) ? null : inputStacks[0]) : null;
					input2 = (inputStacks.length >= 2) ? (input2 = (inputStacks[1] == null) ? null : inputStacks[1]) : null;
					input3 = (inputStacks.length >= 3) ? (input3 = (inputStacks[2] == null) ? null : inputStacks[2]) : null;
					input4 = (inputStacks.length >= 4) ? (input4 = (inputStacks[3] == null) ? null : inputStacks[3]) : null;

					//Add mixer Recipe

					FluidStack oxygen = GT_Values.NF;
					if (material.getComposites() != null){
						for (MaterialStack x : material.getComposites()){
							if (!material.getComposites().isEmpty()){
								if (x != null){
									if (x.getStackMaterial() != null){
										if (x.getStackMaterial().getDust(1) == null){
											if (x.getStackMaterial().getState() == MaterialState.GAS){
												oxygen = x.getStackMaterial().getFluid(1000);							
											}
										}
									}
								}
							}
						}

					}

					if (GT_Values.RA.addMixerRecipe(
							input1, input2,
							input3, input4,
							oxygen,
							null,
							outputStacks,
							(int) Math.max(material.getMass() * 2L * 1, 1),
							6 * material.vVoltageMultiplier))
					{
						Utils.LOG_INFO("Dust Mixer Recipe: "+material.getLocalizedName()+" - Success");
					}
					else {
						Utils.LOG_INFO("Dust Mixer Recipe: "+material.getLocalizedName()+" - Failed");			
					}
				}
			}
		}






	}
}

