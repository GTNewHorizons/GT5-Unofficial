package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

public class RecipeGen_Fluids extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_Fluids(final Material M){
		this(M, false);
	}
	
	public RecipeGen_Fluids(final Material M, final boolean dO){
		this.toGenerate = M;
		this.disableOptional = dO;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate, this.disableOptional);
	}

	private void generateRecipes(final Material material, final boolean dO){

		//Melting Shapes to fluid
		if (!material.getFluid(1).getUnlocalizedName().toLowerCase().contains("plasma")){
			
			if (!material.requiresBlastFurnace()) {

				//Ingot
				if (GT_Values.RA.addFluidExtractionRecipe(material.getIngot(1), //Input
						null, //Input 2
						material.getFluid(144), //Fluid Output
						0, //Chance
						1*20, //Duration
						16 //Eu Tick
						)){
					Logger.WARNING("144l fluid extractor from 1 ingot Recipe: "+material.getLocalizedName()+" - Success");
				}
				else {
					Logger.WARNING("144l fluid extractor from 1 ingot Recipe: "+material.getLocalizedName()+" - Failed");
				}

				//Plate
				if (GT_Values.RA.addFluidExtractionRecipe(material.getPlate(1), //Input
						null, //Input 2
						material.getFluid(144), //Fluid Output
						0, //Chance
						1*20, //Duration
						16 //Eu Tick
						)){
					Logger.WARNING("144l fluid extractor from 1 plate Recipe: "+material.getLocalizedName()+" - Success");
				}
				else {
					Logger.WARNING("144l fluid extractor from 1 plate Recipe: "+material.getLocalizedName()+" - Failed");
				}

				//Double Plate
				if (GT_Values.RA.addFluidExtractionRecipe(material.getPlateDouble(1), //Input
						null, //Input 2
						material.getFluid(288), //Fluid Output
						0, //Chance
						1*20, //Duration
						16 //Eu Tick
						)){
					Logger.WARNING("144l fluid extractor from 1 double plate Recipe: "+material.getLocalizedName()+" - Success");
				}
				else {
					Logger.WARNING("144l fluid extractor from 1 double plate Recipe: "+material.getLocalizedName()+" - Failed");
				}

				//Nugget
				if (GT_Values.RA.addFluidExtractionRecipe(material.getNugget(1), //Input
						null, //Input 2
						material.getFluid(16), //Fluid Output
						0, //Chance
						16, //Duration
						8 //Eu Tick
						)){
					Logger.WARNING("16l fluid extractor from 1 nugget Recipe: "+material.getLocalizedName()+" - Success");
				}
				else {
					Logger.WARNING("16l fluid extractor from 1 nugget Recipe: "+material.getLocalizedName()+" - Failed");
				}

				//Block
				if (GT_Values.RA.addFluidExtractionRecipe(material.getBlock(1), //Input
						null, //Input 2
						material.getFluid(144*9), //Fluid Output
						0, //Chance
						288, //Duration
						16 //Eu Tick
						)){
					Logger.WARNING((144*9)+"l fluid extractor from 1 block Recipe: "+material.getLocalizedName()+" - Success");
				}
				else {
					Logger.WARNING((144*9)+"l fluid extractor from 1 block Recipe: "+material.getLocalizedName()+" - Failed");
				}

			}





			//Making Shapes from fluid

			//Ingot
			if (GT_Values.RA.addFluidSolidifierRecipe(
					ItemList.Shape_Mold_Ingot.get(0), //Item Shape
					material.getFluid(144), //Fluid Input
					material.getIngot(1), //output
					32, //Duration
					8 //Eu Tick
					)){
				Logger.WARNING("144l fluid molder for 1 ingot Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("144l fluid molder for 1 ingot Recipe: "+material.getLocalizedName()+" - Failed");
			}

			//Plate
			if (GT_Values.RA.addFluidSolidifierRecipe(
					ItemList.Shape_Mold_Plate.get(0), //Item Shape
					material.getFluid(144), //Fluid Input
					material.getPlate(1), //output
					32, //Duration
					8 //Eu Tick
					)){
				Logger.WARNING("144l fluid molder for 1 plate Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("144l fluid molder for 1 plate Recipe: "+material.getLocalizedName()+" - Failed");
			}

			//Nugget
			if (GT_Values.RA.addFluidSolidifierRecipe(
					ItemList.Shape_Mold_Nugget.get(0), //Item Shape
					material.getFluid(16), //Fluid Input
					material.getNugget(1), //output
					16, //Duration
					4 //Eu Tick
					)){
				Logger.WARNING("16l fluid molder for 1 nugget Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("16l fluid molder for 1 nugget Recipe: "+material.getLocalizedName()+" - Failed");
			}

			//Gears
			if (GT_Values.RA.addFluidSolidifierRecipe(
					ItemList.Shape_Mold_Gear.get(0), //Item Shape
					material.getFluid(576), //Fluid Input
					material.getGear(1), //output
					128, //Duration
					8 //Eu Tick
					)){
				Logger.WARNING("576l fluid molder for 1 gear Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING("576l fluid molder for 1 gear Recipe: "+material.getLocalizedName()+" - Failed");
			}

			//Blocks
			if (GT_Values.RA.addFluidSolidifierRecipe(
					ItemList.Shape_Mold_Block.get(0), //Item Shape
					material.getFluid(144*9), //Fluid Input
					material.getBlock(1), //output
					288, //Duration
					16 //Eu Tick
					)){
				Logger.WARNING((144*9)+"l fluid molder from 1 block Recipe: "+material.getLocalizedName()+" - Success");
			}
			else {
				Logger.WARNING((144*9)+"l fluid molder from 1 block Recipe: "+material.getLocalizedName()+" - Failed");
			}
		}
	}
}

