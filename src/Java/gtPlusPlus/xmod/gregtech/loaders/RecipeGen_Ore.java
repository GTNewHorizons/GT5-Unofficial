package gtPlusPlus.xmod.gregtech.loaders;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_Ore extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}	

	public RecipeGen_Ore(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private static Material mStone;

	public static void generateRecipes(final Material material){

		if (mStone == null) {
			mStone = MaterialUtils.generateMaterialFromGtENUM(Materials.Stone);
		}

		//if (material.getMaterialComposites().length > 1){
		Logger.MATERIALS("[Recipe Generator Debug] ["+material.getLocalizedName()+"]");
		int tVoltageMultiplier = MaterialUtils.getVoltageForTier(material.vTier);
		if (tVoltageMultiplier < 120) {
			tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 480 : 120;
		}

		final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);
		Material bonusA = null; //Ni
		Material bonusB = null; //Tin

		if (material.getComposites().size() >= 1 && material.getComposites().get(0) != null){
			bonusA = material.getComposites().get(0).getStackMaterial();
		}
		else {
			bonusA = material;
		}

		boolean allFailed = false;


		//Setup Bonuses
		ArrayList<Material> aMatComp = new ArrayList<Material>();		
		for (Material j : MaterialUtils.getCompoundMaterialsRecursively(material)) {
			aMatComp.add(j);
		}

		if (aMatComp.size() < 3) {
			while (aMatComp.size() < 3) {
				aMatComp.add(material);
			}
		}

		AutoMap<Material> amJ = new AutoMap<Material>();
		int aIndexCounter = 0;
		for (Material g : aMatComp) {
			if (g.hasSolidForm()) {
				if (getDust(g) != null && getTinyDust(g) != null) {					
					amJ.put(g);
				}				
			}			
		}

		if (amJ.size() < 2) {
			if (material.getComposites().size() >= 2 && material.getComposites().get(1) != null){
				bonusB = material.getComposites().get(1).getStackMaterial();
				//If Secondary Output has no solid output, try the third (If it exists)
				if (!bonusB.hasSolidForm() && material.getComposites().size() >= 3 && material.getComposites().get(2) != null) {
					bonusB = material.getComposites().get(2).getStackMaterial();
					//If Third Output has no solid output, try the Fourth (If it exists)
					if (!bonusB.hasSolidForm() && material.getComposites().size() >= 4 && material.getComposites().get(3) != null) {
						bonusB = material.getComposites().get(3).getStackMaterial();
						//If Fourth Output has no solid output, try the Fifth (If it exists)
						if (!bonusB.hasSolidForm() && material.getComposites().size() >= 5 && material.getComposites().get(4) != null) {
							bonusB = material.getComposites().get(4).getStackMaterial();
							//If Fifth Output has no solid output, default out to Chrome.
							if (!bonusB.hasSolidForm()) {
								allFailed = true;
								bonusB = mStone;
							}
						}
					}
				}
			}
			else {
				allFailed = true;
			}
		}
		else {
			bonusA = amJ.get(0);
			bonusB = amJ.get(1);
		}

		//Default out if it's made of fluids or some shit.
		if (bonusA == null) {
			bonusA = tVoltageMultiplier <= 100 ? material : mStone;
		}
		//Default out if it's made of fluids or some shit.
		if (allFailed || bonusB == null) {
			bonusB = tVoltageMultiplier <= 100 ? material : mStone;
		}

		AutoMap<Pair<Integer, Material>> componentMap = new AutoMap<Pair<Integer, Material>>();
		for (MaterialStack r : material.getComposites()){
			if (r != null){
				componentMap.put(new Pair<Integer, Material>(r.getPartsPerOneHundred(), r.getStackMaterial()));
			}
		}

		//Need two valid outputs
		if (bonusA == null || bonusB == null || !bonusA.hasSolidForm() || !bonusB.hasSolidForm()) {
			if (bonusA == null) {
				bonusA = mStone;
			}
			if (bonusB == null) {
				bonusB = mStone;
			}
			if (!bonusA.hasSolidForm()) {
				bonusA = mStone;
			}
			if (!bonusB.hasSolidForm()) {
				bonusB = mStone;
			}				
		}

		ItemStack tinyDustA = getTinyDust(bonusA);
		ItemStack tinyDustB = getTinyDust(bonusB);
		ItemStack matDust = getDust(material);
		ItemStack matDustA = getDust(bonusA);
		ItemStack matDustB = getDust(bonusB);

		/**
		 * Macerate
		 */
		//Macerate ore to Crushed
		if (GT_Values.RA.addPulveriserRecipe(material.getOre(1), new ItemStack[]{material.getCrushed(2)}, new int[]{10000}, 20*20, tVoltageMultiplier/2)){
			Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate ore to Crushed ore'");
		}
		//Macerate Crushed to Impure Dust
		if (GT_Values.RA.addPulveriserRecipe(material.getCrushed(1), new ItemStack[]{material.getDustImpure(1), matDustA}, new int[]{10000, 1000}, 20*20, tVoltageMultiplier/2)){
			Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Crushed ore to Impure Dust'");
		}
		//Macerate Washed to Purified Dust
		if (GT_Values.RA.addPulveriserRecipe(material.getCrushedPurified(1), new ItemStack[]{material.getDustPurified(1), matDustA}, new int[]{10000, 1000}, 20*20, tVoltageMultiplier/2)){
			Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Washed ore to Purified Dust'");
		}
		//Macerate Centrifuged to Pure Dust
		if (GT_Values.RA.addPulveriserRecipe(material.getCrushedCentrifuged(1), new ItemStack[]{matDust, matDustA}, new int[]{10000, 1000}, 20*20, tVoltageMultiplier/2)){
			Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Centrifuged ore to Pure Dust'");
		}

		/**
		 * Wash
		 */		
		//Wash into Purified Crushed
		/*if (GT_Values.RA.addOreWasherRecipe(material.getCrushed(1), material.getCrushedPurified(1), bonusA.getTinyDust(1), dustStone, FluidUtils.getWater(1000), 25*20, 16)){
				Logger.MATERIALS("[OreWasher] Added Recipe: 'Wash Crushed ore into Purified Crushed ore'");
			}*/
		//.08 compat method
		if (GT_ModHandler.addOreWasherRecipe(material.getCrushed(1), 1000, material.getCrushedPurified(1), tinyDustA, dustStone)){
			Logger.MATERIALS("[OreWasher] Added Recipe: 'Wash Crushed ore into Purified Crushed ore'");
		}





		/**
		 * Thermal Centrifuge
		 */			
		/*//Crushed ore to Centrifuged Ore
			if (GT_Values.RA.addThermalCentrifugeRecipe(material.getCrushed(1), material.getCrushedCentrifuged(1), tinyDustB, dustStone, 25*20, 24)){
				Logger.MATERIALS("[ThermalCentrifuge] Added Recipe: 'Crushed ore to Centrifuged Ore'");
			}
			//Washed ore to Centrifuged Ore
			if (GT_Values.RA.addThermalCentrifugeRecipe(material.getCrushedPurified(1), material.getCrushedCentrifuged(1), bonusA.getTinyDust(1), dustStone, 25*20, 24)){
				Logger.MATERIALS("[ThermalCentrifuge] Added Recipe: 'Washed ore to Centrifuged Ore'");
			}*/

		Logger.MATERIALS("material.getCrushed(1): "+(material.getCrushed(1) != null));
		Logger.MATERIALS("material.getCrushedPurified(1): "+(material.getCrushedPurified(1) != null));
		Logger.MATERIALS("bonusA.getTinyDust(1): "+(tinyDustA != null)+" | Material: "+(bonusA != null) + " | Material name: "+(bonusA != null ? bonusA.getLocalizedName() : "invalid material"));
		Logger.MATERIALS("bonusB.getTinyDust(1): "+(tinyDustB != null)+" | Material: "+(bonusB != null) + " | Material name: "+(bonusB != null ? bonusB.getLocalizedName() : "invalid material"));

		try {
			//.08 compat
			if (GT_ModHandler.addThermalCentrifugeRecipe(material.getCrushed(1), 200, material.getCrushedCentrifuged(1), tinyDustB, dustStone)){
				Logger.MATERIALS("[ThermalCentrifuge] Added Recipe: 'Crushed ore to Centrifuged Ore' | Input: "+material.getCrushed(1).getDisplayName()+" | Outputs: "+material.getCrushedCentrifuged(1).getDisplayName()+", "+tinyDustB.getDisplayName()+", "+dustStone.getDisplayName()+".");
			}
		}
		catch (Throwable t) {}
		try {
			if (GT_ModHandler.addThermalCentrifugeRecipe(material.getCrushedPurified(1), 200, material.getCrushedCentrifuged(1), tinyDustA, dustStone)){
				Logger.MATERIALS("[ThermalCentrifuge] Added Recipe: 'Washed ore to Centrifuged Ore' | Input: "+material.getCrushedPurified(1).getDisplayName()+" | Outputs: "+material.getCrushedCentrifuged(1).getDisplayName()+", "+tinyDustA.getDisplayName()+", "+dustStone.getDisplayName()+".");
			}
		}
		catch (Throwable t) {}


		/**
		 * Forge Hammer
		 */			
		if (GT_Values.RA.addForgeHammerRecipe(material.getCrushedCentrifuged(1), matDust, 10, tVoltageMultiplier/4)){
			Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Centrifuged to Pure Dust'");
		}
		if (GT_Values.RA.addForgeHammerRecipe(material.getCrushedPurified(1), material.getDustPurified(1), 10, tVoltageMultiplier/4)){
			Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Purified to Purified Dust'");
		}
		if (GT_Values.RA.addForgeHammerRecipe(material.getOre(1), material.getCrushed(1), 10, tVoltageMultiplier/4)){
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
				matDust, tinyDustA,null, 
				null, null,null, 
				new int[]{10000, 10000}, //Chances
				5*20, //Eu
				tVoltageMultiplier/2)){ //Time
			Logger.MATERIALS("[Centrifuge] Added Recipe: Purified Dust to Clean Dust");
		}

		//Impure Dust to Clean
		if (GT_Values.RA.addCentrifugeRecipe(
				material.getDustImpure(1), null,
				null, //In Fluid
				null, //Out Fluid
				matDust, tinyDustB,null, 
				null, null,null, 
				new int[]{10000, 10000}, //Chances
				5*20, //Eu
				tVoltageMultiplier/2)){ //Time
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
						20*1*(tVoltageMultiplier/10), 
						tVoltageMultiplier)){
					Logger.MATERIALS("[Electrolyzer] Generated Electrolyzer recipe for "+matDust.getDisplayName());
				}
				else {
					Logger.MATERIALS("[Electrolyzer] Failed to generate Electrolyzer recipe for "+matDust.getDisplayName());					
				}
			}
			catch(Throwable t){
				t.printStackTrace();
			}
		}
		else if (componentMap.size() > 6 && componentMap.size() <= 9){
			Logger.MATERIALS("[Issue][Electrolyzer] "+material.getLocalizedName()+" is composed of over 6 materials, so an electrolyzer recipe for processing cannot be generated. Trying to create one for the Dehydrator instead.");

			ItemStack mInternalOutputs[] = new ItemStack[9];
			int mChances[] = new int[9];
			int mCellCount = 0;

			int mTotalCount = 0;

			int mCounter = 0;
			for (Pair<Integer, Material> f : componentMap){
				if (f.getValue().getState() != MaterialState.SOLID && f.getValue().getState() != MaterialState.ORE){
					Logger.MATERIALS("[Dehydrator] Found Fluid Component, adding "+f.getKey()+" cells of "+f.getValue().getLocalizedName()+".");
					mInternalOutputs[mCounter++] = f.getValue().getCell(f.getKey());
					mCellCount += f.getKey();
					mTotalCount += f.getKey();
					Logger.MATERIALS("[Dehydrator] In total, adding "+mCellCount+" cells for "+material.getLocalizedName()+" processing.");
				}
				else {
					Logger.MATERIALS("[Dehydrator] Found Solid Component, adding "+f.getKey()+" dusts of "+f.getValue().getLocalizedName()+".");
					mInternalOutputs[mCounter++] = f.getValue().getDust(f.getKey());
					mTotalCount += f.getKey();
				}
			}				

			//Build Output Array
			for (int g=0;g<mInternalOutputs.length;g++){
				Logger.MATERIALS("[Dehydrator] Is output["+g+"] valid with a chance? "+(mInternalOutputs[g] != null ? 10000 : 0));
				mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
			}

			ItemStack emptyCell = null;
			if (mCellCount > 0){
				emptyCell = ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", mCellCount);
				Logger.MATERIALS("[Dehydrator] Recipe now requires "+mCellCount+" empty cells as input.");
			}

			ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
			if (mainDust != null){
				Logger.MATERIALS("[Dehydrator] Recipe now requires "+material.smallestStackSizeWhenProcessing+"x "+mainDust.getDisplayName()+" as input.");					
			}
			else {
				mainDust = material.getDust(mTotalCount);
				Logger.MATERIALS("[Dehydrator] Could not find valid input dust, trying alternative.");	
				if (mainDust != null){
					Logger.MATERIALS("[Dehydrator] Recipe now requires "+mTotalCount+"x "+mainDust.getDisplayName()+" as input.");					
				}
				else {
					Logger.MATERIALS("[Dehydrator] Could not find valid input dust, exiting.");					
				}
			}

			for (int j=0;j<mInternalOutputs.length;j++){
				if (mInternalOutputs[j] == null){
					mInternalOutputs[j] = GT_Values.NI;
					Logger.MATERIALS("[Dehydrator] Set slot "+j+"  to null.");
				}
				else {
					Logger.MATERIALS("[Dehydrator] Set slot "+j+" to "+mInternalOutputs[j].getDisplayName()+".");						
				}
			}

			try{		


				if (CORE.RA.addDehydratorRecipe(
						new ItemStack[]{mainDust, emptyCell},
						null,
						null,
						mInternalOutputs,
						mChances,
						20*1*(tVoltageMultiplier/10),
						tVoltageMultiplier)){
					Logger.MATERIALS("[Dehydrator] Generated Dehydrator recipe for "+matDust.getDisplayName());
				}
				else {
					Logger.MATERIALS("[Dehydrator] Failed to generate Dehydrator recipe for "+matDust.getDisplayName());					
				}
			}
			catch(Throwable t){
				t.printStackTrace();
			}


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
				matDust);



		final ItemStack normalDust = matDust;
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

		//}
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

	public static ItemStack getTinyDust(Material m) {
		ItemStack x = m.getTinyDust(1);
		if (x == null) {
			x = mStone.getDust(1);
		}
		return x;
	}

	public static ItemStack getDust(Material m) {
		ItemStack x = m.getDust(1);
		if (x == null) {
			x = mStone.getDust(1);
		}
		return x;
	}

}
