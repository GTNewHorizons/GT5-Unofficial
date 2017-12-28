package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
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
			final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 60 : 15;
			final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);
			Material bonusA; //Ni
			Material bonusB; //Tin
			
			if (material.getComposites().get(1) != null){
				bonusA = material.getComposites().get(1).getStackMaterial();
			}
			else {
				return ;
			}
			if (material.getComposites().get(2) != null){
				bonusB = material.getComposites().get(2).getStackMaterial();
			}
			else if (material.getComposites().get(1) != null){
				bonusB = material.getComposites().get(1).getStackMaterial();
			}
			else {
				return ;
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
			GT_Values.RA.addPulveriserRecipe(material.getOre(1), new ItemStack[]{material.getCrushed(2)}, new int[]{10000}, 20*20, 2);
			//Macerate Crushed to Impure Dust
			GT_Values.RA.addPulveriserRecipe(material.getCrushed(1), new ItemStack[]{material.getDustImpure(1), bonusA.getDust(1)}, new int[]{10000, 1000}, 20*20, 2);
			//Macerate Washed to Purified Dust
			GT_Values.RA.addPulveriserRecipe(material.getCrushedPurified(1), new ItemStack[]{material.getDustPurified(1), bonusA.getDust(1)}, new int[]{10000, 1000}, 20*20, 2);
			//Macerate Centrifuged to Pure Dust
			GT_Values.RA.addPulveriserRecipe(material.getCrushedCentrifuged(1), new ItemStack[]{material.getDust(1), bonusA.getDust(1)}, new int[]{10000, 1000}, 20*20, 2);

			/**
			 * Wash
			 */		
			//Wash into Purified Crushed
			GT_Values.RA.addOreWasherRecipe(material.getCrushed(1), material.getCrushedPurified(1), bonusA.getTinyDust(1), dustStone, FluidUtils.getWater(1000), 25*20, 16);

			/**
			 * Thermal Centrifuge
			 */			
			//Crushed ore to Centrifuged Ore
			GT_Values.RA.addThermalCentrifugeRecipe(material.getCrushed(1), material.getCrushedCentrifuged(1), bonusB.getTinyDust(1), dustStone, 25*20, 24);
			//Washed ore to Centrifuged Ore
			GT_Values.RA.addThermalCentrifugeRecipe(material.getCrushedPurified(1), material.getCrushedCentrifuged(1), bonusA.getTinyDust(1), dustStone, 25*20, 24);

			/**
			 * Forge Hammer
			 */			
			GT_Values.RA.addForgeHammerRecipe(material.getCrushedCentrifuged(1), material.getDust(1), 10, 16);
			GT_Values.RA.addForgeHammerRecipe(material.getCrushedPurified(1), material.getDustPurified(1), 10, 16);
			GT_Values.RA.addForgeHammerRecipe(material.getOre(1), material.getCrushed(1), 10, 16);
			
			/**
			 * Centrifuge
			 */
			//Purified Dust to Clean
			GT_Values.RA.addCentrifugeRecipe(
					material.getDustPurified(1), null,
					null, //In Fluid
					null, //Out Fluid
					material.getDust(1), bonusA.getTinyDust(1),null, 
					null, null,null, 
					new int[]{10000, 10000}, //Chances
					5*20, //Eu
					5); //Time
			
			
			/**
			 * Electrolyzer
			 */
			
			//Process Dust
			if (componentMap.size() > 0 && componentMap.size() <= 6){
				
				ItemStack mInternalOutputs[] = new ItemStack[componentMap.size()];
				int mChances[] = new int[componentMap.size()];
				int mCellCount = 0;
				
				int mCounter = 0;
				for (Pair<Integer, Material> f : componentMap){
					if (f.getValue().getState() != MaterialState.SOLID){
						mInternalOutputs[mCounter++] = f.getValue().getCell(f.getKey());
						mCellCount += f.getKey();
					}
					else {
						mInternalOutputs[mCounter++] = f.getValue().getDust(f.getKey());
					}
				}				
				
				//Build Output Array
				for (int g=0;g<mInternalOutputs.length;g++){
					mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
				}
				
				ItemStack emptyCell = null;
				if (mCellCount > 0){
					emptyCell = ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", mCellCount);
				}
				
				GT_Values.RA.addElectrolyzerRecipe(
						material.getDust(material.smallestStackSizeWhenProcessing),
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
						240);
			}
			else if (componentMap.size() > 6){
				Logger.MATERIALS("[Issue] "+material.getLocalizedName()+" is composed of over 6 materials, so a recipe for processing cannot be generated.");
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




}
