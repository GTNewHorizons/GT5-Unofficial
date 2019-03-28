package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
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
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_MaterialProcessing extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_MaterialProcessing(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private void generateRecipes(final Material material){

		if (material.getMaterialComposites().length > 1){
			Logger.MATERIALS("[Recipe Generator Debug] ["+material.getLocalizedName()+"]");
			final int tVoltageMultiplier = material.vVoltageMultiplier;	
			int[] partSizes = new int[99];
			if (material.vSmallestRatio != null) {
				partSizes = new int[material.vSmallestRatio.length];
				for (int hu=0;hu<material.vSmallestRatio.length;hu++){
					partSizes[hu] = (int) material.vSmallestRatio[hu];
				}
			}			
			AutoMap<Pair<Integer, Material>> componentMap = new AutoMap<Pair<Integer, Material>>();
			int alnsnfds = 0;
			for (MaterialStack r : material.getComposites()){
				if (r != null){
					componentMap.put(new Pair<Integer, Material>(partSizes[alnsnfds], r.getStackMaterial()));
				}
				alnsnfds++;
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
					if (addCentrifgeRecipe(
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
			else if (componentMap.size() > 6 && componentMap.size() <= 9){
				Logger.MATERIALS("[Issue][Electrolyzer] "+material.getLocalizedName()+" is composed of over 6 materials, so an electrolyzer recipe for processing cannot be generated. Trying to create one for the Dehydrator instead.");

				ItemStack mInternalOutputs[] = new ItemStack[9];
				int mChances[] = new int[9];
				int mCellCount = 0;

				int mTotalCount = 0;

				int mCounter = 0;
				for (Pair<Integer, Material> f : componentMap){
					if (f.getValue().getState() != MaterialState.SOLID){
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
						Logger.MATERIALS("[Dehydrator] Generated Dehydrator recipe for "+material.getDust(1).getDisplayName());
					}
					else {
						Logger.MATERIALS("[Dehydrator] Failed to generate Dehydrator recipe for "+material.getDust(1).getDisplayName());					
					}
				}
				catch(Throwable t){
					t.printStackTrace();
				}


			}


			




		}
	}

	public static boolean addCentrifgeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
		return GT_Values.RA.addCentrifugeRecipe(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, aChances, aDuration, aEUt);
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
