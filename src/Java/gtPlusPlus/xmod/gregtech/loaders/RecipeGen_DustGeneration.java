package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_DustGeneration extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_DustGeneration(final Material M){
		this(M, false);
	}

	public RecipeGen_DustGeneration(final Material M, final boolean O){
		this.toGenerate = M;		
		this.disableOptional = O;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate, this.disableOptional);
	}

	private void generateRecipes(final Material material, final boolean disableOptional){
		final int tVoltageMultiplier = material.vVoltageMultiplier;

		Logger.WARNING("Generating Shaped Crafting recipes for "+material.getLocalizedName()); //TODO

		final ItemStack normalDust = material.getDust(1);
		final ItemStack smallDust = material.getSmallDust(1);
		final ItemStack tinyDust = material.getTinyDust(1);

		final ItemStack[] inputStacks = material.getMaterialComposites();
		final ItemStack outputStacks = material.getDust(material.smallestStackSizeWhenProcessing);


		if (ItemUtils.checkForInvalidItems(tinyDust) && ItemUtils.checkForInvalidItems(normalDust)) {
			if (RecipeUtils.recipeBuilder(
					tinyDust,	tinyDust, tinyDust,
					tinyDust, tinyDust, tinyDust,
					tinyDust, tinyDust, tinyDust,
					normalDust)){
				Logger.WARNING("9 Tiny dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("9 Tiny dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");
			}

			if (RecipeUtils.recipeBuilder(
					normalDust, null, null,
					null, null, null,
					null, null, null,
					material.getTinyDust(9))){
				Logger.WARNING("9 Tiny dust from 1 Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("9 Tiny dust from 1 Recipe: "+material.getLocalizedName()+" - Failed");
			}
		}

		if (ItemUtils.checkForInvalidItems(smallDust) && ItemUtils.checkForInvalidItems(normalDust)) {
			if (RecipeUtils.recipeBuilder(
					smallDust, smallDust, null,
					smallDust, smallDust, null,
					null, null, null,
					normalDust)){
				Logger.WARNING("4 Small dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("4 Small dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");
			}
			if (RecipeUtils.recipeBuilder(
					null, normalDust, null,
					null, null, null,
					null, null, null,
					material.getSmallDust(4))){
				Logger.WARNING("4 Small dust from 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("4 Small dust from 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");
			}
		}

		//Macerate blocks back to dusts.
		final ItemStack materialBlock = material.getBlock(1);
		final ItemStack materialFrameBox = material.getFrameBox(1);

		if (ItemUtils.checkForInvalidItems(materialBlock)) {
			GT_ModHandler.addPulverisationRecipe(materialBlock, material.getDust(9));
		}

		if (ItemUtils.checkForInvalidItems(materialFrameBox)) {
			GT_ModHandler.addPulverisationRecipe(materialFrameBox, material.getDust(2));
		}

		if (ItemUtils.checkForInvalidItems(smallDust) && ItemUtils.checkForInvalidItems(tinyDust)) {
			generatePackagerRecipes(material);
		}

		ItemStack ingot = material.getIngot(1);
		if (ItemUtils.checkForInvalidItems(normalDust) && ItemUtils.checkForInvalidItems(ingot)) {
			addFurnaceRecipe(material);
			addMacerationRecipe(material);
		}

		//Is this a composite?
		if ((inputStacks != null) && !disableOptional){
			//Is this a composite?
			Logger.WARNING("mixer length: "+inputStacks.length);
			if ((inputStacks.length != 0) && (inputStacks.length <= 4)){
				//Log Input items
				Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));
				final long[] inputStackSize = material.vSmallestRatio;
				Logger.WARNING("mixer is stacksizeVar null? "+(inputStackSize != null));
				//Is smallest ratio invalid?
				if (inputStackSize != null){
					//set stack sizes on an input ItemStack[]
					for (short x=0;x<inputStacks.length;x++){
						if ((inputStacks[x] != null) && (inputStackSize[x] != 0)){
							inputStacks[x].stackSize = (int) inputStackSize[x];
						}
					}
					//Relog input values, with stack sizes
					Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));

					//Get us four ItemStacks to input into the mixer
					ItemStack[] input = new ItemStack[4];

					input[0] = (inputStacks.length >= 1) ? ((inputStacks[0] == null) ? null : inputStacks[0]) : null;
					input[1] = (inputStacks.length >= 2) ? ((inputStacks[1] == null) ? null : inputStacks[1]) : null;
					input[2] = (inputStacks.length >= 3) ? ((inputStacks[2] == null) ? null : inputStacks[2]) : null;
					input[3] = (inputStacks.length >= 4) ? ((inputStacks[3] == null) ? null : inputStacks[3]) : null;


					if (inputStacks.length == 1) {
						input[1] = input[0];
						input[0] = CI.getNumberedCircuit(inputStacks.length+10);
					}
					else if (inputStacks.length == 2) {
						input[2] = input[1];
						input[1] = input[0];
						input[0] = CI.getNumberedCircuit(inputStacks.length+10);

					}
					else if (inputStacks.length == 3) {
						input[3] = input[2];
						input[2] = input[1];
						input[1] = input[0];
						input[0] = CI.getNumberedCircuit(inputStacks.length+10);
					}


					/*for (int g = 0; g<4; g++) {						
						if(inputStacks.length > g) {
							input[g] = inputStacks[g] != null ? inputStacks[g] : null;							
						}
						else {
							input[g] = CI.getNumberedCircuit(g+10);
							break;
						}												
					}*/					

					//Add mixer Recipe
					FluidStack oxygen = GT_Values.NF;
					if (material.getComposites() != null){
						for (final MaterialStack x : material.getComposites()){
							if (!material.getComposites().isEmpty()){
								if (x != null){
									if (x.getStackMaterial() != null){
										if (x.getStackMaterial().getDust(1) == null){
											if (x.getStackMaterial().getState() != MaterialState.SOLID && x.getStackMaterial().getState() != MaterialState.ORE && x.getStackMaterial().getState() != MaterialState.PLASMA){
												oxygen = x.getStackMaterial().getFluid(1000);
												break;
											}
										}
									}
								}
							}
						}
					}
					
					input = ItemUtils.cleanItemStackArray(input);

					//Add mixer Recipe
					if (GT_Values.RA.addMixerRecipe(
							input[0], input[1],
							input[2], input[3],
							oxygen,
							null,
							outputStacks,
							(int) Math.max(material.getMass() * 2L * 1, 1),
							material.vVoltageMultiplier)) //Was 6, but let's try 2. This makes Potin LV, for example.
					{
						Logger.WARNING("Dust Mixer Recipe: "+material.getLocalizedName()+" - Success");
					}
					else {
						Logger.WARNING("Dust Mixer Recipe: "+material.getLocalizedName()+" - Failed");
					}

					//Add Shapeless recipe for low tier alloys.
					/*if (tVoltageMultiplier <= 30){
						if (RecipeUtils.addShapedGregtechRecipe(inputStacks, outputStacks)){
							Logger.WARNING("Dust Shapeless Recipe: "+material.getLocalizedName()+" - Success");
						}
						else {
							Logger.WARNING("Dust Shapeless Recipe: "+material.getLocalizedName()+" - Failed");
						}
					}*/
				}
			}
		}






	}

	public static boolean addMixerRecipe_Standalone(final Material material){
		final ItemStack[] inputStacks = material.getMaterialComposites();
		final ItemStack outputStacks = material.getDust(material.smallestStackSizeWhenProcessing);
		//Is this a composite?
		if ((inputStacks != null)){
			//Is this a composite?
			Logger.WARNING("mixer length: "+inputStacks.length);
			if ((inputStacks.length >= 1) && (inputStacks.length <= 4)){
				//Log Input items
				Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));
				final long[] inputStackSize = material.vSmallestRatio;
				Logger.WARNING("mixer is stacksizeVar not null? "+(inputStackSize != null));
				//Is smallest ratio invalid?
				if (inputStackSize != null){
					//set stack sizes on an input ItemStack[]
					for (short x=0;x<inputStacks.length;x++){
						if ((inputStacks[x] != null) && (inputStackSize[x] != 0)){
							inputStacks[x].stackSize = (int) inputStackSize[x];
						}
					}
					//Relog input values, with stack sizes
					Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));

					//Get us four ItemStacks to input into the mixer
					ItemStack input1, input2, input3, input4;
					input1 = inputStacks[0];
					input2 = (inputStacks.length >= 2) ? (input2 = (inputStacks[1] == null) ? null : inputStacks[1]) : null;
					input3 = (inputStacks.length >= 3) ? (input3 = (inputStacks[2] == null) ? null : inputStacks[2]) : null;
					input4 = (inputStacks.length >= 4) ? (input4 = (inputStacks[3] == null) ? null : inputStacks[3]) : null;

					if (inputStacks.length == 1) {
						input2 = input1;
						input1 = CI.getNumberedCircuit(20);
					}
					else if (inputStacks.length == 2) {
						input3 = input2;
						input2 = input1;
						input1 = CI.getNumberedCircuit(20);

					}
					else if (inputStacks.length == 3) {
						input4 = input3;
						input3 = input2;
						input2 = input1;
						input1 = CI.getNumberedCircuit(20);
					}

					//Add mixer Recipe
					FluidStack oxygen = GT_Values.NF;
					if (material.getComposites() != null){
						int compSlot = 0;
						for (final MaterialStack x : material.getComposites()){
							if (!material.getComposites().isEmpty()){
								if (x != null){
									if (x.getStackMaterial() != null){
										if (x.getStackMaterial().getDust(1) == null){
											MaterialState f = x.getStackMaterial().getState();
											if (f == MaterialState.GAS || f == MaterialState.LIQUID || f == MaterialState.PURE_LIQUID){
												oxygen = x.getStackMaterial().getFluid((int) (material.vSmallestRatio[compSlot] * 1000));
											}
										}
									}
								}
							}
							compSlot++;
						}

					}

					//Add mixer Recipe
					try {
						if (GT_Values.RA.addMixerRecipe(
								input1, input2,
								input3, input4,
								oxygen,
								null,
								outputStacks,
								(int) Math.max(material.getMass() * 2L * 1, 1),
								material.vVoltageMultiplier)) //Was 6, but let's try 2. This makes Potin LV, for example.
						{
							Logger.WARNING("Dust Mixer Recipe: "+material.getLocalizedName()+" - Success");
							return true;
						}
						else {
							Logger.WARNING("Dust Mixer Recipe: "+material.getLocalizedName()+" - Failed");
							return false;
						}
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
				}
				else {
					Logger.WARNING("inputStackSize == NUll - "+material.getLocalizedName());
				}
			}
			else {
				Logger.WARNING("InputStacks is out range 1-4 - "+material.getLocalizedName());
			}
		}
		else {
			Logger.WARNING("InputStacks == NUll - "+material.getLocalizedName());
		}
		return false;
	}

	public static boolean generatePackagerRecipes(Material aMatInfo) {
		AutoMap<Boolean> aResults = new AutoMap<Boolean>();
		//Small Dust
		aResults.put(GT_Values.RA.addBoxingRecipe(GT_Utility.copyAmount(4L, new Object[]{aMatInfo.getSmallDust(4)}), ItemList.Schematic_Dust.get(0L, new Object[0]), aMatInfo.getDust(1), 100, 4));
		//Tiny Dust
		aResults.put(GT_Values.RA.addBoxingRecipe(GT_Utility.copyAmount(9L, new Object[]{aMatInfo.getTinyDust(9)}), ItemList.Schematic_Dust.get(0L, new Object[0]), aMatInfo.getDust(1), 100, 4));

		for (boolean b : aResults) {
			if (!b) {
				return false;
			}
		}		
		return true;
	}

	private void addMacerationRecipe(Material aMatInfo){
		try {
			Logger.MATERIALS("Adding Maceration recipe for "+aMatInfo.getLocalizedName()+" Ingot -> Dusts");
			final int chance = (aMatInfo.vTier*10)/MathUtils.randInt(10, 20);
			GT_ModHandler.addPulverisationRecipe(aMatInfo.getIngot(1), aMatInfo.getDust(1), null, chance);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void addFurnaceRecipe(Material aMatInfo){

		ItemStack aDust = aMatInfo.getDust(1);
		ItemStack aOutput;
		try {
			if (aMatInfo.requiresBlastFurnace()) {
				aOutput = aMatInfo.getHotIngot(1);		
				if (ItemUtils.checkForInvalidItems(aOutput)) {	
					if (addBlastFurnaceRecipe(aMatInfo, aDust, null, aOutput, null, aMatInfo.getMeltingPointK())){
						Logger.MATERIALS("Successfully added a blast furnace recipe for "+aMatInfo.getLocalizedName());
					}
					else {
						Logger.MATERIALS("Failed to add a blast furnace recipe for "+aMatInfo.getLocalizedName());
					}
				}
				else {
					Logger.MATERIALS("Failed to add a blast furnace recipe for "+aMatInfo.getLocalizedName());
				}
			}
			else {
				aOutput = aMatInfo.getIngot(1);
				if (ItemUtils.checkForInvalidItems(aOutput)) {
					if (CORE.RA.addSmeltingAndAlloySmeltingRecipe(aDust, aOutput)){
						Logger.MATERIALS("Successfully added a furnace recipe for "+aMatInfo.getLocalizedName());
					}
					else {
						Logger.MATERIALS("Failed to add a furnace recipe for "+aMatInfo.getLocalizedName());
					}
				}				
			}		
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

	}

	private boolean addBlastFurnaceRecipe(Material aMatInfo, final ItemStack input1, final ItemStack input2, final ItemStack output1, final ItemStack output2, final int tempRequired){

		try {
			int timeTaken = 125*aMatInfo.vTier*10;

			if (aMatInfo.vTier <= 4){
				timeTaken = 25*aMatInfo.vTier*10;
			}
			int aSlot = aMatInfo.vTier;
			if (aSlot < 2) {
				aSlot = 2;
			}
			long aVoltage = aMatInfo.vVoltageMultiplier;

			return GT_Values.RA.addBlastRecipe(
					input1,
					input2,
					GT_Values.NF, GT_Values.NF,
					output1,
					output2,
					timeTaken,
					(int) aVoltage,
					tempRequired);
		}
		catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

}

