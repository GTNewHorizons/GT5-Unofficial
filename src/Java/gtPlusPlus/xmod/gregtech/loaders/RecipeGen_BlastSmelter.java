package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.item.ItemStack;

public class RecipeGen_BlastSmelter {

	public static void generateRecipes(){

		Materials[] GregMaterials = Materials.values();

		for (Materials M : GregMaterials){

			if (M.equals(Materials.Iridium) || M.equals(Materials.Osmium) || M.equals(Materials.Osmiridium)
					/*|| !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)
					 || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)
					 || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)
					 || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium) || !M.equals(Materials.Osmiridium)*/
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
					Utils.LOG_INFO("Adding an Alloy Blast Smelter Recipe for "+M+". Gives "+fluidAmount+"L of molten metal.");
					Utils.LOG_INFO("tMaterial.length: "+tMaterial.length+".");
					for (int das=0;das<tItemStackTest.length;das++){
						if (tItemStackTest[das] != null)
							Utils.LOG_INFO("tMaterial["+das+"]: "+tItemStackTest[das].getDisplayName()+" Meta: "+tItemStackTest[das].getItemDamage()+", Amount: "+tItemStackTest[das].stackSize);
					}

					//Generate Recipes for all singular materials that can be made molten.
					if (M.mBlastFurnaceRequired) {
						//doTest = CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration, 240);
					}
					else {
						//doTest = CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration/2, 120);					
					}

					if (doTest){
						//Reset the Variables for compounds if last recipe was a success.
						inputStackCount=0;
						counter=0;


						int mMaterialListSize=0;
						for (MaterialStack ternkfsdf:M.mMaterialList){
							mMaterialListSize++;
						}
						Utils.LOG_INFO("Size: "+mMaterialListSize);
						//If this Material has some kind of compound list, proceed
						if (mMaterialListSize > 1){
							MaterialStack[] tempStack = new MaterialStack[mMaterialListSize];
							circuitGT = ItemUtils.getGregtechCircuit(mMaterialListSize);
							//Just double checking
							if (tempStack.length > 1){

								//Builds me a MaterialStack[] from the MaterialList of M.
								int ooo=0;
								for (MaterialStack xMaterial : M.mMaterialList){
									Utils.LOG_INFO("FOUND: "+xMaterial.mMaterial);
									Utils.LOG_INFO("ADDING: "+xMaterial.mMaterial);
									tempStack[ooo] = M.mMaterialList.get(ooo);
									ooo++;
								}

								//Builds me an ItemStack[] of the materials. - Without a circuit - this gets a good count for the 144L fluid multiplier
								components = new ItemStack[tempStack.length];
								for (MaterialStack aOutputPart : tempStack){
									if (aOutputPart != null){
										Utils.LOG_INFO("Finding dust: "+aOutputPart.mMaterial);
										ItemStack rStack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust"+aOutputPart.mMaterial, (int) aOutputPart.mAmount);
										if (rStack != null){
											Utils.LOG_INFO("Found dust: "+aOutputPart.mMaterial);
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

								//Add a shapeless recipe for each dust this way - Compat mode.
								ItemStack outputStack = tStack;
								outputStack.stackSize = mMaterialListSize;
								RecipeUtils.buildShapelessRecipe(outputStack, components);



								//Set Fluid output
								fluidAmount = 144*inputStackCount;


								Utils.LOG_INFO("Adding an Alloy Blast Smelter Recipe for "+M+" using it's compound dusts. This material has "+ inputStackCount+" parts. Gives "+fluidAmount+"L of molten metal.");
								Utils.LOG_INFO("tMaterial.length: "+components.length+".");
								for (int das=0;das<components.length;das++){
									if (components[das] != null)
										Utils.LOG_INFO("tMaterial["+das+"]: "+components[das].getDisplayName()+" Meta: "+components[das].getItemDamage()+", Amount: "+components[das].stackSize);
								}
								if (M.mBlastFurnaceRequired) {
									//CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 500);							
								}
								else {
									//CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 240);							
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
			gtPlusPlus.core.material.MaterialStack[] tMaterial;
			short counter=0;
			int inputStackCount=0;
			int fluidAmount=0;
			boolean doTest = true;
			tMaterial = M.getComposites();	

			//This Bad boy here is what dictates unique recipes. Fuck life, right?
			ItemStack circuitGT = ItemUtils.getGregtechCircuit(0);


			//Set a duration
			int duration = 0;
			if (M.getMeltingPoint_K() > 150){
				duration = (int) Math.max(M.getMass() / 50L, 1L) * M.getMeltingPoint_K();
			}
			else {
				duration = (int) Math.max(M.getMass() / 50L, 1L) * 150;	
			}


			//Make a simple one Material Materialstack[] and log it for validity.										
			circuitGT = ItemUtils.getGregtechCircuit(1);
			ItemStack[] tItemStackTest = new ItemStack[]{/*circuitGT,*/ tStack};
			inputStackCount = 1;
			fluidAmount = 144*inputStackCount;
			Utils.LOG_INFO("Adding an Alloy Blast Smelter Recipe for "+M.getLocalizedName()+". Gives "+fluidAmount+"L of molten metal.");
			for (int das=0;das<tItemStackTest.length;das++){
				if (tItemStackTest[das] != null)
					Utils.LOG_INFO("tMaterial["+das+"]: "+tItemStackTest[das].getDisplayName()+" Meta: "+tItemStackTest[das].getItemDamage()+", Amount: "+tItemStackTest[das].stackSize);
			}

			//Generate Recipes for all singular materials that can be made molten.
			if (M.requiresBlastFurnace()) {
				CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration, 240);
			}
			else {
				CORE.RA.addBlastSmelterRecipe(tItemStackTest, M.getMolten(fluidAmount), 100, duration/2, 120);					
			}

			if (tMaterial != null){
				//Reset the Variables for compounds if last recipe was a success.
				inputStackCount=0;
				counter=0;


				int mMaterialListSize=0;
				for (gtPlusPlus.core.material.MaterialStack ternkfsdf:M.getComposites()){
					mMaterialListSize++;
				}
				Utils.LOG_INFO("Size: "+mMaterialListSize);
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
								Utils.LOG_INFO("FOUND: "+xMaterial.getStackMaterial().getLocalizedName());
								Utils.LOG_INFO("ADDING: "+xMaterial.getStackMaterial().getLocalizedName());
								tempStack[ooo] = xMaterial;
							}								
							ooo++;
						}

						//Builds me an ItemStack[] of the materials. - Without a circuit - this gets a good count for the 144L fluid multiplier
						components = new ItemStack[9];
						inputStackCount=0;
						for (int irc=0;irc<M.getComposites().length;irc++){
							if (M.getComposites()[irc] != null){
								int r = (int) M.vSmallestRatio[irc];
								inputStackCount = inputStackCount+r;
								components[irc] = M.getComposites()[irc].getDustStack(r);
							}						
						}

						//Set Fluid output
						fluidAmount = 144*inputStackCount;


						Utils.LOG_INFO("Adding an Alloy Blast Smelter Recipe for "+M.getLocalizedName()+" using it's compound dusts. This material has "+ inputStackCount+" parts. Gives "+fluidAmount+"L of molten metal.");
						Utils.LOG_INFO("tMaterial.length: "+components.length+".");
						for (int das=0;das<components.length;das++){
							if (components[das] != null)
								Utils.LOG_INFO("tMaterial["+das+"]: "+components[das].getDisplayName()+" Meta: "+components[das].getItemDamage()+", Amount: "+components[das].stackSize);
						}
						if (M.requiresBlastFurnace()) {
							CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 500);							
						}
						else {
							CORE.RA.addBlastSmelterRecipe(components, M.getMolten(fluidAmount), 100, duration, 240);							
						}
					}
				}
			}
			else {
				Utils.LOG_INFO("doTest: "+doTest+" | tMaterial != null: "+(tMaterial != null));
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
