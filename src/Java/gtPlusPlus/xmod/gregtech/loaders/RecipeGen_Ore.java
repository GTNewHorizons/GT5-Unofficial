package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.array.AutoMap;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_Ore implements Runnable{

	final Material toGenerate;

	public RecipeGen_Ore(final Material M){
		this.toGenerate = M;
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	public static void generateRecipes(final Material material){

		if (material.getMaterialComposites().length > 1){
			Logger.MATERIALS("[Recipe Generator Debug] ["+material.getLocalizedName()+"]");
			final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 120 : 30;
			final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);
			Material bonusA; //Ni
			Material bonusB; //Tin
			
			if (material.getComposites().get(0) != null){
				bonusA = material.getComposites().get(0).getStackMaterial();
			}
			else {
				return ;
			}
			if (material.getComposites().size() >= 1 && material.getComposites().get(1) != null){
				bonusB = material.getComposites().get(1).getStackMaterial();
			}
			else if (material.getComposites().get(0) != null){
				bonusB = material.getComposites().get(0).getStackMaterial();
			}
			else {
				return;
			}
			
			AutoMap<Pair<Integer, Material>> componentMap = new AutoMap<Pair<Integer, Material>>();

			for (MaterialStack r : material.getComposites()){
				if (r != null){
					componentMap.put(new Pair<Integer, Material>(r.getPartsPerOneHundred(), r.getStackMaterial()));
				}
			}
			
			/**
			 * Macerate
			 */
			//Macerate ore to Crushed
			if (GT_Values.RA.addPulveriserRecipe(material.getOre(1), new ItemStack[]{material.getCrushed(2)}, new int[]{10000}, 20*20, 2)){
				Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate ore to Crushed ore'");
			}
			//Macerate Crushed to Impure Dust
			if (GT_Values.RA.addPulveriserRecipe(material.getCrushed(1), new ItemStack[]{material.getDustImpure(1), bonusA.getDust(1)}, new int[]{10000, 1000}, 20*20, 2)){
				Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Crushed ore to Impure Dust'");
			}
			//Macerate Washed to Purified Dust
			if (GT_Values.RA.addPulveriserRecipe(material.getCrushedPurified(1), new ItemStack[]{material.getDustPurified(1), bonusA.getDust(1)}, new int[]{10000, 1000}, 20*20, 2)){
				Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Washed ore to Purified Dust'");
			}
			//Macerate Centrifuged to Pure Dust
			if (GT_Values.RA.addPulveriserRecipe(material.getCrushedCentrifuged(1), new ItemStack[]{material.getDust(1), bonusA.getDust(1)}, new int[]{10000, 1000}, 20*20, 2)){
				Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Centrifuged ore to Pure Dust'");
			}

			/**
			 * Wash
			 */		
			//Wash into Purified Crushed
			if (GT_Values.RA.addOreWasherRecipe(material.getCrushed(1), material.getCrushedPurified(1), bonusA.getTinyDust(1), dustStone, FluidUtils.getWater(1000), 25*20, 16)){
				Logger.MATERIALS("[OreWasher] Added Recipe: 'Wash Crushed oer into Purified Crushed ore'");
			}

			/**
			 * Thermal Centrifuge
			 */			
			//Crushed ore to Centrifuged Ore
			if (GT_Values.RA.addThermalCentrifugeRecipe(material.getCrushed(1), material.getCrushedCentrifuged(1), bonusB.getTinyDust(1), dustStone, 25*20, 24)){
				Logger.MATERIALS("[ThermalCentrifuge] Added Recipe: 'Crushed ore to Centrifuged Ore'");
			}
			//Washed ore to Centrifuged Ore
			if (GT_Values.RA.addThermalCentrifugeRecipe(material.getCrushedPurified(1), material.getCrushedCentrifuged(1), bonusA.getTinyDust(1), dustStone, 25*20, 24)){
				Logger.MATERIALS("[ThermalCentrifuge] Added Recipe: 'Washed ore to Centrifuged Ore'");
			}

			/**
			 * Forge Hammer
			 */			
			if (GT_Values.RA.addForgeHammerRecipe(material.getCrushedCentrifuged(1), material.getDust(1), 10, 16)){
				Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Centrifuged to Pure Dust'");
			}
			if (GT_Values.RA.addForgeHammerRecipe(material.getCrushedPurified(1), material.getDustPurified(1), 10, 16)){
				Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Purified to Purified Dust'");
			}
			if (GT_Values.RA.addForgeHammerRecipe(material.getOre(1), material.getCrushed(1), 10, 16)){
				Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Ore to Crushed'");
			}
			
			/**
			 * Centrifuge
			 */
			//Purified Dust to Clean
			if (GT_Values.RA.addCentrifugeRecipe(
					material.getDustPurified(1), null,
					null, //In Fluid
					null, //Out Fluid
					material.getDust(1), bonusA.getTinyDust(1),null, 
					null, null,null, 
					new int[]{10000, 10000}, //Chances
					5*20, //Eu
					5)){ //Time
				Logger.MATERIALS("[Centrifuge] Added Recipe: Purified Dust to Clean Dust");
			}
			
			//Impure Dust to Clean
			if (GT_Values.RA.addCentrifugeRecipe(
					material.getDustImpure(1), null,
					null, //In Fluid
					null, //Out Fluid
					material.getDust(1), bonusB.getTinyDust(1),null, 
					null, null,null, 
					new int[]{10000, 10000}, //Chances
					5*20, //Eu
					5)){ //Time
				Logger.MATERIALS("[Centrifuge] Added Recipe: Inpure Dust to Clean Dust");
			}
			
			
			/**
			 * Electrolyzer
			 */
			
			//Process Dust
			if (componentMap.size() > 0 && componentMap.size() <= 6){
				
				ItemStack mInternalOutputs[] = new ItemStack[6];
				int mChances[] = new int[6];
				int mCellCount = 0;
				
				int mTotalCount = 0;
				
				int mCounter = 0;
				for (Pair<Integer, Material> f : componentMap){
					if (f.getValue().getState() != MaterialState.SOLID){
						Logger.MATERIALS("[Electrolyzer] Found Fluid Component, adding "+f.getKey()+" cells of "+f.getValue().getLocalizedName()+".");
						mInternalOutputs[mCounter++] = f.getValue().getCell(f.getKey());
						mCellCount += f.getKey();
						mTotalCount += f.getKey();
						Logger.MATERIALS("[Electrolyzer] In total, adding "+mCellCount+" cells for "+material.getLocalizedName()+" processing.");
					}
					else {
						Logger.MATERIALS("[Electrolyzer] Found Solid Component, adding "+f.getKey()+" dusts of "+f.getValue().getLocalizedName()+".");
						mInternalOutputs[mCounter++] = f.getValue().getDust(f.getKey());
						mTotalCount += f.getKey();
					}
				}				
				
				//Build Output Array
				for (int g=0;g<mInternalOutputs.length;g++){
					Logger.MATERIALS("[Electrolyzer] Is output["+g+"] valid with a chance? "+(mInternalOutputs[g] != null ? 10000 : 0));
					mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
				}
				
				ItemStack emptyCell = null;
				if (mCellCount > 0){
					emptyCell = ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", mCellCount);
					Logger.MATERIALS("[Electrolyzer] Recipe now requires "+mCellCount+" empty cells as input.");
				}
				
				ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
				if (mainDust != null){
					Logger.MATERIALS("[Electrolyzer] Recipe now requires "+material.smallestStackSizeWhenProcessing+"x "+mainDust.getDisplayName()+" as input.");					
				}
				else {
					mainDust = material.getDust(mTotalCount);
					Logger.MATERIALS("[Electrolyzer] Could not find valid input dust, trying alternative.");	
					if (mainDust != null){
						Logger.MATERIALS("[Electrolyzer] Recipe now requires "+mTotalCount+"x "+mainDust.getDisplayName()+" as input.");					
					}
					else {
						Logger.MATERIALS("[Electrolyzer] Could not find valid input dust, exiting.");					
					}
				}
				
				for (int j=0;j<mInternalOutputs.length;j++){
					if (mInternalOutputs[j] == null){
						mInternalOutputs[j] = GT_Values.NI;
						Logger.MATERIALS("[Electrolyzer] Set slot "+j+"  to null.");
					}
					else {
						Logger.MATERIALS("[Electrolyzer] Set slot "+j+" to "+mInternalOutputs[j].getDisplayName()+".");						
					}
				}
				
				try{
				if (addElectrolyzerRecipe(
						mainDust,
						emptyCell, //input 2
						null, //Input fluid 1
						null, //Output fluid 1
						mInternalOutputs[0],
						mInternalOutputs[1], 
						mInternalOutputs[2], 
						mInternalOutputs[3],
						mInternalOutputs[4],
						mInternalOutputs[5],
						mChances, 
						20*90, 
						tVoltageMultiplier)){
					Logger.MATERIALS("[Electrolyzer] Generated Electrolyzer recipe for "+material.getDust(1).getDisplayName());
				}
				else {
					Logger.MATERIALS("[Electrolyzer] Failed to generate Electrolyzer recipe for "+material.getDust(1).getDisplayName());					
				}
				}
				catch(Throwable t){
					t.printStackTrace();
				}
			}
			else if (componentMap.size() > 6){
				Logger.MATERIALS("[Issue][Electrolyzer] "+material.getLocalizedName()+" is composed of over 6 materials, so a recipe for processing cannot be generated.");
			}
			
			
			/**
			 * Shaped Crafting
			 */			
			RecipeUtils.recipeBuilder(
					CI.craftingToolHammer_Hard, null, null,
					material.getCrushedPurified(1), null, null,
					null, null, null,
					material.getDustPurified(1));

			RecipeUtils.recipeBuilder(
					CI.craftingToolHammer_Hard, null, null,
					material.getCrushed(1), null, null,
					null, null, null,
					material.getDustImpure(1));
			
			RecipeUtils.recipeBuilder(
					CI.craftingToolHammer_Hard, null, null,
					material.getCrushedCentrifuged(1), null, null,
					null, null, null,
					material.getDust(1));
			
			

			final ItemStack normalDust = material.getDust(1);
			final ItemStack smallDust = material.getSmallDust(1);
			final ItemStack tinyDust = material.getTinyDust(1);

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
	}


	public static boolean addElectrolyzerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
           Logger.MATERIALS("[Electrolyzer] Either both inputs or outputs are null.");
        	return false;
        }
        if ((aInput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("electrolyzer", aInput1, aDuration)) <= 0)) {
        	 Logger.MATERIALS("[Electrolyzer] Fail 1.");
         	 return false;
        }
        if ((aFluidInput != null) && ((aDuration = GregTech_API.sRecipeFile.get("electrolyzer", aFluidInput.getFluid().getName(), aDuration)) <= 0)) {
        	 Logger.MATERIALS("[Electrolyzer] Fail 2.");
         	return false;
        }
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6}, null, aChances, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
        Logger.MATERIALS("[Electrolyzer] Recipe added.");
    	return true;
    }

}
