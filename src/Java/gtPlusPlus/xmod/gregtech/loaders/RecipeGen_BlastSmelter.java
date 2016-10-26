package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class RecipeGen_BlastSmelter {

	public static void generateRecipes(){

		Materials[] GregMaterials = Materials.values();

		for (Materials M : GregMaterials){

			if (M.equals(Materials.Iridium) || M.equals(Materials.Osmium) || M.equals(Materials.Osmiridium)
					|| !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)
					|| !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)
					|| !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)
					|| !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)
					){


				//Add a Blast Smelting Recipe, Let's go!			
				ItemStack tStack;
				if ((null != (tStack = GT_OreDictUnificator.get(OrePrefixes.ingot, M.mSmeltInto, 1L))) && (!M.contains(SubTag.NO_SMELTING) && (M.contains(SubTag.METAL)))) {

					//Prepare some Variables
					ItemStack[] components;
					MaterialStack[] tMaterial;
					short counter=0;
					int inputStackCount=0;
					int fluidAmount=0;
					boolean doTest = true;

					//This Bad boy here is what dictates unique recipes. Fuck life, right?
					ItemStack circuitGT = ItemUtils.getGregtechCircuit(0);


					//Set a duration
					int duration = 0;
					if (M.mBlastFurnaceTemp > 150){
						duration = (int) Math.max(M.getMass() / 50L, 1L) * M.mBlastFurnaceTemp;
					}
					else {
						duration = (int) Math.max(M.getMass() / 50L, 1L) * 150;	
					}


					//Make a simple one Material Materialstack[] and log it for validity.
					tMaterial = new MaterialStack[]{new MaterialStack(M, 1)};	
					circuitGT = ItemUtils.getGregtechCircuit(1);
					ItemStack[] tItemStackTest = new ItemStack[]{circuitGT, ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust"+M, 1)};
					inputStackCount = 1;
					fluidAmount = 144*inputStackCount;
					Utils.LOG_WARNING("Adding an Alloy Blast Smelter Recipe for "+M+". Gives "+fluidAmount+"L of molten metal.");
					Utils.LOG_WARNING("tMaterial.length: "+tMaterial.length+".");
					for (int das=0;das<tItemStackTest.length;das++){
						if (tItemStackTest[das] != null)
							Utils.LOG_WARNING("tMaterial["+das+"]: "+tItemStackTest[das].getDisplayName()+" Meta: "+tItemStackTest[das].getItemDamage()+", Amount: "+tItemStackTest[das].stackSize);
					}

					//Generate Recipes for all singular materials that can be made molten.
					if (M.mBlastFurnaceRequired) {
						doTest = CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration, 240);
					}
					else {
						doTest = CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration/2, 120);					
					}

					if (doTest){
						//Reset the Variables for compounds if last recipe was a success.
						inputStackCount=0;
						counter=0;


						int mMaterialListSize=0;
						for (MaterialStack ternkfsdf:M.mMaterialList){
							mMaterialListSize++;
						}
						Utils.LOG_WARNING("Size: "+mMaterialListSize);
						//If this Material has some kind of compound list, proceed
						if (mMaterialListSize > 1){
							MaterialStack[] tempStack = new MaterialStack[mMaterialListSize];
							circuitGT = ItemUtils.getGregtechCircuit(mMaterialListSize);
							//Just double checking
							if (tempStack.length > 1){

								//Builds me a MaterialStack[] from the MaterialList of M.
								int ooo=0;
								for (MaterialStack xMaterial : M.mMaterialList){
									Utils.LOG_WARNING("FOUND: "+xMaterial.mMaterial);
									Utils.LOG_WARNING("ADDING: "+xMaterial.mMaterial);
									tempStack[ooo] = M.mMaterialList.get(ooo);
									ooo++;
								}

								//Builds me an ItemStack[] of the materials. - Without a circuit - this gets a good count for the 144L fluid multiplier
								components = new ItemStack[tempStack.length];
								for (MaterialStack aOutputPart : tempStack){
									if (aOutputPart != null){
										Utils.LOG_WARNING("Finding dust: "+aOutputPart.mMaterial);
										ItemStack rStack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust"+aOutputPart.mMaterial, (int) aOutputPart.mAmount);
										if (rStack != null){
											Utils.LOG_WARNING("Found dust: "+aOutputPart.mMaterial);
											components[counter] = rStack;
											inputStackCount = inputStackCount+rStack.stackSize;
										}
									}							
									counter++;			
								}


								if (mMaterialListSize > 0 && mMaterialListSize < 9){
									ItemStack[] components_NoCircuit = components;
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
								}

								/*//Add a shapeless recipe for each dust this way - Compat mode.
								ItemStack outputStack = tStack;
								outputStack.stackSize = mMaterialListSize;
								RecipeUtils.buildShapelessRecipe(outputStack, components);*/



								//Set Fluid output
								fluidAmount = 144*inputStackCount;


								Utils.LOG_WARNING("Adding an Alloy Blast Smelter Recipe for "+M+" using it's compound dusts. This material has "+ inputStackCount+" parts. Gives "+fluidAmount+"L of molten metal.");
								Utils.LOG_WARNING("tMaterial.length: "+components.length+".");
								for (int das=0;das<components.length;das++){
									if (components[das] != null)
										Utils.LOG_WARNING("tMaterial["+das+"]: "+components[das].getDisplayName()+" Meta: "+components[das].getItemDamage()+", Amount: "+components[das].stackSize);
								}
								if (M.mBlastFurnaceRequired) {
									CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 500);							
								}
								else {
									CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 240);							
								}
							}
						}
					}
				}
			}
		}
	}


	public static void generateARecipe(Material M){

		//Add a Blast Smelting Recipe, Let's go!			
		ItemStack tStack;
		if (null != (tStack = M.getDust(1))) {

			//Prepare some Variables
			ItemStack[] components;
			ArrayList<gtPlusPlus.core.material.MaterialStack> tMaterial;
			short counter=0;
			int inputStackCount=0;
			int fluidAmount=0;
			boolean doTest = true;
			tMaterial = M.getComposites();	

			//This Bad boy here is what dictates unique recipes. Fuck life, right?
			ItemStack circuitGT = ItemUtils.getGregtechCircuit(0);


			//Set a duration
			int duration = 0;
			if (M.getMeltingPointK() > 150){
				duration = (int) Math.max(M.getMass() / 50L, 1L) * M.getMeltingPointK();
			}
			else {
				duration = (int) Math.max(M.getMass() / 50L, 1L) * 150;	
			}


			int mMaterialListSize=0;
			if (M.getComposites() != null){
				for (gtPlusPlus.core.material.MaterialStack ternkfsdf : M.getComposites()){
					if (ternkfsdf != null)
					mMaterialListSize++;
				}
			}
			else {
				mMaterialListSize = 1;			
			}			

			Utils.LOG_WARNING("Size: "+mMaterialListSize);


			//Make a simple one Material Materialstack[] and log it for validity.										
			circuitGT = ItemUtils.getGregtechCircuit(1);
			ItemStack[] tItemStackTest = new ItemStack[]{circuitGT, tStack};
			inputStackCount = 1;
			fluidAmount = 144*inputStackCount;
			Utils.LOG_WARNING("Adding an Alloy Blast Smelter Recipe for "+M.getLocalizedName()+". Gives "+fluidAmount+"L of molten metal.");
			for (int das=0;das<tItemStackTest.length;das++){
				if (tItemStackTest[das] != null)
					Utils.LOG_WARNING("tMaterial["+das+"]: "+tItemStackTest[das].getDisplayName()+" Meta: "+tItemStackTest[das].getItemDamage()+", Amount: "+tItemStackTest[das].stackSize);
			}

			//Generate Recipes for all singular materials that can be made molten.
			if (mMaterialListSize >= 1){
				if (M.requiresBlastFurnace()) {
					if (CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getFluid(fluidAmount), 100, duration, 240)){
						Utils.LOG_WARNING("Success.");
						if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0), M.getFluid(144), M.getIngot(1), duration, 120)){
							Utils.LOG_INFO("Success, Also added a Fluid solidifier recipe.");
							if (GT_Values.RA.addFluidExtractionRecipe(M.getIngot(1), null, M.getFluid(144), 100, duration, 120)){
								Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
							}
							if (GT_Values.RA.addFluidExtractionRecipe(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("nugget"+M.getUnlocalizedName(), 1), null, M.getFluid(16), 100, duration/9, 120)){
								Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
							}
							if (GT_Values.RA.addFluidExtractionRecipe(M.getSmallDust(1), null, M.getFluid(36), 100, duration/4, 120)){
								Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
							}
							if (GT_Values.RA.addFluidExtractionRecipe(M.getTinyDust(1), null, M.getFluid(16), 100, duration/9, 120)){
								Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
							}
						}
					}
				}
				else {
					Utils.LOG_WARNING("Failed.");
				}
			}
			else {
				if (CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getFluid(fluidAmount), 100, duration/2, 120)){
					Utils.LOG_WARNING("Success.");
					if (GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0), M.getFluid(144), M.getIngot(1), duration/2, 60)){
						Utils.LOG_INFO("Success, Also added a Fluid solidifier recipe.");
						if (GT_Values.RA.addFluidExtractionRecipe(M.getIngot(1), null, M.getFluid(144), 100, duration/2, 60)){
							Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
						}
						ItemStack tempitem = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("nugget"+M.getUnlocalizedName(), 1);
						if (tempitem != null){
							if (GT_Values.RA.addFluidExtractionRecipe(tempitem, null, M.getFluid(16), 100, duration/2/9, 60)){
								Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
							}
						}						
						if (GT_Values.RA.addFluidExtractionRecipe(M.getSmallDust(1), null, M.getFluid(36), 100, duration/2/4, 60)){
							Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
						}
						if (GT_Values.RA.addFluidExtractionRecipe(M.getTinyDust(1), null, M.getFluid(16), 100, duration/2/9, 60)){
							Utils.LOG_INFO("Success, Also added a Fluid Extractor recipe.");
						}
					}
				}	
				else {
					Utils.LOG_WARNING("Failed.");
				}				
			}		

			if (tMaterial != null){
				//Reset the Variables for compounds if last recipe was a success.
				inputStackCount=0;
				counter=0;



				//If this Material has some kind of compound list, proceed
				if (mMaterialListSize > 1){
					gtPlusPlus.core.material.MaterialStack[] tempStack = new gtPlusPlus.core.material.MaterialStack[mMaterialListSize];
					circuitGT = ItemUtils.getGregtechCircuit(mMaterialListSize);
					//Just double checking
					if (tempStack.length > 1){

						//Builds me a MaterialStack[] from the MaterialList of M.
						int ooo=0;
						for (gtPlusPlus.core.material.MaterialStack xMaterial : M.getComposites()){
							if (xMaterial != null){
								if (xMaterial.getStackMaterial() != null){
									Utils.LOG_WARNING("FOUND: "+xMaterial.getStackMaterial().getLocalizedName());
									Utils.LOG_WARNING("ADDING: "+xMaterial.getStackMaterial().getLocalizedName());									
								}								
								tempStack[ooo] = xMaterial;								
							}								
							ooo++;
						}

						//Builds me an ItemStack[] of the materials. - Without a circuit - this gets a good count for the 144L fluid multiplier
						components = new ItemStack[9];
						inputStackCount=0;
						for (int irc=0;irc<M.getComposites().size();irc++){
							if (M.getComposites().get(irc) != null){
								int r = (int) M.vSmallestRatio[irc];
								inputStackCount = inputStackCount+r;
								components[irc] = M.getComposites().get(irc).getDustStack(r);
							}						
						}


						//Adds a circuit
						if (mMaterialListSize < 9 && mMaterialListSize != 0){
							ItemStack[] components_NoCircuit = components;
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
							Utils.LOG_INFO("Should have added a circuit. mMaterialListSize: "+mMaterialListSize+" | circuit: "+components[0].getDisplayName());
						}
						else {
							Utils.LOG_INFO("Did not add a circuit. mMaterialListSize: "+mMaterialListSize);
						}

						//Set Fluid output
						fluidAmount = 144*inputStackCount;

						Utils.LOG_INFO("Adding an Alloy Blast Smelter Recipe for "+M.getLocalizedName()+" using it's compound dusts. This material has "+ inputStackCount+" parts. Gives "+fluidAmount+"L of molten metal.");
						Utils.LOG_INFO("tMaterial.length: "+components.length+".");
						for (int das=0;das<components.length;das++){
							if (components[das] != null)
								Utils.LOG_INFO("tMaterial["+das+"]: "+components[das].getDisplayName()+" Meta: "+components[das].getItemDamage()+", Amount: "+components[das].stackSize);
						}

						//Adds Recipe
						if (M.requiresBlastFurnace()) {
							if (CORE.RA.addBlastSmelterRecipe(components, M.getFluid(fluidAmount), 100, duration, 500)){
								Utils.LOG_WARNING("Success.");
							}
							else {
								Utils.LOG_WARNING("Failed.");
							}
						}
						else {
							if (CORE.RA.addBlastSmelterRecipe(components, M.getFluid(fluidAmount), 100, duration, 240)){
								Utils.LOG_WARNING("Success.");
							}	
							else {
								Utils.LOG_WARNING("Failed.");
							}						
						}
					}
				}
			}
			else {
				Utils.LOG_WARNING("doTest: "+doTest+" | tMaterial != null: "+(tMaterial != null));
			}
		}

	}


	private ItemStack[] getItemStackFromMaterialStack(MaterialStack[] aInput){
		ItemStack[] components = new ItemStack[aInput.length];
		short counter=0;
		for (MaterialStack aOutputPart : aInput){
			if (aOutputPart != null && components[counter] != null){
				ItemStack aStack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust"+aOutputPart.mMaterial, (int) aOutputPart.mAmount);
				components[counter] = aStack;
			}			
			counter++;			
		}
		return components;
	}


}
