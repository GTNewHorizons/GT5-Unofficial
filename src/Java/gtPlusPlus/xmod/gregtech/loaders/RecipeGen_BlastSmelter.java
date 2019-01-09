package gtPlusPlus.xmod.gregtech.loaders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.*;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_BlastSmelter extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_BlastSmelter(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateARecipe(this.toGenerate);
	}

	private void generateARecipe(final Material M){

		//Add a Blast Smelting Recipe, Let's go!
		ItemStack tStack;
		if (null != (tStack = M.getDust(1))) {

			final Material[] badMaterials = {
					FLUORIDES.THORIUM_HEXAFLUORIDE,
					FLUORIDES.THORIUM_TETRAFLUORIDE,
					ALLOY.BLOODSTEEL,
					NUCLIDE.LiFBeF2ThF4UF4,
					NUCLIDE.LiFBeF2ZrF4UF4,
					NUCLIDE.LiFBeF2ZrF4U235
			};
			for (final Material R : badMaterials){
				if (M == R){
					return;
				}
			}

			//Prepare some Variables
			ItemStack[] components;
			ArrayList<MaterialStack> tMaterial = new ArrayList<>();
			int inputStackCount=0;
			int fluidAmount=0;
			final boolean doTest = true;
			tMaterial = M.getComposites();

			//This Bad boy here is what dictates unique recipes. Fuck life, right?
			ItemStack circuitGT = ItemUtils.getGregtechCircuit(0);


			//Set a duration - OLD
			/*int duration = 0;
			if (M.getMeltingPointK() > 150){
				duration = (int) Math.max(M.getMass() / 50L, 1L) * M.getMeltingPointK();
			}
			else {
				duration = (int) Math.max(M.getMass() / 50L, 1L) * 150;
			}*/
						
			long aVoltage = MaterialUtils.getVoltageForTier(M.vTier);
			
			
			//Set a duration - NEW
			int duration = 120*M.vTier*10;

			if (M.vTier <= 4){
				duration = 20*M.vTier*10;
			}

			int mMaterialListSize=0;
			
			int mTotalPartsCounter = M.smallestStackSizeWhenProcessing;
			
			if (M.getComposites() != null){
				for (final gtPlusPlus.core.material.MaterialStack ternkfsdf : M.getComposites()){
					if (ternkfsdf != null) {
						mMaterialListSize++;
						//mTotalPartsCounter += ternkfsdf.getSmallestStackSizes()[0];
					}
				}
			}
			else {
				mMaterialListSize = 1;
			}

			if (duration <= 0){
				final int second = 20;
				duration = 14*second*mMaterialListSize*8;
			}

			Logger.WARNING("[BAS] Size: "+mMaterialListSize);


			//Make a simple one Material Materialstack[] and log it for validity.
			circuitGT = ItemUtils.getGregtechCircuit(1);
			final ItemStack[] tItemStackTest = new ItemStack[]{circuitGT, tStack};
			inputStackCount = 1;
			fluidAmount = 144*inputStackCount;
			Logger.WARNING("[BAS] Adding an Alloy Blast Smelter Recipe for "+M.getLocalizedName()+". Gives "+fluidAmount+"L of molten metal.");
			for (int das=0;das<tItemStackTest.length;das++){
				if (tItemStackTest[das] != null) {
					Logger.WARNING("[BAS] tMaterial["+das+"]: "+tItemStackTest[das].getDisplayName()+" Meta: "+tItemStackTest[das].getItemDamage()+", Amount: "+tItemStackTest[das].stackSize);
				}
			}

			final boolean hasMoreInputThanACircuit = (tItemStackTest.length > 1);

			//Generate Recipes for all singular materials that can be made molten.
			if (hasMoreInputThanACircuit){
				if (M.requiresBlastFurnace()) {
					if (CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getFluid(fluidAmount), 100, (duration/(mTotalPartsCounter > 0 ? mTotalPartsCounter : 1)), (int) aVoltage)){
						Logger.WARNING("[BAS] Success.");
						Logger.WARNING("[BAS] Success, Also added a Fluid solidifier recipe.");
						if (GT_Values.RA.addFluidExtractionRecipe(M.getIngot(1), null, M.getFluid(144), 100, duration, 120)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}
						if (GT_Values.RA.addFluidExtractionRecipe(M.getNugget(1), null, M.getFluid(16), 100, duration/9, 120)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}
						/*if (GT_Values.RA.addFluidExtractionRecipe(M.getSmallDust(1), null, M.getFluid(36), 100, duration/4, 120)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}
						if (GT_Values.RA.addFluidExtractionRecipe(M.getTinyDust(1), null, M.getFluid(16), 100, duration/9, 120)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}*/
					}
				}
				else {
					Logger.WARNING("[BAS] Failed.");
				}
			}
			else {
				if (CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getFluid(fluidAmount), 100, duration/(mTotalPartsCounter > 0 ? mTotalPartsCounter : 1)/2, (int) aVoltage)){
					Logger.WARNING("[BAS] Success.");
					if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0), M.getFluid(144), M.getIngot(1), duration/2, 60)){
						Logger.WARNING("[BAS] Success, Also added a Fluid solidifier recipe.");
						if (GT_Values.RA.addFluidExtractionRecipe(M.getIngot(1), null, M.getFluid(144), 100, duration/2, 60)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}
						if (GT_Values.RA.addFluidExtractionRecipe(M.getNugget(1), null, M.getFluid(16), 100, duration/2/9, 60)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}
						/*if (GT_Values.RA.addFluidExtractionRecipe(M.getSmallDust(1), null, M.getFluid(36), 100, duration/2/4, 60)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}
						if (GT_Values.RA.addFluidExtractionRecipe(M.getTinyDust(1), null, M.getFluid(16), 100, duration/2/9, 60)){
							Logger.WARNING("[BAS] Success, Also added a Fluid Extractor recipe.");
						}*/
					}
				}
				else {
					Logger.WARNING("[BAS] Failed.");
				}
			}

			if (tMaterial != null){
				//Reset the Variables for compounds if last recipe was a success.
				inputStackCount=0;
				
				//If this Material has some kind of compound list, proceed
				if (mMaterialListSize > 1){
					final gtPlusPlus.core.material.MaterialStack[] tempStack = new gtPlusPlus.core.material.MaterialStack[mMaterialListSize];
					circuitGT = ItemUtils.getGregtechCircuit(mMaterialListSize);
					//Just double checking
					if (tempStack.length > 1){

						//Builds me a MaterialStack[] from the MaterialList of M.
						int ooo=0;
						for (final gtPlusPlus.core.material.MaterialStack xMaterial : M.getComposites()){
							if (xMaterial != null){
								if (xMaterial.getStackMaterial() != null){
									Logger.WARNING("[BAS] FOUND: "+xMaterial.getStackMaterial().getLocalizedName());
									Logger.WARNING("[BAS] ADDING: "+xMaterial.getStackMaterial().getLocalizedName());
								}
								tempStack[ooo] = xMaterial;
							}
							ooo++;
						}

						//Builds me an ItemStack[] of the materials. - Without a circuit - this gets a good count for the 144L fluid multiplier
						components = new ItemStack[9];
						inputStackCount=0;
						FluidStack componentsFluid = null;
						for (int irc=0;irc<M.getComposites().size();irc++){
							if (M.getComposites().get(irc) != null){
								final int r = (int) M.vSmallestRatio[irc];
								inputStackCount = inputStackCount+r;
								if ((M.getComposites().get(irc).getStackMaterial().getState() != MaterialState.SOLID) || ((M.getComposites().get(irc).getDustStack(r) == null) || (M.getComposites().get(irc).getDustStack(r) == ItemUtils.getSimpleStack(ModItems.AAA_Broken)))){
									final int xr = r;
									if ((xr > 0) && (xr <= 100)){
										final int mathmatics = (r*1000);
										componentsFluid = FluidUtils.getFluidStack(M.getComposites().get(irc).getStackMaterial().getFluid(mathmatics), mathmatics);
									}
								}
								else {
									components[irc] = M.getComposites().get(irc).getDustStack(r);
								}
							}
						}


						//Adds a circuit
						if ((mMaterialListSize < 9) && (mMaterialListSize != 0)){
							final ItemStack[] components_NoCircuit = components;
							//Builds me an ItemStack[] of the materials. - With a circuit
							components = new ItemStack[components_NoCircuit.length+1];
							for (int fr=0;fr<components.length;fr++){
								if (fr==0){
									components[0] = circuitGT;
								}
								else {
									components[fr] = components_NoCircuit[fr-1];
								}
							}
							Logger.WARNING("[BAS] Should have added a circuit. mMaterialListSize: "+mMaterialListSize+" | circuit: "+components[0].getDisplayName());
						}
						else {
							Logger.WARNING("[BAS] Did not add a circuit. mMaterialListSize: "+mMaterialListSize);
						}

						//Set Fluid output
						fluidAmount = 144*inputStackCount;

						Logger.WARNING("[BAS] Adding an Alloy Blast Smelter Recipe for "+M.getLocalizedName()+" using it's compound dusts. This material has "+ inputStackCount+" parts. Gives "+fluidAmount+"L of molten metal.");
						Logger.WARNING("[BAS] tMaterial.length: "+components.length+".");
						for (int das=0;das<components.length;das++){
							if (components[das] != null) {
								Logger.WARNING("[BAS] tMaterial["+das+"]: "+components[das].getDisplayName()+" Meta: "+components[das].getItemDamage()+", Amount: "+components[das].stackSize);
							}
						}

						//Adds Recipe
						if (M.requiresBlastFurnace()) {
							if (CORE.RA.addBlastSmelterRecipe(components, componentsFluid, M.getFluid(fluidAmount), 100, duration, (int) aVoltage)){
								Logger.WARNING("[BAS] Success.");
							}
							else {
								Logger.WARNING("[BAS] Failed.");
							}
						}
						else {
							if (CORE.RA.addBlastSmelterRecipe(components, componentsFluid, M.getFluid(fluidAmount), 100, duration, (int) aVoltage/2)){
								Logger.WARNING("[BAS] Success.");
							}
							else {
								Logger.WARNING("[BAS] Failed.");
							}
						}
					}
				}
			}
			else {
				Logger.WARNING("[BAS] doTest: "+doTest+" | tMaterial != null: "+(tMaterial != null));
			}
		}
	}
}
