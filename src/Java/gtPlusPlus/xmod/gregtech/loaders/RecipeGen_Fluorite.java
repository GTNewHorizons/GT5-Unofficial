package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraft.item.ItemStack;

public class RecipeGen_Fluorite extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}	

	public RecipeGen_Fluorite(final Material material){
		this.toGenerate = material;
		mRecipeGenMap.add(this);


		/**
		 * Shaped Crafting
		 */			
		RecipeUtils.addShapedRecipe(
				CI.craftingToolHammer_Hard, null, null,
				material.getCrushedPurified(1), null, null,
				null, null, null,
				material.getDustPurified(1));

		RecipeUtils.addShapedRecipe(
				CI.craftingToolHammer_Hard, null, null,
				material.getCrushed(1), null, null,
				null, null, null,
				material.getDustImpure(1));

		RecipeUtils.addShapedRecipe(
				CI.craftingToolHammer_Hard, null, null,
				material.getCrushedCentrifuged(1), null, null,
				null, null, null,
				material.getDust(1));



		final ItemStack normalDust = material.getDust(1);
		final ItemStack smallDust = material.getSmallDust(1);
		final ItemStack tinyDust = material.getTinyDust(1);

		if (RecipeUtils.addShapedRecipe(
				tinyDust, tinyDust, tinyDust,
				tinyDust, tinyDust, tinyDust,
				tinyDust, tinyDust, tinyDust,
				normalDust)){
			Logger.WARNING("9 Tiny dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("9 Tiny dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");
		}

		if (RecipeUtils.addShapedRecipe(
				normalDust, null, null,
				null, null, null,
				null, null, null,
				material.getTinyDust(9))){
			Logger.WARNING("9 Tiny dust from 1 Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("9 Tiny dust from 1 Recipe: "+material.getLocalizedName()+" - Failed");
		}

		if (RecipeUtils.addShapedRecipe(
				smallDust, smallDust, null,
				smallDust, smallDust, null,
				null, null, null,
				normalDust)){
			Logger.WARNING("4 Small dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Logger.WARNING("4 Small dust to 1 Dust Recipe: "+material.getLocalizedName()+" - Failed");
		}

		if (RecipeUtils.addShapedRecipe(
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

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	public static void generateRecipes(final Material material){

		int tVoltageMultiplier = MaterialUtils.getVoltageForTier(material.vTier);

		final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);


		ItemStack tinyDustA = FLUORIDES.FLUORITE.getTinyDust(1);
		ItemStack tinyDustB = FLUORIDES.FLUORITE.getTinyDust(1);
		ItemStack matDust = FLUORIDES.FLUORITE.getDust(1);
		ItemStack matDustA = FLUORIDES.FLUORITE.getDust(1);

		/**
		 * Package
		 */
		//Allow ore dusts to be packaged
		if (ItemUtils.checkForInvalidItems(material.getSmallDust(1)) && ItemUtils.checkForInvalidItems(material.getTinyDust(1))) {
			RecipeGen_DustGeneration.generatePackagerRecipes(material);
		}
		
		
		/**
		 * Macerate
		 */
		//Macerate ore to Crushed
		if (GT_Values.RA.addPulveriserRecipe(material.getOre(1), new ItemStack[]{material.getCrushed(2)}, new int[]{10000}, 20*20, tVoltageMultiplier/2)){
			Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate ore to Crushed ore'");
		}
		//Macerate Centrifuged to Pure Dust
		if (GT_Values.RA.addPulveriserRecipe(material.getCrushedCentrifuged(1), new ItemStack[]{matDust, matDustA}, new int[]{10000, 1000}, 20*20, tVoltageMultiplier/2)){
			Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Centrifuged ore to Pure Dust'");
		}
		if (GT_ModHandler.addThermalCentrifugeRecipe(material.getCrushedPurified(1), (int) Math.min(5000L, Math.abs(material.getMass() * 20L)), material.getCrushedCentrifuged(1), tinyDustA, dustStone)){
			Logger.MATERIALS("[ThermalCentrifuge] Added Recipe: 'Washed ore to Centrifuged Ore' | Input: "+material.getCrushedPurified(1).getDisplayName()+" | Outputs: "+material.getCrushedCentrifuged(1).getDisplayName()+", "+tinyDustA.getDisplayName()+", "+dustStone.getDisplayName()+".");
		}

		GT_Values.RA.addChemicalBathRecipe(
				FLUORIDES.FLUORITE.getCrushed(2), 
				FluidUtils.getFluidStack("hydrogen", 2000),
				FLUORIDES.FLUORITE.getCrushedPurified(8), 
				FLUORIDES.FLUORITE.getDustImpure(4),
				FLUORIDES.FLUORITE.getDustPurified(2),
				new int[] { 10000, 5000, 1000 }, 
				30 * 20, 
				240);

		/**
		 * Forge Hammer
		 */			
		if (GT_Values.RA.addForgeHammerRecipe(material.getCrushedCentrifuged(1), matDust, 10, tVoltageMultiplier/4)){
			Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Centrifuged to Pure Dust'");
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
				(int) Math.max(1L, material.getMass() * 8L), //Time
				tVoltageMultiplier/2)){ //Eu
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
				(int) Math.max(1L, material.getMass() * 8L), //Time
				tVoltageMultiplier/2)){ //Eu
			Logger.MATERIALS("[Centrifuge] Added Recipe: Inpure Dust to Clean Dust");
		}
		
		AutoMap<Pair<Integer, Material>> componentMap = new AutoMap<Pair<Integer, Material>>();
		for (MaterialStack r : material.getComposites()){
			if (r != null){
				componentMap.put(new Pair<Integer, Material>(r.getPartsPerOneHundred(), r.getStackMaterial()));
			}
		}
		
		//Process Dust
		if (componentMap.size() > 1 && componentMap.size() <= 9){
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
						(int) Math.max(material.getMass() * 8L, 1),
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
		
	}

}
